/**
 * 
 */
package document.ui.client.communication.rpc.flow;

import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * @author azhuk
 * Creation date: Aug 14, 2014
 *
 */
public interface DocumentFlowServiceAsync {

	void sendToTheNextStage(long documentId, String infoMessage, int initiatorId,
			AsyncCallback<Boolean> callback);

	void sendRemember(long documentId, String infoMessage,  int initiatorId, AsyncCallback<Void> callback);

	void cancelProcess(long documentId, String infoMessage, int initiatorId,
			AsyncCallback<Void> callback);

	void forcedDocumentToStatus(long documentId, String infoMessage, int initiatorId,
			int toStatus, AsyncCallback<Void> callback);
	
	void sendApproveCurrentUserInfoMsg(long documentId, boolean approved,
			String infoMessage, String initName, AsyncCallback<Void> callback);

	void sendApproveResult(long documentId, String infoMessage,  int initiatorId, AsyncCallback<Void> asyncCallback);

	void publishDoc(int documentId, String infoMsg, int id,
			AsyncCallback<Boolean> asyncCallback);

	
}
