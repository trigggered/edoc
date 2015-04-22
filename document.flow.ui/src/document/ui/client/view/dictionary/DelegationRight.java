/**
 * 
 */
package document.ui.client.view.dictionary;

import java.util.logging.Logger;

import mdb.core.ui.client.data.impl.ViewDataConverter;
import mdb.core.ui.client.events.ICallbackEvent;
import mdb.core.ui.client.events.IDataEditHandler;
import mdb.core.ui.client.view.data.grid.GridView;
import mdb.core.ui.client.view.data.grid.ListOfGrids;
import mdb.core.ui.client.view.data.grid.MasterDetailGridView;
import mdb.core.ui.client.view.dialogs.SelectDialog;

import com.smartgwt.client.data.Record;

import document.ui.client.commons.EViewIdent;
import document.ui.client.resources.locales.Captions;
import document.ui.shared.MdbEntityConst;

/**
 * @author azhuk
 * Creation date: Oct 13, 2014
 *
 */
public class DelegationRight extends MasterDetailGridView {
	
	private static final Logger _logger = Logger
			.getLogger(DelegationRight.class.getName());

	final static int MASTER_ENTITY_ID =  MdbEntityConst.DelegationHow;
	final static int DETAILE_ENTITY_ID =  MdbEntityConst.DelegationTo ;
	private int _whatDelegation=0;
	private String _relationField = "ID_HDELEG";
	

	private EViewIdent _ident;
	/**
	 * @param menuIdent
	 */
	public DelegationRight(EViewIdent ident) {
		_ident = ident;
		setMainEntityId(MASTER_ENTITY_ID);
		
		
		
		switch (_ident) {
		case SignatoryAssists:
			_whatDelegation = 1;
			setCaption(Captions.DELEGATION_SIGN);
			break;
		case ApprovalAssists:
			_whatDelegation = 2;
			setCaption(Captions.DELEGATION_APPROVAL);		
			
			break;
		}
		
		getParams().add("ID_DELEG", String.valueOf(_whatDelegation) );
	}

	@Override
	public GridView getMaster() {
		GridView toReturn =getListOfGrids().getGridMap().get(MASTER_ENTITY_ID);		
		return toReturn;
	}
	
	@Override
	public GridView getDetail() {
		
		
		GridView toReturn =getListOfGrids().getGridMap().get(DETAILE_ENTITY_ID );			
		return toReturn;
	}
	
	

	
	@Override
	protected void createGrids() {
		_listOfGrids = new ListOfGrids();
		
		_listOfGrids.addGrid(MASTER_ENTITY_ID, null, true);
		_listOfGrids.addGrid(DETAILE_ENTITY_ID, null, true);
	}
	
	@Override
	protected void createComponents() {
		super.createComponents();
		setSingleInstance(true);		
		
		
		getMaster().addInsertEvent(new IDataEditHandler() {				
			@Override
			public void onEdit(Record record) {
				SelectDialog.view(MdbEntityConst.EMP_LIST,true, new ICallbackEvent<Record[]>() {
					
					@Override
					public void doWork(Record[] data) {
				
						for (Record rec : data) {
							Record newRecord = new Record();	
							newRecord.setAttribute("OFFICER_NUM", rec.getAttribute("OFFICER_NUM"));		
							newRecord.setAttribute("ID_DELEG", _whatDelegation);		
							newRecord.setAttribute("FULL_NAME", rec.getAttribute("FULL_NAME"));		
							newRecord.setAttribute("JOB_NAME", rec.getAttribute("JOB_NAME"));														
							 
							getMaster().getListGrid().getDataSource().addData(newRecord);					
						}
					}
				});	 					
			}
		});
		
		
		getDetail().addInsertEvent(new IDataEditHandler() {				
			@Override
			public void onEdit(Record record) {
				SelectDialog.view(MdbEntityConst.EMP_LIST,true, new ICallbackEvent<Record[]>() {
					
					@Override
					public void doWork(Record[] data) {
				
						for (Record rec : data) {
							Record newRecord = new Record();
							String relId = getMaster().getSelectedRecord().getAttribute(_relationField);
							
							_logger.info(_relationField +"="+relId);
							newRecord.setAttribute(_relationField, relId);														
							newRecord.setAttribute("OFFICER_NUM", rec.getAttribute("OFFICER_NUM"));				
							newRecord.setAttribute("FULL_NAME", rec.getAttribute("FULL_NAME"));		
							newRecord.setAttribute("JOB_NAME", rec.getAttribute("JOB_NAME"));						
							
							
							_logger.info("New record = "+ ViewDataConverter.JavaScriptObject2Json(newRecord.getJsObj(),true));
							getDetail().getListGrid().getDataSource().addData(newRecord);					
						}
					}
				});	 					
			}
		});
	
	}
	

	/* (non-Javadoc)
	 * @see mdb.core.ui.client.view.data.grid.MasterDetailGridView#getRelationField()
	 */
	@Override
	protected String getRelationField() {
		return _relationField;
	}		
	
		
	
}
