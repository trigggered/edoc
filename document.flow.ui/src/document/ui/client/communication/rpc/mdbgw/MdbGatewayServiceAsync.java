package document.ui.client.communication.rpc.mdbgw;

import java.util.List;

import mdb.core.shared.transport.Request;

import com.google.gwt.user.client.rpc.AsyncCallback;



public interface MdbGatewayServiceAsync  {
	
	
	  void Invoke(Request input, AsyncCallback<Request> callback);

	  void batchInvoke(List<Request> input, AsyncCallback<List<Request>> callback);

}
