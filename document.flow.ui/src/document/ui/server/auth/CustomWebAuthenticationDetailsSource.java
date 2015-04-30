/**
 * 
 */
package document.ui.server.auth;

import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;

import org.springframework.security.authentication.AuthenticationDetailsSource;
import org.springframework.security.web.authentication.WebAuthenticationDetails;

/**
 * @author azhuk
 * Creation date: May 28, 2015
 *
 */
public class CustomWebAuthenticationDetailsSource  implements AuthenticationDetailsSource<HttpServletRequest, WebAuthenticationDetails>  {
	
	private static final Logger _logger = Logger
			.getLogger(CustomWebAuthenticationDetailsSource.class
					.getCanonicalName());

	/* (non-Javadoc)
	 * @see org.springframework.security.authentication.AuthenticationDetailsSource#buildDetails(java.lang.Object)
	 */
	@Override
	public WebAuthenticationDetails buildDetails(HttpServletRequest context) {
		
		return new CustomWebAuthenticationDetails(context);
	}
}
