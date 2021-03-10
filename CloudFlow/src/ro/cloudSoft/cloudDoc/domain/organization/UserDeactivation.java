package ro.cloudSoft.cloudDoc.domain.organization;

import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.google.common.collect.Sets;

@Entity
@Table(name = "user_deactivation")
public class UserDeactivation {

	private Long id;
	
	private User user;
	private String userOldTitle;
	
	private Organization parentOrganization;
	private OrganizationUnit parentOrganizationUnit;
	
	private Set<Group> groups = Sets.newLinkedHashSet();

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id", nullable = false)
	public Long getId() {
		return id;
	}

	@OneToOne(fetch = FetchType.EAGER, optional = false)
	@JoinColumn(name = "user_id", referencedColumnName = "org_entity_id", nullable = false, unique = true)
	public User getUser() {
		return user;
	}

	@Column(name = "user_old_title", nullable = false)
	public String getUserOldTitle() {
		return userOldTitle;
	}

	@ManyToOne(fetch = FetchType.EAGER, optional = true)
	@JoinColumn(name = "parent_organization_id", referencedColumnName = "id", nullable = true)
	public Organization getParentOrganization() {
		return parentOrganization;
	}

	@ManyToOne(fetch = FetchType.EAGER, optional = true)
	@JoinColumn(name = "parent_organization_unit_id", referencedColumnName = "org_entity_id", nullable = true)
	public OrganizationUnit getParentOrganizationUnit() {
		return parentOrganizationUnit;
	}

	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(
		name = "groups_4_user_deactivation",
		joinColumns = @JoinColumn(name = "user_deactivation_id", referencedColumnName = "id", nullable = false),
		inverseJoinColumns = @JoinColumn(name = "group_id", referencedColumnName = "org_entity_id", nullable = false)
	)
	public Set<Group> getGroups() {
		return groups;
	}

	public void setId(Long id) {
		this.id = id;
	}
	public void setUser(User user) {
		this.user = user;
	}
	public void setUserOldTitle(String userOldTitle) {
		this.userOldTitle = userOldTitle;
	}
	public void setParentOrganization(Organization parentOrganization) {
		this.parentOrganization = parentOrganization;
	}
	public void setParentOrganizationUnit(OrganizationUnit parentOrganizationUnit) {
		this.parentOrganizationUnit = parentOrganizationUnit;
	}
	public void setGroups(Set<Group> groups) {
		this.groups = groups;
	}
}