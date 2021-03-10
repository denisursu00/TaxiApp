import { Component, OnInit, ViewChild, EventEmitter } from "@angular/core";
import { Validators, FormControl, FormGroup, FormBuilder, AbstractControl } from "@angular/forms";
import { ArrayUtils, DateConstants, DateUtils, ObjectUtils, ValueOfNomenclatorValueField, FormUtils, NomenclatorUtils, NomenclatorService, NomenclatorAttributeModel, AppError, MessageDisplayer, NomenclatorValueModel, NomenclatorConstants } from "@app/shared";
import { ReprezentantiComisieSauGLModel, NomenclatorValidators, Message } from "@app/shared";
import { ReprezentantiComisieSauGLTabContent } from "./../reprezentanti-comisie-sau-gl-tab-content";
import { ReprezentantiComisiiSauGlValidators } from "./validators/reprezentanti-comisii-sau-gl-validators";

@Component({
	selector: "app-reprezentanti-comisie-sau-gl-general-tab-content",
	templateUrl: "./reprezentanti-comisie-sau-gl-general-tab-content.component.html"
})
export class ReprezentantiComisieSauGLGeneralTabContentComponent extends ReprezentantiComisieSauGLTabContent {

	private formBuilder: FormBuilder;
	public formGroup: FormGroup;
	private nomenclatorService: NomenclatorService;
	private messageDisplayer: MessageDisplayer;
	
	private messages: Message[];

	public dateFormat: string;
	public yearRange: string;

	public minDateForDataInceputMandat: Date;

	public constructor(nomenclatorService: NomenclatorService, messageDisplayer: MessageDisplayer, formBuilder: FormBuilder) {
		super();
		this.nomenclatorService = nomenclatorService;
		this.messageDisplayer = messageDisplayer;
		this.formBuilder = formBuilder;
		this.formGroup = this.formBuilder.group([]);
		this.dateFormat = DateConstants.DATE_FORMAT_FOR_TYPING;
		this.yearRange = DateUtils.getDefaultYearRangeForMetadata();
	}

	protected doWhenNgOnInit(): void {
		this.formGroup.addControl("comisieSauGL", new FormControl(null));
		this.formGroup.addControl("presedinte", new FormControl(null, [NomenclatorValidators.nomenclatorValueRequired()]));
		this.formGroup.addControl("dataInceputMandatPresedinte", new FormControl(null, [Validators.required]));
		this.formGroup.addControl("dataExpirareMandatPresedinte", new FormControl());
		this.formGroup.addControl("vicepresedinte1", new FormControl(null, [NomenclatorValidators.nomenclatorValueRequired()]));
		this.formGroup.addControl("dataInceputMandatVicepresedinte1", new FormControl(null, [Validators.required]));
		this.formGroup.addControl("dataExpirareMandatVicepresedinte1", new FormControl());
		this.formGroup.addControl("vicepresedinte2", new FormControl());
		this.formGroup.addControl("dataInceputMandatVicepresedinte2", new FormControl(null, [ReprezentantiComisiiSauGlValidators.dataInceputMandatVicepresedinteRequiredValidator({
			getVicepresedinteFieldValue: (): any => {
				return this.vicepresedinte2FormControl.value;
			}
		})]));
		this.formGroup.addControl("dataExpirareMandatVicepresedinte2", new FormControl());
		this.formGroup.addControl("vicepresedinte3", new FormControl());
		this.formGroup.addControl("dataInceputMandatVicepresedinte3", new FormControl(null, [ReprezentantiComisiiSauGlValidators.dataInceputMandatVicepresedinteRequiredValidator({
			getVicepresedinteFieldValue: (): any => {
				return this.vicepresedinte3FormControl.value;
			}
		})]));
		this.formGroup.addControl("dataExpirareMandatVicepresedinte3", new FormControl());
		this.formGroup.addControl("responsabilARB", new FormControl(null, [Validators.required]));
		this.formGroup.addControl("membruCDCoordonator", new FormControl(null, [NomenclatorValidators.nomenclatorValueRequired()]));
		this.formGroup.addControl("dataInceputMandatMembruCDCoordonator", new FormControl(null, [Validators.required]));
		this.formGroup.addControl("dataExpirareMandatMembruCDCoordonator", new FormControl());
	}

