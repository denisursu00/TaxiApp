package ro.cloudSoft.cloudDoc.presentation.client.admin;

import java.util.List;

import ro.cloudSoft.cloudDoc.presentation.client.shared.event.AppEventController;
import ro.cloudSoft.cloudDoc.presentation.client.shared.event.AppEventType;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.organization.OrganizationUnitModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.organization.UserModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.providers.GwtLocaleProvider;
import ro.cloudSoft.cloudDoc.presentation.client.shared.providers.GwtServiceProvider;
import ro.cloudSoft.cloudDoc.presentation.client.shared.utils.ComponentUtils;
import ro.cloudSoft.cloudDoc.presentation.client.shared.utils.LoadingManager;
import ro.cloudSoft.cloudDoc.presentation.client.shared.utils.MessageUtils;

import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.widget.Window;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.form.ComboBox;
import com.extjs.gxt.ui.client.widget.form.ComboBox.TriggerAction;
import com.extjs.gxt.ui.client.widget.form.FormPanel;
import com.extjs.gxt.ui.client.widget.form.HiddenField;
import com.extjs.gxt.ui.client.widget.form.TextArea;
import com.extjs.gxt.ui.client.widget.form.TextField;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class OrgUnitDetailsWindow extends Window {
	
	protected OrgUnitFormPanel orgUnitFormPanel;
	
	public OrgUnitDetailsWindow(){		
		initWindow();
		initForm();
		layout();		
	}
	
	private void initWindow(){		
		setMaximizable(true);
		setModal(true);	
		setSize(550, 340);		
		setLayout(new FitLayout()); 		
	}
	
	private void initForm(){
		orgUnitFormPanel = new OrgUnitFormPanel(this);
		add(orgUnitFormPanel);
	}
}
class OrgUnitFormPanel extends FormPanel {

	private OrgUnitDetailsWindow mainWindow;

	private HiddenField<String> idHiddenField;
	private HiddenField<String> parentOrganizationUnitIdHiddenField;
	private HiddenField<String> parentOrganizationIdHiddenField;
	private TextField<String> nameTextField;
	private TextArea descriptionTextArea;
	private ComboBox<UserModel> managerComboBox;

	private Button saveButton;
	private Button cancelButton;

	public OrgUnitFormPanel(OrgUnitDetailsWindow parentWindow) {
		mainWindow = parentWindow;
		initForm();
		addFormActions();
	}

	private void initForm() {
		setHeaderVisible(false);
		setFieldWidth(320);
		initFields();
		initButtons();
		configureValidation();
	}

	private void initFields() {
		idHiddenField = new HiddenField<String>();
		add(idHiddenField);
		parentOrganizationUnitIdHiddenField = new HiddenField<String>();
		add(parentOrganizationUnitIdHiddenField);
		parentOrganizationIdHiddenField = new HiddenField<String>();
		add(parentOrganizationIdHiddenField);
		nameTextField = new TextField<String>();
		nameTextField.setFieldLabel(GwtLocaleProvider.getConstants().NAME());
		add(nameTextField);
		descriptionTextArea = new TextArea();
		descriptionTextArea.setFieldLabel(GwtLocaleProvider.getConstants().DESCRIPTION());
		add(descriptionTextArea);
		managerComboBox = new ComboBox<UserModel>();
		managerComboBox.setFieldLabel(GwtLocaleProvider.getConstants().MANAGER());
		managerComboBox.setStore(new ListStore<UserModel>());
		managerComboBox.setDisplayField(UserModel.USER_PROPERTY_DISPLAY_NAME);
		managerComboBox.setEditable(false);
		managerComboBox.setTriggerAction(TriggerAction.ALL);
		managerComboBox.setForceSelection(true);
		add(managerComboBox);
	}

	private void initButtons() {
		saveButton = new Button();
		getButtonBar().add(saveButton);
		cancelButton = new Button(GwtLocaleProvider.getConstants().CANCEL());
		getButtonBar().add(cancelButton);
	}

	private void configureValidation() {
		ComponentUtils.makeRequiredField(nameTextField);
	}

	private void addFormActions() {
		saveButton.addSelectionListener(new SelectionListener<ButtonEvent>() {
			public void componentSelected(ButtonEvent event) {
				if (isValid()) {
					OrganizationUnitModel orgUnitModelToSave = getOrgUnitFromForm();
					LoadingManager.get().loading();
					GwtServiceProvider.getOrgService().setOrgUnit(orgUnitModelToSave, new AsyncCallback<String>() {
						@Override
						public void onFailure(Throwable exception) {
							MessageUtils.displayError(exception);
							LoadingManager.get().loadingComplete();
						}
						@Override
						public void onSuccess(String orgUnitId) {
							MessageUtils.display(GwtLocaleProvider.getConstants().SUCCESS(), GwtLocaleProvider.getMessages().ORG_UNIT_SAVED());
							AppEventController.fireEvent(AppEventType.OrgUnit);
							mainWindow.hide();
							LoadingManager.get().loadingComplete();
						}
					});
				}
			};
		});
		cancelButton.addSelectionListener(new SelectionListener<ButtonEvent>() {
			public void componentSelected(ButtonEvent event) {
				resetForm();
				mainWindow.hide();
			};
		});
	}

