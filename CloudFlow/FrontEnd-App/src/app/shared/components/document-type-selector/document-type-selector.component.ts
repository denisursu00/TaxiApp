import { Component, Output, Input, OnInit, EventEmitter } from "@angular/core";
import { DocumentTypeModel } from "../../model/content/document-type.model";
import { DocumentTypeService } from "../../service/document-type.service";
import { AppError } from "../../model/app-error";
import { MessageDisplayer } from "../../message-displayer";
import { TranslateUtils } from "../../utils/translate-utils";
import { SelectItem } from "primeng/components/common/selectitem";
import { ObjectUtils } from "../../utils/object-utils";
import { ControlValueAccessor, NG_VALUE_ACCESSOR } from "@angular/forms";

@Component({
	selector: "app-document-type-selector",
	templateUrl: "./document-type-selector.component.html",
	providers: [{
		provide: NG_VALUE_ACCESSOR,
		useExisting: DocumentTypeSelectorComponent,
		multi: true
	}]
})
export class DocumentTypeSelectorComponent implements OnInit, ControlValueAccessor {

	@Input()
	private documentTypeId: number;

	@Input()
	private isSelectToateDisabled: boolean;

	@Output()
	private selectionChanged: EventEmitter<DocumentTypeModel>;

	private documentTypeService: DocumentTypeService;
	private messageDisplayer: MessageDisplayer;
	private translateUtils: TranslateUtils;

	public documentTypeSelectItems: SelectItem[];
	public selectedDocumentType: DocumentTypeModel;

	private onChange: Function;
	private onTouche: Function;

	public constructor(documentTypeService: DocumentTypeService, messageDisplayer: MessageDisplayer, translateUtils: TranslateUtils) {
		this.documentTypeService = documentTypeService;
		this.messageDisplayer = messageDisplayer;
		this.translateUtils = translateUtils;
		this.init();
	}
	
	public ngOnInit(): void {
		this.documentTypeSelectItems = [];
		if ( ObjectUtils.isNullOrUndefined(this.isSelectToateDisabled) || !this.isSelectToateDisabled) {
			this.addDefaultSelectItem();
		}
		this.loadDocumentTypes();
		this.setSelectedDocumentType();
	}

	public init(): void {
		this.selectionChanged = new EventEmitter<DocumentTypeModel>();
	}
	
	private addDefaultSelectItem(): void {
		this.documentTypeSelectItems.push({
			label: this.translateUtils.translateLabel("ALL"),
			value: null
		});
	}

	private loadDocumentTypes(): void {
		this.documentTypeService.getAllDocumentTypesForDisplay({
			onSuccess: (documentTypes: DocumentTypeModel[]): void => {
				this.documentTypeSelectItems = [...this.documentTypeSelectItems, ...this.buildDocumentTypeSelectItems(documentTypes)];
				this.setSelectedDocumentType();
			},
			onFailure: (appError: AppError): void => {
				this.messageDisplayer.displayAppError(appError);
			}
		});
	}

	private setSelectedDocumentType(): void {
		if (ObjectUtils.isNullOrUndefined(this.documentTypeId)) {
			this.selectedDocumentType = null;
			return;
		}
		this.documentTypeSelectItems.forEach((item: SelectItem) => {
			if (ObjectUtils.isNotNullOrUndefined(item.value) && (<DocumentTypeModel>item.value).id === this.documentTypeId) {
				this.selectedDocumentType = item.value;
			}
		});
	}
	
	private buildDocumentTypeSelectItems(documentTypes: DocumentTypeModel[]): SelectItem[] {
		let documentTypeSelectItems: SelectItem[] = [];
		documentTypes.forEach(documentType => {
			documentTypeSelectItems.push(this.buildDocumentTypeSelectItem(documentType));
		});
		return documentTypeSelectItems;
	}

	private buildDocumentTypeSelectItem(documentType: DocumentTypeModel): SelectItem {
		let documentTypeSelectItem: SelectItem = {
			label: documentType.name,
			value: documentType
		};
		return documentTypeSelectItem;
	}

	public onValueChange(event): void {
		this.selectionChanged.emit(this.selectedDocumentType);
		if (ObjectUtils.isNotNullOrUndefined(event.value)) {
			this.documentTypeId = event.value.id;
		} else {
			this.documentTypeId = null;
		}
		this.propagateValue();
	}
	
	private propagateValue() {
		this.onChange(this.documentTypeId);
		this.onTouche();
	}

	public writeValue(documentTypeId: any): void {
		this.documentTypeId = documentTypeId;
		this.setSelectedDocumentType();
	}

	public registerOnChange(fn: any): void {
		this.onChange = fn;
	}
	
	public registerOnTouched(fn: any): void {
		this.onTouche = fn;
	}
}