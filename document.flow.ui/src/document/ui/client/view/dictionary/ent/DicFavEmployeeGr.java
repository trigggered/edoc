/**
 * 
 */
package document.ui.client.view.dictionary.ent;

import java.util.logging.Logger;

import mdb.core.ui.client.events.ICallbackEvent;
import mdb.core.ui.client.view.data.grid.GridView;
import mdb.core.ui.client.events.IDataEditHandler;
import mdb.core.ui.client.view.data.grid.ListOfGrids;
import mdb.core.ui.client.view.data.grid.MasterDetailGridView;

import com.smartgwt.client.data.Record;

import document.ui.client.view.dialogs.EmpSelectDlg;
import document.ui.shared.MdbEntityConst;

/**
 * @author azhuk
 * Creation date: Apr 22, 2015
 *
 */
public class DicFavEmployeeGr extends MasterDetailGridView {
	private static final Logger _logger = Logger
			.getLogger(DicFavEmployeeGr.class.getName());

	private final String RELATION_FIELD= "ID_FAV_GR_EMP";
	
	public DicFavEmployeeGr() {
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
		
		
		getDetail().addInsertEvent(new IDataEditHandler() {				
			@Override
			public void onEdit(Record record) {
				EmpSelectDlg.view(MdbEntityConst.AddDocRecipients,true, new ICallbackEvent<Record[]>() {
					
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

	
}
