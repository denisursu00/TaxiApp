import { JsonObject, JsonProperty } from "json2typescript";
import { JsonDateConverter } from "../../json-mapper";


@JsonObject
export class NumarSedinteComisieGlReportRowModel {

	@JsonProperty("numeComisieGl", String)
	public numeComisieGl: string = null;

	@JsonProperty("dataSedinta", JsonDateConverter)
	public dataSedinta: Date = null;

}