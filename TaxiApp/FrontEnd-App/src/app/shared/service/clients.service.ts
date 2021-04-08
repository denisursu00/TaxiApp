import { ApiCaller } from "../api-caller";
import { AsyncCallback } from "../async-callback";
import { AppError } from "../model";
import { ApiPathConstants } from "../constants";
import { ApiPathUtils } from "../utils";
import { Injectable } from "@angular/core";
import { ClientModel } from "../model/clients/client.model";

@Injectable()
export class ClientsService {

    private apiCaller: ApiCaller;

	constructor(apiCaller: ApiCaller) {
		this.apiCaller = apiCaller;
	}

    public saveClient(clientModel: ClientModel, callback: AsyncCallback<Number, AppError>): void {
        this.apiCaller.call(ApiPathConstants.CLIENT_SAVE, clientModel, Number, callback);
    }

    public getClientById(id: Number, callback: AsyncCallback<ClientModel, AppError>): void {
        let relativePath: string = ApiPathUtils.appendParametersToPath(ApiPathConstants.CLIENT_GET_BY_ID, id.toString());
        this.apiCaller.call(relativePath, null, ClientModel, callback);
    }

    public getClientByUserId(userId: Number, callback: AsyncCallback<ClientModel, AppError>): void {
        let relativePath: string = ApiPathUtils.appendParametersToPath(ApiPathConstants.CLIENT_GET_BY_USER_ID, userId.toString());
        this.apiCaller.call(relativePath, null, ClientModel, callback);
    }

    public deleteClientById(id: Number, callback: AsyncCallback<null, AppError>): void {
        let relativePath: string = ApiPathUtils.appendParametersToPath(ApiPathConstants.CLIENT_DELETE_BY_ID, id.toString());
        this.apiCaller.call(relativePath, null, null, callback);
    }

}