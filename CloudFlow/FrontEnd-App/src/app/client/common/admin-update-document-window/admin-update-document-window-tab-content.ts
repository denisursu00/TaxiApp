import { Input, OnInit } from "@angular/core";
import { AdminUpdateDocumentModel, Message } from "@app/shared";
import { AdminUpdateDocumentWindowTabInputData } from "./admin-update-document-window-tab-input-data";

export abstract class AdminUpdateDocumentWindowTabContent implements OnInit {
	
	@Input()
	public inputData: AdminUpdateDocumentWindowTabInputData;

	public ngOnInit(): void {
		this.doWhenOnInit();
		this.prepareForUpdate();
	}

	protected abstract doWhenOnInit(): void;

	protected abstract prepareForUpdate(): void;

	protected abstract isValid(): boolean;
	
	protected abstract populateDocument(document: AdminUpdateDocumentModel): void;

	public abstract getMessages(): Message[];
}