package document.ui.client.view.doc;

import mdb.core.ui.client.events.ICallbackEvent;
import mdb.core.ui.client.view.IMainView;
import mdb.core.ui.client.view.components.menu.IMenuContainer;
import mdb.core.ui.client.view.data.DataView;
import mdb.core.ui.client.util.BooleanCallback;


import com.smartgwt.client.data.Record;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.layout.Layout;
import com.smartgwt.client.widgets.layout.VLayout;


public class HomeView extends DataView{

	
	private DocumentsOfDay _documentsOfDay;	
	private MyDocuments _myDocuments;
	
	
	@Override
	protected void createComponents () {
		super.createComponents();
		setSingleInstance(true);
		Layout mainLayout = getViewPanel();
		
		_documentsOfDay = new DocumentsOfDay();
		_documentsOfDay.setWidth("50%");
		_documentsOfDay.setShowResizeBar(true);
		_documentsOfDay.setRefreshEvent(new ICallbackEvent<Void>() {
			
			@Override
			public void doWork(Void data) {
				prepareRequestData();
				putRequestToQueue();
				
			}
		});

		
		Layout rightLayout = new VLayout();
		
		//Layout serchLayout = new Layout();
		
		//_searchView = new SearchView();
		//serchLayout.addMember(_searchView);
		
		
		Layout docLayout = new VLayout();
		docLayout.setShowResizeBar(true);
		_myDocuments = new MyDocuments();
		_myDocuments.setWidth100();		
		docLayout.addMember(_myDocuments);
		
		rightLayout.addMembers(docLayout);
		//rightLayout.addMembers(docLayout, serchLayout);
		
		
		mainLayout.addMembers(_documentsOfDay,rightLayout);		
	}

	@Override
	protected void createMenu() {				
	}
	
	@Override
	public String getCaption() {
		return "Home";
	}

	@Override
	public Canvas getCanvas() {
		return this;
	}

	@Override
	protected IMenuContainer createMenuContainer() {
		// TODO Auto-generated method stub
		return null;
	}


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
	
	@Override
	public boolean isSelectedRecord() {
		// TODO Auto-generated method stub
		return false;
	}


	@Override
	public void bindDataComponents() {
		// TODO Auto-generated method stub		
	}

	@Override
	public void prepareRequestData() {
		//super.prepareRequestData(_documentsOfDay, _myDocuments);/*Так не работает*/
		_documentsOfDay.prepareRequestData();
		_myDocuments.prepareRequestData();
	}
	
	@Override
	public void putRequestToQueue() {
		//super.putRequestToQueue(_documentsOfDay, _myDocuments);
		_documentsOfDay.putRequestToQueue();
		_myDocuments.putRequestToQueue();
	}
	
	@Override
	public void setMainView (IMainView mainView) {
		super.setMainView(mainView);
		_documentsOfDay.setMainView(mainView);				
	}
	
	@Override
	public void IsCanClose(final BooleanCallback callback) {
		callback.execute(true);
	}
	

}
