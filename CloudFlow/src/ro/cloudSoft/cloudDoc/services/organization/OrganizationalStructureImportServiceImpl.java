package ro.cloudSoft.cloudDoc.services.organization;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.springframework.transaction.annotation.Transactional;

import ro.cloudSoft.cloudDoc.core.AppException;
import ro.cloudSoft.cloudDoc.core.AppExceptionCodes;
import ro.cloudSoft.cloudDoc.dao.directory.organization.DirectoryGroupDao;
import ro.cloudSoft.cloudDoc.dao.directory.organization.DirectoryOrganizationUnitDao;
import ro.cloudSoft.cloudDoc.dao.directory.organization.DirectoryUserDao;
import ro.cloudSoft.cloudDoc.domain.directory.organization.DirectoryGroup;
import ro.cloudSoft.cloudDoc.domain.directory.organization.DirectoryOrganizationUnit;
import ro.cloudSoft.cloudDoc.domain.directory.organization.DirectoryUser;
import ro.cloudSoft.cloudDoc.domain.organization.Group;
import ro.cloudSoft.cloudDoc.domain.organization.Organization;
import ro.cloudSoft.cloudDoc.domain.organization.OrganizationUnit;
import ro.cloudSoft.cloudDoc.domain.organization.User;
import ro.cloudSoft.cloudDoc.domain.security.SecurityManager;
import ro.cloudSoft.cloudDoc.utils.log.LogHelper;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

public class OrganizationalStructureImportServiceImpl implements OrganizationalStructureImportService {
	
	private static final LogHelper LOGGER = LogHelper.getInstance(OrganizationalStructureImportServiceImpl.class);

	private final DirectoryUserDao directoryUserDao;
	private final DirectoryOrganizationUnitDao directoryOrganizationUnitDao;
	private final DirectoryGroupDao directoryGroupDao;
	
	private final OrganizationService organizationService;
	private final UserService userService;
	private final OrganizationUnitService organizationUnitService;
	private final GroupService groupService;
	
	private final String organizationRootDn;
	
	private final String defaultUserTitle;
	
	public OrganizationalStructureImportServiceImpl(DirectoryUserDao directoryUserDao, DirectoryOrganizationUnitDao directoryOrganizationUnitDao,
			DirectoryGroupDao directoryGroupDao, OrganizationService organizationService, UserService userService, OrganizationUnitService organizationUnitService,
			GroupService groupService, String organizationRootDn, String defaultUserTitle) {
		
		this.directoryUserDao = directoryUserDao;
		this.directoryOrganizationUnitDao = directoryOrganizationUnitDao;
		this.directoryGroupDao = directoryGroupDao;
		
		this.organizationService = organizationService;
		this.userService = userService;
		this.organizationUnitService = organizationUnitService;
		this.groupService = groupService;
		
		this.organizationRootDn = organizationRootDn;
		
		this.defaultUserTitle = defaultUserTitle;
	}

	@Override
	@Transactional(rollbackFor = Throwable.class)
	public void importOrganizationalStructureFromDirectory(SecurityManager userSecurity) throws AppException {
		
		Organization organization = organizationService.getOrganization();
		
		Map<String, User> userByDirectoryDnInLowerCase = Maps.newHashMap();
		
		List<DirectoryUser> directoryUsersInOrganizationRoot = directoryUserDao.getUsersOfParent(organizationRootDn);
		for (DirectoryUser directoryUserInOrganizationRoot : directoryUsersInOrganizationRoot) {
			importUser(organization, directoryUserInOrganizationRoot, userByDirectoryDnInLowerCase, userSecurity);
		}
		
		List<DirectoryOrganizationUnit> directoryOrganizationUnitsInOrganizationRoot = directoryOrganizationUnitDao.getOrganizationUnitsOfParent(organizationRootDn);
		for (DirectoryOrganizationUnit directoryOrganizationUnitInOrganizationRoot : directoryOrganizationUnitsInOrganizationRoot) {
			importOrganizationUnit(organization, directoryOrganizationUnitInOrganizationRoot, userByDirectoryDnInLowerCase, userSecurity);
		}
		
		List<DirectoryGroup> directoryGroups = directoryGroupDao.getAllGroups();
		for (DirectoryGroup directoryGroup : directoryGroups) {
			importGroup(directoryGroup, userByDirectoryDnInLowerCase, userSecurity);
		}
	}
	
