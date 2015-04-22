/**
 * 
 */
package document.ui.server.communication.rpc;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Logger;

import mdb.core.shared.auth.AuthUser;
import mdb.core.shared.data.Params;
import mdb.core.shared.transformation.impl.ResultSetToJSONTransformation;
import mdb.core.shared.transport.IRequestData;
import mdb.core.shared.transport.IRequestData.ExecuteType;
import mdb.core.shared.transport.Request;
import mdb.core.ui.server.RemoteServiceImpl;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import document.ui.client.communication.rpc.MyAuthService;
import document.ui.server.auth.ldap.ExtUserDetails;
import document.ui.server.communication.rpc.mdbgw.MdbRequester;
import document.ui.shared.DebugMode;
import document.ui.shared.MdbEntityConst;

/**
 * @author azhuk
 * Creation date: Jul 21, 2014
 *
 */
public class MyAuthServiceImpl extends RemoteServiceImpl implements MyAuthService {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final Logger _logger = Logger.getLogger(
			MyAuthServiceImpl.class.getName());

	MdbRequester _mdbRequester = new MdbRequester();
	
	
	public static AuthUser ExtractUserInfoFromContext () {	
		AuthUser toReturn = null;
		
		Authentication authentication =
	            SecurityContextHolder.getContext().getAuthentication();
	        
	        if (authentication !=null ){        		        		        		        	
	        	Object oUserDetail = authentication.getPrincipal();
	        	if (oUserDetail instanceof ExtUserDetails ) {
	        		toReturn = new AuthUser();
	        		ExtUserDetails userDetail = (ExtUserDetails) oUserDetail;
	        		_logger.info("ExtUserDetails to AuthUser");
	        		_logger.info("Mail is :" +userDetail.getAttribute("mail"));
	        		_logger.info("Name is :" +authentication.getName());
	        		_logger.info("Full Name is :" +userDetail.getAttribute("cn"));
	        		_logger.info("Emploee ID is :" +userDetail.getAttribute("employeeID"));
	        		
	        		toReturn.setId( Integer.valueOf((String) userDetail.getAttribute("employeeID")) );	        		
	        		
	        		toReturn.setName( (String) userDetail.getAttribute("cn"));	
	        		toReturn.setMail( (String) userDetail.getAttribute("mail"));
	        		toReturn.setLoginName( authentication.getName());    			        		        			        	   	
	        	
	        	}
	        }
	        return toReturn;
	}
	
	
	/* (non-Javadoc)
	 * @see document.ui.client.communication.rpc.AuthService#retrieveUserInfo()
	 */
	@Override
	public AuthUser retrieveUserInfo() {
		AuthUser toReturn = null;
		if ( DebugMode.isActive() ) {			
			_logger.info("RetrieveUserInfo on Debug mode");
			
			toReturn = new AuthUser();
			toReturn.setId(90730);
			toReturn.getRoles().add("1");
		} else {		
		
			_logger.info("RetrieveUserInfo");
			 toReturn =ExtractUserInfoFromContext();			        	
			 toReturn =  getUserRoles(toReturn);		 
		}

	    return toReturn;
	}
	
	private AuthUser getUserRoles(  AuthUser userInfo) {
		_logger.info("get User application  roles ");		
		
		if (userInfo == null) {
			_logger.severe("AuthUser is null");
			return null;
		}
		
		
		String defaultEmloeeRole = "2";
		
		
		int entityId = MdbEntityConst.AssignRoles;		
		 
		Params params = new Params();
		params.add("ID_USER", String.valueOf( userInfo.getId()) );
		
		Request req =  _mdbRequester.getNewRequest(entityId, ExecuteType.GetData, params);		 
		 
		IRequestData entity = req.get( String.valueOf(entityId));	 
		   
				 
		 if (entity != null) {
			 entity.setRequestFieldsDescription(false); 		 		 
		 
			 Request response = _mdbRequester.call(req);
			 IRequestData resData =  response.get(String.valueOf(entityId) );
			 _logger.info("User roles is ="+resData.getData());
			 List<HashMap<String, String>> lstMap = ResultSetToJSONTransformation.deserialise(resData.getData());
			 _logger.info("Success deserialise from JSON " + lstMap.toString());
			 if (lstMap!= null && lstMap.size() != 0 ) { 
			 
					 for (HashMap<String, String> rec : lstMap )  {
						 userInfo.getRoles().add(rec.get("ID_ROLE")); 
					 }
			  } else {
				  userInfo.getRoles().add(defaultEmloeeRole );
			  }
			 
		 }
		 else {
			 _logger.severe("Entity is null");
		 }
		return userInfo;
	}

	
}
