/**
 * 
 */
package document.ui.client.view.doc.card.menu;


import mdb.core.ui.client.command.ICommand;
import mdb.core.ui.client.view.components.menu.IMenu;
import mdb.core.ui.client.view.components.menu.IMenuItem;
import mdb.core.ui.client.view.data.IDataView;
import mdb.core.ui.client.view.dialogs.message.Dialogs;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.smartgwt.client.data.Record;
import mdb.core.ui.client.util.BooleanCallback;

import document.ui.client.commons.EDocStatus;
import document.ui.client.flow.impl.FlowProccessing;
import document.ui.client.resources.locales.Captions;
import document.ui.client.tools.SignControlWrapper;
import document.ui.client.view.doc.card.DocumentActions;
import document.ui.client.view.doc.card.DocumentCard;
import document.ui.client.view.doc.card.section.EDocumentDataSection;

/**
 * @author azhuk
 * Creation date: Mar 23, 2015
 *
 */
public class MenuBAAction extends mdb.core.ui.client.view.components.menu.Menu{
	
	DocumentCard _card;
	
	AsyncCallback<Void> refreshCallBack = new AsyncCallback<Void>() {
		
		@Override
		public void onSuccess(Void result) {
			_card.refresh();
		}
		
		@Override
		public void onFailure(Throwable caught) {
			// TODO Auto-generated method stub
			
			Dialogs.ShowWarnMessage(Captions.ERROR +"\n " + caught.getMessage());
		}
	}; 
			
	public MenuBAAction(DocumentCard card) {
		super("MenuBAAction");
		_card =  card;		
		
		IMenuItem item = addItem(Captions.BA_ACTIONS, "", IMenuItem.ItemType.Menu,0);
		IMenu childMenu = new mdb.core.ui.client.view.components.menu.Menu("Child menu for 'File'");
		item.setChildMenu(childMenu);			
	
		
		item = childMenu.addItem(Captions.TO_REVOKE_STATUS, "", IMenuItem.ItemType.ToolButton,0);
		item.setCommand(new ICommand<IMenuItem>() {						
			@Override
			public void execute(IMenuItem sender) {
				
				Dialogs.AskDialog(Captions.Q_TO_REVOKE_STATUS +_card.getDocumentId(), new BooleanCallback() {
					
					@Override
					public void execute(Boolean value) {
						if (value) {							
							//DocumentActions.udateDocStatus(_card, EDocStatus.Revoked);							
							FlowProccessing.forcedDocumentToStatus(_card.getDocumentId(), EDocStatus.Revoked, refreshCallBack);
						}								
					}
				});										
			}
		});			
		
	// From Petrov D.
		
		item = childMenu.addItem(Captions.TO_APPROVE_STATUS, "", IMenuItem.ItemType.ToolButton,0);
		item.setCommand(new ICommand<IMenuItem>() {						
			@Override
			public void execute(IMenuItem sender) {											
				
				Dialogs.AskDialog(Captions.Q_TO_APPROVE_STATUS, new BooleanCallback() {
					
					@Override
					public void execute(Boolean value) {
						if (value) {
							FlowProccessing.forcedDocumentToStatus(_card.getDocumentId(), EDocStatus.Approved, refreshCallBack);
						}
						
					}
				});
			}

		});

		
		item = childMenu.addItem(Captions.TO_CANCEL_STATUS, "", IMenuItem.ItemType.ToolButton,0);
		item.setCommand(new ICommand<IMenuItem>() {						
			@Override
			public void execute(IMenuItem sender) {											
				
				Dialogs.AskDialog(Captions.Q_TO_CANCEL_STATUS, new BooleanCallback() {
					
					@Override
					public void execute(Boolean value) {
						if (value) {
							FlowProccessing.forcedDocumentToStatus(_card.getDocumentId(), EDocStatus.Cancelled, refreshCallBack);
						}
						
					}
				});
			}

		});
		
		
		item = childMenu.addItem(Captions.ADD_APPROVAL_PERS, "", IMenuItem.ItemType.ToolButton,0);
		item.setCommand(new ICommand<IMenuItem>() {						
			@Override
			public void execute(IMenuItem sender) {												
				
				if (_card.getDocumentStatus() == EDocStatus.Approved) {
					IDataView view =  _card.getDataSections().get(EDocumentDataSection.Approval);
					_card.getMainTabSet().selectTab(view.getViewContainerID());
					//Tab  tab = _mainTabSet.getTab(getDataSections().get(view.getViewContainerID());
					view.setCanEdit(true);												
					view.callInsertEvent();			
				}
			}

		});		
		
		/*
		item = childMenu.addItem(Captions.SEND_TO_APPROVAL, "", IMenuItem.ItemType.ToolButton,0);
		item.setCommand(new ICommand<IMenuItem>() {						
			@Override
			public void execute(IMenuItem sender) {												
				
				if (getDocumentStatus() == EDocStatus.Approval) {
				 
				}					 
			}

		});		
		*/
		/*
		item = childMenu.addItem(Captions.CHECK_SIGN, "", IMenuItem.ItemType.ToolButton,0);
		item.setCommand(new ICommand<IMenuItem>() {						
			@Override
			public void execute(IMenuItem sender) {												
				
				IDataView  attachments= _card.getDataSections().get(EDocumentDataSection.Attachments);
				Record rec = attachments.getSelectedRecord();
				if (rec!=null) {
					String data = rec.getAttribute("HASH_DATA");
					String signature = rec.getAttribute("SIGN_DATA");
					SignControlWrapper.getSignControl().Verify(signature, data, true);
				} else {
					Dialogs.ShowWarnMessage(Captions.NOT_CHOUSE_ATTACH);
				}
			}

		});
		*/
		
		item = childMenu.addItem(Captions.DELETE_D0C, "", IMenuItem.ItemType.ToolButton,0);
		item.setCommand(new ICommand<IMenuItem>() {						
			@Override
			public void execute(IMenuItem sender) {											
				
				Dialogs.AskDialog(Captions.Q_DELETE_D0C, new BooleanCallback() {
					
					@Override
					public void execute(Boolean value) {
						if (value) {
							DocumentActions.deleteDocument(_card);
						}
						
					}
				});
			}

		});
		
		
	
		
		
		/*
		item = childMenu.addItem("GetOwnCertificateUserCode", "", IMenuItem.ItemType.ToolButton,0);
		item.setCommand(new ICommand<IMenuItem>() {						
			@Override
			public void execute(IMenuItem sender) {											

				Dialogs.ShowMessage( SignControlWrapper.getSignControl().getOwnerUserCode() );	
			}

		});
		*/
	}
}        	



