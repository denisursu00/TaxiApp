package ro.cloudSoft.cloudDoc.domain.organization;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

@Entity
@Table(
	name = "PERMISSION",
	uniqueConstraints = @UniqueConstraint(columnNames = { "name" })
)
public class Permission {
	
	private Long id;
	private String name;
	private String label;
	private String description;
	private PermissionGroup permissionGroup;
	private Integer uiOrder;
	
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
	
	@Column(name = "label")	
	public String getLabel() {
		return label;
	}
	public void setLabel(String label) {
		this.label = label;
	}
	
	@Column(name = "description")	
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "permission_group_id", referencedColumnName = "id", nullable = true)
	public PermissionGroup getPermissionGroup() {
		return permissionGroup;
	}	
	public void setPermissionGroup(PermissionGroup permissionGroup) {
		this.permissionGroup = permissionGroup;
	}
	
	@Column(name = "ui_order")	
	public Integer getUiOrder() {
		return uiOrder;
	}
	public void setUiOrder(Integer uiOrder) {
		this.uiOrder = uiOrder;
	}
}
