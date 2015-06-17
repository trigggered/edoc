/**
 * 
 */
package document.ui.client.view.reports;

import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.logging.Logger;

import mdb.core.ui.client.command.ICommand;
import mdb.core.ui.client.data.export.ExportHelper;
import mdb.core.ui.client.view.components.menu.IMenuButtons;
import mdb.core.ui.client.view.components.menu.IMenuItem;
import mdb.core.ui.client.view.components.menu.Menu;
import mdb.core.ui.client.view.components.menu.data.AMenuData;
import mdb.core.ui.client.view.data.grid.GridView;
import mdb.core.ui.client.view.data.grid.ListOfGrids;
import mdb.core.ui.client.view.data.grid.ListOfGrids.EmbeddedGrid;
import mdb.core.ui.client.view.dialogs.message.Dialogs;
import mdb.core.ui.client.view.utils.DateTimeHelper;

import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.Layout;
import com.smartgwt.client.widgets.layout.VLayout;

import document.ui.client.commons.ECorrespondentType;
import document.ui.client.commons.EViewIdent;
import document.ui.client.resources.locales.Captions;
import document.ui.shared.MdbEntityConst;
/**
 * @author azhuk
 * Creation date: May 7, 2015
 *
 */
public class ViewReport extends GridView {
	
	private static final Logger _logger = Logger.getLogger(ViewReport.class
			.getName());
	
	EViewIdent _viewIdent;
	protected Date _dateB;
	protected Date _dateE;

	
	public ViewReport(EViewIdent viewIdent) {
		_viewIdent = viewIdent;
		setCreateMenuNavigator(false);
		switch (_viewIdent) {
			case ReportForBA:
				setCaption("Отчет для БА");
				setMainEntityId(MdbEntityConst.REPORT_FOR_BA);		
				break;
		  case ReportForAuthor:
			  setCaption("Отчет для автора");
			  setMainEntityId(MdbEntityConst.REPORT_FOR_Author);
			  break;
		  case ReportForAprovals:
			  setCaption("Отчет для согласованта");
			  setMainEntityId(MdbEntityConst.REPORT_FOR_Aprovals);
			  break;
		  case ReportForSigners:
			  setCaption("Отчет для подписанта");
			  setMainEntityId(MdbEntityConst.REPORT_FOR_Signers);
			  break;
		  case ReportForExecuters:
			  setCaption("Отчет для исполнителя");
			  setMainEntityId(MdbEntityConst.REPORT_FOR_Executers);
			  break;
		}		   
	}	
	
	
	protected ListGrid createGrid() {
		 return new ExpansionGrid();
	}	
	
	
	class MenuReports extends Menu {
		
		@SuppressWarnings("serial")
		public MenuReports() {
			super("MenuDocInFlow");
			
			_dateB = DateTimeHelper.getFirstDayOfMonth(new Date());			
			_dateE = DateTimeHelper.getLastDayOfMonth( new Date());
						
			IMenuItem item = addItem("","",IMenuItem.ItemType.DateRange,0);
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
			
			item = addItem( Captions.REFRESH, "", IMenuItem.ItemType.ToolButton,0);
			AMenuData.initPropertyMenuItem(item, IMenuButtons.Buttons.dataRefresh );
			item.setCommand(new ICommand<IMenuItem>() {						
				@Override
				public void execute(IMenuItem sender) {						
					callRequestData();				
				}
			});
			
		
			item = addItem(Captions.OPEN_CARD, "", IMenuItem.ItemType.ToolButton,0);
			item.setCommand(new ICommand<IMenuItem>() {						
				@Override
				public void execute(IMenuItem sender) {
					//openSelectedCards(_grDocsInWork);																					
				}

			});	
			
			item = addItem(Captions.EXPAND, "", IMenuItem.ItemType.ToolButton,0);
			item.setCommand(new ICommand<IMenuItem>() {						
				@Override
				public void execute(IMenuItem sender) {
					//openSelectedCards(_grDocsInWork);										
				}

			});
			
			item = addItem(Captions.PRINT, "", IMenuItem.ItemType.ToolButton,0);
			item.setCommand(new ICommand<IMenuItem>() {						
				@Override
				public void execute(IMenuItem sender) {
					print();					 										
				}

			});
			
			item = addItem(Captions.EXPORT2CSV, "", IMenuItem.ItemType.ToolButton,0);
			item.setCommand(new ICommand<IMenuItem>() {						
				@Override
				public void execute(IMenuItem sender) {					
					String data = ExportHelper.exportCSV(getListGrid()).toString();
				Dialogs.ShowMessage(data);	
				}

			});	
			
		}
	}
	
	
	@Override
	protected void createMenu () {
		super.createMenu();
		getMenuContainer().bind(new MenuReports());
		
	}

