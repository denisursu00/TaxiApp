import { Component, Input } from "@angular/core";
import { WorkflowTabContent } from "../workflow-tab-content";
// tslint:disable-next-line:max-line-length
import { WorkflowModel, GroupModel, OrganizationTreeNodeModel, ArrayUtils, FormUtils, DocumentTypeModel, WorkflowStateModel, WorkflowTransitionModel, MetadataDefinitionModel, ObjectUtils, OrganizationService, AppError, MessageDisplayer, ConfirmationUtils, OrganizationUnitModel, BooleanUtils } from "@app/shared";
import { FormBuilder, FormGroup, AbstractControl, Validators, ValidatorFn } from "@angular/forms";
import { StringValidators } from "@app/shared/validators";
import { TransitionRoutingTypeModel } from "@app/shared/model/bpm/transition-routing-type.model";
import { TransitionDeadlineActionModel } from "@app/shared/model/bpm/transition-deadline-action.model";
import { TransitionValidators, WorkflowTransitionsAndStatesProvider } from "./transition-validators";
import { OrganizationEntityModel } from "@app/shared/model/organization-entity.model";
import { ListItemUtils } from "@app/shared/utils/list-item-utils";

@Component({
	selector: "app-workflow-transitions-tab-content",
	templateUrl: "./workflow-transitions-tab-content.component.html",
	styleUrls: ["./workflow-transitions-tab-content.component.css"]
})
export class WorkflowTransitionsTabContentComponent extends WorkflowTabContent {
	
	private organizationService: OrganizationService;
	private messageDisplayer: MessageDisplayer;
	private confirmationUtils: ConfirmationUtils;

	private formBuilder: FormBuilder;
	public form: FormGroup;
	public formVisible: boolean;

	public transitions: WorkflowTransitionModel[];
	public selectedTransition: WorkflowTransitionModel;
	public documentTypes: DocumentTypeModel[];

	public destinationGroupWindowVisible: boolean;
	public transitionDestinationOrganizationUnitWindowVisible: boolean;

	public transitionDeadlineActions: TransitionDeadlineActionModel[];

	public startStates: WorkflowStateModel[];
	public finalStates: WorkflowStateModel[];

	public deleteTransitionButtonDisabled: boolean;

	public scrollHeight: string;

	public constructor(organizationService: OrganizationService, messageDisplayer: MessageDisplayer,
			confirmationUtils: ConfirmationUtils, formBuilder: FormBuilder) {
		super();
		this.organizationService = organizationService;
		this.messageDisplayer = messageDisplayer;
		this.confirmationUtils = confirmationUtils;
		this.formBuilder = formBuilder;
		this.init();
	}

	private init(): void {
		this.formVisible = false;
		this.destinationGroupWindowVisible = false;
		this.transitionDestinationOrganizationUnitWindowVisible = false;
		this.deleteTransitionButtonDisabled = true;
		this.transitionDeadlineActions = TransitionDeadlineActionModel.getDeadlineActions();
		this.startStates = [];
		this.finalStates = [];
		this.transitions = [];
		this.scrollHeight = (window.innerHeight - 300) + "px";
		this.prepareForm();
		this.changeFormPerspective();
	}

	private prepareForm(): void {
		let workflowTransitionsProvider: WorkflowTransitionsAndStatesProvider = {
			getTransitions: (): WorkflowTransitionModel[] => {
				return this.transitions;
			},
			getExcludeTransition: (): WorkflowTransitionModel => {
				return this.selectedTransition;
			},
			getStartState: (): WorkflowStateModel => {
				return this.transitionStartStateFormControl.value;
			}
		};

		this.form = this.formBuilder.group({
			transitionName: [null, [Validators.required, StringValidators.blank(),
				TransitionValidators.transitionNameValidator(workflowTransitionsProvider)]],
			transitionStartState: [null, [TransitionValidators.transitionStartStateValidator(workflowTransitionsProvider)]],
			transitionFinalState: [null, [TransitionValidators.transitionFinalStateValidator(workflowTransitionsProvider)]],
			transitionConditionalRouting: [false],
			transitionRoutingCondition: [null],
			transitionAdditionalViewingRights: [null],
			transitionRoutingType: [null, Validators.required],
			notifications: [null],
			availableForAutomaticActionsOnly: [false],
			transitionDeadlineAction: [false],
			transitionDeadlineActionType: [null, Validators.required],
			transitionDeadlinePeriod: [null],
			transitionDeadlineNotifyResendInterval: [null],
			transitionDestinationGroup: [null, Validators.required],
			transitionDestinationOrganizationUnit: [null, Validators.required],
			transitionDestinationParameter: [null, Validators.required],
			uiSendConfirmation: [false]
		});
	}

