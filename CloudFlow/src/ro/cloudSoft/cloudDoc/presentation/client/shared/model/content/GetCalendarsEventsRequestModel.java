package ro.cloudSoft.cloudDoc.presentation.client.shared.model.content;

import java.util.Date;
import java.util.List;

public class GetCalendarsEventsRequestModel {
	
	private List<Long> calendarIds;
	private Date startDate;
	private Date endDate;
	
	public List<Long> getCalendarIds() {
		return calendarIds;
	}
	public void setCalendarIds(List<Long> calendarIds) {
		this.calendarIds = calendarIds;
	}
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
