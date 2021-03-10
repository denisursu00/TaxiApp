import { JsonObject, JsonProperty } from "json2typescript";
import { TaskFilterModel } from "./project/task-filter.model";
import { SortedTaskAttributeModel } from "./project/sorted-task-attribute.model";

@JsonObject
export class GetPagedProjectTaskViewModelsRequestModel {

	@JsonProperty("projectId", Number)
	public projectId: number = null;
	
	@JsonProperty("filters", [TaskFilterModel])
	public filters: TaskFilterModel[] = [];

	@JsonProperty("sortedAttribute", SortedTaskAttributeModel)
	public sortedAttribute: SortedTaskAttributeModel = null;
}