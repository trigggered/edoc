/**
 * 
 */
package document.ui.server.communication.rpc.flow;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import mdb.core.shared.transformation.impl.JSONTransformation;
import mdb.core.shared.transport.IRequestData.ExecuteType;
import mdb.core.shared.transport.Request;
import mdb.core.shared.transport.RequestEntity;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

import document.ui.client.commons.EDocStatus;
import document.ui.client.communication.rpc.flow.DocumentFlowService;
import document.ui.client.resources.locales.Captions;
import document.ui.server.communication.rpc.mail.MailingServiceImpl;
import document.ui.server.communication.rpc.mdbgw.MdbRequester;
import document.ui.server.mail.EMailType;
import document.ui.server.utils.DocDataHelper;
import document.ui.shared.MdbEntityConst;
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
	
	
	MailingServiceImpl _mailingService = new MailingServiceImpl();	
	
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
	public void sendRemember(long documentId, final String infoMessage,  int initiatorId) {

		callActionFlowProcess(ACTTION_CHANGE_PROCESS_DEADLINE, documentId, infoMessage, initiatorId, 
				new HashMap<String, String>(){{
			        put("ID_STATUS",infoMessage);
			    }});	
		callStageAction(documentId, null);
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

		HashMap<String, String>  flowInfo = DocDataHelper.getDocumentFlowInfo (documentId) ;

		HashMap<String, String>  docCard = DocDataHelper.getDocCard(documentId);
		
		EDocStatus status = EDocStatus.fromInt(Integer.parseInt(docCard.get("ID_STATUS")));
		
		if (flowInfo== null ) {
		 return ;		 
		}
		
		EFlowStage stage = EFlowStage.fromInt(Integer.parseInt(flowInfo.get("ID_FLOW_STAGE")));
		

		
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
		
		callActionFlowProcess(ACTTION_CANCEL_PROCESS, documentId, infoMessage,initiatorId, null);

		
		final HashMap<String, String>	  flowInfo = DocDataHelper.getDocumentFlowInfo (documentId) ;									
			
		if ( flowInfo != null && flowInfo.size()>0) {
			EFlowStage stage = EFlowStage.fromInt(Integer.parseInt(flowInfo.get("ID_FLOW_STAGE")));
			_mailingService.sendCancelMessageToAuthor(stage, documentId,  infoMessage);				
		}
		else {
			_mailingService.sendInfoMessageTo(EMailType.ToAuthorCancelPublish, documentId, infoMessage);
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
			 if (EDocStatus.fromInt(toStatus) == EDocStatus.Revoked) {
				 _mailingService.sendInfoMessageTo(EMailType.RevokeDoc,documentId,infoMessage);
			 }
			 else {
				 _mailingService.sendInfoMessageTo(EMailType.ChangeDocStatus,documentId,infoMessage);
			 }
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
		
		_logger.info(String.format("Run process actionId =%s with params %s ",actionId,  jsonParamsStr));		 
				 
		RequestEntity reqEntity = new RequestEntity (MdbEntityConst.FLOW_ENTITY_ID);
		reqEntity.setExecuteType(ExecuteType.ExecAction);
		reqEntity.setExecActionData(actionId, jsonParamsStr, null);
		
		Request req = new Request();
		req.add(reqEntity);
		
		_mdbRequester.call(req);												
		
	}



	/* (non-Javadoc)
	 * @see document.ui.client.communication.rpc.flow.DocumentFlowService#sendApproveResult(boolean, long, java.lang.String, int)
	 */
	@Override
	public void sendApproveResult(long documentId, String infoMessage,  int initiatorId) {
		Map<String, String> mapDoc = DocDataHelper.getDocCard(documentId);
		
		Map<String, String> mapApproveResult = DocDataHelper.getDocAproveCurrentUser(documentId, initiatorId);
		
		if (mapApproveResult == null || mapApproveResult.size() == 0) {
			return;
		}
		
		  Boolean result =mapApproveResult.get("IS_ACCEPT").equals("1")?true:false;
		  infoMessage = mapApproveResult.get("FULL_NAME") + "\n"+mapApproveResult.get("NOTE");
				
		_mailingService.sendInfoMessageTo(result?EMailType.ApproveCurentUser:EMailType.NotApproveCurentUser, documentId, infoMessage);
		
		EDocStatus status  =  EDocStatus.fromInt(Integer.parseInt( mapDoc.get("ID_STATUS")));
		switch (status) {
			case Draft:
				//_mailingService.sendInfoMessageTo(EMailType.DocumentApproved, documentId, infoMessage);
				break;
			case Approval:
				_mailingService.sendInfoMessageTo(EMailType.DocumentApproved, documentId, Captions.DocAllApproved);
				break;
				
			default:
				break;
		}	 			 
		 
	}



	/* (non-Javadoc)
	 * @see document.ui.client.communication.rpc.flow.DocumentFlowService#publishDoc(int, java.lang.String, int)
	 */
	@Override
	public boolean publishDoc(int documentId, String infoMessage, int initiatorId) {
		callActionFlowProcess(ACTION_SEND_DOC_TO_NEXT_STAGE, documentId, infoMessage, initiatorId, null);
		_mailingService.sendInfoMessageTo(EMailType.DocumentPublished , documentId, null);
		
		_mailingService.sendInfoMessageTo(EMailType.ToExecutor, documentId, "К исполнению");
		
		return true;
	}

}
