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
		case INPUT_DOC:
		case OUTPUT_DOC:
			//toReturn =  new DocumentCard(correspondenType);
			break;
		case INSIDE_PRIKAZ_DOC:
		case INSIDE_NOTIFICATION_DOC:	
			toReturn= new InsideDocumentCard(correspondenType) ;
			break;
		case INSIDE_PROCEDURE_DOC:
			toReturn= new InsideOrderDocumentCard(correspondenType) ;
			break;
		default:
			break;			
		}			
		
		return toReturn;
		
	}
}
