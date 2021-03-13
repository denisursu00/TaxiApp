import { Injectable } from "@angular/core";
import { ApiCaller } from "./../api-caller";
import { AppError/*, OrganizationUnitModel, OrganizationTreeModel, UserModel, DirectoryUserSearchCriteriaModel, DirectoryUserModel, OrganizationTreeNodeModel, GroupModel, RoleModel, RolePermissionMappingViewModel*/ } from "./../model";
import { ApiPathConstants } from "./../constants";
import { AsyncCallback } from "./../async-callback";
import { ApiPathUtils } from "../utils";
//import { DeactivationUserModel } from "../model/organization/deactivation-user.model";
//import { OrganizationEntityModel } from "../model/organization-entity.model";

@Injectable()
export class OrganizationService {

	private apiCaller: ApiCaller;

	constructor(apiCaller: ApiCaller) {
		this.apiCaller = apiCaller;
	}

	/*public getOrganizationTree(callback: AsyncCallback<OrganizationTreeModel, AppError>): void {
		this.apiCaller.call(ApiPathConstants.GET_ORGANIZATION_TREE, null, OrganizationTreeModel, callback);
	}

	public getSortedUsers(callback: AsyncCallback<UserModel[], AppError>): void {
		this.apiCaller.call(ApiPathConstants.GET_SORTED_USERS, null, UserModel, callback);
	}*/

	public getAllUsernames(callback: AsyncCallback<String[], AppError>): void {
		this.apiCaller.call(ApiPathConstants.GET_ALL_USERNAMES, null, String, callback);
	}

	/*public getUsersWithUsername(username: string, callback: AsyncCallback<UserModel[], AppError>): void {
		this.apiCaller.call(ApiPathUtils.appendParametersToPath(ApiPathConstants.GET_USERS_WITH_USERNAME, username), null, UserModel, callback);
	}
	
	public getUsers(callback: AsyncCallback<UserModel[], AppError>): void {
		this.apiCaller.call(ApiPathConstants.GET_USERS, null, UserModel, callback);
	}

	public getAllActiveUsers(callback: AsyncCallback<UserModel[], AppError>): void {
		this.apiCaller.call(ApiPathConstants.GET_ACTIVE_USERS, null, UserModel, callback);
	}

	public getUsersFromGroup(groupId: string, callback: AsyncCallback<UserModel[], AppError>): void {
		this.apiCaller.call(ApiPathUtils.appendParametersToPath(ApiPathConstants.GET_USERS_FROM_GROUP, groupId), null, UserModel, callback);
	}

	public getUsersFromGroupByGroupName(groupName: string, callback: AsyncCallback<UserModel[], AppError>): void {
		this.apiCaller.call(ApiPathUtils.appendParametersToPath(ApiPathConstants.GET_USERS_FROM_GROUP_BY_GROUP_NAME, groupName), null, UserModel, callback);
	}

	public getUserById(id: string, callback: AsyncCallback<UserModel, AppError>): void {
		this.apiCaller.call(ApiPathUtils.appendParametersToPath(ApiPathConstants.GET_USER_BY_ID, id), null, UserModel, callback);
	}
	
	public setUser(userModel: UserModel, callback: AsyncCallback<null, AppError>): void {
		this.apiCaller.call(ApiPathConstants.SET_USER, userModel, null, callback);
	}
	
	public getOrgUnitById(id: string, callback: AsyncCallback<OrganizationUnitModel, AppError>): void {
		this.apiCaller.call(ApiPathUtils.appendParametersToPath(ApiPathConstants.GET_ORG_UNIT_BY_ID, id), null, OrganizationUnitModel, callback);
	}
	
	public setOrgUnit(organizationUnitModel: OrganizationUnitModel, callback: AsyncCallback<null, AppError>): void {
		this.apiCaller.call(ApiPathConstants.SET_ORG_UNIT, organizationUnitModel, null, callback);
	}
	
	public getUsersFromOrgUnit(id: string, callback: AsyncCallback<UserModel[], AppError>): void {
		this.apiCaller.call(ApiPathUtils.appendParametersToPath(ApiPathConstants.GET_USERS_FROM_ORG_UNIT, id), null, UserModel, callback);
	}
	
	public findUsersInDirectory(searchCriteria: DirectoryUserSearchCriteriaModel, callback: AsyncCallback<DirectoryUserModel[], AppError>): void {
		this.apiCaller.call(ApiPathConstants.FIND_USERS_IN_DIRECTORY, searchCriteria, DirectoryUserModel, callback);
	}*/
	
	public importOrganizationalStructureFromDirectory(callback: AsyncCallback<null, AppError>): void {
		this.apiCaller.call(ApiPathConstants.IMPORT_ORGANIZATIONAL_STRUCTURE_FROM_DIRECTORY, null, null, callback);
	}
	
