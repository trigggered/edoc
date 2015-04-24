/**
 * 
 */
package document.ui.server.communication.rpc.flow;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import mdb.core.shared.transformation.impl.ResultSetToJSONTransformation;
import mdb.core.shared.transport.IRequestData;
import mdb.core.shared.transport.IRequestData.ExecuteType;
import mdb.core.shared.transport.Request;
import mdb.core.shared.transport.RequestEntity;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

import document.ui.client.communication.rpc.flow.DocumentFlowService;
import document.ui.server.communication.rpc.mail.MailingServiceImp;
import document.ui.server.communication.rpc.mdbgw.MdbRequester;
import document.ui.shared.MdbEntityConst;
import mdb.core.shared.transformation.impl.JSONTransformation;
/**
 * @author azhuk
 * Creation date: Aug 14, 2014
 *
 */
public class DocumentFlowServiceImpl  extends RemoteServiceServlet implements DocumentFlowService{

	final int ACTTION_CANCEL_PROCESS = 3024;
	final int ACTTION_END_ALL_PROCESS = 3097;
	
	public enum EFlowStage {
		  Unknown(0),
		  Approval(1),
		  InitSigne(2),
		  Signe(3);
		  
		   private int _value;
			
			public int getValue() {
			    return _value;
		   }
			
			private EFlowStage (int value) {
				_value = value;
			}
			
			
			public static EFlowStage fromInt(int value) {	
				switch (value) {
				case 1: 
					return Approval;
				case 2:
					return InitSigne;
				case 3:
					return Signe;
				
				}
				return Unknown;
				
			}
	}
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final Logger _logger = Logger.getLogger(
			DocumentFlowServiceImpl.class.getName());

	
	
	MailingServiceImp _mailingService = new MailingServiceImp();	
	
	MdbRequester _mdbRequester = new MdbRequester();
	
	/* (non-Javadoc)
	 * @see document.ui.client.communication.rpc.flow.IDocumentFlowProcessing#sendToTheNextStage(long, java.lang.String)
	 */
	@Override
	public boolean sendToTheNextStage(long documentId, String infoMessage,  int initiatorId) {
		_logger.info("User Id = "+ initiatorId +" Send doc to next stage. DocId = "+documentId);			
		
		Map<String, String> mapParams = new HashMap<String,String>();
		mapParams.put("id_doc", String.valueOf(documentId) );
		mapParams.put("infoMsg", infoMessage);
		mapParams.put("initialId", String.valueOf(initiatorId));
		
		String  jsonParamsStr= JSONTransformation.map2json(mapParams);
		
		_logger.info("Send document to next stage with parameters = "+ jsonParamsStr);
		RequestEntity reqEntity = new RequestEntity (MdbEntityConst.FLOW_ENTITY_ID);
		reqEntity.setExecuteType(ExecuteType.ExecAction);
		reqEntity.setExecActionData(2985, jsonParamsStr, null);
		
		Request req = new Request();
		req.add(reqEntity);
		
		_mdbRequester.call(req);
		
		callStageAction (documentId);		
		
		return true;
	}
	
	@Override
	public void sendRemember(long documentId) {
		callStageAction(documentId);
	}
	
	@Override
	public void sendPublishedInfoMsg(long documentId) {		
		
		_mailingService.sendInfoMessageAboutPublished(documentId,"", "", "",  "", "");
	}
	
	/**
	 * @param documentId
	 */
	private void callStageAction(long documentId) {

		HashMap<String, String>  info = getDocumentFlowInfo (documentId) ;
		
		if (info== null ) {
		 return ;		 
		}
		
		EFlowStage stage = EFlowStage.fromInt(Integer.parseInt(info.get("ID_FLOW_STAGE")));
		String procDeadline = info.get("DEADLINE"); 
		String author = info.get("EMP_AUTHOR");
		String docTypeName = info.get("CORR_TYPE_FULL");;
		String docName = info.get("NAME");;
		String description = info.get("INFO_MSG");
		
		
		switch ( stage   ) {
		case Approval:
			_mailingService.sendInfoMessageToAcceptingEmp(documentId,procDeadline, author, docTypeName,  docName, description);
			break;
		case  InitSigne:
			break;
		case Signe:
			_mailingService.sendToSignatoryEmp(documentId,procDeadline, author, docTypeName,  docName, description);
			break;
		default:
			break;
		}				

		
	}

