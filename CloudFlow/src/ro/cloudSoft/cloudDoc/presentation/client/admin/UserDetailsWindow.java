package ro.cloudSoft.cloudDoc.presentation.client.admin;

import java.util.List;

import ro.cloudSoft.cloudDoc.presentation.client.shared.event.AppEventController;
import ro.cloudSoft.cloudDoc.presentation.client.shared.event.AppEventType;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.directory.organization.DirectoryUserModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.organization.UserModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.providers.GwtLocaleProvider;
import ro.cloudSoft.cloudDoc.presentation.client.shared.providers.GwtServiceProvider;
import ro.cloudSoft.cloudDoc.presentation.client.shared.providers.ValidatorProvider;
import ro.cloudSoft.cloudDoc.presentation.client.shared.utils.ComponentUtils;
import ro.cloudSoft.cloudDoc.presentation.client.shared.utils.GwtStringUtils;
import ro.cloudSoft.cloudDoc.presentation.client.shared.utils.LoadingManager;
import ro.cloudSoft.cloudDoc.presentation.client.shared.utils.MessageUtils;

import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.widget.MessageBox;
import com.extjs.gxt.ui.client.widget.Window;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.form.CheckBox;
import com.extjs.gxt.ui.client.widget.form.FormPanel;
import com.extjs.gxt.ui.client.widget.form.HiddenField;
import com.extjs.gxt.ui.client.widget.form.TextField;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class UserDetailsWindow extends Window {
	
	protected UserFormPanel userFormPanel;
	
	public UserDetailsWindow(){		
		initWindow();
		initForms();
		layout();
	}
	
	private void initWindow(){	
		setMaximizable(true);
		setModal(true);
		setSize(550, 340);		
		setLayout(new FitLayout()); 
	}
	
	private void initForms(){		
		userFormPanel = new UserFormPanel(this);
		add(userFormPanel);	
	}
	
	public void prepareForImport(DirectoryUserModel directoryUser, String organizationIdAsString, String organizationUnitIdAsString) {
		userFormPanel.prepareForImport(directoryUser, organizationIdAsString, organizationUnitIdAsString);
	}
}	

class UserFormPanel extends FormPanel {

	private UserDetailsWindow mainWindow;

	private HiddenField<String> idHiddenField;
	private HiddenField<String> organizationUnitIdHiddenField;
	private HiddenField<String> organizationIdHiddenField;
	private HiddenField<String> customTitleTemplateHiddenField;
	private TextField<String> firstNameTextField;
	private TextField<String> lastNameTextField;
	private TextField<String> passwordTextField;
	private TextField<String> titleTextField;
	private TextField<String> employeeIdTextField;
	private TextField<String> usernameTextField;
	private TextField<String> emailTextField;
	private CheckBox isManagerCheckBox;

	private Button displayUserGroupsButton;
	private Button saveUserButton;
	private Button cancelUserButton;

	public UserFormPanel(UserDetailsWindow parentWindow) {
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
		organizationUnitIdHiddenField = new HiddenField<String>();
		add(organizationUnitIdHiddenField);
		organizationIdHiddenField = new HiddenField<String>();
		add(organizationIdHiddenField);
		customTitleTemplateHiddenField = new HiddenField<String>();
		add(customTitleTemplateHiddenField);
		firstNameTextField = new TextField<String>();
		firstNameTextField.setFieldLabel(GwtLocaleProvider.getConstants().FIRST_NAME());
		add(firstNameTextField);
		lastNameTextField = new TextField<String>();
		lastNameTextField.setFieldLabel(GwtLocaleProvider.getConstants().LAST_NAME());
		add(lastNameTextField);
		passwordTextField = new TextField<String>();
		passwordTextField.setFieldLabel(GwtLocaleProvider.getConstants().PASSWORD());
		passwordTextField.setPassword(true);
		passwordTextField.setEnabled(false);
		add(passwordTextField);
		titleTextField = new TextField<String>();
		titleTextField.setFieldLabel(GwtLocaleProvider.getConstants().TITLE());
		add(titleTextField);
		employeeIdTextField = new TextField<String>();
		employeeIdTextField.setFieldLabel(GwtLocaleProvider.getConstants().EMPLOYEE_ID());
		add(employeeIdTextField);
		usernameTextField = new TextField<String>();
		usernameTextField.setFieldLabel(GwtLocaleProvider.getConstants().USERNAME());
		add(usernameTextField);
		emailTextField = new TextField<String>();
		emailTextField.setFieldLabel(GwtLocaleProvider.getConstants().EMAIL());
		emailTextField.setEnabled(true);
		add(emailTextField);
		isManagerCheckBox = new CheckBox();
		isManagerCheckBox.setFieldLabel(GwtLocaleProvider.getConstants().MANAGER());
		isManagerCheckBox.setBoxLabel("");
		add(isManagerCheckBox);
	}

