/**
 * 
 */
package document.ui.client.view.dictionary;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.logging.Logger;

import mdb.core.ui.client.command.ICommand;
import mdb.core.ui.client.view.components.menu.IMenuButtons;
import mdb.core.ui.client.view.components.menu.IMenuItem;
import mdb.core.ui.client.view.components.menu.data.AMenuData;
import mdb.core.ui.client.view.utils.DateTimeHelper;
import document.ui.client.commons.EViewIdent;
import document.ui.client.resources.locales.Captions;

/**
 * @author azhuk
 * Creation date: Dec 2, 2014
 *
 */
public class DicGrViewDateSelect extends DictionaryGridView {
	
	/**
	 * @param viewIdent
	 */
	public DicGrViewDateSelect(EViewIdent viewIdent) {
		super(viewIdent);
		// TODO Auto-generated constructor stub
	}

	private static final Logger _logger = Logger
			.getLogger(DicGrViewDateSelect.class.getName());
	
	protected Date _dateB;
	protected Date _dateE;
	
	class MenuDateSelect extends mdb.core.ui.client.view.components.menu.Menu {
		
	
		public MenuDateSelect() {
			super("MenuDateSelect");
			
			_dateB = DateTimeHelper.getFirstDayOfYear();			
			_dateE = DateTimeHelper.getLasttDayOfYear();
						
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
		}
	
	}
	
	@Override
	protected void createMenu() {
		
		getMenuContainer().bind(new MenuDateSelect());
		super.createMenu();
	}
	
	
	@Override
	public void prepareRequestData() {
		
		getParams().add("dateFrom",DateTimeHelper.format(_dateB));
		getParams().add("dateTo",DateTimeHelper.format(_dateE) );
		super.prepareRequestData();
	}

}
