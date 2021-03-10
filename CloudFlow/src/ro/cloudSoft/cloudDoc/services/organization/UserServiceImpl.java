package ro.cloudSoft.cloudDoc.services.organization;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

import ro.cloudSoft.cloudDoc.core.AppException;
import ro.cloudSoft.cloudDoc.core.AppExceptionCodes;
import ro.cloudSoft.cloudDoc.core.constants.NomenclatorConstants;
import ro.cloudSoft.cloudDoc.dao.directory.organization.DirectoryUserDao;
import ro.cloudSoft.cloudDoc.dao.nomenclator.NomenclatorDao;
import ro.cloudSoft.cloudDoc.dao.nomenclator.NomenclatorValueDao;
import ro.cloudSoft.cloudDoc.dao.organizaiton.UserDeactivationDao;
import ro.cloudSoft.cloudDoc.domain.audit.AuditEntityOperation;
import ro.cloudSoft.cloudDoc.domain.nomenclator.NomenclatorValue;
import ro.cloudSoft.cloudDoc.domain.organization.Organization;
import ro.cloudSoft.cloudDoc.domain.organization.OrganizationUnit;
import ro.cloudSoft.cloudDoc.domain.organization.User;
import ro.cloudSoft.cloudDoc.domain.organization.User.UserTypeEnum;
import ro.cloudSoft.cloudDoc.domain.organization.UserAndNomenclatorPersonRelation;
import ro.cloudSoft.cloudDoc.domain.organization.UserDeactivation;
import ro.cloudSoft.cloudDoc.domain.organization.UserDeactivationMode;
import ro.cloudSoft.cloudDoc.domain.security.SecurityManager;
import ro.cloudSoft.cloudDoc.helpers.organization.ReactivateUserAccountHelper;
import ro.cloudSoft.cloudDoc.helpers.organization.UserDeactivationHelperFactory;
import ro.cloudSoft.cloudDoc.plugins.organization.GroupPersistencePlugin;
import ro.cloudSoft.cloudDoc.plugins.organization.OrganizationPersistencePlugin;
import ro.cloudSoft.cloudDoc.plugins.organization.OrganizationUnitPersistencePlugin;
import ro.cloudSoft.cloudDoc.plugins.organization.UserPersistencePlugin;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.organization.UserModel;
import ro.cloudSoft.cloudDoc.presentation.server.converters.organization.UserConverter;
import ro.cloudSoft.cloudDoc.services.AuditService;
import ro.cloudSoft.cloudDoc.services.nomenclator.NomenclatorService;
import ro.cloudSoft.cloudDoc.utils.nomenclator.NomenclatorValueUtils;
import ro.cloudSoft.common.utils.DependencyInjectionUtils;

/**
 *
 * 
 */
public class UserServiceImpl implements UserService, InitializingBean {
	
	private OrganizationService organizationService;
	private OrganizationUnitService organizationUnitService;
	private GroupService groupService;
	
	private AuditService auditService;

	private UserPersistencePlugin userPersistencePlugin;
    private OrganizationPersistencePlugin organizationPersistencePlugin;
    private OrganizationUnitPersistencePlugin organizationUnitPersistencePlugin;
    private GroupPersistencePlugin groupPersistencePlugin;
    
    private DirectoryUserDao directoryUserDao;
    private UserDeactivationDao userDeactivationDao;
    private NomenclatorDao nomenclatorDao;
    private NomenclatorValueDao nomenclatorValueDao;
    private NomenclatorService nomenclatorService;
    
    private UserDeactivationHelperFactory userDeactivationHelperFactory;
    
    // grup implicit unde va fi adaugat user-ul la adaugare
    private String nameOfGroupForAllUsers;
	
	@Override
	public void afterPropertiesSet() throws Exception {		
		DependencyInjectionUtils.checkRequiredDependencies(
			
			organizationService,
			organizationUnitService,
			groupService,
			
			auditService,
			
			userPersistencePlugin,
			organizationPersistencePlugin,
			organizationUnitPersistencePlugin,
			groupPersistencePlugin,
			
			directoryUserDao,
			userDeactivationDao,
			nomenclatorDao,
			nomenclatorValueDao,
			nomenclatorService,
			
			userDeactivationHelperFactory,
			
			nameOfGroupForAllUsers
		);
	}
    
    @Override
    @Transactional(rollbackFor = Throwable.class)
    public void setUser(User user, SecurityManager userSecurity) throws AppException {
    	setUser(user, false, userSecurity);
    }
    
