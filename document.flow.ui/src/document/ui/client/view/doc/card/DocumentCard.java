package document.ui.client.view.doc.card;

import java.util.HashMap;
import java.util.logging.Logger;

import mdb.core.shared.transport.IRequestData;
import mdb.core.shared.transport.IRequestData.ExecuteType;
import mdb.core.shared.transport.RequestEntity;
import mdb.core.ui.client.app.AppController;
import mdb.core.ui.client.communication.IRemoteDataSave;
import mdb.core.ui.client.communication.impl.GatewayQueue;
import mdb.core.ui.client.data.IDataSource;
import mdb.core.ui.client.events.ICallbackEvent;
import mdb.core.ui.client.events.IDataEditHandler;
import mdb.core.ui.client.view.IView;
import mdb.core.ui.client.view.data.DataView;
import mdb.core.ui.client.view.data.IDataView;
import mdb.core.ui.client.view.dialogs.message.Dialogs;

import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.smartgwt.client.data.Record;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.VisibilityMode;
import com.smartgwt.client.util.BooleanCallback;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.Button;
import com.smartgwt.client.widgets.Dialog;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.Layout;
import com.smartgwt.client.widgets.layout.SectionStack;
import com.smartgwt.client.widgets.layout.SectionStackSection;
import com.smartgwt.client.widgets.layout.VLayout;
import com.smartgwt.client.widgets.tab.Tab;
import com.smartgwt.client.widgets.tab.TabSet;

import document.ui.client.commons.ECorrespondentType;
import document.ui.client.commons.EDocStatus;
import document.ui.client.commons.checker.CheckDocumentExists;
import document.ui.client.commons.checker.CheckDocumentUserRight;
import document.ui.client.communication.files.MultiDocumentAttachUploadWidget;
import document.ui.client.flow.impl.FlowProccessing;
import document.ui.client.resources.locales.Captions;
import document.ui.client.resources.locales.Images;
import document.ui.client.view.doc.card.menu.MenuBAAction;
import document.ui.client.view.doc.card.menu.MenuFile;
import document.ui.client.view.doc.card.section.DataFieldsSection;
import document.ui.client.view.doc.card.section.DataGridSection;
import document.ui.client.view.doc.card.section.EDocumentDataSection;
import document.ui.shared.MdbEntityConst;

public abstract class DocumentCard extends DataView implements IRemoteDataSave {	

	private enum EAfterSaveAction {
		None, 
		Refresh,
		Close
	}
	
	public enum EViewState {
	 New,
	 Edit 
	}
		
	

	private EAfterSaveAction  _afterSaveAction = EAfterSaveAction.Refresh;
	
	//private WaitPopup _waitPopup = new WaitPopup();  
	private static final Logger _logger = Logger.getLogger(DocumentCard.class.getName());	
	private Layout _layMainDocFields;
	protected Layout _layAddDocFields;
	protected FlowProccessing _flowProccess;	
	
	private static IDataEditHandler _showEditViewHandler = new IDataEditHandler() {
		
		@Override
		public void onEdit(Record record) {
			if (record != null) {
					DocumentCard.OpenById(record.getAttribute("ID_DOC"),
							ECorrespondentType.fromString( record.getAttribute("CORR_TYPE"))
							);							
			}
		}
	};	
	
	
	
	protected TabSet _mainTabSet;	
	protected EDocStatus _currentStatus;
	private long _documentId;	
	private ECorrespondentType _correspondentTypeRootCode;

	private DataFieldsSection _docMainFields;
	private EViewState _viewState;
	private final int NEW_DOCID_GEN = MdbEntityConst.NEW_DOCID_GEN;	

	protected  HashMap<EDocumentDataSection, IDataView> _hmDataSections;
	
