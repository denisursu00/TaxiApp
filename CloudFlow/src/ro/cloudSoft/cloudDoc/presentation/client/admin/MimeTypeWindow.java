package ro.cloudSoft.cloudDoc.presentation.client.admin;

import ro.cloudSoft.cloudDoc.presentation.client.shared.event.AppEventController;
import ro.cloudSoft.cloudDoc.presentation.client.shared.event.AppEventType;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.content.MimeTypeModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.providers.GwtLocaleProvider;
import ro.cloudSoft.cloudDoc.presentation.client.shared.providers.GwtServiceProvider;
import ro.cloudSoft.cloudDoc.presentation.client.shared.utils.ErrorHelper;
import ro.cloudSoft.cloudDoc.presentation.client.shared.utils.LoadingManager;
import ro.cloudSoft.cloudDoc.presentation.client.shared.utils.MessageUtils;
import ro.cloudSoft.cloudDoc.presentation.client.shared.utils.PropertyEditors;

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

public class MimeTypeWindow extends Window {
	
	private FormPanel form;

	private Button saveButton;
	private Button cancelButton;
	
	private HiddenField<Long> idHiddenField;
	private TextField<String> nameTextField;
	private TextField<String> extensionTextField;
	
	public MimeTypeWindow() {		
		setHeading(GwtLocaleProvider.getConstants().MIME_TYPES());
		setSize(600, 200);
		setMinWidth(400);
		setMinHeight(150);
		setMaximizable(false);
		setModal(true);
		setLayout(new FitLayout());
		initForm();
		initButtons();
		addButtonActions();
	}
	
	private void  initForm(){
		form = new FormPanel();
		form.setHeaderVisible(false);
		idHiddenField = new HiddenField<Long>();
		idHiddenField.setPropertyEditor(PropertyEditors.LONG);
		
		nameTextField = new TextField<String>();
		nameTextField.setFieldLabel(GwtLocaleProvider.getConstants().MIME_TYPE_NAME());
		nameTextField.setAllowBlank(false);
			
		extensionTextField = new TextField<String>();
		extensionTextField.setFieldLabel(GwtLocaleProvider.getConstants().MIME_TYPE_EXTENSION());
		extensionTextField.setAllowBlank(false);
		
		FormData formData = new FormData("92%");
		
		form.add(idHiddenField, formData);
		form.add(nameTextField, formData);
		form.add(extensionTextField, formData);
		
		add(form);
	}
	
	private void initButtons() {
		saveButton = new Button();
		saveButton.setText(GwtLocaleProvider.getConstants().SAVE());
		
		cancelButton = new Button();
		cancelButton.setText(GwtLocaleProvider.getConstants().CANCEL());
		
		getButtonBar().add(saveButton);
		getButtonBar().add(cancelButton);
	}
	
	private void addButtonActions() {
		saveButton.addSelectionListener(new SelectionListener<ButtonEvent>() {
			
			@Override
			public void componentSelected(ButtonEvent event) {
				if (isValid()) {
					MimeTypeModel mimeTypeToSave = getModelFromForm();
					saveMimeType(mimeTypeToSave);
				} else {
					ErrorHelper.displayErrors();
				}
			}
			
			private void saveMimeType(MimeTypeModel mimeTypeToSave) {
				LoadingManager.get().loading();
				GwtServiceProvider.getMimeTypeService().saveMimeType(mimeTypeToSave, new AsyncCallback<Void>() {
					@Override
					public void onFailure(Throwable exception) {
						MessageUtils.displayError(exception);
						LoadingManager.get().loadingComplete();
					}
					
					@Override
					public void onSuccess(Void arg0) {
						MessageUtils.display(GwtLocaleProvider.getConstants().SUCCESS(), GwtLocaleProvider.getMessages().MIME_TYPE_SAVED());
						AppEventController.fireEvent(AppEventType.MimeType);
						hide();
						LoadingManager.get().loadingComplete();
					}
				});
			}
		});
		cancelButton.addSelectionListener(new SelectionListener<ButtonEvent>() {
			@Override
			public void componentSelected(ButtonEvent event) {
				hide();
			};
		});
	}
	
	private boolean isValid() {
		return form.isValid();
	}
	
	private void reset() {
		form.clear();
	}
	
	public void prepareForAdd() {
		reset();
		show();
	}
	
	public void prepareForEdit(final Long mimeTypeId) {
		
		LoadingManager.get().loading();
		GwtServiceProvider.getMimeTypeService().getMimeTypeById(mimeTypeId, new AsyncCallback<MimeTypeModel>() {
			@Override
			public void onFailure(Throwable exception) {
				MessageUtils.displayError(exception);
				LoadingManager.get().loadingComplete();
			}
			@Override
			public void onSuccess(MimeTypeModel mimeType) {
				reset();
				populateForm(mimeType);
				show();
				LoadingManager.get().loadingComplete();
			}
		});


	}
	
	private void populateForm(MimeTypeModel mimeTypeModel){
		idHiddenField.setValue(mimeTypeModel.getId());
		nameTextField.setValue(mimeTypeModel.getName());
		extensionTextField.setValue(mimeTypeModel.getExtension());
	}
	
	private MimeTypeModel getModelFromForm() {
		MimeTypeModel mimeTypeModel = new MimeTypeModel();
		Long mimeTypeId = idHiddenField.getValue();
		mimeTypeModel.setId(mimeTypeId);
		mimeTypeModel.setName(nameTextField.getValue());
		mimeTypeModel.setExtension(extensionTextField.getValue());
		return mimeTypeModel;
	}
}