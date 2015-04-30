/**
 * 
 */
package document.ui.client.view.doc;

import java.util.HashMap;
import java.util.logging.Logger;

import mdb.core.ui.client.view.components.menu.IMenuItem;
import mdb.core.ui.client.view.components.menu.Menu;

import com.smartgwt.client.widgets.grid.ListGridRecord;

import document.ui.client.commons.EViewIdent;
import document.ui.shared.MdbEntityConst;

/**
 * @author azhuk
 * Creation date: Jun 2, 2015
 *
 */
public class DocumentsTreeInsideFin extends DocumentsTreeInside {
	/**
	 * @param viewIdent
	 */
	

	public DocumentsTreeInsideFin(EViewIdent viewIdent) {
		super(viewIdent);
		// TODO Auto-generated constructor stub
	}

	private static final Logger _logger = Logger
			.getLogger(DocumentsTreeInsideFin.class.getName());
	
	
	protected int getBrokenTreeByCorrType() {
		return MdbEntityConst.fin_opert_status;
	}
	
	protected int getBrokenTreeByInitialDevidion() {
		return MdbEntityConst.fin_status_opert;
	}

	@Override
	protected Menu getMenu() {
		DocTreeMenu toReturn = new DocTreeMenu();
		IMenuItem item = toReturn.addItem("По «Статусу»", "", IMenuItem.ItemType.ToolButton,2);
		item.setAttribute("entityId", String.valueOf(getBrokenTreeByInitialDevidion()));			
		item.setCommand(toReturn.getRefreshTreecommand());		 
		item.setPosition(2);
		return toReturn;
	}
	
	@Override
	protected HashMap<String,String>  getDetaileReqParams(ListGridRecord masterSelectedData) {
		HashMap<String,String> params = super.getDetaileReqParams(masterSelectedData);
		String id_fin_opert = masterSelectedData.getAttribute("ID_FIN_OPERT");	
		params.put("ID_FIN_OPERT", id_fin_opert);
		_logger.info("ID_FIN_OPERT="+id_fin_opert);
		return params;			
	}
	
}
