import { JsonObject, JsonProperty } from "json2typescript";
import { JsonDateConverter } from "../../json-mapper";
import { RegistruIntrariAtasamentModel } from "./registru-intrari-atasament.model";
import { ProjectSubactivityModel } from "../project/project-subactivity.model";

@JsonObject
export class RegistruIntrariModel {

	@JsonProperty("id", Number)
	public id: number = null;

	@JsonProperty("numarInregistrare", String)
	public numarInregistrare: string = null;

	@JsonProperty("dataInregistrare", JsonDateConverter)
	public dataInregistrare: Date = null;
	
	@JsonProperty("emitentId", Number)
	public emitentId: number = null;
	
	@JsonProperty("numeEmitent", String)
	public numeEmitent: string = null;
	
	@JsonProperty("departamentEmitent", String)
	public departamentEmitent: string = null;
	
	@JsonProperty("numarDocumentEmitent", String)
	public numarDocumentEmitent: string = null;
	
	@JsonProperty("dataDocumentEmitent", JsonDateConverter)
	public dataDocumentEmitent: Date = null;
	
	@JsonProperty("tipDocumentId", Number)
	public tipDocumentId: number = null;

	@JsonProperty("trimisPeMail", Boolean)
	public trimisPeMail: boolean = false;

	@JsonProperty("continut", String)
	public continut: string = null;

	@JsonProperty("numarPagini", Number)
	public numarPagini: number = null;

	@JsonProperty("numarAnexe", Number)
	public numarAnexe: number = null;

	@JsonProperty("repartizatCatreUserId", Number)
	public repartizatCatreUserId: number = null;

	@JsonProperty("comisieSauGLIds", [Number])
	public comisieSauGLIds: number[] = null;

	@JsonProperty("proiectIds", [Number])
	public proiectIds: number[] = null;

	@JsonProperty("necesitaRaspuns", Boolean)
	public necesitaRaspuns: boolean = false;

	@JsonProperty("termenRaspuns", JsonDateConverter)
	public termenRaspuns: Date = null;

	@JsonProperty("numarInregistrareOfRegistruIesiri", Number)
	public numarInregistrareOfRegistruIesiri: number = null;

	@JsonProperty("observatii", String)
	public observatii: string = null;

	@JsonProperty("raspunsuriBanciCuPropuneri", String)
	public raspunsuriBanciCuPropuneri: string = null;

	@JsonProperty("nrZileIntrareEmitent", Number)
	public nrZileIntrareEmitent: number = null;

	@JsonProperty("nrZileRaspunsIntrare", Number)
	public nrZileRaspunsIntrare: number = null;

	@JsonProperty("nrZileRaspunsEmitent", Number)
	public nrZileRaspunsEmitent: number = null;

	@JsonProperty("nrZileTermenDataRaspuns", Number)
	public nrZileTermenDataRaspuns: number = null;

	@JsonProperty("motivAnulare", String)
	public motivAnulare: string = null;

	@JsonProperty("atasamente", [RegistruIntrariAtasamentModel])
	public atasamente: RegistruIntrariAtasamentModel[] = null;
	
	@JsonProperty("subactivity", ProjectSubactivityModel)
	public subactivity: ProjectSubactivityModel = null;
}