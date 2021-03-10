package ro.cloudSoft.cloudDoc.plugins.organization;

import java.util.Collection;
import java.util.List;
import java.util.Set;

import ro.cloudSoft.cloudDoc.core.AppException;
import ro.cloudSoft.cloudDoc.domain.organization.OrganizationUnit;
import ro.cloudSoft.cloudDoc.domain.organization.User;
import ro.cloudSoft.cloudDoc.domain.security.SecurityManager;

/**
 *
 * 
 */
public interface OrganizationUnitPersistencePlugin {
	
    Long setOrganizationUnit(OrganizationUnit gr, SecurityManager userSecurity) throws AppException;

    /**
     * Returneaza unitatea organizatorica care are ID-ul dat.
     * Daca nu se gaseste, va returna null.
     */
    OrganizationUnit getOrganizationUnitById(Long id);
    
    /**
     * Cauta si returneaza unitatea organizatorica cu numele dat 
     * care are ca parinte organizatia cu ID-ul dat.
     * Daca nu este gasita unitatea, metoda va returna null.
     * 
     * @throws IllegalStateException daca se gasesc mai multe unitati organizatorice avand criteriile date
     */
    OrganizationUnit getOrganizationUnitByNameAndParentOrganization(
		Long parentOrganizationId, String organizationUnitName);

    /**
     * Cauta si returneaza unitatea organizatorica cu numele dat care are ca parinte 
     * unitatea organizatorica cu ID-ul dat.
     * Daca nu este gasita unitatea, metoda va returna null.
     * 
     * @throws IllegalStateException daca se gasesc mai multe unitati organizatorice avand criteriile date
     */
    OrganizationUnit getOrganizationUnitByNameAndParentOrganizationUnit(
		Long parentOrganizationUnitId, String organizationUnitName);
    
    /**
     * Returneaza unitatea organizatorica cu numele dat.
     * Daca nu se gaseste nici o unitate organizatorica, va returna null.
     * 
     * @throws IllegalArgumentException daca se gasesc mai multe unitati organizatorice cu numele dat
     */
    OrganizationUnit getOrganizationUnitByName(String name);

    Set<User> getOrganizationUnitUsers(Long organizationUnitId);
    
    void delete(OrganizationUnit theOrganizationUnit, SecurityManager userSecurity);
    
    List<String> getEmails(Collection<Long> organizationUnitIds);
    
    boolean hasChildOrganizationUnits(Long organizationUnitId);
    
    boolean hasUsers(Long organizationUnitId);
    
    /**
     * Returneaza unitatile organizatorice care il au ca manager pe utilizatorul cu ID-ul specificat.
     */
    List<OrganizationUnit> getOrganizationUnitsWhereUserIsManager(Long userId);
    
    Collection<Long> getIdsForUsersInOrganizationUnit(Long organizationUnitId);
}