/**
 * 
 */
package document.ui.client.view.doc.card.section;

import java.util.Map;
import java.util.logging.Logger;

import mdb.core.shared.transport.IRequestData;
import mdb.core.shared.transport.RequestEntity;
import mdb.core.ui.client.app.AppController;
import mdb.core.ui.client.communication.IRemoteDataSave;
import mdb.core.ui.client.data.IDataComponent;
import mdb.core.ui.client.data.IDataSource;
import mdb.core.ui.client.data.bind.DataBindException;
import mdb.core.ui.client.view.BaseView;
import mdb.core.ui.client.view.components.menu.IMenuContainer;
import mdb.core.ui.client.view.data.DataView;
import mdb.core.ui.client.view.utils.DateTimeHelper;

import com.smartgwt.client.data.Record;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.FormItem;

import document.ui.client.commons.ECorrespondentType;
import document.ui.client.commons.EDocStatus;
import document.ui.client.view.doc.card.DocumentCard;
import document.ui.shared.MdbEntityConst;

/**
 * @author azhuk
 * Creation date: Jan 29, 2014
 *
 */
public class DataFieldsSection extends DataView implements IRemoteDataSave{
	
	private static final Logger _logger = Logger.getLogger(DataFieldsSection.class.getName());

	private int _viewNn = -1;
	
	/**
	 * @param _viewNn the _viewNn to set
	 */
	public void setViewNn(int value) {
		this._viewNn = value;
	}
	

	private class DynamicFieldsSection extends BaseView implements IDataComponent {

		private DynamicForm _dataForm;
		 IDataSource _dataSource; 
		 
		public DynamicFieldsSection() {
			super(EViewPanelType.VLayout);
		}
		
		public DynamicForm getDataForm() {			
			return _dataForm;
		}
		
		@Override
		protected void createComponents() {
			_dataForm = new DynamicForm();		
			_dataForm.setHeight100();;  
			_dataForm.setWidth100();  
			_dataForm.setPadding(5);
			
			//_dataForm.setFixedColWidths(true);
			//_dataForm.setLayoutAlign(VerticalAlignment.BOTTOM);
			//_dataForm.setAlign(Alignment.LEFT);
			//_dataForm.setColWidths("*","300");						
			
			addMember(_dataForm);
		}
		
		@Override
		public void setDataSource(IDataSource value ) {
			setDataSource(value,true);
		}

		/* (non-Javadoc)
		 * @see mdb.core.ui.client.data.IDataComponent#getDataSource()
		 */
		@Override
		public IDataSource getDataSource() {
			return _dataSource;
		}

		/* (non-Javadoc)
		 * @see mdb.core.ui.client.data.IDataComponent#setDataSource(mdb.core.ui.client.data.IDataSource, boolean)
		 */
		@Override
		public void setDataSource(IDataSource value, boolean isCanEdit) {
			if (value != null) {
				_dataSource = value;
				value.setDataComponent(this);
				if (isCanEdit) {
					value.bindToDataComponent(getDataForm());
				}else {
					value.bindToDataComponent(getDataForm(), false);
				}
			}
			
		}

		/* (non-Javadoc)
		 * @see mdb.core.ui.client.data.IDataComponent#setFieldValue(java.lang.String, java.lang.String)
		 */
		@Override
		public void setFieldValue(String fieldName, String value) {
			FormItem item = getDataForm().getField(fieldName);	
			if (item != null) {
				item.setValue(value);
			}			
		}
		
		@Override
		public void setCanEdit(boolean value) {
			super.setCanEdit(value);
			_dataForm.setCanEdit(value);
			/*
			for (FormItem item  : _dataForm.getFields()) {
				item.setCanEdit(value);
			}*/			
		}		
	};
	
	//
		
		
	private DynamicFieldsSection _editDialog;
	private DocumentCard _mainDocCard;
	
		
	public DataFieldsSection(DocumentCard mainDocCard) {
		_mainDocCard = mainDocCard;			
		setMainEntityId(MdbEntityConst.DocCard);
	}	
	
	
	@Override
	protected IMenuContainer createMenuContainer () {
		return null;
	}			
	

