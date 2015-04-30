/**
 * 
 */
package document.ui.client.view;

import java.util.logging.Logger;

import mdb.core.ui.client.view.data.IDataView;
import document.ui.client.commons.ECorrespondentType;
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
	public void prepareRequestData() {				

		for ( IDataView dataView : _hmDataSection.values()) {			
			dataView.getParams().add("corr_type",ECorrespondentType.ACCOUNT_MODEL.toString());			
		}
		
		super.prepareRequestData();
	}
	
	@Override
	protected void createSectionForProcedure() {
		
	}
	
	@Override
	public String getCaption() {
		return Captions.BA_FIN_WORKSPACE;
	}
	
	
}