	protected doWhenNgOnInit(): void {
		this.subscribeToFormControlsChangeEvents();
	}

	public setDocumentsTypes(documentTypes: DocumentTypeModel[]): void {
		this.documentTypes = [...documentTypes];
	}

	public setStates(states: WorkflowStateModel[]): void {
		this.startStates = [];
		this.finalStates = [];
		states.forEach((state: WorkflowStateModel) => {
			if (state.stateType === WorkflowStateModel.STATETYPE_START) {
				this.startStates = [...this.startStates, state];
			} else if (state.stateType === WorkflowStateModel.STATETYPE_INTERMEDIATE) {
				this.startStates = [...this.startStates, state];
				this.finalStates = [...this.finalStates, state];
			} else if (state.stateType === WorkflowStateModel.STATETYPE_STOP) {
				this.finalStates = [...this.finalStates, state];
			}
		});

		ListItemUtils.sort(this.startStates, "name");
		ListItemUtils.sort(this.finalStates, "name");
	}

	private subscribeToFormControlsChangeEvents(): void {
		this.transitionRoutingTypeFormControl.valueChanges.subscribe(() => {
			this.changeRoutingRelatedFormControlsPerspective();
		});
		this.transitionConditionalRoutingFormControl.valueChanges.subscribe(() => {
			this.changeTransitionConditionalRoutingFormControlPerspective();
		});
		this.transitionDeadlineActionFormControl.valueChanges.subscribe(() => {
			this.changeRelatedTransitionDeadlineFormControlsPerspective();
			this.changeTransitionDeadlineNotifyResendIntervalFormControlPerspective();
		});
		this.transitionDeadlineActionTypeFormControl.valueChanges.subscribe(() => {
			this.changeTransitionDeadlineNotifyResendIntervalFormControlPerspective();
		});
		this.transitionFinalStateFormControl.valueChanges.subscribe(() => {
			this.changeTransitionDeadlineActionControlPerspective();
			this.changeRelatedTransitionDeadlineFormControlsPerspective();
			this.changeRoutingRelatedFormControlsPerspective();
		});
	}

	public prepareForAdd(): void {
	}

	public prepareForEdit(): void {
		this.transitions = this.inputData.transitions;
	}

	public populateForSave(workflowModel: WorkflowModel): void {
		this.transitions.forEach((transition: WorkflowTransitionModel) => {
			if (transition.finalState.automaticRunning) {
				transition.routingType = null;
				transition.routingDestinationId = null;
				transition.routingDestinationParameter = null;
			}
		});
		workflowModel.transitions = this.transitions;
	}

	public isValid(): boolean {

		let startFound: boolean = false;
		let finalFound: boolean = false;
		let intermediateFound: boolean = false;

		if (ArrayUtils.isEmpty(this.transitions)) {
			this.messageDisplayer.displayError("NO_TRANSITION_ADDED");
			return false;
		}

		this.transitions.forEach((transition: WorkflowTransitionModel) => {
			if (transition.startState.stateType === WorkflowStateModel.STATETYPE_START) {
				startFound = true;
			}
			if (transition.finalState.stateType === WorkflowStateModel.STATETYPE_INTERMEDIATE) {
				intermediateFound = true;
			}
			if (transition.finalState.stateType === WorkflowStateModel.STATETYPE_STOP) {
				finalFound = true;
			}
		});

		if (!startFound && !finalFound) {
			this.messageDisplayer.displayError("NO_TRANSITION_START_STOP_ADDED");
		} else if (!startFound) {
			this.messageDisplayer.displayError("NO_TRANSITION_START_STATE_ADDED");
		} else if (!finalFound) {
			this.messageDisplayer.displayError("NO_TRANSITION_STOP_STATE_ADDED");
		} else if (!intermediateFound) {
			this.messageDisplayer.displayError("NO_TRANSITION_INTERMEDIATE_STATE_ADDED");
		}

		return startFound && intermediateFound && finalFound;
	}
	
