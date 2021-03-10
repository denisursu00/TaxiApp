package ro.cloudSoft.cloudDoc.domain.calendar;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import ro.cloudSoft.cloudDoc.domain.organization.OrganizationEntity;

@Entity
@Table(name = "CALENDAR_USER_RIGHTS")
public class CalendarUserRights {
	
	private Long id;
	private Calendar calendar;
	private OrganizationEntity user;
	private boolean view;
	private boolean add;
	private boolean edit;
	private boolean delete;
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	
	@ManyToOne
	@JoinColumn(name = "calendar_id", nullable = false)
	public Calendar getCalendar() {
		return calendar;
	}

	public void setCalendar(Calendar calendar) {
		this.calendar = calendar;
	}

	@ManyToOne
	@JoinColumn(name = "user_right_org_entity_id", nullable = false)
	public OrganizationEntity getUser() {
		return user;
	}
	
	public void setUser(OrganizationEntity user) {
		this.user = user;
	}
	
	@Column(name = "view_right")
	public boolean isView() {
		return view;
	}
	
	public void setView(boolean view) {
		this.view = view;
	}
	
	@Column(name = "add_right")
	public boolean isAdd() {
		return add;
	}
	
	public void setAdd(boolean add) {
		this.add = add;
	}
	
	@Column(name = "edit_right")
	public boolean isEdit() {
		return edit;
	}
	
	public void setEdit(boolean edit) {
		this.edit = edit;
	}
	
	@Column(name = "delete_right")
	public boolean isDelete() {
		return delete;
	}
	
	public void setDelete(boolean delete) {
		this.delete = delete;
	}
}
