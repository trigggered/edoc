/**
 * 
 */
package document.ui.client.commons.checkers;

import java.util.logging.Logger;

import mdb.core.ui.client.app.AppController;
import mdb.core.ui.client.events.ICallbackEvent;
import mdb.core.ui.client.data.checkers.SimpleChecker;

import com.smartgwt.client.data.Record;
import com.smartgwt.client.util.BooleanCallback;

import document.ui.shared.MdbEntityConst;

/**
 * @author azhuk
 * Creation date: Oct 16, 2014
 *
 */
public class CheckCanSignatoryDoc extends SimpleChecker {
	private static final Logger _logger = Logger
			.getLogger(CheckCanSignatoryDoc.class.getName());
	
	
	public void isCan(long docId,final BooleanCallback result) {
		
		
		getParams().add("ID_DOC", String.valueOf(docId));
		getParams().add("INITIAL_ID", String.valueOf(AppController.getInstance().getCurrentUser().getId()));
		
		check(MdbEntityConst.CheckCanSignatoryDoc, new ICallbackEvent<Record[]>() {
			
			@Override
			public void doWork(Record[] data) {
				if (data != null &&  data.length >0) {
					String val = data[0].getAttribute("IS_CAN_SIGN");
					 if (val != null ) {
						 
						 _logger.info("Result CheckCanSignatoryDoc  is = "+val);
						  boolean isCan  = Integer.valueOf(val) >0 ;
						  
						  result.execute(isCan);
					 }
				}
				
			}
		});
	}
}
