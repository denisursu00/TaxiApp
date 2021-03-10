import { AfterViewInit, Component, OnInit } from "@angular/core";
import { FormBuilder, FormControl, FormGroup, Validators } from "@angular/forms";
import { ArrayUtils, ComisieSauGLService, FormUtils, ListMetadataItemModel, MessageDisplayer, MetadataDefinitionModel, MetadataInstanceModel, NomenclatorConstants, NomenclatorService, ObjectUtils, StringUtils, WorkflowStateModel, WorkflowStateUtils, ValueOfNomenclatorValueField, AppError, NomenclatorFilter, NomenclatorSimpleFilter, GetNomenclatorValuesRequestModel, NomenclatorValueModel, DocumentConstants, NomenclatorValidators } from "@app/shared";
import { SelectItem } from "primeng/primeng";
import { MetadataEventMediator, MetadataEventName } from "./../../document-metadata/metadata-event-mediator";
import { MetadataCollectionWindowContent } from "./metadata-collection-window-content";

@Component({
    selector: "app-metadata-collection-window-prezenta-membrii-of-prezenta-aga-content",
    templateUrl: "./metadata-collection-window-prezenta-membrii-of-prezenta-aga-content.component.html"
})
export class MetadataCollectionWindowPrezentaMembriiOfPrezentaAgaContentComponent extends MetadataCollectionWindowContent implements OnInit, AfterViewInit {


    private static readonly METADATA_NAME_IMPUTERNICIRE: string = "imputernicire";
    private static readonly METADATA_NAME_PARTICIPANT_ACREDITAT: string = "participant_acreditat";
    private static readonly METADATA_NAME_NUME_PARTICIPANT_INLOCUITOR: string = "nume_participant_inlocuitor";
    private static readonly METADATA_NAME_PRENUME_PARTICIPANT_INLOCUITOR: string = "prenume_participant_inlocuitor";
    private static readonly METADATA_NAME_FUNCTIE_PARTICIPANT: string = "functie_participant_acreditat_inlocuitor";
    private static readonly METADATA_NAME_CALITATE_PARTICIPANT: string = "calitate_participant";
    private static readonly METADATA_NAME_INSTITUTIE: string = "institutie";

    private nomenclatorService: NomenclatorService;
    private messageDisplayer: MessageDisplayer;

    public formBuilder: FormBuilder;
    public formGroup: FormGroup;

    public calitateSelectItems: SelectItem[] = [];
    public imputernicireSelectItems: SelectItem[] = [];

    private metadataCollectionDefinition: MetadataDefinitionModel;
    private documentWorkflowState: WorkflowStateModel;

    public metadataEventMediator: MetadataEventMediator;

    private calitateReprezentantiByCaliateMap: object;

    public institutieCustomFilters: NomenclatorFilter[] = [];
    public requiredFields: boolean[] = [];

    constructor(formBuilder: FormBuilder, comisiiSauGLService: ComisieSauGLService, nomenclatorService: NomenclatorService, messageDisplayer: MessageDisplayer) {
        super();
        this.nomenclatorService = nomenclatorService;
        this.messageDisplayer = messageDisplayer;
        this.formBuilder = formBuilder;
        this.formGroup = this.formBuilder.group([]);
        this.metadataEventMediator = new MetadataEventMediator();
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

    }

    private prepareImputernicire(): void {
        this.imputernicireSelectItems = [];
        let imputernicireMetDef: MetadataDefinitionModel = this.getMetadataDefinitionByName(MetadataCollectionWindowPrezentaMembriiOfPrezentaAgaContentComponent.METADATA_NAME_IMPUTERNICIRE);
        imputernicireMetDef.listItems.forEach((listItem: ListMetadataItemModel) => {
            this.imputernicireSelectItems.push({
                value: listItem.value,
                label: listItem.label
            });
        });
    }

