import { JsonObject, JsonProperty } from "json2typescript";
import { JsonDateConverter } from "@app/shared/json-mapper";

@JsonObject
export class PrezentaAgaReportFilterModel {
    
	@JsonProperty("documentId", String)
	public documentId: string = null;

	@JsonProperty("bancaId", Number)
	public bancaId: number = null;
    
	@JsonProperty("functie", String)
	public functie: string = null;
	
	@JsonProperty("deLaDataInceput", JsonDateConverter)
	public deLaDataInceput: Date = null;
	
	@JsonProperty("panaLaDataInceput", JsonDateConverter)
	public panaLaDataInceput: Date = null;
	
}