import { Component, AfterViewInit } from "@angular/core";
import { WorkflowTabContent } from "../workflow-tab-content";
import { WorkflowModel, WorkflowStateModel, WorkflowTransitionModel, FormUtils, ArrayUtils, ObjectUtils, ConfirmationUtils, MessageDisplayer } from "@app/shared";
import { FormGroup, FormBuilder, AbstractControl, Validators } from "@angular/forms";
import { WorkflowStateValidators } from "./state-validators";
import { StringValidators } from "@app/shared/validators";
import { SelectItem } from "primeng/primeng";

@Component({
	selector: "app-workflow-states-tab-content",
	templateUrl: "./workflow-states-tab-content.component.html",
	styleUrls: ["./workflow-states-tab-content.component.css"]
})
export class WorkflowStatesTabContentComponent extends WorkflowTabContent {

	public static readonly DEFAULT_SELECTED_STATE_TYPE_VALUE: number = 2;

	private confirmationUtils: ConfirmationUtils;
	private messageDisplayer: MessageDisplayer;

	private formBuilder: FormBuilder;
	public stateForm: FormGroup;
	
	public states: WorkflowStateModel[];
	public selectedState: WorkflowStateModel;

	public stateFormVisible: boolean = false;
	public startStateTypeDisabled: boolean = false;

	private transitions: WorkflowTransitionModel[];

	public stateTypeSelectItems: SelectItem[];

	public scrollHeight: string;

	public constructor(confirmationUtils: ConfirmationUtils, messageDisplayer: MessageDisplayer, formBuilder: FormBuilder) {
		super();
		this.confirmationUtils = confirmationUtils;
		this.messageDisplayer = messageDisplayer;
		this.formBuilder = formBuilder;
		this.init();
	}

	private init(): void {
		this.states = [];
		this.scrollHeight = (window.innerHeight - 300) + "px";
		this.prepareStateForm();
		this.prepareStateTypeSelectItems();
	}

	public prepareStateTypeSelectItems(): void {
		this.stateTypeSelectItems = [
			{ label: "LABELS.STATE_START_TYPE", value: 1},
			{ label: "LABELS.STATE_INTERMEDIATE_TYPE", value: 2},
			{ label: "LABELS.STATE_STOP_TYPE", value: 3}
		];
	}

	private getStateTypeSelectItemByValue(value: number): SelectItem {
		let stateTypeSelectItem: SelectItem = null;
		this.stateTypeSelectItems.forEach((selectItem: SelectItem) => {
			if (selectItem.value === value) {
				stateTypeSelectItem = selectItem;
			}
		});
		return stateTypeSelectItem;
	}

	private prepareStateForm(): void {
		let workflowStateProvider = {
			getStates: (): WorkflowStateModel[] => {
				return this.states;
			},
			getExcludeState: (): WorkflowStateModel => {
				return this.selectedState;
			}
		};

		this.stateForm = this.formBuilder.group({
			id: [null],
			stateCode: [null, [Validators.required, StringValidators.blank(),
				WorkflowStateValidators.duplicatedStateCodesValidation(workflowStateProvider)]],
			stateName: [null, [Validators.required, StringValidators.blank(),
				WorkflowStateValidators.duplicatedStateNamesValidation(workflowStateProvider)]],
			stateType: [WorkflowStatesTabContentComponent.DEFAULT_SELECTED_STATE_TYPE_VALUE],
			stateAttachmentPermissionAdd: [true],
			stateAttachmentPermissionDelete: [true],
			automaticRunning: [false],
			classPath: [{value: null, disabled: true}]
		});
	}

	protected doWhenNgOnInit(): void {
	}

	public onSaveState(event: any): void {
		if (!this.isFormValid()) {
			return;
		}
		if (this.isStateSelected()) {
			this.states.forEach((state: WorkflowStateModel) => {
				if (state === this.selectedState) {
					let selectedStateIndex = this.states.indexOf(this.selectedState);					
					this.states[selectedStateIndex] = this.getStateFromForm();
					this.updateStateInTranzitions(this.states[selectedStateIndex]);
				}
			});
			this.selectedState = null;
		} else {
			this.states.push(this.getStateFromForm());
		}
		this.stateFormVisible = false;
	}

