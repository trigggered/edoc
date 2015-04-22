package document.ui.client.view.dictionary;

import mdb.core.ui.client.view.data.tree.TreeView;
import document.ui.client.commons.EViewIdent;
import document.ui.client.view.ViewFactory;

public class DictionaryTreeView extends TreeView {

	private EViewIdent _viewIdent;
	
	
	public DictionaryTreeView(EViewIdent viewIdent) {
		initialize(viewIdent);
	}
	
	public DictionaryTreeView() {		
	}
	
	
	@Override
	protected void createComponents() {
		super.createComponents();
		setSingleInstance(true);
	}
	
	private void initialize (EViewIdent viewIdent) {
		_viewIdent = viewIdent;	 
		ViewFactory.viewInitialize(viewIdent, this);			
	}	
	
	
	public EViewIdent getViewIdent() {
		return _viewIdent;
	}
	
	@Override
	protected void bindTree() throws Exception{		
		super.bindTree();
		expandTree();
	}
}
