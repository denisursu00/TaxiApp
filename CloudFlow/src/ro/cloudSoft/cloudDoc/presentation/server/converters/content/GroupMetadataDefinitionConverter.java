package ro.cloudSoft.cloudDoc.presentation.server.converters.content;

import ro.cloudSoft.cloudDoc.domain.content.GroupMetadataDefinition;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.content.GroupMetadataDefinitionModel;

public class GroupMetadataDefinitionConverter {
	
	public GroupMetadataDefinitionModel toModel(GroupMetadataDefinition entity) {
		GroupMetadataDefinitionModel model = new GroupMetadataDefinitionModel();
		return model;
	}
	
	public GroupMetadataDefinition toEntity(GroupMetadataDefinitionModel model) {
		GroupMetadataDefinition entity = new GroupMetadataDefinition();
		return entity;
	}
}
