package ro.cloudSoft.cloudDoc.presentation.client.shared.model.content;

import java.util.List;

import ro.cloudSoft.cloudDoc.presentation.client.shared.model.nomenclator.NomenclatorFilter;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.nomenclator.NomenclatorSortedAttribute;

public class NomenclatorValueAsViewSearchRequestModel {

	private int offset;
	private int pageSize;
	
	private Long nomenclatorId;
	private List<Long> valueIds;
	private List<NomenclatorFilter> filters;
	private List<NomenclatorSortedAttribute> sortedAttributes;
	
	public int getOffset() {
		return offset;
	}
	public void setOffset(int offset) {
		this.offset = offset;
	}
	public int getPageSize() {
		return pageSize;
	}
	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}
	public Long getNomenclatorId() {
		return nomenclatorId;
	}
	public void setNomenclatorId(Long nomenclatorId) {
		this.nomenclatorId = nomenclatorId;
	}
	public List<Long> getValueIds() {
		return valueIds;
	}
	public void setValueIds(List<Long> valueIds) {
		this.valueIds = valueIds;
	}
	public List<NomenclatorFilter> getFilters() {
		return filters;
	}
	public void setFilters(List<NomenclatorFilter> filters) {
		this.filters = filters;
	}
	public List<NomenclatorSortedAttribute> getSortedAttributes() {
		return sortedAttributes;
	}
	public void setSortedAttributes(List<NomenclatorSortedAttribute> sortedAttributes) {
		this.sortedAttributes = sortedAttributes;
	}
}
