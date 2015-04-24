/**
 * 
 */
package document.ui.server.mail;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import mdb.core.shared.transformation.impl.ResultSetToJSONTransformation;
import mdb.core.shared.transport.IRequestData;
import mdb.core.shared.transport.Request;
import mdb.core.shared.data.Params;
import mdb.core.shared.transport.IRequestData.ExecuteType;
import document.ui.client.commons.EDocStatus;
import document.ui.server.communication.rpc.mdbgw.MdbRequester;
import document.ui.server.mail.template.ITemplateBuiled;
import document.ui.server.mail.template.VelocityBuilder;
import document.ui.server.utils.DocDataHelper;
import document.ui.shared.MdbEntityConst;

/**
 * @author azhuk
 * Creation date: Apr 29, 2015
 *
 */
public class FlowMsgBuilder {
	private static final Logger _logger = Logger.getLogger(FlowMsgBuilder.class
			.getName());

	private MdbRequester 	_mdbRequester = new MdbRequester();
	private List<String> 	_noMailFldList = new ArrayList<String>();
	private Properties 		_mailProp;
	
	
	final String Subj_for_approval_new_vers = "Добавлена новая версия документа. ";
	final String Subj_for_approval = "Запрос на согласование.";
	final String Subj_for_sign = "Запрос на подписание.";	 
	final String Subj_for_cancel_pub ="Отказано в публикации." ;
	final String Subj_for_cancel_sign = "Не подписано." ;
	final String Subj_for_cancel_approval ="Не согласовано.";
	final String Subj_for_doc_pub ="Документ опубликован.";
	
	final String Doc_Subject = "Тема документа: %s";

	ITemplateBuiled _templateBuiled = new VelocityBuilder(); 
	
	public FlowMsgBuilder (Properties mailProp, List<String> 	noMailFldList ) {
			_mailProp = mailProp;
			_noMailFldList = noMailFldList;
	}
	
	
	public Message  createMessage (EMailType mailType, long documentId, String infoMessage) {
		Message   toReturn = new Message() ;
		
		HashMap<String, String> docCard =  DocDataHelper.getDocCard(documentId);					
		docCard.put("infoMsg", infoMessage);
		
				
		toReturn.setFrom((String) _mailProp.getProperty("addrFrom"));
		
		toReturn.setSubject(getdMailSubj(mailType, docCard)+" "+String.format(Doc_Subject, docCard.get("NAME") )  );
		docCard.put("subject", toReturn.getSubject());
		
		
		toReturn.setRecipients(getRecipientsAddr ( mailType,  documentId) );
		toReturn.setBody(_templateBuiled.getMailBody(mailType, getBodyParameters (mailType, docCard) ));	
		
		_logger.info("Addr from "+toReturn.getFrom());
		_logger.info("Subject is :"+toReturn.getSubject());
		_logger.info("Recipients is " + Arrays.toString(toReturn.getRecipients()));
		_logger.info("Content is :"+toReturn.getBody());
		
		
		return toReturn;
	}
	
	private   Map<String, String> getBodyParameters(EMailType mailType, HashMap<String, String> docCard) {
		
		 Map<String, String> toReturn = new HashMap<String, String> ();		 		 
		 
		 String infoMessage = docCard.get ("infoMsg");
		 switch (mailType) {
		 
			case ToAcceptingNewVersion:								
				toReturn.put("infoMsg", infoMessage!=null?Subj_for_approval_new_vers+":"+infoMessage:" ");
				break;
			default:					
				toReturn.put("infoMsg", infoMessage!=null?"Комментарии: "+infoMessage:" ");
		}
		 
		 toReturn.put("author_name", docCard.get ("EMP_AUTHOR"));
		 toReturn.put("id_doc", docCard.get ("ID_DOC"));
		 toReturn.put("doc_type", docCard.get ("CORR_TYPE_FULL"));
		 toReturn.put("doc_subject", docCard.get ("NAME"));
		 toReturn.put("subject", docCard.get ("subject"));
		 toReturn.put("doc_note", docCard.get ("NOTE"));
		 String deadline = docCard.get ("deadline");
		 toReturn.put("deadline", deadline!=null?"Крайний срок: "+deadline:" " );		 		 
		 		 
		 toReturn.put("doc_link", getDocLink(docCard.get ("ID_DOC")) );
		 
		 return toReturn;
	}
	

	
	
	private String getdMailSubj(EMailType emailType,HashMap<String, String> docCard) {
		
		String toReturn = null;
		switch (emailType) {
			case ToAccepting:
				toReturn = Subj_for_approval;				
				break;
			case ToSignatory:
				toReturn = Subj_for_sign;
				break;
			case ToRecipients:				
				break;
			case ToExecutor:
				break;
			case CancelPublishEmailToAuthor:
				toReturn = Subj_for_cancel_pub;
				break;
			case CancelApprovalEmailToAuthor:
				toReturn = Subj_for_cancel_approval;
				break;
			case CancelSignEmailToAuthor:
				toReturn = Subj_for_cancel_sign;
				break;	
			case ToAcceptingNewVersion:
				toReturn = Subj_for_approval_new_vers;
				break;
			case DocumentPublished:
				toReturn = Subj_for_doc_pub;
			case ChangeDocStatus:
				EDocStatus status =  EDocStatus.fromInt( Integer.parseInt(docCard.get("ID_STATUS")));
				toReturn = status.toString();
				break;
			case ApproveCurentUser:
				toReturn = "Согласовано ";
				break;
			case NotApproveCurentUser:
				toReturn = "Не Согласовано ";
				break;
				
		}		
		
		return toReturn;		
	}
	
		
	private String getDocLink(String documentId) {
		String lnk = _mailProp.getProperty("open_doc_lnk");
		return String.format(lnk, getHost(),documentId );		
	}
	
	
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
				
			case CancelPublishEmailToAuthor:
			case CancelSignEmailToAuthor:
			case CancelApprovalEmailToAuthor:
			case DocumentPublished:
			case ApproveCurentUser:
			case NotApproveCurentUser:
			case ChangeDocStatus:
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
	
	
	
}
