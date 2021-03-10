import { Component, Input, Output, EventEmitter } from "@angular/core";
import { GroupModel, ObjectUtils } from "@app/shared";

@Component({
	selector: "app-workflow-transition-destination-group-window",
	templateUrl: "./workflow-transition-destination-group-window.component.html"
})
export class WorkflowTransitionDestinationGroupWindowComponent {

	@Output()
	public selected: EventEmitter<GroupModel>;

	@Output()
	public windowClosed: EventEmitter<void>;

	private selectedGroup: GroupModel;

	public windowVisible: boolean = true;
	public width: number;
	public height: string;

	public constructor() {
		this.selected = new EventEmitter<GroupModel>();
		this.windowClosed = new EventEmitter<void>();
		this.init();
	}

	private init(): void {
		this.adjustSize();
	}

	private adjustSize(): void {
		this.height = "auto";
	}

	public onHide(event: any): void {
		this.windowClosed.emit();
	}

	public onSelectAction(event: any): void {
		this.selected.emit(this.selectedGroup);
		this.windowClosed.emit();
	}

	public onCancelAction(event: any): void {
		this.windowClosed.emit();
	}

	public onGroupSelected(group: GroupModel): void {
		if (ObjectUtils.isNullOrUndefined(group)) {
			throw new Error("The groups array cannot be empty.");
		}
		this.selectedGroup = group;
	}
}
