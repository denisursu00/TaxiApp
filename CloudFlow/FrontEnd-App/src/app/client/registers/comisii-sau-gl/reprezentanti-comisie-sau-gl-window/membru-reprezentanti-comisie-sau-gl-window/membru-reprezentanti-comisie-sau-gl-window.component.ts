import { Component, OnInit, Input, Output, EventEmitter } from "@angular/core";
import { FormBuilder, FormGroup, FormControl } from "@angular/forms";
import { ArrayUtils, ObjectUtils, StringUtils, FormUtils, AppError, NomenclatorValueModel, NomenclatorFilter, NomenclatorConstants, ConfirmationWindowFacade, BaseWindow } from "@app/shared";
import { Validators, AbstractControl, ValidatorFn } from "@angular/forms";
import { MembruReprezentantiComisieSauGLModel, ValueOfNomenclatorValueField, NomenclatorValidators, DiplomaMembruReprezentantiComisieSauGLModel } from "@app/shared";
import { NomenclatorService, MessageDisplayer, NomenclatorUtils, TranslateUtils } from "@app/shared";
import { SelectItem } from "primeng/components/common/selectitem";
import { NomenclatorSimpleFilter, NomenclatorMultipleFilter } from "@app/shared/model/nomenclator";
import { Column, Dialog } from "primeng/primeng";

@Component({
	selector: "app-membru-reprezentanti-comisie-sau-gl-window",
	templateUrl: "./membru-reprezentanti-comisie-sau-gl-window.component.html"
})
export class MembruReprezentantiComisieSauGLWindowComponent extends BaseWindow implements OnInit {

	@Input()
	public mode: "add" | "edit" | "view";

	@Input()
	public inputData: MembruReprezentantiComisieSauGLWindowInputData;
	
	@Input()
	public existingMembersByInstitutieIdAndMembruId: string[] = [];
	
	@Input()
	public existingMembersByInstitutieNameAndMembruName: string[] = [];

	@Input()
	public existingMembriModels: MembruReprezentantiComisieSauGLModel[];
	
	@Output()
	private saved: EventEmitter<MembruReprezentantiComisieSauGLModel>;
	
	@Output()
	private closed: EventEmitter<void>;

	private nomenclatorService: NomenclatorService;
	private messageDisplayer: MessageDisplayer;
	private translateUtils: TranslateUtils;
	
	public dialogVisible: boolean;	

	public headerLabelCode: string;
	public saveLabelCode: string;

	private formBuilder: FormBuilder;
	public formGroup: FormGroup;

	public calitateSelectItems: SelectItem[] = [];
	public stareSelectItems: SelectItem[] = [];

	public institutieCustomFilters: NomenclatorFilter[];
	public membruInstitutieCustomFilters: NomenclatorFilter[];

	public diplomeColumns: Column[];
	public diplomeValues: DiplomaMembruReprezentantiComisieSauGLModel[];
	public selectedDiploma: DiplomaMembruReprezentantiComisieSauGLModel;
	
	public addDiplomaActionEnabled: boolean;
	public editDiplomaActionEnabled: boolean;
	public removeDiplomaActionEnabled: boolean;
	
	public diplomaWindowVisible: boolean;
	public diplomaWindowMode: "add" | "edit" | "view";
	public diplomaWindowTakenDenumiri: string[];
	public diplomaWindowDiploma: DiplomaMembruReprezentantiComisieSauGLModel;
	
	private indexOfDiplomaInEditing: number;

	private persoaneFromNomenclator: NomenclatorValueModel[];
	
	public constructor(nomenclatorService: NomenclatorService, messageDisplayer: MessageDisplayer, 
			translateUtils: TranslateUtils, formBuilder: FormBuilder) {
		super();
		this.nomenclatorService = nomenclatorService;
		this.messageDisplayer = messageDisplayer;
		this.translateUtils = translateUtils;
		this.formBuilder = formBuilder;
		this.unlock();
		this.dialogVisible = false;
		this.saved = new EventEmitter();
		this.closed = new EventEmitter();
		this.populateSelectItems();
		this.persoaneFromNomenclator = [];
	}

