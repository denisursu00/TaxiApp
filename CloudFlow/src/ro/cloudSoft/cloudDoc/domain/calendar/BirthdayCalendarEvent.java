package ro.cloudSoft.cloudDoc.domain.calendar;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@Entity
@DiscriminatorValue("BIRTHDAY")
public class BirthdayCalendarEvent extends CalendarEvent {

	private CalendarRepeatEventEnum repeat;
	private Date birthdate;
	
	@Column(name = "repeat")
	@Enumerated(EnumType.STRING)
	public CalendarRepeatEventEnum getRepeat() {
		return repeat;
	}

	public void setRepeat(CalendarRepeatEventEnum repeat) {
		this.repeat = repeat;
	}
	
	@Column(name = "birthdate")
	public Date getBirthdate() {
		return birthdate;
	}

	public void setBirthdate(Date birthdate) {
		this.birthdate = birthdate;
	}
}
