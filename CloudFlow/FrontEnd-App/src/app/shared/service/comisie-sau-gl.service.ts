import { Injectable } from "@angular/core";
import { ApiCaller } from "../api-caller";
import { AsyncCallback } from "../async-callback";
import { ReprezentantiComisieSauGLEditBundleModel, ReprezentantiComisieSauGLModel } from "../model/arb";
import { AppError } from "../model/app-error";
import { ApiPathConstants } from "../constants/api-path.constants";
import { ApiPathUtils } from "./../utils";
import { NomenclatorValueModel } from "./../model/nomenclator";
import { MembruReprezentantiComisieSauGLModel } from "./../model/arb/membru-reprezentanti-comisie-sau-gl.model";
import { MembruReprezentantiComisieSauGLInfoModel } from "./../model/arb/membru-reprezentanti-comisie-sau-gl-info.model";

@Injectable()
export class ComisieSauGLService {

	private apiCaller: ApiCaller;

	public constructor(apiCaller: ApiCaller) {
		this.apiCaller = apiCaller;
	}

	public getReprezentantiEditBundleByComisieSauGLId(comisieSauGLId: number, callback: AsyncCallback<ReprezentantiComisieSauGLEditBundleModel, AppError>): void {
		let relativePath: string = ApiPathUtils.appendParametersToPath(ApiPathConstants.GET_REPREZENTANTI_EDIT_BUNDLE_BY_COMISIE_SAU_GL_ID, comisieSauGLId.toString());
		this.apiCaller.call(relativePath, null, ReprezentantiComisieSauGLEditBundleModel, callback);
	}

	public saveReprezentanti(model: ReprezentantiComisieSauGLModel, callback: AsyncCallback<void, AppError>): void {
		this.apiCaller.call(ApiPathConstants.SAVE_REPREZENTANTI_COMISIE_SAU_GL, model, null, callback);
	}

	public getAllInstitutiiOfMembriiComisieSaulGL(comisieSauGLId: number, callback: AsyncCallback<NomenclatorValueModel[], AppError>): void {
		let relativePath: string = ApiPathUtils.appendParametersToPath(ApiPathConstants.GET_ALL_INSTITUTII_OF_MEMBRII_COMISIE_SAU_GL, comisieSauGLId.toString());
		this.apiCaller.call(relativePath, null, NomenclatorValueModel, callback);
	}

	public getMembriiReprezentantiComisieSauGLByInstitutie(comisieSauGLId: number, institutieId: number, callback: AsyncCallback<MembruReprezentantiComisieSauGLInfoModel[], AppError>): void {
		let relativePath: string = ApiPathUtils.appendParametersToPath(ApiPathConstants.GET_MEMBRII_REPREZENTANTI_COMISIE_GL_BY_INSTITUTIE, comisieSauGLId.toString(), institutieId.toString());
		this.apiCaller.call(relativePath, null, MembruReprezentantiComisieSauGLInfoModel, callback);
	}
}