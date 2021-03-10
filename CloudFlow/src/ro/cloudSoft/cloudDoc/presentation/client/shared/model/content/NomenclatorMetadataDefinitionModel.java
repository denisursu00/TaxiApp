package ro.cloudSoft.cloudDoc.presentation.client.shared.model.content;

import java.io.Serializable;
import java.util.List;

public class NomenclatorMetadataDefinitionModel extends MetadataDefinitionModel implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private Long nomenclatorId;
	private List<NomenclatorMetadataDefinitionValueSelectionFilterModel> valueSelectionFilters;
	
	public Long getNomenclatorId() {
		return nomenclatorId;
	}
	public void setNomenclatorId(Long nomenclatorId) {
		this.nomenclatorId = nomenclatorId;
	}
	
	public List<NomenclatorMetadataDefinitionValueSelectionFilterModel> getValueSelectionFilters() {
		return valueSelectionFilters;
	}
	public void setValueSelectionFilters(
			List<NomenclatorMetadataDefinitionValueSelectionFilterModel> valueSelectionFilters) {
		this.valueSelectionFilters = valueSelectionFilters;
	}
}
