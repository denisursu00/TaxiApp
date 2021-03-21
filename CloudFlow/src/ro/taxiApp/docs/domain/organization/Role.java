package ro.taxiApp.docs.domain.organization;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

@Entity
@Table(
	name = "ROLE",
	uniqueConstraints = @UniqueConstraint(columnNames = { "name" })
)
public class Role {
	
	private Long id;
	private String name;
	private String description;
	private List<Permission> permissions;
	
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
	
	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(
		name = "role_permission",
		joinColumns = @JoinColumn(name = "role_id", nullable = false, referencedColumnName = "id"),
		inverseJoinColumns = @JoinColumn(name = "permission_id", nullable = false, referencedColumnName = "id")
	)
	public List<Permission> getPermissions() {
		return permissions;
	}
	public void setPermissions(List<Permission> permissions) {
		this.permissions = permissions;
	}
}