	private setValues(): void {
		let comisieSauGLFieldValue: ValueOfNomenclatorValueField = new ValueOfNomenclatorValueField(this.inputData.comisieSauGLNomenclatorId);
		comisieSauGLFieldValue.value = this.inputData.comisieSauGLId;
		let presedinteFieldValue: ValueOfNomenclatorValueField = new ValueOfNomenclatorValueField(this.inputData.persoaneNomenclatorId);	
		let vicepresedinte1FieldValue: ValueOfNomenclatorValueField = new ValueOfNomenclatorValueField(this.inputData.persoaneNomenclatorId);		
		let vicepresedinte2FieldValue: ValueOfNomenclatorValueField = new ValueOfNomenclatorValueField(this.inputData.persoaneNomenclatorId);	
		let vicepresedinte3FieldValue: ValueOfNomenclatorValueField = new ValueOfNomenclatorValueField(this.inputData.persoaneNomenclatorId);			
		let membruCDCoordonatorFieldValue: ValueOfNomenclatorValueField = new ValueOfNomenclatorValueField(this.inputData.membriCDNomenclatorId);			

		if (ObjectUtils.isNotNullOrUndefined(this.inputData.reprezentantiModel)) {
			presedinteFieldValue.value = this.inputData.reprezentantiModel.presedinteId;
			this.dataInceputMandatPresedinteFormControl.setValue(this.inputData.reprezentantiModel.dataInceputMandatPresedinte);
			this.dataExpirareMandatPresedinteFormControl.setValue(this.inputData.reprezentantiModel.dataExpirareMandatPresedinte);
			vicepresedinte1FieldValue.value = this.inputData.reprezentantiModel.vicepresedinte1Id;
			this.dataInceputMandatVicepresedinte1FormControl.setValue(this.inputData.reprezentantiModel.dataInceputMandatVicepresedinte1);
			this.dataExpirareMandatVicepresedinte1FormControl.setValue(this.inputData.reprezentantiModel.dataExpirareMandatVicepresedinte1);
			vicepresedinte2FieldValue.value = this.inputData.reprezentantiModel.vicepresedinte2Id;
			this.dataInceputMandatVicepresedinte2FormControl.setValue(this.inputData.reprezentantiModel.dataInceputMandatVicepresedinte2);
			this.dataExpirareMandatVicepresedinte2FormControl.setValue(this.inputData.reprezentantiModel.dataExpirareMandatVicepresedinte2);
			vicepresedinte3FieldValue.value = this.inputData.reprezentantiModel.vicepresedinte3Id;
			this.dataInceputMandatVicepresedinte3FormControl.setValue(this.inputData.reprezentantiModel.dataInceputMandatVicepresedinte3);
			this.dataExpirareMandatVicepresedinte3FormControl.setValue(this.inputData.reprezentantiModel.dataExpirareMandatVicepresedinte3);
			membruCDCoordonatorFieldValue.value = this.inputData.reprezentantiModel.membruCDCoordonatorId;
			this.dataInceputMandatMembruCDCoordonatorFormControl.setValue(this.inputData.reprezentantiModel.dataInceputMandatMembruCDCoordonator);
			this.dataExpirareMandatMembruCDCoordonatorFormControl.setValue(this.inputData.reprezentantiModel.dataExpirareMandatMembruCDCoordonator);
			this.responsabilARBFormControl.setValue("" + this.inputData.reprezentantiModel.responsabilARBId);
		}

		this.comisieSauGLFormControl.setValue(comisieSauGLFieldValue);
		this.presedinteFormControl.setValue(presedinteFieldValue);
		this.vicepresedinte1FormControl.setValue(vicepresedinte1FieldValue);
		this.vicepresedinte2FormControl.setValue(vicepresedinte2FieldValue);
		this.vicepresedinte3FormControl.setValue(vicepresedinte3FieldValue);
		this.membruCDCoordonatorFormControl.setValue(membruCDCoordonatorFieldValue);
		this.updateMandatoryFields();
	}

