package ro.cloudSoft.cloudDoc.domain.nomenclator;

import java.util.ArrayList;
import java.util.List;

import ro.cloudSoft.cloudDoc.presentation.client.shared.model.nomenclator.NomenclatorFilter;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.nomenclator.NomenclatorSortedAttribute;

public class NomenclatorValueSearchCriteria {
	
	private Long nomenclatorId;
	private List<Long> valueIds;
	private List<NomenclatorFilter> filterValues;
	private List<NomenclatorSortedAttribute> sortedAttributes;
	
	public NomenclatorValueSearchCriteria() {
		this.filterValues = new ArrayList<NomenclatorFilter>();
		this.sortedAttributes = new ArrayList<NomenclatorSortedAttribute>();
	}
	
	public List<Long> getValueIds() {
		return valueIds;
	}
	public void setValueIds(List<Long> valueIds) {
		this.valueIds = valueIds;
	}
	public Long getNomenclatorId() {
		return nomenclatorId;
	}
	public void setNomenclatorId(Long nomenclatorId) {
		this.nomenclatorId = nomenclatorId;
	}
	public List<NomenclatorFilter> getFilterValues() {
		return filterValues;
	}
	public void setFilterValues(List<NomenclatorFilter> filterValues) {
		this.filterValues = filterValues;
	}
	public List<NomenclatorSortedAttribute> getSortedAttributes() {
		return sortedAttributes;
	}
	public void setSortedAttributes(List<NomenclatorSortedAttribute> sortedAttributes) {
		this.sortedAttributes = sortedAttributes;
	}
}
