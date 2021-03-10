package ro.cloudSoft.cloudDoc.helpers.replacementProfiles;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;

import org.springframework.beans.factory.InitializingBean;

import ro.cloudSoft.cloudDoc.dao.replacementProfiles.ReplacementProfileDao;
import ro.cloudSoft.cloudDoc.domain.organization.Group;
import ro.cloudSoft.cloudDoc.domain.organization.OrganizationEntity;
import ro.cloudSoft.cloudDoc.domain.organization.OrganizationUnit;
import ro.cloudSoft.cloudDoc.domain.organization.User;
import ro.cloudSoft.cloudDoc.domain.replacementProfiles.ReplacementProfile;
import ro.cloudSoft.cloudDoc.plugins.organization.GroupPersistencePlugin;
import ro.cloudSoft.cloudDoc.plugins.organization.OrganizationUnitPersistencePlugin;
import ro.cloudSoft.common.utils.DependencyInjectionUtils;

import com.google.common.collect.Maps;

/**
 * 
 */
public class GatherReplacementProfilesForEntitiesIncludingForReplacementsHelperImpl implements GatherReplacementProfilesForEntitiesIncludingForReplacementsHelper, InitializingBean {
	
	private ReplacementProfileDao replacementProfileDao;
	private OrganizationUnitPersistencePlugin organizationUnitPersistencePlugin;
	private GroupPersistencePlugin groupPersistencePlugin;
	
	@Override
	public void afterPropertiesSet() throws Exception {
		DependencyInjectionUtils.checkRequiredDependencies(
			replacementProfileDao,
			organizationUnitPersistencePlugin,
			groupPersistencePlugin
		);
	}
	
	private Map<OrganizationEntity, ReplacementProfile> getActiveReplacementProfilesForUserIncludingForReplacements(Long userId, OrganizationEntity entityToPutInsteadOfUserAsRequester) {
		
		ReplacementProfile replacementProfileForUser = replacementProfileDao.getActiveReplacementProfileForUser(userId);
		if (replacementProfileForUser == null) {
			return Collections.emptyMap();
		}
		
		Map<OrganizationEntity, ReplacementProfile> replacementProfiles = Maps.newHashMap();

		User user = replacementProfileForUser.getSelectedUserProfileWithId(userId);
		OrganizationEntity requesterEntityForUser = user;
		if (entityToPutInsteadOfUserAsRequester != null) {
			requesterEntityForUser = entityToPutInsteadOfUserAsRequester;
		}
		replacementProfiles.put(requesterEntityForUser, replacementProfileForUser);
		
		ReplacementProfile replacementProfile = replacementProfileForUser;
		
		do {
			
			ReplacementProfile replacementProfileWhereReplacementIsRequester = replacementProfileDao.getActiveReplacementProfileWhereReplacementIsRequester(replacementProfile);
			if (replacementProfileWhereReplacementIsRequester == null) {
				break;
			}
			
			replacementProfiles.put(replacementProfile.getReplacement(), replacementProfileWhereReplacementIsRequester);
			
			replacementProfile = replacementProfileWhereReplacementIsRequester;
		} while (true);
		
		return replacementProfiles;
	}
	
	@Override
	public Map<OrganizationEntity, ReplacementProfile> getActiveReplacementProfilesForUserIncludingForReplacements(Long userId) {
		return getActiveReplacementProfilesForUserIncludingForReplacements(userId, null);
	}

	@Override
	public Map<OrganizationEntity, ReplacementProfile> getActiveReplacementProfileForOrganizationUnitIncludingForReplacements(Long organizationUnitId) {
		
		OrganizationUnit organizationUnit = organizationUnitPersistencePlugin.getOrganizationUnitById(organizationUnitId);
		if (organizationUnit == null) {
			throw new IllegalArgumentException("Nu s-a gasit unitatea organizatorica cu ID-ul [" + organizationUnitId + "].");
		}
		
		Collection<Long> idsForUsersInOrganizationUnit = organizationUnitPersistencePlugin.getIdsForUsersInOrganizationUnit(organizationUnitId);
		Map<OrganizationEntity, ReplacementProfile> replacementProfiles = Maps.newHashMap();
		
		for (Long idForUserInOrganizationUnit : idsForUsersInOrganizationUnit) {
			Map<OrganizationEntity, ReplacementProfile> replacementProfilesForUserInOrganizationUnit = getActiveReplacementProfilesForUserIncludingForReplacements(idForUserInOrganizationUnit, organizationUnit);
			replacementProfiles.putAll(replacementProfilesForUserInOrganizationUnit);
		}
		
		return replacementProfiles;
	}
	
	@Override
	public Map<OrganizationEntity, ReplacementProfile> getActiveReplacementProfileForGroupIncludingForReplacements(Long groupId) {
		
		Group group = groupPersistencePlugin.getGroupById(groupId);
		if (group == null) {
			throw new IllegalArgumentException("Nu s-a gasit grupul cu ID-ul [" + groupId + "].");
		}
		
		Collection<Long> idsForUsersInGroup = groupPersistencePlugin.getIdsForUsersInGroup(groupId);
		Map<OrganizationEntity, ReplacementProfile> replacementProfiles = Maps.newHashMap();
		
		for (Long idForUserInGroup : idsForUsersInGroup) {
			Map<OrganizationEntity, ReplacementProfile> replacementProfilesForUserInOrganizationUnit = getActiveReplacementProfilesForUserIncludingForReplacements(idForUserInGroup, group);
			replacementProfiles.putAll(replacementProfilesForUserInOrganizationUnit);
		}
		
		return replacementProfiles;
	}

	public void setReplacementProfileDao(ReplacementProfileDao replacementProfileDao) {
		this.replacementProfileDao = replacementProfileDao;
	}
	public void setOrganizationUnitPersistencePlugin(OrganizationUnitPersistencePlugin organizationUnitPersistencePlugin) {
		this.organizationUnitPersistencePlugin = organizationUnitPersistencePlugin;
	}
	public void setGroupPersistencePlugin(GroupPersistencePlugin groupPersistencePlugin) {
		this.groupPersistencePlugin = groupPersistencePlugin;
	}
}