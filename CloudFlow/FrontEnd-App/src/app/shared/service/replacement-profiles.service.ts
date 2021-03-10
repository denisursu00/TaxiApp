import { Injectable } from "@angular/core";
import { ApiCaller } from "../api-caller";
import { AsyncCallback } from "../async-callback";
import { AppError } from "../model/app-error";
import { ApiPathConstants } from "../constants/api-path.constants";
import { ReplacementProfileModel } from "../model/replacement-profile/replacement-profile.model";
import { ApiPathUtils } from "../utils";
import { GetAvailableReplacementProfilesInWhichRequesterIsReplacementRequestModel } from "../model/content/get-available-replacement-profiles-in-which-requester-is-replacement-request.model";
import { SaveReplacementProfileRequestModel } from "../model/content/save-replacement-profile-request.model";


@Injectable()
export class ReplacementProfilesService {

	private apiCaller: ApiCaller;

	public constructor(apiCaller: ApiCaller) {
		this.apiCaller = apiCaller;
	}

	public getVisibleReplacementProfiles(callback: AsyncCallback<ReplacementProfileModel[], AppError>): void {
		return this.apiCaller.call(ApiPathConstants.GET_VISIBLE_REPLACEMENT_PROFILES, null, ReplacementProfileModel, callback);
	}

	public deleteReplacementProfileById(replacementProfileId: number, callback: AsyncCallback<ReplacementProfileModel[], AppError>): void {
		return this.apiCaller.call(ApiPathUtils.appendParametersToPath(ApiPathConstants.DELETE_REPLACEMENT_PROFILE_BY_ID, replacementProfileId.toString()), 
			null, ReplacementProfileModel, callback);
	}

	public getReplacementProfileById(replacementProfileId: number, callback: AsyncCallback<ReplacementProfileModel, AppError>): void {
		return this.apiCaller.call(ApiPathUtils.appendParametersToPath(ApiPathConstants.GET_REPLACEMENT_PROFILE_BY_ID, replacementProfileId.toString()), 
			null, ReplacementProfileModel, callback);
	}

	public saveReplacementProfile(replacementProfile: SaveReplacementProfileRequestModel, callback: AsyncCallback<null, AppError>): void {
		return this.apiCaller.call(ApiPathConstants.SAVE_REPLACEMENT_PROFILE, replacementProfile, null, callback);
	}

	public returned(replacementProfileId: number, callback: AsyncCallback<null, AppError>): void {
		return this.apiCaller.call(ApiPathUtils.appendParametersToPath(ApiPathConstants.RETURNED, replacementProfileId.toString()), null, null, callback);
	}

	public getAvailableReplacementProfilesInWhichRequesterIsReplacement(
			getAvailableReplacementProfilesInWhichRequesterIsReplacementRequestModel: GetAvailableReplacementProfilesInWhichRequesterIsReplacementRequestModel, 
			callback: AsyncCallback<ReplacementProfileModel[], AppError>): void {
		return this.apiCaller.call(ApiPathConstants.GET_AVAILABLE_REPLACEMENT_PROFILES_IN_WHICH_REQUESTER_IS_REPLACEMENT, 
				getAvailableReplacementProfilesInWhichRequesterIsReplacementRequestModel, ReplacementProfileModel, callback);
	}
}