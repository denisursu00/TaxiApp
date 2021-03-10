import { Injectable } from "@angular/core";
import { ApiCaller } from "../api-caller";
import { AppError } from "./../model/app-error";
import { AsyncCallback } from "./../async-callback";
import { ApiPathConstants } from "./../constants/api-path.constants";
import { DocumentPrezentaOnlineModel, PrezentaMembriiReprezentantiComisieGl } from "../model";
import { ApiPathUtils } from "../utils";
import { ParticipantiModel } from "../model/prezenta-online/participanti.model";

@Injectable()
export class PrezentaOnlineService {

	private apiCaller: ApiCaller;

	constructor(apiCaller: ApiCaller) {
		this.apiCaller = apiCaller;
	}

	public getAllDocumentsPrezenta(callback: AsyncCallback<DocumentPrezentaOnlineModel[], AppError>): void {
		this.apiCaller.call(ApiPathConstants.GET_ALL_DOCUMENTS_PREZENTA, null, DocumentPrezentaOnlineModel, callback);
	}

	public getAllMembriiReprezentantiByComisieGlId(comisieGlId: number, callback: AsyncCallback<PrezentaMembriiReprezentantiComisieGl[], AppError>): void {
		this.apiCaller.call(ApiPathUtils.appendParametersToPath(ApiPathConstants.GET_ALL_MEMBRII_REPREZENTANTI_BY_COMISIE_GL_ID, comisieGlId.toString()), null, PrezentaMembriiReprezentantiComisieGl, callback);
	}

	public saveParticipant(model: PrezentaMembriiReprezentantiComisieGl, callback: AsyncCallback<null, AppError>): void {
		this.apiCaller.call(ApiPathConstants.SAVE_PARTICIPANT, model, null, callback);
	}

	public getAllParticipantiByDocument(documentId: string, documentLocationRealName: string, callback: AsyncCallback<ParticipantiModel, AppError>): void {
		this.apiCaller.call(ApiPathUtils.appendParametersToPath(ApiPathConstants.GET_ALL_PARTICIPANTI_BY_DOCUMENT, documentId, documentLocationRealName), null, ParticipantiModel, callback);
	}

	public deleteParticipant(participantId: number, callback: AsyncCallback<void, AppError>): void {
		this.apiCaller.call(ApiPathUtils.appendParametersToPath(ApiPathConstants.DELETE_PARTICIPANT, participantId.toString()), null, null, callback);
	}

	public isPrezentaFinalizataByDocument(documentId: string, documentLocationRealName: string, callback: AsyncCallback<boolean, AppError>): void {
		this.apiCaller.call(ApiPathUtils.appendParametersToPath(ApiPathConstants.IS_PREZENTA_FINALIZATA_BY_DOCUMENT, documentId, documentLocationRealName), null, Boolean, callback);
	}

	public finalizarePrezentaByDocument(documentId: string, documentLocationRealName: string, callback: AsyncCallback<boolean, AppError>): void {
		this.apiCaller.call(ApiPathUtils.appendParametersToPath(ApiPathConstants.FINALIZARE_PREZENTA_BY_DOCUMENT, documentId, documentLocationRealName), null, Boolean, callback);
	}

	public importaPrezentaOnlineByDocument(documentId: string, documentLocationRealName: string, callback: AsyncCallback<void, AppError>): void {
		this.apiCaller.call(ApiPathUtils.appendParametersToPath(ApiPathConstants.IMPORTA_PREZENTA_ONLINE_BY_DOCUMENT, documentId, documentLocationRealName), null, null, callback);
	}

}