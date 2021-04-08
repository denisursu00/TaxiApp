import { NgModule } from "@angular/core";
import { HttpModule } from "@angular/http";
import { RouterModule } from "@angular/router";
import { SharedModule } from "@app/shared";
import { FormsModule, ReactiveFormsModule } from "@angular/forms";
import { SplitButtonModule } from "primeng/splitbutton";
import { CommonModule } from "@angular/common";
import { TableModule } from "primeng/table";
import { SelectButtonModule } from "primeng/selectbutton";
import {
	ButtonModule,
	InputTextModule,
	DialogModule,
	TabViewModule,
	BlockUIModule,
	ProgressSpinnerModule,
	RadioButtonModule,
	FieldsetModule,
	CheckboxModule,
	InputTextareaModule,
	PickListModule,
	TieredMenuModule,
	DropdownModule,
	AutoCompleteModule,
	OverlayPanelModule,
	KeyFilterModule,
	ListboxModule,
	CalendarModule,
	TooltipModule,
	ColorPickerModule,
	AccordionModule,
	SpinnerModule,
	PasswordModule
} from "primeng/primeng";
import { DataViewModule } from "primeng/dataview";
import { ParametersComponent } from "./parameters/parameters.component";
import { ParameterWindowComponent } from "./parameters/parameter-window/parameter-window.component";
import { AdminRoutingModule } from "./admin-routing.module";
import { DriversComponent } from './drivers/drivers.component';
import { DriversWindowComponent } from './drivers/drivers-window/drivers-window.component';

@NgModule({
	declarations: [
		ParametersComponent,
		ParameterWindowComponent,
		DriversComponent,
		DriversWindowComponent
	],
	imports: [
		RouterModule,
		HttpModule,
		SharedModule,
		FormsModule,
		ReactiveFormsModule,
		SplitButtonModule,
		ButtonModule,
		InputTextModule,
		DialogModule,
		TabViewModule,
		CommonModule,
		CheckboxModule,
		DropdownModule,
		TableModule,
		BlockUIModule,
		ProgressSpinnerModule,
		RadioButtonModule,
		FieldsetModule,
		InputTextareaModule,
		PickListModule,
		DropdownModule,
		TieredMenuModule,
		AutoCompleteModule,
		OverlayPanelModule,
		KeyFilterModule,
		ListboxModule,
		CalendarModule,
		DataViewModule,
		ColorPickerModule,
		SelectButtonModule,
		TooltipModule,
		AccordionModule,
		SpinnerModule,
		PasswordModule,
		AdminRoutingModule
	],
	exports: [
		RouterModule,
		HttpModule
	]
})
export class AdminModule {
}
