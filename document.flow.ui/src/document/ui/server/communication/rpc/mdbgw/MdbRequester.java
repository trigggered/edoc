/**
 * 
 */
package document.ui.server.communication.rpc.mdbgw;

import java.util.logging.Logger;

import mdb.core.shared.data.Params;
import mdb.core.shared.transport.IRequestData.ExecuteType;
import mdb.core.shared.transport.Request;
import mdb.core.shared.transport.RequestEntity;

/**
 * @author azhuk
 * Creation date: Oct 3, 2014
 *
 */
public class MdbRequester {
	
	private static final Logger _logger = Logger.getLogger(MdbRequester.class
			.getName());
	
	MdbGatewayServiceImpl _mdbGetway= new MdbGatewayServiceImpl();
	
	
	public   Request getNewRequest(int entityId, ExecuteType executeType, Params params) {
		_logger.info("Request Entity id="+entityId);
		Request req = new Request();
		 RequestEntity entity = new RequestEntity(entityId);		 
		 entity.setRequestFieldsDescription(false);
		 entity.setExecuteType(executeType);
		 entity.getParams().copyFrom(params);
		 req.add(entity);
		 
		 return req;
	}
	
	
	public Request call (Request request) {		
		return _mdbGetway.Invoke(request);
	}

}
