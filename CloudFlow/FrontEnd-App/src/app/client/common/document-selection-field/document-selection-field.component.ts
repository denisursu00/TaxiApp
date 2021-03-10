import { Component, EventEmitter, Input, OnInit, Output } from "@angular/core";
import { ControlValueAccessor, NG_VALUE_ACCESSOR } from "@angular/forms";
import { DocumentService, BooleanUtils } from "./../../../shared";
import { MessageDisplayer } from "./../../../shared";
import { TranslateUtils, ArrayUtils, ObjectUtils, StringUtils } from "./../../../shared";
import { AppError } from "./../../../shared";
import { DocumentSelectionWindowInputData, SelectedDocument } from "./../document-selection-window";
import { DocumentWindowInputData } from "../document-window";

@Component({
	selector: "app-document-selection-field",
	templateUrl: "./document-selection-field.component.html",
	providers: [{ 
		provide: NG_VALUE_ACCESSOR, 
		useExisting: DocumenSelectiontFieldComponent, 
		multi: true }]
})
export class DocumenSelectiontFieldComponent implements ControlValueAccessor, OnInit {

	private readonly EMPTY_TEXT: string = "";

	@Input()
	public selectionMode: "single" | "multiple";
	
	@Input()
	public readonly: boolean;
	
	@Input()
	public documentTypeId: number;

	@Output()
	public valueChanged: EventEmitter<string | string[]>;

	private fieldValue: ValueOfDocumentSelectionField[] = [];

	public placeholder: string;
	public documentUiValue: string;
	public documentUiValues: DocumentIdentifier[];

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

	public selectedDocument: DocumentIdentifier;

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

	private isSingleMode(): boolean {
		return this.selectionMode === "single";
	}
	private isMultipleMode(): boolean {
		return this.selectionMode === "multiple";
	}

	public get singleSelection(): boolean {
		return this.isSingleMode();
	}

	public get multipleSelection(): boolean {
		return this.isMultipleMode();
	}

	public onSelect(): void {

		this.documentSelectionWindowInputData = new DocumentSelectionWindowInputData();
		this.documentSelectionWindowInputData.documentTypeId = this.documentTypeId;
		this.documentSelectionWindowSelectionMode = this.selectionMode;
		this.documentSelectionWindowVisible = true;
	}

	public onClear(): void {
		this.clearValue();
	}

