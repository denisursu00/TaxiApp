import { Component, Input, EventEmitter, Output, OnInit } from "@angular/core";
import { OrganizationService, MessageDisplayer, AppError, GroupModel, UserModel, ArrayUtils, ObjectUtils, StringUtils, StringValidators, BaseWindow } from "@app/shared";
import { AbstractControl, FormBuilder, FormGroup, Validators } from "@angular/forms";
import { Dialog } from "primeng/primeng";

@Component({
	selector: "app-group-window",
	templateUrl: "./group-window.component.html",
	styleUrls: ["./group-window.component.css"]
})
export class GroupWindowComponent extends BaseWindow implements OnInit {

	@Input()
	private mode: "add" | "edit";
	
	@Input()
	private groupId: string;
	
	@Input()
	private groups: GroupModel[];

	@Output()
	private windowClosed: EventEmitter<void>;

	private organizationService: OrganizationService;
	private messageDisplayer: MessageDisplayer;
	private formBuilder: FormBuilder;
	public form: FormGroup;

	private selectedGroupModel: GroupModel;

	public windowVisible: boolean = false;
	public addButtonVisible: boolean;
	public saveButtonVisible: boolean;
	public width: number;

	public sourceUsers: UserModel[];

	public constructor(organizationService: OrganizationService, messageDisplayer: MessageDisplayer, formBuilder: FormBuilder) {
		super();
		this.organizationService = organizationService;
		this.messageDisplayer = messageDisplayer;
		this.formBuilder = formBuilder;
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
			users: [ new Array<UserModel>() ]
		});
	}

	private adjustSize(): void {
		this.width = window.screen.availWidth - 400;
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
		this.populateSourceUsersAndFilterByTargetUsers([]);
	}

	private prepareForEdit(): void {
		this.getGroupById(this.groupId);
		this.addButtonVisible = false;
		this.saveButtonVisible = true;
	}

	private getGroupById(groupId: string): void {
		this.organizationService.getGroupById(groupId, {
			onSuccess: (groupModel: GroupModel) => {
				this.selectedGroupModel = groupModel;
				this.form.setValue({
					name: groupModel.name,
					description: groupModel.description,
					users: groupModel.users
				});
				this.populateSourceUsersAndFilterByTargetUsers(groupModel.users);
			},
			onFailure: (appError: AppError) => {
				this.messageDisplayer.displayAppError(appError);
			}
		});
	}

	private populateSourceUsersAndFilterByTargetUsers(targetUsers: UserModel[]): void {
		this.organizationService.getUsers({
			onSuccess: (users: UserModel[]): void => {
				this.sourceUsers = users;
				if (ArrayUtils.isNotEmpty(targetUsers)) {
					this.filterSourceUsers(targetUsers);
				}
			},
			onFailure: (appError: AppError): void => {
				this.messageDisplayer.displayAppError(appError);
			}
		});
	}

	private filterSourceUsers(targetUsers: UserModel[]) {
		for (let targetUser of targetUsers) {
			this.sourceUsers = this.sourceUsers.filter(sourceUser => sourceUser.userId !== targetUser.userId);
		}
	}
	
	public get nameFormControl(): AbstractControl {
		return this.getControlByName("name");
	}

	private getControlByName(controlName: string): AbstractControl {
		return this.form.controls[controlName];
	}

	private closeWindow(): void {
		this.windowVisible = false;
		this.windowClosed.emit();
	}

	public onHide(event: any): void {
		this.closeWindow();
	}

	public onCancel(event: any): void {
		this.closeWindow();
	}

	public onAdd(event: any): void {
		if (this.form.valid) {
			let groupModel: GroupModel = this.populateGroupModel(new GroupModel());
			this.validateAndSaveGroup(groupModel);
		}
	}

	public onSave(event: any): void {
		if (this.form.valid) {
			let groupModel: GroupModel = this.populateGroupModel(this.selectedGroupModel);
			this.validateAndSaveGroup(groupModel);
		}
	}

	private populateGroupModel(groupModel: GroupModel): GroupModel {
		if (ObjectUtils.isNullOrUndefined(groupModel)) {
			groupModel = new GroupModel();
		}
		groupModel.name = this.form.controls.name.value;
		groupModel.description = this.form.controls.description.value;
		groupModel.users = this.form.controls.users.value;
		return groupModel;
	}
	
	private validateAndSaveGroup(groupModel: GroupModel): void {
		let valid: boolean = true;
		for (let group of this.groups) {
			if (groupModel.name.toLowerCase() === group.name.toLowerCase() && groupModel.id !== group.id) {
				valid = false;
			} 
		}
		if (valid) {
			this.saveGroup(groupModel);
		} else {
			this.messageDisplayer.displayError("GROUP_WITH_NAME_EXISTS");
		}
	}

	private saveGroup(groupModel: GroupModel): void {
		this.organizationService.setGroup(groupModel, {
			onSuccess: () => {
				this.closeWindow();
				this.messageDisplayer.displaySuccess("GROUP_SAVED");
			},
			onFailure: (appError: AppError) => {
				this.messageDisplayer.displayAppError(appError);
			}
		});
	}
}