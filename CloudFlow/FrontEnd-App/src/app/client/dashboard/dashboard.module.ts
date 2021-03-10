import { NgModule } from "@angular/core";
import { CommonModule } from "@angular/common";
import { FormsModule, ReactiveFormsModule } from "@angular/forms";
import { HttpModule } from "@angular/http";
import { RouterModule } from "@angular/router";
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
import { ScheduleModule } from "primeng/schedule";
import { TreeDragDropService } from "primeng/components/common/treedragdropservice";
import { DashboardRoutingModule } from "./dashboard-routing.module";
import { DashboardComponent } from "@app/client/dashboard/dashboard.component";
import { ProjectsWithDspDegreeOfAchievementChartsComponent, ProjectsWithDspViewerComponent} from "./project";
import { ProjectWithDspFinalizedTasksWindowComponent, ProjectWithDspInProgressTasksWindowComponent } from "./project";
import { ClientCommonModule } from "@app/client/common";
import { TranslateModule, TranslateLoader } from "@ngx-translate/core";
import { AppTranslateLoader } from "@app/shared";
import { ChartsModule } from "@app/charts";

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
		ChartsModule,
		DashboardRoutingModule,
		ClientCommonModule
	],
	declarations: [
		DashboardComponent,
		ProjectsWithDspDegreeOfAchievementChartsComponent,
		ProjectsWithDspViewerComponent,
		ProjectWithDspFinalizedTasksWindowComponent, 
		ProjectWithDspInProgressTasksWindowComponent
	],
	exports: [
		RouterModule,
		HttpModule
	]
})
export class DashboardModule { }
