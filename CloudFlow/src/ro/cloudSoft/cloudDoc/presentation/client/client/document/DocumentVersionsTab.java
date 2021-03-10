package ro.cloudSoft.cloudDoc.presentation.client.client.document;

import java.util.ArrayList;
import java.util.List;

import ro.cloudSoft.cloudDoc.presentation.client.shared.model.bpm.WorkflowModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.bpm.WorkflowStateModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.content.DocumentModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.content.DocumentTypeModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.views.DocumentVersionInfoViewModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.providers.GwtLocaleProvider;
import ro.cloudSoft.cloudDoc.presentation.client.shared.providers.GwtServiceProvider;
import ro.cloudSoft.cloudDoc.presentation.client.shared.utils.LoadingManager;
import ro.cloudSoft.cloudDoc.presentation.client.shared.utils.MessageUtils;

import com.extjs.gxt.ui.client.Style.Scroll;
import com.extjs.gxt.ui.client.Style.SortDir;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.GridEvent;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.widget.form.HiddenField;
import com.extjs.gxt.ui.client.widget.grid.ColumnConfig;
import com.extjs.gxt.ui.client.widget.grid.ColumnModel;
import com.extjs.gxt.ui.client.widget.grid.Grid;
import com.extjs.gxt.ui.client.widget.grid.GridView;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class DocumentVersionsTab extends DocumentBaseTab {
	
	private Grid<DocumentVersionInfoViewModel> versionInfoGrid;
	private ColumnModel columnModel;
	private DocumentWindow documentWindow;
	
	private static final String INPUT_DOCUMENT_ID = "documentId";
	private static final String INPUT_DOCUMENT_LOCATION_REAL_NAME = "documentLocationRealName";
	
	private HiddenField<String> documentIdHiddenField;
	private HiddenField<String> documentLocationRealNameHiddenField;
	
	
	public DocumentVersionsTab() 
	{
		setText(GwtLocaleProvider.getConstants().VERSIONS());
		setScrollMode(Scroll.AUTO);
		
		initGrid();
	}
	private void initGrid()
	{
		initColumnModel();
		ListStore<DocumentVersionInfoViewModel> store = new ListStore<DocumentVersionInfoViewModel>();
		versionInfoGrid = new Grid<DocumentVersionInfoViewModel>(store, columnModel);
		GridView view = new GridView();
		view.setForceFit(true);
		versionInfoGrid.setView(view);
		versionInfoGrid.getStore().sort(DocumentVersionInfoViewModel.PROPERTY_VERSION_NUMBER, SortDir.ASC);
		
		documentIdHiddenField = new HiddenField<String>();
		documentIdHiddenField.setName(INPUT_DOCUMENT_ID);
		documentLocationRealNameHiddenField = new HiddenField<String>();
		documentLocationRealNameHiddenField.setName(INPUT_DOCUMENT_LOCATION_REAL_NAME);
		
		add(documentIdHiddenField);
		add(documentLocationRealNameHiddenField);
        add(versionInfoGrid);
		
  		versionInfoGrid.addListener(Events.OnDoubleClick,
				new Listener<GridEvent<DocumentVersionInfoViewModel>>() {
			@Override
			public void handleEvent(GridEvent<DocumentVersionInfoViewModel> event) {
				DocumentVersionInfoViewModel selectedVersion = event.getModel();
				if (selectedVersion != null) {
					documentWindow=new DocumentWindow();
					documentWindow.setHeading(GwtLocaleProvider.getConstants().VERSION_NR()+" "+selectedVersion.getVersionNumber());
					documentWindow.prepareForViewVersion(selectedVersion.getVersionNumber(),documentIdHiddenField.getValue(), documentLocationRealNameHiddenField.getValue());
				}
			}
		});
	}

	private void initColumnModel()
	{
		List<ColumnConfig> columnConfigs = new ArrayList<ColumnConfig>();
		
		ColumnConfig versionColumnConfig = new ColumnConfig();
		versionColumnConfig.setHeader(GwtLocaleProvider.getConstants().VERSION_NR());
		versionColumnConfig.setId(DocumentVersionInfoViewModel.PROPERTY_VERSION_NUMBER);
		versionColumnConfig.setWidth(150);
		
		ColumnConfig authorColumnConfig = new ColumnConfig();
		authorColumnConfig.setHeader(GwtLocaleProvider.getConstants().VERSION_AUTHOR());
		authorColumnConfig.setId(DocumentVersionInfoViewModel.PROPERTY_VERSION_AUTHOR);
		authorColumnConfig.setWidth(150);
		
		ColumnConfig dateColumnConfig = new ColumnConfig();
		dateColumnConfig.setHeader(GwtLocaleProvider.getConstants().VERSION_DATE());
		dateColumnConfig.setId(DocumentVersionInfoViewModel.PROPERTY_VERSION_CREATION_DATE);
		dateColumnConfig.setWidth(150);
		
		columnConfigs.add(versionColumnConfig);
		columnConfigs.add(authorColumnConfig);
		columnConfigs.add(dateColumnConfig);
		
		columnModel = new ColumnModel(columnConfigs);
	}

	private void loadVersions()
	{
		String documentId = documentIdHiddenField.getValue();
		String documentLocationRealName = documentLocationRealNameHiddenField.getValue();
		if ((documentId != null) && (documentLocationRealName != null)) {	
			LoadingManager.get().loading();
			GwtServiceProvider.getDocumentService().getDocumentVersions(documentId,documentLocationRealName,new AsyncCallback<List<DocumentVersionInfoViewModel>>(){
				@Override
				public void onFailure(Throwable exception) {
					MessageUtils.displayError(exception);
					LoadingManager.get().loadingComplete();
				}
				@Override
				public void onSuccess(List<DocumentVersionInfoViewModel> versions) {					
					versionInfoGrid.getStore().removeAll();
					versionInfoGrid.getStore().add(versions);
					LoadingManager.get().loadingComplete();			
				}
			});
		}
		//versionInfoGrid.show();
	}
	public void prepareForAdd(DocumentTypeModel documentType, String documentLocationRealName, String parentFolderId, WorkflowModel workflow, WorkflowStateModel currentState) {
		reset();
		documentLocationRealNameHiddenField.setValue(documentLocationRealName);
	}

	public void prepareForViewOrEdit(DocumentTypeModel documentType, DocumentModel document, WorkflowModel workflow, WorkflowStateModel currentState) {
	//	reset();
		documentIdHiddenField.setValue(document.getId());
		documentLocationRealNameHiddenField.setValue(document.getDocumentLocationRealName());
        loadVersions();
		layout();
	}
	protected void reset() {
		versionInfoGrid.getStore().removeAll();
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
}
