package ro.cloudSoft.cloudDoc.domain.content;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import ro.cloudSoft.cloudDoc.domain.nomenclator.Nomenclator;

@Entity
@DiscriminatorValue("NOMENCLATOR")
public class NomenclatorMetadataDefinition extends MetadataDefinition {
	
	private Nomenclator nomenclator;
	
	private List<NomenclatorMetadataDefinitionValueSelectionFilter> valueSelectionFilters;
	
	@ManyToOne
    @JoinColumn(name="NOMENCLATOR_ID")
	public Nomenclator getNomenclator() {
		return nomenclator;
	}
	
	public void setNomenclator(Nomenclator nomenclator) {
		this.nomenclator = nomenclator;
	}
	
	@OneToMany(cascade = CascadeType.ALL, mappedBy="metadataDefinition")
	public List<NomenclatorMetadataDefinitionValueSelectionFilter> getValueSelectionFilters() {
		return valueSelectionFilters;
	}
	
	public void setValueSelectionFilters(List<NomenclatorMetadataDefinitionValueSelectionFilter> valueSelectionFilters) {
		this.valueSelectionFilters = valueSelectionFilters;
	}
}