    private prepareCalitati(): void {
        this.calitateSelectItems = [];
        let calitateMetDef: MetadataDefinitionModel = this.getMetadataDefinitionByName(MetadataCollectionWindowPrezentaMembriiOfPrezentaAgaContentComponent.METADATA_NAME_CALITATE_PARTICIPANT);
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

        this.metadataCollectionDefinition = this.inputData.metadataCollectionDefinition;

        this.formGroup.addControl("imputernicire", new FormControl());
        this.formGroup.addControl("participantAcreditat", new FormControl());
        this.formGroup.addControl("numeParticipantInlocuitor", new FormControl());
        this.formGroup.addControl("prenumeParticipantInlocuitor", new FormControl());
        this.formGroup.addControl("functieParticipant", new FormControl());
        this.formGroup.addControl("calitateParticipant", new FormControl());
        this.formGroup.addControl("institutie", new FormControl());

        this.prepareImputernicire();
        this.prepareCalitati();
        this.prepareFieldValues();

    }

    private prepareFieldValues(): void {
        this.lock();
        let nomenclatorCodes: string[] = [NomenclatorConstants.NOMENCLATOR_CODE_INSTITUTII, NomenclatorConstants.NOMENCLATOR_CODE_PERSOANE];
        this.nomenclatorService.getNomenclatorIdByCodeAsMap(nomenclatorCodes, {
            onSuccess: (nomenclatorIdsByCode: any): void => {


                if (this.isViewOrEdit() && ObjectUtils.isNotNullOrUndefined(this.inputData.collectionInstanceRow)) {
                    let imputernicire: string = this.getMetadataValueByName(MetadataCollectionWindowPrezentaMembriiOfPrezentaAgaContentComponent.METADATA_NAME_IMPUTERNICIRE);
                    this.imputernicireFormControl.setValue(imputernicire);
                    this.imputernicireFormControl.updateValueAndValidity();

                    let participantAcreditatId: string = this.getMetadataValueByName(MetadataCollectionWindowPrezentaMembriiOfPrezentaAgaContentComponent.METADATA_NAME_PARTICIPANT_ACREDITAT);
                    let participantValue: ValueOfNomenclatorValueField = new ValueOfNomenclatorValueField(nomenclatorIdsByCode[NomenclatorConstants.NOMENCLATOR_CODE_PERSOANE]);
                    participantValue.value = Number.parseInt(participantAcreditatId);
                    this.participantAcreditatFormControl.setValue(participantValue);

                    let numeParticipantInlocuitor: string = this.getMetadataValueByName(MetadataCollectionWindowPrezentaMembriiOfPrezentaAgaContentComponent.METADATA_NAME_NUME_PARTICIPANT_INLOCUITOR);
                    this.numeParticipantInlocuitorFormControl.setValue(numeParticipantInlocuitor);

                    let prenumeParticipantInlocuitor: string = this.getMetadataValueByName(MetadataCollectionWindowPrezentaMembriiOfPrezentaAgaContentComponent.METADATA_NAME_PRENUME_PARTICIPANT_INLOCUITOR);
                    this.prenumeParticipantInlocuitorFormControl.setValue(prenumeParticipantInlocuitor);

                    let functie: string = this.getMetadataValueByName(MetadataCollectionWindowPrezentaMembriiOfPrezentaAgaContentComponent.METADATA_NAME_FUNCTIE_PARTICIPANT);
                    this.functieParticipantFormControl.setValue(functie);

                    let calitate: string = this.getMetadataValueByName(MetadataCollectionWindowPrezentaMembriiOfPrezentaAgaContentComponent.METADATA_NAME_CALITATE_PARTICIPANT);
                    this.calitateParticipantFormControl.setValue(calitate);

                    let institutieId: string = this.getMetadataValueByName(MetadataCollectionWindowPrezentaMembriiOfPrezentaAgaContentComponent.METADATA_NAME_INSTITUTIE);
                    let institutieNomValue: ValueOfNomenclatorValueField = new ValueOfNomenclatorValueField(nomenclatorIdsByCode[NomenclatorConstants.NOMENCLATOR_CODE_INSTITUTII]);
                    institutieNomValue.value = Number.parseInt(institutieId);
                    this.institutieFormControl.setValue(institutieNomValue);

                } else {
                    let institutieValue = new ValueOfNomenclatorValueField(nomenclatorIdsByCode[NomenclatorConstants.NOMENCLATOR_CODE_INSTITUTII]);
                    this.institutieFormControl.setValue(institutieValue);
                    this.participantAcreditatFormControl.setValue(new ValueOfNomenclatorValueField(nomenclatorIdsByCode[NomenclatorConstants.NOMENCLATOR_CODE_PERSOANE]));
                }

                this.activateValidators();
                this.unlock();
            },
            onFailure: (error: AppError): void => {
                this.unlock();
                this.messageDisplayer.displayAppError(error);
            }
        });

        this.lock();
        let tipInstitutieFilter: GetNomenclatorValuesRequestModel = new GetNomenclatorValuesRequestModel();
        tipInstitutieFilter.nomenclatorCode = NomenclatorConstants.NOMENCLATOR_CODE_TIP_INSTITUTII;
        let membruArbFilter: NomenclatorSimpleFilter = new NomenclatorSimpleFilter();
        membruArbFilter.attributeKey = NomenclatorConstants.TIP_INSTITUTII_ATTR_KEY_TIP;
        membruArbFilter.value = NomenclatorConstants.TIP_INSTITUTII_TIP_MEMBRU_ARB;
        tipInstitutieFilter.filters = [membruArbFilter];
        this.nomenclatorService.getNomenclatorValues(tipInstitutieFilter, {
            onSuccess: (response: NomenclatorValueModel[]): void => {

                if (ObjectUtils.isNotNullOrUndefined(response) && response.length > 0) {
                    let memmbriiArbFilter: NomenclatorSimpleFilter = new NomenclatorSimpleFilter();
                    memmbriiArbFilter.attributeKey = NomenclatorConstants.INSITUTII_ATTR_KEY_TIP_INSTITUTIE;
                    memmbriiArbFilter.value = response[0].id;

                    let neradiatFilter: NomenclatorSimpleFilter = new NomenclatorSimpleFilter();
                    neradiatFilter.attributeKey = NomenclatorConstants.INSTITUTII_ATTRIBUTE_KEY_TIP_RADIAT;
                    neradiatFilter.value = "false";
                    this.institutieCustomFilters = [memmbriiArbFilter, neradiatFilter];
                }
                this.unlock();
            },
            onFailure: (error: AppError): void => {
                this.unlock();
                this.messageDisplayer.displayAppError(error);
            }
        });

    }

