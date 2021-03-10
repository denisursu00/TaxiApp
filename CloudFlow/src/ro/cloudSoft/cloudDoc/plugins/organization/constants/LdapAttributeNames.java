package ro.cloudSoft.cloudDoc.plugins.organization.constants;

import org.springframework.beans.factory.InitializingBean;

import ro.cloudSoft.common.utils.DependencyInjectionUtils;

/**
 * Contine numele atributelor entitatilor din LDAP.
 * 
 * 
 */
public class LdapAttributeNames implements InitializingBean {
	
	public static final String ATTRIBUTE_OBJECT_CLASS = "objectclass";

	private String organizationId;
	private String organizationName;
	private String organizationManager;
	
	private String groupId;
	private String groupName;
	private String groupDescription;
	private String groupMember;
	
	private String organizationUnitId;
	private String organizationUnitName;
	private String organizationUnitDescription;
	private String organizationUnitOrganizationName;
	private String organizationUnitParentOrganizationUnitId;
	private String organizationUnitManagerId;
	
	private String userId;
	private String userUsername;
	private String userFirstName;
	private String userLastName;
	private String userDisplayName;
	private String userPassword;
	private String userMail;
	private String userTitle;
	private String userOrganizationUnitId;
	private String userOrganizationUnitName;
	private String userGroupId;
	private String userEmployeeNumber;
	private String userManager;
	
	@Override
	public void afterPropertiesSet() throws Exception {		
		DependencyInjectionUtils.checkRequiredDependencies(
				
			organizationId,
			organizationName,
			organizationManager,

			groupId,
			groupName,
			groupDescription,
			groupMember,

			organizationUnitId,
			organizationUnitName,
			organizationUnitDescription,
			organizationUnitOrganizationName,
			organizationUnitParentOrganizationUnitId,
			organizationUnitManagerId,

			userId,
			userUsername,
			userFirstName,
			userLastName,
			userDisplayName,
			userPassword,
			userMail,
			userTitle,
			userOrganizationUnitId,
			userOrganizationUnitName,
			userGroupId,
			userEmployeeNumber,
			userManager
		);
	}
	
	public String getOrganizationId() {
		return organizationId;
	}
	public void setOrganizationId(String organizationId) {
		this.organizationId = organizationId;
	}
	public String getOrganizationName() {
		return organizationName;
	}
	public void setOrganizationName(String organizationName) {
		this.organizationName = organizationName;
	}
	public String getOrganizationManager() {
		return organizationManager;
	}
	public void setOrganizationManager(String organizationManager) {
		this.organizationManager = organizationManager;
	}
	public String getGroupId() {
		return groupId;
	}
	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}
	public String getGroupName() {
		return groupName;
	}
	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}
	public String getGroupDescription() {
		return groupDescription;
	}
	public void setGroupDescription(String groupDescription) {
		this.groupDescription = groupDescription;
	}
	public String getGroupMember() {
		return groupMember;
	}
	public void setGroupMember(String groupMember) {
		this.groupMember = groupMember;
	}
	public String getOrganizationUnitId() {
		return organizationUnitId;
	}
	public void setOrganizationUnitId(String organizationUnitId) {
		this.organizationUnitId = organizationUnitId;
	}
	public String getOrganizationUnitName() {
		return organizationUnitName;
	}
	public void setOrganizationUnitName(String organizationUnitName) {
		this.organizationUnitName = organizationUnitName;
	}
	public String getOrganizationUnitDescription() {
		return organizationUnitDescription;
	}
	public void setOrganizationUnitDescription(String organizationUnitDescription) {
		this.organizationUnitDescription = organizationUnitDescription;
	}
	public String getOrganizationUnitOrganizationName() {
		return organizationUnitOrganizationName;
	}
	public void setOrganizationUnitOrganizationName(String organizationUnitOrganizationName) {
		this.organizationUnitOrganizationName = organizationUnitOrganizationName;
	}
	public String getOrganizationUnitParentOrganizationUnitId() {
		return organizationUnitParentOrganizationUnitId;
	}
	public void setOrganizationUnitParentOrganizationUnitId(String organizationUnitParentOrganizationUnitId) {
		this.organizationUnitParentOrganizationUnitId = organizationUnitParentOrganizationUnitId;
	}
	public String getOrganizationUnitManagerId() {
		return organizationUnitManagerId;
	}
	public void setOrganizationUnitManagerId(String organizationUnitManagerId) {
		this.organizationUnitManagerId = organizationUnitManagerId;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getUserUsername() {
		return userUsername;
	}
	public void setUserUsername(String userUsername) {
		this.userUsername = userUsername;
	}
	public String getUserFirstName() {
		return userFirstName;
	}
	public void setUserFirstName(String userFirstName) {
		this.userFirstName = userFirstName;
	}
	public String getUserLastName() {
		return userLastName;
	}
	public void setUserLastName(String userLastName) {
		this.userLastName = userLastName;
	}
	public String getUserDisplayName() {
		return userDisplayName;
	}
	public void setUserDisplayName(String userDisplayName) {
		this.userDisplayName = userDisplayName;
	}
	public String getUserPassword() {
		return userPassword;
	}
	public void setUserPassword(String userPassword) {
		this.userPassword = userPassword;
	}
	public String getUserMail() {
		return userMail;
	}
	public void setUserMail(String userMail) {
		this.userMail = userMail;
	}
	public String getUserTitle() {
		return userTitle;
	}
	public void setUserTitle(String userTitle) {
		this.userTitle = userTitle;
	}
	public String getUserOrganizationUnitId() {
		return userOrganizationUnitId;
	}
	public void setUserOrganizationUnitId(String userOrganizationUnitId) {
		this.userOrganizationUnitId = userOrganizationUnitId;
	}
	public String getUserOrganizationUnitName() {
		return userOrganizationUnitName;
	}
	public void setUserOrganizationUnitName(String userOrganizationUnitName) {
		this.userOrganizationUnitName = userOrganizationUnitName;
	}
	public String getUserGroupId() {
		return userGroupId;
	}
	public void setUserGroupId(String userGroupId) {
		this.userGroupId = userGroupId;
	}
	public String getUserEmployeeNumber() {
		return userEmployeeNumber;
	}
	public void setUserEmployeeNumber(String userEmployeeNumber) {
		this.userEmployeeNumber = userEmployeeNumber;
	}
	public String getUserManager() {
		return userManager;
	}
	public void setUserManager(String userManager) {
		this.userManager = userManager;
	}
}