package ro.cloudSoft.cloudDoc.presentation.client.admin.documenttypes;

import java.util.List;

import ro.cloudSoft.cloudDoc.presentation.client.admin.validators.MetadataDefaultValueValidator;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.bpm.WorkflowStateModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.content.AutoNumberMetadataDefinitionModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.content.ListMetadataDefinitionModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.content.ListMetadataItemModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.content.MetadataCollectionDefinitionModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.content.MetadataDefinitionModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.content.UserMetadataDefinitionModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.providers.GwtLocaleProvider;
import ro.cloudSoft.cloudDoc.presentation.client.shared.providers.ValidatorProvider;
import ro.cloudSoft.cloudDoc.presentation.client.shared.utils.ComponentUtils;
import ro.cloudSoft.cloudDoc.presentation.client.shared.utils.ErrorHelper;
import ro.cloudSoft.cloudDoc.presentation.client.shared.utils.GwtBooleanUtils;
import ro.cloudSoft.cloudDoc.presentation.client.shared.utils.GwtCompareUtils;
import ro.cloudSoft.cloudDoc.presentation.client.shared.utils.PropertyEditors;
import ro.cloudSoft.cloudDoc.presentation.client.shared.validators.CompositeValidator;
import ro.cloudSoft.cloudDoc.presentation.client.shared.validators.MetadataDefinitionNameValidator;
import ro.cloudSoft.cloudDoc.presentation.client.shared.validators.MetadataDefinitionNameValidator.MetadataDefinitionNameValidatorHelper;
import ro.cloudSoft.cloudDoc.presentation.client.shared.widgets.CheckBoxWithNullFix;
import ro.cloudSoft.cloudDoc.presentation.client.shared.widgets.FieldSetWithFixes;
import ro.cloudSoft.cloudDoc.presentation.client.shared.widgets.MetadataTypeComboBox;
import ro.cloudSoft.cloudDoc.presentation.client.shared.widgets.WorkflowStatesSelectionPopupField;
import ro.cloudSoft.cloudDoc.presentation.client.shared.widgets.bpm.WorkflowStateComboBox;
import ro.cloudSoft.cloudDoc.presentation.client.shared.widgets.organization.GroupComboBox;

import com.extjs.gxt.ui.client.Style.Scroll;
import com.extjs.gxt.ui.client.event.BaseEvent;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.form.CheckBox;
import com.extjs.gxt.ui.client.widget.form.FormPanel;
import com.extjs.gxt.ui.client.widget.form.HiddenField;
import com.extjs.gxt.ui.client.widget.form.NumberField;
import com.extjs.gxt.ui.client.widget.form.TextField;
import com.extjs.gxt.ui.client.widget.layout.FormData;
import com.extjs.gxt.ui.client.widget.layout.FormLayout;

public class MetadataDefinitionAddOrEditForm extends FormPanel {
	
	private final MetadataDefinitionsParentType metadataDefinitionsParentType;
	private final ParentOfMetadataDefinitionAddOrEditForm parentOfForm;
	
	private HiddenField<Long> idHiddenField;
	private TextField<String> nameTextField;
	private TextField<String> labelTextField;
	private CheckBox mandatoryCheckBox;
	private CheckBox restrictedOnEditCheckBox;
	private WorkflowStatesSelectionPopupField mandatoryInStepsSelectionField;
	private WorkflowStatesSelectionPopupField restrictedOnEditInStepsSelectionField;
	private CheckBox significantCheckBox;
	private CheckBox indexedCheckBox;
	private NumberField orderNumberField;
	private MetadataTypeComboBox typeComboBox;
	private TextField<String> defaultValueTextField;
	
	private FieldSetWithFixes autoNumberFieldSet;
	private FieldSetWithFixes userFieldSet;
	private FieldSetWithFixes listFieldSet;
	
	private TextField<String> prefixField;
	private NumberField numberLengthField;
	
