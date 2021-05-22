package ro.taxiApp.docs.domain.organization;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

@Entity
@Table(
	name = "PERMISSION_GROUP",
	uniqueConstraints = @UniqueConstraint(columnNames = { "name" })
)
public class PermissionGroup {
	
	private Long id;
	private String name;
	private String label;
	private String description;
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public Long getId() {
		return id;
	}	
	public void setId(Long id) {
		this.id = id;
	}
	
	@Column(name = "label")	
	public String getLabel() {
		return label;
	}	
	public void setLabel(String label) {
		this.label = label;
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
	
}