	public reset(): void {
		this.form.reset();
		this.selectedTransition = null;
		this.formVisible = false;
		this.deleteTransitionButtonDisabled = true;
	}

	public saveTransition(): void {
		if (!this.isFormValid()) {
			return;
		}

		let transition: WorkflowTransitionModel = new WorkflowTransitionModel();
		this.populateTransitionModelFromForm(transition);

		if (ObjectUtils.isNotNullOrUndefined(this.selectedTransition)) {
			transition.id = this.selectedTransition.id;

			let transitionIndex: number = this.transitions.indexOf(this.selectedTransition);
			this.transitions[transitionIndex] = transition;
		} else {
			this.transitions.push(transition);
		}
		this.reset();
	}

	private isFormValid(): boolean {
		FormUtils.validateAllFormFields(this.form);
		return this.form.valid;
	}

	public onAddTransition(event: any): void {
		this.reset();
		this.formVisible = true;
	}

	public onDeleteTransition(event: any): void {
		this.confirmationUtils.confirm("CONFIRM_DELETE_TRANSITION", {
			approve: (): void => {
				let indexOfTransisionToBeDeleted = this.transitions.indexOf(this.selectedTransition);
				this.transitions.splice(indexOfTransisionToBeDeleted, 1);
				this.reset();
			},
			reject: (): void => {}
		});
	}

	public onTransitionSelected(event: any): void {
		this.form.reset();
		this.populateFormFromModel(this.selectedTransition);
		this.formVisible = true;
		this.deleteTransitionButtonDisabled = false;
		this.changeTransitionRoutingTypePerspective();
	}

	public onTransitionUnselected(event: any): void {
		this.reset();
	}

	private populateFormFromModel(transition: WorkflowTransitionModel): void {
		this.transitionNameFormControl.setValue(transition.name);
		this.transitionStartStateFormControl.setValue(transition.startState);
		this.transitionFinalStateFormControl.setValue(transition.finalState);
		
		if (ObjectUtils.isNotNullOrUndefined(transition.routingCondition)) {
			this.transitionConditionalRoutingFormControl.setValue(true);
			this.transitionRoutingConditionFormControl.setValue(transition.routingCondition);
		}

		this.transitionAdditionalViewingRightsFormControl.setValue(transition.extraViewers);

		if (ObjectUtils.isNotNullOrUndefined(transition.routingType)) {
			this.transitionRoutingTypeFormControl.setValue(transition.routingType);

			if (transition.routingType === TransitionRoutingTypeModel.ROUTING_GROUP) {
				this.setTransitionDestinationGroup(transition);
			} else if (transition.routingType === TransitionRoutingTypeModel.ROUTING_OU) {
				this.setTransitionDestinationOrganizationUnit(transition);
			} else if (transition.routingType === TransitionRoutingTypeModel.ROUTING_PARAMETER) {
				this.setTransitionDestinationParameter(transition);
			} else if (transition.routingType === TransitionRoutingTypeModel.ROUTING_PARAMETER_HIERARCHICAL_SUP) {
				this.setTransitionDestinationParameter(transition);
			}
		}

		this.notificationsFormControl.setValue(transition.notifications);
		this.availableForAutomaticActionsOnlyFormControl.setValue(transition.availableForAutomaticActionsOnly);
		
		this.transitionDeadlineActionTypeFormControl.setValue(transition.deadlineActionType);

		if (ObjectUtils.isNotNullOrUndefined(transition.deadlineAction) && transition.deadlineAction) {
			this.transitionDeadlineActionFormControl.setValue(true);
			this.transitionDeadlinePeriodFormControl.setValue(transition.deadlinePeriod);

			if (transition.deadlineActionType === TransitionDeadlineActionModel.DEADLINE_ACTION_NOTIFY) {
				this.transitionDeadlineNotifyResendIntervalFormControl.setValue(transition.deadlineNotifyResendInterval);
			} 
		}
		this.uiSendConfirmationFormControl.setValue(BooleanUtils.isTrue(transition.uiSendConfirmation));
	}

