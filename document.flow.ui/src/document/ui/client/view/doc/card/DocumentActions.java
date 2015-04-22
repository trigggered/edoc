/**
 * 
 */
package document.ui.client.view.doc.card;

import java.util.logging.Logger;

import mdb.core.shared.data.EMdbEntityActionType;
import mdb.core.shared.data.Params;
import mdb.core.ui.client.app.AppController;
import mdb.core.ui.client.communication.impl.SimpleMdbDataRequester;
import mdb.core.ui.client.events.ICallbackEvent;
import mdb.core.ui.client.events.IDataEditHandler;
import mdb.core.ui.client.view.dialogs.message.Dialogs;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Window;
import com.smartgwt.client.data.Record;
import com.smartgwt.client.util.BooleanCallback;
import com.smartgwt.client.util.SC;

import document.ui.client.commons.EDocStatus;
import document.ui.client.resources.locales.Captions;
import document.ui.shared.MdbEntityConst;

/**
 * @author azhuk
 * Creation date: Jul 3, 2014
 *
 */
public class DocumentActions {
	
	private static final Logger _logger = Logger
			.getLogger(DocumentActions.class.getName());	
	
	public static  void addDocumentToFavorits(final long docId) {

		Params params = new Params();
		params.add("ID_DOC", String.valueOf(docId) );
		params.add("CURRENT_USER", String.valueOf(AppController.getInstance().getCurrentUser().getId()));
		SimpleMdbDataRequester.call(EMdbEntityActionType.INSERT, 5127, params, new ICallbackEvent<Record[]>() {
			
			@Override
			public void doWork(Record[] data) {				
				Dialogs.ShowMessage(Captions.DOC_ID +docId + Captions.ADD_TO_FAVORIT);				
			}
		});		
	}
	
	public static  void deleteDocument(final DocumentCard card) {

		Params params = new Params();
		final long docId = card.getDocumentId();
		params.add("ID_DOC", String.valueOf(docId) );
		SimpleMdbDataRequester.callAction(MdbEntityConst.DocCard, 3073, params, new ICallbackEvent<Record[]>() {
			
			@Override
			public void doWork(Record[] data) {
				card.getMainView().closeCurrentTab();
				Dialogs.ShowMessage(Captions.DELETE_SUCSF_RESULT);
			}
		});		
	}
	
	private static IDataEditHandler _openAttachment = new IDataEditHandler() {
		
		@Override
		public void onEdit(final Record record) {			
			
			final String attributeId = record.getAttribute("ID_ATTRIBUTE");
			final String fileName = record.getAttribute("NAME_LOB");
			final String docId = record.getAttribute("ID_DOC");
			
			_logger.info("Try download file:"+fileName + " AttributeId="+attributeId);
			
			SC.ask(Captions.Q_FILE_DOWNLOAD+ fileName, new BooleanCallback() {
				
				@Override
				public void execute(Boolean value) {
					if (!value) return;				
					
					
					if (attributeId != null ) {
						
						
						String url = GWT.getModuleBaseURL() + "download?AttributeId=" +attributeId+
								"&DociD=" + docId;						
						_logger.info("Downlod  URL= "+url);
						Window.open( url, "_blank", "");								
						
					}else {
						_logger.info("File:"+fileName + "not uploaded yet. AttributeId=null" );
						Dialogs.ShowMessage("Get lob by ID_ATTRIBUTE="+record.getAttribute("ID_ATTRIBUTE"));						
					}					
				}
			});	
				 			 	 
		}
	};
	
	
	
	public static IDataEditHandler getDownloadAttachmentEvent() {
		return _openAttachment;
	}

	/**
	 * @param _card
	 * @param approval
	 */
	public static void udateDocStatus(DocumentCard card, EDocStatus status) {

		card.setDocumentStatus(status);
		card.save();
	}
}
