import { Component, OnInit } from "@angular/core";
import { CalendarTabContent } from "../calendar-tab-content";
import { FormBuilder, FormGroup, Validators, AbstractControl } from "@angular/forms";
import { StringValidators, CalendarModel, FormUtils } from "@app/shared";

@Component({
	selector: "app-calendar-general-tab-content",
	templateUrl: "./calendar-general-tab-content.component.html"
})
export class CalendarGeneralTabContentComponent extends CalendarTabContent implements OnInit {

	public static readonly DEFAULT_COLOR: string = "#6b798f";

	private formBuilder: FormBuilder;
	public form: FormGroup;

	public constructor(formBuilder: FormBuilder) {
		super();
		this.formBuilder = formBuilder;
		this.prepareForm();
	}

	private prepareForm(): void {
		this.form = this.formBuilder.group({
			name: [null, [Validators.required, StringValidators.blank()]],
			description: [null],
			color: [CalendarGeneralTabContentComponent.DEFAULT_COLOR, Validators.required]
		});
	}

	public doWhenNgOnInit(): void {
	}

	public reset(): void {
		this.form.reset();
	}
	
	public prepareForAdd(): void {
		this.prepareForm();
	}
	
	public prepareForEdit(): void {
		this.calendarNameFormControl.setValue(this.calendarModel.name);
		this.calendarDescriprionFormControl.setValue(this.calendarModel.description);
		this.calendarColorFormControl.setValue(this.calendarModel.color);
	}
	
	public populateForSave(calendarModel: CalendarModel): void {
		calendarModel.id = null;
		calendarModel.name = this.calendarNameFormControl.value;
		calendarModel.description = this.calendarDescriprionFormControl.value;
		calendarModel.color = this.calendarColorFormControl.value;
	}
	
	public isValid(): boolean {
		FormUtils.validateAllFormFields(this.form);
		return this.form.valid;
	}

	private getControlByName(name: string): AbstractControl {
		return this.form.controls[name];
	}
	
	public get calendarNameFormControl(): AbstractControl {
		return this.getControlByName("name");
	}

	public get calendarDescriprionFormControl(): AbstractControl {
		return this.getControlByName("description");
	}

	public get calendarColorFormControl(): AbstractControl {
		return this.getControlByName("color");
	}
}