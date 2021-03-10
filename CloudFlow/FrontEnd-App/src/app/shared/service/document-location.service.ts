import { ApiPathConstants } from "./../constants/api-path.constants";
import { AppError } from "./../model/app-error";
import { AsyncCallback } from "./../async-callback";
import { ApiCaller } from "./../api-caller";
import { Injectable } from "@angular/core";
import { FolderModel } from "../model/folder.model";
import { DocumentLocationModel } from "../model/document-location.model";
import { ApiPathUtils } from "../utils/api-path-utils";

@Injectable()
export class DocumentLocationService {

	private apiCaller: ApiCaller;

	public constructor(apiCaller: ApiCaller) {
		this.apiCaller = apiCaller;
	}

	public getAllDocumentLocations(callback: AsyncCallback<DocumentLocationModel[], AppError>): void {
		this.apiCaller.call(ApiPathConstants.GET_ALL_DOCUMENT_LOCATIONS, null, DocumentLocationModel, callback);
	}

	public getFoldersFromDocumentLocation(documentLocationRealName: string, callback: AsyncCallback<FolderModel[], AppError>): void {
		this.apiCaller.call(ApiPathConstants.GET_FOLDERS_FROM_DOCUMENT_LOCATION + "/" + documentLocationRealName, null, FolderModel, callback);
	}

	public saveDocumentLocation(documentLocationModel: DocumentLocationModel, callback: AsyncCallback<string, AppError>): void {
		this.apiCaller.call(ApiPathConstants.SAVE_DOCUMENT_LOCATION, documentLocationModel, String, callback);
	}

	public deleteDocumentLocation(documentLocationRealName: string, callback: AsyncCallback<null, AppError>): void {
		this.apiCaller.call(
			ApiPathUtils.appendParametersToPath(ApiPathConstants.DELETE_DOCUMENT_LOCATION, documentLocationRealName),
			null,
			null,
			callback
		);
	}

	public getDocumentLocationByRealName(documentLocationRealName: string, callback: AsyncCallback<DocumentLocationModel, AppError>): void {
		this.apiCaller.call(ApiPathConstants.GET_DOCUMENT_LOCATION_BY_REALNAME + "/" + documentLocationRealName, null, DocumentLocationModel, callback);
	}
}