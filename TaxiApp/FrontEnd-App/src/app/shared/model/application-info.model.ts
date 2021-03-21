import { JsonProperty, JsonObject } from "json2typescript";

@JsonObject
export class ApplicationInfoModel {

	@JsonProperty("environmentName", String)
	public environmentName: string = null;

	@JsonProperty("production", Boolean)
	public production: boolean = false;
}