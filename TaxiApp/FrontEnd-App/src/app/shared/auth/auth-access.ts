import { AdminPermissionEnum } from "@app/shared";
import { ClientPermissionEnum, DispatcherPermissionEnum, DriverPermissionEnum } from "../enums";

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
	},
	DRIVER: {
		PERSONAL_PAGE: {
			permissions: [ DriverPermissionEnum.PERSONAL_PAGE ]
		}
	},
	CLIENT: {
		PERSONAL_PAGE: {
			permissions: [ ClientPermissionEnum.PERSONAL_PAGE ]
		}
	}
};