	private updateMandatoryFields():void {
		if (!this.inputData.isCategorieComisie) {
			this.comisieSauGLFormControl.clearValidators();
			this.comisieSauGLFormControl.updateValueAndValidity();
			this.presedinteFormControl.clearValidators();
			this.presedinteFormControl.updateValueAndValidity();
			this.dataInceputMandatPresedinteFormControl.clearValidators();
			this.dataInceputMandatPresedinteFormControl.updateValueAndValidity();
			this.dataExpirareMandatPresedinteFormControl.clearValidators();
			this.dataExpirareMandatPresedinteFormControl.updateValueAndValidity();
			this.vicepresedinte1FormControl.clearValidators();
			this.vicepresedinte1FormControl.updateValueAndValidity();
			this.dataInceputMandatVicepresedinte1FormControl.clearValidators();
			this.dataInceputMandatVicepresedinte1FormControl.updateValueAndValidity();
			this.dataExpirareMandatVicepresedinte1FormControl.clearValidators();
			this.dataExpirareMandatVicepresedinte1FormControl.clearValidators();
			this.membruCDCoordonatorFormControl.clearValidators();
			this.membruCDCoordonatorFormControl.updateValueAndValidity();
			this.dataInceputMandatMembruCDCoordonatorFormControl.clearValidators();
			this.dataInceputMandatMembruCDCoordonatorFormControl.updateValueAndValidity();
			this.dataExpirareMandatMembruCDCoordonatorFormControl.clearValidators();
			this.dataExpirareMandatMembruCDCoordonatorFormControl.updateValueAndValidity();
		}
	}

	protected prepareForViewOrEdit(): void {
		let dataInfiintariiComisieGL: Date = DateUtils.parseDateTimeFromStorage(this.inputData.comisieSauGL.attribute4);
		this.minDateForDataInceputMandat = DateUtils.addDaysToDate(dataInfiintariiComisieGL, 1);
		this.formGroup.reset();	
		this.setValues();
	}

	public isValid(): boolean {
		return this.validate();
	}

	private validate(): boolean {
		this.messages = [];
		FormUtils.validateAllFormFields(this.formGroup);
		let isValid: boolean = this.formGroup.valid;
		if (!isValid) {
			this.messages.push(Message.createForError("REQUIRED_FIELDS_NOT_COMPLETED_OR_VALUES_NOT_CORRECT"));
		}
		return isValid;
	}

	public populateModel(reprezentantiModel: ReprezentantiComisieSauGLModel): void {
		if (ObjectUtils.isNotNullOrUndefined(this.inputData.reprezentantiModel)) {
			reprezentantiModel.id = this.inputData.reprezentantiModel.id;
		}
		if (ObjectUtils.isNotNullOrUndefined((<ValueOfNomenclatorValueField> this.comisieSauGLFormControl.value).value)) {
			reprezentantiModel.comisieSauGLId = (<ValueOfNomenclatorValueField> this.comisieSauGLFormControl.value).value;
		} 
		if (ObjectUtils.isNotNullOrUndefined((<ValueOfNomenclatorValueField> this.presedinteFormControl.value).value)) {
			reprezentantiModel.presedinteId = (<ValueOfNomenclatorValueField> this.presedinteFormControl.value).value;
		}
		reprezentantiModel.dataInceputMandatPresedinte = this.dataInceputMandatPresedinteFormControl.value;
		reprezentantiModel.dataExpirareMandatPresedinte = this.dataExpirareMandatPresedinteFormControl.value;
		if (ObjectUtils.isNotNullOrUndefined((<ValueOfNomenclatorValueField> this.vicepresedinte1FormControl.value).value)) {
			reprezentantiModel.vicepresedinte1Id = (<ValueOfNomenclatorValueField> this.vicepresedinte1FormControl.value).value;
		}
		reprezentantiModel.dataInceputMandatVicepresedinte1 = this.dataInceputMandatVicepresedinte1FormControl.value;
		reprezentantiModel.dataExpirareMandatVicepresedinte1 = this.dataExpirareMandatVicepresedinte1FormControl.value;
		if (ObjectUtils.isNotNullOrUndefined((<ValueOfNomenclatorValueField> this.vicepresedinte2FormControl.value).value)) {
			reprezentantiModel.vicepresedinte2Id = (<ValueOfNomenclatorValueField> this.vicepresedinte2FormControl.value).value;
		}
		if (ObjectUtils.isNotNullOrUndefined(reprezentantiModel.vicepresedinte2Id)) {
			reprezentantiModel.dataInceputMandatVicepresedinte2 = this.dataInceputMandatVicepresedinte2FormControl.value;
			reprezentantiModel.dataExpirareMandatVicepresedinte2 = this.dataExpirareMandatVicepresedinte2FormControl.value;
		}		
		if (ObjectUtils.isNotNullOrUndefined((<ValueOfNomenclatorValueField> this.vicepresedinte3FormControl.value).value)) {
			reprezentantiModel.vicepresedinte3Id = (<ValueOfNomenclatorValueField> this.vicepresedinte3FormControl.value).value;
		}
		if (ObjectUtils.isNotNullOrUndefined(reprezentantiModel.vicepresedinte3Id)) {
			reprezentantiModel.dataInceputMandatVicepresedinte3 = this.dataInceputMandatVicepresedinte3FormControl.value;
			reprezentantiModel.dataExpirareMandatVicepresedinte3 = this.dataExpirareMandatVicepresedinte3FormControl.value;
		}		
		if (ObjectUtils.isNotNullOrUndefined(this.responsabilARBFormControl.value)) {
			reprezentantiModel.responsabilARBId = parseInt(this.responsabilARBFormControl.value, 0);
		}
		if (ObjectUtils.isNotNullOrUndefined((<ValueOfNomenclatorValueField> this.membruCDCoordonatorFormControl.value).value)) {
			reprezentantiModel.membruCDCoordonatorId = (<ValueOfNomenclatorValueField> this.membruCDCoordonatorFormControl.value).value;
		}
		reprezentantiModel.dataInceputMandatMembruCDCoordonator = this.dataInceputMandatMembruCDCoordonatorFormControl.value;
		reprezentantiModel.dataExpirareMandatMembruCDCoordonator = this.dataExpirareMandatMembruCDCoordonatorFormControl.value;
	}

