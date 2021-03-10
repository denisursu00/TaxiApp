import { Component, OnInit, Input, Output, EventEmitter } from "@angular/core";
import { TaskStatus, UiUtils, BaseWindow } from "@app/shared";
import { Dialog } from "primeng/primeng";

@Component({
	selector: "app-project-with-dsp-in-progress-tasks-window",
	templateUrl: "./project-with-dsp-in-progress-tasks-window.component.html"
})
export class ProjectWithDspInProgressTasksWindowComponent extends BaseWindow implements OnInit {

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
		this.statusFilter = TaskStatus.IN_PROGRESS;
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
