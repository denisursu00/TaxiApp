package ro.cloudSoft.cloudDoc.presentation.server.converters.content;

import ro.cloudSoft.cloudDoc.domain.content.DocumentTypeTemplate;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.content.DocumentTypeTemplateModel;

public class DocumentTypeTemplateConverter {

	public static DocumentTypeTemplateModel getModelFromDocumentTypeTemplate(DocumentTypeTemplate documentTypeTemplate) {
		DocumentTypeTemplateModel documentTypeTemplateModel = new DocumentTypeTemplateModel();
		
		documentTypeTemplateModel.setName(documentTypeTemplate.getName());
		documentTypeTemplateModel.setDescription(documentTypeTemplate.getDescription());
		documentTypeTemplateModel.setExportAvailabilityExpression(documentTypeTemplate.getExportAvailabilityExpression());
		
		return documentTypeTemplateModel;
	}
}