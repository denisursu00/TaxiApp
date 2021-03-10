import { Injectable } from "@angular/core";
import { ApiCaller } from "../api-caller";
import { GetPagedDocumentsFromFolderRequestModel } from "../model/get-paged-documents-from-folder-request.model";
// tslint:disable-next-line:max-line-length
import { DocumentViewModel, DocumentAddBundleModel, DocumentModel, DocumentViewOrEditBundleModel, DocumentViewVersionBundleModel, SendDocumentToWorkflowRequestModel, WorkflowInstanceResponseModel, SendDocumentToWorkflowResponseModel } from "./../model";
import { DocumentValidationRequestModel, DocumentCollectionValidationRequestModel } from "./../model";
import { ApiPathConstants } from "../constants/api-path.constants";
import { AsyncCallback } from "../async-callback";
import { AppError } from "../model/app-error";
import { Page } from "../model/page";
import { PageRequest } from "../model/page-request.model";
import { ApiPathUtils } from "../utils/api-path-utils";
import { DocumentVersionInfoViewModel } from "../model/views/document-version-info-view-model";
import { DocumentValidationResponseModel } from "./../model/content/document-validation-response.model";
import { AutocompleteMetadataRequestModel } from "./../model/content/autocomplete-metadata-request.model";
import { AutocompleteMetadataResponseModel } from "./../model/content/autocomplete-metadata-response.model";
import { Observable } from "rxjs/Observable";
import { ExportType } from "../enums/export-type";
import { HttpResponse } from "@angular/common/http";
import { DocumentFilterModel } from "../model/content/document-filter.model";
import { AdminUpdateDocumentModel, AdminUpdateDocumentBundleModel } from "./../model";

@Injectable()
export class DocumentService {

	private apiCaller: ApiCaller;

	public constructor(apiCaller: ApiCaller) {
		this.apiCaller = apiCaller; 
	}

	public getPagedDocumentsFromFolder(getPagedDocumentsFromFolderRequestModel: PageRequest<GetPagedDocumentsFromFolderRequestModel>, callback: AsyncCallback<Page<DocumentViewModel>, AppError>): void {
		this.apiCaller.call(
			ApiPathConstants.GET_PAGED_DOCUMENTS_FROM_FOLDER, 
			getPagedDocumentsFromFolderRequestModel,
			Page,
			callback
		);
	}
	
	public getPagedDocuments(documentFilterModel: DocumentFilterModel, callback: AsyncCallback<Page<DocumentViewModel>, AppError>): void {
		this.apiCaller.call(
			ApiPathConstants.GET_PAGED_DOCUMENTS, 
			documentFilterModel,
			Page,
			callback
		);
	}

	public getDocumentVersions(documentId: string, documentLocationRealName: string, callback: AsyncCallback<DocumentVersionInfoViewModel[], AppError>): void {
		this.apiCaller.call(
			ApiPathUtils.appendParametersToPath(ApiPathConstants.GET_DOCUMENT_VERSIONS, documentId, documentLocationRealName),
			null,
			DocumentVersionInfoViewModel,
			callback
		);
	}	
	
	public deleteDocument(documentId: string, documentLocationRealName: string, callback: AsyncCallback<Page<null>, AppError>): void {
		this.apiCaller.call(
			ApiPathUtils.appendParametersToPath(ApiPathConstants.DELETE_DOCUMENT, documentId, documentLocationRealName), 
			null,
			null,
			callback
		);
	}
	
	public getDocumentAddBundle(documentTypeId: string, callback: AsyncCallback<DocumentAddBundleModel, AppError>): void {
		const relativePath = ApiPathUtils.appendParametersToPath(ApiPathConstants.GET_DOCUMENT_ADD_BUNDLE, documentTypeId);
		this.apiCaller.call(relativePath, null, DocumentAddBundleModel,	callback);
	}	

	public save(document: DocumentModel, callback: AsyncCallback<string, AppError>): void {
		this.apiCaller.call(ApiPathConstants.SAVE_DOCUMENT, document, String, callback);
	}

	public getDocumentViewOrEditBundle(documentId: string, documentLocationName: string, callback: AsyncCallback<DocumentViewOrEditBundleModel, AppError>): void {
		const relativePath = ApiPathUtils.appendParametersToPath(ApiPathConstants.GET_DOCUMENT_VIEW_OR_EDIT_BUNDLE, documentId, documentLocationName);
		this.apiCaller.call(relativePath, null, DocumentViewOrEditBundleModel,	callback);
	}

