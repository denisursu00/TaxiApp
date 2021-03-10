package ro.cloudSoft.cloudDoc.presentation.server.converters.content;

import ro.cloudSoft.cloudDoc.domain.content.ProjectMetadataDefinition;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.content.ProjectMetadataDefinitionModel;;

public class ProjectMetadataDefinitionConverter {

	public static ProjectMetadataDefinitionModel toModel(ProjectMetadataDefinition metadataDefinition) {
		ProjectMetadataDefinitionModel model = new ProjectMetadataDefinitionModel();
		model.setMultipleProjectsSelection(metadataDefinition.isMultipleProjectsSelection());
		return model;
	}

	public static ProjectMetadataDefinition toEntity(ProjectMetadataDefinitionModel model) {
		ProjectMetadataDefinition metadataDefinition = new ProjectMetadataDefinition();
		metadataDefinition.setMultipleProjectsSelection(model.isMultipleProjectsSelection());
		return metadataDefinition;
	}
}
