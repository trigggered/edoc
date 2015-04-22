/**
 * 
 */
package document.ui.client.commons.checker;

import java.util.logging.Logger;

import com.smartgwt.client.data.Record;
import com.smartgwt.client.util.BooleanCallback;

import mdb.core.ui.client.app.AppController;
import mdb.core.ui.client.events.ICallbackEvent;
import document.ui.client.commons.EDocStatus;
import document.ui.client.view.doc.card.DocumentCard;
import document.ui.shared.MdbEntityConst;

/**
 * @author azhuk
 * Creation date: Nov 18, 2014
 *
 */
public class CheckCanApproval extends Checker {
	private static final Logger _logger = Logger
			.getLogger(CheckCanApproval.class.getName());

	
	public  void isCurrentUserHasRight(DocumentCard card, final BooleanCallback result) {	
		
		isCan(card.getDocumentStatus(), card.getDocumentId(), 0, result) ;
	}
	
	
	/**
	 * @param documentStatus
	 * @param intValue
	 * @return
	 */
	public  void isCan(EDocStatus documentStatus, long documentId, int selectedApprovUsrId, final BooleanCallback result) {

		if (documentStatus != EDocStatus.AtTheApproval) {
			result.execute(false);
			return ;
		}		
		  
				int currentUser = AppController.getInstance().getCurrentUser().getId();
		
				if( selectedApprovUsrId == currentUser)  {
					result.execute(true);
					return;
				}
				else  {
					
					getParams().add("ID_DOC", String.valueOf(documentId) );
					getParams().add("OFFICER_NUM", String.valueOf(currentUser));
					if ( selectedApprovUsrId>0 ) {
						getParams().add("APPR_OFFICER_NUM", String.valueOf(selectedApprovUsrId) );
					}
					
					
					check(MdbEntityConst.CheckCanApprovDoc, new ICallbackEvent<Record[]>() {
						
						@Override
						public void doWork(Record[] data) {
							if (data != null &&  data.length >0) {
								String val = data[0].getAttribute("IS_CAN");
								 if (val != null ) {
									 
									 _logger.info("Result CheckCanApprovalDoc  is = "+val);
									  boolean isCan  = Integer.valueOf(val) >0 ;
									  
									  result.execute(isCan);
								 }
							}
							
						}
					});		
								
				}		
		
	}
}

