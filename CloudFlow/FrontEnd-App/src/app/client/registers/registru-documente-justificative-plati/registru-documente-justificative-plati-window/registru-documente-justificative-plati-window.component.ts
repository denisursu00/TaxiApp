import { Component, Input, Output, EventEmitter, OnInit, ViewEncapsulation, ViewChild } from "@angular/core";
// tslint:disable-next-line:max-line-length
import { RegistruDocumenteJustificativePlatiModel, ModLivrareEnum, ModalitatePlataEnum, TipDocumentEnum } from "@app/shared/model/registru-documente-justificative-plati/registru-documente-justificative-plati.model";
import { RegistruDocumenteJustificativePlatiService, MessageDisplayer, TranslateUtils, Message, StringValidators, DateConstants, DateUtils, AppError, BooleanUtils, ValueOfNomenclatorValueField, FormUtils, ObjectUtils, NomenclatorValueModel, NomenclatorService, AttachmentService, AttachmentModel, ArrayUtils, DownloadUtils, BaseWindow } from "@app/shared";
import { FormBuilder, FormGroup, Validators, AbstractControl, FormControl } from "@angular/forms";
import { SelectItem, FileUpload, Dialog } from "primeng/primeng";
import { NomenclatorValidators } from "@app/shared/validators";
import { RegistruDocumenteJustificativePlatiAtasamentModel } from "@app/shared/model/registru-documente-justificative-plati/registru-documente-justificative-plati-atasamente.model";
import { HttpResponse } from "@angular/common/http";
import { ListItemUtils } from "@app/shared/utils/list-item-utils";

@Component({
	selector: "app-registru-documente-justificative-plati-window",
	templateUrl: "./registru-documente-justificative-plati-window.component.html",
	styles: ["./registru-documente-justificative-plati-window.component.css"]
})
export class RegistruDocumenteJustificativePlatiWindowComponent extends BaseWindow implements OnInit {

	@Input()
	public registruDocumentJustificativPlataId: number;

	@Input()
	public mode: "add" | "edit" | "view";

	@Output()
	private windowClosed: EventEmitter<void>;

	private nomenclatorService: NomenclatorService;
	private registruDocumenteJustificativePlatiService: RegistruDocumenteJustificativePlatiService;
	private messageDisplayer: MessageDisplayer;
	private translateUtils: TranslateUtils;

	private formBuilder: FormBuilder;
	public formGroup: FormGroup;

	private registruDocumenteJustificativePlatiModel: RegistruDocumenteJustificativePlatiModel;

	public windowVisible: boolean = false;

	public title: string = "Registru facturi";
	
	public saveActionEnabled: boolean;

	public dateFormat: string;
	public yearRange: string;

	public readonly: boolean = false;
	public incadrareConformBVCReadonly: boolean = false;
	public tipDocumentReadonly: boolean = false;

	public modLivrareItems: SelectItem[];
	public modalitatePlataItems: SelectItem[];

	public dataInregistrareMinDate: Date;
	public dataInregistrareMaxDate: Date;
	public dataDocumentMinDate: Date;
	public dataScadentaMinDate: Date;
	public currentDate: Date = new Date();

	public formVisible: boolean = false;

	public tipDocumentFacturaId: number;
	public tipDocumentProformaId: number;
	public tipDocumentInstiintareDePlataId: number;
	public tipDocumentChitantaId: number;
	public tipDocumentBonFiscalId: number;

	public plataScadentaStyle: {};

	public valoareMinima: number = 1;
	public validationMessageParameterMinValueForDiurnaZilnica = {value: this.valoareMinima};
	private attachmentService: AttachmentService;	

	@ViewChild(FileUpload)
	private fileUpload: FileUpload;

	public uploadedAttachments: RegistruDocumenteJustificativePlatiAtasamentModel[];

	public constructor(attachmentService: AttachmentService, nomenclatorService: NomenclatorService, 
			registruDocumenteJustificativePlatiService: RegistruDocumenteJustificativePlatiService, 
			messageDisplayer: MessageDisplayer, formBuilder: FormBuilder, translateUtils: TranslateUtils) {
		super();
		this.attachmentService = attachmentService;
		this.nomenclatorService = nomenclatorService;
		this.registruDocumenteJustificativePlatiService = registruDocumenteJustificativePlatiService;
		this.messageDisplayer = messageDisplayer;
		this.formBuilder = formBuilder;
		this.translateUtils = translateUtils;
		this.dateFormat = DateConstants.DATE_FORMAT_FOR_TYPING;
		this.yearRange = DateUtils.getDefaultYearRange();
		this.windowClosed = new EventEmitter<void>();
		this.init();
	}

