/**
 * 
 */
package document.ui.client.view.doc.search;

import mdb.core.ui.client.events.ICallbackEvent;
import mdb.core.ui.client.view.dialogs.input.InputValueDialog;
import document.ui.client.resources.locales.Captions;
import document.ui.client.view.doc.card.DocumentCard;

/**
 * @author azhuk
 * Creation date: May 13, 2014
 *
 */
public class Search {
	
	public static void searchDocById() {
		InputValueDialog dlg = new InputValueDialog(new ICallbackEvent<String>() {			
			@Override
			public void doWork(final String data) {				
				DocumentCard.OpenById(data);				
			}
		});						
		
		dlg.show(Captions.SEARCH, Captions.ENTER_DOC_ID);	
	}
	
}
