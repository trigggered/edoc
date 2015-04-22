/**
 * 
 */
package lob.storage.hashing;

import java.util.logging.Logger;

import com.ply.security.G28147Sblock;
import com.ply.security.G3411Hash;

/**
 * @author azhuk
 * Creation date: Nov 14, 2014
 *
 */
public class Hash3410 {
	
	private static final Logger _logger = Logger.getLogger(Hash3410.class
			.getCanonicalName());
	
	

	public static byte[] hashMessage(byte[] paramArrayOfByte) {
		G3411Hash localG3411Hash;
		try {
			G28147Sblock localG28147Sblock = new G28147Sblock();
			localG3411Hash = new G3411Hash(localG28147Sblock);
		} catch (Exception localException) {
			return new byte[0];
		}

		localG3411Hash.Begin();
		localG3411Hash.Update(paramArrayOfByte, 0, paramArrayOfByte.length / 2);
		localG3411Hash.Update(paramArrayOfByte, paramArrayOfByte.length / 2,
				paramArrayOfByte.length - (paramArrayOfByte.length / 2));
		localG3411Hash.Final();
		byte[] arrayOfByte = localG3411Hash.GetHash();
		return arrayOfByte;
	}

	public static byte[] hashMessage(byte[] paramArrayOfByte, int paramInt) {
		int i = paramInt / 8;
		G3411Hash localG3411Hash;
		try {
			G28147Sblock localG28147Sblock = new G28147Sblock();
			localG3411Hash = new G3411Hash(localG28147Sblock);
		} catch (Exception localException) {
			return new byte[0];
		}

		localG3411Hash.Begin();
		localG3411Hash.Update(paramArrayOfByte, 0, i);
		localG3411Hash.FinalEx(paramArrayOfByte, i, paramInt & 0x7);
		byte[] arrayOfByte = localG3411Hash.GetHash();
		return arrayOfByte;
	}

	public static String hashMessage(String paramString1, String paramString2)
			throws Exception {
		byte[] arrayOfByte = hashMessage(paramString1.getBytes(paramString2));

		String str = "";
		for (int i = 0; i < arrayOfByte.length; ++i) {
			str = str
					+ Integer.toString((arrayOfByte[i] & 0xFF) + 256, 16)
							.substring(1);
		}
		return str;
	}
	
}
