package ro.cloudSoft.cloudDoc.presentation.server.services;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.InitializingBean;

import com.extjs.gxt.ui.client.data.ModelData;
import com.google.common.collect.Lists;

import ro.cloudSoft.cloudDoc.core.AppException;
import ro.cloudSoft.cloudDoc.core.AppExceptionCodes;
import ro.cloudSoft.cloudDoc.core.constants.NomenclatorConstants;
import ro.cloudSoft.cloudDoc.dao.nomenclator.NomenclatorValueDao;
import ro.cloudSoft.cloudDoc.domain.directory.organization.DirectoryUser;
import ro.cloudSoft.cloudDoc.domain.directory.organization.DirectoryUserSearchCriteria;
import ro.cloudSoft.cloudDoc.domain.nomenclator.NomenclatorValue;
import ro.cloudSoft.cloudDoc.domain.organization.Group;
import ro.cloudSoft.cloudDoc.domain.organization.OrganizationUnit;
import ro.cloudSoft.cloudDoc.domain.organization.User;
import ro.cloudSoft.cloudDoc.domain.organization.User.UserTypeEnum;
import ro.cloudSoft.cloudDoc.domain.organization.UserDeactivationMode;
import ro.cloudSoft.cloudDoc.domain.organization.tree.OrganizationNode;
import ro.cloudSoft.cloudDoc.domain.organization.tree.OrganizationTree;
import ro.cloudSoft.cloudDoc.domain.security.SecurityManager;
import ro.cloudSoft.cloudDoc.presentation.client.shared.exceptions.PresentationException;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.directory.organization.DirectoryUserModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.directory.organization.GwtDirectoryUserSearchCriteria;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.organization.GroupModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.organization.GwtUserDeactivationMode;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.organization.OrganizationModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.organization.OrganizationUnitModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.organization.UserModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.services.OrganizationGxtService;
import ro.cloudSoft.cloudDoc.presentation.server.converters.directory.organization.DirectoryUserConverter;
import ro.cloudSoft.cloudDoc.presentation.server.converters.directory.organization.DirectoryUserSearchCriteriaConverter;
import ro.cloudSoft.cloudDoc.presentation.server.converters.organization.GroupConverter;
import ro.cloudSoft.cloudDoc.presentation.server.converters.organization.OrganizationUnitConverter;
import ro.cloudSoft.cloudDoc.presentation.server.converters.organization.UserConverter;
import ro.cloudSoft.cloudDoc.presentation.server.converters.organization.UserDeactivationModeConverter;
import ro.cloudSoft.cloudDoc.presentation.server.utils.PresentationExceptionUtils;
import ro.cloudSoft.cloudDoc.services.directory.organization.DirectoryUserService;
import ro.cloudSoft.cloudDoc.services.organization.GroupService;
import ro.cloudSoft.cloudDoc.services.organization.OrganizationService;
import ro.cloudSoft.cloudDoc.services.organization.OrganizationUnitService;
import ro.cloudSoft.cloudDoc.services.organization.OrganizationalStructureImportService;
import ro.cloudSoft.cloudDoc.services.organization.UserService;
import ro.cloudSoft.cloudDoc.utils.PasswordEncoder;
import ro.cloudSoft.cloudDoc.utils.log.LogHelper;
import ro.cloudSoft.common.utils.DependencyInjectionUtils;

public class OrganizationGxtServiceImpl extends GxtServiceImplBase implements OrganizationGxtService, InitializingBean {
	
	private static final LogHelper LOGGER = LogHelper.getInstance(OrganizationGxtServiceImpl.class);

	private UserService userService;
	private OrganizationUnitService organizationUnitService;
	private GroupService groupService;
	private OrganizationService organizationService;
	
	private DirectoryUserService directoryUserService;
	
	private OrganizationalStructureImportService organizationalStructureImportService;
	private PasswordEncoder passwordEncoder;
	private NomenclatorValueDao nomenclatorValueDao;
	
