import { Injectable } from "@angular/core";
import { ApiCaller } from "../api-caller";
import { AppError, DocumentTemplateModel } from "../model";
import { AsyncCallback } from "../async-callback";
import { ApiPathConstants } from "../constants";
import { Observable } from "rxjs";
import { HttpResponse } from "@angular/common/http";


@Injectable()
export class DocumentTemplateService {

	private apiCaller: ApiCaller;

	public constructor(apiCaller: ApiCaller) {
		this.apiCaller = apiCaller;
	}

	public uploadTemplateFile(formData: FormData, callback: AsyncCallback<DocumentTemplateModel, AppError>): void {
		this.apiCaller.upload(ApiPathConstants.UPLOAD_DOCUMENT_TEMPLATE, formData, DocumentTemplateModel, callback);
	}

	public downloadTemplateFile(documentTypeId: number, templateName: string): Observable<HttpResponse<Blob>> {
		return this.apiCaller.download(ApiPathConstants.DOWNLOAD_DOCUMENT_TEMPLATE, {
			documentTypeId: documentTypeId,
			templateName: templateName
		});
	}
}