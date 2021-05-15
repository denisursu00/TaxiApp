import { AdminPermissionEnum } from "@app/shared";
import { DispatcherPermissionEnum } from "../enums";

export const AUTH_ACCESS = {

	ADMIN: {
		PARAMETERS: {
			permissions: [ AdminPermissionEnum.MANAGE_PARAMETERS ]
		},
		DRIVERS: {
			permissions: [ AdminPermissionEnum.MANAGE_DRIVERS ]
		},
		DISPATCHERS: {
			permissions: [ AdminPermissionEnum.MANAGE_DISPATCHERS ]
		},
		CARS: {
			permissions: [ AdminPermissionEnum.MANAGE_CARS ]
		}
	},
	DISPATCHER: {
		RIDES: {
			permissions: [ DispatcherPermissionEnum.MANAGE_RIDES ]
		}
	}
};
