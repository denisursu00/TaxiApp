import { JsonObject, JsonProperty } from "json2typescript";

@JsonObject
export class DeactivationUserModel {

	@JsonProperty("userId", String)
	public userId: string = null;
	
	@JsonProperty("deactivationMode", String)
	public deactivationMode: string = null;
}