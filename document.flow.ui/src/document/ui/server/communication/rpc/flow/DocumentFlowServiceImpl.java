/**
 * 
 */
package document.ui.server.communication.rpc.flow;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import javax.print.Doc;

import mdb.core.shared.transformation.impl.ResultSetToJSONTransformation;
import mdb.core.shared.transport.IRequestData;
import mdb.core.shared.transport.IRequestData.ExecuteType;
import mdb.core.shared.transport.Request;
import mdb.core.shared.transport.RequestEntity;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

import document.ui.client.commons.EDocStatus;
import document.ui.client.communication.rpc.flow.DocumentFlowService;
import document.ui.server.communication.rpc.mail.MailingServiceImp;
import document.ui.server.communication.rpc.mdbgw.MdbRequester;
import document.ui.server.mail.EMailType;
import document.ui.server.utils.DocDataHelper;
import document.ui.shared.MdbEntityConst;
import mdb.core.shared.transformation.impl.JSONTransformation;
/**
 * @author azhuk
 * Creation date: Aug 14, 2014
 *
 */
public class DocumentFlowServiceImpl  extends RemoteServiceServlet implements DocumentFlowService{

	final int ACTION_SEND_DOC_TO_NEXT_STAGE = 2985;
	final int ACTTION_CANCEL_PROCESS = 3024;
	final int ACTTION_END_ALL_PROCESS = 3097;
	final int ACTTION_CHANGE_PROCESS_DEADLINE = 3098;
	
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
		
		callActionFlowProcess(ACTION_SEND_DOC_TO_NEXT_STAGE, documentId, infoMessage, initiatorId, null);		
		
		callStageAction (documentId, infoMessage);		
		
		return true;
	}
	
	
	
	@Override
	public void sendRemember(long documentId, String infoMessage,  int initiatorId) {
		callActionFlowProcess(ACTTION_CHANGE_PROCESS_DEADLINE, documentId, infoMessage, initiatorId, null);
		callStageAction(documentId, null);
	}
	
	@Override
	public void sendPublishedInfoMsg(long documentId, String infoMessage,  int initiatorId) {		
		_mailingService.sendInfoMessageTo(EMailType.DocumentPublished , documentId, null);
	}
	
	
	@Override
	public void sendApproveCurrentUserInfoMsg(long documentId, boolean approved , String infoMessage,
			String  initName ) {		
		_mailingService.sendInfoMessageTo(approved?EMailType.ApproveCurentUser:EMailType.NotApproveCurentUser
			, documentId, infoMessage);
	}
	
	
	/**
	 * @param documentId
	 */
	private void callStageAction(long documentId, String infoMessage) {

		HashMap<String, String>  info = DocDataHelper.getDocumentFlowInfo (documentId) ;
		
		if (info== null ) {
		 return ;		 
		}
		
		EFlowStage stage = EFlowStage.fromInt(Integer.parseInt(info.get("ID_FLOW_STAGE")));
		
		String procDeadline = info.get("DEADLINE");
		/*
		String author = info.get("EMP_AUTHOR");
		String docTypeName = info.get("CORR_TYPE_FULL");;
		String docName = info.get("NAME");;
		String description = info.get("INFO_MSG");		
		*/
		
		switch ( stage   ) {
		case Approval:
			_mailingService.sendInfoMessageTo(EMailType.ToAccepting , documentId, infoMessage);			
			break;
		case  InitSigne:
			break;
		case Signe:
			_mailingService.sendInfoMessageTo(EMailType.ToSignatory, documentId, infoMessage);			
			break;
		default:
			break;
		}				

		
	}

	public  static EFlowStage getDocumentFlowStage(long documentId) {		
		HashMap<String, String> flowInfo =DocDataHelper.getDocumentFlowInfo (documentId) ;
		
		if (flowInfo!= null ) {
			return EFlowStage.fromInt(Integer.parseInt(flowInfo.get("ID_FLOW_STAGE")));
		}
		else return EFlowStage.Unknown;		
	}
	


	/* (non-Javadoc)
	 * @see document.ui.client.communication.rpc.flow.DocumentFlowService#cancelProcess(long)
	 */
	@Override
	public void cancelProcess(long documentId, String infoMessage,  int initiatorId) {
		
		EFlowStage stage = EFlowStage.Unknown;
		
		final HashMap<String, String>	  flowInfo = DocDataHelper.getDocumentFlowInfo (documentId) ;
		
		
		if (flowInfo != null ) {
		
			callActionFlowProcess(ACTTION_CANCEL_PROCESS, documentId, infoMessage,initiatorId, new HashMap<String, String>(){{
		        put("ID_FLOW", flowInfo.get("ID_FLOW"));
		    }});	
							
			
			
		 stage = EFlowStage.fromInt(Integer.parseInt(flowInfo.get("ID_FLOW_STAGE")));
			//String procDeadline = info.get("DEADLINE"); 
		 String author = flowInfo.get("EMP_AUTHOR");
		 String docTypeName = flowInfo.get("CORR_TYPE_FULL");;
		 String docName = flowInfo.get("NAME");;
			
			_mailingService.sendCancelMessageToAuthor(stage, documentId, author, docTypeName, docName, infoMessage);
		}					
		else {
			_logger.severe(String.format("Not found flow for documen ID =%s",documentId ));
			 return ;
		}							
		
		
		
	}

	/* (non-Javadoc)
	 * @see document.ui.client.communication.rpc.flow.DocumentFlowService#endAllProcess(long, java.lang.String, int, int)
	 */
	@Override
	public void forcedDocumentToStatus(long documentId, String infoMessage,
			int initiatorId, final int toStatus) {
		
		
		callActionFlowProcess(ACTTION_END_ALL_PROCESS, documentId, infoMessage,initiatorId, new HashMap<String, String>(){{
	        put("ID_STATUS",String.valueOf(toStatus));
	    }});	
			
			_mailingService.sendInfoMessageTo(EMailType.ChangeDocStatus,documentId,infoMessage);		
	}	
	
	private void callActionFlowProcess(int actionId, long documentId, String infoMessage,
			int initiatorId, Map<String, String> callParams  ) {
		
		Map<String, String> mapParams = new HashMap<String,String>();
		
		if (callParams!= null) {
			mapParams.putAll(callParams);
		}
		
		mapParams.put("ID_DOC", String.valueOf(documentId) );
		mapParams.put("INFOMSG", infoMessage);
		mapParams.put("INITIATOR_ID", String.valueOf(initiatorId));	
		
		 
		
		String  jsonParamsStr= JSONTransformation.map2json(mapParams);
		
		
		_logger.info(String.format("Run process actionId =%s with params %s ",initiatorId,  jsonParamsStr));
		 
				 
		RequestEntity reqEntity = new RequestEntity (MdbEntityConst.FLOW_ENTITY_ID);
		reqEntity.setExecuteType(ExecuteType.ExecAction);
		reqEntity.setExecActionData(actionId, jsonParamsStr, null);
		
		Request req = new Request();
		req.add(reqEntity);
		
		_mdbRequester.call(req);												
		
	}

}
