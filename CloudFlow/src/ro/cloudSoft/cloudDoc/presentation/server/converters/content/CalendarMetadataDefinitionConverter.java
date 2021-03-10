package ro.cloudSoft.cloudDoc.presentation.server.converters.content;

import ro.cloudSoft.cloudDoc.domain.content.CalendarMetadataDefinition;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.content.CalendarMetadataDefinitionModel;;

public class CalendarMetadataDefinitionConverter {

	public static CalendarMetadataDefinitionModel toModel(CalendarMetadataDefinition metadataDefinition) {
		CalendarMetadataDefinitionModel model = new CalendarMetadataDefinitionModel();
		return model;
	}

	public static CalendarMetadataDefinition toEntity(CalendarMetadataDefinitionModel model) {
		CalendarMetadataDefinition metadataDefinition = new CalendarMetadataDefinition();
		return metadataDefinition;
	}
}
