import { JsonObject, JsonProperty } from "json2typescript";

@JsonObject
export class WorkflowStateModel {

	public static readonly STATETYPE_START: number = 1;
	public static readonly STATETYPE_INTERMEDIATE: number = 2;
	public static readonly STATETYPE_STOP: number = 3;
	
	public static readonly ATTACH_NO_PERM: number = 0;
	public static readonly ATTACH_PERM_ADD: number = 1;
	public static readonly ATTACH_PERM_DELETE: number = 2;
	public static readonly ATTACH_PERM_ALL: number = 3;

	@JsonProperty("id", Number)
	public id: number = null;

	@JsonProperty("name", String)
	public name: string = null;

	@JsonProperty("code", String)
	public code: string = null;

	@JsonProperty("stateType", Number)
	public stateType: number = null;
	
	@JsonProperty("displayStateType", String)
	public displayStateType: string = null;

	@JsonProperty("attachmentsPermission", Number)
	public attachmentsPermission: number = null;

	@JsonProperty("tempId", Number)
	public tempId: number = null; // TODO - posibil poate fi sters

	@JsonProperty("automaticRunning", Boolean)
	public automaticRunning: boolean = null;

	@JsonProperty("classPath", String)
	public classPath: string = null;

	public get stateTypeAsLabelCode() {
		if (this.stateType === WorkflowStateModel.STATETYPE_START) {
			return "STATE_START_TYPE";
		} else if (this.stateType === WorkflowStateModel.STATETYPE_INTERMEDIATE) {
			return "STATE_INTERMEDIATE_TYPE";
		} else if (this.stateType === WorkflowStateModel.STATETYPE_STOP) {
			return "STATE_STOP_TYPE";
		}
	}
}