	private setTransitionDestinationGroup(transition: WorkflowTransitionModel): void {
		this.organizationService.getGroupById(transition.routingDestinationId.toString(), {
			onSuccess: (group: GroupModel): void => {
				this.transitionDestinationGroupFormControl.setValue(group);
			},
			onFailure: (appError: AppError): void => {
				this.messageDisplayer.displayAppError(appError);
			}
		});
	}

	private setTransitionDestinationOrganizationUnit(transition: WorkflowTransitionModel): void {
		this.organizationService.getOrgUnitById(transition.routingDestinationId.toString(), {
			onSuccess: (organizationUnit: OrganizationUnitModel): void => {
				let organizationTreeNode: OrganizationTreeNodeModel = new OrganizationTreeNodeModel();
				organizationTreeNode.id = organizationUnit.id;
				organizationTreeNode.name = organizationUnit.name;
				organizationTreeNode.managerId = organizationUnit.managerId;
				organizationTreeNode.description = organizationUnit.description;
				this.transitionDestinationOrganizationUnitFormControl.setValue(organizationTreeNode);
			},
			onFailure: (appError: AppError): void => {
				this.messageDisplayer.displayAppError(appError);
			}
		});
	}

	private setTransitionDestinationParameter(transition: WorkflowTransitionModel): void {
		this.documentTypes.forEach((documentType: DocumentTypeModel) => {
			documentType.metadataDefinitions.forEach((metadata: MetadataDefinitionModel) => {
				if (metadata.name === transition.routingDestinationParameter) {
					this.transitionDestinationParameterFormControl.setValue(metadata);
				}
			});
		});
	}

	private populateTransitionModelFromForm(transition: WorkflowTransitionModel): void {
		transition.name = this.transitionNameFormControl.value;
		transition.startState = this.transitionStartStateFormControl.value;
		transition.finalState = this.transitionFinalStateFormControl.value;
		
		if (this.transitionConditionalRoutingFormControl.value) {
			transition.routingCondition = this.transitionRoutingConditionFormControl.value;
		} else {
			transition.routingCondition = null;
		}

		transition.extraViewers = this.transitionAdditionalViewingRightsFormControl.value;
		transition.notifications = this.notificationsFormControl.value;

		if ((<WorkflowStateModel>this.transitionFinalStateFormControl.value).stateType !== WorkflowStateModel.STATETYPE_STOP) {
			transition.routingType = this.transitionRoutingTypeFormControl.value;
			transition.routingDestinationId = null;
			transition.routingDestinationParameter = null;
			
			if (this.transitionRoutingTypeFormControl.value === TransitionRoutingTypeModel.ROUTING_GROUP) {
				transition.routingDestinationId = Number((<GroupModel>this.transitionDestinationGroupFormControl.value).id);
			} else if (this.transitionRoutingTypeFormControl.value === TransitionRoutingTypeModel.ROUTING_OU) {
				transition.routingDestinationId = Number((<OrganizationTreeNodeModel>this.transitionDestinationOrganizationUnitFormControl.value).id);
			} else if (this.transitionRoutingTypeFormControl.value === TransitionRoutingTypeModel.ROUTING_PARAMETER) {
				transition.routingDestinationParameter = (<MetadataDefinitionModel>this.transitionDestinationParameterFormControl.value).name;
			} else if (this.transitionRoutingTypeFormControl.value === TransitionRoutingTypeModel.ROUTING_PARAMETER_HIERARCHICAL_SUP) {
				transition.routingDestinationParameter = (<MetadataDefinitionModel>this.transitionDestinationParameterFormControl.value).name;
			}

			transition.availableForAutomaticActionsOnly = this.availableForAutomaticActionsOnlyFormControl.value;
			if (this.transitionDeadlineActionFormControl.value) {
				transition.deadlineAction = true;

				transition.deadlineActionType = this.transitionDeadlineActionTypeFormControl.value;
				transition.deadlinePeriod = Number(this.transitionDeadlinePeriodFormControl.value);
				if (this.transitionDeadlineActionTypeFormControl.value === TransitionDeadlineActionModel.DEADLINE_ACTION_NOTIFY) {
					if (ObjectUtils.isNotNullOrUndefined(this.transitionDeadlineNotifyResendIntervalFormControl.value)) {
						transition.deadlineNotifyResendInterval = Number(this.transitionDeadlineNotifyResendIntervalFormControl.value);
					} else {
						transition.deadlineNotifyResendInterval = 0;
					}
				}
			} else {
				transition.deadlineAction = false;
				transition.deadlinePeriod = null;
				transition.deadlineActionType = null;
				transition.deadlineNotifyResendInterval = null;
			}
		} else {
			transition.routingType = null;
			transition.routingDestinationId = null; 
			transition.routingDestinationParameter = null;
			
			transition.availableForAutomaticActionsOnly = this.availableForAutomaticActionsOnlyFormControl.value;
			
			transition.deadlineAction = false;
			transition.deadlinePeriod = null;
			transition.deadlineActionType = null;
			transition.deadlineNotifyResendInterval = null;
		}
		transition.uiSendConfirmation = BooleanUtils.isTrue(this.uiSendConfirmationFormControl.value);
	}

