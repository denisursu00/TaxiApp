package ro.cloudSoft.cloudDoc.domain.content.search;

import java.util.Map;

import com.google.common.collect.Maps;

public class DocumentAdvancedSearchResultsView extends AbstractDocumentSearchResultsView {

	private Map<Long, String> documentMetadataInstanceDisplayValueByDefinitionId = Maps.newHashMap();
	
	public Map<Long, String> getDocumentMetadataInstanceDisplayValueByDefinitionId() {
		return documentMetadataInstanceDisplayValueByDefinitionId;
	}
	public void setDocumentMetadataInstanceDisplayValueByDefinitionId(Map<Long, String> documentMetadataInstanceDisplayValueByDefinitionId) {
		this.documentMetadataInstanceDisplayValueByDefinitionId = documentMetadataInstanceDisplayValueByDefinitionId;
	}
}