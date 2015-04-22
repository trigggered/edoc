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

	void sendRemember(long documentId, AsyncCallback<Void> callback);

	void cancelProcess(long documentId, String infoMessage, int initiatorId,
			AsyncCallback<Void> callback);

	void sendPublishedInfoMsg(long documentId, AsyncCallback<Void> callback);
	
}
