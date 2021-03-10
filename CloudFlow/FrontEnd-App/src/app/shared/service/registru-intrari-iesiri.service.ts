import { Injectable } from "@angular/core";
import { ApiCaller } from "../api-caller";
import { AppError, RegistruIesiriModel, RegistruIesiriFilterModel, RegistruIesiriViewModelPagingList } from "../model";
import { RegistruIntrariModel } from "../model/registru-intrari-iesiri/registru-intrari.model";
import { AsyncCallback } from "../async-callback";
import { ApiPathConstants } from "../constants";
import { RegistruIntrariViewModel } from "../model/registru-intrari-iesiri/registru-intrari-view.model";
import { ApiPathUtils } from "../utils";
import { RegistruIesiriViewModel } from "../model/registru-intrari-iesiri/registru-iesiri-view.model";
import { Observable } from "rxjs/Observable";
import { Response } from "@angular/http";
import { HttpResponse } from "@angular/common/http";
import { RegistruIntrariViewModelPagingList } from "../model/registru-intrari-iesiri/registru-intrari-view-model-paging-list";

@Injectable()
export class RegistruIntrariIesiriService { 

	private apiCaller: ApiCaller;

	public constructor(apiCaller: ApiCaller) {
		this.apiCaller = apiCaller;
	}

	public getAllRegistruIntrariViewModels(callback: AsyncCallback<RegistruIntrariViewModel[], AppError>): void {
		this.apiCaller.call(ApiPathConstants.GET_ALL_REGISTRU_INTRARI_VIEW_MODELS, null, RegistruIntrariViewModel, callback);
	}

	public getAllIntrari(callback: AsyncCallback<RegistruIntrariModel[], AppError>): void {
		this.apiCaller.call(ApiPathConstants.GET_ALL_INTRARI, null, RegistruIntrariModel, callback);
	}

	public getYearsOfExistingIntrari(callback: AsyncCallback<number[], AppError>): void {
		this.apiCaller.call(ApiPathConstants.GET_YEARS_OF_EXISTING_INTRARI, null, Number, callback);
	}

	public getYearsOfExistingIesiri(callback: AsyncCallback<number[], AppError>): void {
		this.apiCaller.call(ApiPathConstants.GET_YEARS_OF_EXISTING_IESIRI, null, Number, callback);
	}

	public getAllRegistruIntrariViewModelsByYear(year, callback: AsyncCallback<RegistruIntrariViewModel[], AppError>): void {
		this.apiCaller.call(ApiPathUtils.appendParametersToPath(ApiPathConstants.GET_ALL_REGISTRU_INTRARI_VIEW_MODELS_BY_YEAR, year), null, RegistruIntrariViewModel, callback);
	}

	public getRegistruIesiriViewModelsByFilter(filter: RegistruIesiriFilterModel, callback: AsyncCallback<RegistruIesiriViewModelPagingList, AppError>): void {
		this.apiCaller.call(ApiPathConstants.GET_REGISTRU_IESIRI_VIEW_MODELS_BY_FILTER, filter, RegistruIesiriViewModelPagingList, callback);
	}

	public getRegistruIntrariViewModelByFilter(registruIntrariFilter, callback: AsyncCallback<RegistruIntrariViewModelPagingList, AppError>): void {
		this.apiCaller.call(ApiPathConstants.GET_REGISTRU_INTRARI_BY_FILTER,registruIntrariFilter, RegistruIntrariViewModelPagingList, callback);
	}

	public getNrInregistrareMappedRegistriByRegistruId(registruType, registruId, callback: AsyncCallback<String[], null>): void {
		this.apiCaller.call(ApiPathUtils.appendParametersToPath(ApiPathConstants.GET_NR_INREGISTRARE_MAPPED_REGISTRI_BY_REGISTRU_ID, registruType, registruId), null, String, callback);
	}

	public saveRegistruIntrari(registruIntrariModel: RegistruIntrariModel, callback: AsyncCallback<null, AppError>): void {
		this.apiCaller.call(ApiPathConstants.SAVE_REGISTRU_INTRARI, registruIntrariModel, null, callback);
	}

	public saveRegistruIesiri(registruIesiriModel: RegistruIesiriModel, callback: AsyncCallback<null, AppError>): void {
		this.apiCaller.call(ApiPathConstants.SAVE_REGISTRU_IESIRI, registruIesiriModel, null, callback);
	}

	public getRegistruIesiri(registruIesiriId: number, callback: AsyncCallback<RegistruIesiriModel, AppError>): void {
		this.apiCaller.call(ApiPathUtils.appendParametersToPath(ApiPathConstants.GET_REGISTRU_IESIRI, registruIesiriId.toString()), null, RegistruIesiriModel, callback);
	}

