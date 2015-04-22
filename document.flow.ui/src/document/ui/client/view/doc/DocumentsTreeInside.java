/**
 * 
 */
package document.ui.client.view.doc;

import java.util.HashMap;
import java.util.logging.Logger;

import mdb.core.ui.client.view.components.menu.IMenuItem;
import mdb.core.ui.client.view.components.menu.Menu;
import mdb.core.ui.client.view.utils.DateTimeHelper;

import com.smartgwt.client.widgets.grid.ListGridRecord;

import document.ui.client.commons.EViewIdent;
import document.ui.shared.MdbEntityConst;

/**
 * @author azhuk
 * Creation date: Dec 27, 2013
 *
 */
public class DocumentsTreeInside extends DocumentsTreeInOut {
	
	private static final Logger _logger = Logger
			.getLogger(DocumentsTreeInside.class.getName());

	//разбивка по "Инициатор подразделение->тип->статус"
	private final int tree_init_dev_cortype_status = MdbEntityConst.tree_init_dev_cortype_status;
	//разбивка по "Тип->статус->Инициатору подразделения"
	private final int tree_cortype_status_init_dev = MdbEntityConst.tree_cortype_status_init_dev;
	//разбивка по "Статус->Инициатору подразделения->тип"
	private final int tree_status_init_dev_cortype = MdbEntityConst.tree_status_init_dev_cortype;
	
	
	/**
	 * @param viewIdent
	 */
	public DocumentsTreeInside(EViewIdent viewIdent) {
		super(viewIdent);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected int getBrokenTreeByCorrType() {
		return tree_cortype_status_init_dev;
	}
	
	
	@Override
	protected int getBrokenTreeByStatus() {
		return tree_status_init_dev_cortype;
	}
	
	protected int getBrokenTreeByInitialDevidion() {
		return tree_init_dev_cortype_status;
	}
	

	
	@SuppressWarnings("serial")
	@Override
	public void prepareRequestData() {
		_logger.info("CORR_TYPE="+_corrTypeCode+"\n dateFrom="+_dateB+"\n dateTo="+_dateE);
		getMaster().prepareRequestData(new HashMap<String,String>(){{
			put("CORR_CODE",_corrTypeCode.toString());
			put("dateFrom",DateTimeHelper.format(_dateB));
			put("dateTo",DateTimeHelper.format(_dateE) );
			}});		
		
	}
	
	@Override
	protected HashMap<String,String>  getDetaileReqParams(ListGridRecord masterSelectedData) {
		HashMap<String,String> params = super.getDetaileReqParams(masterSelectedData);
		String id_dept_author = masterSelectedData.getAttribute("ID_DEP_OWNER");	
		params.put("ID_DEP_OWNER", id_dept_author);
		_logger.info("ID_DEP_OWNER="+id_dept_author);
		return params;				
	}
	
	@Override
	protected Menu getMenu() {
		DocTreeMenu toReturn = new DocTreeMenu();
		IMenuItem item = toReturn.addItem("По «Подразделению»", "", IMenuItem.ItemType.ToolButton,2);
		item.setAttribute("entityId", String.valueOf(getBrokenTreeByInitialDevidion()));			
		item.setCommand(toReturn.getRefreshTreecommand());		 
		item.setPosition(2);
		return toReturn;
	}
}