	/**
	 * Importa utilizatorul din director care are ca parinte cel dat (poate fi organizatie sau unitate organizatorica).
	 * Daca utilizatorul exista deja, va fi actualizat cu datele din director.
	 */
	private void importUser(Object parent, DirectoryUser directoryUser,
			Map<String, User> userByDirectoryDnInLowerCase, SecurityManager userSecurity)
			throws AppException {
		
		if (StringUtils.isBlank(directoryUser.getUsername())) {
			
			String logMessage = "Utilizatorul din director cu DN-ul [" + directoryUser.getDn() + "] NU are completat username-ul.";
			LOGGER.warn(logMessage, "importul unui utilizator din director", userSecurity);
			
			return;
		}
		
		String userTitleToUse = null;
		if (StringUtils.isNotBlank(directoryUser.getTitle())) {
			userTitleToUse = directoryUser.getTitle();
		} else {
			
			String logMessageWhenDirectoryUserTitleIsBlank = "Utilizatorul din director cu DN-ul [" + directoryUser.getDn() + "] NU are completat titlul.";
			LOGGER.warn(logMessageWhenDirectoryUserTitleIsBlank, "importul unui utilizator din director", userSecurity);
			
			if (StringUtils.isNotBlank(defaultUserTitle)) {
				
				String logMessageWhenDefaultUserTitleExists = "Voi folosi titlul implicit (" + defaultUserTitle + ") pentru utilizatorul cu DN-ul [" + directoryUser.getDn() + "].";
				LOGGER.warn(logMessageWhenDefaultUserTitleExists, "importul unui utilizator din director", userSecurity);
				
				userTitleToUse = defaultUserTitle;
			} else {
				
				String logMessageWhenNoDefaultUserTitleExists = "Nu exista titlu implicit definit pentru utilizatori, " +
					"iar titlul utilizatorului din director cu DN-ul [" + directoryUser.getDn() + "] NU este completat. " +
					"In consecinta, utilizatorul NU poate fi importat.";
				LOGGER.warn(logMessageWhenNoDefaultUserTitleExists, "importul unui utilizator din director", userSecurity);
				
				return;
			}
		}
		
		User user = userService.getUserByUsernameAndTitle(directoryUser.getUsername(), userTitleToUse);
		
		if (user == null) {
			user = new User();
		}
		
		if (parent instanceof OrganizationUnit) {
			OrganizationUnit parentOrganizationUnit = (OrganizationUnit) parent;
			user.setOrganization(null);
			user.setOu(parentOrganizationUnit);
		} else if (parent instanceof Organization) {
			Organization parentOrganization = (Organization) parent;
			user.setOrganization(parentOrganization);
			user.setOu(null);
		} else {
			throw new IllegalArgumentException("Un utilizator trebuie sa aiba ca parinte ori o unitate organizatorica, ori o organizatie.");
		}

		user.setUsername(directoryUser.getUsername());
		user.setFirstName(directoryUser.getFirstName());
		user.setLastName(directoryUser.getLastName());
		user.setPassword(directoryUser.getPassword());
		user.setEmail(directoryUser.getEmail());
		user.setTitle(userTitleToUse);
		user.setEmployeeNumber(directoryUser.getEmployeeNumber());
		
		try {
			
			userService.setUser(user, userSecurity);
			
			String logMessage = "S-a importat cu succes utilizatorul cu ID-ul [" + user.getId() + "], " +
				"numele [" + user.getName() + "], titlul [" + user.getTitle() + "], avand ca parinte " + 
				getParentIdentifiersTextForLogMessages(parent) + ". Sursa a fost utilizatorul din director " +
				"cu DN-ul [" + directoryUser.getDn() + "].";
			LOGGER.info(logMessage, "importul unui utilizator din director", userSecurity);
		} catch (AppException ae) {
			if (ae.getCode().equals(AppExceptionCodes.USER_NOT_IN_DIRECTORY)) {
				
				String logMessage = "S-a cerut importul din director al utilizatorului cu DN-ul " +
					"[" + directoryUser.getDn() + "], insa acesta NU s-a gasit in director " +
					"la cautarea dupa username.";
				LOGGER.error(logMessage, "importul unui utilizator din director", userSecurity);
				
				return;
			} else {
				throw ae;
			}
		}
		
		String userDnInLowerCase = directoryUser.getDn().toLowerCase();
		userByDirectoryDnInLowerCase.put(userDnInLowerCase, user);
	}
	
