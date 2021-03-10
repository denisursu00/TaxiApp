package ro.cloudSoft.cloudDoc.presentation.client.client.document;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ro.cloudSoft.cloudDoc.presentation.client.client.document.metadata.AutoNumberMetadataField;
import ro.cloudSoft.cloudDoc.presentation.client.client.document.metadata.CollectionField;
import ro.cloudSoft.cloudDoc.presentation.client.client.document.metadata.DateMetadataField;
import ro.cloudSoft.cloudDoc.presentation.client.client.document.metadata.ListMetadataComboField;
import ro.cloudSoft.cloudDoc.presentation.client.client.document.metadata.ListMetadataPopupField;
import ro.cloudSoft.cloudDoc.presentation.client.client.document.metadata.MetadataField;
import ro.cloudSoft.cloudDoc.presentation.client.client.document.metadata.MetadataFieldHelper;
import ro.cloudSoft.cloudDoc.presentation.client.client.document.metadata.NumericMetadataField;
import ro.cloudSoft.cloudDoc.presentation.client.client.document.metadata.TextAreaMetadataField;
import ro.cloudSoft.cloudDoc.presentation.client.client.document.metadata.TextMetadataField;
import ro.cloudSoft.cloudDoc.presentation.client.client.document.metadata.UserMetadataField;
import ro.cloudSoft.cloudDoc.presentation.client.client.document.metadata.UserMetadataFieldFactory;
import ro.cloudSoft.cloudDoc.presentation.client.client.events.listeners.InlocuitorOptionUpdaterEventListener;
import ro.cloudSoft.cloudDoc.presentation.client.shared.constants.GwtCerereDeConcediuConstants;
import ro.cloudSoft.cloudDoc.presentation.client.shared.constants.GwtCereriDeConcediuConstants;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.bpm.WorkflowModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.bpm.WorkflowStateModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.content.CollectionInstanceModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.content.DocumentModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.content.DocumentTypeModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.content.ListMetadataDefinitionModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.content.MetadataCollectionDefinitionModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.content.MetadataCollectionInstanceModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.content.MetadataDefinitionModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.content.MetadataInstanceModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.content.UserMetadataDefinitionModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.providers.GwtLocaleProvider;
import ro.cloudSoft.cloudDoc.presentation.client.shared.utils.ComponentUtils;
import ro.cloudSoft.cloudDoc.presentation.client.shared.utils.ErrorHelper;
import ro.cloudSoft.cloudDoc.presentation.client.shared.utils.GwtBusinessUtils;
import ro.cloudSoft.cloudDoc.presentation.client.shared.utils.GwtRegistryUtils;
import ro.cloudSoft.cloudDoc.presentation.client.shared.utils.PropertyEditors;
import ro.cloudSoft.cloudDoc.presentation.client.shared.utils.WordLengthBasedFieldLabelWidthCalculationStrategy;
import ro.cloudSoft.cloudDoc.presentation.client.shared.utils.business.content.GwtMandatoryAttachmentBusinessHelper.MetadataValueByNameProvider;
import ro.cloudSoft.cloudDoc.presentation.client.shared.utils.business.content.GwtMetadataOrCollectionBusinessUtils;

import com.extjs.gxt.ui.client.Style.Scroll;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.form.Field;
import com.extjs.gxt.ui.client.widget.form.FieldSet;
import com.extjs.gxt.ui.client.widget.form.FormPanel;
import com.extjs.gxt.ui.client.widget.form.HiddenField;
import com.extjs.gxt.ui.client.widget.form.TextArea;
import com.extjs.gxt.ui.client.widget.form.TextField;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.extjs.gxt.ui.client.widget.layout.FormData;
import com.extjs.gxt.ui.client.widget.layout.FormLayout;

public class DocumentGeneralPanel extends ContentPanel implements MetadataValueByNameProvider {
	
	private static final int DEFAULT_FORM_LABEL_WIDTH_IN_PIXELS = 100;
	
