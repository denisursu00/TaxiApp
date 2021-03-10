import { Component } from "@angular/core";
import { Page, AppError, MyActivitiesModel, MessageDisplayer, DocumentSearchService, TranslateUtils } from "@app/shared";
import { DocumentWindowInputData } from "@app/client/common/document-window";
import { Column } from "primeng/primeng";

@Component({
	selector: "app-my-document-activities",
	templateUrl: "./my-document-activities.component.html"
})
export class MyDocumentActivitiesComponent {

	private static readonly COLUMN_DOCUMENT_NAME: string = "documentName";
	private static readonly COLUMN_WORKFLOW_SENDER: string = "workflowSender";
	private static readonly COLUMN_WORKFLOW_CURRENT_STATUS: string = "workflowCurrentStatus";
	private static readonly COLUMN_WORKFLOW_NAME: string = "workflowName";
	private static readonly COLUMN_DOCUMENT_TYPE_NAME: string = "documentTypeName";
	private static readonly COLUMN_DOCUMENT_CREATED_DATE: string = "documentCreatedDate";

	private documentSearchService: DocumentSearchService;
	private translateUtils: TranslateUtils;
	private messageDisplayer: MessageDisplayer;

	public myActivitiesPage: Page<MyActivitiesModel>;
	public selectedActivity: MyActivitiesModel;
	public columns: Column[];
	public viewActionEnabled: boolean;

	public documentWindowVisible: boolean;
	public documentWindowInputData: DocumentWindowInputData;
	public documentWindowMode: string;
	public isLoading: boolean;

	public scrollHeight: string;

	public constructor(documentSearchService: DocumentSearchService, translateUtils: TranslateUtils, messageDisplayer: MessageDisplayer) {
		this.documentSearchService = documentSearchService;
		this.translateUtils = translateUtils;
		this.messageDisplayer = messageDisplayer;
		this.init();
	}

	private init(): void {
		this.viewActionEnabled = false;
		this.myActivitiesPage = new Page<MyActivitiesModel>();
		this.populateMyActivities();
		this.prepareColumns();

		this.scrollHeight = (window.innerHeight - 300) + "px";
	}

	private prepareColumns(): void {
		this.columns = [];
		this.columns.push(this.createColumn(MyDocumentActivitiesComponent.COLUMN_DOCUMENT_NAME, "LABELS.DOCUMENT_NAME"));
		this.columns.push(this.createColumn(MyDocumentActivitiesComponent.COLUMN_WORKFLOW_SENDER, "LABELS.WORKFLOW_SENDER"));
		this.columns.push(this.createColumn(MyDocumentActivitiesComponent.COLUMN_WORKFLOW_CURRENT_STATUS, "LABELS.WORKFLOW_CURRENT_STATUS"));
		this.columns.push(this.createColumn(MyDocumentActivitiesComponent.COLUMN_WORKFLOW_NAME, "LABELS.WORKFLOW"));
		this.columns.push(this.createColumn(MyDocumentActivitiesComponent.COLUMN_DOCUMENT_TYPE_NAME, "LABELS.DOCUMENT_TYPE_NAME"));
		this.columns.push(this.createColumn(MyDocumentActivitiesComponent.COLUMN_DOCUMENT_CREATED_DATE, "LABELS.DOC_CREATED"));
	}
	
	private createColumn(field: string, header: string): Column {
		let column: Column = new Column();
		column.field = field;
		column.header = header;
		return column;
	}

	private populateMyActivities(): void {
		this.isLoading = true;
		this.documentSearchService.getMyActivities({
			onSuccess: (myActivities: MyActivitiesModel[]) => {
				this.myActivitiesPage.items = myActivities;
				this.myActivitiesPage.totalItems = myActivities.length;
				this.isLoading = false;
			},
			onFailure: (appError: AppError) => {
				this.messageDisplayer.displayAppError(appError);
				this.isLoading = false;
			}
		});
	}

	private reloadMyActivities(): void {
		this.populateMyActivities();
	}

	private onViewMyActivity(): void {
		this.documentWindowMode = "viewOrEdit";		
		this.documentWindowInputData = new DocumentWindowInputData();
		this.documentWindowInputData.documentId = this.selectedActivity.documentId;
		this.documentWindowInputData.documentLocationRealName = this.selectedActivity.documentLocationRealName;
		this.documentWindowVisible = true;
	}

	public onDocumentWindowClosed(event: any): void {
		this.reloadMyActivities();
		this.documentWindowVisible = false;
	}

	public onDocumentSelect(event: any): void {
		this.viewActionEnabled = true;
	}

	public onDocumentUnselect(event: any): void {
		this.viewActionEnabled = false;
	}
}