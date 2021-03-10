package ro.cloudSoft.cloudDoc.presentation.client.shared.model.calendar;

public enum CalendarEventType {
	
	BIRTHDAY("BIRTHDAY"),
	MEETING("MEETING"),
	AUDIT("AUDIT"),
	HOLIDAY("HOLIDAY");
	
	private final String code;
	
	private CalendarEventType(final String code) {
		this.code = code;
	}
	
	public String getCode() {
		return code;
	}
}
