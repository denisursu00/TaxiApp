import { Component, OnInit, Input, Output, EventEmitter } from "@angular/core";
import { FormControl, FormBuilder, FormGroup, Validators, AbstractControl, ValidatorFn } from "@angular/forms";
import { TranslateUtils, MessageDisplayer, UiUtils, ObjectUtils, AppError, FormUtils, DateConstants, DateUtils, BaseWindow } from "@app/shared";
import { NomenclatorConstants, NomenclatorService, ValueOfNomenclatorValueField, NomenclatorValidators, ArrayUtils, Message, NomenclatorValueModel, CursValutarService, CursValutarModel, StringValidators} from "@app/shared";
import { SelectItem, Dialog } from "primeng/primeng";
import { DeplasareDecontModel, ApelativReprezentantArbDeplasareDecontEnum, DocumentDecizieDeplasareModel, CheltuialaArbModel, CheltuialaReprezentantArbModel, ValutaForCheltuieliReprezentantArbEnum, ModalitatePlataForDecontEnum } from "@app/shared/model/deplasari-deconturi";
import { DeplasariDeconturiService } from "@app/shared/service/deplasari-deconturi.service";
import { NumarDecizieDeplasareModel } from "@app/shared/model/deplasari-deconturi/numar-decizie-deplasare.model";
import { DeplasariDeconturiComponent } from "../deplasari-deconturi.component";
import { ListItemUtils } from "@app/shared/utils/list-item-utils";

@Component({
	selector: "app-deplasari-deconturi-window",
	templateUrl: "./deplasari-deconturi-window.component.html"
})
export class DeplasariDeconturiWindowComponent extends BaseWindow implements OnInit {
	
	public static readonly CURS_VALUTAR_WHEN_VALUTA_RON_SELECTED: number = 0;

	@Input()
	public deplasareDecontId: number;

	@Input()
	public mode: "add" | "edit" | "view";

	@Output()
	private windowClosed: EventEmitter<void>;
	
	private translateUtils: TranslateUtils;
	private formBuilder: FormBuilder;
	private deplasariDeconturiService: DeplasariDeconturiService;
	private messageDisplayer: MessageDisplayer;
	private nomenclatorService: NomenclatorService;
	private cursValutarService: CursValutarService;

	private deplasareDecontModel: DeplasareDecontModel;
	
	public formGroup: FormGroup;
	public windowVisible: boolean = false;
	public title: string;
	public saveActionEnabled: boolean;

	public dateFormat: string;
	public yearRange: string;

	public apelativItems: SelectItem[];
	public numarDecizieItems: SelectItem[];
	public valutaItems: SelectItem[];
	public diurnaZilnicaModalitatePlataItems: SelectItem[];
	public avansPrimitModalitatePlataItems: SelectItem[];

	public readonly: boolean = false;
	public numarDecizieReadonly: boolean = false;

	public numarDecizieDeplasareModels: NumarDecizieDeplasareModel[];

	public organismId: number;

	public messagesWindowVisible: boolean;
	public messagesWindowMessages: Message[];

	public cheltuieliArbFieldsetTitle: string;
	public cheltuieliReprezentantArbFieldsetTitle: string;
	public decontFieldsetTitle: string;

	private cursValutar: CursValutarModel;

	public valoareMinimaDiurnaZilnica: number = 1;
	public validationMessageParameterMinValueForDiurnaZilnica = {value: this.valoareMinimaDiurnaZilnica};
	public valoareMinimaAvansPrimit: number = 0;
	public validationMessageParameterMinValueForAvansPrimit = {value: this.valoareMinimaAvansPrimit};

	public cheltuieliArbDataDecont: Date;

	public constructor(translateUtils: TranslateUtils, formBuilder: FormBuilder, deplasariDeconturiService: DeplasariDeconturiService, 
			messageDisplayer: MessageDisplayer, nomenclatorService: NomenclatorService, cursValutarService: CursValutarService) {
		super();
		this.translateUtils = translateUtils;
		this.formBuilder = formBuilder;
		this.deplasariDeconturiService = deplasariDeconturiService;
		this.messageDisplayer = messageDisplayer;
		this.nomenclatorService = nomenclatorService;
		this.cursValutarService = cursValutarService;
		this.dateFormat = DateConstants.DATE_FORMAT_FOR_TYPING;
		this.numarDecizieItems = [];
		this.yearRange = DateUtils.getDefaultYearRange();
		this.windowClosed = new EventEmitter<void>();
		this.lock();
		this.prepareNomenclatorValuesForNomenclatorValueSelectors([
			NomenclatorConstants.NOMENCLATOR_CODE_REPREZENTANT_ARB_ORGANISME_INTERNE_SI_INTERNATIONALE
		]);
	}

	private prepareNomenclatorValuesForNomenclatorValueSelectors(codes: string[]):void {
		this.nomenclatorService.getNomenclatorIdByCodeAsMap(codes, {
			onSuccess: (nomenclatorIdByCode: object): void => {
				if (ObjectUtils.isNotNullOrUndefined(nomenclatorIdByCode)) {
					this.reprezentantArbFormControl.clearValidators();
					this.reprezentantArbFormControl.setValue(new ValueOfNomenclatorValueField(nomenclatorIdByCode[NomenclatorConstants.NOMENCLATOR_CODE_REPREZENTANT_ARB_ORGANISME_INTERNE_SI_INTERNATIONALE]));
					this.reprezentantArbFormControl.setValidators([NomenclatorValidators.nomenclatorValueRequired()]);
				}
				this.unlock();
				this.openWindow();
			},
			onFailure: (appError: AppError): void => {
				this.unlock();
				this.messageDisplayer.displayAppError(appError);
			}
		});
	}

	public ngOnInit(): void {
		this.init();
	}

	private init(): void {
		this.loadCursValutar();
		this.setTitlesForCheltuieliFieldsets();
		this.prepareForm();
		this.prepareApelativItems();
		this.prepareValutaItems();
		this.prepareDiurnaZilnicaModalitatePlataItems();
		this.prepareAvansPrimitModalitatePlataItems();
		this.prepareByMode();
	}
	
	private loadCursValutar(): void {
		this.cursValutarService.getCursValutarCurent({
			onSuccess: (cursValutar: CursValutarModel): void => {
				this.cursValutar = cursValutar;
			},
			onFailure: (appError: AppError): void => {
				this.messageDisplayer.displayAppError(appError);
			}
		});
	}

