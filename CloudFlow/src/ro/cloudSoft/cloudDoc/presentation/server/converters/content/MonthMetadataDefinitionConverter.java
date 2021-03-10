package ro.cloudSoft.cloudDoc.presentation.server.converters.content;

import ro.cloudSoft.cloudDoc.domain.content.MonthMetadataDefinition;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.content.MonthMetadataDefinitionModel;

public class MonthMetadataDefinitionConverter {

	public static MonthMetadataDefinitionModel getModelFromDateMetadataDefinition(MonthMetadataDefinition metadataDefinition) {
		MonthMetadataDefinitionModel model = new MonthMetadataDefinitionModel();
		model.setAutoCompleteWithCurrentMonth(metadataDefinition.isAutoCompleteWithCurrentMonth());
		return model;
	}

	public static MonthMetadataDefinition getDateMetadataDefinitionFromModel(MonthMetadataDefinitionModel model) {
		MonthMetadataDefinition metadataDefinition = new MonthMetadataDefinition();
		metadataDefinition.setAutoCompleteWithCurrentMonth(model.isAutoCompleteWithCurrentMonth());
		return metadataDefinition;
	}
}