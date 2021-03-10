import { Component, Input, AfterViewInit, EventEmitter, Output } from "@angular/core";
import { FormGroup, FormBuilder, AbstractControl, Validators } from "@angular/forms";
import { MessageDisplayer, MimeTypeService, MimeTypeModel, AppError, ObjectUtils, FormUtils, StringValidators, BaseWindow } from "@app/shared";

@Component({
	selector: "app-mime-type-window",
	templateUrl: "./mime-type-window.component.html",
})
export class MimeTypeWindowComponent extends BaseWindow implements AfterViewInit {

	@Input()
	private mode: "add" | "edit";

	@Input()
	private mimeTypeId: number;

	@Output()
	private windowClosed: EventEmitter<void>;

	@Output()
	private dataSaved: EventEmitter<void>;

	private mimeTypeService: MimeTypeService;
	private messageDisplayer: MessageDisplayer;

	private formBuilder: FormBuilder;
	public form: FormGroup;
	
	public visible: boolean;
	public width: number;

	public constructor(mimeTypeService: MimeTypeService, messageDisplayer: MessageDisplayer, formBuilder: FormBuilder) {
		super();
		this.mimeTypeService = mimeTypeService;
		this.messageDisplayer = messageDisplayer;
		this.formBuilder = formBuilder;
		this.windowClosed = new EventEmitter<void>();
		this.dataSaved = new EventEmitter<void>();
		this.init();
		this.unlock();
	}

	private init(): void {
		this.visible = true;
		this.prepareForm();
		this.adjustSize();
	}

	public ngAfterViewInit(): void {
		if (this.mode === "edit") {
			this.prepareForEdit();
		}
	}

	private adjustSize(): void {
		this.width = window.screen.availWidth - 400;
	}

	private prepareForm(): void {
		this.form = this.formBuilder.group({
			mimeTypeName: ["", [Validators.required, StringValidators.blank()]],
			mimeTypeExtension: ["", [Validators.required, StringValidators.blank()]]
		});
	}

	private prepareForEdit(): void {
		if (ObjectUtils.isNullOrUndefined(this.mimeTypeId)) {
			throw Error("The property [mimeTypeId] cannot be null or undefined.");
		}
		this.lock();
		this.mimeTypeService.getMimeTypeById(this.mimeTypeId, {
			onSuccess: (mimeType: MimeTypeModel): void => {
				this.populateFormFromModel(mimeType);
				this.unlock();
			},
			onFailure: (appError: AppError): void => {
				this.unlock();
				this.messageDisplayer.displayAppError(appError);
			}
		});
	}

	private populateFormFromModel(mimeType: MimeTypeModel): void {
		this.mimeTypeNameFormControl.setValue(mimeType.name);
		this.mimeTypeExtensionFormControl.setValue(mimeType.extension);
	}

	public onSave(): void {
		if (!this.isValid()) {
			return;
		}
		this.lock();
		this.mimeTypeService.saveMimeType(this.buildMimeTypeModel(), {
			onSuccess: (): void => {
				this.unlock();
				this.messageDisplayer.displaySuccess("MIME_TYPE_SAVED");
				this.dataSaved.emit();
				this.windowClosed.emit();				
			},
			onFailure: (appError: AppError): void => {
				this.unlock();
				this.messageDisplayer.displayAppError(appError);
			}
		});
	}

	private buildMimeTypeModel(): MimeTypeModel {
		let mimeType: MimeTypeModel = new MimeTypeModel();

		mimeType.id = null;
		if (ObjectUtils.isNotNullOrUndefined(this.mimeTypeId)) {
			mimeType.id = this.mimeTypeId;
		}
		
		mimeType.name = this.mimeTypeNameFormControl.value;
		mimeType.extension = this.mimeTypeExtensionFormControl.value;
		return mimeType;
	}

	public onHide(event: any): void {
		this.windowClosed.emit();
	}

	private getControlByName(controlName: string): AbstractControl {
		return this.form.controls[controlName];
	}
	
	public get mimeTypeNameFormControl(): AbstractControl {
		return this.getControlByName("mimeTypeName");
	}

	public get mimeTypeExtensionFormControl(): AbstractControl {
		return this.getControlByName("mimeTypeExtension");
	}

	private isValid(): boolean {
		FormUtils.validateAllFormFields(this.form);
		return this.form.valid;
	}
}
