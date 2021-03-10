import { Component, Output, EventEmitter, Input } from "@angular/core";
import { FormBuilder, FormGroup, FormControl, Validators } from "@angular/forms";
import { FormUtils, TranslateUtils, UiUtils, DeplasariDeconturiService, MessageDisplayer, AppError } from "@app/shared";

@Component({
	selector: "app-anulare-deplasare-window",
	templateUrl: "./anulare-deplasare-window.component.html"
})
export class AnulareDeplasareWindowComponent {
	
	@Input()
	public deplasareDecontId: number;

	@Output()
	public windowClosed: EventEmitter<void>;

	private deplasariDeconturiService: DeplasariDeconturiService;
	private messageDisplayer: MessageDisplayer;

	private formBuilder: FormBuilder;
	public formGroup: FormGroup;
	private translateUtils: TranslateUtils;

	public windowVisible: boolean = false;
	public title: string;

	public constructor(deplasariDeconturiService: DeplasariDeconturiService, messageDisplayer: MessageDisplayer, formBuilder: FormBuilder,
			translateUtils: TranslateUtils) {
		this.deplasariDeconturiService = deplasariDeconturiService;
		this.messageDisplayer = messageDisplayer;
		this.formBuilder = formBuilder;
		this.translateUtils = translateUtils;
		this.windowClosed = new EventEmitter<void>();
		this.openWindow();
		this.init();
	}

	private openWindow(): void {
		this.windowVisible = true;
	}

	private init(): void {
		this.title = this.translateUtils.translateLabel("ANULARE_DEPLASARI_TITLE");
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

	public onOkAction(): void {
		if (!this.isValid()) {
			return;
		}
		this.closeDeplasareDecont();
		this.closeWindow();
	}

	private closeDeplasareDecont(): void {
		this.deplasariDeconturiService.cancelDeplasareDecont(this.deplasareDecontId, this.motivAnulareFormControl.value, {
			onSuccess: (): void => {
				this.messageDisplayer.displaySuccess("DEPLASARE_DECONT_CANCELED_SUCCESSFULLY");
				this.closeWindow();
			},
			onFailure: (appError: AppError): void => {
				this.messageDisplayer.displayAppError(appError);
			}
		});
	}

	public onHide(event: any): void {
		this.closeWindow();
			this.windowClosed.emit();
	}

	public onCloseAction(): void {
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