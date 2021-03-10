import { Component, OnInit} from "@angular/core";
import { AppError, MessageDisplayer, IconConstants, DocumentHistoryViewModel, Message, DocumentWorkflowHistoryService, DateConstants } from "@app/shared";
import { DocumentTabContent } from "./../document-tab-content";
import { DocumentModel } from "@app/shared";
import { DocumentAddInfo } from "./../document-add-info";
import { DocumentViewOrEditInfo } from "./../document-view-or-edit-info";
import { Column } from "primeng/primeng";
import { DatePipe } from "@angular/common";

@Component({
	selector: "app-document-history-tab-content",
	templateUrl: "./document-history-tab-content.component.html"
})
export class DocumentHistoryTabContentComponent extends DocumentTabContent {

	private documentWorkflowHistoryService: DocumentWorkflowHistoryService;
	private messageDisplayer: MessageDisplayer;
	private datePipe: DatePipe;

	public columns: Column[];
	public documents: DocumentHistoryViewModel[];

	public scrollHeight: string;

	public constructor(documentWorkflowHistoryService: DocumentWorkflowHistoryService, messageDisplayer: MessageDisplayer, datePipe: DatePipe) {
		super();
		this.documentWorkflowHistoryService = documentWorkflowHistoryService;
		this.messageDisplayer = messageDisplayer;
		this.datePipe = datePipe;

		this.scrollHeight = (window.innerHeight - 200) + "px";
	}

	protected doWhenOnInit(): void {
		this.documents = [];
		this.prepareColumns();
	}

	protected prepareForAdd(): void {
		// Nothing here.
	}
	
	protected prepareForViewOrEdit(): void {
		let viewOrEditInfo: DocumentViewOrEditInfo = <DocumentViewOrEditInfo> this.inputData;
		this.documentWorkflowHistoryService.getDocumentHistory(viewOrEditInfo.document.id, {
			onSuccess: (documents: DocumentHistoryViewModel[]): void => {
				this.documents = documents;
				this.populateFieldWorkflowTransitionDateAsString();
			},
			onFailure: (appError: AppError): void => {
				this.messageDisplayer.displayAppError(appError);
			}
		});
	}
	
	private populateFieldWorkflowTransitionDateAsString(): void {
		this.documents.forEach(document => {
			document.workflowTransitionDateAsString = this.datePipe.transform(
				document.workflowTransitionDate,
				DateConstants.DATE_TIME_FORMAT
			);
		});
	}

	private prepareColumns(): void {
		this.columns = [];
		this.columns.push(
			this.buildColumn("LABELS.HISTORY_AUTHOR", "workflowActor"),
			this.buildColumn("LABELS.HISTORY_DEPARTMENT", "organizationDepartment"),
			this.buildColumn("LABELS.HISTORY_ACTION", "workflowTransitionName"),
			this.buildColumn("LABELS.HISTORY_DATE", "workflowTransitionDateAsString")
		);
	}

	private buildColumn(header: string, field: string): Column {
		let column: Column = new Column();
		column.header = header;
		column.field = field;
		return column;
	}

	public isValid(): boolean {
		return true;
	}

	public populateDocument(document: DocumentModel): void {
		// Nu se implementeaza.
	}

	public getMessages(): Message[] {
		return [];
	}
}