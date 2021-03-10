package ro.cloudSoft.cloudDoc.domain.organization;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

@Entity
@Table(uniqueConstraints = { @UniqueConstraint(columnNames = { "name" }) })
public class Organization {

	private Long id;

	private String name;

	private Set<OrganizationUnit> organizationUnits;

	private Set<User> organizationUsers;

	private User organizationManager;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public Long getId() {
		return id;
	}

	public void setId(Long rootId) {
		this.id = rootId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setOrganizationUnits(Set<OrganizationUnit> organizationUnits) {
		this.organizationUnits = organizationUnits;
	}

	@OneToMany(mappedBy = "organization", cascade = { CascadeType.ALL }, fetch = FetchType.LAZY)
	public Set<OrganizationUnit> getOrganizationUnits() {
		return organizationUnits;
	}

	public void setOrganizationUsers(Set<User> organizationUsers) {
		this.organizationUsers = organizationUsers;
	}

	@OneToMany(mappedBy = "organization", cascade = { CascadeType.ALL }, fetch = FetchType.LAZY)
	public Set<User> getOrganizationUsers() {
		return organizationUsers;
	}

	public void setOrganizationManager(User organizationManager) {
		this.organizationManager = organizationManager;
	}

	@OneToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "manager_id")
	public User getOrganizationManager() {
		return organizationManager;
	}
}