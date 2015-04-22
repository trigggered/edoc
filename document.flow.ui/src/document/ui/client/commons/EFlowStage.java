/**
 * 
 */
package document.ui.client.commons;


/**
 * @author azhuk
 * Creation date: Sep 15, 2014
 *
 */
public enum EFlowStage {
	
	Unknown(0),
	Approval(1),
	InitSigne(2),
	ProcessSign(3);
 
 
 	private int _value;
 
 	public int getValue() {
	    return _value;
 	}
	
	private EFlowStage (int value) {
		_value = value;
	}
 
	public static EFlowStage fromInt(int value) {			
		switch (value ) {
			
			case 1: return Approval;		
			case 2: return InitSigne;
			case 3: return ProcessSign;			
			
			default:
				return Unknown;
		}
	}
	
}
