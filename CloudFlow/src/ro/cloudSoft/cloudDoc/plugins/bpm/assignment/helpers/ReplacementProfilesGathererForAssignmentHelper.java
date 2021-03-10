package ro.cloudSoft.cloudDoc.plugins.bpm.assignment.helpers;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import ro.cloudSoft.cloudDoc.core.AppException;
import ro.cloudSoft.cloudDoc.domain.organization.OrganizationEntity;
import ro.cloudSoft.cloudDoc.domain.organization.User;
import ro.cloudSoft.cloudDoc.domain.replacementProfiles.ReplacementProfile;
import ro.cloudSoft.cloudDoc.domain.replacementProfiles.ReplacementProfileInstanceItem;
import ro.cloudSoft.cloudDoc.helpers.replacementProfiles.GatherReplacementProfilesForEntitiesIncludingForReplacementsHelper;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

/**
 * Aduna profile de inlocuire active pentru a fi folosite in asignari.
 * 
 * 
 */
public class ReplacementProfilesGathererForAssignmentHelper {
	

	private final Collection<Long> idsForUsers;
	private final Collection<Long> idsForOrganizationUnits;
	private final Collection<Long> idsForGroups;
	
	private final GatherReplacementProfilesForEntitiesIncludingForReplacementsHelper gatherReplacementProfilesForEntitiesIncludingForReplacementsHelper;
	
	
	private Set<Long> idsForReplacementUsers;
	private List<ReplacementProfileInstanceItem> replacementProfileInstanceItems;
	
	
	public ReplacementProfilesGathererForAssignmentHelper(Collection<Long> idsForUsers, Collection<Long> idsForOrganizationUnits, Collection<Long> idsForGroups,
			GatherReplacementProfilesForEntitiesIncludingForReplacementsHelper gatherReplacementProfilesForEntitiesIncludingForReplacementsHelper) {
		
		this.idsForUsers = idsForUsers;
		this.idsForOrganizationUnits = idsForOrganizationUnits;
		this.idsForGroups = idsForGroups;
		
		this.gatherReplacementProfilesForEntitiesIncludingForReplacementsHelper = gatherReplacementProfilesForEntitiesIncludingForReplacementsHelper;
	}

	private void addReplacementProfileInstanceItem(OrganizationEntity originallyAssignedEntity, User replacement, ReplacementProfile replacementProfile) {
		
		ReplacementProfileInstanceItem replacementProfileInstanceItem = new ReplacementProfileInstanceItem();
		
		replacementProfileInstanceItem.setOriginallyAssignedEntity(originallyAssignedEntity);
		replacementProfileInstanceItem.setReplacementUsed(replacement);
		replacementProfileInstanceItem.setReplacementProfileUsed(replacementProfile);
		
		replacementProfileInstanceItems.add(replacementProfileInstanceItem);
	}
	
	private void processReplacementProfile(OrganizationEntity originallyAssignedEntity, ReplacementProfile replacementProfile) {
		
		User replacementUser = replacementProfile.getReplacement();
		Long idForReplacementUser = replacementUser.getId();
		
		if (idsForUsers.contains(idForReplacementUser)) {
			// Inlocuitorul era asignat deja de la inceput.
			return;
		}
		
		idsForReplacementUsers.add(idForReplacementUser);
		
		addReplacementProfileInstanceItem(originallyAssignedEntity, replacementUser, replacementProfile);
	}
	
	private void gatherReplacementsByReplacementProfilesForUsers() throws AppException {
		for (Long idForAssignedUser : idsForUsers) {
			Map<OrganizationEntity, ReplacementProfile> replacementProfilesForAssignedUserIncludingForReplacements = gatherReplacementProfilesForEntitiesIncludingForReplacementsHelper.getActiveReplacementProfilesForUserIncludingForReplacements(idForAssignedUser);
			for (Entry<OrganizationEntity, ReplacementProfile> entry : replacementProfilesForAssignedUserIncludingForReplacements.entrySet()) {
				
				OrganizationEntity originallyAssignedUserOrReplacement = entry.getKey();
				ReplacementProfile replacementProfileForAssignedUserOrReplacement = entry.getValue();
				
				processReplacementProfile(originallyAssignedUserOrReplacement, replacementProfileForAssignedUserOrReplacement);
			}
		}
	}
	
	private void gatherReplacementsByReplacementProfilesForOrganizationUnits() throws AppException {
		for (Long idForAssignedOrganizationUnit : idsForOrganizationUnits) {
			Map<OrganizationEntity, ReplacementProfile> replacementProfilesForAssignedOrganizationUnitIncludingForReplacements = gatherReplacementProfilesForEntitiesIncludingForReplacementsHelper.getActiveReplacementProfileForOrganizationUnitIncludingForReplacements(idForAssignedOrganizationUnit);
			for (Entry<OrganizationEntity, ReplacementProfile> entry : replacementProfilesForAssignedOrganizationUnitIncludingForReplacements.entrySet()) {
				
				OrganizationEntity originallyAssignedOrganizationUnitOrReplacement = entry.getKey();
				ReplacementProfile replacementProfileForAssignedOrganizationUnitOrReplacement = entry.getValue();
				
				processReplacementProfile(originallyAssignedOrganizationUnitOrReplacement, replacementProfileForAssignedOrganizationUnitOrReplacement);
			}
		}
	}
	
	private void gatherReplacementsByReplacementProfilesForGroups() throws AppException {
		for (Long idForAssignedGroup : idsForGroups) {
			Map<OrganizationEntity, ReplacementProfile> replacementProfilesForAssignedGroupIncludingForReplacements = gatherReplacementProfilesForEntitiesIncludingForReplacementsHelper.getActiveReplacementProfileForGroupIncludingForReplacements(idForAssignedGroup);
			for (Entry<OrganizationEntity, ReplacementProfile> entry : replacementProfilesForAssignedGroupIncludingForReplacements.entrySet()) {
				
				OrganizationEntity originallyAssignedGroupOrReplacement = entry.getKey();
				ReplacementProfile replacementProfileForAssignedGroupOrReplacement = entry.getValue();
				
				processReplacementProfile(originallyAssignedGroupOrReplacement, replacementProfileForAssignedGroupOrReplacement);
			}
		}
	}
	
	private void init() {
		idsForReplacementUsers = Sets.newHashSet();
		replacementProfileInstanceItems = Lists.newArrayList();
	}
	
	public void gatherReplacementsByReplacementProfiles() throws AppException {
		init();
		gatherReplacementsByReplacementProfilesForUsers();
		gatherReplacementsByReplacementProfilesForOrganizationUnits();
		gatherReplacementsByReplacementProfilesForGroups();
	}
	
	private IllegalStateException getUninitializedException() {
		return new IllegalStateException("Nu s-a rulat obtinerea inlocuitorilor.");
	}
	
	public Set<Long> getIdsForReplacementUsers() {
		if (idsForReplacementUsers == null) {
			throw getUninitializedException();
		}
		return idsForReplacementUsers;
	}
	
	public List<ReplacementProfileInstanceItem> getReplacementProfileInstanceItems() {
		if (replacementProfileInstanceItems == null) {
			throw getUninitializedException();
		}
		return replacementProfileInstanceItems;
	}
}