package ro.cloudSoft.cloudDoc.presentation.server.converters.calendar;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import ro.cloudSoft.cloudDoc.dao.calendar.CalendarDao;
import ro.cloudSoft.cloudDoc.domain.calendar.Calendar;
import ro.cloudSoft.cloudDoc.domain.calendar.CalendarUserRights;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.calendar.CalendarModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.calendar.CalendarUserRightsModel;
import ro.cloudSoft.cloudDoc.presentation.server.converters.organization.OrganizationEntityConverter;
import ro.cloudSoft.cloudDoc.services.calendar.CalendarUserRightsDao;
import ro.cloudSoft.cloudDoc.services.organization.UserService;

public class CalendarConverter {
	
	private UserService userService;
	private CalendarDao calendarDao;
	private CalendarUserRightsDao calendarUserRightsDao;
	
	public CalendarModel toModel(Calendar entity) {

		CalendarModel model = new CalendarModel();
		model.setId(entity.getId());
		model.setName(entity.getName());
		model.setDescription(entity.getDescription());
		model.setColor(entity.getColor());
		model.setPermitAll(entity.isPermitAll());
		
		List<CalendarUserRightsModel> userRights = new ArrayList<CalendarUserRightsModel>();
		for (CalendarUserRights userRightsEntitiy : entity.getUsersRights()) {
			CalendarUserRightsModel userRightsModel = new CalendarUserRightsModel();
			userRightsModel.setId(userRightsEntitiy.getId());
			userRightsModel.setCalendarId(userRightsEntitiy.getCalendar().getId());
			userRightsModel.setView(userRightsEntitiy.isView());
			userRightsModel.setAdd(userRightsEntitiy.isAdd());
			userRightsModel.setEdit(userRightsEntitiy.isEdit());
			userRightsModel.setDelete(userRightsEntitiy.isDelete());
			userRightsModel.setUser(OrganizationEntityConverter.getModelFromOrganizationEntity(userRightsEntitiy.getUser()));
			userRights.add(userRightsModel);
		}
		model.setUsersRights(userRights);
		
		return model;
	}
	
	public Calendar toEntity(CalendarModel model) {
		
		Calendar entity = null;
		
		if (model.getId() != null) {
			entity = calendarDao.find(model.getId());
		} else {
			entity = new Calendar();
		}
		
		entity.setId(model.getId());
		entity.setName(model.getName());
		entity.setDescription(model.getDescription());
		entity.setColor(model.getColor());
		entity.setPermitAll(model.isPermitAll());
		entity.setUsersRights(new HashSet<CalendarUserRights>());
		Set<CalendarUserRights> usersRights = new HashSet<CalendarUserRights>();
		for (CalendarUserRightsModel userRightsModel: model.getUsersRights()) {
			
			CalendarUserRights userRightsEntity = null;
			if (userRightsModel.getId() == null) {
				userRightsEntity = new CalendarUserRights();
			} else {
				userRightsEntity = calendarUserRightsDao.getById(userRightsModel.getId());
			}
			
			userRightsEntity.setId(userRightsModel.getId());
			userRightsEntity.setUser(userService.getUserById(userRightsModel.getUser().getId()));
			userRightsEntity.setCalendar(entity);
			userRightsEntity.setView(userRightsModel.isView());
			userRightsEntity.setAdd(userRightsModel.isAdd());
			userRightsEntity.setEdit(userRightsModel.isEdit());
			userRightsEntity.setDelete(userRightsModel.isDelete());
			usersRights.add(userRightsEntity);
		}
		entity.setUsersRights(usersRights);
		
		return entity;
	}
	
	public List<CalendarModel> toModels(List<Calendar> entities) {
		
		List<CalendarModel> models = new ArrayList<CalendarModel>();
		for (Calendar entity : entities) {
			CalendarModel model = toModel(entity);
			models.add(model);
		}
		return models;
	}

	public void setUserService(UserService userService) {
		this.userService = userService;
	}
	
	public void setCalendarDao(CalendarDao calendarDao) {
		this.calendarDao = calendarDao;
	}
	
	public void setCalendarUserRightsDao(CalendarUserRightsDao calendarUserRightsDao) {
		this.calendarUserRightsDao = calendarUserRightsDao;
	}
}
