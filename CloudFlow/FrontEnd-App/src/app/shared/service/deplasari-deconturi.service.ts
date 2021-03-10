import { Injectable } from "@angular/core";
import { ApiCaller } from "../api-caller";
import { DeplasareDecontModel, DocumentDecizieDeplasareModel } from "../model/deplasari-deconturi";
import { AsyncCallback } from "../async-callback";
import { AppError } from "../model";
import { ApiPathConstants } from "../constants";
import { NumarDecizieDeplasareModel } from "../model/deplasari-deconturi/numar-decizie-deplasare.model";
import { ApiPathUtils, ObjectUtils } from "../utils";
import { DeplasareDecontViewModel } from "../model/deplasari-deconturi/deplasare-decont-view.model";
import { CheltuieliArbSiReprezentantArbReportDateFilterModel } from "../model/reports/cheltuieli-arb-si-reprezentant-arb-report-date-filter.model";

@Injectable()
export class DeplasariDeconturiService { 

	private apiCaller: ApiCaller;

	public constructor(apiCaller: ApiCaller) {
		this.apiCaller = apiCaller;
	}

	public saveDeplasareDecont(deplasareDecontModel: DeplasareDecontModel, callback: AsyncCallback<null, AppError>): void {
		this.apiCaller.call(ApiPathConstants.SAVE_DEPLASARE_DECONT, deplasareDecontModel, null, callback);
	}

	public getNumarDeciziiDeplasariAprobateByReprezentant(reprezentantArbId: number, deplasareDecontId: number, callback: AsyncCallback<NumarDecizieDeplasareModel[], AppError>): void {
		if (ObjectUtils.isNotNullOrUndefined(deplasareDecontId)){
			this.apiCaller.call(ApiPathUtils.appendParametersToPath(ApiPathConstants.GET_NUMAR_DECIZII_DEPLASARI_APROBATE_BY_REPREZENTANT, 
				reprezentantArbId.toString()).concat("?deplasareDecontId=" + deplasareDecontId), null, NumarDecizieDeplasareModel, callback);
		} else {
			this.apiCaller.call(ApiPathUtils.appendParametersToPath(ApiPathConstants.GET_NUMAR_DECIZII_DEPLASARI_APROBATE_BY_REPREZENTANT, 
				reprezentantArbId.toString()), null, NumarDecizieDeplasareModel, callback);
		}
	}

	public getDocumentDecizieDeplasare(documentId: string, documentLocationRealName: string, callback: AsyncCallback<DocumentDecizieDeplasareModel, AppError>): void {
		this.apiCaller.call(ApiPathUtils.appendParametersToPath(ApiPathConstants.GET_DOCUMENT_DECIZIE_DEPLASARE, documentId, documentLocationRealName), null, DocumentDecizieDeplasareModel, callback);
	}

	public getYearsOfExistingDeplasariDeconturi(callback: AsyncCallback<number[], AppError>): void {
		this.apiCaller.call(ApiPathConstants.GET_YEARS_OF_EXISTING_DEPLASARI_DECONTURI, null, Number, callback);
	}

	public getAllDeplasariDeconturiViewModelsByYear(year, callback: AsyncCallback<DeplasareDecontViewModel[], AppError>): void {
		this.apiCaller.call(ApiPathUtils.appendParametersToPath(ApiPathConstants.GET_ALL_DEPLASARI_DECONTURI_VIEW_MODELS_BY_YEAR, year), null, DeplasareDecontViewModel, callback);
	}

	public isDeplasareDecontCanceled(deplasareDecontId: number, callback: AsyncCallback<boolean, AppError>): void {
		this.apiCaller.call(ApiPathUtils.appendParametersToPath(ApiPathConstants.IS_DEPLASARE_DECONT_CANCELED, deplasareDecontId.toString()), null, Boolean, callback);
	}

	public getDeplasareDecontById(deplasareDecontId: number, callback: AsyncCallback<DeplasareDecontModel, AppError>): void {
		this.apiCaller.call(ApiPathUtils.appendParametersToPath(ApiPathConstants.GET_DEPLASATE_DECONT_BY_ID, deplasareDecontId.toString()), null, DeplasareDecontModel, callback);
	}

	public cancelDeplasareDecont(deplasareDecontId: number, motivAnulare: string, callback: AsyncCallback<void, AppError>): void {
		this.apiCaller.call(ApiPathUtils.appendParametersToPath(ApiPathConstants.CANCEL_DEPLASARE_DECONT, deplasareDecontId.toString(), motivAnulare), null, null, callback);
	}

	public removeDeplasareDecont(deplasareDecontId: number, callback: AsyncCallback<void, AppError>): void {
		this.apiCaller.call(ApiPathUtils.appendParametersToPath(ApiPathConstants.REMOVE_DEPLASARE_DECONT, deplasareDecontId.toString()), null, null, callback);
	}

	public finalizeDeplasareDecont(deplasareDecontId: number, callback: AsyncCallback<void, AppError>): void {
		this.apiCaller.call(ApiPathUtils.appendParametersToPath(ApiPathConstants.FINALIZE_DEPLASARE_DECONT, deplasareDecontId.toString()), null, null, callback);
	}
	
	public getTitulariOfExistingDeplasariDeconturi(callback: AsyncCallback<string[], AppError>): void {
		this.apiCaller.call(ApiPathConstants.GET_TITULARI_OF_EXISTING_DEPLASARE_DECONT, null, String, callback);
	}
	
	public getAllNumarDeciziiByFilter(filter: CheltuieliArbSiReprezentantArbReportDateFilterModel, callback: AsyncCallback<string[], AppError>): void {
		this.apiCaller.call(ApiPathConstants.GET_NUMAR_DECIZII_OF_EXISTING_DEPLASARE_DECONT, filter, String, callback);
	}

}