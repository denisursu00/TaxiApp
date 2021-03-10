import { Component, Input, Output, OnInit, EventEmitter, AfterViewInit } from "@angular/core";
import { FormBuilder, FormGroup, AbstractControl } from "@angular/forms";
import { MetadataCollectionWindowContent } from "./metadata-collection-window-content";
import { AppError, MessageDisplayer, MetadataDefinitionModel, ArrayUtils, MetadataCollectionInstanceRowModel, MembruReprezentantiComisieSauGLModel, MembruReprezentantiComisieSauGLInfoModel } from "@app/shared"; 
import { NomenclatorUtils, PageConstants, DocumentConstants, NomenclatorService, PagingList, NomenclatorValueViewModel, NomenclatorSimpleFilter, NomenclatorValueAsViewSearchRequestModel, GetNomenclatorValuesRequestModel, PDialogMinimizer } from "@app/shared";  
import { BooleanUtils, DocumentTypeService, ComisieSauGLService, NomenclatorValueModel, WorkflowStateUtils, StringUtils, NomenclatorConstants } from "@app/shared"; 
import { FormUtils, Message, DocumentService, MetadataCollectionInstanceModel, DocumentValidationResponseModel } from "@app/shared";
import { 
	MetadataTextFormControl, 
	MetadataNumericFormControl,
	MetadataAutoNumberFormControl,
	MetadataDateFormControl,
	MetadataDateTimeFormControl,
	MetadataListFormControl,
	MetadataNomenclatorFormControl,
	MetadataTextAreaFormControl,
	MetadataUserFormControl,
	MetadataFormControl,
	MetadataGroupFormControl
} from "./../../document-metadata/metadata-form-controls";
import {
	IconConstants, 
	ListMetadataItemModel, 
	MetadataInstanceModel ,
	DocumentModel,
	ObjectUtils,
	DateUtils,
	WorkflowStateModel,
	DocumentCollectionValidationRequestModel
} from "@app/shared";
import { MetadataUtils } from "./../../document-metadata/metadata-utils";
import { MetadataEventMediator, MetadataEventName } from "./../../document-metadata/metadata-event-mediator";
import { DocumentMetadataInputData } from "./../../document-metadata/document-metadata.component";
import { MetadataCollectionWindowDynamicContentComponent } from "./metadata-collection-window-dynamic-content.component";
import { FormControl, Validators } from "@angular/forms";
import { SelectItem, Dialog } from "primeng/primeng";
import { ValidatorFn } from "@angular/forms/src/directives/validators";
import { SelectItemModel } from "@app/shared/select-item.model";

@Component({
	selector: "app-metadata-collection-informatii-participanti-of-prezenta-comisii-gl-content",
	templateUrl: "./metadata-collection-window-informatii-participanti-of-prezenta-comisii-gl-content.component.html"
})
export class MetadataCollectionWindowInformatiiParticipantiOfPrezentaComisiiGLContentComponent extends MetadataCollectionWindowContent implements OnInit, AfterViewInit {

	private static readonly METADATA_NAME_INSTITUTIE: string = "nume_institutie";
	private static readonly METADATA_NAME_MEMBRU_ACREDITAT: string = "membru_acreditat";
	private static readonly METADATA_NAME_NUME_MEMBRU_INLOCUITOR: string = "nume_membru_inlocuitor";
	private static readonly METADATA_NAME_PRENUME_MEMBRU_INLOCUITOR: string = "prenume_membru_inlocuitor";
	private static readonly METADATA_NAME_FUNCTIE_MEMBRU: string = "functie_membru";
	private static readonly METADATA_NAME_DEPARTAMENT: string = "departament";
	private static readonly METADATA_NAME_TELEFON: string = "telefon";
	private static readonly METADATA_NAME_EMAIL_PARTICIPANT: string = "adresa_email_participant";
	private static readonly METADATA_NAME_CALITATE_PARTICIPANT: string = "calitate_participant";
	private static readonly METADATA_CALITATE_PARTICIPANT_VALUE_INLOCUITOR: string = "inlocuitor";

	private comisiiSauGLService: ComisieSauGLService;
	private nomenclatorService: NomenclatorService;
	private messageDisplayer: MessageDisplayer;