	public OrganizationGxtServiceImpl(UserService userSvc, OrganizationUnitService organizationUnitSvc,
			GroupService groupSvc, OrganizationService organizationSvc, DirectoryUserService directoryUserSvc,
			OrganizationalStructureImportService organizationalStructureImportSvc, PasswordEncoder passwordEncoder,
			NomenclatorValueDao nomenclatorValueDao) {
		
		userService = userSvc;
		organizationUnitService = organizationUnitSvc;
		groupService = groupSvc;
		organizationService = organizationSvc;
		
		directoryUserService = directoryUserSvc;
		
		organizationalStructureImportService = organizationalStructureImportSvc;
		this.passwordEncoder = passwordEncoder;
		this.nomenclatorValueDao = nomenclatorValueDao;
	}
	
	@Override
	public void afterPropertiesSet() throws Exception {		
		DependencyInjectionUtils.checkRequiredDependencies(
			userService,
			organizationUnitService,
			groupService,
			organizationService
		);
	}

	@Override
	public List<UserModel> getUsers() {
		List<UserModel> models = new ArrayList<UserModel>();
		List<User> data = userService.getAllUsers(getSecurity());
		if (data != null) {
			for (User user : data) {
				models.add(UserConverter.getModelFromUser(user));
			}
		}
		return models;
	}
	
	@Override
	public List<UserModel> getAllActiveUsers() {
		List<UserModel> models = new ArrayList<UserModel>();
		List<User> data = userService.getAllUsers(getSecurity());
		Set<Long> idsOfActiveUsers = userService.getIdsOfActiveUsers();
		if (data != null) {
			for (User user : data) {
				if (idsOfActiveUsers.contains(user.getId())){
					models.add(UserConverter.getModelFromUser(user));
				}
			}
		}
		return models;
	}
	
	@Override
	public Set<Long> getIdsOfActiveUsers() {
		return userService.getIdsOfActiveUsers();
	}
	
	@Override
	public List<UserModel> getUsersFromOrgUnit(String orgUnitId) {
		List<UserModel> userModels = new ArrayList<UserModel>();
		Set<User> users = organizationUnitService.getOrganizationUnitUsers(new Long(orgUnitId));
		
		for (User user : users) {
			userModels.add(UserConverter.getModelFromUser(user));
		}
		
		return userModels;
	}

	@Override
	public List<UserModel> getUsersFromGroup(String groupId) {
		List<UserModel> models = new ArrayList<UserModel>();
		List<User> data = groupService.getAllUsersWithGroup(new Long(groupId), getSecurity());

		if (data != null) {
			for (User user : data) {
				models.add(UserConverter.getModelFromUser(user));
			}
		}

		return models;
	}
	
	@Override
	public List<UserModel> getUsersFromGroupByGroupName(String groupName) {
		List<UserModel> models = new ArrayList<UserModel>();
		
		Group group = groupService.getGroupByName(groupName);
		 
		if (group != null) {
			List<User> data = groupService.getAllUsersWithGroup(group.getId(), getSecurity());
			
			if (data != null) {
				for (User user : data) {
					models.add(UserConverter.getModelFromUser(user));
				}
			}
		}
		
		return models;
	}

	@Override
	public String setOrgUnit(OrganizationUnitModel orgUnitModel) throws PresentationException {
		OrganizationUnit orgUnit = OrganizationUnitConverter.getOrganizationUnitFromModel(
			orgUnitModel, organizationService, organizationUnitService, getSecurity());
		String returnValue = null;
		try {
			returnValue = organizationUnitService.setOrganizationUnit(orgUnit, getSecurity()).toString();
		} catch (AppException ae) {
			throw PresentationExceptionUtils.getPresentationException(ae);
		}
		return returnValue;
	}

