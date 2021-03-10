package ro.cloudSoft.cloudDoc.plugins.bpm.notifications;

import java.util.Collection;

import ro.cloudSoft.cloudDoc.domain.organization.OrganizationEntityType;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;

public class DestinationForAssignedEntitiesTransitionNotificationProvider extends AbstractDestinationForTransitionNotificationProvider {
	
	private final Collection<Long> idsForAssignedUsers;
	private final Collection<Long> idsForAssignedOrganizationUnits;
	private final Collection<Long> idsForAssignedGroups;
	
	public DestinationForAssignedEntitiesTransitionNotificationProvider(Collection<Long> idsForAssignedUsers,
			Collection<Long> idsForAssignedOrganizationUnits, Collection<Long> idsForAssignedGroups) {
		
		this.idsForAssignedUsers = idsForAssignedUsers;
		this.idsForAssignedOrganizationUnits = idsForAssignedOrganizationUnits;
		this.idsForAssignedGroups = idsForAssignedGroups;
	}
	
	@Override
	public Multimap<OrganizationEntityType, Long> getIdsForDestinationOrganizationEntities() {
		
		Multimap<OrganizationEntityType, Long> idsForDestinationOrganizationEntities = HashMultimap.create();
		
		idsForDestinationOrganizationEntities.putAll(OrganizationEntityType.USER, idsForAssignedUsers);
		idsForDestinationOrganizationEntities.putAll(OrganizationEntityType.ORGANIZATION_UNIT, idsForAssignedOrganizationUnits);
		idsForDestinationOrganizationEntities.putAll(OrganizationEntityType.GROUP, idsForAssignedGroups);
		
		return idsForDestinationOrganizationEntities;
	}
}