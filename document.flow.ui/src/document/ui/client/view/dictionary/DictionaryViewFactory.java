package document.ui.client.view.dictionary;

import mdb.core.ui.client.view.data.IDataView;
import document.ui.client.commons.EViewIdent;

public class DictionaryViewFactory {
  
	public static IDataView create( EViewIdent menuIdent) {
		
		switch (menuIdent) {
			case DocFormOfCorrespondence:		
			case Productions:
			case DocStatus:			
			case DocScoupe:
			case DocRisk:	
			case SAPListOfDepartments:
			
				return new DictionaryTreeView(menuIdent);
			case SignatoryAssists:
			case ApprovalAssists:
				return new DelegationRight(menuIdent);
			case CodeDocReserv:
				return new DicGrViewDateSelect(menuIdent); 
				
		default:
			return new DictionaryGridView(menuIdent);
		}		
	  
  }
}
