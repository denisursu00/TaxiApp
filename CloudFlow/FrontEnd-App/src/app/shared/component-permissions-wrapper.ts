import { StringUtils } from "./utils";
import { AuthManager } from "./auth";

export interface ComponentPermissionsWrapper {

	isAddPermissionAllowed(): boolean;

	isDeletePermissionAllowed(): boolean;

	isEditPermissionAllowed(): boolean;
}