package ro.cloudSoft.cloudDoc.domain.organization;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
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
	name = "USERS"
)
public class User {
	
	private Long id;
	private String firstName;
	private String lastName;
	private String password;
	private String username;
	private String email;
	private String mobile;
	private String employeeNumber;
	private Set<Role> roles;
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Transient
	public String getName() {
		return firstName + " " + lastName;
	}

	@Transient
	public String getDisplayName() {
		return firstName + " " + lastName;
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
				Objects.equal(usernameInLowercase, otherUsernameInLowercase)
			);
		}
	}

	@Override
	public int hashCode() {
		if (getId() != null) {
			return Objects.hashCode(getId());
		}
		String usernameInLowercase = StringUtils.lowerCase(getUsername());
		return Objects.hashCode(usernameInLowercase);
	}
	
	public static enum UserTypeEnum {
		PERSON,
		SIMPLE_USER,
		HIDDEN
	}
}