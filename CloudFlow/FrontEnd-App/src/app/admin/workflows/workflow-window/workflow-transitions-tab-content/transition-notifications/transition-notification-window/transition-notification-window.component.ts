import { Component, Input, OnInit, Output, EventEmitter } from "@angular/core";
import { FormGroup, FormBuilder, Validators, AbstractControl } from "@angular/forms";
import { SelectItem } from "primeng/primeng";
import {
	TransitionNotificationModelType,
	TransitionNotificationModel,
	DocumentTypeModel,
	MetadataDefinitionModel,
	AssignedEntityTransitionNotificationModel,
	InitiatorTransitionNotificationModel,
	HierarchicalSuperiorOfInitiatorTransitionNotificationModel,
	ManuallyChosenEntitiesTransitionNotificationModel,
	UserMetadataTransitionNotificationModel,
	ObjectUtils,
	FormUtils,
	ArrayUtils,
	HierarchicalSuperiorOfUserMetadataTransitionNotificationModel
} from "@app/shared";

@Component({
	selector: "app-transition-notification-window",
	templateUrl: "./transition-notification-window.component.html"
})
export class TransitionNotificationWindowComponent implements OnInit {

	@Input()
	public mode: "add" | "edit";

	@Input()
	public notificationType: TransitionNotificationModelType;

	@Input()
	public documentTypes: DocumentTypeModel[];

	@Input()
	public notification: TransitionNotificationModel;

	@Output()
	public windowClosed: EventEmitter<void>;

	@Output()
	public notificationSaved: EventEmitter<TransitionNotificationModel>;

	private formBuilder: FormBuilder;
	public form: FormGroup;

	public userMetadatasSelectItems: SelectItem[];

	public userMetadataSelectorVisible: boolean;
	public manuallyChosenEntitiesSelectorVisible: boolean;

	public width: number | string;
	public height: number | string;
	public visible: boolean;

	public constructor(formBuilder: FormBuilder) {
		this.formBuilder = formBuilder;
		this.windowClosed = new EventEmitter<void>();
		this.notificationSaved = new EventEmitter<TransitionNotificationModel>();
		this.init();
	}

	private init(): void {
		this.visible = false;
		this.userMetadataSelectorVisible = false;
		this.manuallyChosenEntitiesSelectorVisible = false;
		this.userMetadatasSelectItems = [];
		this.prepareForm();
		this.adjustSize();
	}

	private adjustSize(): void {
		this.height = "auto";
		this.width = window.screen.availWidth - 400;
	}

	private prepareForm(): void {
		this.form = this.formBuilder.group({
			emailSubject: [null, Validators.required],
			emailBody: [null, Validators.required],
			userMetadata: [null],
			manuallyChosenEntities: [null]
		});
	}

	public ngOnInit(): void {
		if (this.mode === "add") {
			this.prepareForAdd();
		} else if (this.mode === "edit") {
			this.prepareForEdit();
		}
	}

	private prepareForAdd(): void {
		this.changeFormPerspective();
		this.visible = true;
	}

	private prepareForEdit(): void {
		this.changeFormPerspective();
		this.emailSubjectFormControl.setValue(this.notification.emailSubjectTemplate);
		this.emailBodyFormControl.setValue(this.notification.emailContentTemplate);
		this.populateTypeSpecificFields();
		this.visible = true;
	}

	private changeFormPerspective(): void {
		if (this.notificationType === TransitionNotificationModelType.METADATA) {
			this.userMetadataSelectorVisible = true;
			this.userMetadataFormControl.setValidators(Validators.required);
		} else if (this.notificationType === TransitionNotificationModelType.MANUALLY_CHOSEN_ENTITIES) {
			this.manuallyChosenEntitiesSelectorVisible = true;
			this.manuallyChosenEntitiesFormControl.setValidators(Validators.required);
		} else if (this.notificationType === TransitionNotificationModelType.HIERARCHICAL_SUPERIOR_OF_USER_METADATA) {
			this.userMetadataSelectorVisible = true;
			this.userMetadataFormControl.setValidators(Validators.required);
		}
	}

	public onSave(): void {
		if (!this.isValid()) {
			return;
		}
		let notification: TransitionNotificationModel = this.getNotificationFromForm();
		this.notificationSaved.emit(notification);
		this.windowClosed.emit();
	}

	public isValid(): boolean {
		FormUtils.validateAllFormFields(this.form);
		return this.form.valid;
	}

	private getNotificationFromForm(): TransitionNotificationModel {
		let notification: TransitionNotificationModel = this.createNotificationInstance();
		
		this.setCommonPropertiesFromForm(notification);
		this.setSpecificPropertiesFromForm(notification);

		return notification;
	}

