/**
 * 
 */
package document.ui.server.auth;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;

import org.springframework.security.web.authentication.WebAuthenticationDetails;

/**
 * @author azhuk
 * Creation date: May 28, 2015
 *
 */
public class CustomWebAuthenticationDetails extends WebAuthenticationDetails  {
	private static final Logger _logger = Logger
			.getLogger(CustomWebAuthenticationDetails.class.getCanonicalName());
	
	private final String app_id;

    public CustomWebAuthenticationDetails(HttpServletRequest request) {
        super(request);
        app_id = request.getParameter("app_id");
    }

    public String getAppId() {
        return app_id;
    }

    @Override
    public int hashCode() {
    	int code = super.hashCode();
    
    	if(app_id!=null ){
    		code *= this.app_id.hashCode() % 7;	
    	}
    	
    	return code;
    }
    
    @Override
    public boolean equals(Object obj) { 
    	return super.equals(obj);	
    }
    
    @Override
    public String toString() {
    	
    	
    	StringBuilder sb = new StringBuilder();
		sb.append(super.toString()).append(": ");
		sb.append("app_id: ").append(getAppId());
		

		return sb.toString();
    	
    }
}
