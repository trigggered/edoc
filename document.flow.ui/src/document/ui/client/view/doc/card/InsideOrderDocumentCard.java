/**
 * 
 */
package document.ui.client.view.doc.card;

import document.ui.client.commons.ECorrespondentType;
import document.ui.client.view.doc.card.section.DataFieldsSection;
import document.ui.client.view.doc.card.section.DataGridSection;
import document.ui.client.view.doc.card.section.EDocumentDataSection;
import document.ui.shared.MdbEntityConst;

/**
 * @author azhuk
 * Creation date: May 23, 2014
 *
 */
public class InsideOrderDocumentCard  extends InsideDocumentCard{

	private DataFieldsSection _docOrderFields;		
	private DataGridSection _docRelations;

	
	public InsideOrderDocumentCard(ECorrespondentType correspondentType) {
		super(correspondentType);		
	}
	
	@Override
	protected void initCorrespondentTypeRootCode() {
		setCorrespondentTypeRootCode(ECorrespondentType.INSIDE_PROCEDURE_DOC);
	}
	
	@Override
	protected void createDocumentParamsTab() {
		super.createDocumentParamsTab();
		_docRelations = createDocDataSection(EDocumentDataSection.DocRelations);	
	}
	
	/**
	 * @param order
	 * @return
	 */
	@Override
	protected DataFieldsSection createDocAddData() {		
		_docOrderFields = new DataFieldsSection(this);		
		_docOrderFields.setMainEntityId(MdbEntityConst.DocOrderCard);
		_layAddDocFields.addMember(_docOrderFields);
		_hmDataSections.put(EDocumentDataSection.OrderFields,_docOrderFields);
		super.createDocAddData();
		return _docOrderFields;
	}
	
	@Override
	public void prepareRequestData() {
		super.prepareRequestData();
		_docOrderFields.prepareRequestData();
		_docRelations.prepareRequestData();		
		_docOrderFields.getDataBinder().getDataProvider().getRequest().setPosition(2);	
	}
	

	@Override
	public void putRequestToQueue() {
		super.putRequestToQueue();
		_docOrderFields.putRequestToQueue();		
		_docRelations.putRequestToQueue();
	}	
	
	@Override
	public void prepareSavedData() {
		super.prepareSavedData();
		_docOrderFields.prepareSavedData();
		_docRelations.prepareSavedData();
	}
	
	@Override
	public void putSavedDataToQueue() {
		super.putSavedDataToQueue();
		_docOrderFields.putSavedDataToQueue();
		_docRelations.putSavedDataToQueue();
	}	
	
}
