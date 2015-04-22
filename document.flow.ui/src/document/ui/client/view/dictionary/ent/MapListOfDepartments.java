package document.ui.client.view.dictionary.ent;

import java.util.HashMap;
import java.util.logging.Logger;

import mdb.core.ui.client.communication.impl.GatewayQueue;
import mdb.core.ui.client.view.data.grid.MasterTreeDetailGridView;
import mdb.core.ui.client.view.data.tree.TreeView;

import com.smartgwt.client.data.Record;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.DragDataAction;
import com.smartgwt.client.widgets.TransferImgButton;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.smartgwt.client.widgets.grid.events.RecordDropEvent;
import com.smartgwt.client.widgets.grid.events.RecordDropHandler;
import com.smartgwt.client.widgets.layout.Layout;
import com.smartgwt.client.widgets.layout.VStack;

import document.ui.client.commons.EViewIdent;
import document.ui.client.view.ViewFactory;

public class MapListOfDepartments extends MasterTreeDetailGridView{

	private class DepTree extends TreeView {
			
		@Override
		protected void bindTree() throws Exception {
			super.bindTree();
			//expandTree();
			collapseTree();
		}
	};
	
	private static final Logger _logger = Logger.getLogger(MapListOfDepartments.class.getName());
	
	private TreeView _sapDepTree;
	
	
	public MapListOfDepartments () {		
		clearEvents (getMaster());
		clearEvents (getDetail());		
		clearEvents(_sapDepTree);
	}	

	
	@Override
	protected void createMenu() {
		// TODO Auto-generated method stub		
	}
	
	@Override
	protected void createComponents() {
		super.createComponents();
		
		//getMaster().setMainEntityId(MdbEntityConst.SAPListOfDepartmentsForMap);
		ViewFactory.viewInitialize(EViewIdent.ListOfDepartments, getMaster());		
		getMaster().setWidth("25%");
		
		
		ViewFactory.viewInitialize(EViewIdent.MapListOfDepartments, getDetail());				
		getDetail().setWidth("50%");		
		getDetail().getListGrid().setCanAcceptDroppedRecords(true);  
		//getDetail().getListGrid().setCanRemoveRecords(true); 	          
		//getDetail().getListGrid().setPreventDuplicates(true);
		getDetail().getListGrid().setCanDragRecordsOut(true);
		getDetail().getListGrid().setDragDataAction(DragDataAction.MOVE);	
		
		getDetail().getListGrid().addRecordDropHandler(new RecordDropHandler() {			
			@Override
			public void onRecordDrop(RecordDropEvent event) {
				_logger.info("Link SAPListOfDepartments is record id="+event.getDropRecords()[0].getAttribute("ID")+" with "
						+ "ListOfDepartments id="+getMaster().getSelectedRecord().getAttribute("ID"));
				event.cancel();
				//
				Record rec = new Record();				 
				rec.setAttribute("ID_DEP", getMaster().getSelectedRecord().getAttribute("ID"));
				rec.setAttribute("SAP_DEP", event.getDropRecords()[0].getAttribute("ID"));
				rec.setAttribute("SAP_DEP_NAME", event.getDropRecords()[0].getAttribute("DEP_NAME"));
				getDetail().getListGrid().addData(rec);
				getDetail().getMainDataSource().save();				
			}
		});
		
		

		 _sapDepTree = new DepTree ();
		ViewFactory.viewInitialize(EViewIdent.SAPListOfDepartments, _sapDepTree);
		//_sapDepTree.setMainEntityId(MdbEntityConst.SAPListOfDepartmentsForMap);
		_sapDepTree.setWidth("25%");
		_sapDepTree.getListGrid().setCanDragRecordsOut(true);
		_sapDepTree.getListGrid().setDragDataAction(DragDataAction.COPY);
		
		getMaster().setShowResizeBar(true);
		getDetail().setShowResizeBar(true);				
		
		getViewPanel().addMembers(getMaster(), getDetail(), createMoveControl(),_sapDepTree);
		
	}
	
	private Layout createMoveControl() {
		VStack moveControls = new VStack(10);  
        moveControls.setWidth(32);  
        moveControls.setHeight100();  
        moveControls.setLayoutAlign(Alignment.CENTER);  
  
        TransferImgButton rightArrow = new TransferImgButton(TransferImgButton.RIGHT, new ClickHandler() {			
			@Override
			public void onClick(ClickEvent event) {					
				getDetail().getListGrid().removeData(getDetail().getSelectedRecord());
				getDetail().getMainDataSource().save();				
			}
		});         
        
        moveControls.addMember(rightArrow); 
  
        TransferImgButton leftArrow = new TransferImgButton(TransferImgButton.LEFT, new ClickHandler() {			
			@Override
			public void onClick(ClickEvent event) {
				Record rec = new Record();				 
				rec.setAttribute("ID_DEP", getMaster().getSelectedRecord().getAttribute("ID"));
				rec.setAttribute("SAP_DEP", _sapDepTree.getSelectedRecord().getAttribute("ID"));
				rec.setAttribute("SAP_DEP_NAME", _sapDepTree.getSelectedRecord().getAttribute("DEP_NAME"));
				getDetail().getListGrid().addData(rec);
				getDetail().getMainDataSource().save();					
			}
		});
        
        moveControls.addMember(leftArrow);
        return moveControls;
	}
	
	

	
	@Override
	public void prepareRequestData() {
		getMaster().prepareRequestData();		
		getDetail().prepareRequestData();
		_sapDepTree.prepareRequestData();		
	}

	@Override
	public void putRequestToQueue() {
		super.putRequestToQueue();
		getMaster().putRequestToQueue();		
		getDetail().putRequestToQueue();
		_sapDepTree.putRequestToQueue();
	}
	
	@Override	
	public String getCaption() {
		return "Маппинг подразделений";			
	}

	@Override
	protected void requestDetailData(ListGridRecord value) {		
		super.requestDetailData(value);
		HashMap<String,String> params = new HashMap<String,String>();		
		params.put("ID_DEP", value.getAttribute("ID"));
		getDetail().prepareRequestData(params);
		//getDetail().putRequestToGatewayQueue();
		GatewayQueue.instance().getProcessor().run();		
	}


}
