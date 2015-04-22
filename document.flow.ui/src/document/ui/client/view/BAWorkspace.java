/**
 * 
 */
package document.ui.client.view;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.logging.Logger;

import mdb.core.shared.data.Params;
import mdb.core.ui.client.app.AppController;
import mdb.core.ui.client.command.ICommand;
import mdb.core.ui.client.communication.impl.SimpleMdbDataRequester;
import mdb.core.ui.client.data.bind.DataBindException;
import mdb.core.ui.client.events.ICallbackEvent;
import mdb.core.ui.client.events.IDoubleValuesCallbackEvent;
import mdb.core.ui.client.view.components.menu.IMenuButtons;
import mdb.core.ui.client.view.components.menu.IMenuItem;
import mdb.core.ui.client.view.components.menu.data.AMenuData;
import mdb.core.ui.client.view.data.DataView;
import mdb.core.ui.client.view.data.grid.GridView;
import mdb.core.ui.client.view.dialogs.input.InputTextDialog;
import mdb.core.ui.client.view.dialogs.message.Dialogs;
import mdb.core.ui.client.view.utils.DateTimeHelper;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.smartgwt.client.data.Record;
import com.smartgwt.client.types.SelectionAppearance;
import com.smartgwt.client.types.SelectionStyle;
import com.smartgwt.client.util.BooleanCallback;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.tab.Tab;
import com.smartgwt.client.widgets.tab.TabSet;

import document.ui.client.commons.ECorrespondentType;
import document.ui.client.commons.EFlowStage;
import document.ui.client.commons.checker.CheckGenerateDocCode;
import document.ui.client.flow.impl.FlowProccessing;
import document.ui.client.resources.locales.Captions;
import document.ui.client.view.doc.card.DocumentCard;
import document.ui.shared.MdbEntityConst;

/**
 * @author azhuk
 * Creation date: Aug 6, 2014
 *
 */
public class BAWorkspace extends DataView {
	
	private static final Logger _logger = Logger.getLogger(BAWorkspace.class
			.getName());
	
	TabSet 		_mainTabSet;
	GridView 	_grDocsInWork;
	GridView 	_grDocsToPublish;
	GridView 	_grOrderOnUpdatedDay;
	GridView 	_grDocsPublished;
	
	protected Date _dateB;
	protected Date _dateE;
	
	private CheckGenerateDocCode _checkGenerateDocCode = new CheckGenerateDocCode(); 	
	
	class MenuDocInFlow extends mdb.core.ui.client.view.components.menu.Menu {
		
		public MenuDocInFlow() {
			super("MenuDocInFlow");
			
			_dateB = DateTimeHelper.getFirstDayOfMonth(new Date());			
			_dateE = DateTimeHelper.getLastDayOfMonth( new Date());
						
			IMenuItem item = addItem("","",IMenuItem.ItemType.DateRange,0);
			item.setValueMap(new LinkedHashMap<String, Object>(){
			{
				put("dateFrom",_dateB);
				put("dateTo",_dateE);				
				}}
			);	
			item.setCommand(new ICommand<IMenuItem>() {
				@Override
				public void execute(IMenuItem sender) {
					String att = "dateRangeType";
					if ( sender.isAttributeExist(att) ) {					
						if (sender.getAttribute("dateRangeType").equalsIgnoreCase("dateFrom")) {
							_dateB = (Date) sender.getValue();
						}
						else { 
							_dateE = (Date) sender.getValue();						
						}
					}
				}
			});
			
			item = addItem( Captions.REFRESH, "", IMenuItem.ItemType.ToolButton,0);
			AMenuData.initPropertyMenuItem(item, IMenuButtons.Buttons.dataRefresh );
			item.setCommand(new ICommand<IMenuItem>() {						
				@Override
				public void execute(IMenuItem sender) {						
					callRequestData();				
				}
			});
			
		
			item = addItem(Captions.OPEN_CARD, "", IMenuItem.ItemType.ToolButton,0);
			item.setCommand(new ICommand<IMenuItem>() {						
				@Override
				public void execute(IMenuItem sender) {
					openSelectedCards(_grDocsInWork);																					
				}

			});	
			
			item = addItem("","", IMenuItem.ItemType.Separator,3);
			
			item = addItem(Captions.SEND_REMEMBER , "", IMenuItem.ItemType.ToolButton,0);
			item.setCommand(new ICommand<IMenuItem>() {						
				@Override
				public void execute(IMenuItem sender) {						
					sendRemember();				
				}

			});

			item = addItem(Captions.SEND_TO_SIGN , "", IMenuItem.ItemType.ToolButton,0);
			item.setCommand(new ICommand<IMenuItem>() {						
				@Override
				public void execute(IMenuItem sender) {						
					sendForSignature();			
				}
			});				
					
			
			
			item = addItem( Captions.REJECT, "", IMenuItem.ItemType.ToolButton,0);
			item.setImg(AMenuData.getButtonInitialiser().get(IMenuButtons.Buttons.dataCancel).getImg());
			item.setCommand(new ICommand<IMenuItem>() {						
				@Override
				public void execute(IMenuItem sender) {						
					cancel(_grDocsInWork);			
				}								
			});
			
			item = addItem(Captions.DELETE, "", IMenuItem.ItemType.ToolButton,0);
			item.setImg(AMenuData.getButtonInitialiser().get(IMenuButtons.Buttons.dataDelete).getImg());
			item.setCommand(new ICommand<IMenuItem>() {						
				@Override
				public void execute(IMenuItem sender) {						
					deleteProcess(_grDocsInWork);			
				}
											
			});		
		}
	};	
	
