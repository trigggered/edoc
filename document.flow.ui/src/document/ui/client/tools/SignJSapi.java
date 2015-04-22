/**
 * 
 */
package document.ui.client.tools;

import com.google.gwt.dom.client.Element;

/**
 * @author azhuk
 * Creation date: Dec 17, 2014
 *
 */
public class SignJSapi {

	protected static native String VerifyJs(Element formSign,
			String signature,				// Вхідний. Підпис для перевірки у  вигляді BASE64-строки 
		String data, 				// Вхідний. Дані для перевірки ЕЦП
		boolean showSignerInfo) /*-{
		
		return formSign.Verify(signature,data,showSignerInfo);
		
	}-*/;
	
	protected  static native String getOwnCertificateUserCodeJS(Element formSign ) /*-{
		return formSign.GetOwnCertificateUserCode();	
	}-*/;
	
	protected  static native String getOwnCertificateUserCodeJSApplet(Element formSign ) /*-{		
		var userCode = "Empty";
			 try {			  				
        
        		var array = formSign.GetOwnCertificate();
        		var info = formSign.ParseCertificate(array);
        		userCode = info.GetSubjUserCode();        		  
			 }
			 catch (e) { 	     
		            alert("Error. " + e.message);
		     
		    }
		  
		return userCode;
	}-*/;
	
	
	protected static native String hashJs(Element formSign, String data) /*-{
		return formSign.hash(data);
	}-*/;
	
	protected static native String signJs(Element formSign, String data) /*-{	
		//return formSign.RawSign(data);
		return formSign.Sign(data);
	}-*/;
	
	protected static native  boolean initSignControlJS(Element formSign) /*-{
	
    	var showAlert = false;
	    try {   	 	    	 
	    		   		
	   		if ( formSign.IsInitialized() ) {
	   			if (showAlert == true) {
	            	alert("Ключ уже активирован! ");
	   			}
	   			return true;
	        }	   		
	   		
	        formSign.Initialize();
	        formSign.SetUIMode(true);
	        
	        formSign.ResetOperation();
	        formSign.ResetPrivateKey();
	        formSign.ReadPrivateKey();
	        
	        
	 
	        activateResult = true;
	        if (showAlert == true) {
	            alert("Ключ активирован успешно");
	        }
	    }
	    catch (e) {	 
	        if (showAlert == true) {
	            alert("Ключ не активирован. " + e.message);
	        }
	    }
	    return activateResult;
		
	}-*/;	
	
	
	protected static native  boolean readKeyJS(Element formSign) /*-{	
    try {   	 	    	     		   		
   		
	        formSign.ResetOperation();
	        formSign.ResetPrivateKey();
	        formSign.ReadPrivateKey();
        
	    }
	    catch (e) {	        	        
	         alert("Ключ не активирован. " + e.message);
	        return false;
	    }
	    return  true;
	
	}-*/;	

	
	protected static native  boolean initSignControl(Element formSign) /*-{
	
	var showAlert = true;
    try {	    	 
    	 
    	
	    if (formSign===null ) {	    	 
				    alert("Sign Control  object is null");
				    return;																					    					    	
	    }   
   		
   		
   		if ( formSign.IsInitialized() ) {
   			if (showAlert == true) {
            	alert("Ключ уже активирован! ");
   			}
   			return true;
        }	
   		
   		
        formSign.Initialize();
        
        readKey(formSign);
       
        
        this.isSignActivated = true;
        activateResult = true;
        if (showAlert == true) {
            alert("Ключ активирован успешно");
        }
    }
    catch (e) {
        this.isSignActivated = false;
        if (showAlert == true) {
            alert("Ключ не активирован. " + e.message);
        }
    }
    return activateResult;
	
	}-*/;
	
	protected static native  boolean readKey(Element formSign) /*-{
		if (formSign===null || typeof (formSign ) === 'undefined' ) {	    	 
					    alert("Sign Control  object is null");
					    return;																					    					    	
		    }	
		    
		 	formSign.ResetOperation();
	        formSign.ResetPrivateKey();
	        formSign.ReadPrivateKey();
	}-*/;
}
