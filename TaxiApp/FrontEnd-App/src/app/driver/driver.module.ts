import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { HttpModule } from "@angular/http";
import { RouterModule } from "@angular/router";
import { SharedModule } from "@app/shared";
import { FormsModule, ReactiveFormsModule } from "@angular/forms";
import { SplitButtonModule } from "primeng/splitbutton";
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
import { DataViewModule } from 'primeng/dataview';
import { DriverRoutingModule } from './driver-routing.module';
import { PersonalPageDriverComponent } from './personal-page-driver/personal-page-driver.component';

@NgModule({
  declarations: [PersonalPageDriverComponent],
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
    DriverRoutingModule
  ],
	exports: [
		RouterModule,
		HttpModule
	]
})
export class DriverModule { }