	private CheckBox onlyUsersFromGroupCheckBox;
	private GroupComboBox groupOfPermittedUsers;
	private CheckBoxWithNullFix autoCompleteWithCurrentUserCheckBox;
	private WorkflowStateComboBox autoCompleteWithCurrentUserStateComboBox;
	
	private CheckBox multipleSelectionCheckBox;
	private CheckBox extendableCheckBox;
	private ListMetadataItemsField listItemsField;
	
	private Button saveButton;
	private Button cancelButton;

	public MetadataDefinitionAddOrEditForm(MetadataDefinitionsParentType metadataDefinitionsParentType, ParentOfMetadataDefinitionAddOrEditForm parentOfForm) {
		
		this.metadataDefinitionsParentType = metadataDefinitionsParentType;
		this.parentOfForm = parentOfForm;
		
		initForm();
	}
	
	private void initForm() {
		
		setHeaderVisible(false);
		setLabelWidth(120);
		setFieldWidth(315);
		setScrollMode(Scroll.AUTO);

		initFields();
		initButtons();
	}
	
	private void initFields() {
		
		FormData formData = new FormData("90%");
		
		idHiddenField = new HiddenField<Long>();
		idHiddenField.setPropertyEditor(PropertyEditors.LONG);
		add(idHiddenField, formData);
		
		nameTextField = new TextField<String>();
		nameTextField.setName(MetadataDefinitionModel.PROPERTY_NAME);
		nameTextField.setFieldLabel(GwtLocaleProvider.getConstants().NAME());
		nameTextField.setAllowBlank(false);
		nameTextField.setMaxLength(MetadataDefinitionModel.LENGTH_NAME);
		nameTextField.setValidator(new CompositeValidator(ValidatorProvider.getRequiredFieldValidator(), new MetadataDefinitionNameValidator(parentOfForm)));
		add(nameTextField, formData);
		
		labelTextField = new TextField<String>();
		labelTextField.setName(MetadataDefinitionModel.PROPERTY_LABEL);
		labelTextField.setFieldLabel(GwtLocaleProvider.getConstants().LABEL());
		labelTextField.setAllowBlank(false);
		labelTextField.setMaxLength(MetadataDefinitionModel.LENGTH_LABEL);
		add(labelTextField, formData);
		
		typeComboBox = null;
		if (metadataDefinitionsParentType.equals(MetadataDefinitionsParentType.DOCUMENT_TYPE)) {
			typeComboBox = MetadataTypeComboBox.getInstance();
		} else if (metadataDefinitionsParentType.equals(MetadataDefinitionsParentType.COLLECTION)) {
			typeComboBox = MetadataTypeComboBox.getInstanceForCollection();
		} else {
			throw new IllegalArgumentException("Tip necunoscut de parinte pentru definitii de metadate: [" + metadataDefinitionsParentType + "]");
		}
		typeComboBox.setFieldLabel(GwtLocaleProvider.getConstants().TYPE());
		typeComboBox.setAllowBlank(false);
		typeComboBox.addListener(Events.SelectionChange, new Listener<BaseEvent>() {
			
			@Override
			public void handleEvent(BaseEvent be) {
				String selectedType = typeComboBox.getSelectedType();
				changeFormPerspectiveByMetadataType(selectedType);				
			}
		});
		add(typeComboBox, formData);
		
		mandatoryCheckBox = new CheckBox();
		mandatoryCheckBox.setName(MetadataDefinitionModel.PROPERTY_MANDATORY);
		mandatoryCheckBox.setFieldLabel(GwtLocaleProvider.getConstants().MANDATORY());
		mandatoryCheckBox.setBoxLabel("");
		mandatoryCheckBox.addListener(Events.Change, new Listener<BaseEvent>() {
			
			@Override
			public void handleEvent(BaseEvent be) {
				boolean isChecked = ComponentUtils.isChecked(mandatoryCheckBox);
				mandatoryInStepsSelectionField.setVisible(isChecked);
				if (!isChecked) {
					mandatoryInStepsSelectionField.resetSelectedItems();
				}
			}			
		});
		add(mandatoryCheckBox, formData);
		
		mandatoryInStepsSelectionField = new WorkflowStatesSelectionPopupField();
		mandatoryInStepsSelectionField.setFieldLabel(GwtLocaleProvider.getConstants().MANDATORY_IN_STEPS());
		add(mandatoryInStepsSelectionField, formData);
		
		restrictedOnEditCheckBox = new CheckBox();
		restrictedOnEditCheckBox.setName(MetadataDefinitionModel.PROPERTY_RESTRICTED_ON_EDIT);
		restrictedOnEditCheckBox.setFieldLabel(GwtLocaleProvider.getConstants().EDIT_RESTRICTION());
		restrictedOnEditCheckBox.setBoxLabel("");
		restrictedOnEditCheckBox.addListener(Events.Change, new Listener<BaseEvent>() {
			
			@Override
			public void handleEvent(BaseEvent be) {
				boolean isChecked = ComponentUtils.isChecked(restrictedOnEditCheckBox);
				restrictedOnEditInStepsSelectionField.setVisible(isChecked);
				restrictedOnEditInStepsSelectionField.setSelectionRequired(isChecked);
				if (!isChecked) {
					restrictedOnEditInStepsSelectionField.resetSelectedItems();
				}				
			}			
		});
		add(restrictedOnEditCheckBox, formData);
		
		restrictedOnEditInStepsSelectionField = new WorkflowStatesSelectionPopupField();
		restrictedOnEditInStepsSelectionField.setFieldLabel(GwtLocaleProvider.getConstants().RESTRICTED_IN_STEPS());
		add(restrictedOnEditInStepsSelectionField, formData);
		
		significantCheckBox = new CheckBox();
		significantCheckBox.setName(MetadataDefinitionModel.PROPERTY_REPRESENTATIVE);
		significantCheckBox.setFieldLabel(GwtLocaleProvider.getConstants().SIGNIFICANT());
		significantCheckBox.setBoxLabel("");
		if (metadataDefinitionsParentType.equals(MetadataDefinitionsParentType.DOCUMENT_TYPE)) {
			add(significantCheckBox, formData);
		}
		
		indexedCheckBox = new CheckBox();
		indexedCheckBox.setName(MetadataDefinitionModel.PROPERTY_INDEXED);
		indexedCheckBox.setFieldLabel(GwtLocaleProvider.getConstants().INDEXED());
		indexedCheckBox.setBoxLabel("");
		add(indexedCheckBox, formData);
		
		orderNumberField = new NumberField();
		orderNumberField.setName(MetadataDefinitionModel.PROPERTY_ORDER_NUMBER);
		orderNumberField.setFieldLabel(GwtLocaleProvider.getConstants().ORDER_NUMBER());
		orderNumberField.setAllowNegative(false);
		orderNumberField.setAllowDecimals(false);
		add(orderNumberField, formData);
		
		autoNumberFieldSet = new FieldSetWithFixes();
		autoNumberFieldSet.setHeading(GwtLocaleProvider.getConstants().AUTONUMBER());
		autoNumberFieldSet.setLayout(new FormLayout());
		add(autoNumberFieldSet);
		
		userFieldSet = new FieldSetWithFixes();
		userFieldSet.setHeading(GwtLocaleProvider.getConstants().USER());
		userFieldSet.setLayout(new FormLayout());
		if (metadataDefinitionsParentType.equals(MetadataDefinitionsParentType.DOCUMENT_TYPE)) {
			add(userFieldSet);
		}
		
		listFieldSet = new FieldSetWithFixes();
		listFieldSet.setHeading(GwtLocaleProvider.getConstants().LIST());
		listFieldSet.setLayout(new FormLayout());
		add(listFieldSet);
		
		defaultValueTextField = new TextField<String>();
		defaultValueTextField.setFieldLabel(GwtLocaleProvider.getConstants().DEFAULT_VALUE());
		defaultValueTextField.setValidator(new MetadataDefaultValueValidator(this));
		if (metadataDefinitionsParentType.equals(MetadataDefinitionsParentType.DOCUMENT_TYPE)) {
			add(defaultValueTextField, formData);
		}
		
		initAutoNumberRelatedFields();
		initUserRelatedFields();
		initListRelatedFields();
	}
	
