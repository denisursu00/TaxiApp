package ro.cloudSoft.cloudDoc.presentation.client.client.document.metadata;

import ro.cloudSoft.cloudDoc.presentation.client.shared.model.content.UserMetadataDefinitionModel;

public class UserMetadataFieldFactory {

	public static UserMetadataField forMetadataDefinition(UserMetadataDefinitionModel metadataDefinition) {
		if (metadataDefinition.isOnlyUsersFromGroup()) {
			String groupId = metadataDefinition.getIdOfGroupOfPermittedUsers();
			if (groupId == null) {
				String exceptionMessage = "Metadata de tip user cu ID-ul [" + metadataDefinition.getId() + "] " +
					"si numele [" + metadataDefinition.getName() + "] are pusa restrictie pe grup, insa " +
					"ID-ul grupului NU este specificat.";
				throw new IllegalStateException(exceptionMessage);
			}
			return new UserInGroupMetadataField(metadataDefinition);
		} else {
			return new UserMetadataField(metadataDefinition);
		}
	}
}