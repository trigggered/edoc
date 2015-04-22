package document.ui.client.view.doc.search;

import mdb.core.shared.transport.IRequestData;
import mdb.core.shared.transport.RequestEntity;
import mdb.core.ui.client.events.IDataEditHandler;
import mdb.core.ui.client.view.components.menu.IMenuContainer;
import mdb.core.ui.client.view.data.grid.GridView;

import com.smartgwt.client.data.Record;
import com.smartgwt.client.widgets.Canvas;

import document.ui.client.view.doc.card.DocumentCard;

public class SearchResult extends GridView {

	int _executeActionId = 0;
	String _jsonSearchCriteria;
	
	/**
	 * @return the _executeActionId
	 */
	public int getExecuteActionId() {
		return _executeActionId;
	}


	/**
	 * @return the jsonSearchCriteria
	 */
	public String getJsonSearchCriteria() {
		return _jsonSearchCriteria;
	}


	/**
	 * @param _executeActionId the _executeActionId to set
	 */
	public void setExecuteActionId(int value) {
		this._executeActionId = value;
	}


	/**
	 * @param _jsonSearchCriteria the jsonSearchCriteria to set
	 */
	public void setJsonSearchCriteria(String value) {
		this._jsonSearchCriteria = value;
	}

	
	
	public SearchResult() {
		 addEditEvent(DocumentCard.getShowEditViewHandler());
		 addInsertEvent(new IDataEditHandler() {
			
			@Override
			public void onEdit(Record record) {
				// TODO Auto-generated method stub				
			}
		});
	}
	
	
	@Override
	public String getCaption() {
		return "Результат поиска";
	}

	@Override
	public Canvas getCanvas() {
		return this;
	}

	@Override
	protected IMenuContainer createMenuContainer() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void createMenu() {
		// TODO Auto-generated method stub				
		
	}
	
	@Override
	public void prepareRequestData() {		
		
		IRequestData re = getDataBinder().getDataProvider().getRequest().add(new RequestEntity (getMainEntityId() ));		
		if (_executeActionId!= 0 ) {
			re.setRequestFieldsDescription(false);
			re.setExecuteType(IRequestData.ExecuteType.ApplyFilter);		
			re.setExecActionData(_executeActionId,null, _jsonSearchCriteria);
		}else {
			re.setRequestFieldsDescription(true);
		}
    	
	}

}
