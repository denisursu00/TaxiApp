import { Component } from "@angular/core";
import { Column, MenuItem } from "primeng/primeng";
import { DatePipe } from "@angular/common";
import {
	DocumentSimpleSearchResultsViewModel,
	DocumentAdvancedSearchResultsViewsWrapperModel,
	Page,
	TranslateUtils,
	DateConstants,
	DocumentAdvancedSearchResultsViewModel,
	DateUtils
} from "@app/shared";
import { DocumentWindowInputData } from "@app/client/common/document-window";

@Component({
	selector: "app-results-tab-content",
	templateUrl: "./results-tab-content.component.html",
	styleUrls: ["./results-tab-content.component.css"]
})
export class ResultsTabContentComponent {

	private static readonly COLUMN_DOCUMENT_ID: string = "id";
	private static readonly COLUMN_DOCUMENT_NAME: string = "documentName";
	private static readonly COLUMN_CREATED_DATE: string = "documentCreatedDate";
	private static readonly COLUMN_DOCUMENT_AUTHOR_DISPLAY_NAME: string = "documentAuthorDisplayName";
	private static readonly COLUMN_WORKFLOW_NAME: string = "workflowName";
	private static readonly COLUMN_WORKFLOW_CURRENT_STATE_NAME: string = "workflowCurrentStateName";
	private static readonly COLUMN_WORKFLOW_SENDER_DISPLAY_NAME: string = "workflowSenderDisplayName";
	private static readonly COLUMN_DOCUMENT_TYPE_NAME: string = "documentTypeName";

	private translateUtils: TranslateUtils;
	private datePipe: DatePipe;

	public columns: Column[];
	public documentsPage: Page<any>;
	public selectedDocument: any;
	public documentLocationRealName: string;

	public documentWindowInputData: DocumentWindowInputData;
	public documentWindowMode: string;
	public documentWindowVisible: boolean;

	public contextMenuItems: MenuItem[];

	public scrollHeight: string;

	public constructor(translateUtils: TranslateUtils, datePipe: DatePipe) {
		this.translateUtils = translateUtils;
		this.datePipe = datePipe;
		this.init();
	}

	private init(): void {
		this.resetResultsTable();
		this.documentLocationRealName = null;
		this.documentWindowVisible = false;
		this.prepareContextMenuItems();

		this.scrollHeight = (window.innerHeight - 300) + "px";
	}

	private resetResultsTable(): void {
		this.columns = [];
		this.documentsPage = new Page<any>();
		this.addDefaultResultsTableColumns();
	}

	private prepareContextMenuItems(): void {
		this.contextMenuItems = [];
		let documentPreviewLabel = this.translateUtils.translateLabel("PREVIEW");
		this.contextMenuItems.push(
			{
				label: documentPreviewLabel,
				command: this.viewSelectedDocument.bind(onclick, this)
			}
		);
	}

	private viewSelectedDocument(thisTab: ResultsTabContentComponent): void {
		thisTab.documentWindowMode = "viewOrEdit";		
		thisTab.documentWindowInputData = new DocumentWindowInputData();
		thisTab.documentWindowInputData.documentId = thisTab.selectedDocument.id;
		thisTab.documentWindowInputData.documentLocationRealName = thisTab.documentLocationRealName;
		thisTab.documentWindowVisible = true;
	}

	private onDocumentWindowClosed(event): void {
		this.documentWindowVisible = false;
	}

	private addDefaultResultsTableColumns(): void {
		this.columns.push(this.createColumn(ResultsTabContentComponent.COLUMN_DOCUMENT_NAME, this.translateUtils.translateLabel("DOCUMENT_NAME")));
		this.columns.push(this.createColumn(ResultsTabContentComponent.COLUMN_CREATED_DATE, this.translateUtils.translateLabel("DOC_CREATED")));
		this.columns.push(this.createColumn(ResultsTabContentComponent.COLUMN_DOCUMENT_AUTHOR_DISPLAY_NAME, this.translateUtils.translateLabel("DOC_AUTHOR")));
		this.columns.push(this.createColumn(ResultsTabContentComponent.COLUMN_WORKFLOW_NAME, this.translateUtils.translateLabel("WORKFLOW")));
		this.columns.push(this.createColumn(ResultsTabContentComponent.COLUMN_WORKFLOW_CURRENT_STATE_NAME, this.translateUtils.translateLabel("WORKFLOW_CURRENT_STATUS")));
		this.columns.push(this.createColumn(ResultsTabContentComponent.COLUMN_WORKFLOW_SENDER_DISPLAY_NAME, this.translateUtils.translateLabel("WORKFLOW_SENDER")));
	}

	private createColumn(field: string, header: string): Column {
		let column: Column = new Column();
		column.field = field;
		column.header = header;
		return column;
	}

	public displaySimpleSearchResults(documentLocationRealName: string, documentSimpleSearchResultsViewModel: DocumentSimpleSearchResultsViewModel[]): void {
		this.resetResultsTable();
		this.documentLocationRealName = documentLocationRealName;
		this.addDocumentTypeNameColumn();
		this.buildSimpleSearchResultsPage(documentSimpleSearchResultsViewModel);
	}