	private void initAutoNumberRelatedFields() {
		
		FormData formData = new FormData("90%");
		
		prefixField = new TextField<String>();
		prefixField.setFieldLabel(GwtLocaleProvider.getConstants().PREFIX());	
		prefixField.setAllowBlank(false);
		autoNumberFieldSet.add(prefixField, formData);
		
		numberLengthField = new NumberField();
		numberLengthField.setFieldLabel(GwtLocaleProvider.getConstants().NUMBER_LENGTH());	
		numberLengthField.setAllowBlank(false);
		autoNumberFieldSet.add(numberLengthField, formData);
	}
	
	private void initUserRelatedFields() {
		
		FormData formData = new FormData("90%");
		
		onlyUsersFromGroupCheckBox = new CheckBox();
		onlyUsersFromGroupCheckBox.setBoxLabel("");
		onlyUsersFromGroupCheckBox.setFieldLabel(GwtLocaleProvider.getConstants().ONLY_USERS_FROM_A_GROUP());
		onlyUsersFromGroupCheckBox.addListener(Events.Change, new Listener<BaseEvent>() {
			
			@Override
			public void handleEvent(BaseEvent be) {
				boolean isChecked = ComponentUtils.getCheckBoxValue(onlyUsersFromGroupCheckBox);
				groupOfPermittedUsers.setVisible(isChecked);
				groupOfPermittedUsers.setAllowBlank(!isChecked);
				if (!isChecked) {
					groupOfPermittedUsers.setValue(null);
				}
			}
		});
		userFieldSet.add(onlyUsersFromGroupCheckBox, formData);
		
		groupOfPermittedUsers = new GroupComboBox();
		groupOfPermittedUsers.setFieldLabel(GwtLocaleProvider.getConstants().GROUP());
		userFieldSet.add(groupOfPermittedUsers, formData);
		
		autoCompleteWithCurrentUserCheckBox = new CheckBoxWithNullFix();
		autoCompleteWithCurrentUserCheckBox.setBoxLabel("");
		autoCompleteWithCurrentUserCheckBox.setFieldLabel(GwtLocaleProvider.getConstants().AUTO_COMPLETE_WITH_CURRENT_USER());
		autoCompleteWithCurrentUserCheckBox.addListener(Events.Change, new Listener<BaseEvent>() {
			
			@Override
			public void handleEvent(BaseEvent be) {
				boolean isChecked = autoCompleteWithCurrentUserCheckBox.isChecked();
				autoCompleteWithCurrentUserStateComboBox.setVisible(isChecked);
				autoCompleteWithCurrentUserStateComboBox.setAllowBlank(!isChecked);
				if (!isChecked) {
					autoCompleteWithCurrentUserStateComboBox.resetSelectedState();
				}
			}
		});
		userFieldSet.add(autoCompleteWithCurrentUserCheckBox, formData);
		
		autoCompleteWithCurrentUserStateComboBox = new WorkflowStateComboBox();
		autoCompleteWithCurrentUserStateComboBox.setFieldLabel(GwtLocaleProvider.getConstants().AUTO_COMPLETE_WITH_CURRENT_USER_STEP());
		userFieldSet.add(autoCompleteWithCurrentUserStateComboBox, formData);
	}
	
