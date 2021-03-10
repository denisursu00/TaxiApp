import { TransitionNotificationModelType } from "./transition-notification-model-type";
import { TransitionNotificationModel } from "./transition-notification.model";
import { JsonObject, JsonProperty } from "json2typescript";

@JsonObject
export class HierarchicalSuperiorOfUserMetadataTransitionNotificationModel extends TransitionNotificationModel {

	public constructor() {
		super(TransitionNotificationModelType.HIERARCHICAL_SUPERIOR_OF_USER_METADATA);
	}

	@JsonProperty("metadataName", String)
	public metadataName: string;
}