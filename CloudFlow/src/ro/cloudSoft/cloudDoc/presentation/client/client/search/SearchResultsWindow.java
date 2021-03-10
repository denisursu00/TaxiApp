package ro.cloudSoft.cloudDoc.presentation.client.client.search;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

import ro.cloudSoft.cloudDoc.presentation.client.client.WindowManager;
import ro.cloudSoft.cloudDoc.presentation.client.client.document.DocumentWindow;
import ro.cloudSoft.cloudDoc.presentation.client.shared.CustomGridCellRenderer;
import ro.cloudSoft.cloudDoc.presentation.client.shared.constants.GwtFormatConstants;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.content.search.AbstractDocumentSearchResultsViewModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.content.search.DocumentAdvancedSearchResultsViewModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.content.search.DocumentAdvancedSearchResultsViewsWrapperModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.content.search.DocumentSimpleSearchResultsViewModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.providers.GwtLocaleProvider;
import ro.cloudSoft.cloudDoc.presentation.client.shared.utils.GwtStringUtils;

import com.extjs.gxt.ui.client.Style.SelectionMode;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.GridEvent;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.widget.Window;
import com.extjs.gxt.ui.client.widget.grid.ColumnConfig;
import com.extjs.gxt.ui.client.widget.grid.ColumnData;
import com.extjs.gxt.ui.client.widget.grid.ColumnModel;
import com.extjs.gxt.ui.client.widget.grid.Grid;
import com.extjs.gxt.ui.client.widget.grid.GridView;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;

public class SearchResultsWindow extends Window {

	private Grid<AbstractDocumentSearchResultsViewModel> resultsGrid;
	private DocumentWindow documentWindow;
	private String documentLocationRealName;
	
	public SearchResultsWindow() {
		setHeading(GwtLocaleProvider.getConstants().SEARCH_RESULTS());
		int width = com.google.gwt.user.client.Window.getClientWidth() - 200;
		int height = com.google.gwt.user.client.Window.getClientHeight() - 100;
		setSize(width, height);
		setLayout(new FitLayout());
		
		documentWindow = WindowManager.getDocumentWindow();
		
		initResultsGrid();
		addListeners();
		
		// urmeaza un scurtcircuit la fereastra :)
		show();
		hide();
	}
	
	private void initResultsGrid() {
		
		ListStore<AbstractDocumentSearchResultsViewModel> store = new ListStore<AbstractDocumentSearchResultsViewModel>();
		
		List<ColumnConfig> cols = new ArrayList<ColumnConfig>();
		
		// Un grid trebuie sa aiba cel putin o coloana; altfel va da exceptie la adaugarea modelelor in store.
		ColumnConfig dummyColumnConfig = new ColumnConfig();
		dummyColumnConfig.setId("dummy");
		cols.add(dummyColumnConfig);
		
		ColumnModel model = new ColumnModel(cols);
		
		resultsGrid = new Grid<AbstractDocumentSearchResultsViewModel>(store, model);
		
		GridView gridView = new GridView();
		gridView.setForceFit(true);
		resultsGrid.setView(gridView);
		
		resultsGrid.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
		add(resultsGrid);
	}
	
	private void addListeners() {
		resultsGrid.addListener(Events.RowDoubleClick, new Listener<GridEvent<AbstractDocumentSearchResultsViewModel>>() {
			
			@Override
			public void handleEvent(GridEvent<AbstractDocumentSearchResultsViewModel> event) {
				toBack();
				documentWindow.prepareForViewOrEdit(event.getModel().getDocumentId(), documentLocationRealName);
			}
		});
	}
	
	private List<ColumnConfig> getCommonColumnConfigs() {
		
		List<ColumnConfig> columnConfigs = new ArrayList<ColumnConfig>();
		
		ColumnConfig documentNameColumnConfig = new ColumnConfig();
		documentNameColumnConfig.setHeader(GwtLocaleProvider.getConstants().DOCUMENT_NAME());
		documentNameColumnConfig.setId(AbstractDocumentSearchResultsViewModel.PROPERTY_DOCUMENT_NAME);
		documentNameColumnConfig.setWidth(100);
		columnConfigs.add(documentNameColumnConfig);
		
		ColumnConfig documentCreatedDateColumnConfig = new ColumnConfig();
		documentCreatedDateColumnConfig.setHeader(GwtLocaleProvider.getConstants().DOC_CREATED());
		documentCreatedDateColumnConfig.setId(AbstractDocumentSearchResultsViewModel.PROPERTY_DOCUMENT_CREATED_DATE);
		documentCreatedDateColumnConfig.setDateTimeFormat(GwtFormatConstants.GWT_DATE_TIME_FORMATTER_FOR_DISPLAY);
		documentCreatedDateColumnConfig.setWidth(100);
		columnConfigs.add(documentCreatedDateColumnConfig);
		
		ColumnConfig documentAuthorDisplayNameColumnConfig = new ColumnConfig();
		documentAuthorDisplayNameColumnConfig.setHeader(GwtLocaleProvider.getConstants().DOC_AUTHOR());
		documentAuthorDisplayNameColumnConfig.setId(AbstractDocumentSearchResultsViewModel.PROPERTY_DOCUMENT_AUTHOR_DISPLAY_NAME);
		documentAuthorDisplayNameColumnConfig.setWidth(100);
		columnConfigs.add(documentAuthorDisplayNameColumnConfig);
		
		ColumnConfig workflowNameColumnConfig = new ColumnConfig();
		workflowNameColumnConfig.setHeader(GwtLocaleProvider.getConstants().WORKFLOW());
		workflowNameColumnConfig.setId(AbstractDocumentSearchResultsViewModel.PROPERTY_WORKFLOW_NAME);
		workflowNameColumnConfig.setWidth(100);
		columnConfigs.add(workflowNameColumnConfig);
		
		ColumnConfig workflowCurrentStateNameColumnConfig = new ColumnConfig();
		workflowCurrentStateNameColumnConfig.setHeader(GwtLocaleProvider.getConstants().WORKFLOW_CURRENT_STATUS());
		workflowCurrentStateNameColumnConfig.setId(AbstractDocumentSearchResultsViewModel.PROPERTY_WORKFLOW_CURRENT_STATE_NAME);
		workflowCurrentStateNameColumnConfig.setWidth(100);
		columnConfigs.add(workflowCurrentStateNameColumnConfig);
		
		ColumnConfig workflowSenderDisplayNameColumnConfig = new ColumnConfig();
		workflowSenderDisplayNameColumnConfig.setHeader(GwtLocaleProvider.getConstants().WORKFLOW_SENDER());
		workflowSenderDisplayNameColumnConfig.setId(AbstractDocumentSearchResultsViewModel.PROPERTY_WORKFLOW_SENDER_DISPLAY_NAME);
		workflowSenderDisplayNameColumnConfig.setWidth(100);
		columnConfigs.add(workflowSenderDisplayNameColumnConfig);
		
		return columnConfigs;
	}
	