	public getDocumentViewVersionBundle(documentLocationName: string, documentId: string, versionNr: string, callback: AsyncCallback<DocumentViewVersionBundleModel, AppError>): void {
		const relativePath = ApiPathUtils.appendParametersToPath(ApiPathConstants.GET_DOCUMENT_VIEW_VERSION_BUNDLE, documentLocationName, documentId, versionNr);
		this.apiCaller.call(relativePath, null, DocumentViewVersionBundleModel,	callback);
	}

	public checkin(document: DocumentModel, callback: AsyncCallback<string, AppError>): void {
		this.apiCaller.call(ApiPathConstants.CHECKIN_DOCUMENT, document, String, callback);
	}

	public checkout(documentLocationName: string, documentId: string, callback: AsyncCallback<null, AppError>): void {
		const relativePath = ApiPathUtils.appendParametersToPath(ApiPathConstants.CHECKOUT_DOCUMENT, documentLocationName, documentId);
		this.apiCaller.call(relativePath, null, null, callback);
	}

	public undoCheckout(documentLocationName: string, documentId: string, callback: AsyncCallback<null, AppError>): void {
		const relativePath = ApiPathUtils.appendParametersToPath(ApiPathConstants.UNDO_CHECKOUT_DOCUMENT, documentLocationName, documentId);
		this.apiCaller.call(relativePath, null, null, callback);
	}

	public sendDocumentToWorkflow(sendRequest: SendDocumentToWorkflowRequestModel, callback: AsyncCallback<SendDocumentToWorkflowResponseModel, AppError>): void {
		this.apiCaller.call(ApiPathConstants.SEND_DOCUMENT_TO_WORKFLOW, sendRequest, SendDocumentToWorkflowResponseModel, callback);
	}

	public existDocumentsOfType(documentTypeId: number, callback: AsyncCallback<boolean, AppError>): void {
		const relativePath = ApiPathUtils.appendParametersToPath(ApiPathConstants.EXIST_DOCUMENTS_OF_TYPE, "" + documentTypeId);
		this.apiCaller.call(relativePath, null, null, callback);
	}

	public validateDocument(requestModel: DocumentValidationRequestModel, callback: AsyncCallback<DocumentValidationResponseModel, AppError>): void {
		this.apiCaller.call(ApiPathConstants.VALIDATE_DOCUMENT, requestModel, DocumentValidationResponseModel, callback);
	}

	public validateDocumentCollection(requestModel: DocumentCollectionValidationRequestModel, callback: AsyncCallback<DocumentValidationResponseModel, AppError>): void {
		this.apiCaller.call(ApiPathConstants.VALIDATE_DOCUMENT_COLLECTION, requestModel, DocumentValidationResponseModel, callback);
	}

	public getDocumentName(documentLocationName: string, documentId: string, callback: AsyncCallback<String, AppError>): void {
		const relativePath = ApiPathUtils.appendParametersToPath(ApiPathConstants.GET_DOCUMENT_NAME, documentLocationName, documentId);
		this.apiCaller.call(relativePath, null, String, callback);
	}

	public autocompleteMetadata(request: AutocompleteMetadataRequestModel, callback: AsyncCallback<AutocompleteMetadataResponseModel, AppError>): void {
		this.apiCaller.call(ApiPathConstants.AUTOCOMPLETE_METADATA, request, AutocompleteMetadataResponseModel, callback);
	}

	public exportDocument(documentLocationRealName: string, documentId: string, templateName: string, exportType: ExportType): Observable<HttpResponse<Blob>> {
		return this.apiCaller.download(ApiPathConstants.EXPORT_DOCUMENT, {
			documentLocationRealName: documentLocationRealName,
			documentId: documentId,
			templateName: templateName,
			exportType: exportType
		});
	}

	public downloadDocumentAttachment(documentLocationRealName: string, documentId: string, attachmentName: string, versionNumber: number): Observable<HttpResponse<Blob>> {
		return this.apiCaller.download(ApiPathConstants.DOWNLOAD_DOCUMENT_ATTACHMENT, {
			documentLocationRealName: documentLocationRealName,
			documentId: documentId,
			attachmentName: attachmentName,
			versionNumber: versionNumber
		});
	}

	public getAdminUpdateDocumentBundle(documentId: string, documentLocationName: string, callback: AsyncCallback<AdminUpdateDocumentBundleModel, AppError>): void {
		const relativePath = ApiPathUtils.appendParametersToPath(ApiPathConstants.GET_ADMIN_UPDATE_DOCUMENT_BUNDLE, documentId, documentLocationName);
		this.apiCaller.call(relativePath, null, AdminUpdateDocumentBundleModel,	callback);
	}

	public updateDocument(document: AdminUpdateDocumentModel, callback: AsyncCallback<null, AppError>): void {
		this.apiCaller.call(ApiPathConstants.ADMIN_UPDATE_DOCUMENT, document, null, callback);
	}
}