	private isFormValid(): boolean {
		FormUtils.validateAllFormFields(this.stateForm);
		return this.stateForm.valid;
	}

	private updateStateInTranzitions(state: WorkflowStateModel): void {
		this.transitions.forEach((transition: WorkflowTransitionModel) => {
			if (transition.startState.code === this.selectedState.code) {
				transition.startState = state;
			}
			if (transition.finalState.code === this.selectedState.code) {
				transition.finalState = state;
			}
		});
	}

	public isStateSelected() {
		return ObjectUtils.isNotNullOrUndefined(this.selectedState);
	}

	private getStateFromForm(): WorkflowStateModel {
		let state = new WorkflowStateModel;
		state.id = this.idFormControl.value;
		state.code = this.stateCodeFormControl.value.trim();
		state.name = this.stateNameFormControl.value.trim();
		state.stateType = this.stateTypeFormControl.value;

		state.automaticRunning = this.stateAutomaticRunningFormControl.value;
		if (state.automaticRunning) {
			state.classPath = this.stateClassPathFormControl.value;
		}

		if (this.stateAttachmentPermissionAddFormControl.value && this.stateAttachmentPermissionDeleteFormControl.value) {
			state.attachmentsPermission = WorkflowStateModel.ATTACH_PERM_ALL;
		} else if (this.stateAttachmentPermissionAddFormControl.value) {
			state.attachmentsPermission = WorkflowStateModel.ATTACH_PERM_ADD;
		} else if (this.stateAttachmentPermissionDeleteFormControl.value) {
			state.attachmentsPermission = WorkflowStateModel.ATTACH_PERM_DELETE;
		} else {
			state.attachmentsPermission = WorkflowStateModel.ATTACH_NO_PERM;
		}
	
		return state;
	}

	public prepareForAdd(): void {
		// TODO: Implement
	}

	public prepareForEdit(): void {
		if (ArrayUtils.isEmpty(this.inputData.transitions)) {
			return;
		}
		this.inputData.transitions.forEach((transition: WorkflowTransitionModel) => {
			let state = transition.startState;
			if (!this.stateExists(state)) {
				this.states.push(state);
			}
			state = transition.finalState;
			if (!this.stateExists(state)) {
				this.states.push(state);
			}
		});
	}

	private stateExists(state: WorkflowStateModel): boolean {
		let filteredStates: WorkflowStateModel[] = this.states.filter(
			theState => theState.id === state.id
		);
		return ArrayUtils.isNotEmpty(filteredStates);
	}

	public populateForSave(workflowModel: WorkflowModel): void {
	}

	public isValid(): boolean {
		return true;
	}
	
	public reset(): void {
		this.selectedState = null;
		this.resetStateForm();
		this.prepareStartStateTypeVisibility();
	}

	public onAddState(event: any): void {
		this.reset();
		this.stateFormVisible = true;
	}

	public onDeleteState(event: any): void {
		if (this.isStateUsedInTransitions(this.selectedState)) {
			this.messageDisplayer.displayError("CANNOT_DELETE_STATE");
			return;
		}
		this.confirmationUtils.confirm("CONFIRM_DELETE_STATE", {
			approve: (): void => {
				let indexOfStateToBeDeleted: number = this.states.indexOf(this.selectedState);
				this.states.splice(indexOfStateToBeDeleted, 1);
				this.reset();
			},
			reject: (): void => {}
		});
	}

	public onStateSelected(event: any): void {		
		this.resetStateForm();
		this.prepareStartStateTypeVisibility();

		this.populateStateFromForm(this.selectedState);
		this.stateFormVisible = true;
	}

	public prepareStartStateTypeVisibility(): void {
		let filteredStates: WorkflowStateModel[] = this.states.filter(
			state => state.stateType === WorkflowStateModel.STATETYPE_START
		);
		this.startStateTypeDisabled = ArrayUtils.isNotEmpty(filteredStates);
	}

	private resetStateForm(): void {
		this.stateForm.reset();
		this.prepareDefaultValuesOnStateForm();
	}

