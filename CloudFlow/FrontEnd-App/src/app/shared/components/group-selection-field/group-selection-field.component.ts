import { Component, Input, Output, OnInit, EventEmitter } from "@angular/core";
import { ControlValueAccessor, NG_VALUE_ACCESSOR } from "@angular/forms";
import { StringUtils, ObjectUtils, ArrayUtils } from "./../../utils";
import { GroupService } from "./../../service";
import { AppError, GroupModel } from "./../../model";
import { MessageDisplayer } from "./../../message-displayer";

@Component({
	selector: "app-group-selection-field",
	template: `
		<span *ngIf="loading">{{'MESSAGES.LOADING' | translate}}</span>
		<p-dropdown *ngIf="!loading"
			[options]="groups" 
			[(ngModel)]="selectedGroup"
			optionLabel="name"
			filter="true"
			[style]="{'width':'100%'}"
			autoDisplayFirst="false"
			[placeholder]="'LABELS.SELECT' | translate" 
			(onChange)="onSelectionChanged($event)"
			filter="true"
			(onBlur)="onSelectionBlured($event)">
		</p-dropdown>
	`,
	providers: [
		{ provide: NG_VALUE_ACCESSOR, useExisting: GroupSelectionFieldComponent, multi: true }
	]
})
export class GroupSelectionFieldComponent implements ControlValueAccessor, OnInit {

	@Output()
	public selectionChanged: EventEmitter<string>;

	public groups: GroupModel[];
	public selectedGroup: GroupModel;
	public loading: boolean;

	private groupService: GroupService;
	private messageDisplayer: MessageDisplayer;

	private fieldValue: string;

	private onChange: any = () => {};
	private onTouched: any = () => {};

	public constructor(groupService: GroupService, messageDisplayer: MessageDisplayer) {
		this.groupService = groupService;
		this.messageDisplayer = messageDisplayer;
		this.selectionChanged = new EventEmitter();
		this.loading = true;
	}

	public ngOnInit(): void {
		this.loadGroups();
	}

	private loadGroups(): void {
		this.groupService.getGroups({
			onSuccess: (groups: GroupModel[]) => {
				this.groups = groups;
				this.selectGroup();
				this.loading = false;
			},
			onFailure: (appError: AppError) => {
				this.messageDisplayer.displayAppError(appError);
			}
		});	
	}

	private selectGroup(): void {
		this.selectedGroup = null;
		if (ArrayUtils.isEmpty(this.groups)) {
			return;
		}
		if (ObjectUtils.isNullOrUndefined(this.fieldValue)) {
			return;
		}
		this.groups.forEach((group: GroupModel) => {
			if (this.fieldValue === group.id) {
				this.selectedGroup = group;
			}
		});
	}

	private propagateValue(): void {
		if (ObjectUtils.isNullOrUndefined(this.selectedGroup)) {
			this.fieldValue = null;
		} else {
			this.fieldValue = this.selectedGroup.id;
		}
		this.onChange(this.fieldValue);
		this.onTouched();
	}

	public onSelectionChanged(event: any): void {
		this.propagateValue();
		this.selectionChanged.emit(this.fieldValue);
	}

	public onSelectionBlured(event: any): void {
		this.onTouched();
	}

	public writeValue(groupId: string): void {
		this.fieldValue = groupId;
		this.selectGroup();
	}
	
	public registerOnChange(fn: any): void {
		this.onChange = fn;
	}

	public registerOnTouched(fn: any): void {
		this.onTouched = fn;
	}
}