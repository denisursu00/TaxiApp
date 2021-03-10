import { JsonObject, JsonProperty } from "json2typescript";

@JsonObject
export class SortedTaskAttributeModel {

	@JsonProperty("propertyName", String)
	public propertyName: string = null;

	@JsonProperty("order", String)
	public order: string = null;
}

export enum SortedTaskAttributeOrderDirection {

	ASC = "ASC",
	DESC = "DESC"
}