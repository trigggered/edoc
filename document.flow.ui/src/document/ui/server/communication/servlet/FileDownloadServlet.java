/**
 * 
 */
package document.ui.server.communication.servlet;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import document.ui.server.auth.SessionValidateChecker;
import document.ui.server.communication.rpc.mdbgw.MdbRequester;
import document.ui.shared.MdbEntityConst;
import mdb.core.shared.auth.IUserInfo;
import mdb.core.shared.data.EMdbEntityActionType;
import mdb.core.shared.exceptions.SessionExpiredException;
import mdb.core.shared.gw.LobManagerClientBuilder;
import mdb.core.shared.gw.RemoteServiceClientType;
import mdb.core.shared.gw.lob.ILobManagerRemote;
import mdb.core.shared.transformation.impl.JSONTransformation;
import mdb.core.shared.transport.IRequestData;
import mdb.core.shared.transport.LobStoredResult;
import mdb.core.shared.transport.Request;
import mdb.core.shared.transport.IRequestData.ExecuteType;

/**
 * @author azhuk
 * Creation date: May 8, 2014
 *
 */
public class FileDownloadServlet  extends HttpServlet{
	
private static final long serialVersionUID = 1L;
	private static final Logger _logger = Logger
			.getLogger(FileDownloadServlet.class.getName());
		
	MdbRequester _mdbRequester = new MdbRequester(); 
	
	protected void doGet( HttpServletRequest req, HttpServletResponse resp ) throws ServletException, IOException
    {
		IUserInfo userInfo = null;
    
			try {
				userInfo = SessionValidateChecker.checkSessionValid();
			} catch (SessionExpiredException e) {

				_logger.severe(e.getMessage());
				return;
			}
		
		
        String attributeId = req.getParameter( "AttributeId" );        
        
        _logger.info(
        		String.format("Try download file name for  attributeId= %s from UserId =%s", 
        				attributeId , userInfo.getId())
        );        
        
        int BUFFER = 1024 * 100;
        resp.setContentType( "application/octet-stream" );
        resp.setCharacterEncoding("UTF-8");
        
                
        
        ServletOutputStream outputStream = resp.getOutputStream();
        
        LobStoredResult lobStore = getLobStoreResult(Long.parseLong(attributeId));
        String fileName = lobStore.getLobName();
        
        try {
			URI uri = new URI(null, null, fileName , null);
			fileName  = uri.toASCIIString();
		} catch (URISyntaxException e) {		
			_logger.severe(e.getMessage());
		}
        
        resp.setHeader("Content-Disposition", String.format( "attachment;filename=\"%s\"",fileName) );
        
        
        byte[] bytes=lobStore.getData();
        resp.setContentLength( bytes.length);
        resp.setBufferSize( BUFFER );
        outputStream.write(bytes);
        outputStream.flush();
        outputStream.close();
        _logger.info(String.format ("Success write file %s to ServletOutputStream", fileName));
        
        loggingLobDownload(userInfo.getId(), attributeId, fileName);
    }
	
	

	private LobStoredResult getLobStoreResult(long attributeId) {
		ILobManagerRemote client =  LobManagerClientBuilder.create(RemoteServiceClientType.WebSrvInvoke);
		return  client.getLob( attributeId );        
	}
	
		
	
	private void loggingLobDownload(  int userId, String attributeId,  String fileName) {
		_logger.info("Write download information to DB ");		
	
		 HashMap<String, String > data = new HashMap<String,String>();
		 data.put("OFFICER_NUM", String.valueOf( userId) );
		 data.put("ID_LOB_ATTR", attributeId);
		 data.put("FILE_NAME", fileName); 		 		
		
		 Request req =  _mdbRequester.getNewRequest(MdbEntityConst.LogingAttachDownload, ExecuteType.ChangeData, null);		 
		 
		 IRequestData entity = req.get( String.valueOf(MdbEntityConst.LogingAttachDownload));		 
		   
				 
		 if (entity != null) {
		 entity.setRequestFieldsDescription(false);
		 entity.setExecuteType(ExecuteType.ChangeData);	 		 
		 
		 entity.addtDataForChange(EMdbEntityActionType.INSERT, JSONTransformation.map2json(data));		 		 		 
		 
		 _mdbRequester.call(req);
		 }
		 else {
			 _logger.severe("Entity is null");
		 }		
	}

}
