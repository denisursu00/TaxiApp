import { Injectable } from "@angular/core";
import { environment } from "@app/../environments/environment";
import { JsonConvert, JsonConverter, JsonCustomConvert, OperationMode, ValueCheckingMode } from "json2typescript";
import { ObjectUtils } from "./utils/object-utils";
import { ArrayUtils } from "./utils/array-utils";
import { ManuallyChosenEntitiesTransitionNotificationModel, UserMetadataTransitionNotificationModel, TransitionNotificationModel, HierarchicalSuperiorOfUserMetadataTransitionNotificationModel } from "./model/bpm/notifications";
import { OrganizationEntityModel } from "./model/organization-entity.model";

@Injectable()
export class JsonMapper {

	private getJsonConverter(): JsonConvert {
		let jsonConvert: JsonConvert = new JsonConvert();
		if (!environment.production) {
			// jsonConvert.operationMode = OperationMode.LOGGING; // print some debug data
		}
		jsonConvert.ignorePrimitiveChecks = false; // don't allow assigning number to string etc.
		jsonConvert.valueCheckingMode = ValueCheckingMode.ALLOW_NULL;
		return jsonConvert;
	}

	public deserialize(json: any, classReference: { new (): any;}): any {		
		return this.getJsonConverter().deserialize(json, classReference);
	}

	public serialize(data: any): any {
		return this.getJsonConverter().serialize(data);
	}
}

@JsonConverter
export class JsonDateConverter implements JsonCustomConvert<Date> {

	serialize(date: Date): any {
		if (date !== null && date !== undefined) {
			return date.toISOString();
		}
		return null;
	}

	deserialize(date: any): Date {
		if (date !== null && date !== undefined) {
			return new Date(date);
		}
		return null;
	}
}



@JsonConverter
export class JsonTransitionNotificationsConverter implements JsonCustomConvert<TransitionNotificationModel[]> {

	serialize(notifications: TransitionNotificationModel[]): TransitionNotificationModel[] {
		if (ArrayUtils.isEmpty(notifications)) {
			return [];
		}
		let notificationSerialized: any[] = [];
		notifications.forEach((notification: TransitionNotificationModel) => {
			if (notification instanceof ManuallyChosenEntitiesTransitionNotificationModel) {
				let manuallyChosenEntityNotification: ManuallyChosenEntitiesTransitionNotificationModel = new ManuallyChosenEntitiesTransitionNotificationModel();
				manuallyChosenEntityNotification.id = notification.id;
				manuallyChosenEntityNotification.emailContentTemplate = notification.emailContentTemplate;
				manuallyChosenEntityNotification.emailSubjectTemplate = notification.emailSubjectTemplate;
				manuallyChosenEntityNotification.manuallyChosenEntities = notification.manuallyChosenEntities;
				manuallyChosenEntityNotification.type = notification.type;
				notificationSerialized.push(manuallyChosenEntityNotification); 
			} else if (notification instanceof UserMetadataTransitionNotificationModel) {
				let userMetadataNotification: UserMetadataTransitionNotificationModel = new UserMetadataTransitionNotificationModel();
				userMetadataNotification.id = notification.id;
				userMetadataNotification.emailContentTemplate = notification.emailContentTemplate;
				userMetadataNotification.emailSubjectTemplate = notification.emailSubjectTemplate;
				userMetadataNotification.metadataName = notification.metadataName;
				userMetadataNotification.type = notification.type;
				notificationSerialized.push(userMetadataNotification);
			} else if (notification instanceof HierarchicalSuperiorOfUserMetadataTransitionNotificationModel) {
				let hierarchicalSuperiorOfUserMetadataTransitionNotificationModel: HierarchicalSuperiorOfUserMetadataTransitionNotificationModel = new HierarchicalSuperiorOfUserMetadataTransitionNotificationModel();
				hierarchicalSuperiorOfUserMetadataTransitionNotificationModel.id = notification.id;
				hierarchicalSuperiorOfUserMetadataTransitionNotificationModel.emailContentTemplate = notification.emailContentTemplate;
				hierarchicalSuperiorOfUserMetadataTransitionNotificationModel.emailSubjectTemplate = notification.emailSubjectTemplate;
				hierarchicalSuperiorOfUserMetadataTransitionNotificationModel.metadataName = notification.metadataName;
				hierarchicalSuperiorOfUserMetadataTransitionNotificationModel.type = notification.type;
				notificationSerialized.push(hierarchicalSuperiorOfUserMetadataTransitionNotificationModel);
			} else {
				notificationSerialized.push(notification);
			}
		});
		return notificationSerialized;
	}

