package ro.cloudSoft.cloudDoc.presentation.client.shared.event;

public class AppEvent {
	
	private AppEventType type;
	
	public AppEvent(AppEventType type) {
		this.type = type;
	}
	
	public AppEventType getType() {
		return type;
	}
}