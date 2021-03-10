import { Component, OnInit, Output, EventEmitter, Input } from "@angular/core";
import { FormGroup, FormBuilder, AbstractControl, Validators } from "@angular/forms";
import { NomenclatorValidators, NomenclatorService, ObjectUtils, ValueOfNomenclatorValueField, NomenclatorConstants, NomenclatorFilter, NomenclatorMultipleFilter, BaseWindow, AdminPermissionEnum, TranslateUtils, RegistruIntrariModel } from "@app/shared";
import { MessageDisplayer, StringValidators, DateConstants, DateUtils, ArrayUtils, RegistruIesiriDestinatariModel, FormUtils, StringUtils } from "@app/shared";
import { RegistruIntrariFieldInputData } from "../../registru-intrari/registru-intrari-field/registru-intrari-field.component";
import { AuthManager } from "@app/shared/auth";

@Component({
	selector: "app-registru-iesiri-destinatar-window",
	templateUrl: "./registru-iesiri-destinatar-window.component.html"
})
export class RegistruIesiriDestinatarWindowComponent extends BaseWindow implements OnInit {

	@Input()
	public mode: "add" | "edit" | "view";

	@Input()
	public destinatar: RegistruIesiriDestinatariModel;

	@Input()
	public registruIesiriId: number;

	@Input()
	public nomenclatorIdByCode: Map<string, number>;

	@Output()
	public windowClosed: EventEmitter<void>;

	@Input()
	public inputData: RegistruIesiriDestinatarWindowInputData;

	@Output()
	public dataSaved: EventEmitter<RegistruIesiriDestinatariModel>;

	private nomenclatorService: NomenclatorService;
	private formBuilder: FormBuilder;
	private translateUtils: TranslateUtils;
	private messageDisplayer: MessageDisplayer;
	private authManager: AuthManager;

	public visible: boolean;

	public dateFormat: string;
	public yearRange: string;

	public formGroup: FormGroup;

	public registruIntrariFieldInputData: RegistruIntrariFieldInputData;

	public saveActionEnabled: boolean = false;

	public customFilters: NomenclatorFilter[] = [];

	public constructor(nomenclatorService: NomenclatorService, formBuilder: FormBuilder, messageDisplayer: MessageDisplayer, authManager: AuthManager, translateUtils: TranslateUtils) {
		super();
		this.nomenclatorService = nomenclatorService;
		this.formBuilder = formBuilder;
		this.messageDisplayer = messageDisplayer;
		this.translateUtils = translateUtils;
		this.windowClosed = new EventEmitter<void>();
		this.dataSaved = new EventEmitter<RegistruIesiriDestinatariModel>();
		this.authManager = authManager;
	}
	
	private init(): void {
		this.visible = true;
		this.saveActionEnabled = (this.isAddMode() || this.isEditMode());
		
		this.dateFormat = DateConstants.DATE_FORMAT_FOR_TYPING;
		this.yearRange = DateUtils.getDefaultYearRange();
		
		this.prepareForm();
	}

	private prepareForm(): void {
		this.formGroup = this.formBuilder.group({
			destinatarFromNomenclator: [null, NomenclatorValidators.nomenclatorValueRequired()],
			numeDestinatar: [null, [StringValidators.blank, Validators.required]],
			departament: [null],
			nrInregistrare: [null],
			dataInregistrare: [null],
			comisie: [null],
			observatii: [null],
			intrare: [null, Validators.required]
		});
	}
	
	public ngOnInit(): void {	
		this.init();	
		if (this.isEditMode()) {
			this.prepareForEdit();
		} else if (this.isViewMode()) {
			this.prepareForView();
		} else {
			this.prepareForAdd();
		}
		this.prepareNomenclatorValuesForNomenclatorValueSelectors();
	}
	
	private prepareForEdit(): void {
		if (ObjectUtils.isNullOrUndefined(this.destinatar)) {
			throw new Error("Input property [destinatar] cannot be null or undefined on edit mode.");
		}
		this.preparePerspectiveForEditMode();
	}
	