    @Override
    @Transactional(rollbackFor = Throwable.class)
    public void setUserAndSetAsManager(User user, SecurityManager userSecurity) throws AppException {
    	setUser(user, true, userSecurity);    	
    }
    
    private void setUser(User user, boolean setAsManager, SecurityManager userSecurity) throws AppException {
    	
    	if (userPersistencePlugin.userWithUsernameAndTitleExists(user.getUsername(), user.getTitle(), user.getId())) {
    		throw new AppException(AppExceptionCodes.USER_EXISTS);
    	}
    	
    	boolean isAddOperation = (user.getId() == null);
    	
    	// TODO - De decomentat daca se va folosi LDAP in final.
    	/*
    	if (isAddOperation && !directoryUserDao.userExistsWithUsername(user.getUsername())) {
			throw new AppException(AppExceptionCodes.USER_NOT_IN_DIRECTORY);
		}
		*/
    	
    	userPersistencePlugin.saveUser(user);
    	
    	if (isAddOperation) {
    		groupPersistencePlugin.addUserToGroup(user, nameOfGroupForAllUsers, userSecurity);
    	}
    	
		if (setAsManager) {
			
			OrganizationUnit organizationUnit = user.getOu();
			Organization organization = user.getOrganization();
			
			if (organizationUnit != null) {
				organizationUnit.setManager(user);
				organizationUnitService.setOrganizationUnit(organizationUnit, userSecurity);
			} else if (organization != null) {
				organization.setOrganizationManager(user);
				organizationService.save(organization, userSecurity);
			}
		}
		
		if (user.getType().equals(UserTypeEnum.PERSON)) {
			saveUserAndNomenclatorPersonRelation(user);
		}
		
		AuditEntityOperation operation = (isAddOperation) ? AuditEntityOperation.ADD : AuditEntityOperation.MODIFY;
		auditService.auditUserOperation(userSecurity, user, operation);
    }
    
    private void saveUserAndNomenclatorPersonRelation(User user) {
    	
    	UserAndNomenclatorPersonRelation userPersonRelation = userPersistencePlugin.getUserAndNomenclatorPersonRelationByUserId(user.getId());
    	
		if (userPersonRelation == null) {
			userPersonRelation = new UserAndNomenclatorPersonRelation();
			userPersonRelation.setUser(user);
			NomenclatorValue person = new NomenclatorValue();
			person.setNomenclator(nomenclatorDao.findByCode(NomenclatorConstants.PERSOANE_NOMENCLATOR_CODE));			
			userPersonRelation.setNomenclatorPerson(person);
		}
		
		NomenclatorValue person = userPersonRelation.getNomenclatorPerson();
		NomenclatorValueUtils.setAttributeValue(person, NomenclatorConstants.PERSOANE_ATTRIBUTE_KEY_NUME, user.getLastName());
		NomenclatorValueUtils.setAttributeValue(person, NomenclatorConstants.PERSOANE_ATTRIBUTE_KEY_PRENUME, user.getFirstName());
		NomenclatorValueUtils.setAttributeValue(person, NomenclatorConstants.PERSOANE_ATTRIBUTE_KEY_EMAIL, user.getEmail());
		NomenclatorValueUtils.setAttributeValue(person, NomenclatorConstants.PERSOANE_ATTRIBUTE_KEY_TELEFON, user.getPhone());
		NomenclatorValueUtils.setAttributeValue(person, NomenclatorConstants.PERSOANE_ATTRIBUTE_KEY_FAX, user.getFax());
		NomenclatorValueUtils.setAttributeValue(person, NomenclatorConstants.PERSOANE_ATTRIBUTE_KEY_MOBIL, user.getMobile());
		if (user.getOu() != null) {
			NomenclatorValueUtils.setAttributeValue(person, NomenclatorConstants.PERSOANE_ATTRIBUTE_KEY_DEPARTAMENT, user.getOu().getName());
		}
		NomenclatorValueUtils.setAttributeValue(person, NomenclatorConstants.PERSOANE_ATTRIBUTE_KEY_FUNCTIE, user.getTitle());
		NomenclatorValueUtils.setAttributeValue(person, NomenclatorConstants.PERSOANE_ATTRIBUTE_KEY_INSTITUTIE, nomenclatorService.getInstitutieArb().getId().toString());
		
		nomenclatorValueDao.save(person);
		
		if (userPersonRelation.getId() == null) {
			userPersistencePlugin.saveUserAndNomenclatorPersonRelation(userPersonRelation);
		}
    }

