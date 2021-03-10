import { ArrayUtils, ObjectUtils, StringUtils } from "./../../../utils";
import { NomenclatorAttributeModel } from "./../../../model";
import { NomenclatorRunExpressionsResponseModel } from "./../../../model";

export class AttributeEventMediator {

	private handlers: AttributeEventHandler[];

	public constructor() {
		this.handlers = [];
	}

	public subscribe(handler: AttributeEventHandler): void {		
		if (ObjectUtils.isNullOrUndefined(handler)) {
			throw new Error("handler cannot be null/undefined");
		}
		this.handlers.push(handler);
	}

	public fireEvent(event: AttributeEvent): void {	
		if (ObjectUtils.isNullOrUndefined(event)) {
			throw new Error("event cannot be null/undefined");
		}	
		this.handlers.forEach((handler: AttributeEventHandler) => {
			if (handler.eventName === event.eventName) {
				if (StringUtils.isNotBlank(handler.attributeKey)) {
					if (ObjectUtils.isNotNullOrUndefined(event.attributeDefinition) && event.attributeDefinition.key === handler.attributeKey) {
						handler.handle(event);
					}
				} else {
					handler.handle(event);
				}				
			}
		});
	}
}

export interface AttributeEventHandler {
	eventName: AttributeEventName;
	attributeKey?: string;
	handle(event: AttributeEvent): void;
}

export interface AttributeEvent {
	eventName: AttributeEventName;
	attributeDefinition?: NomenclatorAttributeModel; 
	attributeValue?: any;
	attributeValues?: any;
	expressionsRanResponse?: NomenclatorRunExpressionsResponseModel;
}

export enum AttributeEventName {
	ATTRIBUTE_INITIALIZED = "ATTRIBUTE_INITIALIZED",
	ATTRIBUTE_VALUE_CHANGE = "ATTRIBUTE_VALUE_CHANGE",
	ALL_ATTRIBUTES_READY = "ALL_ATTRIBUTES_READY",
	EXPRESSIONS_RAN = "EXPRESSIONS_RAN"
}