import { Component, OnInit, Input, Output, EventEmitter, ElementRef, HostListener, ViewChild } from "@angular/core";
import { DatePipe } from "@angular/common";
import { Column, DataTable } from "primeng/primeng";
import { SortEvent } from "primeng/components/common/sortevent";
import {
	DocumentService,
	GetPagedDocumentsFromFolderRequestModel,
	DocumentViewModel,
	AppError,
	ArrayUtils,
	DateConstants,
	FolderTreeNode,
	DocumentLocationModel,
	FolderModel,
	MessageDisplayer,
	DocumentMetadataViewModel,
	TranslateUtils,
	PageConstants,
	PageRequest,
	Page,
	ObjectUtils,
	DocumentModel,
	DocumentTypeService,
	DocumentTypeModel,
	MetadataDefinitionModel,
	AttributeTypeEnum,
	ValueOfNomenclatorValueField,
	DateUtils,
	ProjectService,
	ProjectModel,
	WorkflowConstants,
	DocumentConstants,
	BooleanUtils,
	StringUtils
} from "@app/shared";
import { DocumentFilterModel } from "@app/shared/model/content/document-filter.model";
import { MetadataFilterModel } from "@app/shared/model/content/metadata-filter.model";

@Component({
	selector: "app-document-grid",
	templateUrl: "./document-grid.component.html",
	styleUrls: ["./document-grid.component.css"]
})
export class DocumentGridComponent {

	private static readonly COLUMN_DOCUMENT_ID: string = "id";

	private static readonly COLUMN_DOCUMENT_NAME: string = "documentName";
	private static readonly COLUMN_AUTHOR_NAME: string = "documentAuthorName";
	private static readonly COLUMN_CREATED_DATE: string = "documentCreatedDate";
	private static readonly COLUMN_DOCUMENT_LOCKED: string = "documentBlocked";
	private static readonly COLUMN_DOCUMENT_LOCKED_BY_NAME: string = "documentBlockedByName";
	private static readonly COLUMN_DOCUMENT_STATUS: string = "documentStatus";

	private static readonly COLUMN_START_POZITION_FOR_METADATA_IN_DATATABLE: number = 2;
	private static readonly INITIAL_PAGE_OFFSET: number = 0;

	private static readonly DOCUMENT_STATUS_IMPORTED: string = "IMPORTED";
	private static readonly DOCUMENT_STATUS_FINISHED: string = "FINNISHED";

	@Output()
	private selectionChanged: EventEmitter<string | void>;

	@Output()
	private loadStarted: EventEmitter<void>;

	@Output()
	private loadEnded: EventEmitter<void>;

	@ViewChild('documentDataTable') 
	public documentDataTable: DataTable;

	private documentService: DocumentService;
	private documentTypeService: DocumentTypeService;
	private projectService: ProjectService;
	private translateUtils: TranslateUtils;
	private messageDisplayer: MessageDisplayer;
	private datePipe: DatePipe;
	private elementRef: ElementRef;

	private _folder: FolderModel;
	public selectedDocument: any;
	public rowIndexOfFirstElementOfCurrentPage: number;
	
	public dateFormat: String;
	public yearRange: String;

	public documentsPage: Page<any>;
	public columns: Column[];
	public documents: DocumentViewModel[];
	public nomenclatorValueByColumnName: any = {};
	public listItemsByColumnName: any = {};
	private metadataTypeById: any = {};
	private projectSelectItems = [];
	private statusSelectItems: any[] = [];
	private booleanSelectItems: any[] = [];

	private widthRatio: number;
	public gridStyle: {};

	public documentFilterData: any[] = [];

	private filter: DocumentFilterModel ;

	private filterHashCode: any;

	public scrollHeight: string;
	
	public constructor(documentService: DocumentService, documentTypeService: DocumentTypeService, projectService: ProjectService, translateUtils: TranslateUtils, messageDisplayer: MessageDisplayer, datePipe: DatePipe, elementRef: ElementRef) {
		this.documentService = documentService;
		this.documentTypeService = documentTypeService;
		this.projectService = projectService;
		this.translateUtils = translateUtils;
		this.messageDisplayer = messageDisplayer;
		this.datePipe = datePipe;
		this.elementRef = elementRef;
		this.init();
	}

	private adjustGridWidth(): void {
		this.calculateWidthRatio();
		let gridWidthAsStyleObject = { width: this.getGridClientWidth() + "px" };
		this.setGridStyle(gridWidthAsStyleObject);
	}

	private calculateWidthRatio(): void {
		this.widthRatio = window.innerWidth / this.getGridClientWidth();
	}

	private getGridClientWidth(): number {
		return this.elementRef.nativeElement.querySelector("p-table > div").clientWidth;
	}

