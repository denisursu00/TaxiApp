package ro.cloudSoft.cloudDoc.web.rest.resources;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import ro.cloudSoft.cloudDoc.core.AppException;
import ro.cloudSoft.cloudDoc.domain.organization.tree.OrganizationNode;
import ro.cloudSoft.cloudDoc.domain.organization.tree.OrganizationTree;
import ro.cloudSoft.cloudDoc.dto.organization.OrganizationTreeDTO;
import ro.cloudSoft.cloudDoc.dto.organization.OrganizationTreeNodeDTO;
import ro.cloudSoft.cloudDoc.dto.organization.OrganizationTreeNodeDTO.Type;
import ro.cloudSoft.cloudDoc.presentation.client.shared.exceptions.PresentationException;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.OrganizationEntityModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.directory.organization.DirectoryUserModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.directory.organization.GwtDirectoryUserSearchCriteria;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.organization.DeactivationUserModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.organization.GroupModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.organization.OrganizationUnitModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.organization.RoleModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.organization.RolePermissionMappingViewModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.organization.UserModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.services.OrganizationGxtService;
import ro.cloudSoft.cloudDoc.presentation.client.shared.sorters.UsersByActiveStatusComparator;
import ro.cloudSoft.cloudDoc.services.organization.OrganizationService;
import ro.cloudSoft.cloudDoc.services.organization.OrganizationUnitService;
import ro.cloudSoft.cloudDoc.services.organization.RoleService;
import ro.cloudSoft.cloudDoc.services.organization.UserService;

@Component
@Path("/OrganizationGxtService")
@Consumes({MediaType.APPLICATION_JSON})
@Produces({MediaType.APPLICATION_JSON})
public class OrganizationGxtServiceResource extends BaseResource {
	
	@Autowired
	private OrganizationService organizationService;

	@Autowired
	private OrganizationGxtService organizationGxtService;
	
	@Autowired
	private OrganizationUnitService organizationUnitService;

	@Autowired
	private UserService userService;
	
	@Autowired
	public RoleService roleService;
	
	@POST
	@Path("/getOrganizationTree")
	public OrganizationTreeDTO getOrganization() {
		
		OrganizationTree organizationTree = organizationService.load(getSecurity());
		OrganizationNode root = organizationTree.getRootElement();
		
		OrganizationTreeDTO organizationTreeDTO = new OrganizationTreeDTO();
		
		OrganizationTreeNodeDTO organizationRootNode = new OrganizationTreeNodeDTO();
		organizationRootNode.setId(root.getId());
		organizationRootNode.setName(root.getName());
		organizationRootNode.setType(Type.ORGANIZATION);
		if (root.getManagerId() != null) {
			organizationRootNode.setManagerId(String.valueOf(root.getManagerId()));
		}
		organizationRootNode.setChildren(getNodeChildren(root));
		organizationTreeDTO.setRootNode(organizationRootNode);
		
		return organizationTreeDTO;
	}
	
	private List<OrganizationTreeNodeDTO> getNodeChildren(OrganizationNode organizationNode) {
		List<OrganizationTreeNodeDTO> children = new ArrayList<OrganizationTreeNodeDTO>();
		for (OrganizationNode node : organizationNode.getChildren()) {
			
			OrganizationTreeNodeDTO organizationTreeNode = new OrganizationTreeNodeDTO();
			organizationTreeNode.setParentId(organizationNode.getId());
			organizationTreeNode.setId(node.getId());
			organizationTreeNode.setName(node.getName());
			
			if (node.getType() == 1) {
				organizationTreeNode.setType(Type.ORGANIZATION_UNIT);
				if (node.getManagerId() != null) {
					organizationTreeNode.setManagerId(String.valueOf(node.getManagerId()));
				}
				organizationTreeNode.setChildren(getNodeChildren(node));
			} else if (node.getType() == 2) {
				organizationTreeNode.setType(Type.USER);
				organizationTreeNode.setTitle(node.getTitle());
				organizationTreeNode.setCustomTitleTemplate(node.getCustomTitleTemplate());
			}
			
			children.add(organizationTreeNode);
		}
		return children;
	}
	
	@POST
	@Path("/getGroups")
	public List<GroupModel> getGroups() {
		return organizationGxtService.getGroups();
	}
	
	@POST
	@Path("/getSortedUsers")
	public List<UserModel> getSortedUsers() {
		List<UserModel> users = organizationGxtService.getUsers();
		Set<Long> idsOfActiveUsers = organizationGxtService.getIdsOfActiveUsers();
		Collections.sort(users, new UsersByActiveStatusComparator(idsOfActiveUsers));
		return users;
	}
	
