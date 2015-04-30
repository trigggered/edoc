package document.ui.client.view.dictionary;

import mdb.core.ui.client.view.data.IDataView;
import document.ui.client.commons.EViewIdent;
import document.ui.client.view.dictionary.ent.DicFavEmployeeGr;

public class DictionaryViewFactory {
  
	public static IDataView create( EViewIdent viewIdent) {
		
		switch (viewIdent) {
			case DocFormOfCorrespondence:		
			case Productions:
			case DocStatus:			
			//case DocScoupe:
			//case DocRisk:	
			case SAPListOfDepartments:			
				return new DictionaryTreeView(viewIdent);
				
			case SignatoryAssists:
			case ApprovalAssists:
				return new DelegationRight(viewIdent);
			case CodeDocReserv:
				return new DicGrViewDateSelect(viewIdent);
				
			case DicBAGrEmp:
			case DicGrEmp:
				  return new DicFavEmployeeGr(viewIdent);				  			  
				
		default:
			return new DictionaryGridView(viewIdent);
		}		
	  
  }
}
