/**
 * 
 */
package document.ui.server.mail.template;

import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import document.ui.server.mail.EMailType;

/**
 * @author azhuk
 * Creation date: Apr 30, 2015
 *
 */
public interface ITemplateBuiled {
	String getMailBody (EMailType emailType, Map<String, String> bodyParameters);

	/**
	 * @return
	 */
	String getTemplatePath();
}
