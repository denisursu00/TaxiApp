import { JsonObject, JsonProperty } from "json2typescript";

@JsonObject
export class DspActivitateAttachmentViewModel {

	@JsonProperty("id", Number)
	public id: number = null;

	@JsonProperty("name", String)
	public name: string = null;
}