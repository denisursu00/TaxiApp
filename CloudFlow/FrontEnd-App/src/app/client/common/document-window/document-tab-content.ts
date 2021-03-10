import { Input, OnInit } from "@angular/core";
import { DocumentModel, DocumentTypeModel, WorkflowModel, WorkflowStateModel, Message } from "@app/shared";
import { DocumentAddInfo } from "./document-add-info";
import { DocumentViewOrEditInfo } from "./document-view-or-edit-info";

export abstract class DocumentTabContent implements OnInit {
	
	@Input()
	public inputData: DocumentAddInfo | DocumentViewOrEditInfo;

	@Input()
	public mode: "add" | "viewOrEdit";

	@Input()
	public readonly: boolean = true;

	public ngOnInit(): void {
		this.doWhenOnInit();
		if (this.isAdd()) {
			this.prepareForAdd();
		} else if (this.isViewOrEdit()) {
			this.prepareForViewOrEdit();
		}
	}

	protected abstract doWhenOnInit(): void;

	protected isAdd(): boolean {
		return (this.mode === "add");
	}

	protected isViewOrEdit(): boolean {
		return (this.mode === "viewOrEdit");
	}

	protected abstract prepareForAdd(): void;
	
	protected abstract prepareForViewOrEdit(): void;

	protected abstract isValid(): boolean;
	
	protected abstract populateDocument(document: DocumentModel): void;

	public abstract getMessages(): Message[];
}