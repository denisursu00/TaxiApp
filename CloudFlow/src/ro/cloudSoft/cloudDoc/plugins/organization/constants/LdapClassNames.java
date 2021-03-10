package ro.cloudSoft.cloudDoc.plugins.organization.constants;

import org.springframework.beans.factory.InitializingBean;

import ro.cloudSoft.common.utils.DependencyInjectionUtils;

/**
 * Contine numele claselor entitatilor din LDAP.
 * 
 * 
 */
public class LdapClassNames implements InitializingBean {

	private String organizationClass;
	private String groupClass;
	private String organizationUnitClass;
	private String userClass;
	
	@Override
	public void afterPropertiesSet() throws Exception {		
		DependencyInjectionUtils.checkRequiredDependencies(
			organizationClass,
			groupClass,
			organizationUnitClass,
			userClass
		);
	}
	
	public String getOrganizationClass() {
		return organizationClass;
	}
	public void setOrganizationClass(String organizationClass) {
		this.organizationClass = organizationClass;
	}
	public String getGroupClass() {
		return groupClass;
	}
	public void setGroupClass(String groupClass) {
		this.groupClass = groupClass;
	}
	public String getOrganizationUnitClass() {
		return organizationUnitClass;
	}
	public void setOrganizationUnitClass(String organizationUnitClass) {
		this.organizationUnitClass = organizationUnitClass;
	}
	public String getUserClass() {
		return userClass;
	}
	public void setUserClass(String userClass) {
		this.userClass = userClass;
	}
}