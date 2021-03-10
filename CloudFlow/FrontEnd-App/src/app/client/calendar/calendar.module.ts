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
	CalendarModule as PrimeNGCalendarModule,
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
import { SharedModule } from "@app/shared";
import { ClientCommonModule } from "@app/client/common";
import { CalendarRoutingModule } from "./calendar-routing.module";
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
		PrimeNGCalendarModule,
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
		ClientCommonModule,
		CalendarRoutingModule
	],
	declarations: [
	]
})
export class CalendarModule { }