	private addDocumentTypeNameColumn(): void {
		this.columns.push(
			this.createColumn(ResultsTabContentComponent.COLUMN_DOCUMENT_TYPE_NAME, this.translateUtils.translateLabel("DOCUMENT_TYPE_NAME"))
		);
	}

	private buildSimpleSearchResultsPage(documentSimpleSearchResultsViewModel: DocumentSimpleSearchResultsViewModel[]): void {
		documentSimpleSearchResultsViewModel.forEach((documentSimpleSearchResultView: DocumentSimpleSearchResultsViewModel) => {
			let document: any = {};
			document[ResultsTabContentComponent.COLUMN_DOCUMENT_ID] = documentSimpleSearchResultView.documentId;
			document[ResultsTabContentComponent.COLUMN_DOCUMENT_NAME] = documentSimpleSearchResultView.documentName;
			document[ResultsTabContentComponent.COLUMN_DOCUMENT_AUTHOR_DISPLAY_NAME] = documentSimpleSearchResultView.documentAuthorDisplayName;
			document[ResultsTabContentComponent.COLUMN_WORKFLOW_NAME] = documentSimpleSearchResultView.workflowName;
			document[ResultsTabContentComponent.COLUMN_WORKFLOW_CURRENT_STATE_NAME] = documentSimpleSearchResultView.workflowCurrentStateName;
			document[ResultsTabContentComponent.COLUMN_WORKFLOW_SENDER_DISPLAY_NAME] = documentSimpleSearchResultView.workflowSenderDisplayName;
			document[ResultsTabContentComponent.COLUMN_DOCUMENT_TYPE_NAME] = documentSimpleSearchResultView.documentTypeName;
			document[ResultsTabContentComponent.COLUMN_CREATED_DATE] = this.getFormattedDateAsString(documentSimpleSearchResultView.documentCreatedDate);
			
			this.documentsPage.items.push(document);
		});
	}

	private getFormattedDateAsString(date: Date): string {
		let formatted: string = this.datePipe.transform(
			date,
			DateConstants.DATE_FORMAT
		);
		return formatted;
	}

	public displayAdvancedSearchResults(documentLocationRealName: string, documentAdvancedSearchResultsViewsWrapperModel: DocumentAdvancedSearchResultsViewsWrapperModel): void {
		this.resetResultsTable();
		this.documentLocationRealName = documentLocationRealName;
		this.prepareColumnsByRepresentativeMetadataDefinitions(documentAdvancedSearchResultsViewsWrapperModel.representativeMetadataDefinitionLabelById);
		this.buildAdvancedSearchResultsPage(documentAdvancedSearchResultsViewsWrapperModel.searchResultsViews);
	}

	private prepareColumnsByRepresentativeMetadataDefinitions(representativeMetadataDefinitions: object): void {
		Object.entries(representativeMetadataDefinitions).forEach((representativeMetadataDefinition: {}) => {
			let metadataId: string = representativeMetadataDefinition[0];
			let metadataLabel: string = representativeMetadataDefinition[1];
			this.columns.push(
				this.createColumn(metadataId, metadataLabel)
			);
		});
	}

	private buildAdvancedSearchResultsPage(searchResultsViews: DocumentAdvancedSearchResultsViewModel[]): void {
		searchResultsViews.forEach((documentAdvancedSearchResultView: DocumentAdvancedSearchResultsViewModel) => {
			let document: any = {};
			document[ResultsTabContentComponent.COLUMN_DOCUMENT_ID] = documentAdvancedSearchResultView.documentId;
			document[ResultsTabContentComponent.COLUMN_DOCUMENT_NAME] = documentAdvancedSearchResultView.documentName;
			document[ResultsTabContentComponent.COLUMN_DOCUMENT_AUTHOR_DISPLAY_NAME] = documentAdvancedSearchResultView.documentAuthorDisplayName;
			document[ResultsTabContentComponent.COLUMN_WORKFLOW_NAME] = documentAdvancedSearchResultView.workflowName;
			document[ResultsTabContentComponent.COLUMN_WORKFLOW_CURRENT_STATE_NAME] = documentAdvancedSearchResultView.workflowCurrentStateName;
			document[ResultsTabContentComponent.COLUMN_WORKFLOW_SENDER_DISPLAY_NAME] = documentAdvancedSearchResultView.workflowSenderDisplayName;
			document[ResultsTabContentComponent.COLUMN_CREATED_DATE] = this.getFormattedDateAsString(documentAdvancedSearchResultView.documentCreatedDate);

			Object.entries(documentAdvancedSearchResultView.documentMetadataInstanceDisplayValueByDefinitionId).forEach((documentMetadataInstanceDisplayValue: object) => {
				let metadataId: string = documentMetadataInstanceDisplayValue[0];
				let metadataValue: string = documentMetadataInstanceDisplayValue[1];
				document[metadataId] = metadataValue;
			});

			this.documentsPage.items.push(document);
		});
	}
}