	deserialize(notifications: any[]): TransitionNotificationModel[] {
		if (ArrayUtils.isEmpty(notifications)) {
			return [];
		}
		let notificationsDeserialized: any[] = [];		
		notifications.forEach(notification => {
			if (ObjectUtils.isNotNullOrUndefined(notification.manuallyChosenEntities)) {
				let manuallyChosenEntityNotification: ManuallyChosenEntitiesTransitionNotificationModel = new ManuallyChosenEntitiesTransitionNotificationModel();
				manuallyChosenEntityNotification.id = notification.id;
				manuallyChosenEntityNotification.emailContentTemplate = notification.emailContentTemplate;
				manuallyChosenEntityNotification.emailSubjectTemplate = notification.emailSubjectTemplate;
				manuallyChosenEntityNotification.manuallyChosenEntities = [];
				
				notification.manuallyChosenEntities.forEach((manuallyChosenEntity: OrganizationEntityModel) => {
					let entity: OrganizationEntityModel = new OrganizationEntityModel();
					entity.id = manuallyChosenEntity.id;
					entity.type = manuallyChosenEntity.type;
					manuallyChosenEntityNotification.manuallyChosenEntities.push(entity);
				});

				manuallyChosenEntityNotification.type = notification.type;
				notificationsDeserialized.push(manuallyChosenEntityNotification);
			} else if (ObjectUtils.isNotNullOrUndefined(notification.metadataName)) {
				let userMetadataNotification: UserMetadataTransitionNotificationModel = new UserMetadataTransitionNotificationModel();
				userMetadataNotification.id = notification.id;
				userMetadataNotification.emailContentTemplate = notification.emailContentTemplate;
				userMetadataNotification.emailSubjectTemplate = notification.emailSubjectTemplate;
				userMetadataNotification.metadataName = notification.metadataName;
				userMetadataNotification.type = notification.type;
				notificationsDeserialized.push(userMetadataNotification);
			} else if (ObjectUtils.isNotNullOrUndefined(notification.metadataName)) {
				let hierarchicalSuperiorOfUserMetadataTransitionNotificationModel: HierarchicalSuperiorOfUserMetadataTransitionNotificationModel = new HierarchicalSuperiorOfUserMetadataTransitionNotificationModel();
				hierarchicalSuperiorOfUserMetadataTransitionNotificationModel.id = notification.id;
				hierarchicalSuperiorOfUserMetadataTransitionNotificationModel.emailContentTemplate = notification.emailContentTemplate;
				hierarchicalSuperiorOfUserMetadataTransitionNotificationModel.emailSubjectTemplate = notification.emailSubjectTemplate;
				hierarchicalSuperiorOfUserMetadataTransitionNotificationModel.metadataName = notification.metadataName;
				hierarchicalSuperiorOfUserMetadataTransitionNotificationModel.type = notification.type;
				notificationsDeserialized.push(hierarchicalSuperiorOfUserMetadataTransitionNotificationModel);
			} else {
				let transitionNotification: TransitionNotificationModel = {
					id: notification.id,
					emailContentTemplate: notification.emailContentTemplate,
					emailSubjectTemplate: notification.emailSubjectTemplate,
					type: notification.type
				};
				notificationsDeserialized.push(transitionNotification);
			}
		});
		return notificationsDeserialized;
	}
}