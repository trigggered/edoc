/**
 * 
 */
package document.ui.client.communication.rpc;

import mdb.core.shared.auth.AuthUser;

import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * @author azhuk
 * Creation date: Jul 21, 2014
 *
 */
public interface MyAuthServiceAsync {

	void retrieveUserInfo(AsyncCallback<AuthUser> callback);

}
