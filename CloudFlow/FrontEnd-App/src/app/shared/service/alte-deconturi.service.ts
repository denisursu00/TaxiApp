import { Injectable } from "@angular/core";
import { ApiCaller } from "../api-caller";
import { AsyncCallback } from "../async-callback";
import { ApiPathConstants } from "../constants";
import { ApiPathUtils } from "../utils";
import { AppError } from "../model";
import { AlteDeconturiViewModel, AlteDeconturiModel } from "../model/alte-deconturi";
import { Observable } from "rxjs/Observable";
import { Response } from "@angular/http";
import { HttpResponse } from "@angular/common/http";

@Injectable()
export class AlteDeconturiService { 

	private apiCaller: ApiCaller;

	public constructor(apiCaller: ApiCaller) {
		this.apiCaller = apiCaller;
	}

	public getYearsOfExistingDeconturi(callback: AsyncCallback<number[], AppError>): void {
		this.apiCaller.call(ApiPathConstants.GET_YEARS_OF_EXISTING_DECONTURI, null, Number, callback);
	}

	public getAllAlteDeconturiViewModelsByYear(year, callback: AsyncCallback<AlteDeconturiViewModel[], AppError>): void {
		this.apiCaller.call(ApiPathUtils.appendParametersToPath(ApiPathConstants.GET_ALL_ALTE_DECONTURI_VIEW_MODELS_BY_YEAR, year), null, AlteDeconturiViewModel, callback);
	}
	
	public getDecontById(decontId, callback: AsyncCallback<AlteDeconturiModel, AppError>): void {
		this.apiCaller.call(ApiPathUtils.appendParametersToPath(ApiPathConstants.GET_DECONT_BY_ID, decontId), null, AlteDeconturiModel, callback);
	}

	public isDecontCanceled(decontId: number, callback: AsyncCallback<boolean, AppError>): void {
		this.apiCaller.call(ApiPathUtils.appendParametersToPath(ApiPathConstants.IS_DECONT_CANCELED, decontId.toString()), null, Boolean, callback);
	}

	public saveAlteDeconturi(alteDeconturiModel: AlteDeconturiModel, callback: AsyncCallback<null, AppError>): void {
		this.apiCaller.call(ApiPathConstants.SAVE_ALTE_DECONTURI, alteDeconturiModel, null, callback);
	}
	
	public deleteDecontById(decontId: number, callback: AsyncCallback<null, AppError>): void {
		this.apiCaller.call(ApiPathUtils.appendParametersToPath(ApiPathConstants.DELETE_DECONT_BY_ID, decontId.toString()), null, null, callback);
	}

	public cancelDecont(decontId: number, motivAnulare: string, callback: AsyncCallback<null, AppError>): void {
		this.apiCaller.call(ApiPathUtils.appendParametersToPath(ApiPathConstants.CANCEL_DECONT, decontId.toString(), motivAnulare), null, null, callback);
	}
	
	public finalizeDecontById(decontId: number, callback: AsyncCallback<null, AppError>): void {
		this.apiCaller.call(ApiPathUtils.appendParametersToPath(ApiPathConstants.FINALIZE_DECONT_BY_ID, decontId.toString()), null, null, callback);
	}

	public isDecontFinalized(decontId: number, callback: AsyncCallback<boolean, AppError>): void {
		this.apiCaller.call(ApiPathUtils.appendParametersToPath(ApiPathConstants.IS_DECONT_FINALIZED, decontId.toString()), null, Boolean, callback);
	}

	public downloadAtasamentOfCheltuiala(attachmentId: number): Observable<HttpResponse<Blob>> {
		let relativePath: string = ApiPathUtils.appendParametersToPath(ApiPathConstants.DOWNLOAD_ATASAMENT_OF_CHELTUIALA, attachmentId.toString());
		return this.apiCaller.download(relativePath);
	}

	public getTitulariOfExistingDeconturi(callback: AsyncCallback<string[], AppError>): void {
		this.apiCaller.call(ApiPathConstants.GET_TITULARI_OF_EXISTING_DECONTURI, null, String, callback);
	}

}