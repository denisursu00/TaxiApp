package ro.cloudSoft.cloudDoc.services.organization;

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
public interface OrganizationUnitService {
    
    /**
     * Returneaza unitatea organizatorica care are ID-ul dat.
     * Daca nu se gaseste, va returna null.
     */
    public OrganizationUnit getOrganizationUnitById(Long id);
    
    /**
     * Returneaza unitatea organizatorica care are ID-ul dat.
     * Daca nu se gaseste, va returna null.
     */
    public OrganizationUnit getOrganizationUnitById(Long id, SecurityManager userSecurity);
    
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

    /**
     * Adauga sau modifica o unitate organizatorica
     * 
     * @return ID-ul unitatii organizatorice salvate
     */
    public Long setOrganizationUnit(OrganizationUnit gr, SecurityManager userSecurity) throws AppException;

	public Set<User> getOrganizationUnitUsers(Long orgUnitId);
	
    public void delete(OrganizationUnit group, SecurityManager userSecurity) throws AppException;
    
    List<String> getEmails(Collection<Long> organizationUnitIds);
    
    void moveOrganizationUnitToOrganizationUnit(Long idForOrganizationUnitToMove,
		Long idForDestinationOrganizationUnit, SecurityManager userSecurity)
    	throws AppException;
    
    void moveOrganizationUnitToOrganization(Long idForOrganizationUnitToMove,
		Long idForDestinationOrganization, SecurityManager userSecurity)
		throws AppException;
}