	private initForm(): void {
		this.formGroup = this.formBuilder.group([]);
		this.formGroup.addControl("institutie", new FormControl(null, this.isAddOrEdit() ? [NomenclatorValidators.nomenclatorValueRequired()] : []));
		this.formGroup.addControl("membruInstitutie", new FormControl(null, []));
		this.formGroup.addControl("nume", new FormControl(null, this.isAddOrEdit() ? [Validators.required] : []));
		this.formGroup.addControl("prenume", new FormControl(null, this.isAddOrEdit() ? [Validators.required] :[]));
		this.formGroup.addControl("functie", new FormControl(null, this.isAddOrEdit() ? [Validators.required] : []));
		this.formGroup.addControl("departament", new FormControl(null, this.isAddOrEdit() ? [Validators.required] : []));
		this.formGroup.addControl("email", new FormControl(null, this.isAddOrEdit() ? [Validators.required, Validators.email] : []));
		this.formGroup.addControl("telefon", new FormControl(null, this.isAddOrEdit() ? [Validators.required] : []));
		this.formGroup.addControl("calitate", new FormControl(null, this.isAddOrEdit() ? [Validators.required] : []));
		this.formGroup.addControl("stare", new FormControl(null, this.isAddOrEdit() ? [Validators.required] : []));
	}

	private populateSelectItems(): void {
		this.calitateSelectItems = [];
		this.calitateSelectItems.push(this.createCalitateSelectItem(MembruReprezentantiComisieSauGLModel.CALITATE_TITULAR));
		this.calitateSelectItems.push(this.createCalitateSelectItem(MembruReprezentantiComisieSauGLModel.CALITATE_SUPLEANT));
		this.calitateSelectItems.push(this.createCalitateSelectItem(MembruReprezentantiComisieSauGLModel.CALITATE_INLOCUITOR));
		this.stareSelectItems = [];
		this.stareSelectItems.push(this.createStareSelectItem(MembruReprezentantiComisieSauGLModel.STARE_ACTIV));
		this.stareSelectItems.push(this.createStareSelectItem(MembruReprezentantiComisieSauGLModel.STARE_INACTIV));
	}

	private createCalitateSelectItem(itemValue: string): SelectItem {
		return {
			value: itemValue,
			label: this.translateUtils.translateLabel("MEMBRU_REPREZENTANTI_COMISIE_SAU_GL_CALITATE_" + itemValue)
		};
	}

	private createStareSelectItem(itemValue: string): SelectItem {
		return {
			value: itemValue,
			label: this.translateUtils.translateLabel("MEMBRU_REPREZENTANTI_COMISIE_SAU_GL_STARE_" + itemValue)
		};
	}

	private isAdd(): boolean {
		return this.mode === "add";
	}

	private isEdit(): boolean {
		return this.mode === "edit";
	}

	private isAddOrEdit(): boolean {
		return (this.isAdd() || this.isEdit());
	}

	private isView(): boolean {
		return this.mode === "view";
	}

	public get readonly(): boolean {
		return this.isView();
	}

	public ngOnInit(): void {
		
		if (ObjectUtils.isNullOrUndefined(this.mode)) {
			throw new Error("mode cannot be null/undefined");
		}
		
		this.initForm();

		this.headerLabelCode = ObjectUtils.isNullOrUndefined(this.inputData.membruModel) ? "ADD" : this.isView() ? "VIEW" : "EDIT";
		this.saveLabelCode = ObjectUtils.isNullOrUndefined(this.inputData.membruModel) ? "ADD" : "SAVE";
		
		let institutieFieldValue: ValueOfNomenclatorValueField = new ValueOfNomenclatorValueField(this.inputData.institutieNomenclatorId);
		let membruInstitutieFieldValue: ValueOfNomenclatorValueField = new ValueOfNomenclatorValueField(this.inputData.persoaneNomenclatorId);
		this.diplomeValues = [];

		if (ObjectUtils.isNotNullOrUndefined(this.inputData.membruModel)) {
			institutieFieldValue.value = this.inputData.membruModel.institutieId; 
			membruInstitutieFieldValue.value = this.inputData.membruModel.membruInstitutieId; 
			if (ObjectUtils.isNotNullOrUndefined(this.inputData.membruModel.membruInstitutieId)) {
				this.populateFieldsBySelectedMembruInstitutie(this.inputData.membruModel.membruInstitutieId);
			} else {
				this.numeFormControl.setValue(this.inputData.membruModel.nume);
				this.prenumeFormControl.setValue(this.inputData.membruModel.prenume);
				this.departamentFormControl.setValue(this.inputData.membruModel.departament);
				this.functieFormControl.setValue(this.inputData.membruModel.functie);
				this.emailFormControl.setValue(this.inputData.membruModel.email);
				this.telefonFormControl.setValue(this.inputData.membruModel.telefon);
			}			
			this.calitateFormControl.setValue(this.inputData.membruModel.calitate);
			this.stareFormControl.setValue(this.inputData.membruModel.stare);
			this.diplomeValues = this.inputData.membruModel.diplome;
		} else {
			this.stareFormControl.setValue(MembruReprezentantiComisieSauGLModel.STARE_ACTIV);
		}
		if (ObjectUtils.isNullOrUndefined(this.diplomeValues)) {
			this.diplomeValues = [];
		}
		this.institutieFormControl.setValue(institutieFieldValue);
		this.membruInstitutieFormControl.setValue(membruInstitutieFieldValue);
		this.prepareDiplomeColumns();
		this.prepareInstitutieCustomFilters(() => {
			this.updateMembruInstitutieCustomFilters();
			this.changePerspective();
			this.dialogVisible = true;
		});
	}