    private activateValidators(): void {
        this.checkAndAddRequiredValidator(MetadataCollectionWindowPrezentaMembriiOfPrezentaAgaContentComponent.METADATA_NAME_IMPUTERNICIRE, this.imputernicireFormControl);
        this.checkAndAddRequiredValidator(MetadataCollectionWindowPrezentaMembriiOfPrezentaAgaContentComponent.METADATA_NAME_PARTICIPANT_ACREDITAT, this.participantAcreditatFormControl);
        this.checkAndAddRequiredValidator(MetadataCollectionWindowPrezentaMembriiOfPrezentaAgaContentComponent.METADATA_NAME_NUME_PARTICIPANT_INLOCUITOR, this.numeParticipantInlocuitorFormControl);
        this.checkAndAddRequiredValidator(MetadataCollectionWindowPrezentaMembriiOfPrezentaAgaContentComponent.METADATA_NAME_PRENUME_PARTICIPANT_INLOCUITOR, this.prenumeParticipantInlocuitorFormControl);
        this.checkAndAddRequiredValidator(MetadataCollectionWindowPrezentaMembriiOfPrezentaAgaContentComponent.METADATA_NAME_FUNCTIE_PARTICIPANT, this.functieParticipantFormControl);
        this.checkAndAddRequiredValidator(MetadataCollectionWindowPrezentaMembriiOfPrezentaAgaContentComponent.METADATA_NAME_CALITATE_PARTICIPANT, this.calitateParticipantFormControl);
        this.checkAndAddRequiredValidator(MetadataCollectionWindowPrezentaMembriiOfPrezentaAgaContentComponent.METADATA_NAME_INSTITUTIE, this.institutieFormControl);

    }

