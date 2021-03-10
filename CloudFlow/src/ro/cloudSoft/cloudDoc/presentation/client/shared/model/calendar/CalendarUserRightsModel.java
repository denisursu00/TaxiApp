package ro.cloudSoft.cloudDoc.presentation.client.shared.model.calendar;

import ro.cloudSoft.cloudDoc.presentation.client.shared.model.OrganizationEntityModel;

public class CalendarUserRightsModel {

	private Long id;
	private Long calendarId;
	private OrganizationEntityModel user;
	private boolean view;
	private boolean add;
	private boolean edit;
	private boolean delete;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getCalendarId() {
		return calendarId;
	}
	public void setCalendarId(Long calendarId) {
		this.calendarId = calendarId;
	}
	public OrganizationEntityModel getUser() {
		return user;
	}
	public void setUser(OrganizationEntityModel user) {
		this.user = user;
	}
	public boolean isView() {
		return view;
	}
	public void setView(boolean view) {
		this.view = view;
	}
	public boolean isAdd() {
		return add;
	}
	public void setAdd(boolean add) {
		this.add = add;
	}
	public boolean isEdit() {
		return edit;
	}
	public void setEdit(boolean edit) {
		this.edit = edit;
	}
	public boolean isDelete() {
		return delete;
	}
	public void setDelete(boolean delete) {
		this.delete = delete;
	}
}
