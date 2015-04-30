/**
 * 
 */
package document.ui.client.view.dialogs;

import com.smartgwt.client.data.Record;

import document.ui.client.resources.locales.Captions;
import mdb.core.ui.client.command.ICommand;
import mdb.core.ui.client.data.IDataNavigator;
import mdb.core.ui.client.data.IDataSource;
import mdb.core.ui.client.events.ICallbackEvent;
import mdb.core.ui.client.view.components.menu.IMenu;
import mdb.core.ui.client.view.components.menu.IMenuContainer;
import mdb.core.ui.client.view.components.menu.IMenuItem;
import mdb.core.ui.client.view.components.menu.data.MenuDataNavigator;
import mdb.core.ui.client.view.components.menu.data.MenuDataPaging;
import mdb.core.ui.client.view.data.IDataView;
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
	
	class MenuFiltered extends MenuDataNavigator {
		
		/**
		 * @param name
		 * @param dataSource
		 * @param view
		 */
		public MenuFiltered(String name, IDataSource dataSource, IDataView view) {
			super(name, dataSource, view);
		}

		protected void createDefaultButtons() {
			setShowCaption(false);
			setShowHint(true);			
			createButton(IDataNavigator.Buttons.dataFilter);						
		}	
	}

	class EmpSelectedGrid  extends GridView {

		@Override
		protected void bindMenuDataNavigator(int entityId) { 		
			
			 IMenuContainer container =  getMenuContainer();
			 IDataSource ds = getDataSource(entityId);				
					
					
			IMenu menuNavigation = new MenuFiltered("MenuDataNavigator", ds,this);
			menuNavigation.setPosition(1);
			container.bind(menuNavigation);											
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
