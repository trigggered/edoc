/**
 * 
 */
package document.ui.client.commons.checkers;

import java.util.logging.Logger;

import mdb.core.ui.client.app.AppController;
import mdb.core.ui.client.events.ICallbackEvent;
import mdb.core.ui.client.view.dialogs.message.Dialogs;
import mdb.core.ui.client.data.checkers.SimpleChecker;

import com.smartgwt.client.data.Record;

import document.ui.client.commons.ECorrespondentType;
import document.ui.client.resources.locales.Captions;
import document.ui.shared.MdbEntityConst;

/**
 * @author azhuk
 * Creation date: Jun 11, 2014
 *
 */
public class CheckDocumentExists  extends SimpleChecker {

	private static final Logger _logger = Logger
			.getLogger(CheckDocumentExists.class.getName());

	private static CheckDocumentExists _checkDocumentExists =  new CheckDocumentExists();
	
	public void isDocExist(final String documentId,  final ICallbackEvent<ECorrespondentType> result ) {
		
		getParams().add("ID_DOC", documentId);
		getParams().add("CURRENT_USER", String.valueOf(AppController.getInstance().getCurrentUser().getId()));
		
		_logger.info("Check isDocExist for documentId="+documentId+" and user id = "+AppController.getInstance().getCurrentUser().getId());
		
		check(MdbEntityConst.CheckDocExist, new ICallbackEvent<Record[]>() {
			
			@Override
			public void doWork(Record[] data) {
				if (data != null &&  data.length >0) {
					String val = data[0].getAttribute("CORR_TYPE");
					 if (val != null ) {						 
						 _logger.info("Document id ="+ documentId+" is exist");						  						 
						  result.doWork(ECorrespondentType.fromString(val));
					 }
					 else {					 
						 
						 _logger.info("UNKNOWN document type");
						 result.doWork(ECorrespondentType.UNKNOWN);
					 }
				}	
				else {
					
					_logger.info("Document id ="+ documentId+" is NOT exist or NOT user Right");
					CheckDocumentFlowStage.getInstance().checkFailedStage(documentId,  new ICallbackEvent<Boolean>() {
						
						@Override
						public void doWork(Boolean data) {
							if (data) {
								Dialogs.ShowWarnMessage(Captions.PROCESS_APP_SIGN_STOP );
							} else {
								result.doWork(ECorrespondentType.UNKNOWN);		
							}
						}
					});										
				}
			}
		});		
	}
	
	
	public static void isExist(final String documentId,  final ICallbackEvent<ECorrespondentType> result ) {
		_checkDocumentExists.isDocExist(documentId, result);
	}

}
