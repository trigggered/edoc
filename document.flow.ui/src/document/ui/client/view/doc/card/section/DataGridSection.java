/**
 * 
 */
package document.ui.client.view.doc.card.section;

import java.util.logging.Logger;

import mdb.core.shared.transport.IRequestData;
import mdb.core.shared.transport.IRequestData.ExecuteType;
import mdb.core.shared.transport.RequestEntity;
import mdb.core.ui.client.communication.IRemoteDataSave;
import mdb.core.ui.client.events.ICallbackEvent;
import mdb.core.ui.client.events.IDataEditHandler;
import mdb.core.ui.client.view.data.grid.GridView;
import mdb.core.ui.client.view.dialogs.SelectDialog;
import mdb.core.ui.client.view.dialogs.edit.EditDialog;
import mdb.core.ui.client.view.dialogs.select.MultiStepSelectDialog;
import mdb.core.ui.client.view.utils.DateTimeHelper;

import com.smartgwt.client.data.Criteria;
import com.smartgwt.client.data.Record;
import com.smartgwt.client.util.BooleanCallback;
import com.smartgwt.client.widgets.grid.ListGridRecord;

import document.ui.client.commons.checker.CheckCanApproval;
import document.ui.client.commons.checker.CheckDocumentUserRight;
import document.ui.client.resources.locales.Captions;
import document.ui.client.resources.locales.Images;
import document.ui.client.view.dialogs.EmpSelectDlg;
import document.ui.client.view.doc.card.DocumentActions;
import document.ui.client.view.doc.card.DocumentCard;
import document.ui.client.view.doc.card.menu.MenuAttachment;
import document.ui.shared.MdbEntityConst;

/**
 * @author azhuk
 * Creation date: Mar 20, 2014
 *
 */
public class DataGridSection extends GridView implements IRemoteDataSave{
	private static final Logger _logger = Logger
			.getLogger(DataGridSection.class.getName());
	
	
	private int _selectedListEntityId;  
	private DocumentCard _mainDoc;
	private EDocumentDataSection _docAdditionalDataType;	
	private boolean _currentUserHasApprovRight = false;
	
