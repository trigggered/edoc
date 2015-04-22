package document.ui.client.view.doc.search;

import mdb.core.ui.client.data.bind.DataBindException;
import mdb.core.ui.client.data.impl.ViewDataConverter;
import mdb.core.ui.client.view.components.menu.IMenuContainer;
import mdb.core.ui.client.view.data.DataView;

import com.smartgwt.client.data.Record;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.widgets.Button;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.Layout;
import com.smartgwt.client.widgets.layout.VLayout;
import com.smartgwt.client.widgets.tab.Tab;
import com.smartgwt.client.widgets.tab.TabSet;
import com.smartgwt.client.widgets.tab.events.TabSelectedEvent;
import com.smartgwt.client.widgets.tab.events.TabSelectedHandler;

public class SearchView extends DataView{	

	//InputSearchCriteriaView _simpleSearchCriteriaView;
	InputSearchCriteriaView _advancedSearchCriteriaView;
	InputSearchCriteriaView _contextSearchCriteriaView;
	
	InputSearchCriteriaView _activeCriteriaView;
	SearchResult _searchResult; 
	
	@Override
	protected void createComponents () {
		super.createComponents();
		setSingleInstance(true);
		Layout la = new VLayout();
		
		TabSet tabSet = new TabSet();
		tabSet.setWidth100();

		/*
		Tab tabSearch = new Tab("Стандартный");		
		_simpleSearchCriteriaView = new InputSearchCriteriaView(2934);
		tabSearch.setPane(_simpleSearchCriteriaView);		
		tabSearch.addTabSelectedHandler(new TabSelectedHandler() {			
			@Override
			public void onTabSelected(TabSelectedEvent event) {				
				_activeCriteriaView = _simpleSearchCriteriaView;
				
			}
		});
		*/
	
		Tab tabAdvSearch = new Tab("Поиск");
		_advancedSearchCriteriaView = new InputSearchCriteriaView(2935);
		tabAdvSearch.setPane(_advancedSearchCriteriaView);
		tabAdvSearch.addTabSelectedHandler(new TabSelectedHandler() {			
			@Override
			public void onTabSelected(TabSelectedEvent event) {
				_activeCriteriaView = _advancedSearchCriteriaView;
				
			}
		});
		
		Tab tabContextSearch = new Tab("По содержимому");
		_contextSearchCriteriaView = new InputSearchCriteriaView(2946);
		tabContextSearch.setPane(_contextSearchCriteriaView);
		tabContextSearch.addTabSelectedHandler(new TabSelectedHandler() {			
			@Override
			public void onTabSelected(TabSelectedEvent event) {
				_activeCriteriaView = _contextSearchCriteriaView;				
			}
		});		
	
		//tabSet.addTab(tabSearch);
		tabSet.addTab(tabAdvSearch);
		tabSet.addTab(tabContextSearch);
		
		_searchResult = new SearchResult();
		_searchResult.setMainEntityId(5096);
		//_searchResult.setWidth100();
		_searchResult.setWidth("50%");
		
		la.addMembers(tabSet, createBottomLayout());
		la.setWidth("50%");
		la.setShowResizeBar(true);
		getViewPanel().addMembers(la,_searchResult);		
	}
	
	
	
	protected Layout createBottomLayout() {
		 Layout btnlayout = new HLayout();
	      
		 btnlayout.setHeight("5%");
		 btnlayout.setAlign(Alignment.RIGHT);
		 btnlayout.setMargin(10);
		 btnlayout.setMembersMargin(10);

	      
		 Button btnClear = new Button();
		 btnClear.setTitle( "Очистить");		  
		 btnClear.addClickHandler(new com.smartgwt.client.widgets.events.ClickHandler() {		
			@Override
			public void onClick(com.smartgwt.client.widgets.events.ClickEvent event) {
				clearCriteria();				
			}
		});	 
	      
		  Button btnOk = new Button();
		  btnOk.setTitle( "Искать");		  
	      btnOk.addClickHandler(new com.smartgwt.client.widgets.events.ClickHandler() {		
			@Override
			public void onClick(com.smartgwt.client.widgets.events.ClickEvent event) {
				search();				
			}
		});	            
	          	    
	      btnlayout.addMembers(btnClear ,btnOk);
	      return btnlayout;
	}	
	
	
	/**
	 * 
	 */
	protected void clearCriteria() {

		_advancedSearchCriteriaView.clearCriteria();
		_contextSearchCriteriaView.clearCriteria();
	}



	private InputSearchCriteriaView getActiveSearchInputCriteria() {
		return _activeCriteriaView; 	
	}
	

	protected void search() {		
		
		InputSearchCriteriaView searchCriteriaView = getActiveSearchInputCriteria();
		if ( searchCriteriaView.isValidate() ) {
			Record  data = searchCriteriaView.getDynamicFields().getDataForm().getValuesAsRecord();
			if (data != null) {
				String searchCriteria = ViewDataConverter.record2Json(data);
				_searchResult.setJsonSearchCriteria(searchCriteria);
				_searchResult.setExecuteActionId(searchCriteriaView.getActionId());
				_searchResult.callRequestData();
			}
		}
	}



	@Override
	public String getCaption() {
		return "Поиск документов";
	}

	@Override
	public Canvas getCanvas() {
		return this;
	}

	@Override
	protected IMenuContainer createMenuContainer() {	
		return null;
	}

	@Override
	protected void createMenu() {
		// TODO Auto-generated method stub		
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
			prepareRequestData( _advancedSearchCriteriaView, _contextSearchCriteriaView, _searchResult);		
	}

	@Override
	public void putRequestToQueue() {
		putRequestToQueue(_advancedSearchCriteriaView, _contextSearchCriteriaView, _searchResult);
	}
	
	/* (non-Javadoc)
	 * @see mdb.core.ui.client.view.data.DataView#bindDataComponents()
	 */
	@Override
	public void bindDataComponents() throws DataBindException {
		// TODO Auto-generated method stub		
	}
		
}
