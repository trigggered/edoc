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
public enum  EDevisions {
	Unknown(0),
	AUD(1),
	COMPL(2),
	OPS(3),
	REENG(4),
	IT(5),
	PROC(6),
	HR(7),
	RB(8),
	PF(9),
	COLL(10),
	FIN(11),
	PB(12),
	COM(13),
	CM(14),
	PMO(15),
	RM(16),
	CB(17),
	SEC(18),
	LEG(19),
	CB_B(20),
	OPS_B(21),
	PB_B(22);
	
	private int _value;
	
	public int getValue() {
	    return _value;
   }
	
	private EDevisions (int value) {
		_value = value;
	}
	
	
	public String toString() {
		switch (_value ) {
		case 0: 
			return "Unknown";
		case 1:
			return "AUD"; 
		case 2:
			return "COMPL"; 
		case 3:
			return "OPS"; 
		case 4:
			return "REENG"; 
		case 5:
			return "IT"; 
		case 6:
			return "PROC"; 
		case 7:
			return "HR"; 
		case 8:
			return "RB"; 
		case 9:
			return "PF"; 
		case 10:
			return "COLL"; 
		case 11:
			return "FIN"; 
		case 12:
			return "PB"; 
		case 13:
			return "COM"; 
		case 14:
			return "CM"; 
		case 15:
			return "PMO"; 
		case 16:
			return "RM"; 
		case 17:
			return "CB"; 
		case 18:
			return "SEC"; 
		case 19:
			return "LEG"; 
		case 20:
			return "CB-B"; 
		case 21:
			return "OPS-B"; 
		case 22:
			return "PB-B"; 
		}
		return null;
	}
	
	public static EDevisions fromString (String value) {	
		
		List<EDevisions> lst=  getList();
		
		for ( EDevisions item :  lst ) {
				if (value.equalsIgnoreCase(item.toString()))
				{
					return item;
				}
		}
		
		return EDevisions.Unknown;
	}
	
	public static List<EDevisions> getList() {
		List<EDevisions> toReturn =
                new ArrayList<EDevisions>(EnumSet.allOf(EDevisions.class));
		return toReturn;
	}
	
	public static EDevisions parseString (String value) {
		EDevisions toReturn = null;
		
		 String[] ar = value.split("-");
		 if ( ar.length >0 ) {			 
			 return fromString(ar[0].trim());			 
		 }
		
		return EDevisions.Unknown;
	}
}