	class MenuDocToPublish extends mdb.core.ui.client.view.components.menu.Menu {
		
		public MenuDocToPublish() {
			super("MenuDocToPublish");	
			
			IMenuItem  item = addItem( Captions.REFRESH, "", IMenuItem.ItemType.ToolButton,0);
			AMenuData.initPropertyMenuItem(item, IMenuButtons.Buttons.dataRefresh );
			item.setCommand(new ICommand<IMenuItem>() {						
				@Override
				public void execute(IMenuItem sender) {						
					callRequestData();				
				}
			});
			
			
			item = addItem(Captions.OPEN_CARD, "", IMenuItem.ItemType.ToolButton,1);
			item.setCommand(new ICommand<IMenuItem>() {						
				@Override
				public void execute(IMenuItem sender) {
					openSelectedCards(_grDocsToPublish);																					
				}

			});
			
			item = addItem(Captions.PUBLISH , "", IMenuItem.ItemType.ToolButton,2);
			item.setCommand(new ICommand<IMenuItem>() {						
				@Override
				public void execute(IMenuItem sender) {						
					publishingDoc(_grDocsToPublish);			
				}
				
			});		
			
			item = addItem(Captions.REJECT  , "", IMenuItem.ItemType.ToolButton,3);
			item.setImg(AMenuData.getButtonInitialiser().get(IMenuButtons.Buttons.dataCancel).getImg());
			item.setCommand(new ICommand<IMenuItem>() {						
				@Override
				public void execute(IMenuItem sender) {						
					cancel(_grDocsToPublish);		
				}
				
			});																			
		
		}
	};    
	
	public BAWorkspace () { 
		super(EViewPanelType.VLayout);
		setSingleInstance(true);							
	}
	
	@Override
	protected void createComponents() {
		super.createComponents();	
		_mainTabSet = new TabSet();
		_mainTabSet.setWidth100();
		_mainTabSet.setHeight100();
		/*Основные параметры*/
		Tab tabDocsOnDay = new Tab();
		tabDocsOnDay.setTitle(Captions.DOC_IN_WORK);
		_grDocsInWork = new GridView(MdbEntityConst.BAWorkspace);
		_grDocsInWork.setCreateMenuNavigator(false);
		_grDocsInWork.getListGrid().setSelectionType(SelectionStyle.SIMPLE);
		_grDocsInWork.getListGrid().setSelectionAppearance(SelectionAppearance.CHECKBOX);
		_grDocsInWork.getMenuContainer().bind(new MenuDocInFlow());		
		_grDocsInWork.setAutoSave(true);		

		tabDocsOnDay.setPane(_grDocsInWork);
		_mainTabSet.addTab(tabDocsOnDay);
		
		
		Tab tabDocsToPublish = new Tab(Captions.DOC_FOR_PUBLISH);		
		_grDocsToPublish = new GridView(MdbEntityConst.DocToPublish);
		_grDocsToPublish.setCreateMenuNavigator(false);
		_grDocsToPublish.getListGrid().setSelectionType(SelectionStyle.SIMPLE);
		_grDocsToPublish.getListGrid().setSelectionAppearance(SelectionAppearance.CHECKBOX);
		_grDocsToPublish.getMenuContainer().bind(new MenuDocToPublish());			
		tabDocsToPublish.setPane(_grDocsToPublish);
		_mainTabSet.addTab(tabDocsToPublish);
		
		
		
		Tab tabOrderInUpdatedDay = new Tab();
		tabOrderInUpdatedDay.setTitle(Captions.ORDER_FOR_REFRESH);
		_grOrderOnUpdatedDay = new GridView(MdbEntityConst.OrderOnUpdatedDay);
		_grOrderOnUpdatedDay.setCreateMenuNavigator(false);
		_grOrderOnUpdatedDay.addViewEvent(DocumentCard.getShowEditViewHandler());
		_grOrderOnUpdatedDay.addEditEvent(DocumentCard.getShowEditViewHandler());				
		tabOrderInUpdatedDay.setPane(_grOrderOnUpdatedDay);
		_mainTabSet.addTab(tabOrderInUpdatedDay);
		
		
		Tab tabDocInForce = new Tab();
		tabDocInForce.setTitle(Captions.DOC_PUBLISHED);
		_grDocsPublished = new GridView(MdbEntityConst.DOC_PUBLISHED);
		_grDocsPublished.setCreateMenuNavigator(false);
		_grDocsPublished.addViewEvent(DocumentCard.getShowEditViewHandler());
		_grDocsPublished.addEditEvent(DocumentCard.getShowEditViewHandler());				
		tabDocInForce.setPane(_grDocsPublished);
		_mainTabSet.addTab(tabDocInForce);			

		
		
		getViewPanel().addMember(_mainTabSet);
	}
	
