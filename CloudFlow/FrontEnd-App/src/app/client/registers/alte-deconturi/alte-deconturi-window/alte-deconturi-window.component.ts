import { Component, OnInit, Input, Output, EventEmitter } from "@angular/core";
import { FormControl, FormBuilder, FormGroup, Validators } from "@angular/forms";
import { TranslateUtils, AlteDeconturiService, MessageDisplayer, UiUtils, ObjectUtils, AppError, FormUtils, DateConstants, DateUtils, StringValidators, BaseWindow } from "@app/shared";
import { AlteDeconturiModel, TipAvansPrimitEnum, AlteDeconturiCheltuieliModel } from "@app/shared/model/alte-deconturi";
import { SelectItem, Dialog } from "primeng/primeng";

@Component({
	selector: "app-alte-deconturi-window",
	templateUrl: "./alte-deconturi-window.component.html"
})
export class AlteDeconturiWindowComponent extends BaseWindow implements OnInit {

	@Input()
	public decontId: number;

	@Input()
	public mode: "add" | "view";

	@Output()
	private windowClosed: EventEmitter<void>;
	
	private translateUtils: TranslateUtils;
	private formBuilder: FormBuilder;
	private alteDeconturiService: AlteDeconturiService;
	private messageDisplayer: MessageDisplayer;

	private alteDeconturiModel: AlteDeconturiModel;
	
	public formGroup: FormGroup;
	public windowVisible: boolean = false;
	public title: string;
	public saveActionEnabled: boolean;

	public dateFormat: string;
	public yearRange: string;

	public tipAvansPrimitItems: SelectItem[];

	public readonly: boolean = false;

	public valoareMinimaAvansPrimit: number = 0;
	public validationMessageMinLengthParameter = {value: this.valoareMinimaAvansPrimit};

	public constructor(translateUtils: TranslateUtils, formBuilder: FormBuilder, alteDeconturiService: AlteDeconturiService, 
			messageDisplayer: MessageDisplayer) {
		super();
		this.translateUtils = translateUtils;
		this.formBuilder = formBuilder;
		this.alteDeconturiService = alteDeconturiService;
		this.messageDisplayer = messageDisplayer;
		this.dateFormat = DateConstants.DATE_FORMAT_FOR_TYPING;
		this.yearRange = DateUtils.getDefaultYearRange();
		this.windowClosed = new EventEmitter<void>();
		this.lock();
	}

	public ngOnInit(): void {
		this.init();
	}

	private init(): void {
		this.prepareForm();
		this.prepareTipAvansPrimitItems();
		this.prepateByMode();
		this.totalCheltuieliFormControl.setValue(0);
		this.totalDeIncasatRestituitFormControl.setValue(0);
	}

	private prepareForm(): void {
		this.formGroup = this.formBuilder.group([]);
		this.formGroup.addControl("titularDecont", new FormControl(null, [StringValidators.blank, Validators.required]));
		this.formGroup.addControl("dataDecont", new FormControl(null, [Validators.required]));
		this.formGroup.addControl("avansPrimit", new FormControl(null, [Validators.required, Validators.min(this.valoareMinimaAvansPrimit)]));
		this.formGroup.addControl("tipAvansPrimit", new FormControl(null, [Validators.required]));
		this.formGroup.addControl("cheltuieli", new FormControl(null, [Validators.required]));
		this.formGroup.addControl("totalCheltuieli", new FormControl(null, []));
		this.formGroup.addControl("totalDeIncasatRestituit", new FormControl(null, []));
	}

	private prepareTipAvansPrimitItems(): void {
		this.tipAvansPrimitItems = [
			{label: this.translateUtils.translateLabel(TipAvansPrimitEnum.CARD), value: TipAvansPrimitEnum.CARD},
			{label: this.translateUtils.translateLabel(TipAvansPrimitEnum.NUMERAR), value: TipAvansPrimitEnum.NUMERAR}
		]
	}

	private prepateByMode(): void {
		if (this.isAdd()) {
			this.prepareForAdd();
		} else if (this.isView()) {
			this.prepareForView();
		}
	}

	private isView(): boolean {
		return this.mode === "view";
	}

	private isAdd(): boolean {
		return this.mode === "add";
	}

	private prepareForAdd(): void {
		this.updatePerspectiveForAdd();
	}

	private updatePerspectiveForAdd(): void {
		this.saveActionEnabled = true;
		this.unlock();
		this.openWindow();
		this.setTitle();
	}

	private setTitle(): void {
		let action: string = null;
		if (this.isAdd()) {
			action = this.translateUtils.translateLabel("ADD");	
		} else if (this.isView()) {
			action = this.translateUtils.translateLabel("VIEW");	
		}
		this.title = action;
	}

	private openWindow(): void {
		this.windowVisible = true;
	}

	private prepareForView(): void {
		this.alteDeconturiService.getDecontById(this.decontId, {
			onSuccess: (alteDeconturiModel: AlteDeconturiModel): void => {
				this.alteDeconturiModel = alteDeconturiModel;
				this.populateForm();
				this.updatePerspectiveForView();
			},
			onFailure: (appError: AppError): void => {
				this.unlock();
				this.messageDisplayer.displayAppError(appError);
			}
		});
	}

