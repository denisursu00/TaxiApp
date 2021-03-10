import { JsonObject, JsonProperty } from "json2typescript";

@JsonObject
export class ReprezentantiBancaPerFunctieSiComisieReportRowModel {

	@JsonProperty("numeComisie", String)
	public numeComisie: string = null;

	@JsonProperty("institutie", String)
	public institutie: string = null;

	@JsonProperty("numeParticipant", String)
	public numeParticipant: string = null;

	@JsonProperty("functie", String)
	public functie: string = null;

	@JsonProperty("email", String)
	public email: string = null;
    
}