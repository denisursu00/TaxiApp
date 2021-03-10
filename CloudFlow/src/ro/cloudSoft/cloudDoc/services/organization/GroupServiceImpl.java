package ro.cloudSoft.cloudDoc.services.organization;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.InitializingBean;

import ro.cloudSoft.cloudDoc.core.AppException;
import ro.cloudSoft.cloudDoc.core.AppExceptionCodes;
import ro.cloudSoft.cloudDoc.domain.audit.AuditEntityOperation;
import ro.cloudSoft.cloudDoc.domain.organization.Group;
import ro.cloudSoft.cloudDoc.domain.organization.User;
import ro.cloudSoft.cloudDoc.domain.security.SecurityManager;
import ro.cloudSoft.cloudDoc.plugins.organization.GroupPersistencePlugin;
import ro.cloudSoft.cloudDoc.services.AuditService;
import ro.cloudSoft.common.utils.DependencyInjectionUtils;

/**
 *
 * 
 */
public class GroupServiceImpl implements GroupService, InitializingBean
{
	
	private AuditService auditService;
	
	private GroupPersistencePlugin groupPersistencePlugin;
	
    private List<String> restrictedSecurityGroups;
	
	@Override
	public void afterPropertiesSet() throws Exception {		
		DependencyInjectionUtils.checkRequiredDependencies(
				
			auditService,
			
			groupPersistencePlugin,
			
			restrictedSecurityGroups
		);
	}

    @Override
    public Group getGroup(Long id, SecurityManager userSecurity) {
        Group group = groupPersistencePlugin.getGroup(id, userSecurity);
        auditService.auditGroupOperation(userSecurity, group, AuditEntityOperation.READ);
        return group;
    }
    
    @Override
    public Group getGroupById(Long id) {
    	return groupPersistencePlugin.getGroupById(id);
    }
    
    @Override
    public Group getGroupByName(String groupName) {
    	return groupPersistencePlugin.getGroupByName(groupName);
    }

    @Override
    public Long setGroup(Group group, SecurityManager userSecurity) throws AppException {
    	
    	boolean isAddOperation = (group.getId() == null);
    	Long id = groupPersistencePlugin.setGroup(group);
    	
    	AuditEntityOperation operation = (isAddOperation) ? AuditEntityOperation.ADD : AuditEntityOperation.MODIFY;
    	auditService.auditGroupOperation(userSecurity, group, operation);
    	
    	return id;
    }
    
    @Override
    public void updateGroups(Collection<Group> groups) {
    	groupPersistencePlugin.updateGroups(groups);
    }

    @Override
    public ArrayList<Group> getGroups( SecurityManager userSecurity)
    {
        return groupPersistencePlugin.getGroups(userSecurity);
    }

    @Override
	public void delete(Group theGroup, SecurityManager userSecurity) throws AppException {
	
		if (existGroupNameInRestrictedSecurityGroups(theGroup.getName())) {
			 throw new AppException(AppExceptionCodes.CANNOT_DELETE_GROUP_DUE_TO_SECURITY);
		}
			 
		groupPersistencePlugin.delete(theGroup, userSecurity);
		auditService.auditGroupOperation(userSecurity, theGroup, AuditEntityOperation.DELETE); 
	}

    @Override
    public List<User> getAllUsersWithGroup(Long roleId, SecurityManager userSecurity)
    {
        return groupPersistencePlugin.getAllUsersWithGroup(roleId, userSecurity);
    }

    /**
     * Verifica daca un un nume de grup exista in lista de grupuri restrictionate
     * din cauza securitatii.
     * @param goupName
     * @return
     */
    private boolean existGroupNameInRestrictedSecurityGroups(String goupName){
    	if (restrictedSecurityGroups == null)
    		return false;
    	else{
    		for(String group : restrictedSecurityGroups){
    			if (group.equalsIgnoreCase(goupName))
    				return true;
    		}
    	}
    	return false;
    }
    
    @Override
    public List<String> getEmails(Collection<Long> groupIds) {
    	return this.groupPersistencePlugin.getEmails(groupIds);
    }
    
    @Override
    public boolean groupExists(Long groupId) {
    	return groupPersistencePlugin.groupExists(groupId);
    }
    
    @Override
    public boolean groupWithNameExists(String groupName) {
    	Group groupWithName = getGroupByName(groupName);
    	return (groupWithName != null);
    }
    
    @Override
    public List<String> getGroupNamesOfUserWithId(Long userId) {
    	return groupPersistencePlugin.getGroupNamesOfUserWithId(userId);
    }
    
    @Override
    public boolean isUserInGroupWithName(Long userId, String groupName) {
    	return groupPersistencePlugin.isUserInGroupWithName(userId, groupName);
    }
    
    @Override
    public boolean isUserInGroupWithId(Long userId, Long groupId) {
    	return groupPersistencePlugin.isUserInGroupWithId(userId, groupId);
    }
    
    public void setAuditService(AuditService auditService) {
		this.auditService = auditService;
	}
    public void setGroupPersistencePlugin(GroupPersistencePlugin groupPersistencePlugin) {
		this.groupPersistencePlugin = groupPersistencePlugin;
	}
    public void setRestrictedSecurityGroups(List<String> restrictedSecurityGroups) {
		this.restrictedSecurityGroups = restrictedSecurityGroups;
	}

}