/**
 * 
 */
package document.ui.client.view.dictionary.ent;

import mdb.core.ui.client.app.AppController;
import mdb.core.ui.client.events.ICallbackEvent;
import mdb.core.ui.client.events.IDataEditHandler;
import mdb.core.ui.client.view.data.grid.GridView;
import mdb.core.ui.client.view.data.grid.ListOfGrids;
import mdb.core.ui.client.view.data.grid.MasterDetailGridView;
import mdb.core.ui.client.view.dialogs.edit.EditDialog;
import mdb.core.ui.client.view.dialogs.select.MultiStepSelectDialog;

import com.smartgwt.client.data.Record;
import com.smartgwt.client.util.BooleanCallback;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.grid.ListGrid;

import document.ui.client.commons.EViewIdent;
import document.ui.client.commons.checker.CheckDocumentUserRight;
import document.ui.client.resources.locales.Captions;
import document.ui.shared.MdbEntityConst;

/**
 * @author azhuk
 * Creation date: Apr 22, 2015
 *
 */
public class DicFavEmployeeGr extends MasterDetailGridView {
	//private static final Logger _logger = Logger.getLogger(DicFavEmployeeGr.class.getName());

	private EViewIdent _viewIdent;	
	private final String RELATION_FIELD= "ID_FAV_GR_EMP";
		
	public DicFavEmployeeGr(	EViewIdent viewIdent	) {
		_viewIdent = viewIdent;		
		setMainEntityId(MdbEntityConst.DIC_FAV_GR_EMP );
	}
	
	
	/* (non-Javadoc)
	 * @see mdb.core.ui.client.view.data.grid.MasterDetailGridView#getMaster()
	 */
	@Override
	public GridView getMaster() {
		GridView toReturn =getListOfGrids().getGridMap().get(MdbEntityConst.DIC_FAV_GR_EMP);		
		return toReturn;
	}

	/* (non-Javadoc)
	 * @see mdb.core.ui.client.view.data.grid.MasterDetailGridView#getDetail()
	 */
	@Override
	public GridView getDetail() {

		GridView toReturn =getListOfGrids().getGridMap().get(MdbEntityConst.DIC_FAV_EMP );			
		return toReturn;
	}

	/* (non-Javadoc)
	 * @see mdb.core.ui.client.view.data.grid.MasterDetailGridView#createGrids()
	 */
	@Override
	protected void createGrids() {
		_listOfGrids = new ListOfGrids();
		
		_listOfGrids.addGrid(MdbEntityConst.DIC_FAV_GR_EMP, null, true);
		_listOfGrids.addGrid(MdbEntityConst.DIC_FAV_EMP , null, true);
		
	}
	/* (non-Javadoc)
	 * @see mdb.core.ui.client.view.data.grid.MasterDetailGridView#getRelationField()
	 */
	@Override
	protected String getRelationField() {
		return RELATION_FIELD;
	}

	@Override
	protected void createComponents() {
		super.createComponents();
		setSingleInstance(true);		
		final boolean isBa = CheckDocumentUserRight.isHasBARole();
		
		getMaster().getParams( ).add("CURRENT_USER", String.valueOf(AppController.getInstance().getCurrentUser().getId()) );
		getMaster().getParams( ).add("IS_BA", isBa?"1":"");				
		//getParams().add("CURRENT_USER", String.valueOf(AppController.getInstance().getCurrentUser().getId()) );
		
		getMaster().addInsertEvent(new IDataEditHandler() {
			
			@Override
			public void onEdit(Record record) {				
				Record initRecord =  new Record();								
				if (_viewIdent == EViewIdent.DicGrEmp ) {					
					initRecord.setAttribute("OWN_OFFICER_NUM", 
							AppController.getInstance().getCurrentUser().getId());
					initRecord.setAttribute("OWNER_NAME",AppController.getInstance().getCurrentUser().getName());
				}
				else {
					initRecord.setAttribute("OWNER_NAME","Всем");
				}
				EditDialog.viewForNewRecord(getMaster().getMainDataSource(), initRecord,null);					
			}
		});
		
		
		getMaster().addDeleteEvent(new IDataEditHandler() {
			
			@Override
			public void onEdit(Record record) {
			
				if (isBa) {
					deleteMaster();
				}else {
					int id = AppController.getInstance().getCurrentUser().getId();
					String ownerId = record.getAttribute("OWN_OFFICER_NUM");
					
					if (ownerId!= null && ownerId.equals(String.valueOf(id) )) {
						deleteMaster();
					}
				
				}
			}
		});
		
		
		
		getDetail().addInsertEvent(new IDataEditHandler() {				
			@Override
			public void onEdit(Record record) {
				
				if (_viewIdent == EViewIdent.DicGrEmp &&  
						getSelectedRecord().getAttributeAsInt(getRelationField()) ==1 ) {									
					return;
				}
				
				
				MultiStepSelectDialog.view(MdbEntityConst.AddDocRecipients,true, new ICallbackEvent<Record[]>() {
					
					@Override
					public void doWork(Record[] data) {
				
						for (Record rec : data) {
							Record newRecord = new Record();	
										
							newRecord.setAttribute("VALUE_ID", rec.getAttribute("VALUE_ID"));
							newRecord.setAttribute("ID_GR", rec.getAttribute("ID_GR"));
							newRecord.setAttribute("NAME", rec.getAttribute("NAME"));
							
							String relId = getMaster().getSelectedRecord().getAttribute(getRelationField());							
							newRecord.setAttribute(getRelationField(), relId );													 											
							
							getDetail().getListGrid().getDataSource().addData(newRecord);					
						}
					}
				});	 					
			}
		});			
	}
	
	
	
	public void deleteMaster() {	
		
		if (getMaster().isSelectedRecord()) {		
			
			
			
			
			SC.ask( Captions.Q_DELETE_REC, new BooleanCallback() {
				
				@Override
				public void execute(Boolean value) {
					if (value) {									 						 						
						for  (Record rec : getMaster().getSelectedRecords()) {
							getMaster().removeRecord(rec);
							getMaster().callAfterDeleteEvent();
						}								
					}				
				}
			});				
		}		
	}
	
	
	@Override
	public String getCaption() {
		return Captions.DIC_FAV_GR_EMP;		
	}
	
	@Override
	public  ListGrid getListGrid() {
		return getDetail().getListGrid();		
	}
	
	
}
