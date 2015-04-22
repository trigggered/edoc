package document.ui.client.view.main;

import document.ui.client.commons.EViewIdent;
import document.ui.client.view.ViewFactory;
import mdb.core.ui.client.view.IView;
import mdb.core.ui.client.view.components.menu.mdb.IMdbMainMenuAction;

public class MdbMainMenuAction implements IMdbMainMenuAction {

	@Override
	public IView getView(String menuAction) {
		EViewIdent viewMenuIdent  = EViewIdent.valueOf(menuAction);		
		IView view = ViewFactory.create(viewMenuIdent);				
		return view;
	}
}
