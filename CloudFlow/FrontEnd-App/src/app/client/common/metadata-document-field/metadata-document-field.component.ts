import { Component, Input, Output, OnInit, OnChanges, SimpleChanges, EventEmitter } from "@angular/core";
import { ControlValueAccessor, NG_VALUE_ACCESSOR } from "@angular/forms";
import { TranslateUtils, BooleanUtils, ObjectUtils, ArrayUtils, NomenclatorUtils, DocumentService, StringUtils } from "@app/shared";
import { AppError, MessageDisplayer, MetadataDefinitionModel } from "@app/shared";
import { DocumentSelectionWindowInputData, SelectedDocument } from "./../document-selection-window/document-selection-window.component";
import { DocumentWindowInputData } from "@app/client/common/document-window/document-window-input-data";

@Component({
	selector: "app-metadata-document-field",
	templateUrl: "./metadata-document-field.component.html",
	providers: [
		{ provide: NG_VALUE_ACCESSOR, useExisting: MetadataDocumentFieldComponent, multi: true }
	]
})
export class MetadataDocumentFieldComponent implements ControlValueAccessor, OnInit {

	private readonly EMPTY_TEXT: string = "";

	@Input()
	public metadataDefinition: MetadataDefinitionModel;

	@Input()
	public readonly: boolean;

	@Output()
	public valueChanged: EventEmitter<string | string[]>;

	private fieldValue: MetadataDocumentValue[];

	public placeholder: string;
	public documentUiValue: string;
	public documentUiValues: MultipleSelectionDocumentUiValue[];

	public clearEnabled: boolean;
	public viewDocumentEnabled: boolean;
	public selectEnabled: boolean;

	private documentService: DocumentService;
	private messageDisplayer: MessageDisplayer;
	private translateUtils: TranslateUtils;

	public documentSelectionWindowVisible: boolean;
	public documentSelectionWindowInputData: DocumentSelectionWindowInputData;
	public documentSelectionWindowSelectionMode: string;

	public viewDocumentWindowVisible: boolean;
	public viewDocumentWindowInputData: DocumentWindowInputData;
	public viewDocumentWindowMode: string;

	private cachedDocumentName: object;

	public selectedDocument: MultipleSelectionDocumentUiValue;

	private onChange: any = () => { };
	private onTouched: any = () => { };

	public constructor(documentService: DocumentService, messageDisplayer: MessageDisplayer, translateUtils: TranslateUtils) {
		this.documentService = documentService;
		this.messageDisplayer = messageDisplayer;
		this.translateUtils = translateUtils;
		
		this.selectEnabled = false;
		this.clearEnabled = false;
		
		this.placeholder = this.EMPTY_TEXT;
		this.documentUiValue = this.EMPTY_TEXT;
		this.documentUiValues = [];
		this.documentSelectionWindowVisible = false;

		this.cachedDocumentName = {};

		this.valueChanged = new EventEmitter();
	}

	public ngOnInit(): void {
		this.updatePerspective();
	}

	public get singleSelection(): boolean {
		return BooleanUtils.isFalse(this.metadataDefinition.multipleDocumentsSelection);
	}

	public get multipleSelection(): boolean {
		return BooleanUtils.isTrue(this.metadataDefinition.multipleDocumentsSelection);
	}

	public onSelect(): void {
		this.documentSelectionWindowSelectionMode = "single";
		if (this.multipleSelection) {
			this.documentSelectionWindowSelectionMode = "multiple";
		}
		this.documentSelectionWindowInputData = new DocumentSelectionWindowInputData();
		this.documentSelectionWindowInputData.documentTypeId = this.metadataDefinition.metadataDocumentTypeId;
		this.documentSelectionWindowVisible = true;
	}

	public onClear(): void {
		this.clearValue();
	}

	public onViewDocument(): void {

		this.viewDocumentWindowMode = "viewOrEdit";
		this.viewDocumentWindowInputData = new DocumentWindowInputData();

		if (this.singleSelection) {
			let value: MetadataDocumentValue = <MetadataDocumentValue> this.fieldValue[0];
			this.viewDocumentWindowInputData.documentId = value.documentId;
			this.viewDocumentWindowInputData.documentLocationRealName = value.documentLocatioRealName;
		} else if (this.multipleSelection) {
			this.viewDocumentWindowInputData.documentId = this.selectedDocument.documentId;
			this.viewDocumentWindowInputData.documentLocationRealName = this.selectedDocument.documentLocationRealName;
		}

		this.viewDocumentWindowVisible = true;
	}

