package ro.cloudSoft.cloudDoc.plugins.bpm.notifications;

import ro.cloudSoft.cloudDoc.domain.bpm.notifications.ManuallyChosenEntitiesTransitionNotification;
import ro.cloudSoft.cloudDoc.domain.organization.OrganizationEntity;
import ro.cloudSoft.cloudDoc.domain.organization.OrganizationEntityType;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;

public class DestinationForManuallyChosenEntitiesTransitionNotificationProvider extends AbstractDestinationForTransitionNotificationProvider {

	private final ManuallyChosenEntitiesTransitionNotification notification;
	
	public DestinationForManuallyChosenEntitiesTransitionNotificationProvider(ManuallyChosenEntitiesTransitionNotification notification) {
		this.notification = notification;
	}
	
	@Override
	public Multimap<OrganizationEntityType, Long> getIdsForDestinationOrganizationEntities() {
		
		if (notification.getManuallyChosenEntities().isEmpty()) {
			throw new IllegalStateException("Notificarea pentru entitati alese manual cu ID-ul [" + notification.getId() + "] NU are setate entitati.");
		}
		
		Multimap<OrganizationEntityType, Long> idsForDestinationOrganizationEntities = HashMultimap.create();
		
		for (OrganizationEntity manuallyChosenEntity : notification.getManuallyChosenEntities()) {
			if (manuallyChosenEntity.isUser()) {
				idsForDestinationOrganizationEntities.put(OrganizationEntityType.USER, manuallyChosenEntity.getId());
			} else if (manuallyChosenEntity.isOrganizationUnit()) {
				idsForDestinationOrganizationEntities.put(OrganizationEntityType.ORGANIZATION_UNIT, manuallyChosenEntity.getId());
			} else if (manuallyChosenEntity.isGroup()) {
				idsForDestinationOrganizationEntities.put(OrganizationEntityType.GROUP, manuallyChosenEntity.getId());
			} else {
				throw new IllegalStateException("Tip necunoscut de entitate organizatorica: [" + manuallyChosenEntity.getClass().getName() + "]");
			}
		}
		
		return idsForDestinationOrganizationEntities;
	}
}