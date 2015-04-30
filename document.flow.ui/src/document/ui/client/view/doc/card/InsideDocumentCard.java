/**
 * 
 */
package document.ui.client.view.doc.card;

import java.util.logging.Logger;

import mdb.core.ui.client.app.AppController;
import mdb.core.ui.client.view.data.IDataView;

import com.smartgwt.client.data.Record;
import com.smartgwt.client.widgets.Button;
import com.smartgwt.client.widgets.layout.Layout;

import document.ui.client.commons.ECorrespondentType;
import document.ui.client.resources.locales.Captions;
import document.ui.client.view.doc.card.section.DataFieldsSection;
import document.ui.client.view.doc.card.section.DataGridSection;
import document.ui.client.view.doc.card.section.EDocumentDataSection;
import document.ui.shared.MdbEntityConst;

/**
 * @author azhuk
 * Creation date: Mar 19, 2014
 *
 */
public  class InsideDocumentCard extends DocumentCard{
 
	private static final Logger _logger = Logger.getLogger(InsideDocumentCard.class.getName());
	
	Button _btnApprove;	
	Button _btnNotApprove;	
	
	public InsideDocumentCard() {
	}

	public InsideDocumentCard(ECorrespondentType correspondentType) {
		super(correspondentType);		
	}
	
	/* (non-Javadoc)
	 * @see document.ui.client.view.doc.card.DocumentCard#createComponents()
	 */
	@Override
	protected void createComponents() {			
		
		setCorrespondentType(ECorrespondentType.INSIDE_PRIKAZ);
		super.createComponents();		
	}	
	
	
	@Override
	protected void createFieldSections() {
	
		DataFieldsSection  docCancelChangeFields = new DataFieldsSection(this);		
		docCancelChangeFields.setMainEntityId(MdbEntityConst.CANCEL_CHANGE_DOC);
		docCancelChangeFields.setViewNn(3);
		_layAddDocFields.addMember(docCancelChangeFields);
		
		_hmDataSections.put(EDocumentDataSection.CancelChangeFields,docCancelChangeFields);		
		
	}

	
	@Override
	protected void createGridSections() {
		createDataSection (EDocumentDataSection.Approval);
		createDataSection (EDocumentDataSection.Recipients);
		createDataSection (EDocumentDataSection.Executors);
	}
	
	
	
	
	@Override
	protected Layout createBottomLayout() {
		Layout btnlayout = super.createBottomLayout();
		
		 _btnApprove= new Button();	

		  _btnApprove.setTitle(Captions.APPROVE);
		  _btnApprove.setVisible(false);
		  _btnApprove.addClickHandler(new com.smartgwt.client.widgets.events.ClickHandler() {		
			@Override
			public void onClick(com.smartgwt.client.widgets.events.ClickEvent event) {
				approve(true);
			}
		});	      
	      
	      
		  _btnNotApprove  = new Button();
		  _btnNotApprove.setVisible(false);
		  _btnNotApprove.setTitle(Captions. NOT_APPROVE);
		  _btnNotApprove.addClickHandler(new com.smartgwt.client.widgets.events.ClickHandler() {		
			@Override
			public void onClick(com.smartgwt.client.widgets.events.ClickEvent event) {		
				//docData.callEditEvent();
				approve(false);
			}			
		}); 		  
		  
		 btnlayout.addMembers(_btnApprove, _btnNotApprove);
		return btnlayout ;
	}
	
	
	private void approve(boolean isApprove) {		
		
		DataGridSection docAcceptsEmp = (DataGridSection) _hmDataSections.get(EDocumentDataSection.Approval);		
		String userId = String.valueOf( AppController.getInstance().getCurrentUser().getId());
		for ( Record rec:  docAcceptsEmp.getListGrid().getRecords() ) {
			 if ( rec.getAttribute("OFFICER_NUM").equals(userId))  {
				 	docAcceptsEmp.getListGrid().selectSingleRecord(rec);
					rec.setAttribute("IS_ACCEPT", isApprove?1:0);
					docAcceptsEmp.getListGrid().updateData(rec);
					docAcceptsEmp.callEditEvent();	
				 
				 return;
			 }			 			 
		}		
		_logger.info("Rec not found");		
	}
	
	
	@Override 
	public void visibleButtons(Boolean[] visibles) {
		super.visibleButtons(visibles);		
		
		if (visibles.length >6 ) {
		_btnApprove.setVisible(visibles[6]);
		_btnNotApprove.setVisible(visibles[7]);
		}
	}


	@Override
	public void prepareRequestData() {
		super.prepareRequestData();		
		for (IDataView  view : _hmDataSections.values()) {
			view.prepareRequestData();
		}
		
	}
	

	@Override
	public void putRequestToQueue() {
		super.putRequestToQueue();
		
		for (IDataView  view : _hmDataSections.values()) {
			view.putRequestToQueue();
		}		
	}	
	
	@Override
	public void prepareSavedData() {
		super.prepareSavedData();
		DataFieldsSection section = (DataFieldsSection) _hmDataSections.get(EDocumentDataSection.CancelChangeFields);
		section.prepareSavedData();
	}
	
	@Override
	public void putSavedDataToQueue() {
		super.putSavedDataToQueue();
		
		for (IDataView  view : _hmDataSections.values()) {
			view.putRequestToQueue();		
		}		
	}		

}
