package ro.cloudSoft.cloudDoc.domain.organization;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;

@Entity
@Table(uniqueConstraints = {@UniqueConstraint(columnNames={"name", "PARENTOU_ORG_ENTITY_ID"})})
public class OrganizationUnit extends OrganizationEntity {
	
    private Organization organization;
    private String name;    
    private String description;
    private OrganizationUnit parentOu;    
    private Set<OrganizationUnit> subOus;    
    private Set<User> users;
    private User manager;

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@ManyToOne(targetEntity=ro.cloudSoft.cloudDoc.domain.organization.OrganizationUnit.class,optional=true)
	@JoinColumn(name="PARENTOU_ORG_ENTITY_ID")
	public OrganizationUnit getParentOu() {
		return parentOu;
	}
	
	public void setParentOu(OrganizationUnit parentOu) {
		this.parentOu = parentOu;
	}

	@OneToOne(targetEntity=User.class, optional=true, fetch=FetchType.EAGER )
	public User getManager() {
		return manager;
	}

	public void setManager(User manager) {
		this.manager = manager;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getName()
	{
		return name;
	}

	@Transient
	public String getDisplayName()
	{
		return name;
	}

	/**
	 * @param subOus the subOus to set
	 */
	public void setSubOus(Set<OrganizationUnit> subOus)
	{
		this.subOus = subOus;
	}

	/**
	 * @return the subOus
	 */
	@OneToMany(cascade={CascadeType.ALL}, mappedBy="parentOu", fetch=FetchType.LAZY)
	public Set<OrganizationUnit> getSubOus()
	{
		return subOus;
	}

	/**
	 * @param users the users to set
	 */
	public void setUsers(Set<User> users)
	{
		this.users = users;
	}

	/**
	 * @return the users
	 */
	@OneToMany(mappedBy="ou", cascade={CascadeType.ALL, CascadeType.REMOVE}, fetch=FetchType.LAZY)
	public Set<User> getUsers()
	{
		return users;
	}

	/**
	 * @param organization the organization to set
	 */
	public void setOrganization(Organization organization)
	{
		this.organization = organization;
	}

	/**
	 * @return the organization
	 */
	@ManyToOne( targetEntity = Organization.class )
	@JoinColumn(name = "organization_id", nullable = true)
	public Organization getOrganization()
	{
		return organization;
	}  
}