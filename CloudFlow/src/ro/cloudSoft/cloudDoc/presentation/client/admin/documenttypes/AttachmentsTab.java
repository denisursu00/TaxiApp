package ro.cloudSoft.cloudDoc.presentation.client.admin.documenttypes;

import java.util.ArrayList;
import java.util.List;

import ro.cloudSoft.cloudDoc.presentation.client.shared.model.bpm.WorkflowStateModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.content.DocumentTypeModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.content.MimeTypeModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.providers.GwtLocaleProvider;
import ro.cloudSoft.cloudDoc.presentation.client.shared.providers.GwtServiceProvider;
import ro.cloudSoft.cloudDoc.presentation.client.shared.utils.ComponentUtils;
import ro.cloudSoft.cloudDoc.presentation.client.shared.utils.ErrorHelper;
import ro.cloudSoft.cloudDoc.presentation.client.shared.utils.LoadingManager;
import ro.cloudSoft.cloudDoc.presentation.client.shared.utils.MessageUtils;
import ro.cloudSoft.cloudDoc.presentation.client.shared.widgets.CheckBoxWithNullFix;
import ro.cloudSoft.cloudDoc.presentation.client.shared.widgets.FieldSetWithFixes;
import ro.cloudSoft.cloudDoc.presentation.client.shared.widgets.WorkflowStatesSelectionPopupField;

