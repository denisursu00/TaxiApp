import { JsonDateConverter } from "@app/shared/json-mapper";
import { JsonObject, JsonProperty } from "json2typescript";

@JsonObject
export class CarCategoryModel {

    @JsonProperty("id", Number)
	public id: number = null;

    @JsonProperty("code", String)
	public code: string = null;

    @JsonProperty("name", String)
	public name: string = null;

}