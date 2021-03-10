import { JsonObject, JsonProperty } from "json2typescript";
import { TransitionNotificationModelType } from "./transition-notification-model-type";

@JsonObject
export abstract class TransitionNotificationModel {

	protected constructor(type: TransitionNotificationModelType) {
		this.type = type;
	}

	@JsonProperty("id", Number)
	public id: number = null;

	@JsonProperty("type", String)
	public type: TransitionNotificationModelType;

	@JsonProperty("emailSubjectTemplate", String)
	public emailSubjectTemplate: string = null;

	@JsonProperty("emailContentTemplate", String)
	public emailContentTemplate: string = null;

	// Se foloseste doar in interfata
	public details?: string;
}