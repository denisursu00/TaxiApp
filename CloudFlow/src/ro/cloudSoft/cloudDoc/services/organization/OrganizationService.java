package ro.cloudSoft.cloudDoc.services.organization;

import ro.cloudSoft.cloudDoc.core.AppException;
import ro.cloudSoft.cloudDoc.domain.organization.Organization;
import ro.cloudSoft.cloudDoc.domain.organization.tree.OrganizationTree;
import ro.cloudSoft.cloudDoc.domain.security.SecurityManager;

/**
 *
 * 
 */
public interface OrganizationService {
	
    Long save(Organization organization, SecurityManager userSecurity) throws AppException;

    OrganizationTree load( SecurityManager userSecurity);
    
    Organization getOrganizationById(Long organizationId);
    
    /**
     * Returneaza organizatia existenta in aplicatie.
     * 
     * @throws IllegalStateException daca nu se gaseste nici o organizatie sau daca se gasesc mai multe
     */
    Organization getOrganization();
}