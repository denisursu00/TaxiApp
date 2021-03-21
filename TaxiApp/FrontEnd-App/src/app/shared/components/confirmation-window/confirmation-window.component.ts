import { Component, OnInit, Input, Output, EventEmitter } from "@angular/core";
import { ArrayUtils } from "./../../utils/array-utils";
import { ObjectUtils } from "./../../utils/object-utils";
import { ConfirmationCallback } from "./../../model/confirmation-callback";
import { Message, MessageType } from "./../../model";
import { TranslateUtils } from "./../../utils/translate-utils";
import { BooleanUtils } from "./../../utils/boolean-utils";

@Component({
	selector: "app-confirmation-window",
	template: `
		<p-dialog [(visible)]="windowVisible" 
				[modal]="true" 
				[closable]="false" 
				[showHeader]="true"
				[style]="{'max-width':'550px'}"
				appendTo="body">				
			<p-header>{{'LABELS.CONFIRMATION' | translate}}</p-header>
			<div *ngFor="let message of messages" style="padding-bottom: 5px;">
				<p-message *ngIf="message.translate" [severity]="message.type" [text]="'MESSAGES.' + message.code | translate"></p-message>
				<p-message *ngIf="!message.translate" [severity]="message.type" [text]="message.code"></p-message>
			</div>			
			<p-footer>
				<p-button (onClick)="onActionConfirmed()" [label]="'LABELS.YES' | translate"></p-button>
				<p-button (onClick)="onActionRejected()" [label]="'LABELS.NO' | translate"></p-button>
			</p-footer>
		</p-dialog>
	`
})
export class ConfirmationWindowComponent implements OnInit {

	@Input()
	public messages: Message[];
	
	@Input()
	public confirmationCallback: ConfirmationCallback;

	public windowVisible: boolean = true;

	public constructor() {
	}

	public ngOnInit(): void {
		this.init();
	}

	private init(): void {
		if (ArrayUtils.isEmpty(this.messages)) {
			throw new Error("messages cannot be null or empty");
		}
		if (ObjectUtils.isNullOrUndefined(this.confirmationCallback)) {
			throw new Error("confirmationCallback must be defined");
		}
	}

	public onActionConfirmed(): void {
		this.windowVisible = false;
		this.confirmationCallback.approve();
	}

	public onActionRejected(): void {
		this.windowVisible = false;
		this.confirmationCallback.reject();
	}
}

export class ConfirmationWindowFacade {

	public visible: boolean = false;
	public messages: Message[];
	public callback: ConfirmationCallback;

	public confirm(callback: ConfirmationCallback, messageCodeOrMessage: string, translateMessage?: boolean, messageType: MessageType = MessageType.WARN): void {
		this.messages = [];
		this.messages.push(new Message(messageType, messageCodeOrMessage, translateMessage));
		this.callback = callback;
		this.visible = true;
	}

	public hide(): void {
		this.visible = false;
	}
}