	/**
	 * Importa unitatea organizatorica din director, avand ca parinte cel dat (poate fi organizatie sau unitate organizatorica).
	 * Daca exista deja unitatea, va fi actualizata cu datele din director.
	 */
	private void importOrganizationUnit(Object parent, DirectoryOrganizationUnit directoryOrganizationUnit,
			Map<String, User> userByDirectoryDnInLowerCase, SecurityManager userSecurity) throws AppException {

		OrganizationUnit organizationUnit = null;
		if (parent instanceof OrganizationUnit) {
			OrganizationUnit parentOrganizationUnit = (OrganizationUnit) parent;
			organizationUnit = organizationUnitService.getOrganizationUnitByNameAndParentOrganizationUnit(
				parentOrganizationUnit.getId(), directoryOrganizationUnit.getName());
		} else if (parent instanceof Organization) {
			Organization parentOrganization = (Organization) parent;
			organizationUnit = organizationUnitService.getOrganizationUnitByNameAndParentOrganization(
				parentOrganization.getId(), directoryOrganizationUnit.getName());
		} else {
			throw new IllegalArgumentException("O unitate organizatorica trebuie sa aiba ca parinte ori o unitate organizatorica, ori o organizatie.");
		}
		
		if (organizationUnit == null) {
			organizationUnit = new OrganizationUnit();
		}

		if (parent instanceof OrganizationUnit) {
			OrganizationUnit parentOrganizationUnit = (OrganizationUnit) parent;
			organizationUnit.setOrganization(null);
			organizationUnit.setParentOu(parentOrganizationUnit);
		} else if (parent instanceof Organization) {
			Organization parentOrganization = (Organization) parent;
			organizationUnit.setOrganization(parentOrganization);
			organizationUnit.setParentOu(null);
		} else {
			throw new IllegalArgumentException("O unitate organizatorica trebuie sa aiba ca parinte ori o unitate organizatorica, ori o organizatie.");
		}

		organizationUnit.setName(directoryOrganizationUnit.getName());
		organizationUnit.setDescription(directoryOrganizationUnit.getDescription());
		
		organizationUnitService.setOrganizationUnit(organizationUnit, userSecurity);
		
		String logMessage = "S-a importat cu succes unitatea organizatorica cu ID-ul [" + organizationUnit.getId() + "], " +
			"numele [" + organizationUnit.getName() + "], avand ca parinte " + getParentIdentifiersTextForLogMessages(parent) + ". " +
			"Sursa a fost unitatea organizatorica din director cu DN-ul [" + directoryOrganizationUnit.getDn() + "].";
		LOGGER.info(logMessage, "importul unei unitati organizatorice din director", userSecurity);
		
		String directoryOrganizationUnitDn = directoryOrganizationUnit.getDn();
		
		List<DirectoryUser> childDirectoryUsers = directoryUserDao.getUsersOfParent(directoryOrganizationUnitDn);
		for (DirectoryUser childDirectoryUser : childDirectoryUsers) {
			importUser(organizationUnit, childDirectoryUser, userByDirectoryDnInLowerCase, userSecurity);
		}
		
		List<DirectoryOrganizationUnit> childDirectoryOrganizationUnits = directoryOrganizationUnitDao.getOrganizationUnitsOfParent(directoryOrganizationUnitDn);
		for (DirectoryOrganizationUnit childDirectoryOrganizationUnit : childDirectoryOrganizationUnits) {
			importOrganizationUnit(organizationUnit, childDirectoryOrganizationUnit, userByDirectoryDnInLowerCase, userSecurity);
		}
	}
	
	/**
	 * Importa grupul din director.
	 * Daca exista deja, va fi actualizat cu utilizatorii din director care fac parte din grup.
	 */
	private void importGroup(DirectoryGroup directoryGroup, Map<String, User> userByDirectoryDnInLowerCase, SecurityManager userSecurity) throws AppException {
		
		Group group = groupService.getGroupByName(directoryGroup.getName());
		if (group == null) {
			group = new Group();
		}
		
		group.setName(directoryGroup.getName());
		group.setDescription(directoryGroup.getDescription());
		
		Set<User> memberUsers = Sets.newHashSet();
		for (String memberUserDirectoryDn : directoryGroup.getMemberDns()) {
			String memberUserDirectoryDnInLowerCase = memberUserDirectoryDn.toLowerCase();
			User memberUser = userByDirectoryDnInLowerCase.get(memberUserDirectoryDnInLowerCase);
			if (memberUser != null) {
				memberUsers.add(memberUser);
			} else {
				String logMessage = "S-a gasit in LDAP ca membru al grupului [" + directoryGroup.getDn() + "], " +
					"utilizatorul cu DN-ul [" + memberUserDirectoryDn + "], insa utilizatorul nu a fost gasit.";
				LOGGER.warn(logMessage, "importul unui grup din director", userSecurity);
			}
		}
		group.getUsers().addAll(memberUsers);
		
		groupService.setGroup(group, userSecurity);
		
		String logMessage = "S-a importat cu succes grupul cu ID-ul [" + group.getId() + "] si numele " +
			"[" + group.getName() + "]. Sursa a fost grupul din director cu DN-ul [" + directoryGroup.getDn() + "].";
		LOGGER.info(logMessage, "importul unui grup din director", userSecurity);
	}
	
	/**
	 * Returneaza textul cu identificatorii parintelui dat pentru a fi folosit in mesaje de log.
	 */
	private String getParentIdentifiersTextForLogMessages(Object parent) {
		if (parent instanceof Organization) {
			Organization parentOrganization = (Organization) parent;
			return ("organizatia cu ID-ul [" + parentOrganization.getId() + "] si numele [" + parentOrganization.getName() + "]");
		} else if (parent instanceof OrganizationUnit) {
			OrganizationUnit parentOrganizationUnit = (OrganizationUnit) parent;
			return ("unitatea organizatorica cu ID-ul [" + parentOrganizationUnit.getId() + "] si numele [" + parentOrganizationUnit.getName() + "]");
		} else {
			throw new IllegalArgumentException("Parinte de tip necunoscut: [" + parent.getClass() + "] - [" + parent + "]");
		}
	}
}