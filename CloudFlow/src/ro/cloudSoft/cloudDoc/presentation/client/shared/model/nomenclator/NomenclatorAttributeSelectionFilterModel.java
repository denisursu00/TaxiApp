package ro.cloudSoft.cloudDoc.presentation.client.shared.model.nomenclator;

public class NomenclatorAttributeSelectionFilterModel {

	private Long id;
	private Long filterAttributeId;
	private String filterAttributeKey;
	private String defaultFilterValue;
	private String attributeKeyForAutocompleteFilterValue;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getFilterAttributeId() {
		return filterAttributeId;
	}
	public void setFilterAttributeId(Long filterAttributeId) {
		this.filterAttributeId = filterAttributeId;
	}
	public String getFilterAttributeKey() {
		return filterAttributeKey;
	}
	public void setFilterAttributeKey(String filterAttributeKey) {
		this.filterAttributeKey = filterAttributeKey;
	}
	public String getDefaultFilterValue() {
		return defaultFilterValue;
	}
	public void setDefaultFilterValue(String defaultFilterValue) {
		this.defaultFilterValue = defaultFilterValue;
	}
	public String getAttributeKeyForAutocompleteFilterValue() {
		return attributeKeyForAutocompleteFilterValue;
	}
	public void setAttributeKeyForAutocompleteFilterValue(String attributeKeyForAutocompleteFilterValue) {
		this.attributeKeyForAutocompleteFilterValue = attributeKeyForAutocompleteFilterValue;
	}
}
