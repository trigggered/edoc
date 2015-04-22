/**
 * 
 */
package document.ui.client.view;

import com.google.gwt.user.client.ui.Frame;

import document.ui.client.resources.locales.Captions;
import mdb.core.ui.client.view.BaseView;

/**
 * @author azhuk
 * Creation date: Jan 5, 2015
 *
 */
public class HTMLViewer extends BaseView {	
	
	@Override
	protected void createComponents() {		
		super.createComponents();
		
		 Frame frame=new Frame("pages/userguide/DMS_user_guide.html");		
	
		 
		 frame.setHeight("100%");
		 frame.setWidth("100%");
		 getViewPanel().addMember(frame);
	}
	
	@Override
	public String getCaption() {
		return Captions.USER_GUIDE;
	}
	
}
