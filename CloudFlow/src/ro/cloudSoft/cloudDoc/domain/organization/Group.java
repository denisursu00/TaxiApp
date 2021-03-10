package ro.cloudSoft.cloudDoc.domain.organization;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.google.common.collect.Sets;

@Entity
@Table(name = "EDOCGROUP")
public class Group extends OrganizationEntity {

	public static final int LENGTH_NAME = 128;
	public static final int LENGTH_DESCRIPTION = 1024;

	private String name;
	private String description;
	private Set<User> users = Sets.newLinkedHashSet();

	@Column(length = LENGTH_DESCRIPTION)
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(nullable = false, unique = true, length = LENGTH_NAME)
	public String getName() {
		return name;
	}
	
	@Override
	@Transient
	public String getDisplayName() {
		return name;
	}

	public void setUsers(Set<User> users) {
		this.users = users;
	}

	@ManyToMany(
		targetEntity = User.class,
		cascade = {CascadeType.MERGE},
		fetch = FetchType.LAZY
	)
	@JoinTable(
		name = "GROUP_USER",
		joinColumns = @JoinColumn(name = "GROUP_ID"),
		inverseJoinColumns = @JoinColumn(name = "USER_ID")
	)
	public Set<User> getUsers() {
		return users;
	}
}