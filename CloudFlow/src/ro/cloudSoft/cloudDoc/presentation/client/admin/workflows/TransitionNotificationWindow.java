package ro.cloudSoft.cloudDoc.presentation.client.admin.workflows;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import ro.cloudSoft.cloudDoc.presentation.client.shared.model.OrganizationEntityModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.bpm.notifications.AssignedEntityTransitionNotificationModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.bpm.notifications.HierarchicalSuperiorOfInitiatorTransitionNotificationModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.bpm.notifications.InitiatorTransitionNotificationModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.bpm.notifications.ManuallyChosenEntitiesTransitionNotificationModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.bpm.notifications.TransitionNotificationModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.bpm.notifications.TransitionNotificationModelType;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.bpm.notifications.UserMetadataTransitionNotificationModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.content.DocumentTypeModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.content.MetadataDefinitionModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.propertyEditors.EnumPropertyEditor;
import ro.cloudSoft.cloudDoc.presentation.client.shared.providers.GwtLocaleProvider;
import ro.cloudSoft.cloudDoc.presentation.client.shared.utils.ComponentUtils;
import ro.cloudSoft.cloudDoc.presentation.client.shared.utils.ErrorHelper;
import ro.cloudSoft.cloudDoc.presentation.client.shared.utils.PropertyEditors;
import ro.cloudSoft.cloudDoc.presentation.client.shared.widgets.PlainOrganizationalStructureEntitiesSelectionComponent;

import com.extjs.gxt.ui.client.Style.Scroll;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.widget.Window;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.form.ComboBox.TriggerAction;
import com.extjs.gxt.ui.client.widget.form.FieldSet;
import com.extjs.gxt.ui.client.widget.form.FormPanel;
import com.extjs.gxt.ui.client.widget.form.HiddenField;
import com.extjs.gxt.ui.client.widget.form.SimpleComboBox;
import com.extjs.gxt.ui.client.widget.form.TextArea;
import com.extjs.gxt.ui.client.widget.form.TextField;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.extjs.gxt.ui.client.widget.layout.FormData;

public class TransitionNotificationWindow extends Window {
	
	private final TransitionNotificationsComponent parentComponent;
	
	private FormPanel formPanel;
	
	private HiddenField<Integer> indexHiddenField;
	
	private HiddenField<TransitionNotificationModelType> typeHiddenField;
	private HiddenField<Long> idHiddenField;
	
	private TextField<String> emailSubjectTemplateTextField;
	private TextArea emailContentTemplateTextArea;
	
	private FieldSet manuallyChosenEntitiesFieldSet;
	private ManuallyChosenEntitiesSelectionComponent manuallyChosenEntitiesSelectionComponent;
	
	private SimpleComboBox<String> userMetadataComboBox;
	
	private Button saveButton;
	private Button cancelButton;
	
