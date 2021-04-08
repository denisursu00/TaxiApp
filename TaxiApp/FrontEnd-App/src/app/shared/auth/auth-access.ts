import { AdminPermissionEnum } from "@app/shared";

export const AUTH_ACCESS = {

	ADMIN: {
		PARAMETERS: {
			permissions: [ AdminPermissionEnum.MANAGE_PARAMETERS ]
		},
		DRIVERS: {
			permissions: [ AdminPermissionEnum.MANAGE_DRIVERS ]
		}
	}
};
