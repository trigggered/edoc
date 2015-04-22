/**
 * 
 */
package document.ui.client.commons.checker;

import java.util.logging.Logger;

import mdb.core.ui.client.app.AppController;
import mdb.core.ui.client.events.ICallbackEvent;

import com.smartgwt.client.data.Record;

import document.ui.shared.MdbEntityConst;

/**
 * @author azhuk
 * Creation date: Oct 30, 2014
 *
 */
public class CheckDocumentFlowStage extends Checker {
	private static final Logger _logger = Logger
			.getLogger(CheckDocumentFlowStage.class.getName());
	
	
	private static  CheckDocumentFlowStage _instance;  
	
	public static CheckDocumentFlowStage getInstance() {
		
		if (_instance == null) {
			_instance  = new CheckDocumentFlowStage();
		}
		return _instance;
	}
		
	
	public void getFlowStage(final long documentId,  final ICallbackEvent<Integer> result ) {
		
		getParams().add("id_doc", String.valueOf(documentId));				
		
		check(MdbEntityConst.FLOW_ENTITY_ID, new ICallbackEvent<Record[]>() {
			
			@Override
			public void doWork(Record[] data) {
				if (data != null &&  data.length >0) {
					String val = data[0].getAttribute("ID_FLOW_STAGE");
					 if (val != null ) {						 
						 _logger.info("Document id ="+ documentId+" is exist");						  						 
						  result.doWork( Integer.valueOf(val) );
					 }
					 else {
						 _logger.info("Flow process for Document id ="+ documentId+" is NOT exist");
						 //result.doWork(ECorrespondentType.UNKNOWN);
					 }
				}
				
			}
		});
		
	}
	
	
	public void checkFailedStage(final String documentId,  final ICallbackEvent<Boolean> result ) {
		
		getParams().add("ID_DOC", documentId);
		getParams().add("CURRENT_USER", String.valueOf(AppController.getInstance().getCurrentUser().getId()));
		
		
		check(MdbEntityConst.CHECK_FAILED_STAGE, new ICallbackEvent<Record[]>() {
			
			@Override
			public void doWork(Record[] data) {
				if (data != null &&  data.length >0) {					 
					_logger.info("Failed flow process for Document id ="+ documentId+" is exist");
					result.doWork(true);
				} else {
					_logger.info("Failed flow process for Document id ="+ documentId+" is NOT exist");
					result.doWork(false);
				}
				
			}
		});
	}

}