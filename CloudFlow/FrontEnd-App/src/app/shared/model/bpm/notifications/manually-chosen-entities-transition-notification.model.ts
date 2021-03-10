import { TransitionNotificationModelType } from "./transition-notification-model-type";
import { TransitionNotificationModel } from "./transition-notification.model";
import { OrganizationEntityModel } from "../../organization-entity.model";
import { JsonProperty, JsonObject } from "json2typescript";

@JsonObject
export class ManuallyChosenEntitiesTransitionNotificationModel extends TransitionNotificationModel {

	public constructor() {
		super(TransitionNotificationModelType.MANUALLY_CHOSEN_ENTITIES);
	}

	@JsonProperty("manuallyChosenEntities", [OrganizationEntityModel])
	public manuallyChosenEntities: OrganizationEntityModel[];
}