
package document.ui.client.view;

import java.util.logging.Logger;

import mdb.core.ui.client.app.AppController;
import mdb.core.ui.client.events.ICallbackEvent;
import mdb.core.ui.client.events.IDataEditHandler;
import mdb.core.ui.client.view.IView;
import mdb.core.ui.client.view.data.IDataView;
import mdb.core.ui.client.view.data.grid.GridView;
import mdb.core.ui.client.view.dialogs.SelectDialog;

import com.smartgwt.client.data.Record;
import com.smartgwt.client.widgets.grid.ListGridRecord;

import document.ui.client.commons.ECorrespondentType;
import document.ui.client.commons.EViewIdent;
import document.ui.client.resources.locales.Captions;
import document.ui.client.resources.locales.Images;
import document.ui.client.view.dictionary.DictionaryViewFactory;
import document.ui.client.view.dictionary.ent.MapListOfDepartments;
import document.ui.client.view.doc.DocumentsOfDay;
import document.ui.client.view.doc.DocumentsTreeInOut;
import document.ui.client.view.doc.DocumentsTreeInside;
import document.ui.client.view.doc.HomeView;
import document.ui.client.view.doc.MyDocuments;
import document.ui.client.view.doc.card.DocumentCard;
import document.ui.client.view.doc.search.Search;
import document.ui.client.view.doc.search.SearchView;
import document.ui.shared.MdbEntityConst;

public class ViewFactory {
	
	private static final Logger _logger = Logger.getLogger(ViewFactory.class.getName());

  public static IView create (EViewIdent viewIdent) {
	  
	  _logger.info("Create view for "+viewIdent);
	  
	  switch  (viewIdent) {
	   /*Dictionary*/
	  case DocStatus:
	  case SAPListOfEmployee:	  
	  case SAPListOfDepartments:
	  case ListOfDepartments:  		  	  
	  case DocScoupe:	  
	  case DocRisk:
	  case DocFormOfCorrespondence:
	  case Productions:
	  case FavoritesEmp:
	  case FavoritesDoc:
	  case DicDocTypeOfOrder:	  
	  case DicBA:
	  case SignatoryAssists:
	  case ApprovalAssists:
	  case NoMailingEmp:
	  case DicRegions:
	  case SAP_HR_PERS_HTEXT:
	  case SAP_HR_PERS_HTEXT_V:
	  case SAP_HR_PERS_ORGEH:
	  case SAP_HR_PERS_WERKS:
	  case CodeDocReserv:
		  return DictionaryViewFactory.create(viewIdent);	  
		 
	  case MapListOfDepartments:
		  return new MapListOfDepartments();		  
	  case Home:
		  return new HomeView();
	  case DocumentsOfDay:
		  return new DocumentsOfDay();
	  case MyDocs:
		  return new MyDocuments();
	  case Search : 
		  return new SearchView();		  
	  case SearchById:
		  Search.searchDocById();
		  return null;
	  case InDoc:
	  case	OutDoc:
		  return new DocumentsTreeInOut(viewIdent);
	  case	InsideDoc:		  
		  return new DocumentsTreeInside(viewIdent);
		  
	  case BAWorkspace:
		  return new BAWorkspace();
	  case  NewInDoc:
		  return DocumentCard.newDocument(ECorrespondentType.INPUT_DOC);
	  case  NewOutDoc:		
		  return DocumentCard.newDocument(ECorrespondentType.OUTPUT_DOC);		  
	  case  NewInsideCommandDoc:
		  return DocumentCard.newDocument(ECorrespondentType.INSIDE_PRIKAZ_DOC);
	  case  NewInsideNotificationDoc:
		  return DocumentCard.newDocument(ECorrespondentType.INSIDE_NOTIFICATION_DOC);
	  case  NewInsideOrderDoc:
		  return DocumentCard.newDocument(ECorrespondentType.INSIDE_PROCEDURE_DOC);
	  case	AssignRoles:
		  return null;
	  case RoleActions:
		  return null;	
	case Reports:
			return null;
	case UserGuide:
		return new HTMLViewer();
	default:
		return null;
	  }
		  
  }
  
