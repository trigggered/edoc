/**
 * 
 */
package lob.storage.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.logging.Logger;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;



/**
 * @author azhuk
 * Creation date: Nov 12, 2014
 *
 */
public class ZipHelper {
	private static final Logger _logger = Logger.getLogger(ZipHelper.class
			.getName());
	
	public static  byte[] compress (byte[]...bytes) throws IOException {
		
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ZipOutputStream zos = new ZipOutputStream(baos);

		//byte[][] input =bytes;
		 for (byte[] input :  bytes ) {
			
			 zos.putNextEntry(new ZipEntry(""));
			 zos.write(input);
			 zos.closeEntry();

		 }
		 
		 zos.close();
		 
		return baos.toByteArray();
		
	}
	
	public static  byte[] compress (HashMap<String, byte[]> files) throws IOException {
		
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ZipOutputStream zos = new ZipOutputStream(baos);

		 for ( Entry<String,byte[]>  entry: files.entrySet() ) {			 		 		
			 zos.putNextEntry(new ZipEntry(entry.getKey()));
			 zos.write(entry.getValue());
			 zos.closeEntry();
		 }
		 
		 zos.close();
		 
		return baos.toByteArray();
		
	}
}
