/**
 * 
 */
package document.ui.client.commons.checker;

import java.util.logging.Logger;

import mdb.core.ui.client.app.AppController;
import mdb.core.ui.client.events.ICallbackEvent;

import com.smartgwt.client.util.BooleanCallback;

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
		_logger.info("Document Author="+ card.getDocumentAuthor());
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
		 for ( String role : AppController.getInstance().getCurrentUser().getRoles() ) {			 
			  if( role.equals("1") ) {
				  return true;
			  }		 
		 }	
		 return  false;
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
								CheckDocumentUserRight.isCurrentUserHasApproveRight(card, callBack);	
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
			
			_logger.info("isCanEditDocument = "+toReturn);
			return toReturn;
	}
	
	
	public static  void isCurrentUserHasApproveRight(DocumentCard card, final BooleanCallback result) {	
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

		_logger.info("Check is has BA Role");
		final boolean isBa = CheckDocumentUserRight.isHasBARole();				
		_logger.info("BA Role="+isBa);
		
		_logger.info("Check is User BA or Author");
		
		final  boolean  isAuthor; 
		if (docCard.getViewState() == EViewState.New) {
			isAuthor = true;
		}else  {
			isAuthor=  CheckDocumentUserRight.isAuthorCurrentUser(docCard);	
		}
		
		final  boolean  isUserBAorAuthor = isBa || isAuthor;
		
		//final  boolean  isUserBAorAuthor =(docCard.getViewState() == EViewState.New)?true:isBa || CheckDocumentUserRight.isAuthorCurrentUser(docCard);
		_logger.info("isUserBAorAuthor = "+isUserBAorAuthor);	 						
		
		
		_logger.info("changeVisibleControls:  isBa=" +isBa+ " isUsetBAorAuthor="+isUserBAorAuthor+ " status = "+docCard.getDocumentStatus() );
			switch (docCard.getDocumentStatus() ) {
			case Draft :
				docCard.visibleButtons(new Boolean[]{true,true,isAuthor,false,false, false,false,false,false});						
				break;
			case AtTheApproval:
				
					CheckDocumentUserRight.isCurrentUserHasApproveRight(docCard, new BooleanCallback() {						
						@Override
						public void execute(Boolean value) {
							docCard.visibleButtons(new Boolean[]{isUserBAorAuthor || value,true,false,false,false, false, true,true,true});
						}
					});									
				break;
			case Approval:
				boolean isCommandDoc = docCard.getCorrespondentType() == ECorrespondentType.INSIDE_PRIKAZ;
				docCard.visibleButtons(new Boolean[]{isUserBAorAuthor,true,false,isUserBAorAuthor && isCommandDoc,false, false, false, false,false});
				break;
			case AtTheSigning:				
				CheckDocumentUserRight.isCanSignatoryDoc(docCard, new BooleanCallback() {					
					@Override
					public void execute(Boolean value) {						
						docCard.visibleButtons(new Boolean[]{value||isBa,true,false,false,value,value, false,false,false});
					}
				});												
				return;				
			case Signed:				
			case Valid:				
			case Revoked:
			case Cancelled:
				docCard.visibleButtons(new Boolean[]{isBa,true,false,false,false,false, false,false,false});
				break;							
			default:
				docCard.visibleButtons(new Boolean[]{true,true,true,false,false, false,false,false,false});
				break;
		}		
	}
	
}