	private prepareForView(): void {
		if (ObjectUtils.isNullOrUndefined(this.destinatar)) {
			throw new Error("Input property [destinatar] cannot be null or undefined on edit mode.");
		}
		this.preparePerspectiveForViewMode();
	}
	
	private prepareForAdd():void {
		this.destinatar = null;
		this.intrareFormControl.disable();
	}

	public prepareNomenclatorValueSelectors(): void {	
		if (ObjectUtils.isNotNullOrUndefined(this.nomenclatorIdByCode)) {
			this.destinatarFromNomenclatorFormControl.setValue(new ValueOfNomenclatorValueField(this.nomenclatorIdByCode.get(NomenclatorConstants.NOMENCLATOR_CODE_INSTITUTII)));
			this.comisieFormControl.setValue(new ValueOfNomenclatorValueField(this.nomenclatorIdByCode.get(NomenclatorConstants.NOMENCLATOR_CODE_COMISII_SAU_GL)));
		}
	}
		
	private prepareNomenclatorValuesForNomenclatorValueSelectors(): void {
		let customFilter: NomenclatorMultipleFilter= new NomenclatorMultipleFilter();
		customFilter.attributeKey = NomenclatorConstants.INSTITUTII_ATTRIBUTE_KEY_TIP_RADIAT;
		customFilter.values = [false, null];
		this.customFilters.push(customFilter);
		this.prepareNomenclatorValueSelectors();
		this.updateFieldsValues();
	}
	
	private preparePerspectiveForViewMode(): void {
		FormUtils.disableAllFormFields(this.formGroup);
	}

	private preparePerspectiveForEditMode(): void {
		if (ObjectUtils.isNotNullOrUndefined(this.inputData.registruIesiriId)){
			this.intrareFormControl.disable();
			this.destinatarFromNomenclatorFormControl.disable();
			this.numeDestinatarFormControl.disable();
			if (!this.authManager.hasPermission(AdminPermissionEnum.EDIT_REGISTRU_INTRARI_IESIRI_ELEVATED)) {
				this.departamentFormControl.disable();
				this.comisieFormControl.disable();
			}else{
				if (!this.inputData.hasMappedDestinatar){
					if (ObjectUtils.isNullOrUndefined(this.destinatar.destinatarExistentId)) {
						this.numeDestinatarFormControl.enable();
					}else {
						this.destinatarFromNomenclatorFormControl.enable();
					}
				}
			}
		}else{
			if (ObjectUtils.isNotNullOrUndefined(this.destinatar.destinatarExistentId)) {
				this.numeDestinatarFormControl.disable();
			}else {
				this.destinatarFromNomenclatorFormControl.disable();
			}
		}
		
	}

	private updateFieldsValues(): void {
		this.registruIntrariFieldInputData = new RegistruIntrariFieldInputData();
		if (ObjectUtils.isNotNullOrUndefined(this.destinatar)){
			if (ObjectUtils.isNotNullOrUndefined(this.destinatar.destinatarExistentId)) {
				this.destinatarFromNomenclatorFormControl.value.value = this.destinatar.destinatarExistentId;
				this.destinatarFromNomenclatorFormControl.setValue(this.destinatarFromNomenclatorFormControl.value);
			}
			if (ObjectUtils.isNotNullOrUndefined(this.destinatar.comisieGlId)) {
				this.comisieFormControl.value.value = this.destinatar.comisieGlId;
				this.comisieFormControl.setValue(this.comisieFormControl.value);
			}
			this.numeDestinatarFormControl.setValue(this.destinatar.nume);
			this.departamentFormControl.setValue(this.destinatar.departament);
			this.nrInregistrareFormControl.setValue(this.destinatar.numarInregistrare);
			this.dataInregistrareFormControl.setValue(this.destinatar.dataInregistrare);
			this.intrareFormControl.setValue(this.destinatar.registruIntrariId);
			this.observatiiFormControl.setValue(this.destinatar.observatii);	
			this.registruIntrariFieldInputData.destinatarId = this.destinatar.destinatarExistentId;
			this.registruIntrariFieldInputData.numeDestinatar = this.destinatar.nume;
		}
		if (ObjectUtils.isNotNullOrUndefined(this.inputData)) { 
			this.registruIntrariFieldInputData.tipDocumentId = this.inputData.tipDocumentId;
			this.registruIntrariFieldInputData.registruIesiriId = this.inputData.registruIesiriId;
		}
		this.registruIntrariFieldInputData.isIntrariForIesiri = true;
		this.updateDisabledNrInregistrareForm();
	}

