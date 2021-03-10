package ro.cloudSoft.cloudDoc.domain.nomenclator;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "NOMENCLATOR_ATTRIBUTE")
public class NomenclatorAttribute {
	
	public static final String DATE_FORMAT = "yyyy.MM.dd";
	
	private Long id;
	private Nomenclator nomenclator;
	private String name;
	private String key;
	private String columnName;
	private Long uiOrder;
	private NomenclatorAttributeTypeEnum type;
	private Nomenclator typeNomenclator;
	private boolean required;
	private String requiredCheckExpression;
	private String invisibleCheckExpression;
	private String defaultValue;
	private boolean readonlyOnAdd;
	private boolean readonlyOnEdit;
	private List<NomenclatorAttributeSelectionFilter> typeNomenclatorSelectionFilters;
	private String typeNomenclatorSelectionFiltersCustomClass;
	private String typeNomenclatorSelectionFiltersCustomClassAttributeKeys;
	private String attributeKeyForAutocomplete;
	private NomenclatorAttributeAutocompleteType autocompleteType;
	private String nomenclatorAttributeKeyForAutocomplete;
	private Nomenclator completionSuggestionNomenclator;
	private String completionSuggestionNomenclatorAttributeKey;
	
	public static enum NomenclatorAttributeAutocompleteType {
		NOMENCLATOR_ATTRIBUTE,
		COPY_VALUE,
		DELETE_VALUE
	}
	
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "nomenclator_id")
	public Nomenclator getNomenclator() {
		return nomenclator;
	}

	public void setNomenclator(Nomenclator nomenclator) {
		this.nomenclator = nomenclator;
	}

	@Column(name = "name")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "key")
	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	@Column(name = "column_name")
	public String getColumnName() {
		return columnName;
	}

	public void setColumnName(String columnName) {
		this.columnName = columnName;
	}

	@Column(name = "ui_order")
	public Long getUiOrder() {
		return uiOrder;
	}

	public void setUiOrder(Long uiOrder) {
		this.uiOrder = uiOrder;
	}

	@Column(name = "type")
	@Enumerated(EnumType.STRING)
	public NomenclatorAttributeTypeEnum getType() {
		return type;
	}

	public void setType(NomenclatorAttributeTypeEnum type) {
		this.type = type;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "type_nomenclator_id")
	public Nomenclator getTypeNomenclator() {
		return typeNomenclator;
	}

	public void setTypeNomenclator(Nomenclator typeNomenclator) {
		this.typeNomenclator = typeNomenclator;
	}
	
	@Column(name = "REQUIRED")
	public boolean isRequired() {
		return required;
	}
	
	public void setRequired(boolean required) {
		this.required = required;
	}
	
	@Column(name = "REQUIRED_CHECK_EXPRESSION")
	public String getRequiredCheckExpression() {
		return requiredCheckExpression;
	}
	
	public void setRequiredCheckExpression(String requiredCheckExpression) {
		this.requiredCheckExpression = requiredCheckExpression;
	}
	
	@Column(name = "INVISIBLE_CHECK_EXPRESSION")
	public String getInvisibleCheckExpression() {
		return invisibleCheckExpression;
	}
	
	public void setInvisibleCheckExpression(String invisibleCheckExpression) {
		this.invisibleCheckExpression = invisibleCheckExpression;
	}
	
	@Column(name = "default_value", nullable = true)
	public String getDefaultValue() {
		return defaultValue;
	}
	
	public void setDefaultValue(String defaultValue) {
		this.defaultValue = defaultValue;
	}
	
	@Column(name = "READONLY_ON_ADD")	
	public boolean isReadonlyOnAdd() {
		return readonlyOnAdd;
	}
	
	public void setReadonlyOnAdd(boolean readonlyOnAdd) {
		this.readonlyOnAdd = readonlyOnAdd;
	}
	
	@Column(name = "READONLY_ON_EDIT")	
	public boolean isReadonlyOnEdit() {
		return readonlyOnEdit;
	}
	
	public void setReadonlyOnEdit(boolean readonlyOnEdit) {
		this.readonlyOnEdit = readonlyOnEdit;
	}
	
	@OneToMany(cascade = CascadeType.ALL, mappedBy="attribute")
	public List<NomenclatorAttributeSelectionFilter> getTypeNomenclatorSelectionFilters() {
		return typeNomenclatorSelectionFilters;
	}
	
	public void setTypeNomenclatorSelectionFilters(
			List<NomenclatorAttributeSelectionFilter> typeNomenclatorSelectionFilters) {
		this.typeNomenclatorSelectionFilters = typeNomenclatorSelectionFilters;
	}
	
	@Column(name = "TYPE_NOMENCLATOR_SELECTION_FILTERS_CUSTOM_CLASS")	
	public String getTypeNomenclatorSelectionFiltersCustomClass() {
		return typeNomenclatorSelectionFiltersCustomClass;
	}
	
	public void setTypeNomenclatorSelectionFiltersCustomClass(String typeNomenclatorSelectionFiltersCustomClass) {
		this.typeNomenclatorSelectionFiltersCustomClass = typeNomenclatorSelectionFiltersCustomClass;
	}
	
	@Column(name = "TYPE_NOMENCLATOR_SELECTION_FILTERS_CUSTOM_CLASS_ATTRIBUTE_KEYS")	
	public String getTypeNomenclatorSelectionFiltersCustomClassAttributeKeys() {
		return typeNomenclatorSelectionFiltersCustomClassAttributeKeys;
	}
	
	public void setTypeNomenclatorSelectionFiltersCustomClassAttributeKeys(
			String typeNomenclatorSelectionFiltersCustomClassAttributeKeys) {
		this.typeNomenclatorSelectionFiltersCustomClassAttributeKeys = typeNomenclatorSelectionFiltersCustomClassAttributeKeys;
	}
	
	@Column(name = "ATTRIBUTE_KEY_FOR_AUTOCOMPLETE")
	public String getAttributeKeyForAutocomplete() {
		return attributeKeyForAutocomplete;
	}

	public void setAttributeKeyForAutocomplete(String attributeKeyForAutocomplete) {
		this.attributeKeyForAutocomplete = attributeKeyForAutocomplete;
	}
	
	@Column(name = "AUTOCOMPLETE_TYPE")
	@Enumerated(EnumType.STRING)
	public NomenclatorAttributeAutocompleteType getAutocompleteType() {
		return autocompleteType;
	}

	public void setAutocompleteType(NomenclatorAttributeAutocompleteType autocompleteType) {
		this.autocompleteType = autocompleteType;
	}
	
	@Column(name = "NOMENCLATOR_ATTRIBUTE_KEY_FOR_AUTOCOMPLETE")
	public String getNomenclatorAttributeKeyForAutocomplete() {
		return nomenclatorAttributeKeyForAutocomplete;
	}

	public void setNomenclatorAttributeKeyForAutocomplete(String nomenclatorAttributeKeyForAutocomplete) {
		this.nomenclatorAttributeKeyForAutocomplete = nomenclatorAttributeKeyForAutocomplete;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "COMPLETION_SUGGESTION_NOMENCLATOR_ID", updatable = false)
	public Nomenclator getCompletionSuggestionNomenclator() {
		return completionSuggestionNomenclator;
	}

	public void setCompletionSuggestionNomenclator(Nomenclator completionSuggetionNomenclator) {
		this.completionSuggestionNomenclator = completionSuggetionNomenclator;
	}
	
	@Column(name = "COMPLETION_SUGGESTION_NOMENCLATOR_ATTRIBUTE_KEY", updatable = false)
	public String getCompletionSuggestionNomenclatorAttributeKey() {
		return completionSuggestionNomenclatorAttributeKey;
	}

	public void setCompletionSuggestionNomenclatorAttributeKey(String completionSuggestionNomenclatorAttributeKey) {
		this.completionSuggestionNomenclatorAttributeKey = completionSuggestionNomenclatorAttributeKey;
	}
}
