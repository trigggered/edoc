/**
 * 
 */
package document.ui.client.view.doc.card;

import java.util.logging.Logger;

import document.ui.client.commons.ECorrespondentType;
import document.ui.client.view.doc.card.section.DataFieldsSection;
import document.ui.client.view.doc.card.section.EDocumentDataSection;
import document.ui.shared.MdbEntityConst;

/**
 * @author azhuk
 * Creation date: May 15, 2015
 *
 */
public class AccountModelDocumentCard extends InsideDocumentCard {
	/**
	 * @param correspondenType
	 */
	private static final Logger _logger = Logger
			.getLogger(AccountModelDocumentCard.class.getName());
	
	public AccountModelDocumentCard(ECorrespondentType correspondenType) {
		super(correspondenType);		
	}
	
	
	@Override
	protected void createGridSections() {
		createDataSection (EDocumentDataSection.Approval);
		createDataSection (EDocumentDataSection.Recipients);				
	}

	
	@Override
	protected void createFieldSections() {		
		DataFieldsSection docSection = new DataFieldsSection(this);		
		_layAddDocFields.addMember(docSection);
		docSection.setMainEntityId(MdbEntityConst.DocAccountModelCard);
		_hmDataSections.put(EDocumentDataSection.AccountModelFields,docSection);
		super.createFieldSections();		
	}
	
	protected int getViewnnForCancelChangeDoc() {
		return 4;
	}
	
	@Override
	public void prepareSavedData() {
		super.prepareSavedData();
		DataFieldsSection section = (DataFieldsSection) _hmDataSections.get(EDocumentDataSection.AccountModelFields);
		section.prepareSavedData();
	}	
	
}
