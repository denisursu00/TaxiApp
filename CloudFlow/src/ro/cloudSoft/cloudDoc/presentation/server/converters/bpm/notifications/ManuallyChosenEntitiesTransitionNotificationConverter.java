package ro.cloudSoft.cloudDoc.presentation.server.converters.bpm.notifications;

import java.util.List;
import java.util.Set;

import ro.cloudSoft.cloudDoc.domain.bpm.notifications.ManuallyChosenEntitiesTransitionNotification;
import ro.cloudSoft.cloudDoc.domain.organization.OrganizationEntity;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.OrganizationEntityModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.bpm.notifications.ManuallyChosenEntitiesTransitionNotificationModel;
import ro.cloudSoft.cloudDoc.presentation.server.converters.organization.OrganizationEntityConverter;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

public class ManuallyChosenEntitiesTransitionNotificationConverter implements SpecificTransitionNotificationConverter<ManuallyChosenEntitiesTransitionNotification, ManuallyChosenEntitiesTransitionNotificationModel> {

	@Override
	public Class<ManuallyChosenEntitiesTransitionNotification> getNotificationClass() {
		return ManuallyChosenEntitiesTransitionNotification.class;
	}
	
	@Override
	public Class<ManuallyChosenEntitiesTransitionNotificationModel> getNotificationModelClass() {
		return ManuallyChosenEntitiesTransitionNotificationModel.class;
	}
	
	@Override
	public ManuallyChosenEntitiesTransitionNotification createNewInstance() {
		return new ManuallyChosenEntitiesTransitionNotification();
	}
	
	@Override
	public ManuallyChosenEntitiesTransitionNotificationModel createNewModelInstance() {
		return new ManuallyChosenEntitiesTransitionNotificationModel();
	}
	
	@Override
	public void setSpecificPropertiesToModel(ManuallyChosenEntitiesTransitionNotification transitionNotification,
			ManuallyChosenEntitiesTransitionNotificationModel transitionNotificationModel) {
		
		List<OrganizationEntityModel> manuallyChosenEntityModels = Lists.newArrayList();
		for (OrganizationEntity manuallyChosenEntity : transitionNotification.getManuallyChosenEntities()) {
			OrganizationEntityModel manuallyChosenEntityModel = OrganizationEntityConverter.getModelFromOrganizationEntity(manuallyChosenEntity);
			manuallyChosenEntityModels.add(manuallyChosenEntityModel);
		}
		transitionNotificationModel.setManuallyChosenEntities(manuallyChosenEntityModels);
	}
	
	@Override
	public void setSpecificPropertiesFromModel(ManuallyChosenEntitiesTransitionNotificationModel transitionNotificationModel,
			ManuallyChosenEntitiesTransitionNotification transitionNotification) {
		
		Set<OrganizationEntity> manuallyChosenEntities = Sets.newHashSet();
		for (OrganizationEntityModel manuallyChosenEntityModel : transitionNotificationModel.getManuallyChosenEntities()) {
			OrganizationEntity manuallyChosenEntity = OrganizationEntityConverter.getOrganizationEntityFromModel(manuallyChosenEntityModel);
			manuallyChosenEntities.add(manuallyChosenEntity);
		}
		transitionNotification.setManuallyChosenEntities(manuallyChosenEntities);
	}
}