	private clearValue(): void {
		if (this.singleSelection) {
			this.fieldValue = [];
		} else if (this.multipleSelection) {
			let mdvToRemove: MetadataDocumentValue = null;
			this.fieldValue.forEach((mdv: MetadataDocumentValue) => {
				if (mdv.documentLocatioRealName === this.selectedDocument.documentLocationRealName && mdv.documentId === this.selectedDocument.documentId) {
					mdvToRemove = mdv;
				}
			});
			ArrayUtils.removeElement(this.fieldValue, mdvToRemove);
			this.selectedDocument = null;
		}
		this.propagateValue();
		this.updatePerspective();
		this.loadDocumentUiValues();
	}

	public onDocumentSelectionWindowValuesSelected(selectedDocumentOrDocuments: SelectedDocument | SelectedDocument[]): void {

		if (ObjectUtils.isNullOrUndefined(selectedDocumentOrDocuments)) {
			return;
		}

		if (this.singleSelection) {
			let selectedDocument: SelectedDocument = <SelectedDocument> selectedDocumentOrDocuments;
			if (this.fieldValueContainsDocument(selectedDocument)) {
				return;
			}
			this.fieldValue = [];
			this.fieldValue.push(new MetadataDocumentValue(selectedDocument.documentLocationRealName, selectedDocument.documentId));
		} else if (this.multipleSelection) {
			let newFieldValue: MetadataDocumentValue[] = [...this.fieldValue];
			let selectedDocuments: SelectedDocument[] = <SelectedDocument[]> selectedDocumentOrDocuments;
			let areChanges: boolean = false;
			selectedDocuments.forEach((selectedDocument: SelectedDocument) => {
				if (!this.fieldValueContainsDocument(selectedDocument)) {
					newFieldValue.push(new MetadataDocumentValue(selectedDocument.documentLocationRealName, selectedDocument.documentId));
					areChanges = true;
				}
			});
			if (!areChanges) {
				return;
			}
			this.fieldValue = newFieldValue;
		} else {
			throw new Error("wrong selection");
		}		
		this.propagateValue();
		this.updatePerspective();
		this.loadDocumentUiValues();
	}

	private fieldValueContainsDocument(doc: SelectedDocument): boolean {
		if (ArrayUtils.isEmpty(this.fieldValue)) {
			return false;
		}
		let found: boolean = false;
		this.fieldValue.forEach((mdv: MetadataDocumentValue) => {
			if (mdv.documentLocatioRealName === doc.documentLocationRealName && mdv.documentId === doc.documentId) {
				found = true;
			}
		});
		return found;
	}

	private loadDocumentUiValues(): void {

		this.documentUiValue = this.EMPTY_TEXT;
		this.placeholder = this.EMPTY_TEXT;
		this.documentUiValues = [];

		if (ArrayUtils.isEmpty(this.fieldValue)) {			
			return;
		}

		this.resolveDocumentNames(() => {
			if (this.singleSelection) {				
				if (this.fieldValue.length === 1) {
					let value: MetadataDocumentValue = this.fieldValue[0];
					let cachedKey: string = this.buildCachedKeyForDocumentName(value);
					this.documentUiValue = this.cachedDocumentName[cachedKey];
				}
			} else if (this.multipleSelection) {
				this.fieldValue.forEach((value: MetadataDocumentValue) => {
					let documentUiValue: MultipleSelectionDocumentUiValue = new MultipleSelectionDocumentUiValue();
					documentUiValue.documentId = value.documentId;
					documentUiValue.documentLocationRealName = value.documentLocatioRealName;
					let cachedKey: string = this.buildCachedKeyForDocumentName(value);
					documentUiValue.documentName = this.cachedDocumentName[cachedKey];
					this.documentUiValues.push(documentUiValue);
				});
				this.sortDocumentUiValues();
			}
		});
	}

	private sortDocumentUiValues(): void {
		if (ArrayUtils.isEmpty(this.documentUiValues)) {
			return;
		}
		this.documentUiValues.sort((md1: MultipleSelectionDocumentUiValue, md2: MultipleSelectionDocumentUiValue): number => {
			if (md1.documentName < md2.documentName) {
				return -1;
			}
			if (md1.documentName > md2.documentName) {
				return 1;
			}
			return 0;
		});
	}

	private resolveDocumentNames(callback: () => void): void {

		let metadataDocumentValues: MetadataDocumentValue[] = [];

		this.fieldValue.forEach((metadataDocumentValue: MetadataDocumentValue) => {
			let cachedKey: string = metadataDocumentValue.documentLocatioRealName + ":" + metadataDocumentValue.documentId;
			let documentName: string = this.cachedDocumentName[cachedKey];
			if (StringUtils.isBlank(documentName)) {
				metadataDocumentValues.push(metadataDocumentValue);
			}			
		});

		if (ArrayUtils.isEmpty(metadataDocumentValues)) {
			callback();
			return;
		}

		let counter: number = 0;
		metadataDocumentValues.forEach((metadataDocumentValue: MetadataDocumentValue) => {
			let cachedKey: string = this.buildCachedKeyForDocumentName(metadataDocumentValue);
			this.documentService.getDocumentName(metadataDocumentValue.documentLocatioRealName, metadataDocumentValue.documentId, {
				onSuccess: (documentName: string): void => {
					counter++;
					this.cachedDocumentName[cachedKey] = documentName;
					if (counter === metadataDocumentValues.length) {
						callback();
					}
				},
				onFailure: (error: AppError): void => {
					// ?
				}
			});		
		});
	}