import com.extjs.gxt.ui.client.Style.Scroll;
import com.extjs.gxt.ui.client.event.BaseEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.TabItem;
import com.extjs.gxt.ui.client.widget.form.AdapterField;
import com.extjs.gxt.ui.client.widget.form.CheckBox;
import com.extjs.gxt.ui.client.widget.form.FieldSet;
import com.extjs.gxt.ui.client.widget.form.FormPanel;
import com.extjs.gxt.ui.client.widget.form.TextField;
import com.extjs.gxt.ui.client.widget.grid.CellEditor;
import com.extjs.gxt.ui.client.widget.grid.CheckColumnConfig;
import com.extjs.gxt.ui.client.widget.grid.ColumnConfig;
import com.extjs.gxt.ui.client.widget.grid.ColumnModel;
import com.extjs.gxt.ui.client.widget.grid.EditorGrid;
import com.extjs.gxt.ui.client.widget.grid.GridView;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.extjs.gxt.ui.client.widget.layout.FormData;
import com.extjs.gxt.ui.client.widget.layout.FormLayout;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class AttachmentsTab extends TabItem {

	private FormPanel attachmentsForm;

	private CheckBoxWithNullFix mandatoryAttachmentCheckBox;
	
	private FieldSet mandatoryAttachmentInStatesFieldSet;
	private WorkflowStatesSelectionPopupField mandatoryAttachmentStatesSelectionField;
	
	private FieldSet mandatoryAttachmentWhenMetadataHasValueFieldSet;
	private TextField<String> metadataNameInMandatoryAttachmentConditionTextField;
	private TextField<String> metadataValueInMandatoryAttachmentConditionTextField;
	
	private EditorGrid<MimeTypeModel> allowedAttachmentTypesEditGrid;
	private AdapterField allowedAttachmentTypesGridAdapterField;
	private LayoutContainer allowedAttachmentTypesGridContainer;

	public AttachmentsTab() {
		setText(GwtLocaleProvider.getConstants().ATTACHMENTS());
		setScrollMode(Scroll.AUTO);
		initForm();
	}

	private void initForm() {
		
		attachmentsForm = new FormPanel();
		attachmentsForm.setHeaderVisible(false);
		attachmentsForm.setLabelWidth(150);
		attachmentsForm.setBorders(false);
		attachmentsForm.setBodyBorder(false);
		
		initMandatoryAttachmentRelatedFormComponents();
		initAllowedAttachmentTypesRelatedFormComponents();


		add(attachmentsForm);
	}
	
	private void initMandatoryAttachmentRelatedFormComponents() {

		mandatoryAttachmentCheckBox = new CheckBoxWithNullFix();
		mandatoryAttachmentCheckBox.setFieldLabel(GwtLocaleProvider.getConstants().MANDATORY_ATTACHMENT());
		mandatoryAttachmentCheckBox.setBoxLabel("");
		mandatoryAttachmentCheckBox.addListener(Events.Change, new Listener<BaseEvent>() {
			
			@Override
			public void handleEvent(BaseEvent be) {

				boolean isChecked = ComponentUtils.isChecked(mandatoryAttachmentCheckBox);
				
				if (!isChecked) {
					mandatoryAttachmentInStatesFieldSet.setExpanded(false);
					mandatoryAttachmentWhenMetadataHasValueFieldSet.setExpanded(false);
				}
				
				mandatoryAttachmentInStatesFieldSet.setVisible(isChecked);
				mandatoryAttachmentWhenMetadataHasValueFieldSet.setVisible(isChecked);	
			}
		});
		
		initMandatoryAttachmentInStatesFieldSet();
		initMandatoryAttachmentWhenMetadataHasValueFieldSet();

		FormData formData = ComponentUtils.FORM_DATA_FOR_FIELDS_WITH_ALL_AVAILABLE_WIDTH;
		
		attachmentsForm.add(mandatoryAttachmentCheckBox, formData);
		attachmentsForm.add(mandatoryAttachmentInStatesFieldSet, formData);
		attachmentsForm.add(mandatoryAttachmentWhenMetadataHasValueFieldSet, formData);
	}
	
	private void initMandatoryAttachmentInStatesFieldSet() {
		
		mandatoryAttachmentInStatesFieldSet = new FieldSetWithFixes();
		mandatoryAttachmentInStatesFieldSet.setCheckboxToggle(true);
		mandatoryAttachmentInStatesFieldSet.setHeading(GwtLocaleProvider.getConstants().MANDATORY_ATTACHMENT_IN_STEPS());
		mandatoryAttachmentInStatesFieldSet.setLayout(new FormLayout());
		
		Listener<BaseEvent> expandOrCollapseListenerForMandatoryAttachmentInStatesFieldSet = new Listener<BaseEvent>() {
			
			@Override
			public void handleEvent(BaseEvent be) {
				
				boolean isExpanded = mandatoryAttachmentInStatesFieldSet.isExpanded();
				
				mandatoryAttachmentStatesSelectionField.setSelectionRequired(isExpanded);
				
				if (!isExpanded) {
					mandatoryAttachmentStatesSelectionField.resetSelectedItems();
				}
			}
		};
		mandatoryAttachmentInStatesFieldSet.addListener(Events.Expand, expandOrCollapseListenerForMandatoryAttachmentInStatesFieldSet);
		mandatoryAttachmentInStatesFieldSet.addListener(Events.Collapse, expandOrCollapseListenerForMandatoryAttachmentInStatesFieldSet);
		
		mandatoryAttachmentStatesSelectionField = new WorkflowStatesSelectionPopupField();
		mandatoryAttachmentStatesSelectionField.setFieldLabel(GwtLocaleProvider.getConstants().STATES());
		
		FormData formData = ComponentUtils.FORM_DATA_FOR_FIELDS_WITH_ALL_AVAILABLE_WIDTH;
		mandatoryAttachmentInStatesFieldSet.add(mandatoryAttachmentStatesSelectionField, formData);
	}
	
	private void initMandatoryAttachmentWhenMetadataHasValueFieldSet() {
		
		mandatoryAttachmentWhenMetadataHasValueFieldSet = new FieldSetWithFixes();
		mandatoryAttachmentWhenMetadataHasValueFieldSet.setCheckboxToggle(true);
		mandatoryAttachmentWhenMetadataHasValueFieldSet.setHeading(GwtLocaleProvider.getConstants().MANDATORY_ATTACHMENT_WHEN_METADATA_HAS_VALUE());
		mandatoryAttachmentWhenMetadataHasValueFieldSet.setLayout(new FormLayout());
		
		Listener<BaseEvent> expandOrCollapseListenerForMandatoryAttachmentWhenMetadataHasValueFieldSet = new Listener<BaseEvent>() {
			
			@Override
			public void handleEvent(BaseEvent be) {
				
				boolean isExpanded = mandatoryAttachmentWhenMetadataHasValueFieldSet.isExpanded();
				
				metadataNameInMandatoryAttachmentConditionTextField.setAllowBlank(!isExpanded);
				metadataValueInMandatoryAttachmentConditionTextField.setAllowBlank(!isExpanded);
				
				if (!isExpanded) {
					metadataNameInMandatoryAttachmentConditionTextField.setValue(null);
					metadataValueInMandatoryAttachmentConditionTextField.setValue(null);
				}
			}
		};
		mandatoryAttachmentWhenMetadataHasValueFieldSet.addListener(Events.Expand, expandOrCollapseListenerForMandatoryAttachmentWhenMetadataHasValueFieldSet);
		mandatoryAttachmentWhenMetadataHasValueFieldSet.addListener(Events.Collapse, expandOrCollapseListenerForMandatoryAttachmentWhenMetadataHasValueFieldSet);
		
		metadataNameInMandatoryAttachmentConditionTextField = new TextField<String>();
		metadataNameInMandatoryAttachmentConditionTextField.setFieldLabel(GwtLocaleProvider.getConstants().METADATA_NAME());
		
		metadataValueInMandatoryAttachmentConditionTextField = new TextField<String>();
		metadataValueInMandatoryAttachmentConditionTextField.setFieldLabel(GwtLocaleProvider.getConstants().METADATA_VALUE());

		FormData formData = ComponentUtils.FORM_DATA_FOR_FIELDS_WITH_ALL_AVAILABLE_WIDTH;
		
		mandatoryAttachmentWhenMetadataHasValueFieldSet.add(metadataNameInMandatoryAttachmentConditionTextField, formData);
		mandatoryAttachmentWhenMetadataHasValueFieldSet.add(metadataValueInMandatoryAttachmentConditionTextField, formData);
	}
	
	private void initAllowedAttachmentTypesRelatedFormComponents() {

		List<ColumnConfig> configs = new ArrayList<ColumnConfig>();

		ColumnConfig colName = new ColumnConfig();
		colName.setId(MimeTypeModel.PROPERTY_NAME);
		colName.setHeader(GwtLocaleProvider.getConstants().ATTACHMENT_NAME());
		colName.setMenuDisabled(true);
		colName.setWidth(300);
		configs.add(colName);

		ColumnConfig colExtension = new ColumnConfig();
		colExtension.setId(MimeTypeModel.PROPERTY_EXTENSION);
		colExtension.setHeader(GwtLocaleProvider.getConstants().ATTACHMENT_EXTENSION());
		colExtension.setMenuDisabled(true);
		colExtension.setWidth(100);
		configs.add(colExtension);

		CheckColumnConfig colAllowed = new CheckColumnConfig(MimeTypeModel.PROPERTY_ALLOWED, GwtLocaleProvider.getConstants().ATTACHMENT_ALLOWED(), 70);
		CellEditor checkBoxEditor = new CellEditor(new CheckBox());
		colAllowed.setEditor(checkBoxEditor);
		colAllowed.setWidth(50);
		colAllowed.setMenuDisabled(true);
		configs.add(colAllowed);

		ColumnModel model = new ColumnModel(configs);
		ListStore<MimeTypeModel> store = new ListStore<MimeTypeModel>();

		allowedAttachmentTypesEditGrid = new EditorGrid<MimeTypeModel>(store, model);
		GridView view = new GridView();
		view.setForceFit(true);
		allowedAttachmentTypesEditGrid.setView(view);
		allowedAttachmentTypesEditGrid.setBorders(false);
		allowedAttachmentTypesEditGrid.setAutoExpandColumn(MimeTypeModel.PROPERTY_NAME);
		allowedAttachmentTypesEditGrid.addPlugin(colAllowed);

		allowedAttachmentTypesGridContainer = new LayoutContainer();
		allowedAttachmentTypesGridContainer.setLayout(new FitLayout());
		allowedAttachmentTypesGridContainer.setHeight(300);
		allowedAttachmentTypesGridContainer.setBorders(false);
		allowedAttachmentTypesGridContainer.add(allowedAttachmentTypesEditGrid);

		allowedAttachmentTypesGridAdapterField = new AdapterField(allowedAttachmentTypesGridContainer);

		allowedAttachmentTypesGridAdapterField.setFieldLabel(GwtLocaleProvider.getConstants().ALLOWED_ATTACHMENT_TYPES());
		allowedAttachmentTypesGridAdapterField.setBorders(false);
		
		FormData formData = ComponentUtils.FORM_DATA_FOR_FIELDS_WITH_ALL_AVAILABLE_WIDTH;
		attachmentsForm.add(allowedAttachmentTypesGridAdapterField, formData);
	}

	private void reset() {
		
		// Resetam formularul.
		attachmentsForm.clear();
		
		mandatoryAttachmentStatesSelectionField.resetSelectedItems();
		mandatoryAttachmentStatesSelectionField.resetPossibleItems();
		
		// Goleste lista de atasamente.
		allowedAttachmentTypesEditGrid.getStore().removeAll();
	}

	public boolean isValid() {
		
		boolean isFormValid = attachmentsForm.isValid();
		if (!isFormValid) {
			ErrorHelper.addError(GwtLocaleProvider.getMessages().REQUIRED_FIELDS_NOT_COMPLETED_OR_VALUES_NOT_CORRECT());
			return false;
		}
		
		boolean hasToHaveAttachments = (mandatoryAttachmentCheckBox.getValue() != null) && (mandatoryAttachmentCheckBox.getValue().equals(Boolean.TRUE));
		boolean hasAttachmentTypesInList = allowedAttachmentTypesEditGrid.getStore().findModels(MimeTypeModel.PROPERTY_ALLOWED, new Boolean(true)).size() > 0;

		if (hasToHaveAttachments) {
			if (hasAttachmentTypesInList) {
				return true;
			} else {
				ErrorHelper.addError(GwtLocaleProvider.getMessages().NO_SELECTED_ALLOWED_ATTACHMENT_TYPES());
				return false;
			}
		} else {
			return true;
		}
	}

	private void populateAllowedAttachmentTypesGrid(final List<MimeTypeModel> allowedMimeTypes) {
		LoadingManager.get().loading();
		GwtServiceProvider.getMimeTypeService().getAllMimeTypes(new AsyncCallback<List<MimeTypeModel>>() {
			@Override
			public void onFailure(Throwable exception) {
				MessageUtils.displayError(exception);
				LoadingManager.get().loadingComplete();
			}
			@Override
			public void onSuccess(List<MimeTypeModel> mimeTypesList) {
				for (MimeTypeModel m : mimeTypesList) {
					m.setAllowed(true);
					// daca vine pe editare schimbam foaia
					if (allowedMimeTypes != null) {
						boolean found = false;
						for (MimeTypeModel mimeType : allowedMimeTypes) {
							if (mimeType.getId().longValue() == m.getId().longValue()) {
								found = true;
								break;
							}
						}
						m.setAllowed(found);
					}
				}
				allowedAttachmentTypesEditGrid.getStore().add(mimeTypesList);
				LoadingManager.get().loadingComplete();
			}
		});
	}

	public void prepareForAdd() {
		reset();
		populateAllowedAttachmentTypesGrid(null);
	}

	public void prepareForEdit(DocumentTypeModel documentType, List<WorkflowStateModel> workflowStates) {
		
		reset();
		
		mandatoryAttachmentCheckBox.setValue(documentType.isMandatoryAttachment());

		mandatoryAttachmentInStatesFieldSet.setExpanded(documentType.isMandatoryAttachmentInStates());
		mandatoryAttachmentStatesSelectionField.setPossibleItems(workflowStates);
		mandatoryAttachmentStatesSelectionField.setCodesForSelectedStatesAsString(documentType.getMandatoryAttachmentStates());
		
		mandatoryAttachmentWhenMetadataHasValueFieldSet.setExpanded(documentType.isMandatoryAttachmentWhenMetadataHasValue());
		metadataNameInMandatoryAttachmentConditionTextField.setValue(documentType.getMetadataNameInMandatoryAttachmentCondition());
		metadataValueInMandatoryAttachmentConditionTextField.setValue(documentType.getMetadataValueInMandatoryAttachmentCondition());
		
		populateAllowedAttachmentTypesGrid(documentType.getAllowedAttachmentTypes());

	}

	public void populate(DocumentTypeModel documentType) {
		
		documentType.setMandatoryAttachment(ComponentUtils.getCheckBoxValue(mandatoryAttachmentCheckBox));
		
		boolean mandatoryAttachmentInStates = mandatoryAttachmentInStatesFieldSet.isExpanded();
		documentType.setMandatoryAttachmentInStates(mandatoryAttachmentInStates);
		if (mandatoryAttachmentInStates) {
			documentType.setMandatoryAttachmentStates(mandatoryAttachmentStatesSelectionField.getCodesForSelectedStatesAsString());
		}
		
		boolean mandatoryAttachmentWhenMetadataHasValue = mandatoryAttachmentWhenMetadataHasValueFieldSet.isExpanded();
		documentType.setMandatoryAttachmentWhenMetadataHasValue(mandatoryAttachmentWhenMetadataHasValue);
		if (mandatoryAttachmentWhenMetadataHasValue) {
			documentType.setMetadataNameInMandatoryAttachmentCondition(metadataNameInMandatoryAttachmentConditionTextField.getValue());
			documentType.setMetadataValueInMandatoryAttachmentCondition(metadataValueInMandatoryAttachmentConditionTextField.getValue());
		}
		
		List<MimeTypeModel> allowedAttachmentTypes = allowedAttachmentTypesEditGrid.getStore().findModels(MimeTypeModel.PROPERTY_ALLOWED, new Boolean(true));
		documentType.setAllowedAttachmentTypes(allowedAttachmentTypes);
	}
}