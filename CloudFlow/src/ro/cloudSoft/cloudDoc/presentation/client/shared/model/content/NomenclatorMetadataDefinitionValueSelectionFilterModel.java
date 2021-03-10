package ro.cloudSoft.cloudDoc.presentation.client.shared.model.content;

public class NomenclatorMetadataDefinitionValueSelectionFilterModel {
	
	private Long id;	
	private Long filterAttributeId;
	private String filterAttributeKey;
	private String defaultFilterValue;
	private String metadataNameForAutocompleteFilterValue;
	
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
	public String getMetadataNameForAutocompleteFilterValue() {
		return metadataNameForAutocompleteFilterValue;
	}
	public void setMetadataNameForAutocompleteFilterValue(String metadataNameForAutocompleteFilterValue) {
		this.metadataNameForAutocompleteFilterValue = metadataNameForAutocompleteFilterValue;
	}
}