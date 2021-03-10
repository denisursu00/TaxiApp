package ro.cloudSoft.cloudDoc.domain.organization;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;

import org.apache.commons.lang.StringUtils;

import com.google.common.base.Objects;

@Entity
@Table(
	name = "EDOCUSER",
	uniqueConstraints = @UniqueConstraint(columnNames = { "username", "title" })
)
public class User extends OrganizationEntity {
	
	private String firstName;
	private String lastName;
	private String password;
	private String title;
	private String customTitleTemplate;
	private String username;
	private String email;
	private String phone;
	private String fax;
	private String mobile;
	private String employeeNumber;
	private OrganizationUnit ou;
	private Organization organization;
	private UserTypeEnum type;
	private Set<Group> groups;
	private Set<Role> roles;
	
	@Transient
	public String getName() {
		return firstName + " " + lastName;
	}

	@Column(name = "title", nullable = false)
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}
	
	@Column(name = "custom_title_template")
	public String getCustomTitleTemplate() {
		return customTitleTemplate;
	}
	
	public void setCustomTitleTemplate(String customTitleTemplate) {
		this.customTitleTemplate = customTitleTemplate;
	}

	@Override
	@Transient
	public String getDisplayName() {
		return firstName + " " + lastName;
	}

	@Transient
	public String getDisplayNameWithTitle() {
		String displayName = getDisplayName();
		String title = getTitle();
		if (StringUtils.isNotBlank(title)) {
			return (displayName + " - " + title);
		} else {
			return displayName;
		}
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@Column(name = "username", nullable = false)
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
	
	@Column(name = "phone")
	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	@Column(name = "fax")
	public String getFax() {
		return fax;
	}

	public void setFax(String fax) {
		this.fax = fax;
	}
	
	@Column(name = "mobile")
	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getEmployeeNumber() {
		return employeeNumber;
	}

	public void setEmployeeNumber(String employeeNumber) {
		this.employeeNumber = employeeNumber;
	}

	public void setOu(OrganizationUnit ou) {
		this.ou = ou;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	public OrganizationUnit getOu() {
		return ou;
	}

	public void setGroups(Set<Group> groups) {
		this.groups = groups;
	}

	@ManyToMany(mappedBy = "users", targetEntity = Group.class)
	public Set<Group> getGroups() {
		return groups;
	}

	public void setOrganization(Organization organization) {
		this.organization = organization;
	}

	@ManyToOne(targetEntity = Organization.class)
	@JoinColumn(name = "organization_id", nullable = true)
	public Organization getOrganization() {
		return organization;
	}
	
	public void setType(UserTypeEnum type) {
		this.type = type;
	}
	
	@Column(name = "type")
	@Enumerated(EnumType.STRING)
	public UserTypeEnum getType() {
		return type;
	}

	@ManyToMany(targetEntity = Role.class, cascade = {CascadeType.MERGE}, fetch = FetchType.LAZY)
	@JoinTable(
		name = "USER_ROLE", 
		joinColumns = @JoinColumn(name = "USER_ID"),
		inverseJoinColumns = @JoinColumn(name = "ROLE_ID")
	)
	public Set<Role> getRoles() {
		return roles;
	}
	
	public void setRoles(Set<Role> roles) {
		this.roles = roles;
	}
	
	@Override
	public boolean equals(Object obj) {
		
		if (!(obj instanceof User)) {
			return false;
		}
		
		User other = (User) obj;
		
		String usernameInLowercase = StringUtils.lowerCase(getUsername());
		String otherUsernameInLowercase = StringUtils.lowerCase(other.getUsername());
		
		if (getId() != null) {
			return (
				Objects.equal(getId(), other.getId())
			);
		} else {
			return (
				Objects.equal(usernameInLowercase, otherUsernameInLowercase) &&
				Objects.equal(getTitle(), other.getTitle())
			);
		}
	}

	@Override
	public int hashCode() {
		if (getId() != null) {
			return Objects.hashCode(getId());
		}
		String usernameInLowercase = StringUtils.lowerCase(getUsername());
		return Objects.hashCode(usernameInLowercase, getTitle());
	}
	
	public static enum UserTypeEnum {
		PERSON,
		SIMPLE_USER,
		HIDDEN
	}
}