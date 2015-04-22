/**
 * 
 */
package document.ui.client.flow.impl;

import java.util.logging.Logger;

import mdb.core.ui.client.app.AppController;
import mdb.core.ui.client.command.ICommand;
import mdb.core.ui.client.events.IDoubleValuesCallbackEvent;
import mdb.core.ui.client.view.data.IDataView;
import mdb.core.ui.client.view.data.grid.GridView;
import mdb.core.ui.client.view.dialogs.input.InputTextDialog;
import mdb.core.ui.client.view.dialogs.message.Dialogs;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.smartgwt.client.util.BooleanCallback;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.grid.ListGridRecord;

import document.ui.client.commons.ECorrespondentType;
import document.ui.client.commons.EDocStatus;
import document.ui.client.communication.rpc.flow.DocumentFlowService;
import document.ui.client.communication.rpc.flow.DocumentFlowServiceAsync;
import document.ui.client.flow.IFlowProcessing;
import document.ui.client.resources.locales.Captions;
import document.ui.client.tools.SignControlWrapper;
import document.ui.client.view.doc.card.DocumentCard;
import document.ui.client.view.doc.card.section.EDocumentDataSection;

/**
 * @author azhuk
 * Creation date: Aug 7, 2014
 *
 */
public class FlowProccessing implements IFlowProcessing {
	private static final Logger _logger = Logger.getLogger(FlowProccessing.class
			.getName());
	
	
	private static  DocumentFlowServiceAsync _asyncFlowService = GWT.create(DocumentFlowService.class);
	
	private DocumentCard _docCard;
	
	public FlowProccessing ( DocumentCard docCard) {		
		_docCard = docCard;
	}
	
	
	public  EDocStatus getDocumentNextPossibleStatus(ECorrespondentType corrType, EDocStatus currentStatus) {
		
		if ( corrType == ECorrespondentType.INSIDE_PRIKAZ_DOC) {
		
				switch (currentStatus) {
					case Draft :
						return EDocStatus.AtTheApproval;
					case AtTheApproval:
						return EDocStatus.Approval;
					case Approval: 
						return EDocStatus.AtTheSigning;
					case AtTheSigning: 
						return EDocStatus.Signed;
					case Signed:
						return EDocStatus.Valid;						
					default:
						return EDocStatus.Unknown;
					}
				
		} 
		else {
				switch (currentStatus) {
				case Draft :
					return EDocStatus.AtTheApproval;
				case AtTheApproval:
					return EDocStatus.Approval;
				case Approval: 
					return EDocStatus.Valid;							
				default:
					return EDocStatus.Unknown;
			}		
		}
	}
	
	
	public  void sendToTheNextStage( final BooleanCallback callBack) {
		
		EDocStatus currentStatus = _docCard.getDocumentStatus();		
			
		if ( _docCard.isHaseChanges() ) {
			Dialogs.ShowMessage(Captions.NOT_SAVED_DATA_IN_DOC);			
			return;
		}
		
		final EDocStatus nextPossibleStatus = getDocumentNextPossibleStatus(_docCard.getCorrespondentTypeRootCode(), currentStatus);
		
		String askMsg  = null;
		 ICommand<Boolean> command = null;
		
		boolean isPreCheckOk = false;
		boolean isGetInputMsg = true;
		 
		switch ( nextPossibleStatus) {
			case AtTheApproval:
				isGetInputMsg = true;
				askMsg = Captions.SEND_TO_APPROVAL;
				  isPreCheckOk = checkApprovalListExist();
				  if ( !isPreCheckOk  ) {
					  Dialogs.ShowMessage(Captions.NOT_CHOUSE_APPR_LIST);				
					  return;
				  }
				  
				  isPreCheckOk = checkAttachmentsExist();
				  if ( !isPreCheckOk  ) {
					  Dialogs.ShowMessage(Captions.NOT_UPLOAD_ATTACH);				
					  return;
				  }
				  
				  
				  
				  command = new ICommand<Boolean>() {					
					@Override
					public void execute(Boolean sender) {						
								if (sender) {
									Dialogs.ShowMessage(Captions.DOC_SENT_TO_APPR);									
									callBack.execute(true);
								}
					}
				};
				break;
			case AtTheSigning:
				isPreCheckOk =checkSignList();
				isGetInputMsg = true;
				if ( !isPreCheckOk  ) {
					Dialogs.ShowMessage(Captions.NOT_CHOUSE_SIGN);					
					return;
				}
				
				
				askMsg = Captions.Q_SENT_DOC_TO_SIGN;
				command = new ICommand<Boolean>() {					
					@Override
					public void execute(Boolean sender) {						
					
						if (sender ) {
							Dialogs.ShowMessage(Captions.DOC_SENT_TO_SIGN);
							callBack.execute(true);
						} 
					}
				};
				break;
			case Signed:
				isPreCheckOk = true;
				isGetInputMsg = false;
				
				askMsg = Captions.Q_SIGN_DOC; 
				
				command = new ICommand<Boolean>() {					
					@Override
					public void execute(Boolean sender) {						
						if (sender ) {
							Dialogs.ShowMessage(Captions.DOC_SIGN);							
							callBack.execute(true);							
						} 
					}
				};				
				
				break;
		default:
			break;
		}		
		
		if (!isPreCheckOk) {
			return;
		}	
		
		
		final ICommand<Boolean> execCommand = command;
		
		if (isGetInputMsg) {
				InputTextDialog textDlg = new InputTextDialog(askMsg, new IDoubleValuesCallbackEvent<Boolean, String>() {			
					@Override
					public void execute(Boolean isOkClick, String resultTextInfo) {
						if (isOkClick ) {
							
							callFlowProcessing ( _docCard.getDocumentId(), resultTextInfo, execCommand);		
							
							
						} else{
							callBack.execute(false);
						}				
					}
				}); 					
				textDlg.view();	
		} else {
			SC.ask(askMsg, new BooleanCallback() {
				
				@Override
				public void execute(Boolean value) {
					if (value) {
						if ( sign() ) {
							callFlowProcessing ( _docCard.getDocumentId(), null, execCommand);						
						}	
					}
					
				}
			});
		}		
	}	
	
	
	/**
	 * @return
	 */
	protected boolean sign() {
		
		
		if ( SignControlWrapper.getSignControl().initialize()  ) 
		{	
			_logger.info("Sign control activated successfully");

			int userId = AppController.getInstance().getCurrentUser().getId();
			
						
			
			
			if ( !SignControlWrapper.getSignControl().CheckLoginUserIdKeyOwner( userId ) ){
				SC.warn(Captions.ERROR,  Captions.USERID_NOT_EQUALS_KEY_ID );
				
				return false;
			}
			
			
			GridView  view =  (GridView) _docCard.getDataSections().get(EDocumentDataSection.Attachments);
			
			for ( final ListGridRecord rec :  view.getListGrid().getRecords() ) {				
				
				final String data =rec.getAttribute("HASH_DATA");
				//String  sign = SignAppletWrapper.sign(data);
				//String  sign = SignXWrapper.sign( data );
				_logger.info("Try sign data:"+data);
				String  sign = SignControlWrapper.getSignControl().sign(data);
				rec.setAttribute("SIGN_DATA", sign);
				rec.setAttribute("SIGN_ID", userId);		
			
				view.getMainDataSource().getDataSource().updateData(rec);						 
			}			
		
			view.getListGrid().redraw();
			//_docCard.saveCard();
			return true;
		} else {
			SC.warn("Ошибка инициализации ключа");
			return false;
		}
	}


