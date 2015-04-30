/**
 * 
 */
package document.ui.client.communication.rpc.mail;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

/**
 * @author azhuk
 * Creation date: May 6, 2015
 *
 */

@RemoteServiceRelativePath("mail")
public interface MailingService   extends RemoteService{

	//void sendApproveMsgResult (boolean result, long documentId, String infoMessage,  int initiatorId);
}
