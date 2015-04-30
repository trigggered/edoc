/**
 * 
 */
package document.ui.server.mail;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;
import java.util.logging.Logger;

import mdb.core.shared.configuration.PropertyLoader;
import mdb.core.shared.data.Params;
import mdb.core.shared.gw.MailGatewayClientBuilder;
import mdb.core.shared.gw.RemoteServiceClientType;
import mdb.core.shared.gw.mail.IMailGatewayServiceRemote;
import mdb.core.shared.transformation.impl.ResultSetToJSONTransformation;
import mdb.core.shared.transport.IRequestData;
import mdb.core.shared.transport.IRequestData.ExecuteType;
import mdb.core.shared.transport.Request;
import document.ui.server.communication.rpc.flow.DocumentFlowServiceImpl.EFlowStage;
import document.ui.server.communication.rpc.mdbgw.MdbRequester;
import document.ui.shared.MdbEntityConst;


/**
 * @author azhuk
 * Creation date: Oct 1, 2014
 *
 */
@Deprecated
public class CopyOfMailingServiceImp  {
	
	private static final Logger _logger = Logger
			.getLogger(CopyOfMailingServiceImp.class.getName());		
	
	private static final String PROPERTIES_FILE = "MailTemplate.properties";

	private IMailGatewayServiceRemote _mailClient = MailGatewayClientBuilder.create(RemoteServiceClientType.EjbInvoke);
	private MdbRequester _mdbRequester = new MdbRequester();
	
	Properties _mailProp;
	List<String> _noMailFldList = new ArrayList<String>();  
	
	public CopyOfMailingServiceImp()  {
		_logger.info("Try load Properties file "); 
		_mailProp = PropertyLoader.loadProperties(PROPERTIES_FILE, this.getClass());
		if ( _mailProp != null) {  
			_logger.info("Properties file loading");
			
			fillNoMailingEmpList();
		}
	}
	

	private void sendInfoMessageTo(EMailType mailType, long documentId,
			String procDeadline, String author, String docTypeName,String docName ,String description) {
	
		String addFrom =_mailProp.getProperty("addrFrom");  
		_logger.info("Addr from "+addFrom);
		String subj = buildMailSubj(documentId, mailType );
		_logger.info("Subject is :" + subj);
		
		String content = buildMailContent(mailType, documentId ,procDeadline ,author,docTypeName,docName,  description) ;
		_logger.info("Content is " + content);
		
		
		String[]  recipients = getRecipientsAddr (mailType, documentId);
		
		_logger.info("Recipients is " + Arrays.toString(recipients));						
		
		sendMessage (recipients, addFrom, subj, content );				
	}
	
	public void sendInfoMessageToAcceptingEmp(long documentId,
			String procDeadline, String author, String docTypeName,String docName, String description) {
		
		sendInfoMessageTo( EMailType.ToAccepting , documentId,
				procDeadline, author, docTypeName,docName, description);						
		
	}	

	public void sendInfoMessageAboutPublished(long documentId,
			String procDeadline, String author, String docTypeName,String docName, String description) {
		_logger.info("sendInfoMessageAboutPublished docId="+documentId);
		sendInfoMessageTo( EMailType.DocumentPublished , documentId,
				procDeadline, author, docTypeName,docName, description);						
		
	}	
	

	public void sendInfoMsgAboutNewVersion(long documentId, String procDeadline, String author,
			String docTypeName, String docName, String description) {
		
		sendInfoMessageTo( EMailType.ToAcceptingNewVersion , documentId,
				procDeadline, author, docTypeName,docName, description);		
	}
	
	
	public void sendCancelMessageToAuthor(EFlowStage stage, long documentId,
			 String author, String docTypeName,String docName, String description) {
		
		//EFlowStage.InitSigne
		//EFlowStage.Signe
		
		EMailType etype =  EMailType.ToAuthorCancelPublish;
		
		switch (stage ) {
		case Unknown:
			etype =  EMailType.ToAuthorCancelPublish;
		case Approval:
			etype =  EMailType.ToAuthorCancelApproval;
			break;
		case Signe:
		case InitSigne:
			etype =  EMailType.ToAuthorCancelSign;					
			break;		
		}
		
		sendInfoMessageTo( etype, documentId,
				"", author, docTypeName,docName, description);						
		
	}
	
	
	public void sendToSignatoryEmp(long documentId, 
			String procDeadline, String author, String docTypeName,String docName, String description) {
		
		sendInfoMessageTo( EMailType.ToSignatory, documentId,
				procDeadline, author, docTypeName, docName, description);	
	}
	
	private void sendMessage (String[]  recipients, String addFrom, String subj, String content ) {
		if ( recipients== null || recipients.length == 0) {
			_logger.severe("Recipients list is empty");
		}
		else {
			_mailClient.send(addFrom,recipients,subj,content);
		}
	}
	
