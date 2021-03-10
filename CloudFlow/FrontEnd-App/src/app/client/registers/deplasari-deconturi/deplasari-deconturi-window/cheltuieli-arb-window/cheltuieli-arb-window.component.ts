import { Component, OnInit, Input, Output, EventEmitter } from "@angular/core";
import { CheltuialaArbModel, TipDocumentJustificativForCheltuieliArbEnum, ValutaForCheltuieliArbEnum, ModalitatePlataForDecontEnum } from "@app/shared/model/deplasari-deconturi";
import { FormBuilder, FormGroup, FormControl, Validators, AbstractControl, ValidatorFn } from "@angular/forms";
import { DateConstants, DateUtils, UiUtils, ObjectUtils, FormUtils, StringValidators, TranslateUtils, CursValutarService, CursValutarModel, AppError, MessageDisplayer, BaseWindow} from "@app/shared";
import { SelectItem, Dialog } from "primeng/primeng";
import { DeplasariDeconturiWindowComponent } from "../deplasari-deconturi-window.component";
import { ListItemUtils } from "@app/shared/utils/list-item-utils";

@Component({
	selector: "app-cheltuieli-arb-window",
	templateUrl: "./cheltuieli-arb-window.component.html"
})
export class CheltuieliArbWindowComponent extends BaseWindow implements OnInit {

	public static readonly CURS_VALUTAR_WHEN_VALUTA_RON_SELECTED: number = 0;

	@Input()
	public dataDecont: Date;

	@Input()
	public mode: "add" | "edit" | "view";

	@Input()
	public cheltuiala: CheltuialaArbModel;

	@Output()
	public windowClosed: EventEmitter<void>;

	@Output()
	public dataSaved: EventEmitter<CheltuialaArbModel>;

	private cursValutarService: CursValutarService;
	private formBuilder: FormBuilder;
	private translateUtils: TranslateUtils;
	private messageDisplayer: MessageDisplayer;

	public visible: boolean;

	public dateFormat: string;
	public yearRange: string;

	public formGroup: FormGroup;

	public tipDocumentJustificativItems: SelectItem[];
	public valutaItems: SelectItem[];
	public modalitatePlataItems: SelectItem[];

	public saveButtonVisible: boolean = true;

	public valoareMinimaCheltuiala: number = 1;
	public validationMessageMinLengthParameter = {value: this.valoareMinimaCheltuiala};

	private cursValutar: CursValutarModel;
	
	public mandatoryVisible: boolean;

	public constructor(cursValutarService: CursValutarService, formBuilder: FormBuilder, translateUtils: TranslateUtils,
			messageDisplayer: MessageDisplayer) {
		super();
		this.cursValutarService = cursValutarService;
		this.formBuilder = formBuilder;
		this.translateUtils = translateUtils;
		this.messageDisplayer = messageDisplayer;

		this.windowClosed = new EventEmitter<void>();
		this.dataSaved = new EventEmitter<CheltuialaArbModel>();
		this.init();
	}
	
	private init(): void {
		this.visible = false;
		this.tipDocumentJustificativItems = [];
		this.valutaItems = [];
		
		this.dateFormat = DateConstants.DATE_FORMAT_FOR_TYPING;
		this.yearRange = DateUtils.getDefaultYearRange();
		
		this.prepareForm();
		this.prepareTipDocumentJustificativItems();
		this.prepareValutaItems();
		this.loadCursValutar();
		this.prepareModalitatePlataItems();
	}

	private prepareForm(): void {
		this.formGroup = this.formBuilder.group([]);
		this.formGroup.addControl("valuta", new FormControl(null, [Validators.required]));
		this.formGroup.addControl("cursValutar", new FormControl(null, [Validators.required]));
		this.formGroup.addControl("modalitatePlata", new FormControl(null, [Validators.required]));
		this.formGroup.addControl("numarDocumentJustificativ", new FormControl(null, []));
		this.formGroup.addControl("dataDocumentJustificativ", new FormControl(null, []));
		this.formGroup.addControl("tipDocumentJustificativ", new FormControl(null, [Validators.required]));
		this.formGroup.addControl("valoareCheltuiala", new FormControl(null, [Validators.required, Validators.min(this.valoareMinimaCheltuiala)]));
		this.formGroup.addControl("explicatie", new FormControl(null, []));
	}