	private openWindow(): void {
		this.windowVisible = true;
	}

	private init(): void {
		this.loadingVisible = true;
		this.prepareForm();

		let currentDate: Date = new Date();

		let minDate: Date = new Date();
		minDate.setMonth(currentDate.getMonth() - 3);
		this.dataInregistrareMinDate = minDate;
		this.dataDocumentMinDate = minDate;
		
		
		this.dataInregistrareMaxDate = currentDate;

		this.uploadedAttachments = [];
	}

	private prepareForm(): void {
		this.formGroup = this.formBuilder.group([]);
		this.formGroup.addControl("dataInregistrare", new FormControl(null, [Validators.required]));
		this.formGroup.addControl("emitent", new FormControl(null, [Validators.required]));
		this.formGroup.addControl("tipDocument", new FormControl(null, []));
		this.formGroup.addControl("numarDocument", new FormControl(null, [Validators.required]));
		this.formGroup.addControl("dataDocument", new FormControl(null, [Validators.required]));
		this.formGroup.addControl("modLivrare", new FormControl(null, [Validators.required]));
		this.formGroup.addControl("detalii", new FormControl(null, [Validators.required]));
		this.formGroup.addControl("valoare", new FormControl(null, [Validators.required]));
		this.formGroup.addControl("moneda", new FormControl(null, []));
		this.formGroup.addControl("dataScadenta", new FormControl(null, [Validators.required]));
		this.formGroup.addControl("modalitatePlata", new FormControl(null, [Validators.required]));
		this.formGroup.addControl("reconciliereCuExtrasBanca", new FormControl(null, []));
		this.formGroup.addControl("platit", new FormControl(null, []));
		this.formGroup.addControl("dataPlatii", new FormControl(null, []));
		this.formGroup.addControl("incadrareConformBVC", new FormControl(null, []));
		this.formGroup.addControl("intrareEmitere", new FormControl(null, []));
		this.formGroup.addControl("plataScadenta", new FormControl(null, []));
		this.formGroup.addControl("scadentaEmitere", new FormControl(null, []));
	}