	public onInRegisterEntryNumberChanged(registruIntrari: RegistruIntrariModel):  void {
		if (this.inputData.registruIesiriWindowPerspective === "add"){
			this.observatiiFormControl.setValue(registruIntrari.observatii);
			this.departamentFormControl.setValue(registruIntrari.departamentEmitent);
			this.dataInregistrareFormControl.setValue(registruIntrari.dataDocumentEmitent);
			this.nrInregistrareFormControl.setValue(registruIntrari.numarDocumentEmitent);
			this.departamentFormControl.updateValueAndValidity();
			this.observatiiFormControl.updateValueAndValidity();
			this.departamentFormControl.updateValueAndValidity();
			this.nrInregistrareFormControl.updateValueAndValidity();
			this.messageDisplayer.displayInfo("OUT_REGISTER_ENTRY_FIELDS_UPDATED_FROM_IN_REGISTER_ENTRY");
		}
		if (registruIntrari.comisieSauGLIds != null){
			(<ValueOfNomenclatorValueField>this.comisieFormControl.value).value = registruIntrari.comisieSauGLIds[0];
			this.comisieFormControl.setValue(this.comisieFormControl.value);
		}
	}

	public onSave(): void {
		if (!this.isFormValid()) {
			return;
		}

		let destinatar: RegistruIesiriDestinatariModel = new RegistruIesiriDestinatariModel();

		if (this.isEditMode()) {
			destinatar.id = this.destinatar.id;			
		} else {
			destinatar.id = null;
		}
		destinatar.departament = this.departamentFormControl.value;
		destinatar.numarInregistrare = this.nrInregistrareFormControl.value;
		destinatar.dataInregistrare = this.dataInregistrareFormControl.value;
		destinatar.observatii = this.observatiiFormControl.value;
		if (ObjectUtils.isNotNullOrUndefined(this.comisieFormControl.value)  && ObjectUtils.isNotNullOrUndefined((<ValueOfNomenclatorValueField>this.comisieFormControl.value).value)) {
			destinatar.comisieGlId = (<ValueOfNomenclatorValueField>this.comisieFormControl.value).value;
		} else {
			destinatar.comisieGlId = null;
		}

		if (ObjectUtils.isNotNullOrUndefined(this.registruIesiriId)) {
			destinatar.registruIesiriId = Number(this.registruIesiriId);
		}

		destinatar.registruIntrariId = this.intrareFormControl.value;

		if (StringUtils.isNotBlank(this.numeDestinatarFormControl.value)) {
			destinatar.nume = this.numeDestinatarFormControl.value;
			this.dataSaved.emit(destinatar);
			this.windowClosed.emit();
		} else {
			destinatar.destinatarExistentId = this.destinatarFromNomenclatorFormControl.value.value;
			this.dataSaved.emit(destinatar);
			this.windowClosed.emit();
		}
		
	}
	
	public isFormValid(): boolean {
		FormUtils.validateAllFormFields(this.formGroup);
		return this.formGroup.valid;
	}

	public onDestinatarFromNomenclatorChanged(event: any): void {	
		this.registruIntrariFieldInputData.destinatarId = this.destinatarFromNomenclatorFormControl.value.value;
		this.registruIntrariFieldInputData.numeDestinatar = null;		
		if (this.destinatarFromNomenclatorValueExists()) {
			this.numeDestinatarFormControl.disable();
		} else {
			this.numeDestinatarFormControl.enable();
		}
		this.updateDisabledNrInregistrareForm();
	}

