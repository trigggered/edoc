package document.ui.client.view.doc;

import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.logging.Logger;

import mdb.core.ui.client.app.AppController;
import mdb.core.ui.client.command.ICommand;
import mdb.core.ui.client.communication.impl.GatewayQueue;
import mdb.core.ui.client.view.components.menu.IMenuButtons;
import mdb.core.ui.client.view.components.menu.IMenuItem;
import mdb.core.ui.client.view.components.menu.Menu;
import mdb.core.ui.client.view.components.menu.data.AMenuData;
import mdb.core.ui.client.view.data.grid.MasterTreeDetailGridView;
import mdb.core.ui.client.view.utils.DateTimeHelper;

import com.smartgwt.client.widgets.grid.ListGridRecord;

import document.ui.client.commons.ECorrespondentType;
import document.ui.client.commons.EViewIdent;
import document.ui.client.resources.locales.Captions;
import document.ui.client.view.ViewFactory;
import document.ui.client.view.doc.card.DocumentCard;
import document.ui.shared.MdbEntityConst;

public class DocumentsTreeInOut extends MasterTreeDetailGridView{
	private static final Logger _logger = Logger
			.getLogger(DocumentsTreeInOut.class.getName());
	
	enum EDocumentTreeBy {
		CorresoindentType,
		Status,
		InitDept,
		Product
	};

	class DocumentsGrid extends DetailGrid {
		public DocumentsGrid () {
			setCreateMenuNavigator(false);
		}
		@Override
		protected int getEntityIdForFilters() {
			return MdbEntityConst.DOC_LIST;
		}		
	}

	//Дерево в разбивке по "Типу документа"
	private final int tree_corrtype_status = MdbEntityConst.tree_corrtype_status;
	//Дерево в разбивке по "Статусу документа"
	private final int tree_status_corrtype = MdbEntityConst.tree_status_corrtype;		
	
	
	private EViewIdent _viewIdent;
	
	protected ECorrespondentType _corrTypeCode;
	protected Date _dateB;// = DateTimeHelper.getFirstDayOfMonth(new Date());
	protected Date _dateE;// = DateTimeHelper.getLastDayOfMonth( new Date());
		 
	protected  class DocTreeMenu extends Menu {
		
		protected ICommand<IMenuItem> _refreshTreecommand = new ICommand<IMenuItem>() {
			
			@Override
			public void execute(IMenuItem sender) {
				getMaster().setMainEntityId(Integer.parseInt(sender.getAttribute("entityId")));
				callRequestData();					
			}
		};
		
		
		public DocTreeMenu() {
			super("DocTreeMenu");
			create();
		}
		
		public ICommand<IMenuItem> getRefreshTreecommand() {
			return _refreshTreecommand;
		}
		
		@SuppressWarnings("serial")
		protected  void create() {						
			 
			//_dateB = DateTimeHelper.getFirstDayOfMonth(new Date());
			_dateB = DateTimeHelper.getFirstDayOfYear();
			_dateE = DateTimeHelper.getLastDayOfMonth( new Date());
			
			IMenuItem item = addItem("По «Типу документа»", "", IMenuItem.ItemType.ToolButton,0);
			item.setAttribute("entityId", String.valueOf(getBrokenTreeByCorrType()));			
			item.setCommand(_refreshTreecommand);			
			/*
			item = addItem("по «Статусу»", "", IMenuItem.ItemType.ToolButton,1);
			item.setAttribute("entityId", String.valueOf(getBrokenTreeByStatus()));
			item.setCommand(_refreshTreecommand);
			*/
			
			//item = addItem("Период","", IMenuItem.ItemType.Label,2);
			item = addItem("","", IMenuItem.ItemType.Separator,3);			
			item = addItem("","",IMenuItem.ItemType.DateRange,4);
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
			
			item = addItem( Captions.REFRESH,Captions.REFRESH, IMenuItem.ItemType.ToolButton, 5);
			
			AMenuData.initPropertyMenuItem(item, IMenuButtons.Buttons.dataRefresh );
			item.setCommand(new ICommand<IMenuItem>() {				
				@Override
				public void execute(IMenuItem sender) {
					callRequestData();	
				}
			});			
		}				
	}
	
	
	public DocumentsTreeInOut(EViewIdent viewIdent) {	
		
		clearEvents(getMaster());		
		initialize(viewIdent);
	}