    private checkAndAddRequiredValidator(metadataDefName: string, formControl: FormControl): void {
        this.requiredFields[metadataDefName] = false;
        let metadataDefinition: MetadataDefinitionModel = this.getMetadataDefinitionByName(metadataDefName);
        if (StringUtils.isNotBlank(metadataDefinition.mandatoryStates)) {
            if (WorkflowStateUtils.isStateFound(metadataDefinition.mandatoryStates, this.inputData.documentWorkflowState.code)) {
                if (metadataDefName === MetadataCollectionWindowPrezentaMembriiOfPrezentaAgaContentComponent.METADATA_NAME_INSTITUTIE
                    || metadataDefName === MetadataCollectionWindowPrezentaMembriiOfPrezentaAgaContentComponent.METADATA_NAME_PARTICIPANT_ACREDITAT) {
                    formControl.setValidators([NomenclatorValidators.nomenclatorValueRequired()]);
                    this.requiredFields[metadataDefName] = true;
                } else {
                    formControl.setValidators([Validators.required]);
                    this.requiredFields[metadataDefName] = true;
                }
            }
        }
        formControl.updateValueAndValidity();
    }

    public get institutieOptionLabel(): string {
        return NomenclatorConstants.INSITUTII_ATTR_KEY_DENUMIRE_INSTITUTIE;
    }

    public onCalitateParticipantValueChanged(event: any): void {
    }

