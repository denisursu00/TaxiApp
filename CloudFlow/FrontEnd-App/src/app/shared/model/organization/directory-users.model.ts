import { DirectoryUserModel } from "./directory-user.model";

export interface DirectoryUsersModel {
	directoryUsers: DirectoryUserModel[];
	organizationId: string;
	organizationUnitId: string;
}