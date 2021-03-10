package ro.cloudSoft.cloudDoc.presentation.client.client.folder;

import java.util.List;

import ro.cloudSoft.cloudDoc.presentation.client.shared.event.AppEventController;
import ro.cloudSoft.cloudDoc.presentation.client.shared.event.AppEventType;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.content.DocumentTypeModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.content.FolderModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.providers.GwtLocaleProvider;
import ro.cloudSoft.cloudDoc.presentation.client.shared.providers.GwtServiceProvider;
import ro.cloudSoft.cloudDoc.presentation.client.shared.utils.ComponentUtils;
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
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.widget.TabItem;
import com.extjs.gxt.ui.client.widget.TabPanel;
import com.extjs.gxt.ui.client.widget.Window;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.form.ComboBox;
import com.extjs.gxt.ui.client.widget.form.ComboBox.TriggerAction;
import com.extjs.gxt.ui.client.widget.form.FormPanel;
import com.extjs.gxt.ui.client.widget.form.HiddenField;
import com.extjs.gxt.ui.client.widget.form.TextArea;
import com.extjs.gxt.ui.client.widget.form.TextField;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.extjs.gxt.ui.client.widget.layout.FormData;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class FolderWindow extends Window {
	
	private static final int ORIGINAL_WIDTH = 1160;
	private static final int ORIGINAL_HEIGHT = 600;
	
	private static final DocumentTypeModel ALL_DOCUMENT_TYPES = new DocumentTypeModel("(" + GwtLocaleProvider.getConstants().ALL() + ")");
	
	private TabPanel tabPanel = new TabPanel();
	
	private GeneralTab generalTab = new GeneralTab();
	private PermissionsTab permissionsTab = new PermissionsTab();
	
	private Button saveButton;
	private Button cancelButton;

	public FolderWindow() {
		setHeading(GwtLocaleProvider.getConstants().FOLDER());
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
					FolderModel folder = getFolder();
					LoadingManager.get().loading();
					GwtServiceProvider.getFolderService().saveFolder(folder, new AsyncCallback<Void>() {
						@Override
						public void onFailure(Throwable exception) {
							MessageUtils.displayError(exception);
							LoadingManager.get().loadingComplete();
						}
						@Override
						public void onSuccess(Void nothing) {
							MessageUtils.display(GwtLocaleProvider.getConstants().SUCCESS(), GwtLocaleProvider.getMessages().FOLDER_SAVED());
							AppEventController.fireEvent(AppEventType.Folder);
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
	
	public void prepareForAdd(String documentLocationRealName, String path, String parentId) {
		show();
		toFront();
		generalTab.prepareForAdd(documentLocationRealName, path, parentId);
		permissionsTab.prepareForAdd();
	}
	
	public void prepareForEdit(final String documentLocationRealName, final String path, String folderId) {
		LoadingManager.get().loading();
		GwtServiceProvider.getFolderService().getFolderById(folderId, documentLocationRealName, new AsyncCallback<FolderModel>() {
			@Override
			public void onFailure(Throwable exception) {
				MessageUtils.displayError(exception);
				LoadingManager.get().loadingComplete();
			}
			@Override
			public void onSuccess(FolderModel folder) {
				
				show();
				toFront();
				generalTab.prepareForEdit(documentLocationRealName, path, folder);
				permissionsTab.prepareForEdit(folder);
				
				LoadingManager.get().loadingComplete();
			}
		});
	}
	
	private boolean isValid() {
		boolean isGeneralTabValid = generalTab.isValid();
		boolean isPermissionsTabValid = permissionsTab.isValid();
		return isGeneralTabValid & isPermissionsTabValid;
	}
	
	private FolderModel getFolder() {
		FolderModel folder = new FolderModel();
		
		generalTab.populate(folder);
		permissionsTab.populate(folder);
		
		return folder;
	}
	
	private void calculateResizePercentage(int w, int h){
		float widthP = (float)w / (float)ORIGINAL_WIDTH;
		float heightP = (float)h / (float)ORIGINAL_HEIGHT;		
		permissionsTab.manageFolderPermissionsComponent.changeSize(widthP, heightP);
	}
	
	private abstract class BaseTab extends TabItem {
		
		public abstract void reset();
		public abstract boolean isValid();
		public abstract void populate(FolderModel folder);
	}
	
	private class GeneralTab extends BaseTab {
		
		FormPanel formPanel;
		
		// proprietatile personale
		HiddenField<String> idHiddenField;
		TextField<String> pathReadOnlyTextField;
		TextField<String> nameTextField;
		TextArea descriptionTextArea;
		// proprietatile parintilor
		HiddenField<String> documentLocationRealNameHiddenField;
		HiddenField<String> parentIdHiddenField;
		// tipul documentelor din folder
		ComboBox<DocumentTypeModel> documentTypeComboBox;
		
		public GeneralTab() {
			setText(GwtLocaleProvider.getConstants().GENERAL());
			
			formPanel = new FormPanel();
			formPanel.setHeaderVisible(false);
			
			idHiddenField = new HiddenField<String>();
			
			pathReadOnlyTextField = new TextField<String>();
			pathReadOnlyTextField.setFieldLabel(GwtLocaleProvider.getConstants().PATH());
			pathReadOnlyTextField.setReadOnly(true);
			
			nameTextField = new TextField<String>();
			nameTextField.setFieldLabel(GwtLocaleProvider.getConstants().NAME());
			
			descriptionTextArea = new TextArea();
			descriptionTextArea.setFieldLabel(GwtLocaleProvider.getConstants().DESCRIPTION());
			
			documentLocationRealNameHiddenField = new HiddenField<String>();
			
			parentIdHiddenField = new HiddenField<String>();
			
			documentTypeComboBox = new ComboBox<DocumentTypeModel>();
			documentTypeComboBox.setFieldLabel(GwtLocaleProvider.getConstants().ALLOWED_DOCUMENT_TYPE());
			documentTypeComboBox.setDisplayField(DocumentTypeModel.PROPERTY_NAME);
			documentTypeComboBox.setEditable(false);
			documentTypeComboBox.setForceSelection(true);
			documentTypeComboBox.setTriggerAction(TriggerAction.ALL);
			documentTypeComboBox.setStore(new ListStore<DocumentTypeModel>());
			
			// validarea formularului
			ComponentUtils.makeRequiredField(this.nameTextField);
			
			FormData formData = new FormData("98%");

			formPanel.add(idHiddenField);
			formPanel.add(pathReadOnlyTextField, formData);
			formPanel.add(nameTextField, formData);
			formPanel.add(descriptionTextArea, formData);
			formPanel.add(documentLocationRealNameHiddenField);
			formPanel.add(parentIdHiddenField);
			formPanel.add(documentTypeComboBox, formData);
			
			add(formPanel);
		}
		
		@Override
		public void reset() {
			formPanel.clear();
			documentTypeComboBox.getStore().removeAll();
			documentTypeComboBox.getStore().add(ALL_DOCUMENT_TYPES);
			documentTypeComboBox.setValue(ALL_DOCUMENT_TYPES);
		}
		
		private void populateDocumentTypeComboBox(final Long documentTypeId) {
			LoadingManager.get().loading();
			GwtServiceProvider.getDocumentTypeService().getAllDocumentTypesForDisplay(new AsyncCallback<List<DocumentTypeModel>>() {
				public void onFailure(Throwable exception) {
					MessageUtils.displayError(exception);
					LoadingManager.get().loadingComplete();
				}
				public void onSuccess(List<DocumentTypeModel> documentTypes) {
					
					documentTypeComboBox.getStore().add(documentTypes);
					if (documentTypeId != null) {
						DocumentTypeModel selectedDocumentType = documentTypeComboBox.getStore().findModel(DocumentTypeModel.PROPERTY_ID, documentTypeId);
						if (selectedDocumentType != null) {
							documentTypeComboBox.setValue(selectedDocumentType);
						}
					}
					
					LoadingManager.get().loadingComplete();
				}
			});
		}

		public void prepareForAdd(String documentLocationRealName, String path, String parentId) {
			reset();
			pathReadOnlyTextField.setValue(path);
			documentLocationRealNameHiddenField.setValue(documentLocationRealName);
			parentIdHiddenField.setValue(parentId);
			populateDocumentTypeComboBox(null);
		}

		public void prepareForEdit(String documentLocationRealName, String path, FolderModel folder) {
			reset();
			idHiddenField.setValue(folder.getId());
			pathReadOnlyTextField.setValue(path);
			nameTextField.setValue(folder.getName());
			descriptionTextArea.setValue(folder.getDescription());
			documentLocationRealNameHiddenField.setValue(documentLocationRealName);
			parentIdHiddenField.setValue(folder.getParentId());
			populateDocumentTypeComboBox(folder.getDocumentTypeId());
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
		public void populate(FolderModel folder) {
			folder.setId(idHiddenField.getValue());
			folder.setName(nameTextField.getValue());
			folder.setDescription(descriptionTextArea.getValue());
			folder.setDocumentLocationRealName(documentLocationRealNameHiddenField.getValue());
			folder.setParentId(parentIdHiddenField.getValue());
			if ((documentTypeComboBox.getValue() == null) || documentTypeComboBox.getValue().equals(ALL_DOCUMENT_TYPES)) {
				folder.setDocumentTypeId(null);
			} else {
				folder.setDocumentTypeId(documentTypeComboBox.getValue().getId());
			}
		}
	}
	
	private class PermissionsTab extends BaseTab {
		
		private ManagePermissionsComponent manageFolderPermissionsComponent = new ManagePermissionsComponent();
		
		public PermissionsTab() {
			
			setText(GwtLocaleProvider.getConstants().SECURITY());
			setLayout(new FitLayout());
			
			add(manageFolderPermissionsComponent);
		}
		
		@Override
		public void reset() {
			manageFolderPermissionsComponent.reset();
		}

		public void prepareForAdd() {
			reset();
			manageFolderPermissionsComponent.populate(null);
			this.manageFolderPermissionsComponent.setReadOnly(false);
		}

		public void prepareForEdit(FolderModel folder) {
			reset();
			manageFolderPermissionsComponent.populate(folder.getPermissions());
			this.manageFolderPermissionsComponent.setReadOnly(!GwtPermissionBusinessUtils.canChangePermissions(folder.getPermissions(), GwtRegistryUtils.getUserSecurity()));
		}
		
		@Override
		public boolean isValid() {
			boolean hasEntities = true;
			boolean areAllPermissionsSet = true;
			if (manageFolderPermissionsComponent.getSelectedEntities().isEmpty()) {
				hasEntities = false;
				ErrorHelper.addError(GwtLocaleProvider.getMessages().NO_SELECTED_ENTITIES());
			}
			if (!manageFolderPermissionsComponent.areAllPermissionsSet()) {
				areAllPermissionsSet = false;
				ErrorHelper.addError(GwtLocaleProvider.getMessages().PERMISSIONS_NOT_SET_FOR_ALL_SELECTED_ENTITIES());
			}
			return hasEntities && areAllPermissionsSet;
		}

		@Override
		public void populate(FolderModel folder) {
			folder.setPermissions(manageFolderPermissionsComponent.getPermissions());
		}
	}
}