	private DataGridSection _docAttachmentsList;
	private MultiDocumentAttachUploadWidget _uploadWidget; 
	
	
	protected Button _btnSendToSigne;
	protected Button _btnSendToApprove;
	protected Button _btnClose;
	protected Button _btnSave;
	protected Button _btnSign;
	protected Button _btnCancelSign;
	
	
	public DocumentCard() {
		super(EViewPanelType.VLayout);		
		//ObjectElement o = Document.get().createObjectElement();
		//o.setCId(DEBUG_ID_PREFIX);						
	}
	
	
	public DocumentCard(ECorrespondentType correspondentType) {
		super(EViewPanelType.VLayout);
		_correspondentTypeRootCode = correspondentType;		
	}	
	

	protected DataGridSection createDocDataSection(EDocumentDataSection value) {		
		DataGridSection docData = new DataGridSection(value, this);
		_hmDataSections.put(value, docData);
		Tab tab = new Tab();
		tab.setTitle( docData.getCaption());
		tab.setIcon( docData.getImgCaption());
		
		tab.setPane(docData);
		_mainTabSet.addTab(tab);
		
		docData.setViewContainerID(tab.getID());		
		
		return docData;	
	}
	

	protected void createComponents() {
		_flowProccess = new FlowProccessing(this);
		super.createComponents();		
		
		//setImgCaption("doc/Document 2.png");
		Layout viewPanel = getViewPanel();		
		//viewPanel.addChild();
		Layout mainLayout = new HLayout();
		mainLayout.setHeight("95%");
		_mainTabSet = new TabSet();
		_mainTabSet.setWidth("50%");
		_mainTabSet.setShowResizeBar(true);
		/*Основные параметры*/
		Tab tabDocField = new Tab();
		tabDocField.setTitle(Captions.DOC_MAIN_FLD);
		_hmDataSections = new HashMap<EDocumentDataSection, IDataView>();
		_docMainFields = new DataFieldsSection(this);
		_docMainFields.setHeight100();
		_docMainFields.setWidth100();
		
		_hmDataSections.put(EDocumentDataSection.MainFields, _docMainFields);
		 Layout layFields 	= new HLayout();
		
		_layMainDocFields 	= new HLayout();
		_layAddDocFields 	= new VLayout();
		
		layFields.addMembers(_layMainDocFields, _layAddDocFields);
		
		_layMainDocFields.addMember(_docMainFields);
		tabDocField.setPane(layFields);		
		_mainTabSet.addTab(tabDocField);
				
	
		/*Вложения*/		
		Layout rightLayout = new HLayout();		
		TabSet tabSetRight = new TabSet();
		Tab tabAttachments = new Tab(Captions.ATACHMENTS);
		tabAttachments.setIcon(Images.ATACHMENTS );
		
		tabSetRight.addTab(tabAttachments);
		
		SectionStack stackAttachments = new SectionStack();
		stackAttachments.setVisibilityMode(VisibilityMode.MULTIPLE);		
		stackAttachments.setAnimateSections(true);		
		
		tabAttachments.setPane(stackAttachments);
		SectionStackSection sectionAttachList = new SectionStackSection();
		sectionAttachList.setExpanded(true);
		sectionAttachList.setTitle(Captions.ATACHMENTS_LIST );
		
		
		_docAttachmentsList =  new DataGridSection(EDocumentDataSection.Attachments, this);
		_hmDataSections.put(EDocumentDataSection.Attachments,_docAttachmentsList);
		
		sectionAttachList.addItem(_docAttachmentsList);		
		stackAttachments.addSection(sectionAttachList);			
		
		SectionStackSection sectionAddAttach = new SectionStackSection();
		sectionAddAttach.setExpanded(true);
		sectionAddAttach.setTitle( Captions.ADD_ATACH);
		//sectionAddAttach.setIcon(Images.ADD_ATACH);
		
		
					
		_uploadWidget = new MultiDocumentAttachUploadWidget();					
		
		IDataEditHandler _dataEditHandler = new IDataEditHandler() {			
			@Override
			public void onEdit(Record record) {
				if (record != null) {					

					String idStor = record.getAttributeAsString("ID_STOR");
					_uploadWidget.addFileForUpload(getDocumentId(), idStor);					
				}
			}
		}; 
		
		
		IDataEditHandler _dataInsertHandler = new IDataEditHandler() {			
			@Override
			public void onEdit(Record record) {													
					 _uploadWidget.addFileForUpload(getDocumentId(), null);										
			}
		};
		
		_docAttachmentsList.addInsertEvent(_dataInsertHandler);				
		_docAttachmentsList.addEditEvent(_dataEditHandler);		
		
		sectionAddAttach.addItem(_uploadWidget);		
		stackAttachments.addSection(sectionAddAttach);			
				
		rightLayout.addMember(tabSetRight);		
		
		mainLayout.addMembers(_mainTabSet, rightLayout);
		Layout buttomsL =  createBottomLayout();
		viewPanel.addMembers(mainLayout, buttomsL);				
		createDocAddData();
		createDocumentParamsTab();		
	}
	