	private setTitlesForCheltuieliFieldsets(): void {
		this.cheltuieliArbFieldsetTitle = this.translateUtils.translateLabel("CHELTUIELI_ARB");
		this.cheltuieliReprezentantArbFieldsetTitle = this.translateUtils.translateLabel("CHELTUIELI_REPREZENTANT_ARB");
		this.decontFieldsetTitle = this.translateUtils.translateLabel("DECONT");
	}

	private prepareForm(): void {
		this.formGroup = this.formBuilder.group([]);
		this.formGroup.addControl("apelativ", new FormControl(null, [Validators.required]));
		this.formGroup.addControl("reprezentantArb", new FormControl(null, [NomenclatorValidators.nomenclatorValueRequired()]));
		this.formGroup.addControl("numarDecizie", new FormControl(null, [Validators.required]));
		this.formGroup.addControl("institutie", new FormControl(null, []));
		this.formGroup.addControl("dataDecizie", new FormControl({value: null, disabled: true}, []));
		this.formGroup.addControl("denumireOrganism", new FormControl(null, []));
		this.formGroup.addControl("abreviereOrganism", new FormControl(null, []));
		this.formGroup.addControl("comitet", new FormControl(null, []));
		this.formGroup.addControl("numarDeplasariEfectuate", new FormControl(null, []));
		this.formGroup.addControl("numarDeplasariBugetateRamase", new FormControl(null, []));
		this.formGroup.addControl("eveniment", new FormControl(null, []));
		this.formGroup.addControl("tara", new FormControl(null, []));
		this.formGroup.addControl("oras", new FormControl(null, []));
		this.formGroup.addControl("dataPlecare", new FormControl({value: null, disabled: true}, []));
		this.formGroup.addControl("dataSosire", new FormControl({value: null, disabled: true}, []));
		this.formGroup.addControl("dataConferintaInceput", new FormControl({value: null, disabled: true}, []));
		this.formGroup.addControl("dataConferintaSfarsit", new FormControl({value: null, disabled: true}, []));
		this.formGroup.addControl("numarNopti", new FormControl(null, []));
		this.formGroup.addControl("minutaIntalnireTransmisa", new FormControl(false, [Validators.required]));
		this.formGroup.addControl("observatii", new FormControl(null, []));
		this.formGroup.addControl("cheltuieliArbTitularDecont", new FormControl(null, []));
		this.formGroup.addControl("cheltuieliArbTipDecont", new FormControl({value: "Deplasare Decont", disabled: true}, []));
		this.formGroup.addControl("cheltuieliArbDataDecont", new FormControl(null, []));
		this.formGroup.addControl("cheltuieliArb", new FormControl(null, []));
		this.formGroup.addControl("cheltuieliReprezentantArbDiurnaZilnica", new FormControl(null, []));
		this.formGroup.addControl("cheltuieliReprezentantArbDiurnaZilnicaValuta", new FormControl(null, []));
		this.formGroup.addControl("cheltuieliReprezentantArbDiurnaZilnicaCursValutar", new FormControl(null, []));
		this.formGroup.addControl("cheltuieliReprezentantArbDiurnaZilnicaModalitatePlata", new FormControl(null, []));
		this.formGroup.addControl("cheltuieliReprezentantArbNumarZile", new FormControl(null, []));
		this.formGroup.addControl("cheltuieliReprezentantArbTotalDiurna", new FormControl(null, []));
		this.formGroup.addControl("cheltuieliReprezentantArbAvansPrimitSuma", new FormControl(null, []));
		this.formGroup.addControl("cheltuieliReprezentantArbAvansPrimitSumaValuta", new FormControl(null, []));
		this.formGroup.addControl("cheltuieliReprezentantArbAvansPrimitSumaCursValutar", new FormControl(null, []));
		this.formGroup.addControl("cheltuieliReprezentantArbAvansPrimitCardSauNumerar", new FormControl(null, []));
		this.formGroup.addControl("cheltuieliReprezentantArb", new FormControl(null, []));
		this.formGroup.addControl("detaliiNumarDeplasariBugetateNomenclatorValueId", new FormControl(null, []));
	}

	private prepareApelativItems(): void {
		this.apelativItems = [
			{label: this.translateUtils.translateLabel(ApelativReprezentantArbDeplasareDecontEnum.DOMNISOARA), value: ApelativReprezentantArbDeplasareDecontEnum.DOMNISOARA},
			{label: this.translateUtils.translateLabel(ApelativReprezentantArbDeplasareDecontEnum.DOAMNA), value: ApelativReprezentantArbDeplasareDecontEnum.DOAMNA},
			{label: this.translateUtils.translateLabel(ApelativReprezentantArbDeplasareDecontEnum.DOMNUL), value: ApelativReprezentantArbDeplasareDecontEnum.DOMNUL}
		];

		ListItemUtils.sortByLabel(this.apelativItems);
	}

	private prepareValutaItems(): void {
		this.valutaItems = [
			{label: this.translateUtils.translateLabel(ValutaForCheltuieliReprezentantArbEnum.RON), value: ValutaForCheltuieliReprezentantArbEnum.RON},
			{label: this.translateUtils.translateLabel(ValutaForCheltuieliReprezentantArbEnum.EUR), value: ValutaForCheltuieliReprezentantArbEnum.EUR},
			{label: this.translateUtils.translateLabel(ValutaForCheltuieliReprezentantArbEnum.USD), value: ValutaForCheltuieliReprezentantArbEnum.USD}
		];
		
		ListItemUtils.sortByLabel(this.valutaItems);
	}

	private prepareDiurnaZilnicaModalitatePlataItems(): void {
		this.diurnaZilnicaModalitatePlataItems = [
			{label: this.translateUtils.translateLabel(ModalitatePlataForDecontEnum.CARD), value: ModalitatePlataForDecontEnum.CARD},
			{label: this.translateUtils.translateLabel(ModalitatePlataForDecontEnum.NUMERAR), value: ModalitatePlataForDecontEnum.NUMERAR}
		];
		
		ListItemUtils.sortByLabel(this.diurnaZilnicaModalitatePlataItems);
	}

	private prepareAvansPrimitModalitatePlataItems(): void {
		this.avansPrimitModalitatePlataItems = [
			{label: this.translateUtils.translateLabel(ModalitatePlataForDecontEnum.CARD), value: ModalitatePlataForDecontEnum.CARD},
			{label: this.translateUtils.translateLabel(ModalitatePlataForDecontEnum.NUMERAR), value: ModalitatePlataForDecontEnum.NUMERAR}
		];
	
		ListItemUtils.sortByLabel(this.avansPrimitModalitatePlataItems);
	}