    @Override
    public List<User> getAllUsers( SecurityManager userSecurity) {
        return userPersistencePlugin.getAllUsers();
    }
    
    @Override
    public Set<Long> getIdsOfActiveUsers() {
    	return userPersistencePlugin.getIdsOfUsersInGroupWithName(nameOfGroupForAllUsers);
    }

    @Override
    @Transactional
    public void delete(User theUser, SecurityManager userSecurity) {
    	
    	UserAndNomenclatorPersonRelation userPersonRelation = userPersistencePlugin.getUserAndNomenclatorPersonRelationByUserId(theUser.getId());
    	nomenclatorService.deleteNomenclatorValue(userPersonRelation.getNomenclatorPerson().getId());
    	userPersistencePlugin.deleteUserAndNomenclatorPersonRelation(userPersonRelation);
    	
    	userPersistencePlugin.delete(theUser);
    	auditService.auditUserOperation(userSecurity, theUser, AuditEntityOperation.DELETE);
    }
    
    private User getSuperiorFromOrganization(User user, Organization organization) {
    	if (organization.getOrganizationManager() != null) {
			if (!organization.getOrganizationManager().equals(user)) {
				return organization.getOrganizationManager();
			} else {
				return null;
			}
		} else {
			return null;
		}
    }
    
    private User getSuperiorFromOrganizationUnit(User user, OrganizationUnit organizationUnit) {
    	if ((organizationUnit.getManager() != null) && !organizationUnit.getManager().equals(user)) {
			return organizationUnit.getManager();
    	} else {
    		if (organizationUnit.getParentOu() != null) {
    			return getSuperiorFromOrganizationUnit(user, organizationUnit.getParentOu());
    		} else if (organizationUnit.getOrganization() != null) {
    			return getSuperiorFromOrganization(user, organizationUnit.getOrganization());
    		} else {
    			// TODO De logat cazul
    			throw new IllegalStateException("O unitate organizatorica trebuie sa aiba un parinte.");
    		}
    	}
    }
    
    @Override
    public User getSuperior(Long userId) {
    	User user = userPersistencePlugin.getUserById(userId);
    	if (user.getOu() != null) {
    		return getSuperiorFromOrganizationUnit(user, user.getOu());
    	} else if (user.getOrganization() != null) {
    		return getSuperiorFromOrganization(user, user.getOrganization());
    	} else {
    		// TODO De logat situatia
    		throw new IllegalStateException("Un user trebuie sa aiba un parinte.");
    	}
    }
    
    @Override
    public Set<Long> getDirectlySubordinateUserIds(Long userId, SecurityManager userSecurity) {
    	
    	Set<Long> directlySubordinateUserIds = Sets.newHashSet();
    	
    	List<OrganizationUnit> organizationUnitsWhereUserIsManager = organizationUnitPersistencePlugin.getOrganizationUnitsWhereUserIsManager(userId);
    	for (OrganizationUnit organizationUnit : organizationUnitsWhereUserIsManager) {
    		for (User userInOrganizationUnit : organizationUnit.getUsers()) {
    			if (!userInOrganizationUnit.getId().equals(userId)) {
    				directlySubordinateUserIds.add(userInOrganizationUnit.getId());
    			}
    		}
    		for (OrganizationUnit subOrganizationUnit : organizationUnit.getSubOus()) {
    			if ((subOrganizationUnit.getManager() != null) && !subOrganizationUnit.getManager().getId().equals(userId)) {
    				directlySubordinateUserIds.add(subOrganizationUnit.getManager().getId());
    			}
    		}
    	}
    	
    	List<Organization> organizationsWhereUserIsManager = organizationPersistencePlugin.getOrganizationsWhereUserIsManager(userId);
    	for (Organization organization : organizationsWhereUserIsManager) {
    		for (User userInOrganization : organization.getOrganizationUsers()) {
    			if (!userInOrganization.getId().equals(userId)) {
    				directlySubordinateUserIds.add(userInOrganization.getId());
    			}
    		}
    		for (OrganizationUnit organizationUnitInOrganization : organization.getOrganizationUnits()) {
    			if ((organizationUnitInOrganization.getManager() != null) && !organizationUnitInOrganization.getManager().getId().equals(userId)) {
    				directlySubordinateUserIds.add(organizationUnitInOrganization.getManager().getId());
    			}
    		}
    	}
    	
    	return directlySubordinateUserIds;
    }
    
