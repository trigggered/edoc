/**
 * 
 */
package document.ui.client.commons.checkers;

import java.util.logging.Logger;
import mdb.core.ui.client.events.ICallbackEvent;
import com.smartgwt.client.data.Record;
import document.ui.shared.MdbEntityConst;
import mdb.core.ui.client.data.checkers.SimpleChecker;
/**
 * @author azhuk
 * Creation date: Oct 16, 2014
 *
 */
public class CheckGenerateDocCode extends SimpleChecker {
	private static final Logger _logger = Logger
			.getLogger(CheckGenerateDocCode.class.getName());
	
	public void generate (final String idDoc, final ICallbackEvent<String> result) {
		
		getParams().add("id_doc",  idDoc );
		
		check(MdbEntityConst.CheckDocCode, new ICallbackEvent<Record[]>() {
			
			@Override
			public void doWork(Record[] data) {
				if (data != null &&  data.length >0) {
					String val = data[0].getAttribute("DOC_CODE");
					 if (val != null ) {						 
						 _logger.info("For Document id ="+  idDoc +" generated doc_code = "+ val);
						 
						  result.doWork(val);
					 }
					 else {
						 _logger.info("Cann not generate doc code for Document id ="+ idDoc );
						 result.doWork(null);
					 }
				}
				
			}
		});
		
		
	}
}
