import { NgModule } from "@angular/core";
import { CommonModule } from "@angular/common";
import { WorkspacePanelComponent } from "./workspace-panel.component";
import { FormsModule, ReactiveFormsModule } from "@angular/forms";
import { HttpModule } from "@angular/http";
import { RouterModule } from "@angular/router";
import { SharedModule } from "@app/shared";
import { FolderToolbarComponent } from "./folder-toolbar";
import { TableModule } from "primeng/table";
import { 
	DocumentWindowComponent, 
	DocumentGeneralTabContentComponent, 
	DocumentAttachmentsTabContentComponent,
	DocumentHistoryTabContentComponent,
	DocumentVersionsTabContentComponent,
	DocumentSecurityTabContentComponent,
	DocumentWorkflowGraphTabContentComponent	
} from "./../common/document-window";
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
import { ScheduleModule } from "primeng/schedule";
import { TreeDragDropService } from "primeng/components/common/treedragdropservice";
import { DocumentGridComponent } from "./document-grid/document-grid.component";
import { FolderWindowComponent } from "./folder-window/folder-window.component";
import { FolderGeneralTabContentComponent } from "./folder-window/folder-general-tab-content/folder-general-tab-content.component";
import { FolderSecurityTabContentComponent } from "./folder-window/folder-security-tab-content/folder-security-tab-content.component";
import { DocumentToolbarComponent } from "./document-toolbar/document-toolbar.component";
import { DocumentLocationToolbarComponent } from "./document-location-toolbar/document-location-toolbar.component";
import { DocumentLocationWindowComponent } from "./document-location-window/document-location-window.component";
import { DocumentLocationSecurityTabContentComponent } from "./document-location-window/document-location-security-tab-content/document-location-security-tab-content.component";
import { DocumentLocationGeneralTabContentComponent } from "./document-location-window/document-location-general-tab-content/document-location-general-tab-content.component";
import { ClientCommonModule } from "@app/client/common";
import { TranslateModule, TranslateLoader } from "@ngx-translate/core";
import { AppTranslateLoader } from "@app/shared";
import { WorkspacePanelRoutingModule } from "./workspace-panel-routing.module";

@NgModule({
	imports: [
		TranslateModule.forChild({
			loader: {
				provide: TranslateLoader,
				useClass: AppTranslateLoader
			}
		}),
		CommonModule,
		RouterModule,
		HttpModule,
		SharedModule,
		InputTextModule,
		InputTextareaModule,
		DialogModule,
		ButtonModule,
		DataTableModule,
		CommonModule,
		MultiSelectModule,
		FormsModule,
		ReactiveFormsModule,
		DropdownModule,
		TreeModule,
		TabViewModule,
		AccordionModule,
		FileUploadModule,
		MessageModule,
		MessagesModule,
		FieldsetModule,
		CalendarModule,
		BlockUIModule,
		ProgressSpinnerModule,
		KeyFilterModule,
		TableModule,
		TieredMenuModule,
		ContextMenuModule,
		PickListModule,
		CheckboxModule,
		PanelModule,
		TooltipModule,
		ScheduleModule,
		SelectButtonModule,
		InputSwitchModule,
		DataViewModule,
		SpinnerModule,
		ClientCommonModule,
		WorkspacePanelRoutingModule
	],
	exports: [
		RouterModule,
		HttpModule
	],
	declarations: [
		WorkspacePanelComponent,
		FolderToolbarComponent,
		DocumentGridComponent,
		FolderWindowComponent,
		FolderGeneralTabContentComponent,
		FolderSecurityTabContentComponent,
		DocumentToolbarComponent,
		DocumentLocationToolbarComponent,
		DocumentLocationWindowComponent,
		DocumentLocationGeneralTabContentComponent,
		DocumentLocationSecurityTabContentComponent
	],
	providers: [
		TreeDragDropService
	]
})
export class WorkspacePanelModule { }