	public DataGridSection (EDocumentDataSection docAdditionalDataType,  DocumentCard doc) {
		_docAdditionalDataType = docAdditionalDataType;
		switch(_docAdditionalDataType) {
		case Approval:
			setMainEntityId(MdbEntityConst.ACCEPTING_EMP);
			setCaption( Captions.APPROVALS);
			setImgCaption(Images.ACCEPTING_EMP );
			_selectedListEntityId = MdbEntityConst.EMP_LIST;
			addInsertEvent(_showInsertEmpViewHandler);
			addEditEvent(_showEditAcceptingEmpViewHandler);
			 break;
		case Recipients:
			setMainEntityId(MdbEntityConst.RECIPIENTS_EMP);			
			setCaption(Captions.DOC_RECIPIENTS);
			setImgCaption(Images.DOC_RECIPIENTS );
			setIsMultiSelect(true);
			getParams().add("RECIPIENTS_TYPE", "0");
			addInsertEvent(_showInsertGrRecipientsViewHandler);
			break;
		case 	Executors:
			setMainEntityId(MdbEntityConst.RECIPIENTS_EMP);
			setCaption(Captions.DOC_EMP_EXECUTERS);
			setImgCaption(Images.DOC_EMP_EXECUTERS );
			setIsMultiSelect(true);			
			getParams().add("RECIPIENTS_TYPE", "1");
			addInsertEvent(_showInsertGrRecipientsViewHandler);
			break;
		case DocRelations:
			setMainEntityId(MdbEntityConst.DOC_RELATIONS);
			setCaption(Captions.DOC_RELATIONS );
			addInsertEvent(_showInsertDocViewHandler);
			addEditEvent(DocumentCard.getShowEditViewHandler());
			getParams().add("ID_REL_TYPE", "0");
			_selectedListEntityId = MdbEntityConst.DOC_LIST;
			break;
		case Attachments:
			setCreateMenuNavigator(false);
			setMainEntityId(MdbEntityConst.DOC_ATTACH_LIST);
			setCaption(Captions.ATACHMENTS );			
			addViewEvent(DocumentActions.getDownloadAttachmentEvent());
			getParams().add("ISLEAF", "1");
			//addEditEvent(_newAttachmentVersion);
			getMenuContainer().bind(new MenuAttachment(this));
			break;
		default:
			break;			
		}		
		_mainDoc = doc;
	}		
	
	
	private  IDataEditHandler _showInsertEmpViewHandler = new IDataEditHandler() {
		
		@Override
		public void onEdit(Record record) {
			
			MultiStepSelectDialog.view(_selectedListEntityId,true, new ICallbackEvent<Record[]>() {
				
				@Override
				public void doWork(Record[] data) {
			
					for (Record rec : data) {
						ListGridRecord newRecord = new ListGridRecord();										
							
						String officerNum = rec.getAttribute("OFFICER_NUM");
						Criteria criteria = new Criteria("OFFICER_NUM", officerNum);
						Record[] filteringRec =  _grid.getDataSource().applyFilter(_grid.getRecords(), criteria);
						if (filteringRec != null && filteringRec.length > 0) {
							return;
						}
						
						newRecord.setAttribute("ID_DOC", _mainDoc.getDocumentId());			
						newRecord.setAttribute("OFFICER_NUM", rec.getAttribute("OFFICER_NUM"));
						newRecord.setAttribute("FULL_NAME", rec.getAttribute("FULL_NAME"));
						 						
						int orderNo = _grid.getRecordList().toArray().length;
							
						newRecord.setAttribute("ORDER_NO", orderNo++ );
						_grid.addData(newRecord);
												
					}					
				}
			});	 	 
		}
	};	
	
	
private  IDataEditHandler _showInsertGrRecipientsViewHandler = new IDataEditHandler() {
		
		@Override
		public void onEdit(Record record) {
			
			EmpSelectDlg.view( MdbEntityConst.AddDocRecipients,true, new ICallbackEvent<Record[]>() {
				
				@Override
				public void doWork(Record[] data) {
			
					for (Record rec : data) {
						ListGridRecord newRecord = new ListGridRecord();										
							
						newRecord.setAttribute("ID_DOC", _mainDoc.getDocumentId());			
						newRecord.setAttribute("VALUE_ID", rec.getAttribute("VALUE_ID"));
						newRecord.setAttribute("ID_GR", rec.getAttribute("ID_GR"));
						newRecord.setAttribute("NAME", rec.getAttribute("NAME"));
						newRecord.setAttribute("RECIPIENTS_TYPE", getParams().paramByName("RECIPIENTS_TYPE").getValue());						 											
						
						_grid.addData(newRecord);																		
					}											
				}
			});	 	 
		}
	};	
	
