import { Injectable } from "@angular/core";
import { ApiCaller } from "./../api-caller";
import { AppError } from "./../model";
import { ApiPathConstants } from "./../constants";
import { AsyncCallback } from "./../async-callback";
import { GroupModel } from "../model/organization/group.model";

@Injectable()
export class GroupService {

	private apiCaller: ApiCaller;

	public constructor(apiCaller: ApiCaller) {
		this.apiCaller = apiCaller;
	}
	
	public getGroups(callback: AsyncCallback<GroupModel[], AppError>): void {
		this.apiCaller.call(ApiPathConstants.GET_GROUPS, null, GroupModel, callback);
	}
}