	public onViewDocument(): void {

		this.viewDocumentWindowMode = "viewOrEdit";
		this.viewDocumentWindowInputData = new DocumentWindowInputData();

		if (this.singleSelection) {
			let value: ValueOfDocumentSelectionField = <ValueOfDocumentSelectionField> this.fieldValue[0];
			this.viewDocumentWindowInputData.documentId = value.documentTypeId;
			this.viewDocumentWindowInputData.documentLocationRealName = value.value.documentLocationRealName;
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
			let mdvToRemove: ValueOfDocumentSelectionField = null;
			this.fieldValue.forEach((mdv: ValueOfDocumentSelectionField) => {
				if (mdv.value.documentLocationRealName === this.selectedDocument.documentLocationRealName && mdv.value.documentId === this.selectedDocument.documentId) {
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
			let value = new DocumentIdentifier(selectedDocument.documentLocationRealName, selectedDocument.documentId)
			let values = [];
			values.push(value);

			this.fieldValue = [];
			this.fieldValue.push(new ValueOfDocumentSelectionField(selectedDocument.documentId, values));
		} else if (this.multipleSelection) {
			let newFieldValue: ValueOfDocumentSelectionField[] = [...this.fieldValue];
			let selectedDocuments: SelectedDocument[] = <SelectedDocument[]> selectedDocumentOrDocuments;
			let areChanges: boolean = false;
			selectedDocuments.forEach((selectedDocument: SelectedDocument) => {
				if (!this.fieldValueContainsDocument(selectedDocument)) {
					let value = new DocumentIdentifier(selectedDocument.documentLocationRealName, selectedDocument.documentId)
					let values = [];
					values.push(value);

					newFieldValue.push(new ValueOfDocumentSelectionField(selectedDocument.documentId, values));
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
		this.fieldValue.forEach((mdv: ValueOfDocumentSelectionField) => {
			if (mdv.value.documentLocationRealName === doc.documentLocationRealName && mdv.value.documentId === doc.documentId) {
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
					let value: ValueOfDocumentSelectionField = this.fieldValue[0];
					let cachedKey: string = this.buildCachedKeyForDocumentName(value);
					this.documentUiValue = this.cachedDocumentName[cachedKey];
				}
			} else if (this.multipleSelection) {
				this.fieldValue.forEach((value: ValueOfDocumentSelectionField) => {
					let documentUiValue: DocumentIdentifier = new DocumentIdentifier(value.value.documentLocationRealName, value.value.documentId);
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
		this.documentUiValues.sort((md1: DocumentIdentifier, md2: DocumentIdentifier): number => {
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

		let documentValues: ValueOfDocumentSelectionField[] = [];

		this.fieldValue.forEach((documentValue: ValueOfDocumentSelectionField) => {
			let cachedKey: string = documentValue.value.documentLocationRealName + ":" + documentValue.value.documentId;
			let documentName: string = this.cachedDocumentName[cachedKey];
			if (StringUtils.isBlank(documentName)) {
				documentValues.push(documentValue);
			}			
		});

		if (ArrayUtils.isEmpty(documentValues)) {
			callback();
			return;
		}

		let counter: number = 0;
		documentValues.forEach((documentValue: ValueOfDocumentSelectionField) => {
			let cachedKey: string = this.buildCachedKeyForDocumentName(documentValue);
			this.documentService.getDocumentName(documentValue.value.documentLocationRealName, documentValue.value.documentId, {
				onSuccess: (documentName: string): void => {
					counter++;
					this.cachedDocumentName[cachedKey] = documentName;
					if (counter === documentValues.length) {
						callback();
					}
				},
				onFailure: (error: AppError): void => {
					// ?
				}
			});		
		});
	}

	private buildCachedKeyForDocumentName(documentValue: ValueOfDocumentSelectionField): string {
		return documentValue.value.documentLocationRealName + ":" + documentValue.value.documentId;
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
			this.fieldValue.forEach((documentValue: ValueOfDocumentSelectionField) => {
				valuesAsString.push(documentValue);
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
				this.selectEnabled = true;
			}
		} else if (this.multipleSelection) {
			this.viewDocumentEnabled = ObjectUtils.isNotNullOrUndefined(this.selectedDocument);
			if (BooleanUtils.isFalse(this.readonly)) {
				this.clearEnabled = ObjectUtils.isNotNullOrUndefined(this.selectedDocument);
				this.selectEnabled = true;
			}
		}
	}

	public writeValue(valueOrValues: string[]): void {
		this.fieldValue = [];
		if (ObjectUtils.isNotNullOrUndefined(valueOrValues)) {
			if (ObjectUtils.isArray(valueOrValues)) {
				valueOrValues.forEach((valueAsString: string) => {
					//this.fieldValue.push(valueAsString);
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

export class ValueOfDocumentSelectionField {

	private _documentTypeId: string;
	private _values: DocumentIdentifier[];

	public constructor(documentTypeId: string, values: DocumentIdentifier[]) {
		if (ObjectUtils.isNullOrUndefined(documentTypeId)) {
			throw new Error("documentTypeId cannot be null/undefined");
		}
		this._documentTypeId = documentTypeId;

		if (ObjectUtils.isNullOrUndefined(values)) {
			throw new Error("documentTypeId cannot be null/undefined");
		}
		this._values = values;
	}

	public get documentTypeId(): string {
		return this._documentTypeId;
	}

	public get value(): DocumentIdentifier {
		if (ArrayUtils.isEmpty(this._values)) {
			return undefined;
		}
		if (this._values.length > 1) {
			throw new Error("A single value was asked for, but multiple values found: " + this._values);
		}
		return this._values[0];
	}

	public set value(value: DocumentIdentifier) {
		this._values = [value];
	}

	public get values(): DocumentIdentifier[] {
		return this._values;
	}

	public set values(values: DocumentIdentifier[]) {
		this._values = values;
	}
}

export class DocumentIdentifier {
	
	private _documentLocationRealName: string;
	private _documentId: string;
	public documentName: string;

	public constructor(documentLocatioRealName: string, documentId: string) {
		if (ObjectUtils.isNullOrUndefined(documentLocatioRealName)) {
			throw new Error("documentLocatioRealName cannot be null/undefined");
		}
		if (ObjectUtils.isNullOrUndefined(documentId)) {
			throw new Error("documentId cannot be null/undefined");
		}
		this._documentLocationRealName = documentLocatioRealName;
		this._documentId = documentId;
	}

	public get documentLocationRealName(): string {
		return this._documentLocationRealName;
	}

	public get documentId(): string {
		return this._documentId;
	}
}