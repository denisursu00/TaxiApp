import { NgModule } from "@angular/core";
import { CommonModule } from "@angular/common";
import { FormsModule, ReactiveFormsModule } from "@angular/forms";
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
	SpinnerModule, 
	ScheduleModule
} from "primeng/primeng";
import { TableModule } from "primeng/table";
import { MetadataUserFieldComponent } from "./metadata-user-field/metadata-user-field.component";
import { MetadataListFieldComponent } from "./metadata-list-field/metadata-list-field.component";
import { MetadataNumericFieldComponent } from "./metadata-numeric-field/metadata-numeric-field.component";
import { MetadataNomenclatorFieldComponent } from "./metadata-nomenclator-field/metadata-nomenclator-field.component";
import { MetadataDateFieldComponent } from "./metadata-date-field/metadata-date-field.component";
import { MetadataDateTimeFieldComponent } from "./metadata-date-time-field/metadata-date-time-field.component";
import { MetadataMonthFieldComponent } from "./metadata-month-field/metadata-month-field.component";
import { MetadataGroupFieldComponent } from "./metadata-group-field/metadata-group-field.component";
import { DocumentLocationSelectorComponent } from "./document-location-selector";
import { DocumentSelectionWindowComponent } from "./document-selection-window";
import { MetadataDocumentFieldComponent} from "./metadata-document-field/metadata-document-field.component";
import { MetadataCalendarFieldComponent} from "./metadata-calendar-field/metadata-calendar-field.component";
import { MetadataProjectFieldComponent} from "./metadata-project-field/metadata-project-field.component";
import { ProjectSelectionWindowComponent} from "./project-selection-window";
import { DspViewWindowComponent } from "./dsp-view-window";
import { 
	DocumentWindowComponent,
	DocumentGeneralTabContentComponent, 
	DocumentAttachmentsTabContentComponent,
	DocumentHistoryTabContentComponent,
	DocumentVersionsTabContentComponent,
	DocumentSecurityTabContentComponent,
	DocumentWorkflowGraphTabContentComponent,
	DocumentMetadataComponent,
	ChooseWorkflowDestinationUserWindowComponent,
	ChooseWorkflowTransitionWindowComponent,
	ChooseDocumentTemplateWindowComponent,
	MetadataCollectionFieldComponent,
	MetadataCollectionWindowComponent,
	MetadataCollectionWindowDynamicContentComponent,
	MetadataCollectionWindowInformatiiParticipantiOfPrezentaComisiiGLContentComponent,
	MetadataCollectionWindowPrezentaMembriiOfPrezentaAgaContentComponent
} from "./document-window";
import { 
	AdminUpdateDocumentWindowComponent,
	AdminUpdateDocumentWindowGeneralTabContentComponent, 
	AdminUpdateDocumentWindowAttachmentsTabContentComponent,
	AdminUpdateDocumentMetadataComponent,
	AdminUpdateDocumentMetadataCollectionFieldComponent,
	AdminUpdateDocumentMetadataCollectionWindowComponent,
	AdminUpdateDocumentMetadataCollectionWindowDynamicContentComponent
} from "./admin-update-document-window";
import { TranslateModule, TranslateLoader } from "@ngx-translate/core";
import { AppTranslateLoader } from "@app/shared";
import { SharedModule } from "@app/shared";
import { CalendarManagerComponent, CalendarEventWindowComponent } from "./calendar-manager";
import { ProjectTaskSelectorComponent } from "./project-task-selector";
import { MyDocumentActivitiesComponent } from "./my-document-activities";
import { MyProjectTasksComponent } from "./my-project-tasks";
import { CompleteTaskWindowComponent } from "./my-project-tasks/complete-task-window/complete-task-window.component";
import { TaskWindowComponent } from "./my-project-tasks/task-window/task-window.component";
import { TaskGeneralTabContentComponent } from "./my-project-tasks/task-window/task-general-tab-content/task-general-tab-content.component";
import { TaskAssignmentTabContentComponent } from "./my-project-tasks/task-window/task-assignment-tab-content/task-assignment-tab-content.component";
import { ViewTaskWindowComponent } from "./my-project-tasks/view-task-window/view-task-window.component";
import { DocumenSelectiontFieldComponent } from "./document-selection-field";
import { TAsksViewWindowComponent } from "./tasks-view-window";

