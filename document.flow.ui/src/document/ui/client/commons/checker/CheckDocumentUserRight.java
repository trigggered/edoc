/**
 * 
 */
package document.ui.client.commons.checker;

import java.util.logging.Logger;

import com.smartgwt.client.util.BooleanCallback;

import mdb.core.ui.client.app.AppController;
import mdb.core.ui.client.events.ICallbackEvent;
import document.ui.client.commons.ECorrespondentType;
import document.ui.client.commons.EDocStatus;
import document.ui.client.view.doc.card.DocumentCard;
import document.ui.client.view.doc.card.DocumentCard.EViewState;

/**
 * @author azhuk
 * Creation date: Oct 27, 2014
 *
 */
public class CheckDocumentUserRight {
	
	private static final Logger _logger = Logger
			.getLogger(CheckDocumentUserRight.class.getName());
	
	public static boolean isAuthorCurrentUser (DocumentCard card) {		
		return card.getDocumentAuthor() == AppController.getInstance().getCurrentUser().getId();
	}
	
	public static boolean isCanDeleteAtatchment (DocumentCard card) {
		
		boolean toReturn =  isHasBARole();
		
		
		 
		 if ( !toReturn) { 
			 /*toReturn = isHasBARole() &&
					 (card.getDocumentStatus() == EDocStatus.AtTheApproval|| card.getDocumentStatus() == EDocStatus.Approval);
					 */
			 toReturn = isAuthorCurrentUser(card) &&  
					 (card.getDocumentStatus() == EDocStatus.Draft || card.getDocumentStatus() == EDocStatus.AtTheApproval);
		 }
		 
		 _logger.info("isCanDeleteAtatchment:"+toReturn );
		 
		 return toReturn;
	}
	
	public static boolean isCanSendToSign (DocumentCard card) {
		return isAuthorCurrentUser(card);
	}
	
	public static boolean isHasBARole() {
		boolean toReturn = false;
		 for ( String role : AppController.getInstance().getCurrentUser().getRoles() ) {
			 toReturn  = role.equals("1");
			  if(toReturn) {
				  return true;
			  }		 
		 }
	
		 return toReturn ;
	}
	
	
	
	public static void isCanAddNewVersion(DocumentCard card, BooleanCallback callBack) {
		
		boolean isBa = isHasBARole();
		boolean isAuthor =  isAuthorCurrentUser(card);
		EDocStatus status = card.getDocumentStatus() ;
		
		if (isBa ) {
			 
			callBack.execute(status != EDocStatus.Cancelled || status != EDocStatus.Revoked); 
		}
		else  if (isAuthor) {
			callBack.execute(status == EDocStatus.Draft || status == EDocStatus.AtTheApproval || status == EDocStatus.Approval);
					
		}
		else {
				switch (card.getDocumentStatus()  ) {
				
						case AtTheApproval:
								CheckDocumentUserRight.isCurrentUserHasApprovRight(card, callBack);	
							break;
						case AtTheSigning:
							 isCanSignatoryDoc(card, callBack);
							 break;
						default:
							callBack.execute(false);
							break;
				}	
			
		}		

	}
	
	
	public static boolean  isCanAddAttachments(DocumentCard card) {
		
		boolean isBa = isHasBARole();
		boolean isAuthor =  isAuthorCurrentUser(card);
		
		switch (card.getDocumentStatus() ) {
			case  Draft:
				return true;
			case AtTheApproval:
				if (isAuthor) {
					return true;
				}				
			default :
				if (isBa ) {				
					return true ;
				}
		}		
		return false;
	}
	


	public static  boolean isCanEditDocument (DocumentCard card) {							 
				 
		  boolean toReturn = false;
		  
			if (    isHasBARole()  ) {
				toReturn  = true;
			}		
			else  if (isAuthorCurrentUser(card) ) 
				{
					toReturn = 	(card.getViewState() == EViewState.New  ||  
								 card.getDocumentStatus() == EDocStatus.Draft  ||
								 card.getDocumentStatus() == EDocStatus.AtTheApproval ||
								 card.getDocumentStatus() == EDocStatus.Approval );					    							 
					
					
				}   
			
			return toReturn;
	}
	
	
	public static  void isCurrentUserHasApprovRight(DocumentCard card, final BooleanCallback result) {	
		CheckCanApproval checker  = new CheckCanApproval();
		checker.isCurrentUserHasRight(card, result);
		
	}
	
	public static  void isCanSignatoryDoc(DocumentCard card, final BooleanCallback mainCallBack) {
		final long docId = card.getDocumentId();
		CheckCanSignatoryDoc checker  = new CheckCanSignatoryDoc();		
		checker.isCan(docId, new BooleanCallback() {
			
			@Override
			public void execute(Boolean value) {
					if (value) {
						CheckDocumentFlowStage checkDocumentFlowStage = new CheckDocumentFlowStage();
						checkDocumentFlowStage.getFlowStage(docId,  new ICallbackEvent<Integer>() {
							
							@Override
							public void doWork(Integer data) {
								mainCallBack.execute(data.intValue() == 3);										
							}
						});
						
					} else {
						mainCallBack.execute(false);
					}
			}
		});
	}

	public static void changeVisibleControls (final DocumentCard docCard) {

		final boolean isBa = CheckDocumentUserRight.isHasBARole();
		//boolean isAuthor =  CheckDocumentUserRight.isAuthorCurrentUser(docCard);
		
		
		final boolean isUsetBAorAuthor = CheckDocumentUserRight.isHasBARole() || CheckDocumentUserRight.isAuthorCurrentUser(docCard);
		
	 
			switch (docCard.getDocumentStatus() ) {
			case Draft :
				docCard.visibleButtons(new Boolean[]{isUsetBAorAuthor,true,true,false,false, false});						
				break;
			case AtTheApproval:
				
					CheckDocumentUserRight.isCurrentUserHasApprovRight(docCard, new BooleanCallback() {						
						@Override
						public void execute(Boolean value) {
							docCard.visibleButtons(new Boolean[]{isUsetBAorAuthor || value,true,false,false,false, false});
						}
					});					
				
				break;
			case Approval:
				boolean isCommandDoc = docCard.getCorrespondentTypeRootCode() == ECorrespondentType.INSIDE_PRIKAZ_DOC;
				docCard.visibleButtons(new Boolean[]{isUsetBAorAuthor,true,false,isUsetBAorAuthor && isCommandDoc,false, false});
				break;
			case AtTheSigning:				
				CheckDocumentUserRight.isCanSignatoryDoc(docCard, new BooleanCallback() {
					
					@Override
					public void execute(Boolean value) {						
						docCard.visibleButtons(new Boolean[]{value||isBa,true,false,false,value,value});
					}
				});				
								
				return;				
			case Signed:				
			case Valid:				
			case Revoked:
			case Cancelled:
				docCard.visibleButtons(new Boolean[]{isBa,true,false,false,false,false});
				break;		
					
			default:
				break;
		}		
	}
	
}
