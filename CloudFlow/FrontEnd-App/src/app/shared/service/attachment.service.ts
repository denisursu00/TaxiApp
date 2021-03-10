import { Injectable } from "@angular/core";
import { ApiCaller } from "../api-caller";
import { DocumentAttachmentModel, AppError, AttachmentModel } from "../model";
import { AsyncCallback } from "../async-callback";
import { ApiPathConstants } from "../constants";
import { AlteDeconturiViewModel } from "../model/alte-deconturi";
import { Observable } from "rxjs/Observable";
import { ApiPathUtils } from "./../utils/api-path-utils";
import { Response } from "@angular/http";
import { HttpResponse } from "@angular/common/http";


@Injectable()
export class AttachmentService {

	private apiCaller: ApiCaller;

	public constructor(apiCaller: ApiCaller) {
		this.apiCaller = apiCaller;
	}

	public uploadFile(formData: FormData, callback: AsyncCallback<AttachmentModel, AppError>): void {
		this.apiCaller.upload(ApiPathConstants.UPLOAD_ATTACHMENT, formData, AttachmentModel, callback);
	}

	public downloadFile(attachmentName: string): Observable<HttpResponse<Blob>> {
		let relativePath: string = ApiPathUtils.appendParametersToPath(ApiPathConstants.DOWNLOAD_ATTACHMENT, attachmentName);
		return this.apiCaller.download(relativePath);
	}

	public deleteFile(attachmentName: string, callback: AsyncCallback<void, AppError>): void {
		let relativePath: string = ApiPathUtils.appendParametersToPath(ApiPathConstants.DELETE_ATTACHMENT, attachmentName);
		return this.apiCaller.call(relativePath, null, null, callback);
	}
}