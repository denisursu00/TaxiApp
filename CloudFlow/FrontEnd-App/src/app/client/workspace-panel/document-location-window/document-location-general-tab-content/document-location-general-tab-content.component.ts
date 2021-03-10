import { Component, Input, OnInit } from "@angular/core";
import { FormBuilder, FormGroup, Validators, AbstractControl } from "@angular/forms";
import { DocumentLocationModel } from "@app/shared";

@Component({
	selector: "app-document-location-general-tab-content",
	templateUrl: "./document-location-general-tab-content.component.html",
	styleUrls: ["./document-location-general-tab-content.component.css"]
})
export class DocumentLocationGeneralTabContentComponent implements OnInit {

	@Input()
	public mode: "new" | "edit";

	@Input()
	public name: string;
	
	@Input()
	public description: string;

	private formBuilder: FormBuilder;
	public form: FormGroup;

	public constructor(formBuilder: FormBuilder) {
		this.formBuilder = formBuilder;
		this.init();
	}

	private init(): void {
		this.form = this.buildForm();
	}

	public ngOnInit(): void {
		if (this.mode === "new") {
			this.prepareForAdd();
		} else if (this.mode === "edit") {
			this.prepareForEdit();
		}
	}

	private buildForm(): FormGroup {
		return this.formBuilder.group({
			name: ["", Validators.required],
			description: [""]
		});
	}

	public prepareForAdd(): void {
	}

	public prepareForEdit(): void {
		this.getControlByName("name").setValue(this.name);
		this.getControlByName("description").setValue(this.description);
	}

	public get documentLocationNameFormControl(): AbstractControl {
		return this.getControlByName("name");
	}

	private getControlByName(controlName: string): AbstractControl {
		return this.form.controls[controlName];
	}

	public populateDocumentLocation(documentLocation: DocumentLocationModel): void {
		documentLocation.name = this.getControlByName("name").value;
		documentLocation.description = this.getControlByName("description").value;
	}

	public isValid(): boolean {
		if (!this.form.valid) {
			this.documentLocationNameFormControl.markAsTouched();
		}
		return this.form.valid;
	}
}
