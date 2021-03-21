import { Component, OnInit, Input, Output, EventEmitter } from "@angular/core";
import { ArrayUtils } from "@app/shared/utils/array-utils";
import { Message } from "./../../model/message";

@Component({
	selector: "app-messages-window",
	template: `
		<p-dialog [(visible)]="windowVisible" [modal]="true" [closable]="true" [showHeader]="true" (onHide)="onHide($event)" appendTo="body">				
			<p-header>{{'LABELS.MESSAGES_WINDOW_DEFAULT_HEADER_TEXT' | translate}}</p-header>
			<div *ngFor="let message of messages" style="padding-bottom: 5px;">
				<p-message *ngIf="message.translate" [severity]="message.type" [text]="'MESSAGES.' + message.code | translate"></p-message>
				<p-message *ngIf="!message.translate" [severity]="message.type" [text]="message.code"></p-message>
			</div>			
			<p-footer>
				<p-button (onClick)="onOKAction($event)" label="OK"></p-button>
			</p-footer>
		</p-dialog>
	`
})
export class MessagesWindowComponent implements OnInit {

	@Input()
	public messages: Message[];
	
	@Output()
	private windowClosed: EventEmitter<void>;
	
	public windowVisible: boolean = false;

	public constructor() {
		this.windowClosed = new EventEmitter();
	}

	public ngOnInit(): void {
		this.init();
	}

	private init(): void {
		if (ArrayUtils.isEmpty(this.messages)) {
			throw new Error("messages cannot be null or empty");
		}
		this.windowVisible = true;
	}

	public onHide(event: any): void {
		this.close();
	}

	public onOKAction(event: any): void {
		this.close();
	}

	private close(): void {
		this.windowVisible = false;		
		this.windowClosed.emit();
	}
}