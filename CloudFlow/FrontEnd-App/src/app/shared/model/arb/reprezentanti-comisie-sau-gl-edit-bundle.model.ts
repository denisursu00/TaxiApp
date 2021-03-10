import { JsonObject, JsonProperty } from "json2typescript";
import { JsonDateConverter } from "../../json-mapper";
import { ObjectUtils, DateUtils } from "./../../utils";
import { ReprezentantiComisieSauGLModel } from "./reprezentanti-comisie-sau-gl.model";
import { NomenclatorValueModel } from "../nomenclator";

@JsonObject
export class ReprezentantiComisieSauGLEditBundleModel {
	
	@JsonProperty("reprezentanti", ReprezentantiComisieSauGLModel)
	public reprezentanti: ReprezentantiComisieSauGLModel = null;

	@JsonProperty("comisieSauGL", NomenclatorValueModel)
	public comisieSauGL: NomenclatorValueModel = null;

	@JsonProperty("persoaneNomenclatorId", Number)
	public persoaneNomenclatorId: number = null;

	@JsonProperty("membriCDNomenclatorId", Number)
	public membriCDNomenclatorId: number = null;

	@JsonProperty("institutiiNomenclatorId", Number)
	public institutiiNomenclatorId: number = null;

	@JsonProperty("nrAniValabilitateMandatPresedinteVicepresedinte", Number)
	public nrAniValabilitateMandatPresedinteVicepresedinte: number = null;

	@JsonProperty("nrAniValabilitateMandatMembruCdCoordonator", Number)
	public nrAniValabilitateMandatMembruCdCoordonator: number = null;
	
	@JsonProperty("isCategorieComisie", Boolean)
	public isCategorieComisie: boolean = null;
}