	public onDataInceputMandatPresedinteChanged(event: any): void {
		this.updateDataExpirareMandatPresedinte();
	}

	public onDataInceputMandatPresedinteClearClicked(event: any): void {
		this.updateDataExpirareMandatPresedinte();
	}

	private updateDataExpirareMandatPresedinte(): void {
		// tslint:disable-next-line:max-line-length
		this.calculateAndUpdateDataExpirareMandat(this.dataInceputMandatPresedinteFormControl, this.dataExpirareMandatPresedinteFormControl, this.inputData.nrAniValabilitateMandatPresedinteVicepresedinte);
	}

	public onDataInceputMandatVicepresedinte1Changed(event: any): void {
		this.updateDataExpirareMandatVicepresedinte1();
	}

	public onDataInceputMandatVicepresedinte1ClearClicked(event: any): void {
		this.updateDataExpirareMandatVicepresedinte1();
	}

	private updateDataExpirareMandatVicepresedinte1(): void {
		// tslint:disable-next-line:max-line-length
		this.calculateAndUpdateDataExpirareMandat(this.dataInceputMandatVicepresedinte1FormControl, this.dataExpirareMandatVicepresedinte1FormControl, this.inputData.nrAniValabilitateMandatPresedinteVicepresedinte);
	}

	public onVicepresedinte2ValueChanged(event: any): void {
		this.dataInceputMandatVicepresedinte2FormControl.updateValueAndValidity();
	}

	public onDataInceputMandatVicepresedinte2Changed(event: any): void {
		this.updateDataExpirareMandatVicepresedinte2();
	}

	public onDataInceputMandatVicepresedinte2ClearClicked(event: any): void {
		this.updateDataExpirareMandatVicepresedinte2();
	}

	private updateDataExpirareMandatVicepresedinte2(): void {
		// tslint:disable-next-line:max-line-length
		this.calculateAndUpdateDataExpirareMandat(this.dataInceputMandatVicepresedinte2FormControl, this.dataExpirareMandatVicepresedinte2FormControl, this.inputData.nrAniValabilitateMandatPresedinteVicepresedinte);
	}

	private calculateAndUpdateDataExpirareMandat(dataInceputFormControl: FormControl, dataExpirareFormControl: FormControl, nrAniValabilitateMandat: number): void {
		let dataInceput: Date = dataInceputFormControl.value;
		if (ObjectUtils.isNullOrUndefined(dataInceput)) {
			dataExpirareFormControl.reset();
		} else {
			let dataExpirare: Date = DateUtils.addYearsToDate(dataInceput, nrAniValabilitateMandat);
			dataExpirareFormControl.setValue(dataExpirare);
		}
		dataExpirareFormControl.updateValueAndValidity();
	}

	public onVicepresedinte3ValueChanged(event: any): void {
		this.dataInceputMandatVicepresedinte3FormControl.updateValueAndValidity();
	}

	public onDataInceputMandatVicepresedinte3Changed(event: any): void {
		this.updateDataExpirareMandatVicepresedinte3();
	}

