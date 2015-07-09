/**
 * 
 */
package document.ui.client.view;

import java.util.Date;
import java.util.HashMap;
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
import mdb.core.ui.client.view.data.IDataView;
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
import document.ui.client.commons.EDocStatus;
import document.ui.client.commons.EFlowStage;
import document.ui.client.commons.checkers.CheckGenerateDocCode;
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
	
	enum EDataSection {
		DocsInWork,
		DocsToPublish,
		ProcedureToUpdatedDay,
		DocsPublished
	}
	
	TabSet 		_mainTabSet;
	
	protected HashMap<EDataSection , GridView> _hmDataSection;
	
	protected Date _dateB;
	protected Date _dateE;
	
	private CheckGenerateDocCode _checkGenerateDocCode = new CheckGenerateDocCode(); 	
	
	class MenuDocInFlow extends mdb.core.ui.client.view.components.menu.Menu {
		
		@SuppressWarnings("serial")
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
					
					DocumentCard.openSelectedCards(_hmDataSection.get(EDataSection.DocsInWork));																					
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
					
					cancel(_hmDataSection.get(EDataSection.DocsInWork));			
				}								
			});
			
			item = addItem(Captions.DELETE, "", IMenuItem.ItemType.ToolButton,0);
			item.setImg(AMenuData.getButtonInitialiser().get(IMenuButtons.Buttons.dataDelete).getImg());
			item.setCommand(new ICommand<IMenuItem>() {						
				@Override
				public void execute(IMenuItem sender) {						
					deleteProcess(_hmDataSection.get(EDataSection.DocsInWork));			
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
					DocumentCard.openSelectedCards(_hmDataSection.get(EDataSection.DocsToPublish) );																					
				}

			});
			
			item = addItem(Captions.PUBLISH , "", IMenuItem.ItemType.ToolButton,2);
			item.setCommand(new ICommand<IMenuItem>() {						
				@Override
				public void execute(IMenuItem sender) {						
					publishingDoc(_hmDataSection.get(EDataSection.DocsToPublish) );			
				}
				
			});		
			
			item = addItem(Captions.REJECT  , "", IMenuItem.ItemType.ToolButton,3);
			item.setImg(AMenuData.getButtonInitialiser().get(IMenuButtons.Buttons.dataCancel).getImg());
			item.setCommand(new ICommand<IMenuItem>() {						
				@Override
				public void execute(IMenuItem sender) {
					
					cancel(_hmDataSection.get(EDataSection.DocsToPublish));		
				}
				
			});																			
		
		}
	};    
	
	public BAWorkspace () { 
		super(EViewPanelType.VLayout);
		setSingleInstance(true);							
	}
	
	protected GridView  createDataSection (EDataSection edataSection,  int entityId) {
		GridView dataSection = new GridView(entityId);
		dataSection.setCreateMenuNavigator(false);
		dataSection.getListGrid().setSelectionType(SelectionStyle.SIMPLE);
		dataSection.getListGrid().setSelectionAppearance(SelectionAppearance.CHECKBOX);				
		dataSection.setAutoSave(true);
		_hmDataSection.put(edataSection, dataSection);
		return dataSection;
		
	}
	
	@Override
	protected void createComponents() {
		super.createComponents();	
		_hmDataSection = new HashMap<EDataSection , GridView>(); 
		
		_mainTabSet = new TabSet();
		_mainTabSet.setWidth100();
		_mainTabSet.setHeight100();
		/*Основные параметры*/
		
		Tab tabDocsOnDay = new Tab();
		tabDocsOnDay.setTitle(Captions.DOC_IN_WORK);
		GridView view = createDataSection(EDataSection.DocsInWork,  MdbEntityConst.BAWorkspace);
		view.getMenuContainer().bind(new MenuDocInFlow());
		tabDocsOnDay.setPane(view);

		_mainTabSet.addTab(tabDocsOnDay);
		
		
		Tab tabDocsToPublish = new Tab(Captions.DOC_FOR_PUBLISH);	
		view = createDataSection(EDataSection.DocsToPublish,MdbEntityConst.DocToPublish);
		view.getMenuContainer().bind(new MenuDocToPublish());
		tabDocsToPublish.setPane(view);		
		_mainTabSet.addTab(tabDocsToPublish);		
		
		
		createSectionForProcedure();		
		
		Tab tabDocPublished = new Tab();
		tabDocPublished.setTitle(Captions.DOC_PUBLISHED);
		tabDocPublished.setPane(createDataSection(EDataSection.DocsPublished, MdbEntityConst.DOC_PUBLISHED));
		_mainTabSet.addTab(tabDocPublished);			
		
		
		getViewPanel().addMember(_mainTabSet);
	}

	/**
	 * 
	 */
	protected
	void createSectionForProcedure() {
		Tab tabOrderInUpdatedDay = new Tab();
		tabOrderInUpdatedDay.setTitle(Captions.PROCEDURE_TO_UPDATE);
		tabOrderInUpdatedDay.setPane(createDataSection(EDataSection.ProcedureToUpdatedDay,  MdbEntityConst.ProcedureToUpdatedDay));
		_mainTabSet.addTab(tabOrderInUpdatedDay);
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
		
		getParams().add("DTA_B",DateTimeHelper.format(_dateB));
		getParams().add("DTA_E",DateTimeHelper.format(_dateE) );
		getParams().add("CURRENT_USER", String.valueOf(AppController.getInstance().getCurrentUser().getId()));
		getParams().add("CORR_ROOT_CODE", ECorrespondentType.getRootCodeCorrespondentType());
		
		
		for ( IDataView dataView : _hmDataSection.values()) {
			dataView.getParams().putAll(getParams());			
		}			
		
		IDataView [] arr = _hmDataSection.values().toArray(new IDataView[_hmDataSection.values().size()]);
		prepareRequestData(arr);
	}

	@Override
	public void putRequestToQueue()  {		
		
		IDataView [] arr = _hmDataSection.values().toArray(new IDataView[_hmDataSection.values().size()]);
		putRequestToQueue(arr);			 
		
	}


	/* (non-Javadoc)
	 * @see mdb.core.ui.client.view.data.DataView#bindDataComponents()
	 */
	@Override
	public void bindDataComponents() throws DataBindException {
		// TODO Auto-generated method stub		
	}
	
	
	
	
	
	private void sendRemember() {
		Record[] records =_hmDataSection.get(EDataSection.DocsInWork).getListGrid().getSelectedRecords();
		if ( records.length == 0) {
			Dialogs.ShowMessage(Captions.NOT_CHOUSE_DOC);			
			return;
		}
		
		for (final Record rec : records) {						
		
			FlowProccessing.sendRemember(Integer.parseInt(rec.getAttribute("ID_DOC")) , 
						EDocStatus.fromInt(Integer.parseInt( rec.getAttribute("ID_STATUS"))));	
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
		
		GridView  view = _hmDataSection.get(EDataSection.DocsInWork);
		Record[] records =view.getListGrid().getSelectedRecords();
		if ( records.length == 0) {
			Dialogs.ShowMessage(Captions.NOT_CHOUSE_DOC);
			return;
		}
		for (final Record rec : records) {
			
			String flowStage = rec.getAttribute("ID_FLOW_STAGE");
			final String documentId = rec.getAttribute("ID_DOC");
			
			if ( EFlowStage.fromInt( Integer.parseInt(flowStage) ) ==  EFlowStage.InitSigne) {				
				
				generateCodeDoc(view, new ICallbackEvent<String>() {
					
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
		
		
		SimpleMdbDataRequester.callAction( _hmDataSection.get(EDataSection.DocsInWork).getMainEntityId(), 3057, params, new ICallbackEvent<Record[]>() {
			
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
																					Dialogs.ShowMessage(Captions.DOC_ID +" " +idDoc + " "+Captions.PUBLISHED );																					
																					callRequestData();
																				}
																			}
																		});																						
															} 	
															
														}
													});		
													
												}
											};
						
						
						
						if ( ECorrespondentType.fromInt( Integer.valueOf(docType).intValue() ) != ECorrespondentType.INSIDE_PRIKAZ) {
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
