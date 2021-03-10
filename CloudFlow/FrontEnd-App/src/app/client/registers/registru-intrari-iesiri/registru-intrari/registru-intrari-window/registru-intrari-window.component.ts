import { Component, Input, Output, EventEmitter, OnInit, ViewChild } from "@angular/core";
import { TranslateUtils, NomenclatorValidators, FormUtils, NomenclatorConstants, NomenclatorService, ObjectUtils, MessageDisplayer, 
	AppError, ValueOfNomenclatorValueField, DateConstants, StringUtils, NomenclatorSortedAttribute, BaseWindow, AdminPermissionEnum, 
	ConfirmationWindowFacade, StringValidators, NomenclatorMultipleFilter, DateUtils, ArrayUtils, ProjectService, ProjectModel, RegistruIntrariIesiriService, 
	RaspunsuriBanciCuPropuneriEnum, NomenclatorValueModel, AttachmentService, AttachmentModel, DownloadUtils, RegistruIntrariAtasamentModel, NomenclatorUtils, NomenclatorFilter, RegistruIesiriModel, RegistruIesiriDestinatariModel } from "@app/shared";
import { FormBuilder, FormGroup, FormControl, Validators } from "@angular/forms";
import { SelectItem, FileUpload, MultiSelect } from "primeng/primeng";
import { RegistruIntrariModel } from "@app/shared/model/registru-intrari-iesiri/registru-intrari.model";
import { RegistruIesiriFieldInputData } from "../../registru-iesiri/registru-iesiri-field/registru-iesiri-field.component";
import { HttpResponse } from "@angular/common/http";
import { ListItemUtils } from "@app/shared/utils/list-item-utils";
import { AuthManager } from "@app/shared/auth";
import { ProjectSubactivityModel } from "@app/shared/model/project/project-subactivity.model";

@Component({
	selector: "app-registru-intrari-window",
	templateUrl: "./registru-intrari-window.component.html"
})
export class RegistruIntrariWindowComponent extends BaseWindow implements OnInit {

	private static readonly DEFAULT_VALUE_FOR_NR_ZILE: number = 0;
	private static readonly NEGATIVE_VALUE_STYLE = {"background-color": "red", "color": "white"};
	private static readonly POSITIVE_VALUE_STYLE = {"background-color": "green", "color": "white"};
	private static readonly TIP_REGISTRU_INTRARI = "intrari";

	@Input()
	public registruId: number;

	@Input()
	public nomenclatorIdByCode: Map<string, number>;

	@Input()
	public mode: "add" | "edit" | "view";

	@Output()
	private windowClosed: EventEmitter<void>;

	private translateUtils: TranslateUtils;
	private formBuilder: FormBuilder;
	private registruIntrariIesiriService: RegistruIntrariIesiriService;
	private nomenclatorService: NomenclatorService;
	private projectService: ProjectService;
	private messageDisplayer: MessageDisplayer;
	private authManager: AuthManager;

	public registruIntrariModel: RegistruIntrariModel;
	
	public confirmationWindow: ConfirmationWindowFacade;
	public formGroup: FormGroup;
	public windowVisible: boolean = false;
	public title: string;
	public saveActionEnabled: boolean;

	public dateFormat: string;
	public yearRange: string;

	public comisiiSauGLNomenclatorValueFieldSelectionMode: string;

	public raspunsuriBanciCuPropuneriItems: SelectItem[];
	public proiecteItems: SelectItem[];
	public subproiecteItems: SelectItem[];

	public dataDocumentEmitentMaxDate: Date;
	public termenRaspunsMinDate: Date;

	public nrZileIntrareEmitentStyle: {};
	public nrZileRaspunsIntrareStyle: {};
	public nrZileRaspunsEmitentStyle: {};
	public nrZileTermenDataRaspunsStyle: {};

	public numeEmitentReadonly: boolean;
	public tipDocumentReadonly: boolean;
	public readonly: boolean;
	public comisiiGlReadonly: boolean;

	public emitentValueField: ValueOfNomenclatorValueField;
	public tipDocumentValueField: ValueOfNomenclatorValueField;
	public comisiiSauGLValueField: ValueOfNomenclatorValueField;

	public registruIesiriFieldInputData: RegistruIesiriFieldInputData;

	public codDocumentEchivalentCerereIesire: string = null;

	public widthDropdownProiect: string;

	public emitentExistentFilters: NomenclatorFilter[];

	@ViewChild(FileUpload)
	private fileUpload: FileUpload;

	public uploadedAttachments:RegistruIntrariAtasamentModel[];

	private attachmentService: AttachmentService;
	
	public valoareMinima: number = 0;
	public validationMessageMinValueParameter = {value: this.valoareMinima};
	public customSortedAttributes: NomenclatorSortedAttribute[] = [];