	@Override
	public String getCaption() {
		return Captions.BA_WORKSPACE;
	}



	/* (non-Javadoc)
	 * @see mdb.core.ui.client.view.data.IDataView#getSelectedRecord()
	 */
	@Override
	public Record getSelectedRecord() {
		return null;
	}


	@Override
	public Record[] getSelectedRecords() {
		return null;
	}
	

	/* (non-Javadoc)
	 * @see mdb.core.ui.client.view.data.IDataView#isSelectedRecord()
	 */
	
	@Override
	public boolean isSelectedRecord() {	
		return false;
	}



	/* (non-Javadoc)
	 * @see mdb.core.ui.client.view.data.DataView#prepareRequestData()
	 */
	@Override
	public void prepareRequestData() {
		
		_logger.info("DateB:"+DateTimeHelper.format(_dateB));
		_logger.info("DateE:"+DateTimeHelper.format(_dateE));
		
		_grDocsInWork.getParams().add("dateFrom",DateTimeHelper.format(_dateB));
		_grDocsInWork.getParams().add("dateTo",DateTimeHelper.format(_dateE) );
		
		_grDocsPublished.getParams().add("DTA_B", DateTimeHelper.format(new Date()) );
		_grDocsPublished.getParams().add("DTA_E", DateTimeHelper.format(new Date()) );
		_grDocsPublished.getParams().add("CURRENT_USER", String.valueOf(AppController.getInstance().getCurrentUser().getId()));
		
		prepareRequestData(_grDocsInWork, _grOrderOnUpdatedDay, _grDocsToPublish,_grDocsPublished);
	}

	@Override
	public void putRequestToQueue()  {
		putRequestToQueue(_grDocsInWork, _grOrderOnUpdatedDay, _grDocsToPublish, _grDocsPublished);
	}


	/* (non-Javadoc)
	 * @see mdb.core.ui.client.view.data.DataView#bindDataComponents()
	 */
	@Override
	public void bindDataComponents() throws DataBindException {
		// TODO Auto-generated method stub		
	}
	
	
	
	private void openSelectedCards(GridView view) {
		Record[] records =view.getListGrid().getSelectedRecords();
		for (Record rec : records) {							
			DocumentCard.OpenById(rec.getAttribute("ID_DOC"), 
					ECorrespondentType.fromString(rec.getAttribute("CORR_TYPE")));
		}	
	}
	
	private void sendRemember() {
		Record[] records =_grDocsInWork.getListGrid().getSelectedRecords();
		if ( records.length == 0) {
			Dialogs.ShowMessage(Captions.NOT_CHOUSE_DOC);			
			return;
		}
		
		for (final Record rec : records) {			
			final String docid = rec.getAttribute("ID_DOC");
			FlowProccessing.sendRemember(Integer.parseInt(docid));	
		}				
	}
	
	
	private void cancel(GridView view) {
		
		Record[] records =view.getListGrid().getSelectedRecords();
		if ( records.length == 0) {
			Dialogs.ShowMessage(Captions.NOT_CHOUSE_DOC);			
			return;
		}
		
		for (final Record rec : records) {			
			
			final String docid = rec.getAttribute("ID_DOC");
			FlowProccessing.cancelProcess(Long.parseLong(docid), new AsyncCallback<Void>() {
				
				@Override
				public void onSuccess(Void result) {
					SC.say("Автору документа: "+docid +" отправлен отказ ");		
					callRequestData();					
				}
				
				@Override
				public void onFailure(Throwable caught) {
					_logger.severe(caught.getMessage());
					
				}
			});				
		}			
		
	}
	
