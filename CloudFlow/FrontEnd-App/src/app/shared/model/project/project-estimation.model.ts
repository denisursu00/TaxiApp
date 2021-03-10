import { JsonObject, JsonProperty } from "json2typescript";
import { JsonDateConverter } from "../../json-mapper";

@JsonObject
export class ProjectEstimationModel {

	@JsonProperty("id", Number)
	public id: number = null;
	
	@JsonProperty("estimationInPercent", Number)
	public estimationInPercent: number = null;
	
	@JsonProperty("startDate", JsonDateConverter)
	public startDate: Date = null;
	
	@JsonProperty("endDate", JsonDateConverter)
	public endDate: Date = null;

	@JsonProperty("projectId", Number)
	public projectId: number = null;
}