	private static void callFlowProcessing ( long docId, String msg, final ICommand<Boolean> execCommand ) {
		_asyncFlowService.sendToTheNextStage(docId, msg , 
				 AppController.getInstance().getCurrentUser().getId(),
				new AsyncCallback<Boolean>() {
			
			@Override
			public void onSuccess(Boolean result) {
				execCommand.execute(true);						
			}
			
			@Override
			public void onFailure(Throwable caught) {
				_logger.severe(caught.getMessage());
				execCommand.execute(false);							
			}
		});
	}
	
	
	public  static void sendToSignFromBA( final int documentId, String infoMsg, int initialId, final ICommand<Boolean> callBack) {
		
		callFlowProcessing (  documentId, infoMsg, callBack );		
	}
	
	public  static void publishDoc( final int documentId, String infoMsg, int initialId, final ICommand<Boolean> callBack) {
		
		_asyncFlowService.sendToTheNextStage(documentId, infoMsg , 
				 AppController.getInstance().getCurrentUser().getId(),
				new AsyncCallback<Boolean>() {
			
			@Override
			public void onSuccess(Boolean result) {
				callBack.execute(true);				
				
					_asyncFlowService.sendPublishedInfoMsg(documentId, new AsyncCallback<Void>() {						
						@Override
						public void onSuccess(Void result) {						
							
						}
						
						@Override
						public void onFailure(Throwable caught) {
							
						}
					});
			}
			
			@Override
			public void onFailure(Throwable caught) {
				_logger.severe(caught.getMessage());
				callBack.execute(false);							
			}
		});
		
		//callFlowProcessing (  documentId, infoMsg, callBack );
		
	}
	
	
	public static void sendRemember(final int documentId) {
		
		_asyncFlowService.sendRemember(documentId, new AsyncCallback<Void>() {
			
			@Override
			public void onSuccess(Void result) {
				SC.say(Captions.SEND_REMEMBER_BY_DOC +documentId);
				
			}
			
			@Override
			public void onFailure(Throwable caught) {
				_logger.severe(caught.getMessage());
			}
		}); 		
	}
	
	/**
	 * @return
	 */
	protected boolean checkSignList() {
		IDataView view = _docCard.getDataSections().get(EDocumentDataSection.MainFields);		
		String signID = view.getSelectedRecord().getAttribute("SIGN_OFFICER_NUM");
	return signID != null && !signID.equalsIgnoreCase(""); 
		
	}


	/**
	 * @return
	 */
	protected boolean checkApprovalListExist() {
		IDataView view = _docCard.getDataSections().get(EDocumentDataSection.Approval);		
		if ( view.getDataSources().containsKey(view.getMainEntityId()) ) {
			return view.getDataSources().get(view.getMainEntityId()).getRecords().length >0;
		}
		
		
		return false;
	}

	
	protected boolean checkAttachmentsExist() {
		IDataView view = _docCard.getDataSections().get(EDocumentDataSection.Attachments);		
		if ( view.getDataSources().containsKey(view.getMainEntityId()) ) {
			return view.getDataSources().get(view.getMainEntityId()).getRecords().length >0;
		}
		
		return false;
	}


	/**
	 * @param parseInt
	 */
	public static void cancelProcess(final long docId, final AsyncCallback<Void> callBack) {
		
		InputTextDialog textDlg = new InputTextDialog("Сообщение об отказе для документа:" +docId, new IDoubleValuesCallbackEvent<Boolean, String>() {			
			@Override
			public void execute(Boolean isOkClick, String resultTextInfo) {
				if (isOkClick ) {
					
					_asyncFlowService.cancelProcess(docId, resultTextInfo,  AppController.getInstance().getCurrentUser().getId(),callBack);																				
					
				} 			
			}
		}); 					
		textDlg.view();		
	}	
	
	
	
}