	// elementele "ajutatoare"
	private Map<Long, MetadataField> metadataFieldMap;
	private Map<Long, CollectionField> collectionFieldMap;
	private WorkflowStateModel currentState;
	// elementele principale
	private FormPanel formPanel;
	// elementele din "formPanel"
	private HiddenField<String> documentIdHiddenField;
	private HiddenField<String> documentTypeIdHiddenField;
	private HiddenField<Boolean> keepAllVersionsHiddenField;
	private HiddenField<String> documentLocationRealNameHiddenField;
	private HiddenField<String> parentFolderIdHiddenField;
	private HiddenField<Boolean> hasStableVersionHiddenField;
	private HiddenField<Long> workflowStateIdHiddenField;
	private HiddenField<String> authorHiddenField;
	private HiddenField<Date> createdDateHiddenField;
	private TextField<String> documentNameTextField;
	private TextArea documentDescriptionTextField;
	private FieldSet metadataFieldSet;
	private FieldSet collectionFieldSet;
	
	public DocumentGeneralPanel() {
		
		setHeaderVisible(false);
		setLayout(new FitLayout());
		
		metadataFieldMap = new HashMap<Long, MetadataField>();
		collectionFieldMap = new HashMap<Long, CollectionField>();
		
		formPanel = new FormPanel();
		formPanel.setBodyBorder(false);
		formPanel.setHeaderVisible(false);
		formPanel.setScrollMode(Scroll.AUTO);
		
		FormData formData = ComponentUtils.FORM_DATA_FOR_FIELDS_WITH_ALL_AVAILABLE_WIDTH;
		
		documentIdHiddenField = new HiddenField<String>();
		formPanel.add(documentIdHiddenField);
		
		documentTypeIdHiddenField = new HiddenField<String>();
		formPanel.add(documentTypeIdHiddenField);
		
		keepAllVersionsHiddenField = new HiddenField<Boolean>();
		keepAllVersionsHiddenField.setPropertyEditor(PropertyEditors.BOOLEAN);
		formPanel.add(keepAllVersionsHiddenField);
		
		documentLocationRealNameHiddenField = new HiddenField<String>();
		formPanel.add(documentLocationRealNameHiddenField);
		
		parentFolderIdHiddenField = new HiddenField<String>();
		formPanel.add(parentFolderIdHiddenField);
		
		hasStableVersionHiddenField = new HiddenField<Boolean>();
		hasStableVersionHiddenField.setPropertyEditor(PropertyEditors.BOOLEAN);
		formPanel.add(hasStableVersionHiddenField);
		
		this.workflowStateIdHiddenField = new HiddenField<Long>();
		this.workflowStateIdHiddenField.setPropertyEditor(PropertyEditors.LONG);
		this.formPanel.add(this.workflowStateIdHiddenField);
		
		this.authorHiddenField = new HiddenField<String>();
		this.formPanel.add(this.authorHiddenField);
		
		this.createdDateHiddenField = new HiddenField<Date>();
		this.createdDateHiddenField.setPropertyEditor(PropertyEditors.DATE);
		this.formPanel.add(this.createdDateHiddenField);
		
		documentNameTextField = new TextField<String>();
		documentNameTextField.setAllowBlank(false);
		documentNameTextField.setFieldLabel(GwtLocaleProvider.getConstants().NAME());
		formPanel.add(documentNameTextField, formData);
		
		documentDescriptionTextField = new TextArea();
		documentDescriptionTextField.setFieldLabel(GwtLocaleProvider.getConstants().DESCRIPTION());
		formPanel.add(documentDescriptionTextField, formData);
		
		metadataFieldSet = new FieldSet();
		metadataFieldSet.setHeading(GwtLocaleProvider.getConstants().METADATA());
		metadataFieldSet.setLayout(new FormLayout());
		formPanel.add(metadataFieldSet);
		
		collectionFieldSet = new FieldSet();
		collectionFieldSet.setHeading(GwtLocaleProvider.getConstants().METADATA_COLLECTIONS());
		collectionFieldSet.setLayout(new FormLayout());
		formPanel.add(collectionFieldSet);
		
		add(formPanel);
	}
	
	public boolean isValid() {
		if (!formPanel.isValid()) {
			ErrorHelper.addError(GwtLocaleProvider.getMessages().REQUIRED_FIELDS_NOT_COMPLETED_OR_VALUES_NOT_CORRECT());
			return false;
		}
		return true;
	}

