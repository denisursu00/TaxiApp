import { Component, Input, Output, EventEmitter, OnInit } from "@angular/core";
import { OrganizationService } from "../../service/organization.service";
import { UserModel } from "../../model/organization/user.model";
import { ControlValueAccessor, NG_VALUE_ACCESSOR } from "@angular/forms";
import { MessageDisplayer } from "../../message-displayer";
import { AppError } from "../../model/app-error";
import { ObjectUtils } from "../../utils/object-utils";
import { ArrayUtils } from "../../utils/array-utils";

@Component({
	selector: "app-user-selector",
	templateUrl: "./user-selector.component.html",
	providers: [{
		provide: NG_VALUE_ACCESSOR,
		useExisting: UserSelectorComponent,
		multi: true
	}]
})
export class UserSelectorComponent implements OnInit, ControlValueAccessor {

	@Input()
	public readonly: boolean = false;

	@Input()
	public displayFirst: boolean = false;
	
	@Input()
	public labelDisplayMode: "name" | "name-title";

	@Input()
	public userId: string;

	@Input()
	public activated: boolean = true;

	@Output()
	private selectionChanged: EventEmitter<string>;

	private organizationService: OrganizationService;
	private messageDisplayer: MessageDisplayer;

	public users: UserModel[];
	public selectedUser: UserModel;

	public optionLabel: string;

	private onChange = (userId: string) => {};
	private onTouche = () => {};

	public constructor(organizationService: OrganizationService, messageDisplayer: MessageDisplayer) {
		this.organizationService = organizationService;
		this.messageDisplayer = messageDisplayer;
		this.selectionChanged = new EventEmitter<string>();
		this.init();
	}

	private init(): void {
		this.users = [];
	}

	public ngOnInit(): void {
		this.setOptionLabel();
		this.populateUsers();
	}

	private setOptionLabel(): void {
		if (ObjectUtils.isNullOrUndefined(this.labelDisplayMode)) {
			this.optionLabel = "name";
		} else if (this.labelDisplayMode === "name") {
			this.optionLabel = "name";
		} else if (this.labelDisplayMode === "name-title") {
			this.optionLabel = "displayName";
		}
	}

	private populateUsers(): void {
		if ( this.activated == true ) { 
			this.organizationService.getAllActiveUsers({
				onSuccess: (users: UserModel[]): void => {
					this.users = users;
					this.setSelectedUserByUserId(this.userId);
				},
				onFailure: (appError: AppError): void => {
					this.messageDisplayer.displayAppError(appError);
				}
			});
		} else {
			this.organizationService.getUsers({
				onSuccess: (users: UserModel[]): void => {
					this.users = users;
					this.setSelectedUserByUserId(this.userId);
				},
				onFailure: (appError: AppError): void => {
					this.messageDisplayer.displayAppError(appError);
				}
			});
		}
	}

	public onUserChanged(value: any): void {
		let userId: string = null;
		if (ObjectUtils.isNotNullOrUndefined(this.selectedUser)) {
			userId = this.selectedUser.userId;
		}
		this.selectionChanged.emit(userId);
		this.propagateValue();
	}

	private propagateValue(): void {
		let userId: string = null;
		if (ObjectUtils.isNotNullOrUndefined(this.selectedUser)) {
			userId = this.selectedUser.userId;
		}
		this.onChange(userId);
		this.onTouche();
	}

	public writeValue(userId: string): void {
		this.userId = userId;
		this.setSelectedUserByUserId(this.userId);
	}

	public registerOnChange(fn: any): void {
		this.onChange = fn;
	}
	
	public registerOnTouched(fn: any): void {
		this.onTouche = fn;
	}

	public setDisabledState(isDisabled: boolean): void {
		this.readonly = isDisabled;
	}

	private setSelectedUserByUserId(userId: string): void {
		if (ObjectUtils.isNullOrUndefined(userId)) {
			return;
		}
		
		let filteredUsers: UserModel[] = this.users.filter((user: UserModel) => {
			return user.userId === String(userId);
		});
		if (filteredUsers.length > 1) {
			throw Error("Two or more users cannot have same id. ID = [" + filteredUsers[0].userId + "]");
		}
		if (ArrayUtils.isEmpty(filteredUsers)) {
			return;
		}

		this.selectedUser = filteredUsers[0];
		this.propagateValue();
		this.selectionChanged.emit(this.selectedUser.userId);
	}
}
