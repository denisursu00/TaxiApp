import { Component, OnInit, EventEmitter, Output } from "@angular/core";
import { DocumentTabContent } from "./../document-tab-content";
import { DocumentAddInfo } from "./../document-add-info";
import { DocumentViewOrEditInfo } from "./../document-view-or-edit-info";
import { DocumentWindowInputData } from "./../../document-window/document-window-input-data";
import { DocumentModel, Message, DocumentVersionInfoViewModel, AppError, MessageDisplayer, IconConstants, DocumentService, DateConstants, TranslateUtils } from "@app/shared";
import { Column } from "primeng/primeng";
import { MenuItem } from "primeng/components/common/menuitem";
import { DatePipe } from "@angular/common";
import * as moment from "moment";

@Component({
	selector: "app-document-versions-tab-content",
	templateUrl: "./document-versions-tab-content.component.html"
})
export class DocumentVersionsTabContentComponent extends DocumentTabContent {

	@Output()
	private viewDocumentVersion: EventEmitter<string>;

	private documentService: DocumentService;
	private messageDisplayer: MessageDisplayer;
	private datePipe: DatePipe;
	private translateUtils: TranslateUtils;

	public columns: Column[];
	public documentVersions: DocumentVersionInfoViewModel[];
	
	public contextMenuItems: MenuItem[];
	public selectedDocument: DocumentVersionInfoViewModel;

	public scrollHeight: string;

	public constructor(documentService: DocumentService, messageDisplayer: MessageDisplayer, datePipe: DatePipe, translateUtils: TranslateUtils) {
		super();
		this.documentService = documentService;
		this.messageDisplayer = messageDisplayer;
		this.datePipe = datePipe;
		this.translateUtils = translateUtils;
		this.viewDocumentVersion = new EventEmitter();	

		this.scrollHeight = (window.innerHeight - 200) + "px";
	}

	protected doWhenOnInit(): void {
		this.documentVersions = [];
		this.prepareColumns();
		this.prepareContextMenuItems();
	}

	private prepareContextMenuItems(): void {
		this.contextMenuItems = [];
		let documentPreviewLabel = this.translateUtils.translateLabel("PREVIEW");
		this.contextMenuItems.push(
			{ label: documentPreviewLabel, command: (event) => this.viewDocument(this.selectedDocument) }
		);
	}

	private viewDocument(viewVersionDocument: DocumentVersionInfoViewModel): void {
		this.viewDocumentVersion.emit(viewVersionDocument.versionNumber);
	}
	
	protected prepareForAdd(): void {
		// Nu se prepara pentru adaugare deoarece acest tab trebuie sa fie 
		// vizibil doar in caz de editare document.
	}
	
	protected prepareForViewOrEdit(): void {
		const viewOrEditInfo: DocumentViewOrEditInfo = <DocumentViewOrEditInfo> this.inputData;
		this.documentService.getDocumentVersions(viewOrEditInfo.document.id, viewOrEditInfo.document.documentLocationRealName, {
			onSuccess: (documentVersions: DocumentVersionInfoViewModel[]): void => {
				this.documentVersions = documentVersions;
				this.populateFieldVersionCreationDateForView();
			},
			onFailure: (appError: AppError): void => {
				this.messageDisplayer.displayAppError(appError);
			}
		});
	}

	private populateFieldVersionCreationDateForView(): void {
		this.documentVersions.forEach(document => {
			document.versionCreationDateForView = this.datePipe.transform(
				moment(document.versionCreationDate, DateConstants.DATE_TIME_FORMAT).toDate(),
				DateConstants.DATE_TIME_FORMAT
			);
		});
	}
	private prepareColumns(): void {
		this.columns = [];
		this.columns.push(
			this.buildColumn("LABELS.VERSION_NR", "versionNumber"),
			this.buildColumn("LABELS.VERSION_AUTHOR", "versionAuthor"),
			this.buildColumn("LABELS.VERSION_DATE", "versionCreationDateForView")
		);
	}

	private buildColumn(header: string, field: string): Column {
		let column: Column = new Column();
		column.header = header;
		column.field = field;
		return column;
	}

	public isValid(): boolean {
		// Nu se valideaza nimic deoarece aici doar se afiseaza date
		return true;
	}

	public populateDocument(document: DocumentModel): void {
		// Nu se populeaza documentul deoarece in acest tab doar se afiseaza date
	}

	public getMessages(): Message[] {
		return [];
	}
}