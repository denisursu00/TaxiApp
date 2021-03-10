package ro.cloudSoft.cloudDoc.presentation.client.shared.model.content.search;

import java.util.LinkedHashMap;
import java.util.List;

import com.extjs.gxt.ui.client.data.BaseModel;
import com.google.gwt.user.client.rpc.IsSerializable;

public class DocumentAdvancedSearchResultsViewsWrapperModel extends BaseModel implements IsSerializable {

	private static final long serialVersionUID = 1L;

	public static final String PROPERTY_REPRESENTATIVE_METADATA_DEFINITION_LABEL_BY_ID = "representativeMetadataDefinitionLabelById";
	public static final String PROPERTY_SEARCH_RESULTS_VIEWS = "searchResultsViews";
	
	public LinkedHashMap<Long, String> getRepresentativeMetadataDefinitionLabelById() {
		return get(PROPERTY_REPRESENTATIVE_METADATA_DEFINITION_LABEL_BY_ID);
	}
	public List<DocumentAdvancedSearchResultsViewModel> getSearchResultsViews() {
		return get(PROPERTY_SEARCH_RESULTS_VIEWS);
	}
	public void setRepresentativeMetadataDefinitionLabelById(LinkedHashMap<Long, String> representativeMetadataDefinitionLabelById) {
		set(PROPERTY_REPRESENTATIVE_METADATA_DEFINITION_LABEL_BY_ID, representativeMetadataDefinitionLabelById);
	}
	public void setSearchResultsViews(List<DocumentAdvancedSearchResultsViewModel> searchResultsViews) {
		set(PROPERTY_SEARCH_RESULTS_VIEWS, searchResultsViews);
	}
}