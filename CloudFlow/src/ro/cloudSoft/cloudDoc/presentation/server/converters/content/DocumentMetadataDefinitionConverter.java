package ro.cloudSoft.cloudDoc.presentation.server.converters.content;

import ro.cloudSoft.cloudDoc.domain.content.DocumentMetadataDefinition;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.content.DocumentMetadataDefinitionModel;

public class DocumentMetadataDefinitionConverter {

	public static DocumentMetadataDefinitionModel toModel(DocumentMetadataDefinition metadataDefinition) {
		DocumentMetadataDefinitionModel model = new DocumentMetadataDefinitionModel();
		model.setMetadataDocumentTypeId(metadataDefinition.getMetadataDocumentTypeId());
		model.setMultipleDocumentsSelection(metadataDefinition.isMultipleDocumentsSelection());
		return model;
	}

	public static DocumentMetadataDefinition toEntity(DocumentMetadataDefinitionModel model) {
		DocumentMetadataDefinition metadataDefinition = new DocumentMetadataDefinition();
		metadataDefinition.setMetadataDocumentTypeId(model.getMetadataDocumentTypeId());
		metadataDefinition.setMultipleDocumentsSelection(model.isMultipleDocumentsSelection());
		return metadataDefinition;
	}
}