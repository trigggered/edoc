/**
 * 
 */
package document.ui.client.view;

import java.util.logging.Logger;

import mdb.core.ui.client.app.AppController;

import com.google.gwt.event.logical.shared.ValueChangeEvent;

import document.ui.shared.DebugMode;

/**
 * @author azhuk
 * Creation date: Feb 3, 2014
 *
 */
public class EdocAppController extends AppController {

	//private final boolean DEBUG_MODE = false;
	@SuppressWarnings("unused")
	private static final Logger _logger = Logger.getLogger(EdocAppController.class
			.getName());		
	
	
	public EdocAppController() {
		super();		
		initialAppContext();
	}
	
	/**
	 * 
	 */
	public void initialAppContext() {
		if ( getCurrentUser()!= null) {
			getAppContext().setValue("CURRENT_USER", String.valueOf( getCurrentUser().getId() ));
		}
	}

	/* (non-Javadoc)
	 * @see com.google.gwt.event.logical.shared.ValueChangeHandler#onValueChange(com.google.gwt.event.logical.shared.ValueChangeEvent)
	 *  Assume that your url as
	 *   http://www.myPageName/myproject.html?#orderId:99999
	 *//*
	@Override
	public void onValueChange(ValueChangeEvent<String> event) {
		
		String token = event.getValue();			
				
		
		_logger.info("Calling onValueChange. token= "+token);		
		
	    if (token != null) {
	        String[] tokens = History.getToken().split(":");
	        final String token1 = tokens[0];
	        final String token2 = tokens.length > 1 ? tokens[1] : "";

	        _logger.info("token1 = "+token1);
	        _logger.info("token2 = "+token2);
	        
	        EViewIdent viewMenuIdent  = null;
	       
	        try {
	        	viewMenuIdent  = EViewIdent.valueOf(token1);
	       	}
	        catch (IllegalArgumentException e) {
	        	viewMenuIdent = EViewIdent.Unknown;
	        }
	        
			//IView view = ViewFactory.create(viewMenuIdent);
	        switch (viewMenuIdent) {
		        case DocumentCard:
		        	if ( token2.length() > 0) {			        
			            _logger.info("open document Id= "+token2);			            
			            DocumentCard.OpenById(token2);
			            
			        }
		        	break;
		        case Unknown:
		        	_logger.info("Command not found!");
		        	break;
		        default:
		        	break;
	        }            
		
	    }
	}	
	
	*/
	
	
	/* (non-Javadoc)
	 * @see com.google.gwt.event.logical.shared.ValueChangeHandler#onValueChange(com.google.gwt.event.logical.shared.ValueChangeEvent)
	 *  Assume that your url as
	 *   http://127.0.0.1:8081/document.flow.ui/index.html?DocumentCard=4726
	 */
	
	@Override
	public void onValueChange(ValueChangeEvent<String> event) {
		/*
		String documentCardId = com.google.gwt.user.client.Window.Location.getParameter("DocumentCard");
		_logger.info("Calling onValueChange. DocumentCard= "+documentCardId);		
		if (documentCardId != null && documentCardId.length() >0) {
			 DocumentCard.OpenById(documentCardId);
		}	
		*/
	}
	
	@Override
	public boolean isDebugMode() {
		return DebugMode.isActive();		
	}
	
}