	protected abstract void createDocumentParamsTab();	
	
	protected abstract DataFieldsSection createDocAddData();
	
	protected DocumentCard getSelf() {
		return this;
	}
	
	protected Layout createBottomLayout() {
		
		 Layout btnlayout = new HLayout();
	      
		 btnlayout.setAlign(Alignment.RIGHT);
		 btnlayout.setMargin(10);
		 btnlayout.setMembersMargin(10);	      
		 
		  _btnSave = new Button();		  
		  _btnSave.setTitle(Captions.SAVE);		  
	      _btnSave.addClickHandler(new com.smartgwt.client.widgets.events.ClickHandler() {		
			@Override
			public void onClick(com.smartgwt.client.widgets.events.ClickEvent event) {
				save();				
			}
		});	      
	      
	      
	      _btnClose = new Button();
	      _btnClose.setTitle(Captions.CLOSE);
	      _btnClose.addClickHandler(new com.smartgwt.client.widgets.events.ClickHandler() {		
			@Override
			public void onClick(com.smartgwt.client.widgets.events.ClickEvent event) {			
				close();
			}
		}); 
	      
	      
	      com.smartgwt.client.widgets.events.ClickHandler btnFlowClickHandler = new com.smartgwt.client.widgets.events.ClickHandler() {		
				@Override
				public void onClick(com.smartgwt.client.widgets.events.ClickEvent event) {
					sendToTheNextStage();				
				}
			};	  
			
	      
	      _btnSendToApprove = new Button();
	      _btnSendToApprove.setTitle(Captions.ON_APPROOVAL);		  
	      _btnSendToApprove.addClickHandler(btnFlowClickHandler);	      
	      
	      _btnSendToSigne = new Button();
	      _btnSendToSigne.setTitle(Captions.ON_SIGN);		  
	      _btnSendToSigne.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				if (CheckDocumentUserRight.isCanSendToSign(getSelf()) ) {
					sendToTheNextStage();	
				} else {
					Dialogs.ShowMessage(Captions.MSG_DOC_SEND_TO_SIGN);
					
				}	
				
			}
		});
	      
	      _btnSign = new Button();
	      _btnSign.setTitle(Captions.SIGN );		  
	      _btnSign.addClickHandler(new ClickHandler() {			
	    	  
			@Override
			public void onClick(ClickEvent event) {
				sendToTheNextStage();											
			}
		});	   
	      
	      _btnCancelSign = new Button();
	      _btnCancelSign.setTitle( Captions.CANCEL);		  
	      _btnCancelSign.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				
				final long docId = getDocumentId();
				
							FlowProccessing.cancelProcess(docId, new AsyncCallback<Void>() {
								
								@Override
								public void onSuccess(Void result) {
									Dialogs.ShowMessage(Captions.MSG_DOC_SEND_REFUSE_TO_AUTHOR);								
									callRequestData();
									
								}
								
								@Override
								public void onFailure(Throwable caught) {
									_logger.severe(caught.getMessage());
									
								}
							});						
				
			}
		}) ;
	      
	      btnlayout.addMembers(_btnSave, _btnClose, _btnSendToApprove,_btnSendToSigne,_btnSign, _btnCancelSign);
	      visibleButtons(new Boolean[]{ false,false,false,false,false,false});
	      return btnlayout;
	}
	
	
	
	public void visibleButtons(Boolean[] visibles) {
		_btnSave.setVisible(visibles[0]);
		_btnClose.setVisible(visibles[1]);
		_btnSendToApprove.setVisible(visibles[2]);
		_btnSendToSigne.setVisible(visibles[3]);
		_btnSign.setVisible(visibles[4]);
		_btnCancelSign.setVisible(visibles[5]);
	}		
	
	
	public   HashMap<EDocumentDataSection, IDataView>  getDataSections() {
		return _hmDataSections;	
	}	
	
	
	private void  sendToTheNextStage() {
		_flowProccess.sendToTheNextStage(new BooleanCallback() {			
			@Override
			public void execute(Boolean value) {
				if (value) {
					simpleSave(false);
					//close();
				}
				
			}
		});
	}
	
	@Override
	public Record getSelectedRecord() {
		return _docMainFields.getSelectedRecord();
	}
	
	@Override
	public Record[] getSelectedRecords() {
		return _docMainFields.getSelectedRecords();
	}

	@Override
	public boolean isSelectedRecord() {
		// TODO Auto-generated method stub
		return false;
	}
	

	@Override
	public void bindDataComponents() {		
	
		if (_viewState == EViewState.New) {
			IDataSource ds = getDataSource(Integer.valueOf(NEW_DOCID_GEN));			
			
			Record[] recs = ds.getRecords();
		    String docId =recs[0].getAttribute("ID_DOC"); 	   	    		    	
			setDocumentId(Long.parseLong(docId ));
			_currentStatus =EDocStatus.Draft;
			
			getOwnerWindow().setTitle(getCaption());
			
			visibleButtons(new Boolean[]{true,true,true,false,false, false});			
		}
		else {			
			CheckDocumentUserRight.changeVisibleControls(this);
		}
		
		enableEditState(CheckDocumentUserRight.isCanEditDocument(this));
		hideWaitingPopup();		
	}
	
	private void hideWaitingPopup() {
		//_waitPopup.hide();
	}
	
	@Override
	public void bindDataComponentsAfterChange() {

		getDataBinder().getDataProvider().getRequest().clear();		
		saveAttachments();				
	}
	
	/**
	 * @param isCan
	 */
	private void enableEditState(boolean isCanEdit) {
		_logger.info("Set component disable = "+isCanEdit);
		 for (IDataView view : getDataSections().values() )  {
			 view.setCanEdit(isCanEdit);
		 }		 
	}


	@Override
	protected void createMenu() {		    		        
        
        getMenuContainer().bind(new MenuFile(this));
        
        
        if ( CheckDocumentUserRight.isHasBARole() ) {
        	getMenuContainer().bind(new MenuBAAction(this));
        }
	}	

	
	
	@Override	
	public String getCaption() {
		return getCorrespondentTypeRootCode().getCaption() + getDocumentId();		
	}

	@Override
	public void prepareRequestData() {				
		_logger.info("################ Start prepareRequestData ######################");
		if (_viewState == EViewState.New) {
		_logger.info("Create new document, request new docId");
		
			getDataBinder().getDataProvider().getRequest().setPosition(0);
			
	    	IRequestData entity = getDataBinder().getDataProvider().getRequest().add(new RequestEntity (NEW_DOCID_GEN ));
	    	entity.setRequestFieldsDescription(false);
		} else  {
		_logger.info("Reqyesr data for exists document");
			getDataBinder().getDataProvider().getRequest().setPosition(100);
		}
		prepareRequestData(_docMainFields, _docAttachmentsList);		 
	}
	
	
	@Override
	public void putRequestToQueue()  {
		putRequestToQueue(this,_docMainFields, _docAttachmentsList);
	}	
	
	
	public static void OpenById(final String documentId, ECorrespondentType correspondentTypeRootCode) {						
		
			IView doc =  GetDocViewById(documentId, correspondentTypeRootCode);
					
					if (doc != null) {
						AppController.getInstance().getMainView().openViewInTab(doc);
					}
	}
	
	
	public static void OpenById(final String documentId) {
	
		CheckDocumentExists.isExist(documentId, new ICallbackEvent<ECorrespondentType>() {
			
			@Override
			public void doWork(ECorrespondentType data) {
						if ( data != ECorrespondentType.UNKNOWN ) {
							IView doc =  GetDocViewById(documentId, data);
							
							if (doc != null) {
								AppController.getInstance().getMainView().openViewInTab(doc);
							} 					
						} else {
							SC.say(Captions.ERROR, Captions.NOT_FOUND_DOC +documentId  );
						}
			}
		});		
		
	}
	
	
	public static IView GetDocViewById(String documentId, ECorrespondentType correspondentTypeRootCode) {
		DocumentCard doc =  DocumentCardFactory.create(correspondentTypeRootCode);
		doc.setSingleInstance(true);
		doc._viewState = EViewState.Edit;
		doc.setDocumentId(Long.parseLong(documentId));		
		return doc;
	}

	
	
	public static IView newDocument(ECorrespondentType correspondentTypeRootCode) {		
		DocumentCard doc =  DocumentCardFactory.create(correspondentTypeRootCode);
		doc._viewState = EViewState.New;
		return doc;
	}
	
	public static IDataEditHandler getShowEditViewHandler() {
			return _showEditViewHandler;			
	}

	public long getDocumentId() {
		return _documentId;
	}
	
	public void setDocumentId(long value) {
		this._documentId = value;				 
	}
	
	/**
	 * @return the _correspondentTypeRootCode
	 */
	public ECorrespondentType getCorrespondentTypeRootCode() {
		return _correspondentTypeRootCode;
	}

	/**
	 * @param _correspondentTypeRootCode._correspondentTypeRootCode the _correspondentTypeRootCode to set
	 */
	public void setCorrespondentTypeRootCode(ECorrespondentType value) {
		this._correspondentTypeRootCode = value;
	}		
	
	
	
	@Override
	public void close() {		
		IsCanClose(null);
	}
	
		
	@Override
	public void IsCanClose(final BooleanCallback callback) {		
			
		if ( isHaseChanges()) {
			
			Dialog dialog = new Dialog();
		     dialog.setShowModalMask(true);
		     dialog.setButtons(Dialog.YES,Dialog.NO, Dialog.CANCEL);     
		     
		     
			SC.ask(Captions.ALARM, Captions.Q_DOC_HAS_CHANGE_SAVE_BEFORE_CLOSE , new BooleanCallback() {
				
				@Override
				public void execute(Boolean value) {					
						if (value == null) {
							return;
						}
						else {	
							
								if (value) {
									_afterSaveAction = EAfterSaveAction.Close;
									simpleSave(false);									
								} else {
									loseChange();
									closeWnd();
								}
						}													
															
							
				}
				
			}, dialog);				
		}
		else {
			AppController.getInstance().getMainView().closeCurrentTab();			
		}
			
	}
	
	private void closeWnd() {
		AppController.getInstance().getMainView().closeCurrentTab();
	}

	
	@Override
	public void loseChange() {
		for ( IDataView view :  _hmDataSections.values()) {
			view.loseChange();	
		}
	}
	
	public EViewState getViewState() {
		return _viewState;
	}
		
	
	/* (non-Javadoc)
	 * @see mdb.core.ui.client.communication.IRemoteDataSave#save()
	 */
	@Override
	public void save() {
		
		if (!isValidate() ) {
			SC.warn(Captions.ERROR_VALIDATION ,Captions.ERROR_REQUIRED );
			return; //false;
		}
		
		if ( isHaseChanges() ) {			

			SC.ask(Captions.Q_SAVE_CHANGES , new BooleanCallback() {				
				@Override
				public void execute(Boolean value) {
					if (value) {
						simpleSave(false);
					}																				
				}
			});
			
		}		
		
	}		
	
	
	private void simpleSave(boolean checkHaseChanges) {
	
		if (!isValidate() ) {
			SC.warn(Captions.ERROR_VALIDATION , Captions.ERROR_REQUIRED);
			return;
		}
		
		if (checkHaseChanges) {		
			 if ( !isHaseChanges() ) {
				 return;
			 }
		}				
			
		saveCard();					
	}	
	
	public void saveCard() {
		_viewState = EViewState.Edit;
		//_waitPopup.show(Captions.SAVE_PROCESS );
		prepareSavedData();
		putSavedDataToQueue();
		GatewayQueue.instance().getProcessor().run();
	}
	
	
	private void saveAttachments() {
		_logger.info("saveAttachments");
		_uploadWidget.uploadAllFiles(new ICallbackEvent<Boolean>() {			
			@Override
			public void doWork(Boolean data) {				
				_logger.info("Clear for uploads bar");
				_uploadWidget.clearFilesForUpload();
				callAfterSaveAction();
			}
		});		
	}
	
	private void callAfterSaveAction() {
		_logger.info("AfterSaveAction = "+_afterSaveAction);
		switch (_afterSaveAction) {
		case Refresh:
				SC.say(Captions.DOC_SAVE_CORRECT, new BooleanCallback() {
					
					@Override
					public void execute(Boolean value) {
						_logger.info("Call refresh");						
						refresh();				
					}
				});
			break;
		case Close:
			hideWaitingPopup();
			closeWnd();
			break;
		default:
			break;
		}		
	}
	
	/**
	 * @return
	 */
	@Override
	public boolean isHaseChanges() {	

		boolean toReturn =_uploadWidget.getCountFileForUpload() >0;
		
		if (toReturn) return true;
		
		else {
			
			for (IDataView view : _hmDataSections.values()) {		
				if ( view.isHaseChanges() )
				 return true;
			}		
		}
		 return false;				
	}

	/* (non-Javadoc)
	 * @see mdb.core.ui.client.communication.IRemoteDataSave#isValidate()
	 */
	@Override
	public boolean isValidate() {
		boolean toReturn = true;
		for (IDataView view: _hmDataSections.values()) {			 
			
			 if(   !view.isValidate() ) {
				 toReturn = false;
			 }
		}
		
		return toReturn;
	}

	/* (non-Javadoc)
	 * @see mdb.core.ui.client.communication.IRemoteDataSave#prepareSavedData()
	 */
	@Override
	public void prepareSavedData() {
		_docMainFields.prepareSavedData();		
		_docAttachmentsList.prepareSavedData();
	}

	/* (non-Javadoc)
	 * @see mdb.core.ui.client.communication.IRemoteDataSave#putSavedDataToQueue()
	 */
	@Override
	public void putSavedDataToQueue() {
		_docMainFields.putRequestToQueue();		
		_docAttachmentsList.putSavedDataToQueue();
				
		RequestEntity rq = new RequestEntity();
		rq.setExecuteType(ExecuteType.ChangeData);
		getDataBinder().getDataProvider().getRequest().add(rq);
		GatewayQueue.instance().put(getDataBinder().getDataProvider());
	}
	
	public DataFieldsSection getDocumentFields() {
		return _docMainFields;
	}

	/**
	 * @param nextPossibleStatus
	 */
	public void setDocumentStatus(EDocStatus value) {		
		_docMainFields.setFieldValue("ID_STATUS",String.valueOf(value.getValue()) );
	}
	
	
	public EDocStatus getDocumentStatus() {		
		String idStatus = _docMainFields.getFieldValue("ID_STATUS");
		if (idStatus!= null) {		
			return EDocStatus.fromInt(Integer.parseInt(idStatus));
		} else return EDocStatus.Draft;
	}
	
	
	public int  getDocumentAuthor() {		
		return  Integer.parseInt(_docMainFields.getFieldValue("ID_EMP_AUTHOR"));		
	}	
		
	
	public void refresh() {
		callRequestData();
	}
	
	public String getLinkToDocument() {		
		String lnkToDoc =  Window.Location.getProtocol()+"//"+Window.Location.getHost()+"/document.flow.ui/index.html?DocumentCard="+getDocumentId();
		//String lnkToDoc =  Window.Location.getHref()+"index.html?DocumentCard="+getDocumentId();
		_logger.info("Link to Document = "+ lnkToDoc);		
		return lnkToDoc;
	}
	
	public TabSet getMainTabSet() {
		return _mainTabSet;
	}


}
