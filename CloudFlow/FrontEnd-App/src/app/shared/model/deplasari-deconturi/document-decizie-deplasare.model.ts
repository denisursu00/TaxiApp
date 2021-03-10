import { JsonObject, JsonProperty } from "json2typescript";
import { JsonDateConverter } from "../../json-mapper";

@JsonObject
export class DocumentDecizieDeplasareModel {

	@JsonProperty("denumireInstitutie", String)
	public denumireInstitutie: string = null;

	@JsonProperty("dataDecizie", JsonDateConverter)
	public dataDecizie: Date = null;
	
	@JsonProperty("organismId", Number)
	public organismId: number = null;
	
	@JsonProperty("denumireOrganism", String)
	public denumireOrganism: string = null;
	
	@JsonProperty("abreviereOrganism", String)
	public abreviereOrganism: string = null;
	
	@JsonProperty("denumireComitet", String)
	public denumireComitet: string = null;
	
	@JsonProperty("numarDeplasariEfectuate", Number)
	public numarDeplasariEfectuate: number = null;
	
	@JsonProperty("numarDeplasariBugetateRamase", Number)
	public numarDeplasariBugetateRamase: number = null;
	
	@JsonProperty("eveniment", String)
	public eveniment: string = null;
	
	@JsonProperty("tara", String)
	public tara: string = null;
	
	@JsonProperty("oras", String)
	public oras: string = null;
	
	@JsonProperty("dataPlecare", JsonDateConverter)
	public dataPlecare: Date = null;
	
	@JsonProperty("dataSosire", JsonDateConverter)
	public dataSosire: Date = null;
	
	@JsonProperty("dataConferintaInceput", JsonDateConverter)
	public dataConferintaInceput: Date = null;
	
	@JsonProperty("dataConferintaSfarsit", JsonDateConverter)
	public dataConferintaSfarsit: Date = null;
	
	@JsonProperty("detaliiNumarDeplasariBugetateNomenclatorValueId", Number)
	public detaliiNumarDeplasariBugetateNomenclatorValueId: number = null;
}