	@POST
	@Path("/getAllUsernames")
	public List<String> getAllUsernames() throws PresentationException {
		return organizationGxtService.getAllUsernames();
	}

	@POST
	@Path("/getUsersWithUsername/{username}")
	public List<UserModel> getUsersWithUsername(@PathParam("username") String username) throws PresentationException {
		return organizationGxtService.getUsersWithUsername(username);
	}

	@POST
	@Path("/getUsers")
	public List<UserModel> getUsers() {
		return this.organizationGxtService.getUsers();
	}
	
	@POST
	@Path("/getAllActiveUsers")
	public List<UserModel> getAllActiveUsers() {
		return this.organizationGxtService.getAllActiveUsers();
	}
	
	@POST
	@Path("/getUsersFromGroup/{groupId}")
	public List<UserModel> getUsersFromGroup(@PathParam("groupId") String groupId) {
		List<UserModel> usersFromGroup = organizationGxtService.getUsersFromGroup(groupId);
		Set<Long> idsOfActiveUsers = organizationGxtService.getIdsOfActiveUsers();
		Collections.sort(usersFromGroup, new UsersByActiveStatusComparator(idsOfActiveUsers));
		return usersFromGroup;
	}
	
	@POST
	@Path("/getUsersFromGroupByGroupName/{groupName}")
	public List<UserModel> getUsersFromGroupByGroupName(@PathParam("groupName") String groupName) {
		List<UserModel> usersFromGroup = organizationGxtService.getUsersFromGroupByGroupName(groupName);
		Set<Long> idsOfActiveUsers = organizationGxtService.getIdsOfActiveUsers();
		Collections.sort(usersFromGroup, new UsersByActiveStatusComparator(idsOfActiveUsers));
		return usersFromGroup;
	}

	@POST
	@Path("/getUserById/{id}")
	public UserModel getUserById(@PathParam("id") String id) {
		return this.organizationGxtService.getUserById(id);
	}

	@POST
	@Path("/setUser")
	public void setUser(UserModel userModel) throws PresentationException {
		this.organizationGxtService.setUser(userModel);
	}
	
	
	@POST
	@Path("/getOrgUnitById/{id}")
	public OrganizationUnitModel getOrgUnitById(@PathParam("id") String id) throws PresentationException {
		return this.organizationGxtService.getOrgUnitById(id);
	}

	@POST
	@Path("/setOrgUnit")
	public void setOrgUnit(OrganizationUnitModel organizationUnitModel) throws PresentationException {
		this.organizationGxtService.setOrgUnit(organizationUnitModel);
	}

	@POST
	@Path("/getUsersFromOrgUnit/{id}")
	public List<UserModel> getUsersFromOrgUnit(@PathParam("id") String id) throws PresentationException {
		return this.organizationGxtService.getUsersFromOrgUnit(id);
	}

	@POST
	@Path("/findUsersInDirectory")
	public List<DirectoryUserModel> findUsersInDirectory(GwtDirectoryUserSearchCriteria searchCriteria) throws PresentationException {
		return this.organizationGxtService.findUsersInDirectory(searchCriteria);
	}

	@POST
	@Path("/deleteOrgUnit")
	public void deleteOrgUnit(OrganizationUnitModel orgUnitModel) throws PresentationException {
		this.organizationGxtService.deleteOrgUnit(orgUnitModel);
	}

	@POST
	@Path("/deleteUser")
	public void deleteUser(UserModel userModel) throws PresentationException {
		this.organizationGxtService.deleteUser(userModel);
	}

	@POST
	@Path("/importOrganizationalStructureFromDirectory")
	public void importOrganizationalStructureFromDirectory() throws PresentationException {
		this.organizationGxtService.importOrganizationalStructureFromDirectory();
	}

	@POST
	@Path("/deactivateUser")
	public void deactivateUser(DeactivationUserModel deactivationUserModel) throws PresentationException {
		this.organizationGxtService.deactivateUserWithId(Long.parseLong(deactivationUserModel.getUserId()), deactivationUserModel.getDeactivationMode());
	}

	@POST
	@Path("/reactivateUserWithId/{id}")
	public void reactivateUserWithId(@PathParam("id") Long id) throws PresentationException {
		this.organizationGxtService.reactivateUserWithId(id);
	}

	@POST
	@Path("/moveUserToOrganization/{idOfUserToMove}/{idOfOrganizationToMoveTo}")
	public void moveUserToOrganization(@PathParam("idOfUserToMove") String idOfUserToMove, @PathParam("idOfOrganizationToMoveTo") String idOfOrganizationToMoveTo) 
			throws PresentationException, NumberFormatException, AppException {
		this.userService.moveUserToOrganization(Long.parseLong(idOfUserToMove), Long.parseLong(idOfOrganizationToMoveTo), getSecurity());
	}

