package ro.cloudSoft.cloudDoc.presentation.client.client.document.metadata;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ro.cloudSoft.cloudDoc.presentation.client.shared.data.AppStoreCache;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.content.UserMetadataDefinitionModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.organization.GroupModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.organization.UserModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.providers.SorterProvider;

import com.extjs.gxt.ui.client.store.ListStore;

public class UserInGroupMetadataField extends UserMetadataField {
	
	private static final String PARAMETER_NAME_GROUP_ID = "groupId";
	
	private static Map<String, Object> getAdditionalParameters(String groupId) {
		Map<String, Object> additionalParameters = new HashMap<String, Object>();
		additionalParameters.put(PARAMETER_NAME_GROUP_ID,  groupId);
		return additionalParameters;
	}
	
	private static String getGroupId(Map<String, Object> additionalParameters) {
		return (String) additionalParameters.get(PARAMETER_NAME_GROUP_ID);
	}
	
	public UserInGroupMetadataField(UserMetadataDefinitionModel metadataDefinition) {
		super(metadataDefinition, getAdditionalParameters(metadataDefinition.getIdOfGroupOfPermittedUsers()));
	}

	@Override
	protected void populateStoreWithUsers(ListStore<UserModel> store, Map<String, Object> additionalParameters) {
		
		store.setStoreSorter(SorterProvider.getUserListSorter());
		
		String groupId = getGroupId(additionalParameters);
		
		ListStore<GroupModel> groupsInAppCache = AppStoreCache.getGroupListStore();
		if (groupsInAppCache.getModels().isEmpty()) {
			String exceptionMessage = "S-a incercat popularea unei metadate de tip user cu " +
				"restrictie pe grup (ID grup: [" + groupId + "]), insa grupurile NU s-au incarcat in cache.";
			throw new IllegalStateException(exceptionMessage);
		}
		
		GroupModel foundGroupWithId = groupsInAppCache.findModel(GroupModel.PROPERTY_ID, groupId);
		if (foundGroupWithId == null) {
			String exceptionMessage = "NU s-a gasit in cache grupul cu ID-ul [" + groupId + "], " +
				"necesar pentru o metadata de tip user cu restrictie pe grup.";
			throw new IllegalStateException(exceptionMessage);
		}
		
		List<UserModel> usersInGroup = foundGroupWithId.getUsers();
		store.add(usersInGroup);
	}
}