	private prepareDiplomeColumns(): void {
		this.diplomeColumns = [];
		this.diplomeColumns.push(this.createDiplemaColumn("denumire", "MEMBRU_REPREZENTANTI_COMISIE_SAU_GL_DIPLOMA_DENUMIRE"));
		this.diplomeColumns.push(this.createDiplemaColumn("an", "MEMBRU_REPREZENTANTI_COMISIE_SAU_GL_DIPLOMA_AN"));
		this.diplomeColumns.push(this.createDiplemaColumn("observatii", "MEMBRU_REPREZENTANTI_COMISIE_SAU_GL_DIPLOMA_OBSERVATII"));
	}

	private createDiplemaColumn(fieldName: string, columnHeaderCode: string): Column {
		let column: Column = new Column();
		column.header = this.translateUtils.translateLabel(columnHeaderCode);
		column.field = fieldName;
		return column;
	}

	private prepareInstitutieCustomFilters(callback: () => any): void {
		if (ObjectUtils.isNullOrUndefined(callback)) {
			throw new Error("callback cannot be null/undefined");
		}
		this.nomenclatorService.getNomenclatorValuesByNomenclatorCode(NomenclatorConstants.NOMENCLATOR_CODE_TIP_INSTITUTII, {
			onSuccess: (nomenclatorValues: NomenclatorValueModel[]): void => {
				
				let institutieCustomFilters = [];
				let customFilter: NomenclatorMultipleFilter = new NomenclatorMultipleFilter();
				customFilter.attributeKey = NomenclatorConstants.INSITUTII_ATTR_KEY_TIP_INSTITUTIE;
				customFilter.values = [];
				nomenclatorValues.forEach((nomenclatorValue: NomenclatorValueModel) => {
					let tipInstitutie: string = nomenclatorValue[NomenclatorConstants.TIP_INSTITUTII_ATTR_KEY_TIP];
					if (tipInstitutie === NomenclatorConstants.TIP_INSTITUTII_TIP_MEMBRU_ARB 
							|| tipInstitutie === NomenclatorConstants.TIP_INSTITUTII_TIP_MEMBRU_AFILIAT) {
						customFilter.values.push(nomenclatorValue.id);
					}
				});
				institutieCustomFilters.push(customFilter);

				this.institutieCustomFilters = institutieCustomFilters;
				callback();
			},
			onFailure: (error: AppError): void => {
				this.messageDisplayer.displayAppError(error);
			}
		});
	}

	private updateMembruInstitutieCustomFilters(): void {
		let fielValue: ValueOfNomenclatorValueField = this.institutieFormControl.value;
		this.membruInstitutieCustomFilters = [];
		if (NomenclatorUtils.fieldValueHasValue(fielValue)) {			
			let customFilter: NomenclatorSimpleFilter = new NomenclatorSimpleFilter();
			customFilter.attributeKey = NomenclatorConstants.PERSOANE_ATTR_KEY_INSTITUTIE;
			customFilter.value = fielValue.value;		
			this.membruInstitutieCustomFilters.push(customFilter);			
		}	
	}