	public constructor(attachmentService: AttachmentService, translateUtils: TranslateUtils, formBuilder: FormBuilder, 
			registruIntrariIesiriService: RegistruIntrariIesiriService, nomenclatorService: NomenclatorService, 
			projectService: ProjectService, messageDisplayer: MessageDisplayer, authManager: AuthManager) {
		super();
		this.attachmentService = attachmentService;
		this.translateUtils = translateUtils;
		this.formBuilder = formBuilder;
		this.registruIntrariIesiriService = registruIntrariIesiriService;
		this.nomenclatorService = nomenclatorService;
		this.projectService = projectService;
		this.messageDisplayer = messageDisplayer;
		this.raspunsuriBanciCuPropuneriItems = [];
		this.proiecteItems = [];
		this.subproiecteItems = [];
		this.confirmationWindow = new ConfirmationWindowFacade();
		this.dateFormat = DateConstants.DATE_FORMAT_FOR_TYPING;
		this.yearRange = DateUtils.getDefaultYearRange();
		this.windowClosed = new EventEmitter<void>();
		this.authManager = authManager;
		this.emitentExistentFilters = new Array<NomenclatorMultipleFilter>();
		let radiatFilter: NomenclatorMultipleFilter = new NomenclatorMultipleFilter();
		radiatFilter.attributeKey = NomenclatorConstants.INSTITUTII_ATTRIBUTE_KEY_TIP_RADIAT;
		radiatFilter.values = [null, false];
		this.emitentExistentFilters.push(radiatFilter);
		this.lock();

	}

	public ngOnInit(): void {
		this.init();
	}

	private init(): void {
		this.prepareForm();
		this.prepareNomenclatorSortedAttributes();
		this.prepareNomenclatorValuesForNomenclatorValueSelectors();
		this.prepareProiecteItems();
		this.prepareRaspunsuriBanciCuPropuneriItems();

		this.comisiiSauGLNomenclatorValueFieldSelectionMode = "multiple";
		this.prepateByMode();

		FormUtils.enableOrDisableFormControl(this.necesitaRaspunsFormControl, false);

		this.uploadedAttachments = [];
	}

	private prepareForm(): void {
		this.formGroup = this.formBuilder.group([]);
		this.formGroup.addControl("dataInregistrare", new FormControl(null, []));
		this.formGroup.addControl("emitent", new FormControl(null, []));
		this.formGroup.addControl("numeEmitent", new FormControl(null, [Validators.required, StringValidators.blank()]));
		this.formGroup.addControl("departamentEmitent", new FormControl(null, []));
		this.formGroup.addControl("numarDocumentEmitent", new FormControl(null, [Validators.required]));
		this.formGroup.addControl("dataDocumentEmitent", new FormControl(null, [Validators.required]));
		this.formGroup.addControl("tipDocument", new FormControl(null, [NomenclatorValidators.nomenclatorValueRequired()]));
		this.formGroup.addControl("trimisPeMail", new FormControl(null, []));
		this.formGroup.addControl("continut", new FormControl(null, [Validators.required]));
		this.formGroup.addControl("numarPagini", new FormControl(null));
		this.formGroup.addControl("numarAnexe", new FormControl(null));
		this.formGroup.addControl("repartizatCatreUser", new FormControl(null, [Validators.required]));
		this.formGroup.addControl("comisiiSauGL", new FormControl(null, []));
		this.formGroup.addControl("proiecte", new FormControl(null, [Validators.required]));
		this.formGroup.addControl("subproiect", new FormControl(null, []));
		this.formGroup.addControl("necesitaRaspuns", new FormControl(null, []));
		this.formGroup.addControl("termenRaspuns", new FormControl(null, []));
		this.formGroup.addControl("numarInregistrareOfRegistruIesiri", new FormControl(null, []));
		this.formGroup.addControl("observatii", new FormControl(null, []));
		this.formGroup.addControl("raspunsuriBanciCuPropuneri", new FormControl(null, [Validators.required]));
		this.formGroup.addControl("nrZileIntrareEmitent", new FormControl(RegistruIntrariWindowComponent.DEFAULT_VALUE_FOR_NR_ZILE, []));
		this.formGroup.addControl("nrZileRaspunsIntrare", new FormControl(RegistruIntrariWindowComponent.DEFAULT_VALUE_FOR_NR_ZILE, []));
		this.formGroup.addControl("nrZileRaspunsEmitent", new FormControl(RegistruIntrariWindowComponent.DEFAULT_VALUE_FOR_NR_ZILE, []));
		this.formGroup.addControl("nrZileTermenDataRaspuns", new FormControl(RegistruIntrariWindowComponent.DEFAULT_VALUE_FOR_NR_ZILE, []));
	}

	private prepareRaspunsuriBanciCuPropuneriItems(): void {
		this.raspunsuriBanciCuPropuneriItems = [
			{label: this.translateUtils.translateLabel("YES"), value: RaspunsuriBanciCuPropuneriEnum.DA},
			{label: this.translateUtils.translateLabel("NO"), value: RaspunsuriBanciCuPropuneriEnum.NU},
			{label: this.translateUtils.translateLabel("NA"), value: RaspunsuriBanciCuPropuneriEnum.NA}
		];
	}

	private prepareNomenclatorValuesForNomenclatorValueSelectors():void {
		if (this.nomenclatorIdByCode){
			this.emitentFormControl.setValue(new ValueOfNomenclatorValueField(this.nomenclatorIdByCode.get(NomenclatorConstants.NOMENCLATOR_CODE_INSTITUTII)));
			this.tipDocumentFormControl.setValue(new ValueOfNomenclatorValueField(this.nomenclatorIdByCode.get(NomenclatorConstants.NOMENCLATOR_CODE_REGISTRU_INTRARI_TIP_DOCUMENT)));
			this.comisiiSauGLFormControl.setValue(new ValueOfNomenclatorValueField(this.nomenclatorIdByCode.get(NomenclatorConstants.NOMENCLATOR_CODE_COMISII_SAU_GL)));
			this.openWindow();
		}
		this.unlock();
	}

