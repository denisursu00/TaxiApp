package ro.cloudSoft.cloudDoc.presentation.client.client.documentlocation;

import ro.cloudSoft.cloudDoc.presentation.client.shared.event.AppEventController;
import ro.cloudSoft.cloudDoc.presentation.client.shared.event.AppEventType;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.content.DocumentLocationModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.providers.GwtLocaleProvider;
import ro.cloudSoft.cloudDoc.presentation.client.shared.providers.GwtServiceProvider;
import ro.cloudSoft.cloudDoc.presentation.client.shared.utils.ErrorHelper;
import ro.cloudSoft.cloudDoc.presentation.client.shared.utils.GwtRegistryUtils;
import ro.cloudSoft.cloudDoc.presentation.client.shared.utils.LoadingManager;
import ro.cloudSoft.cloudDoc.presentation.client.shared.utils.MessageUtils;
import ro.cloudSoft.cloudDoc.presentation.client.shared.utils.business.content.GwtPermissionBusinessUtils;
import ro.cloudSoft.cloudDoc.presentation.client.shared.widgets.ManagePermissionsComponent;

import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.event.WindowEvent;
import com.extjs.gxt.ui.client.widget.TabItem;
import com.extjs.gxt.ui.client.widget.TabPanel;
import com.extjs.gxt.ui.client.widget.Window;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.form.FormPanel;
import com.extjs.gxt.ui.client.widget.form.HiddenField;
import com.extjs.gxt.ui.client.widget.form.TextArea;
import com.extjs.gxt.ui.client.widget.form.TextField;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.extjs.gxt.ui.client.widget.layout.FormData;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class DocumentLocationWindow extends Window {
	
	private static final int ORIGINAL_WIDTH = 1160;
	private static final int ORIGINAL_HEIGHT = 600;
	
	private TabPanel tabPanel = new TabPanel();
	
	private GeneralTab generalTab = new GeneralTab();
	private PermissionsTab permissionsTab = new PermissionsTab();
	
	private Button saveButton;
	private Button cancelButton;

	public DocumentLocationWindow() {
		setHeading(GwtLocaleProvider.getConstants().WORKSPACE());
		setLayout(new FitLayout());
		setModal(true);
		setSize(ORIGINAL_WIDTH, ORIGINAL_HEIGHT);
		setMinWidth(600);
		setMinHeight(400);
		
		initTabs();
		initButtons();
		addListener(Events.Resize, new Listener<WindowEvent>(){
			public void handleEvent(WindowEvent be) {
				calculateResizePercentage(be.getWidth(), be.getHeight());				
			}			
		});
	}
	
	private void initTabs() {
		
		tabPanel.add(generalTab);
		tabPanel.add(permissionsTab);
		
		add(tabPanel);
	}
	
	private void initButtons() {
		saveButton = new Button();
		saveButton.setText(GwtLocaleProvider.getConstants().SAVE());
		saveButton.addSelectionListener(new SelectionListener<ButtonEvent>() {
			
			@Override
			public void componentSelected(ButtonEvent be) {
				if (isValid()) {
					DocumentLocationModel documentLocation = getDocumentLocation();
					
					LoadingManager.get().loading();
					GwtServiceProvider.getDocumentLocationService().saveDocumentLocation(documentLocation, new AsyncCallback<String>() {
						@Override
						public void onFailure(Throwable exception) {
							MessageUtils.displayError(exception);
							LoadingManager.get().loadingComplete();
						}
						@Override
						public void onSuccess(String realName) {
							MessageUtils.display(GwtLocaleProvider.getConstants().SUCCESS(), GwtLocaleProvider.getMessages().DOCUMENT_LOCATION_SAVED());
							AppEventController.fireEvent(AppEventType.DocumentLocation);
							hide();
							LoadingManager.get().loadingComplete();
						}
					});
				} else {
					ErrorHelper.displayErrors();
				}
			}
		});
		
		cancelButton = new Button();
		cancelButton.setText(GwtLocaleProvider.getConstants().CANCEL());
		cancelButton.addSelectionListener(new SelectionListener<ButtonEvent>() {
			
			@Override
			public void componentSelected(ButtonEvent be) {
				hide();
			}
		});
		
		getButtonBar().add(saveButton);
		getButtonBar().add(cancelButton);
	}
	
	public void prepareForAdd() {
		show();
		toFront();
		generalTab.prepareForAdd();
		permissionsTab.prepareForAdd();
	}
	
	public void prepareForEdit(String documentLocationRealName) {
		LoadingManager.get().loading();
		GwtServiceProvider.getDocumentLocationService().getDocumentLocationByRealName(documentLocationRealName, new AsyncCallback<DocumentLocationModel>() {
			@Override
			public void onFailure(Throwable exception) {
				MessageUtils.displayError(exception);
				LoadingManager.get().loadingComplete();
			}
			@Override
			public void onSuccess(DocumentLocationModel documentLocation) {
				
				show();
				toFront();
				generalTab.prepareForEdit(documentLocation);
				permissionsTab.prepareForEdit(documentLocation);

				LoadingManager.get().loadingComplete();
			}
		});
	}
	
	private boolean isValid() {
		boolean isGeneralTabValid = generalTab.isValid();
		boolean isPermissionsTabValid = permissionsTab.isValid();
		return isGeneralTabValid & isPermissionsTabValid;
	}
	
	private DocumentLocationModel getDocumentLocation() {
		DocumentLocationModel documentLocation = new DocumentLocationModel();
		
		generalTab.populate(documentLocation);
		permissionsTab.populate(documentLocation);
		
		return documentLocation;
	}
	
	private void calculateResizePercentage(int w, int h){
		float widthP = (float)w / (float)ORIGINAL_WIDTH;
		float heightP = (float)h / (float)ORIGINAL_HEIGHT;		
		permissionsTab.manageDocumentLocationPermissionsComponent.changeSize(widthP, heightP);
	}
	
	private abstract class BaseTab extends TabItem {
		
		public abstract void reset();
		public abstract void prepareForAdd();
		public abstract void prepareForEdit(DocumentLocationModel documentLocation);
		public abstract boolean isValid();
		public abstract void populate(DocumentLocationModel documentLocation);
	}
	
	private class GeneralTab extends BaseTab {
		
		FormPanel formPanel;
		
		HiddenField<String> realNameHiddenField;
		TextField<String> nameTextField;
		TextArea descriptionTextArea;
		
		public GeneralTab() {
			setLayout(new FitLayout());
			setText(GwtLocaleProvider.getConstants().GENERAL());
			
			formPanel = new FormPanel();
			formPanel.setBodyBorder(false);
			formPanel.setBorders(false);
			formPanel.setHeaderVisible(false);
			
			realNameHiddenField = new HiddenField<String>();
			
			nameTextField = new TextField<String>();
			nameTextField.setFieldLabel(GwtLocaleProvider.getConstants().NAME());
			
			descriptionTextArea = new TextArea();
			descriptionTextArea.setFieldLabel(GwtLocaleProvider.getConstants().DESCRIPTION());
			
			// validarea formularului
			nameTextField.setAllowBlank(false);
			
			FormData formData = new FormData("95%");
			
			formPanel.add(realNameHiddenField);
			formPanel.add(nameTextField, formData);
			formPanel.add(descriptionTextArea, formData);
			
			add(formPanel);
		}
		
		@Override
		public void reset() {
			formPanel.clear();
		}

		@Override
		public void prepareForAdd() {
			reset();
		}

		@Override
		public void prepareForEdit(DocumentLocationModel documentLocation) {
			reset();
			realNameHiddenField.setValue(documentLocation.getRealName());
			nameTextField.setValue(documentLocation.getName());
			descriptionTextArea.setValue(documentLocation.getDescription());
		}
		
		@Override
		public boolean isValid() {
			boolean isFormValid = formPanel.isValid();
			if (!isFormValid) {
				ErrorHelper.addError(GwtLocaleProvider.getMessages().REQUIRED_FIELDS_NOT_COMPLETED());
			}
			return isFormValid;
		}

		@Override
		public void populate(DocumentLocationModel documentLocation) {
			documentLocation.setRealName(realNameHiddenField.getValue());
			documentLocation.setName(nameTextField.getValue());
			documentLocation.setDescription(descriptionTextArea.getValue());
		}
	}
	
	private class PermissionsTab extends BaseTab {
		
		private ManagePermissionsComponent manageDocumentLocationPermissionsComponent = new ManagePermissionsComponent();
		
		public PermissionsTab() {
			
			setText(GwtLocaleProvider.getConstants().SECURITY());
			setLayout(new FitLayout());
			
			add(manageDocumentLocationPermissionsComponent);
		}
		
		@Override
		public void reset() {
			manageDocumentLocationPermissionsComponent.reset();
		}

		@Override
		public void prepareForAdd() {
			reset();
			manageDocumentLocationPermissionsComponent.populate(null);
			this.manageDocumentLocationPermissionsComponent.setReadOnly(false);
		}

		@Override
		public void prepareForEdit(DocumentLocationModel documentLocation) {
			reset();
			manageDocumentLocationPermissionsComponent.populate(documentLocation.getPermissions());
			this.manageDocumentLocationPermissionsComponent.setReadOnly(!GwtPermissionBusinessUtils.canChangePermissions(documentLocation.getPermissions(), GwtRegistryUtils.getUserSecurity()));
		}
		
		@Override
		public boolean isValid() {
			boolean hasEntities = true;
			boolean areAllPermissionsSet = true;
			if (manageDocumentLocationPermissionsComponent.getSelectedEntities().isEmpty()) {
				hasEntities = false;
				ErrorHelper.addError(GwtLocaleProvider.getMessages().NO_SELECTED_ENTITIES());
			}
			if (!manageDocumentLocationPermissionsComponent.areAllPermissionsSet()) {
				areAllPermissionsSet = false;
				ErrorHelper.addError(GwtLocaleProvider.getMessages().PERMISSIONS_NOT_SET_FOR_ALL_SELECTED_ENTITIES());
			}
			return hasEntities && areAllPermissionsSet;
		}

		@Override
		public void populate(DocumentLocationModel documentLocation) {
			documentLocation.setPermissions(manageDocumentLocationPermissionsComponent.getPermissions());
		}
	}
}