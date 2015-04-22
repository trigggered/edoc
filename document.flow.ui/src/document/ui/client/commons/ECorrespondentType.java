/**
 * 
 */
package document.ui.client.commons;


/**
 * @author azhuk
 * Creation date: May 14, 2014
 *
 */
public enum ECorrespondentType {
	INPUT_DOC(1),
	OUTPUT_DOC(2),
	INSIDE_PRIKAZ_DOC(485),
	INSIDE_PROCEDURE_DOC(486),
	INSIDE_NOTIFICATION_DOC(493),
	UNKNOWN(6); 
	
	// NewInsideCommandDoc:
	// NewInsideNotificationDo

	private int _value;
	
	public int getValue() {
	    return _value;
   }
	
	private ECorrespondentType (int value) {
		_value = value;
	}
	
	
	public static ECorrespondentType fromInt(int value) {			
		switch (value ) {
			case 1: return INPUT_DOC;		
			case 2: return OUTPUT_DOC;
			case 485: return INSIDE_PRIKAZ_DOC;
			case 486: return INSIDE_PROCEDURE_DOC;			
			case 493: return INSIDE_NOTIFICATION_DOC;
			case 6: return UNKNOWN;
			
			default:
				return INSIDE_PRIKAZ_DOC;
		}
	}
	
	
	
public static ECorrespondentType fromString(String value) {
		
	if ( value.equals(ECorrespondentType.INPUT_DOC.toString()) ) {
			return ECorrespondentType.INPUT_DOC;		}
	else if ( value.equals(ECorrespondentType.OUTPUT_DOC.toString()) )
			return ECorrespondentType.OUTPUT_DOC;
	else if( value.equals(ECorrespondentType.INSIDE_PRIKAZ_DOC.toString()) )
			return ECorrespondentType.INSIDE_PRIKAZ_DOC;
	else if( value.equals(ECorrespondentType.INSIDE_PROCEDURE_DOC.toString()) )
		return ECorrespondentType.INSIDE_PROCEDURE_DOC;	
	else if( value.equals(ECorrespondentType.INSIDE_NOTIFICATION_DOC.toString()) )
		return ECorrespondentType.INSIDE_NOTIFICATION_DOC;
	else return ECorrespondentType.UNKNOWN;	
		
	}

public String toString() {
	switch (_value) {
		case 1:
			 return "1.1.";
		case 2:
			return "1.2.";
		case 485:
			return "1.3.1.";
		case 486:
			return "1.3.2.";
		case 493:
			return "1.3.3.";
		case 6:
			return "";	
		}
	return "";
	
	}

	public String getCaption () {
		switch ( fromInt(_value) )  {
		case INPUT_DOC:
			return "Входящий:";
		case OUTPUT_DOC:
			return "Исходящий:";
		case INSIDE_PRIKAZ_DOC:
			return "Приказ ";		
		case INSIDE_PROCEDURE_DOC:
			return "Порядок ";
		case INSIDE_NOTIFICATION_DOC:
			return "Уведомление ";		
		default:
			return "";			
		}
	}
}