	private prepareNomenclatorSortedAttributes(): void {
		let sortedAttribute: NomenclatorSortedAttribute = new NomenclatorSortedAttribute();
		sortedAttribute.attributeKey = NomenclatorConstants.REGISTRU_INTRARI_TIP_DOCUMENT_ATTR_KEY_DENUMIRE;
		sortedAttribute.type = "ASC";
		this.customSortedAttributes.push(sortedAttribute);
	}

	private prepateByMode(): void {
		if (this.isAdd()) {
			this.prepareForAdd();
		} else if (this.isView() || this.isEdit()) {
			this.prepareForViewOrEdit();
		}
	}

	public isEdit(): boolean {
		return this.mode === "edit";
	}

	public isView(): boolean {
		return this.mode === "view";
	}

	public isAdd(): boolean {
		return this.mode === "add";
	}

	private prepareForAdd(): void {
		this.updatePerspectiveForAdd();
	}

	private updatePerspectiveForAdd(): void {
		this.setTitle();
		this.saveActionEnabled = true;
		let currentDate: Date = new Date();
		this.dataInregistrareFormControl.setValue(currentDate);
		this.raspunsuriBanciCuPropuneriFormControl.setValue(RaspunsuriBanciCuPropuneriEnum.NA);
		this.raspunsuriBanciCuPropuneriFormControl.updateValueAndValidity();
		this.numarPaginiFormControl.setValue(0);
		this.numarAnexeFormControl.setValue(0);
		this.numarAnexeFormControl.updateValueAndValidity();
		this.numarPaginiFormControl.updateValueAndValidity();
		FormUtils.enableOrDisableFormControl(this.dataInregistrareFormControl, false);
		FormUtils.enableOrDisableFormControl(this.numarInregistrareRegistruIesiriFormControl, false);
		this.numeEmitentFormControl.setValidators(Validators.required);
		this.dataDocumentEmitentMaxDate = this.dataInregistrareFormControl.value;
		this.termenRaspunsMinDate = this.dataInregistrareFormControl.value;
		this.nrZileIntrareEmitentStyle = RegistruIntrariWindowComponent.POSITIVE_VALUE_STYLE;
		this.nrZileRaspunsIntrareStyle = RegistruIntrariWindowComponent.POSITIVE_VALUE_STYLE;
		this.nrZileRaspunsEmitentStyle = RegistruIntrariWindowComponent.POSITIVE_VALUE_STYLE;
		this.nrZileTermenDataRaspunsStyle = RegistruIntrariWindowComponent.POSITIVE_VALUE_STYLE;
	}

	private setTitle(): void {
		let action: string = null;
		if (this.isAdd()) {
			let currentYear: number = new Date().getFullYear();
			action = this.translateUtils.translateLabel("ADD");
			this.registruIntrariIesiriService.getLastNrInregistrareByTipRegistruAndYear(RegistruIntrariWindowComponent.TIP_REGISTRU_INTRARI, currentYear, {
				onSuccess: (nrInregistrare: number) => {
					this.title += " - "+this.translateUtils.translateLabel("REGISTRU_IESIRI_NR_INREGISTRARE")+": "+(nrInregistrare+1)+" ~ "+currentYear;
				},
				onFailure: (appError: AppError) => {
					this.messageDisplayer.displayAppError(appError);
					this.unlock();
				}
			});
		} else if (this.isEdit()) {
			action = this.translateUtils.translateLabel("EDIT");	
		} else if (this.isView()) {
			action = this.translateUtils.translateLabel("VIEW");	
		}
		this.title = action;
	}

	private openWindow(): void {
		this.windowVisible = true;
	}

	private prepareForViewOrEdit(): void {
		this.getRegistruIntrariById();
	}

	private getRegistruIntrariById(): void {
		this.registruIntrariIesiriService.getRegistruIntrariById(this.registruId, {
			onSuccess: (registru: RegistruIntrariModel): void => {
				this.registruIntrariModel = registru;
				this.populateForm();
				this.updatePerspectiveForViewOrEdit();
			},
			onFailure: (appError: AppError): void => {
				this.unlock();
				this.messageDisplayer.displayAppError(appError);
			}
		});
	}

