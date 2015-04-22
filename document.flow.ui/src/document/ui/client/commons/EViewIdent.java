/**
 * 
 */
package document.ui.client.commons;


/**
 * @author azhuk
 * Creation date: May 14, 2014
 *
 */
public enum EViewIdent {
	Unknown,
	/*Dic ORG UNIT*/
	SAPListOfEmployee,
	SAPListOfDepartments,
	SAPGJobs,
	SAPJobs,	
	ListOfDepartments,
	MapListOfDepartments,
	
	SAP_HR_PERS_HTEXT,
	SAP_HR_PERS_HTEXT_V,
	SAP_HR_PERS_ORGEH,
	SAP_HR_PERS_WERKS,
	
	//EmployeesList,
	FavoritesEmp,
	EmployeeSearch,				
	
	/*Dictionary*/
	DocStatus,
	DocScoupe,	
	DocRisk,
	DocFormOfCorrespondence,
	Productions,
	DicDocTypeOfOrder,
	SignatoryAssists,
	DicBA, 
	DicRegions,
	
	/*Security*/		
	RoleActions,
	AssignRoles,
	
	/*Document management */
	DocEmpRecipients,
	DocEmpSigners,
	DocApplySign,
	Home,
	DocumentsOfDay,
	Search,
	SearchById,
	SearchByCode,
	InDoc,
	OutDoc,		
	InsideDoc,
	NewInDoc,
	NewOutDoc,		
	BAWorkspace, 
	
	NewInsideCommandDoc,
	//NewInsideDoc,
	NewInsideOrderDoc,
	NewInsideNotificationDoc,
	
	
	AllDocs,
	MyDocs,
	DocumentCard,
	Reports,
	FavoritesDoc,
	AcceptingProcess,
	NoMailingEmp, 
	ApprovalAssists,
	CodeDocReserv,
	UserGuide
}
