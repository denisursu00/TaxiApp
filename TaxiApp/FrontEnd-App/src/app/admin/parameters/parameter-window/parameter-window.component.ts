import { Component, OnInit, Input, Output, EventEmitter } from "@angular/core";
import { ParametersService, MessageDisplayer, StringValidators, FormUtils, ParameterType, DateConstants, DateUtils, UiUtils, ParameterModel, AppError, StringUtils, BaseWindow } from "@app/shared";
import { FormBuilder, FormGroup, Validators, AbstractControl } from "@angular/forms";
import { SelectItem, Dialog } from "primeng/primeng";
import { ListItemUtils } from "@app/shared/utils/list-item-utils";

@Component({
	selector: "app-parameter-window",
	templateUrl: "./parameter-window.component.html"
})
export class ParameterWindowComponent extends BaseWindow implements OnInit {
	
	@Input()
	public mode: "add" | "edit";

	@Input()
	public parameterId: number;

	@Output()
	public windowClosed: EventEmitter<void>;

	@Output()
	public dataSaved: EventEmitter<void>;

	private parametersService: ParametersService;
	private messageDisplayer: MessageDisplayer;

	private formBuilder: FormBuilder;
	public form: FormGroup;

	public typeSelectItems: SelectItem[];

	public dateFormat: string;
	public yearRange: string;

	public windowVisible: boolean;

	public constructor(parametersService: ParametersService, messageDisplayer: MessageDisplayer, formBuilder: FormBuilder) {
		super();
		this.parametersService = parametersService;
		this.messageDisplayer = messageDisplayer;
		this.formBuilder = formBuilder;
		this.windowClosed = new EventEmitter<void>();
		this.dataSaved = new EventEmitter<void>();
		this.init();
	}

	private init(): void {
		
		this.windowVisible = true;

		this.dateFormat = DateConstants.DATE_FORMAT_FOR_TYPING;
		this.yearRange = DateUtils.getDefaultYearRange();

		this.prepareForm();
		this.prepareTypeSelectItems();
	}

	private prepareForm(): void {
		this.form = this.formBuilder.group({
			name: [null, [Validators.required, StringValidators.blank()]],
			description: [null, [Validators.required, StringValidators.blank()]],
			type: [null, Validators.required],
			stringValue: [null, [Validators.required, StringValidators.blank()]],
			numberValue: [null, Validators.required],
			dateValue: [null, Validators.required],
			booleanValue: [null]
		});
	}

	private prepareTypeSelectItems(): void {
		this.typeSelectItems = [
			{ label: ParameterType.STRING, value: ParameterType.STRING },
			{ label: ParameterType.NUMBER, value: ParameterType.NUMBER },
			{ label: ParameterType.DATE, value: ParameterType.DATE },
			{ label: ParameterType.BOOLEAN, value: ParameterType.BOOLEAN }
		];

		ListItemUtils.sortByLabel(this.typeSelectItems);
	}

	public ngOnInit(): void {
		if (this.isAddMode()) {
			this.prepareForAdd();
		} else if (this.isEditMode()) {
			this.prepareForEdit();
		}
	}

	private isAddMode(): boolean {
		return this.mode === "add";
	}

	private isEditMode(): boolean {
		return this.mode === "edit";
	}

	private prepareForAdd(): void {
		this.lock();
		this.disableParameterValueRelatedFormControls();
		this.unlock();
	}

	private prepareForEdit(): void {
		this.lock();
		this.disableParameterValueRelatedFormControls();
		this.parametersService.getParameterById(this.parameterId, {
			onSuccess: (parameterModel: ParameterModel): void => {
				this.prepareFormFromParameterModel(parameterModel);
				this.changeFormPerspectiveForParameterValueFormControls();
				this.unlock();
			},
			onFailure: (appError: AppError): void => {
				this.messageDisplayer.displayAppError(appError);
				this.unlock();
			}
		});
	}

	private prepareFormFromParameterModel(parameterModel: ParameterModel): void {
		this.nameFormControl.setValue(parameterModel.name);
		this.descriptionFormControl.setValue(parameterModel.description);
		this.typeFormControl.setValue(parameterModel.type);
		
		if (parameterModel.type === ParameterType.STRING) {
			this.stringValueFormControl.setValue(parameterModel.value);
		} else if (parameterModel.type === ParameterType.NUMBER) {
			this.numberValueFormControl.setValue(parameterModel.value);
		} else if (parameterModel.type === ParameterType.DATE) {
			let parameterValue: Date = DateUtils.parseFromStorage(parameterModel.value);
			this.dateValueFormControl.setValue(parameterValue);
		} else if (parameterModel.type === ParameterType.BOOLEAN) {
			this.booleanValueFormControl.setValue(parameterModel.value);
		}
	}