	private populateForm(): void {
		
		setTimeout( () => {

			this.emitentValueField = new ValueOfNomenclatorValueField(this.emitentFormControl.value.nomenclatorId);
			this.emitentValueField.value = this.registruIntrariModel.emitentId;
			this.tipDocumentValueField = new ValueOfNomenclatorValueField(this.tipDocumentFormControl.value.nomenclatorId);
			this.tipDocumentValueField.value = this.registruIntrariModel.tipDocumentId;
			this.comisiiSauGLValueField = new ValueOfNomenclatorValueField(this.comisiiSauGLFormControl.value.nomenclatorId);
			this.comisiiSauGLValueField.values = this.registruIntrariModel.comisieSauGLIds;

			this.formGroup.patchValue({
				emitent: this.emitentValueField,
				dataInregistrare: this.registruIntrariModel.dataInregistrare,
				numeEmitent: this.registruIntrariModel.numeEmitent,
				departamentEmitent: this.registruIntrariModel.departamentEmitent,
				numarDocumentEmitent: this.registruIntrariModel.numarDocumentEmitent,
				dataDocumentEmitent: this.registruIntrariModel.dataDocumentEmitent,
				tipDocument: this.tipDocumentValueField,
				trimisPeMail: this.registruIntrariModel.trimisPeMail,
				continut: this.registruIntrariModel.continut,
				numarPagini: this.registruIntrariModel.numarPagini,
				numarAnexe: this.registruIntrariModel.numarAnexe,
				repartizatCatreUser: this.registruIntrariModel.repartizatCatreUserId,
				comisiiSauGL: this.comisiiSauGLValueField,
				proiecte: this.registruIntrariModel.proiectIds,
				subproiect: this.registruIntrariModel.subactivity != null ? this.registruIntrariModel.subactivity.id : null,
				necesitaRaspuns: this.registruIntrariModel.necesitaRaspuns,
				termenRaspuns: this.registruIntrariModel.termenRaspuns,
				numarInregistrareOfRegistruIesiri: this.registruIntrariModel.numarInregistrareOfRegistruIesiri,
				observatii: this.registruIntrariModel.observatii,
				raspunsuriBanciCuPropuneri: this.registruIntrariModel.raspunsuriBanciCuPropuneri,
				nrZileIntrareEmitent: this.registruIntrariModel.nrZileIntrareEmitent,
				nrZileRaspunsIntrare: this.registruIntrariModel.nrZileRaspunsIntrare,
				nrZileRaspunsEmitent: this.registruIntrariModel.nrZileRaspunsEmitent,
				nrZileTermenDataRaspuns: this.registruIntrariModel.nrZileTermenDataRaspuns
			});
			this.prepareSubactivitySelectItems(this.registruIntrariModel.proiectIds);

			this.uploadedAttachments = this.registruIntrariModel.atasamente;
			this.onEmitentValueChanged();
		}, 500);
	}

	private updatePerspectiveForViewOrEdit(): void {

		if (this.isView()) {
			FormUtils.enableOrDisableFormControl(this.numarDocumentEmitentFormControl, false);
			FormUtils.enableOrDisableFormControl(this.dataDocumentEmitentFormControl, false);
			// TODO Cand gata Alin RegistruIesiri - dupa ce termina Alin RegistruIesiri adica peste 2 saptamani
			FormUtils.enableOrDisableFormControl(this.numarInregistrareRegistruIesiriFormControl, false);
			FormUtils.enableOrDisableFormControl(this.observatiiFormControl, false);
			FormUtils.enableOrDisableFormControl(this.raspunsuriBanciCuPropuneriFormControl, false);
			FormUtils.enableOrDisableFormControl(this.subproiectFormControl, false);
			this.tipDocumentReadonly = true;
		} else if (this.isEdit()) {
			this.saveActionEnabled = true;
			this.tipDocumentReadonly = true;
			this.dataDocumentEmitentMaxDate = this.registruIntrariModel.dataInregistrare;
		}

		this.setTitle();
		this.nrZileIntrareEmitentStyle = RegistruIntrariWindowComponent.POSITIVE_VALUE_STYLE;
		this.nrZileRaspunsIntrareStyle = RegistruIntrariWindowComponent.POSITIVE_VALUE_STYLE;
		this.nrZileRaspunsEmitentStyle = RegistruIntrariWindowComponent.POSITIVE_VALUE_STYLE;
		this.nrZileTermenDataRaspunsStyle = RegistruIntrariWindowComponent.POSITIVE_VALUE_STYLE;

		FormUtils.enableOrDisableFormControl(this.necesitaRaspunsFormControl, false);
		FormUtils.enableOrDisableFormControl(this.numeEmitentFormControl, false);
		this.readonly = true;
		this.comisiiGlReadonly = true;

		if ((this.isEdit() && !this.isElevatedEditPermissionAllowed()) || this.isView()){
			FormUtils.enableOrDisableFormControl(this.dataInregistrareFormControl, false);
			FormUtils.enableOrDisableFormControl(this.departamentEmitentFormControl, false);
			FormUtils.enableOrDisableFormControl(this.trimisPeMailFormControl, false);
			FormUtils.enableOrDisableFormControl(this.continutFormControl, false);
			FormUtils.enableOrDisableFormControl(this.numarPaginiFormControl, false);
			FormUtils.enableOrDisableFormControl(this.numarAnexeFormControl, false);
			FormUtils.enableOrDisableFormControl(this.repartizatCatreUserFormControl, false);
			FormUtils.enableOrDisableFormControl(this.proiecteFormControl, false);
			FormUtils.enableOrDisableFormControl(this.termenRaspunsFormControl, false);
			FormUtils.enableOrDisableFormControl(this.nrZileIntrareEmitentFormControl, false);
			FormUtils.enableOrDisableFormControl(this.nrZileRaspunsIntrareFormControl, false);
			FormUtils.enableOrDisableFormControl(this.nrZileRaspunsEmitentFormControl, false);
			FormUtils.enableOrDisableFormControl(this.nrZileTermenDataRaspunsFormControl, false);
		}

		if ( this.isEdit() && this.isElevatedEditPermissionAllowed() ){
			this.comisiiGlReadonly = false;
			if ( ObjectUtils.isNullOrUndefined(this.registruIntrariModel.numarInregistrareOfRegistruIesiri)){
				this.readonly = false;
				FormUtils.enableOrDisableFormControl(this.numeEmitentFormControl, true);
			}
		}

		FormUtils.enableOrDisableFormControl(this.numarInregistrareRegistruIesiriFormControl, false);

		if (this.registruIntrariModel.nrZileIntrareEmitent >= 0) {
			this.nrZileIntrareEmitentStyle = RegistruIntrariWindowComponent.POSITIVE_VALUE_STYLE;
		} else {
			this.nrZileIntrareEmitentStyle = RegistruIntrariWindowComponent.NEGATIVE_VALUE_STYLE;
		}

		if (this.registruIntrariModel.nrZileRaspunsEmitent >= 0) {
			this.nrZileRaspunsEmitentStyle = RegistruIntrariWindowComponent.POSITIVE_VALUE_STYLE;
		} else {
			this.nrZileRaspunsEmitentStyle = RegistruIntrariWindowComponent.NEGATIVE_VALUE_STYLE;
		}

		if (this.registruIntrariModel.nrZileRaspunsIntrare >= 0) {
			this.nrZileRaspunsIntrareStyle = RegistruIntrariWindowComponent.POSITIVE_VALUE_STYLE;
		} else {
			this.nrZileRaspunsIntrareStyle = RegistruIntrariWindowComponent.NEGATIVE_VALUE_STYLE;
		}

		if (this.registruIntrariModel.nrZileTermenDataRaspuns >= 0) {
			this.nrZileTermenDataRaspunsStyle = RegistruIntrariWindowComponent.POSITIVE_VALUE_STYLE;
		} else {
			this.nrZileTermenDataRaspunsStyle = RegistruIntrariWindowComponent.NEGATIVE_VALUE_STYLE;
		}
	}

