import { JsonProperty, JsonObject } from "json2typescript";

@JsonObject
export class DirectoryUserSearchCriteriaModel {

	@JsonProperty("firstName", String)
	public firstName: string;
	@JsonProperty("lastName", String)
	public lastName: string;
	@JsonProperty("username", String)
	public username: string;
}