	protected int getBrokenTreeByCorrType() {
		return tree_corrtype_status;
	}
	
	
	protected int getBrokenTreeByStatus() {
		return tree_status_corrtype;
	}
	
	protected int getDetailedEntityId() {
		return MdbEntityConst.DocsList;
	}
	
	private void initialize (EViewIdent viewIdent) {
		_viewIdent =viewIdent;
		ViewFactory.viewInitialize(viewIdent, this);
		switch (_viewIdent) {
		  case InDoc:
			  _corrTypeCode= ECorrespondentType.INPUT;				  
			  break;
		  case	OutDoc:
			  _corrTypeCode= ECorrespondentType.OUTPUT;
			  break;
		  case	InsideDoc:
			  _corrTypeCode= ECorrespondentType.INSIDE_PRIKAZ;
			  break;
		  case	InsideFinDoc:
			  _corrTypeCode= ECorrespondentType.ACCOUNT_MODEL;
			  break;		  
		  case 	AllDocs:
			  _corrTypeCode= ECorrespondentType.UNKNOWN;
			  break;
		default:
		}
	}
	
	@Override
	protected void createComponents() {		
		super.createComponents();	
		setSingleInstance(true);
		
		getMaster().setMainEntityId(getBrokenTreeByCorrType() );
		getDetail().setMainEntityId(getDetailedEntityId());		

	}

	@Override
	protected DetailGrid createDetailComponent() {		
		
		DetailGrid toReturn = new DocumentsGrid();
		toReturn.addEditEvent(DocumentCard.getShowEditViewHandler());
		toReturn.addInsertEvent(DocumentCard.getShowEditViewHandler());
		
		toReturn.setWidth("70%");
		return toReturn;
	}
	
	
	@SuppressWarnings("serial")
	@Override
	public void prepareRequestData() {
		_logger.info("Request document tree for Typr ="+_corrTypeCode);
		getMaster().prepareRequestData(new HashMap<String,String>(){{
			put("CORR_CODE",_corrTypeCode.toString());
			}});		
		getDetail().prepareRequestData();
		
	}

	@Override
	public void putRequestToQueue() {		
		getMaster().putRequestToQueue();
		getDetail().prepareRequestData();
	}
	

	
	protected Menu getMenu() {
		return new DocTreeMenu();
	}
	
	@Override
	protected void createMenu() {
		if (getMenuContainer() != null) {
			getMenuContainer().bind(getMenu());
		}		
	}	


	protected HashMap<String,String>  getDetaileReqParams(ListGridRecord masterSelectedData) {
		
		if (masterSelectedData !=null) {	
			String statusId = masterSelectedData.getAttribute("ID_STATUS");
			String corrCode = masterSelectedData.getAttribute("CORR_CODE");
				
		if (corrCode!=null && !corrCode.isEmpty() && statusId != null && !statusId.isEmpty()) {
			HashMap<String,String> params = new HashMap<String,String>();
			params.put("dateFrom",DateTimeHelper.format(_dateB));
			params.put("dateTo",DateTimeHelper.format(_dateE) );
			params.put("CURRENT_USER", String.valueOf(AppController.getInstance().getCurrentUser().getId()));
			
			params.put("CORR_TYPE" , corrCode);
			params.put("ID_STATUS", statusId);
			
			_logger.info("CORR_TYPE="+corrCode+"\n dateFrom="+_dateB+"\n dateTo="+_dateE+"\n ID_STATUS="+statusId);
			return params;	
			}
		}
		return null; 
	}
	
	@Override
	protected void requestDetailData(ListGridRecord value) {
		super.requestDetailData(value);
		HashMap<String,String> params = getDetaileReqParams(value);	
				
			if(params!=null) {		
			getDetail().prepareRequestData(params);
			//getDetail().putRequestToGatewayQueue();
			GatewayQueue.instance().getProcessor().run();
		}
	}

}