	public onInstitutieValueChanged(fielValue: ValueOfNomenclatorValueField): void {
		this.updateMembruInstitutieCustomFilters();
		if (NomenclatorUtils.fieldValueHasValue(this.membruInstitutieFormControl.value)) {
			this.membruInstitutieFormControl.setValue(new ValueOfNomenclatorValueField(this.inputData.persoaneNomenclatorId));
			this.resetPersoanaFields();
		}
		this.changePerspective();
	}

	private resetPersoanaFields(): void {
		this.numeFormControl.reset();
		this.prenumeFormControl.reset();
		this.functieFormControl.reset();
		this.departamentFormControl.reset();
		this.emailFormControl.reset();
		this.telefonFormControl.reset();
	}

	public onMembruInstitutieValueChanged(fielValue: ValueOfNomenclatorValueField): void {
		this.resetPersoanaFields();
		if (NomenclatorUtils.fieldValueHasValue(fielValue)) {			
			this.populateFieldsBySelectedMembruInstitutie(fielValue.value);
		}
		this.changePerspective();
	}

	private changePerspective(): void {

		let enableMembruInstitutieField: boolean = NomenclatorUtils.fieldValueHasValue(this.institutieFormControl.value);
		FormUtils.enableOrDisableFormControl(this.membruInstitutieFormControl, enableMembruInstitutieField);

		let enablePersoanaFields: boolean = !NomenclatorUtils.fieldValueHasValue(this.membruInstitutieFormControl.value);		
		FormUtils.enableOrDisableFormControl(this.numeFormControl, enablePersoanaFields);
		FormUtils.enableOrDisableFormControl(this.prenumeFormControl, enablePersoanaFields);
		FormUtils.enableOrDisableFormControl(this.functieFormControl, enablePersoanaFields);
		FormUtils.enableOrDisableFormControl(this.departamentFormControl, enablePersoanaFields);
		FormUtils.enableOrDisableFormControl(this.emailFormControl, enablePersoanaFields);
		FormUtils.enableOrDisableFormControl(this.telefonFormControl, enablePersoanaFields);
		
		this.changePerspectiveDiplome();
	}

	private changePerspectiveDiplome(): void {
		this.addDiplomaActionEnabled = this.isAddOrEdit();
		this.editDiplomaActionEnabled = (this.isEdit() && ObjectUtils.isNotNullOrUndefined(this.selectedDiploma));
		this.removeDiplomaActionEnabled = (this.isAddOrEdit() && ObjectUtils.isNotNullOrUndefined(this.selectedDiploma));
	}

	private populateFieldsBySelectedMembruInstitutie(membruInstitutieId: number): void {
		this.lock();
		this.nomenclatorService.getNomenclatorValue(membruInstitutieId, {
			onSuccess: (nomenclatorValueModel: NomenclatorValueModel): void => {
				if (ObjectUtils.isNotNullOrUndefined(nomenclatorValueModel)) {
					this.numeFormControl.setValue(nomenclatorValueModel[NomenclatorConstants.PERSOANE_ATTR_KEY_NUME]);
					this.prenumeFormControl.setValue(nomenclatorValueModel[NomenclatorConstants.PERSOANE_ATTR_KEY_PRENUME]);		
					this.departamentFormControl.setValue(nomenclatorValueModel[NomenclatorConstants.PERSOANE_ATTR_KEY_DEPARTAMENT]);
					this.functieFormControl.setValue(nomenclatorValueModel[NomenclatorConstants.PERSOANE_ATTR_KEY_FUNCTIE]);
					this.emailFormControl.setValue(nomenclatorValueModel[NomenclatorConstants.PERSOANE_ATTR_KEY_EMAIL]);
					this.telefonFormControl.setValue(nomenclatorValueModel[NomenclatorConstants.PERSOANE_ATTR_KEY_TELEFON]);
				}
				this.unlock();
			},
			onFailure: (error: AppError): void => {
				this.unlock();
				this.messageDisplayer.displayAppError(error);
			}
		});
	}

