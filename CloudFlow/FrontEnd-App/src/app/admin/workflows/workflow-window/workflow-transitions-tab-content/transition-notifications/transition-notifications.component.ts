import { Component, Input, OnInit, EventEmitter, Output } from "@angular/core";
import { MenuItem } from "primeng/primeng";
import { OrganizationEntityModel } from "@app/shared/model/organization-entity.model";
import {
	TranslateUtils,
	DocumentTypeService,
	DocumentTypeModel,
	ConfirmationUtils,
	StringUtils,
	OrganizationService,
	AppError,
	MessageDisplayer,
	ObjectUtils,
	ArrayUtils,
	TransitionNotificationModelType,
	TransitionNotificationModel,
	UserMetadataTransitionNotificationModel,
	ManuallyChosenEntitiesTransitionNotificationModel,
	HierarchicalSuperiorOfUserMetadataTransitionNotificationModel
} from "@app/shared";
import { ControlValueAccessor, NG_VALUE_ACCESSOR } from "@angular/forms";

@Component({
	selector: "app-transition-notifications",
	templateUrl: "./transition-notifications.component.html",
	providers: [{
		provide: NG_VALUE_ACCESSOR,
		useExisting: TransitionNotificationsComponent,
		multi: true
	}]
})
export class TransitionNotificationsComponent implements OnInit, ControlValueAccessor {

	@Input()
	public notifications: TransitionNotificationModel[];

	@Output()
	private selectionChanged: EventEmitter<TransitionNotificationModel[]>;

	private documentTypeService: DocumentTypeService;
	private organizationService: OrganizationService;

	private translateUtils: TranslateUtils;
	private confirmationUtils: ConfirmationUtils;
	private messageDisplayer: MessageDisplayer;

	public selectedNotification: TransitionNotificationModel;

	public notificationTypeMenuItems: MenuItem[];

	public notificationWindowVisible: boolean;
	public notificationWindowMode: "add" | "edit";
	public notificationType: string;
	public notification: TransitionNotificationModel;

	private _documentTypes: DocumentTypeModel[];

	private onChange = (notifications: TransitionNotificationModel[]) => {};
	private onTouche = () => {};

	public constructor(
			documentTypeService: DocumentTypeService, organizationService: OrganizationService,
			translateUtils: TranslateUtils, confirmationUtils: ConfirmationUtils, messageDisplayer: MessageDisplayer) {
		this.documentTypeService = documentTypeService;
		this.organizationService = organizationService;
		this.translateUtils = translateUtils;
		this.messageDisplayer = messageDisplayer;
		this.confirmationUtils = confirmationUtils;
		this.selectionChanged = new EventEmitter<TransitionNotificationModel[]>();
		this.init();
	}

	private init(): void {
		this.notifications = [];
		this.notificationWindowVisible = false;
		this.prepareTransitionNotificationTypeMenuItems();
	}

	public ngOnInit(): void {
		this.setNotificationsDetailsColumnValue();
	}

	private prepareTransitionNotificationTypeMenuItems(): void {
		this.notificationTypeMenuItems = [];
		for (let transitionType in TransitionNotificationModelType) {
			if (isNaN(Number(transitionType))) {
				this.notificationTypeMenuItems.push(this.constructTransitionNotificationTypeMenuItem(transitionType));
			}
		}
	}

	private constructTransitionNotificationTypeMenuItem(transitionType: string): MenuItem {
		let menuItem: MenuItem = {
			label: this.translateUtils.translateLabel(transitionType),
			command: this.onAddNotification.bind(onclick, this, transitionType)
		};
		return menuItem;
	}

	private onAddNotification(thisComponent: TransitionNotificationsComponent, transitionType: string): void {
		thisComponent.notificationWindowMode = "add";
		thisComponent.notificationType = transitionType;
		thisComponent.notificationWindowVisible = true;
	}

	public onEditNotification(event: any): void {
		this.notificationWindowMode = "edit";
		this.notificationType = this.selectedNotification.type;
		this.notification = this.selectedNotification;
		this.notificationWindowVisible = true;
	}

	private setNotificationsDetailsColumnValue(): void {
		if (ArrayUtils.isEmpty(this.notifications)) {
			return;
		}
		this.notifications.forEach((notification: TransitionNotificationModel) => {
			this.setNotificationDetailsColumnValue(notification);
		});
	}

