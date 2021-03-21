import { JsonObject, JsonProperty } from "json2typescript";

@JsonObject
export class ParameterModel {

	@JsonProperty("id", Number)
	public id: number = null;
	
	@JsonProperty("name", String)
	public name: string = null;
	
	@JsonProperty("description", String)
	public description: string = null;
	
	@JsonProperty("value", String)
	public value: string = null;
	
	@JsonProperty("type", String)
	public type: string = null;
}


export enum ParameterType {

	STRING = "STRING",
	NUMBER = "NUMBER",
	DATE = "DATE",
	BOOLEAN = "BOOLEAN"
}