	private void initListRelatedFields() {
		
		FormData formData = new FormData("90%");
		
		multipleSelectionCheckBox = new CheckBox();
		multipleSelectionCheckBox.setFieldLabel(GwtLocaleProvider.getConstants().MULTIPLE_SELECTION());
		multipleSelectionCheckBox.setBoxLabel("");
		if (metadataDefinitionsParentType.equals(MetadataDefinitionsParentType.DOCUMENT_TYPE)) {
			listFieldSet.add(multipleSelectionCheckBox, formData);
		}
		
		extendableCheckBox =  new CheckBox();
		extendableCheckBox.setFieldLabel(GwtLocaleProvider.getConstants().EXTENDABLE());
		extendableCheckBox.setBoxLabel("");
		if (metadataDefinitionsParentType.equals(MetadataDefinitionsParentType.DOCUMENT_TYPE)) {
			listFieldSet.add(extendableCheckBox, formData);
		}

		listItemsField = new ListMetadataItemsField();
		listItemsField.setFieldLabel(GwtLocaleProvider.getConstants().ALLOWED_VALUES());
		listFieldSet.add(listItemsField, formData);
	}
	
	private void initButtons() {
		
		saveButton = new Button();
		saveButton.setText(GwtLocaleProvider.getConstants().ADD());
		getButtonBar().add(saveButton);
		
		cancelButton = new Button();
		cancelButton.setText(GwtLocaleProvider.getConstants().CANCEL());
		getButtonBar().add(cancelButton);
		
		addButtonActions();
	}
	
