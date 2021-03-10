import { Injectable } from "@angular/core";
import { Subject ,  Observable } from "rxjs";
import { MenuItem } from "primeng/primeng";
import { TranslateUtils, StringUtils, ArrayUtils, ObjectUtils } from "@app/shared";
import { MENU_STRUCTURE, MenuItemDefinition } from "@app/app.menu-structure";

@Injectable()
export class BreadcrumbService {

	private itemsSource = new Subject<MenuItem[]>();

	itemsHandler = this.itemsSource.asObservable();

	private breadcrumbItemsByRouteUrl: object = null;
	private translateUtils: TranslateUtils;

	private currentItems: any[] = null;

	constructor(translateUtils: TranslateUtils) {
		this.translateUtils = translateUtils;
		
		this.translateUtils.onLangChangedHandler(() => {
			this.prepareBreadcrumbItems();
			if (ArrayUtils.isNotEmpty(this.currentItems)) {
				let newItems: MenuItem[] = [];
				this.currentItems.forEach((item: any) => {
					item.label = this.translateUtils.translateLabel(item.labelCode);
					newItems.push(item);
				});
				this.setItems(newItems);
			}
		});
	}

	setItems(items: MenuItem[]) {
		this.currentItems = items;
		this.itemsSource.next(items);
	}

	setItemsByRouteUrl(url: string) {
		this.setItems(this.getBreadcrumbItemsByRouteUrl(url));
	}

	private getBreadcrumbItemsByRouteUrl(url: string): MenuItem[] {
		if (ObjectUtils.isNullOrUndefined(this.breadcrumbItemsByRouteUrl)) {
			this.prepareBreadcrumbItems();
		}
		let routerLink: string = StringUtils.removeSlashFromStartAndEnd(url);
		let items: MenuItem[] = this.breadcrumbItemsByRouteUrl[routerLink];
		if (ArrayUtils.isEmpty(items)) {
			return [];
		}
		return items;
	}

	private prepareBreadcrumbItemFromDefinition(pathItems: MenuItem[], itemDefinition: MenuItemDefinition): MenuItem {
		
		let item: any = {};		
		
		item.label = this.translateUtils.translateLabel(itemDefinition.labelCode);
		item.routerLink = itemDefinition.path;
		item.labelCode = itemDefinition.labelCode;

		if (ArrayUtils.isNotEmpty(itemDefinition.items)) {
			let childMenuItems: MenuItem[] = [];
			for (let childIndex = 0; childIndex < itemDefinition.items.length; childIndex++) {
				let childItemDefinition: MenuItemDefinition = itemDefinition.items[childIndex];
				let childMenuItem: MenuItem = this.prepareBreadcrumbItemFromDefinition([...pathItems, item], childItemDefinition);
				childMenuItems.push(childMenuItem);
			}
			item.items = childMenuItems;
		}
		if (StringUtils.isNotBlank(item.routerLink)) {
			let routerLink: string = StringUtils.removeSlashFromStartAndEnd(item.routerLink);
			this.breadcrumbItemsByRouteUrl[routerLink] = [...pathItems, item];
		}
		return item;
	}

	private prepareBreadcrumbItems(): MenuItem[] {
		this.breadcrumbItemsByRouteUrl = {};		
		let items: MenuItem[] = [];
		MENU_STRUCTURE.forEach((itemDefinition: MenuItemDefinition, itemIndex: number) => {			
			let item: MenuItem = this.prepareBreadcrumbItemFromDefinition([], itemDefinition);
			items.push(item);
		});
		return items;
	}
}
