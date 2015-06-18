/**
 * 
 */
package document.ui.server.mail;


public enum EMailType{
	ToAccepting,
	ToSignatory,
	ToRecipients,
	ToExecutor,
	ToAuthorCancelPublish, 
	ToAuthorCancelSign,
	ToAuthorCancelApproval,
	ToAuthorCancel,
	ToAcceptingNewVersion,
	
	DocumentApproved,
	DocumentSigned,
	DocumentPublished, 
	
	ApproveCurentUser,	
	NotApproveCurentUser,
	ReqApprovalFromUser,
	ChangeDocStatus,
	RevokeDoc
	
	
}