	private void addButtonActions() {
		saveButton.addSelectionListener(new SelectionListener<ButtonEvent>() {
			
			@Override
			public void componentSelected(ButtonEvent be) {
				
				if (!isFormValid()) {
					ErrorHelper.displayErrors();
					return;
				}
				
				MetadataDefinitionModel metadataDefinition = getMetadataDefinitionFromForm();
				parentOfForm.onAddedOrEditedMetadataDefinition(metadataDefinition);
			}
		});
		cancelButton.addSelectionListener(new SelectionListener<ButtonEvent>() {
			
			@Override
			public void componentSelected(ButtonEvent be) {
				parentOfForm.onCancelAddOrEditMetadataDefinition();
			}
		});
	}
	
	private void changeFormPerspectiveByMetadataType(String metadataType) {
		updateAutoNumberTypeSpecificFieldsPerspective(metadataType);
		updateListTypeSpecificFieldsPerspective(metadataType);
		updateUserTypeSpecificFieldsPerspective(metadataType);
		updateCollectionTypeSpecificFieldsPerspective(metadataType);		
	}
	
	private void updateAutoNumberTypeSpecificFieldsPerspective(String metadataType) {
		
		boolean isAutoNumberType = GwtCompareUtils.areEqual(metadataType, MetadataDefinitionModel.TYPE_AUTO_NUMBER);
		boolean isNotAutoNumberType = (!isAutoNumberType);
		
		autoNumberFieldSet.setVisible(isAutoNumberType);
		
		prefixField.setAllowBlank(isNotAutoNumberType);
		numberLengthField.setAllowBlank(isNotAutoNumberType);
	}

	private void updateListTypeSpecificFieldsPerspective(String metadataType) {

		boolean isListType = GwtCompareUtils.areEqual(metadataType, MetadataDefinitionModel.TYPE_LIST);
		
		listFieldSet.setVisible(isListType);
		
		listItemsField.setItemsRequired(isListType);
	}
	
	private void updateUserTypeSpecificFieldsPerspective(String metadataType) {

		boolean isUserType = GwtCompareUtils.areEqual(metadataType, MetadataDefinitionModel.TYPE_USER);
		
		userFieldSet.setVisible(isUserType);
	}
	
	private void updateCollectionTypeSpecificFieldsPerspective(String metadataType) {

		boolean isCollectionType = GwtCompareUtils.areEqual(metadataType, MetadataDefinitionModel.TYPE_METADATA_COLLECTION);
		boolean isNotCollectionType = (!isCollectionType);
			
		significantCheckBox.setVisible(isNotCollectionType);
		indexedCheckBox.setVisible(isNotCollectionType);
		defaultValueTextField.setVisible(isNotCollectionType);
	}
	
	public void resetWorkflowStates() {
		mandatoryInStepsSelectionField.resetPossibleItems();
		restrictedOnEditInStepsSelectionField.resetPossibleItems();
		autoCompleteWithCurrentUserStateComboBox.resetPossibleStates();
	}
	