	private prepareDefaultValuesOnStateForm(): void {
		this.stateTypeFormControl.setValue(WorkflowStatesTabContentComponent.DEFAULT_SELECTED_STATE_TYPE_VALUE);
		this.stateAttachmentPermissionAddFormControl.setValue(true);
		this.stateAttachmentPermissionDeleteFormControl.setValue(true);
		this.stateAutomaticRunningFormControl.setValue(false);
	}

	private populateStateFromForm(state: WorkflowStateModel): void {
		this.idFormControl.setValue(state.id);
		this.stateCodeFormControl.setValue(state.code);
		this.stateNameFormControl.setValue(state.name);
		this.stateTypeFormControl.setValue(state.stateType);
		
		this.stateAutomaticRunningFormControl.setValue(state.automaticRunning);
		this.stateClassPathFormControl.disable();
		if (state.automaticRunning) {
			this.stateClassPathFormControl.setValue(state.classPath);
			this.stateClassPathFormControl.enable();
		}

		this.stateAttachmentPermissionAddFormControl.setValue(false);
		this.stateAttachmentPermissionDeleteFormControl.setValue(false);
		if (state.attachmentsPermission === WorkflowStateModel.ATTACH_PERM_ALL) {
			this.stateAttachmentPermissionAddFormControl.setValue(true);
			this.stateAttachmentPermissionDeleteFormControl.setValue(true);
		} else if (state.attachmentsPermission === WorkflowStateModel.ATTACH_PERM_ADD) {
			this.stateAttachmentPermissionAddFormControl.setValue(true);
		} else if (state.attachmentsPermission === WorkflowStateModel.ATTACH_PERM_DELETE) {
			this.stateAttachmentPermissionDeleteFormControl.setValue(true);
		}
	}

	public setTransitions(transitions: WorkflowTransitionModel[]): void {
		this.transitions = transitions;
	}

	private isStateUsedInTransitions(state: WorkflowStateModel): boolean {
		let isUsed: boolean = false;
		this.transitions.forEach((transition: WorkflowTransitionModel) => {
			if (transition.startState.code === state.code) {
				isUsed = true;
			}
			if (transition.finalState.code === state.code) {
				isUsed = true;
			}
		});
		return isUsed;
	}

	public onStateTypeChanged(event: any): void {
		
		this.stateAutomaticRunningFormControl.disable();
		this.stateClassPathFormControl.disable();

		let stateType: number = Number(this.stateTypeFormControl.value.value);
		if (stateType === WorkflowStateModel.STATETYPE_INTERMEDIATE) {
			this.stateAutomaticRunningFormControl.enable();
		}

		if (stateType === WorkflowStateModel.STATETYPE_INTERMEDIATE && this.stateAutomaticRunningFormControl.enabled) {
			this.stateClassPathFormControl.enable();
		}
	}

	public onAutomaticRunningChanged(event: any): void {
		let automaticRunning: boolean = this.stateAutomaticRunningFormControl.value;

		this.stateClassPathFormControl.enable();
		if (!automaticRunning) {
			this.stateClassPathFormControl.disable();
		}
	}

	private getControlByName(name: string): AbstractControl {
		return this.stateForm.controls[name];
	}

	public get idFormControl(): AbstractControl {
		return this.getControlByName("id");
	}

	public get stateCodeFormControl(): AbstractControl {
		return this.getControlByName("stateCode");
	}

	public get stateNameFormControl(): AbstractControl {
		return this.getControlByName("stateName");
	}

	public get stateTypeFormControl(): AbstractControl {
		return this.getControlByName("stateType");
	}

	public get stateAttachmentPermissionAddFormControl(): AbstractControl {
		return this.getControlByName("stateAttachmentPermissionAdd");
	}

	public get stateAttachmentPermissionDeleteFormControl(): AbstractControl {
		return this.getControlByName("stateAttachmentPermissionDelete");
	}

	public get stateAutomaticRunningFormControl(): AbstractControl {
		return this.getControlByName("automaticRunning");
	}

	public get stateClassPathFormControl(): AbstractControl {
		return this.getControlByName("classPath");
	}
}