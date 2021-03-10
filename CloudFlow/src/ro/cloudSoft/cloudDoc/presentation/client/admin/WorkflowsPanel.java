package ro.cloudSoft.cloudDoc.presentation.client.admin;

import java.util.ArrayList;
import java.util.List;

import ro.cloudSoft.cloudDoc.presentation.client.admin.workflows.WorkflowWindow;
import ro.cloudSoft.cloudDoc.presentation.client.shared.constants.NavigationConstants;
import ro.cloudSoft.cloudDoc.presentation.client.shared.event.AppEvent;
import ro.cloudSoft.cloudDoc.presentation.client.shared.event.AppEventController;
import ro.cloudSoft.cloudDoc.presentation.client.shared.event.AppEventHandler;
import ro.cloudSoft.cloudDoc.presentation.client.shared.event.AppEventType;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.bpm.WorkflowModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.providers.GwtLocaleProvider;
import ro.cloudSoft.cloudDoc.presentation.client.shared.providers.GwtServiceProvider;
import ro.cloudSoft.cloudDoc.presentation.client.shared.utils.ComponentUtils;
import ro.cloudSoft.cloudDoc.presentation.client.shared.utils.ComponentUtils.ConfirmCallback;
import ro.cloudSoft.cloudDoc.presentation.client.shared.utils.GwtRegistryUtils;
import ro.cloudSoft.cloudDoc.presentation.client.shared.utils.LoadingManager;
import ro.cloudSoft.cloudDoc.presentation.client.shared.utils.MessageUtils;
import ro.cloudSoft.cloudDoc.presentation.client.shared.utils.web.GwtUrlBuilder;

