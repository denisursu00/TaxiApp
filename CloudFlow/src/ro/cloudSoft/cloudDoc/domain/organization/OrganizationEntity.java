package ro.cloudSoft.cloudDoc.domain.organization;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Transient;

import ro.cloudSoft.common.utils.hibernate.HibernateEntityUtils;


@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class OrganizationEntity 
{
    
    private Long id;
    
	@Id 
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "ORG_ENTITY_ID")
	public Long getId()
	{
		return id;
	}

	public void setId(Long id)
	{
		this.id = id;
	}
	
	@Transient
	public abstract String getDisplayName();

	@Transient
	public boolean isUser() {
		return HibernateEntityUtils.isOfType(this, User.class);
	}

	@Transient
	public boolean isOrganizationUnit() {
		return HibernateEntityUtils.isOfType(this, OrganizationUnit.class);
	}

	@Transient
	public boolean isGroup() {
		return HibernateEntityUtils.isOfType(this, Group.class);
	}
}