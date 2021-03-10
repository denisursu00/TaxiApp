import { Component, OnInit, ViewChild } from "@angular/core";
import { MetadataDefinitionModel, ArrayUtils, ObjectUtils } from "@app/shared";
import { DocumentTypeModel, Message } from "@app/shared";
import { DocumentTypeTabContent } from "./../document-type-tab-content";
import { MetadataDefinitionsManagerInputData } from "./../metadata-definitions-manager/metadata-definitions-manager-input-data";
import { MetadataDefinitionsManagerComponent } from "./../metadata-definitions-manager/metadata-definitions-manager.component";

@Component({
	selector: "app-document-type-metadata-tab-content",
	templateUrl: "./document-type-metadata-tab-content.component.html"
})
export class DocumentTypeMetadataTabContentComponent extends DocumentTypeTabContent {
	
	@ViewChild(MetadataDefinitionsManagerComponent)
	public metadataDefinitionsManager: MetadataDefinitionsManagerComponent;

	public metadataDefinitionsManagerVisible: boolean;
	public metadataDefinitionsManagerInputData: MetadataDefinitionsManagerInputData;

	private messages: Message[];

	public constructor() {
		super();
		this.metadataDefinitionsManagerVisible = false;
	}

	protected doWhenNgOnInit(): void {
	}

	protected prepareForAdd(): void {
		this.metadataDefinitionsManagerVisible = true;
	}

	protected prepareForEdit(): void {
		let newMetadataDefinitionsManagerInputData: MetadataDefinitionsManagerInputData = new MetadataDefinitionsManagerInputData();
		if (ArrayUtils.isNotEmpty(this.inputData.documentType.metadataDefinitions)) {
			newMetadataDefinitionsManagerInputData.metadataDefinitions = this.inputData.documentType.metadataDefinitions;
		}
		newMetadataDefinitionsManagerInputData.workflowStates = this.inputData.workflowStates;
		this.metadataDefinitionsManagerInputData = newMetadataDefinitionsManagerInputData;
		this.metadataDefinitionsManagerVisible = true;
	}

	public populateDocumentType(documentType: DocumentTypeModel): void {
		documentType.metadataDefinitions = this.metadataDefinitionsManager.getMetadataDefinitions();
	}

	public isValid(): boolean {
		return this.validate();
	}

	private validate(): boolean {
		this.messages = [];
		let isValid: boolean = true;
		let metadataDefinitions: MetadataDefinitionModel[] = this.metadataDefinitionsManager.getMetadataDefinitions();
		if (ArrayUtils.isNotEmpty(metadataDefinitions)) {
			metadataDefinitions.forEach((metadataDefinition: MetadataDefinitionModel) => {
				if (metadataDefinition.type === MetadataDefinitionModel.TYPE_METADATA_COLLECTION) {
					if (ArrayUtils.isEmpty(metadataDefinition.metadataDefinitions)) {
						isValid = false;					
					}
				}
			});
		}
		if (!isValid) {
			this.messages.push(Message.createForError("SOME_COLLECTIONS_HAVE_NO_DEFINED_METADATAS"));
		}
		return isValid;
	}

	public getMessages(): Message[] {
		return this.messages;
	}
}