	public void setWorkflowStates(List<WorkflowStateModel> workflowStates) {
		mandatoryInStepsSelectionField.setPossibleItems(workflowStates);
		restrictedOnEditInStepsSelectionField.setPossibleItems(workflowStates);
		autoCompleteWithCurrentUserStateComboBox.setPossibleStates(workflowStates);
	}
	
	public void prepareForAdd() {
		
		resetForm();
		reValidate();
		
		saveButton.setText(GwtLocaleProvider.getConstants().ADD());
	}
	
	public void prepareForEdit(MetadataDefinitionModel metadataDefinition) {
		
		resetForm();
		populateForm(metadataDefinition);
		reValidate();
		
		saveButton.setText(GwtLocaleProvider.getConstants().SAVE_METADATA());
	}
	
	public void resetForm() {
		
		clear();
		
		mandatoryInStepsSelectionField.resetSelectedItems();
		restrictedOnEditInStepsSelectionField.resetSelectedItems();
		
		listItemsField.removeAllListItems();

		ComponentUtils.clearValidationMessages(this);
	}
	
	private void populateForm(MetadataDefinitionModel metadataDefinition) {
		populateCommonPropertiesInForm(metadataDefinition);
		populateTypeSpecificPropertiesInForm(metadataDefinition);
	}
	
	private void populateCommonPropertiesInForm(MetadataDefinitionModel metadataDefinition) {
		
		idHiddenField.setValue(metadataDefinition.getId());
		
		nameTextField.setValue(metadataDefinition.getName());
		labelTextField.setValue(metadataDefinition.getLabel());
		
		typeComboBox.setSelectedType(metadataDefinition.getType());
		
		boolean mandatory = GwtBooleanUtils.isTrue(metadataDefinition.isMandatory());
		mandatoryCheckBox.setValue(mandatory);
		if (mandatory) {
			mandatoryInStepsSelectionField.setCodesForSelectedStatesAsString(metadataDefinition.getMandatoryStates());
		}
		
		boolean restrictedOnEdit = GwtBooleanUtils.isTrue(metadataDefinition.isRestrictedOnEdit());
		restrictedOnEditCheckBox.setValue(restrictedOnEdit);
		if (restrictedOnEdit) {
			restrictedOnEditInStepsSelectionField.setCodesForSelectedStatesAsString(metadataDefinition.getRestrictedOnEditStates());
		}
		
		significantCheckBox.setValue(metadataDefinition.isRepresentative());
		indexedCheckBox.setValue(metadataDefinition.isIndexed());
		orderNumberField.setValue(metadataDefinition.getOrderNumber());
		defaultValueTextField.setValue(metadataDefinition.getDefaultValue());
	}
	
	private void populateTypeSpecificPropertiesInForm(MetadataDefinitionModel metadataDefinition) {
		if (metadataDefinition.getType().equals(MetadataDefinitionModel.TYPE_AUTO_NUMBER)) {
			populateAutoNumberTypeSpecificPropertiesInForm((AutoNumberMetadataDefinitionModel) metadataDefinition);
		} else if (metadataDefinition.getType().equals(MetadataDefinitionModel.TYPE_LIST)) {
			populateListTypeSpecificPropertiesInForm((ListMetadataDefinitionModel) metadataDefinition);
		} else if (metadataDefinition.getType().equals(MetadataDefinitionModel.TYPE_USER)) {
			populateUserTypeSpecificPropertiesInForm((UserMetadataDefinitionModel) metadataDefinition);
		}
	}
	
	private void populateAutoNumberTypeSpecificPropertiesInForm(AutoNumberMetadataDefinitionModel metadataDefinition) {
		prefixField.setValue(metadataDefinition.getPrefix());
		numberLengthField.setValue(metadataDefinition.getNumberLength());
	}

