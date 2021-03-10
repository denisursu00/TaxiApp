package ro.cloudSoft.cloudDoc.presentation.client.client.document;

import java.util.ArrayList;
import java.util.List;

import ro.cloudSoft.cloudDoc.presentation.client.shared.model.content.DocumentModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.content.DocumentTypeModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.views.DocumentHistoryViewModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.providers.GwtLocaleProvider;
import ro.cloudSoft.cloudDoc.presentation.client.shared.providers.GwtServiceProvider;
import ro.cloudSoft.cloudDoc.presentation.client.shared.utils.LoadingManager;
import ro.cloudSoft.cloudDoc.presentation.client.shared.utils.MessageUtils;

import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.widget.TabItem;
import com.extjs.gxt.ui.client.widget.form.HiddenField;
import com.extjs.gxt.ui.client.widget.grid.ColumnConfig;
import com.extjs.gxt.ui.client.widget.grid.ColumnModel;
import com.extjs.gxt.ui.client.widget.grid.Grid;
import com.extjs.gxt.ui.client.widget.grid.GridView;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class DocumentHistoryTab extends TabItem {
	
	private Grid<DocumentHistoryViewModel> historyGrid;
	private ColumnModel columnModel;
	
	private static final String INPUT_DOCUMENT_ID = "documentId";
	private HiddenField<String> documentIdHiddenField;

	public DocumentHistoryTab(){
		super();
		setText(GwtLocaleProvider.getConstants().HISTORY());
		initGrid();
	}
	
	private void initGrid(){
		initColumnModel();
		ListStore<DocumentHistoryViewModel> store = new ListStore<DocumentHistoryViewModel>();
		historyGrid = new Grid<DocumentHistoryViewModel>(store, columnModel);
		GridView view = new GridView();
		view.setForceFit(true);
		historyGrid.setView(view);
		
		documentIdHiddenField = new HiddenField<String>();
		documentIdHiddenField.setName(INPUT_DOCUMENT_ID);
		add(documentIdHiddenField);
        add(historyGrid);
		
	}
	private void initColumnModel()
	{
		List<ColumnConfig> columnConfigs = new ArrayList<ColumnConfig>();
		
		ColumnConfig authorColumnConfig = new ColumnConfig();
		authorColumnConfig.setHeader(GwtLocaleProvider.getConstants().HISTORY_AUTHOR());
		authorColumnConfig.setId(DocumentHistoryViewModel.PROPERTY_WORKFLOW_ACTOR);
		authorColumnConfig.setWidth(150);
		
		ColumnConfig departmentColumnConfig = new ColumnConfig();
		departmentColumnConfig.setHeader(GwtLocaleProvider.getConstants().HISTORY_DEPARTMENT());
		departmentColumnConfig.setId(DocumentHistoryViewModel.PROPERTY_ORGANIZATION_DEPARTMENT);
		departmentColumnConfig.setWidth(150);
		
		ColumnConfig actionColumnConfig = new ColumnConfig();
		actionColumnConfig.setHeader(GwtLocaleProvider.getConstants().HISTORY_ACTION());
		actionColumnConfig.setId(DocumentHistoryViewModel.PROPERTY_WORKFLOW_TRANSITION_NAME);
		actionColumnConfig.setWidth(200);

		ColumnConfig dateColumnConfig = new ColumnConfig();
		dateColumnConfig.setHeader(GwtLocaleProvider.getConstants().HISTORY_DATE());
		dateColumnConfig.setId(DocumentHistoryViewModel.PROPERTY_WORKFLOW_TRANSITION_DATE);
		dateColumnConfig.setWidth(150);
		
		columnConfigs.add(authorColumnConfig);
		columnConfigs.add(departmentColumnConfig);
		columnConfigs.add(actionColumnConfig);
		columnConfigs.add(dateColumnConfig);
		
		columnModel = new ColumnModel(columnConfigs);
	}
	
	private void loadHistory()
	{
		String documentId = documentIdHiddenField.getValue();
		
		if (documentId != null) {
			LoadingManager.get().loading();
			GwtServiceProvider.getDocumentHistoryService().getDocumentHistory(documentId,new AsyncCallback<List<DocumentHistoryViewModel>>(){
				@Override
				public void onFailure(Throwable exception) {
					MessageUtils.displayError(exception);
					LoadingManager.get().loadingComplete();
				}
				@Override
				public void onSuccess(List<DocumentHistoryViewModel> history) {
					historyGrid.getStore().removeAll();
				//	add(historyGrid);
					
					historyGrid.getStore().add(history);
					historyGrid.reconfigure(historyGrid.getStore(), columnModel);
					historyGrid.render(historyGrid.getParent().getElement());
					//layout();
					LoadingManager.get().loadingComplete();			
				}
			});
		}
	}
	public void prepareForAdd(DocumentTypeModel documentType, String documentLocationRealName, String parentFolderId) {
		reset();
	}
	public void prepareForViewOrEdit(DocumentTypeModel documentType, DocumentModel document) {
			reset();
			documentIdHiddenField.setValue(document.getId());
	        loadHistory();
			layout();
		}
	protected void reset() {
		//historyGrid.getStore().removeAll();
	}
	public  void setReadOnly(boolean readOnly)
	{
	//TO DO
	}
	
	// Metoda nu trebuie sa faca nimic intrucat tab-ul este doar pt. afisare.
	public void populate(DocumentModel document) {
	}
	
	public  boolean isValid()
	{
		//TO DO
		return true;
	}
//	@Override
//	protected void onRender(Element parent, int index )
//	{
//		super.onRender(parent, index);
//		Window.alert("OnRender");
//		setText(GwtLocaleProvider.getConstants().HISTORY());
//		initGrid();
//	}
//	
}
