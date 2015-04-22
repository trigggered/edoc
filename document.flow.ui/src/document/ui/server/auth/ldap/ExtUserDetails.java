/**
 * 
 */
package document.ui.server.auth.ldap;

import java.util.ArrayList;
import java.util.Collection;
/**
 * @author azhuk
 * Creation date: Jul 18, 2014
 *
 */
import java.util.HashMap;
import java.util.Map;

import javax.naming.Name;

import org.springframework.ldap.core.DirContextOperations;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.ldap.userdetails.LdapUserDetailsImpl;
import org.springframework.util.Assert;

public class ExtUserDetails extends LdapUserDetailsImpl {

	// ~ Instance fields
	// ================================================================================================

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String dn;
	private String password;
	private String username;
	private Collection<GrantedAuthority> authorities = AuthorityUtils.NO_AUTHORITIES;
	private Map<String, Object> attributes = new HashMap<String, Object>();

	private boolean accountNonExpired = true;
	private boolean accountNonLocked = true;
	private boolean credentialsNonExpired = true;
	private boolean enabled = true;
	// PPolicy data
	private int timeBeforeExpiration = Integer.MAX_VALUE;
	private int graceLoginsRemaining = Integer.MAX_VALUE;

	// ~ Constructors
	// ===================================================================================================

	protected ExtUserDetails() {
	}

	// ~ Methods
	// ========================================================================================================
	public Map<String,Object> getAttributes() {
		return attributes;
	}

	public Object getAttribute(String key) {

		return attributes.get(key);
	}

	public Collection<GrantedAuthority> getAuthorities() {
		return authorities;
	}

	public String getDn() {
		return dn;
	}

	public String getPassword() {
		return password;
	}

	public String getUsername() {
		return username;
	}

	public boolean isAccountNonExpired() {
		return accountNonExpired;
	}

	public boolean isAccountNonLocked() {
		return accountNonLocked;
	}

	public boolean isCredentialsNonExpired() {
		return credentialsNonExpired;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public int getTimeBeforeExpiration() {
		return timeBeforeExpiration;
	}

	public int getGraceLoginsRemaining() {
		return graceLoginsRemaining;
	}

	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(super.toString()).append(": ");
		sb.append("Username: ").append(this.username).append("; ");
		sb.append("Password: [PROTECTED]; ");
		sb.append("Enabled: ").append(this.enabled).append("; ");
		sb.append("AccountNonExpired: ").append(this.accountNonExpired)
				.append("; ");
		sb.append("credentialsNonExpired: ").append(this.credentialsNonExpired)
				.append("; ");
		sb.append("AccountNonLocked: ").append(this.accountNonLocked)
				.append("; ");

		if (this.getAuthorities() != null) {
			sb.append("Granted Authorities: ");
			boolean first = true;

			for (Object authority : this.getAuthorities()) {
				if (first) {
					first = false;
				} else {
					sb.append(", ");
				}

				sb.append(authority.toString());
			}
		} else {
			sb.append("Not granted any authorities");
		}

		return sb.toString();
	}

	// ~ Inner Classes
	// ==================================================================================================

	/**
	 * Variation of essence pattern. Used to create mutable intermediate object
	 */
	public static class Essence {
		protected ExtUserDetails instance = createTarget();
		private Collection<GrantedAuthority> mutableAuthorities = new ArrayList<GrantedAuthority>();

		private Map<String, Object> mutableAttributes = new HashMap<String, Object>();

		public Essence() {
		}

		public Essence(DirContextOperations ctx) {
			setDn(ctx.getDn());
		}

		public Essence(ExtUserDetails copyMe) {
			setDn(copyMe.getDn());
			setUsername(copyMe.getUsername());
			setPassword(copyMe.getPassword());
			setEnabled(copyMe.isEnabled());
			setAccountNonExpired(copyMe.isAccountNonExpired());
			setCredentialsNonExpired(copyMe.isCredentialsNonExpired());
			setAccountNonLocked(copyMe.isAccountNonLocked());
			setAuthorities(copyMe.getAuthorities());
			setAttributes(copyMe.getAttributes());
		}

		protected ExtUserDetails createTarget() {
			return new ExtUserDetails();
		}

		/**
		 * Adds the authority to the list, unless it is already there, in which
		 * case it is ignored
		 */
		public void addAttribute(String key, Object value) {
			mutableAttributes.put(key, value);
		}

		/**
		 * Adds the authority to the list, unless it is already there, in which
		 * case it is ignored
		 */
		public void addAuthority(GrantedAuthority a) {
			if (!hasAuthority(a)) {
				mutableAuthorities.add(a);
			}
		}

		private boolean hasAuthority(GrantedAuthority a) {
			for (GrantedAuthority authority : mutableAuthorities) {
				if (authority.equals(a)) {
					return true;
				}
			}
			return false;
		}

		public ExtUserDetails createUserDetails() {
			Assert.notNull(instance,
					"Essence can only be used to create a single instance");
			Assert.notNull(instance.username, "username must not be null");
			Assert.notNull(instance.getDn(),
					"Distinguished name must not be null");

			instance.authorities = getGrantedAuthorities();

			ExtUserDetails newInstance = instance;
			newInstance.attributes = mutableAttributes; 

			instance = null;

			return newInstance;
		}

		public Collection<GrantedAuthority> getGrantedAuthorities() {
			return mutableAuthorities;
		}

		public void setAccountNonExpired(boolean accountNonExpired) {
			instance.accountNonExpired = accountNonExpired;
		}

		public void setAccountNonLocked(boolean accountNonLocked) {
			instance.accountNonLocked = accountNonLocked;
		}

		public void setAuthorities(Collection<GrantedAuthority> collection) {
			mutableAuthorities = collection;
		}

		public void setAttributes(Map<String, Object> attributes) {
			mutableAttributes = attributes;
		}

		public void setCredentialsNonExpired(boolean credentialsNonExpired) {
			instance.credentialsNonExpired = credentialsNonExpired;
		}

		public void setDn(String dn) {
			instance.dn = dn;
		}

		public void setDn(Name dn) {
			instance.dn = dn.toString();
		}

		public void setEnabled(boolean enabled) {
			instance.enabled = enabled;
		}

		public void setPassword(String password) {
			instance.password = password;
		}

		public void setUsername(String username) {
			instance.username = username;
		}

		public void setTimeBeforeExpiration(int timeBeforeExpiration) {
			instance.timeBeforeExpiration = timeBeforeExpiration;
		}

		public void setGraceLoginsRemaining(int graceLoginsRemaining) {
			instance.graceLoginsRemaining = graceLoginsRemaining;
		}
	}
}