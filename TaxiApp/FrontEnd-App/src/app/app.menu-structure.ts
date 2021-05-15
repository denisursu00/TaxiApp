import { AdminPermissionEnum, DispatcherPermissionEnum} from "@app/shared";
import { AUTH_ACCESS } from "@app/shared/auth";

export interface MenuItemDefinition {
	labelCode: string;
	icon?: string;
	path?: string;
	authPermissions?: AdminPermissionEnum[] | DispatcherPermissionEnum[];
	items?: MenuItemDefinition[];
}

export const MENU_STRUCTURE: MenuItemDefinition[] = [
	{
		labelCode: "ADMIN",
		items: [
			{
				labelCode: "PARAMETERS",
				icon: "fa fa-list-ul",
				path: "admin/parameters",
				authPermissions: AUTH_ACCESS.ADMIN.PARAMETERS.permissions
			},
			{
				labelCode: "DRIVERS",
				icon: "fa fa-user",
				path: "admin/drivers",
				authPermissions: AUTH_ACCESS.ADMIN.DRIVERS.permissions
			},
			{
				labelCode: "DISPATCHERS",
				icon: "fa fa-user",
				path: "admin/dispatchers",
				authPermissions: AUTH_ACCESS.ADMIN.DISPATCHERS.permissions
			},
			{
				labelCode: "CARS",
				icon: "fa fa-gear",
				path: "admin/cars",
				authPermissions: AUTH_ACCESS.ADMIN.CARS.permissions
			}
		]
	},
	{
		labelCode: "DISPATCHER",
		items: [
			{
				labelCode: "RIDES",
				icon: "fa fa-list-ul",
				path: "dispatcher/rides",
				authPermissions: AUTH_ACCESS.DISPATCHER.RIDES.permissions
			}
		]
	}
];