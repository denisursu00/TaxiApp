import { Component, ViewChild, Input, OnChanges, OnInit } from "@angular/core";
import { NomenclatorAttributesListComponent } from "./nomenclator-attributes-list/nomenclator-attributes-list.component";
import { NomenclatorAttributeDefinitionComponent } from "./nomenclator-attribute-definition/nomenclator-attribute-definition.component";
import { NomenclatorAttributeModel, ObjectUtils, ConfirmationUtils, ArrayUtils, Message, FormUtils } from "@app/shared";
import { FormGroup, FormBuilder, Validators, FormControl, AbstractControl, ValidatorFn } from "@angular/forms";
import { ValueOfNomenclatorUiAttributesSelectionField, NomenclatorUiAttributesSelectionFieldComponent } from "@app/shared";
import { NomenclatorAttributeDefinitionInputData } from "./nomenclator-attribute-definition/nomenclator-attribute-definition.component";

@Component({
	selector: "app-nomenclator-attributes-manager-component",
	templateUrl: "./nomenclator-attributes-manager.component.html"
})
export class NomenclatorAttributesManagerComponent implements OnInit {

	@Input()
	public nomenclatorAttributes: NomenclatorAttributeModel[];

	@Input()
	public nomenclatorUiAttributeNames: string[];

	@ViewChild(NomenclatorAttributesListComponent)
	public nomenclatorAttributesListComponent: NomenclatorAttributesListComponent;

	@ViewChild(NomenclatorAttributeDefinitionComponent)
	public nomenclatorAttributeDefinitionComponent: NomenclatorAttributeDefinitionComponent;

	@ViewChild(NomenclatorUiAttributesSelectionFieldComponent)
	public nomenclatorUiAttributesSelectionFieldComponent: NomenclatorUiAttributesSelectionFieldComponent;

	public nomenclatorAttributeDefinitionComponentVisible: boolean;
	public selectedNomenclatorAttribute: NomenclatorAttributeModel;
	public nomenclatorAttributeDefinitionInputData: NomenclatorAttributeDefinitionInputData;

	public formBuilder: FormBuilder;
	public form: FormGroup;

	public deleteActionEnabled: boolean;

	public nomenclatorUiAttributes: NomenclatorAttributeModel[];
	public nomenclatorUiAttributesNames: String[];

	private confirmationUtils: ConfirmationUtils;
	private messages: Message[];
	
	public constructor(confirmationUtils: ConfirmationUtils, formBuilder: FormBuilder) {
		this.confirmationUtils = confirmationUtils;
		this.formBuilder = formBuilder;
		this.nomenclatorAttributes = [];
		this.nomenclatorAttributeDefinitionComponentVisible = false;
		this.init();
	}

	private init(): void {
		this.form = this.formBuilder.group({
			uiNomenclatorAttributes: [null, this.uiAttributesValidator()]
		});
	}

	public ngOnInit(): void {
		let value: ValueOfNomenclatorUiAttributesSelectionField = new ValueOfNomenclatorUiAttributesSelectionField();
		value.candidateAttributes = this.nomenclatorAttributes;
		value.values = this.nomenclatorUiAttributeNames;
		this.uiNomenclatorAttributesFormControl.setValue(value);
	}
	
	private getFormControlByName(controlName: string): FormControl {
		return <FormControl> this.form.get(controlName);
	}

	public get uiNomenclatorAttributesFormControl(): FormControl { 
		return this.getFormControlByName("uiNomenclatorAttributes"); 
	}

	public onAddAction(event: any): void {

		this.nomenclatorAttributeDefinitionInputData = new NomenclatorAttributeDefinitionInputData;
		this.nomenclatorAttributeDefinitionInputData.editAttributeModel = null; // explicit set
		this.nomenclatorAttributeDefinitionInputData.attributeModels = this.getClonedAttributeModels();

		this.nomenclatorAttributesListComponent.onAttributeUnselected(this.selectedNomenclatorAttribute);
		this.nomenclatorAttributeDefinitionComponentVisible = true;
		this.changePerspective();
	}

	private getClonedAttributeModels(): NomenclatorAttributeModel[] {
		let clonedAttributes: NomenclatorAttributeModel[] = [];
		if (ArrayUtils.isNotEmpty(this.nomenclatorAttributes)) {
			this.nomenclatorAttributes.forEach((attributeModel: NomenclatorAttributeModel) => {
				clonedAttributes.push(attributeModel.clone());
			});
		}
		return clonedAttributes;
	}

	public onNomenclatorAttributeDefinitionCanceled(event: any): void {
		this.selectedNomenclatorAttribute = null;
		this.nomenclatorAttributeDefinitionComponentVisible = false;
		this.changePerspective();
	}

	public onNomenclatorAttributeSelectionChanged(nomenclatorAttribute: NomenclatorAttributeModel): void {
		if (ObjectUtils.isNotNullOrUndefined(nomenclatorAttribute)) {
			
			this.selectedNomenclatorAttribute = nomenclatorAttribute;

			this.nomenclatorAttributeDefinitionInputData = new NomenclatorAttributeDefinitionInputData();
			this.nomenclatorAttributeDefinitionInputData.editAttributeModel = this.selectedNomenclatorAttribute;
			this.nomenclatorAttributeDefinitionInputData.attributeModels = this.getClonedAttributeModels();

			this.nomenclatorAttributeDefinitionComponentVisible = true;
			this.changePerspective();
		} else {
			this.nomenclatorAttributeDefinitionComponentVisible = false;
		}
	}