	private  IDataEditHandler _showEditAcceptingEmpViewHandler = new IDataEditHandler() {		
		@Override
		public void onEdit(final Record record) {			
			
				String selectedApprovUsrId = record.getAttributeAsString("OFFICER_NUM");			
				
				CheckCanApproval check = new CheckCanApproval(); 
				
				check.isCan(getDocumentCard().getDocumentStatus(), getDocumentCard().getDocumentId(),
						Integer.valueOf(selectedApprovUsrId).intValue(), new BooleanCallback() {
					
					@Override
					public void execute(Boolean value) {
						if (value) {
							record.setAttribute("ACCEPT_DTA", DateTimeHelper.getSysdate());
							EditDialog.viewForEdit(getMainDataSource(), _grid, false);
						} else {
							EditDialog.view(getMainDataSource(), _grid);	
						}
						
					}
				});								
				
		}
	};	
	
	
	private  IDataEditHandler _showInsertDocViewHandler = new IDataEditHandler() {
		
		@Override
		public void onEdit(Record record) {
			
			SelectDialog.view(_selectedListEntityId, true, new ICallbackEvent<Record[]>() {
				
				@Override
				public void doWork(Record[] data) {
			
					for (Record rec : data) {
						Record newRecord = new Record();										
							
						newRecord.setAttribute("ID_MAIN_DOC", _mainDoc.getDocumentId());
						newRecord.setAttribute("ID_DOC", _mainDoc.getDocumentId());
						newRecord.setAttribute("ID_CHILD_DOC", rec.getAttribute("ID_DOC"));
						//newRecord.setAttribute("ID_REL_TYPE", 0);
						newRecord.setAttribute("ID_REL_TYPE", getParams().paramByName("ID_REL_TYPE").getValue());
						
						newRecord.setAttribute("DOC_CODE", rec.getAttribute("DOC_CODE"));			
						newRecord.setAttribute("DOC_NAME", rec.getAttribute("NAME"));
						
						_grid.getDataSource().addData(newRecord);
					}
				}
			});	 	 
		}
	};
	
	
	@Override
	public void prepareRequestData() {		
		
		_logger.info("################ Start prepareRequestData id for Grid Section: "+_docAdditionalDataType.toString()+" ######################");
		_logger.info("Put parametr ID_DOC= "+String.valueOf(_mainDoc.getDocumentId()));
		 getDataBinder().getDataProvider().getRequest().setPosition(2);
    	IRequestData entity = getDataBinder().getDataProvider().getRequest().add(new RequestEntity (getMainEntityId()));
    	
    	entity.getParams().putAll(getParams());
    	entity.getParams().add("ID_DOC", String.valueOf(_mainDoc.getDocumentId()));
    	
    	entity.setExecuteType(ExecuteType.GetData);
    	super.prepareRequestData();
    	_logger.info("################# End prepareRequestData  #####################");
	}

	public void visibleButtons(Boolean[] visibles) {
		
	}
	
	@Override
	public void createMenu() {				 	
	}		

	/* (non-Javadoc)
	 * @see mdb.core.ui.client.communication.IRemoteDataSave#isValidate()
	 */
	@Override
	public boolean isValidate() {		
		return true;
	}


	/* (non-Javadoc)
	 * @see mdb.core.ui.client.communication.IRemoteDataSave#prepareSavedData()
	 */
	@Override
	public void prepareSavedData() {
		
	}


	/* (non-Javadoc)
	 * @see mdb.core.ui.client.communication.IRemoteDataSave#putSavedDataToQueue()
	 */
	@Override
	public void putSavedDataToQueue() {
		putRequestToQueue();				
	}


	/* (non-Javadoc)
	 * @see mdb.core.ui.client.communication.IRemoteDataSave#save()
	 */
	@Override
	public void save() {
	}
	
	@Override
	protected void bindMenuDataNavigator() {
		super.bindMenuDataNavigator();		
	}
	
	public DocumentCard getDocumentCard() {
		return _mainDoc;
	}
	

	@Override
	public void callEditEvent() {
		if (_currentUserHasApprovRight && _docAdditionalDataType	== EDocumentDataSection.Approval ) {
	
			_editHandler.onEdit(getSelectedRecord());
		} 
		else if (_docAdditionalDataType	== EDocumentDataSection.Attachments) {
			_editHandler.onEdit(getSelectedRecord());
		}
		else {
			super.callEditEvent();
		}
	}
	
	
	
	
	@Override
	public void setCanEdit(boolean value) {
		super.setCanEdit(value);		
		
		if ( _docAdditionalDataType	== EDocumentDataSection.Approval) {			
			
			CheckDocumentUserRight.isCurrentUserHasApprovRight(getDocumentCard(), new BooleanCallback() {
				
				@Override
				public void execute(Boolean value) {
					_currentUserHasApprovRight  = value;				
				}
			});
		}
						
	}
	
}
