/**
 * 
 */
package document.ui.client.tools;


/**
 * @author azhuk
 * Creation date: Dec 17, 2014
 *
 */
public class SignControlWrapper   {
	
	private static ISignControl _signControl;
	
	public static ISignControl getSignControl () {
		
		if (_signControl != null) {
			return _signControl;
		} else {
			_signControl = createSignControl();
		}
			
		return _signControl;
	}

	/**
	 * @return
	 */
	private static ISignControl createSignControl() {		
		return new SignAppletWrapper();
		//return new SignXWrapper();
	}


}