	public void displayResults(String documentLocationRealName, List<DocumentSimpleSearchResultsViewModel> searchResultsViews) {
		
		reset();
		
		this.documentLocationRealName = documentLocationRealName;
		
		resultsGrid.getStore().removeAll();
		resultsGrid.getStore().add(searchResultsViews);
		
		List<ColumnConfig> columnConfigs = getCommonColumnConfigs();
		
		ColumnConfig documentTypeNameColumnConfig = new ColumnConfig();
		documentTypeNameColumnConfig.setHeader(GwtLocaleProvider.getConstants().DOCUMENT_TYPE_NAME());
		documentTypeNameColumnConfig.setId(DocumentSimpleSearchResultsViewModel.PROPERTY_DOCUMENT_TYPE_NAME);
		documentTypeNameColumnConfig.setWidth(100);
		columnConfigs.add(documentTypeNameColumnConfig);
		
		ColumnModel columnModel = new ColumnModel(columnConfigs);
		resultsGrid.reconfigure(resultsGrid.getStore(), columnModel);
		
		show();
	}
	
	public void displayResults(String documentLocationRealName, DocumentAdvancedSearchResultsViewsWrapperModel searchResultsViewsWrapper) {
		
		reset();
		
		this.documentLocationRealName = documentLocationRealName;
		
		resultsGrid.getStore().removeAll();
		resultsGrid.getStore().add(searchResultsViewsWrapper.getSearchResultsViews());
		
		List<ColumnConfig> columnConfigs = getCommonColumnConfigs();
		
		for (Entry<Long, String> representativeMetadataDefinitionLabelByIdEntry : searchResultsViewsWrapper.getRepresentativeMetadataDefinitionLabelById().entrySet()) {
			
			final Long representativeMetadataDefinitionId = representativeMetadataDefinitionLabelByIdEntry.getKey();
			String representativeMetadataDefinitionLabel = representativeMetadataDefinitionLabelByIdEntry.getValue();
			
			ColumnConfig representativeMetadataColumnConfig = new ColumnConfig();
			representativeMetadataColumnConfig.setHeader(representativeMetadataDefinitionLabel);
			representativeMetadataColumnConfig.setWidth(100);
			representativeMetadataColumnConfig.setRenderer(new CustomGridCellRenderer<AbstractDocumentSearchResultsViewModel>() {
				
				@Override
				public Object doRender(AbstractDocumentSearchResultsViewModel abstractSearchResultsView, String property,
						ColumnData config, int rowIndex, int colIndex, ListStore<AbstractDocumentSearchResultsViewModel> store,
						Grid<AbstractDocumentSearchResultsViewModel> grid) {
					
					if (!(abstractSearchResultsView instanceof DocumentAdvancedSearchResultsViewModel)) {
						throw new IllegalStateException("Rezultatul NU este de la cautare avansata: [" + abstractSearchResultsView.getClass().getName() + "].");
					}					
					DocumentAdvancedSearchResultsViewModel searchResultsView = (DocumentAdvancedSearchResultsViewModel) abstractSearchResultsView;
					
					String metadataInstanceDisplayValue = searchResultsView.getDocumentMetadataInstanceDisplayValueByDefinitionId().get(representativeMetadataDefinitionId);
					return GwtStringUtils.nullToEmpty(metadataInstanceDisplayValue);
				}
			});
			
			columnConfigs.add(representativeMetadataColumnConfig);
		}
		
		ColumnModel columnModel = new ColumnModel(columnConfigs);
		resultsGrid.reconfigure(resultsGrid.getStore(), columnModel);
		
		show();
	}
	
	private void reset() {
		documentLocationRealName = null;
	}
}