import { Component, Input, EventEmitter, Output } from "@angular/core";
import { DirectoryUserModel } from "@app/shared/model/organization/directory-user.model";
import { Column } from "primeng/primeng";

@Component({
	selector: "app-users-found-in-directory-window",
	templateUrl: "./users-found-in-directory-window.component.html",
})
export class UsersFoundInDirectoryWindowComponent {

	private static readonly COLUMN_USERNAME: string = "username";
	private static readonly COLUMN_FIRST_NAME: string = "firstName";
	private static readonly COLUMN_LAST_NAME: string = "lastName";
	private static readonly COLUMN_EMAIL: string = "email";
	private static readonly COLUMN_TITLE: string = "title";

	@Input()
	public usersFoundInDirectory: DirectoryUserModel[] = [];

	@Input()
	private organizationId: string = null;
	
	@Input()
	private organizationUnitId: string = null;

	@Output()
	private windowClosed: EventEmitter<void>;

	@Output()
	private directoryUserSelected: EventEmitter<DirectoryUserModel>;

	public columns: Column[];
	public selectedUser: DirectoryUserModel;
	public windowVisible: boolean = false;
	public width: number;
	public selectButtonDisabled: boolean = true;

	public constructor() {
		this.windowClosed = new EventEmitter<void>();
		this.directoryUserSelected = new EventEmitter<DirectoryUserModel>();
		this.init();
	}

	private init(): void {
		this.prepareColumns();
		this.openWindow();
		this.adjustSize();
	}

	private openWindow(): void {
		this.windowVisible = true;
	}

	private adjustSize(): void {
		this.width = window.screen.availWidth - 400;
	}

	private prepareColumns(): void {
		this.columns = [];
		this.columns.push(
			this.buildColumn("LABELS.USERNAME", UsersFoundInDirectoryWindowComponent.COLUMN_USERNAME),
			this.buildColumn("LABELS.FIRST_NAME", UsersFoundInDirectoryWindowComponent.COLUMN_FIRST_NAME),
			this.buildColumn("LABELS.LAST_NAME", UsersFoundInDirectoryWindowComponent.COLUMN_LAST_NAME),
			this.buildColumn("LABELS.EMAIL", UsersFoundInDirectoryWindowComponent.COLUMN_EMAIL),
			this.buildColumn("LABELS.TITLE", UsersFoundInDirectoryWindowComponent.COLUMN_TITLE)
		);
	}

	private buildColumn(header: string, field: string): Column {
		let column: Column = new Column();
		column.header = header;
		column.field = field;
		return column;
	}

	private onHide(event: any): void {
		this.windowClosed.emit();
	}

	public onRowSelect(event): void {
		this.selectButtonDisabled = false;
	}

	public onRowUnselect(event): void {
		this.selectButtonDisabled = true;
	}

	public onSelectAction(event: any): void {
		this.windowClosed.emit();
		this.selectedUser.organizationId = this.organizationId;
		this.selectedUser.organizationUnitId = this.organizationUnitId;
		this.directoryUserSelected.emit(this.selectedUser);
	}
 }