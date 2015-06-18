/**
 * 
 */
package document.ui.server.communication.servlet;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import mdb.core.shared.exceptions.SessionExpiredException;
import document.ui.server.auth.SessionValidateChecker;

/**
 * @author azhuk
 * Creation date: Jun 17, 2015
 *
 */
public class CSVDownloadServlet   extends HttpServlet {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;	
	private static final Logger _logger = Logger
			.getLogger(CSVDownloadServlet.class.getCanonicalName());
	
	private final static String SPLIT ="="; 
	
	protected void doGet( HttpServletRequest req, HttpServletResponse resp ) throws ServletException, IOException
    {
		try {
			SessionValidateChecker.checkSessionValid();
		} catch (SessionExpiredException e) {
			_logger.severe(e.getMessage());
			return;
		}

		
		_logger.info("Query string=" +req.getQueryString());
		String decodeStr = URLDecoder.decode(req.getQueryString(),StandardCharsets.UTF_8.toString());
		String[] parameters = decodeStr.split(SPLIT);		

		String data = parameters[1];				
		
		
        int BUFFER = 1024 * 100;
        resp.setContentType( "application/octet-stream" );
        resp.setCharacterEncoding(StandardCharsets.UTF_8.toString());
        
        ServletOutputStream outputStream = resp.getOutputStream();
        String fileName = "export.csv";
        
        try {
			URI uri = new URI(null, null, fileName , null);
			fileName  = uri.toASCIIString();
		} catch (URISyntaxException e) {		
			_logger.severe(e.getMessage());
		}
        
        resp.setHeader("Content-Disposition", String.format( "attachment;filename=\"%s\"",fileName) );     
        
        
        byte[] bytes=data.getBytes();
        resp.setContentLength( bytes.length);
        resp.setBufferSize( BUFFER );
        outputStream.write(bytes);
        outputStream.flush();
        outputStream.close();
        _logger.info(String.format ("Success write file %s to ServletOutputStream", fileName));                
    }
}