    @Override
    public Set<Long> getAllSubordinateUserIds(Long userId, SecurityManager userSecurity) {
    	
    	Set<User> subordinateUsers = Sets.newHashSet();
    	Set<OrganizationUnit> organizationUnits = Sets.newHashSet();
    	
    	List<OrganizationUnit> organizationUnitsWhereUserIsManager = organizationUnitPersistencePlugin.getOrganizationUnitsWhereUserIsManager(userId);
    	for (OrganizationUnit organizationUnit : organizationUnitsWhereUserIsManager) {
    		organizationUnits.add(organizationUnit);
    	}
    	
    	List<Organization> organizationsWhereUserIsManager = organizationPersistencePlugin.getOrganizationsWhereUserIsManager(userId);
    	for (Organization organization : organizationsWhereUserIsManager) {
    		subordinateUsers.addAll(organization.getOrganizationUsers());
    		organizationUnits.addAll(organization.getOrganizationUnits());
    	}
    	
    	while (!organizationUnits.isEmpty()) {
    		Set<OrganizationUnit> subOrganizationUnits = Sets.newHashSet();
    		for (OrganizationUnit organizationUnit : organizationUnits) {
    			subordinateUsers.addAll(organizationUnit.getUsers());
    			subOrganizationUnits.addAll(organizationUnit.getSubOus());
    		}
    		organizationUnits = subOrganizationUnits;
    	}
    	
    	Set<Long> subordinateUserIds = Sets.newHashSet();
    	for (User subordinateUser : subordinateUsers) {
    		subordinateUserIds.add(subordinateUser.getId());
    	}
    	subordinateUserIds.remove(userId);
    	
    	return subordinateUserIds;
    }

    @Override
	public List<Long> getOrganizationUnitIds(Long userId) {
		return userPersistencePlugin.getOrganizationUnitIds(userId);
	}

    @Override
	public List<Long> getGroupIds(Long userId) {
		return userPersistencePlugin.getGroupIds(userId);
	}
	
    @Override
    public List<String> getGroupNames(Long userId) {
    	return userPersistencePlugin.getGroupNames(userId);
    }
    
	@Override
	public Map<Long, String> getUsersNameMap(Set<Long> ids, SecurityManager userSecurity) {
		return userPersistencePlugin.getUsersNameMap(ids);
	}
	
	@Override
    public	List<HashMap<String, HashMap<String,String>>> getUsersAndDepartment(String userIds,SecurityManager userSecurity) throws AppException {
		return userPersistencePlugin.getUsersAndDepartment(userIds);
	}
	
	@Override
	public List<String> getEmails(Collection<Long> userIds) {
		return this.userPersistencePlugin.getEmails(userIds);
	}
	
	private void handleManagerOfParentOnUserMove(User userToMove, SecurityManager userSecurity) throws AppException {

		OrganizationUnit organizationUnitWhereTheUserWas = userToMove.getOu();
		Organization organizationWhereTheUserWas = userToMove.getOrganization();
		
		if (organizationUnitWhereTheUserWas != null) {					
			User managerForOrganizationUnitWhereTheUserWas = organizationUnitWhereTheUserWas.getManager();
			boolean wasUserManager = (
				(managerForOrganizationUnitWhereTheUserWas != null) &&
				managerForOrganizationUnitWhereTheUserWas.getId().equals(userToMove.getId())
			);
			if (wasUserManager) {
				organizationUnitWhereTheUserWas.setManager(null);
				organizationUnitService.setOrganizationUnit(organizationUnitWhereTheUserWas, userSecurity);
			}
		} else if (organizationWhereTheUserWas != null) {
			User managerForOrganizationWhereTheUserWas = organizationWhereTheUserWas.getOrganizationManager();
			boolean wasUserManager = (
				(managerForOrganizationWhereTheUserWas != null) &&
				managerForOrganizationWhereTheUserWas.getId().equals(userToMove.getId())
			);
			if (wasUserManager) {
				organizationWhereTheUserWas.setOrganizationManager(null);
				organizationService.save(organizationWhereTheUserWas, userSecurity);
			}
		}
	}
	
