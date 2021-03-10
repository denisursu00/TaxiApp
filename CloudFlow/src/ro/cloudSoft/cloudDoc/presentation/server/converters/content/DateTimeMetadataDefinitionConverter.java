package ro.cloudSoft.cloudDoc.presentation.server.converters.content;

import ro.cloudSoft.cloudDoc.domain.content.DateTimeMetadataDefinition;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.content.DateTimeMetadataDefinitionModel;

public class DateTimeMetadataDefinitionConverter {

	public static DateTimeMetadataDefinitionModel getModelFromDateTimeMetadataDefinition(DateTimeMetadataDefinition metadataDefinition) {
		DateTimeMetadataDefinitionModel model = new DateTimeMetadataDefinitionModel();
		model.setAutoCompleteWithCurrentDateTime(metadataDefinition.isAutoCompleteWithCurrentDateTime());
		return model;
	}

	public static DateTimeMetadataDefinition getDateTimeMetadataDefinitionFromModel(DateTimeMetadataDefinitionModel model) {
		DateTimeMetadataDefinition metadataDefinition = new DateTimeMetadataDefinition();
		metadataDefinition.setAutoCompleteWithCurrentDateTime(model.isAutoCompleteWithCurrentDateTime());
		return metadataDefinition;
	}
}