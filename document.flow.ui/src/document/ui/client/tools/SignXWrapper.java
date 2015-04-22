/**
 * 
 */
package document.ui.client.tools;

import java.util.logging.Logger;

import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.RootPanel;

/**
 * @author azhuk
 * Creation date: Nov 4, 2014
 *
 */
public class SignXWrapper extends SignJSapi implements ISignControl{
	private static final Logger _logger = Logger.getLogger(SignXWrapper.class
			.getName());
	
	private static final  String SIGN_CONTROL_ID =  "formSign";
	private static final  String SIGN_CLASSID =  "A7D1E5B8-F3D4-480C-8F2E-125D2A7AE971";	
	private static final String SIGN_OBJECT = "<object "
			+ "width=\"0\" "
			+ "height=\"0\" "
			+ "classid=\"clsid"+SIGN_CLASSID+"\" "
			+ "id=formSign >"
			+ "</object>";

	
	@Override
	public void  regSignControlAsHTML() {
		RootPanel.get().add(new HTML(getObjectHTML()));	
	}
	
	
	
	protected String getObjectHTML () {		
		return SIGN_OBJECT; 	
	}
	

	protected String getSignControlId() {
		return SIGN_CONTROL_ID;
	}	
	
	
	protected Element getSignControl() {
		_logger.info("Get Sign control html element by ID="+getSignControlId()); 
		 return  Document.get().getElementById(getSignControlId());		 		 
	}
	
	@Override
	public boolean initialize() 
	{
		_logger.info("Try init sign control library");
		 Element sign = getSignControl();
		 if (sign == null) {
			 _logger.severe("Not found sign control by ID");
			 return false;
		 } else {
			 _logger.info("Sign control found. Try Initialize.");
		 }
		 boolean toReturn =initSignControlJS(sign);
		 
		 _logger.info("Initialize result ="+toReturn);
		 return toReturn;
	}
	
	
	public boolean readKey() {
		return true;
	}

	
	@Override
	public String sign(String data) {
		
		if (initialize()) {
			Element sign = getSignControl(); 
			return signJs(sign, data);
		} else return null;
		
	}
	
	@Override
	public  String Verify(
			String signature,				// Вхідний. Підпис для перевірки у 
								// вигляді BASE64-строки 
		String data, 				// Вхідний. Дані для перевірки ЕЦП
		boolean showSignerInfo) {
		
		if (initialize()) {
			Element formSign = getSignControl();
			return VerifyJs(formSign, signature, data, showSignerInfo);
		} else return null;		 
		
	}
	
	/*
	@Override
	public  String getOwnCertificateUserCode() {
		if (initialize()) {
			Element formSign = getSignControl();
			return getOwnCertificateUserCode(formSign);
		} else return null;		 
		
	}
	*/
	
	
	@Override
	public boolean CheckLoginUserIdKeyOwner(int userId) {
		String useridKeyOwner = getOwnerUserCode();
		_logger.info("User id = "+userId +". Key owner ID = "+useridKeyOwner);
		return userId == Integer.valueOf(useridKeyOwner).intValue();					
		
	}
	
	public String getOwnerUserCode()  {
		String useridKeyOwner = null;
		if ( initialize() ) {
			Element formSign = getSignControl();
			useridKeyOwner = getOwnCertificateUserCodeJS(formSign);
		}
		return useridKeyOwner;
	}
	
	
	
}