	private void populateListTypeSpecificPropertiesInForm(ListMetadataDefinitionModel metadataDefinition) {
		extendableCheckBox.setValue(metadataDefinition.isExtendable());
		multipleSelectionCheckBox.setValue(metadataDefinition.isMultipleSelection());
		listItemsField.setListItems(metadataDefinition.getListItems());	
	}
	
	private void populateUserTypeSpecificPropertiesInForm(UserMetadataDefinitionModel metadataDefinition) {
		
		onlyUsersFromGroupCheckBox.setValue(metadataDefinition.isOnlyUsersFromGroup());
		groupOfPermittedUsers.setSelectedGroupById(metadataDefinition.getIdOfGroupOfPermittedUsers());
		
		autoCompleteWithCurrentUserCheckBox.setChecked(metadataDefinition.isAutoCompleteWithCurrentUser());
		autoCompleteWithCurrentUserStateComboBox.setSelectedStateByCode(metadataDefinition.getAutoCompleteWithCurrentUserStateCode());
	}
	
	private void reValidate() {
		ComponentUtils.reValidateForm(this);
	}
	
	private boolean isFormValid() {
		// Validarea se face individual, fiecare camp avand logica de validare necesara.
		return isValid();
	}
	
	private MetadataDefinitionModel getMetadataDefinitionFromForm() {
		MetadataDefinitionModel metadataDefinition = getMetadataDefinitionWithTypeSpecificPropertiesFromForm();
		setCommonPropertiesToMetadataDefinitionFromForm(metadataDefinition);
		return metadataDefinition;
	}
	
	private MetadataDefinitionModel getMetadataDefinitionWithTypeSpecificPropertiesFromForm() {
		
		String type = getSelectedType();
		if (type == null) {
			throw new IllegalStateException("S-a incercat luarea definitiei de metadata din formular, insa NU s-a completat tipul.");
		}
		
		if (type.equals(MetadataDefinitionModel.TYPE_TEXT)) {
			MetadataDefinitionModel metadataDefinition = new MetadataDefinitionModel();
			return metadataDefinition;
		} else if (type.equals(MetadataDefinitionModel.TYPE_NUMERIC)) {
			MetadataDefinitionModel metadataDefinition = new MetadataDefinitionModel();
			return metadataDefinition;
		} else if (type.equals(MetadataDefinitionModel.TYPE_AUTO_NUMBER)) {
			AutoNumberMetadataDefinitionModel metadataDefinition = new AutoNumberMetadataDefinitionModel();
			setAutoNumberTypeSpecificPropertiesFromForm(metadataDefinition);
			return metadataDefinition;
		} else if (type.equals(MetadataDefinitionModel.TYPE_DATE)) {
			MetadataDefinitionModel metadataDefinition = new MetadataDefinitionModel();
			return metadataDefinition;
		} else if (type.equals(MetadataDefinitionModel.TYPE_LIST)) {
			ListMetadataDefinitionModel metadataDefinition = new ListMetadataDefinitionModel();
			setListTypeSpecificPropertiesFromForm(metadataDefinition);
			return metadataDefinition;
		} else if (type.equals(MetadataDefinitionModel.TYPE_USER)) {
			UserMetadataDefinitionModel metadataDefinition = new UserMetadataDefinitionModel();
			setUserTypeSpecificPropertiesFromForm(metadataDefinition);
			return metadataDefinition;
		} else if (type.equals(MetadataDefinitionModel.TYPE_TEXT_AREA)) {
			MetadataDefinitionModel metadataDefinition = new MetadataDefinitionModel();
			return metadataDefinition;
		} else if (type.equals(MetadataDefinitionModel.TYPE_METADATA_COLLECTION)) {
			MetadataCollectionDefinitionModel collectionDefinition = new MetadataCollectionDefinitionModel();
			return collectionDefinition;
		} else {
			throw new IllegalStateException("Tip necunoscut de metadata: [" + type + "]");
		}
	}
	
