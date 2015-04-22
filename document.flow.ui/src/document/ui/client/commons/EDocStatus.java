/**
 *Статусы документов 
 * 
 * 
 *  1	На исполнении
	2	Отправлен ответ
	3	Архивный
	5	Проект документа (драфт)
	6	На согласовании
	7	Cогласован
	8	На подписании 
	9	Подписанный
	10	Действующий
	11	Отмененный
*/		
	
 
package document.ui.client.commons;



/**
 * @author azhuk
 * Creation date: Jul 1, 2014
 *
 */
public enum EDocStatus {
	Unknown(0),
	Execution(1), 
	Sent2answer(2), 
	Archive(3),
	
	Draft(4), 
	AtTheApproval(5), 
	Approval(6), 
	AtTheSigning(7), 
	Signed(8), 
	Valid(9),
	Revoked(10),
	Cancelled(11);
	

	private int _value;
	
	public int getValue() {
	    return _value;
   }
	
	private EDocStatus (int value) {
		_value = value;
	}
	
	
	public static EDocStatus fromInt(int value) {			
		switch (value ) {
			case 0: return Unknown;
			case 1: return Execution;		
			case 2: return Sent2answer;
			case 3: return Archive;
			case 4: return Draft;			
			case 5: return AtTheApproval;
			case 6: return Approval;
			case 7: return AtTheSigning;
			case 8: return Signed;
			case 9: return Valid;
			case 10: return Revoked;
			case 11: return  Cancelled;
			
			default:
				return Unknown;
		}
	}
	
}
