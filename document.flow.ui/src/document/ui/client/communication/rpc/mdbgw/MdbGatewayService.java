package document.ui.client.communication.rpc.mdbgw;

import java.util.List;

import mdb.core.shared.exceptions.SessionExpiredException;
import mdb.core.shared.transport.Request;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;


@RemoteServiceRelativePath("gateway")
public interface MdbGatewayService   extends RemoteService   {
	
	Request Invoke(Request input);
	List<Request> batchInvoke(List<Request> input) throws SessionExpiredException, Exception;	

}
