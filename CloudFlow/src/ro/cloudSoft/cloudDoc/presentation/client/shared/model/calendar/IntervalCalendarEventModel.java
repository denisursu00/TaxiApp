package ro.cloudSoft.cloudDoc.presentation.client.shared.model.calendar;

import java.util.Date;

public class IntervalCalendarEventModel extends CalendarEventModel {
	
	public IntervalCalendarEventModel(CalendarEventType type) {
		super(type);
	}
	
	private Date startDate;
	private Date endDate;

	public Date getStartDate() {
		return startDate;
	}
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
	public Date getEndDate() {
		return endDate;
	}
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
}
