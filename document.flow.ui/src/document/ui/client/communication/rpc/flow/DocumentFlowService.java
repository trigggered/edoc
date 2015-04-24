/**
 * 
 */
package document.ui.client.communication.rpc.flow;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

/**
 * @author azhuk
 * Creation date: Aug 14, 2014
 *
 */

@RemoteServiceRelativePath("flow")
public interface DocumentFlowService extends RemoteService{
	
	public boolean  sendToTheNextStage(long documentId, String infoMessage, int initiatorId);
	
	public void sendRemember (long documentId, String infoMessage,  int initiatorId);
	public void sendPublishedInfoMsg(long documentId,String infoMessage,  int initiatorId);
	
	void cancelProcess(long documentId, String infoMessage,  int initiatorId);
	void forcedDocumentToStatus(long documentId, String infoMessage,  int initiatorId, int toStatus);

	/**
	 * @param documentId
	 * @param approved
	 * @param infoMessage
	 * @param initName
	 */
	void sendApproveCurrentUserInfoMsg(long documentId, boolean approved,
			String infoMessage, String initName);
}
