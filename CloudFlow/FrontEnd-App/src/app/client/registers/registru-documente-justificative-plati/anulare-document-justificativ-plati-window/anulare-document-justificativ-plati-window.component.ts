import { Component, Input, Output, EventEmitter } from "@angular/core";
import { FormBuilder, FormGroup, FormControl, Validators } from "@angular/forms";
import { FormUtils, TranslateUtils } from "@app/shared";
import { Dialog } from "primeng/primeng";

@Component({
	selector: "app-anulare-document-justificativ-plati-window",
	templateUrl: "./anulare-document-justificativ-plati-window.component.html"
})
export class AnulareDocumentJustificativPlatiWindowComponent {
	
	@Input()
	public maximized: boolean = false;

	@Output()
	private windowClosed: EventEmitter<string>;

	private formBuilder: FormBuilder;
	public formGroup: FormGroup;
	private translateUtils: TranslateUtils;

	public windowVisible: boolean = false;
	public title: string;

	public constructor(formBuilder: FormBuilder, translateUtils: TranslateUtils) {
		this.formBuilder = formBuilder;
		this.translateUtils = translateUtils;
		this.windowClosed = new EventEmitter<string>();
		this.openWindow();
		this.init();
	}

	private openWindow(): void {
		this.windowVisible = true;
	}

	private init(): void {
		this.title = this.translateUtils.translateLabel("ANULARE_DOCUMENT_JUSTIFICATIV_PLATI_TITLE");
		this.prepareForm();
	}

	private prepareForm(): void {
		this.formGroup = this.formBuilder.group([]);
		this.formGroup.addControl("motivAnulare", new FormControl(null, [Validators.required]));
	}

	private isValid(): boolean {
		return this.validate();
	}

	private validate(): boolean {
		FormUtils.validateAllFormFields(this.formGroup);
		let isValid: boolean = this.formGroup.valid;
		return isValid;
	}

	public onOkAction(event: any): void {
		if (this.isValid()) {
			this.closeWindow();
			this.windowClosed.emit(this.motivAnulareFormControl.value);
		}
	}

	public onShow(event: any, pDialog: Dialog): void {
		pDialog.center();
		if (this.maximized) {
			setTimeout(() => {
				pDialog.maximize();
			}, 0);
		}
	}

	public onHide(event: any): void {
		this.closeWindow();
			this.windowClosed.emit();
	}

	public onCloseAction(event: any): void {
		this.closeWindow();
		this.windowClosed.emit();
	}

	private closeWindow(): void {
		this.windowVisible = false;
	}
	
	public get motivAnulareFormControl(): FormControl {
		return <FormControl> this.formGroup.get("motivAnulare");
	}
}