	private prepareProiecteItems(): void {
		this.projectService.getAllProjects({
			onSuccess: (projectModels: ProjectModel[]): void => {
				projectModels.forEach(projectModel => {
					let proiectItem: SelectItem = {label: projectModel.name, value: projectModel.id};
					this.proiecteItems.push(proiectItem);
				});

				ListItemUtils.sortByLabel(this.proiecteItems);
			},
			onFailure: (appError: AppError): void => {
				this.messageDisplayer.displayAppError(appError);
			}
		});
	}

	private prepareRegistruIesiriFieldInputData(): void{
		if (ObjectUtils.isNotNullOrUndefined(this.codDocumentEchivalentCerereIesire)) {
			FormUtils.enableOrDisableFormControl(this.numarInregistrareRegistruIesiriFormControl, true);
			this.registruIesiriFieldInputData = new RegistruIesiriFieldInputData();
			this.registruIesiriFieldInputData.tipDocumentId = (<ValueOfNomenclatorValueField>this.tipDocumentFormControl.value).value;
			let emitentId = (<ValueOfNomenclatorValueField>this.emitentFormControl.value).value;
			if (ObjectUtils.isUndefined(emitentId)) {
				this.registruIesiriFieldInputData.emitentId = null;
			} else {
				this.registruIesiriFieldInputData.emitentId = emitentId;
			}
			this.registruIesiriFieldInputData.numeEmitent = this.numeEmitentFormControl.value;
			this.registruIesiriFieldInputData.isIesiriForIntrari = true;
		} else {
			FormUtils.enableOrDisableFormControl(this.numarInregistrareRegistruIesiriFormControl, false);
		}

	}

	public onRegistruIesiriChanged(registruIesiri: RegistruIesiriModel): void {
		if (this.isAdd()){
			let foundRecipient: RegistruIesiriDestinatariModel;
			let isEmitentExistent: boolean = this.numeEmitentFormControl.disabled;
			if (isEmitentExistent){
				foundRecipient = registruIesiri.destinatari.find(destinatar => destinatar.destinatarExistentId === (<ValueOfNomenclatorValueField>this.emitentFormControl.value).value);
			}else{
				foundRecipient = registruIesiri.destinatari.find(destinatar => destinatar.nume === this.numeEmitentFormControl.value);
			}
			this.continutFormControl.setValue(registruIesiri.continut);
			this.repartizatCatreUserFormControl.setValue(registruIesiri.intocmitDeUserId);
			this.proiecteFormControl.setValue(registruIesiri.proiectIds);
			this.prepareSubactivitySelectItems(registruIesiri.proiectIds);
			this.subproiectFormControl.setValue(!!registruIesiri.subactivity ? registruIesiri.subactivity.id : null);
			if (foundRecipient.comisieGlId){
				(<ValueOfNomenclatorValueField>this.comisiiSauGLFormControl.value).values = [foundRecipient.comisieGlId];
			}else{
				(<ValueOfNomenclatorValueField>this.comisiiSauGLFormControl.value).values = [];
			}
			this.comisiiSauGLFormControl.setValue(this.comisiiSauGLFormControl.value);
			this.observatiiFormControl.setValue(foundRecipient.observatii);
			this.continutFormControl.updateValueAndValidity();
			this.repartizatCatreUserFormControl.updateValueAndValidity();
			this.proiecteFormControl.updateValueAndValidity();
			this.subproiectFormControl.updateValueAndValidity();
			this.comisiiSauGLFormControl.updateValueAndValidity();
			this.observatiiFormControl.updateValueAndValidity();
			this.messageDisplayer.displayInfo("OUT_REGISTER_ENTRY_FIELDS_UPDATED_FROM_IN_REGISTER_ENTRY");
		}
	}
	
