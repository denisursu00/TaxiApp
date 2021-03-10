package ro.cloudSoft.cloudDoc.presentation.client.shared.model.nomenclator;

import java.util.List;

import ro.cloudSoft.cloudDoc.domain.nomenclator.NomenclatorAttribute.NomenclatorAttributeAutocompleteType;

public class NomenclatorAttributeModel {
	
	private Long id;
	private Long nomenclatorId;
	private String name;
	private String key;
	private Long uiOrder;
	private String type;
	private Long typeNomenclatorId;
	private boolean required;
	private String requiredCheckExpression;
	private String invisibleCheckExpression;
	private String defaultValue;
	private boolean readonlyOnAdd;
	private boolean readonlyOnEdit;
	private List<NomenclatorAttributeSelectionFilterModel> typeNomenclatorSelectionFilters;
	private String typeNomenclatorSelectionFiltersCustomClass;
	private String typeNomenclatorSelectionFiltersCustomClassAttributeKeys;	
	private String attributeKeyForAutocomplete;
	private NomenclatorAttributeAutocompleteType autocompleteType;
	private String nomenclatorAttributeKeyForAutocomplete;
	private Long completionSuggestionNomenclatorId;
	private String completionSuggestionNomenclatorAttributeKey;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getNomenclatorId() {
		return nomenclatorId;
	}
	public void setNomenclatorId(Long nomenclatorId) {
		this.nomenclatorId = nomenclatorId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	public Long getUiOrder() {
		return uiOrder;
	}
	public void setUiOrder(Long uiOrder) {
		this.uiOrder = uiOrder;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public Long getTypeNomenclatorId() {
		return typeNomenclatorId;
	}
	public void setTypeNomenclatorId(Long typeNomenclatorId) {
		this.typeNomenclatorId = typeNomenclatorId;
	}
	public boolean isRequired() {
		return required;
	}
	public void setRequired(boolean required) {
		this.required = required;
	}
	public String getDefaultValue() {
		return defaultValue;
	}
	public void setDefaultValue(String defaultValue) {
		this.defaultValue = defaultValue;
	}
	public boolean isReadonlyOnAdd() {
		return readonlyOnAdd;
	}
	public void setReadonlyOnAdd(boolean readonlyOnAdd) {
		this.readonlyOnAdd = readonlyOnAdd;
	}
	public boolean isReadonlyOnEdit() {
		return readonlyOnEdit;
	}
	public void setReadonlyOnEdit(boolean readonlyOnEdit) {
		this.readonlyOnEdit = readonlyOnEdit;
	}
	public List<NomenclatorAttributeSelectionFilterModel> getTypeNomenclatorSelectionFilters() {
		return typeNomenclatorSelectionFilters;
	}
	public void setTypeNomenclatorSelectionFilters(
			List<NomenclatorAttributeSelectionFilterModel> typeNomenclatorSelectionFilters) {
		this.typeNomenclatorSelectionFilters = typeNomenclatorSelectionFilters;
	}
	public String getTypeNomenclatorSelectionFiltersCustomClass() {
		return typeNomenclatorSelectionFiltersCustomClass;
	}
	public void setTypeNomenclatorSelectionFiltersCustomClass(String typeNomenclatorSelectionFiltersCustomClass) {
		this.typeNomenclatorSelectionFiltersCustomClass = typeNomenclatorSelectionFiltersCustomClass;
	}
	public String getTypeNomenclatorSelectionFiltersCustomClassAttributeKeys() {
		return typeNomenclatorSelectionFiltersCustomClassAttributeKeys;
	}
	public void setTypeNomenclatorSelectionFiltersCustomClassAttributeKeys(
			String typeNomenclatorSelectionFiltersCustomClassAttributeKeys) {
		this.typeNomenclatorSelectionFiltersCustomClassAttributeKeys = typeNomenclatorSelectionFiltersCustomClassAttributeKeys;
	}
	public String getAttributeKeyForAutocomplete() {
		return attributeKeyForAutocomplete;
	}
	public void setAttributeKeyForAutocomplete(String attributeKeyForAutocomplete) {
		this.attributeKeyForAutocomplete = attributeKeyForAutocomplete;
	}
	public NomenclatorAttributeAutocompleteType getAutocompleteType() {
		return autocompleteType;
	}
	public void setAutocompleteType(NomenclatorAttributeAutocompleteType autocompleteType) {
		this.autocompleteType = autocompleteType;
	}
	public String getNomenclatorAttributeKeyForAutocomplete() {
		return nomenclatorAttributeKeyForAutocomplete;
	}
	public void setNomenclatorAttributeKeyForAutocomplete(String nomenclatorAttributeKeyForAutocomplete) {
		this.nomenclatorAttributeKeyForAutocomplete = nomenclatorAttributeKeyForAutocomplete;
	}
	public String getRequiredCheckExpression() {
		return requiredCheckExpression;
	}
	public void setRequiredCheckExpression(String requiredCheckExpression) {
		this.requiredCheckExpression = requiredCheckExpression;
	}
	public String getInvisibleCheckExpression() {
		return invisibleCheckExpression;
	}
	public void setInvisibleCheckExpression(String invisibleCheckExpression) {
		this.invisibleCheckExpression = invisibleCheckExpression;
	}
	public Long getCompletionSuggestionNomenclatorId() {
		return completionSuggestionNomenclatorId;
	}
	public void setCompletionSuggestionNomenclatorId(Long completionSuggestionNomenclatorId) {
		this.completionSuggestionNomenclatorId = completionSuggestionNomenclatorId;
	}
	public String getCompletionSuggestionNomenclatorAttributeKey() {
		return completionSuggestionNomenclatorAttributeKey;
	}
	public void setCompletionSuggestionNomenclatorAttributeKey(String completionSuggestionNomenclatorAttributeKey) {
		this.completionSuggestionNomenclatorAttributeKey = completionSuggestionNomenclatorAttributeKey;
	}
}
