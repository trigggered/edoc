/**
 * 
 */
package lob.storage.hashing;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Logger;



/**
 * @author azhuk
 * Creation date: Oct 23, 2014
 *
 */
public class HashingHelper {
	private static final Logger _logger = Logger.getLogger(HashingHelper.class
			.getCanonicalName());
	
	public static String getHashMD5(byte[] value) {
		MessageDigest messageDigest = null;
		try {
			messageDigest = MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException e) {
			_logger.severe(e.getMessage());
			return "Error in hash generated";
		}
		messageDigest.reset();
		messageDigest.update(value);
		
		final byte[] resultByte = messageDigest.digest();		
		
		StringBuffer sb = new StringBuffer();
        for (int i = 0; i < resultByte.length; ++i) {
            sb.append(Integer.toHexString((resultByte[i] & 0xFF) | 0x100).substring(1,3));
        }
        return sb.toString();
		
	}
	
	public static String getHash3410(byte[] value) {						
		byte[] hash = Hash3410.hashMessage(value);
		
		String toReturn = "";
		for (int i = 0; i < hash.length; ++i) {
			toReturn = toReturn
					+ Integer.toString((hash[i] & 0xFF) + 256, 16)
							.substring(1);
		}
		
		return toReturn;
		
	}
	
}
