/**
 * 
 */
package document.uploading.repository.filesystem;


import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import document.uploading.enums.EDevisions;
import document.uploading.enums.EDocumentTypes;
import document.uploading.repository.db.FileDbWriter;

/**
 * @author azhuk
 * Creation date: Aug 1, 2014
 *
 */
public class FileProcessor {
	private static final Logger _logger = Logger.getLogger(FileProcessor.class
			.getName());
	
	Map<String,Long> _mapCodeIdDoc = new HashMap<String,Long>();
	
	String _year;
	EDocumentTypes _documentTypes;
	EDevisions _devisions;
	String _correntDir;
	
	FileDbWriter _writer = new FileDbWriter();

	private boolean _processedAdditions;
	/**
	 * @return the _year
	 */
	public String getYear() {
		return _year;
	}


	/**
	 * @return the _documentTypes
	 */
	public EDocumentTypes getDocumentTypes() {
		return _documentTypes;
	}


	/**
	 * @return the _devisions
	 */
	public EDevisions getDevisions() {
		return _devisions;
	}


	/**
	 * @param _year the _year to set
	 */
	public void setYear(String value) {
		this._year = value;
	}


	/**
	 * @param _documentTypes the _documentTypes to set
	 */
	public void setDocumentTypes(EDocumentTypes value) {
		this._documentTypes = value;
	}


	/**
	 * @param _devisions the _devisions to set
	 */
	public void setDevisions(EDevisions value) {
		this._devisions = value;
	}

	
	/**
	 * @param file
	 */
	public void process(Path file) {
		byte[] data = null;
		try {
			data = Files.readAllBytes(file);	
	
			
			String fileName = file.getFileName().toString();										
								
			
			if (_processedAdditions) {
				//String fn = getFileNameWithoutExtension(fileName);				
				
				String docCode = file.getName(file.getNameCount()-2).toString();
				Long docId = Long.valueOf(0);
				
				if (_mapCodeIdDoc.containsKey(docCode)) {					
					docId = _mapCodeIdDoc.get(docCode);
					_logger.info("For doc code"+docCode+ " Doc ID is "+docId);										
				}
				
				_writer.writeAdditions (  docId.longValue(), fileName, docCode,  data);
				_writer.commitAndClose();
			}
			else {
				String docCode = extractDocCodeFromFileName(fileName);
				
				long id = _writer.write(getDocumentTypes(), getDevisions(),				
						Integer.valueOf(getYear()).intValue(), fileName, docCode, data);
			
					_mapCodeIdDoc.put(docCode, Long.valueOf(id));
				
			}		
			
		} catch (IOException e) {
			_logger.severe(e.getMessage());
			
		} catch (NumberFormatException e) {
			_logger.severe(e.getMessage());
		} 
		catch (SQLException e) {
			_logger.severe(e.getMessage());
		}
		
	}	
	
	public static String getFileNameWithoutExtension(String fileName) {	
		  
		  int index = fileName.lastIndexOf('.');
		      if (index>0&& index <= fileName.length() - 2 ) {
		    	  return fileName.substring(0, index);
		      }  
		    return fileName;
	}
	
	
	private String extractDocCodeFromFileName(String fileName) {
		String fn = getFileNameWithoutExtension(fileName);
		return fn;
		/*if (getDocumentTypes() != EDocumentTypes.Orders ) {
			String[] arr =  fn.split("-в"); 		
	 		return arr[0];	
		} else return fn;
		*/
	}
	
	


	/**
	 * @param b
	 */
	public void setProcessedAdditions(boolean value) {
		_processedAdditions = value;
		
	}
	

	
}
