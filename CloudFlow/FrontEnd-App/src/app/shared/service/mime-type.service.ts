import { Injectable } from "@angular/core";
import { ApiCaller } from "../api-caller";
import { ApiPathConstants } from "../constants/api-path.constants";
import { MimeTypeModel, AppError } from "../model";
import { AsyncCallback } from "../async-callback";
import { ApiPathUtils } from "../utils/api-path-utils";

@Injectable()
export class MimeTypeService {

	private apiCaller: ApiCaller;

	constructor(apiCaller: ApiCaller) {
		this.apiCaller = apiCaller;
	}

	public getAllMimeTypes(callback: AsyncCallback<MimeTypeModel[], AppError>): void {
		this.apiCaller.call(ApiPathConstants.GET_ALL_MIME_TYPES, null, MimeTypeModel, callback);
	}
	
	public saveMimeType(mimeTypeMode: MimeTypeModel, callback: AsyncCallback<null, AppError>): void {
		this.apiCaller.call(ApiPathConstants.SAVE_MIME_TYPE, mimeTypeMode, null, callback);
	}
	
	public getMimeTypeById(mimeTypeId: number, callback: AsyncCallback<MimeTypeModel, AppError>): void {
		this.apiCaller.call(ApiPathUtils.appendParametersToPath(ApiPathConstants.GET_MIME_TYPE_BY_ID, mimeTypeId.toString()), null, MimeTypeModel, callback);
	}
	
	public deleteMimeType(mimeTypeId: number, callback: AsyncCallback<null, AppError>): void {
		this.apiCaller.call(ApiPathUtils.appendParametersToPath(ApiPathConstants.DELETE_MIME_TYPE, mimeTypeId.toString()), null, null, callback);
	}
}