	@Override
	protected  void createComponents() {
		super.createComponents();		
		
		_editDialog = new DynamicFieldsSection();
		//setShowEdges(true);		
		getViewPanel().addMember(_editDialog);
	}
	
	
	/* (non-Javadoc)
	 * @see mdb.core.ui.client.view.data.IDataView#getSelectedRecord()
	 */
	@Override
	public Record getSelectedRecord() {
		return _editDialog.getDataForm().getValuesAsRecord();		
	}
	
	@Override
	public Record[] getSelectedRecords() {
		Record[] toReturn = new Record[1];
		toReturn[0] = _editDialog.getDataForm().getValuesAsRecord();
		return toReturn;
	}

	/* (non-Javadoc)
	 * @see mdb.core.ui.client.view.data.IDataView#isSelectedRecord()
	 */
	@Override
	public boolean isSelectedRecord() {
		// TODO Auto-generated method stub
		return false;
	}


	/* (non-Javadoc)
	 * @see mdb.core.ui.client.view.data.DataView#prepareRequestData()
	 */
	@Override
	public void prepareRequestData() {
		_logger.info("################ Start prepareRequestData  for Fields Section ######################");
		getDataBinder().getDataProvider().getRequest().setPosition(1);
		IRequestData re = getDataBinder().getDataProvider().getRequest().add(new RequestEntity(getMainEntityId()));
		long docId = _mainDocCard.getDocumentId();
		_logger.info("Put parameter: ID_DOC= "+docId);
		re.getParams().add("ID_DOC",  String.valueOf(_mainDocCard.getDocumentId()));	
		re.getParams().add("CORRT_ROOT_CODE", _mainDocCard.getCorrespondentTypeRootCode().toString());
		
		if (_viewNn == -1) {
		
			switch (_mainDocCard.getCorrespondentTypeRootCode()) {
						case INSIDE_PRIKAZ_DOC: 	_viewNn = 3;
							break;
						case INSIDE_PROCEDURE_DOC : 	_viewNn = 4;
							break;
						case INSIDE_NOTIFICATION_DOC: _viewNn = 5;
							break;
						default:
							_viewNn =1;
							break;
			}					
		} 
		re.getParams().add("VIEW_NN", String.valueOf( _viewNn ));
		re.setExecuteType(mdb.core.shared.transport.IRequestData.ExecuteType.GetData);				
	}

	
	/* (non-Javadoc)
	 * @see mdb.core.ui.client.view.data.DataView#bindDataComponents()
	 */
	@Override
	public void bindDataComponents() throws DataBindException {
		IDataSource ds =  getMainDataSource();
		ds.setLocalKeyGen(false);		
		_editDialog.setDataSource(ds);
		
		
		switch (_mainDocCard.getViewState())  {
			case  New:
				
				Record newRecord = new Record();
				
				//if (getMainEntityId() == MAIN_ENTITY_ID) {
									
						switch (_mainDocCard.getCorrespondentTypeRootCode()) {
							case INPUT_DOC:
							case OUTPUT_DOC:
								newRecord.setAttribute("ID_STATUS", 1);
								break;
							case INSIDE_PRIKAZ_DOC:
								newRecord.setAttribute("ID_STATUS", EDocStatus.Draft.getValue());
								newRecord.setAttribute("ID_CORR", ECorrespondentType.INSIDE_PRIKAZ_DOC.getValue());
								break;
							case INSIDE_PROCEDURE_DOC:
								newRecord.setAttribute("ID_CORR", ECorrespondentType.INSIDE_PROCEDURE_DOC.getValue());
								newRecord.setAttribute("ID_STATUS", EDocStatus.Draft.getValue());								
								newRecord.setAttribute("CORR_TYPE_FULL", "Порядок");
								newRecord.setAttribute("ORD_VERSION", 1);
								newRecord.setAttribute("ORD_LEVEL", 3);
								//newRecord.setAttribute("DATE_SH_UPD", DateTimeHelper.addMonths(DateTimeHelper.getSysdate(), 35));
								
								
								break;
							case INSIDE_NOTIFICATION_DOC:
								newRecord.setAttribute("ID_STATUS", EDocStatus.Draft.getValue());
								newRecord.setAttribute("ID_CORR", ECorrespondentType.INSIDE_NOTIFICATION_DOC.getValue());
								break;
						case UNKNOWN:
							break;
						default:
							break;
						}
					
						
						//newRecord.setAttribute("DATE_PUB", DateTimeHelper.getSysdate());
						//newRecord.setAttribute("DATE_EFFECTIVE", DateTimeHelper.getSysdate());
						
						
						newRecord.setAttribute("ID_DOC", _mainDocCard.getDocumentId());
						newRecord.setAttribute("DATE_IN", DateTimeHelper.getSysdate() );						
						newRecord.setAttribute("DATE_EFFECTIVE", DateTimeHelper.getSysdate() );
						
						newRecord.setAttribute("ID_EMP_REG", AppController.getInstance().getCurrentUser().getId());
						newRecord.setAttribute("REG_FULL_NAME", AppController.getInstance().getCurrentUser().getName());
						newRecord.setAttribute("REG_DEP_NAME", AppController.getInstance().getCurrentUser().getDevisionName());				
						
						newRecord.setAttribute("ID_EMP_AUTHOR", AppController.getInstance().getCurrentUser().getId());
						newRecord.setAttribute("EMP_AUTHOR", AppController.getInstance().getCurrentUser().getName());
						newRecord.setAttribute("AUTHOR_DEP_NAME", AppController.getInstance().getCurrentUser().getDevisionName());				
						
						newRecord.setAttribute("IS_CLOSED", 0);
						newRecord.setAttribute("ID_INFO_TYPE", 1);
						
						
						
				/*}
				else if (getMainEntityId() != MAIN_ENTITY_ID) {
					newRecord.setAttribute("ORD_VERSION", 1);
				}*/
				
				
				_editDialog.getDataForm().editNewRecord(newRecord);			
				
				break;
			default:				
				
				break;
				
		}		
		//_editDialog.getDataForm().validate();
	}

