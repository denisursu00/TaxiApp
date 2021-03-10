import { JsonObject, JsonProperty } from "json2typescript";
import { NomenclatorAttributeModel } from "./nomenclator-attribute.model";

@JsonObject
export class NomenclatorModel {
	
	@JsonProperty("id", Number)
	public id: number = null;

	@JsonProperty("name", String)
	public name: string = null;

	@JsonProperty("description", String)
	public description: string = null;

	@JsonProperty("attributes", [NomenclatorAttributeModel])
	public attributes: NomenclatorAttributeModel[] = null;

	@JsonProperty("uiAttributeNames", [String])
	public uiAttributeNames: String[] = null;
}