  public static void viewInitialize (EViewIdent viewIdent, final IDataView view) {
	   
	  _logger.info("Initialize view for "+viewIdent);
	  switch (viewIdent) {
	  case SignatoryAssists:
		  	view.setMainEntityId( MdbEntityConst.DicSignatories);
			view.setCaption(Captions.DIC_SIGN_LIST);
			view.addInsertEvent(new IDataEditHandler() {				
				@Override
				public void onEdit(Record record) {
					SelectDialog.view(MdbEntityConst.EMP_LIST,true, new ICallbackEvent<Record[]>() {
						
						@Override
						public void doWork(Record[] data) {
					
							for (Record rec : data) {
								ListGridRecord newRecord = new ListGridRecord();	
								newRecord.setAttribute("OFFICER_NUM", rec.getAttribute("OFFICER_NUM"));								
								newRecord.setAttribute("RANG", rec.getAttribute("RANG"));															
								newRecord.setAttribute("FULL_NAME", rec.getAttribute("FULL_NAME"));		
								newRecord.setAttribute("JOB_NAME", rec.getAttribute("JOB_NAME"));
								newRecord.setAttribute("DEP_ABBR", rec.getAttribute("DEP_ABBR"));
								newRecord.setAttribute("SAP_DEP_NAME", rec.getAttribute("SAP_DEP_NAME"));
								GridView grView = (GridView)view; 
								grView.getListGrid().getDataSource().addData(newRecord);					
							}
						}
					});	 					
				}
			});
		  break;
	  case DicBA:
		  	view.setMainEntityId( MdbEntityConst.DicBA);
			view.setCaption(Captions.DIC_BA_LIST);
			view.addInsertEvent(new IDataEditHandler() {				
				@Override
				public void onEdit(Record record) {
					SelectDialog.view(MdbEntityConst.EMP_LIST,true, new ICallbackEvent<Record[]>() {						
						@Override
						public void doWork(Record[] data) {
					
							for (Record rec : data) {
								ListGridRecord newRecord = new ListGridRecord();	
								newRecord.setAttribute("OFFICER_NUM", rec.getAttribute("OFFICER_NUM"));
								newRecord.setAttribute("RANG", rec.getAttribute("RANG"));
								newRecord.setAttribute("FULL_NAME", rec.getAttribute("FULL_NAME"));								
								newRecord.setAttribute("JOB_NAME", rec.getAttribute("JOB_NAME"));
								//newRecord.setAttribute("DEP_ABBR", rec.getAttribute("DEP_ABBR"));
								//newRecord.setAttribute("SAP_DEP_NAME", rec.getAttribute("SAP_DEP_NAME"));
								newRecord.setAttribute("IS_ACTIVE", 0);
								
								GridView grView = (GridView)view; 
								grView.getListGrid().getDataSource().addData(newRecord);					
							}
						}
					});	 					
				}
			});
		  break;	  		
		/*Grid*/
		case DicDocTypeOfOrder:
			view.setMainEntityId( MdbEntityConst.DicDocTypeOfOrder);
			view.setCaption(Captions.DIC_ORDER_TYPE);
			break;			
		case ListOfDepartments:
			view.setMainEntityId(MdbEntityConst.DicListOfDepartments);
			view.setCaption(Captions.DEVISIONS);
			break;
		case MapListOfDepartments:
			view.setMainEntityId(MdbEntityConst.MapListOfDepartments);
			view.setCaption("Маппинг Подразделений");
			break;
		case SAPListOfEmployee:
			view.setMainEntityId( MdbEntityConst.SAPListOfEmployee);
			view.setCaption("Сотрудники SAP");
			break;	
		case SAPListOfDepartments:
			view.setMainEntityId( MdbEntityConst.SAPListOfDepartments);
			view.setCaption("Подразделения SAP");
			break;	
		case SAPGJobs:
			view.setMainEntityId(MdbEntityConst.SAPGJobs);
			view.setCaption("Груповые должности SAP");
			break;			
		case SAPJobs:
			view.setMainEntityId(MdbEntityConst.SAPJobs);
			view.setCaption("Должности SAP");
			break;			
			
		  case SAP_HR_PERS_HTEXT:
			  view.setMainEntityId(MdbEntityConst.SAP_HR_PERS_HTEXT);
			  view.setCaption("Классификация подразделения (принадлежность к ГБ или сети)");
			  break;
		  case SAP_HR_PERS_HTEXT_V:
			  view.setMainEntityId(MdbEntityConst.SAP_HR_PERS_HTEXT_V);
			  view.setCaption("Вертикаль");
			  break;
		  case SAP_HR_PERS_ORGEH:
			  view.setMainEntityId(MdbEntityConst.SAP_HR_PERS_ORGEH);
			  view.setCaption(Captions.DEVISIONS );
			  break;
		  case SAP_HR_PERS_WERKS:
			  view.setMainEntityId(MdbEntityConst.SAP_HR_PERS_WERKS);
			  view.setCaption(Captions.DIC_EMP_REGION);
			  break;
		  case DicRegions:
			  view.setMainEntityId(MdbEntityConst.DicRegions);
			  view.setCaption(Captions.REGION);
			  break;
		  case CodeDocReserv:
			  view.setMainEntityId(MdbEntityConst.CodeDocReserv);
			  view.setCaption(Captions.CODE_DOC_RESERV);
			  break;
		case NoMailingEmp:
			view.setMainEntityId(MdbEntityConst.NoMailingEmp);					
			view.setCaption(Captions.ExclusionFromMailing);
			view.addInsertEvent(new IDataEditHandler() {
				
				@Override
				public void onEdit(Record record) {
					SelectDialog.view(MdbEntityConst.EMP_LIST,true, new ICallbackEvent<Record[]>() {
						
						@Override
						public void doWork(Record[] data) {
					
							for (Record rec : data) {
								ListGridRecord newRecord = new ListGridRecord();																
								
								newRecord.setAttribute("OFFICER_NUM", rec.getAttribute("OFFICER_NUM"));
								newRecord.setAttribute("DEP_ABBR", rec.getAttribute("DEP_ABBR"));								
								newRecord.setAttribute("DEP_NAME", rec.getAttribute("DEP_NAME"));
								newRecord.setAttribute("RANG", rec.getAttribute("RANG"));															
								newRecord.setAttribute("FULL_NAME", rec.getAttribute("FULL_NAME"));
								newRecord.setAttribute("JOB_NAME", rec.getAttribute("JOB_NAME"));
								newRecord.setAttribute("SAP_DEP_NAME", rec.getAttribute("SAP_DEP_NAME"));
								
								GridView grView = (GridView)view; 
								grView.getListGrid().getDataSource().addData(newRecord);					
							}
						}
					});	 					
				}
			});
			break;
				
		case FavoritesEmp:
			view.setMainEntityId(MdbEntityConst.FAVORITES_EMP);
			view.getParams().add("CURRENT_USER", String.valueOf(AppController.getInstance().getCurrentUser().getId()) );		
			view.setCaption(Captions.FAVORIT_EMP);
			view.addInsertEvent(new IDataEditHandler() {
				
				@Override
				public void onEdit(Record record) {
					SelectDialog.view(MdbEntityConst.EMP_LIST,true, new ICallbackEvent<Record[]>() {
						
						@Override
						public void doWork(Record[] data) {
					
							for (Record rec : data) {
								ListGridRecord newRecord = new ListGridRecord();	
								
								newRecord.setAttribute("EMP_OWNER", AppController.getInstance().getCurrentUser().getId() );
								newRecord.setAttribute("EMP_FAVORITES", rec.getAttribute("OFFICER_NUM"));
								newRecord.setAttribute("OFFICER_NUM", rec.getAttribute("OFFICER_NUM"));
								newRecord.setAttribute("DEP_ABBR", rec.getAttribute("DEP_ABBR"));								
								newRecord.setAttribute("DEP_NAME", rec.getAttribute("DEP_NAME"));
								newRecord.setAttribute("RANG", rec.getAttribute("RANG"));															
								newRecord.setAttribute("FULL_NAME", rec.getAttribute("FULL_NAME"));
								newRecord.setAttribute("JOB_NAME", rec.getAttribute("JOB_NAME"));
								newRecord.setAttribute("SAP_DEP_NAME", rec.getAttribute("SAP_DEP_NAME"));
								
								GridView grView = (GridView)view; 
								grView.getListGrid().getDataSource().addData(newRecord);					
							}
						}
					});	 					
				}
			});
			break;
		case FavoritesDoc:
			view.setMainEntityId(MdbEntityConst.FAVORITES_DOC);
			view.getParams().add("CURRENT_USER", String.valueOf(AppController.getInstance().getCurrentUser().getId()) );		
			view.setCaption(Captions.FAVORIT_DOC );
			view.addEditEvent (DocumentCard.getShowEditViewHandler());
			
			view.addInsertEvent(new IDataEditHandler() {
				
				@Override
				public void onEdit(Record record) {
					SelectDialog.view(MdbEntityConst.DOC_LIST,true, new ICallbackEvent<Record[]>() {
						
						@Override
						public void doWork(Record[] data) {
					
							for (Record rec : data) {
								ListGridRecord newRecord = new ListGridRecord();	
									
									newRecord.setAttribute("ID_DOC", rec.getAttribute("ID_DOC"));									
									newRecord.setAttribute("DOC_CODE", rec.getAttribute("DOC_CODE"));			
									newRecord.setAttribute("NAME", rec.getAttribute("NAME"));																
									newRecord.setAttribute("CURRENT_USER", String.valueOf(AppController.getInstance().getCurrentUser().getId()) );  
						            newRecord.setAttribute("CORR_TYPE_FULL", rec.getAttribute("CORR_TYPE_FULL"));
						            newRecord.setAttribute("DATE_IN", rec.getAttribute("DATE_IN"));
						            newRecord.setAttribute("DATE_EFFECTIVE", rec.getAttribute("DATE_EFFECTIVE"));
						            newRecord.setAttribute("DATE_EXPIRE", rec.getAttribute("DATE_EXPIRE"));
						            newRecord.setAttribute("EMP_AUTHOR", rec.getAttribute("EMP_AUTHOR"));
						            newRecord.setAttribute("OWNER_DEP_NAME", rec.getAttribute("OWNER_DEP_NAME"));
						            newRecord.setAttribute("STATUS", rec.getAttribute("STATUS"));
						            newRecord.setAttribute("DATE_PUB", rec.getAttribute("DATE_PUB"));
						              
								GridView grView = (GridView)view; 
								grView.getListGrid().getDataSource().addData(newRecord);					
							}
						}
					});	 
					
				}
			});
			break;		
		/*Tree*/	
		case DocFormOfCorrespondence:
			view.setMainEntityId(MdbEntityConst.DocFormOfCorrespondence);
			view.setCaption(Captions.DIC_CORR_TYPE);
			break;		
		case DocStatus:
			view.setMainEntityId(MdbEntityConst.DicDocStatus);
			view.setCaption(Captions.DIC_DOC_STATUS );
			break;
		case DocScoupe:
			view.setMainEntityId(MdbEntityConst.DocScoupe);
			view.setCaption(Captions.DIC_ORDER_SCOUP );
			break;			
		case DocRisk:
			view.setMainEntityId(MdbEntityConst.DocRisk);
			view.setCaption(Captions.DIC_RISC_ZONE);		
			break;		
		case	InsideDoc:			
			view.setCaption(Captions.ALL_DOCS);
			view.setImgCaption(Images.ALL_DOCS);
			break;		  
		case 	AllDocs:
			view.setCaption(Captions.ALL_DOCS_IN_PERIOD);
			break;
		case DocumentCard:
			view.setCaption(Captions.DOC_CARD);			
			break;		
		default:
			break;				
		}  
  	}  
}
