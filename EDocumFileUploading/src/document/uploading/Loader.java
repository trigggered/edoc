/**
 * 
 */
package document.uploading;

import java.io.IOException;
import java.util.logging.Logger;

import document.uploading.repository.filesystem.TreeFilesWalker;

/**
 * @author azhuk
 * Creation date: Aug 1, 2014
 *
 */
public class Loader {
	private static final Logger _logger = Logger.getLogger(Loader.class
			.getName());
	
	final static String REPOSITORY_PATH = "/home/azhuk/Desktop/e-workflow & e-database/2013/Уведомления/PF - Потребительское кредитование/УВЕД-PF-2013-9";
	
	public  static void main(String[] args){
		
		try {
		if (args.length >0) {
		
				TreeFilesWalker.main(new String[] {REPOSITORY_PATH,"3"});			
		}
		else {
			TreeFilesWalker.main(args);
		}
		
		} catch (IOException e) {
			_logger.severe(e.getMessage());
		}
	}
}
