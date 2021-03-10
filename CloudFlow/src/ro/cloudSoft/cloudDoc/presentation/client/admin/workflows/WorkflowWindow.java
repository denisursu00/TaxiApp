package ro.cloudSoft.cloudDoc.presentation.client.admin.workflows;

import java.util.List;

import ro.cloudSoft.cloudDoc.presentation.client.shared.event.AppEventController;
import ro.cloudSoft.cloudDoc.presentation.client.shared.event.AppEventType;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.bpm.WorkflowModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.content.DocumentTypeModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.providers.GwtLocaleProvider;
import ro.cloudSoft.cloudDoc.presentation.client.shared.providers.GwtServiceProvider;
import ro.cloudSoft.cloudDoc.presentation.client.shared.utils.ComponentUtils;
import ro.cloudSoft.cloudDoc.presentation.client.shared.utils.ComponentUtils.ConfirmCallback;
import ro.cloudSoft.cloudDoc.presentation.client.shared.utils.ErrorHelper;
import ro.cloudSoft.cloudDoc.presentation.client.shared.utils.LoadingManager;
import ro.cloudSoft.cloudDoc.presentation.client.shared.utils.MessageUtils;

import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.event.WindowEvent;
import com.extjs.gxt.ui.client.widget.TabPanel;
import com.extjs.gxt.ui.client.widget.Window;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class WorkflowWindow extends Window {

	protected static final int ORIGINAL_WIDTH = 1220;
	protected static final int ORIGINAL_HEIGHT = 585;
	
	private TabPanel tabPanel = new TabPanel();
	
	private GeneralTab generaltab = new GeneralTab(this);
	private SupervisorsTab supervisorsTab = new SupervisorsTab();
	private StatesTab statesTab = new StatesTab(this);
	private TransitionsTab transitionsTab = new TransitionsTab(this);
	
	private Button saveButton;
	private Button cancelButton;
	
	public WorkflowWindow() {
		initTabs();
		initButtons();
		initWindow();
	}
	
	private void initTabs() {
		
		tabPanel.add(generaltab);
		tabPanel.add(supervisorsTab);
		tabPanel.add(statesTab);
		tabPanel.add(transitionsTab);
		
		add(tabPanel);
	}
	
	private void initWindow() {
		
		setMaximizable(true);
		setModal(true);
		setSize(ORIGINAL_WIDTH, ORIGINAL_HEIGHT);
		setMinHeight(400);
		setMinWidth(600);
		setLayout(new FitLayout());
		
		add(tabPanel);
		addButton(saveButton);
		addButton(cancelButton);
		
		addListener(Events.Resize, new Listener<WindowEvent>(){
			public void handleEvent(WindowEvent be) {
				calculateResizePercentage(be.getWidth(), be.getHeight());				
			}			
		});
	}
	
	private void initButtons(){
		saveButton = new Button(GwtLocaleProvider.getConstants().SAVE());
		cancelButton = new Button(GwtLocaleProvider.getConstants().CANCEL());
		addListeners();
	}
	
	private void addListeners(){
		saveButton.addSelectionListener(new SelectionListener<ButtonEvent>() {
			
			@Override
			public void componentSelected(ButtonEvent ce) {
				if (isValid()) {
					
					final WorkflowModel workflowToSave = getWorkflowModelFromAllTabs();
					
					Long workflowId = workflowToSave.getId();
					boolean isEdit = (workflowId != null);
					if (isEdit) {
						LoadingManager.get().loading();
						GwtServiceProvider.getWorkflowService().hasInstances(workflowId, new AsyncCallback<Boolean>() {
							
							@Override
							public void onFailure(Throwable exception) {
								MessageUtils.displayError(exception);
								LoadingManager.get().loadingComplete();
							}
							
							@Override
							public void onSuccess(Boolean hasActiveInstances) {
								if (hasActiveInstances) {
									ComponentUtils.confirm(GwtLocaleProvider.getConstants().SAVE(),
											GwtLocaleProvider.getMessages().CONFIRM_EDIT_WORKFLOW_INSTANCES_EXIST(),
											new ConfirmCallback() {
										
										@Override
										public void onYes() {
											saveWorkflow(workflowToSave);
										}
									});
								} else {
									saveWorkflow(workflowToSave);
								}
								LoadingManager.get().loadingComplete();
							}
						});
					} else {
						saveWorkflow(workflowToSave);
					}
					
					
				} else {
					ErrorHelper.displayErrors();
				}
			}
			
			private void saveWorkflow(WorkflowModel workflowToSave) {
				LoadingManager.get().loading();
				GwtServiceProvider.getWorkflowService().saveWorkflow(workflowToSave, new AsyncCallback<Void>() {
					@Override
					public void onFailure(Throwable exception) {
						MessageUtils.displayError(exception);
						LoadingManager.get().loadingComplete();
					}
					@Override
					public void onSuccess(Void nothing) {
						MessageUtils.display(GwtLocaleProvider.getConstants().SUCCESS(), GwtLocaleProvider.getMessages().WORKFLOW_SAVED());
						AppEventController.fireEvent(AppEventType.Workflow);
						hide();
						LoadingManager.get().loadingComplete();
					}
				});
			}
		});
		
		cancelButton.addSelectionListener(new SelectionListener<ButtonEvent>(){
			@Override
			public void componentSelected(ButtonEvent ce) {
				hide();
			}
		});
	}
	
	private void reset(){
		tabPanel.setSelection(generaltab);
	}
	
	public void prepareForAdd(){
		reset();
		setHeading(GwtLocaleProvider.getConstants().WORKFLOW()+" : ");
		show();
		generaltab.prepareForAdd();
		supervisorsTab.prepareForAdd();
		statesTab.prepareForAdd();
		transitionsTab.prepareForAdd();
	}
	
	public void prepareForEdit(Long workflowId) {
		
		reset();
		
		LoadingManager.get().loading();
		GwtServiceProvider.getWorkflowService().getWorkflowById(workflowId, new AsyncCallback<WorkflowModel>(){
			@Override
			public void onFailure(Throwable exception) {
				MessageUtils.displayError(exception);
				LoadingManager.get().loadingComplete();
			}
			@Override
			public void onSuccess(WorkflowModel workflowModel) {
				
				setHeading(GwtLocaleProvider.getConstants().WORKFLOW()+" : "+ workflowModel.getName());
				show();
				
				generaltab.prepareForEdit(workflowModel);
				supervisorsTab.prepareForEdit(workflowModel);
				statesTab.prepareForEdit(workflowModel);
				transitionsTab.prepareForEdit(workflowModel);

				LoadingManager.get().loadingComplete();
			}
		});
	}
	
	public void doWithSelectedDocumentTypes(List<DocumentTypeModel> documentTypes) {
		generaltab.doWithSelectedDocumentTypes(documentTypes);
		supervisorsTab.doWithSelectedDocumentTypes(documentTypes);
		statesTab.doWithSelectedDocumentTypes(documentTypes);
		transitionsTab.doWithSelectedDocumentTypes(documentTypes);
	}
	
	private boolean isValid() {
		
		boolean isValid = true;
		
		if (!generaltab.isValid()) {
			tabPanel.setSelection(generaltab);
			isValid = false;
		}
		if (!transitionsTab.isValid()) {
			tabPanel.setSelection(transitionsTab);
			isValid = false;
		}
		
		return isValid;
	}
	
	private WorkflowModel getWorkflowModelFromAllTabs(){
		WorkflowModel workflowModel = new WorkflowModel();
		generaltab.populateForSave(workflowModel);
		supervisorsTab.populateForSave(workflowModel);
		statesTab.populateForSave(workflowModel);
		transitionsTab.populateForSave(workflowModel);
		return workflowModel;
	}
	
	public StatesTab getStatesTab(){
		return this.statesTab;
	}
	
	public GeneralTab getGeneralTab(){
		return this.generaltab;
	}
	
	public TransitionsTab getTransitionsTab(){
		return this.transitionsTab;
	}
	
	private void calculateResizePercentage(int w, int h){
		float widthP = (float)w / (float)WorkflowWindow.ORIGINAL_WIDTH;
		float heightP = (float)h / (float)WorkflowWindow.ORIGINAL_HEIGHT;		
		generaltab.changeSize(widthP, heightP);		
	}
	
}