	public onNumeDestinatarValueChanged(): void {
		this.registruIntrariFieldInputData.destinatarId = null;
		this.registruIntrariFieldInputData.numeDestinatar = this.numeDestinatarFormControl.value;
		if (StringUtils.isNotBlank(this.numeDestinatarFormControl.value)) {
			this.destinatarFromNomenclatorFormControl.disable();
		} else {
			this.destinatarFromNomenclatorFormControl.enable();
		}
		this.updateDisabledNrInregistrareForm();
	}

	private updateDisabledNrInregistrareForm(): void {
		let isDestinatarNou = ObjectUtils.isNotNullOrUndefined(this.registruIntrariFieldInputData.numeDestinatar);
		if(isDestinatarNou && StringUtils.isBlank(this.registruIntrariFieldInputData.numeDestinatar)) {
			isDestinatarNou = false;
		}
		let isDestinatarExistent = ObjectUtils.isNotNullOrUndefined(this.registruIntrariFieldInputData.destinatarId);
		let isTipDocumentRaspuns = ObjectUtils.isNotNullOrUndefined(this.inputData.codDocumentEchivalent);
		let isRegistruEditMode = ObjectUtils.isNotNullOrUndefined(this.inputData.registruIesiriId);
		if (isTipDocumentRaspuns && (isDestinatarExistent || isDestinatarNou) && !isRegistruEditMode) {
			this.intrareFormControl.enable();
		} else {			
			this.intrareFormControl.disable();
		}
	}

	private destinatarFromNomenclatorValueExists(): boolean {
		let destinatarFromNomenclatorValue: ValueOfNomenclatorValueField = this.destinatarFromNomenclatorFormControl.value;
		return ArrayUtils.isNotEmpty(destinatarFromNomenclatorValue.values);
	}
	
	public getHeaderTitle(): string {
		let title: string = "";
		let action: string;
		if (this.isAddMode()){
			action = "ADD";
		} else if (this.isEditMode()){
			action = "EDIT";
		} else {
			action = "VIEW";
		}
		title += this.translateUtils.translateLabel(action)+" "+this.translateUtils.translateLabel("REGISTRU_IESIRI_DESTINATAR");
		if (this.inputData.registruIesiriRegistryNumber){
			title += " - "+this.translateUtils.translateLabel("REGISTRU_IESIRI_NR_INREGISTRARE")+": "+this.inputData.registruIesiriRegistryNumber;
		}
		return title;
	}

	private isAddMode(): boolean {
		return this.mode === "add";
	}

	private isEditMode(): boolean {
		return this.mode === "edit";
	}

	public isViewMode(): boolean {
		return this.mode === "view";
	}

	private getControlByName(name: string): AbstractControl {
		return this.formGroup.controls[name];
	}

	public get destinatarFromNomenclatorFormControl(): AbstractControl {
		return this.getControlByName("destinatarFromNomenclator");
	}

	public get numeDestinatarFormControl(): AbstractControl {
		return this.getControlByName("numeDestinatar");
	}

	public get departamentFormControl(): AbstractControl {
		return this.getControlByName("departament");
	}

	public get nrInregistrareFormControl(): AbstractControl {
		return this.getControlByName("nrInregistrare");
	}

	public get dataInregistrareFormControl(): AbstractControl {
		return this.getControlByName("dataInregistrare");
	}

	public get comisieFormControl(): AbstractControl {
		return this.getControlByName("comisie");
	}

	public get observatiiFormControl(): AbstractControl {
		return this.getControlByName("observatii");
	}

	public get intrareFormControl(): AbstractControl {
		return this.getControlByName("intrare");
	}

	public onHide($event): void {
		this.windowClosed.emit();
	}
}

export class RegistruIesiriDestinatarWindowInputData {
	public tipDocumentId: number;	
	public codDocumentEchivalent: string;
	public registruIesiriId: number;
	public hasMappedDestinatar: boolean;
	public registruIesiriRegistryNumber: string;
	public registruIesiriWindowPerspective: "add" | "view" | "edit";
}