	/**
	 * Reseteaza valorile din formular.
	 */
	private void resetForm() {
		clear();
		managerComboBox.getStore().removeAll();
	}

	private OrganizationUnitModel getOrgUnitFromForm() {
		OrganizationUnitModel orgUnitModel = new OrganizationUnitModel();
		orgUnitModel.setId(idHiddenField.getValue());
		orgUnitModel.setParentOrganizationUnitId(parentOrganizationUnitIdHiddenField.getValue());
		orgUnitModel.setParentOrganizationId(parentOrganizationIdHiddenField.getValue());
		orgUnitModel.setName(nameTextField.getValue());
		orgUnitModel.setDescription(descriptionTextArea.getValue());
		if (managerComboBox.getValue() != null) {
			orgUnitModel.setManagerId(managerComboBox.getValue().getUserId());
		}
		return orgUnitModel;
	}

	public void prepareForAdd(String parentOrganizationUnitId, String parentOrganizationId) {
		// Goleste toate campurile formularului.
		resetForm();
		mainWindow.show();
		saveButton.setText(GwtLocaleProvider.getConstants().ADD());
		cancelButton.setVisible(true);
		// Seteaza id-ul parintelui.
		parentOrganizationUnitIdHiddenField.setValue(parentOrganizationUnitId);
		parentOrganizationIdHiddenField.setValue(parentOrganizationId);
		/*
		 * Din moment ce este vorba de o unit. org. noua, nu poate avea useri in
		 * ea, deci nu avem cum sa alegem un manager.
		 */
		managerComboBox.setVisible(false);
		mainWindow.setHeading(GwtLocaleProvider.getConstants().ORG_UNIT()+" : ");
		setVisible(true);
		mainWindow.layout();		
	}

	public void prepareForEdit(final OrganizationUnitModel selectedOrgUnit) {
		// Goleste toate campurile formularului.
		resetForm();
		mainWindow.show();
		saveButton.setText(GwtLocaleProvider.getConstants().SAVE());
		cancelButton.setVisible(true);
		
		LoadingManager.get().loading();	
		GwtServiceProvider.getOrgService().getOrgUnitById(selectedOrgUnit.getId(), new AsyncCallback<OrganizationUnitModel>() {
			
			@Override
			public void onFailure(Throwable exception) {
				MessageUtils.displayError(exception);
				LoadingManager.get().loadingComplete();
			}
			
			@Override
			public void onSuccess(final OrganizationUnitModel orgUnitModel) {
				
				StringBuffer heading = new StringBuffer(GwtLocaleProvider.getConstants().ORG_UNIT()+" : ");
				if (orgUnitModel.getName() != null)
					heading.append(orgUnitModel.getName());
				mainWindow.setHeading(heading.toString());
				// Populeaza campurile formularului.
				idHiddenField.setValue(orgUnitModel.getId());
				parentOrganizationUnitIdHiddenField.setValue(orgUnitModel.getParentOrganizationUnitId());
				parentOrganizationIdHiddenField.setValue(orgUnitModel.getParentOrganizationId());
				nameTextField.setValue(orgUnitModel.getName());
				descriptionTextArea.setValue(orgUnitModel.getDescription());
				
				LoadingManager.get().loading();
				
				GwtServiceProvider.getOrgService().getUsersFromOrgUnit(orgUnitModel.getId(), new AsyncCallback<List<UserModel>>() {
					@Override
					public void onSuccess(List<UserModel> users) {
						if (!users.isEmpty()) {
							managerComboBox.getStore().add(users);
							// Selecteaza manager-ul unit. org., daca exista.
							if (orgUnitModel.getManagerId() != null) {
								for (UserModel user : users) {
									if (user.getUserId().equals(orgUnitModel.getManagerId())) {
										managerComboBox.setValue(user);
										break;
									}
								}
							}
							managerComboBox.setVisible(true);
						} else {
							// Daca unit. org. nu are useri, nu avem cum sa alegem un manager.
							managerComboBox.setVisible(false);
						}
						LoadingManager.get().loadingComplete();
					}
					
					@Override
					public void onFailure(Throwable exception) {
						MessageUtils.displayError(exception);
						LoadingManager.get().loadingComplete();
					}
				});
				
				setVisible(true);
				mainWindow.layout();

				LoadingManager.get().loadingComplete();
			}
		});
	}
}