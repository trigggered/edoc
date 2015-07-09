/**
 * 
 */
package document.ui.client.view.doc.card.menu;


import com.smartgwt.client.util.BooleanCallback;

import mdb.core.shared.utils.Clipboard;
import mdb.core.ui.client.command.ICommand;
import mdb.core.ui.client.view.components.menu.IMenu;
import mdb.core.ui.client.view.components.menu.IMenuItem;
import mdb.core.ui.client.view.dialogs.message.Dialogs;
import document.ui.client.commons.EDocStatus;
import document.ui.client.commons.checkers.CheckDocumentUserRight;
import document.ui.client.flow.impl.FlowProccessing;
import document.ui.client.resources.locales.Captions;
import document.ui.client.view.doc.card.DocumentActions;
import document.ui.client.view.doc.card.DocumentCard;

/**
 * @author azhuk
 * Creation date: Mar 23, 2015
 *
 */
public class MenuFile extends mdb.core.ui.client.view.components.menu.Menu  {

	DocumentCard _card;
	
	public DocumentCard getCard() {
	 return _card;	
	}
	
	public MenuFile(DocumentCard card) {		
		super("MenuFile");
		_card = card;
		
		IMenuItem item = addItem(Captions.ACTIONS, "", IMenuItem.ItemType.Menu,0);
		IMenu childMenu = new mdb.core.ui.client.view.components.menu.Menu("Child menu for 'File'");
		item.setChildMenu(childMenu);	
		
		
		item = childMenu.addItem(Captions.PRINT, "", IMenuItem.ItemType.ToolButton,0);
		item.setCommand(new ICommand<IMenuItem>() {						
			@Override
			public void execute(IMenuItem sender) {
				_card.print();
			}
		});
		
		
		item = childMenu.addItem(Captions.SHOW_DOC_LNK, "", IMenuItem.ItemType.ToolButton,0);
		item.setCommand(new ICommand<IMenuItem>() {						
			@Override
			public void execute(IMenuItem sender) {						
				Dialogs.ShowMessage(Captions.LINK_2_DOCUMENT, getCard().getLinkToDocument());
				Clipboard.copy(getCard().getLinkToDocument());
			}
		});						
	
		item = childMenu.addItem(Captions.Q_ADD_TO_FAVORIT, "", IMenuItem.ItemType.ToolButton,0);
		item.setCommand(new ICommand<IMenuItem>() {						
			@Override
			public void execute(IMenuItem sender) {						
				DocumentActions.addDocumentToFavorits(getCard().getDocumentId());				
			}
		});
		
		
		item = childMenu.addItem(Captions.REFRESH, "", IMenuItem.ItemType.ToolButton,0);
		item.setCommand(new ICommand<IMenuItem>() {						
			@Override
			public void execute(IMenuItem sender) {										
				getCard().refresh();
			}
		});		
		
		
		
			item = childMenu.addItem(Captions.DELETE_D0C, "", IMenuItem.ItemType.ToolButton,0);
			item.setCommand(new ICommand<IMenuItem>() {						
				@Override
				public void execute(IMenuItem sender) {
					
					if (CheckDocumentUserRight.isAuthorCurrentUser(getCard()) && getCard().getDocumentStatus() == EDocStatus.Draft)  {
						
						Dialogs.AskDialog(Captions.Q_DELETE, new BooleanCallback() {							
							@Override
							public void execute(Boolean value) {
								if (value) {
									DocumentActions.deleteDocument(getCard());
								}							
							}
						});
					}	
					else {
						Dialogs.ShowWarnMessage(Captions.ONLY_AUTHOR_CAN_DEL_DOC);
						
					}																		
					
				}	
			});
			
			item = childMenu.addItem(Captions.SEND_REMEMBER_TO_APPROVAL, "", IMenuItem.ItemType.ToolButton,0);
			item.setCommand(new ICommand<IMenuItem>() {						
				@Override
				public void execute(IMenuItem sender) {
					
					if (CheckDocumentUserRight.isAuthorCurrentUser(getCard()) && getCard().getDocumentStatus() == EDocStatus.AtTheApproval)  {
						
						Dialogs.AskDialog(Captions.Q_SEND_REMEMBER_BY_DOC_TO_APPROVAL, new BooleanCallback() {							
							@Override
							public void execute(Boolean value) {
								if (value) {

									FlowProccessing.sendRemember(_card.getDocumentId(), 
											_card.getDocumentStatus() );

								}							
							}
						});
					}else {
						Dialogs.ShowMessage(Captions.ONLY_AUTHOR_CAN_SEND_REMEMBER);
					}
										
				}	
			});		
	}
	
}
