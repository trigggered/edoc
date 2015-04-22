package document.ui.client.view.doc.search;

import document.ui.shared.MdbEntityConst;
import mdb.core.shared.data.Params;
import mdb.core.shared.transport.IRequestData;
import mdb.core.shared.transport.RequestEntity;
import mdb.core.ui.client.data.bind.DataBindException;
import mdb.core.ui.client.data.impl.MdbDataSource;
import mdb.core.ui.client.data.impl.fields.DataSourceFields;
import mdb.core.ui.client.data.impl.fields.DataSourceFieldsBuilder;
import mdb.core.ui.client.view.data.DynamicFieldsView;

public class InputSearchCriteriaView extends DynamicFieldsView{	
	
	
	private int _searchActionId;	

	
	public InputSearchCriteriaView(int searchActionId) {		
		_searchActionId = searchActionId;	
		setMainEntityId(MdbEntityConst.ENTITY_FLDS_DESCR);
	}	
	
	
	public int getActionId() {
		return _searchActionId;
	}
	


	protected Params createParamsReq() {
		Params params = new Params();
		params.add("ID_DENTITY", String.valueOf(MdbEntityConst.DocSearch));
		params.add("ID_DENTITY_ACTION", "8" );
		params.add("ID_EXT_METHOD",String.valueOf( getActionId()) );
		return params;
	}
	
	/* (non-Javadoc)
	 * @see mdb.core.ui.client.view.data.DataView#prepareRequestData()
	 */
	@Override
	public void prepareRequestData() {		
		IRequestData re =   getDataBinder().getDataProvider().getRequest().add(new RequestEntity(getMainEntityId(), DataSourceFieldsBuilder.FLD_ENTITY_DESC_KEY+MdbEntityConst.ENTITY_FLDS_DESCR, false ));
		re.setParams(createParamsReq());
	}
	

	/* (non-Javadoc)
	 * @see mdb.core.ui.client.view.data.DataView#bindDataComponents()
	 */
	@Override
	public void bindDataComponents() throws DataBindException {
		
		IRequestData rqd =	getDataBinder().getDataProvider().getResponse().get(DataSourceFieldsBuilder.FLD_ENTITY_DESC_KEY+MdbEntityConst.ENTITY_FLDS_DESCR );				
		
		if (rqd != null) {	
			
			DataSourceFields  fields = DataSourceFieldsBuilder.createFields(getDataBinder().getDataProvider(), rqd);
			
			final MdbDataSource ds = new MdbDataSource();
			ds.setKeyFilds(rqd.getKeys());
			ds.setDataSourceFields(fields);
			ds.createEmptyRecord();
			getDynamicFields().setDataSource(ds);										
			
		}			
	}
	
	public void clearCriteria() {
		getDynamicFields().getDataForm().reset();
	}
	
	
		

}
