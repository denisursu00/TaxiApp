package ro.cloudSoft.cloudDoc.presentation.client.admin;

import java.util.List;

import ro.cloudSoft.cloudDoc.presentation.client.shared.model.directory.organization.DirectoryUserModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.directory.organization.GwtDirectoryUserSearchCriteria;
import ro.cloudSoft.cloudDoc.presentation.client.shared.providers.GwtLocaleProvider;
import ro.cloudSoft.cloudDoc.presentation.client.shared.providers.GwtServiceProvider;
import ro.cloudSoft.cloudDoc.presentation.client.shared.utils.LoadingManager;
import ro.cloudSoft.cloudDoc.presentation.client.shared.utils.MessageUtils;

import com.extjs.gxt.ui.client.Style.Scroll;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.widget.Window;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.form.FormPanel;
import com.extjs.gxt.ui.client.widget.form.HiddenField;
import com.extjs.gxt.ui.client.widget.form.TextField;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.extjs.gxt.ui.client.widget.layout.FormData;
import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * 
 */
public class FindUserInDirectoryWindow extends Window {

	private UsersFoundInDirectoryWindow usersFoundInDirectoryWindow = new UsersFoundInDirectoryWindow();

	private FormPanel formPanel = new FormPanel();
	
	private HiddenField<String> organizationIdAsStringHiddenField = new HiddenField<String>();
	private HiddenField<String> organizationUnitIdAsStringHiddenField = new HiddenField<String>();
	
	private TextField<String> firstNameTextField = new TextField<String>();
	private TextField<String> lastNameTextField = new TextField<String>();
	private TextField<String> usernameTextField = new TextField<String>();
	
	private Button searchButton = new Button();
	private Button cancelButton = new Button();
	
	public FindUserInDirectoryWindow() {

		setHeading(GwtLocaleProvider.getConstants().USER_FROM_DIRECTORY());
		setLayout(new FitLayout());
		setModal(true);
		setResizable(false);
		setSize(640, 400);
		
		formPanel.setBodyBorder(false);
		formPanel.setBorders(false);
		formPanel.setHeaderVisible(false);
		formPanel.setLabelWidth(150);
		formPanel.setScrollMode(Scroll.AUTO);
		
		FormData formData = new FormData("95%");
		
		formPanel.add(organizationUnitIdAsStringHiddenField);
		
		formPanel.add(organizationIdAsStringHiddenField);
		
		firstNameTextField.setFieldLabel(GwtLocaleProvider.getConstants().FIRST_NAME());
		formPanel.add(firstNameTextField, formData);
		
		lastNameTextField.setFieldLabel(GwtLocaleProvider.getConstants().LAST_NAME());
		formPanel.add(lastNameTextField, formData);
		
		usernameTextField.setFieldLabel(GwtLocaleProvider.getConstants().USERNAME());
		formPanel.add(usernameTextField, formData);
		
		add(formPanel);
		
		searchButton.addSelectionListener(new SelectionListener<ButtonEvent>() {
			
			@Override
			public void componentSelected(ButtonEvent buttonEvent) {
					
					GwtDirectoryUserSearchCriteria userSearchCriteria = getUserSearchCriteria();
					
					final String organizationIdAsString = organizationIdAsStringHiddenField.getValue();
					final String organizationUnitIdAsString = organizationUnitIdAsStringHiddenField.getValue();
					
					LoadingManager.get().loading();
					GwtServiceProvider.getOrgService().findUsersInDirectory(userSearchCriteria, new AsyncCallback<List<DirectoryUserModel>>() {

						@Override
						public void onFailure(Throwable exception) {
							MessageUtils.displayError(exception);
							LoadingManager.get().loadingComplete();
						}
						
						@Override
						public void onSuccess(List<DirectoryUserModel> users) {
							usersFoundInDirectoryWindow.showFoundUsers(users, organizationIdAsString, organizationUnitIdAsString);
							hide();
							LoadingManager.get().loadingComplete();
						}
					});
				}
		});
		
		searchButton.setText(GwtLocaleProvider.getConstants().SEARCH());
		getButtonBar().add(searchButton);
		
		cancelButton.addSelectionListener(new SelectionListener<ButtonEvent>() {
			
			@Override
			public void componentSelected(ButtonEvent buttonEvent) {
				hide();
			}
		});
		cancelButton.setText(GwtLocaleProvider.getConstants().CANCEL());
		getButtonBar().add(cancelButton);
	}
	
	public void prepareForSearch(String organizationIdAsString, String organizationUnitIdAsString) {
		
		reset();
		
		this.organizationIdAsStringHiddenField.setValue(organizationIdAsString);
		this.organizationUnitIdAsStringHiddenField.setValue(organizationUnitIdAsString);
		
		show();
	}
	
	private void reset() {
		formPanel.clear();
	}
	
	public GwtDirectoryUserSearchCriteria getUserSearchCriteria() {
		
		GwtDirectoryUserSearchCriteria userSearchCriteria = new GwtDirectoryUserSearchCriteria();
		
		userSearchCriteria.setFirstName(firstNameTextField.getValue());
		userSearchCriteria.setLastName(lastNameTextField.getValue());
		userSearchCriteria.setUsername(usernameTextField.getValue());
		
		return userSearchCriteria;
	}
}