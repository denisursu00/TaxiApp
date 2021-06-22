import { AdminPermissionEnum, ClientPermissionEnum, DispatcherPermissionEnum, DriverPermissionEnum} from "@app/shared";
import { AUTH_ACCESS } from "@app/shared/auth";

export interface MenuItemDefinition {
	labelCode: string;
	icon?: string;
	path?: string;
	authPermissions?: AdminPermissionEnum[] | DispatcherPermissionEnum[] | DriverPermissionEnum[] | ClientPermissionEnum[];
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
	},
	{
		labelCode: "DRIVER",
		items: [
			{
				labelCode: "PERSONAL_PAGE",
				icon: "fa fa-user",
				path: "driver/personal-page",
				authPermissions: AUTH_ACCESS.DRIVER.PERSONAL_PAGE.permissions
			},
			{
				labelCode: "DRIVER_ACCEPT_ORDER_PAGE",
				icon: "pi pi-map-marker",
				path: "driver/accept-order",
				authPermissions: AUTH_ACCESS.DRIVER.ACCEPT_ORDER.permissions
			}
		]
	},
	{
		labelCode: "CLIENT",
		items: [
			{
				labelCode: "PERSONAL_PAGE",
				icon: "fa fa-user",
				path: "client/personal-page",
				authPermissions: AUTH_ACCESS.CLIENT.PERSONAL_PAGE.permissions
			},
			{
				labelCode: "ORDER_TAXI",
				icon: "pi pi-map-marker",
				path: "client/order-taxi",
				authPermissions: AUTH_ACCESS.CLIENT.ORDER_TAXI.permissions
			}
		]
	}
];