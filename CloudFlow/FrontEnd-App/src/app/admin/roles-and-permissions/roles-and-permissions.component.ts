import { Component, OnInit } from "@angular/core";
import { OrganizationService, RolePermissionMappingViewModel } from "@app/shared";
import { ArrayUtils, ObjectUtils, AppError } from "@app/shared";
import { MessageDisplayer } from "@app/shared";

@Component({
	selector: "app-roles",
	templateUrl: "./roles-and-permissions.component.html"
})
export class RolesAndPermissionsComponent implements OnInit {
	
	private organizationService: OrganizationService;
	private messageDisplayer: MessageDisplayer;

	public rolePermissionMappingViews: RolePermissionMappingViewModel[];
	public loading: boolean;
	public rowGroupMetadata: any;

	public scrollHeight: string;

	public constructor(organizationService: OrganizationService, 
			messageDisplayer: MessageDisplayer) {
		this.organizationService = organizationService;
		this.messageDisplayer = messageDisplayer;	
		this.init();
	}

	private init(): void {
		this.loading = false;
		this.rolePermissionMappingViews = [];
		this.scrollHeight = (window.innerHeight - 180) + "px";
	}

	public ngOnInit(): void {
		this.loadRoles();
	}

	private lock(): void {
		this.loading = true;
	}

	private unlock(): void {
		this.loading = false;
	}

	private loadRoles(): void {
		this.lock();
		this.organizationService.getAllRolePermissionMappingViews({
			onSuccess: (rolePermissionMappingViews: RolePermissionMappingViewModel[]) => {
				this.rolePermissionMappingViews = rolePermissionMappingViews;
				this.updateRowGroupMetaData();
				this.unlock();
			},
			onFailure: (error: AppError) => {
				this.unlock();
				this.messageDisplayer.displayAppError(error);
			}
		});
	}

	private refresh(): void {
		this.loadRoles();
	}

	public onRefresh(event: any): void {
		this.refresh();
	}

	public onSort() {
		this.updateRowGroupMetaData();
	}

	public updateRowGroupMetaData() {
		this.rowGroupMetadata = {};
		if (this.rolePermissionMappingViews) {
			for (let i = 0; i < this.rolePermissionMappingViews.length; i++) {
				let rowData = this.rolePermissionMappingViews[i];
				let brand = rowData.role;
				if (i === 0) {
					this.rowGroupMetadata[brand] = { index: 0, size: 1 };
				} else {
					let previousRowData = this.rolePermissionMappingViews[i - 1];
					let previousRowGroup = previousRowData.role;
					if (brand === previousRowGroup) {
						this.rowGroupMetadata[brand].size++;
					} else {
						this.rowGroupMetadata[brand] = { index: i, size: 1 };
					}
				}
			}
		}
	}
}
