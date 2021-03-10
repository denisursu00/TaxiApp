import { ArrayUtils, ObjectUtils, MetadataDefinitionModel, StringUtils } from "@app/shared";

export class MetadataEventMediator {

	private handlers: MetadataEventHandler[];

	public constructor() {
		this.handlers = [];
	}

	public subscribe(handler: MetadataEventHandler): void {		
		if (ObjectUtils.isNullOrUndefined(handler)) {
			throw new Error("handler cannot be null/undefined");
		}
		this.handlers.push(handler);
	}

	public fireEvent(event: MetadataEvent): void {	
		if (ObjectUtils.isNullOrUndefined(event)) {
			throw new Error("event cannot be null/undefined");
		}	
		this.handlers.forEach((handler: MetadataEventHandler) => {
			if (handler.eventName === event.eventName) {
				if (StringUtils.isNotBlank(handler.metadataName)) {
					if (ObjectUtils.isNotNullOrUndefined(event.metadataDefinition) && event.metadataDefinition.name === handler.metadataName) {
						handler.handle(event);
					}
				} else {
					handler.handle(event);
				}				
			}
		});
	}
}

export interface MetadataEventHandler {
	eventName: MetadataEventName;
	metadataName?: string;
	handle(event: MetadataEvent): void;
}

export interface MetadataEvent {
	eventName: MetadataEventName;
	metadataDefinition?: MetadataDefinitionModel; 
	metadataValue?: any;
	metadatasValues?: any; // using for ALL_METADATAS_READY event
}

export enum MetadataEventName {
	METADATA_INITIALIZED = "METADATA_INITIALIZED",
	METADATA_VALUE_CHANGE = "METADATA_VALUE_CHANGE",
	ALL_METADATAS_READY = "ALL_METADATAS_READY"
}