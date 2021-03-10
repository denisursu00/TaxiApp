import { JsonObject, JsonProperty } from "json2typescript";
import { RegistruIesiriDestinatariModel } from "./registru-iesiri-destinatari.model";
import { JsonDateConverter } from "../../json-mapper";
import { RegistruIesiriAtasamentModel } from "./registru-iesiri-atasamente.model";
import { ProjectSubactivityModel } from "../project/project-subactivity.model";

@JsonObject
export class RegistruIesiriModel {

	@JsonProperty("id", Number)
	public id: number = null;

	@JsonProperty("numarInregistrare", String)
	public numarInregistrare: string = null;
	
	@JsonProperty("dataInregistrare", JsonDateConverter)
	public dataInregistrare: Date = null;
	
	@JsonProperty("tipDocumentId", Number)
	public tipDocumentId: number = null;

	@JsonProperty("numarPagini", Number)
	public numarPagini: number = null;

	@JsonProperty("numarAnexe", Number)
	public numarAnexe: number = null;

	@JsonProperty("intocmitDeUserId", Number)
	public intocmitDeUserId: number = null;

	@JsonProperty("trimisPeMail", Boolean)
	public trimisPeMail: boolean = false;

	@JsonProperty("continut", String)
	public continut: string = null;

	@JsonProperty("asteptamRaspuns", Boolean)
	public asteptamRaspuns: boolean = false;

	@JsonProperty("termenRaspuns", JsonDateConverter)
	public termenRaspuns: Date = null;

	@JsonProperty("destinatari", [RegistruIesiriDestinatariModel])
	public destinatari: RegistruIesiriDestinatariModel[] = [];

	@JsonProperty("proiectIds", [Number])
	public proiectIds: number[] = [];

	@JsonProperty("anulat", Boolean)
	public anulat: boolean = false;

	@JsonProperty("inchis", Boolean)
	public inchis: boolean = false;

	@JsonProperty("motivAnulare", String)
	public motivAnulare: string = null;
	
	@JsonProperty("atasamente", [RegistruIesiriAtasamentModel])
	public atasamente:  RegistruIesiriAtasamentModel[] = [];
	
	@JsonProperty("subactivity", ProjectSubactivityModel)
	public subactivity: ProjectSubactivityModel = null;
}