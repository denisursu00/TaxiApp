package ro.cloudSoft.cloudDoc.presentation.client.admin;

import ro.cloudSoft.cloudDoc.presentation.client.shared.constants.GwtFormatConstants;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.log.GwtLogActorType;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.log.GwtLogLevel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.log.LogEntryModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.propertyEditors.EnumPropertyEditor;
import ro.cloudSoft.cloudDoc.presentation.client.shared.providers.GwtLocaleProvider;
import ro.cloudSoft.cloudDoc.presentation.client.shared.utils.PropertyEditors;

import com.extjs.gxt.ui.client.Style.Scroll;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.widget.Window;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.form.DateField;
import com.extjs.gxt.ui.client.widget.form.FormPanel;
import com.extjs.gxt.ui.client.widget.form.TextArea;
import com.extjs.gxt.ui.client.widget.form.TextField;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.extjs.gxt.ui.client.widget.layout.FormData;
import com.google.gwt.user.client.Element;

public class LogEntryDetailsWindow extends Window {

	private FormPanel formPanel;
	
	private TextField<GwtLogLevel> levelTextField;
	private DateField timeDateField;
	
	private TextField<String> moduleTextField;
	private TextField<String> operationTextField;
	
	private TextField<GwtLogActorType> actorTypeTextField;
	private TextField<String> actorDisplayNameTextField;
	private TextField<Long> userIdTextField;
	
	private TextArea messageTextArea;
	private TextArea exceptionTextArea;
	
	private Button closeButton;
	
	public LogEntryDetailsWindow() {
		
		setHeading(GwtLocaleProvider.getConstants().LOG_ENTRY());
		setLayout(new FitLayout());
		setMaximizable(false);
		setModal(true);
		setResizable(false);
		setSize(900, 540);
		
		initFormPanel();
		initButtons();
	}
	
	private void initFormPanel() {
		
		levelTextField = new TextField<GwtLogLevel>();
		levelTextField.setFieldLabel(GwtLocaleProvider.getConstants().LEVEL());
		levelTextField.setPropertyEditor(new EnumPropertyEditor<GwtLogLevel>(GwtLogLevel.class));
		levelTextField.setReadOnly(true);
		
		timeDateField = new DateField();
		timeDateField.getPropertyEditor().setFormat(GwtFormatConstants.GWT_DATE_TIME_FORMATTER_FOR_LOG);
		timeDateField.setFieldLabel(GwtLocaleProvider.getConstants().DATE_TIME());
		timeDateField.setReadOnly(true);
		
		moduleTextField = new TextField<String>();
		moduleTextField.setFieldLabel(GwtLocaleProvider.getConstants().MODULE());
		moduleTextField.setReadOnly(true);
		
		operationTextField = new TextField<String>();
		operationTextField.setFieldLabel(GwtLocaleProvider.getConstants().OPERATION());
		operationTextField.setReadOnly(true);
		
		actorTypeTextField = new TextField<GwtLogActorType>();
		actorTypeTextField.setFieldLabel(GwtLocaleProvider.getConstants().ACTOR_TYPE());
		actorTypeTextField.setPropertyEditor(new EnumPropertyEditor<GwtLogActorType>(GwtLogActorType.class));
		actorTypeTextField.setReadOnly(true);
		
		actorDisplayNameTextField = new TextField<String>();
		actorDisplayNameTextField.setFieldLabel(GwtLocaleProvider.getConstants().ACTOR_NAME());
		actorDisplayNameTextField.setReadOnly(true);
		
		userIdTextField = new TextField<Long>();
		userIdTextField.setFieldLabel(GwtLocaleProvider.getConstants().USERID());
		userIdTextField.setPropertyEditor(PropertyEditors.LONG);
		userIdTextField.setReadOnly(true);
		
		messageTextArea = new TextArea();
		messageTextArea.setFieldLabel(GwtLocaleProvider.getConstants().MESSAGE());
		messageTextArea.setHeight(100);
		messageTextArea.setReadOnly(true);
		
		exceptionTextArea = new TextArea() {
			
			@Override
			protected void onRender(Element target, int index) {
				super.onRender(target, index);
				getElement().setAttribute("wrap", "off");
			}
		};
		exceptionTextArea.setFieldLabel(GwtLocaleProvider.getConstants().EXCEPTION());
		exceptionTextArea.setHeight(400);
		exceptionTextArea.setReadOnly(true);
		
		formPanel = new FormPanel();
		formPanel.setBodyBorder(false);
		formPanel.setBorders(false);
		formPanel.setHeaderVisible(false);
		formPanel.setScrollMode(Scroll.AUTO);
		
		FormData formData = new FormData("95%");
		
		formPanel.add(levelTextField, formData);
		formPanel.add(timeDateField, formData);
		formPanel.add(moduleTextField, formData);
		formPanel.add(operationTextField, formData);
		formPanel.add(actorTypeTextField, formData);
		formPanel.add(actorDisplayNameTextField, formData);
		formPanel.add(userIdTextField, formData);
		formPanel.add(messageTextArea, formData);
		formPanel.add(exceptionTextArea, formData);
		
		add(formPanel);
	}
	
	private void initButtons() {
		
		closeButton = new Button();
		closeButton.setText(GwtLocaleProvider.getConstants().CLOSE());
		closeButton.addSelectionListener(new SelectionListener<ButtonEvent>() {
			
			@Override
			public void componentSelected(ButtonEvent event) {
				hide();
			}
		});
		
		getButtonBar().add(closeButton);
	}
	
	public void displayLogEntry(LogEntryModel logEntry) {
		
		levelTextField.setValue(logEntry.getLevel());
		timeDateField.setValue(logEntry.getTime());
		
		moduleTextField.setValue(logEntry.getModule());
		operationTextField.setValue(logEntry.getOperation());
		
		actorTypeTextField.setValue(logEntry.getActorType());
		actorDisplayNameTextField.setValue(logEntry.getActorDisplayName());
		userIdTextField.setValue(logEntry.getUserId());
		
		messageTextArea.setValue(logEntry.getMessage());
		exceptionTextArea.setValue(logEntry.getException());
		
		show();
	}
}