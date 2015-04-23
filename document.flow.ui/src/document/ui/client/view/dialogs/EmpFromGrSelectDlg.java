/**
 * 
 */
package document.ui.client.view.dialogs;


import mdb.core.ui.client.events.ICallbackEvent;
import mdb.core.ui.client.view.data.IListDataView;
import mdb.core.ui.client.view.dialogs.SelectDialog;
import mdb.core.ui.client.view.dialogs.select.MultiStepSelectDialog;

import com.smartgwt.client.data.Record;

import document.ui.client.commons.EViewIdent;
import document.ui.client.view.dictionary.ent.DicFavEmployeeGr;

/**
 * @author azhuk
 * Creation date: Apr 23, 2015
 *
 */
public class EmpFromGrSelectDlg extends MultiStepSelectDialog {
	
	/**
	 * @param entityId
	 * @param isCanMultiSelect
	 * @param callbackEvent
	 */
	public EmpFromGrSelectDlg (boolean isCanMultiSelect,
			ICallbackEvent<Record[]> callbackEvent) {
		super(-1, isCanMultiSelect, callbackEvent);
	}
		

	public static void view (boolean isCanMultiSelect, ICallbackEvent<Record[]>  callbackEvent) {
		
		SelectDialog dlg = new EmpFromGrSelectDlg(isCanMultiSelect, callbackEvent);
		dlg.callRequestData();
		
		dlg.show();		
	}
	
	
	protected IListDataView   createListDataView() {
		return new DicFavEmployeeGr(EViewIdent.DicGrEmp);
	}
	
}
