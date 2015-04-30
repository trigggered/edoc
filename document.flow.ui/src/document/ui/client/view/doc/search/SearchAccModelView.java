/**
 * 
 */
package document.ui.client.view.doc.search;

import java.util.logging.Logger;

import document.ui.shared.MdbEntityConst;
import mdb.core.ui.client.view.IView;

/**
 * @author azhuk
 * Creation date: Jun 3, 2015
 *
 */
public class SearchAccModelView extends SearchView implements IView {
	private static final Logger _logger = Logger
			.getLogger(SearchAccModelView.class.getName());
	
	final static int SIMPLE_SEARH = 3144;
	final static int CONTEXT_SEARCH = 3145;
	
	
	@Override
	protected int getSimpleSearchActionId() {
		return SIMPLE_SEARH;
	}
	
	@Override
	protected int getContextSearchActionId() {
		return CONTEXT_SEARCH;
	}
	
	@Override
	protected int getSearchResultEntityId() {
		return MdbEntityConst.FIN_DOC_LIST;
	}
	
}
