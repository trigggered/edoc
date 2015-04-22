/**
 * 
 */
package lob.storage.environment;


/**
 * @author azhuk
 * Creation date: Aug 30, 2013
 *
 */
public class Property {	
	
	public enum StorageType {
		DbStorage(1),
		FsStorage(2);
		
		private int _value;    
		
		private StorageType (int val) {
			_value = val;
		}
		

		public int getValue() {
			return _value;
		}	
		   
		public StorageType fromInt(int value) {
			switch (value) {
			case 1: 
				return DbStorage;
			case 2: 
				return  FsStorage;
			default : return 	DbStorage;
			}
		}
	}
	
	
	public static StorageType getDefaultStorageType() {
		return StorageType.DbStorage;
	}
}
