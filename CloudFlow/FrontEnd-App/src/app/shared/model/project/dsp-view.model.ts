import { JsonObject, JsonProperty } from "json2typescript";
import { JsonDateConverter } from "../../json-mapper";
import { DspActivitateViewModel } from "./dsp-activitate-view.model";
import { DspComisieGlViewModel } from "./dsp-comisie-gl-view.model";
import { DspParticipantViewModel } from "./dsp-participant-view.model";

@JsonObject
export class DspViewModel {

	@JsonProperty("projectId", Number)
	public projectId: number = null;
	
	@JsonProperty("numeProiect", String)
	public numeProiect: string = null;

	@JsonProperty("abreviereProiect", String)
	public abreviereProiect: string = null;

	@JsonProperty("descriere", String)
	public descriere: string = null;

	@JsonProperty("initiatorProiect", String)
	public initiatorProiect: string = null;

	@JsonProperty("domeniuBancar", String)
	public domeniuBancar: string = null;

	@JsonProperty("proiectInitiatDeArb", String)
	public proiectInitiatDeArb: string = null;

	@JsonProperty("incadrareProiect", String)
	public incadrareProiect: string = null;

	@JsonProperty("arieDeCuprindere", String)
	public arieDeCuprindere: string = null;

	@JsonProperty("proiectInitiatDeAltaEntitate", String)
	public proiectInitiatDeAltaEntitate: string = null;

	@JsonProperty("dataInceputProiect", JsonDateConverter)
	public dataInceputProiect: Date = null;

	@JsonProperty("dataSfarsitProiect", JsonDateConverter)
	public dataSfarsitProiect: Date = null;

	@JsonProperty("dataImplementarii", JsonDateConverter)
	public dataImplementarii: Date = null;

	@JsonProperty("evaluareaImpactului", String)
	public evaluareaImpactului: string = null;

	@JsonProperty("gradDeImportanta", String)
	public gradDeImportanta: string = null;

	@JsonProperty("gradDeRealizareEstimatDeResponsabil", Number)
	public gradDeRealizareEstimatDeResponsabil: number = null;

	@JsonProperty("responsabilProiect", String)
	public responsabilProiect: string = null;

	@JsonProperty("autoritatiImplicate", String)
	public autoritatiImplicate: string = null;

	@JsonProperty("obiectiveProiect", String)
	public obiectiveProiect: string = null;

	@JsonProperty("cadrulLegal", String)
	public cadrulLegal: string = null;

	@JsonProperty("specificitateProiect", String)
	public specificitateProiect: string = null;

	@JsonProperty("participanti", [DspParticipantViewModel])
	public participanti: DspParticipantViewModel[] = [];

	@JsonProperty("comisiiGlImplicate", [DspComisieGlViewModel])
	public comisiiGlImplicate: DspComisieGlViewModel[] = [];

	@JsonProperty("activitati", [DspActivitateViewModel])
	public activitati: DspActivitateViewModel[] = [];
}