package ro.cloudSoft.cloudDoc.presentation.server.converters.content;

import ro.cloudSoft.cloudDoc.domain.content.UserMetadataDefinition;
import ro.cloudSoft.cloudDoc.domain.organization.Group;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.content.UserMetadataDefinitionModel;
import ro.cloudSoft.cloudDoc.services.organization.GroupService;

public class UserMetadataDefinitionConverter {

	public static UserMetadataDefinitionModel getModel(UserMetadataDefinition metadataDefinition) {
		
		UserMetadataDefinitionModel metadataDefinitionModel = new UserMetadataDefinitionModel();
		
		metadataDefinitionModel.setOnlyUsersFromGroup(metadataDefinition.isOnlyUsersFromGroup());
		
		String idOfGroupOfPermittedUsers = null;
		if (metadataDefinition.getGroupOfPermittedUsers() != null) {
			idOfGroupOfPermittedUsers = metadataDefinition.getGroupOfPermittedUsers().getId().toString();
		}
		metadataDefinitionModel.setIdOfGroupOfPermittedUsers(idOfGroupOfPermittedUsers);
		
		metadataDefinitionModel.setAutoCompleteWithCurrentUser(metadataDefinition.isAutoCompleteWithCurrentUser());
		metadataDefinitionModel.setAutoCompleteWithCurrentUserStateCode(metadataDefinition.getAutoCompleteWithCurrentUserStateCode());
		
		return metadataDefinitionModel;
	}
	
	public static UserMetadataDefinition getFromModel(UserMetadataDefinitionModel metadataDefinitionModel, GroupService groupService) {
		
		UserMetadataDefinition metadataDefinition = new UserMetadataDefinition();
		
		metadataDefinition.setOnlyUsersFromGroup(metadataDefinitionModel.isOnlyUsersFromGroup());
		
		Group groupOfPermittedUsers = null;
		if (metadataDefinitionModel.getIdOfGroupOfPermittedUsers() != null) {
			String idOfGroupOfPermittedUsersAsString = metadataDefinitionModel.getIdOfGroupOfPermittedUsers();
			Long idOfGroupOfPermittedUsers = Long.valueOf(idOfGroupOfPermittedUsersAsString);
			groupOfPermittedUsers = groupService.getGroupById(idOfGroupOfPermittedUsers);
		}
		metadataDefinition.setGroupOfPermittedUsers(groupOfPermittedUsers);
		
		metadataDefinition.setAutoCompleteWithCurrentUser(metadataDefinitionModel.isAutoCompleteWithCurrentUser());
		metadataDefinition.setAutoCompleteWithCurrentUserStateCode(metadataDefinitionModel.getAutoCompleteWithCurrentUserStateCode());		
		
		return metadataDefinition;
	}
}