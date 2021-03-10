package ro.cloudSoft.cloudDoc.presentation.server.converters.content;

import ro.cloudSoft.cloudDoc.domain.content.DateMetadataDefinition;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.content.DateMetadataDefinitionModel;

public class DateMetadataDefinitionConverter {

	public static DateMetadataDefinitionModel getModelFromDateMetadataDefinition(DateMetadataDefinition metadataDefinition) {
		DateMetadataDefinitionModel model = new DateMetadataDefinitionModel();
		model.setAutoCompleteWithCurrentDate(metadataDefinition.isAutoCompleteWithCurrentDate());
		return model;
	}

	public static DateMetadataDefinition getDateMetadataDefinitionFromModel(DateMetadataDefinitionModel model) {
		DateMetadataDefinition metadataDefinition = new DateMetadataDefinition();
		metadataDefinition.setAutoCompleteWithCurrentDate(model.isAutoCompleteWithCurrentDate());
		return metadataDefinition;
	}
}