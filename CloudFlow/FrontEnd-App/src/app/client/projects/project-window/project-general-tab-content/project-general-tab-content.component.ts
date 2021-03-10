import { Component, OnInit, Input } from "@angular/core";
import { ProjectTabContent } from "../project-tab-content";
import { ProjectModel, StringValidators, FormUtils, DateConstants, DateUtils, NomenclatorService, NomenclatorConstants, AppError, MessageDisplayer, NomenclatorValueModel, ProjectType, SaveNomenclatorValueResponseModel,
	TranslateUtils, 
	AdminPermissionEnum} from "@app/shared";
import { FormBuilder, FormGroup, Validators, AbstractControl } from "@angular/forms";
import { AuthManager } from "@app/shared/auth";
import { SelectItem } from "primeng/api";

@Component({
	selector: "app-project-general-tab-content",
	templateUrl: "./project-general-tab-content.component.html"
})
export class ProjectGeneralTabContentComponent extends ProjectTabContent {
	
	private nomenclatorService: NomenclatorService;
	private authManager: AuthManager;
	private messageDisplayer: MessageDisplayer;
	private translateUtils: TranslateUtils;
	private formBuilder: FormBuilder;
	public form: FormGroup;

	public dateFormat: string;
	public yearRange: string;

	public displayDescriptionWindow: boolean;
	private gradImportantaNomenclatorValues: NomenclatorValueModel[];
	public descriptionWindowHeader: string;
	public importanceRankDescription: string;
	public importanceRankSelectItems: SelectItem[];
	public selectImportanceValueForDescriptionEdit: SelectItem;

	public constructor(formBuilder: FormBuilder, authManager: AuthManager, nomenclatorService: NomenclatorService, messageDisplayer: MessageDisplayer, translateUtils: TranslateUtils) {
		super();
		this.nomenclatorService = nomenclatorService;
		this.messageDisplayer = messageDisplayer;
		this.authManager = authManager;
		this.translateUtils = translateUtils;
		this.formBuilder = formBuilder;
		this.init();
	}

	private init(): void {
		this.displayDescriptionWindow = false;
		this.dateFormat = DateConstants.DATE_FORMAT_FOR_TYPING;
		this.yearRange = DateUtils.getDefaultYearRange();
		this.prepareForm();
	}

	private prepareForm(): void {
		this.form = this.formBuilder.group({
			name: [null, [Validators.required, StringValidators.blank()]],
			description: [null],
			abbreviation: [null, [Validators.required, StringValidators.blank()]],
			userResponsible:  [null, Validators.required],
			importanceRank: [null, this.isAdd() ? null : Validators.required],
			startDate: [null, Validators.required],
			endDate: [null, Validators.required],
			implementationDate: [null, Validators.required]
		});
	}

	public doWhenOnInit(): void {
		this.getGradImportantaNomenclatorIdByCode()
			.then( gradImportantaNomenclatorId => {
				this.getNomenclatorValuesByNomenclatorId(gradImportantaNomenclatorId)
					.then(values => {
						this.gradImportantaNomenclatorValues = values;
						this.importanceRankSelectItems = [];
						this.gradImportantaNomenclatorValues.forEach(nomValue => this.importanceRankSelectItems.push({value: nomValue.id, label: nomValue[NomenclatorConstants.IMPORTANTA_PROIECTE_ATTR_KEY_GRAD]}));
						if (this.isViewOrEdit() && (this.project.importance != null)){
							let foundNomValue = this.gradImportantaNomenclatorValues.find(nomValue => nomValue[NomenclatorConstants.IMPORTANTA_PROIECTE_ATTR_KEY_VALOARE] === this.project.importance.toString());
							if (foundNomValue){
								this.importanceRankFormControl.patchValue(foundNomValue.id);
							}
						}
					});
			});
		if (!this.isImportanceRequired()){
			this.importanceRankFormControl.clearValidators();
		}
	}

	public prepareForAdd(): void {
		this.prepareForm();
	}
	
	public prepareForViewOrEdit(): void {
		
		this.projectNameFormControl.setValue(this.project.name);
		this.projectDescriptionFormControl.setValue(this.project.description);
		this.projectAbbreviationFormControl.setValue(this.project.projectAbbreviation);
		this.userResponsibleFormControl.setValue(this.project.responsibleUserId);
		this.startDateFormControl.setValue(this.project.startDate);
		this.endDateFormControl.setValue(this.project.endDate);
		this.implementationDateFormControl.setValue(DateUtils.formatForDisplay(this.project.implementationDate));
	}

	public reset(): void {
		this.form.reset();
	}

	private getGradImportantaNomenclatorIdByCode(): Promise<number> {
		return new Promise<number>((resolve, reject) => {
			this.nomenclatorService.getNomenclatorIdByCodeAsMap([NomenclatorConstants.NOMENCLATOR_CODE_IMPORTANTA_PROIECTE], {
				onSuccess: (nomenclatorIdByCode: object): void => {
					resolve(nomenclatorIdByCode[NomenclatorConstants.NOMENCLATOR_CODE_IMPORTANTA_PROIECTE]);
				}, onFailure: (error: AppError): void => {
					this.messageDisplayer.displayAppError(error);
					reject();
				}
			});
		});
	}

