import { Component, Input, OnInit } from "@angular/core";
import { FormGroup } from "@angular/forms";
import { DateUtils, BooleanUtils, ObjectUtils, StringUtils, AppError, DateConstants } from "@app/shared";
import { MetadataInstanceModel, DocumentTypeModel, ArrayUtils, NomenclatorUtils } from "@app/shared";
import { MetadataDefinitionModel } from "@app/shared";
import { AutocompleteMetadataRequestModel, AutocompleteMetadataResponseModel } from "@app/shared";
import { MessageDisplayer } from "@app/shared";
import { MetadataUtils } from "./metadata-utils";
import { AbstractControl } from "@angular/forms";
import { AdminUpdateDocumentMetadataCollectionFieldInputData } from "./../metadata-collection-field";

@Component({
	selector: "app-admin-update-document-metadata",
	templateUrl: "./document-metadata.component.html"
})
export class AdminUpdateDocumentMetadataComponent implements OnInit {

	@Input()
	public inputData: AdminUpdateDocumentMetadataInputData;

	@Input()
	public formGroup: FormGroup;
	
	private messageDisplayer: MessageDisplayer;

	public metadataDefinition: MetadataDefinitionModel;
	
	public readonly: boolean = false;

	public dateFormat: string;
	public dateTimeFormat: string;
	public monthFormat: string;
	public yearRange: string;

	public metadataCollectionFieldInputData: AdminUpdateDocumentMetadataCollectionFieldInputData;

	public constructor(messageDisplayer: MessageDisplayer) {
		this.messageDisplayer = messageDisplayer;
		this.dateFormat = DateConstants.DATE_FORMAT_FOR_TYPING;
		this.dateTimeFormat = DateConstants.DATE_TIME_FORMAT_FOR_TYPING;
		this.monthFormat = DateConstants.MONTH_FORMAT_FOR_TYPING;
		this.yearRange = DateUtils.getDefaultYearRangeForMetadata();
	}

	public ngOnInit(): void {

		if (ObjectUtils.isNullOrUndefined(this.inputData)) {
			throw new Error("inputData cannot be null");
		}

		this.metadataDefinition = this.inputData.metadataDefinition;
		this.makeMetadataCollectionFieldInputDataIfApplicable();
	}


	public makeMetadataCollectionFieldInputDataIfApplicable(): void {
		if (this.metadataDefinition.type === MetadataDefinitionModel.TYPE_METADATA_COLLECTION) {
			this.metadataCollectionFieldInputData = {
				documentId: this.inputData.documentId,
				documentLocationRealName: this.inputData.documentLocationRealName,
				documentType: this.inputData.documentType,
				metadataCollectionDefinition: this.inputData.metadataDefinition
			};
		}
	}

	public get metadataFormControl() {
		return this.formGroup.get(this.inputData.metadataDefinition.name);
	}

	public onMetadataValueChanged(): void {
	}
}

export interface AdminUpdateDocumentMetadataInputData {

	documentId?: string;
	documentLocationRealName?: string;
	documentType: DocumentTypeModel;

	metadataDefinition: MetadataDefinitionModel;
}
