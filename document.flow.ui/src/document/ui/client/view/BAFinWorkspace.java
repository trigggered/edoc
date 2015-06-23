/**
 * 
 */
package document.ui.client.view;

import java.util.logging.Logger;

import document.ui.client.resources.locales.Captions;

/**
 * @author azhuk
 * Creation date: May 18, 2015
 *
 */
public class BAFinWorkspace extends BAWorkspace {
	private static final Logger _logger = Logger.getLogger(BAFinWorkspace.class
			.getName());

	
	@Override
	protected void createSectionForProcedure() {
		
	}
	
	@Override
	public String getCaption() {
		return Captions.BA_FIN_WORKSPACE;
	}
	
	
}
