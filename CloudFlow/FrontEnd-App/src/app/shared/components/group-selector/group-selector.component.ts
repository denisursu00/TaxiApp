import { Component, OnInit, EventEmitter, Output, Input } from "@angular/core";
import { ArrayUtils, ObjectUtils } from "../../utils";
import { AppError } from "./../../model";
import { GroupService } from "../../service/group.service";
import { MessageDisplayer } from "@app/shared/message-displayer";
import { GroupModel } from "../../model/organization/group.model";

@Component({
	selector: "app-group-selector",
	templateUrl: "./group-selector.component.html"
})
export class GroupSelectorComponent implements OnInit {

	@Input()
	public selectionType: "single" | "multiple";

	@Output()
	public selectionChanged = new EventEmitter<GroupModel[]>();

	@Output() 
	public dataLoaded = new EventEmitter<GroupModel[]>();

	public groups: GroupModel[];
	public selectedGroups: GroupModel[];

	private groupService: GroupService;
	private messageDisplayer: MessageDisplayer;

	public constructor(groupService: GroupService, messageDisplayer: MessageDisplayer) {
		this.groupService = groupService;
		this.messageDisplayer = messageDisplayer;
	}

	public ngOnInit(): void {
		this.init();
	}

	private init(): void {
		this.reset();
		this.loadGroups();
	}

	private reset(): void {
		this.groups = [];
	}

	private loadGroups(): void {
		this.groupService.getGroups({
			onSuccess: (groups: GroupModel[]) => {
				this.groups = groups;
				this.dataLoaded.emit(groups);
			},
			onFailure: (appError: AppError) => {
				this.messageDisplayer.displayAppError(appError);
			}
		});
	}

	private getSelectedGroupModels(): GroupModel[] {
		if (ArrayUtils.isEmpty(this.selectedGroups)) {
			return [];
		}
		return this.selectedGroups;
	}

	public onSelectionChange(event): void {
		this.selectionChanged.emit(this.getSelectedGroupModels());
	}

	public isMultipleSelection(): boolean {
		if (ObjectUtils.isNullOrUndefined(this.selectionType)) {
			return true;
		}
		if (this.selectionType === "single") {
			return false;
		} else if (this.selectionType === "multiple") {
			return true;
		}
		throw new Error("Input property [selection mode] cannot have other values than [single, multiple]");
	}

}