	private setNotificationDetailsColumnValue(notification: TransitionNotificationModel): void {
		if (notification.type === TransitionNotificationModelType.MANUALLY_CHOSEN_ENTITIES) {
			this.setDetailsForManuallyChosenEntitiesNotification(notification);
		} else if (notification.type === TransitionNotificationModelType.METADATA) {
			this.setDetailsForUserMetadataNotification(notification);
		} else if (notification.type === TransitionNotificationModelType.HIERARCHICAL_SUPERIOR_OF_USER_METADATA) {
			this.setDetailsForHierarchicalSuperiorOfUserMetadataNotification(notification);
		}
	}
	
	private setDetailsForManuallyChosenEntitiesNotification(notification: TransitionNotificationModel): void {
		let manuallyChosenEntitiesTransitionNotification = <ManuallyChosenEntitiesTransitionNotificationModel> notification;
		let manuallyChosenEntities: OrganizationEntityModel[] = manuallyChosenEntitiesTransitionNotification.manuallyChosenEntities;
		this.organizationService.getNamesForOrganizationEntities(manuallyChosenEntities, {
			onSuccess: (namesForManuallyChosenEntities: string[]): void => {
				notification.details = StringUtils.joinWith(", ", ...namesForManuallyChosenEntities);
			},
			onFailure: (appError: AppError): void => {
				this.messageDisplayer.displayAppError(appError);
			}
		});
	}
	
	private setDetailsForUserMetadataNotification(notification: TransitionNotificationModel): void {
		let userMetadataTransitionNotification: UserMetadataTransitionNotificationModel = <UserMetadataTransitionNotificationModel> notification;
		notification.details = userMetadataTransitionNotification.metadataName;
	}
	
	private setDetailsForHierarchicalSuperiorOfUserMetadataNotification(notification: TransitionNotificationModel): void {
		let hierarchicalSuperiorOfUserMetadataNotification: HierarchicalSuperiorOfUserMetadataTransitionNotificationModel = <HierarchicalSuperiorOfUserMetadataTransitionNotificationModel> notification;
		notification.details = hierarchicalSuperiorOfUserMetadataNotification.metadataName;
	}

	public onNotificationWindowClosed(event: any) {
		this.notificationWindowVisible = false;
	}

	public onSaveNotification(notification: TransitionNotificationModel): void {
		this.setNotificationDetailsColumnValue(notification);
		if (this.isEditMode() && ObjectUtils.isNotNullOrUndefined(this.selectedNotification)) {
			let indexOfSelectedNotification: number = this.notifications.indexOf(this.selectedNotification);
			this.notifications[indexOfSelectedNotification] = notification;
		} else if (this.isAddMode()) {
			this.notifications.push(notification);
		}
		this.propagateValue();
	}

	private isAddMode(): boolean {
		return this.notificationWindowMode === "add";
	}

	private isEditMode(): boolean {
		return this.notificationWindowMode === "edit";
	}

	public onDeleteNotification(event: any): void {
		this.confirmationUtils.confirm("CONFIRM_TRANSITION_NOTIFICATIONS", {
			approve: (): void => {
				let indexOfNotificationToBeDeleted = this.notifications.indexOf(this.selectedNotification);
				this.notifications.splice(indexOfNotificationToBeDeleted, 1);
				this.selectedNotification = null;
				this.propagateValue();
			},
			reject: (): void => {}
		});
	}

	public isNotificationSelected(): boolean {
		return ObjectUtils.isNotNullOrUndefined(this.selectedNotification);
	}

	private propagateValue(): void {
		this.selectionChanged.emit(this.notifications);
		this.onChange(this.notifications);
		this.onTouche();
	}

	public writeValue(notifications: TransitionNotificationModel[]): void {
		if (ArrayUtils.isEmpty(notifications)) {
			this.notifications = [];
		} else {
			this.notifications = [...notifications];
		}
		this.setNotificationsDetailsColumnValue();
	}

	public registerOnChange(fn: any): void {
		this.onChange = fn;
	}
	
	public registerOnTouched(fn: any): void {
		this.onTouche = fn;
	}

	@Input()
	public set documentTypes(documentTypes: DocumentTypeModel[]) {
		this._documentTypes = documentTypes;
	}
}
