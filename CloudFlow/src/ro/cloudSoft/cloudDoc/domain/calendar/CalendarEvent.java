package ro.cloudSoft.cloudDoc.domain.calendar;

import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import ro.cloudSoft.cloudDoc.domain.nomenclator.NomenclatorValue;
import ro.cloudSoft.cloudDoc.utils.hibernate.EntityUtils;
import ro.cloudSoft.cloudDoc.utils.hibernate.HibernateProxyUtils;

@Entity
@Table(name = "CALENDAR_EVENT")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "type", discriminatorType = DiscriminatorType.STRING)
public abstract class CalendarEvent {
	
	private Long id;
	private String subject;
	private String description;
	private Calendar calendar;
	private boolean allDay;
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	
	@Column( name = "subject" )
	public String getSubject() {
		return subject;
	}
	
	public void setSubject(String subject) {
		this.subject = subject;
	}
	
	@Column( name = "description" )
	public String getDescription() {
		return description;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "calendar_id")
	public Calendar getCalendar() {
		return calendar;
	}

	public void setCalendar(Calendar calendar) {
		this.calendar = calendar;
	}

	@Column( name ="all_day" )
	public boolean isAllDay() {
		return allDay;
	}

	public void setAllDay(boolean allDay) {
		this.allDay = allDay;
	}

	public static <C extends CalendarEvent> C cast(CalendarEvent calendarEvent, Class<C> calendarEventClass) {
		if (calendarEvent == null) {
		  return null;
		}
		CalendarEvent realcalendarEvent = HibernateProxyUtils.unproxyIfNeeded(calendarEvent);
		return calendarEventClass.cast(realcalendarEvent);
	}

	public static boolean isInstanceOf(CalendarEvent calendarEvent, Class<? extends CalendarEvent> calendarEventClass) {
		if (calendarEvent == null) {
		  return false;
		}
		return calendarEventClass.isAssignableFrom(EntityUtils.getRealClass(calendarEvent));
	}
}
