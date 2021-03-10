import { JsonObject, JsonProperty } from "json2typescript";
import { JsonDateConverter } from "../../json-mapper";
import { DatePipe } from "@angular/common/src/pipes";
import { DateConstants } from "../../constants/date.constants";

@JsonObject
export class DocumentHistoryViewModel {

	@JsonProperty("workflowActor", String)
	public workflowActor: string = null;

	@JsonProperty("workflowTransitionName", String)
	public workflowTransitionName: string = null;

	@JsonProperty("workflowTransitionDate", JsonDateConverter)
	public workflowTransitionDate: Date = null;

	@JsonProperty("organizationDepartment", String)
	public organizationDepartment: string = null;

	public workflowTransitionDateAsString: String;
}
