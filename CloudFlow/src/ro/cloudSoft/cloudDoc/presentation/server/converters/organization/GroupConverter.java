package ro.cloudSoft.cloudDoc.presentation.server.converters.organization;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import ro.cloudSoft.cloudDoc.domain.organization.Group;
import ro.cloudSoft.cloudDoc.domain.organization.User;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.organization.GroupModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.organization.UserModel;
import ro.cloudSoft.cloudDoc.services.organization.UserService;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

public class GroupConverter {

	/**
	 * Converteste grupul in modelul echivalent.
	 * Daca grupul este null, atunci va returna null.
	 */
	public static GroupModel getModelFromGroup(Group group) {
		
		if (group == null) {
			return null;
		}
		
    	GroupModel groupModel = new GroupModel();
    	
    	groupModel.setId(group.getId().toString());
    	groupModel.setName(group.getName());
    	groupModel.setDescription(group.getDescription());
    	
    	List<UserModel> userModels = Lists.newArrayList();
    	for (User user : group.getUsers()) {
    		UserModel userModel = UserConverter.getModelFromUser(user);
    		userModels.add(userModel);
    	}
    	groupModel.setUsers(userModels);
    	
    	return groupModel;
    }
	
    public static Group getGroupFromModel(GroupModel groupModel,
    		Collection<UserModel> userModels, UserService userService) {
    	
        Group group = new Group(); 
        
        if (groupModel.getId() != null) {
        	group.setId(Long.valueOf(groupModel.getId()));
        }
        
        group.setName(groupModel.getName());
        group.setDescription(groupModel.getDescription()); 
        
        if (!userModels.isEmpty()) {
	        Set<Long> userIds = Sets.newHashSet();
	        for (UserModel userModel : userModels) {
	        	Long userId = Long.valueOf(userModel.getUserId());
	        	userIds.add(userId);
	        }
	        List<User> users = userService.getUsersWithIds(userIds);
	        group.setUsers(Sets.newHashSet(users));
        } else {
        	group.setUsers(Collections.<User> emptySet());
        }
        
        return group;
    }
}