import com.extjs.gxt.ui.client.Style.HorizontalAlignment;
import com.extjs.gxt.ui.client.Style.SelectionMode;
import com.extjs.gxt.ui.client.event.BaseEvent;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.GridEvent;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.MessageBoxEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.Dialog;
import com.extjs.gxt.ui.client.widget.MessageBox;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.grid.ColumnConfig;
import com.extjs.gxt.ui.client.widget.grid.ColumnModel;
import com.extjs.gxt.ui.client.widget.grid.Grid;
import com.extjs.gxt.ui.client.widget.grid.GridView;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.extjs.gxt.ui.client.widget.toolbar.ToolBar;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class WorkflowsPanel extends ContentPanel implements AppEventHandler {

	private ToolBar toolBar;
	private Button addButton;
	private Button deleteButton;
	private Button viewGraphButton;
	private Button versionButton;
	private Button refreshButton;
	private Grid<WorkflowModel> workflowsGrid;
	
	private WorkflowWindow workflowWindow = new WorkflowWindow();
	
	public WorkflowsPanel() {		
		initToolBar();
		initGridWorkflow();
		initWindow();
		addListeners();
		
		AppEventController.subscribe(this, AppEventType.Workflow);
	}
	
	private void initWindow(){
		setHeading(GwtLocaleProvider.getConstants().ADMIN_WORKFLOW());
		setLayout(new FitLayout());
		
		// adaugare componente
		setTopComponent(toolBar);
		add(workflowsGrid);
		
		addListener(Events.Attach, new Listener<BaseEvent>() {
			public void handleEvent(BaseEvent event) {
				loadWorkflowsGrid();
			};
		});
	}
	
	private void initToolBar(){
		toolBar = new ToolBar();
		toolBar.setSpacing(5);
		toolBar.setStyleName("admin_toolbar");
		toolBar.setSpacing(10);
		toolBar.setBorders(true);

		
		addButton =  new Button(GwtLocaleProvider.getConstants().ADD());
		addButton.setStyleName("admin_toolbar_buttons");
		addButton.setIconStyle("icon-add");
		addButton.setBorders(true);
		
		deleteButton = new Button(GwtLocaleProvider.getConstants().DELETE());
		deleteButton.setStyleName("admin_toolbar_buttons");
		deleteButton.setIconStyle("icon-delete");
		deleteButton.setBorders(true);
		deleteButton.setEnabled(false);
		
		viewGraphButton = new Button(GwtLocaleProvider.getConstants().VIEW_GRAPH());
		viewGraphButton.setStyleName("admin_toolbar_buttons");
		viewGraphButton.setIconStyle("icon-viewGraph");
		viewGraphButton.setBorders(true);
		viewGraphButton.setEnabled(false);
		boolean isWorkflowGraphViewGeneratorEnabled = GwtRegistryUtils.getAppComponentsAvailabilityConstants().isWorkflowGraphViewGeneratorEnabled();
		viewGraphButton.setVisible(isWorkflowGraphViewGeneratorEnabled);
		
		versionButton = new Button(GwtLocaleProvider.getConstants().ADD_VERSION_REVISION());
		versionButton.setStyleName("admin_toolbar_buttons");
		versionButton.setIconStyle("icon-version");
		versionButton.setBorders(true);
		
		refreshButton = new Button();
		refreshButton.setText(GwtLocaleProvider.getConstants().REFRESH());
		refreshButton.setIconStyle("icon-refresh");
		refreshButton.setStyleName("admin_toolbar_buttons");
		refreshButton.setBorders(true);
		
		toolBar.add(addButton);
		toolBar.add(deleteButton);
		toolBar.add(viewGraphButton);
		toolBar.add(versionButton);
		toolBar.add(refreshButton);
	}
	
	private void initGridWorkflow(){
		List<ColumnConfig> cols = new ArrayList<ColumnConfig>();
		
		ColumnConfig colName = new ColumnConfig();
		colName.setId(WorkflowModel.PROPERTY_NAME);
		colName.setHeader(GwtLocaleProvider.getConstants().WORKFLOW());
		colName.setWidth(200);
		
		ColumnConfig colVersion = new ColumnConfig();
		colVersion.setAlignment(HorizontalAlignment.CENTER);
		colVersion.setId(WorkflowModel.PROPERTY_VERSION_NUMBER);
		colVersion.setHeader(GwtLocaleProvider.getConstants().WORKFLOW_VERSION());
		colVersion.setWidth(100);
		
		ColumnConfig colDescription = new ColumnConfig();
		colDescription.setId(WorkflowModel.PROPERTY_DESCRIPTION);
		colDescription.setHeader(GwtLocaleProvider.getConstants().DESCRIPTION());
		colDescription.setWidth(400);
		
		ColumnConfig colDocumentTypes = new ColumnConfig();
		colDocumentTypes.setId(WorkflowModel.PROPERTY_DISPLAY_DOCUMENT_TYPES);
		colDocumentTypes.setHeader(
				GwtLocaleProvider.getConstants().WORKFLOW_ASSOCIATED_DOCUMENT_TYPES());
		colDocumentTypes.setWidth(400);
		
		cols.add(colName);
		cols.add(colVersion);
		cols.add(colDescription);
		cols.add(colDocumentTypes);
		
		ColumnModel model = new ColumnModel(cols);
		ListStore<WorkflowModel> store = new ListStore<WorkflowModel>();
		
		workflowsGrid = new Grid<WorkflowModel>(store, model);
		workflowsGrid.setAutoExpandColumn(WorkflowModel.PROPERTY_DESCRIPTION);
		GridView view = new GridView();
		view.setForceFit(true);
		workflowsGrid.setView(view);
		workflowsGrid.setStripeRows(true);
		workflowsGrid.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
	}
	
	private void addListeners(){
		addButton.addSelectionListener(new SelectionListener<ButtonEvent>(){
			@Override
			public void componentSelected(ButtonEvent ce) {
				workflowWindow.prepareForAdd();
			}
		});
		
		deleteButton.addSelectionListener(new SelectionListener<ButtonEvent>(){
			@Override
			public void componentSelected(ButtonEvent ce) {
				MessageBox.confirm(GwtLocaleProvider.getConstants().DELETE(),
						GwtLocaleProvider.getMessages().CONFIRM_DELETE_WORKFLOW(),
						new Listener<MessageBoxEvent>() {
							public void handleEvent(MessageBoxEvent mbe) {
								if (mbe.getButtonClicked().getItemId().equals(Dialog.YES)) {
									WorkflowModel selectedWorkflow = getSelectedWorkflow();
									LoadingManager.get().loading();
									GwtServiceProvider.getWorkflowService().deleteWorkflow(selectedWorkflow.getId(), new AsyncCallback<Void>() {
										@Override
										public void onFailure(Throwable exception) {
											MessageUtils.displayError(exception);
											LoadingManager.get().loadingComplete();
										}
										@Override
										public void onSuccess(Void nothing) {
											MessageUtils.display(GwtLocaleProvider.getConstants().SUCCESS(), GwtLocaleProvider.getMessages().WORKFLOW_DELETED());
											loadWorkflowsGrid();
											LoadingManager.get().loadingComplete();
										}
									});
								}
							}
						});
			}
		});
		
		viewGraphButton.addSelectionListener(new SelectionListener<ButtonEvent>() {
			
			@Override
			public void componentSelected(ButtonEvent event) {
				
				WorkflowModel workflow = getSelectedWorkflow();
				if (workflow == null) {
					return;
				}
				
				String viewGraphUrl = new GwtUrlBuilder(NavigationConstants.getViewWorkflowGraphLink())
					.setParameter("workflowId", workflow.getId())
					.build();
				Window.open(viewGraphUrl, "_blank", null);
			}
		});
		
		versionButton.addSelectionListener(new SelectionListener<ButtonEvent>(){
			@Override
			public void componentSelected(ButtonEvent ce) {
				
				final WorkflowModel workflow = getSelectedWorkflow();
				if (workflow == null) {
					return;
				}
				
				ComponentUtils.confirm(GwtLocaleProvider.getConstants().ADD_VERSION_REVISION(),
						GwtLocaleProvider.getMessages().CONFIRM_CREATE_WORKFLOW_VERSION(),
						new ConfirmCallback() {
					
					@Override
					public void onYes() {
						LoadingManager.get().loading();
						GwtServiceProvider.getWorkflowService().createNewVersion(workflow.getId(), new AsyncCallback<Long>() {
							
							@Override
							public void onFailure(Throwable exception) {
								MessageUtils.displayError(exception);
								LoadingManager.get().loadingComplete();
							}
							
							@Override
							public void onSuccess(Long idOfNewVersionWorkflow) {
								
								MessageUtils.display(GwtLocaleProvider.getConstants().SUCCESS(),
									GwtLocaleProvider.getMessages().WORKFLOW_VERSION_CREATED());
								AppEventController.fireEvent(AppEventType.Workflow);
								
								workflowWindow.prepareForEdit(idOfNewVersionWorkflow);
								
								LoadingManager.get().loadingComplete();
							}
						});
					}
				});
			}
		});
		refreshButton.addSelectionListener( new SelectionListener<ButtonEvent>() {
			public void componentSelected( ButtonEvent event) {
				refresh();
			};
		});

		workflowsGrid.addListener(Events.OnClick, new Listener<GridEvent<WorkflowModel>>(){
			@Override
			public void handleEvent(GridEvent<WorkflowModel> be) {
				changeToolBarPerspective(be.getModel());
			}
		});
		
		workflowsGrid.addListener(Events.OnDoubleClick, new Listener<GridEvent<WorkflowModel>>(){
			@Override
			public void handleEvent(GridEvent<WorkflowModel> be) {
				workflowWindow.prepareForEdit(getSelectedWorkflow().getId());
			}
		});
		
	}
	
	private void refresh() {
		loadWorkflowsGrid();
	}
	
	private WorkflowModel getSelectedWorkflow(){
		return workflowsGrid.getSelectionModel().getSelectedItem();
	}
	
	private void changeToolBarPerspective(WorkflowModel workflowModel){
		if (workflowModel != null){
			deleteButton.setEnabled(true);
			viewGraphButton.setEnabled(true);
			versionButton.setEnabled(true);
		}else {
			deleteButton.setEnabled(false);
			viewGraphButton.setEnabled(false);
			versionButton.setEnabled(false);
		}
	}
	
	/**
	 * Aduce din baza de date fluxurile si le incarca in grid.
	 */
	public void loadWorkflowsGrid() {
		changeToolBarPerspective(null);
		LoadingManager.get().loading();
		GwtServiceProvider.getWorkflowService().getAllWorkflows(new AsyncCallback<List<WorkflowModel>>(){
			@Override
			public void onFailure(Throwable exception) {
				MessageUtils.displayError(exception);
				LoadingManager.get().loadingComplete();
			}
			@Override
			public void onSuccess(List<WorkflowModel> result) {
				workflowsGrid.getStore().removeAll();
				workflowsGrid.getStore().add(result);
				LoadingManager.get().loadingComplete();
			}
		});
	}
	
	@Override
	public void handleEvent(AppEvent event) {
		if (event.getType().equals(AppEventType.Workflow)) {
			if (AdminPanelDispatcher.isActivePanel(this)) {
				refresh();
			}
		}
	}
}