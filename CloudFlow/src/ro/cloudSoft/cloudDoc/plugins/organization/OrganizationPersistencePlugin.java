package ro.cloudSoft.cloudDoc.plugins.organization;

import java.util.List;

import ro.cloudSoft.cloudDoc.core.AppException;
import ro.cloudSoft.cloudDoc.domain.organization.Organization;
import ro.cloudSoft.cloudDoc.domain.organization.tree.OrganizationTree;
import ro.cloudSoft.cloudDoc.domain.security.SecurityManager;

public interface OrganizationPersistencePlugin {
    
	/**
	 * Returneaza organizatia pentru a fi folosita de catre populatorul organizatoric.
	 * 
	 * Cazul este unul exceptional fata de cel normal cand organizatia este ceruta
	 * intr-un request de la utilizator, populatorul ruland la initializarea aplicatiei.
	 */
    Organization getOrganizationForPopulator(final String organizationName, SecurityManager userSecurity);
    
    /**
     * Allows loading into the memory the organization from the repository
     * @return
     */
    public OrganizationTree load( SecurityManager userSecurity);

    /**
     * Save organization definition into repository
     * @param orgman the organization to be saved
     */
    public Long setOrganization(Organization orgman, SecurityManager userSecurity) throws AppException;
    
    Organization getOrganizationById(Long organizationId);
    
    /**
     * Returneaza organizatia existenta in aplicatie.
     * 
     * @throws IllegalStateException daca nu se gaseste nici o organizatie sau daca se gasesc mai multe
     */
    Organization getOrganization();

    /**
     * Returneaza organizatiile care il au ca manager pe utilizatorul cu ID-ul specificat.
     */
    List<Organization> getOrganizationsWhereUserIsManager(Long userId);
}