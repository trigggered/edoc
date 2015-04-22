package document.ui.client.view.doc;

import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;

import mdb.core.ui.client.app.AppController;
import mdb.core.ui.client.command.ICommand;
import mdb.core.ui.client.data.bind.DataBindException;
import mdb.core.ui.client.events.ICallbackEvent;
import mdb.core.ui.client.view.IMainView;
import mdb.core.ui.client.view.IView;
import mdb.core.ui.client.view.components.menu.IMenuButtons;
import mdb.core.ui.client.view.components.menu.IMenuItem;
import mdb.core.ui.client.view.components.menu.Menu;
import mdb.core.ui.client.view.components.menu.data.AMenuData;
import mdb.core.ui.client.view.data.DataView;
import mdb.core.ui.client.view.data.grid.ListOfGrids;
import mdb.core.ui.client.view.data.grid.ListOfGrids.EmbeddedGrid;
import mdb.core.ui.client.view.utils.DateTimeHelper;

import com.smartgwt.client.data.Record;
import com.smartgwt.client.types.VisibilityMode;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.layout.Layout;
import com.smartgwt.client.widgets.layout.SectionStack;
import com.smartgwt.client.widgets.layout.SectionStackSection;



import document.ui.client.resources.locales.Captions;
import document.ui.client.view.doc.card.DocumentCard;
import document.ui.shared.MdbEntityConst;


public class DocumentsOfDay extends DataView{
	
	private ListOfGrids _listOfGrids;

	
//	private Date _selectedDate =new Date();
	private ICallbackEvent<Void> _refreshCallback;

	
	protected Date _dateB=new Date();
	protected Date _dateE=new Date();
	
	
	@Override
	protected Layout createViewPanel() {	

		SectionStack sectionStack = new SectionStack();		
		
		sectionStack.setWidth100();
		sectionStack.setHeight100();
		sectionStack.setVisibilityMode(VisibilityMode.MULTIPLE);		
		sectionStack.setAnimateSections(true);		
		
		 
		SectionStackSection sectionPublished = new SectionStackSection(Captions.DOC_PUBLISHED );		
		sectionPublished.setExpanded(true);
		
		SectionStackSection sectionEnteredIntoForce = new SectionStackSection(Captions.DOC_Entered_into_force );		
		sectionEnteredIntoForce.setExpanded(true);
		
		
		Layout l0 = new Layout();
		l0.addMember(_listOfGrids.getDataComponent(MdbEntityConst.DOC_PUBLISHED));
		sectionPublished.addItem(l0);
		
		
		Layout l1 = new Layout();
		l1.addMember(_listOfGrids.getDataComponent(MdbEntityConst.ENTERING_IN_FORCE));
		sectionEnteredIntoForce.addItem(l1);
		
		
		SectionStackSection sectionOutOfDoc = new SectionStackSection( Captions.DOC_OUT_OF_ORDER);
		sectionOutOfDoc.setExpanded(false);
		
		
		Layout l2 = new Layout();
		l2.addMember(_listOfGrids.getDataComponent(MdbEntityConst.OUT_OF_DOC));
		sectionOutOfDoc.addItem(l2);
	
		sectionStack.setSections(sectionPublished, sectionEnteredIntoForce,sectionOutOfDoc);
		
		for (EmbeddedGrid emGrid : _listOfGrids.getList()) {
			emGrid.addEditEvent(DocumentCard.getShowEditViewHandler());
			emGrid.addInsertEvent(DocumentCard.getShowEditViewHandler());
		}
		return sectionStack;		
	}


	@Override
	protected void createMenu() {
		
		Menu menu = new Menu("");
			
		IMenuItem	item = menu.addItem("","",IMenuItem.ItemType.DateRange,1);
		item.setValueMap(new LinkedHashMap<String, Object>(){
		{
			put("dateFrom",_dateB);
			put("dateTo",_dateE);				
			}}
		);			
		item.setCommand(new ICommand<IMenuItem>() {
			@Override
			public void execute(IMenuItem sender) {
				String att = "dateRangeType";
				if ( sender.isAttributeExist(att) ) {					
					if (sender.getAttribute("dateRangeType").equalsIgnoreCase("dateFrom")) {
						_dateB = (Date) sender.getValue();
					}
					else { 
						_dateE = (Date) sender.getValue();						
					}
				}
			}
		});
		
		item = menu.addItem(Captions.REFRESH,"", IMenuItem.ItemType.MenuItem,2);
		AMenuData.initPropertyMenuItem(item, IMenuButtons.Buttons.dataRefresh );
		item.setCommand(new ICommand<IMenuItem>() {			
			@Override
			public void execute(IMenuItem sender) {						
						
				redraw();
			}
		});
		
		
		item = menu.addItem(Captions.DOC_ID,"", IMenuItem.ItemType.ButtonDataItem,3);
		item.setWidth(100);
		item.setCommand(new ICommand<IMenuItem>() {
			
			@Override
			public void execute(IMenuItem sender) {																	 

				DocumentCard.OpenById((String)sender.getValue());
			}
		});
		
		
		getMenuContainer().bind(menu);
	}
	
	@Override
	public String getCaption() {
		return Captions.DOC_OF_DAY;	
	}

	@Override
	public Canvas getCanvas() {
		return this;
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
	protected void createComponents() {
		_listOfGrids = new ListOfGrids(MdbEntityConst.DOC_PUBLISHED, MdbEntityConst.ENTERING_IN_FORCE,MdbEntityConst.OUT_OF_DOC);
		super.createComponents();
		setSingleInstance(true);
	}

	
	
	public void refreshData(String dtaB, String dtaE) {			
		HashMap<String, String> params = new HashMap<String,String>();
		params.put("DTA_B", dtaB);		
		params.put("DTA_E", dtaE);
		params.put("CURRENT_USER", String.valueOf(AppController.getInstance().getCurrentUser().getId()));
		_listOfGrids.prepareRequestData(params);		
	}


	public void setRefreshEvent (ICallbackEvent<Void> callBack) {
		_refreshCallback = callBack;
	}
	
	@Override
	public void prepareRequestData() {		
		refreshData(DateTimeHelper.format(_dateB), DateTimeHelper.format(_dateE));
	}


	/* (non-Javadoc)
	 * @see mdb.core.ui.client.view.data.DataView#bindDataComponents()
	 */
	@Override
	public void bindDataComponents() throws DataBindException {
		// TODO Auto-generated method stub		
	}
	
	@Override
	public void setMainView (IMainView mainView) {
		super.setMainView(mainView);
		for ( IView view :  _listOfGrids.getList() ) {
			view.setMainView(mainView);
		}				
	}
	
	@Override
	public void redraw() {
		if (_refreshCallback != null) {
			_refreshCallback.doWork(null);
		}
		super.redraw();
	}
	
}

