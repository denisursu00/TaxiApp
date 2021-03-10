package ro.cloudSoft.cloudDoc.presentation.client.shared.model.calendar;

public class HolidayCalendarEventModel extends IntervalCalendarEventModel {
	
	public HolidayCalendarEventModel() {
		super(CalendarEventType.HOLIDAY);
	}
	
	private Long userId;
	private String documentId;
	private String documentLocationRealName;

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getDocumentId() {
		return documentId;
	}

	public void setDocumentId(String documentId) {
		this.documentId = documentId;
	}

	public String getDocumentLocationRealName() {
		return documentLocationRealName;
	}

	public void setDocumentLocationRealName(String documentLocationRealName) {
		this.documentLocationRealName = documentLocationRealName;
	}
	
}
