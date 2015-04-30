/**
 * 
 */
package document.ui.client.view.doc.card;

import document.ui.client.commons.ECorrespondentType;


/**
 * @author azhuk
 * Creation date: Mar 19, 2014
 *
 */
public class DocumentCardFactory {

	
	public static DocumentCard create(ECorrespondentType  correspondenType) {
		DocumentCard toReturn = null;
		switch (correspondenType) {
		case INPUT:
		case OUTPUT:
			//toReturn =  new DocumentCard(correspondenType);
			break;
		case INSIDE_PRIKAZ:
		case INSIDE_NOTIFICATION:	
			toReturn= new InsideDocumentCard(correspondenType) ;
			break;
		case INSIDE_PROCEDURE:
			toReturn= new InsideProcedureDocumentCard(correspondenType) ;
			break;
		case ACCOUNT_MODEL:
			toReturn= new AccountModelDocumentCard(correspondenType) ;
		default:
			break;			
		}			
		
		return toReturn;
		
	}
}