	@Override
	public void prepareRequestData() {
		
		_logger.info("DateB:"+DateTimeHelper.format(_dateB));
		_logger.info("DateE:"+DateTimeHelper.format(_dateE));
		
		getParams().add("dateFrom",DateTimeHelper.format(_dateB));
		getParams().add("dateTo",DateTimeHelper.format(_dateE) );
		getParams().add("CORR_ROOT_CODE", ECorrespondentType.getRootCodeCorrespondentType());
		
		super.prepareRequestData();
	}
	
	

   final class ExpansionGrid extends ListGrid {

	   public DataSource getRelatedDataSource(ListGridRecord record) {    
           //return ItemSupplyXmlDS.getInstance();
		   return null;
       }   
	   
    @Override
    protected Canvas getExpansionComponent(final ListGridRecord record) {  
    	
    	final ListGrid mainGrid = getListGrid();
    	
    	Layout  layExpansion = new VLayout();    	
    	Layout  layGrids = new HLayout();
    	layExpansion.setPadding(5);
    	layGrids.setHeight("30%");
    	ListOfGrids nestedGrids = new ListOfGrids();
    	
    	
    	switch (_viewIdent) {
		case ReportForBA:
			
			EmbeddedGrid grid = nestedGrids.addGrid(MdbEntityConst.EXECUTERS_EMP,null);
			grid.setCaption(Captions.DOC_EMP_EXECUTERS);			
			
			
			grid = nestedGrids.addGrid(MdbEntityConst.RECIPIENTS_EMP,null);
			grid.setCaption(Captions.DOC_EMP_RECIPIENTS);			
			
			grid = nestedGrids.addGrid(MdbEntityConst.ACCEPTING_EMP,null);
			grid.setCaption(Captions.DOC_EMP_APPROVALS);
			
			/*
			PortalLayout portalLayout = new PortalLayout();
			
	        portalLayout.setWidth100();  
	        portalLayout.setHeight100();
	        
			EmbeddedGrid grid = nestedGrids.addGrid(MdbEntityConst.EXECUTERS_EMP,null);
			grid.setCaption(Captions.DOC_EMP_EXECUTERS);					
			Portlet portlet = new Portlet();
			portlet.setTitle(grid.getCaption());
			portlet.addMember(grid);
			portalLayout.addMember(portlet);
	        	
			
			grid = nestedGrids.addGrid(MdbEntityConst.RECIPIENTS_EMP,null);
			grid.setCaption(Captions.DOC_EMP_RECIPIENTS);
			portlet = new Portlet();
			portlet.setTitle(grid.getCaption());
			portlet.addMember(grid);
			portalLayout.addMember(portlet);
	        
			grid = nestedGrids.addGrid(MdbEntityConst.ACCEPTING_EMP,null);
			grid.setCaption(Captions.DOC_EMP_APPROVALS);
			portlet = new Portlet();
			portlet.setTitle(grid.getCaption());
			portlet.addMember(grid);
			portalLayout.addMember(portlet);
	        
	        
	        layGrids.addMember(portalLayout);	        	        
	       */ 
			break;
	  case ReportForAuthor:		  
		  break;
	  case ReportForAprovals:		  
		  break;
	  case ReportForSigners:		  
		  break;
	  case ReportForExecuters:		  
		  break;
		default:
			break;
    	}
    	
    	 layGrids.addMembers(nestedGrids.toArray());
    	Map<String, String > params = new HashMap<String,String>();
    	
    	params.put("ID_DOC", record.getAttribute("ID_DOC"));
    	
    	nestedGrids.prepareRequestData(params);
    	nestedGrids.callRequestData();
    	
    	
    	//layGrids.addMembers(nestedGrids.toArray());
   	

        HLayout layButtons = new HLayout(10);    
        layButtons.setAlign(Alignment.CENTER);    

        IButton closeButton = new IButton("Close");        
        closeButton.addClickHandler(new ClickHandler() {    
            public void onClick(ClickEvent event) {    
                mainGrid.collapseRecord(record);    
            }    
        });
        
        layButtons.addMember(closeButton);   
        
        layExpansion.addMembers(layGrids, layButtons);    

        return layExpansion;    
    }   
  
   };
   
   @Override
	protected void createComponents() {
		super.createComponents();		
		getListGrid().setDrawAheadRatio(4);  
		  getListGrid().setCanExpandRecords(true);  
		  getListGrid().setAutoFetchData(true);		  
	}
	
}
