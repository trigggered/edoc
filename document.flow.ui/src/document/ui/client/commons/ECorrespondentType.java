/**
 * 
 */
package document.ui.client.commons;

import mdb.core.ui.client.app.AppController;
import document.ui.client.resources.locales.Captions;


/**
 * @author azhuk
 * Creation date: May 14, 2014
 *
 */
public enum ECorrespondentType {
	INPUT(1),
	OUTPUT(2),
	INSIDE_PRIKAZ(485),
	INSIDE_PROCEDURE(486),
	INSIDE_NOTIFICATION(493),
	ACCOUNT_MODEL(496),
	UNKNOWN(6);
	 
	private int _value;
	
	public int getValue() {
	    return _value;
   }
	
	private ECorrespondentType (int value) {
		_value = value;
	}
	
	
	public static ECorrespondentType fromInt(int value) {			
		switch (value ) {
			case 1: return INPUT;		
			case 2: return OUTPUT;
			case 485: return INSIDE_PRIKAZ;
			case 486: return INSIDE_PROCEDURE;			
			case 493: return INSIDE_NOTIFICATION;
			case 496: return ACCOUNT_MODEL;
			case 6: return UNKNOWN;
			
			default:
				return INSIDE_PRIKAZ;
		}
	}
	
	
	
public static ECorrespondentType fromString(String value) {
		
	if ( value.equals(ECorrespondentType.INPUT.toString()) ) {
			return ECorrespondentType.INPUT;		}
	else if ( value.equals(ECorrespondentType.OUTPUT.toString()) )
			return ECorrespondentType.OUTPUT;
	else if( value.equals(ECorrespondentType.INSIDE_PRIKAZ.toString()) )
			return ECorrespondentType.INSIDE_PRIKAZ;
	else if( value.equals(ECorrespondentType.INSIDE_PROCEDURE.toString()) )
		return ECorrespondentType.INSIDE_PROCEDURE;	
	else if( value.equals(ECorrespondentType.INSIDE_NOTIFICATION.toString()) )
		return ECorrespondentType.INSIDE_NOTIFICATION;
	else if( value.equals(ECorrespondentType.ACCOUNT_MODEL.toString()) )
		return ECorrespondentType.ACCOUNT_MODEL;
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
		case 496:
			return "2.1.";	
		case 6:
			return "";	
		}
	return "";
	
	}

	public String getCaption () {
		switch ( fromInt(_value) )  {
		case INPUT:
			return Captions.INPUT ;
		case OUTPUT:
			return Captions.OUTPUT ;
		case INSIDE_PRIKAZ:
			return Captions.PRIKAZ ;		
		case INSIDE_PROCEDURE:
			return Captions.PROCEDURE;
		case INSIDE_NOTIFICATION:
			return Captions.NOTIFICATION;		
		case ACCOUNT_MODEL:
			return Captions.ACCOUNT_MODEL ;	
		default:
			return "";			
		}
	}
	
	public static String getRootCodeCorrespondentType() {
		
		switch (		AppController.getInstance().getCurrentUser().getChooseApplicationID() ) {
			case 1:  return "1.3.";
			case 2:  return "2";
			default:
				return "1.3";
		}
	}
}
