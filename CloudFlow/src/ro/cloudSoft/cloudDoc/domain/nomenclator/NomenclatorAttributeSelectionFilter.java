package ro.cloudSoft.cloudDoc.domain.nomenclator;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "NOMENCLATOR_ATTRIBUTE_SELECTION_FILTER")
public class NomenclatorAttributeSelectionFilter {
	
	private Long id;	
	private NomenclatorAttribute attribute;
	
	private NomenclatorAttribute filterAttribute;
	private String defaultFilterValue;
	private String attributeKeyForAutocompleteFilterValue;
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "attribute_id")
	public NomenclatorAttribute getAttribute() {
		return attribute;
	}

	public void setAttribute(NomenclatorAttribute attribute) {
		this.attribute = attribute;
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
	
	@Column(name = "attribute_key_for_autocomplete_filter_value")
	public String getAttributeKeyForAutocompleteFilterValue() {
		return attributeKeyForAutocompleteFilterValue;
	}

	public void setAttributeKeyForAutocompleteFilterValue(String attributeKeyForAutocompleteFilterValue) {
		this.attributeKeyForAutocompleteFilterValue = attributeKeyForAutocompleteFilterValue;
	}		
}
