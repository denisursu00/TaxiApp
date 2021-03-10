package ro.cloudSoft.cloudDoc.services.organization;

import org.springframework.beans.factory.InitializingBean;

import ro.cloudSoft.cloudDoc.core.AppException;
import ro.cloudSoft.cloudDoc.domain.organization.Organization;
import ro.cloudSoft.cloudDoc.domain.organization.tree.OrganizationTree;
import ro.cloudSoft.cloudDoc.domain.security.SecurityManager;
import ro.cloudSoft.cloudDoc.plugins.organization.OrganizationPersistencePlugin;
import ro.cloudSoft.common.utils.DependencyInjectionUtils;

public class OrganizationServiceImpl implements OrganizationService, InitializingBean {
	
	private OrganizationPersistencePlugin organizationPersistencePlugin;
	
	@Override
	public void afterPropertiesSet() throws Exception {		
		DependencyInjectionUtils.checkRequiredDependencies(
			organizationPersistencePlugin
		);
	}

	@Override
    public Long save(Organization organization, SecurityManager userSecurity) throws AppException {
        return organizationPersistencePlugin.setOrganization( organization, userSecurity );
    }
    
	@Override
    public OrganizationTree load(SecurityManager userSecurity) {
		return organizationPersistencePlugin.load(userSecurity);
    }
	
	@Override
	public Organization getOrganizationById(Long organizationId) {
		return organizationPersistencePlugin.getOrganizationById(organizationId);
	}
	
	@Override
	public Organization getOrganization() {
		return organizationPersistencePlugin.getOrganization();
	}
	
	public void setOrganizationPersistencePlugin(OrganizationPersistencePlugin organizationPersistencePlugin) {
		this.organizationPersistencePlugin = organizationPersistencePlugin;
	}
}