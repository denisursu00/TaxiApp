package ro.cloudSoft.cloudDoc.plugins.bpm.notifications;

import ro.cloudSoft.cloudDoc.domain.organization.OrganizationEntityType;

import com.google.common.collect.Multimap;

public abstract class AbstractDestinationForTransitionNotificationProvider {
	
	public abstract Multimap<OrganizationEntityType, Long> getIdsForDestinationOrganizationEntities();
}