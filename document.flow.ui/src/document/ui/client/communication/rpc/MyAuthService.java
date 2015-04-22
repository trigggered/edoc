/**
 * 
 */
package document.ui.client.communication.rpc;

import mdb.core.shared.auth.AuthUser;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

/**
 * @author azhuk
 * Creation date: Jul 21, 2014
 *
 */

@RemoteServiceRelativePath("auth")
public interface MyAuthService extends RemoteService {
	 
	public AuthUser retrieveUserInfo();
	
}
