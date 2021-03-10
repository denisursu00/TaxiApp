import { Component, OnInit, ViewChild } from "@angular/core";
import { AppError, Message, MessageDisplayer, ArrayUtils, StringUtils, ObjectUtils, AdminUpdateDocumentModel } from "@app/shared";
import { AdminUpdateDocumentWindowTabContent } from "./../admin-update-document-window-tab-content";

@Component({
	selector: "app-admin-update-document-attachments-tab-content",
	templateUrl: "./attachments-tab-content.component.html"
})
export class AdminUpdateDocumentWindowAttachmentsTabContentComponent extends AdminUpdateDocumentWindowTabContent {
	
	private messages: Message[];

	public constructor() {
		super();
		this.messages = [];
	}

	public doWhenOnInit(): void {
	}

	protected prepareForUpdate(): void {
		// Nothing here.
	}

	public isValid(): boolean {
		return this.validate();
	}
	
	private validate(): boolean {
		this.messages = [];
		// 	TODO
		return ArrayUtils.isEmpty(this.messages);
	}


	public populateDocument(document: AdminUpdateDocumentModel): void {
		// TODO 
	}

	public getMessages(): Message[] {
		return this.messages;
	}
}