	private void setAutoNumberTypeSpecificPropertiesFromForm(AutoNumberMetadataDefinitionModel metadataDefinition) {
		metadataDefinition.setPrefix(prefixField.getValue());
		metadataDefinition.setNumberLength(ComponentUtils.getIntegerFromField(numberLengthField));
	}
	
	private void setListTypeSpecificPropertiesFromForm(ListMetadataDefinitionModel metadataDefinition) {
		metadataDefinition.setMultipleSelection(ComponentUtils.getCheckBoxValue(multipleSelectionCheckBox));
		metadataDefinition.setExtendable(ComponentUtils.getCheckBoxValue(multipleSelectionCheckBox));
		metadataDefinition.setListItems(listItemsField.getListItems());
	}
	
	private void setUserTypeSpecificPropertiesFromForm(UserMetadataDefinitionModel metadataDefinition) {
		
		boolean onlyUsersFromGroup = ComponentUtils.getCheckBoxValue(onlyUsersFromGroupCheckBox);
		metadataDefinition.setOnlyUsersFromGroup(onlyUsersFromGroup);
		
		if (onlyUsersFromGroup) {
			String idOfGroupOfPermittedUsers = groupOfPermittedUsers.getIdOfSelectedGroup();
			metadataDefinition.setIdOfGroupOfPermittedUsers(idOfGroupOfPermittedUsers);
		}
		
		boolean autoCompleteWithCurrentUser = autoCompleteWithCurrentUserCheckBox.isChecked();
		metadataDefinition.setAutoCompleteWithCurrentUser(autoCompleteWithCurrentUser);
		
		if (autoCompleteWithCurrentUser) {
			String autoCompleteWithCurrentUserStateCode = autoCompleteWithCurrentUserStateComboBox.getCodeOfSelectedState();
			metadataDefinition.setAutoCompleteWithCurrentUserStateCode(autoCompleteWithCurrentUserStateCode);
		}
	}
	
	private void setCommonPropertiesToMetadataDefinitionFromForm(MetadataDefinitionModel metadataDefinition) {
		
		metadataDefinition.setId(idHiddenField.getValue());
		
		metadataDefinition.setName(nameTextField.getValue());
		metadataDefinition.setLabel(labelTextField.getValue());
		
		metadataDefinition.setType(typeComboBox.getSelectedType());
		
		boolean mandatory = ComponentUtils.getCheckBoxValue(mandatoryCheckBox);
		metadataDefinition.setMandatory(mandatory);
		if (mandatory) {
			metadataDefinition.setMandatoryStates(mandatoryInStepsSelectionField.getCodesForSelectedStatesAsString());
		}
		
		boolean restrictedOnEdit = ComponentUtils.getCheckBoxValue(restrictedOnEditCheckBox);
		metadataDefinition.setRestrictedOnEdit(restrictedOnEdit);
		if (restrictedOnEdit) {
			metadataDefinition.setRestrictedOnEditStates(restrictedOnEditInStepsSelectionField.getCodesForSelectedStatesAsString());
		}
		
		metadataDefinition.setIndexed(ComponentUtils.getCheckBoxValue(indexedCheckBox));
		metadataDefinition.setRepresentative(ComponentUtils.getCheckBoxValue(significantCheckBox));
		metadataDefinition.setOrderNumber(ComponentUtils.getIntegerFromField(orderNumberField));
			
		metadataDefinition.setDefaultValue(defaultValueTextField.getValue());
	}
	
	public String getSelectedType() {
		return typeComboBox.getSelectedType();
	}
	
	public List<ListMetadataItemModel> getListItems() {
		return listItemsField.getListItems();
	}
	
	public static enum MetadataDefinitionsParentType {
		DOCUMENT_TYPE, COLLECTION
	}
	
	public static interface ParentOfMetadataDefinitionAddOrEditForm extends MetadataDefinitionNameValidatorHelper {
		
		void onAddedOrEditedMetadataDefinition(MetadataDefinitionModel savedMetadataDefinition);
		
		void onCancelAddOrEditMetadataDefinition();
	}
}