	private prepareByMode(): void {
		if (this.isAdd()) {
			this.prepareForAdd();
		} else if (this.isEdit() || this.isView()) {
			this.prepareForViewOrEdit();
		}
	}

	private isAdd(): boolean {
		return this.mode === "add";
	}

	private isEdit(): boolean {
		return this.mode === "edit";
	}

	private isView(): boolean {
		return this.mode === "view";
	}

	private prepareForAdd(): void {
		this.updatePerspectiveForAdd();
	}

	private updatePerspectiveForAdd(): void {
		this.numarDecizieReadonly = true;
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
		} else if (this.isEdit()) {
			action = this.translateUtils.translateLabel("EDIT");	
		}
		this.title = action;
	}

	private openWindow(): void {
		this.windowVisible = true;
	}

	private prepareForViewOrEdit(): void {
		this.deplasariDeconturiService.getDeplasareDecontById(this.deplasareDecontId, {
			onSuccess: (deplasareDecontModel: DeplasareDecontModel): void => {
				this.deplasareDecontModel = deplasareDecontModel;				
				this.prepareNumarDeciziiDeplasariAprobateByReprezentant( this.deplasareDecontModel.reprezentantArbId, this.deplasareDecontModel.id,() => {
					this.populateForm();
					this.updatePerspectiveForView();				
					this.updateInputParameterForCheltuieliWindow();			
				});	
			},
			onFailure: (appError: AppError): void => {
				this.unlock();
				this.messageDisplayer.displayAppError(appError);
			}
		});
	}

	private populateForm(): void {

		this.nomenclatorService.getNomenclatorValue(this.deplasareDecontModel.reprezentantArbId, {
			onSuccess: (nomenclatorValue: NomenclatorValueModel): void => {
				let reprezentantArbValueField: ValueOfNomenclatorValueField = new ValueOfNomenclatorValueField(nomenclatorValue.nomenclatorId);
				reprezentantArbValueField.value = this.deplasareDecontModel.reprezentantArbId;

				this.reprezentantArbFormControl.setValue(reprezentantArbValueField);
			},
			onFailure: (appError: AppError): void => {
				this.messageDisplayer.displayAppError(appError);
			}
		});
		
		this.organismId = this.deplasareDecontModel.organismId;

		this.formGroup.patchValue({
			apelativ: this.deplasareDecontModel.apelativ,
			institutie: this.deplasareDecontModel.denumireInstitutie,
			dataDecizie: this.deplasareDecontModel.dataDecizie,
			denumireOrganism: this.deplasareDecontModel.denumireOrganism,
			abreviereOrganism: this.deplasareDecontModel.abreviereOrganism,
			comitet: this.deplasareDecontModel.denumireComitet,
			numarDeplasariEfectuate: this.deplasareDecontModel.numarDeplasariEfectuate,
			numarDeplasariBugetateRamase: this.deplasareDecontModel.numarDeplasariBugetateRamase,
			eveniment: this.deplasareDecontModel.eveniment,
			tara: this.deplasareDecontModel.tara,
			oras: this.deplasareDecontModel.oras,
			dataPlecare : this.deplasareDecontModel.dataPlecare,
			dataSosire : this.deplasareDecontModel.dataSosire,
			dataConferintaInceput : this.deplasareDecontModel.dataConferintaInceput,
			dataConferintaSfarsit : this.deplasareDecontModel.dataConferintaSfarsit,
			numarNopti: this.deplasareDecontModel.numarNopti,
			minutaIntalnireTransmisa: this.deplasareDecontModel.minutaIntalnireTransmisa,
			observatii: this.deplasareDecontModel.observatii,
			detaliiNumarDeplasariBugetateNomenclatorValueId: this.deplasareDecontModel.detaliiNumarDeplasariBugetateNomenclatorValueId,
			
			cheltuieliArbTitularDecont: this.deplasareDecontModel.cheltuieliArbTitularDecont,
			cheltuieliArbTipDecont: this.deplasareDecontModel.cheltuieliArbTipDecont,
			cheltuieliArbDataDecont: this.deplasareDecontModel.cheltuieliArbDataDecont,
			cheltuieliArb: this.deplasareDecontModel.cheltuieliArb,

			cheltuieliReprezentantArbDiurnaZilnica: this.deplasareDecontModel.cheltuieliReprezentantArbDiurnaZilnica,
			cheltuieliReprezentantArbDiurnaZilnicaValuta: this.deplasareDecontModel.cheltuieliReprezentantArbDiurnaZilnicaValuta,
			cheltuieliReprezentantArbDiurnaZilnicaCursValutar: this.deplasareDecontModel.cheltuieliReprezentantArbDiurnaZilnicaCursValutar,
			cheltuieliReprezentantArbDiurnaZilnicaModalitatePlata: this.deplasareDecontModel.cheltuieliReprezentantArbDiurnaZilnicaModalitatePlata,
			cheltuieliReprezentantArbNumarZile: this.deplasareDecontModel.cheltuieliReprezentantArbNumarZile,
			cheltuieliReprezentantArbTotalDiurna: Number(this.deplasareDecontModel.cheltuieliReprezentantArbTotalDiurna).toFixed(2),
			cheltuieliReprezentantArbAvansPrimitSuma: this.deplasareDecontModel.cheltuieliReprezentantArbAvansPrimitSuma,
			cheltuieliReprezentantArbAvansPrimitSumaValuta: this.deplasareDecontModel.cheltuieliReprezentantArbAvansPrimitSumaValuta,
			cheltuieliReprezentantArbAvansPrimitSumaCursValutar: this.deplasareDecontModel.cheltuieliReprezentantArbAvansPrimitSumaCursValutar,
			cheltuieliReprezentantArbAvansPrimitCardSauNumerar: this.deplasareDecontModel.cheltuieliReprezentantArbAvansPrimitCardSauNumerar,
			cheltuieliReprezentantArb: this.deplasareDecontModel.cheltuieliReprezentantArb
		});

		this.setSelectedNumarDecizie(this.deplasareDecontModel.numarDecizie);
	}

	private updatePerspectiveForView(): void {
		this.saveActionEnabled = true;
		if (this.isView()) {
			this.readonly = true;
			FormUtils.disableAllFormFields(this.formGroup);
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
		if ( ArrayUtils.isNotEmpty(this.cheltuieliArbFormControl.value) || ArrayUtils.isNotEmpty(this.cheltuieliReprezentantArbFormControl.value) )  {

			this.cheltuieliArbFormControl.clearValidators();
			this.cheltuieliReprezentantArbFormControl.clearValidators();
			
			this.cheltuieliArbFormControl.updateValueAndValidity();
			this.cheltuieliReprezentantArbFormControl.updateValueAndValidity();
		} 

		if (this.isValid()) {
			this.saveActionEnabled = false;
			let deplasareDecontModel: DeplasareDecontModel = this.buildAlteDeconturiModel();
			this.saveDeplasareDecont(deplasareDecontModel);
		}
	}

	private isValid(): boolean {
		return this.validate();
	}

	private validate(): boolean {
		FormUtils.validateAllFormFields(this.formGroup);
		return this.formGroup.valid;
	}

	private buildAlteDeconturiModel(): DeplasareDecontModel {

		let deplasareDecontModel: DeplasareDecontModel = new DeplasareDecontModel();

		if (ObjectUtils.isNotNullOrUndefined(this.deplasareDecontModel)) {
			deplasareDecontModel.id = this.deplasareDecontModel.id;
			deplasareDecontModel.numarInregistrare = this.deplasareDecontModel.numarInregistrare;
		}

		if (this.isEdit()) {
			deplasareDecontModel.id = this.deplasareDecontModel.id;
		}

		deplasareDecontModel.apelativ = this.apelativFormControl.value;
		deplasareDecontModel.reprezentantArbId = this.reprezentantArbFormControl.value.value;
		deplasareDecontModel.numarDecizie = (<NumarDecizieDeplasareModel>this.numarDecizieFormControl.value).numarDecizie;
		deplasareDecontModel.denumireInstitutie = this.institutieFormControl.value;
		deplasareDecontModel.dataDecizie = this.dataDecizieFormControl.value;
		deplasareDecontModel.organismId = this.organismId; // AICI nu e form control
		deplasareDecontModel.denumireComitet = this.comitetFormControl.value;
		deplasareDecontModel.numarDeplasariEfectuate = this.numarDeplasariEfectuateFormControl.value;
		deplasareDecontModel.numarDeplasariBugetateRamase = this.numarDeplasariBugetateRamaseFormControl.value;
		deplasareDecontModel.eveniment = this.evenimentFormControl.value;
		deplasareDecontModel.tara = this.taraFormControl.value;
		deplasareDecontModel.oras = this.orasFormControl.value;
		deplasareDecontModel.dataPlecare = this.dataPlecareFormControl.value;
		deplasareDecontModel.dataSosire = this.dataSosireFormControl.value;
		deplasareDecontModel.dataConferintaInceput = this.dataConferintaInceputFormControl.value;
		deplasareDecontModel.dataConferintaSfarsit = this.dataConferintaSfarsitFormControl.value;
		deplasareDecontModel.numarNopti = this.numarNoptiFormControl.value;
		deplasareDecontModel.minutaIntalnireTransmisa = this.minutaIntalnireTransmisaFormControl.value;
		deplasareDecontModel.observatii = this.observatiiFormControl.value;
		
		deplasareDecontModel.documentId = (<NumarDecizieDeplasareModel>this.numarDecizieFormControl.value).documentId;
		deplasareDecontModel.documentLocationRealName = (<NumarDecizieDeplasareModel>this.numarDecizieFormControl.value).documentLocationRealName;

		deplasareDecontModel.cheltuieliArbTitularDecont = this.cheltuieliArbTitularDecontFormControl.value;
		deplasareDecontModel.cheltuieliArbTipDecont = this.cheltuieliArbTipDecontFormControl.value;
		deplasareDecontModel.cheltuieliArbDataDecont = this.cheltuieliArbDataDecontFormControl.value;
		deplasareDecontModel.cheltuieliArb = this.cheltuieliArbFormControl.value;

		deplasareDecontModel.cheltuieliReprezentantArbDiurnaZilnica = this.cheltuieliReprezentantArbDiurnaZilnicaFormControl.value;
		deplasareDecontModel.cheltuieliReprezentantArbDiurnaZilnicaValuta = this.cheltuieliReprezentantArbDiurnaZilnicaValutaFormControl.value;
		deplasareDecontModel.cheltuieliReprezentantArbDiurnaZilnicaCursValutar = this.cheltuieliReprezentantArbDiurnaZilnicaCursValutarFormControl.value;
		deplasareDecontModel.cheltuieliReprezentantArbDiurnaZilnicaModalitatePlata = this.cheltuieliReprezentantArbDiurnaZilnicaModalitatePlataFormControl.value;
		deplasareDecontModel.cheltuieliReprezentantArbNumarZile = this.cheltuieliReprezentantArbNumarZileFormControl.value;
		deplasareDecontModel.cheltuieliReprezentantArbTotalDiurna = Number(this.cheltuieliReprezentantArbTotalDiurnaFormControl.value);
		deplasareDecontModel.cheltuieliReprezentantArbAvansPrimitSuma = this.cheltuieliReprezentantArbAvansPrimitSumaFormControl.value;
		deplasareDecontModel.cheltuieliReprezentantArbAvansPrimitSumaValuta = this.cheltuieliReprezentantArbAvansPrimitSumaValutaFormControl.value;
		deplasareDecontModel.cheltuieliReprezentantArbAvansPrimitSumaCursValutar = this.cheltuieliReprezentantArbAvansPrimitSumaCursValutarFormControl.value;
		deplasareDecontModel.cheltuieliReprezentantArbAvansPrimitCardSauNumerar = this.cheltuieliReprezentantArbAvansPrimitCardSauNumerarFormControl.value;
		deplasareDecontModel.cheltuieliReprezentantArb = this.cheltuieliReprezentantArbFormControl.value;
		deplasareDecontModel.detaliiNumarDeplasariBugetateNomenclatorValueId = this.detaliiNumarDeplasariBugetateNomenclatorValueIdFormControl.value;

		return deplasareDecontModel;
	}

	private saveDeplasareDecont(deplasareDecontModel: DeplasareDecontModel): void {
		this.deplasariDeconturiService.saveDeplasareDecont(deplasareDecontModel, {
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

	public onReprezentantArbValueChanged(): void {
		this.loadNumarDeciziiDeplasariAprobate();
		this.setSelectedTitularDecont();
	}
	
	private loadNumarDeciziiDeplasariAprobate() {
		if (ObjectUtils.isNotNullOrUndefined(this.reprezentantArbFormControl.value.value)) {
			this.prepareNumarDeciziiDeplasariAprobateByReprezentant(this.reprezentantArbFormControl.value.value);
		} else {
			this.numarDecizieDeplasareModels = [];
		}
	}

	private setSelectedTitularDecont(){
		let idReprezentantArb: any = this.reprezentantArbFormControl.value.value;
		
		if (!ObjectUtils.isNullOrUndefined(idReprezentantArb)) {
			this.nomenclatorService.getNomenclatorValue(idReprezentantArb, {
				onSuccess: (reprezentantArbNomenclatorValue: NomenclatorValueModel): void => {
					let idReprezentant: any = reprezentantArbNomenclatorValue[NomenclatorConstants.NOMENCLATOR_CODE_REPREZENTANT_ARB_ORGANISME_INTERNE_SI_INTERNATIONALE_ATTR_KEY_REPREZENTANT];
					this.nomenclatorService.getNomenclatorValue(idReprezentant, {
						onSuccess: (reprezentantNomenclatorValue: NomenclatorValueModel): void => {
							let nume: string = reprezentantNomenclatorValue[NomenclatorConstants.PERSOANE_ATTR_KEY_NUME];
							let prenume: string = reprezentantNomenclatorValue[NomenclatorConstants.PERSOANE_ATTR_KEY_PRENUME];
							this.cheltuieliArbTitularDecontFormControl.setValue(nume + " " + prenume);
						},
						onFailure: (appError: AppError): void => {
							this.messageDisplayer.displayAppError(appError);
						}
					});	
				},
				onFailure: (appError: AppError): void => {
					this.messageDisplayer.displayAppError(appError);
				}
			});
		} else {
			this.cheltuieliArbTitularDecontFormControl.setValue("");
		}
		
	}


	private prepareNumarDeciziiDeplasariAprobateByReprezentant(reprezentantArbId: number, documentId?: number, callback?: () => any): void {
		this.deplasariDeconturiService.getNumarDeciziiDeplasariAprobateByReprezentant(reprezentantArbId, documentId, {
			onSuccess: (numarDecizieDeplasareModels: NumarDecizieDeplasareModel[]): void => {
				this.numarDecizieDeplasareModels = numarDecizieDeplasareModels;
				if (ArrayUtils.isNotEmpty(this.numarDecizieDeplasareModels)) {
					this.prepareNumarDeciziiItems();
					this.numarDecizieReadonly = false;					
				} else {
					this.showErrorMessages();
				}
				if (ObjectUtils.isNotNullOrUndefined(callback)) {
					callback();
				}
			},
			onFailure: (appError: AppError): void => {
				this.messageDisplayer.displayAppError(appError);
			}
		});
	}

	private prepareNumarDeciziiItems(): void {
		this.numarDecizieItems = [];
		this.numarDecizieDeplasareModels.forEach((numarDecizieDeplasareModel: NumarDecizieDeplasareModel) => {
			let item: SelectItem = {
				label: numarDecizieDeplasareModel.numarDecizie,
				value: numarDecizieDeplasareModel
			};
			this.numarDecizieItems.push(item);
		});

		ListItemUtils.sortByLabel(this.numarDecizieItems);
	}

	private setSelectedNumarDecizie(numarDecizie: string): void{
		this.numarDecizieItems.forEach((numarDecizieItem: SelectItem) => {
			if (numarDecizieItem.value.numarDecizie === numarDecizie) {
				this.numarDecizieFormControl.setValue(numarDecizieItem.value);
			} 
		});
	}

	public onNumarDecizieValueChanged(): void {

		let selectedNumarDecizieDeplasareModel: NumarDecizieDeplasareModel = this.numarDecizieFormControl.value;
		let documentId: string = selectedNumarDecizieDeplasareModel.documentId;
		let documentLocationRealName: string = selectedNumarDecizieDeplasareModel.documentLocationRealName;
		this.deplasariDeconturiService.getDocumentDecizieDeplasare(documentId, documentLocationRealName, {
			onSuccess: (documentDecizieDeplasareModel: DocumentDecizieDeplasareModel): void => {

				this.institutieFormControl.setValue(documentDecizieDeplasareModel.denumireInstitutie);
				this.dataDecizieFormControl.setValue(documentDecizieDeplasareModel.dataDecizie);
				this.organismId = documentDecizieDeplasareModel.organismId;
				this.denumireOrganismFormControl.setValue(documentDecizieDeplasareModel.denumireOrganism);
				this.abreviereOrganismFormControl.setValue(documentDecizieDeplasareModel.abreviereOrganism);
				this.comitetFormControl.setValue(documentDecizieDeplasareModel.denumireComitet);
				this.numarDeplasariEfectuateFormControl.setValue(documentDecizieDeplasareModel.numarDeplasariEfectuate);
				this.numarDeplasariBugetateRamaseFormControl.setValue(documentDecizieDeplasareModel.numarDeplasariBugetateRamase);
				this.evenimentFormControl.setValue(documentDecizieDeplasareModel.eveniment);
				this.taraFormControl.setValue(documentDecizieDeplasareModel.tara);
				this.orasFormControl.setValue(documentDecizieDeplasareModel.oras);
				this.dataPlecareFormControl.setValue(documentDecizieDeplasareModel.dataPlecare);
				this.dataSosireFormControl.setValue(documentDecizieDeplasareModel.dataSosire);
				this.dataConferintaInceputFormControl.setValue(documentDecizieDeplasareModel.dataConferintaInceput);
				this.dataConferintaSfarsitFormControl.setValue(documentDecizieDeplasareModel.dataConferintaSfarsit);
				this.detaliiNumarDeplasariBugetateNomenclatorValueIdFormControl.setValue(documentDecizieDeplasareModel.detaliiNumarDeplasariBugetateNomenclatorValueId);
				
				this.calculateNumarNopti();
				this.calculateNumarZile();

				if (documentDecizieDeplasareModel.numarDeplasariBugetateRamase <= 0) {
					this.messageDisplayer.displayWarn("NU_EXISTA_DEPLASARI_BUGETATE_RAMASE");
					this.saveActionEnabled = false;
				}
			},
			onFailure: (appError: AppError): void => {
				this.messageDisplayer.displayAppError(appError);
			}
		});

		this.onCheltuieliArbDataDecontChanged();
	}


	public onCheltuieliReprezentantArbDiurnaZilnicaValutaValueChanged(): void {
		this.prepareCursValutarForDiurnaZilnica();
		this.updateTotalDiurna();
	}

	private prepareCursValutarForDiurnaZilnica(): void {
		if (this.cheltuieliReprezentantArbDiurnaZilnicaValutaFormControl.value === ValutaForCheltuieliReprezentantArbEnum.EUR) {
			this.cheltuieliReprezentantArbDiurnaZilnicaCursValutarFormControl.setValue(this.cursValutar.eur);
		} else if (this.cheltuieliReprezentantArbDiurnaZilnicaValutaFormControl.value === ValutaForCheltuieliReprezentantArbEnum.USD) {
			this.cheltuieliReprezentantArbDiurnaZilnicaCursValutarFormControl.setValue(this.cursValutar.usd);
		} else if (this.cheltuieliReprezentantArbDiurnaZilnicaValutaFormControl.value === ValutaForCheltuieliReprezentantArbEnum.RON) {
			this.cheltuieliReprezentantArbDiurnaZilnicaCursValutarFormControl.setValue(DeplasariDeconturiWindowComponent.CURS_VALUTAR_WHEN_VALUTA_RON_SELECTED);
		}
	}

	public onCheltuieliReprezentantArbAvansPrimitSumaValutaValueChanged(): void {
		this.prepareCursValutarForAvansPrimit();
	}

	private prepareCursValutarForAvansPrimit(): void {
		if (this.cheltuieliReprezentantArbAvansPrimitSumaValutaFormControl.value === ValutaForCheltuieliReprezentantArbEnum.EUR) {
			this.cheltuieliReprezentantArbAvansPrimitSumaCursValutarFormControl.setValue(this.cursValutar.eur);
		} else if (this.cheltuieliReprezentantArbAvansPrimitSumaValutaFormControl.value === ValutaForCheltuieliReprezentantArbEnum.USD) {
			this.cheltuieliReprezentantArbAvansPrimitSumaCursValutarFormControl.setValue(this.cursValutar.usd);
		} else if (this.cheltuieliReprezentantArbAvansPrimitSumaValutaFormControl.value === ValutaForCheltuieliReprezentantArbEnum.RON) {
			this.cheltuieliReprezentantArbAvansPrimitSumaCursValutarFormControl.setValue(DeplasariDeconturiWindowComponent.CURS_VALUTAR_WHEN_VALUTA_RON_SELECTED);
		}
	}

	private calculateNumarNopti(): void {
		let dataPlecare = new Date();
		dataPlecare.setTime(this.dataPlecareFormControl.value.getTime());
		let dataSosire = new Date();
		dataSosire.setTime(this.dataSosireFormControl.value.getTime());
		dataPlecare = DateUtils.removeTimeFromDate(dataPlecare);
		dataSosire = DateUtils.removeTimeFromDate(dataSosire);
		let numarNopti: number = Math.floor((dataSosire.getTime() - dataPlecare.getTime()) / DateConstants.ONE_DAY_AS_MILISECONDS);
		this.numarNoptiFormControl.setValue(numarNopti);		
	}

	private calculateNumarZile(): void {
		let dataPlecare = this.dataPlecareFormControl.value;
		let dataSosire = this.dataSosireFormControl.value;
		let numarZile: number = Math.round((dataSosire.getTime() - dataPlecare.getTime()) / DateConstants.ONE_DAY_AS_MILISECONDS);
		this.cheltuieliReprezentantArbNumarZileFormControl.setValue(numarZile);
	}

	public onCheltuieliArbDataChanged(cheltuieli: CheltuialaArbModel[]): void {

	}

	public onCheltuieliReprezentantArbDataChanged(cheltuieli: CheltuialaReprezentantArbModel[]): void {
		
	}

	public onDiurnaZilnicaChanged(): void {
		this.prepareCursValutarForDiurnaZilnica();		
		this.updateTotalDiurna();

	}

	private updateTotalDiurna(): void {
		let totalDiurnaZilnica: Number ;
		let diurnaZilnica: Number = this.cheltuieliReprezentantArbDiurnaZilnicaFormControl.value;
		let nrZile: Number = this.cheltuieliReprezentantArbNumarZileFormControl.value;
		let cursValutar: Number = this.cheltuieliReprezentantArbDiurnaZilnicaCursValutarFormControl.value;
		if (this.cheltuieliReprezentantArbDiurnaZilnicaValutaFormControl.value === ValutaForCheltuieliReprezentantArbEnum.RON) {
			totalDiurnaZilnica = Number(diurnaZilnica) * Number(nrZile);
		} else {
			totalDiurnaZilnica = Number(diurnaZilnica) * Number(nrZile) * Number(cursValutar);
		}
		this.cheltuieliReprezentantArbTotalDiurnaFormControl.setValue(totalDiurnaZilnica.toFixed(2));
	}

	public onAvansPrimitChanged(): void {
		this.prepareCursValutarForAvansPrimit();
	}

	public onAfterToggleDecontFieldset(event: any): void {
		this.changeDecontFormPerspective(event.collapsed);
	}

	private changeDecontFormPerspective(decontFieldsetCollapsed: boolean): void {
		this.cheltuieliArbTitularDecontFormControl.clearValidators();
		this.cheltuieliArbDataDecontFormControl.clearValidators();
		this.cheltuieliArbFormControl.clearValidators();
		this.cheltuieliReprezentantArbFormControl.clearValidators();
		
		if (!decontFieldsetCollapsed) {
			this.cheltuieliArbTitularDecontFormControl.setValidators([Validators.required, StringValidators.blank()]);
			this.cheltuieliArbDataDecontFormControl.setValidators([Validators.required]);
			this.cheltuieliArbFormControl.setValidators([DeplasariDecontValidators.cheltuieliArbSauCheltuieliReprezentantArbRequired(this.cheltuieliArbFormControl.value, this.cheltuieliReprezentantArbFormControl.value)]);
			this.cheltuieliReprezentantArbFormControl.setValidators([DeplasariDecontValidators.cheltuieliArbSauCheltuieliReprezentantArbRequired(this.cheltuieliArbFormControl.value, this.cheltuieliReprezentantArbFormControl.value)]);
		}

		this.cheltuieliArbTitularDecontFormControl.updateValueAndValidity();
		this.cheltuieliArbDataDecontFormControl.updateValueAndValidity();
		this.cheltuieliArbFormControl.updateValueAndValidity();
		this.cheltuieliReprezentantArbFormControl.updateValueAndValidity();
	}

	public onAfterToggleCheltuieliArbFieldset(event: any): void {
		this.changeCheltuieliArbFormPerspective(event.collapsed);
	}

	private changeCheltuieliArbFormPerspective(cheltuieliArbFieldsetCollapsed: boolean): void {
		
		this.cheltuieliArbFormControl.clearValidators();
		
		if (!cheltuieliArbFieldsetCollapsed) {
			this.cheltuieliArbFormControl.setValidators([Validators.required]);
		}

		this.cheltuieliArbFormControl.setValidators([DeplasariDecontValidators.cheltuieliArbSauCheltuieliReprezentantArbRequired(this.cheltuieliArbFormControl.value, this.cheltuieliReprezentantArbFormControl.value)]);
		
		this.cheltuieliArbFormControl.updateValueAndValidity();
	}

	private getDateDocumenteJustificativeFromCheltuieliArb(): Date[] {
		let dateDocumentJustificative: Date[] = [];

		const cheltuieliArb: CheltuialaArbModel[] = this.cheltuieliArbFormControl.value;
		if (ArrayUtils.isNotEmpty(cheltuieliArb)) {
			cheltuieliArb.forEach((cheltuialaArb: CheltuialaArbModel) => {
				dateDocumentJustificative.push(cheltuialaArb.dataDocumentJustificativ);
			});
		}

		return dateDocumentJustificative;
	}

	public onAfterToggleCheltuieliReprezentantArbFieldset(event: any): void {
		this.changeCheltuieliiReprezentantArbFormPerspective(event.collapsed);
	}

	private changeCheltuieliiReprezentantArbFormPerspective(cheltuieliiReprezentantArbFieldsetCollapsed: boolean): void {
		
		this.cheltuieliReprezentantArbDiurnaZilnicaFormControl.clearValidators();
		this.cheltuieliReprezentantArbDiurnaZilnicaValutaFormControl.clearValidators();
		this.cheltuieliReprezentantArbDiurnaZilnicaModalitatePlataFormControl.clearValidators();
		this.cheltuieliReprezentantArbAvansPrimitSumaFormControl.clearValidators();
		this.cheltuieliReprezentantArbAvansPrimitSumaValutaFormControl.clearValidators();
		this.cheltuieliReprezentantArbAvansPrimitCardSauNumerarFormControl.clearValidators();
		this.cheltuieliReprezentantArbFormControl.clearValidators();
		
		if (!cheltuieliiReprezentantArbFieldsetCollapsed) {
			this.cheltuieliReprezentantArbDiurnaZilnicaFormControl.setValidators([Validators.required, Validators.min(this.valoareMinimaDiurnaZilnica)]);
			this.cheltuieliReprezentantArbDiurnaZilnicaValutaFormControl.setValidators([Validators.required]);			
			this.cheltuieliReprezentantArbDiurnaZilnicaModalitatePlataFormControl.setValidators([Validators.required]);
			this.cheltuieliReprezentantArbAvansPrimitSumaFormControl.setValidators([Validators.required, Validators.min(this.valoareMinimaAvansPrimit)]);
			this.cheltuieliReprezentantArbAvansPrimitSumaValutaFormControl.setValidators([Validators.required]);
			this.cheltuieliReprezentantArbAvansPrimitCardSauNumerarFormControl.setValidators([Validators.required]);
			this.cheltuieliReprezentantArbFormControl.setValidators([Validators.required]);
		}

		this.cheltuieliReprezentantArbFormControl.setValidators([DeplasariDecontValidators.cheltuieliArbSauCheltuieliReprezentantArbRequired(this.cheltuieliArbFormControl.value, this.cheltuieliReprezentantArbFormControl.value)]);
		
		this.cheltuieliReprezentantArbDiurnaZilnicaFormControl.updateValueAndValidity();
		this.cheltuieliReprezentantArbDiurnaZilnicaValutaFormControl.updateValueAndValidity();
		this.cheltuieliReprezentantArbDiurnaZilnicaModalitatePlataFormControl.updateValueAndValidity();
		this.cheltuieliReprezentantArbAvansPrimitSumaFormControl.updateValueAndValidity();
		this.cheltuieliReprezentantArbAvansPrimitSumaValutaFormControl.updateValueAndValidity();
		this.cheltuieliReprezentantArbAvansPrimitCardSauNumerarFormControl.updateValueAndValidity();
		this.cheltuieliReprezentantArbFormControl.updateValueAndValidity();
	}

	private showErrorMessages(): void {
		this.messagesWindowMessages = [
			{
				type: "error",
				code: "NU_EXISTA_DECIZII_PENTRU_REPREZENTANTUL_SELECTAT"
			}
		];
		this.messagesWindowVisible = true;
	}
	
	public onCheltuieliArbDataDecontChanged():void {
		if (ObjectUtils.isNotNullOrUndefined(this.cheltuieliArbDataDecontFormControl.value)) {

			this.cheltuieliArbDataDecont = this.cheltuieliArbDataDecontFormControl.value;

			let dataSosire: Date = this.dataSosireFormControl.value;
			let dataDecont: Date = this.cheltuieliArbDataDecontFormControl.value;
			dataSosire =  DateUtils.removeTimeFromDate(dataSosire);
			this.cheltuieliArbDataDecontFormControl.setValidators([DeplasariDecontValidators.dataDecontGreaterThanDataSosire(dataDecont, dataSosire)]); 
			this.cheltuieliArbDataDecontFormControl.updateValueAndValidity();
		}
	}
	
	public get isCampObligatoriu(): boolean {
		if (this.isView()) {
			return false;
		} else {
			return true;
		}
	}

	public isReadOnly(): boolean {
		if(this.isView()) {
			return true;
		}else { 
			if(this.isEdit()) {
				return true;
			} else {
				return false;
			}
		}
	}

	public onMessagesWindowClosed(): void {
		this.closeWindow();
	}

	public get apelativFormControl(): FormControl {
		return this.getControlByName("apelativ");
	}

	public get reprezentantArbFormControl(): FormControl {
		return this.getControlByName("reprezentantArb");
	}

	public get numarDecizieFormControl(): FormControl {
		return this.getControlByName("numarDecizie");
	}

	public get institutieFormControl(): FormControl {
		return this.getControlByName("institutie");
	}

	public get dataDecizieFormControl(): FormControl {
		return this.getControlByName("dataDecizie");
	}

	public get denumireOrganismFormControl(): FormControl {
		return this.getControlByName("denumireOrganism");
	}

	public get abreviereOrganismFormControl(): FormControl {
		return this.getControlByName("abreviereOrganism");
	}

	public get comitetFormControl(): FormControl {
		return this.getControlByName("comitet");
	}

	public get numarDeplasariEfectuateFormControl(): FormControl {
		return this.getControlByName("numarDeplasariEfectuate");
	}

	public get numarDeplasariBugetateRamaseFormControl(): FormControl {
		return this.getControlByName("numarDeplasariBugetateRamase");
	}

	public get evenimentFormControl(): FormControl {
		return this.getControlByName("eveniment");
	}

	public get taraFormControl(): FormControl {
		return this.getControlByName("tara");
	}

	public get orasFormControl(): FormControl {
		return this.getControlByName("oras");
	}

	public get dataPlecareFormControl(): FormControl {
		return this.getControlByName("dataPlecare");
	}

	public get dataSosireFormControl(): FormControl {
		return this.getControlByName("dataSosire");
	}

	public get dataConferintaInceputFormControl(): FormControl {
		return this.getControlByName("dataConferintaInceput");
	}

	public get dataConferintaSfarsitFormControl(): FormControl {
		return this.getControlByName("dataConferintaSfarsit");
	}

	public get numarNoptiFormControl(): FormControl {
		return this.getControlByName("numarNopti");
	}

	public get minutaIntalnireTransmisaFormControl(): FormControl {
		return this.getControlByName("minutaIntalnireTransmisa");
	}

	public get observatiiFormControl(): FormControl {
		return this.getControlByName("observatii");
	}

	public get cheltuieliArbTitularDecontFormControl(): FormControl {
		return this.getControlByName("cheltuieliArbTitularDecont");
	}

	public get cheltuieliArbTipDecontFormControl(): FormControl {
		return this.getControlByName("cheltuieliArbTipDecont");
	}

	public get cheltuieliArbNumarDecontFormControl(): FormControl {
		return this.getControlByName("cheltuieliArbNumarDecont");
	}

	public get cheltuieliArbDataDecontFormControl(): FormControl {
		return this.getControlByName("cheltuieliArbDataDecont");
	}

	public get cheltuieliArbFormControl(): FormControl {
		return this.getControlByName("cheltuieliArb");
	}

	public get cheltuieliReprezentantArbDiurnaZilnicaFormControl(): FormControl {
		return this.getControlByName("cheltuieliReprezentantArbDiurnaZilnica");
	}

	public get cheltuieliReprezentantArbDiurnaZilnicaValutaFormControl(): FormControl {
		return this.getControlByName("cheltuieliReprezentantArbDiurnaZilnicaValuta");
	}

	public get cheltuieliReprezentantArbDiurnaZilnicaCursValutarFormControl(): FormControl {
		return this.getControlByName("cheltuieliReprezentantArbDiurnaZilnicaCursValutar");
	}

	public get cheltuieliReprezentantArbDiurnaZilnicaModalitatePlataFormControl(): FormControl {
		return this.getControlByName("cheltuieliReprezentantArbDiurnaZilnicaModalitatePlata");
	}

	public get cheltuieliReprezentantArbNumarZileFormControl(): FormControl {
		return this.getControlByName("cheltuieliReprezentantArbNumarZile");
	}

	public get cheltuieliReprezentantArbTotalDiurnaFormControl(): FormControl {
		return this.getControlByName("cheltuieliReprezentantArbTotalDiurna");
	}

	public get cheltuieliReprezentantArbAvansPrimitSumaFormControl(): FormControl {
		return this.getControlByName("cheltuieliReprezentantArbAvansPrimitSuma");
	}

	public get cheltuieliReprezentantArbAvansPrimitSumaValutaFormControl(): FormControl {
		return this.getControlByName("cheltuieliReprezentantArbAvansPrimitSumaValuta");
	}

	public get cheltuieliReprezentantArbAvansPrimitSumaCursValutarFormControl(): FormControl {
		return this.getControlByName("cheltuieliReprezentantArbAvansPrimitSumaCursValutar");
	}

	public get cheltuieliReprezentantArbAvansPrimitCardSauNumerarFormControl(): FormControl {
		return this.getControlByName("cheltuieliReprezentantArbAvansPrimitCardSauNumerar");
	}

	public get cheltuieliReprezentantArbFormControl(): FormControl {
		return this.getControlByName("cheltuieliReprezentantArb");
	}

	public get detaliiNumarDeplasariBugetateNomenclatorValueIdFormControl(): FormControl {
		return this.getControlByName("detaliiNumarDeplasariBugetateNomenclatorValueId");
	}

	private getControlByName(controlName: string): FormControl {
		return <FormControl> this.formGroup.controls[controlName];
	}

	private updateInputParameterForCheltuieliWindow(): void {
		if (ObjectUtils.isNotNullOrUndefined(this.cheltuieliArbDataDecontFormControl.value)) {
			this.cheltuieliArbDataDecont = this.cheltuieliArbDataDecontFormControl.value;
		}
	}

}

class DeplasariDecontValidators {
	public static dataDecontGreaterThanDataSosire(dataDecont: Date, dataSosire: Date): ValidatorFn | null {
		return (control: AbstractControl): {[key: string]: any} => {
			if (ObjectUtils.isNullOrUndefined(dataSosire)) {
				return { dataSosireRequired: true };				
			} else {
				if(dataSosire > dataDecont) {
				return { dataDecontLessThanDataSosire  : true};
				}
			}
			return null;
		};
	}

	public static cheltuieliArbSauCheltuieliReprezentantArbRequired(cheltuieliArb: any, cheltuieliReprezentantArb: any): ValidatorFn | null {
		return (control: AbstractControl): {[key: string]: any} => {			
			if ( ArrayUtils.isEmpty(cheltuieliArb) &&  ArrayUtils.isEmpty(cheltuieliReprezentantArb)) {
				return { cheltuieliArbSauCheltuieliReprezentantArbRequired : true };				
			} 
			return null;
		};
	}
}
