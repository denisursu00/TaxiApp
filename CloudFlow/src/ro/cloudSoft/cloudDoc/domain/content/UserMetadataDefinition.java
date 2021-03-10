package ro.cloudSoft.cloudDoc.domain.content;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import ro.cloudSoft.cloudDoc.domain.organization.Group;

@Entity
@DiscriminatorValue("USER")
public class UserMetadataDefinition extends MetadataDefinition {
	
	private boolean onlyUsersFromGroup;
	private Group groupOfPermittedUsers;
	
	
	private boolean autoCompleteWithCurrentUser;
	private String autoCompleteWithCurrentUserStateCode;
	
	
	@Column(name = "only_users_from_group", nullable = true)
	public boolean isOnlyUsersFromGroup() {
		return onlyUsersFromGroup;
	}

	@ManyToOne(fetch = FetchType.LAZY, optional = true)
	@JoinColumn(name = "id_of_group_of_permitted_users", referencedColumnName = "org_entity_id", nullable = true)
	public Group getGroupOfPermittedUsers() {
		return groupOfPermittedUsers;
	}
	
	@Column(name = "auto_compl_with_current_user", nullable = true)
	public boolean isAutoCompleteWithCurrentUser() {
		return autoCompleteWithCurrentUser;
	}
	
	@Column(name = "auto_cpl_w_crt_usr_state_code", nullable = true)
	public String getAutoCompleteWithCurrentUserStateCode() {
		return autoCompleteWithCurrentUserStateCode;
	}
	
	public void setOnlyUsersFromGroup(boolean onlyUsersFromGroup) {
		this.onlyUsersFromGroup = onlyUsersFromGroup;
	}
	public void setGroupOfPermittedUsers(Group groupOfPermittedUsers) {
		this.groupOfPermittedUsers = groupOfPermittedUsers;
	}
	public void setAutoCompleteWithCurrentUser(boolean autoCompleteWithCurrentUser) {
		this.autoCompleteWithCurrentUser = autoCompleteWithCurrentUser;
	}
	public void setAutoCompleteWithCurrentUserStateCode(String autoCompleteWithCurrentUserStateCode) {
		this.autoCompleteWithCurrentUserStateCode = autoCompleteWithCurrentUserStateCode;
	}
}