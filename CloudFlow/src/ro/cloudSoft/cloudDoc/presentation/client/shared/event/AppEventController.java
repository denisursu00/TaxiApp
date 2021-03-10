package ro.cloudSoft.cloudDoc.presentation.client.shared.event;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AppEventController {

	private static Map<AppEventType, List<AppEventHandler>> handlerMap = new HashMap<AppEventType, List<AppEventHandler>>();
	
	public static void subscribe(AppEventHandler handler, AppEventType eventType) {
		List<AppEventHandler> eventHandlers = getHandlerListForEventType(eventType);
		eventHandlers.add(handler);
	}
	
	public static void fireEvent(AppEventType eventType) {
		AppEvent event = new AppEvent(eventType);
		List<AppEventHandler> appEventSubscribers = getHandlerListForEventType(eventType);
		for (AppEventHandler subscriber : appEventSubscribers) {
			subscriber.handleEvent(event);
		}
	}
	
	private static List<AppEventHandler> getHandlerListForEventType(AppEventType appEventType) {
		List<AppEventHandler> list = handlerMap.get(appEventType);
		if (list == null) {
			list = new ArrayList<AppEventHandler>();
			handlerMap.put(appEventType, list);
		}
		return list;
	}
}