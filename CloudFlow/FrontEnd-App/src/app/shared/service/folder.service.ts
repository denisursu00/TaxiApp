import { FolderModel } from "../model/folder.model";
import { ApiPathConstants } from "./../constants/api-path.constants";
import { AppError } from "./../model/app-error";
import { AsyncCallback } from "./../async-callback";
import { ApiCaller } from "./../api-caller";
import { Injectable } from "@angular/core";
import { ApiPathUtils } from "../utils/api-path-utils";
import { MoveFolderRequestModel } from "../model/move-folder-request.model";
import { GetFolderPathRequestModel } from "../model/get-folder-path-request.model";

@Injectable()
export class FolderService {

	private apiCaller: ApiCaller;

	public constructor(apiCaller: ApiCaller) {
		this.apiCaller = apiCaller;
	}
	
	public getFoldersFromFolder(documentLocationRealName: string, parentId: string, callback: AsyncCallback<FolderModel[], AppError>): void {
		this.apiCaller.call(
			ApiPathUtils.appendParametersToPath(ApiPathConstants.GET_FOLDERS_FROM_FOLDER, documentLocationRealName, parentId),
			null,
			FolderModel,
			callback
		);
	}

	public getFolderById(folderId: string, documentLocationRealName: string, callback: AsyncCallback<FolderModel, AppError>): void {
		this.apiCaller.call(
			ApiPathUtils.appendParametersToPath(ApiPathConstants.GET_FOLDER_BY_ID, folderId, documentLocationRealName),
			null,
			FolderModel,
			callback
		);
	}

	public saveFolder(folder: FolderModel, callback: AsyncCallback<null, AppError>): void {
		this.apiCaller.call(
			ApiPathConstants.SAVE_FOLDER,
			folder,
			null,
			callback
		);
	}

	public deleteFolder(folderId: string, documentLocationRealName: string, callback: AsyncCallback<null, AppError>): void {
		this.apiCaller.call(
			ApiPathUtils.appendParametersToPath(ApiPathConstants.DELETE_FOLDER, folderId, documentLocationRealName),
			null,
			null,
			callback
		);
	}

	public moveFolder(moveFolderRequestModel: MoveFolderRequestModel, callback: AsyncCallback<null, AppError>): void {
		this.apiCaller.call(
			ApiPathConstants.MOVE_FOLDER, 
			moveFolderRequestModel,
			null,
			callback
		);
	}

	public getFolderPath(getFolderPathRequestModel: GetFolderPathRequestModel, callback: AsyncCallback<String, AppError>): void {
		this.apiCaller.call(ApiPathConstants.GET_FOLDER_PATH,
			getFolderPathRequestModel,
			String,
			callback
		);
	}

	
}