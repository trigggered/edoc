/**
 * 
 */
package document.ui.client.communication.files;

import mdb.core.ui.client.view.fileupload.FileUploadComponent;
import mdb.core.ui.client.view.fileupload.MultiFileUploadWidget;

/**
 * @author azhuk
 * Creation date: Sep 5, 2014
 *
 */
public class MultiDocumentAttachUploadWidget  extends MultiFileUploadWidget{
	
	
	public FileUploadComponent addFileForUpload (long idDoc, String idStor) {
		DocumentAttachUploadComponet component = new  DocumentAttachUploadComponet(idDoc, idStor);
		
		setUploadComponent(component);
		return component;
	}	

	
}
