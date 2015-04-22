/**
 * 
 */
package document.ui.server.auth.ldap;

/**
 * @author azhuk
 * Creation date: Jul 18, 2014
 *
 */
import java.util.Collection;
import java.util.logging.Logger;

import javax.naming.NamingEnumeration;
import javax.naming.directory.Attributes;

import org.springframework.ldap.core.DirContextAdapter;
import org.springframework.ldap.core.DirContextOperations;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.ldap.userdetails.LdapUserDetailsMapper;
import org.springframework.util.Assert;


public class AttributesLDAPUserDetailsContextMapper extends LdapUserDetailsMapper {

	private static final Logger logger = Logger
			.getLogger(AttributesLDAPUserDetailsContextMapper.class.getName());

	private String[] attributesToPopulate = new String[] {};
	private String[] roleAttributes = null;
	private boolean mapAllAttributes = false;

	public boolean isMapAllAttributes() {
		return mapAllAttributes;
	}

	public void setMapAllAttributes(boolean mapAllAttributes) {
		this.mapAllAttributes = mapAllAttributes;
	}

	public String[] getAttributesToPopulate() {
		return attributesToPopulate;
	}

	public void setAttributesToPopulate(String[] attributesToPopulate) {
		this.attributesToPopulate = attributesToPopulate;
	}
  
	

	
	@Override
	public UserDetails mapUserFromContext(DirContextOperations ctx,
			String username, Collection<? extends GrantedAuthority> authorities) {
	
		logger.info("Mapping User "+ username +"+ from Context to me ");
		/*
		logger.info("ExtUserDetails to AuthUser");
		logger.info("Mail is :" +ctx.getStringAttribute("mail"));
		logger.info("Emploee ID is :" +ctx.getStringAttribute("employeeID"));
		
		
		*/
		// LdapUserDetailsImpl origImpl =
		// (LdapUserDetailsImpl)super.mapUserFromContext(ctx, username,
		// authorities);

		String dn = ctx.getNameInNamespace();

		logger.info("Mapping user details from context with DN: " + dn);

		ExtUserDetails.Essence essence = new ExtUserDetails.Essence();
		essence.setDn(dn);
		
		essence.addAttribute("mail", ctx.getStringAttribute("mail"));
		essence.addAttribute("employeeID", ctx.getStringAttribute("employeeID"));
		essence.addAttribute("cn", ctx.getStringAttribute("cn"));
		essence.addAttribute("description", ctx.getStringAttribute("description"));
		
		

		// Object passwordValue =
		// ctx.getObjectAttribute(super.passwordAttributeName);

		// if (passwordValue != null) {
		// essence.setPassword(mapPassword(passwordValue));
		// }

		
		essence.setUsername(username);

		// Map the roles
		for (int i = 0; (roleAttributes != null) && (i < roleAttributes.length); i++) {
			String[] rolesForAttribute = ctx
					.getStringAttributes(roleAttributes[i]);
			if (rolesForAttribute == null) {
				logger.info("Couldn't read role attribute '"
						+ roleAttributes[i] + "' for user " + dn);
				continue;
			}
			for (int j = 0; j < rolesForAttribute.length; j++) {
				GrantedAuthority authority = createAuthority(rolesForAttribute[j]);
				if (authority != null) {
					essence.addAuthority(authority);
				}
			}
		} // Add the supplied authorities for (GrantedAuthority grantedAuthority
			// : authorities) { essence.addAuthority(grantedAuthority); }
		
		DirContextAdapter dca = (DirContextAdapter) ctx;

		for (int i = 0; i < attributesToPopulate.length; i++) {
			String attribute = dca.getStringAttribute(attributesToPopulate[i]);
			essence.addAttribute(attributesToPopulate[i], attribute);
			logger.info("Custom Attribute: '" + attributesToPopulate[i]
					+ "' =" + attribute);
		}
		
		if (isMapAllAttributes()) {

			Attributes attributes = dca.getAttributes();

			NamingEnumeration<String> attributelist = attributes.getIDs();

			while (attributelist.hasMoreElements()) {
				String key = (String) attributelist.nextElement();

				essence.addAttribute(key, attributes.get(key));
				logger.info("Attribute List: '" + key + "' ="
						+ attributes.get(key));
			}

		}
		//return new User	
		ExtUserDetails toReturn =essence.createUserDetails();
		
		//logger.info("ExtUserDetails Attribite Emploee ID is :" +toReturn.getAttribute("employeeID"));
		
		return toReturn;
	}
	

	@Override
	public void setRoleAttributes(String[] roleAttributes) {
		Assert.notNull(roleAttributes, "roleAttributes array cannot be null");
		this.roleAttributes = roleAttributes;
	}



}