@NgModule({
	imports: [
		TranslateModule.forChild({
			loader: {
				provide: TranslateLoader,
				useClass: AppTranslateLoader
			}
		}),
		InputTextModule,
		InputTextareaModule,
		DialogModule,
		ButtonModule,
		DataTableModule,
		CommonModule,
		MultiSelectModule,
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
		TieredMenuModule,
		ContextMenuModule,
		PickListModule,
		CheckboxModule,
		PanelModule,
		TooltipModule,
		SelectButtonModule,
		InputSwitchModule,
		SpinnerModule,
		ScheduleModule,
		TableModule,
		FormsModule,		
		ReactiveFormsModule,
		SharedModule
	],
	declarations: [
		DocumentLocationSelectorComponent,
		MetadataUserFieldComponent,
		MetadataListFieldComponent,
		MetadataNumericFieldComponent,
		MetadataNomenclatorFieldComponent,
		MetadataDateFieldComponent,
		MetadataDateTimeFieldComponent,
		MetadataMonthFieldComponent,
		MetadataGroupFieldComponent,
		DocumentSelectionWindowComponent,
		MetadataDocumentFieldComponent,
		MetadataCalendarFieldComponent,
		MetadataCalendarFieldComponent,
		MetadataProjectFieldComponent,
		ProjectSelectionWindowComponent,
		DspViewWindowComponent,
		DocumentWindowComponent,
		DocumentGeneralTabContentComponent, 
		DocumentAttachmentsTabContentComponent,
		DocumentHistoryTabContentComponent,
		DocumentVersionsTabContentComponent,
		DocumentSecurityTabContentComponent,
		DocumentWorkflowGraphTabContentComponent,
		DocumentMetadataComponent,
		ChooseWorkflowDestinationUserWindowComponent,
		ChooseWorkflowTransitionWindowComponent,
		ChooseDocumentTemplateWindowComponent,
		MetadataCollectionFieldComponent,
		MetadataCollectionWindowComponent,
		MetadataCollectionWindowDynamicContentComponent,
		MetadataCollectionWindowInformatiiParticipantiOfPrezentaComisiiGLContentComponent,
		MetadataCollectionWindowPrezentaMembriiOfPrezentaAgaContentComponent,
		CalendarManagerComponent,
		CalendarEventWindowComponent,
		ProjectTaskSelectorComponent,
		MyDocumentActivitiesComponent,
		MyProjectTasksComponent,
		CompleteTaskWindowComponent,
		TaskWindowComponent,
		TaskGeneralTabContentComponent,
		TaskAssignmentTabContentComponent,
		ViewTaskWindowComponent,
		DocumenSelectiontFieldComponent,
		TAsksViewWindowComponent,
		AdminUpdateDocumentWindowComponent,
		AdminUpdateDocumentWindowGeneralTabContentComponent, 
		AdminUpdateDocumentWindowAttachmentsTabContentComponent,
		AdminUpdateDocumentMetadataComponent,
		AdminUpdateDocumentMetadataCollectionFieldComponent,
		AdminUpdateDocumentMetadataCollectionWindowComponent,
		AdminUpdateDocumentMetadataCollectionWindowDynamicContentComponent
	],
	exports: [
		DocumentLocationSelectorComponent,
		MetadataUserFieldComponent,
		MetadataListFieldComponent,
		MetadataNumericFieldComponent,
		MetadataNomenclatorFieldComponent,
		MetadataDateFieldComponent,
		MetadataDateTimeFieldComponent,
		MetadataMonthFieldComponent,
		MetadataGroupFieldComponent,
		DocumentSelectionWindowComponent,
		MetadataDocumentFieldComponent,
		MetadataCalendarFieldComponent,
		MetadataCalendarFieldComponent,
		MetadataProjectFieldComponent,
		ProjectSelectionWindowComponent,
		DspViewWindowComponent,
		DocumentWindowComponent,
		DocumentGeneralTabContentComponent, 
		DocumentAttachmentsTabContentComponent,
		DocumentHistoryTabContentComponent,
		DocumentVersionsTabContentComponent,
		DocumentSecurityTabContentComponent,
		DocumentWorkflowGraphTabContentComponent,
		DocumentMetadataComponent,
		ChooseWorkflowDestinationUserWindowComponent,
		ChooseWorkflowTransitionWindowComponent,
		ChooseDocumentTemplateWindowComponent,
		MetadataCollectionFieldComponent,
		MetadataCollectionWindowComponent,
		MetadataCollectionWindowDynamicContentComponent,
		MetadataCollectionWindowInformatiiParticipantiOfPrezentaComisiiGLContentComponent,
		MetadataCollectionWindowPrezentaMembriiOfPrezentaAgaContentComponent,
		CalendarManagerComponent,
		CalendarEventWindowComponent,
		ProjectTaskSelectorComponent,
		MyDocumentActivitiesComponent,
		MyProjectTasksComponent,
		CompleteTaskWindowComponent,
		TaskWindowComponent,
		TaskGeneralTabContentComponent,
		TaskAssignmentTabContentComponent,
		ViewTaskWindowComponent,
		DocumenSelectiontFieldComponent,
		TAsksViewWindowComponent,
		AdminUpdateDocumentWindowComponent,
		AdminUpdateDocumentWindowGeneralTabContentComponent, 
		AdminUpdateDocumentWindowAttachmentsTabContentComponent,
		AdminUpdateDocumentMetadataComponent,
		AdminUpdateDocumentMetadataCollectionFieldComponent,
		AdminUpdateDocumentMetadataCollectionWindowComponent,
		AdminUpdateDocumentMetadataCollectionWindowDynamicContentComponent
	]
})
export class ClientCommonModule { }