	@Override
	@Transactional(rollbackFor = Throwable.class)
	public void moveUserToOrganizationUnit(Long idForUserToMove,
			Long idForDestinationOganizationUnit, SecurityManager userSecurity)
			throws AppException {
		
		User userToMove = userPersistencePlugin.getUserById(idForUserToMove);
		OrganizationUnit destinationOganizationUnit = organizationUnitPersistencePlugin.getOrganizationUnitById(idForDestinationOganizationUnit);
		
		handleManagerOfParentOnUserMove(userToMove, userSecurity);
		
		userToMove.setOu(destinationOganizationUnit);
		userToMove.setOrganization(null);
		
		userPersistencePlugin.saveUser(userToMove);
		
		auditService.auditUserOperation(userSecurity, userToMove, AuditEntityOperation.MOVE);
	}
	
	@Override
	@Transactional(rollbackFor = Throwable.class)
	public void moveUserToOrganization(Long idForUserToMove,
			Long idForDestinationOrganization, SecurityManager userSecurity)
			throws AppException {
		
		User userToMove = userPersistencePlugin.getUserById(idForUserToMove);
		Organization destinationOrganization = organizationPersistencePlugin
			.getOrganizationById(idForDestinationOrganization);
		
		handleManagerOfParentOnUserMove(userToMove, userSecurity);
		
		userToMove.setOu(null);
		userToMove.setOrganization(destinationOrganization);
		
		userPersistencePlugin.saveUser(userToMove);
		
		auditService.auditUserOperation(userSecurity, userToMove, AuditEntityOperation.MOVE);
	}
	
	@Override
	public String getDisplayName(Long userId) {
		return userPersistencePlugin.getDisplayName(userId);
	}
	
	@Override
	public List<User> getUsersWithIds(Collection<Long> userIds) {
		return userPersistencePlugin.getUsersWithIds(userIds);
	}
	
	@Override
	public boolean hasMultipleAccounts(String username) {
		return userPersistencePlugin.hasMultipleAccounts(username);
	}
	
	@Override
	public boolean userExists(Long id) {
		return userPersistencePlugin.userExists(id);
	}
	
	@Override
	public User getUserByUsername(String username) {
		return userPersistencePlugin.getUserByUsername(username);
	}
	
	@Override
	public User getActiveUserByUsername(String username) {
		List<User> foundActiveUsersWithUsername = userPersistencePlugin.getUserAccountsWithUsernameInGroupWithName(username, nameOfGroupForAllUsers);
		if (foundActiveUsersWithUsername.size() == 1) {
			return foundActiveUsersWithUsername.get(0);
		} else if (foundActiveUsersWithUsername.size() == 0) {
			return null;
		} else {
			throw new IllegalArgumentException("S-au gasit mai multi utilizatori activi cu username-ul [" + username + "].");
		}
	}
	
	@Override
	public User getUserByUsernameAndTitle(String username, String title) {
		return userPersistencePlugin.getUserByUsernameAndTitle(username, title);
	}
	
	@Override
	public User getUserById(Long id) {
		return userPersistencePlugin.getUserById(id);
	}
	
	@Override
	public User getUserById(Long id, SecurityManager userSecurity) {
		User user = getUserById(id);
		auditService.auditUserOperation(userSecurity, user, AuditEntityOperation.READ);
		return user;
	}
	
	@Override
	public List<User> getUsersWithUsername(String username) {
		return userPersistencePlugin.getUsersWithUsername(username);
	}
	
	/**
	 * Returneaza toate username-urile distincte.
	 */
	@Override
	public List<String> getAllUsernames() {
		return userPersistencePlugin.getAllUsernames();
	}
	
	@Override
	public void updateUsers(Collection<User> users) {
		userPersistencePlugin.updateUsers(users);
	}
	
	@Override
	@Transactional(rollbackFor = Throwable.class)
	public void deactivateUserWithId(Long userId, UserDeactivationMode deactivationMode) throws AppException {
		userDeactivationHelperFactory.getHelperFor(userId, deactivationMode).deactivate();
	}
	
	@Override
	public boolean isUserAccountActive(User userAccount) {
		return groupService.isUserInGroupWithName(userAccount.getId(), nameOfGroupForAllUsers);
	}
	
	@Override
	public List<User> getActiveUserAccountsOfUserWithId(Long userId) {
		return userPersistencePlugin.getUserAccountsForUserWithIdInGroupWithName(userId, nameOfGroupForAllUsers);
	}
	
