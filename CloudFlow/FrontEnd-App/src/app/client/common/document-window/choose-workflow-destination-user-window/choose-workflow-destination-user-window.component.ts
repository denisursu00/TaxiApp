import { Component, OnInit, Input, Output, EventEmitter } from "@angular/core";
import { OrganizationTreeNodeModel, ArrayUtils } from "@app/shared";
import { Dialog } from "primeng/primeng";

@Component({
	selector: "app-choose-workflow-destination-user-window",
	template: `
		<p-dialog #pDialog [(visible)]="windowVisible" [modal]="true" [closable]="false" [showHeader]="true"
				[contentStyle]="{'height':'70vh'}" [style]="{'width':'90vw','height':'auto'}"
				(onShow)="onShow($event, pDialog)" (onHide)="onHide($event)">				
			<p-header>{{'LABELS.ORGANIZATIONAL_STRUCTURE' | translate}}</p-header>
			<app-organization-selector 
				selectionMode="single" 
				[selectableEntityTypes]="['user']"
				(selectionChanged)="onOrganizationSelectionChanged($event)">
			</app-organization-selector>
			<p-footer>
				<p-button (onClick)="onOKAction($event)" [disabled]="!okActionEnabled" label="OK"></p-button>
			</p-footer>
		</p-dialog>
	`
})
export class ChooseWorkflowDestinationUserWindowComponent implements OnInit {

	@Output()
	private destinationUserChose: EventEmitter<string>;

	public windowVisible: boolean = true;

	public okActionEnabled: boolean = false;

	public selectedUserId: string = null;

	public constructor() {
		this.destinationUserChose = new EventEmitter();
	}

	public ngOnInit(): void {
		this.init();
	}

	private init(): void {
		// nothing now
	}

	public onOrganizationSelectionChanged(selectedModels: OrganizationTreeNodeModel[]) {
		this.selectedUserId = null;
		this.okActionEnabled = false;
		if (ArrayUtils.isNotEmpty(selectedModels)) {
			selectedModels.forEach((selectedModel: OrganizationTreeNodeModel) => {
				if (selectedModel.isUser()) {
					this.selectedUserId = selectedModel.id;
				}
			});
		}
		if (this.selectedUserId !== null) {
			this.okActionEnabled = true;
		}
	}

	public onOKAction(event: any): void {
		if (this.selectedUserId === null) {
			return;
		}	
		this.destinationUserChose.emit(this.selectedUserId);
		this.windowVisible = false;		
	}

	public onHide(event: any) {
	}
	
	public onShow(event: any, pDialog: Dialog): void {
		pDialog.center();
	}
}