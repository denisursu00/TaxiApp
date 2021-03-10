import { Component, ViewChild, Input, OnInit } from "@angular/core";
import { PermissionManagerComponent, PermissionModel, AclService, PermissionBusinessUtils, SecurityManagerModel, AppError, MessageDisplayer, FolderModel, ArrayUtils } from "@app/shared";

@Component({
	selector: "app-folder-security-tab-content",
	templateUrl: "./folder-security-tab-content.component.html",
	styleUrls: ["./folder-security-tab-content.component.css"]
})
export class FolderSecurityTabContentComponent implements OnInit {
	
	@Input()
	public mode: "new" | "edit";

	@Input()
	public permissions: PermissionModel[];

	@ViewChild(PermissionManagerComponent)
	private permissionManager: PermissionManagerComponent;
	
	private aclService: AclService;
	private messageDisplayer: MessageDisplayer;

	public readonly: boolean;
	public activateDefaultPermissions: boolean;

	public constructor(aclService: AclService, messageDisplayer: MessageDisplayer) {
		this.aclService = aclService;
		this.messageDisplayer = messageDisplayer;
	}

	public ngOnInit(): void {
		if (this.mode === "new") {
			this.prepareForAdd();
		} else if (this.mode === "edit") {
			this.prepareForEdit();
		}
	}

	public prepareForAdd(): void {
		this.activateDefaultPermissions = true;
		this.readonly = false;
	}

	public prepareForEdit(): void {
		this.activateDefaultPermissions = false;
		this.aclService.getSecurityManager({
			onSuccess: (securityManager: SecurityManagerModel): void => {
				this.readonly = !PermissionBusinessUtils.canChangePermissions(this.permissions, securityManager);
			},
			onFailure: (appError: AppError): void => {
				this.messageDisplayer.displayAppError(appError);
			}
		});
	}
	
	public isValid(): boolean {
		return this.validate();
	}
	
	private validate(): boolean {
		let isValid: boolean = this.permissionManager.isValid();		
		if (!isValid) {
			let permissions: PermissionModel[] = this.permissionManager.getPermissions();
			let validationMessageCodes = this.permissionManager.getValidationMessageCodes();
			if (ArrayUtils.isNotEmpty(validationMessageCodes)) {
				validationMessageCodes.forEach((messageCode: string) => {
					this.messageDisplayer.displayError(messageCode);
				});
			}
		}
		return isValid;
	}

	public populateFolder(folder: FolderModel): void {
		folder.permissions = this.permissionManager.getPermissions();
	}
}