	public formBuilder: FormBuilder;
	public formGroup: FormGroup;
	
	public institutieSelectItems: SelectItem[] = [];
	public membriAcreditati: MembruReprezentantiComisieSauGLInfoModel[] = [];	
	public calitateSelectItems: SelectItem[] = [];

	public selectMembruAcreditatDialogVisible: boolean = false;
	public selectionMembruAcreditat: MembruReprezentantiComisieSauGLInfoModel;
	public selectedMembruAcreditat: MembruReprezentantiComisieSauGLInfoModel;

	public selectMembruAcreditatEnabled: boolean = false;

	private metadataCollectionDefinition: MetadataDefinitionModel;
	private documentWorkflowState: WorkflowStateModel;

	public metadataEventMediator: MetadataEventMediator;

	private comisieGLId: number;
	public membriAcreditatiPageSize: number;
	private calitateReprezentantiByCaliateMap: object;

	private pDialogMinimizerSelectMembruAcreditatDialog: PDialogMinimizer;

	constructor(formBuilder: FormBuilder, comisiiSauGLService: ComisieSauGLService, nomenclatorService: NomenclatorService, messageDisplayer: MessageDisplayer) {
		super();
		this.comisiiSauGLService = comisiiSauGLService;
		this.nomenclatorService = nomenclatorService;
		this.messageDisplayer = messageDisplayer;
		this.formBuilder = formBuilder;
		this.formGroup = this.formBuilder.group([]);
		this.metadataEventMediator = new MetadataEventMediator();

		this.calitateReprezentantiByCaliateMap = {};
		this.calitateReprezentantiByCaliateMap["TITULAR"]= "titular";
		this.calitateReprezentantiByCaliateMap["SUPLEANT"]= "supleant";
		this.calitateReprezentantiByCaliateMap["INLOCUITOR"]= "inlocuitor";
	}

	public ngOnInit() {
		this.init();
	}

	public ngAfterViewInit(): void {
		setTimeout(() => {
			this.metadataEventMediator.fireEvent({
				eventName: MetadataEventName.ALL_METADATAS_READY
			});
		}, 0);		
	}

	private prepareDataForSelect(callback: () => any) {
		this.institutieSelectItems = [];
		this.membriAcreditati = [];
		if (ObjectUtils.isNotNullOrUndefined(this.inputData.documentFormDataProvider)) {
			this.lock();
			this.comisiiSauGLService.getAllInstitutiiOfMembriiComisieSaulGL(this.comisieGLId, {
				onSuccess: (institutii: NomenclatorValueModel[]): void => {
					if (ArrayUtils.isNotEmpty(institutii)) {
						institutii.forEach((institutie: NomenclatorValueModel) => {							
							this.institutieSelectItems.push({
								value: String(institutie.id),
								label: institutie[NomenclatorConstants.INSITUTII_ATTR_KEY_DENUMIRE_INSTITUTIE] 
							});
						});
					}
					if (this.isViewOrEdit()) {
						let institutieId: string = this.getMetadataValueByName(MetadataCollectionWindowInformatiiParticipantiOfPrezentaComisiiGLContentComponent.METADATA_NAME_INSTITUTIE);
						if (StringUtils.isNotBlank(institutieId)) {
							this.loadMembriReprezentantiComisieSauGLByInstitutie(this.comisieGLId, Number(institutieId), () => {
								this.unlock();
								callback();
							});
						} else {
							this.unlock();
							callback();
						}			
					} else {
						this.unlock();	
						callback();				
						
					}
				},
				onFailure: (error: AppError): void => {
					this.unlock();
					callback();
					this.messageDisplayer.displayAppError(error);
				}
			});
		} else {
			this.unlock();
			callback();
		}
		this.appendInstitutiiFromTipAlteInstitutii();
	}

