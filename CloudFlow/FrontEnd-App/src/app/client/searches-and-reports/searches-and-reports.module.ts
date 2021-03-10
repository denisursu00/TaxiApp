import { NgModule } from "@angular/core";
import { CommonModule } from "@angular/common";
import { FormsModule, ReactiveFormsModule } from "@angular/forms";
import { ClientCommonModule } from "@app/client/common";
import { SearchesAndReportsComponent } from "./searches-and-reports.component";
import { AdvancedSearchTabContentComponent } from "./advanced-search-tab-content";
import { ResultsTabContentComponent } from "./results-tab-content/results-tab-content.component";
import { SimpleSearchTabContentComponent } from "./simple-search-tab-content";
import { SearchesAndReportsRoutingModule } from "./searches-and-reports-routing.module";
import { SharedModule } from "@app/shared";
import { TableModule } from "primeng/table";
import {
	InputTextModule,
	InputTextareaModule,
	DialogModule,
	ButtonModule,
	DataTableModule,
	MultiSelectModule,
	DropdownModule,
	TreeModule,
	ConfirmDialogModule,
	FileUploadModule,
	TabViewModule,
	AccordionModule,
	FieldsetModule,
	CalendarModule,
	BlockUIModule,
	ProgressSpinnerModule,
	MessageModule,
	MessagesModule,
	TieredMenuModule,
	KeyFilterModule,
	ContextMenuModule,
	PickListModule,
	CheckboxModule,
	PanelModule,
	TooltipModule,
	SelectButtonModule,
	InputSwitchModule,
	ChartModule,
	SpinnerModule
} from "primeng/primeng";
import { DataViewModule } from "primeng/dataview";
import { SearchDocumentMetadataComponent } from "./advanced-search-tab-content";

@NgModule({
	imports: [
		CommonModule,
		ClientCommonModule,
		SearchesAndReportsRoutingModule,
		SharedModule,
		TableModule,
		InputTextModule,
		InputTextareaModule,
		DialogModule,
		ButtonModule,
		DataTableModule,
		MultiSelectModule,
		DropdownModule,
		TreeModule,
		ConfirmDialogModule,
		FileUploadModule,
		TabViewModule,
		AccordionModule,
		FieldsetModule,
		CalendarModule,
		BlockUIModule,
		ProgressSpinnerModule,
		MessageModule,
		MessagesModule,
		TieredMenuModule,
		KeyFilterModule,
		ContextMenuModule,
		PickListModule,
		CheckboxModule,
		PanelModule,
		TooltipModule,
		SelectButtonModule,
		InputSwitchModule,
		ChartModule,
		SpinnerModule,
		FormsModule,
		ReactiveFormsModule
	],
	declarations: [
		SearchesAndReportsComponent,
		AdvancedSearchTabContentComponent,
		SimpleSearchTabContentComponent,
		ResultsTabContentComponent,
		SearchDocumentMetadataComponent
	]
})
export class SearchesAndReportsModule { }