	public onEmitentValueChanged(): void {

		this.prepareRegistruIesiriFieldInputData();

		if (ArrayUtils.isNotEmpty(this.emitentFormControl.value.values)) {
			this.numeEmitentFormControl.reset();
			this.numeEmitentFormControl.clearValidators();
			FormUtils.enableOrDisableFormControl(this.numeEmitentFormControl, false);
		} else {
			this.numeEmitentReadonly = false;
			this.numeEmitentFormControl.setValidators([Validators.required, StringValidators.blank()]);
			FormUtils.enableOrDisableFormControl(this.numeEmitentFormControl, true);
		}
	}

	public onDataDocumentEmitentValueSelected(dataDocumentEmitent: Date): void {
		this.calculateNrZileIntrareEmitent();
	}

	public onDataDocumentEmitentValueCleared(event: any): void {
		this.nrZileIntrareEmitentFormControl.reset();
		this.calculateNrZileIntrareEmitent();
	}

	private calculateNrZileIntrareEmitent(): void {
		let days: number = null;
		if (ObjectUtils.isNotNullOrUndefined(this.dataDocumentEmitentFormControl.value)) {
			let dataInregistrare = this.dataInregistrareFormControl.value;
			let dataDocument = this.dataDocumentEmitentFormControl.value;
			days = Math.floor((dataInregistrare.getTime() - dataDocument.getTime()) / DateConstants.ONE_DAY_AS_MILISECONDS);
			this.nrZileIntrareEmitentFormControl.setValue(days);

			if (days >= 0) {
				this.nrZileIntrareEmitentStyle = RegistruIntrariWindowComponent.POSITIVE_VALUE_STYLE;
			} else if (days < 0) {
				this.nrZileIntrareEmitentStyle = RegistruIntrariWindowComponent.NEGATIVE_VALUE_STYLE;
			}
		} else {
			this.nrZileIntrareEmitentStyle = null;
		}
	}

	private calculateNrZileRaspunsIntrare(): void {
		// TODO - Dupa ce se implementeaza registru iesiri (depinde de registru iesiri) - dupa ce termina Alin RegistruIesiri adica peste 2 saptamani
	}

	private calculateNrZileRaspunsEmitent(): void {
		// TODO - Dupa ce se implementeaza registru iesiri (depinde de registru iesiri) - dupa ce termina Alin RegistruIesiri adica peste 2 saptamani
	}

	private calculateNrZileTermenDataRaspuns(): void {
		// TODO - Dupa ce se implementeaza registru iesiri (depinde de registru iesiri) - dupa ce termina Alin RegistruIesiri adica peste 2 saptamani
	}

