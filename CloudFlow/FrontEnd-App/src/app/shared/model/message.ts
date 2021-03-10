import { ObjectUtils } from "./../utils";

export class Message {

	public type: "success" | "info" | "warn" | "error";
	public code: string;
	public translate?: boolean = true;

	public constructor(type: any, code: string, translate?: boolean) {
		this.type = type;
		this.code = code;
		if (ObjectUtils.isNotNullOrUndefined(translate)) {
			this.translate = translate;
		}
	}

	public static createForError(messageCodeOrMessage: string, translate?: boolean): Message {
		return new Message("error", messageCodeOrMessage, translate);
	}

	public static createForWarn(messageCodeOrMessage: string, translate?: boolean): Message {
		return new Message("warn", messageCodeOrMessage, translate);
	}

	public static createForInfo(messageCodeOrMessage: string, translate?: boolean): Message {
		return new Message("info", messageCodeOrMessage, translate);
	}

	public static createForSuccess(messageCodeOrMessage: string, translate?: boolean): Message {
		return new Message("success", messageCodeOrMessage, translate);
	}
}

export enum MessageType {

	WARN = "warn",
	ERROR = "error",
	INFO = "info",
	SUCCESS = "success"
}