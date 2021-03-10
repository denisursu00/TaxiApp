import { NgModule } from "@angular/core";
import { CommonModule } from "@angular/common";
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
import { FormsModule, ReactiveFormsModule } from "@angular/forms";
import { NomenclatorsRoutingModule } from "./nomenclators-routing.module";
import { NomenclatorsDataComponent } from "./nomenclators-data";
import { SharedModule } from "@app/shared";
import { ClientCommonModule } from "@app/client/common";
import { TranslateModule, TranslateLoader } from "@ngx-translate/core";
import { AppTranslateLoader } from "@app/shared";

@NgModule({
	imports: [
		TranslateModule.forChild({
			loader: {
				provide: TranslateLoader,
				useClass: AppTranslateLoader
			}
		}),
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
		SelectButtonModule,
		InputSwitchModule,
		SpinnerModule,
		CommonModule,
		NomenclatorsRoutingModule,
		ClientCommonModule
	],
	declarations: [
		NomenclatorsDataComponent
	]
})
export class NomenclatorsModule { }
