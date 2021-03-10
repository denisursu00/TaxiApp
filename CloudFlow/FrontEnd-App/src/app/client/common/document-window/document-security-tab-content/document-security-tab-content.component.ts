import { Component, OnInit, ViewChild } from "@angular/core";
import { DocumentTabContent } from "./../document-tab-content";
import { DocumentModel, PermissionManagerComponent, ArrayUtils, PermissionModel, Message } from "@app/shared";
import { DocumentAddInfo } from "./../document-add-info";
import { DocumentViewOrEditInfo } from "./../document-view-or-edit-info";

@Component({
	selector: "app-document-security-tab-content",
	templateUrl: "./document-security-tab-content.component.html"
})
export class DocumentSecurityTabContentComponent extends DocumentTabContent {

	@ViewChild(PermissionManagerComponent)
	private permissionManagerComponent: PermissionManagerComponent;

	private messages: Message[];

	public permissions: PermissionModel[];
	public activateDefaultPermissions: boolean;

	public constructor() {
		super();
	}

	protected doWhenOnInit(): void {
		// Nothing here.
	}

	protected prepareForAdd(): void {
		this.readonly = false;
		this.activateDefaultPermissions = true;
	}
	
	protected prepareForViewOrEdit(): void {
		let viewOrEditInfo: DocumentViewOrEditInfo = <DocumentViewOrEditInfo> this.inputData;
		this.permissions = viewOrEditInfo.document.permissions;
		this.activateDefaultPermissions = false;
	}

	public isValid(): boolean {
		return this.validate();
	}
	
	private validate(): boolean {
		this.messages = [];
		let isValid: boolean = this.permissionManagerComponent.isValid();		
		if (!isValid) {
			let permissions: PermissionModel[] = this.permissionManagerComponent.getPermissions();
			let validationMessageCodes = this.permissionManagerComponent.getValidationMessageCodes();
			if (ArrayUtils.isNotEmpty(validationMessageCodes)) {
				validationMessageCodes.forEach((errorCode: string) => {
					this.messages.push(Message.createForError(errorCode));
				});
			}
		}
		return isValid;
	}

	public populateDocument(document: DocumentModel): void {
		document.permissions = this.permissionManagerComponent.getPermissions();
	}

	public getMessages(): Message[] {
		return this.messages;
	}
}