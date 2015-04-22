/**
 * 
 */
package document.ui.client.tools;


/**
 * @author azhuk
 * Creation date: Dec 17, 2014
 *
 */
public interface ISignControl {

	public boolean initialize();
	
	public void  regSignControlAsHTML();		
	
	public String sign(String data);		
	
	public  String Verify(
			String signature,				// Вхідний. Підпис для перевірки у 
								// вигляді BASE64-строки 
		String data, 				// Вхідний. Дані для перевірки ЕЦП
		boolean showSignerInfo);

	/**
	 * @param userId
	 * @return
	 */
	public boolean CheckLoginUserIdKeyOwner(int userId);
	
	public String getOwnerUserCode();
	
}
