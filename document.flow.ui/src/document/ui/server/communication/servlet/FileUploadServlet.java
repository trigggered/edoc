package document.ui.server.communication.servlet;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import mdb.core.shared.gw.mdb.IMdbGatewayServiceRemote;
import mdb.core.shared.transformation.impl.JSONTransformation;
import mdb.core.shared.transport.IRequestData.ExecuteType;
import mdb.core.shared.transport.LobStoredResult;
import mdb.core.shared.transport.Request;
import mdb.core.shared.transport.RequestEntity;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import com.google.gson.Gson;

import document.ui.server.communication.rpc.flow.DocumentFlowServiceImpl;
import document.ui.server.communication.rpc.mail.MailingServiceImpl;
import document.ui.server.communication.rpc.mdbgw.MdbGatewayServiceImpl;
import document.ui.server.fileupload.MemoryFileItemFactory;
import document.ui.server.fileupload.UploadFileWriter;
import document.ui.server.mail.EMailType;
import document.ui.shared.MdbEntityConst;

/**
 * servlet to handle file upload requests
 * 
 * @author hturksoy
 * 
 */
public class FileUploadServlet extends HttpServlet {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private static final Logger _logger = Logger.getLogger(FileUploadServlet.class.getName());
	
	
	private Gson _gson = new Gson();		
	
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        super.doGet(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        
    	_logger.info("Upload file. Request CharacterEncoding is " +req.getCharacterEncoding() );
    	//
        // process only multipart requests
        if (ServletFileUpload.isMultipartContent(req)) {

            // Create a factory for disk-based file items
            FileItemFactory factory = new MemoryFileItemFactory();        	

            // Create a new file upload handler
            ServletFileUpload upload = new ServletFileUpload(factory);
            String maxSizeValue = getServletConfig().getInitParameter("maxSize");
            if (maxSizeValue!= null) {            	
            	_logger.info(String.format( "max size of the upload request %s bytes", maxSizeValue) );
            	long fileSizeMax = Long.parseLong(maxSizeValue);            
            	upload.setFileSizeMax(fileSizeMax);
            }
            upload.setHeaderEncoding("UTF-8");
            

            HashMap<String, String > dataAttributes = new HashMap<String,String>();
            FileItem  lobItem = null;
            
            try {  
                List<FileItem> items = upload.parseRequest(req);
                for (FileItem item : items) {
                    // process only file upload - discard other form item types
                    if ( item.isFormField() ) {
                    	String value = item.getString();
                    	if (value != null && !value.equalsIgnoreCase("null")) {
                    		dataAttributes.put(item.getFieldName(), item.getString());
                    	}
                    }
                    else {
                    	lobItem  = item;
                    }                                                          
                }
                
	               if (lobItem == null) {
	            	   throw new IOException("The attachment is null.");
	               }
            	   
                    //JSONObject result = new JSONObject();
                   LobStoredResult lobStoredResult = UploadFileWriter.write(lobItem);
                // Parse the request
              
                    if (lobStoredResult != null )                    
                    {                 
                    	dataAttributes.put("ID_ATTRIBUTE", String.valueOf(lobStoredResult.getAttributeId()));
                    	dataAttributes.put("FILE_NAME", String.valueOf(lobStoredResult.getLobName()));
                    	
                    	writeAddinationData (dataAttributes);
                    	//String jsonStr = _gson.toJson(lobStorInfo);
                    	
                    	String jsonStr = _gson.toJson(lobStoredResult);                                        	
                    	resp.setContentType("text/html");                    	
                    	resp.setStatus(HttpServletResponse.SC_CREATED);                    	
                        
                       // resp.getWriter().print(
                       // String.format("The file %s size is %s was created successfully.", item.getName(), item.getSize()));
                        resp.getWriter().println(jsonStr);
                        resp.flushBuffer();
                    } else
                        throw new IOException("The file already exists in repository.");
              
                    
            } catch (Exception e) {
            	_logger.severe(e.getMessage());
                resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                        "An error occurred while creating the file : " + e.getMessage());
            }

        } else {
            resp.sendError(HttpServletResponse.SC_UNSUPPORTED_MEDIA_TYPE,
                            "Request contents type is not supported by the servlet.");
        }
    }
   
   
	 private boolean writeAddinationData (HashMap<String, String>  data) {
		 IMdbGatewayServiceRemote clientProxy = MdbGatewayServiceImpl.getClientProxy();		 		 
		 
		 Request req = new Request();
		 RequestEntity entity = new RequestEntity(); 
		 entity.setEntityId(MdbEntityConst.DOC_ATTACH_LIST);
		 entity.setRequestFieldsDescription(false);
		 entity.setExecuteType(ExecuteType.ChangeData);		 		 
		 entity.addForInsert(null,JSONTransformation.map2json(data));
		 req.add(entity);
		 _logger.info("Send request id = "+ req.getRequestId() +" from upload servlet");
		 Request response =  clientProxy.call(req);		 
		 _logger.info("Receive response  id = "+ response.getRequestId() +" from upload servlet");
		 
		 //:ID_DOC, :ID_ATTRIBUTE, :ATTACH_AUTHOR, :ID_STOR
		 
		 String idStorPrev =  data.get("ID_STOR");
		 if (idStorPrev!= null && !idStorPrev.equals("") ) {
			 sendInfoMsgAboutNewVersion (Long.parseLong(data.get("ID_DOC")), data.get("FILE_NAME")  );
		 }
		 
		return true;		 
	 }
	 
	 
	 private void sendInfoMsgAboutNewVersion (long documentId, String attachmentName ) {
		 MailingServiceImpl maling = new MailingServiceImpl();		 		
		  maling.sendInfoMessageTo(EMailType.ToAcceptingNewVersion, documentId, attachmentName);			 			
		 
	 }

    
}