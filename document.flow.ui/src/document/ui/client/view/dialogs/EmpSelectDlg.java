/**
 * 
 */
package document.ui.client.view.dialogs;

import java.util.logging.Logger;

import com.smartgwt.client.data.Record;

import document.ui.client.resources.locales.Captions;
import mdb.core.ui.client.command.ICommand;
import mdb.core.ui.client.events.ICallbackEvent;
import mdb.core.ui.client.view.components.menu.IMenu;
import mdb.core.ui.client.view.components.menu.IMenuItem;
import mdb.core.ui.client.view.data.IListDataView;
import mdb.core.ui.client.view.data.grid.GridView;
import mdb.core.ui.client.view.dialogs.SelectDialog;
import mdb.core.ui.client.view.dialogs.select.MultiStepSelectDialog;

/**
 * @author azhuk
 * Creation date: Apr 23, 2015
 *
 */
public class EmpSelectDlg extends MultiStepSelectDialog{
	
	private static final Logger _logger = Logger.getLogger(EmpSelectDlg.class.getName());
	
	class EmpSelectedGrid  extends GridView {
		/* (non-Javadoc)
		 * @see mdb.core.ui.client.view.data.AListView#createMenu()
		 */
		@Override
		protected void createMenu() {			
			super.createMenu();
			
			class  MenuFile extends mdb.core.ui.client.view.components.menu.Menu {
				public MenuFile() {
					super("MenuFile");
					
					IMenuItem item = addItem(Captions.DIC_FAV_GR_EMP, "", IMenuItem.ItemType.Menu,0);
					IMenu childMenu = new mdb.core.ui.client.view.components.menu.Menu("Child menu for 'File'");
					item.setChildMenu(childMenu);
					
					item = childMenu.addItem(Captions.Dictionary, "", IMenuItem.ItemType.ToolButton,0);
					item.setCommand(new ICommand<IMenuItem>() {
						
						@Override
						public void execute(IMenuItem sender) {
							EmpFromGrSelectDlg.view(true,getSelf().getCallbackEvent());							
						}
					});
				}				
			}			
			getMenuContainer().bind(new MenuFile());
		} 
	}
	
	/**
	 * @param entityId
	 * @param isCanMultiSelect
	 * @param callbackEvent
	 */
	public EmpSelectDlg(int entityId, boolean isCanMultiSelect,
			ICallbackEvent<Record[]> callbackEvent) {
		super(entityId, isCanMultiSelect, callbackEvent);
		
	}

	public static void view (int entityId, boolean isCanMultiSelect, ICallbackEvent<Record[]>  callbackEvent) {
		
		SelectDialog dlg = new EmpSelectDlg(entityId, isCanMultiSelect, callbackEvent);
		dlg.callRequestData();
		
		dlg.show();	
		
	}
	
	@Override
	protected IListDataView   createListDataView() {
		return new  EmpSelectedGrid();
		
	}
	
	private EmpSelectDlg getSelf() {
		return this;
	}
	
	
}
