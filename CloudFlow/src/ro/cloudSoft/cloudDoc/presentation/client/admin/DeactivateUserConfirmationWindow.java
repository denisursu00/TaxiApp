package ro.cloudSoft.cloudDoc.presentation.client.admin;

import ro.cloudSoft.cloudDoc.presentation.client.shared.event.AppEventController;
import ro.cloudSoft.cloudDoc.presentation.client.shared.event.AppEventType;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.organization.GwtUserDeactivationMode;
import ro.cloudSoft.cloudDoc.presentation.client.shared.providers.GwtLocaleProvider;
import ro.cloudSoft.cloudDoc.presentation.client.shared.providers.GwtServiceProvider;
import ro.cloudSoft.cloudDoc.presentation.client.shared.utils.LoadingManager;
import ro.cloudSoft.cloudDoc.presentation.client.shared.utils.MessageUtils;
import ro.cloudSoft.cloudDoc.presentation.client.shared.widgets.CheckBoxWithNullFix;
import ro.cloudSoft.cloudDoc.presentation.client.shared.widgets.CustomText;

import com.extjs.gxt.ui.client.Style.HorizontalAlignment;
import com.extjs.gxt.ui.client.Style.Scroll;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.widget.VerticalPanel;
import com.extjs.gxt.ui.client.widget.Window;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.layout.FitData;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class DeactivateUserConfirmationWindow extends Window {

	private VerticalPanel verticalPanel;
	
	private CustomText confirmationQuestionText;
	private CheckBoxWithNullFix deactivateAllUserAccountsCheckBox;
	
	private Button yesButton;
	private Button noButton;
	
	private Long userId;
	
	public DeactivateUserConfirmationWindow() {
		
		setButtonAlign(HorizontalAlignment.CENTER);
		setHeading(GwtLocaleProvider.getConstants().DEACTIVATE_USER());
		setLayout(new FitLayout());
		setClosable(false);
		setMaximizable(false);
		setMinimizable(false);
		setResizable(false);
		setSize(320, 180);
		
		verticalPanel = new VerticalPanel();
		verticalPanel.setScrollMode(Scroll.AUTOY);
		verticalPanel.setSpacing(10);
		add(verticalPanel, new FitData(10));
		
		confirmationQuestionText = new CustomText();
		confirmationQuestionText.setText(GwtLocaleProvider.getMessages().CONFIRM_DEACTIVATE_USER());
		verticalPanel.add(confirmationQuestionText);
		
		deactivateAllUserAccountsCheckBox = new CheckBoxWithNullFix();
		deactivateAllUserAccountsCheckBox.setBoxLabel(GwtLocaleProvider.getConstants().DEACTIVATE_ALL_ACCOUNTS_OF_USER());
		verticalPanel.add(deactivateAllUserAccountsCheckBox);
		
		yesButton = new Button();
		yesButton.setText(GwtLocaleProvider.getConstants().YES());
		yesButton.addSelectionListener(new SelectionListener<ButtonEvent>() {
			
			@Override
			public void componentSelected(ButtonEvent ce) {
				
				if (userId == null) {
					throw new IllegalStateException("S-a incercat dezactivarea unui utilizator, dar NU s-a specificat ID-ul.");
				}
				
				hide();
				
				GwtUserDeactivationMode deactivationMode = getDeactivationMode();

				LoadingManager.get().loading();
				GwtServiceProvider.getOrgService().deactivateUserWithId(userId, deactivationMode, new AsyncCallback<Void>() {
					
					@Override
					public void onFailure(Throwable exception) {
						MessageUtils.displayError(exception);
						LoadingManager.get().loadingComplete();
					}
					
					@Override
					public void onSuccess(Void nothing) {
						MessageUtils.display(GwtLocaleProvider.getConstants().SUCCESS(),
							GwtLocaleProvider.getMessages().USER_DEACTIVATED());
						AppEventController.fireEvent(AppEventType.User);
						LoadingManager.get().loadingComplete();
					}
				});
			}
		});
		getButtonBar().add(yesButton);
		
		noButton = new Button();
		noButton.setText(GwtLocaleProvider.getConstants().NO());
		noButton.addSelectionListener(new SelectionListener<ButtonEvent>() {
			
			@Override
			public void componentSelected(ButtonEvent ce) {
				hide();
			}
		});
		getButtonBar().add(noButton);
	}
	
	public void prepareForConfirm(Long userId) {
		reset();
		this.userId = userId;
		show();
	}
	
	private void reset() {
		this.userId = null;
		deactivateAllUserAccountsCheckBox.setChecked(false);
	}
	
	private GwtUserDeactivationMode getDeactivationMode() {
		return (deactivateAllUserAccountsCheckBox.isChecked()) ? GwtUserDeactivationMode.ALL_ACCOUNTS : GwtUserDeactivationMode.SINGLE_ACCOUNT;
	}
}