	private prepareTipDocumentJustificativItems(): void {
		this.tipDocumentJustificativItems = [
			{label: this.translateUtils.translateLabel(TipDocumentJustificativForCheltuieliArbEnum.CAZARE), value: TipDocumentJustificativForCheltuieliArbEnum.CAZARE},
			{label: this.translateUtils.translateLabel(TipDocumentJustificativForCheltuieliArbEnum.BILET_DE_AVION), value: TipDocumentJustificativForCheltuieliArbEnum.BILET_DE_AVION},
			{label: this.translateUtils.translateLabel(TipDocumentJustificativForCheltuieliArbEnum.ASIGURARE_MEDICALA), value: TipDocumentJustificativForCheltuieliArbEnum.ASIGURARE_MEDICALA},
			{label: this.translateUtils.translateLabel(TipDocumentJustificativForCheltuieliArbEnum.TRANSFER_AEROPORT), value: TipDocumentJustificativForCheltuieliArbEnum.TRANSFER_AEROPORT},
			{label: this.translateUtils.translateLabel(TipDocumentJustificativForCheltuieliArbEnum.ALTE_CHELTUIELI), value: TipDocumentJustificativForCheltuieliArbEnum.ALTE_CHELTUIELI}
		];

		ListItemUtils.sortByLabel(this.tipDocumentJustificativItems);
	}

	private prepareValutaItems(): void {
		this.valutaItems = [
			{label: this.translateUtils.translateLabel(ValutaForCheltuieliArbEnum.RON), value: ValutaForCheltuieliArbEnum.RON},
			{label: this.translateUtils.translateLabel(ValutaForCheltuieliArbEnum.EUR), value: ValutaForCheltuieliArbEnum.EUR},
			{label: this.translateUtils.translateLabel(ValutaForCheltuieliArbEnum.USD), value: ValutaForCheltuieliArbEnum.USD}
		];
		
		ListItemUtils.sortByLabel(this.valutaItems);
	}

	private prepareModalitatePlataItems(): void {
		this.modalitatePlataItems = [
			{label: this.translateUtils.translateLabel(ModalitatePlataForDecontEnum.CARD), value: ModalitatePlataForDecontEnum.CARD},
			{label: this.translateUtils.translateLabel(ModalitatePlataForDecontEnum.NUMERAR), value: ModalitatePlataForDecontEnum.NUMERAR}
		];
		
		ListItemUtils.sortByLabel(this.modalitatePlataItems);
	}
	
	private loadCursValutar(): void {
		
		this.cursValutarService.getCursValutarCurent({
			onSuccess: (cursValutar: CursValutarModel): void => {
				this.cursValutar = cursValutar;

				if (this.isEditMode()) {
					this.setCursValutar(this.cheltuiala.valuta);
				}

				this.visible = true;
			},
			onFailure: (appError: AppError): void => {
				this.messageDisplayer.displayAppError(appError);
				this.visible = true;
			}
		});
	}

	public ngOnInit(): void {
		this.mandatoryVisible = true;
		if (this.isEditMode()) {
			this.prepareForEdit();
		} else if (this.isViewMode()) {
			this.prepareForView();
		}
	}

	private prepareForEdit(): void {
		if (ObjectUtils.isNullOrUndefined(this.cheltuiala)) {
			throw new Error("Input property [cheltuiala] cannot be null or undefined on edit mode.");
		}
		this.prepareFormFromCheltuiala();
	}

	private prepareForView(): void {
		if (ObjectUtils.isNullOrUndefined(this.cheltuiala)) {
			throw new Error("Input property [cheltuiala] cannot be null or undefined on edit mode.");
		}
		this.preparePerspectiveForViewMode();
		this.prepareFormFromCheltuiala();
	}

	private preparePerspectiveForViewMode(): void {
		FormUtils.disableAllFormFields(this.formGroup);
		this.saveButtonVisible = false;
		this.mandatoryVisible = false;
	}

	private prepareFormFromCheltuiala(): void {
		this.valutaFormControl.setValue(this.cheltuiala.valuta);

		if (!this.isEditMode()) {
			this.cursValutarFormControl.setValue(this.cheltuiala.cursValutar);
		}

		this.modalitatePlataFormControl.setValue(this.cheltuiala.modalitatePlata);
		this.tipDocumentJustificativFormControl.setValue(this.cheltuiala.tipDocumentJustificativ);
		this.explicatieFormControl.setValue(this.cheltuiala.explicatie);
		this.numarDocumentJustificativFormControl.setValue(this.cheltuiala.numarDocumentJustificativ);
		this.dataDocumentJustificativFormControl.setValue(this.cheltuiala.dataDocumentJustificativ);
		this.valoareCheltuialaFormControl.setValue(this.cheltuiala.valoareCheltuiala);
	}

