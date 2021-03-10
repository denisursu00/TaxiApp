import { Component, OnInit, Output, EventEmitter, Input } from "@angular/core";
import { FormBuilder, FormGroup, Validators, AbstractControl } from "@angular/forms";
import { AppError, OrganizationService, MessageDisplayer, OrganizationUnitModel, UserModel, StringUtils, ArrayUtils, UiUtils, StringValidators } from "@app/shared";
import { SelectItem } from "primeng/primeng";

@Component({
	selector: "app-organization-unit-window",
	templateUrl: "./organization-unit-window.component.html"
})
export class OrganizationUnitWindowComponent implements OnInit {

	@Input()
	private mode: "add" | "edit";
	
	@Input()
	private organizationId: string = null;
	
	@Input()
	private organizationUnitId: string = null;

	@Output()
	private windowClosed: EventEmitter<void>;

	private organizationService: OrganizationService;
	private messageDisplayer: MessageDisplayer;
	private formBuilder: FormBuilder;
	private form: FormGroup;
	private selectedOrgUnit: OrganizationUnitModel;

	public windowVisible: boolean = false;
	public addButtonVisible: boolean;
	public saveButtonVisible: boolean;
	public width: number;
	public userSelectItems: SelectItem[] = new Array<SelectItem>();
	public managerFieldVisible: boolean = false;

	public constructor(formBuilder: FormBuilder, organizationService: OrganizationService, messageDisplayer: MessageDisplayer) {
		this.formBuilder = formBuilder;
		this.organizationService = organizationService;
		this.messageDisplayer = messageDisplayer;
		this.windowClosed = new EventEmitter<void>();
		this.init();
	}

	private init(): void {
		this.openWindow();
		this.prepareForm();
		this.adjustSize();
	}

	private openWindow(): void {
		this.windowVisible = true;
	}

	private prepareForm(): void {
		this.form = this.formBuilder.group({
			name: ["", [Validators.required, StringValidators.blank()]],
			description: [""],
			managerId: [""]
		});
	}

	private adjustSize(): void {
		this.width = UiUtils.getDialogWidth();
	}

	public ngOnInit(): void {
		if (this.isAdd()) {
			this.prepareForAdd();
		} else if (this.isEdit()) {
			this.prepareForEdit();
		}
	}

	private isAdd(): boolean {
		return this.mode === "add";
	}

	private isEdit(): boolean {
		return this.mode === "edit";
	}

	private prepareForAdd(): void {
		this.addButtonVisible = true;
		this.saveButtonVisible = false;
	}

	private prepareForEdit(): void {
		this.getOrgUnitById(this.organizationUnitId);
		this.addButtonVisible = false;
		this.saveButtonVisible = true;
	}

	private getOrgUnitById(organizationUnitId: string): void {
		this.organizationService.getOrgUnitById(organizationUnitId, {
			onSuccess: (organizationUnitModel: OrganizationUnitModel): void => {
				this.selectedOrgUnit = organizationUnitModel;
				this.form.patchValue({
					name: organizationUnitModel.name,
					description: organizationUnitModel.description
				});
				this.organizationService.getUsersFromOrgUnit(organizationUnitModel.id, {
					onSuccess: (users: UserModel[]): void => {						
						if (ArrayUtils.isNotEmpty(users)) {
							this.userSelectItems = [...this.userSelectItems, ...this.prepareUserSelectItems(users)];
							this.managerFieldVisible = true;
							if (StringUtils.isNotBlank(organizationUnitModel.managerId)) {
								users.forEach(user => {
									if (user.userId === organizationUnitModel.managerId) {
										this.form.patchValue({
											managerId: user.userId
										});
									}
								});
							}
						} else {
							this.managerFieldVisible = false;
						}
					},
					onFailure: (appError: AppError): void => {
						this.messageDisplayer.displayAppError(appError);
					}
				})
			},
			onFailure: (appError: AppError): void => {
				this.messageDisplayer.displayAppError(appError);
			}
		});
	}

	private prepareUserSelectItems(users: UserModel[]): SelectItem[] {
		let userSelectItems: SelectItem[] = [];
		users.forEach(user => {
			userSelectItems.push(this.createUserSelectItem(user));
		});
		return userSelectItems;
	}

	private createUserSelectItem(user: UserModel): SelectItem {
		let userSelectItem: SelectItem = {
			label: user.name,
			value: user.userId
		};
		return userSelectItem;
	}
	
	private get nameFormControl(): AbstractControl {
		return this.getControlByName("name");
	}

	private getControlByName(controlName: string): AbstractControl {
		return this.form.controls[controlName];
	}

	private closeWindow(): void {
		this.windowVisible = false;
		this.windowClosed.emit();
	}

	private onHide(event: any): void {
		this.windowClosed.emit();
	}

	public onCancelAction(event: any): void {
		this.windowClosed.emit();
	}

	public onSaveAction(event: any): void {
		if (this.form.valid) {
			this.selectedOrgUnit = this.populateOrgUnitForSave(this.selectedOrgUnit);
			this.saveOrgUnit(this.selectedOrgUnit);
		}
	}

	private populateOrgUnitForSave(organizationUnitModel: OrganizationUnitModel): OrganizationUnitModel {
		organizationUnitModel.name = this.form.value.name;
		organizationUnitModel.description = this.form.value.description;
		if (StringUtils.isNotBlank(this.form.value.managerId)) {
			organizationUnitModel.managerId = this.form.value.managerId;
		}
		return organizationUnitModel;
	}

	public onAddAction(event: any): void {
		if (this.form.valid) {
			let organizationUnit: OrganizationUnitModel = new OrganizationUnitModel();
			organizationUnit = this.populateOrgUnitForAdd(organizationUnit);
			this.saveOrgUnit(organizationUnit);
		}
	}

	private populateOrgUnitForAdd(organizationUnitModel: OrganizationUnitModel): OrganizationUnitModel {
		if (StringUtils.isNotBlank(this.organizationId)) {
			organizationUnitModel.parentOrganizationId = this.organizationId;
		} else if (StringUtils.isNotBlank(this.organizationUnitId)) {
			organizationUnitModel.parentOrganizationUnitId = this.organizationUnitId;
		}
		organizationUnitModel.name = this.form.value.name;
		organizationUnitModel.description = this.form.value.description;
		return organizationUnitModel;
	}

	private saveOrgUnit(organizationUnitModel: OrganizationUnitModel) {
		this.organizationService.setOrgUnit(organizationUnitModel, {
			onSuccess: () => {
				this.messageDisplayer.displaySuccess("ORG_UNIT_SAVED");
				this.windowClosed.emit();
			},
			onFailure: (appError: AppError): void => {
				this.messageDisplayer.displayAppError(appError);
			}
		});
	}
}