	public void populate(DocumentModel document) {
		// date generale
		document.setId(documentIdHiddenField.getValue());
		document.setDocumentTypeId(Long.parseLong(documentTypeIdHiddenField.getValue()));
		document.setDocumentLocationRealName(documentLocationRealNameHiddenField.getValue());
		document.setParentFolderId(parentFolderIdHiddenField.getValue());
		document.setWorkflowStateId(this.workflowStateIdHiddenField.getValue());
		document.setAuthor(this.authorHiddenField.getValue());
		document.setCreated(this.createdDateHiddenField.getValue());
		document.setDocumentName(documentNameTextField.getValue());
		document.setDocumentDescription(documentDescriptionTextField.getValue());
		// metadatele
		List<MetadataInstanceModel> metadataInstances = new ArrayList<MetadataInstanceModel>();
		for (Long metadataDefinitionId : metadataFieldMap.keySet()) {
			MetadataField metadataField = metadataFieldMap.get(metadataDefinitionId);
			List<String> metadataValues = metadataField.getMetadataValues();
			if (!metadataValues.isEmpty()) {
				MetadataInstanceModel metadataInstance = new MetadataInstanceModel();
				metadataInstance.setMetadataDefinitionId(metadataDefinitionId);
				metadataInstance.setValues(metadataValues);
				metadataInstances.add(metadataInstance);
			}
		}
		document.setMetadataInstances(metadataInstances);
		// colectiile
		List<MetadataCollectionInstanceModel> metadataCollectionInstancs = new ArrayList<MetadataCollectionInstanceModel>();
		for (Long collectionDefinitionId : collectionFieldMap.keySet()) {
			List<CollectionInstanceModel> collectionInstanceList = collectionFieldMap.get(collectionDefinitionId).getCollectionInstanceList();
			MetadataCollectionInstanceModel instance = new MetadataCollectionInstanceModel();
			instance.setMetadataDefinitionId(collectionDefinitionId);
			instance.setCollectionInstanceRows(collectionInstanceList);
			metadataCollectionInstancs.add(instance);
		}
		document.setMetadataCollectionInstances(metadataCollectionInstancs);
	}
	
	public void prepareForAdd(final DocumentTypeModel documentType, String documentLocationRealName, String parentFolderId, WorkflowModel workflow, WorkflowStateModel currentState) {
		
		this.currentState = currentState;
		
		authorHiddenField.setValue(GwtRegistryUtils.getUserSecurity().getUserIdAsString());
		documentNameTextField.setValue(documentType.getName());
		documentTypeIdHiddenField.setValue(documentType.getId().toString());
		keepAllVersionsHiddenField.setValue(documentType.isKeepAllVersions());
		documentLocationRealNameHiddenField.setValue(documentLocationRealName);
		parentFolderIdHiddenField.setValue(parentFolderId);

		populateMetadataFieldSet(documentType, null);
		populateCollectionFieldSet(documentType, null);
		
		formPanel.layout();
	}

	public void prepareForViewOrEdit(final DocumentTypeModel documentType, final DocumentModel document, WorkflowModel workflow, WorkflowStateModel currentState) {
		
		this.currentState = currentState;
		
		documentIdHiddenField.setValue(document.getId());
		documentTypeIdHiddenField.setValue(documentType.getId().toString());
		keepAllVersionsHiddenField.setValue(documentType.isKeepAllVersions());
		documentLocationRealNameHiddenField.setValue(document.getDocumentLocationRealName());
		parentFolderIdHiddenField.setValue(document.getParentFolderId());
		hasStableVersionHiddenField.setValue(document.getHasStableVersion());
		this.workflowStateIdHiddenField.setValue(document.getWorkflowStateId());
		this.authorHiddenField.setValue(document.getAuthor());
		this.createdDateHiddenField.setValue(document.getCreated());
		documentNameTextField.setValue(document.getDocumentName());
		documentDescriptionTextField.setValue(document.getDocumentDescription());

		populateMetadataFieldSet(documentType, document);
		populateCollectionFieldSet(documentType, document);
		
		formPanel.layout();
	}
	
	public void onAdd() {
		for (MetadataField metadataField : metadataFieldMap.values()) {
			metadataField.onAddDocument(currentState);
		}
	}
	