	public TransitionNotificationWindow(final TransitionNotificationsComponent parentComponent) {
		
		this.parentComponent = parentComponent;
		
		setHeading(GwtLocaleProvider.getConstants().NOTIFICATION());
		setLayout(new FitLayout());
		setModal(true);
		setSize(900, 450);
		
		formPanel = new FormPanel();
		formPanel.setBodyBorder(false);
		formPanel.setHeaderVisible(false);
		formPanel.setScrollMode(Scroll.AUTO);
		add(formPanel);
		
		FormData formData = new FormData("90%");
		
		indexHiddenField = new HiddenField<Integer>();
		indexHiddenField.setPropertyEditor(PropertyEditors.INTEGER);
		formPanel.add(indexHiddenField);
		
		typeHiddenField = new HiddenField<TransitionNotificationModelType>();
		typeHiddenField.setPropertyEditor(new EnumPropertyEditor<TransitionNotificationModelType>(TransitionNotificationModelType.class));
		formPanel.add(typeHiddenField);
		
		idHiddenField = new HiddenField<Long>();
		idHiddenField.setPropertyEditor(PropertyEditors.LONG);
		formPanel.add(idHiddenField);
		
		emailSubjectTemplateTextField = new TextField<String>();
		emailSubjectTemplateTextField.setAllowBlank(false);
		emailSubjectTemplateTextField.setFieldLabel(GwtLocaleProvider.getConstants().EMAIL_SUBJECT());
		formPanel.add(emailSubjectTemplateTextField, formData);
		
		emailContentTemplateTextArea = new TextArea();
		emailContentTemplateTextArea.setAllowBlank(false);
		emailContentTemplateTextArea.setFieldLabel(GwtLocaleProvider.getConstants().EMAIL_BODY());
		emailContentTemplateTextArea.setHeight(150);
		formPanel.add(emailContentTemplateTextArea, formData);
		
		manuallyChosenEntitiesFieldSet = new FieldSet();
		manuallyChosenEntitiesFieldSet.setHeading(GwtLocaleProvider.getConstants().MANUALLY_CHOSEN_ENTITIES());
		manuallyChosenEntitiesFieldSet.setLayout(new FitLayout());
		formPanel.add(manuallyChosenEntitiesFieldSet, formData);
		
		manuallyChosenEntitiesSelectionComponent = new ManuallyChosenEntitiesSelectionComponent();
		manuallyChosenEntitiesFieldSet.add(manuallyChosenEntitiesSelectionComponent);
		
		userMetadataComboBox = new SimpleComboBox<String>();
		userMetadataComboBox.setEditable(false);
		userMetadataComboBox.setFieldLabel(GwtLocaleProvider.getConstants().METADATA());
		userMetadataComboBox.setTriggerAction(TriggerAction.ALL);
		formPanel.add(userMetadataComboBox, formData);
		
		saveButton = new Button();
		saveButton.setText(GwtLocaleProvider.getConstants().SAVE());
		saveButton.addSelectionListener(new SelectionListener<ButtonEvent>() {
			
			@Override
			public void componentSelected(ButtonEvent event) {
				if (isValid()) {
					
					Integer index = indexHiddenField.getValue();
					TransitionNotificationModel notification = getNotificationFromForm();
					
					parentComponent.onSave(index, notification);
					closeWindow();
				} else {
					ErrorHelper.displayErrors();
				}
			}
		});
		getButtonBar().add(saveButton);
		
		cancelButton = new Button();
		cancelButton.setText(GwtLocaleProvider.getConstants().CANCEL());
		cancelButton.addSelectionListener(new SelectionListener<ButtonEvent>() {
			
			@Override
			public void componentSelected(ButtonEvent event) {
				closeWindow();
			}
		});
		getButtonBar().add(cancelButton);
	}
	
	public void doWithSelectedDocumentTypes(List<DocumentTypeModel> documentTypes) {
		
		Set<String> distinctUserMetadataNames = new LinkedHashSet<String>();
		
		for (DocumentTypeModel documentType : documentTypes) {
			for (MetadataDefinitionModel metadata : documentType.getMetadataDefinitions()) {
				if (metadata.getType().equals(MetadataDefinitionModel.TYPE_USER)) {
					distinctUserMetadataNames.add(metadata.getName());
				}
			}
		}
		
		List<String> distinctUserMetadataNamesAsList = new ArrayList<String>(distinctUserMetadataNames);
		Collections.sort(distinctUserMetadataNamesAsList, String.CASE_INSENSITIVE_ORDER);
		
		userMetadataComboBox.removeAll();
		userMetadataComboBox.add(distinctUserMetadataNamesAsList);
	}
	
	public void reset() {
		formPanel.clear();
		manuallyChosenEntitiesSelectionComponent.reset();
	}
	
	public void prepareForAdd(TransitionNotificationModelType notificationType) {
		
		reset();

		changeFormPerspectiveForType(notificationType);
		
		typeHiddenField.setValue(notificationType);
		manuallyChosenEntitiesSelectionComponent.populate(null);
		
		populateTypeSpecificFields(null);
		
		showWindow();
	}
	
