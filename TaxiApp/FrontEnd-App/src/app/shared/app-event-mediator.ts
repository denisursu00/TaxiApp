import { Dictionary } from "typescript-collections";

export abstract class AppEvent {
}

export interface AppEventHandler<E extends AppEvent> {
	handle(event: E): void;
}

export class AppEventMediator {

	static subscribers: Dictionary<Function, AppEventHandler<AppEvent>[]> = new Dictionary<Function, AppEventHandler<AppEvent>[]>();

	static subscribe<E extends AppEvent>(eventClass: Function, eventHandler: AppEventHandler<E>): void {
		let eventHandlers: AppEventHandler<AppEvent>[] = this.subscribers.getValue(eventClass);
		if (typeof eventHandlers === "undefined") {
			eventHandlers = [];
		}
		eventHandlers.push(eventHandler);
		this.subscribers.setValue(eventClass, eventHandlers);
	}
	
	static unsubscribe(unsubscribeEventClass: Function, unsubscribeEventHandler: AppEventHandler<AppEvent>): void {
		this.subscribers.forEach((eventClass: Function, eventHandlers: AppEventHandler<AppEvent>[]) => {
			if (unsubscribeEventClass === eventClass) {
				eventHandlers.forEach((eventHandler: AppEventHandler<AppEvent>, index: number) => {
					if (eventHandler === unsubscribeEventHandler) {
						eventHandlers.splice(index, 1);
					}
				});
			}			
		});
	}

	static fire(event: AppEvent) {
		this.subscribers.forEach((eventClass: Function, eventHandlers: AppEventHandler<AppEvent>[]) => {
			if (event instanceof eventClass) {
				eventHandlers.forEach((eventHandler: AppEventHandler<AppEvent>) => {
					eventHandler.handle(event);
				});
			}			
		});
	}
}