	private getNomenclatorValuesByNomenclatorId(nomeclatorId: number): Promise<NomenclatorValueModel[]> {
		return new Promise<NomenclatorValueModel[]>((resolve, reject) => {
			this.nomenclatorService.getNomenclatorValuesByNomenclatorId(nomeclatorId, {
				onSuccess: (nomenclatorValues: NomenclatorValueModel[]): void => {
					resolve(nomenclatorValues);
				}, onFailure: (error: AppError): void => {
					this.messageDisplayer.displayAppError(error);
					reject();
				}
			});
		});
	}

	private updateImportanceRankDescription(importanceRankToUpdate: NomenclatorValueModel): Promise<void> {
		return new Promise<void>((resolve, reject) => {
			this.nomenclatorService.saveNomenclatorValue(importanceRankToUpdate, {
				onSuccess: (response: SaveNomenclatorValueResponseModel): void => {
					this.messageDisplayer.displaySuccess("SAVED_SUCCESSFULLY");
					resolve();
				},onFailure: (error: AppError): void => {
					this.messageDisplayer.displayAppError(error);
					reject();
				}
			});
		});
	}

	public getImportanceRankDescription(item: SelectItem): string {
		let description: string = this.gradImportantaNomenclatorValues.find(nomValue => nomValue.id === item.value)[NomenclatorConstants.IMPORTANTA_PROIECTE_ATTR_KEY_DESCRIERE];
		return description != null || "" ? description : this.translateUtils.translateLabel("NO_DESCRIPTION");
	}

	public hasEditImportanceRankDescriptionPermission(): boolean {
		return this.authManager.hasPermission(AdminPermissionEnum.EDIT_IMPORTANCE_RANK_DESCRIPTION);
	}
	
	public descriptionChanged(): void {
		let importanceRankToUpdate: NomenclatorValueModel = this.gradImportantaNomenclatorValues
			.find(nomValue => nomValue.id === this.selectImportanceValueForDescriptionEdit.value);
		let oldDescription = importanceRankToUpdate[NomenclatorConstants.IMPORTANTA_PROIECTE_ATTR_KEY_DESCRIERE];
		importanceRankToUpdate[NomenclatorConstants.IMPORTANTA_PROIECTE_ATTR_KEY_DESCRIERE] = this.importanceRankDescription;
		this.updateImportanceRankDescription(importanceRankToUpdate)
			.catch(() => importanceRankToUpdate[NomenclatorConstants.IMPORTANTA_PROIECTE_ATTR_KEY_DESCRIERE] = oldDescription);
		this.displayDescriptionWindow = false;
	}

	public onEditImportanceRankDescription(eventItem: SelectItem, event: MouseEvent): void {
		event.stopPropagation();
		this.selectImportanceValueForDescriptionEdit = eventItem;
		this.descriptionWindowHeader = eventItem.label;
		let foundImportanceRank: NomenclatorValueModel = this.gradImportantaNomenclatorValues.find(nomValue => nomValue.id === eventItem.value);
		this.importanceRankDescription = foundImportanceRank ? foundImportanceRank[NomenclatorConstants.IMPORTANTA_PROIECTE_ATTR_KEY_DESCRIERE] : null;
		this.displayDescriptionWindow = true;
	}

	public isValid(): boolean {
		FormUtils.validateAllFormFields(this.form);
		return this.form.valid;
	}

	public populateForSave(project: ProjectModel): void {
		project.name = this.projectNameFormControl.value;
		project.description = this.projectDescriptionFormControl.value;
		project.projectAbbreviation = this.projectAbbreviationFormControl.value;
		project.responsibleUserId = Number(this.userResponsibleFormControl.value);
		project.startDate = this.startDateFormControl.value;
		project.endDate = this.endDateFormControl.value;
		project.implementationDate = this.endDateFormControl.value;
		let foundNomValue = this.gradImportantaNomenclatorValues.find(nomValue => nomValue.id === this.importanceRankFormControl.value);
		project.importance = foundNomValue ? Number.parseInt(foundNomValue[NomenclatorConstants.IMPORTANTA_PROIECTE_ATTR_KEY_VALOARE]) : null;
	}

	public onEndDateSelected(endDate: Date): void {
		this.implementationDateFormControl.setValue(DateUtils.formatForDisplay(endDate));
	}

	public isUserResponsible(): boolean {
		return this.authManager.getLoggedInUser().id === this.project.responsibleUserId;
	}

	public isImportanceRequired(): boolean {
		if (this.isAdd()){
			return false;
		}else{
			return this.project.type === ProjectType.DSP;
		}
	}

	private getControlByName(name: string): AbstractControl {
		return this.form.controls[name];
	}
	
	public get projectNameFormControl(): AbstractControl {
		return this.getControlByName("name");
	}
	
	public get projectDescriptionFormControl(): AbstractControl {
		return this.getControlByName("description");
	}
	
	public get projectAbbreviationFormControl(): AbstractControl {
		return this.getControlByName("abbreviation");
	}
	
	public get userResponsibleFormControl(): AbstractControl {
		return this.getControlByName("userResponsible");
	}
	
	public get importanceRankFormControl(): AbstractControl {
		return this.getControlByName("importanceRank");
	}
	
	public get startDateFormControl(): AbstractControl {
		return this.getControlByName("startDate");
	}
	
	public get endDateFormControl(): AbstractControl {
		return this.getControlByName("endDate");
	}
	
	public get implementationDateFormControl(): AbstractControl {
		return this.getControlByName("implementationDate");
	}
}