	public void onEdit() {
		for (MetadataField metadataField : metadataFieldMap.values()) {
			metadataField.onEditDocument(currentState);
		}
	}
	
	private void populateMetadataFieldSet(DocumentTypeModel documentType, DocumentModel document) {
		
		Collection<MetadataDefinitionModel> metadataDefinitionsWithoutCollections = new ArrayList<MetadataDefinitionModel>();
		
		for (MetadataDefinitionModel metadataDefinition : documentType.getMetadataDefinitions()) {
			
			if (!metadataDefinition.getType().equals(MetadataDefinitionModel.TYPE_METADATA_COLLECTION)) {
				metadataDefinitionsWithoutCollections.add(metadataDefinition);
			}
			
			String metadataDefinitionName = metadataDefinition.getName();
			
			MetadataField metadataField = null;
			
			if (metadataDefinition.getType().equals(MetadataDefinitionModel.TYPE_TEXT)) {
				TextMetadataField textMetadataField = new TextMetadataField(metadataDefinitionName);
				metadataField = textMetadataField;
			} else if (metadataDefinition.getType().equals(MetadataDefinitionModel.TYPE_NUMERIC)) {
				NumericMetadataField numericMetadataField = new NumericMetadataField(metadataDefinitionName);
				metadataField = numericMetadataField;
			} else if (metadataDefinition.getType().equals(MetadataDefinitionModel.TYPE_AUTO_NUMBER)) {
				AutoNumberMetadataField autoNumberMetadataField = new AutoNumberMetadataField(metadataDefinitionName);
				metadataField = autoNumberMetadataField;
			} else if (metadataDefinition.getType().equals(MetadataDefinitionModel.TYPE_DATE)) {
				DateMetadataField dateMetadataField = new DateMetadataField(metadataDefinitionName);
				metadataField = dateMetadataField;
			} else if (metadataDefinition.getType().equals(MetadataDefinitionModel.TYPE_LIST)) {
				ListMetadataDefinitionModel listMetadataDefinition = (ListMetadataDefinitionModel) metadataDefinition;
				if (listMetadataDefinition.isExtendable().booleanValue() || listMetadataDefinition.isMultipleSelection().booleanValue()) {
					ListMetadataPopupField listMetadataPopupField = new ListMetadataPopupField(metadataDefinitionName, listMetadataDefinition.isMultipleSelection().booleanValue(), listMetadataDefinition.isExtendable().booleanValue(), listMetadataDefinition.getListItems());
					metadataField = listMetadataPopupField;
				} else {
					ListMetadataComboField listMetadataComboField = new ListMetadataComboField(metadataDefinitionName, listMetadataDefinition.getListItems());
					metadataField = listMetadataComboField;
				}
			} else if (metadataDefinition.getType().equals(MetadataDefinitionModel.TYPE_USER)) {
				UserMetadataDefinitionModel userMetadataDefinition = (UserMetadataDefinitionModel) metadataDefinition;
				UserMetadataField userMetadataField = UserMetadataFieldFactory.forMetadataDefinition(userMetadataDefinition);
				metadataField = userMetadataField;
			} else if (metadataDefinition.getType().equals(MetadataDefinitionModel.TYPE_TEXT_AREA)) {
				TextAreaMetadataField textAreaMetadataField = new TextAreaMetadataField(metadataDefinitionName);
				metadataField = textAreaMetadataField;
			} 
			
			if (metadataField != null) {
				boolean isMandatory = GwtMetadataOrCollectionBusinessUtils.isMetadataMandatory(metadataDefinition, currentState);
				populateMetadataValue(metadataDefinition, document, metadataField);
				metadataField.setMandatory(isMandatory);
				metadataField.setRestrictedOnEdit(GwtMetadataOrCollectionBusinessUtils.isMetadataRestrictedOnEdit(metadataDefinition, currentState));
				metadataFieldMap.put(metadataDefinition.getId(), metadataField);

				Field<?> metadataFormField = (Field<?>) metadataField;
				metadataFormField.setFieldLabel((isMandatory) ? GwtBusinessUtils.getLabelForMandatoryField(metadataDefinition.getLabel()) : metadataDefinition.getLabel());
				
				metadataFieldSet.add(metadataFormField, ComponentUtils.FORM_DATA_FOR_FIELDS_WITH_ALL_AVAILABLE_WIDTH);
				
				/*if (metadataFormField instanceof ListMetadataPopupField) {
					metadataFieldSet.add(metadataFormField, new FormData("90%"));
				} else {
					metadataFieldSet.add(metadataFormField);
				}*/
			}
		}
		
		GwtCereriDeConcediuConstants cereriDeConcediuConstants = GwtRegistryUtils.getCereriDeConcediuConstants();
		if (cereriDeConcediuConstants.isCerereDeConcediu(documentType.getId())) {
			
			GwtCerereDeConcediuConstants constantsForCerere = cereriDeConcediuConstants.getFor(documentType.getId());
			
			UserMetadataField inlocuitorMetadataField = (UserMetadataField) metadataFieldMap.get(constantsForCerere.getInlocuitorMetadataId());
			DateMetadataField dataInceputMetadataField = (DateMetadataField) metadataFieldMap.get(constantsForCerere.getDataInceputMetadataId());
			DateMetadataField dataSfarsitMetadataField = (DateMetadataField) metadataFieldMap.get(constantsForCerere.getDataSfarsitMetadataId());
			
			InlocuitorOptionUpdaterEventListener datesChangedEventListener = new InlocuitorOptionUpdaterEventListener(
				inlocuitorMetadataField, dataInceputMetadataField, dataSfarsitMetadataField);
			
			dataInceputMetadataField.getDatePicker().addListener(Events.Select, datesChangedEventListener);
			dataSfarsitMetadataField.getDatePicker().addListener(Events.Select, datesChangedEventListener);
			
			if ((dataInceputMetadataField.getValue() != null) || (dataSfarsitMetadataField != null)) {
				/*
				 * Daca am un document deschis si sunt deja completate campurile, atunci
				 * trebuie sa verific si sa actualizez inlocuitorii disponibili.
				 */
				datesChangedEventListener.checkAndUpdate();
			}
		}
		
		int neededFieldLabelWidth = new WordLengthBasedFieldLabelWidthCalculationStrategy(
			DEFAULT_FORM_LABEL_WIDTH_IN_PIXELS, metadataDefinitionsWithoutCollections)
			.calculateNeededLabelWidth();
		((FormLayout) this.metadataFieldSet.getLayout()).setLabelWidth(neededFieldLabelWidth);
		// Trebuie sa re-setez container-ul layout-ului, altfel NU isi va re-seta label width-ul.
		((FormLayout) this.metadataFieldSet.getLayout()).setContainer(this.metadataFieldSet);
		this.metadataFieldSet.layout();
		
		this.metadataFieldSet.setVisible(metadataDefinitionsWithoutCollections.size() > 0);
	}
	
