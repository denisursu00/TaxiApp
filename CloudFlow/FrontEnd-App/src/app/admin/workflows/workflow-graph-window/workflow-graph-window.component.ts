import { Component, Input, OnInit, Output, EventEmitter } from "@angular/core";
import { ObjectUtils, ServletPathConstants, UrlBuilder, BaseWindow } from "@app/shared";
import { environment } from "@app/../environments/environment";
import { Dialog } from "primeng/primeng";

@Component({
	selector: "app-workflow-graph-window",
	templateUrl: "./workflow-graph-window.component.html"
})
export class WorkflowGraphWindowComponent extends BaseWindow implements OnInit {

	private static readonly URL_PARAMETER_WORKFLOW_ID: string = "workflowId";

	@Input()
	public workflowId: number;

	@Output()
	public windowClosed: EventEmitter<void>;

	public visible: boolean;
	public width: number;
	public height: number | string;

	public workflowGraphImageUrl: string;

	public constructor() {
		super();
		this.windowClosed = new EventEmitter<void>();
		this.init();
	}

	private init(): void {
		this.visible = true;
		this.height = "auto";
		this.adjustSize();
	}

	private adjustSize(): void {
		this.width = window.screen.availWidth - 400;
	}

	public ngOnInit(): void {
		if (ObjectUtils.isNullOrUndefined(this.workflowId)) {
			throw new Error("Property [workflowId] cannot be null or undefined.");
		}
		this.workflowGraphImageUrl = this.createWorkflowGraphImageUrl(this.workflowId);
	}

	private createWorkflowGraphImageUrl(workflowId: number): string {
		let workflowGraphImageUrl = ServletPathConstants.GET_WORKFLOW_IMAGE_SERVLET;
		let urlBuilder: UrlBuilder = new UrlBuilder(
			workflowGraphImageUrl, {
				key: WorkflowGraphWindowComponent.URL_PARAMETER_WORKFLOW_ID,
				value: workflowId
			}, {
				key: "random",
				value: (new Date()).getTime()
			}
		);
		return urlBuilder.build();
	}

	public onClose(): void {
		this.visible = false;
		this.windowClosed.emit();
	}

	public onHide(evemt: any): void {
		this.onClose();
	}
}
