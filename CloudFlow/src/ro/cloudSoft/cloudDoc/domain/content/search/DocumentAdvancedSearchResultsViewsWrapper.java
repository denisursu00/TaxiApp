package ro.cloudSoft.cloudDoc.domain.content.search;

import java.util.LinkedHashMap;
import java.util.List;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

public class DocumentAdvancedSearchResultsViewsWrapper {

	private LinkedHashMap<Long, String> representativeMetadataDefinitionLabelById = Maps.newLinkedHashMap();
	private List<DocumentAdvancedSearchResultsView> searchResultsViews = Lists.newArrayList();
	
	public LinkedHashMap<Long, String> getRepresentativeMetadataDefinitionLabelById() {
		return representativeMetadataDefinitionLabelById;
	}
	public List<DocumentAdvancedSearchResultsView> getSearchResultsViews() {
		return searchResultsViews;
	}
	public void setRepresentativeMetadataDefinitionLabelById(LinkedHashMap<Long, String> representativeMetadataDefinitionLabelById) {
		this.representativeMetadataDefinitionLabelById = representativeMetadataDefinitionLabelById;
	}
	public void setSearchResultsViews(List<DocumentAdvancedSearchResultsView> searchResultsViews) {
		this.searchResultsViews = searchResultsViews;
	}
}