	public onGroupSelected(group: GroupModel): void {
		this.transitionDestinationGroupFormControl.setValue(group);
	}

	public onGroupWindowClosed(): void {
		this.destinationGroupWindowVisible = false;
	}

	public openDestinationGroupWindow(): void {
		this.destinationGroupWindowVisible = true;
	}

	public openTransitionDestinationOrganizationUnitWindow(): void {
		this.transitionDestinationOrganizationUnitWindowVisible = true;
	}

	public onOrganizationUnitSelected(organizationTreeNodeModel: OrganizationTreeNodeModel[]): void {
		if (ArrayUtils.isEmpty(organizationTreeNodeModel)) {
			throw new Error("organizationTreeNodeModel can't be empty");
		}
		this.transitionDestinationOrganizationUnitFormControl.setValue(organizationTreeNodeModel[0]);
	}

	public onTransitionDestinationOrganizationUnitWindowClosed(event: any): void {
		this.transitionDestinationOrganizationUnitWindowVisible = false;
	}

	public changeFormPerspective(): void {
		this.changeTransitionConditionalRoutingFormControlPerspective();
		this.changeRelatedTransitionDeadlineFormControlsPerspective();
		this.changeTransitionDeadlineNotifyResendIntervalFormControlPerspective();
		this.changeRoutingRelatedFormControlsPerspective();
	}

	public changeRoutingRelatedFormControlsPerspective(): void {

		this.prepareControlState(this.transitionDestinationGroupFormControl,
			this.transitionRoutingTypeFormControl.enable && this.transitionRoutingTypeFormControl.value === TransitionRoutingTypeModel.ROUTING_GROUP, [Validators.required]);
		
		this.prepareControlState(this.transitionDestinationOrganizationUnitFormControl,
			this.transitionRoutingTypeFormControl.enable && this.transitionRoutingTypeFormControl.value === TransitionRoutingTypeModel.ROUTING_OU, [Validators.required]);

		this.prepareControlState(this.transitionDestinationParameterFormControl,
			this.transitionRoutingTypeFormControl.enable && (this.transitionRoutingTypeFormControl.value === TransitionRoutingTypeModel.ROUTING_PARAMETER || this.transitionRoutingTypeFormControl.value === TransitionRoutingTypeModel.ROUTING_PARAMETER_HIERARCHICAL_SUP), [Validators.required]);
	}

	private changeTransitionConditionalRoutingFormControlPerspective(): void {
		this.prepareControlState(this.transitionRoutingConditionFormControl,
			this.transitionConditionalRoutingFormControl.value, [Validators.required]);
	}

	private changeRelatedTransitionDeadlineFormControlsPerspective(): void {
		this.prepareControlState(this.transitionDeadlinePeriodFormControl,
			this.transitionDeadlineActionFormControl.enabled && this.transitionDeadlineActionFormControl.value, [Validators.required]);
		
		this.prepareControlState(this.transitionDeadlineNotifyResendIntervalFormControl,
			this.transitionDeadlineActionFormControl.enabled && this.transitionDeadlineActionFormControl.value, [Validators.required]);
			
		this.prepareControlState(this.transitionDeadlineActionTypeFormControl,
			this.transitionDeadlineActionFormControl.enabled && this.transitionDeadlineActionFormControl.value, [Validators.required]);
	}

