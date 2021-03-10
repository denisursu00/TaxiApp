import { TransitionNotificationModelType } from "./transition-notification-model-type";
import { TransitionNotificationModel } from "./transition-notification.model";
import { JsonObject, JsonProperty } from "json2typescript";

@JsonObject
export class UserMetadataTransitionNotificationModel extends TransitionNotificationModel {

	public constructor() {
		super(TransitionNotificationModelType.METADATA);
	}

	@JsonProperty("metadataName", String)
	public metadataName: string;
}