	@Override
	public void deleteOrgUnit(OrganizationUnitModel orgUnitModel) throws PresentationException {
		try {
			OrganizationUnit orgUnit = organizationUnitService.getOrganizationUnitById(new Long(orgUnitModel.getId()));
			organizationUnitService.delete(orgUnit, getSecurity());
		} catch (AppException ae) {
			throw PresentationExceptionUtils.getPresentationException(ae);
		}
	}

	@Override
	public OrganizationUnitModel getOrgUnitById(String id) {
		if (id == null)
			return null;
		OrganizationUnit orgUnit = organizationUnitService.getOrganizationUnitById(new Long(id));
		if (orgUnit == null)
			return null;
		OrganizationUnitModel groupModel = OrganizationUnitConverter.getModelFromOrganizationUnit(orgUnit);
		return groupModel;
	}

	@Override
	public void deleteUser(UserModel userModel) {
		User user = userService.getUserById(Long.valueOf(userModel.getUserId()));
		userService.delete(user, getSecurity());
	}

	@Override
	public void setUser(UserModel userModel) throws PresentationException {
		User userEntity = null;
		if (userModel.getUserIdAsLong() != null) {
			userEntity = userService.getUserById(userModel.getUserIdAsLong());
		}
		if (userModel.getType().equals(UserTypeEnum.PERSON.name()) && !existsDenumireFunctiePersoana(userModel.getTitle())) {
			throw new PresentationException(AppExceptionCodes.FUNCTION_NAME_DOES_NOT_EXIST_IN_NOMENCLATOR.name());
		}
		try {
			if (StringUtils.isNotEmpty(userModel.getPassword())) {
				userModel.setPassword(passwordEncoder.generatePasswordHash(userModel.getPassword()));
			}
			User user = UserConverter.getUserFromModel(userModel, userEntity,
				organizationService, organizationUnitService, getSecurity());

			boolean setAsManager = userModel.isIsManager();
			if (setAsManager) {
				userService.setUserAndSetAsManager(user, getSecurity());
			} else {
				userService.setUser(user, getSecurity());
			}
		} catch (AppException ae) {
			throw PresentationExceptionUtils.getPresentationException(ae);
		}
	}
	
	public boolean existsDenumireFunctiePersoana(String denumireFunctiePersoana) {
		List<NomenclatorValue> nomenclatorValues = nomenclatorValueDao.findByNomenclatorCodeAndAttribute(NomenclatorConstants.FUNCTII_PERSOANE, NomenclatorConstants.NOMENCLATOR_FUNCTII_PERSOANE_ATTR_KEY_DENUMIRE, denumireFunctiePersoana);
		return CollectionUtils.isNotEmpty(nomenclatorValues);
	}
	
	@Override
	public UserModel getUserById(String id) {
		if (id == null)
			return null;
		User user = userService.getUserById(new Long(id), getSecurity());
		if (user == null)
			return null;
		UserModel userModel = UserConverter.getModelFromUser(user);
		return userModel;
	}

	@Override
	public OrganizationModel getOrganization() {
		OrganizationModel organizationModel = new OrganizationModel();
		OrganizationTree organizationTree = organizationService.load(getSecurity());
		OrganizationNode root = organizationTree.getRootElement();
		organizationModel.setId(root.getId().toString());
		organizationModel.setName(root.getName());
		organizationModel.setChildren(getNodeChildren(root));
		if (root.getManagerId() != null) {
			organizationModel.setManagerId(root.getManagerId().toString());
		}
		return organizationModel;
	}

