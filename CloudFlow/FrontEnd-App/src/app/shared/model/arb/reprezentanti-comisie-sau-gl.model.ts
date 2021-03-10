import { JsonObject, JsonProperty } from "json2typescript";
import { JsonDateConverter } from "../../json-mapper";
import { ObjectUtils, DateUtils } from "./../../utils";
import { MembruReprezentantiComisieSauGLModel } from "./membru-reprezentanti-comisie-sau-gl.model";

@JsonObject
export class ReprezentantiComisieSauGLModel {

	@JsonProperty("id", Number)
	public id: number = null;

	@JsonProperty("comisieSauGLId", Number)
	public comisieSauGLId: number = null;

	@JsonProperty("presedinteId", Number)
	public presedinteId: number = null;

	@JsonProperty("dataInceputMandatPresedinte", JsonDateConverter)
	public dataInceputMandatPresedinte: Date = null;

	@JsonProperty("dataExpirareMandatPresedinte", JsonDateConverter)
	public dataExpirareMandatPresedinte: Date = null;

	@JsonProperty("vicepresedinte1Id", Number)
	public vicepresedinte1Id: number = null;

	@JsonProperty("dataInceputMandatVicepresedinte1", JsonDateConverter)
	public dataInceputMandatVicepresedinte1: Date = null;

	@JsonProperty("dataExpirareMandatVicepresedinte1", JsonDateConverter)
	public dataExpirareMandatVicepresedinte1: Date = null;

	@JsonProperty("vicepresedinte2Id", Number, true)
	public vicepresedinte2Id: number = null;

	@JsonProperty("dataInceputMandatVicepresedinte2", JsonDateConverter, true)
	public dataInceputMandatVicepresedinte2: Date = null;
	
	@JsonProperty("dataExpirareMandatVicepresedinte2", JsonDateConverter, true)
	public dataExpirareMandatVicepresedinte2: Date = null;

	@JsonProperty("vicepresedinte3Id", Number, true)
	public vicepresedinte3Id: number = null;

	@JsonProperty("dataInceputMandatVicepresedinte3", JsonDateConverter, true)
	public dataInceputMandatVicepresedinte3: Date = null;

	@JsonProperty("dataExpirareMandatVicepresedinte3", JsonDateConverter, true)
	public dataExpirareMandatVicepresedinte3: Date = null;

	@JsonProperty("responsabilARBId", Number)
	public responsabilARBId: number = null;

	@JsonProperty("membruCDCoordonatorId", Number)
	public membruCDCoordonatorId: number = null;

	@JsonProperty("dataInceputMandatMembruCDCoordonator", JsonDateConverter)
	public dataInceputMandatMembruCDCoordonator: Date = null;

	@JsonProperty("dataExpirareMandatMembruCDCoordonator", JsonDateConverter)
	public dataExpirareMandatMembruCDCoordonator: Date = null;

	@JsonProperty("membri", [MembruReprezentantiComisieSauGLModel])
	public membri: MembruReprezentantiComisieSauGLModel[] = null;
}