	@POST
	@Path("/moveUserToOrganizationUnit/{idOfUserToMove}/{idOfOrganizationUnitToMoveTo}")
	public void moveUserToOrganizationUnit(@PathParam("idOfUserToMove") String idOfUserToMove, @PathParam("idOfOrganizationUnitToMoveTo") String idOfOrganizationUnitToMoveTo) 
			throws PresentationException, NumberFormatException, AppException {
		this.userService.moveUserToOrganizationUnit(Long.parseLong(idOfUserToMove), Long.parseLong(idOfOrganizationUnitToMoveTo), getSecurity());
	}

	@POST
	@Path("/moveOrganizationUnitToOrganization/{idOfOrganizationUnitToMove}/{idOfOrganizationToMoveTo}")
	public void moveOrganizationUnitToOrganization(@PathParam("idOfOrganizationUnitToMove") String idOfOrganizationUnitToMove, 
			@PathParam("idOfOrganizationToMoveTo") String idOfOrganizationToMoveTo) throws PresentationException, NumberFormatException, AppException {
		this.organizationUnitService.moveOrganizationUnitToOrganization(Long.parseLong(idOfOrganizationUnitToMove), Long.parseLong(idOfOrganizationToMoveTo), getSecurity());
	}

	@POST
	@Path("/moveOrganizationUnitToOrganizationUnit/{idOfOrganizationUnitToMove}/{idOfOrganizationUnitToMoveTo}")
	public void moveOrganizationUnitToOrganizationUnit(@PathParam("idOfOrganizationUnitToMove") String idOfOrganizationUnitToMove, 
			@PathParam("idOfOrganizationUnitToMoveTo") String idOfOrganizationUnitToMoveTo) throws PresentationException, NumberFormatException, AppException {
		this.organizationUnitService.moveOrganizationUnitToOrganizationUnit(Long.parseLong(idOfOrganizationUnitToMove), Long.parseLong(idOfOrganizationUnitToMoveTo), getSecurity());
	}
	
	@POST
	@Path("/getNamesForOrganizationEntities")
	public List<String> getNamesForOrganizationEntities(Collection<OrganizationEntityModel> organizationEntities) {

		List<String> namesForOrganizationEntities = new ArrayList<String>();
		
		for (OrganizationEntityModel organizationEntity : organizationEntities) {
			
			String nameForOrganizationEntity = null;
			if (organizationEntity.getType().equals(OrganizationEntityModel.TYPE_USER)) {
				nameForOrganizationEntity = this.organizationGxtService.getUserById(organizationEntity.getId().toString()).getDisplayName();
			} else if (organizationEntity.getType().equals(OrganizationEntityModel.TYPE_ORG_UNIT)) {
				nameForOrganizationEntity = this.organizationGxtService.getOrgUnitById(organizationEntity.getId().toString()).getDisplayName();
			} else if (organizationEntity.getType().equals(OrganizationEntityModel.TYPE_GROUP)) {
				nameForOrganizationEntity = this.organizationGxtService.getGroupById(organizationEntity.getId().toString()).getName();
			} else {
				throw new IllegalArgumentException("Tip necunoscut de entitate organizatorica: [" + organizationEntity.getType() + "]");
			}
			
			namesForOrganizationEntities.add(nameForOrganizationEntity);
		}
		
		return namesForOrganizationEntities;
	}

	@POST
	@Path("/deleteGroup")
	public void deleteGroup(GroupModel group) throws PresentationException {
		this.organizationGxtService.deleteGroup(group);
	}

	@POST
	@Path("/getGroupById/{groupId}")
	public GroupModel getGroupById(@PathParam("groupId") String groupId) throws PresentationException {
		return this.organizationGxtService.getGroupById(groupId);
	}

	@POST
	@Path("/setGroup")
	public void setGroup(GroupModel groupModel) throws PresentationException {
		this.organizationGxtService.setGroup(groupModel, groupModel.getUsers());
	}
	
	@POST
	@Path("/getAllRoles")
	public List<RoleModel> getAllRoles() {
		return roleService.getAllRoles();
	}
	
	@POST
	@Path("/getAvailableRolesForUser/{userId}")
	public List<RoleModel> getAvailableRolesForUser(@PathParam("userId") String userId) {
		return roleService.getAvailableRolesForUser(Long.valueOf(userId));
	}
	
	@POST
	@Path("/getAllUsersWithAssignedTasks")
	public List<UserModel> getAllUsersWithAssignedTasks() {
		return userService.getAllUsersWithAssignedTasks();
	}
	
	@POST
	@Path("/getAllRolePermissionMappingViews")
	public List<RolePermissionMappingViewModel> getAllRolePermissionMappingViews() {
		return roleService.getAllRolePermissionMappingViews();
	}
}
