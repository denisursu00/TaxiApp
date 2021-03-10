package ro.cloudSoft.cloudDoc.presentation.client.shared.model.content.search;

import java.util.Map;

public class DocumentAdvancedSearchResultsViewModel extends AbstractDocumentSearchResultsViewModel {

	private static final long serialVersionUID = 1L;
	
	public static final String PROPERTY_DOCUMENT_METADATA_INSTANCE_DISPLAY_VALUE_BY_DEFINITION_ID = "documentMetadataInstanceDisplayValueByDefinitionId";
	
	public Map<Long, String> getDocumentMetadataInstanceDisplayValueByDefinitionId() {
		return get(PROPERTY_DOCUMENT_METADATA_INSTANCE_DISPLAY_VALUE_BY_DEFINITION_ID);
	}
	public void setDocumentMetadataInstanceDisplayValueByDefinitionId(Map<Long, String> documentMetadataInstanceDisplayValueByDefinitionId) {
		set(PROPERTY_DOCUMENT_METADATA_INSTANCE_DISPLAY_VALUE_BY_DEFINITION_ID, documentMetadataInstanceDisplayValueByDefinitionId);
	}
}