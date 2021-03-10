import { Component, OnInit, Output, EventEmitter, Input } from "@angular/core";
import { UiUtils, TaskStatus, BaseWindow } from "@app/shared";
import { Dialog } from "primeng/primeng";

@Component({
	selector: "app-project-with-dsp-finalized-tasks-window",
	templateUrl: "./project-with-dsp-finalized-tasks-window.component.html"
})
export class ProjectWithDspFinalizedTasksWindowComponent extends BaseWindow implements OnInit {

	@Input()
	public projectId: number;
	
	@Output()
	public windowClosed: EventEmitter<void>;

	public statusFilterDisabled: boolean;

	public statusFilter: TaskStatus;

	public windowVisible: boolean = false;

	public constructor() {
		super();
		this.windowClosed = new EventEmitter<void>();

		this.init();
	}

	private init(): void {
		
		this.statusFilterDisabled = true;
		this.statusFilter = TaskStatus.FINALIZED;
	}

	public ngOnInit(): void {
		this.windowVisible = true;
	}

	public onHide(event: any): void {
		this.windowClosed.emit();
	}

	public onCancel(): void {
		this.windowClosed.emit();
	}
}
