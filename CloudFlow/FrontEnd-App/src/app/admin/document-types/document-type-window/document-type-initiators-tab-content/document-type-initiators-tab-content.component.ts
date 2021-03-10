import { Component, OnInit} from "@angular/core";
import { AppError, ArrayUtils } from "@app/shared";
import { DocumentTypeModel } from "@app/shared";
import { DocumentTypeEditInfo } from "./../document-type-edit-info";
import { DocumentTypeTabContent } from "./../document-type-tab-content";
import { OrganizationEntityModel } from "@app/shared/model/organization-entity.model";
import { Message } from "@app/shared";

@Component({
	selector: "app-document-type-initiators-tab-content",
	templateUrl: "./document-type-initiators-tab-content.component.html"
})
export class DocumentTypeInitiatorsTabContentComponent extends DocumentTypeTabContent {

	public initiators: OrganizationEntityModel[];
	public allowAnyInitiator: boolean = false;
	public readonly: boolean = false;

	private messages: Message[];

	public constructor() {
		super();
	}

	protected doWhenNgOnInit(): void {
		// Nothing here, just carry on
	}

	protected prepareForAdd(): void {
		// Nothing here, just carry on
	}
	
	protected prepareForEdit(): void {
		this.initiators = this.inputData.documentType.initiators;
		this.allowAnyInitiator = this.inputData.documentType.allowAnyInitiator;
		this.readonly = this.allowAnyInitiator;
	}

	public populateDocumentType(documentType: DocumentTypeModel): void {
		if (this.allowAnyInitiator) {
			documentType.allowAnyInitiator = this.allowAnyInitiator;
			documentType.initiators = [];
		} else {
			documentType.allowAnyInitiator = this.allowAnyInitiator;
			documentType.initiators = this.initiators;
		}
	}

	public isValid(): boolean {
		return this.validate();
	}

	private validate(): boolean {
		this.messages = [];
		let isValid: boolean = !(ArrayUtils.isEmpty(this.initiators) && !this.allowAnyInitiator);
		if (!isValid) {
			this.messages.push(Message.createForError("NO_SELECTED_INITIATORS"));
		}
		return isValid;
	}

	public onOrganizationalStructureEntitiesSelectorSelectionChanged(selectedEntities: OrganizationEntityModel[]): void {
		this.initiators = selectedEntities;
	}

	public onChangeAllowAnyInitiator(event: any): void {
		this.readonly = this.allowAnyInitiator;
	}

	public getMessages(): Message[] {
		return this.messages;
	}
}