/**
 * 
 */
package document.ui.client.view.doc;


import mdb.core.ui.client.app.AppController;
import mdb.core.ui.client.data.bind.DataBindException;
import mdb.core.ui.client.view.components.menu.IMenuContainer;
import mdb.core.ui.client.view.data.DataView;
import mdb.core.ui.client.view.data.IDataView;
import mdb.core.ui.client.view.data.grid.GridView;

import com.smartgwt.client.data.Record;
import com.smartgwt.client.widgets.layout.Layout;
import com.smartgwt.client.widgets.tab.Tab;
import com.smartgwt.client.widgets.tab.TabSet;

import document.ui.client.commons.EViewIdent;
import document.ui.client.resources.locales.Captions;
import document.ui.client.view.ViewFactory;
import document.ui.client.view.doc.card.DocumentCard;
import document.ui.shared.MdbEntityConst;

/**
 * @author azhuk
 * Creation date: May 15, 2014
 * Мои документы
 */
public class MyDocuments extends DataView{
	
	public class SimpleGrid extends GridView {
		 public SimpleGrid (int entityId) {
			 super(entityId);			 
			 setCreateMenuNavigator(false);	 
		 }
		 @Override
		 public void createMenu() {
			 
		 }
		 
		 @Override
		 public IMenuContainer createMenuContainer() {
			return null;
			 
		 }
	}
		
	GridView _grForExecution;
	GridView _grForSigning;
	GridView _grForApproval; 
	
	IDataView _grFavoritesDoc;
	GridView _grCreatingDoc;
	
	
	public MyDocuments() {
		setCaption("Мои документы");
		getParams().add("OFFICER_NUM",String.valueOf( AppController.getInstance().getCurrentUser().getId()));
	}

	@Override
	protected void createComponents() {		
		super.createComponents();
		setSingleInstance(true);
		Layout viewPanel = getViewPanel();
		
		TabSet tabs = new TabSet();
		tabs.setWidth100();
		tabs.setHeight100();
		
		Tab tabForExecution = new Tab(Captions.DOC_TO_EXECUTE );
		_grForExecution = new SimpleGrid(MdbEntityConst.MY_DOC_EXEC_INFORM);		
		_grForExecution.setWidth100();
		_grForExecution.addEditEvent(DocumentCard.getShowEditViewHandler());
		
		tabForExecution.setPane(_grForExecution);
		tabs.addTab(tabForExecution);					
		
		
		Tab tabForApproval = new Tab(Captions.DOC_TO_APPROVALS );
		_grForApproval = new SimpleGrid(MdbEntityConst.MY_DOC_FOR_APPROVAL);		
		_grForApproval.setWidth100();
		_grForApproval.addEditEvent(DocumentCard.getShowEditViewHandler());
		tabForApproval.setPane(_grForApproval);
		tabs.addTab(tabForApproval);	
		
		Tab tabForInformation = new Tab(Captions.DOC_TO_SIGN );
		_grForSigning = new SimpleGrid(MdbEntityConst.MY_DOC_FOR_SIGNING);		
		_grForSigning.setWidth100();
		_grForSigning.addEditEvent(DocumentCard.getShowEditViewHandler());
		tabForInformation.setPane(_grForSigning);
		tabs.addTab(tabForInformation);
		
		Tab tabMyFavoritsDoc = new Tab(Captions.FAVORIT );
		_grFavoritesDoc = (IDataView) ViewFactory.create(EViewIdent.FavoritesDoc);		
		tabMyFavoritsDoc.setPane(_grFavoritesDoc.getCanvas());
		tabs.addTab(tabMyFavoritsDoc);
		
		Tab tabCreatingDoc = new Tab(Captions.MY_CREATE_DOC);
		_grCreatingDoc = new SimpleGrid(MdbEntityConst.MY_CREATING_DOC);		
		_grCreatingDoc.setWidth100();
		_grCreatingDoc.addEditEvent(DocumentCard.getShowEditViewHandler());
		tabCreatingDoc.setPane(_grCreatingDoc);
		tabs.addTab(tabCreatingDoc);	
		
		
		
		
		viewPanel.addMember(tabs);		
	}
	
	/* (non-Javadoc)
	 * @see mdb.core.ui.client.view.data.IDataView#getSelectedRecord()
	 */
	@Override
	public Record getSelectedRecord() {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public Record[] getSelectedRecords() {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see mdb.core.ui.client.view.data.IDataView#isSelectedRecord()
	 */
	@Override
	public boolean isSelectedRecord() {
		// TODO Auto-generated method stub
		return false;
	}

	/* (non-Javadoc)
	 * @see mdb.core.ui.client.view.data.DataView#prepareRequestData()
	 */
	@Override
	public void prepareRequestData() {
		
		String currentUser = String.valueOf(AppController.getInstance().getCurrentUser().getId());
		getParams().add("CURRENT_USER", currentUser);
		
		getParams().add("OFFICER_NUM", currentUser);
		
		_grForExecution.getParams().copyFrom(getParams());
		_grForExecution.getParams().add("RECIPIENTS_TYPE", "1");
		
		_grForSigning.getParams().copyFrom(getParams());				
		_grForApproval.getParams().copyFrom(getParams());
		_grCreatingDoc.getParams().copyFrom(getParams());
		
		
		//_grFavoritesDoc.getParams().copyFrom(getParams());
		prepareRequestData(_grForExecution, _grForSigning,_grForApproval, _grFavoritesDoc, _grCreatingDoc);
	}
	
	@Override
	public void putRequestToQueue() {
		putRequestToQueue(_grForExecution, _grForSigning,_grForApproval, _grFavoritesDoc, _grCreatingDoc);
	}

	/* (non-Javadoc)
	 * @see mdb.core.ui.client.view.data.DataView#bindDataComponents()
	 */
	@Override
	public void bindDataComponents() throws DataBindException {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	protected IMenuContainer createMenuContainer() {	
		return null;
	}

	@Override
	protected void createMenu() {		
	}
	
	@Override
	public boolean isHaseChanges() {
		return false;
	}

}
