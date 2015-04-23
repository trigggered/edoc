/**
 * 
 */
package document.ui.server.auth.ad;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.ldap.core.DirContextOperations;
import org.springframework.security.core.GrantedAuthority;

/**
 * @author azhuk
 * Creation date: Jul 18, 2014
 *
 */
public class CustomAdAuthenticationProvider extends ActiveDirectoryLdapAuthenticationProvider {
	

	/**
	 * @param domain
	 * @param url
	 */
	public CustomAdAuthenticationProvider(String domain, String url) {
		super(domain, url);
	}

	
	 @Override
	   protected Collection<? extends GrantedAuthority> loadUserAuthorities(DirContextOperations userData, String username, String password) {
		 
		 @SuppressWarnings("unchecked")
		ArrayList<GrantedAuthority> authorities = (ArrayList<GrantedAuthority>) super.loadUserAuthorities(userData, username, password);
		
		 
		 /*		 
		 _logger.info(userData.getStringAttribute("employeeID")); // Tab number
		 _logger.info(userData.getStringAttribute("cn")); // Full name
		 _logger.info(userData.getStringAttribute("description")); //login name
		 _logger.info(userData.getStringAttribute("mail")); //e-mail
		 
		 _logger.info("First Name: "+userData.getStringAttribute("givenName") ); 
		 _logger.info("Last Name: "+ userData.getStringAttribute("sn"));
		 
		 
		 for (GrantedAuthority  grant  : authorities) {
			 _logger.info(grant.getAuthority() );			 
		 }
		 
		 */
		return authorities;
	 
	 }
		 
	 
}
