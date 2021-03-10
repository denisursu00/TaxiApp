package ro.cloudSoft.cloudDoc.domain.calendar;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
public abstract class IntervalCalendarEvent extends CalendarEvent {

	private Date startDate;
	private Date endDate;

	@Column( name = "start_date" )
	@Temporal( TemporalType.TIMESTAMP )
	public Date getStartDate() {
		return startDate;
	}
	
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
	
	@Column( name = "end_date" )
	@Temporal( TemporalType.TIMESTAMP )
	public Date getEndDate() {
		return endDate;
	}
	
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
}
