import { Injectable } from "@angular/core";
import { ApiCaller } from "../api-caller";
import { RegistruDocumenteJustificativePlatiModel } from "../model/registru-documente-justificative-plati/registru-documente-justificative-plati.model";
import { AsyncCallback } from "../async-callback";
import { AppError } from "../model";
import { ApiPathConstants } from "../constants";
import { ApiPathUtils } from "../utils";
import { Observable } from "rxjs/Observable";
import { Response } from "@angular/http";
import { HttpResponse } from "@angular/common/http";

@Injectable()
export class RegistruDocumenteJustificativePlatiService { 

	private apiCaller: ApiCaller;

	public constructor(apiCaller: ApiCaller) {
		this.apiCaller = apiCaller;
	}

	public getAllDocumenteJustificativePlati(callback: AsyncCallback<RegistruDocumenteJustificativePlatiModel[], AppError>): void {
		this.apiCaller.call(ApiPathConstants.GET_ALL_DOCUMENTE_JUSTIFICATIVE_PLATI, null, RegistruDocumenteJustificativePlatiModel, callback);
	}

	public getAllByYear(year, callback: AsyncCallback<RegistruDocumenteJustificativePlatiModel[], AppError>): void {
		this.apiCaller.call(ApiPathUtils.appendParametersToPath(ApiPathConstants.GET_ALL_BY_YEAR, year), null, RegistruDocumenteJustificativePlatiModel, callback);
	}

	public getDocumentJustificativPlati(documentJustificativPlatiId, callback: AsyncCallback<RegistruDocumenteJustificativePlatiModel, AppError>): void {
		this.apiCaller.call(ApiPathUtils.appendParametersToPath(ApiPathConstants.GET_DOCUMENT_JUSTIFICATIV_PLATI, documentJustificativPlatiId), null, RegistruDocumenteJustificativePlatiModel, callback);
	}

	public saveDocumentJustificativPlati(documentJustificativPlatiModel: RegistruDocumenteJustificativePlatiModel, callback: AsyncCallback<null, AppError>): void {
		this.apiCaller.call(ApiPathConstants.SAVE_DOCUMENT_JUSTIFICATIV_PLATI, documentJustificativPlatiModel, null, callback);
	}

	public cancelDocumentJustificativPlati(documentJustificativPlatiModel: RegistruDocumenteJustificativePlatiModel, callback: AsyncCallback<null, AppError>): void {
		this.apiCaller.call(ApiPathConstants.CANCEL_DOCUMENT_JUSTIFICATIV_PLATI, documentJustificativPlatiModel, null, callback);
	}

	public getYearsWithInregistrariDocumenteJustificativePlati(callback: AsyncCallback<Number[], AppError>): void {
		this.apiCaller.call(ApiPathConstants.GET_YEARS_WITH_INREGISTRARI_DOCUMENTE_JUSTIFICATIVE_PLATI, null, Number, callback);
	}

	public downloadAtasamentById(attachmentId: number): Observable<HttpResponse<Blob>> {
		let relativePath: string = ApiPathUtils.appendParametersToPath(ApiPathConstants.DOWNLOAD_ATTACHMENT_OF_REGISTRU_DOCUMENTE_JUSTIFICATIVE_PLATI_BY_ID, attachmentId.toString());
		return this.apiCaller.download(relativePath);
	}
}