	public void prepareForEdit(Integer index, TransitionNotificationModel notification) {
		
		reset();
		
		changeFormPerspectiveForType(notification.getType());
		
		indexHiddenField.setValue(index);
		
		typeHiddenField.setValue(notification.getType());
		idHiddenField.setValue(notification.getId());
		
		emailSubjectTemplateTextField.setValue(notification.getEmailSubjectTemplate());
		emailContentTemplateTextArea.setValue(notification.getEmailContentTemplate());
		
		populateTypeSpecificFields(notification);
		
		showWindow();
	}
	
	private void changeFormPerspectiveForType(TransitionNotificationModelType notificationType) {
		
		boolean isManuallyChosenEntitiesType = notificationType.equals(TransitionNotificationModelType.MANUALLY_CHOSEN_ENTITIES);
		manuallyChosenEntitiesFieldSet.setVisible(isManuallyChosenEntitiesType);
		
		boolean isUserMetadataType = notificationType.equals(TransitionNotificationModelType.METADATA);
		userMetadataComboBox.setVisible(isUserMetadataType);
		userMetadataComboBox.setAllowBlank(!isUserMetadataType);
	}
	
	private void populateTypeSpecificFields(TransitionNotificationModel notification) {
		
		if (notification == null) {
			return;
		}
		
		if (notification.getType().equals(TransitionNotificationModelType.MANUALLY_CHOSEN_ENTITIES)) {
			ManuallyChosenEntitiesTransitionNotificationModel manuallyChosenEntitiesNotification = (ManuallyChosenEntitiesTransitionNotificationModel) notification;
			manuallyChosenEntitiesSelectionComponent.populate(manuallyChosenEntitiesNotification.getManuallyChosenEntities());
		} else if (notification.getType().equals(TransitionNotificationModelType.METADATA)) {
			UserMetadataTransitionNotificationModel userMetadataNotification = (UserMetadataTransitionNotificationModel) notification;
			userMetadataComboBox.setSimpleValue(userMetadataNotification.getMetadataName());
		}
	}
	
	private void showWindow() {
		ComponentUtils.putParentWindowToBackIfExists(parentComponent);
		show();
		toFront();
	}
	
	private void closeWindow() {
		hide();
	}
	
	private TransitionNotificationModelType getMandatoryTypeFromForm() {
		TransitionNotificationModelType type = typeHiddenField.getValue();
		if (type == null) {
			throw new IllegalStateException("Nu este setat tipul de notificare in formular.");
		}
		return type;
	}
	
	private boolean isValid() {
		
		boolean isFormValid = formPanel.isValid();
		if (!isFormValid) {
			return false;
		}
		
		TransitionNotificationModelType type = getMandatoryTypeFromForm();
		
		if (type.equals(TransitionNotificationModelType.MANUALLY_CHOSEN_ENTITIES)) {
			boolean areEntitiesSelected = (!manuallyChosenEntitiesSelectionComponent.getSelectedEntities().isEmpty());
			if (!areEntitiesSelected) {
				ErrorHelper.addError(GwtLocaleProvider.getMessages().NO_SELECTED_ENTITIES());
				return false;
			}
		}
			
		return true;
	}
	
	private TransitionNotificationModel getNotificationFromForm() {
		
		TransitionNotificationModel notification = createNewNotificationInstance();
		
		setCommonPropertiesFromForm(notification);
		setSpecificPropertiesFromForm(notification);
		
		return notification;
	}
	
