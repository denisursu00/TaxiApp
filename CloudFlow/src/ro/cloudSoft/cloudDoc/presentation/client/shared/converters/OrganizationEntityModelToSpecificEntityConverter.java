package ro.cloudSoft.cloudDoc.presentation.client.shared.converters;

import ro.cloudSoft.cloudDoc.presentation.client.shared.model.OrganizationEntityModel;

public class OrganizationEntityModelToSpecificEntityConverter {

	public static String getUserId(OrganizationEntityModel organizationEntity) {
		
		if (!organizationEntity.getType().equals(OrganizationEntityModel.TYPE_USER)) {
			throw new IllegalArgumentException("Entitatea nu este de tip user, ci de tipul [" + organizationEntity.getType() + "].");
		}
		
		Long id = organizationEntity.getId();
		String userId = toStringOrNull(id);
		return userId;
	}
	
	public static String getOrganizationUnitId(OrganizationEntityModel organizationEntity) {
		
		if (!organizationEntity.getType().equals(OrganizationEntityModel.TYPE_ORG_UNIT)) {
			throw new IllegalArgumentException("Entitatea nu este de tip unitate organizatorica, ci de tipul [" + organizationEntity.getType() + "].");
		}
		
		Long id = organizationEntity.getId();
		String organizationUnitId = toStringOrNull(id);
		return organizationUnitId;
	}
	
	public static String getGroupId(OrganizationEntityModel organizationEntity) {
		
		if (!organizationEntity.getType().equals(OrganizationEntityModel.TYPE_GROUP)) {
			throw new IllegalArgumentException("Entitatea nu este de tip grup, ci de tipul [" + organizationEntity.getType() + "].");
		}
		
		Long id = organizationEntity.getId();
		String groupId = toStringOrNull(id);
		return groupId;
	}
	
	private static String toStringOrNull(Long value) {
		if (value == null) {
			return null;
		} else {
			return value.toString();
		}
	}
}