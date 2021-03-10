import { JsonObject, JsonProperty } from "json2typescript";
import { JsonDateConverter } from "../../json-mapper";

@JsonObject
export class DiplomaMembruReprezentantiComisieSauGLModel {

	@JsonProperty("id", Number)
	public id: number = null;

	@JsonProperty("denumire", String)
	public denumire: string = null;

	@JsonProperty("an", String)
	public an: String = null;

	@JsonProperty("observatii", String, true)
	public observatii: string = null;

	public clone(): DiplomaMembruReprezentantiComisieSauGLModel {
		let clone: DiplomaMembruReprezentantiComisieSauGLModel = new DiplomaMembruReprezentantiComisieSauGLModel();
		clone.id = this.id;
		clone.denumire = this.denumire;
		clone.an = this.an;
		clone.observatii = this.observatii;
		return clone;
	}
}