	private TransitionNotificationModel createNewNotificationInstance() {
		
		TransitionNotificationModelType type = getMandatoryTypeFromForm();
		
		if (type.equals(TransitionNotificationModelType.ASSIGNED_ENTITY)) {
			return new AssignedEntityTransitionNotificationModel();
		} else if (type.equals(TransitionNotificationModelType.INITIATOR)) {
			return new InitiatorTransitionNotificationModel();
		} else if (type.equals(TransitionNotificationModelType.HIERARCHICAL_SUPERIOR_OF_INITIATOR)) {
			return new HierarchicalSuperiorOfInitiatorTransitionNotificationModel();
		} else if (type.equals(TransitionNotificationModelType.MANUALLY_CHOSEN_ENTITIES)) {
			return new ManuallyChosenEntitiesTransitionNotificationModel();
		} else if (type.equals(TransitionNotificationModelType.METADATA)) {
			return new UserMetadataTransitionNotificationModel();
		} else {
			throw new IllegalStateException("Tip necunoscut de notificare: " + type);
		}
	}
	
	private void setCommonPropertiesFromForm(TransitionNotificationModel notification) {
		notification.setId(idHiddenField.getValue());
		notification.setEmailSubjectTemplate(emailSubjectTemplateTextField.getValue());
		notification.setEmailContentTemplate(emailContentTemplateTextArea.getValue());
	}
	
	private void setSpecificPropertiesFromForm(TransitionNotificationModel notification) {
		
		TransitionNotificationModelType type = getMandatoryTypeFromForm();
		
		if (type.equals(TransitionNotificationModelType.MANUALLY_CHOSEN_ENTITIES)) {
			
			ManuallyChosenEntitiesTransitionNotificationModel manuallyChosenEntitiesNotification = (ManuallyChosenEntitiesTransitionNotificationModel) notification;
			
			List<OrganizationEntityModel> manuallyChosenEntities = manuallyChosenEntitiesSelectionComponent.getSelectedEntitiesAsOrganizationEntities();
			manuallyChosenEntitiesNotification.setManuallyChosenEntities(manuallyChosenEntities);
		} else if (type.equals(TransitionNotificationModelType.METADATA)) {
			
			UserMetadataTransitionNotificationModel userMetadataNotification = (UserMetadataTransitionNotificationModel) notification;
			
			String metadataName = userMetadataComboBox.getSimpleValue();
			userMetadataNotification.setMetadataName(metadataName);
		}
	}
	
	public static class ManuallyChosenEntitiesSelectionComponent extends PlainOrganizationalStructureEntitiesSelectionComponent {
		
		private static final int LEFT_PANEL_ORIGINAL_WIDTH = 300;
		private static final int LEFT_PANEL_ORIGINAL_HEIGHT = 300;
		private static final int RIGHT_PANEL_ORIGINAL_WIDTH = 300;
		private static final int RIGHT_PANEL_ORIGINAL_HEIGHT = 300;
		private static final int BUTTONS_PANEL_ORIGINAL_WIDTH = 70;
		private static final int BUTTONS_PANEL_ORIGINAL_HEIGHT = 300;
		
		public ManuallyChosenEntitiesSelectionComponent() {
			super(OrganizationTreeExpandMode.MANUAL);
			changeSize(1, 1);
		}

		@Override
		public void changeSize(float wIndex, float hIndex){
			leftPanel.setSize(Math.round(LEFT_PANEL_ORIGINAL_WIDTH * wIndex), Math.round(LEFT_PANEL_ORIGINAL_HEIGHT * hIndex));
			buttonsPanel.setSize(Math.round(BUTTONS_PANEL_ORIGINAL_WIDTH * wIndex), Math.round(BUTTONS_PANEL_ORIGINAL_HEIGHT * hIndex));
			if (wIndex < 1)
				rightPanel.setSize(Math.round(RIGHT_PANEL_ORIGINAL_WIDTH * wIndex), Math.round(RIGHT_PANEL_ORIGINAL_HEIGHT * hIndex));
			else
				rightPanel.setSize(Math.round(RIGHT_PANEL_ORIGINAL_WIDTH * wIndex), Math.round(RIGHT_PANEL_ORIGINAL_HEIGHT * hIndex));
		}
	}
}