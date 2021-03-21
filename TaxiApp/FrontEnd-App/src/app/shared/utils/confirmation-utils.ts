import { ConfirmationService } from "primeng/api";
import { Injectable } from "@angular/core";
import { ConfirmationCallback } from "./../model/confirmation-callback";
import { TranslateUtils } from "./translate-utils";
import { Confirmation } from "primeng/components/common/confirmation";
import { ObjectUtils } from "./object-utils";
import { MessageDisplayer } from "../message-displayer";

@Injectable()
export class ConfirmationUtils {

	private confirmationService: ConfirmationService;
	private messageDisplayer: MessageDisplayer;
	private translateUtils: TranslateUtils;

	public constructor(confirmationService: ConfirmationService, messageDisplayer: MessageDisplayer, translateUtils: TranslateUtils) {
		this.confirmationService = confirmationService;
		this.messageDisplayer = messageDisplayer;
		this.translateUtils = translateUtils;
	}

	// tslint:disable-next-line:max-line-length
	public confirm(messageCode: string, callback: ConfirmationCallback, headerCode?: string, iconNearMessage?: string, confirmationDialogKey?: string) {
		/*
		if (ObjectUtils.isNullOrUndefined(headerCode)) {
			headerCode = "DEFAULT_CONFIRM_HEADER_TITLE";
		}
		let translatedHeader: string = this.translateUtils.translateMessage(headerCode);
		let translatedMessage: string = this.translateUtils.translateMessage(messageCode);
		this.confirmationService.confirm(
			this.buildConfirmationObject(translatedMessage, translatedHeader, callback, iconNearMessage, confirmationDialogKey)
		);
		*/

		// TODO - Am ales aceasta varianta pentru confirmare intrucat varianta cu <p-confirmDialog> nu
		// mai functioneaza cand e vorba de lazy loading pentru module.
		let translatedMessage: string = this.translateUtils.translateMessage(messageCode);
		if (confirm(translatedMessage)) {
			callback.approve();
		} else {
			callback.reject();
		}
	}

	// tslint:disable-next-line:max-line-length
	private buildConfirmationObject(message: string, header: string, callback: ConfirmationCallback, icon?: string, key?: string): Confirmation {
		let confirmation: Confirmation = {
			message: message,
			header: header,
			accept: callback.approve,
			reject: callback.reject,
		};
		if (ObjectUtils.isNotNullOrUndefined(icon)) {
			confirmation.icon = icon;
		}
		if (ObjectUtils.isNotNullOrUndefined(key)) {
			confirmation.key = key;
		}
		return confirmation;
	}
}