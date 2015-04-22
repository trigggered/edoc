/**
 * 
 */
package document.ui.client.commons.checker;

import java.util.logging.Logger;

import mdb.core.shared.data.Params;
import mdb.core.ui.client.communication.impl.SimpleMdbDataRequester;
import mdb.core.ui.client.events.ICallbackEvent;

import com.smartgwt.client.data.Record;

/**
 * @author azhuk
 * Creation date: Oct 16, 2014
 *
 */
public class Checker {
	@SuppressWarnings("unused")
	private static final Logger _logger = Logger.getLogger(Checker.class
			.getName());
	
	SimpleMdbDataRequester _dataRequester; 
	ICallbackEvent<Record[]> _callBack;
	Params _params = new Params();
	
	/**
	 * @return the _params
	 */
	public Params getParams() {
		return _params;
	}

	public Checker(){
		_dataRequester = new SimpleMdbDataRequester(new ICallbackEvent<Record[]>() {
			
			@Override
			public void doWork(Record[] data) {
				_callBack.doWork(data);				
			}
		});		
	}
	
	protected  void check(int entityId,   ICallbackEvent<Record[]> callBack) {
		_callBack = callBack;		
		_dataRequester.request(entityId, getParams());
	}
	
}
