import { JsonObject, JsonProperty } from "json2typescript";

@JsonObject
export class PageRequest<P> {

	@JsonProperty("payload")
	public payload: P = null;

	@JsonProperty("offset", Number)
	public offset: number = null;
	
	@JsonProperty("limit", Number)
	public limit: number = null;
}