	public onDataInceputMandatVicepresedinte3ClearClicked(event: any): void {
		this.updateDataExpirareMandatVicepresedinte3();
	}

	private updateDataExpirareMandatVicepresedinte3(): void {
		// tslint:disable-next-line:max-line-length
		this.calculateAndUpdateDataExpirareMandat(this.dataInceputMandatVicepresedinte3FormControl, this.dataExpirareMandatVicepresedinte3FormControl, this.inputData.nrAniValabilitateMandatPresedinteVicepresedinte);
	}

	public onDataInceputMandatMembruCDCoordonatorChanged(event: any): void {
		this.updateDataExpirareMandatMembruCDCoordonator();
	}

	public onDataInceputMandatMembruCDCoordonatorClearClicked(event: any): void {
		this.updateDataExpirareMandatMembruCDCoordonator();
	}

	public updateDataExpirareMandatMembruCDCoordonator(): void {
		// tslint:disable-next-line:max-line-length
		this.calculateAndUpdateDataExpirareMandat(this.dataInceputMandatMembruCDCoordonatorFormControl, this.dataExpirareMandatMembruCDCoordonatorFormControl, this.inputData.nrAniValabilitateMandatMembruCdCoordonator);
	}

	public get dataInceputMandatVicepresedinte2Required(): boolean {
		return NomenclatorUtils.fieldValueHasValue(this.vicepresedinte2FormControl.value);
	}

	public get dataInceputMandatVicepresedinte3Required(): boolean {
		return NomenclatorUtils.fieldValueHasValue(this.vicepresedinte3FormControl.value);
	}

	public getMessages(): Message[] {
		return this.messages;
	}

	public get comisieSauGLFormControl(): FormControl {
		return this.getFormControlByName("comisieSauGL"); 
	}

	public get presedinteFormControl(): FormControl {
		return this.getFormControlByName("presedinte"); 
	}
	
	public get dataInceputMandatPresedinteFormControl(): FormControl {
		return this.getFormControlByName("dataInceputMandatPresedinte"); 
	}

	public get dataExpirareMandatPresedinteFormControl(): FormControl { 
		return this.getFormControlByName("dataExpirareMandatPresedinte"); 
	}

	public get vicepresedinte1FormControl(): FormControl {
		return this.getFormControlByName("vicepresedinte1"); 
	}
	
	public get dataInceputMandatVicepresedinte1FormControl(): FormControl { 
		return this.getFormControlByName("dataInceputMandatVicepresedinte1"); 
	}

	public get dataExpirareMandatVicepresedinte1FormControl(): FormControl { 
		return this.getFormControlByName("dataExpirareMandatVicepresedinte1"); 
	}

	public get vicepresedinte2FormControl(): FormControl {
		return this.getFormControlByName("vicepresedinte2"); 
	}
	
	public get dataInceputMandatVicepresedinte2FormControl(): FormControl {
		return this.getFormControlByName("dataInceputMandatVicepresedinte2"); 
	}

	public get dataExpirareMandatVicepresedinte2FormControl(): FormControl {
		return this.getFormControlByName("dataExpirareMandatVicepresedinte2"); 
	}

	public get vicepresedinte3FormControl(): FormControl {
		return this.getFormControlByName("vicepresedinte3"); 
	}
	
	public get dataInceputMandatVicepresedinte3FormControl(): FormControl {
		return this.getFormControlByName("dataInceputMandatVicepresedinte3"); 
	}

	public get dataExpirareMandatVicepresedinte3FormControl(): FormControl {
		return this.getFormControlByName("dataExpirareMandatVicepresedinte3"); 
	}

	public get responsabilARBFormControl(): FormControl {
		return this.getFormControlByName("responsabilARB"); 
	}

	public get membruCDCoordonatorFormControl(): FormControl {
		return this.getFormControlByName("membruCDCoordonator"); 
	}

	public get dataInceputMandatMembruCDCoordonatorFormControl(): FormControl {
		return this.getFormControlByName("dataInceputMandatMembruCDCoordonator"); 
	}

	public get dataExpirareMandatMembruCDCoordonatorFormControl(): FormControl { 
		return this.getFormControlByName("dataExpirareMandatMembruCDCoordonator"); 
	}
	
	private getFormControlByName(controlName: string): FormControl {
		return <FormControl> this.formGroup.get(controlName);
	}
	
}