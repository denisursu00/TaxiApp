import { JsonDateConverter } from "@app/shared/json-mapper";
import { JsonObject, JsonProperty } from "json2typescript";

@JsonObject
export class RoleModel {

    @JsonProperty("id", Number)
	public id: number = null;

    @JsonProperty("name", String)
	public name: string = null;

    @JsonProperty("description", String)
	public description: string = null;

}