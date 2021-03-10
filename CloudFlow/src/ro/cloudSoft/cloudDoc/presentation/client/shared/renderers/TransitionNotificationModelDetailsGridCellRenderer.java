package ro.cloudSoft.cloudDoc.presentation.client.shared.renderers;

import java.util.List;

import ro.cloudSoft.cloudDoc.presentation.client.shared.CustomGridCellRenderer;
import ro.cloudSoft.cloudDoc.presentation.client.shared.helpers.NamesForOrganizationEntitiesHelper;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.OrganizationEntityModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.bpm.notifications.ManuallyChosenEntitiesTransitionNotificationModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.bpm.notifications.TransitionNotificationModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.bpm.notifications.TransitionNotificationModelType;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.bpm.notifications.UserMetadataTransitionNotificationModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.utils.GwtStringUtils;

import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.widget.grid.ColumnData;
import com.extjs.gxt.ui.client.widget.grid.Grid;

public class TransitionNotificationModelDetailsGridCellRenderer extends CustomGridCellRenderer<TransitionNotificationModel> {
	
	public static final TransitionNotificationModelDetailsGridCellRenderer INSTANCE = new TransitionNotificationModelDetailsGridCellRenderer();
	
	@Override
	public Object doRender(TransitionNotificationModel notification, String property, ColumnData config, int rowIndex,
			int colIndex, ListStore<TransitionNotificationModel> store, Grid<TransitionNotificationModel> grid) {
		
		TransitionNotificationModelType notificationType = notification.getType();
		
		if (notificationType.equals(TransitionNotificationModelType.ASSIGNED_ENTITY)) {
			return getDetailsForAssignedEntityNotification(notification);
		} else if (notificationType.equals(TransitionNotificationModelType.INITIATOR)) {
			return getDetailsForInitiatorNotification(notification);
		}else if (notificationType.equals(TransitionNotificationModelType.MANUALLY_CHOSEN_ENTITIES)) {
			return getDetailsForManuallyChosenEntitiesNotification(notification);
		}else if (notificationType.equals(TransitionNotificationModelType.METADATA)) {
			return getDetailsForUserMetadataNotification(notification);
		} else {
			return CustomGridCellRenderer.VALUE_WHEN_NOTHING_TO_RENDER;
		}
	}
	
	private String getDetailsForAssignedEntityNotification(TransitionNotificationModel notification) {
		return CustomGridCellRenderer.VALUE_WHEN_NOTHING_TO_RENDER;
	}
	
	private String getDetailsForInitiatorNotification(TransitionNotificationModel notification) {
		return CustomGridCellRenderer.VALUE_WHEN_NOTHING_TO_RENDER;
	}
	
	private String getDetailsForManuallyChosenEntitiesNotification(TransitionNotificationModel notification) {
		ManuallyChosenEntitiesTransitionNotificationModel manuallyChosenEntitiesNotification = (ManuallyChosenEntitiesTransitionNotificationModel) notification;
		List<OrganizationEntityModel> manuallyChosenEntities = manuallyChosenEntitiesNotification.getManuallyChosenEntities();
		List<String> namesForManuallyChosenEntities = new NamesForOrganizationEntitiesHelper(manuallyChosenEntities).getNames();
		String joinedNamesForManuallyChosenEntities = GwtStringUtils.join(namesForManuallyChosenEntities, ", ");
		return joinedNamesForManuallyChosenEntities;
	}
	
	private String getDetailsForUserMetadataNotification(TransitionNotificationModel notification) {
		UserMetadataTransitionNotificationModel userMetadataNotification = (UserMetadataTransitionNotificationModel) notification;
		String metadataName = userMetadataNotification.getMetadataName();
		return metadataName;
	}
}