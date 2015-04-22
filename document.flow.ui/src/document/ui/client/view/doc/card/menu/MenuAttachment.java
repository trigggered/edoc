/**
 * 
 */
package document.ui.client.view.doc.card.menu;

import java.util.logging.Logger;

import mdb.core.ui.client.command.ICommand;
import mdb.core.ui.client.events.IDataEditHandler;
import mdb.core.ui.client.events.IDoubleValuesCallbackEvent;
import mdb.core.ui.client.view.components.menu.IMenuButtons;
import mdb.core.ui.client.view.components.menu.IMenuItem;
import mdb.core.ui.client.view.components.menu.Menu;
import mdb.core.ui.client.view.components.menu.data.AMenuData;
import mdb.core.ui.client.view.data.IDataView;
import mdb.core.ui.client.view.dialogs.input.InputTextDialog;
import mdb.core.ui.client.view.dialogs.message.Dialogs;

import com.smartgwt.client.data.Record;
import com.smartgwt.client.util.BooleanCallback;
import com.smartgwt.client.util.SC;

import document.ui.client.commons.checker.CheckDocumentUserRight;
import document.ui.client.resources.locales.Captions;
import document.ui.client.view.doc.card.section.DataGridSection;
import document.ui.client.view.doc.card.section.EDocumentDataSection;


/**
 * @author azhuk
 * Creation date: Aug 7, 2014
 *
 */
public class MenuAttachment extends Menu {	
	
	private static final Logger _logger = Logger
			.getLogger(MenuAttachment.class.getName());
	
	DataGridSection _view;
	
	public MenuAttachment(DataGridSection view) {
		super("MenuAttachment");
		_view = view;	
		
		_view.addDeleteEvent(new IDataEditHandler() {
			
			@Override
			public void onEdit(Record record) {								
					_view.getMainDataSource().getDataSource().removeData(record);				
			}
		});
		
		IMenuItem item = addItem( Captions.ADD_ATACH , "", IMenuItem.ItemType.ToolButton,0);
		AMenuData.initPropertyMenuItem(item, IMenuButtons.Buttons.dataInsert );
		item.setCommand(new ICommand<IMenuItem>() {
			
			@Override
			public void execute(IMenuItem sender) {
				if (CheckDocumentUserRight.isCanAddAttachments(_view.getDocumentCard())) {
				//if ( _view.getDocumentCard().getDocumentStatus() == EDocStatus.Draft ) {
					_view.callInsertEvent();									
				} else {
					Dialogs.ShowMessage(Captions.CANOT_ADD_ATTACH );		
				}
			}
		});		
	
	
		item = addItem(Captions.ADD_ATTACH_VERSION, "", IMenuItem.ItemType.ToolButton,0);
		item.setImg(AMenuData.getButtonInitialiser().get(IMenuButtons.Buttons.dataEdit).getImg());		
		item.setCommand(new ICommand<IMenuItem>() {
					
			@Override
			public void execute(IMenuItem sender) {
				
				
				CheckDocumentUserRight.isCanAddNewVersion(_view.getDocumentCard(), new BooleanCallback() {
					
					@Override
					public void execute(Boolean value) {
						if (!value) {
							Dialogs.ShowMessage(Captions.CANOT_ADD_ATTACH_VERSION );
							return;
						}
						
						
						Record rec = _view.getSelectedRecord();
						if ( rec== null) {
							Dialogs.ShowMessage(Captions.MUST_CHOSE_ATTACH );

						} else {										
							SC.ask(Captions.Q_ADD_NEW_VERSION +rec.getAttributeAsString("NAME_LOB"), 
									new BooleanCallback() {											
								@Override
								public void execute(Boolean value) {
									if (!value) return;
									_view.callEditEvent();
								}
							});										
						}	
						
						
					}
				});																		
										
			}
		});	
		
		item = addItem(Captions.DOWNLOAD, "", IMenuItem.ItemType.ToolButton,0);
		item.setImg(AMenuData.getButtonInitialiser().get(IMenuButtons.Buttons.download).getImg());
		
		item.setCommand(new ICommand<IMenuItem>() {
			
			@Override
			public void execute(IMenuItem sender) {
				_view.callViewEvent();									
			}
		});
		
		
		item = addItem(Captions.RENAME , "", IMenuItem.ItemType.ToolButton,0);
		item.setCommand(new ICommand<IMenuItem>() {
			
			@Override
			public void execute(IMenuItem sender) {
				 if (!CheckDocumentUserRight.isHasBARole()) {
					 return;
				 }
				 
				 IDataView mainFieldView =  _view.getDocumentCard().getDataSections().get(EDocumentDataSection.MainFields);
				 String docCode  = mainFieldView.getSelectedRecord().getAttribute("DOC_CODE");
				 final Record editRec = _view.getSelectedRecord();
				 
				 String attachName = editRec.getAttribute("NAME_LOB");
				   
				 _logger.info("Doc code is"+docCode);
				 if (docCode != null) {
					 String atachExt = null;
					 int pointIdx = attachName.lastIndexOf(".");
					 if (pointIdx > 0 ) {
						 atachExt = attachName.substring(pointIdx);
					 }
					 
					 
					 attachName = atachExt!=null? docCode+atachExt:attachName;
				 }				 				 
				 
				 InputTextDialog dlg = new InputTextDialog (Captions.FILE_NAME, Captions.ENTER_NEW_FILE_NAME , attachName, new IDoubleValuesCallbackEvent<Boolean, String>() {
				 	
					@Override
					public void execute(Boolean value1, String value2) {
						if (value1) {
							editRec.setAttribute("NAME_LOB", value2);	
							_view.getMainDataSource().getDataSource().updateData(editRec);
							_view.getListGrid().redraw();
						}						
					}
				});
				 
				 dlg.view();
			}
		});
		
		
		item = addItem(Captions.SHOW_HIDE_VERSION, "", IMenuItem.ItemType.ToolButton,0);
		item.setCommand(new ICommand<IMenuItem>() {
			
			@Override
			public void execute(IMenuItem sender) {
				 IDataView attachView =  _view.getDocumentCard().getDataSections().get(EDocumentDataSection.Attachments);
				 if (attachView!= null) {
					 
					 if ( attachView.getParams().paramExist("ISLEAF") ) {
						 attachView.getParams().clear();
					 } 
					 else { 
						 attachView.getParams().add("ISLEAF", "1");					 
					 }
					 
					 attachView.redraw();
				 }
				
			}
		});
		
		item = addItem(Captions.DELETE , "", IMenuItem.ItemType.ToolButton,0);
		AMenuData.initPropertyMenuItem(item, IMenuButtons.Buttons.dataDelete);
		
		item.setCommand(new ICommand<IMenuItem>() {								
			@Override
			public void execute(IMenuItem sender) {

				
				if (!CheckDocumentUserRight.isCanDeleteAtatchment(_view.getDocumentCard()) ) {
					Dialogs.ShowMessage(Captions.CANOT_DEL_ATTACH);					
					return;
				}
				
				Record rec = _view.getSelectedRecord();
				if ( rec== null) {
					Dialogs.ShowMessage(Captions.NOT_CHOUSE_ATTACH);					
				}
				else {
					 
					SC.ask(Captions.Q_DELETE_CURRENT_ATTACHMENT+ rec.getAttributeAsString("NAME_LOB")+ " ?", new BooleanCallback() {
						
						@Override
						public void execute(Boolean value) {
							if (value) {
								_view.callDeleteEvent();
							}
						}
					} );
				}				
			}
		});																	
	}
}
