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
 * Creation date: Mar 19, 2014
 *
 */
public  class InsideDocumentCard extends DocumentCard{

	private DataGridSection _docAcceptsEmp;
	private DataGridSection _docRecipientsEmp;
	private DataGridSection  _docExecutorsEmp;
	
	private DataFieldsSection _docCancelChangeFields;
	 
	
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
		initCorrespondentTypeRootCode();
		super.createComponents();		
	}	
	
	
	@Override
	protected DataFieldsSection createDocAddData() {

		_docCancelChangeFields = new DataFieldsSection(this);		
		_docCancelChangeFields.setMainEntityId(MdbEntityConst.CANCEL_CHANGE_DOC);
		_docCancelChangeFields.setViewNn(3);
		_layAddDocFields.addMember(_docCancelChangeFields);
		
		_hmDataSections.put(EDocumentDataSection.CancelChangeFields,_docCancelChangeFields);
		return _docCancelChangeFields;
	}

	
	@Override
	protected void createDocumentParamsTab() {
		_docAcceptsEmp = createDocDataSection (EDocumentDataSection.Approval);
		_docRecipientsEmp = createDocDataSection (EDocumentDataSection.Recipients);		
		_docExecutorsEmp = createDocDataSection (EDocumentDataSection.Executors);
	}
	
	
	protected void initCorrespondentTypeRootCode() {
		setCorrespondentTypeRootCode(ECorrespondentType.INSIDE_PRIKAZ_DOC);
	}


	@Override
	public void prepareRequestData() {
		super.prepareRequestData();
		_docCancelChangeFields.prepareRequestData();
		_docAcceptsEmp.prepareRequestData();
		_docRecipientsEmp.prepareRequestData();
		_docExecutorsEmp.prepareRequestData();
	}
	

	@Override
	public void putRequestToQueue() {
		super.putRequestToQueue();
		_docCancelChangeFields.putRequestToQueue();
		_docAcceptsEmp.putRequestToQueue();
		_docRecipientsEmp.putRequestToQueue();
		_docExecutorsEmp.putRequestToQueue();
	}	
	
	@Override
	public void prepareSavedData() {
		super.prepareSavedData();
		_docCancelChangeFields.prepareSavedData();
	}
	
	@Override
	public void putSavedDataToQueue() {
		super.putSavedDataToQueue();
		_docCancelChangeFields.putSavedDataToQueue();
		_docAcceptsEmp.putSavedDataToQueue();
		_docRecipientsEmp.putSavedDataToQueue();
		_docExecutorsEmp.putSavedDataToQueue();		
	}	
	

}
