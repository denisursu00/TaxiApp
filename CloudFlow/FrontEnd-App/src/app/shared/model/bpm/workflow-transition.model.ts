import { JsonObject, JsonProperty } from "json2typescript";
import { WorkflowStateModel } from "./workflow-state.model";
import { OrganizationEntityModel } from "./../organization-entity.model";
import { TransitionNotificationModel } from "./notifications";
import { JsonTransitionNotificationsConverter } from "../../json-mapper";

@JsonObject
export class WorkflowTransitionModel {

	@JsonProperty("id", Number)
	public id: number = null;

	@JsonProperty("name", String)
	public name: string = null;

	@JsonProperty("routingCondition", String)
	public routingCondition: string = null;

	@JsonProperty("routingType", String)
	public routingType: string = null;

	@JsonProperty("routingDestinationId", Number)
	public routingDestinationId: number = null;
	
	@JsonProperty("routingDestinationParameter", String)
	public routingDestinationParameter: string = null;

	@JsonProperty("startState", WorkflowStateModel)
	public startState: WorkflowStateModel = null;

	@JsonProperty("finalState", WorkflowStateModel)
	public finalState: WorkflowStateModel = null;
	
	@JsonProperty("availableForAutomaticActionsOnly", Boolean)
	public availableForAutomaticActionsOnly: boolean = false;

	@JsonProperty("deadlineAction", Boolean)
	public deadlineAction: boolean = false;

	@JsonProperty("deadlinePeriod", Number)
	public deadlinePeriod: number = null;
	
	@JsonProperty("deadlineActionType", Number)
	public deadlineActionType: number = null;

	@JsonProperty("startStateName", String)
	public startStateName: string = null;

	@JsonProperty("startStateCode", String)
	public startStateCode: string = null;

	@JsonProperty("finalStateName", String)
	public finalStateName: string = null;

	@JsonProperty("finalStateCode", String)
	public finalStateCode: string = null;

	@JsonProperty("deadlineNotifyResendInterval", Number)
	public deadlineNotifyResendInterval: number = null;

	@JsonProperty("extraViewers", [OrganizationEntityModel])
	public extraViewers: OrganizationEntityModel[] = null;

	@JsonProperty("notifications", JsonTransitionNotificationsConverter)
	public notifications: TransitionNotificationModel[] = null;

	@JsonProperty("uiSendConfirmation", Boolean)
	public uiSendConfirmation: boolean = false;
}