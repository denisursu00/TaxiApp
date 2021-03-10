import { Injectable } from "@angular/core";
import { ApiCaller } from "../api-caller";
import { AsyncCallback } from "../async-callback";
import { ApiPathConstants } from "../constants/api-path.constants";
import { DocumentHistoryViewModel } from "../model/views/document-history-view.model";
import { AppError } from "../model/app-error";
import { ApiPathUtils } from "../utils/api-path-utils";

@Injectable()
export class DocumentWorkflowHistoryService {

	private apiCaller: ApiCaller;

	public constructor(apiCaller: ApiCaller) {
		this.apiCaller = apiCaller;
	}

	public getDocumentHistory(documentId: string, callback: AsyncCallback<DocumentHistoryViewModel[], AppError>): void {
		this.apiCaller.call(
			ApiPathUtils.appendParametersToPath(ApiPathConstants.GET_DOCUMENT_HISTORY, documentId),
			null,
			DocumentHistoryViewModel,
			callback
		);
	}
}