	private buildCachedKeyForDocumentName(metadataDocumentValue: MetadataDocumentValue): string {
		return metadataDocumentValue.documentLocatioRealName + ":" + metadataDocumentValue.documentId;
	}

	public onDocumentSelectionWindowWindowClosed(event: any): void {
		this.documentSelectionWindowVisible = false;
		this.onTouched();
	}

	public onViewDocumentWindowClosed(event: any): void {
		this.viewDocumentWindowVisible = false;
	}

	public onDocumentSelect(): void {
		this.updatePerspective();
	}

	public onDocumentUnselect(): void {
		this.updatePerspective();
	}

	private propagateValue(): void {
		if (ArrayUtils.isEmpty(this.fieldValue)) {
			this.onChange(null);
			this.valueChanged.emit(null);					
		} else {
			let valuesAsString = [];
			this.fieldValue.forEach((metadataDocumentValue: MetadataDocumentValue) => {
				valuesAsString.push(metadataDocumentValue.toStringFormat());
			});
			this.onChange(valuesAsString);	
			this.valueChanged.emit(valuesAsString);		
		}		
		this.onTouched();
	}

	private updatePerspective(): void {

		this.clearEnabled = false;
		this.selectEnabled = false;
		this.viewDocumentEnabled = false;
		
		if (this.singleSelection) {
			this.viewDocumentEnabled = ArrayUtils.isNotEmpty(this.fieldValue);
			if (BooleanUtils.isFalse(this.readonly)) {
				this.clearEnabled = ArrayUtils.isNotEmpty(this.fieldValue);
				this.selectEnabled = ObjectUtils.isNotNullOrUndefined(this.metadataDefinition.metadataDocumentTypeId);
			}
		} else if (this.multipleSelection) {
			this.viewDocumentEnabled = ObjectUtils.isNotNullOrUndefined(this.selectedDocument);
			if (BooleanUtils.isFalse(this.readonly)) {
				this.clearEnabled = ObjectUtils.isNotNullOrUndefined(this.selectedDocument);
				this.selectEnabled = ObjectUtils.isNotNullOrUndefined(this.metadataDefinition.metadataDocumentTypeId);
			}
		}
	}

	public writeValue(valueOrValues: string[]): void {
		this.fieldValue = [];
		if (ObjectUtils.isNotNullOrUndefined(valueOrValues)) {
			if (ObjectUtils.isArray(valueOrValues)) {
				valueOrValues.forEach((valueAsString: string) => {
					this.fieldValue.push(MetadataDocumentValue.getFromStringFormat(valueAsString));
				});
			} else {
				throw new Error("object is not valid");
			}
		}
		this.updatePerspective();
		this.loadDocumentUiValues();
	}

	public registerOnChange(fn: any): void {
		this.onChange = fn;
	}

	public registerOnTouched(fn: any): void {
		this.onTouched = fn;
	}
}

export class MetadataDocumentValue {

	private _documentLocatioRealName: string;
	private _documentId: string;

	public constructor(documentLocatioRealName: string, documentId: string) {
		if (ObjectUtils.isNullOrUndefined(documentLocatioRealName)) {
			throw new Error("documentLocatioRealName cannot be null/undefined");
		}
		if (ObjectUtils.isNullOrUndefined(documentId)) {
			throw new Error("documentId cannot be null/undefined");
		}
		this._documentLocatioRealName = documentLocatioRealName;
		this._documentId = documentId;
	}

	public get documentLocatioRealName(): string {
		return this._documentLocatioRealName;
	}

	public get documentId(): string {
		return this._documentId;
	}

	public static getFromStringFormat(valueAsString: string): MetadataDocumentValue {
		if (StringUtils.isNotBlank(valueAsString)) {
			let documentLocationAndId: string[] = valueAsString.split(":");
			return new MetadataDocumentValue(documentLocationAndId[0], documentLocationAndId[1]); 
		}
		return null;
	}

	public toStringFormat(): string {
		return this._documentLocatioRealName + ":" + this._documentId;
	}
}

class MultipleSelectionDocumentUiValue {

	public documentLocationRealName: string;
	public documentId: string;

	public documentName: string;
}