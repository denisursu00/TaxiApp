package ro.cloudSoft.cloudDoc.presentation.client.admin.audit;

import java.util.ArrayList;
import java.util.List;

import ro.cloudSoft.cloudDoc.presentation.client.shared.constants.GwtFormatConstants;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.audit.AuditEntryModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.audit.GwtAuditSearchCriteria;
import ro.cloudSoft.cloudDoc.presentation.client.shared.providers.GwtLocaleProvider;
import ro.cloudSoft.cloudDoc.presentation.client.shared.providers.GwtServiceProvider;
import ro.cloudSoft.cloudDoc.presentation.client.shared.renderers.EnumWithLocalizedLabelGridCellRenderer;
import ro.cloudSoft.cloudDoc.presentation.client.shared.utils.LoadingManager;
import ro.cloudSoft.cloudDoc.presentation.client.shared.utils.MessageUtils;

import com.extjs.gxt.ui.client.data.BasePagingLoader;
import com.extjs.gxt.ui.client.data.ModelData;
import com.extjs.gxt.ui.client.data.PagingLoadConfig;
import com.extjs.gxt.ui.client.data.PagingLoadResult;
import com.extjs.gxt.ui.client.data.PagingLoader;
import com.extjs.gxt.ui.client.data.RpcProxy;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.widget.grid.ColumnConfig;
import com.extjs.gxt.ui.client.widget.grid.ColumnModel;
import com.extjs.gxt.ui.client.widget.grid.Grid;
import com.extjs.gxt.ui.client.widget.grid.GridSelectionModel;
import com.extjs.gxt.ui.client.widget.grid.GridView;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class AuditSearchResultsGrid extends Grid<AuditEntryModel> {
	
	private final AuditSearchCriteriaProvider searchCriteriaProvider;
	
	private BasePagingLoader<PagingLoadResult<AuditEntryModel>> auditEntriesLoader;

	public AuditSearchResultsGrid(AuditSearchCriteriaProvider searchCriteriaProvider) {
		
		this.searchCriteriaProvider = searchCriteriaProvider;
		
		initStore();
		initColumnModel();
		initOthersFromGridConstructor();
	}
	
	private void initStore() {
		
		RpcProxy<PagingLoadResult<AuditEntryModel>> auditEntriesProxy = new RpcProxy<PagingLoadResult<AuditEntryModel>>() {

			@Override
			protected void load(Object loadConfig, final AsyncCallback<PagingLoadResult<AuditEntryModel>> callback) {
				
				if (!(loadConfig instanceof PagingLoadConfig)) {
					throw new IllegalArgumentException("Proxy-ul functioneaza doar pentru paginare.");
				}
				PagingLoadConfig pagingLoadConfig = (PagingLoadConfig) loadConfig;
				
				GwtAuditSearchCriteria searchCriteria = searchCriteriaProvider.getSearchCriteria();
				
				LoadingManager.get().loading();
				GwtServiceProvider.getAuditService().searchAuditEntries(pagingLoadConfig, searchCriteria, new AsyncCallback<PagingLoadResult<AuditEntryModel>>() {
					
					@Override
					public void onSuccess(PagingLoadResult<AuditEntryModel> auditEntriesPagingLoadResult) {
						callback.onSuccess(auditEntriesPagingLoadResult);
						LoadingManager.get().loadingComplete();
					}
					
					@Override
					public void onFailure(Throwable exception) {
						MessageUtils.displayError(exception);
						callback.onFailure(exception);
						LoadingManager.get().loadingComplete();
					}
				});
			}
		};
		
		auditEntriesLoader = new BasePagingLoader<PagingLoadResult<AuditEntryModel>>(auditEntriesProxy);
		auditEntriesLoader.setRemoteSort(false);
		auditEntriesLoader.setReuseLoadConfig(false);
		
		ListStore<AuditEntryModel> auditEntriesStore = new ListStore<AuditEntryModel>(auditEntriesLoader);
		store = auditEntriesStore;
	}
	
	private void initColumnModel() {
		
		ColumnConfig dateTimeColumnConfig = new ColumnConfig();
		dateTimeColumnConfig.setHeader(GwtLocaleProvider.getConstants().DATE_TIME());
		dateTimeColumnConfig.setWidth(180);
		dateTimeColumnConfig.setId(AuditEntryModel.PROPERTY_DATE_TIME);
		dateTimeColumnConfig.setDateTimeFormat(GwtFormatConstants.GWT_DATE_TIME_FORMATTER_FOR_LOG);
		
		ColumnConfig userDisplayNameColumnConfig = new ColumnConfig();
		userDisplayNameColumnConfig.setHeader(GwtLocaleProvider.getConstants().USER());
		userDisplayNameColumnConfig.setWidth(220);
		userDisplayNameColumnConfig.setId(AuditEntryModel.PROPERTY_USER_DISPLAY_NAME);
		
		ColumnConfig entityTypeColumnConfig = new ColumnConfig();
		entityTypeColumnConfig.setHeader(GwtLocaleProvider.getConstants().ENTITY_TYPE());
		entityTypeColumnConfig.setWidth(120);
		entityTypeColumnConfig.setRenderer(new EnumWithLocalizedLabelGridCellRenderer<ModelData>(AuditEntryModel.PROPERTY_ENTITY_TYPE));
		
		ColumnConfig entityIdentifierColumnConfig = new ColumnConfig();
		entityIdentifierColumnConfig.setHeader(GwtLocaleProvider.getConstants().ENTITY_IDENTIFIER());
		entityIdentifierColumnConfig.setWidth(200);
		entityIdentifierColumnConfig.setId(AuditEntryModel.PROPERTY_ENTITY_IDENTIFIER);
		
		ColumnConfig entityDisplayNameColumnConfig = new ColumnConfig();
		entityDisplayNameColumnConfig.setHeader(GwtLocaleProvider.getConstants().ENTITY_DISPLAY_NAME());
		entityDisplayNameColumnConfig.setWidth(250);
		entityDisplayNameColumnConfig.setId(AuditEntryModel.PROPERTY_ENTITY_DISPLAY_NAME);
		
		ColumnConfig operationColumnConfig = new ColumnConfig();
		operationColumnConfig.setHeader(GwtLocaleProvider.getConstants().OPERATION());
		operationColumnConfig.setWidth(120);
		operationColumnConfig.setRenderer(new EnumWithLocalizedLabelGridCellRenderer<ModelData>(AuditEntryModel.PROPERTY_OPERATION));
		
		List<ColumnConfig> columnConfigs = new ArrayList<ColumnConfig>();
		columnConfigs.add(dateTimeColumnConfig);
		columnConfigs.add(userDisplayNameColumnConfig);
		columnConfigs.add(entityTypeColumnConfig);
		columnConfigs.add(entityIdentifierColumnConfig);
		columnConfigs.add(entityDisplayNameColumnConfig);
		columnConfigs.add(operationColumnConfig);
		
		ColumnModel columnModel = new ColumnModel(columnConfigs);
		cm = columnModel;
	}
	
	private void initOthersFromGridConstructor() {
		view = new GridView();
	    focusable = true;
	    baseStyle = "x-grid-panel";
	    setSelectionModel(new GridSelectionModel<AuditEntryModel>());
	}
	
	public PagingLoader<PagingLoadResult<AuditEntryModel>> getPagingLoader() {
		return auditEntriesLoader;
	}
	
	public static interface AuditSearchCriteriaProvider {
		
		GwtAuditSearchCriteria getSearchCriteria();
	}
}