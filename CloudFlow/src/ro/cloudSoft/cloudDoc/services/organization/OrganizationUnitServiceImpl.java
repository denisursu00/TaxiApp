package ro.cloudSoft.cloudDoc.services.organization;

import static ro.cloudSoft.cloudDoc.core.AppExceptionCodes.CANNOT_DELETE_ORGANIZATION_UNIT_CHILDREN_EXIST;

import java.util.Collection;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.InitializingBean;

import ro.cloudSoft.cloudDoc.core.AppException;
import ro.cloudSoft.cloudDoc.domain.audit.AuditEntityOperation;
import ro.cloudSoft.cloudDoc.domain.organization.Organization;
import ro.cloudSoft.cloudDoc.domain.organization.OrganizationUnit;
import ro.cloudSoft.cloudDoc.domain.organization.User;
import ro.cloudSoft.cloudDoc.domain.security.SecurityManager;
import ro.cloudSoft.cloudDoc.plugins.organization.OrganizationPersistencePlugin;
import ro.cloudSoft.cloudDoc.plugins.organization.OrganizationUnitPersistencePlugin;
import ro.cloudSoft.cloudDoc.services.AuditService;
import ro.cloudSoft.common.utils.DependencyInjectionUtils;

/**
 *
 * , Nicolae Albu
 */
public class OrganizationUnitServiceImpl implements OrganizationUnitService, InitializingBean {
	
	private AuditService auditService;

    private OrganizationPersistencePlugin organizationPersistencePlugin;
    private OrganizationUnitPersistencePlugin organizationUnitPersistencePlugin;
	
	@Override
	public void afterPropertiesSet() throws Exception {		
		DependencyInjectionUtils.checkRequiredDependencies(
				
			auditService,
			
			organizationPersistencePlugin,
			organizationUnitPersistencePlugin
		);
	}
    
    @Override
    public OrganizationUnit getOrganizationUnitById(Long id) {
    	return organizationUnitPersistencePlugin.getOrganizationUnitById(id);
    }
    
    @Override
    public OrganizationUnit getOrganizationUnitById(Long id, SecurityManager userSecurity) {
    	OrganizationUnit organizationUnit = getOrganizationUnitById(id);
    	auditService.auditOrganizationUnitOperation(userSecurity, organizationUnit, AuditEntityOperation.READ);
    	return organizationUnit;
    }
    
    @Override
    public OrganizationUnit getOrganizationUnitByNameAndParentOrganization(
    		Long parentOrganizationId, String organizationUnitName) {
    	
    	return organizationUnitPersistencePlugin.getOrganizationUnitByNameAndParentOrganization(
			parentOrganizationId, organizationUnitName);
    }
    
    @Override
    public OrganizationUnit getOrganizationUnitByNameAndParentOrganizationUnit(
    		Long parentOrganizationUnitId, String organizationUnitName) {
    	
    	return organizationUnitPersistencePlugin.getOrganizationUnitByNameAndParentOrganizationUnit(
			parentOrganizationUnitId, organizationUnitName);
    }
    
    @Override
    public OrganizationUnit getOrganizationUnitByName(String name) {
    	return organizationUnitPersistencePlugin.getOrganizationUnitByName(name);
    }

    @Override
    public Long setOrganizationUnit(OrganizationUnit organizationUnit, SecurityManager userSecurity) throws AppException {
    	
    	boolean isAddOperation = (organizationUnit.getId() == null);
        Long id = organizationUnitPersistencePlugin.setOrganizationUnit(organizationUnit, userSecurity);
        
        AuditEntityOperation operation = (isAddOperation) ? AuditEntityOperation.ADD : AuditEntityOperation.MODIFY;
        auditService.auditOrganizationUnitOperation(userSecurity, organizationUnit, operation);
        
        return id;
    }
    
    @Override
    public Set<User> getOrganizationUnitUsers(Long orgUnitId) {
         return organizationUnitPersistencePlugin.getOrganizationUnitUsers(orgUnitId);
    }

    @Override
    public void delete(OrganizationUnit organizationUnit, SecurityManager userSecurity) throws AppException {
    	
    	Long organizationUnitId = organizationUnit.getId();
    	if (organizationUnitPersistencePlugin.hasChildOrganizationUnits(organizationUnitId)
    			|| organizationUnitPersistencePlugin.hasUsers(organizationUnitId)) {
    		throw new AppException(CANNOT_DELETE_ORGANIZATION_UNIT_CHILDREN_EXIST);
    	}
    		
        organizationUnitPersistencePlugin.delete(organizationUnit, userSecurity);
        
        auditService.auditOrganizationUnitOperation(userSecurity, organizationUnit, AuditEntityOperation.DELETE);
    }
    
    @Override
    public List<String> getEmails(Collection<Long> organizationUnitIds) {
    	return this.organizationUnitPersistencePlugin.getEmails(organizationUnitIds);
    }
    
    @Override
    public void moveOrganizationUnitToOrganizationUnit(Long idForOrganizationUnitToMove,
    		Long idForDestinationOrganizationUnit, SecurityManager userSecurity)
    		throws AppException {
    	
    	OrganizationUnit organizationUnitToMove = organizationUnitPersistencePlugin.getOrganizationUnitById(idForOrganizationUnitToMove);
    	OrganizationUnit destinationOrganizationUnit = organizationUnitPersistencePlugin.getOrganizationUnitById(idForDestinationOrganizationUnit);
    	
    	organizationUnitToMove.setParentOu(destinationOrganizationUnit);
    	organizationUnitToMove.setOrganization(null);
    	
    	organizationUnitPersistencePlugin.setOrganizationUnit(organizationUnitToMove, userSecurity);
    	
    	auditService.auditOrganizationUnitOperation(userSecurity, organizationUnitToMove, AuditEntityOperation.MOVE);
    }
    
    @Override
    public void moveOrganizationUnitToOrganization(Long idForOrganizationUnitToMove,
    		Long idForDestinationOrganization, SecurityManager userSecurity)
    		throws AppException {
    	
    	OrganizationUnit organizationUnitToMove = organizationUnitPersistencePlugin.getOrganizationUnitById(idForOrganizationUnitToMove);
    	Organization destinationOrganization = organizationPersistencePlugin.getOrganizationById(idForDestinationOrganization);
    	
    	organizationUnitToMove.setParentOu(null);
    	organizationUnitToMove.setOrganization(destinationOrganization);
    	
    	organizationUnitPersistencePlugin.setOrganizationUnit(organizationUnitToMove, userSecurity);
    	
    	auditService.auditOrganizationUnitOperation(userSecurity, organizationUnitToMove, AuditEntityOperation.MOVE);
    }
    
    public void setAuditService(AuditService auditService) {
		this.auditService = auditService;
	}
    public void setOrganizationPersistencePlugin(OrganizationPersistencePlugin organizationPersistencePlugin) {
		this.organizationPersistencePlugin = organizationPersistencePlugin;
	}
    public void setOrganizationUnitPersistencePlugin(OrganizationUnitPersistencePlugin organizationUnitPersistencePlugin) {
		this.organizationUnitPersistencePlugin = organizationUnitPersistencePlugin;
	}
}