	public getAllRegistruIesiriViewModels(callback: AsyncCallback<RegistruIesiriViewModel[], AppError>): void {
		this.apiCaller.call(ApiPathConstants.GET_ALL_REGISTRU_IESIRI_VIEW_MODELS, null, RegistruIesiriViewModel, callback);
	}
	
	public getRegistruIntrariById(registruId, callback: AsyncCallback<null, AppError>): void {
		this.apiCaller.call(ApiPathUtils.appendParametersToPath(ApiPathConstants.GET_REGISTRU_INTRARI_BY_ID, registruId), null, RegistruIntrariModel, callback);
	}

	public cancelRegistruIntrari(registruIntrariId: number, motivAnulare: string, callback: AsyncCallback<null, AppError>): void {
		this.apiCaller.call(ApiPathUtils.appendParametersToPath(ApiPathConstants.CANCEL_REGISTRU_INTRARI, registruIntrariId.toString(), motivAnulare), null, null, callback);
	}

	public cancelRegistruIesiri(registruIesiriId: number, motivAnulare: string, callback: AsyncCallback<null, AppError>): void {
		this.apiCaller.call(ApiPathUtils.appendParametersToPath(ApiPathConstants.CANCEL_REGISTRU_IESIRI, registruIesiriId.toString(), motivAnulare), null, null, callback);
	}

	public isRegistruIesiriCanceled(registruIesiriId: number, callback: AsyncCallback<boolean, AppError>): void {
		this.apiCaller.call(ApiPathUtils.appendParametersToPath(ApiPathConstants.IS_REGISTRU_IESIRI_CANCELED, registruIesiriId.toString()), null, Boolean, callback);
	}

	public isRegistruIntrariCanceled(registruIntrariId: number, callback: AsyncCallback<boolean, AppError>): void {
		this.apiCaller.call(ApiPathUtils.appendParametersToPath(ApiPathConstants.IS_REGISTRU_INTRARI_CANCELED, registruIntrariId.toString()), null, Boolean, callback);
	}

	public isRegistruIntrariFinalized(registruIntrariId: number, callback: AsyncCallback<boolean, AppError>): void {
		this.apiCaller.call(ApiPathUtils.appendParametersToPath(ApiPathConstants.IS_REGISTRU_INTRARI_FINALIZED, registruIntrariId.toString()), null, Boolean, callback);
	}

	public finalizeRegistruIntrari(registruIntrariId: number, callback: AsyncCallback<void, AppError>): void {
		this.apiCaller.call(ApiPathUtils.appendParametersToPath(ApiPathConstants.FINALIZE_REGISTRU_INTRARI, registruIntrariId.toString()), null, null, callback);
	}

	public finalizeRegistruIesiri(registruIesiriId: number, callback: AsyncCallback<void, AppError>): void {
		this.apiCaller.call(ApiPathUtils.appendParametersToPath(ApiPathConstants.FINALIZE_REGISTRU_IESIRI, registruIesiriId.toString()), null, null, callback);
	}

	public downloadAtasamentById(attachmentId: number): Observable<HttpResponse<Blob>> {
		let relativePath: string = ApiPathUtils.appendParametersToPath(ApiPathConstants.DOWNLOAD_ATTACHMENT_OF_REGISTRU_INTRARI_BY_ID, attachmentId.toString());
		return this.apiCaller.download(relativePath);
	}

	public downloadAtasamentRegistruIesiriById(attachmentId: number): Observable<HttpResponse<Blob>> {
		let relativePath: string = ApiPathUtils.appendParametersToPath(ApiPathConstants.DOWNLOAD_ATTACHMENT_OF_REGISTRU_IESIRI_BY_ID, attachmentId.toString());
		return this.apiCaller.download(relativePath);
	}

	public getLastNrInregistrareByTipRegistruAndYear(tipRegistru: string, year: number, callback: AsyncCallback<number, AppError>): void {
		this.apiCaller.call(ApiPathUtils.appendParametersToPath(ApiPathConstants.GET_LAST_NR_INREGISTRARE_BY_TIP_AND_YEAR, tipRegistru, year.toString()), null, Number, callback);
	}

	public isSubactivityUsedInAnyRegisterEntry(subactivityId: number, callback: AsyncCallback<boolean, AppError>): void {
		this.apiCaller.call(ApiPathUtils.appendParametersToPath(ApiPathConstants.IS_SUBACTIVITY_USED_IN_ANY_REGISTER_ENTRY, subactivityId.toString()), null, Boolean, callback);
	}
}