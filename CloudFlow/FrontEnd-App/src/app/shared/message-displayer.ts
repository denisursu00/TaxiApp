import { Message } from "primeng/components/common/api";
import { MessageService } from "primeng/components/common/messageservice";
import { Injectable } from "@angular/core";
import { AppError } from "./model";
import { TranslateUtils } from "./utils/translate-utils";
import { BooleanUtils } from "./utils/boolean-utils";
import { ObjectUtils } from "./utils/object-utils";

@Injectable({providedIn: "root"})
export class MessageDisplayer {

	private messageService: MessageService;
	private translateUtils: TranslateUtils;

	public constructor(messageService: MessageService, translateUtils: TranslateUtils) {
		this.messageService = messageService;
		this.translateUtils = translateUtils;
	}

	public displaySuccess(messageCode: string, translate?: boolean): void {
		this.displayMessage("success", messageCode, translate);
	}

	public displayInfo(messageCode: string, translate?: boolean): void {
		this.displayMessage("info", messageCode, translate);
	}

	public displayWarn(messageCode: string, translate?: boolean): void {
		this.displayMessage("warn", messageCode, translate);
	}

	public displayError(errorOrErrorCode: string, translate?: boolean): void {
		this.displayMessage("error", errorOrErrorCode, translate);
	}

	public displayAppError(appError: AppError): void {
		this.displayError(appError.errorCode);
	}

	public displayMessage(severity: string, messageOrMessageCode: string, translate?: boolean): void {
		
		// tslint:disable-next-line:no-inferrable-types
		let titleCode: string = severity.toUpperCase();
		let messageSeverity: string = severity;
		let detail: string = messageOrMessageCode;
		if (ObjectUtils.isNullOrUndefined(translate) || BooleanUtils.isTrue(translate)) {
			detail = this.translateUtils.translateMessage(messageOrMessageCode);
		}
		const message: Message = {
			severity: messageSeverity,
			summary: this.translateUtils.translateLabel(titleCode),
			detail: detail
		};
		this.messageService.add(message);
	}
}