	private String buildMailSubj(long documentId, EMailType emailType ) {
		
		String propName = null;
		switch (emailType) {
			case ToAccepting:
				propName = "Subj_for_approval";				
				break;
			case ToSignatory:
				propName = "Subj_for_sign";
				break;
			case ToRecipients:				
				break;
			case ToExecutor:
				break;
			case ToAuthorCancelPublish:
				propName = "Subj_for_cancel_pub";
				break;
			case ToAuthorCancelApproval:
				propName = "Subj_for_cancel_approval";
				break;
			case ToAuthorCancelSign:
				propName = "Subj_for_cancel_sign";
				break;	
			case ToAcceptingNewVersion:
				propName = "Subj_for_approval_new_vers";
				break;
			case DocumentPublished:
				propName = "Subj_for_doc_pub";
				break;
		}
		
		String toReturn = _mailProp.getProperty(propName);
		return String.format(toReturn, documentId);		
	}
	
	
	/**
	 * @param documentId
	 * @param accepting
	 * @return
	 */
	private String buildMailContent(EMailType emailType, 
					long documentId, 
					String procDeadline, 
					String author, 
					String docTypeName,
					String docName,
					String description) {
		String propName = null;
		switch (emailType) {
			case ToAccepting:
				propName = "Body_for_approval";				
				break;
			case ToSignatory:
				propName = "Body_for_sign";
				break;
			case ToRecipients:
				break;
			case ToExecutor:
				break;
			case ToAuthorCancelPublish:
				propName = "Body_for_cancel_pub";
				break;
			case ToAuthorCancelApproval:
				propName = "Body_for_cancel_approval";
				break;
			case ToAuthorCancelSign:				
				propName = "Body_for_cancel_sign";
				break;
			case ToAcceptingNewVersion:
				propName = "Body_for_approval_new_ver";	
				break;
			case DocumentPublished:
				propName = "Body_for_doc_pub";
				break;
		}
		
		String toReturn = _mailProp.getProperty(propName);
		
		return String.format(toReturn, documentId, procDeadline, author,docTypeName , docName,  description,
				buildLink(documentId), procDeadline );	
	}
	
	
	/**
	 * @param documentId
	 * @return
	 */
	private String buildLink(long documentId) {
		String lnk = _mailProp.getProperty("open_doc_lnk");
		return String.format(lnk, getHost(),documentId );		
	}

	
	/**
	 * @return
	 */
	private static String getHost() {
		String host = System.getProperty("jboss.host.name");
		if ( host.contains("t-") ) {
			return host;
		}
		else
			return "doc.bnppua.net.intra";
		//return System.getProperty("jboss.host.name")+":8081";		
	}

	private String[]  getRecipientsAddr ( EMailType emailType, long documentId) {
		List<String> toReturn = new ArrayList<String>();		 
		Request req = null;		 
		Params params = new Params();
		params.add("ID_DOC", String.valueOf(documentId));
		int entittId = 0;
		
		String emailFldName = null;
		String isSendInfoFldName = null;
		String comareValue = null;
		
		switch (emailType) {
			case ToAccepting:
				entittId = MdbEntityConst.ACCEPTING_EMP;							
				emailFldName ="E_MAILE";
				isSendInfoFldName ="IS_ACCEPT";
				comareValue="0";
				break;
				
			case ToAcceptingNewVersion:
				entittId = MdbEntityConst.ACCEPTING_EMP;						
				emailFldName ="E_MAILE";
				isSendInfoFldName ="GET_INF_NEW_VER";
				comareValue="1";
				break;
				
			case ToSignatory:
				entittId =  MdbEntityConst.DocSignListEmp;	
				emailFldName ="E_MAILE";
				break;
				
			case ToRecipients:
				entittId = MdbEntityConst.E_MAIL_DOC_RECIPIENTS;
				params.add("RECIPIENTS_TYPE", "0");
				emailFldName ="E_MAILE";
				break;
				
			case ToExecutor:
				entittId = MdbEntityConst.E_MAIL_DOC_RECIPIENTS;
				params.add("RECIPIENTS_TYPE", "1");
				emailFldName ="E_MAILE";
				break;
				
			case ToAuthorCancelPublish:
			case ToAuthorCancelSign:
			case ToAuthorCancelApproval:
			case ToAuthorCancel:
			case DocumentPublished:
				entittId = MdbEntityConst.DocCard;
				emailFldName ="AUTHOR_E_MAILE";
				break;
			}
		
			req= _mdbRequester.getNewRequest(entittId , ExecuteType.GetData, params );
		
			Request response = _mdbRequester.call(req);
			IRequestData data= response.get( String.valueOf(entittId)  );
			
			List<HashMap<String, String>> lstMap = ResultSetToJSONTransformation.deserialise(data.getData() );
			
			if (lstMap != null) {
				_logger.info("Recipients count = " +lstMap.size());
				for (HashMap<String, String> map : lstMap) {
					if (isSendInfoFldName != null) {						
						String val =  map.get(isSendInfoFldName);						
						if (val ==null || val.length() == 0 || val.equalsIgnoreCase(comareValue)) {
							toReturn.add(map.get(emailFldName));
						}						
					}
					else 						
						toReturn.add(map.get(emailFldName));					
				}
			}	
			
			toReturn.removeAll(_noMailFldList);
			String [] dsf = new String[toReturn.size()];
			toReturn.toArray(dsf);		
			
		return dsf;
	}
	
	
	private void  fillNoMailingEmpList() {	
	
		int entittId = MdbEntityConst.NoMailingEmp;
		String emailFldName = "E_MAILE";
		
		Request req= _mdbRequester.getNewRequest( entittId, ExecuteType.GetData, null );
		
		Request response = _mdbRequester.call(req);
		IRequestData data= response.get( String.valueOf(entittId)  );		
		
		List<HashMap<String, String>> lstMap = ResultSetToJSONTransformation.deserialise(data.getData() );
		
		if (lstMap != null) {
			_logger.info("Recipients count = " +lstMap.size());
			for (HashMap<String, String> map : lstMap) {
				_noMailFldList.add(map.get(emailFldName));			
						
			}
		}		
	}	
}
