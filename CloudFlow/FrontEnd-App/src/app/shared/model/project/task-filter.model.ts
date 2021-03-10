import { JsonObject, JsonProperty } from "json2typescript";

@JsonObject
export class TaskFilterModel {
	
	@JsonProperty("propertyName", String)
	public propertyName: string = null;
	
	@JsonProperty("values", [String])
	public values: string[] = [];

	@JsonProperty("valueType", String)
	public valueType: string = null;
	
	@JsonProperty("matchMode", String)
	public matchMode: string = null;

	@JsonProperty("aplicability", String)
	public aplicability: string = null;
}

export enum TaskFilterValueType {

	STRING = "STRING",
	INTEGER = "INTEGER",
	DECIMAL = "DECIMAL",
	DATE = "DATE",
	BOOLEAN = "BOOLEAN"
}

export enum TaskFilterMatchMode {

	EQUAL = "EQUAL",
	LIKE = "LIKE",
	BETWEEN = "BETWEEN",
	IN = "IN",
	GREATER = "GREATER",
	LOWER = "LOWER"
}

export enum TaskFilterApplicability {

	TASK = "TASK",
	ASSIGNMENTS = "ASSIGNMENTS",
	SUBACTIVITY = "SUBACTIVITY"
}