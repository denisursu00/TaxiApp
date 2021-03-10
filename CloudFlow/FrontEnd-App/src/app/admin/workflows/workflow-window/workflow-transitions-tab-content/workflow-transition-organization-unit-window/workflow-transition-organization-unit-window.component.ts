import { Component, Output, EventEmitter } from "@angular/core";
import { OrganizationTreeNodeModel, ObjectUtils } from "@app/shared";

@Component({
	selector: "app-workflow-transition-organization-unit-window",
	templateUrl: "./workflow-transition-organization-unit-window.component.html"
})
export class WorkflowTransitionOrganizationUnitWindowComponent {

	@Output()
	public selected: EventEmitter<OrganizationTreeNodeModel>;

	@Output()
	public windowClosed: EventEmitter<void>;

	private selectedOrganization: OrganizationTreeNodeModel;

	public windowVisible: boolean = true;
	public width: number;
	public height: string;

	public constructor() {
		this.selected = new EventEmitter<OrganizationTreeNodeModel>();
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
		this.selected.emit(this.selectedOrganization);
		this.windowClosed.emit();
	}

	public onCancelAction(event: any): void {
		this.windowClosed.emit();
	}

	public onOrganizationSelectionChanged(selectedOrganization: OrganizationTreeNodeModel): void {
		if (ObjectUtils.isNullOrUndefined(selectedOrganization)) {
			throw new Error("The groups array cannot be empty.");
		}
		this.selectedOrganization = selectedOrganization;
	}
}