	/*public deleteOrgUnit(oraganizationUnitModel: OrganizationUnitModel, callback: AsyncCallback<null, AppError>): void {
		this.apiCaller.call(ApiPathConstants.DELETE_ORGANIZATION_UNIT, oraganizationUnitModel, null, callback);
	}
	
	public deleteUser(userModel: UserModel, callback: AsyncCallback<null, AppError>): void {
		this.apiCaller.call(ApiPathConstants.DELETE_USER, userModel, null, callback);
	}

	public deactivateUser(deactivationUserModel: DeactivationUserModel, callback: AsyncCallback<null, AppError>): void {
		this.apiCaller.call(ApiPathConstants.DEACTIVATE_USER, deactivationUserModel, null, callback);
	}*/

	public reactivateUser(id: string, callback: AsyncCallback<null, AppError>): void {
		this.apiCaller.call(ApiPathUtils.appendParametersToPath(ApiPathConstants.REACTIVATE_USER_WITH_ID, id), null, null, callback);
	}

	public moveUserToOrganization(idOfUserToMove: string, idOfOrganizationToMoveTo: string, callback: AsyncCallback<null, AppError>): void {
		this.apiCaller.call(ApiPathUtils.appendParametersToPath(ApiPathConstants.MOVE_USER_TO_ORGANIZATION, idOfUserToMove, idOfOrganizationToMoveTo), null, null, callback);
	}

	public moveUserToOrganizationUnit(idOfUserToMove: string, idOfOrganizationUnitToMoveTo: string, callback: AsyncCallback<null, AppError>): void {
		this.apiCaller.call(ApiPathUtils.appendParametersToPath(ApiPathConstants.MOVE_USER_TO_ORGANIZATION_UNIT, idOfUserToMove, idOfOrganizationUnitToMoveTo), null, null, callback);
	}

	public moveOrganizationUnitToOrganization(idOfOrganizationUnitToMove: string, idOfOrganizationToMoveTo: string, callback: AsyncCallback<null, AppError>): void {
		this.apiCaller.call(ApiPathUtils.appendParametersToPath(ApiPathConstants.MOVE_ORGANIZATION_UNIT_TO_ORGANIZATION, idOfOrganizationUnitToMove, idOfOrganizationToMoveTo), null, null, callback);
	}

	public moveOrganizationUnitToOrganizationUnit(idOfOrganizationUnitToMove: string, idOfOrganizationUnitToMoveTo: string, callback: AsyncCallback<null, AppError>): void {
		this.apiCaller.call(ApiPathUtils.appendParametersToPath(ApiPathConstants.MOVE_ORGANIZATION_UNIT_TO_ORGANIZATION_UNIT, idOfOrganizationUnitToMove, idOfOrganizationUnitToMoveTo), null, null, callback);
	}

	/*public getNamesForOrganizationEntities(organizations: OrganizationEntityModel[], callback: AsyncCallback<string[], AppError>): void {
		this.apiCaller.call(ApiPathConstants.GET_NAMES_FOR_ORGANIZATION_ENTITIES, organizations, String, callback);
	}
	
	public deleteGroup(group: GroupModel, callback: AsyncCallback<null, AppError>): void {
		this.apiCaller.call(ApiPathConstants.DETELE_GROUP, group, null, callback);
	}
	
	public getGroupById(groupId: string, callback: AsyncCallback<GroupModel, AppError>): void {
		this.apiCaller.call(ApiPathUtils.appendParametersToPath(ApiPathConstants.GET_GROUP_BY_ID, groupId), null, GroupModel, callback);
	}
	
	public setGroup(groupModel: GroupModel, callback: AsyncCallback<null, AppError>): void {
		this.apiCaller.call(ApiPathConstants.SET_GROUP, groupModel, null, callback);
	}

	public getAvailableRolesForUser(userId: string, callback: AsyncCallback<RoleModel[], AppError>): void {
		this.apiCaller.call(ApiPathUtils.appendParametersToPath(ApiPathConstants.GET_AVAILABLE_ROLES_FOR_USER, userId), null, String, callback);
	}

	public getAllRoles(callback: AsyncCallback<RoleModel[], AppError>): void {
		this.apiCaller.call(ApiPathConstants.GET_ALL_ROLES, null, String, callback);
	}
	
	public getAllUsersWithAssignedTasks(callback: AsyncCallback<UserModel[], AppError>): void {
		this.apiCaller.call(ApiPathConstants.GET_ALL_USERS_WITH_ASSIGNED_TASKS, null, UserModel, callback);
	}

	public getAllRolePermissionMappingViews(callback: AsyncCallback<RolePermissionMappingViewModel[], AppError>): void {
		this.apiCaller.call(ApiPathConstants.GET_ALL_ROLE_PERMISSION_MAPPING_VIEWS, null, RolePermissionMappingViewModel, callback);
	}*/
}