	private populateForm(): void {
		this.formGroup.patchValue({
			titularDecont: this.alteDeconturiModel.titularDecont,
			dataDecont: this.alteDeconturiModel.dataDecont,
			avansPrimit: this.alteDeconturiModel.avansPrimit,
			tipAvansPrimit: this.alteDeconturiModel.tipAvansPrimit,
			cheltuieli: this.alteDeconturiModel.cheltuieli,
			totalCheltuieli: this.alteDeconturiModel.totalCheltuieli,
			totalDeIncasatRestituit: this.alteDeconturiModel.totalDeIncasatRestituit
		});
	}

	private updatePerspectiveForView(): void {
		if (this.isView()) {
			this.readonly = true;
			FormUtils.disableAllFormFields(this.formGroup);
			this.dataDecontFormControl.clearValidators();
			FormUtils.enableOrDisableFormControl(this.dataDecontFormControl, false);
			this.saveActionEnabled = false;
		}
		this.setTitle();
		this.unlock();
		this.openWindow();
	}

	public onHide(event: any): void {
		this.closeWindow();
	}

	public onCloseAction(event: any): void {
		this.closeWindow();
	}

	private closeWindow(): void {
		this.windowVisible = false;
		this.windowClosed.emit();
	}

	public onSaveAction(event: any): void {
		if (this.isValid()) {
			this.saveActionEnabled = false;
			let alteDeconturiModel: AlteDeconturiModel = this.buildAlteDeconturiModel();
			this.saveAlteDeconturi(alteDeconturiModel);
		}
	}

	private isValid(): boolean {
		return this.validate();
	}

	private validate(): boolean {
		FormUtils.validateAllFormFields(this.formGroup);
		return this.formGroup.valid;
	}

	private buildAlteDeconturiModel(): AlteDeconturiModel {

		let alteDeconturiModel: AlteDeconturiModel = new AlteDeconturiModel();

		alteDeconturiModel.titularDecont = this.titularDecontFormControl.value;
		alteDeconturiModel.dataDecont = this.dataDecontFormControl.value;
		alteDeconturiModel.avansPrimit = this.avansPrimitFormControl.value;
		alteDeconturiModel.tipAvansPrimit = this.tipAvansPrimitFormControl.value;
		alteDeconturiModel.cheltuieli = this.cheltuieliFormControl.value;
		alteDeconturiModel.totalCheltuieli = this.totalCheltuieliFormControl.value;
		alteDeconturiModel.totalDeIncasatRestituit = this.totalDeIncasatRestituitFormControl.value;

		return alteDeconturiModel;
	}

	private saveAlteDeconturi(alteDeconturiModel: AlteDeconturiModel): void {
			
		this.alteDeconturiService.saveAlteDeconturi(alteDeconturiModel, {
			onSuccess: (): void => {
				this.messageDisplayer.displaySuccess("SAVED_SUCCESSFULLY");
				this.closeWindow();
			},
			onFailure: (appError: AppError): void => {
				this.saveActionEnabled = true;
				this.messageDisplayer.displayAppError(appError);
			}
		});
	}

	public onAlteDeconturiCheltuieliDataChanged(cheltuieli: AlteDeconturiCheltuieliModel[]): void {
		this.calculateTotals(cheltuieli);
	}

	private calculateTotals(cheltuieli: AlteDeconturiCheltuieliModel[]): void {

		let totalCheltuieli: number = 0;
		cheltuieli.forEach((cheltuiala: AlteDeconturiCheltuieliModel) => {
			totalCheltuieli += cheltuiala.valoareCheltuiala;
		});
		this.totalCheltuieliFormControl.setValue(totalCheltuieli);

		if (ObjectUtils.isNotNullOrUndefined(this.totalCheltuieliFormControl.value) && ObjectUtils.isNotNullOrUndefined(this.avansPrimitFormControl.value)) {
			let totalDeIncasatRestituit: number = this.totalCheltuieliFormControl.value - this.avansPrimitFormControl.value;
			this.totalDeIncasatRestituitFormControl.setValue(totalDeIncasatRestituit);
		}
	}

	public onAvansPrimitValueChanged(event: any): void {
		let totalDeIncasatRestituit: number = this.totalCheltuieliFormControl.value - this.avansPrimitFormControl.value;
		this.totalDeIncasatRestituitFormControl.setValue(totalDeIncasatRestituit);
	}

	public get titularDecontFormControl(): FormControl {
		return this.getControlByName("titularDecont");
	}

	public get dataDecontFormControl(): FormControl {
		return this.getControlByName("dataDecont");
	}

	public get avansPrimitFormControl(): FormControl {
		return this.getControlByName("avansPrimit");
	}

	public get tipAvansPrimitFormControl(): FormControl {
		return this.getControlByName("tipAvansPrimit");
	}

	public get cheltuieliFormControl(): FormControl {
		return this.getControlByName("cheltuieli");
	}

	public get totalCheltuieliFormControl(): FormControl {
		return this.getControlByName("totalCheltuieli");
	}

	public get totalDeIncasatRestituitFormControl(): FormControl {
		return this.getControlByName("totalDeIncasatRestituit");
	}

	private getControlByName(controlName: string): FormControl {
		return <FormControl> this.formGroup.controls[controlName];
	}
}