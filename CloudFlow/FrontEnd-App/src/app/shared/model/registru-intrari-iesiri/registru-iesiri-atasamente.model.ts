import { JsonObject, JsonProperty } from "json2typescript";

@JsonObject
export class RegistruIesiriAtasamentModel {

	@JsonProperty("id", Number)
	public id: number = null;
	
	@JsonProperty("fileName", String)
	public fileName: string = null;
}