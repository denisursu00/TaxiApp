import { Component } from "@angular/core";
import { NomenclatorTabContent } from "../nomenclator-tab-content";
import { NomenclatorModel, Message, StringValidators, FormUtils } from "@app/shared";
import { FormBuilder, FormGroup, Validators, AbstractControl } from "@angular/forms";

@Component({
	selector: "app-nomenclator-general-tab-content",
	templateUrl: "./nomenclator-general-tab-content.component.html"
})
export class NomenclatorGeneralTabContentComponent extends NomenclatorTabContent {

	private messages: Message[];

	private formBuilder: FormBuilder;
	public form: FormGroup;

	public constructor(formBuilder: FormBuilder) {
		super();
		this.formBuilder = formBuilder;
		this.init();
	}

	private init(): void {
		this.prepareForm();
	}

	private prepareForm(): void {
		this.form = this.formBuilder.group({
			name: [null, [Validators.required, StringValidators.blank()]],
			description: [null],
		});
	}
	
	public get nameFormControl(): AbstractControl {
		return this.form.controls["name"];
	}
	
	public get descriptionFormControl(): AbstractControl {
		return this.form.controls["description"];
	}

	protected doWhenNgOnInit(): void {
		// Nothing here
	}

	protected prepareForAdd(): void {
		// Nothing here
	}
	
	protected prepareForEdit(): void {
		this.form.patchValue({
			name: this.inputData.name,
			description: this.inputData.description
		});
	}

	public populateNomenclator(nomenclator: NomenclatorModel): void {
		if (this.isEdit()) {
			nomenclator.id = this.inputData.id;
		}
		nomenclator.name = this.form.controls.name.value;
		nomenclator.description = this.form.controls.description.value;
	}

	public isValid(): boolean {
		return this.validate();
	}

	private validate(): boolean {
		this.messages = [];
		FormUtils.validateAllFormFields(this.form);
		let isValid: boolean = this.form.valid;
		if (!isValid) {
			this.messages.push(Message.createForError("REQUIRED_FIELDS_NOT_COMPLETED"));
		}
		return isValid;
	}

	public getMessages(): Message[] {
		return this.messages;
	}
}