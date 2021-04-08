import { AdminPermissionEnum} from "@app/shared";
import { AUTH_ACCESS } from "@app/shared/auth";

export interface MenuItemDefinition {
	labelCode: string;
	icon?: string;
	path?: string;
	authPermissions?: AdminPermissionEnum[];
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
			}
		]
	}
];