	private appendInstitutiiFromTipAlteInstitutii(): void {
							
		let idTipInstitutieAlteInstitutii: number;
		let requestModelForTipInstitutii: GetNomenclatorValuesRequestModel = new GetNomenclatorValuesRequestModel();
		requestModelForTipInstitutii.nomenclatorCode = NomenclatorConstants.NOMENCLATOR_CODE_TIP_INSTITUTII;
		let nomenclatorTipInstitutiiFilter: NomenclatorSimpleFilter = new NomenclatorSimpleFilter();
		nomenclatorTipInstitutiiFilter.attributeKey = NomenclatorConstants.TIP_INSTITUTII_ATTR_KEY_TIP;
		nomenclatorTipInstitutiiFilter.value = NomenclatorConstants.TIP_INSTITUTII_TIP_ALTE_INSTITUTII;
		requestModelForTipInstitutii.filters = [];
		requestModelForTipInstitutii.filters.push(nomenclatorTipInstitutiiFilter);

		this.nomenclatorService.getNomenclatorValues(requestModelForTipInstitutii, {
			onSuccess: (result: NomenclatorValueModel[]): void => {		
				
				result.forEach((value: NomenclatorValueModel) => {
					idTipInstitutieAlteInstitutii = value.id;
				});
				
				let requestModel: GetNomenclatorValuesRequestModel = new GetNomenclatorValuesRequestModel();
				requestModel.nomenclatorCode = NomenclatorConstants.NOMENCLATOR_CODE_INSTITUTII;
				let nomenclatorFilter: NomenclatorSimpleFilter = new NomenclatorSimpleFilter();
				nomenclatorFilter.attributeKey = NomenclatorConstants.INSITUTII_ATTR_KEY_TIP_INSTITUTIE;
				nomenclatorFilter.value = idTipInstitutieAlteInstitutii;
				requestModel.filters = [];
				requestModel.filters.push(nomenclatorFilter);
		
				this.nomenclatorService.getNomenclatorValues(requestModel, {
					onSuccess: (result: NomenclatorValueModel[]): void => {		
						
						result.forEach((value: NomenclatorValueModel) => {
							
							this.institutieSelectItems.push( {
								label: value[NomenclatorConstants.INSITUTII_ATTR_KEY_DENUMIRE_INSTITUTIE],
								value: "" + value.id
							});
						});
		
						this.unlock();
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
		
	}

	private loadMembriReprezentantiComisieSauGLByInstitutie(comisieGLId: number, institutieId: number, callback: () => any): void {
		this.comisiiSauGLService.getMembriiReprezentantiComisieSauGLByInstitutie(comisieGLId, institutieId, {
			onSuccess: (membriiInfo: MembruReprezentantiComisieSauGLInfoModel[]): void => {
				this.membriAcreditati = membriiInfo;
				callback();
			},
			onFailure: (error: AppError): void => {
				callback();
				this.messageDisplayer.displayAppError(error);
			}
		});
	}

	private prepareCalitati(): void {
		this.calitateSelectItems = [];
		let calitateMetDef: MetadataDefinitionModel = this.getMetadataDefinitionByName(MetadataCollectionWindowInformatiiParticipantiOfPrezentaComisiiGLContentComponent.METADATA_NAME_CALITATE_PARTICIPANT);		
		calitateMetDef.listItems.forEach((listItem: ListMetadataItemModel) => {
			this.calitateSelectItems.push({
				value: listItem.value,
				label: listItem.label
			});
		});
	}

	private getMetadataDefinitionByName(metadataName: string): MetadataDefinitionModel {
		let returnMetDef: MetadataDefinitionModel = null;
		this.metadataCollectionDefinition.metadataDefinitions.forEach((metDef: MetadataDefinitionModel) => {
			if (metDef.name === metadataName) {
				returnMetDef = metDef;
			}
		});
		return returnMetDef;
	}

	private getMetadataValueByName(metadataName: string) {
		let metadataDefinition: MetadataDefinitionModel = this.getMetadataDefinitionByName(metadataName);
		if (ObjectUtils.isNotNullOrUndefined(this.inputData.collectionInstanceRow)) {
			let metadataInstanceList: MetadataInstanceModel[] = this.inputData.collectionInstanceRow.metadataInstanceList;
			let metadataValues: string[] = null;
			metadataInstanceList.forEach((metadataInstance: MetadataInstanceModel) => {
				if (metadataInstance.metadataDefinitionId === metadataDefinition.id) {
					metadataValues = metadataInstance.values;
				}
			});
			if (ArrayUtils.isNotEmpty(metadataValues)) {
				return metadataValues[0];
			}
		}
		return null;
	}

	private init(): void {

		this.membriAcreditatiPageSize = PageConstants.DEFAULT_PAGE_SIZE;
		
		this.metadataCollectionDefinition = this.inputData.metadataCollectionDefinition;
		
		this.formGroup.addControl("institutie", new FormControl());
		this.formGroup.addControl("numeMembruInlocuitor", new FormControl());
		this.formGroup.addControl("prenumeMembruInlocuitor", new FormControl());
		this.formGroup.addControl("membruAcreditat", new FormControl(null, [InformatiiParticipantiValidators.membruAcreditatOrInlocuitorValidator({
			getNumeMebruInlocuitor: (): string => {
				let value: any = this.numeMembruInlocuitorFormControl.value;
				return ObjectUtils.isNotNullOrUndefined(value) ? String(value) : null;
			},
			getPrenumeMebruInlocuitor: (): string => {
				let value: any = this.prenumeMembruInlocuitorFormControl.value;
				return ObjectUtils.isNotNullOrUndefined(value) ? String(value) : null;
			}
		})]));
		this.formGroup.addControl("functieMembru", new FormControl());
		this.formGroup.addControl("departament", new FormControl());
		this.formGroup.addControl("telefon", new FormControl());
		this.formGroup.addControl("adresaEmailParticipant", new FormControl());
		this.formGroup.addControl("calitateParticipant", new FormControl());

		let institutieMetadataDefinition: MetadataDefinitionModel = this.getMetadataDefinitionByName(MetadataCollectionWindowInformatiiParticipantiOfPrezentaComisiiGLContentComponent.METADATA_NAME_INSTITUTIE);
		if (StringUtils.isNotBlank(institutieMetadataDefinition.mandatoryStates)) {
			if (WorkflowStateUtils.isStateFound(institutieMetadataDefinition.mandatoryStates, this.inputData.documentWorkflowState.code)) {
				this.institutieFormControl.setValidators([Validators.required]);
			}
		}

		let functieMembruMetadataDefinition: MetadataDefinitionModel = this.getMetadataDefinitionByName(MetadataCollectionWindowInformatiiParticipantiOfPrezentaComisiiGLContentComponent.METADATA_NAME_FUNCTIE_MEMBRU);
		if (StringUtils.isNotBlank(functieMembruMetadataDefinition.mandatoryStates)) {
			if (WorkflowStateUtils.isStateFound(functieMembruMetadataDefinition.mandatoryStates, this.inputData.documentWorkflowState.code)) {
				this.functieMembruFormControl.setValidators([Validators.required]);
			}
		}

		this.prepareCalitati();

		let rawComisieGlId: any = this.inputData.documentFormDataProvider.getMetadataValue(DocumentConstants.METADATA_NAME_DENUMIRE_COMISIE_GL_OF_PREZENTA_COMISII_GL);
		if (ObjectUtils.isNullOrUndefined(rawComisieGlId)) {
			return;
		}
		this.comisieGLId = Number(rawComisieGlId);

		this.prepareDataForSelect(() => {
			if (this.isViewOrEdit()) {

				let institutieId: string = this.getMetadataValueByName(MetadataCollectionWindowInformatiiParticipantiOfPrezentaComisiiGLContentComponent.METADATA_NAME_INSTITUTIE);			
				this.institutieFormControl.setValue(institutieId);

				let membruAcreditat: string = this.getMetadataValueByName(MetadataCollectionWindowInformatiiParticipantiOfPrezentaComisiiGLContentComponent.METADATA_NAME_MEMBRU_ACREDITAT);			
				if (StringUtils.isNotBlank(membruAcreditat)) {
					this.membruAcreditatFormControl.setValue(membruAcreditat);
				} else {
					let numeMembruInlocuitor: string = this.getMetadataValueByName(MetadataCollectionWindowInformatiiParticipantiOfPrezentaComisiiGLContentComponent.METADATA_NAME_NUME_MEMBRU_INLOCUITOR);			
					this.numeMembruInlocuitorFormControl.setValue(numeMembruInlocuitor);

					let prenumeMembruInlocuitor: string = this.getMetadataValueByName(MetadataCollectionWindowInformatiiParticipantiOfPrezentaComisiiGLContentComponent.METADATA_NAME_PRENUME_MEMBRU_INLOCUITOR);			
					this.prenumeMembruInlocuitorFormControl.setValue(prenumeMembruInlocuitor);
				}

				let functieMembruInlocuitor: string = this.getMetadataValueByName(MetadataCollectionWindowInformatiiParticipantiOfPrezentaComisiiGLContentComponent.METADATA_NAME_FUNCTIE_MEMBRU);			
				this.functieMembruFormControl.setValue(functieMembruInlocuitor);

				let departament: string = this.getMetadataValueByName(MetadataCollectionWindowInformatiiParticipantiOfPrezentaComisiiGLContentComponent.METADATA_NAME_DEPARTAMENT);			
				this.departamentFormControl.setValue(departament);

				let telefon: string = this.getMetadataValueByName(MetadataCollectionWindowInformatiiParticipantiOfPrezentaComisiiGLContentComponent.METADATA_NAME_TELEFON);			
				this.telefonFormControl.setValue(telefon);

				let emailParticipant: string = this.getMetadataValueByName(MetadataCollectionWindowInformatiiParticipantiOfPrezentaComisiiGLContentComponent.METADATA_NAME_EMAIL_PARTICIPANT);			
				this.adresaEmailParticipantFormControl.setValue(emailParticipant);

				let calitateParticipant: string = this.getMetadataValueByName(MetadataCollectionWindowInformatiiParticipantiOfPrezentaComisiiGLContentComponent.METADATA_NAME_CALITATE_PARTICIPANT);			
				this.calitateParticipantFormControl.setValue(calitateParticipant);
			}
			this.membruAcreditatFormControl.updateValueAndValidity();

			this.changePerspective();
		});
	}

	public get institutieOptionLabel(): string {
		return NomenclatorConstants.INSITUTII_ATTR_KEY_DENUMIRE_INSTITUTIE;
	}

	public onInstitutieValueChanged(event: any): void {
		this.doWhenInstitutieValueChanged();
	}

	private doWhenInstitutieValueChanged(): void {		
		let institutieId: string = this.institutieFormControl.value;
		if (ObjectUtils.isNotNullOrUndefined(institutieId)) {
			this.lock();
			this.loadMembriReprezentantiComisieSauGLByInstitutie(this.comisieGLId, Number(institutieId), () => {
				this.unlock();
			});
		}
		let membruAcreditat: string = this.membruAcreditatFormControl.value;
		if (ObjectUtils.isNotNullOrUndefined(membruAcreditat)) {
			this.selectedMembruAcreditat = null;
			this.membruAcreditatFormControl.reset();
			this.doWhenMembruAcreditatValueChanged();
		} else {
			this.changePerspective();
		}
	}

	public onMembruAcreditatValueChanged(event: any): void {
		this.doWhenMembruAcreditatValueChanged();
	}

	private doWhenMembruAcreditatValueChanged(): void {

		this.membruAcreditatFormControl.reset();
		this.numeMembruInlocuitorFormControl.reset();
		this.prenumeMembruInlocuitorFormControl.reset();
		this.functieMembruFormControl.reset();
		this.departamentFormControl.reset();
		this.telefonFormControl.reset();
		this.adresaEmailParticipantFormControl.reset();
		this.calitateParticipantFormControl.reset();

		if (ObjectUtils.isNotNullOrUndefined(this.selectedMembruAcreditat)) {
			
			let membruAcreditat: string = this.selectedMembruAcreditat.nume + " " + this.selectedMembruAcreditat.prenume;
			this.membruAcreditatFormControl.setValue(membruAcreditat);

			this.functieMembruFormControl.setValue(this.selectedMembruAcreditat.functie);
			this.departamentFormControl.setValue(this.selectedMembruAcreditat.departament);
			this.telefonFormControl.setValue(this.selectedMembruAcreditat.telefon);
			this.adresaEmailParticipantFormControl.setValue(this.selectedMembruAcreditat.email);
			this.calitateParticipantFormControl.setValue(this.calitateReprezentantiByCaliateMap[this.selectedMembruAcreditat.calitate]);		
		}
		this.changePerspective();
	}

	private changePerspective(): void {
		if (StringUtils.isBlank(this.membruAcreditatFormControl.value)) {
			this.numeMembruInlocuitorFormControl.enable();
			this.prenumeMembruInlocuitorFormControl.enable();
		} else {
			this.numeMembruInlocuitorFormControl.disable();
			this.prenumeMembruInlocuitorFormControl.disable();
		}
		this.selectMembruAcreditatEnabled = ObjectUtils.isNotNullOrUndefined(this.institutieFormControl.value);
	}

	public onCalitateParticipantValueChanged(event: any): void {
	}

	public getMetadataInstances(): MetadataInstanceModel[] {
		
		let metadataInstances: MetadataInstanceModel[] = [];

		let metadataName: string = MetadataCollectionWindowInformatiiParticipantiOfPrezentaComisiiGLContentComponent.METADATA_NAME_INSTITUTIE;
		this.prepareAndAddMetadataInstance(metadataName, this.institutieFormControl, metadataInstances);

		metadataName = MetadataCollectionWindowInformatiiParticipantiOfPrezentaComisiiGLContentComponent.METADATA_NAME_MEMBRU_ACREDITAT;
		this.prepareAndAddMetadataInstance(metadataName, this.membruAcreditatFormControl, metadataInstances);
		
		metadataName = MetadataCollectionWindowInformatiiParticipantiOfPrezentaComisiiGLContentComponent.METADATA_NAME_NUME_MEMBRU_INLOCUITOR;
		this.prepareAndAddMetadataInstance(metadataName, this.numeMembruInlocuitorFormControl, metadataInstances);

		metadataName = MetadataCollectionWindowInformatiiParticipantiOfPrezentaComisiiGLContentComponent.METADATA_NAME_PRENUME_MEMBRU_INLOCUITOR;
		this.prepareAndAddMetadataInstance(metadataName, this.prenumeMembruInlocuitorFormControl, metadataInstances);

		metadataName = MetadataCollectionWindowInformatiiParticipantiOfPrezentaComisiiGLContentComponent.METADATA_NAME_FUNCTIE_MEMBRU;
		this.prepareAndAddMetadataInstance(metadataName, this.functieMembruFormControl, metadataInstances);

		metadataName = MetadataCollectionWindowInformatiiParticipantiOfPrezentaComisiiGLContentComponent.METADATA_NAME_DEPARTAMENT;
		this.prepareAndAddMetadataInstance(metadataName, this.departamentFormControl, metadataInstances);

		metadataName = MetadataCollectionWindowInformatiiParticipantiOfPrezentaComisiiGLContentComponent.METADATA_NAME_TELEFON;
		this.prepareAndAddMetadataInstance(metadataName, this.telefonFormControl, metadataInstances);

		metadataName = MetadataCollectionWindowInformatiiParticipantiOfPrezentaComisiiGLContentComponent.METADATA_NAME_EMAIL_PARTICIPANT;
		this.prepareAndAddMetadataInstance(metadataName, this.adresaEmailParticipantFormControl, metadataInstances);

		metadataName = MetadataCollectionWindowInformatiiParticipantiOfPrezentaComisiiGLContentComponent.METADATA_NAME_CALITATE_PARTICIPANT;
		this.prepareAndAddMetadataInstance(metadataName, this.calitateParticipantFormControl, metadataInstances);

		return metadataInstances;
	}

	private prepareAndAddMetadataInstance(metadataName: string, formControl: FormControl, metadataInstances: MetadataInstanceModel[]): void {
		let metDef: MetadataDefinitionModel = this.getMetadataDefinitionByName(metadataName);	
		let value: any = formControl.value;
		if (ObjectUtils.isNotNullOrUndefined(value)) {		
			let metadataInstance: MetadataInstanceModel = new MetadataInstanceModel();
			metadataInstance.metadataDefinitionId = metDef.id;
			let valueAsString: string = null;
			if (ObjectUtils.isNumber(value)) {
				valueAsString = String(value);
			} else if (ObjectUtils.isString(value)) {
				valueAsString = value;
			} else {
				throw new Error("control value type unknown");
			}
			metadataInstance.values = [valueAsString];
			metadataInstances.push(metadataInstance);
		}	
	}
		
	public get institutieLabel(): string {
		return this.getMetadataLabel(MetadataCollectionWindowInformatiiParticipantiOfPrezentaComisiiGLContentComponent.METADATA_NAME_INSTITUTIE);
	}

	public get membruAcreditatLabel(): string {
		return this.getMetadataLabel(MetadataCollectionWindowInformatiiParticipantiOfPrezentaComisiiGLContentComponent.METADATA_NAME_MEMBRU_ACREDITAT);
	}

	public get numeMembruInlocuitorLabel(): string {
		return this.getMetadataLabel(MetadataCollectionWindowInformatiiParticipantiOfPrezentaComisiiGLContentComponent.METADATA_NAME_NUME_MEMBRU_INLOCUITOR);
	}

	public get prenumeMembruInlocuitorLabel(): string {
		return this.getMetadataLabel(MetadataCollectionWindowInformatiiParticipantiOfPrezentaComisiiGLContentComponent.METADATA_NAME_PRENUME_MEMBRU_INLOCUITOR);
	}

	public get functieMembruLabel(): string {
		return this.getMetadataLabel(MetadataCollectionWindowInformatiiParticipantiOfPrezentaComisiiGLContentComponent.METADATA_NAME_FUNCTIE_MEMBRU);
	}

	public get departamentLabel(): string {
		return this.getMetadataLabel(MetadataCollectionWindowInformatiiParticipantiOfPrezentaComisiiGLContentComponent.METADATA_NAME_DEPARTAMENT);
	}

	public get telefonLabel(): string {
		return this.getMetadataLabel(MetadataCollectionWindowInformatiiParticipantiOfPrezentaComisiiGLContentComponent.METADATA_NAME_TELEFON);
	}

	public get adresaEmailParticipantLabel(): string {
		return this.getMetadataLabel(MetadataCollectionWindowInformatiiParticipantiOfPrezentaComisiiGLContentComponent.METADATA_NAME_EMAIL_PARTICIPANT);
	}

	public get calitateParticipantLabel(): string {
		return this.getMetadataLabel(MetadataCollectionWindowInformatiiParticipantiOfPrezentaComisiiGLContentComponent.METADATA_NAME_CALITATE_PARTICIPANT);
	}

	private getMetadataLabel(metadataName: string): string {
		let label: string = null;
		this.metadataCollectionDefinition.metadataDefinitions.forEach((metadataDefinition: MetadataDefinitionModel) => {
			if (metadataDefinition.name === metadataName) {
				label = metadataDefinition.label;
			}
		});
		return label;
	}

	public get institutieFormControl(): FormControl { 
		return <FormControl> this.formGroup.get("institutie");	
	}

	public get membruAcreditatFormControl(): FormControl { 
		return <FormControl> this.formGroup.get("membruAcreditat");	
	}

	public get numeMembruInlocuitorFormControl(): FormControl { 
		return <FormControl> this.formGroup.get("numeMembruInlocuitor");	
	}

	public get prenumeMembruInlocuitorFormControl(): FormControl { 
		return <FormControl> this.formGroup.get("prenumeMembruInlocuitor");	
	}

	public get functieMembruFormControl(): FormControl{ 
		return <FormControl> this.formGroup.get("functieMembru");	
	}
	
	public get departamentFormControl(): FormControl { 
		return <FormControl> this.formGroup.get("departament");	
	}

	public get telefonFormControl(): FormControl { 
		return <FormControl> this.formGroup.get("telefon");	
	}
	
	public get adresaEmailParticipantFormControl(): FormControl{ 
		return <FormControl> this.formGroup.get("adresaEmailParticipant");	
	}

	public get calitateParticipantFormControl(): FormControl { 
		return <FormControl> this.formGroup.get("calitateParticipant");	
	}

	public validate(): boolean {
		FormUtils.validateAllFormFields(this.formGroup);
		return this.formGroup.valid;
	}

	public onShowSelectMembruAcreditatDialog(event: any, pDialog: Dialog): void {
		this.pDialogMinimizerSelectMembruAcreditatDialog = new PDialogMinimizer(pDialog);
		pDialog.center();
		setTimeout(() => {
			pDialog.maximize();
		}, 0);
	}
	
	public onToggleMinimizeSelectMembruAcreditatDialog(pDialog: Dialog): void {
		this.pDialogMinimizerSelectMembruAcreditatDialog.toggleMinimize();		
	}

	public get selectMembruAcreditatDialogMinimized(): boolean {
		if (ObjectUtils.isNullOrUndefined(this.pDialogMinimizerSelectMembruAcreditatDialog)) {
			return false;
		}
		return this.pDialogMinimizerSelectMembruAcreditatDialog.minimized;
	}

	public onHideSelectMembruAcreditatDialog(event: any): void {
		this.selectMembruAcreditatDialogVisible = false;
	}

	public onMembruAcreditatSelected(event: any): void {
	}

	public onMembruAcreditatUnselected(event: any): void {
	}

	public onSelectMembruAcreditatDialogOKAction(): void {
		if (ObjectUtils.isNotNullOrUndefined(this.selectionMembruAcreditat)) {
			this.selectMembruAcreditatDialogVisible = false;
			this.selectedMembruAcreditat = this.selectionMembruAcreditat;
			this.doWhenMembruAcreditatValueChanged();
		} else {
			this.calitateParticipantFormControl.setValue(null);
		}
	}

	public onSelectMembruAcreditatDialogCloseAction(): void {
		this.selectMembruAcreditatDialogVisible = false;
	}

	public onClearSelectedMembruAcreditat(): void {
		this.selectedMembruAcreditat = null;
		this.doWhenMembruAcreditatValueChanged();
	}

	public onSelectMembruAcreditat(): void {
		this.selectMembruAcreditatDialogVisible = true;
	}

	public get clearSelectedMembruAcreditatEnabled(): boolean {
		return StringUtils.isNotBlank(this.membruAcreditatFormControl.value);
	}

	public get membruAcreditatDialogOKActionEnabled(): boolean {
		return this.selectionMembruAcreditat !== null;
	}

	public onNumeInlocuitorChanged(): void {
		this.membruAcreditatFormControl.updateValueAndValidity();
		this.updateCalitateParticipant();
	}

	public onPrenumeInlocuitorChanged(): void {
		this.membruAcreditatFormControl.updateValueAndValidity();
		this.updateCalitateParticipant();
	}

	private updateCalitateParticipant(): void {
		if (StringUtils.isBlank(this.numeMembruInlocuitorFormControl.value) && StringUtils.isBlank(this.prenumeMembruInlocuitorFormControl.value)) {
			this.calitateParticipantFormControl.setValue(null);
				} else {
			this.calitateParticipantFormControl.setValue(MetadataCollectionWindowInformatiiParticipantiOfPrezentaComisiiGLContentComponent.METADATA_CALITATE_PARTICIPANT_VALUE_INLOCUITOR);
		}
	}

	public get title(): string {
		return "";
	}
}

class InformatiiParticipantiValidators {

	public static membruAcreditatOrInlocuitorValidator(provider: MembruAcreditatOrInlocuitorValidationProvider): ValidatorFn | null {
		return (control: AbstractControl): {[key: string]: any} => {
			let membruAcreditat: string = ObjectUtils.isNotNullOrUndefined(control.value) ? String(control.value) : null;	
			if (StringUtils.isNotBlank(membruAcreditat)) {
				return null;
			}
			if (StringUtils.isNotBlank(provider.getNumeMebruInlocuitor()) && StringUtils.isNotBlank(provider.getPrenumeMebruInlocuitor())) {
				return null;
			}
			return { membruAcreditatOrInlocuitorRequired: true };
		};
	}
}

interface MembruAcreditatOrInlocuitorValidationProvider {

	getNumeMebruInlocuitor(): string;

	getPrenumeMebruInlocuitor(): string;
}