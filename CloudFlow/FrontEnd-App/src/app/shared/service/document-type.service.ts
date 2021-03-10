import { Injectable } from "@angular/core";
import { ApiCaller } from "../api-caller";
import { AsyncCallback } from "../async-callback";
import { DocumentTypeModel } from "../model/content/document-type.model";
import { AppError } from "../model/app-error";
import { ApiPathConstants } from "../constants/api-path.constants";
import { ApiPathUtils } from "./../utils";

@Injectable()
export class DocumentTypeService {

	private apiCaller: ApiCaller;

	public constructor(apiCaller: ApiCaller) {
		this.apiCaller = apiCaller;
	}

	public getAllDocumentTypesForDisplay(callback: AsyncCallback<DocumentTypeModel[], AppError>): void {
		this.apiCaller.call(ApiPathConstants.GET_ALL_DOCUMENT_TYPES_FOR_DISPLAY, null, DocumentTypeModel, callback);
	}

	public getDocumentTypeById(documentTypeId: number, callback: AsyncCallback<DocumentTypeModel, AppError>): void {
		this.apiCaller.call(ApiPathUtils.appendParametersToPath(ApiPathConstants.GET_DOCUMENT_TYPE_BY_ID, "" + documentTypeId), null, DocumentTypeModel, callback);
	}

	public getAvailableDocumentTypes(callback: AsyncCallback<DocumentTypeModel[], AppError>): void {
		this.apiCaller.call(ApiPathConstants.GET_AVAILABLE_DOCUMENT_TYPES, null, DocumentTypeModel, callback);
	}

	public getAvailableDocumentTypesForSearch(callback: AsyncCallback<DocumentTypeModel[], AppError>): void {
		this.apiCaller.call(ApiPathConstants.GET_AVAILABLE_DOCUMENT_TYPES_FOR_SEARCH, null, DocumentTypeModel, callback);
	}

	public deleteDocumentType(documentTypeId: number, callback: AsyncCallback<void, AppError>): void {
		let relativePath: string = ApiPathUtils.appendParametersToPath(ApiPathConstants.DELETE_DOCUMENT_TYPE, documentTypeId.toString());
		this.apiCaller.call(relativePath, null, null, callback);
	}

	public saveDocumentType(documentType: DocumentTypeModel, callback: AsyncCallback<void, AppError>): void {
		this.apiCaller.call(ApiPathConstants.SAVE_DOCUMENT_TYPE, documentType, null, callback);
	}

	public getDocumentTypesWithNoWorkflow(callback: AsyncCallback<DocumentTypeModel[], AppError>): void {
		this.apiCaller.call(ApiPathConstants.GET_DOCUMENT_TYPES_WITH_NO_WORKFLOW, null, DocumentTypeModel, callback);
	}

	public existDocumentTypeWithName(documentTypeName: string, callback: AsyncCallback<boolean, AppError>): void {
		let relativePath: string = ApiPathUtils.appendParametersToPath(ApiPathConstants.EXIST_DOCUMENT_TYPE_WITH_NAME, documentTypeName);
		this.apiCaller.call(relativePath, null, Boolean, callback);
	}
	
	public getDocumentTypeIdByName(documentTypeName: string, callback: AsyncCallback<number, AppError>): void {
		let relativePath: string = ApiPathUtils.appendParametersToPath(ApiPathConstants.GET_DOCUMENT_TYPE_BY_NAME, documentTypeName);
		this.apiCaller.call(relativePath, null, Number, callback);
	}
}