	public onSave(): void {
		if (!this.isFormValid()) {
			return;
		}

		let cheltuiala: CheltuialaArbModel = new CheltuialaArbModel();

		if (this.isEditMode()) {
			cheltuiala.id = this.cheltuiala.id;
		} else {
			cheltuiala.id = null;
		}
		
		cheltuiala.valuta = this.valutaFormControl.value;
		cheltuiala.cursValutar = this.cursValutarFormControl.value;
		cheltuiala.modalitatePlata = this.modalitatePlataFormControl.value;
		cheltuiala.tipDocumentJustificativ = this.tipDocumentJustificativFormControl.value;
		cheltuiala.explicatie = this.explicatieFormControl.value;
		cheltuiala.numarDocumentJustificativ = this.numarDocumentJustificativFormControl.value;
		cheltuiala.dataDocumentJustificativ = this.dataDocumentJustificativFormControl.value;
		cheltuiala.valoareCheltuiala = this.valoareCheltuialaFormControl.value;

		this.dataSaved.emit(cheltuiala);
		this.windowClosed.emit();
	}
	
	public isFormValid(): boolean {
		FormUtils.validateAllFormFields(this.formGroup);
		return this.formGroup.valid;
	}

	private isEditMode(): boolean {
		return this.mode === "edit";
	}

	public isViewMode(): boolean {
		return this.mode === "view";
	}

	public onValutaChanged(event: any): void {
		let valuta: string = this.valutaFormControl.value;
		this.setCursValutar(valuta);
	}

	private setCursValutar(valuta: string): void {
		if (valuta === ValutaForCheltuieliArbEnum.RON) {
			this.cursValutarFormControl.setValue(CheltuieliArbWindowComponent.CURS_VALUTAR_WHEN_VALUTA_RON_SELECTED);
		} else if (valuta === ValutaForCheltuieliArbEnum.EUR) {
			this.cursValutarFormControl.setValue(this.cursValutar.eur);
		} else if (valuta === ValutaForCheltuieliArbEnum.USD) {
			this.cursValutarFormControl.setValue(this.cursValutar.usd);
		}
	}
	
	public onTipDocumentJustificativValueChanged(event: any): void {
		if (this.isExplicatieCampObligatoriu()) {
			this.explicatieFormControl.setValidators([Validators.required, StringValidators.blank]);
			this.explicatieFormControl.updateValueAndValidity();
		} else {
			this.explicatieFormControl.clearValidators();
			this.explicatieFormControl.updateValueAndValidity();
		}
	}

	public isExplicatieCampObligatoriu(): boolean {
		return this.tipDocumentJustificativFormControl.value === TipDocumentJustificativForCheltuieliArbEnum.ALTE_CHELTUIELI;
	} 

	private getControlByName(name: string): AbstractControl {
		return this.formGroup.controls[name];
	}

	public get valutaFormControl(): AbstractControl {
		return this.getControlByName("valuta");
	}

	public get cursValutarFormControl(): AbstractControl {
		return this.getControlByName("cursValutar");
	}

	public get modalitatePlataFormControl(): AbstractControl {
		return this.getControlByName("modalitatePlata");
	}

	public get numarDocumentJustificativFormControl(): AbstractControl {
		return this.getControlByName("numarDocumentJustificativ");
	}

	public get dataDocumentJustificativFormControl(): AbstractControl {
		return this.getControlByName("dataDocumentJustificativ");
	}

	public get tipDocumentJustificativFormControl(): AbstractControl {
		return this.getControlByName("tipDocumentJustificativ");
	}

	public get valoareCheltuialaFormControl(): AbstractControl {
		return this.getControlByName("valoareCheltuiala");
	}

	public get explicatieFormControl(): AbstractControl {
		return this.getControlByName("explicatie");
	}

	public onHide($event): void {
		this.windowClosed.emit();
	}

	public onCancel($event): void {
		this.windowClosed.emit();
	}

	public onDataDocumentJustificativSelected(): void{
		let cheltuieliArbDataDecont: Date = this.dataDecont;
		let dataDocumentJustificativ: Date = this.dataDocumentJustificativFormControl.value;

		this.dataDocumentJustificativFormControl.setValidators([CheltuieliArbValidators.dataDecontGreaterThanDataDocumentJustificativ(cheltuieliArbDataDecont, dataDocumentJustificativ)]); 
		this.dataDocumentJustificativFormControl.updateValueAndValidity();
	}
}

class CheltuieliArbValidators {
	public static dataDecontGreaterThanDataDocumentJustificativ(dataDecont: Date, dataDocumentJustificativ: Date): ValidatorFn | null {
		return (control: AbstractControl): {[key: string]: any} => {
			if (ObjectUtils.isNullOrUndefined(dataDecont)) {
                return { dataDocumentJustificativRequired: true };				
            } else {
                if(dataDocumentJustificativ > dataDecont) {
                    return { dataDecontLessThanDataDocumentJustificativ : true};
                }
            }
			return null;
		};
	}

}