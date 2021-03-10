import { Component, Input, Output, OnInit, EventEmitter, AfterViewInit } from "@angular/core";
import { FormBuilder, FormGroup, AbstractControl } from "@angular/forms";
import { AppError, MessageDisplayer, MetadataDefinitionModel, ArrayUtils, MetadataCollectionInstanceRowModel, BooleanUtils, DocumentTypeModel, DocumentConstants, BaseWindow } from "@app/shared"; 
import { FormUtils, Message, MetadataCollectionInstanceModel, DocumentValidationResponseModel } from "@app/shared";
import {
	IconConstants, 
	ListMetadataItemModel, 
	MetadataInstanceModel ,
	DocumentModel,
	ObjectUtils,
	DateUtils,
	WorkflowStateModel,
	DocumentCollectionValidationRequestModel
} from "@app/shared";
import { AdminUpdateDocumentMetadataInputData } from "./../../document-metadata/document-metadata.component";
import { MetadataCollectionWindowContent } from "./metadata-collection-window-content";
import { AdminUpdateDocumentMetadataCollectionWindowDynamicContentComponent } from "./metadata-collection-window-dynamic-content.component";
import { ViewChild } from "@angular/core";
import { Dialog } from "primeng/primeng";

@Component({
	selector: "app-admin-update-document-metadata-collection-window",
	templateUrl: "./metadata-collection-window.component.html"
})
export class AdminUpdateDocumentMetadataCollectionWindowComponent extends BaseWindow implements OnInit {

	@Input()
	public inputData: MetadataCollectionWindowInputData;

	@Output()
	private canceled: EventEmitter<void>;

	@Output()
	private saved: EventEmitter<MetadataCollectionInstanceRowModel>;

	@ViewChild(AdminUpdateDocumentMetadataCollectionWindowDynamicContentComponent)
	private dynamicContentComponent: AdminUpdateDocumentMetadataCollectionWindowDynamicContentComponent;

	private metadataCollectionDefinition: MetadataDefinitionModel;

	public windowVisible: boolean;
	public title: string;
	public width: number;
	public height: any;

	public dynamicContentEnabled: boolean = false;

	public constructor() {
		super();
		this.saved = new EventEmitter();
		this.canceled = new EventEmitter();
		this.unlock();
	}

	public ngOnInit(): void {
		if (ObjectUtils.isNullOrUndefined(this.inputData)) {
			throw new Error("inputData cannot be null");
		}		
		this.metadataCollectionDefinition = this.inputData.metadataCollectionDefinition;
		this.init();
	}
	
	private init(): void {
		this.title = this.metadataCollectionDefinition.label;
		this.enableContent();
		this.windowVisible = true;
	}

	private enableContent() {
		this.dynamicContentEnabled = true;
	}

	public onSaveAction(event: any) {
		this.lock();
		this.validate((valid: boolean): void => {
			if (valid) {
				this.saved.emit(this.prepareMetadataCollectionInstance());
				this.windowVisible = false;
			} else {
				this.unlock();
			}
		});
	}	

	private prepareMetadataCollectionInstance(): MetadataCollectionInstanceRowModel {		
		let rowModel: MetadataCollectionInstanceRowModel = new MetadataCollectionInstanceRowModel();
		rowModel.id = ObjectUtils.isNotNullOrUndefined(this.inputData.collectionInstanceRow) ? this.inputData.collectionInstanceRow.id : null;		
		rowModel.metadataInstanceList = this.getContentComponent().getMetadataInstances();
		return rowModel;
	}

	private validate(callback: (valid: boolean) => void): void {
		let isValid: boolean = this.getContentComponent().validate();
		callback(isValid);
	}

	private getContentComponent(): MetadataCollectionWindowContent {
		if (ObjectUtils.isNotNullOrUndefined(this.dynamicContentComponent)) {
			return this.dynamicContentComponent;
		}
		throw new Error("nu s-a putut determina componenta de content");
	}

	public onCloseAction(event: any) {
		this.windowVisible = false;
		this.canceled.emit();
	}

	public onHide(event: any): void {
		this.canceled.emit();
	}
}

export interface MetadataCollectionWindowInputData {

	documentId?: string;
	documentLocationRealName?: string;
	documentType: DocumentTypeModel;
	
	metadataCollectionDefinition: MetadataDefinitionModel;
	collectionInstanceRow: MetadataCollectionInstanceRowModel;
}