	private void deleteProcess(final GridView gridView) {
		gridView.callDeleteEvent();			
	}	
	
	private void sendForSignature () {
		
		Record[] records =_grDocsInWork.getListGrid().getSelectedRecords();
		if ( records.length == 0) {
			Dialogs.ShowMessage(Captions.NOT_CHOUSE_DOC);
			return;
		}
		for (final Record rec : records) {
			
			String flowStage = rec.getAttribute("ID_FLOW_STAGE");
			final String documentId = rec.getAttribute("ID_DOC");
			
			if ( EFlowStage.fromInt( Integer.parseInt(flowStage) ) ==  EFlowStage.InitSigne) {				
				
				generateCodeDoc(_grDocsInWork, new ICallbackEvent<String>() {
					
					@Override
					public void doWork(String data) {
						
						setDocCodeToDocCard(documentId, data);
						
						FlowProccessing.sendToSignFromBA(Integer.parseInt(documentId), 
								rec.getAttribute("INFO_MSG") , AppController.getInstance().getCurrentUser().getId() ,
								new ICommand<Boolean>() {
									
									@Override
									public void execute(Boolean value) {
										if (value) {
											SC.say(Captions.DOC_ID +documentId + Captions.SENDIED_TO_SIGN);
											callRequestData();
										}
									}
								});						
					}
				});									
			}
		}		
	}
	
	
	private void setDocCodeToDocCard(String documentId,  String docCode) {
		
		Params params = new Params();
		params.add("id_doc", documentId);
		params.add("CODE", docCode);
		
		SimpleMdbDataRequester.callAction( _grDocsInWork.getMainEntityId(), 3057, params, new ICallbackEvent<Record[]>() {
			
			@Override
			public void doWork(Record[] data) {
				Dialogs.ShowMessage(Captions.APPLY_CODE_TO_DOC);				
			}
		});					
	}

	private void generateCodeDoc(GridView view,  final ICallbackEvent<String> callbackEvent) {
		
		Record[] records =view.getListGrid().getSelectedRecords();
		if ( records.length == 0) {
			Dialogs.ShowMessage(Captions.NOT_CHOUSE_DOC);			
			return;
		}
		
			for (final Record rec : records) {			
		
				generateCodeDoc(rec,  callbackEvent);
						
			}
	}
	
	
	private void generateCodeDoc(Record rec,  final ICallbackEvent<String> callbackEvent) {
		
						final String idDoc = rec.getAttribute("ID_DOC");

						_checkGenerateDocCode.generate(idDoc, new ICallbackEvent<String>() {
							
							@Override
							public void doWork(String data) {							
								
								
								InputTextDialog textDlg = new InputTextDialog(Captions.CODE_DOC_GENERATE ,
										Captions.CODE_DOC ,data, new IDoubleValuesCallbackEvent<Boolean, String>() {			
									@Override
									public void execute(Boolean result, String docCode) {
										
										if (result ) {																		
											callbackEvent.doWork(docCode);
																															
										} 			
									}
								}); 			
								
								textDlg.view();								
							}
						});					
						
			
	}
	
	

	private void publishingDoc(GridView view) {
	
			Record[] records =view.getListGrid().getSelectedRecords();
			if ( records.length == 0) {
				Dialogs.ShowMessage(Captions.NOT_CHOUSE_DOC);			
				return;
			}
		
			for (final Record rec : records) {						

						String docType = rec.getAttribute("ID_CORR");
						final String idDoc = rec.getAttribute("ID_DOC");
						
						
						
							final ICallbackEvent<String>  callPublish = new ICallbackEvent<String>() {
							
												@Override
												public void doWork(final String data) {
													SC.ask(Captions.Q_PUBLISH_DOC, new BooleanCallback() {
														
														@Override
														public void execute(Boolean value) {
															if (value ) {																		
																
																FlowProccessing.publishDoc(Integer.parseInt(idDoc), 
																		data, AppController.getInstance().getCurrentUser().getId() ,
																		new ICommand<Boolean>() {
																			
																			@Override
																			public void execute(Boolean value) {
																				if (value) {
																					SC.say(Captions.DOC_ID +idDoc +Captions.PUBLISHED );
																					callRequestData();
																				}
																			}
																		});																						
															} 	
															
														}
													});		
													
												}
											};
						
						
						
						if ( ECorrespondentType.fromInt( Integer.valueOf(docType).intValue() ) != ECorrespondentType.INSIDE_PRIKAZ_DOC) {
							generateCodeDoc(rec, new ICallbackEvent<String>() {
								
								@Override
								public void doWork(String data) {
									callPublish.doWork(data);
									
								}
							});		
						} else 
							callPublish.doWork(null);						
			}
	}
	
}
