/**
 * 
 */
package document.ui.client.tools;


import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.RootPanel;

/**
 * @author azhuk
 * Creation date: Dec 17, 2014
 *
 */
public class SignAppletWrapper extends SignXWrapper {

	
	private static final  String SIGN_CONTROL_ID =  "euSign";
	private static final String SIGN_OBJECT = 
			"<applet "
			//+ "codebase=\"http://10.91.11.20:8081/document.flow.ui/applet\" "
			+ "codebase="+getCodeBaseUrl()
			+ "code=\"com.iit.certificateAuthority.endUser.libraries.signJava.EndUser.class \" "
			+ "archive=\"EUSignJava.jar\" "
			+ "id=\""+SIGN_CONTROL_ID+"\" "
			+ "width=\"100%\" "
			+ "height=\"1\">"
			+ "</applet>";

/*
	private static final String SIGN_OBJECT = 
			"<object "
			+ "codetype=\"application/java\" "
			+ "codebase=\"http://127.0.0.1:8081/document.flow.ui/applet\" "
			+ "classid=\"java:com.iit.certificateAuthority.endUser.libraries.signJava.EndUser.class \" "
			+ "archive=\"EUSignJava.jar\" "
			+ "id=\""+SIGN_CONTROL_ID+"\" "
			+ "width=\"100%\" "
			+ "height=\"1\">"
			+ "</object>";
	
	*/
	public   void  regSignControlAsHTML() {
		RootPanel.get().add(new HTML(getObjectHTML()));		
	}
	
	private static String getCodeBaseUrl() {		
		return  "\""+GWT.getHostPageBaseURL()+"applet\"";
	}
	
	
	@Override
	public String getObjectHTML () {
		
		return SIGN_OBJECT; 	
	}
	
	@Override
	public String getSignControlId() {
		return SIGN_CONTROL_ID;
	}
	
	
	@Override
	public String getOwnerUserCode()  {
		String useridKeyOwner = null;
		if ( initialize() ) {
			Element formSign = getSignControl();
			useridKeyOwner = getOwnCertificateUserCodeJSApplet(formSign);
		}
		
		return useridKeyOwner;
	}
	
	
	
}
