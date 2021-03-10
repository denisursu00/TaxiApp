import { Component, Output, EventEmitter, Input } from "@angular/core";
import { FormBuilder, FormGroup, FormControl, Validators } from "@angular/forms";
import { FormUtils, TranslateUtils, UiUtils, RegistruIntrariIesiriService, MessageDisplayer, AppError, AlteDeconturiService } from "@app/shared";

@Component({
	selector: "app-anulare-alte-deconturi-window",
	templateUrl: "./anulare-alte-deconturi-window.component.html"
})
export class AnulareAlteDeconturiWindowComponent {
	
	@Input()
	public decontId: number;

	@Output()
	public windowClosed: EventEmitter<void>;

	private alteDeconturiService: AlteDeconturiService;
	private messageDisplayer: MessageDisplayer;

	private formBuilder: FormBuilder;
	private translateUtils: TranslateUtils;

	public formGroup: FormGroup;
	public windowVisible: boolean = false;
	public title: string;

	public constructor(alteDeconturiService: AlteDeconturiService, messageDisplayer: MessageDisplayer, formBuilder: FormBuilder,
			translateUtils: TranslateUtils) {
		this.alteDeconturiService = alteDeconturiService;
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
		this.title = this.translateUtils.translateLabel("ANULARE_DECONT_TITLE");
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
		this.cancelDecont();
	}

	private cancelDecont(): void {
		this.alteDeconturiService.cancelDecont(this.decontId, this.motivAnulareFormControl.value, {
			onSuccess: (): void => {
				this.messageDisplayer.displaySuccess("DECONT_CANCELED_SUCCESSFULLY");
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