	private void populateCollectionFieldSet(DocumentTypeModel documentType, DocumentModel document) {
		
		Collection<MetadataCollectionDefinitionModel> collectionDefinitions = new ArrayList<MetadataCollectionDefinitionModel>();
		
		for (MetadataDefinitionModel metadataDefinition : documentType.getMetadataDefinitions()) {
			
			if (!metadataDefinition.getType().equals(MetadataDefinitionModel.TYPE_METADATA_COLLECTION)) {
				continue;
			}
			
			MetadataCollectionDefinitionModel collectionDefinition = (MetadataCollectionDefinitionModel) metadataDefinition;
			collectionDefinitions.add(collectionDefinition);
			
			boolean mandatory = GwtMetadataOrCollectionBusinessUtils.isCollectionMandatory(collectionDefinition, currentState);
			
			CollectionField collectionField = new CollectionField(collectionDefinition, this.currentState);
			collectionField.setFieldLabel(mandatory ? GwtBusinessUtils.getLabelForMandatoryField(collectionDefinition.getLabel()) : collectionDefinition.getLabel());
			collectionField.setMandatory(mandatory);
			collectionField.setRestrictedOnEdit(GwtMetadataOrCollectionBusinessUtils.isCollectionRestrictedOnEdit(collectionDefinition, currentState));
			collectionFieldMap.put(collectionDefinition.getId(), collectionField);
			collectionFieldSet.add(collectionField, ComponentUtils.FORM_DATA_FOR_FIELDS_WITH_ALL_AVAILABLE_WIDTH);
			
			if (document != null) {
				List<CollectionInstanceModel> collectionInstanceList = null;
				for (MetadataCollectionInstanceModel instance : document.getMetadataCollectionInstances()) {
					if (instance.getMetadataDefinitionId().equals(collectionDefinition.getId())) {
						collectionInstanceList = instance.getCollectionInstanceRows();
					}
				}
				if (collectionInstanceList != null) {
					for (CollectionInstanceModel collectionInstance : collectionInstanceList) {
						collectionField.addCollectionInstance(collectionInstance);
					}
				}
			}
		}

		int neededFieldLabelWidth = new WordLengthBasedFieldLabelWidthCalculationStrategy(
			DEFAULT_FORM_LABEL_WIDTH_IN_PIXELS, collectionDefinitions)
			.calculateNeededLabelWidth();
		((FormLayout) this.collectionFieldSet.getLayout()).setLabelWidth(neededFieldLabelWidth);
		// Trebuie sa re-setez container-ul layout-ului, altfel NU isi va re-seta label width-ul.
		((FormLayout) this.collectionFieldSet.getLayout()).setContainer(this.collectionFieldSet);
		this.collectionFieldSet.layout();
		
		this.collectionFieldSet.setVisible(collectionDefinitions.size() > 0);
	}
	
