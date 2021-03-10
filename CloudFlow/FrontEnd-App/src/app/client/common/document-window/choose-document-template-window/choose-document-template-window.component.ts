import { Component, OnInit, Input, Output, EventEmitter } from "@angular/core";
import { ArrayUtils, ObjectUtils, DocumentTemplateModel } from "@app/shared";

@Component({
	selector: "app-choose-document-template-window",
	template: `
		<p-dialog [visible]="windowVisible" [modal]="true" [closable]="false" [showHeader]="true">				
			<p-header>{{'LABELS.CHOOSE_TEMPLATE' | translate}}</p-header>			
			<p-dropdown
				[options]="templates" 
				[(ngModel)]="selectedTemplate"
				optionLabel="description"
				[style]="{'width':'100%'}"
				[placeholder]="'LABELS.SELECT' | translate"
				[editable]="false"
				(onChange)="onSelectionValueChanged($event)"
				filter="true"
				appendTo="body">
			</p-dropdown>
			<p-footer>
				<p-button (onClick)="onOKAction($event)" [disabled]="!okActionEnabled" label="OK"></p-button>
				<p-button (onClick)="onCancelAction($event)" [label]="'LABELS.CANCEL' | translate"></p-button>
			</p-footer>
		</p-dialog>
	`
})
export class ChooseDocumentTemplateWindowComponent implements OnInit {

	@Input()
	public templates: DocumentTemplateModel[];

	@Output()
	private templateChose: EventEmitter<DocumentTemplateModel>;

	@Output()
	private canceled: EventEmitter<void>;

	public windowVisible: boolean = true;
	public okActionEnabled: boolean = false;

	public selectedTemplate: DocumentTemplateModel;
	
	public constructor() {
		this.templateChose = new EventEmitter();
		this.canceled = new EventEmitter();
	}
	
	public ngOnInit(): void {
		this.init();
	}

	private init(): void {
		// Nothing here.
	}

	public onSelectionValueChanged(event: any) {
		this.okActionEnabled = ObjectUtils.isNotNullOrUndefined(this.selectedTemplate);
	}

	public onOKAction(event: any): void {
		if (ObjectUtils.isNullOrUndefined(this.selectedTemplate)) {
			return;
		}	
		this.templateChose.emit(this.selectedTemplate);
		this.windowVisible = false;		
	}

	public onCancelAction(event: any): void {
		this.canceled.emit();
		this.windowVisible = false;
	}
}