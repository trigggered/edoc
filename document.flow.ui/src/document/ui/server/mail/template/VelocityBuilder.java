/**
 * 
 */
package document.ui.server.mail.template;

import java.io.StringWriter;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Logger;

import org.apache.commons.codec.CharEncoding;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.runtime.RuntimeConstants;
import org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader;

import document.ui.server.mail.EMailType;


/**
 * @author azhuk
 * Creation date: Apr 28, 2015
 *
 */
public class VelocityBuilder implements ITemplateBuiled{
	private static final Logger _logger = Logger.getLogger(VelocityBuilder.class
			.getName());
		
	VelocityEngine _ve;

	String TEMPLATE_PATH = "document/ui/server/mail/template/";
	
	public VelocityBuilder () {
		 /*
         *   first, get and initialize an engine
         */
        _ve = new VelocityEngine();
        
        _ve.setProperty(RuntimeConstants.RESOURCE_LOADER, "classpath");
        _ve.setProperty("classpath.resource.loader.class", ClasspathResourceLoader.class.getName());
        _ve.setProperty("input.encoding", CharEncoding.UTF_8);
        _ve.setProperty("output.encoding", CharEncoding.UTF_8);               
        
        _ve.setProperty("runtime.log.logsystem.class", "org.apache.velocity.runtime.log.SimpleLog4JLogSystem");
        _ve.setProperty("runtime.log.logsystem.log4j.category", "velocity");
        _ve.setProperty("runtime.log.logsystem.log4j.logger", "velocity");
        
        _ve.init();        	
	}
	
	@Override
	 public  String getMailBody (EMailType emailType, Map<String, String> bodyParameters)  {
		       

		        VelocityContext context = new VelocityContext();
		        
		        for (Entry<String, String>  entry : bodyParameters.entrySet()) {
		        	context.put(entry.getKey(), entry.getValue());		        	
		        }		        		        

		        /*
		         *   get the Template  
		         */		        		        
		        Template t = _ve.getTemplate( getTemplatePath(emailType), CharEncoding.UTF_8);

		        /*
		         *  now render the template into a Writer, here 
		         *  a StringWriter 
		         */

		        StringWriter writer = new StringWriter();

		        t.merge( context, writer );

		        /*
		         *  use the output in the body of your emails
		         */

		        return writer.toString();
		    }
	 
	 private  String getTemplatePath(EMailType emailType )  {
		 String toReturn = getTemplatePath() + "tmpFlowMsg.vm";
		 
		 /*
			switch (emailType) {
				case ToAccepting:						
				case ToSignatory:					
				case ToRecipients:
				case ToExecutor:					
				case CancelPublishEmailToAuthor:				
				case CancelApprovalEmailToAuthor:					
				case CancelSignEmailToAuthor:						
				case ToAcceptingNewVersion:
					toReturn = getTemplatePath() + "tmpFlowMsg.vm";					
					break;
					
				case DocumentSigned:
				case DocumentApproved:
				case DocumentPublished:
					toReturn = getTemplatePath() + "tmpFlowMsg.vm";					
				case ApproveCurentUser:
				case NotApproveCurentUser:
					break;
					
			}
			*/ 
			return toReturn;
	 }
	 
	 @Override
	 public String getTemplatePath() {
		 return TEMPLATE_PATH;
	 }
}

