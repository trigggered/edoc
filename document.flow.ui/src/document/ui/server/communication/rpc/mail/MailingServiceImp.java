/**
 * 
 */
package document.ui.server.communication.rpc.mail;

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
import document.ui.client.commons.EDocStatus;
import document.ui.server.communication.rpc.flow.DocumentFlowServiceImpl.EFlowStage;
import document.ui.server.communication.rpc.mdbgw.MdbRequester;
import document.ui.server.mail.EMailType;
import document.ui.server.mail.FlowMsgBuilder;
import document.ui.server.mail.Message;
import document.ui.shared.MdbEntityConst;


/**
 * @author azhuk
 * Creation date: Oct 1, 2014
 *
 */
public class MailingServiceImp  {
	
	private static final Logger _logger = Logger
			.getLogger(MailingServiceImp.class.getName());		
	
	private static final String PROPERTIES_FILE = "MailTemplate.properties";

	private IMailGatewayServiceRemote _mailClient = MailGatewayClientBuilder.create(RemoteServiceClientType.EjbInvoke);
	private MdbRequester _mdbRequester = new MdbRequester();
	
	Properties _mailProp;
	List<String> _noMailFldList = new ArrayList<String>();  
	
	FlowMsgBuilder _floMsgBuilder;
	
	public MailingServiceImp()  {
		_logger.info("Try load Properties file "); 
		_mailProp = PropertyLoader.loadProperties(PROPERTIES_FILE, this.getClass());
		if ( _mailProp != null) {  
			_logger.info("Properties file loading");
			
			fillNoMailingEmpList();
		}
		
		_floMsgBuilder = new FlowMsgBuilder(_mailProp, _noMailFldList);
	}
	

	public  void sendInfoMessageTo(EMailType mailType, long documentId, String infoMessage) {	
		
		Message msg = _floMsgBuilder.createMessage(mailType, documentId, infoMessage);
		sendMessage(msg);				
	}
	

	
	public void sendCancelMessageToAuthor(EFlowStage stage, long documentId,
			 String author, String docTypeName,String docName, String description) {
				
		EMailType etype =  EMailType.CancelPublishEmailToAuthor;
		
		switch (stage ) {
		case Unknown:
			etype =  EMailType.CancelPublishEmailToAuthor;
		case Approval:
			etype =  EMailType.CancelApprovalEmailToAuthor;
			break;
		case Signe:
		case InitSigne:
			etype =  EMailType.CancelSignEmailToAuthor;					
			break;		
		}
		
		sendInfoMessageTo( etype, documentId, description);						
		
	}	
	
	
	private void sendMessage (Message msg) {
		
		if ( msg.getRecipients() == null || msg.getRecipients().length == 0) {
			_logger.severe("Recipients list is empty");
		}
		else				
		_mailClient.send(msg.getFrom(), msg.getRecipients(), msg.getSubject(),  msg.getBody());
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
