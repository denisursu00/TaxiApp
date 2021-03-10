import { Injectable } from "@angular/core";
import { Subject ,  Observable } from "rxjs";
import { MenuItem } from "primeng/primeng";
import { MENU_STRUCTURE, MenuItemDefinition } from "@app/app.menu-structure";
import { AuthManager } from "@app/shared/auth";
import { TranslateUtils, ArrayUtils, ObjectUtils, StringUtils } from "@app/shared";
import { BreadcrumbService } from "@app/breadcrumb.service";

@Injectable()
export class AppMenuService {

	private authManager: AuthManager;
	private translateUtils: TranslateUtils;

	constructor(authManager: AuthManager, translateUtils: TranslateUtils) {
		this.authManager = authManager;
		this.translateUtils = translateUtils;
	}

	public getMenu(): MenuItem[] {
		return this.prepareMenuItems();
	}

	private prepareMenuItemFromDefinition(itemDefinition: MenuItemDefinition): MenuItem {
		
		let menuItem: MenuItem = {};		
		
		menuItem.label = this.translateUtils.translateLabel(itemDefinition.labelCode);
		menuItem.routerLink = itemDefinition.path;
		menuItem.icon = itemDefinition.icon;
		
		let menuItemVisible = this.isMenuItemVisibleForLoggedInUser(itemDefinition);		
		if (menuItemVisible && ArrayUtils.isNotEmpty(itemDefinition.items)) {
			let childMenuItems: MenuItem[] = [];
			let atLeastOneItemVisible: boolean = false;
			for (let childIndex = 0; childIndex < itemDefinition.items.length; childIndex++) {
				let childItemDefinition: MenuItemDefinition = itemDefinition.items[childIndex];
				let childMenuItem: MenuItem = this.prepareMenuItemFromDefinition(childItemDefinition);
				if (!atLeastOneItemVisible) {
					atLeastOneItemVisible = childMenuItem.visible;					
				}
				childMenuItems.push(childMenuItem);
			}			
			menuItemVisible = atLeastOneItemVisible;
			menuItem.items = childMenuItems;
		}		
		menuItem.visible = menuItemVisible;
		
		return menuItem;
	}

	private isMenuItemVisibleForLoggedInUser(itemDefinition: MenuItemDefinition): boolean {
		if (ArrayUtils.isEmpty(itemDefinition.authPermissions)) {
			return true;
		}
		return this.authManager.hasAnyPermission(itemDefinition.authPermissions);
	}

	private prepareMenuItems(): MenuItem[] {		
		let menuItems: MenuItem[] = [];
		MENU_STRUCTURE.forEach((itemDefinition: MenuItemDefinition, itemIndex: number) => {			
			let menuItem: MenuItem = this.prepareMenuItemFromDefinition(itemDefinition);
			menuItem.expanded = (itemIndex === 0);
			menuItems.push(menuItem);
		});
		return menuItems;
	}
}