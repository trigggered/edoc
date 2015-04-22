/**
 * 
 */
package document.ui.client.communication.files;

import java.util.HashMap;
import java.util.logging.Logger;

import mdb.core.ui.client.app.AppController;
import mdb.core.ui.client.view.fileupload.FileUploadComponent;

import com.google.gwt.user.client.ui.Hidden;

/**
 * @author azhuk
 * Creation date: Sep 5, 2014
 *
 */
public class DocumentAttachUploadComponet extends FileUploadComponent {
	@SuppressWarnings("unused")
	private static final Logger _logger = Logger
			.getLogger(DocumentAttachUploadComponet.class.getName());
	
	
	HashMap<String, Hidden> _hidenFieldsMap; 
	
	public DocumentAttachUploadComponet (long idDoc, String idStor) {
		_hidenFieldsMap =  new HashMap<String, Hidden>();
		
		Hidden docId = new Hidden("ID_DOC", String.valueOf( idDoc) );
		Hidden storId = new Hidden("ID_STOR", idStor);
		Hidden auhorId = new Hidden("ATTACH_AUTHOR", String.valueOf(AppController.getInstance().getCurrentUser().getId()) );
		

		
		_hidenFieldsMap.put(docId.getName(), docId);
		_hidenFieldsMap.put(storId.getName(), storId);
		_hidenFieldsMap.put(auhorId.getName(), auhorId);

		
		for (Hidden item : _hidenFieldsMap.values()) {
			getFormItemsPanel().add(item);			
		}
		
	}			
	  
	
	/**
	 * @return the _hidenFieldsMap
	 */
	public HashMap<String, Hidden> getHidenFieldsMap() {
		return _hidenFieldsMap;
	}			
	
}
