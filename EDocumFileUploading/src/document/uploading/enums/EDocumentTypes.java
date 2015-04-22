/**
 * 
 */
package document.uploading.enums;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

/**
 * @author azhuk
 * Creation date: Aug 1, 2014
 *
 */
public enum EDocumentTypes {
  
	Unknown(0),
	Commands(485),
	Orders(486),  
  Notifications(493);
  
  private int _value;
	
	public int getValue() {
	    return _value;
   }
	
	private EDocumentTypes (int value) {
		_value = value;
	}
	
	public String toString() {
		
		switch (_value) {
		
		case 485:
			return "Приказы";
		case 486:
			return "Порядки";
		case 493:
			return "Уведомления";
		}
		return null;
	}
	
	public static  List<EDocumentTypes> getList() {
		List<EDocumentTypes> toReturn =
                new ArrayList<EDocumentTypes>(EnumSet.allOf(EDocumentTypes.class));
		return toReturn;
	}
	
	
	public static EDocumentTypes fromString (String value) {		
		
		List<EDocumentTypes> lst=  getList();
		
		for ( EDocumentTypes item :  lst ) {
				if (value.equalsIgnoreCase(item.toString()))
				{
					return item;
				}
		}
		
		return EDocumentTypes.Unknown;
	}
}
