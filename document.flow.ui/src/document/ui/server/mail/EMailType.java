/**
 * 
 */
package document.ui.server.mail;


public enum EMailType{
	ToAccepting,
	ToSignatory,
	ToRecipients,
	ToExecutor,
	CancelPublishEmailToAuthor, 
	CancelSignEmailToAuthor,
	CancelApprovalEmailToAuthor,		
	ToAcceptingNewVersion,
	
	DocumentApproved,
	DocumentSigned,
	DocumentPublished, 
	
	ApproveCurentUser,
	NotApproveCurentUser, 
	ChangeDocStatus 
	
}