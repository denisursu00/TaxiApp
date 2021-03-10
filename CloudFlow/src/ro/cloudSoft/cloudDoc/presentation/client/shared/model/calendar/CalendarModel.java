package ro.cloudSoft.cloudDoc.presentation.client.shared.model.calendar;

import java.util.List;

public class CalendarModel {
	
	private Long id;
	private String name;
	private String description;
	private String color;
	private boolean permitAll;
	private List<CalendarUserRightsModel> usersRights;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getColor() {
		return color;
	}
	public void setColor(String color) {
		this.color = color;
	}
	public boolean isPermitAll() {
		return permitAll;
	}
	public void setPermitAll(boolean permitAll) {
		this.permitAll = permitAll;
	}
	public List<CalendarUserRightsModel> getUsersRights() {
		return usersRights;
	}
	public void setUsersRights(List<CalendarUserRightsModel> usersRights) {
		this.usersRights = usersRights;
	}
}