	private init(): void {
		this.scrollHeight = (window.innerHeight - 300) + "px";
		this.documentsPage = new Page<any>();
		this.dateFormat = DateConstants.DATE_FORMAT_FOR_TYPING;
		this.yearRange = DateUtils.getDefaultYearRange();
		this.prepareStandardFiltersSelectItems();
		this.selectionChanged = new EventEmitter<string | void>();
		this.loadStarted = new EventEmitter<void>();
		this.loadEnded = new EventEmitter<void>();
		this.reset();
		this.loadDocuments(DocumentGridComponent.INITIAL_PAGE_OFFSET);
	}

	@HostListener("window:resize", ["$event"])
	private onWindowResize(event) {
		let gridWidthAsStyleObject = { width: this.getCalculatedWidthOfGrid(event.target.innerWidth) + "px" };
		this.setGridStyle(gridWidthAsStyleObject);
	}

	private getCalculatedWidthOfGrid(width: number): number {
		return width / this.widthRatio;
	}

	private setGridStyle(style: {}): void {
		this.gridStyle = style;
	}

	private reset(): void {
		this.rowIndexOfFirstElementOfCurrentPage = DocumentGridComponent.INITIAL_PAGE_OFFSET;
		this.selectedDocument = {};
		this.columns = [];
		this.filter = new DocumentFilterModel();
		this.documentsPage = new Page<any>();
		this.addDefaultTableColumns();
	}

	public onLazyLoad(event: any): void {	
		let filterAsString: string = JSON.stringify(event.filters);
		if (this.filterHashCode != StringUtils.hashCode(filterAsString)) {
			this.rowIndexOfFirstElementOfCurrentPage = 0;
		} else {
			this.rowIndexOfFirstElementOfCurrentPage = event.first;
		}
		this.filterHashCode = StringUtils.hashCode(filterAsString);
		
		this.prepareFilters(event.filters);	
		this.loadDocuments(this.rowIndexOfFirstElementOfCurrentPage);
		this.selectedDocument = {};
		this.selectionChanged.emit();
	}

	private loadDocuments(offset: number): void {
		if (!this.isFolderSelected()) {
			return;
		}
		this.loadStarted.emit();

		this.documentService.getPagedDocuments(this.buildPageRequest(offset), {
			onSuccess: (page: Page<DocumentViewModel>) => {
				this.documents = page.items;
				this.buildDocumentsPage(page);

				this.adjustGridWidth();
				this.loadEnded.emit();
			},
			onFailure: (error: AppError) => {
				this.messageDisplayer.displayAppError(error);
				this.loadEnded.emit();
			}
		});
	}

	private buildColumnsFromDocumentType(documentTypeModel: DocumentTypeModel): any {
		let columnPosition: number = DocumentGridComponent.COLUMN_START_POZITION_FOR_METADATA_IN_DATATABLE;
		documentTypeModel.metadataDefinitions.forEach(metadata => {
			if (!this.metadataColumnExists(metadata) && this.metadataHasToBeDisplayed(metadata)) {
				if (metadata.type === MetadataDefinitionModel.TYPE_NOMENCLATOR) {
					this.nomenclatorValueByColumnName[metadata.name] = new ValueOfNomenclatorValueField(metadata.nomenclatorId);
				}
				if (metadata.type === MetadataDefinitionModel.TYPE_LIST) {
					this.listItemsByColumnName[metadata.name] = metadata.listItems;
				}
				if (metadata.type === MetadataDefinitionModel.TYPE_PROJECT) {
					this.prepareProjectSelectItems();
				}

				this.metadataTypeById[metadata.id] = metadata.type;
				this.columns.splice(columnPosition, 0, this.buildColumn(metadata.name, metadata.label, metadata.type, "" + metadata.id));
				columnPosition++;
			}
		});
	}
	private prepareProjectSelectItems(): void {
		this.projectSelectItems = [];
		this.projectService.getUserProjects({
			onSuccess: (projects: ProjectModel[]): void => {
				projects.forEach((project: ProjectModel) => {
					this.projectSelectItems.push({label: project.name, value: project.id});
				});
			},
			onFailure: (appError: AppError): void => {
				this.messageDisplayer.displayAppError(appError);
			}
		});
	}


	private metadataHasToBeDisplayed(metadata: MetadataDefinitionModel): boolean {
		return (metadata.representative);
	}

	private buildColumn(field: string, header: string, filterType: string, filterKey: string): Column {
		let column: Column = new Column();
		column.field = field;
		column.header = header;
		column.filterType = filterType;
		column.filterField = filterKey;
		return column;
	}