	private ArrayList<ModelData> getNodeChildren(OrganizationNode node) {
		ArrayList<ModelData> list = new ArrayList<ModelData>();
		for (OrganizationNode nod : node.getChildren()) {
			// type = 1 inseamna group, type = 2 inseamna user, type = 0 inseamna root
			if (nod.getType() == 1) {
				OrganizationUnitModel gm = new OrganizationUnitModel();
				gm.setId(nod.getId().toString());
				gm.setName(nod.getName());
				// Daca unitatea organizatorica are manager, i-l seteaza modelului.
				if (nod.getManagerId() != null) {
					gm.setManagerId(nod.getManagerId().toString());
				}
				gm.setChildren(getNodeChildren(nod));
				list.add(gm);
			} else if (nod.getType() == 2) {
				UserModel um = new UserModel();
				um.setUserId(nod.getId().toString());
				um.setName(nod.getName());
				um.setTitle(nod.getTitle());
				um.setCustomTitleTemplate(nod.getCustomTitleTemplate());
				list.add(um);
			}
		}
		return list;
	}

	@Override
	public List<GroupModel> getGroups() {
		
		List<GroupModel> models = new ArrayList<GroupModel>();
		List<Group> data = groupService.getGroups(getSecurity());

		if (data != null) {
			for (Group group : data) {
				GroupModel rm = GroupConverter.getModelFromGroup(group);
				models.add(rm);
			}
		}
		
		return models;
	}
	
	@Override
	public GroupModel getGroupById(String idAsString) {
		Long id = Long.valueOf(idAsString);
		Group group = groupService.getGroup(id, getSecurity());
		GroupModel groupModel = GroupConverter.getModelFromGroup(group);
		return groupModel;
	}

	@Override
	public String setGroup(GroupModel groupModel, Collection<UserModel> userModels) throws PresentationException {
		Group group = GroupConverter.getGroupFromModel(groupModel, userModels, userService);
		try {
			return groupService.setGroup(group, getSecurity()).toString();
		} catch (AppException ae) {
			throw PresentationExceptionUtils.getPresentationException(ae);
		}
	}

	@Override
	public void deleteGroup(GroupModel groupModel) throws PresentationException {
		Long groupId = Long.valueOf(groupModel.getId());
		try{
			Group group = groupService.getGroup(groupId, getSecurity());
			groupService.delete(group, getSecurity());
		}catch (AppException ae) {
			throw PresentationExceptionUtils.getPresentationException(ae);
		}
	}

	@Override
	public void move(ModelData itemToMove, ModelData destinationItem) throws PresentationException {
		
		SecurityManager userSecurity = getSecurity();
		
		try {
			if (itemToMove instanceof UserModel) {
				
				UserModel modelForUserToMove = (UserModel) itemToMove;
				Long idForUserToMove = Long.valueOf(modelForUserToMove.getUserId());
				
				if (destinationItem instanceof OrganizationUnitModel) {
					
					OrganizationUnitModel modelForDestinationOrganizationUnit = (OrganizationUnitModel) destinationItem;
					Long idForDestinationOrganizationUnit = Long.valueOf(modelForDestinationOrganizationUnit.getId());
					
					userService.moveUserToOrganizationUnit(idForUserToMove,
						idForDestinationOrganizationUnit, userSecurity);
				} else if (destinationItem instanceof OrganizationModel) {
					
					OrganizationModel modelForDestinationOrganization = (OrganizationModel) destinationItem;
					Long idForDestinationOrganization = Long.valueOf(modelForDestinationOrganization.getId());
					
					userService.moveUserToOrganization(idForUserToMove,
						idForDestinationOrganization, userSecurity);
				} else {
					String logMessage = "S-a incercat mutarea unui user intr-o entitate ne-suportata: [" + destinationItem + "].";
					LOGGER.error(logMessage, "move", userSecurity);
					throw new AppException();
				}
			} else if (itemToMove instanceof OrganizationUnitModel) {
				
				OrganizationUnitModel modelForOrganizationUnitToMove = (OrganizationUnitModel) itemToMove;
				Long idForOrganizationUnitToMove = Long.valueOf(modelForOrganizationUnitToMove.getId());
				
				if (destinationItem instanceof OrganizationUnitModel) {
					
					OrganizationUnitModel modelForDestinationOrganizationUnit = (OrganizationUnitModel) destinationItem;
					Long idForDestinationOrganizationUnit = Long.valueOf(modelForDestinationOrganizationUnit.getId());
					
					organizationUnitService.moveOrganizationUnitToOrganizationUnit(
						idForOrganizationUnitToMove, idForDestinationOrganizationUnit, userSecurity);
				} else if (destinationItem instanceof OrganizationModel) {
					
					OrganizationModel modelForDestinationOrganization = (OrganizationModel) destinationItem;
					Long idForDestinationOrganization = Long.valueOf(modelForDestinationOrganization.getId());
					
					organizationUnitService.moveOrganizationUnitToOrganization(
						idForOrganizationUnitToMove, idForDestinationOrganization, userSecurity);
				} else {
					String logMessage = "S-a incercat mutarea unei unitati organizatorice " +
						"intr-o entitate ne-suportata: [" + destinationItem + "].";
					LOGGER.error(logMessage, "move", userSecurity);
					throw new AppException();
				}
			} else {
				String logMessage = "S-a incercat mutarea unei entitati ne-suportate: [" + destinationItem + "].";
				LOGGER.error(logMessage, "move", userSecurity);
				throw new AppException();
			}
		} catch (AppException ae) {
			throw PresentationExceptionUtils.getPresentationException(ae);
		}
	}
	