	public onNecesitaRaspunsValueChanged(event: any): void {
		if (this.necesitaRaspunsFormControl.value) {
			this.termenRaspunsFormControl.setValidators(Validators.required);
			FormUtils.enableOrDisableFormControl(this.termenRaspunsFormControl, true);
		} else {
			this.termenRaspunsFormControl.clearValidators();
			this.termenRaspunsFormControl.reset();
			FormUtils.enableOrDisableFormControl(this.termenRaspunsFormControl, false);
		}
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
			let registruIntrariModel: RegistruIntrariModel = this.buildRegistruIntrariModel();
			this.saveRegistruIntrari(registruIntrariModel);
		}
	}

	private isValid(): boolean {
		return this.validate();
	}

	private validate(): boolean {
		FormUtils.validateAllFormFields(this.formGroup);
		return this.formGroup.valid;
	}

	private buildRegistruIntrariModel(): RegistruIntrariModel {

		let registruIntrariModel: RegistruIntrariModel = new RegistruIntrariModel();
		if (ObjectUtils.isNotNullOrUndefined(this.registruId)) {
			registruIntrariModel.id = this.registruId;
			registruIntrariModel.numarInregistrare = this.registruIntrariModel.numarInregistrare;
		}

		registruIntrariModel.dataInregistrare = this.dataInregistrareFormControl.value;

		if (ObjectUtils.isNotNullOrUndefined(this.emitentFormControl.value.value)) {
			registruIntrariModel.emitentId = this.emitentFormControl.value.value;
		} else if (ObjectUtils.isNotNullOrUndefined(this.numeEmitentFormControl.value)) {
			registruIntrariModel.numeEmitent = this.numeEmitentFormControl.value;
		}

		registruIntrariModel.departamentEmitent = this.departamentEmitentFormControl.value;
		registruIntrariModel.numarDocumentEmitent = this.numarDocumentEmitentFormControl.value;
		registruIntrariModel.dataDocumentEmitent = this.dataDocumentEmitentFormControl.value;
		registruIntrariModel.tipDocumentId = this.tipDocumentFormControl.value.value;
		registruIntrariModel.trimisPeMail = this.trimisPeMailFormControl.value;
		registruIntrariModel.continut = this.continutFormControl.value;
		registruIntrariModel.numarAnexe = !!this.numarAnexeFormControl.value ? this.numarAnexeFormControl.value : 0;
		registruIntrariModel.numarPagini = !!this.numarPaginiFormControl.value ? this.numarAnexeFormControl.value : 0;
		registruIntrariModel.repartizatCatreUserId = parseFloat(this.repartizatCatreUserFormControl.value);

		let comisieSauGLIds: number[] = new Array<number>();
		if (ArrayUtils.isNotEmpty(this.comisiiSauGLFormControl.value.values)) {
			this.comisiiSauGLFormControl.value.values.forEach(nomenclatorValueId => {
				comisieSauGLIds.push(nomenclatorValueId);
				registruIntrariModel.comisieSauGLIds = comisieSauGLIds;
			});
		}
		
		let proiectIds: number[] = new Array<number>();
		if (ArrayUtils.isNotEmpty(this.proiecteFormControl.value)) {
			this.proiecteFormControl.value.forEach(proiectId => {
				proiectIds.push(proiectId);
				registruIntrariModel.proiectIds = proiectIds;
			});
		}
		if (this.subproiectFormControl.value != null){
			let subactivityModel: ProjectSubactivityModel = new ProjectSubactivityModel();
			subactivityModel.id = this.subproiectFormControl.value;
			registruIntrariModel.subactivity = subactivityModel;
		}

		registruIntrariModel.necesitaRaspuns = this.necesitaRaspunsFormControl.value;
		registruIntrariModel.termenRaspuns = this.termenRaspunsFormControl.value;
		// TODO De mapat - Numere inregistrare iesiri - dupa ce termina Alin RegistruIesiri adica peste 2 saptamani
		registruIntrariModel.numarInregistrareOfRegistruIesiri = this.numarInregistrareRegistruIesiriFormControl.value;
		registruIntrariModel.observatii = this.observatiiFormControl.value;
		registruIntrariModel.raspunsuriBanciCuPropuneri = this.raspunsuriBanciCuPropuneriFormControl.value;
		registruIntrariModel.nrZileIntrareEmitent = this.nrZileIntrareEmitentFormControl.value;
		registruIntrariModel.nrZileRaspunsIntrare = this.nrZileRaspunsIntrareFormControl.value;
		registruIntrariModel.nrZileRaspunsEmitent = this.nrZileRaspunsEmitentFormControl.value;
		registruIntrariModel.nrZileTermenDataRaspuns = this.nrZileTermenDataRaspunsFormControl.value;

		registruIntrariModel.atasamente = this.uploadedAttachments;
		return registruIntrariModel;
	}

	private saveRegistruIntrari(registruIntrariModel: RegistruIntrariModel): void {
		this.registruIntrariIesiriService.saveRegistruIntrari(registruIntrariModel, {
			onSuccess: (): void => {
				this.messageDisplayer.displaySuccess("SAVED_SUCCESSFULLY");
				this.closeWindow();
			},
			onFailure: (appError: AppError): void => {
				this.messageDisplayer.displayAppError(appError);
			}
		});
	}

	private getProjectSubactivities(projectId: number): Promise<ProjectSubactivityModel[]> {
		return new Promise<ProjectSubactivityModel[]>((resolve, reject) => {
			this.projectService.getProjectSubactivities(projectId, {
				onSuccess: (subactivities: ProjectSubactivityModel[]): void => {
					resolve(subactivities);
				},onFailure: (error: AppError): void => {
					this.messageDisplayer.displayAppError(error);
					reject();
				}
			});
		});
	}
	
	public prepareSubactivitySelectItems(projectIds: Array<number>): void {
		if (projectIds.length === 1){
			this.getProjectSubactivities(projectIds[0])
				.then(subactivities => {
					this.subproiecteItems = [];
					subactivities.forEach(subactivity => {
						this.subproiecteItems.push({value: subactivity.id, label: subactivity.name});
					});
					if (this.isEdit() || this.isAdd()){
						this.subproiectFormControl.enable();
					}
				});
		}else{
			this.subproiectFormControl.patchValue(null);
			this.subproiecteItems = [];
			this.subproiectFormControl.disable();
		}
	}

	public onTipDocumentChanged(): void{		
		
		this.lock();
		
		
		if (!(<ValueOfNomenclatorValueField>this.tipDocumentFormControl.value).value) {		
			this.unlock();
			return;
		}
		
		this.nomenclatorService.getNomenclatorValue((<ValueOfNomenclatorValueField>this.tipDocumentFormControl.value).value, {
			onSuccess: (nomenclatorValue: NomenclatorValueModel): void => {
				this.codDocumentEchivalentCerereIesire = nomenclatorValue[NomenclatorConstants.REGISTRU_IESIRI_TIP_DOCUMENT_ATTR_KEY_CODE_ECHIVALENT_CERERE_INTRARE];
				let isTipDocumentAsteaptaRaspuns: Boolean = NomenclatorUtils.getFieldValueAsBoolean(nomenclatorValue, NomenclatorConstants.REGISTRU_IESIRI_TIP_DOCUMENT_ATTR_KEY_ASTEAPTA_RASPUNS);
				if (isTipDocumentAsteaptaRaspuns) {
					this.necesitaRaspunsFormControl.setValue(true);
				} else {
					this.necesitaRaspunsFormControl.setValue(false);
				}		
				if (StringUtils.isNotBlank(this.codDocumentEchivalentCerereIesire))	{
					this.numarInregistrareRegistruIesiriFormControl.setValidators(Validators.required);
				} else {
					this.numarInregistrareRegistruIesiriFormControl.clearValidators();

				}		
				this.prepareRegistruIesiriFieldInputData();	
				this.unlock();
			},
			onFailure: (appError: AppError): void => {
				this.messageDisplayer.displayAppError(appError);				
				this.prepareRegistruIesiriFieldInputData();
				this.unlock();
			}
		});		
	}
	
	private resetSelectedAttachments(): void {
		this.fileUpload.files = [];
	}

	public get fileUploadDisabled(): boolean {
		return this.isView();
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
						let attachmentModel: RegistruIntrariAtasamentModel = new RegistruIntrariAtasamentModel();
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

	private uploadedAttachmentsContainsAttachmentWithName(name: string): boolean {
		return ArrayUtils.isNotEmpty(
			this.uploadedAttachments.filter(
				attachment => attachment.fileName === name
			)
		);
	}

	public onDownloadAtasament(attachment: RegistruIntrariAtasamentModel): void {
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
			this.registruIntrariIesiriService.downloadAtasamentById(attachment.id).subscribe(
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

	public onRemoveAtasament(attachment: RegistruIntrariAtasamentModel): void {
		this.confirmationWindow.confirm({
			approve: (): void => {
				this.confirmationWindow.hide();
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
			},
			reject: (): void => {
				this.confirmationWindow.hide();
			}
		},"CONFIRM_DELETE_ATTACHMENT");
		
	}

	public onRemoveFile(file: File){
		this.confirmationWindow.confirm({
			approve: (): void => {
				this.confirmationWindow.hide();
			},
			reject: (): void => {
				this.fileUpload.files.push(file);
				this.confirmationWindow.hide();
			}
		},"CONFIRM_DELETE_ATTACHMENT");
	}

	public onClickProiect(): void {
		this.widthDropdownProiect = (document.getElementById('anexaId').clientWidth * 1) + "px";	
	}
	
	public onProiectSelectionChanged(event: any, multiSelect: MultiSelect){
		this.prepareSubactivitySelectItems(event.value);
		multiSelect.hide();
	}
	
	public get dataInregistrareFormControl(): FormControl {
		return this.getControlByName("dataInregistrare");
	}
	
	public get emitentFormControl(): FormControl {
		return this.getControlByName("emitent");
	}
	
	public get numeEmitentFormControl(): FormControl {
		return this.getControlByName("numeEmitent");
	}

	public get departamentEmitentFormControl(): FormControl {
		return this.getControlByName("departamentEmitent");
	}

	public get numarDocumentEmitentFormControl(): FormControl {
		return this.getControlByName("numarDocumentEmitent");
	}

	public get dataDocumentEmitentFormControl(): FormControl {
		return this.getControlByName("dataDocumentEmitent");
	}

	public get tipDocumentFormControl(): FormControl {
		return this.getControlByName("tipDocument");
	}

	public get trimisPeMailFormControl(): FormControl {
		return this.getControlByName("trimisPeMail");
	}

	public get continutFormControl(): FormControl {
		return this.getControlByName("continut");
	}

	public get numarPaginiFormControl(): FormControl {
		return this.getControlByName("numarPagini");
	}

	public get numarAnexeFormControl(): FormControl {
		return this.getControlByName("numarAnexe");
	}

	public get repartizatCatreUserFormControl(): FormControl {
		return this.getControlByName("repartizatCatreUser");
	}

	public get comisiiSauGLFormControl(): FormControl {
		return this.getControlByName("comisiiSauGL");
	}

	public get proiecteFormControl(): FormControl {
		return this.getControlByName("proiecte");
	}

	public get subproiectFormControl(): FormControl {
		return this.getControlByName("subproiect");
	}

	public get necesitaRaspunsFormControl(): FormControl {
		return this.getControlByName("necesitaRaspuns");
	}

	public get termenRaspunsFormControl(): FormControl {
		return this.getControlByName("termenRaspuns");
	}

	public get numarInregistrareRegistruIesiriFormControl(): FormControl {
		return this.getControlByName("numarInregistrareOfRegistruIesiri");
	}

	public get observatiiFormControl(): FormControl {
		return this.getControlByName("observatii");
	}

	public get raspunsuriBanciCuPropuneriFormControl(): FormControl {
		return this.getControlByName("raspunsuriBanciCuPropuneri");
	}

	public get nrZileIntrareEmitentFormControl(): FormControl {
		return this.getControlByName("nrZileIntrareEmitent");
	}

	public get nrZileRaspunsIntrareFormControl(): FormControl {
		return this.getControlByName("nrZileRaspunsIntrare");
	}

	public get nrZileRaspunsEmitentFormControl(): FormControl {
		return this.getControlByName("nrZileRaspunsEmitent");
	}

	public get nrZileTermenDataRaspunsFormControl(): FormControl {
		return this.getControlByName("nrZileTermenDataRaspuns");
	}

	private getControlByName(controlName: string): FormControl {
		return <FormControl> this.formGroup.controls[controlName];
	}

	isElevatedEditPermissionAllowed(): boolean {
		return this.authManager.hasPermission(AdminPermissionEnum.EDIT_REGISTRU_INTRARI_IESIRI_ELEVATED);
	}
}