	/* (non-Javadoc)
	 * @see mdb.core.ui.client.view.BaseView#createMenu()
	 */
	@Override
	protected void createMenu() {
		// TODO Auto-generated method stub
	}

	/**
	 * 
	 */
	@Override
	public void save() {
		
	}
	
	
	public void loseChange() {
		_editDialog.getDataForm().resetValues();
		_editDialog.getDataSource().getRequestEntity().loseChanges();
	}

	/* (non-Javadoc)
	 * @see mdb.core.ui.client.communication.IRemoteDataSave#isValidate()
	 */
	@Override
	public boolean isValidate() {
		return _editDialog.getDataForm().validate();		
	}

	/* (non-Javadoc)
	 * @see mdb.core.ui.client.communication.IRemoteDataSave#prepareSavedData()
	 */
	@Override
	public void prepareSavedData() {			  
		
		FormItem item = _editDialog.getDataForm().getItem("ID_DOC");
		if (item != null) {
			Object val = item.getValue();
			if (val == null) {
				item.setValue(_mainDocCard.getDocumentId());
			}
		}
		
		if ( isHaseChanges() ) {
			  _editDialog.getDataForm().saveData();
		}
	}

	@Override
	public boolean isHaseChanges() {		
		@SuppressWarnings("rawtypes")
		Map changetValues = _editDialog.getDataForm().getChangedValues();
		return changetValues == null || changetValues.size() >0;
	}
	
	/* (non-Javadoc)
	 * @see mdb.core.ui.client.communication.IRemoteDataSave#putSavedDataToQueue()
	 */
	@Override
	public void putSavedDataToQueue() {		
		putRequestToQueue();		
	}


	/**
	 * @param string
	 * @return
	 */
	public String getFieldValue(String fieldName) {		
		Record rec = getSelectedRecord();		
		String toReturn = rec.getAttributeAsString(fieldName);		
		
		return toReturn;
	}


	/**
	 * @param string
	 * @param valueOf
	 */
	public void setFieldValue(String fieldName, String value) {		
		FormItem item = _editDialog.getDataForm().getItem(fieldName);
		if (item != null) {
			item.setValue(value);			
		}			
	}
	
	@Override
	public void setCanEdit(boolean value) {
		_editDialog.setCanEdit(value);
		
	}

}