	public onSaveAction(event: any): void {
		FormUtils.validateAllFormFields(this.formGroup);
		if (this.formGroup.invalid) {
			return;
		}
		let newMembruModel: MembruReprezentantiComisieSauGLModel = new MembruReprezentantiComisieSauGLModel();
		if (ObjectUtils.isNotNullOrUndefined(this.inputData.membruModel)) {
			newMembruModel.id = this.inputData.membruModel.id;
		}
		newMembruModel.institutieId = (<ValueOfNomenclatorValueField> this.institutieFormControl.value).value;
		newMembruModel.membruInstitutieId = (<ValueOfNomenclatorValueField> this.membruInstitutieFormControl.value).value;
		if (ObjectUtils.isNullOrUndefined(newMembruModel.membruInstitutieId)) {
			newMembruModel.nume = this.numeFormControl.value;
			newMembruModel.prenume = this.prenumeFormControl.value;
			newMembruModel.email = this.emailFormControl.value;
			newMembruModel.functie = this.functieFormControl.value;
			newMembruModel.departament = this.departamentFormControl.value;
			newMembruModel.telefon = this.telefonFormControl.value;
		}
		newMembruModel.stare = this.stareFormControl.value;
		newMembruModel.calitate = this.calitateFormControl.value;
		newMembruModel.diplome = this.diplomeValues;
		
		this.getNomenclatorPersonsFromInstitutie(newMembruModel.institutieId).then(()=>{
			if (this.isPersonFromNomenclator(newMembruModel)) { //daca are membruInstitutieId
				if (this.alreadyExistsPersonFromNomenclator(newMembruModel)) { // daca in institutia data deja exista un membru cu acest membruInstitutieId
					this.messageDisplayer.displayError("PERSON_ALREADY_EXISTS", true);
					return;
				} else {
					this.saved.emit(newMembruModel);
					this.dialogVisible = false;
				}
			} else { //daca nu are membruInstitutieId
				if (this.memberAlreadyExists(newMembruModel) || this.hasPersonNomenclatorEquivalent(newMembruModel)) { //daca a fost deja adaugat un membru cu aceleasi date sau daca exista un membru in nomenclator cu aceleasi date 
					this.messageDisplayer.displayError("PERSON_ALREADY_EXISTS", true);
					return;
				} else {
					this.saved.emit(newMembruModel);
					this.dialogVisible = false;
				}
			}
		});

	}

	private memberAlreadyExists(membru: MembruReprezentantiComisieSauGLModel): boolean {
		for (let membruExistent of this.existingMembriModels) {
			if (   membru.nume === membruExistent.nume
				&& membru.prenume === membruExistent.prenume
				&& membru.institutieId === membruExistent.institutieId
				&& membru.functie === membruExistent.functie
				&& membru.departament === membruExistent.departament) {
				return true;
			}
		}
		return false;
	}

	private hasPersonNomenclatorEquivalent(membru: MembruReprezentantiComisieSauGLModel): boolean {
		for (let persoana of this.persoaneFromNomenclator) {
			if (    membru.nume.localeCompare(persoana[NomenclatorConstants.PERSOANE_ATTR_KEY_NUME].trim()) === 0
					&& membru.prenume.localeCompare(persoana[NomenclatorConstants.PERSOANE_ATTR_KEY_PRENUME].trim()) === 0
					&& membru.functie.localeCompare(persoana[NomenclatorConstants.PERSOANE_ATTR_KEY_FUNCTIE].trim()) === 0
					&& membru.departament.localeCompare(persoana[NomenclatorConstants.PERSOANE_ATTR_KEY_DEPARTAMENT].trim()) === 0) {
				return true;
			}
		}
		return false;
	}

	private alreadyExistsPersonFromNomenclator(membru: MembruReprezentantiComisieSauGLModel): boolean {
		for (let membruExistent of this.existingMembriModels) {
			if (membruExistent.membruInstitutieId === membru.membruInstitutieId
				&& membruExistent.institutieId === membru.institutieId) {
				return true;
			}
		}
		return false;
	}

	private isPersonFromNomenclator(membru: MembruReprezentantiComisieSauGLModel): boolean {
		return ObjectUtils.isNotNullOrUndefined(membru.membruInstitutieId);
	}

	private getNomenclatorPersonsFromInstitutie(institutieId: number): Promise<void> {
		return new Promise<void>((resolve, reject) =>{
			this.persoaneFromNomenclator = [];
			this.lock();
			this.nomenclatorService.getNomenclatorValuesByNomenclatorCode(NomenclatorConstants.NOMENCLATOR_CODE_PERSOANE, {
				onSuccess: (nomenclatorValues: NomenclatorValueModel[]): void => {
					nomenclatorValues.forEach((persoana: NomenclatorValueModel)=>{
						if (StringUtils.toNumber(persoana[NomenclatorConstants.PERSOANE_ATTR_KEY_INSTITUTIE]) === institutieId) {
							this.persoaneFromNomenclator.push(persoana);
						}
					});
					resolve();
					this.unlock();
				},
				onFailure: (error: AppError): void => {
					reject();
					this.messageDisplayer.displayAppError(error);
				}
			});
		});
	}