	private void initButtons() {
		displayUserGroupsButton = new Button(GwtLocaleProvider.getConstants().GROUPS());
		getButtonBar().add(displayUserGroupsButton);
		saveUserButton = new Button();
		getButtonBar().add(saveUserButton);
		cancelUserButton = new Button(GwtLocaleProvider.getConstants().CANCEL());
		getButtonBar().add(cancelUserButton);
	}

	private void configureValidation() {
		ComponentUtils.makeRequiredField(firstNameTextField);
		ComponentUtils.makeRequiredField(lastNameTextField);
		ComponentUtils.makeRequiredField(passwordTextField);
		ComponentUtils.makeRequiredField(titleTextField);
		ComponentUtils.makeRequiredField(usernameTextField);
		emailTextField.setAllowBlank(false);
		emailTextField.setValidator(ValidatorProvider.getEmailValidator());
	}

	private void addFormActions() {
		displayUserGroupsButton.addSelectionListener(new SelectionListener<ButtonEvent>() {
			
			@Override
			public void componentSelected(ButtonEvent evemt) {
				
				String userIdAsString = idHiddenField.getValue();
				if (userIdAsString == null) {
					return;
				}
				Long userId = Long.valueOf(userIdAsString);
				
				LoadingManager.get().loading();
				GwtServiceProvider.getOrgService().getGroupNamesOfUserWithId(userId, new AsyncCallback<List<String>>() {

					@Override
					public void onFailure(Throwable exception) {
						MessageUtils.displayError(exception);
						LoadingManager.get().loadingComplete();
					}
					
					@Override
					public void onSuccess(List<String> groupNamesOfUser) {
						String groupNamesOfUserJoined = GwtStringUtils.join(groupNamesOfUser, "; ");
						MessageBox.alert(GwtLocaleProvider.getConstants().GROUPS(), groupNamesOfUserJoined, null);
						LoadingManager.get().loadingComplete();
					}					
				});
			}
		});
		saveUserButton.addSelectionListener(new SelectionListener<ButtonEvent>() {
			public void componentSelected(ButtonEvent event) {
				if (isValid()) {
					UserModel userModelToSave = getUserFromForm();
					LoadingManager.get().loading();
					GwtServiceProvider.getOrgService().setUser(userModelToSave, new AsyncCallback<Void>() {
						@Override
						public void onFailure(Throwable exception) {
							MessageUtils.displayError(exception);
							LoadingManager.get().loadingComplete();
						}
						public void onSuccess(Void nothing) {
							MessageUtils.display(GwtLocaleProvider.getConstants().SUCCESS(), GwtLocaleProvider.getMessages().USER_SAVED());
							AppEventController.fireEvent(AppEventType.User);
							mainWindow.hide();
							LoadingManager.get().loadingComplete();
						};
					});
				}
			}
		});
		cancelUserButton.addSelectionListener(new SelectionListener<ButtonEvent>() {
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
	}

	private UserModel getUserFromForm() {
		UserModel userModel = new UserModel();
		userModel.setUserId(idHiddenField.getValue());
		userModel.setOrganizationUnitId(organizationUnitIdHiddenField.getValue());
		userModel.setOrganizationId(organizationIdHiddenField.getValue());
		userModel.setFirstName(firstNameTextField.getValue());
		userModel.setLastName(lastNameTextField.getValue());
		userModel.setPassword(passwordTextField.getValue());
		userModel.setTitle(titleTextField.getValue());
		userModel.setCustomTitleTemplate(customTitleTemplateHiddenField.getValue());
		userModel.setEmployeeNumber(employeeIdTextField.getValue());
		userModel.setUserName(usernameTextField.getValue());
		userModel.setEmail(emailTextField.getValue());
		userModel.setIsManager(isManagerCheckBox.getValue());
		return userModel;
	}

	public void prepareForAdd(String organizationUnitId, String organizationId) {
		// Goleste toate campurile formularului.
		resetForm();
		//disable la parola - acest camp e preluat din ldap
		passwordTextField.hide();
		mainWindow.show();
		displayUserGroupsButton.setVisible(false);
		saveUserButton.setText(GwtLocaleProvider.getConstants().ADD());
		cancelUserButton.setVisible(true);
		// Seteaza id-ul parintelui.
		organizationUnitIdHiddenField.setValue(organizationUnitId);
		organizationIdHiddenField.setValue(organizationId);
		mainWindow.setHeading(GwtLocaleProvider.getConstants().USER()+" :");
		setVisible(true);
		mainWindow.layout();		
		
	}

	public void prepareForEdit(UserModel selectedUser) {
		// Goleste toate campurile formularului.
		resetForm();
		mainWindow.show();
		displayUserGroupsButton.setVisible(true);
		saveUserButton.setText(GwtLocaleProvider.getConstants().SAVE());
		cancelUserButton.setVisible(true);
		LoadingManager.get().loading();
		// Ia toate detaliile user-ului de pe server.
		GwtServiceProvider.getOrgService().getUserById(selectedUser.getUserId(), new AsyncCallback<UserModel>() {
			@Override
			public void onFailure(Throwable exception) {
				MessageUtils.displayError(exception);
				LoadingManager.get().loadingComplete();
			}
			@Override
			public void onSuccess(final UserModel userModel) {
				// Titlul ferestrei va avea forma : User:[Titlu - ]Nume utilizator
				StringBuffer heading = new StringBuffer(GwtLocaleProvider.getConstants().USER()+" : ");
				if (userModel.getTitle() != null) {
					heading.append(userModel.getTitle());
				}
				if ((userModel.getTitle() != null) && (userModel.getUserName() != null)) {
					heading.append(" - ");
				}
				if (userModel.getUserName() != null) {
					heading.append(userModel.getUserName());
				}
				mainWindow.setHeading(heading.toString());
				idHiddenField.setValue(userModel.getUserId());
				organizationUnitIdHiddenField.setValue(userModel.getOrganizationUnitId());
				organizationIdHiddenField.setValue(userModel.getOrganizationId());
				firstNameTextField.setValue(userModel.getFirstName());
				lastNameTextField.setValue(userModel.getLastName());
				passwordTextField.setValue(userModel.getPassword());
				if (userModel.getUserId() != null )
				{
					passwordTextField.disable();
				}
				titleTextField.setValue(userModel.getTitle());
				customTitleTemplateHiddenField.setValue(userModel.getCustomTitleTemplate());
				employeeIdTextField.setValue(userModel.getEmployeeNumber());
				usernameTextField.setValue(userModel.getUserName());
				emailTextField.setValue(userModel.getEmail());
				isManagerCheckBox.setValue(userModel.isIsManager() != null ? userModel.isIsManager().booleanValue() : false );
				LoadingManager.get().loadingComplete();
				setVisible(true);
				mainWindow.layout();				
			};
		});
	}

	public void prepareForImport(DirectoryUserModel directoryUser, String organizationIdAsString, String organizationUnitIdAsString) {
		resetForm();
		mainWindow.show();
		displayUserGroupsButton.setVisible(false);
		saveUserButton.setText(GwtLocaleProvider.getConstants().IMPORT());
		cancelUserButton.setVisible(true);
		
		StringBuffer heading = new StringBuffer();
		heading.append(GwtLocaleProvider.getConstants().USER()).append(": ");
		if (directoryUser.getTitle() != null) {
			heading.append(directoryUser.getTitle());
			if (directoryUser.getUsername() != null) {
				heading.append(" - ");
			}
		}
		if (directoryUser.getUsername() != null) {
			heading.append(directoryUser.getUsername());
		}
		mainWindow.setHeading(heading.toString());
	
		usernameTextField.setValue(directoryUser.getUsername());
		firstNameTextField.setValue(directoryUser.getFirstName());
		lastNameTextField.setValue(directoryUser.getLastName());
		passwordTextField.setValue(directoryUser.getPassword());
		emailTextField.setValue(directoryUser.getEmail());
		titleTextField.setValue(directoryUser.getTitle());
		employeeIdTextField.setValue(directoryUser.getEmployeeNumber());
		
		organizationIdHiddenField.setValue(organizationIdAsString);
		organizationUnitIdHiddenField.setValue(organizationUnitIdAsString);
		
		setVisible(true);
		mainWindow.layout();
	}
}