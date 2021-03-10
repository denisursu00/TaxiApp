import { Component, OnInit, Input, Output, EventEmitter } from "@angular/core";
import { ArrayUtils, ObjectUtils } from "@app/shared";
import { SelectItem } from "primeng/primeng";

@Component({
	selector: "app-choose-workflow-transition-window",
	template: `
		<p-dialog [visible]="windowVisible" [modal]="true" [closable]="false" [showHeader]="true">				
			<p-header>{{'LABELS.CHOOSE_TRANSITION' | translate}}</p-header>			
			<p-dropdown
				[options]="transitionSelectItems" 
				[(ngModel)]="selectedTransition"
				[style]="{'width':'100%'}"
				[placeholder]="'LABELS.SELECT' | translate"
				[editable]="false"
				(onChange)="onSelectionValueChanged($event)"
				filter="true"
				appendTo="body">
			</p-dropdown>
			<p-footer>
				<p-button (onClick)="onOKAction($event)" [disabled]="!okActionEnabled" label="OK"></p-button>
			</p-footer>
		</p-dialog>
	`
})
export class ChooseWorkflowTransitionWindowComponent implements OnInit {

	@Input()
	public transitions: string[];

	@Output()
	private transitionChose: EventEmitter<string>;

	public windowVisible: boolean = true;

	public transitionSelectItems: SelectItem[];
	public selectedTransition: string;

	public okActionEnabled: boolean = false;

	public constructor() {
		this.transitionChose = new EventEmitter();
	}

	public ngOnInit(): void {
		this.init();
	}

	private init(): void {
		this.transitionSelectItems = [];
		if (ArrayUtils.isNotEmpty(this.transitions)) {
			this.transitions.forEach((transition: string) => {
				this.transitionSelectItems.push({
					value: transition,
					label: transition
				});
			});
		}
	}

	public onSelectionValueChanged(event: any) {
		this.okActionEnabled = ObjectUtils.isNotNullOrUndefined(this.selectedTransition);
	}

	public onOKAction(event: any): void {
		if (ObjectUtils.isNullOrUndefined(this.selectedTransition)) {
			return;
		}	
		this.transitionChose.emit(this.selectedTransition);
		this.windowVisible = false;		
	}
}