	private prepareStandardFiltersSelectItems(): void {
		this.booleanSelectItems = [
			{ label: this.translateUtils.translateLabel("YES"), value: true },
			{ label: this.translateUtils.translateLabel("NO"), value: false }
		];
		this.statusSelectItems = [
			{ label: this.translateUtils.translateLabel("DOC_STATUS_RUNNING"), value: WorkflowConstants.STATUS_RUNNING },
			{ label: this.translateUtils.translateLabel("DOC_STATUS_FINNISHED"), value: WorkflowConstants.STATUS_FINISED },
			{ label: this.translateUtils.translateLabel("DOC_STATUS_IMPORTED"), value: WorkflowConstants.STATUS_IMPORTED }
		];
	}

	private prepareFilters(filters: {}): void {

		this.filter = new DocumentFilterModel();

		if (ObjectUtils.isNullOrUndefined(filters)) {
			return;
		}

		Object.entries(filters).forEach((filter: {}) => {
			if (filter[0] === DocumentGridComponent.COLUMN_DOCUMENT_NAME) {
				this.filter.nameFilterValue = "" + filter[1].value;
			} else if (filter[0] === DocumentGridComponent.COLUMN_AUTHOR_NAME) {
				this.filter.authorFilterValue = "" + filter[1].value;
			} else if (filter[0] === DocumentGridComponent.COLUMN_CREATED_DATE) {
				this.filter.createdFilterValue = filter[1].value;
			} else if (filter[0] === DocumentGridComponent.COLUMN_DOCUMENT_LOCKED) {
				this.filter.lockedFilterValue = "" + filter[1].value;
			} else if (filter[0] === DocumentGridComponent.COLUMN_DOCUMENT_STATUS) {
				this.filter.statusFilterValue = "" + filter[1].value;
			} else {
				let filterKey: number = Number.parseInt(filter[0]);
				let filterValue: string = this.formatFilterValue(filter);
				this.filter.metadataFilters.push(this.buildMetadataFilter(filterKey, filterValue));
			}
		});
	}

	private formatFilterValue(filter: {}): string {
		let filterValue = filter[1].value;
		if (this.metadataTypeById[filter[0]] === MetadataDefinitionModel.TYPE_DATE) {
			filterValue = DateUtils.formatForStorage(filterValue);
		}
		if (this.metadataTypeById[filter[0]] === MetadataDefinitionModel.TYPE_DATE_TIME) {
			filterValue = DateUtils.formatForStorage(filterValue);
		}

		return "" + filterValue;
	}


	private  buildMetadataFilter(filterKey: number, filterValue: any): MetadataFilterModel {
		let metadataFilter: MetadataFilterModel = new MetadataFilterModel();
		metadataFilter.metadataId = filterKey;
		metadataFilter.value = filterValue;
		return metadataFilter;
	}
	private isFolderSelected(): boolean {
		return ObjectUtils.isNotNullOrUndefined(this._folder);
	}

	private metadataColumnExists(metadata: MetadataDefinitionModel): boolean {
		let columnExists = false;
		this.columns.forEach(column => {
			if (column.field === metadata.name) {
				columnExists = true;
			}
		});
		return columnExists;
	}

	private buildPageRequest(offset: number): DocumentFilterModel {
		this.filter.documentLocationRealName = this._folder.documentLocationRealName;
		this.filter.folderId = this._folder.id;
		this.filter.documentTypeId = this._folder.documentTypeId;
		this.filter.sameType = this._folder.hasDocumentType();
		this.filter.pageSize = PageConstants.DEFAULT_PAGE_SIZE;
		this.filter.offset = offset;
		return this.filter;
	}

	private addDefaultTableColumns(): void {
		this.columns.push(
			this.buildColumn(DocumentGridComponent.COLUMN_DOCUMENT_NAME, "LABELS.DOC_NAME", "TEXT", DocumentGridComponent.COLUMN_DOCUMENT_NAME)
		);
		this.columns.push(
			this.buildColumn(DocumentGridComponent.COLUMN_AUTHOR_NAME, "LABELS.DOC_AUTHOR", "USER", DocumentGridComponent.COLUMN_AUTHOR_NAME)
		);
		this.columns.push(
			this.buildColumn(DocumentGridComponent.COLUMN_CREATED_DATE, "LABELS.DOC_CREATED", "DATE", DocumentGridComponent.COLUMN_CREATED_DATE)
		);
		this.columns.push(
			this.buildColumn(DocumentGridComponent.COLUMN_DOCUMENT_LOCKED, "LABELS.DOC_LOCK", "BOOLEAN", DocumentGridComponent.COLUMN_DOCUMENT_LOCKED)
		);
		this.columns.push(
			this.buildColumn(DocumentGridComponent.COLUMN_DOCUMENT_STATUS, "LABELS.DOC_STATUS", "STATUS", DocumentGridComponent.COLUMN_DOCUMENT_STATUS)
		);
	}