	public onCancelAction(event: any): void {
		this.dialogVisible = false;
	}

	public onHide(event: any): void {
		this.closed.emit();
	}

	public onAddDiplomaAction(): void {
		this.indexOfDiplomaInEditing = -1;
		this.diplomaWindowMode = "add";
		this.diplomaWindowDiploma = null;
		this.diplomaWindowTakenDenumiri = this.getDenumiriDiplome();
		this.diplomaWindowVisible = true;
	}

	public onEditDiplomaAction(): void {
		this.indexOfDiplomaInEditing = this.diplomeValues.indexOf(this.selectedDiploma);
		this.diplomaWindowMode = "edit";
		this.diplomaWindowDiploma = this.selectedDiploma.clone();
		this.diplomaWindowTakenDenumiri = this.getDenumiriDiplome();
		ArrayUtils.removeElement(this.diplomaWindowTakenDenumiri, this.selectedDiploma.denumire);
		this.diplomaWindowVisible = true;
	}

	private getDenumiriDiplome(): string[] {
		let denumiri: string[] = [];
		if (ArrayUtils.isNotEmpty(this.diplomeValues)) {
			this.diplomeValues.forEach((diploma: DiplomaMembruReprezentantiComisieSauGLModel) => {
				denumiri.push(diploma.denumire);
			});
		}
		return denumiri;
	}

	public onRemoveDiplomaAction(): void {
		ArrayUtils.removeElement(this.diplomeValues, this.selectedDiploma);
		this.selectedDiploma = null;
		this.changePerspectiveDiplome();
	}

	public onDiplomaWindowSaved(diploma: DiplomaMembruReprezentantiComisieSauGLModel): void {
		if (this.indexOfDiplomaInEditing >= 0) {
			this.diplomeValues[this.indexOfDiplomaInEditing] = diploma;
		} else {
			this.diplomeValues.push(diploma);
		}
		this.indexOfDiplomaInEditing = -1;
	}

	public onDiplomaWindowClosed(): void {
		this.indexOfDiplomaInEditing = -1;
		this.diplomaWindowVisible = false;
	}

	public get saveActionEnabled(): boolean {
		return !this.isView();
	}

	public onDiplomaSelect(): void {
		this.changePerspectiveDiplome();
	}

	public onDiplomaUnselect(): void {
		this.changePerspectiveDiplome();
	}

	public get institutieFormControl(): FormControl {
		return <FormControl> this.formGroup.get("institutie");
	}

	public get membruInstitutieFormControl(): FormControl {
		return <FormControl> this.formGroup.get("membruInstitutie");
	}

	public get numeFormControl(): FormControl {
		return <FormControl> this.formGroup.get("nume");
	}
	
	public get prenumeFormControl(): FormControl {
		return <FormControl> this.formGroup.get("prenume");
	}

	public get departamentFormControl(): FormControl {
		return <FormControl> this.formGroup.get("departament");
	}

	public get functieFormControl(): FormControl {
		return <FormControl> this.formGroup.get("functie");
	}

	public get emailFormControl(): FormControl {
		return <FormControl> this.formGroup.get("email");
	}

	public get telefonFormControl(): FormControl {
		return <FormControl> this.formGroup.get("telefon");
	}
	
	public get stareFormControl(): FormControl {
		return <FormControl> this.formGroup.get("stare");
	}

	public get calitateFormControl(): FormControl {
		return <FormControl> this.formGroup.get("calitate");
	}
}

export class MembruReprezentantiComisieSauGLWindowInputData {
	
	public membruModel: MembruReprezentantiComisieSauGLModel;
	
	public institutieNomenclatorId: number;
	public persoaneNomenclatorId: number;
}

class MembruAsIdentifier {

	public denumireInstitutie: string;
	public nume: string;
	public prenume: string;
	
	public buildKeyFromInstitutieDenumireAndMembruNumePrenume(): string {
		return this.denumireInstitutie + "-" + this.nume + "-" + this.prenume;
	}
}