	private void populateMetadataValue(MetadataDefinitionModel metadataDefinition, DocumentModel document, MetadataField metadataField) {
		if (document != null) {
			if (document.getMetadataInstanceMap().containsKey(metadataDefinition.getId())) {
				metadataField.setMetadataValues(document.getMetadataInstanceMap().get(metadataDefinition.getId()).getValues());
			}
		} else {
			if (metadataDefinition.getDefaultValue() != null) {
				metadataField.setMetadataValues(metadataDefinition.getDefaultValue());
			}
		}
	}

	public void reset() {
		metadataFieldMap.clear();
		collectionFieldMap.clear();
		metadataFieldSet.removeAll();
		collectionFieldSet.removeAll();
		formPanel.clear();
	}

	public void setReadOnly(boolean readOnly) {
		this.documentNameTextField.setReadOnly(readOnly);
		this.documentDescriptionTextField.setReadOnly(readOnly);
		for (MetadataField metadataField : this.metadataFieldMap.values()) {
			metadataField.setRestrictedOnEdit(readOnly || metadataField.isRestrictedOnEdit());
		}
		for (CollectionField collectionField : this.collectionFieldMap.values()) {
			collectionField.setReadOnly(readOnly);
		}
	}

	public String getDocumentId() {
		return documentIdHiddenField.getValue();
	}
	public void setDocumentId(String documentId) {
		this.documentIdHiddenField.setValue(documentId);
	}
	public Long getDocumentTypeId() {
		return Long.valueOf(documentTypeIdHiddenField.getValue());
	}
	public String getAuthorUserIdAsString() {
		return authorHiddenField.getValue();
	}
	public Boolean isKeepAllVersions() {
		return keepAllVersionsHiddenField.getValue();
	}
	public String getDocumentLocationRealName() {
		return documentLocationRealNameHiddenField.getValue();
	}
	public Boolean getHasStableVersion() {
		return hasStableVersionHiddenField.getValue();
	}
	
	/**
	 * Returneaza valoarea unei metadate, a carei definitie are ID-ul dat.
	 * Daca nu se gaseste campul metadatei, va returna null.
	 */
	public String getMetadataValue(Long metadataDefinitionId) {
		
		MetadataField metadataField = metadataFieldMap.get(metadataDefinitionId);
		if (metadataField == null) {
			return null;
		}
		
		return MetadataFieldHelper.getValue(metadataField);
	}

	@Override
	public String getMetadataValueByName(String metadataDefinitionName) {
		
		for (MetadataField metadataField : metadataFieldMap.values()) {
			if (metadataField.getMetadataDefinitionName().equals(metadataDefinitionName)) {
				return MetadataFieldHelper.getValue(metadataField);
			}
		}
		
		throw new IllegalArgumentException("NU s-a gasit campul pentru metadata cu numele [" + metadataDefinitionName + "].");
	}
}