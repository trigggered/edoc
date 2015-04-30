/**
 * 
 */
package document.ui.client.view.doc.card;

import document.ui.client.commons.ECorrespondentType;
import document.ui.client.view.doc.card.section.DataFieldsSection;
import document.ui.client.view.doc.card.section.EDocumentDataSection;
import document.ui.shared.MdbEntityConst;

/**
 * @author azhuk
 * Creation date: May 23, 2014
 *
 */
public class InsideProcedureDocumentCard  extends InsideDocumentCard{


	public InsideProcedureDocumentCard(ECorrespondentType correspondentType) {
		super(correspondentType);		
	}
	
	
	@Override
	protected void createComponents() {			
		
		setCorrespondentType(ECorrespondentType.INSIDE_PROCEDURE);
		super.createComponents();		
	}
	
	@Override
	protected void createGridSections() {
		super.createGridSections();
		createDataSection(EDocumentDataSection.DocRelations);	
	}
	
	/**
	 * @param order
	 * @return
	 */
	@Override
	protected void createFieldSections() {		
		DataFieldsSection docOrderFields = new DataFieldsSection(this);		
		docOrderFields.setMainEntityId(MdbEntityConst.DocProcedureCard);
		_layAddDocFields.addMember(docOrderFields);
		_hmDataSections.put(EDocumentDataSection.ProcedureFields,docOrderFields);
		super.createFieldSections();		
		
	}
	
	@Override
	public void prepareRequestData() {
		super.prepareRequestData();
		_hmDataSections.get(EDocumentDataSection.ProcedureFields).getDataProvider().getRequest().setPosition(2);	
	}
	
	@Override
	public void prepareSavedData() {
		super.prepareSavedData();
		
		DataFieldsSection section = (DataFieldsSection) _hmDataSections.get(EDocumentDataSection.ProcedureFields);
		section.prepareSavedData();
	}
	
	
/*
 * 
	@Override
	public void putRequestToQueue() {
		super.putRequestToQueue();
		_docOrderFields.putRequestToQueue();		
		_docRelations.putRequestToQueue();
	}	
	*/
	/*
	@Override
	public void prepareSavedData() {
		super.prepareSavedData();
		_docOrderFields.prepareSavedData();
		_docRelations.prepareSavedData();
	}
	*/
	/*
	@Override
	public void putSavedDataToQueue() {
		super.putSavedDataToQueue();
		_docOrderFields.putSavedDataToQueue();
		_docRelations.putSavedDataToQueue();
	}	
	*/
	
}
