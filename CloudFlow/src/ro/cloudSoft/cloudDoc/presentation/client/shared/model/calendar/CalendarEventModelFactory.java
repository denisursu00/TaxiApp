package ro.cloudSoft.cloudDoc.presentation.client.shared.model.calendar;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

import com.sun.star.uno.RuntimeException;

import ro.cloudSoft.cloudDoc.core.constants.NomenclatorConstants;
import ro.cloudSoft.cloudDoc.domain.calendar.AuditCalendarEvent;
import ro.cloudSoft.cloudDoc.domain.calendar.BirthdayCalendarEvent;
import ro.cloudSoft.cloudDoc.domain.calendar.CalendarEvent;
import ro.cloudSoft.cloudDoc.domain.calendar.HolidayCalendarEvent;
import ro.cloudSoft.cloudDoc.domain.calendar.MeetingCalendarEvent;
import ro.cloudSoft.cloudDoc.domain.nomenclator.NomenclatorValue;
import ro.cloudSoft.cloudDoc.domain.organization.OrganizationEntity;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.OrganizationEntityModel;
import ro.cloudSoft.cloudDoc.presentation.server.converters.organization.OrganizationEntityConverter;
import ro.cloudSoft.cloudDoc.utils.nomenclator.NomenclatorValueUtils;
import ro.cloudSoft.common.utils.DateUtils;

public class CalendarEventModelFactory {
	
	public CalendarEventModel createFromJsonNode(ObjectMapper mapper, JsonNode jsonNode) throws JsonParseException, JsonMappingException, IOException {
		
		String type = (String)(jsonNode.get("type").asText());
		
		if (type.equals(CalendarEventType.BIRTHDAY.getCode())) {
			return mapper.readValue(jsonNode, BirthdayCalendarEventModel.class);
		} else if (type.equals(CalendarEventType.HOLIDAY.getCode())) {
			return mapper.readValue(jsonNode, HolidayCalendarEventModel.class);
		} else if (type.equals(CalendarEventType.MEETING.getCode())) {
			return mapper.readValue(jsonNode, MeetingCalendarEventModel.class);
		} else if (type.equals(CalendarEventType.AUDIT.getCode())) {
			return mapper.readValue(jsonNode, AuditCalendarEventModel.class);
		}
		throw new RuntimeException("Calendar event type [" + type + "] not implemented.");
	}
	
