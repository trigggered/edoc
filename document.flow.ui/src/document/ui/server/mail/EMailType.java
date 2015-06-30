/**
 * 
 */
package document.ui.server.mail;


public enum EMailType{
	ToAccepting,
	ToNextAccepting,
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