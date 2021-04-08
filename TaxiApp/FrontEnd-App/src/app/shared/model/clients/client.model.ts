import { JsonObject, JsonProperty } from "json2typescript";

@JsonObject
export class ClientModel {

    @JsonProperty("id", Number)
	public id: number = null;

    @JsonProperty("userId", Number)
	public userId: number = null;

}