	private changePerspective(): void {
		this.deleteActionEnabled = ObjectUtils.isNotNullOrUndefined(this.selectedNomenclatorAttribute);
	}

	public onDeleteAction(event: any): void {
		this.confirmationUtils.confirm("CONFIRM_DELETE_NOMENCLATOR_ATTRIBUTE", {
			approve: (): void => {
				this.deleteSelectedNomenclatorAttribute();
			},
			reject: (): void => {}
		});
	}

	private deleteSelectedNomenclatorAttribute(): void {
		let indexOfNomenclatorAttributeToBeDeleted: number = this.nomenclatorAttributes.indexOf(this.selectedNomenclatorAttribute);
		this.nomenclatorAttributes.splice(indexOfNomenclatorAttributeToBeDeleted, 1);
		this.nomenclatorAttributesListComponent.onAttributeUnselected(this.selectedNomenclatorAttribute);
		this.selectedNomenclatorAttribute = null;
		this.nomenclatorAttributeDefinitionComponentVisible = false;
		this.refreshNomenclatorUiAttributes();
		this.changePerspective();
	}

	public onNomenclatorAttributeDataSaved(nomenclatorAttribute: NomenclatorAttributeModel): void {
		if (ArrayUtils.isEmpty(this.nomenclatorAttributes)) {
			this.nomenclatorAttributes = [];
		}
		if (nomenclatorAttribute !== this.selectedNomenclatorAttribute) {
			let nomenclatorNameExists: boolean = false;
			this.nomenclatorAttributes.forEach((attribute: NomenclatorAttributeModel) => {
				if (attribute.name === nomenclatorAttribute.name) {
					nomenclatorNameExists = true;
				}
			});
			if (nomenclatorNameExists) {
				this.nomenclatorAttributeDefinitionComponent.nameFormControl.setErrors({nameExists: true});
				this.nomenclatorAttributeDefinitionComponent.nameFormControl.markAsDirty();
				this.nomenclatorAttributeDefinitionComponent.nameFormControl.markAsTouched();
				return;
			} else {
				this.nomenclatorAttributes.push(nomenclatorAttribute);
				this.refreshNomenclatorUiAttributes();
			}
		}
		this.nomenclatorAttributesListComponent.onAttributeUnselected(this.selectedNomenclatorAttribute);
		this.selectedNomenclatorAttribute = null;
		this.nomenclatorAttributeDefinitionComponentVisible = false;
		this.changePerspective();
	}

	private refreshNomenclatorUiAttributes(): void {
		this.nomenclatorUiAttributes = [];
		this.nomenclatorAttributes.forEach((nomenclatorAttribute) => {
			this.nomenclatorUiAttributes.push(nomenclatorAttribute);
		});

		let value: ValueOfNomenclatorUiAttributesSelectionField = new ValueOfNomenclatorUiAttributesSelectionField();
		value.candidateAttributes = this.nomenclatorAttributes;
		value.values = [];
		let currentValue: ValueOfNomenclatorUiAttributesSelectionField = this.uiNomenclatorAttributesFormControl.value;
		if (ObjectUtils.isNotNullOrUndefined(currentValue)) {
			if (ArrayUtils.isNotEmpty(currentValue.values)) {
				currentValue.values.forEach((uiName: string) => {
					this.nomenclatorUiAttributes.forEach((na: NomenclatorAttributeModel) => {
						if (uiName === na.name) {
							if (!ArrayUtils.elementExists(value.values, uiName)) {
								value.values.push(uiName);
							}
						}
					});
				});
				
			}
		}
		this.uiNomenclatorAttributesFormControl.setValue(value);
	}

	public getNomenclatorAttributes(): NomenclatorAttributeModel[] {
		return this.nomenclatorAttributesListComponent.getNomenclatorAttributes();
	}

	public getNomenclatorUiAttributes(): string[] {
		let value: ValueOfNomenclatorUiAttributesSelectionField = this.uiNomenclatorAttributesFormControl.value;
		if (ObjectUtils.isNotNullOrUndefined(value)) {
			if (ArrayUtils.isNotEmpty(value.values)) {
				return value.values;
			}
		}
		return [];
	}

	public isValid(): boolean {
		return this.validate();
	}

	private validate(): boolean {
		this.messages = [];
		FormUtils.validateAllFormFields(this.form);
		let isValid: boolean = ArrayUtils.isNotEmpty(this.nomenclatorAttributes);
		if (!isValid) {
			this.messages.push(Message.createForError("NOMANCLATOR_HAS_NO_ATTRIBUTES"));
		}
		if (!this.form.valid) {
			this.messages.push(Message.createForError("REQUIRED_FIELDS_NOT_COMPLETED"));
		}
		return isValid && this.form.valid;
	}

	public getMessages(): Message[] {
		return this.messages;
	}

	private uiAttributesValidator(): ValidatorFn | null {
		return (control: AbstractControl): {[key: string]: any} => {			
			let controlValue: ValueOfNomenclatorUiAttributesSelectionField = control.value;
			if (ObjectUtils.isNotNullOrUndefined(controlValue)) {
				if (ArrayUtils.isNotEmpty(controlValue.values)) {
					return null;
				}				
			}		
			return { required: true };
		};
	}
}