	public ngOnInit(): void {
		if (this.isAdd()) {
			this.prepareForAdd();
		} else if (this.isView() || this.isEdit()) {
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
		this.prepareSelectItems();
		this.prepareNomenclators(["registru_facturi_tip_document", "monede"]);
		this.registruDocumentJustificativPlataId = null;
		this.registruDocumenteJustificativePlatiModel = null;
		this.readonly = false;
		this.incadrareConformBVCReadonly = false;
		this.tipDocumentReadonly = false;
		this.saveActionEnabled = true;
		this.enableOrDisableFormControl(this.dataPlatiiFormControl, false);
	}

	private prepareForViewOrEdit(): void {

		this.prepareSelectItems();
		this.tipDocumentReadonly = true;

		this.registruDocumenteJustificativePlatiService.getDocumentJustificativPlati(this.registruDocumentJustificativPlataId, {
			onSuccess: (registruDocumentJustificativPlati: RegistruDocumenteJustificativePlatiModel) => {
				this.registruDocumenteJustificativePlatiModel = registruDocumentJustificativPlati;

				this.prepareNomenclators(["registru_facturi_tip_document", "monede"]);

				if (this.platitFormControl.value) {
					this.enableOrDisableFormControl(this.dataPlatiiFormControl, true);
				} else {
					this.enableOrDisableFormControl(this.dataPlatiiFormControl, false);
				}

				this.saveActionEnabled = true;

				if (this.isView()) {
					this.readonly = true;
					this.incadrareConformBVCReadonly = true;
					this.enableOrDisableFormControl(this.dataInregistrareFormControl, false);
					this.enableOrDisableFormControl(this.dataDocumentFormControl, false);
					this.enableOrDisableFormControl(this.dataScadentaFormControl, false);
					this.enableOrDisableFormControl(this.dataPlatiiFormControl, false);
					this.enableOrDisableFormControl(this.reconciliereCuExtrasBancaFormControl, false);
					this.enableOrDisableFormControl(this.platitFormControl, false);
					this.saveActionEnabled = false;
				}
			},
			onFailure: (appError: AppError) => {
				this.loadingVisible = false;
				this.messageDisplayer.displayAppError(appError);
			}
		});
	}

	private prepareSelectItems(): void {
		this.prepareModLivrareItems();
		this.prepareModalitatePlataItems();
	}

	private prepareNomenclators(codes: string[]):void {
		this.nomenclatorService.getNomenclatorIdByCodeAsMap(codes, {
			onSuccess: (nomenclatorIdByCode: object): void => {
				if (ObjectUtils.isNotNullOrUndefined(nomenclatorIdByCode)) {
					
					this.prepareTipDocumentIds(nomenclatorIdByCode["registru_facturi_tip_document"]);
					
					this.tipDocumentFormControl.clearValidators();
					this.tipDocumentFormControl.setValue(new ValueOfNomenclatorValueField(nomenclatorIdByCode["registru_facturi_tip_document"]));
					this.tipDocumentFormControl.setValidators([NomenclatorValidators.nomenclatorValueRequired()]);
					
					this.monedaFormControl.clearValidators();
					this.monedaFormControl.setValue(new ValueOfNomenclatorValueField(nomenclatorIdByCode["monede"]));
					this.monedaFormControl.setValidators([NomenclatorValidators.nomenclatorValueRequired()]);
					
					this.loadingVisible = false;
					this.openWindow();

					if (this.isEdit() || this.isView()) {
						this.populateForm();
						this.calculateDataScadentaMinDate();

						if (this.registruDocumenteJustificativePlatiModel.plataScadenta <= 0) {
							this.plataScadentaStyle = {"background-color": "green" };
						} else if (this.registruDocumenteJustificativePlatiModel.plataScadenta > 0) {
							this.plataScadentaStyle = {"background-color": "red" };
						} else {
							this.plataScadentaStyle = null;
						}
					}
				}
			},
			onFailure: (error: AppError): void => {
				this.messageDisplayer.displayAppError(error);
			}
		});
	}

	public prepareTipDocumentIds(nomenclatorIdByCode: number): void {
		this.nomenclatorService.getNomenclatorValuesByNomenclatorId(nomenclatorIdByCode, {
			onSuccess: (tipDocumentNomenclatorValueModels: NomenclatorValueModel[]) => {
				tipDocumentNomenclatorValueModels.forEach((tipDocumentNomenclatorValueModel) => {
					if (tipDocumentNomenclatorValueModel.attribute1 === TipDocumentEnum.FACTURA_FISCALA) {
						this.tipDocumentFacturaId = tipDocumentNomenclatorValueModel.id;
					} else if (tipDocumentNomenclatorValueModel.attribute1 === TipDocumentEnum.PROFORMA) {
						this.tipDocumentProformaId = tipDocumentNomenclatorValueModel.id;
					} else if (tipDocumentNomenclatorValueModel.attribute1 === TipDocumentEnum.INSTIINTARE_DE_PLATA) {
						this.tipDocumentInstiintareDePlataId = tipDocumentNomenclatorValueModel.id;
					} else if (tipDocumentNomenclatorValueModel.attribute1 === TipDocumentEnum.CHITANTA) {
						this.tipDocumentChitantaId = tipDocumentNomenclatorValueModel.id;
					} else if (tipDocumentNomenclatorValueModel.attribute1 === TipDocumentEnum.BON_FISCAL) {
						this.tipDocumentBonFiscalId = tipDocumentNomenclatorValueModel.id;
					}
				});
		
				if (this.isEdit()) {
					this.enableOrDisableFormControl(this.tipDocumentFormControl, false);
					if (this.isTipDocumentChitanta(this.registruDocumenteJustificativePlatiModel.tipDocument.id) ||
						this.isTipDocumentBonFiscal(this.registruDocumenteJustificativePlatiModel.tipDocument.id)) {
							this.enableOrDisableFormControl(this.dataScadentaFormControl, false);
							this.enableOrDisableFormControl(this.platitFormControl, false);
					}
				}

				if (this.isEdit() && this.isTipDocumentInstiintareDePlata(this.registruDocumenteJustificativePlatiModel.tipDocument.id)) {
					this.readonly = true;
					this.incadrareConformBVCReadonly = false;
					this.enableOrDisableFormControl(this.dataInregistrareFormControl, false);
					this.enableOrDisableFormControl(this.dataDocumentFormControl, false);
					this.enableOrDisableFormControl(this.dataScadentaFormControl, false);
					this.enableOrDisableFormControl(this.dataPlatiiFormControl, false);
					this.enableOrDisableFormControl(this.reconciliereCuExtrasBancaFormControl, false);
				}
			},
			onFailure: (appError: AppError) => {
				this.messageDisplayer.displayAppError(appError);
			}
		});
	}

	private prepareModLivrareItems(): void {
		this.modLivrareItems = [
			{ label: this.translateUtils.translateLabel(ModLivrareEnum.POSTA), value: ModLivrareEnum.POSTA },
			{ label: this.translateUtils.translateLabel(ModLivrareEnum.EMAIL), value: ModLivrareEnum.EMAIL },
			{ label: this.translateUtils.translateLabel(ModLivrareEnum.CURIER), value: ModLivrareEnum.CURIER },
			{ label: this.translateUtils.translateLabel(ModLivrareEnum.LIVRARE_EMITENT), value: ModLivrareEnum.LIVRARE_EMITENT }
		];

		ListItemUtils.sortByLabel(this.modLivrareItems);
	}

	private prepareModalitatePlataItems(): void {
		this.modalitatePlataItems = [
			{ label: this.translateUtils.translateLabel(ModalitatePlataEnum.INTERNET_BANKING), value: ModalitatePlataEnum.INTERNET_BANKING },
			{ label: this.translateUtils.translateLabel(ModalitatePlataEnum.ORDIN_DE_PLATA), value: ModalitatePlataEnum.ORDIN_DE_PLATA },
			{ label: this.translateUtils.translateLabel(ModalitatePlataEnum.NUMERAR), value: ModalitatePlataEnum.NUMERAR },
			{ label: this.translateUtils.translateLabel(ModalitatePlataEnum.CARD), value: ModalitatePlataEnum.CARD },
			{ label: this.translateUtils.translateLabel(ModalitatePlataEnum.DIRECT_DEBIT), value: ModalitatePlataEnum.DIRECT_DEBIT }
		];
		
		ListItemUtils.sortByLabel(this.modalitatePlataItems);
	}

	private populateForm(): void {

		let tipDocumentValueField = new ValueOfNomenclatorValueField(this.registruDocumenteJustificativePlatiModel.tipDocument.nomenclatorId);
		tipDocumentValueField.value = this.registruDocumenteJustificativePlatiModel.tipDocument.id;

		let monedeValueField = new ValueOfNomenclatorValueField(this.registruDocumenteJustificativePlatiModel.moneda.nomenclatorId);
		monedeValueField.value = this.registruDocumenteJustificativePlatiModel.moneda.id;

		this.formGroup.setValue({
			dataInregistrare: this.registruDocumenteJustificativePlatiModel.dataInregistrare,
			emitent: this.registruDocumenteJustificativePlatiModel.emitent,
			tipDocument: tipDocumentValueField,
			numarDocument: this.registruDocumenteJustificativePlatiModel.numarDocument,
			dataDocument: this.registruDocumenteJustificativePlatiModel.dataDocument,
			modLivrare: this.registruDocumenteJustificativePlatiModel.modLivrare,
			detalii: this.registruDocumenteJustificativePlatiModel.detalii,
			valoare: this.registruDocumenteJustificativePlatiModel.valoare.toFixed(2),
			moneda: monedeValueField,
			dataScadenta: this.registruDocumenteJustificativePlatiModel.dataScadenta,
			modalitatePlata: this.registruDocumenteJustificativePlatiModel.modalitatePlata,
			reconciliereCuExtrasBanca: this.registruDocumenteJustificativePlatiModel.reconciliereCuExtrasBanca,
			platit: this.registruDocumenteJustificativePlatiModel.platit,
			dataPlatii: this.registruDocumenteJustificativePlatiModel.dataPlatii,
			incadrareConformBVC: this.registruDocumenteJustificativePlatiModel.incadrareConformBVC,
			intrareEmitere: this.registruDocumenteJustificativePlatiModel.intrareEmitere,
			plataScadenta: this.registruDocumenteJustificativePlatiModel.plataScadenta,
			scadentaEmitere: this.registruDocumenteJustificativePlatiModel.scadentaEmitere
		});
		this.uploadedAttachments = this.registruDocumenteJustificativePlatiModel.atasamente;
	}

	private enableOrDisableFormControl(formControl: FormControl, enable: boolean): void {
		FormUtils.enableOrDisableFormControl(formControl, enable);
	}

	private isValid(): boolean {
		return this.validate();
	}

	private validate(): boolean {
		FormUtils.validateAllFormFields(this.formGroup);
		return this.formGroup.valid;
	}

	public onSaveAction(): void {
		if (this.isValid()) {
			this.registruDocumenteJustificativePlatiModel = this.prepareDocumentJustificativPlatiModel();
			this.saveDocumentJustificativPlati();
		}
	}

	private prepareDocumentJustificativPlatiModel(): RegistruDocumenteJustificativePlatiModel {
		let documentJustificativPlatiModel: RegistruDocumenteJustificativePlatiModel = new RegistruDocumenteJustificativePlatiModel();
		if (ObjectUtils.isNotNullOrUndefined(this.registruDocumentJustificativPlataId)) {
			documentJustificativPlatiModel.id = this.registruDocumentJustificativPlataId;
			documentJustificativPlatiModel.numarInregistrare = this.registruDocumenteJustificativePlatiModel.numarInregistrare;
		}

		// TODO - De verificat daca se poate modifica dataInregistrare la Editare
		documentJustificativPlatiModel.dataInregistrare = this.dataInregistrareFormControl.value;
		documentJustificativPlatiModel.emitent = this.emitentFormControl.value.trim();
		documentJustificativPlatiModel.tipDocument = this.tipDocumentFormControl.value;
		documentJustificativPlatiModel.numarDocument = this.numarDocumentFormControl.value.trim();
		documentJustificativPlatiModel.dataDocument = this.dataDocumentFormControl.value;
		documentJustificativPlatiModel.modLivrare = this.modLivrareFormControl.value;
		documentJustificativPlatiModel.detalii = this.detaliiFormControl.value.trim();
		documentJustificativPlatiModel.valoare = parseFloat(this.valoareFormControl.value);

		documentJustificativPlatiModel.moneda = new NomenclatorValueModel();
		documentJustificativPlatiModel.moneda.id = this.monedaFormControl.value.value;
		documentJustificativPlatiModel.moneda.nomenclatorId = this.monedaFormControl.value.nomenclatorId;

		documentJustificativPlatiModel.tipDocument = new NomenclatorValueModel();
		documentJustificativPlatiModel.tipDocument.id = this.tipDocumentFormControl.value.value;
		documentJustificativPlatiModel.tipDocument.nomenclatorId = this.tipDocumentFormControl.value.nomenclatorId;

		documentJustificativPlatiModel.dataScadenta = this.dataScadentaFormControl.value;
		documentJustificativPlatiModel.modalitatePlata = this.modalitatePlataFormControl.value;
		documentJustificativPlatiModel.atasamente = this.uploadedAttachments;

		if (ObjectUtils.isNotNullOrUndefined(this.reconciliereCuExtrasBancaFormControl.value)) {
			documentJustificativPlatiModel.reconciliereCuExtrasBanca = this.reconciliereCuExtrasBancaFormControl.value;
		}
		if (ObjectUtils.isNotNullOrUndefined(this.platitFormControl.value)) {
			documentJustificativPlatiModel.platit = this.platitFormControl.value;
		}
		if (ObjectUtils.isNotNullOrUndefined(this.dataPlatiiFormControl.value)) {
			documentJustificativPlatiModel.dataPlatii = this.dataPlatiiFormControl.value;
		}
		if (ObjectUtils.isNotNullOrUndefined(this.formGroup.controls.incadrareConformBVC.value)) {
			documentJustificativPlatiModel.incadrareConformBVC = this.formGroup.controls.incadrareConformBVC.value.trim();
		}
		if (ObjectUtils.isNotNullOrUndefined(this.formGroup.controls.intrareEmitere.value)) {
			documentJustificativPlatiModel.intrareEmitere = this.formGroup.controls.intrareEmitere.value;
		}
		if (ObjectUtils.isNotNullOrUndefined(this.formGroup.controls.plataScadenta.value)) {
			documentJustificativPlatiModel.plataScadenta = this.formGroup.controls.plataScadenta.value;
		}
		if (ObjectUtils.isNotNullOrUndefined(this.formGroup.controls.scadentaEmitere.value)) {
			documentJustificativPlatiModel.scadentaEmitere = this.formGroup.controls.scadentaEmitere.value;
		}

		return documentJustificativPlatiModel;
	}

	private saveDocumentJustificativPlati(): void {		
		this.registruDocumenteJustificativePlatiService.saveDocumentJustificativPlati(this.registruDocumenteJustificativePlatiModel, {
			onSuccess: () => {
				this.messageDisplayer.displaySuccess("REGISTRU_DOCUMENTE_JUSTIFICATIVE_PLATI_SAVED_SUCCESSFULLY");
				this.closeWindow();
			},
			onFailure: (appError: AppError) => {
				this.messageDisplayer.displayAppError(appError);
			}
		});
	}

	public onHide(event: any): void {
		this.closeWindow();
	}

	public onCloseAction(): void {
		this.closeWindow();
	}

	private closeWindow(): void {
		this.windowVisible = false;
		this.windowClosed.emit();
	}

	public onTipDocumentValueChanged(event: any): void {
		if (this.isTipDocumentFactura(event.value) || this.isTipDocumentProforma(event.value) || this.isTipDocumentInstiintareDePlata(event.value)) {
			this.dataScadentaFormControl.reset();
			this.enableOrDisableFormControl(this.dataScadentaFormControl, true);
			this.platitFormControl.reset();
			this.enableOrDisableFormControl(this.platitFormControl, true);
			this.dataPlatiiFormControl.reset();
			this.dataPlatiiFormControl.clearValidators();
			this.enableOrDisableFormControl(this.dataPlatiiFormControl, false);
			this.valoareFormControl.setValidators([Validators.required]);
			this.valoareFormControl.updateValueAndValidity();
		} else if (this.isTipDocumentChitanta(event.value) || this.isTipDocumentBonFiscal(event.value)) {
			this.dataScadentaFormControl.reset();
			this.enableOrDisableFormControl(this.dataScadentaFormControl, false);
			this.platitFormControl.reset();
			this.platitFormControl.setValue(true);
			this.enableOrDisableFormControl(this.platitFormControl, false);
			this.dataPlatiiFormControl.reset();
			this.dataPlatiiFormControl.setValidators(Validators.required);
			this.enableOrDisableFormControl(this.dataPlatiiFormControl, false);
			this.valoareFormControl.setValidators([Validators.required, Validators.min(this.valoareMinima)]);
			this.valoareFormControl.updateValueAndValidity();
		}

		this.setDataPlata();
	}

	private isTipDocumentFactura(tipDocumentId: number): boolean {
		return tipDocumentId === this.tipDocumentFacturaId;
	}

	private isTipDocumentProforma(tipDocumentId: number): boolean {
		return tipDocumentId === this.tipDocumentProformaId;
	}

	private isTipDocumentInstiintareDePlata(tipDocumentId: number): boolean {
		return tipDocumentId === this.tipDocumentInstiintareDePlataId;
	}

	private isTipDocumentChitanta(tipDocumentId: number): boolean {
		return tipDocumentId === this.tipDocumentChitantaId;
	}

	private isTipDocumentBonFiscal(tipDocumentId: number): boolean {
		return tipDocumentId === this.tipDocumentBonFiscalId;
	}

	public onPlatitValueChanged(event: any): void {
		if (this.platitFormControl.value) {
			this.dataPlatiiFormControl.reset();
			this.dataPlatiiFormControl.setValidators(Validators.required);
			this.enableOrDisableFormControl(this.dataPlatiiFormControl, true);
			this.plataScadentaFormControl.reset();
			this.plataScadentaStyle = null;
		} else {
			if (this.dataScadentaFormControl.value != null) {
				this.dataPlatiiFormControl.setValue(this.dataScadentaFormControl.value);
				this.calculateDaysForPlataScadenta();
			}
			this.dataPlatiiFormControl.clearValidators();
			this.enableOrDisableFormControl(this.dataPlatiiFormControl, false);
		}
	}

	public onDataInregistrareValueChanged(dataInregistrare: Date): void {
		this.dataScadentaFormControl.reset();
		this.dataPlatiiFormControl.reset();
		this.plataScadentaFormControl.reset();
		this.dataDocumentFormControl.reset();
		this.calculateDaysForIntrareEmitere();
		this.calculateDataScadentaMinDate()
	}

	public onDataDocumentValueChanged(dataDocument: Date): void {
		this.calculateDaysForIntrareEmitere();
		this.calculateDaysForScadentaEmitere();
		this.setDataPlata();
	}

	public onValoareValueChanged(): void {
		this.valoareFormControl.setValue(parseFloat(this.valoareFormControl.value).toFixed(2));
	}

	public onDataScadentaValueChanged(dataScadenta: Date): void {
		if (!this.platitFormControl.value) {
			this.dataPlatiiFormControl.setValue(dataScadenta);
		}
		this.calculateDaysForPlataScadenta();
		this.calculateDaysForScadentaEmitere();
	}

	public onClearDataScadenta(event: any): void {
		if (!this.platitFormControl.value) {
			this.dataPlatiiFormControl.reset();
		}
	}

	public onDataPlatiiValueChanged(dataPlatii: Date): void {
		if (this.isTipDocumentChitanta(this.tipDocumentFormControl.value.value) || this.isTipDocumentBonFiscal(this.tipDocumentFormControl.value.value)) {
			this.dataScadentaFormControl.setValue(dataPlatii);
			this.onDataScadentaValueChanged(dataPlatii);
		}
		this.calculateDaysForPlataScadenta();
	}

	public onClearDataPlatii(): void {
		this.dataPlatiiFormControl.reset();
		this.plataScadentaFormControl.reset();
	}

	public onModalitatiPlataChange(event: any): void {
		if (event.value === ModalitatePlataEnum.NUMERAR) {
			this.reconciliereCuExtrasBancaFormControl.setValue(false);
		} else {
			this.reconciliereCuExtrasBancaFormControl.setValue(true);
		}		
	}

	public  calculateDataScadentaMinDate(): void {
		if (this.dataInregistrareFormControl.value == null) {
			this.dataScadentaMinDate = new Date();

		} else {
			let nextDayOfDataInregistare: Date = new Date();
			nextDayOfDataInregistare.setTime(this.dataInregistrareFormControl.value.getTime() + DateConstants.ONE_DAY_AS_MILISECONDS);
			this.dataScadentaMinDate = nextDayOfDataInregistare;
		}
		console.log("data scadenta minDate:", this.dataScadentaMinDate);
	}

	public get dataDocumentMaxDate(): Date {
		
		return this.dataInregistrareFormControl.value;
	}

	private calculateDaysForIntrareEmitere(): void {
		if (ObjectUtils.isNotNullOrUndefined(this.dataInregistrareFormControl.value) && ObjectUtils.isNotNullOrUndefined(this.dataDocumentFormControl.value)) {
			let dataInregistrare = this.dataInregistrareFormControl.value;
			let dataDocument = this.dataDocumentFormControl.value;
			let days: number = Math.round( (dataInregistrare.getTime() / DateConstants.ONE_DAY_AS_MILISECONDS - dataDocument.getTime() / DateConstants.ONE_DAY_AS_MILISECONDS ));
			this.intrareEmitereFormControl.setValue(days);
		} else {
			this.intrareEmitereFormControl.reset();
		}
	}

	private calculateDaysForPlataScadenta(): void {
		let days: number = null;
		if (ObjectUtils.isNotNullOrUndefined(this.dataScadentaFormControl.value)) {
			let dataScadenta = this.dataScadentaFormControl.value;
			if (this.platitFormControl.value && ObjectUtils.isNotNullOrUndefined(this.dataPlatiiFormControl.value)) {
				let dataPlatii = this.dataPlatiiFormControl.value;
				dataPlatii = DateUtils.removeTimeFromDate(dataPlatii);
				dataScadenta = DateUtils.removeTimeFromDate(dataScadenta);
				days = Math.round( (dataPlatii.getTime() - dataScadenta.getTime()) / DateConstants.ONE_DAY_AS_MILISECONDS );
			} else if (!this.platitFormControl.value) {
				days = Math.floor( (new Date().getTime() - dataScadenta.getTime()) / DateConstants.ONE_DAY_AS_MILISECONDS );
			}
			this.plataScadentaFormControl.setValue(days);
		} else {
			this.plataScadentaFormControl.reset();
		}

		if (days <= 0) {
			this.plataScadentaStyle = {"background-color": "green" };
		} else if (days > 0) {
			this.plataScadentaStyle = {"background-color": "red" };
		} else {
			this.plataScadentaStyle = null;
		}
	}

	private calculateDaysForScadentaEmitere(): void {
		if (ObjectUtils.isNotNullOrUndefined(this.dataScadentaFormControl.value) && ObjectUtils.isNotNullOrUndefined(this.dataDocumentFormControl.value)) {
			let dataScadenta = this.dataScadentaFormControl.value;
			let dataDocument = this.dataDocumentFormControl.value;
			let days: number = Math.round( (dataScadenta.getTime() / DateConstants.ONE_DAY_AS_MILISECONDS  - dataDocument.getTime() / DateConstants.ONE_DAY_AS_MILISECONDS ));
			this.scadentaEmitereFormControl.setValue(days);
		} else {
			this.scadentaEmitereFormControl.reset();
		}
	}

	private setDataPlata(): void{
		if (ObjectUtils.isNotNullOrUndefined(this.tipDocumentFormControl.value) && ObjectUtils.isNotNullOrUndefined(this.dataDocumentFormControl.value) ) {		
			
			let tipDocumentId: number = this.tipDocumentFormControl.value.value;
			let dataDocument: Date = this.dataDocumentFormControl.value; 

			if (this.isTipDocumentBonFiscal(tipDocumentId) || this.isTipDocumentChitanta(tipDocumentId)){
				this.dataPlatiiFormControl.setValue(dataDocument);
				this.onDataPlatiiValueChanged(dataDocument);						
			}  else {
				if (!this.platitFormControl.value) {
					this.dataPlatiiFormControl.setValue(null);
				}
			}
		} else {
			if (!this.platitFormControl.value) {
				this.dataPlatiiFormControl.setValue(null);
			}
		}
	}

	public onClearDataDocument(event: any): void {
		if (!this.platitFormControl.value) {
			this.dataPlatiiFormControl.setValue(null);
			this.dataDocumentFormControl.setValue(null);
		}
	}

	public onUpload(event): void {
				
		let nrOfUploads: number = event.files.length;
		if (nrOfUploads > 0) {
			this.lock();
		}

		for(let file of event.files) {

			if (!this.uploadedAttachmentsContainsAttachmentWithName(file.name)) {

				let formData:FormData = new FormData();
				formData.append("attachment", file, file.name);
				
				this.attachmentService.uploadFile(formData, {
					onSuccess: (attachment: AttachmentModel) => {
						let attachmentModel: RegistruDocumenteJustificativePlatiAtasamentModel = new RegistruDocumenteJustificativePlatiAtasamentModel();
						attachmentModel.fileName = attachment.name;
						this.uploadedAttachments.push(attachmentModel);
						nrOfUploads--;
						if (nrOfUploads === 0) {
							this.unlock();
						}
					},
					onFailure: (appError: AppError) => {
						nrOfUploads--;
						if (nrOfUploads === 0) {
							this.unlock();
						}
						this.messageDisplayer.displayAppError(appError);
					}
				});
			} else {
				nrOfUploads--;
				if (nrOfUploads === 0) {
					this.unlock();
				}
			}
		}
		
		this.resetSelectedAttachments();
	}

	private resetSelectedAttachments(): void {
		this.fileUpload.files = [];
	}

	private uploadedAttachmentsContainsAttachmentWithName(name: string): boolean {
		return ArrayUtils.isNotEmpty(
			this.uploadedAttachments.filter(
				attachment => attachment.fileName === name
			)
		);
	}

	public onRemoveAtasament(attachment: RegistruDocumenteJustificativePlatiAtasamentModel): void {	
		this.lock();
		this.attachmentService.deleteFile(attachment.fileName, {
			onSuccess: (): void => {
				ArrayUtils.removeElement(this.uploadedAttachments, attachment);
				this.unlock();
			}, 
			onFailure: (error: AppError): void => {
				this.unlock();
				this.messageDisplayer.displayAppError(error);
			}
		});
	}

	public onDownloadAtasament(attachment: RegistruDocumenteJustificativePlatiAtasamentModel): void {
		this.lock();
		if (ObjectUtils.isNullOrUndefined(attachment.id) ) {	
			this.attachmentService.downloadFile(attachment.fileName).subscribe(
				(response: HttpResponse<Blob>) => {
					this.unlock();
					DownloadUtils.saveFileFromResponse(response, attachment.fileName);
				}, 
				(error: any) => {
					this.unlock();
					this.messageDisplayer.displayError("DOWNLOAD_FILE_ERROR");
				}
			);
		} else {
			this.registruDocumenteJustificativePlatiService.downloadAtasamentById(attachment.id).subscribe(
				(response: HttpResponse<Blob>) => {
					this.unlock();
					DownloadUtils.saveFileFromResponse(response, attachment.fileName);
				}, 
				(error: any) => {
					this.unlock();
					this.messageDisplayer.displayError("DOWNLOAD_FILE_ERROR");
				}
			);
		}
	}

	public get fileUploadDisabled(): boolean {
		return this.isView();
	}

	public get dataInregistrareFormControl(): FormControl {
		return this.getControlByName("dataInregistrare");
	}
	
	public get emitentFormControl(): FormControl {
		return this.getControlByName("emitent");
	}
	
	public get tipDocumentFormControl(): FormControl {
		return this.getControlByName("tipDocument");
	}
	
	public get dataDocumentFormControl(): FormControl {
		return this.getControlByName("dataDocument");
	}
	
	public get numarDocumentFormControl(): FormControl {
		return this.getControlByName("numarDocument");
	}
	
	public get modLivrareFormControl(): FormControl {
		return this.getControlByName("modLivrare");
	}
	
	public get detaliiFormControl(): FormControl {
		return this.getControlByName("detalii");
	}
	
	public get valoareFormControl(): FormControl {
		return this.getControlByName("valoare");
	}
	
	public get monedaFormControl(): FormControl {
		return this.getControlByName("moneda");
	}
	
	public get dataScadentaFormControl(): FormControl {
		return this.getControlByName("dataScadenta");
	}
	
	public get modalitatePlataFormControl(): FormControl {
		return this.getControlByName("modalitatePlata");
	}
	
	public get reconciliereCuExtrasBancaFormControl(): FormControl {
		return this.getControlByName("reconciliereCuExtrasBanca");
	}
	
	public get platitFormControl(): FormControl {
		return this.getControlByName("platit");
	}
	
	public get dataPlatiiFormControl(): FormControl {
		return this.getControlByName("dataPlatii");
	}
	
	public get intrareEmitereFormControl(): FormControl {
		return this.getControlByName("intrareEmitere");
	}
	
	public get plataScadentaFormControl(): FormControl {
		return this.getControlByName("plataScadenta");
	}
	
	public get scadentaEmitereFormControl(): FormControl {
		return this.getControlByName("scadentaEmitere");
	}

	private getControlByName(controlName: string): FormControl {
		return <FormControl> this.formGroup.controls[controlName];
	}
}