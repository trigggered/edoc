/**
 * 
 */
package document.ui.server.utils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import mdb.core.shared.data.Params;
import mdb.core.shared.transformation.impl.ResultSetToJSONTransformation;
import mdb.core.shared.transport.IRequestData;
import mdb.core.shared.transport.Request;
import mdb.core.shared.transport.RequestEntity;
import mdb.core.shared.transport.IRequestData.ExecuteType;
import document.ui.server.communication.rpc.mdbgw.MdbRequester;
import document.ui.shared.MdbEntityConst;

/**
 * @author azhuk
 * Creation date: Apr 30, 2015
 *
 */
public class DocDataHelper {
	private static final Logger _logger = Logger.getLogger(DocDataHelper.class
			.getCanonicalName());
	

	public  static HashMap<String, String>  getDocumentFlowInfo (long documentId) {
		Request req = new Request();
		
		RequestEntity reqEntity = new RequestEntity (MdbEntityConst.FLOW_ENTITY_ID);
		reqEntity.getParams().add("ID_DOC", String.valueOf(documentId) );
		req.add(reqEntity);
		 MdbRequester  mdbRequester = new MdbRequester();
		Request responce =  mdbRequester.call(req);
		
		 for (IRequestData resEntity : responce.getArrayListValues() ) {
			 if ( resEntity.getEntityId() == MdbEntityConst.FLOW_ENTITY_ID) {
				 
				 List<HashMap<String, String>> lstMap = ResultSetToJSONTransformation.deserialise(resEntity.getData() );
					
					if (lstMap != null && lstMap.size() >0) {						
						return lstMap.get(0);
					}									
			 }
		 }		 
		 return null;		
	}
	
	public static  HashMap<String, String> getDocCard(long documentId)  {
		
		String entityId = String.valueOf(MdbEntityConst.DocCard);
		Params params = new Params();
		params.add("ID_DOC", String.valueOf(documentId)) ;
		
		MdbRequester  mdbRequester = new MdbRequester();
		Request	req= mdbRequester.getNewRequest(MdbEntityConst.DocCard, ExecuteType.GetData, params );
		Request response = mdbRequester.call(req);
		IRequestData data= response.get( entityId   );		
		
		List<HashMap<String, String>> lstMap = ResultSetToJSONTransformation.deserialise(data.getData() );
		
		return lstMap.size() > 0 ? lstMap.get(0): null;
	}

	public static  HashMap<String, String> getEmployeeData(long initiatorId)  {
		
		String entityId = String.valueOf(MdbEntityConst.EmpById);
		Params params = new Params();
		params.add("OFFICER_NUM", String.valueOf(initiatorId)) ;
		
		MdbRequester  mdbRequester = new MdbRequester();
		
		Request	req= mdbRequester.getNewRequest(MdbEntityConst.EmpById, ExecuteType.GetData, params );
		
		Request response = mdbRequester.call(req);
		IRequestData data= response.get( entityId   );		
		
		List<HashMap<String, String>> lstMap = ResultSetToJSONTransformation.deserialise(data.getData() );
		
		return lstMap.size() > 0 ? lstMap.get(0): null;
	}
	
	/**
	 * @param documentId
	 * @param initiatorId
	 * @return
	 */
	public static Map<String, String> getDocAproveCurrentUser(long documentId,
			int initiatorId) {
		String entityId = String.valueOf(MdbEntityConst.ACCEPTING_EMP);
		Params params = new Params();
		params.add("ID_DOC", String.valueOf(documentId)) ;
		params.add("OFFICER_NUM", String.valueOf(initiatorId)) ;
		
		MdbRequester  mdbRequester = new MdbRequester();
		Request	req= mdbRequester.getNewRequest(MdbEntityConst.ACCEPTING_EMP, ExecuteType.GetData, params );
		Request response = mdbRequester.call(req);
		IRequestData data= response.get( entityId   );		
		
		List<HashMap<String, String>> lstMap = ResultSetToJSONTransformation.deserialise(data.getData() );
		
		return lstMap.size() > 0 ? lstMap.get(0): null;
	}
	
	
}