	private createNotificationInstance(): TransitionNotificationModel {
		if (this.notificationType === TransitionNotificationModelType.ASSIGNED_ENTITY) {
			return new AssignedEntityTransitionNotificationModel();
		} else if (this.notificationType === TransitionNotificationModelType.INITIATOR) {
			return new InitiatorTransitionNotificationModel();
		} else if (this.notificationType === TransitionNotificationModelType.HIERARCHICAL_SUPERIOR_OF_INITIATOR) {
			return new HierarchicalSuperiorOfInitiatorTransitionNotificationModel();
		} else if (this.notificationType === TransitionNotificationModelType.MANUALLY_CHOSEN_ENTITIES) {
			return new ManuallyChosenEntitiesTransitionNotificationModel();
		} else if (this.notificationType === TransitionNotificationModelType.METADATA) {
			return new UserMetadataTransitionNotificationModel();
		} else if (this.notificationType === TransitionNotificationModelType.HIERARCHICAL_SUPERIOR_OF_USER_METADATA) {
			return new HierarchicalSuperiorOfUserMetadataTransitionNotificationModel();
		} else {
			throw new Error("Tip necunoscut de notificare: " + this.notificationType);
		}
	}

	private setCommonPropertiesFromForm(notification: TransitionNotificationModel): void {
		if (ObjectUtils.isNotNullOrUndefined(this.notification)) {
			notification.id = this.notification.id;
		}
		notification.emailSubjectTemplate = this.emailSubjectFormControl.value;
		notification.emailContentTemplate = this.emailBodyFormControl.value;
	}

	private setSpecificPropertiesFromForm(notification: TransitionNotificationModel): void {
		if (this.notificationType === TransitionNotificationModelType.MANUALLY_CHOSEN_ENTITIES) {
			let manuallyChosenEntities: ManuallyChosenEntitiesTransitionNotificationModel = <ManuallyChosenEntitiesTransitionNotificationModel> notification;
			manuallyChosenEntities.manuallyChosenEntities = this.manuallyChosenEntitiesFormControl.value;
		} else if (this.notificationType === TransitionNotificationModelType.METADATA) {
			let userMetadataNotification: UserMetadataTransitionNotificationModel = <UserMetadataTransitionNotificationModel> notification;
			let metadataName: string = (<MetadataDefinitionModel>this.userMetadataFormControl.value).name;
			userMetadataNotification.metadataName = metadataName;
		} else if (this.notificationType === TransitionNotificationModelType.HIERARCHICAL_SUPERIOR_OF_USER_METADATA) {
			let hierarchicalSuperiorOfUserMetadataTransitionNotificationModel: HierarchicalSuperiorOfUserMetadataTransitionNotificationModel = <HierarchicalSuperiorOfUserMetadataTransitionNotificationModel> notification;
			let metadataName: string = (<MetadataDefinitionModel>this.userMetadataFormControl.value).name;
			hierarchicalSuperiorOfUserMetadataTransitionNotificationModel.metadataName = metadataName;
		}
	}

	private populateTypeSpecificFields(): void {
		if (this.notificationType === TransitionNotificationModelType.MANUALLY_CHOSEN_ENTITIES) {
			let manuallyChosenEntities: ManuallyChosenEntitiesTransitionNotificationModel = <ManuallyChosenEntitiesTransitionNotificationModel> this.notification;
			this.manuallyChosenEntitiesFormControl.setValue(manuallyChosenEntities.manuallyChosenEntities);
		} else if (this.notificationType === TransitionNotificationModelType.METADATA) {
			let userMetadataNotification: UserMetadataTransitionNotificationModel = <UserMetadataTransitionNotificationModel> this.notification;
			this.userMetadataFormControl.setValue(userMetadataNotification.metadataName);
			this.setUserMetadataFormControlValue(userMetadataNotification.metadataName);
		} else if (this.notificationType === TransitionNotificationModelType.HIERARCHICAL_SUPERIOR_OF_USER_METADATA) {
			let hierarchicalSuperiorOfUserMetadataTransitionNotificationModel: HierarchicalSuperiorOfUserMetadataTransitionNotificationModel = <HierarchicalSuperiorOfUserMetadataTransitionNotificationModel> this.notification;
			this.setUserMetadataFormControlValue(hierarchicalSuperiorOfUserMetadataTransitionNotificationModel.metadataName);
		}
	}

	private setUserMetadataFormControlValue(userMetadataName: string): void {
		this.documentTypes.forEach((documentType: DocumentTypeModel) => {
			documentType.metadataDefinitions.forEach((metadata: MetadataDefinitionModel) => {
				if (metadata.name === userMetadataName) {
					this.userMetadataFormControl.setValue(metadata);
				}
			});
		});
	}

	public onHide(): void {
		this.visible = false;
		this.windowClosed.emit();
	}

	private getControlByName(controlName: string): AbstractControl {
		return this.form.controls[controlName];
	}
	
	public get emailSubjectFormControl(): AbstractControl {
		return this.getControlByName("emailSubject");
	}
	
	public get emailBodyFormControl(): AbstractControl {
		return this.getControlByName("emailBody");
	}
	
	public get userMetadataFormControl(): AbstractControl {
		return this.getControlByName("userMetadata");
	}
	
	public get manuallyChosenEntitiesFormControl(): AbstractControl {
		return this.getControlByName("manuallyChosenEntities");
	}
}
