import { NgModule } from "@angular/core";
import { CommonModule } from "@angular/common";
import { FormsModule, ReactiveFormsModule } from "@angular/forms";
import { ClientCommonModule } from "@app/client/common";
import { ProjectsComponent } from "./projects.component";
import { ProjectsRoutingModule } from "./projects-routing.module";
import { TranslateModule, TranslateLoader } from "@ngx-translate/core";
import { AppTranslateLoader } from "@app/shared";
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
import { TableModule } from "primeng/table";
import { SharedModule } from "@app/shared";
import { 
	ProjectWindowComponent,
	ProjectComisiiSauGlTabContentComponent,
	ProjectEstimationWindowComponent,
	ProjectEstimationsTabContentComponent,
	ProjectGeneralTabContentComponent,
	ProjectParticipantsTabContentComponent,
	ProjectSubactivitiesTabContentComponent
} from "./project-window";

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
		TableModule,
		CommonModule,
		ProjectsRoutingModule,
		FormsModule, 
		ReactiveFormsModule,
		SharedModule,
		ClientCommonModule
	],
	declarations: [
		ProjectsComponent,
		ProjectWindowComponent,
		ProjectComisiiSauGlTabContentComponent,
		ProjectEstimationWindowComponent,
		ProjectEstimationsTabContentComponent,
		ProjectGeneralTabContentComponent,
		ProjectParticipantsTabContentComponent,
		ProjectSubactivitiesTabContentComponent
	]
})
export class ProjectsModule { }
