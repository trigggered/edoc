package document.ui.server.auth;

import java.util.ArrayList;
import java.util.Collection;

import mdb.core.shared.auth.AuthUser;
import mdb.core.shared.auth.IUserInfo;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

public class CustomUserAuthentication implements Authentication {
	
	private static final long serialVersionUID = -3091441742758356129L;
	
	private boolean authenticated;
	
	private SimpleGrantedAuthority grantedAuthority;
	private Authentication authentication;
	private IUserInfo _userInfo;
	
	public CustomUserAuthentication(String role, Authentication authentication) {
		this.grantedAuthority = new SimpleGrantedAuthority(role);
		this.authentication = authentication;
		
		
		_userInfo = new AuthUser(authentication.getPrincipal().toString());
		_userInfo.getRoles().add(role);
	}

	@Override
	public Collection<GrantedAuthority> getAuthorities() {
		Collection<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
		authorities.add(grantedAuthority);
		return authorities;
	}

	@Override
	public Object getCredentials() {
		return authentication.getCredentials();
	}

	@Override
	public Object getDetails() {
		return authentication.getDetails();
	}

	@Override
	public Object getPrincipal() {
		return authentication.getPrincipal();
	}

	@Override
	public boolean isAuthenticated() {
		return authenticated;
	}

	@Override
	public void setAuthenticated(boolean authenticated) throws IllegalArgumentException {
		this.authenticated = authenticated;
	}

	@Override
	public String getName() {
		return this.getClass().getSimpleName();
	}

	public IUserInfo getUserInfo() {
		return _userInfo;
	}
}
