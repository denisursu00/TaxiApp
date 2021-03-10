import { JsonObject, JsonProperty } from "json2typescript";

@JsonObject
export class AppError {

	@JsonProperty()
	public errorCode: string = null;

	@JsonProperty()
	public errorDetails: string = null;
}