	public onSaveAction(event: any): void {
		if (!this.isFormValid()) {
			return;
		}
		this.saveParameter();
	}

	private isFormValid(): boolean {
		FormUtils.validateAllFormFields(this.form);
		return this.form.valid;
	}

	private saveParameter(): void {
		this.parametersService.saveParameter(this.getParameterModelFromForm(), {
			onSuccess: (): void => {
				this.messageDisplayer.displaySuccess("SAVED_SUCCESSFULLY");
				this.dataSaved.emit();
				this.closeWindow();
			},
			onFailure: (appError: AppError): void => {
				this.messageDisplayer.displayAppError(appError);
			}
		});
	}

	private getParameterModelFromForm(): ParameterModel {
		let model: ParameterModel = new ParameterModel();
		if (this.isEditMode()) {
			model.id = this.parameterId;
		}
		model.name = this.nameFormControl.value;
		model.description = this.descriptionFormControl.value;
		
		let parameterType: string = this.typeFormControl.value;
		model.type = parameterType;

		if (parameterType === ParameterType.STRING) {
			model.value = this.stringValueFormControl.value;
		} else if (parameterType === ParameterType.NUMBER) {
			model.value = String(this.numberValueFormControl.value);
		} else if (parameterType === ParameterType.DATE) {
			let parameterValue: Date = this.dateValueFormControl.value;
			model.value = DateUtils.formatForStorage(parameterValue);
		} else if (parameterType === ParameterType.BOOLEAN) {
			model.value = String(this.booleanValueFormControl.value);
		}
		return model;
	}

	public onHide(event: any): void {
		this.closeWindow();
	}

	public onCloseAction(event: any): void {
		this.closeWindow();
	}

	private closeWindow(): void {
		this.unlock();
		this.windowVisible = false;
		this.windowClosed.emit();
	}

	public isParameterTypeSelected(): boolean {
		return StringUtils.isNotBlank(this.typeFormControl.value);
	}

	private disableParameterValueRelatedFormControls(): void {
		FormUtils.enableOrDisableFormControl(this.stringValueFormControl, false);
		FormUtils.enableOrDisableFormControl(this.numberValueFormControl, false);
		FormUtils.enableOrDisableFormControl(this.dateValueFormControl, false);
		FormUtils.enableOrDisableFormControl(this.booleanValueFormControl, false);
	}

	public onParameterTypeChanged(event: any): void {
		this.changeFormPerspectiveForParameterValueFormControls();
	}

	private changeFormPerspectiveForParameterValueFormControls(): void {

		this.disableParameterValueRelatedFormControls();

		let selectedParameterValue: ParameterType = this.typeFormControl.value;
		
		if (selectedParameterValue === ParameterType.STRING) {
			FormUtils.enableOrDisableFormControl(this.stringValueFormControl, true);
		} else if (selectedParameterValue === ParameterType.NUMBER) {
			FormUtils.enableOrDisableFormControl(this.numberValueFormControl, true);
		} else if (selectedParameterValue === ParameterType.DATE) {
			FormUtils.enableOrDisableFormControl(this.dateValueFormControl, true);
		} else if (selectedParameterValue === ParameterType.BOOLEAN) {
			FormUtils.enableOrDisableFormControl(this.booleanValueFormControl, true);
		}
	}

	public get nameFormControl(): AbstractControl {
		return FormUtils.getControlByName(this.form, "name");
	}

	public get descriptionFormControl(): AbstractControl {
		return FormUtils.getControlByName(this.form, "description");
	}

	public get stringValueFormControl(): AbstractControl {
		return FormUtils.getControlByName(this.form, "stringValue");
	}

	public get numberValueFormControl(): AbstractControl {
		return FormUtils.getControlByName(this.form, "numberValue");
	}

	public get dateValueFormControl(): AbstractControl {
		return FormUtils.getControlByName(this.form, "dateValue");
	}

	public get booleanValueFormControl(): AbstractControl {
		return FormUtils.getControlByName(this.form, "booleanValue");
	}

	public get typeFormControl(): AbstractControl {
		return FormUtils.getControlByName(this.form, "type");
	}
}
