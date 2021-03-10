import { Input, OnInit } from "@angular/core";
import { DocumentTypeModel } from "@app/shared";
import { DocumentTypeEditInfo } from "./document-type-edit-info";
import { Message } from "@app/shared";

export abstract class DocumentTypeTabContent implements OnInit {
	
	@Input()
	public mode: "add" | "edit";

	@Input()
	public inputData: DocumentTypeEditInfo;

	public ngOnInit(): void {
		this.doWhenNgOnInit();
		if (this.isAdd()) {
			this.prepareForAdd();
		} else if (this.isEdit()) {
			this.prepareForEdit();
		}
	}

	protected abstract doWhenNgOnInit(): void;

	protected isAdd(): boolean {
		return (this.mode === "add");
	}

	protected isEdit(): boolean {
		return (this.mode === "edit");
	}

	protected abstract prepareForAdd(): void;
	
	protected abstract prepareForEdit(): void;

	protected abstract isValid(): boolean;
	
	protected abstract populateDocumentType(documentType: DocumentTypeModel): void;

	public abstract getMessages(): Message[];
}