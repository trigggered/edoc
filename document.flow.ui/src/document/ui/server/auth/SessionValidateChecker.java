package document.ui.server.auth;

import java.util.logging.Logger;

import mdb.core.shared.auth.AuthUser;
import mdb.core.shared.auth.IUserInfo;
import mdb.core.shared.exceptions.SessionExpiredException;
import document.ui.server.communication.rpc.MyAuthServiceImpl;
import document.ui.shared.DebugMode;


public class SessionValidateChecker {

	private static final Logger _logger = Logger.getLogger(SessionValidateChecker.class.getName());
			
	public static IUserInfo checkSessionValid() throws SessionExpiredException{
		
		AuthUser toReturn =  MyAuthServiceImpl.ExtractUserInfoFromContext();

		/***********************************/
		
		if (toReturn ==  null) {
			if (DebugMode.isActive()) {
				_logger.info("DebugMode is Active. Set default user id = 708609 ");
				toReturn = new AuthUser("anonim");
				toReturn.setId( 90730 );
				
			}
			else {
				_logger.severe("Not Authorise user ot Session is Expired ");
				throw new SessionExpiredException();
			}
		}		
		/***********************************/			
		return toReturn;
		
	}
}