	public  static EFlowStage getDocumentFlowStage(long documentId) {		
		HashMap<String, String> flowInfo = getDocumentFlowInfo (documentId) ;
		
		if (flowInfo!= null ) {
			return EFlowStage.fromInt(Integer.parseInt(flowInfo.get("ID_FLOW_STAGE")));
		}
		else return EFlowStage.Unknown;		
	}
	

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

	/* (non-Javadoc)
	 * @see document.ui.client.communication.rpc.flow.DocumentFlowService#cancelProcess(long)
	 */
	@Override
	public void cancelProcess(long documentId, String infoMessage,  int initiatorId) {
		
		EFlowStage stage = EFlowStage.Unknown;
		
		HashMap<String, String>	  flowInfo = getDocumentFlowInfo (documentId) ;
		
		
		//String procDeadline = null; 
		String author = null;
		String docTypeName = null;
		String docName = null;
		String description = infoMessage;
		
		if (flowInfo != null ) {
		
			Map<String, String> mapParams = new HashMap<String,String>();
			mapParams.put("ID_DOC", String.valueOf(documentId) );
			mapParams.put("ID_FLOW", flowInfo.get("ID_FLOW"));
			mapParams.put("INFOMSG", infoMessage);
			mapParams.put("INITIATOR_ID", String.valueOf(initiatorId));
			
			
			String  jsonParamsStr= JSONTransformation.map2json(mapParams);
			
			_logger.info("CANCEL PROCESS   = "+ jsonParamsStr);
			 
					 
			RequestEntity reqEntity = new RequestEntity (MdbEntityConst.FLOW_ENTITY_ID);
			reqEntity.setExecuteType(ExecuteType.ExecAction);
			reqEntity.setExecActionData(ACTTION_CANCEL_PROCESS , jsonParamsStr, null);
			
			Request req = new Request();
			req.add(reqEntity);
			
			_mdbRequester.call(req);		
			
			
			 stage = EFlowStage.fromInt(Integer.parseInt(flowInfo.get("ID_FLOW_STAGE")));
			//String procDeadline = info.get("DEADLINE"); 
			author = flowInfo.get("EMP_AUTHOR");
			docTypeName = flowInfo.get("CORR_TYPE_FULL");;
			docName = flowInfo.get("NAME");;
			
		
		}					
		else {
			_logger.severe(String.format("Not found flow for documen ID =%s",documentId ));
			 return ;
		}							
		
		_mailingService.sendCancelMessageToAuthor(stage, documentId, author, docTypeName, docName, description);
		
	}

	/* (non-Javadoc)
	 * @see document.ui.client.communication.rpc.flow.DocumentFlowService#endAllProcess(long, java.lang.String, int, int)
	 */
	@Override
	public void forcedDocumentToStatus(long documentId, String infoMessage,
			int initiatorId, int toStatus) {

		EFlowStage stage = EFlowStage.Unknown;		
		
		/*
		String author = null;
		String docTypeName = null;
		String docName = null;
		String description = infoMessage;
		
		*/
		
			Map<String, String> mapParams = new HashMap<String,String>();
			mapParams.put("ID_DOC", String.valueOf(documentId) );
			mapParams.put("INFOMSG", infoMessage);
			mapParams.put("INITIATOR_ID", String.valueOf(initiatorId));
			mapParams.put("ID_STATUS", String.valueOf(toStatus));
			
			String  jsonParamsStr= JSONTransformation.map2json(mapParams);
			
			_logger.info("RUN END ALL PROCESS   = "+ jsonParamsStr);
			 
					 
			RequestEntity reqEntity = new RequestEntity (MdbEntityConst.FLOW_ENTITY_ID);
			reqEntity.setExecuteType(ExecuteType.ExecAction);
			reqEntity.setExecActionData(ACTTION_END_ALL_PROCESS, jsonParamsStr, null);
			
			Request req = new Request();
			req.add(reqEntity);
			
			_mdbRequester.call(req);											
		
			//_mailingService.sendCancelMessageToAuthor(stage, documentId, author, docTypeName, docName, description);
		
	}	
	

}