	public CalendarEventModel createFromEntity(CalendarEvent entity) {
		
		if (CalendarEvent.isInstanceOf(entity, BirthdayCalendarEvent.class)) {
			BirthdayCalendarEventModel model = new BirthdayCalendarEventModel();
			model.setId(entity.getId());
			model.setSubject(entity.getSubject());
			model.setDescription(entity.getDescription());
//			model.setBirthdate(DateUtils.setCurrentYear(((BirthdayCalendarEvent)entity).getBirthdate()));
			model.setBirthdate(((BirthdayCalendarEvent)entity).getBirthdate());
			model.setAllDay(entity.isAllDay());
			model.setCalendarId(entity.getCalendar().getId());
			model.setRepeat(((BirthdayCalendarEvent)entity).getRepeat());
			model.setColor(entity.getCalendar().getColor());
			model.setType(CalendarEventType.BIRTHDAY);
			return model;
		} else if (CalendarEvent.isInstanceOf(entity, MeetingCalendarEvent.class)) {
			MeetingCalendarEventModel model = new MeetingCalendarEventModel();
			model.setId(entity.getId());
			model.setSubject(entity.getSubject());
			model.setDescription(entity.getDescription());
			model.setStartDate(((MeetingCalendarEvent)entity).getStartDate());
			model.setEndDate(((MeetingCalendarEvent)entity).getEndDate());
			model.setLocation(((MeetingCalendarEvent)entity).getLocation());
			model.setAllDay(entity.isAllDay());
			model.setReminderMinutes(((MeetingCalendarEvent)entity).getReminderMinutes());
			model.setRepeat(((MeetingCalendarEvent)entity).getRepeat());
			model.setCalendarId(entity.getCalendar().getId());
			NomenclatorValue reprezentantExtern = ((MeetingCalendarEvent)entity).getReprezentantExtern();
			if (reprezentantExtern != null) {
				String numeReprezentant = NomenclatorValueUtils.getAttributeValueAsString(reprezentantExtern, NomenclatorConstants.PERSOANE_ATTRIBUTE_KEY_NUME);
				String prenumeReprezentant = NomenclatorValueUtils.getAttributeValueAsString(reprezentantExtern, NomenclatorConstants.PERSOANE_ATTRIBUTE_KEY_PRENUME);
				model.setReprezentantExtern(numeReprezentant + " " + prenumeReprezentant);
			}
			model.setDocumentId(((MeetingCalendarEvent)entity).getDocumentId());
			model.setDocumentLocationRealName(((MeetingCalendarEvent)entity).getDocumentLocationRealName());
			List<OrganizationEntityModel> attendees = new ArrayList<OrganizationEntityModel>();
			for (OrganizationEntity attendee : ((MeetingCalendarEvent)entity).getAttendees()) {
				attendees.add(OrganizationEntityConverter.getModelFromOrganizationEntity(attendee));
			}
			model.setAttendees(attendees);
			model.setColor(entity.getCalendar().getColor());
			model.setType(CalendarEventType.MEETING);
			return model;
		} else if (CalendarEvent.isInstanceOf(entity, AuditCalendarEvent.class)) {
			AuditCalendarEventModel model = new AuditCalendarEventModel();
			model.setId(entity.getId());
			model.setSubject(entity.getSubject());
			model.setDescription(entity.getDescription());
			model.setStartDate(((AuditCalendarEvent)entity).getStartDate());
			model.setEndDate(((AuditCalendarEvent)entity).getEndDate());
			model.setLocation(((AuditCalendarEvent)entity).getLocation());
			model.setAllDay(entity.isAllDay());
			model.setCalendarId(entity.getCalendar().getId());
			model.setDocumentId(((AuditCalendarEvent)entity).getDocumentId());
			model.setDocumentLocationRealName(((AuditCalendarEvent)entity).getDocumentLocationRealName());
			
			List<OrganizationEntityModel> attendees = new ArrayList<OrganizationEntityModel>();
			for (OrganizationEntity attendee : ((AuditCalendarEvent)entity).getAttendees()) {
				attendees.add(OrganizationEntityConverter.getModelFromOrganizationEntity(attendee));
			}
			model.setAttendees(attendees);
			model.setColor(entity.getCalendar().getColor());
			model.setType(CalendarEventType.AUDIT);
			return model;
		} else if (CalendarEvent.isInstanceOf(entity, HolidayCalendarEvent.class)) {
			HolidayCalendarEventModel model = new HolidayCalendarEventModel();
			model.setId(entity.getId());
			model.setSubject(entity.getSubject());
			model.setDescription(entity.getDescription());
			model.setStartDate(((HolidayCalendarEvent)entity).getStartDate());
			model.setEndDate(((HolidayCalendarEvent)entity).getEndDate());
			model.setCalendarId(entity.getCalendar().getId());
			model.setUserId(((HolidayCalendarEvent)entity).getUser().getId());
			model.setAllDay(entity.isAllDay());
			model.setColor(entity.getCalendar().getColor());
			model.setType(CalendarEventType.HOLIDAY);
			model.setDocumentId(((HolidayCalendarEvent)entity).getDocumentId());
			model.setDocumentLocationRealName(((HolidayCalendarEvent)entity).getDocumentLocationRealName());
			
			return model;
		}
		
		throw new RuntimeException("CalendarEvent instance is not an BirthdayCalendarEvent instance or MeetingCalendarEvent instance "
				+ "or HolidayCalendarEvent instance.");
	}
}