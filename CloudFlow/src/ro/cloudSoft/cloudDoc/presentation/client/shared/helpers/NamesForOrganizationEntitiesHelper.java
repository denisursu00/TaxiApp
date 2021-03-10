package ro.cloudSoft.cloudDoc.presentation.client.shared.helpers;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import ro.cloudSoft.cloudDoc.presentation.client.shared.converters.OrganizationEntityModelToSpecificEntityConverter;
import ro.cloudSoft.cloudDoc.presentation.client.shared.data.AppStoreCache;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.OrganizationEntityModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.organization.GroupModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.organization.OrganizationUnitModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.organization.UserModel;

import com.extjs.gxt.ui.client.data.ModelData;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.store.TreeStore;

public class NamesForOrganizationEntitiesHelper {
	
	private static final String VALUE_WHEN_ENTITY_NOT_FOUND = "?";

	private final TreeStore<ModelData> organizationTreeStore;
	private final ListStore<GroupModel> groupListStore;
	
	private final Collection<OrganizationEntityModel> organizationEntities;
	
	public NamesForOrganizationEntitiesHelper(Collection<OrganizationEntityModel> organizationEntities) {
		
		organizationTreeStore = AppStoreCache.getOrganizationTreeStore();
		groupListStore = AppStoreCache.getGroupListStore();
		
		this.organizationEntities = organizationEntities;
	}
	
	public List<String> getNames() {
		
		List<String> namesForOrganizationEntities = new ArrayList<String>(organizationEntities.size());
		
		for (OrganizationEntityModel organizationEntity : organizationEntities) {
			
			String nameForOrganizationEntity = null;
			if (organizationEntity.getType().equals(OrganizationEntityModel.TYPE_USER)) {
				nameForOrganizationEntity = getNameForUser(organizationEntity);
			} else if (organizationEntity.getType().equals(OrganizationEntityModel.TYPE_ORG_UNIT)) {
				nameForOrganizationEntity = getNameForOrganizationUnit(organizationEntity);
			} else if (organizationEntity.getType().equals(OrganizationEntityModel.TYPE_GROUP)) {
				nameForOrganizationEntity = getNameForGroup(organizationEntity);
			} else {
				throw new IllegalArgumentException("Tip necunoscut de entitate organizatorica: [" + organizationEntity.getType() + "]");
			}
			
			namesForOrganizationEntities.add(nameForOrganizationEntity);
		}
		
		return namesForOrganizationEntities;
	}
	
	private String getNameForUser(OrganizationEntityModel userAsOrganizationEntity) {
		String userId = OrganizationEntityModelToSpecificEntityConverter.getUserId(userAsOrganizationEntity);
		ModelData foundUserAsModelData = organizationTreeStore.findModel(UserModel.USER_PROPERTY_USERID, userId);
		if (foundUserAsModelData == null) {
			return getValueWhenEntityNotFound();
		} else {
			if (!(foundUserAsModelData instanceof UserModel)) {
				throw new IllegalStateException("Entitatea gasita in TreeStore nu este user: [" + foundUserAsModelData + "].");
			}
			UserModel foundUser = (UserModel) foundUserAsModelData;
			return foundUser.getDisplayName();
		}
	}
	
	private String getNameForOrganizationUnit(OrganizationEntityModel organizationUnitAsOrganizationEntity) {
		String organizationUnitId = OrganizationEntityModelToSpecificEntityConverter.getOrganizationUnitId(organizationUnitAsOrganizationEntity);
		ModelData foundOrganizationUnitAsModelData = organizationTreeStore.findModel(OrganizationUnitModel.PROPERTY_ID, organizationUnitId);
		if (foundOrganizationUnitAsModelData == null) {
			return getValueWhenEntityNotFound();
		} else {
			if (!(foundOrganizationUnitAsModelData instanceof OrganizationUnitModel)) {
				throw new IllegalStateException("Entitatea gasita in TreeStore nu este unitate organizatorica: [" + foundOrganizationUnitAsModelData + "].");
			}
			OrganizationUnitModel foundOrganizationUnit = (OrganizationUnitModel) foundOrganizationUnitAsModelData;
			return foundOrganizationUnit.getDisplayName();
		}
	}
	
	private String getNameForGroup(OrganizationEntityModel groupAsOrganizationEntity) {
		String groupId = OrganizationEntityModelToSpecificEntityConverter.getGroupId(groupAsOrganizationEntity);
		GroupModel foundGroup = groupListStore.findModel(GroupModel.PROPERTY_ID, groupId);
		if (foundGroup == null) {
			return getValueWhenEntityNotFound();
		} else {
			return foundGroup.getName();
		}
	}
	
	private String getValueWhenEntityNotFound() {
		return VALUE_WHEN_ENTITY_NOT_FOUND;
	}
}