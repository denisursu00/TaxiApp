package ro.cloudSoft.cloudDoc.services.replacementProfiles;

import java.util.Collection;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.transaction.annotation.Transactional;

import ro.cloudSoft.cloudDoc.dao.replacementProfiles.ReplacementProfileInstanceDao;
import ro.cloudSoft.cloudDoc.dao.replacementProfiles.ReplacementProfileInstanceItemDao;
import ro.cloudSoft.cloudDoc.domain.replacementProfiles.ReplacementProfile;
import ro.cloudSoft.cloudDoc.domain.replacementProfiles.ReplacementProfileInstance;
import ro.cloudSoft.cloudDoc.domain.replacementProfiles.ReplacementProfileInstanceItem;
import ro.cloudSoft.common.utils.DependencyInjectionUtils;

import com.google.common.base.Objects;
import com.google.common.collect.Lists;

/**
 * 
 */
public class ReplacementProfileInstanceServiceImpl implements ReplacementProfileInstanceService, InitializingBean {

	private ReplacementProfileInstanceDao replacementProfileInstanceDao;
	private ReplacementProfileInstanceItemDao replacementProfileInstanceItemDao;
	
	@Override
	public void afterPropertiesSet() throws Exception {
		DependencyInjectionUtils.checkRequiredDependencies(
			replacementProfileInstanceDao,
			replacementProfileInstanceItemDao
		);
	}
	
	@Override
	@Transactional(rollbackFor = Throwable.class)
	public void setReplacementProfileInstanceForDocument(ReplacementProfileInstance replacementProfileInstance) {
		
		String documentLocationRealName = replacementProfileInstance.getDocumentLocationRealName();
		String documentId = replacementProfileInstance.getDocumentId();
		
		// Inlatur instantele vechi pentru document.
		replacementProfileInstanceDao.deleteProfileInstancesForDocument(documentLocationRealName, documentId);

		if (!replacementProfileInstance.getItems().isEmpty()) {
			replacementProfileInstanceDao.createNewProfileInstance(replacementProfileInstance);
		}
	}
	
	@Override
	@Transactional(rollbackFor = Throwable.class)
	public void addReplacementProfileInstanceItemsForDocument(String documentLocationRealName, String documentId,
			Collection<ReplacementProfileInstanceItem> replacementProfileInstanceItemsToAdd) {
		
		ReplacementProfileInstance replacementProfileInstance = replacementProfileInstanceDao.getProfileInstanceForDocument(documentLocationRealName, documentId);
		if (replacementProfileInstance == null) {
			replacementProfileInstance = new ReplacementProfileInstance();
			replacementProfileInstance.setDocumentIdentifiers(documentLocationRealName, documentId);
		}
		
		Collection<ReplacementProfileInstanceItem> nonExistingReplacementProfileInstanceItemsToAdd = Lists.newLinkedList();
		
		replacementProfileInstanceItemsToAddIteration:
		for (ReplacementProfileInstanceItem replacementProfileInstanceItemToAdd : replacementProfileInstanceItemsToAdd) {
			for (ReplacementProfileInstanceItem existingReplacementProfileInstanceItem : replacementProfileInstance.getItems()) {
				if (isSameReplacementProfileInstanceItem(replacementProfileInstanceItemToAdd, existingReplacementProfileInstanceItem)) {
					continue replacementProfileInstanceItemsToAddIteration;
				}
			}
			nonExistingReplacementProfileInstanceItemsToAdd.add(replacementProfileInstanceItemToAdd);
		}
		
		if (nonExistingReplacementProfileInstanceItemsToAdd.isEmpty()) {
			return;
		}
		
		replacementProfileInstance.getItems().addAll(nonExistingReplacementProfileInstanceItemsToAdd);
		replacementProfileInstanceDao.createOrUpdateProfileInstance(replacementProfileInstance);
	}
	
	@Override
	public void removeReplacementProfileInstancesForDocument(String documentLocationRealName, String documentId) {
		replacementProfileInstanceDao.deleteProfileInstancesForDocument(documentLocationRealName, documentId);
	}
	
	@Override
	public Collection<ReplacementProfileInstanceItem> getReplacementProfileInstanceItemsForProfile(ReplacementProfile replacementProfile) {
		return replacementProfileInstanceItemDao.getInstanceItemsForProfile(replacementProfile);
	}
	
	@Override
	@Transactional(rollbackFor = Throwable.class)
	public void deleteReplacementProfileInstanceItems(Collection<ReplacementProfileInstanceItem> items) {
		replacementProfileInstanceItemDao.deleteProfileInstanceItems(items);
		replacementProfileInstanceDao.deleteProfileInstancesWithNoItems();
	}
	


	@Override
	public ReplacementProfileInstanceItem getReplacementProfileInstanceItemForReplacement(
			String documentLocationRealName, String documentId, Long replacementUserId) {
		
		return replacementProfileInstanceItemDao.getReplacementProfileInstanceItemForReplacement(
			documentLocationRealName, documentId, replacementUserId);
	}
	
	private boolean isSameReplacementProfileInstanceItem(ReplacementProfileInstanceItem item1, ReplacementProfileInstanceItem item2) {
		return (
			Objects.equal(item1.getOriginallyAssignedEntity().getClass(), item2.getOriginallyAssignedEntity().getClass()) &&
			Objects.equal(item1.getOriginallyAssignedEntity().getId(), item2.getOriginallyAssignedEntity().getId()) &&
			Objects.equal(item1.getReplacementUsed().getId(), item2.getReplacementUsed().getId())
		);
	}
	
	public void setReplacementProfileInstanceDao(ReplacementProfileInstanceDao replacementProfileInstanceDao) {
		this.replacementProfileInstanceDao = replacementProfileInstanceDao;
	}
	public void setReplacementProfileInstanceItemDao(ReplacementProfileInstanceItemDao replacementProfileInstanceItemDao) {
		this.replacementProfileInstanceItemDao = replacementProfileInstanceItemDao;
	}
}