    public getMetadataInstances(): MetadataInstanceModel[] {

        let metadataInstances: MetadataInstanceModel[] = [];

        this.prepareAndAddMetadataInstance(MetadataCollectionWindowPrezentaMembriiOfPrezentaAgaContentComponent.METADATA_NAME_IMPUTERNICIRE,
            this.imputernicireFormControl, metadataInstances);

        this.prepareAndAddMetadataInstance(MetadataCollectionWindowPrezentaMembriiOfPrezentaAgaContentComponent.METADATA_NAME_PARTICIPANT_ACREDITAT,
            this.participantAcreditatFormControl, metadataInstances);

        this.prepareAndAddMetadataInstance(MetadataCollectionWindowPrezentaMembriiOfPrezentaAgaContentComponent.METADATA_NAME_NUME_PARTICIPANT_INLOCUITOR,
            this.numeParticipantInlocuitorFormControl, metadataInstances);

        this.prepareAndAddMetadataInstance(MetadataCollectionWindowPrezentaMembriiOfPrezentaAgaContentComponent.METADATA_NAME_PRENUME_PARTICIPANT_INLOCUITOR,
            this.prenumeParticipantInlocuitorFormControl, metadataInstances);

        this.prepareAndAddMetadataInstance(MetadataCollectionWindowPrezentaMembriiOfPrezentaAgaContentComponent.METADATA_NAME_FUNCTIE_PARTICIPANT,
            this.functieParticipantFormControl, metadataInstances);

        this.prepareAndAddMetadataInstance(MetadataCollectionWindowPrezentaMembriiOfPrezentaAgaContentComponent.METADATA_NAME_CALITATE_PARTICIPANT,
            this.calitateParticipantFormControl, metadataInstances);

        this.prepareAndAddMetadataInstance(MetadataCollectionWindowPrezentaMembriiOfPrezentaAgaContentComponent.METADATA_NAME_INSTITUTIE,
            this.institutieFormControl, metadataInstances);

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
            } else if (value instanceof ValueOfNomenclatorValueField) {
                if (ObjectUtils.isNullOrUndefined(value.value)) {
                    return;
                }
                valueAsString = value.value.toString();
            } else {
                throw new Error("control value type unknown");
            }
            metadataInstance.values = [valueAsString];
            metadataInstances.push(metadataInstance);
        }
    }

    public get imputernicireLabel(): string {
        return this.getMetadataLabel(MetadataCollectionWindowPrezentaMembriiOfPrezentaAgaContentComponent.METADATA_NAME_IMPUTERNICIRE);
    }

    public get participantAcreditatLabel(): string {
        return this.getMetadataLabel(MetadataCollectionWindowPrezentaMembriiOfPrezentaAgaContentComponent.METADATA_NAME_PARTICIPANT_ACREDITAT);
    }

    public get numeParticipantInlocuitorLabel(): string {
        return this.getMetadataLabel(MetadataCollectionWindowPrezentaMembriiOfPrezentaAgaContentComponent.METADATA_NAME_NUME_PARTICIPANT_INLOCUITOR);
    }

    public get prenumeParticipantInlocuitorLabel(): string {
        return this.getMetadataLabel(MetadataCollectionWindowPrezentaMembriiOfPrezentaAgaContentComponent.METADATA_NAME_PRENUME_PARTICIPANT_INLOCUITOR);
    }

    public get functieParticipantLabel(): string {
        return this.getMetadataLabel(MetadataCollectionWindowPrezentaMembriiOfPrezentaAgaContentComponent.METADATA_NAME_FUNCTIE_PARTICIPANT);
    }

    public get calitateParticipantLabel(): string {
        return this.getMetadataLabel(MetadataCollectionWindowPrezentaMembriiOfPrezentaAgaContentComponent.METADATA_NAME_CALITATE_PARTICIPANT);
    }

    public get institutieLabel(): string {
        return this.getMetadataLabel(MetadataCollectionWindowPrezentaMembriiOfPrezentaAgaContentComponent.METADATA_NAME_INSTITUTIE);
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

    public get imputernicireFormControl(): FormControl {
        return <FormControl>this.formGroup.get("imputernicire");
    }

    public get participantAcreditatFormControl(): FormControl {
        return <FormControl>this.formGroup.get("participantAcreditat");
    }

    public get numeParticipantInlocuitorFormControl(): FormControl {
        return <FormControl>this.formGroup.get("numeParticipantInlocuitor");
    }

    public get prenumeParticipantInlocuitorFormControl(): FormControl {
        return <FormControl>this.formGroup.get("prenumeParticipantInlocuitor");
    }

    public get functieParticipantFormControl(): FormControl {
        return <FormControl>this.formGroup.get("functieParticipant");
    }

    public get institutieFormControl(): FormControl {
        return <FormControl>this.formGroup.get("institutie");
    }

    public get calitateParticipantFormControl(): FormControl {
        return <FormControl>this.formGroup.get("calitateParticipant");
    }

    public validate(): boolean {
        FormUtils.validateAllFormFields(this.formGroup);
        return this.formGroup.valid;
    }

    public onMembruAcreditatSelected(event: any): void {
    }

    public onMembruAcreditatUnselected(event: any): void {
    }

    public get title(): string {
        return "";
    }

    public onImputernicireValueChanged(event: any): void {
        this.prepareCalitati();

        if (event.value === DocumentConstants.METADATA_LIST_IMPUTERNICIRE_PREZENTA_MEMBRII_OF_PREZENTA_AGA_VALUE_NU) {
            let calitateSelectItemsFiltered = this.calitateSelectItems.filter(selectItem => (selectItem.value === "presedinte_dg" || selectItem.value === "vicepresedinte_dgad"));
            this.calitateSelectItems = calitateSelectItemsFiltered;
        } else {
            let calitateSelectItemsFiltered = this.calitateSelectItems.filter(selectItem => (selectItem.value === "imputernicit"));
            this.calitateSelectItems = calitateSelectItemsFiltered;
        }
    }

    public participantAcreditatValueChanged(event: any): void {
        let participantAcreditatId = event.value;
        if (ObjectUtils.isNotNullOrUndefined(participantAcreditatId)) {
            this.nomenclatorService.getNomenclatorValue(participantAcreditatId, {
                onSuccess: (response: NomenclatorValueModel): void => {
                    if (ObjectUtils.isNotNullOrUndefined(response)) {
                        this.functieParticipantFormControl.setValue(response[NomenclatorConstants.PERSOANE_ATTR_KEY_FUNCTIE]);
                    }
                },
                onFailure: (error: AppError): void => {
                    this.messageDisplayer.displayAppError(error);
                }
            });
        }
    }

}