	@Override
	public List<String> getAllUsernames() throws PresentationException {
		return userService.getAllUsernames();
	}
	
	@Override
	public List<UserModel> getUsersWithUsername(String username) throws PresentationException {
		
		List<User> users = userService.getUsersWithUsername(username);
		List<UserModel> userModels = Lists.newLinkedList();
		
		for (User user : users) {
			UserModel userModel = UserConverter.getModelFromUser(user);
			userModels.add(userModel);
		}
		
		return userModels;
	}
	
	@Override
	public List<DirectoryUserModel> findUsersInDirectory(GwtDirectoryUserSearchCriteria gwtDirectoryUserSearchCriteria) throws PresentationException {
		
		DirectoryUserSearchCriteria directoryUserSearchCriteria = DirectoryUserSearchCriteriaConverter.getFromGwt(gwtDirectoryUserSearchCriteria);
		
		List<DirectoryUser> foundUsers = directoryUserService.findUsers(directoryUserSearchCriteria);
		List<DirectoryUserModel> foundUserModels = Lists.newArrayList();
		
		for (DirectoryUser foundUser : foundUsers) {
			DirectoryUserModel foundUserModel = DirectoryUserConverter.getModelFromDirectoryUser(foundUser);
			foundUserModels.add(foundUserModel);
		}
		
		return foundUserModels;
	}
	
	@Override
	public void importOrganizationalStructureFromDirectory() throws PresentationException {
		try {
			organizationalStructureImportService.importOrganizationalStructureFromDirectory(getSecurity());
		} catch (AppException ae) {
			throw PresentationExceptionUtils.getPresentationException(ae);
		}
	}
	
	@Override
	public void deactivateUserWithId(Long userId, GwtUserDeactivationMode gwtDeactivationMode) throws PresentationException {
		UserDeactivationMode deactivationMode = UserDeactivationModeConverter.getFromGwt(gwtDeactivationMode);
		try {
			userService.deactivateUserWithId(userId, deactivationMode);
		} catch (AppException ae) {
			throw PresentationExceptionUtils.getPresentationException(ae);
		}
	}
	
	@Override
	public void reactivateUserWithId(Long userId) throws PresentationException {
		try {
			userService.reactivateUserWithId(userId, getSecurity());
		} catch (AppException ae) {
			throw PresentationExceptionUtils.getPresentationException(ae);
		}
	}
	
	@Override
	public List<String> getGroupNamesOfUserWithId(Long userId) {
		return groupService.getGroupNamesOfUserWithId(userId);
	}
	
}