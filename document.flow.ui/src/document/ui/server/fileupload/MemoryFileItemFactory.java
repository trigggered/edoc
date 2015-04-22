/**
 * 
 */
package document.ui.server.fileupload;

import java.util.logging.Logger;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;

/**
 * @author azhuk
 * Creation date: Aug 7, 2013
 *
 */
public class MemoryFileItemFactory implements FileItemFactory{
	private static final Logger _logger = Logger
			.getLogger(MemoryFileItemFactory.class.getName());

	/**
     * The default threshold above which uploads will be stored on disk.
     */
    public static final int DEFAULT_SIZE_THRESHOLD = 10240;
    
	/**
     * The threshold above which uploads will be stored on disk.
     */
    private int sizeThreshold = DEFAULT_SIZE_THRESHOLD;
    
    
	/* (non-Javadoc)
	 * @see org.apache.commons.fileupload.FileItemFactory#createItem(java.lang.String, java.lang.String, boolean, java.lang.String)
	 */
	@Override
	public FileItem createItem(String fieldName, String contentType,
			boolean isFormField, String fileName) {
		
		MemoryFileItem result = new MemoryFileItem(fieldName, contentType,
                isFormField, fileName, sizeThreshold,null);
		_logger.info("Create MemoryFileItem:" +fileName+" "+fieldName+" "+ contentType);
		return result;
	}
}