	private buildDocumentsPage(page: Page<DocumentViewModel>): void {
		this.documentsPage = new Page<any>();
		let documents: any = [];

		page.items.forEach(document => {
			documents.push(this.buildDocument(document));
		});

		this.documentsPage.items = documents;
		this.documentsPage.pageSize = page.pageSize;
		this.documentsPage.totalItems = page.totalItems;
	}

	private buildDocument(documentModel: DocumentViewModel): any {
		let document = {};
		document[DocumentGridComponent.COLUMN_DOCUMENT_ID] = documentModel.documentId;
		document[DocumentGridComponent.COLUMN_DOCUMENT_NAME] = documentModel.documentName;
		document[DocumentGridComponent.COLUMN_AUTHOR_NAME] = documentModel.documentAuthorName;
		document[DocumentGridComponent.COLUMN_DOCUMENT_LOCKED] = documentModel.locked;
		document[DocumentGridComponent.COLUMN_DOCUMENT_LOCKED_BY_NAME] = documentModel.documentLockedByName;
		document[DocumentGridComponent.COLUMN_DOCUMENT_STATUS] = documentModel.documentStatus;

		let formattedDocumentCreatedDate: string = this.datePipe.transform(
			documentModel.documentCreatedDate,
			DateConstants.DATE_FORMAT
		);
		document[DocumentGridComponent.COLUMN_CREATED_DATE] = formattedDocumentCreatedDate;

		if (ArrayUtils.isNotEmpty(documentModel.documentMetadata)) {
			documentModel.documentMetadata.forEach(metadata => {
				document[metadata.name] = metadata.value;
			});
		}
		
		return document;
	}

	public onDocumentSelect(event): void {
		this.selectionChanged.emit(event.data[DocumentGridComponent.COLUMN_DOCUMENT_ID]);
	}

	public onDocumentUnselect(event): void {
		this.selectionChanged.emit();
	}

	public setFolderAndResetDocumentGrid(folder: FolderModel) {
		this._folder = folder;
		this.reset();
		if (ObjectUtils.isNotNullOrUndefined(this._folder) && ObjectUtils.isNotNullOrUndefined(folder.documentTypeId)) {
			this.loadDocumentType();
		}
		this.documentDataTable.reset();
		this.loadDocuments(DocumentGridComponent.INITIAL_PAGE_OFFSET);
	}

	private loadDocumentType(): void {
		let documentTypeId = this._folder.documentTypeId;
		if (ObjectUtils.isNotNullOrUndefined(documentTypeId)) {
			this.documentTypeService.getDocumentTypeById(documentTypeId, {
				onSuccess: (documentTypeModel: DocumentTypeModel) => {
					this.buildColumnsFromDocumentType(documentTypeModel);
					this.loadEnded.emit();
				},
				onFailure: (error: AppError) => {
					this.messageDisplayer.displayAppError(error);
					this.loadEnded.emit();
				}
			});
		}
	}

	public refresh(): void {
		this.loadDocuments(this.rowIndexOfFirstElementOfCurrentPage);
	}

	public onDocumentDeleted(): void {
		this.refresh();
	}

	public isDocumentLockColumn(column: Column): boolean {
		return column.field === DocumentGridComponent.COLUMN_DOCUMENT_LOCKED;
	}

	public isDocumentStatusColumn(column: Column): boolean {
		return column.field === DocumentGridComponent.COLUMN_DOCUMENT_STATUS;
	}

	public getMessageForLockedDocument(document: any): string {
		let translatedMessage = this.translateUtils.translateMessage("LOCKED_BY_USER");
		translatedMessage += document[DocumentGridComponent.COLUMN_DOCUMENT_LOCKED_BY_NAME];	
		return translatedMessage;
	}

	public getMessageForUnlockedDocument(document: any): string {
		let translatedMessage: string = "";
		let status: string = document[DocumentGridComponent.COLUMN_DOCUMENT_STATUS];
		if (status === DocumentGridComponent.DOCUMENT_STATUS_IMPORTED || status === DocumentGridComponent.DOCUMENT_STATUS_FINISHED) {
			translatedMessage = this.translateUtils.translateLabel("DOC_LOCK_TOOLTIP_ON");
		} else {
			translatedMessage = this.translateUtils.translateLabel("DOC_LOCK_TOOLTIP_OFF");
		}		
		return translatedMessage;
	}

	public getMessageForStatusDocument(document: any): string {
		return this.translateUtils.translateLabel("DOC_STATUS_" + document[DocumentGridComponent.COLUMN_DOCUMENT_STATUS]);
	}
}