package ro.cloudSoft.cloudDoc.presentation.server.converters.bpm.notifications;

import java.util.Collections;
import java.util.Map;

import ro.cloudSoft.cloudDoc.domain.bpm.WorkflowTransition;
import ro.cloudSoft.cloudDoc.domain.bpm.notifications.TransitionNotification;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.bpm.notifications.TransitionNotificationModel;

import com.google.common.collect.Maps;

@SuppressWarnings({ "rawtypes", "unchecked" })
public class TransitionNotificationConverter {
	
	private static final Map<Class, SpecificTransitionNotificationConverter> CONVERTER_BY_NOTIFICATION_CLASS;
	private static final Map<Class, SpecificTransitionNotificationConverter> CONVERTER_BY_NOTIFICATION_MODEL_CLASS;
	
	static {
		
		Map<Class, SpecificTransitionNotificationConverter> converterByNotificationClass = Maps.newHashMap();
		Map<Class, SpecificTransitionNotificationConverter> converterByNotificationModelClass = Maps.newHashMap();
		
		AssignedEntityTransitionNotificationConverter assignedEntityTransitionNotificationConverter = new AssignedEntityTransitionNotificationConverter();
		converterByNotificationClass.put(assignedEntityTransitionNotificationConverter.getNotificationClass(), assignedEntityTransitionNotificationConverter);
		converterByNotificationModelClass.put(assignedEntityTransitionNotificationConverter.getNotificationModelClass(), assignedEntityTransitionNotificationConverter);
		
		InitiatorTransitionNotificationConverter initiatorTransitionNotificationConverter = new InitiatorTransitionNotificationConverter();
		converterByNotificationClass.put(initiatorTransitionNotificationConverter.getNotificationClass(), initiatorTransitionNotificationConverter);
		converterByNotificationModelClass.put(initiatorTransitionNotificationConverter.getNotificationModelClass(), initiatorTransitionNotificationConverter);
		
		HierarchicalSuperiorOfInitiatorTransitionNotificationConverter hierarchicalSuperiorOfInitiatorTransitionNotificationConverter = new HierarchicalSuperiorOfInitiatorTransitionNotificationConverter();
		converterByNotificationClass.put(hierarchicalSuperiorOfInitiatorTransitionNotificationConverter.getNotificationClass(), hierarchicalSuperiorOfInitiatorTransitionNotificationConverter);
		converterByNotificationModelClass.put(hierarchicalSuperiorOfInitiatorTransitionNotificationConverter.getNotificationModelClass(), hierarchicalSuperiorOfInitiatorTransitionNotificationConverter);
		
		ManuallyChosenEntitiesTransitionNotificationConverter manuallyChosenEntitiesTransitionNotificationConverter = new ManuallyChosenEntitiesTransitionNotificationConverter();
		converterByNotificationClass.put(manuallyChosenEntitiesTransitionNotificationConverter.getNotificationClass(), manuallyChosenEntitiesTransitionNotificationConverter);
		converterByNotificationModelClass.put(manuallyChosenEntitiesTransitionNotificationConverter.getNotificationModelClass(), manuallyChosenEntitiesTransitionNotificationConverter);
		
		UserMetadataTransitionNotificationConverter userMetadataTransitionNotificationConverter = new UserMetadataTransitionNotificationConverter();
		converterByNotificationClass.put(userMetadataTransitionNotificationConverter.getNotificationClass(), userMetadataTransitionNotificationConverter);
		converterByNotificationModelClass.put(userMetadataTransitionNotificationConverter.getNotificationModelClass(), userMetadataTransitionNotificationConverter);
		
		HierarchicalSuperiorOfUserMetadataTransitionNotificationConverter hierarchicalSuperiorOfUserMetadataTransitionNotificationConverter = new HierarchicalSuperiorOfUserMetadataTransitionNotificationConverter();
		converterByNotificationClass.put(hierarchicalSuperiorOfUserMetadataTransitionNotificationConverter.getNotificationClass(), hierarchicalSuperiorOfUserMetadataTransitionNotificationConverter);
		converterByNotificationModelClass.put(hierarchicalSuperiorOfUserMetadataTransitionNotificationConverter.getNotificationModelClass(), hierarchicalSuperiorOfUserMetadataTransitionNotificationConverter);
		
		CONVERTER_BY_NOTIFICATION_CLASS = Collections.unmodifiableMap(converterByNotificationClass);
		CONVERTER_BY_NOTIFICATION_MODEL_CLASS = Collections.unmodifiableMap(converterByNotificationModelClass);
	}

	public static TransitionNotificationModel getModel(TransitionNotification transitionNotification) {
		
		if (transitionNotification == null) {
			return null;
		}
		
		SpecificTransitionNotificationConverter specificConverter = CONVERTER_BY_NOTIFICATION_CLASS.get(transitionNotification.getClass());
		if (specificConverter == null) {
			throw new IllegalArgumentException("Tip necunoscut de notificare: [" + transitionNotification.getClass().getName() + "]");
		}
		
		TransitionNotificationModel transitionNotificationModel = specificConverter.createNewModelInstance();
		
		transitionNotificationModel.setId(transitionNotification.getId());
		transitionNotificationModel.setEmailSubjectTemplate(transitionNotification.getEmailSubjectTemplate());
		transitionNotificationModel.setEmailContentTemplate(transitionNotification.getEmailContentTemplate());
		
		specificConverter.setSpecificPropertiesToModel(transitionNotification, transitionNotificationModel);
		
		return transitionNotificationModel;
	}
	
	public static TransitionNotification getFromModel(TransitionNotificationModel transitionNotificationModel, WorkflowTransition transition) {
		
		if (transitionNotificationModel == null) {
			return null;
		}
		
		SpecificTransitionNotificationConverter specificConverter = CONVERTER_BY_NOTIFICATION_MODEL_CLASS.get(transitionNotificationModel.getClass());
		if (specificConverter == null) {
			throw new IllegalArgumentException("Tip necunoscut de model de notificare: [" + transitionNotificationModel.getClass().getName() + "]");
		}
		
		TransitionNotification transitionNotification = specificConverter.createNewInstance();
		
		transitionNotification.setId(transitionNotificationModel.getId());
		transitionNotification.setTransition(transition);
		transitionNotification.setEmailSubjectTemplate(transitionNotificationModel.getEmailSubjectTemplate());
		transitionNotification.setEmailContentTemplate(transitionNotificationModel.getEmailContentTemplate());
		
		specificConverter.setSpecificPropertiesFromModel(transitionNotificationModel, transitionNotification);
		
		return transitionNotification;
	}
}