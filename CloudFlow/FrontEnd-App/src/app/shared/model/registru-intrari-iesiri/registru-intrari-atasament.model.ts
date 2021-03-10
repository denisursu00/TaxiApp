import { JsonObject, JsonProperty } from "json2typescript";

@JsonObject
export class RegistruIntrariAtasamentModel {

	@JsonProperty("id", Number)
	public id: number = null;
	
	@JsonProperty("fileName", String)
	public fileName: string = null;
}