package ro.cloudSoft.cloudDoc.domain.calendar;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

@Entity
@Table(
	name = "CALENDAR",
	uniqueConstraints = @UniqueConstraint(columnNames= { "name" })
)
public class Calendar {
	
	private Long id;
	private String name;
	private String description;
	private String color;
	private boolean permitAll;
	private Set<CalendarUserRights> usersRights;
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	
	@Column(name = "name")
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	@Column(name = "description")
	public String getDescription() {
		return description;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}
	
	@Column(name = "color")
	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}
	
	@Column(name = "PERMIT_ALL")
	public boolean isPermitAll() {
		return permitAll;
	}
	
	public void setPermitAll(boolean permitAll) {
		this.permitAll = permitAll;
	}
	
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "calendar")
	public Set<CalendarUserRights> getUsersRights() {
		return usersRights;
	}

	public void setUsersRights(Set<CalendarUserRights> usersRights) {
		this.usersRights = usersRights;
	}
}