	private changeTransitionDeadlineNotifyResendIntervalFormControlPerspective(): void {
		this.prepareControlState(this.transitionDeadlineNotifyResendIntervalFormControl,
			this.transitionDeadlineActionTypeFormControl.value === TransitionDeadlineActionModel.DEADLINE_ACTION_NOTIFY, [Validators.required]);
	}

	private changeTransitionDeadlineActionControlPerspective(): void {
		if (ObjectUtils.isNullOrUndefined(this.transitionFinalStateFormControl.value)) {
			return;
		}
		this.prepareControlState(this.transitionDeadlineActionFormControl,
			(<WorkflowStateModel>this.transitionFinalStateFormControl.value).stateType !== WorkflowStateModel.STATETYPE_STOP, []);
	}

	private changeTransitionRoutingTypePerspective(): void {
		let finalStateAutoRunning: boolean = (<WorkflowStateModel>this.transitionFinalStateFormControl.value).automaticRunning;
		let isFinalStateAnEndState = (<WorkflowStateModel>this.transitionFinalStateFormControl.value).stateType !== WorkflowStateModel.STATETYPE_STOP;
		
		this.prepareControlState(this.transitionRoutingTypeFormControl,
			!finalStateAutoRunning && isFinalStateAnEndState, [Validators.required]);
	}

	private prepareControlState(control: AbstractControl, enabled: boolean, validators: ValidatorFn[]): void {
		if (enabled) {
			control.enable();
			control.setValidators(validators);
		} else if (!enabled) {
			control.disable();
			control.clearValidators();
		}
	}

	public endStateChanged(): void {
		if (ObjectUtils.isNotNullOrUndefined(this.transitionFinalStateFormControl.value)) {
			this.changeTransitionRoutingTypePerspective();
		}
	}

	private getControlByName(name: string): AbstractControl {
		return this.form.controls[name];
	}

	public get transitionNameFormControl(): AbstractControl {
		return this.getControlByName("transitionName");
	}

	public get transitionStartStateFormControl(): AbstractControl {
		return this.getControlByName("transitionStartState");
	}

	public get transitionFinalStateFormControl(): AbstractControl {
		return this.getControlByName("transitionFinalState");
	}

	public get transitionConditionalRoutingFormControl(): AbstractControl {
		return this.getControlByName("transitionConditionalRouting");
	}

	public get transitionRoutingConditionFormControl(): AbstractControl {
		return this.getControlByName("transitionRoutingCondition");
	}

	public get transitionAdditionalViewingRightsFormControl(): AbstractControl {
		return this.getControlByName("transitionAdditionalViewingRights");
	}

	public get transitionRoutingTypeFormControl(): AbstractControl {
		return this.getControlByName("transitionRoutingType");
	}
	
	public get notificationsFormControl(): AbstractControl {
		return this.getControlByName("notifications");
	}
	
	public get availableForAutomaticActionsOnlyFormControl(): AbstractControl {
		return this.getControlByName("availableForAutomaticActionsOnly");
	}
	
	public get transitionDeadlineActionFormControl(): AbstractControl {
		return this.getControlByName("transitionDeadlineAction");
	}
	
	public get transitionDeadlineActionTypeFormControl(): AbstractControl {
		return this.getControlByName("transitionDeadlineActionType");
	}
	
	public get transitionDeadlinePeriodFormControl(): AbstractControl {
		return this.getControlByName("transitionDeadlinePeriod");
	}
	
	public get transitionDeadlineNotifyResendIntervalFormControl(): AbstractControl {
		return this.getControlByName("transitionDeadlineNotifyResendInterval");
	}
	
	public get transitionDestinationGroupFormControl(): AbstractControl {
		return this.getControlByName("transitionDestinationGroup");
	}
	
	public get transitionDestinationOrganizationUnitFormControl(): AbstractControl {
		return this.getControlByName("transitionDestinationOrganizationUnit");
	}
	
	public get transitionDestinationParameterFormControl(): AbstractControl {
		return this.getControlByName("transitionDestinationParameter");
	}

	public get uiSendConfirmationFormControl(): AbstractControl {
		return this.getControlByName("uiSendConfirmation");
	}
}