package ro.cloudSoft.cloudDoc.domain.content;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import ro.cloudSoft.cloudDoc.domain.nomenclator.NomenclatorAttribute;

@Entity
@Table(name = "metadata_definition_nomenclator_value_selection_filter")
public class NomenclatorMetadataDefinitionValueSelectionFilter {
	
	private Long id;	
	private NomenclatorMetadataDefinition metadataDefinition;
	
	private NomenclatorAttribute filterAttribute;
	private String defaultFilterValue;
	private String metadataNameForAutocompleteFilterValue;
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "metadata_definition_id")
	public NomenclatorMetadataDefinition getMetadataDefinition() {
		return metadataDefinition;
	}

	public void setMetadataDefinition(NomenclatorMetadataDefinition metadataDefinition) {
		this.metadataDefinition = metadataDefinition;
	}
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "filter_attribute_id")
	public NomenclatorAttribute getFilterAttribute() {
		return filterAttribute;
	}

	public void setFilterAttribute(NomenclatorAttribute filterAttribute) {
		this.filterAttribute = filterAttribute;
	}
	
	@Column(name = "default_filter_value")
	public String getDefaultFilterValue() {
		return defaultFilterValue;
	}

	public void setDefaultFilterValue(String defaultFilterValue) {
		this.defaultFilterValue = defaultFilterValue;
	}
	
	@Column(name = "metadata_name_for_autocomplete_filter_value")
	public String getMetadataNameForAutocompleteFilterValue() {
		return metadataNameForAutocompleteFilterValue;
	}

	public void setMetadataNameForAutocompleteFilterValue(String metadataNameForAutocompleteFilterValue) {
		this.metadataNameForAutocompleteFilterValue = metadataNameForAutocompleteFilterValue;
	}		
}