	@Override
	public Set<Long> getIdsOfActiveUserAccountsForUsername(String username) {
		return userPersistencePlugin.getIdsOfUserAccountsWithUsernameInGroupWithName(username, nameOfGroupForAllUsers);
	}
	
	@Override
	public List<User> getActiveUserAccountsForUsername(String username) {
		return userPersistencePlugin.getUserAccountsWithUsernameInGroupWithName(username, nameOfGroupForAllUsers);
	}
	
	@Override
	public void saveUserDeactivations(Collection<UserDeactivation> userDeactivations) {
		userDeactivationDao.saveAll(userDeactivations);
	}
	
	@Override
	@Transactional(rollbackFor = Throwable.class)
	public void reactivateUserWithId(Long userId, SecurityManager userSecurity) throws AppException {
		new ReactivateUserAccountHelper(userId, this, groupService, userSecurity).reactivate();
	}
	
	@Override
	public UserDeactivation getDeactivationForUserWithId(Long userId) {
		return userDeactivationDao.getForUserWithId(userId);
	}
	
	@Override
	public void deleteUserDeactivation(UserDeactivation userDeactivation) {
		userDeactivationDao.delete(userDeactivation);
	}
	
	@Override
	public User getUserByNomenclatorPersonId(Long nomenclatorPersonId) {
		return this.userPersistencePlugin.getUserByNomenclatorPersonId(nomenclatorPersonId);
	}
	
	@Override
	public NomenclatorValue getNomenclatorPersonByUserId(Long userId) {
		return this.userPersistencePlugin.getNomenclatorPersonByUserId(userId);
	}
	
	@Override
	public List<String> getUserRoleNames(Long userId) {
		return this.userPersistencePlugin.getUserRoleNames(userId);
	}
	
	public void setOrganizationService(OrganizationService organizationService) {
		this.organizationService = organizationService;
	}
	public void setOrganizationUnitService(OrganizationUnitService organizationUnitService) {
		this.organizationUnitService = organizationUnitService;
	}
	public void setGroupService(GroupService groupService) {
		this.groupService = groupService;
	}
	public void setAuditService(AuditService auditService) {
		this.auditService = auditService;
	}
	public void setUserPersistencePlugin(UserPersistencePlugin userPersistencePlugin) {
		this.userPersistencePlugin = userPersistencePlugin;
	}
    public void setOrganizationPersistencePlugin(OrganizationPersistencePlugin organizationPersistencePlugin) {
		this.organizationPersistencePlugin = organizationPersistencePlugin;
	}
    public void setOrganizationUnitPersistencePlugin(OrganizationUnitPersistencePlugin organizationUnitPersistencePlugin) {
		this.organizationUnitPersistencePlugin = organizationUnitPersistencePlugin;
	}
    public void setGroupPersistencePlugin(GroupPersistencePlugin groupPersistencePlugin) {
		this.groupPersistencePlugin = groupPersistencePlugin;
	}
    public void setDirectoryUserDao(DirectoryUserDao directoryUserDao) {
		this.directoryUserDao = directoryUserDao;
	}
    public void setUserDeactivationDao(UserDeactivationDao userDeactivationDao) {
		this.userDeactivationDao = userDeactivationDao;
	}
    public void setUserDeactivationHelperFactory(UserDeactivationHelperFactory userDeactivationHelperFactory) {
		this.userDeactivationHelperFactory = userDeactivationHelperFactory;
	}
    public void setNameOfGroupForAllUsers(String nameOfGroupForAllUsers) {
		this.nameOfGroupForAllUsers = nameOfGroupForAllUsers;
	}
    public void setNomenclatorDao(NomenclatorDao nomenclatorDao) {
		this.nomenclatorDao = nomenclatorDao;
	}
    public void setNomenclatorValueDao(NomenclatorValueDao nomenclatorValueDao) {
		this.nomenclatorValueDao = nomenclatorValueDao;
	}
    public void setNomenclatorService(NomenclatorService nomenclatorService) {
		this.nomenclatorService = nomenclatorService;
	}

	@Override
	public List<UserModel> getAllUsersWithAssignedTasks() {
		
		List<User>users =  userPersistencePlugin.getAllUsersWithAssignedTasks();
		
		List<UserModel> userModels = new ArrayList<UserModel>();
		
		for (User user : users) {
			UserModel userModel = UserConverter.getModelFromUser(user);
			userModels.add(userModel);
		}
		
		return userModels;
	}

}