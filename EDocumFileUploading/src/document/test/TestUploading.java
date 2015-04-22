/**
 * 
 */
package document.test;

import static org.junit.Assert.*;

import java.io.IOException;
import java.sql.SQLException;
import java.util.logging.Logger;

import org.junit.Test;

import document.uploading.enums.EDevisions;
import document.uploading.enums.EDocumentTypes;
import document.uploading.repository.db.DbHelper;
import document.uploading.repository.db.FileDbWriter;
import document.uploading.repository.filesystem.TreeFilesWalker;


/**
 * @author azhuk
 * Creation date: Aug 1, 2014
 *
 */
public class TestUploading {
	private static final Logger _logger = Logger.getLogger(TestUploading.class
			.getCanonicalName());

	//final String REPOSITORY_PATH = "/home/azhuk/Desktop/e-workflow & e-database/2014/Порядки/RB - Розничный бизнес";
	
	final String REPOSITORY_PATH = "/home/azhuk/Desktop/e-workflow & e-database";
	
	//final String REPOSITORY_PATH = "/home/azhuk/Desktop/e-workflow & e-database/2013/Уведомления/IT - ИТ";
	
	//final String REPOSITORY_PATH = "/home/azhuk/Desktop/e-workflow & e-database/2013/Порядки/PF - Потребительское кредитование";

	@Test
	public void testReadingFoldersTree() {
		
		try {
			TreeFilesWalker.main(new String[] {REPOSITORY_PATH,"3"});
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	public void testConnection() {
		assertNotNull(DbHelper.getConnection());
	}
	

	public void testSqls() {
		FileDbWriter writer = new FileDbWriter();
		long id = 0;
		try {
			id = writer.write(EDocumentTypes.Orders, EDevisions.IT, 2013, "asassasf","erer", null);
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			_logger.severe(e.getMessage());
		}
		assertTrue(id>0);
	}
	
	
	public void testExtractDocCodeFromFileName() {
		//String fileName = "ПОР-COLL-2-в1.doc";
		String fileName = "ПОР-COLL-2";
		
 		String[] arr =  fileName.split("-в");
 		
 		String docCode = arr[0];
 		
 		assertEquals("ПОР-COLL-2", docCode);
	}
	
	
}
