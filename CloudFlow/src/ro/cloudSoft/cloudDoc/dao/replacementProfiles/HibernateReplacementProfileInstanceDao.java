package ro.cloudSoft.cloudDoc.dao.replacementProfiles;

import java.util.List;

import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import org.springframework.transaction.annotation.Transactional;

import ro.cloudSoft.cloudDoc.domain.replacementProfiles.ReplacementProfileInstance;
import ro.cloudSoft.cloudDoc.domain.replacementProfiles.ReplacementProfileInstanceItem;

import com.google.common.collect.Iterables;

/**
 * 
 */
public class HibernateReplacementProfileInstanceDao extends HibernateDaoSupport implements ReplacementProfileInstanceDao {

	@Override
	@Transactional(rollbackFor = Throwable.class)
	public void deleteProfileInstancesForDocument(String documentLocationRealName, String documentId) {
		
		String query =
			"FROM ReplacementProfileInstance " +
			"WHERE id.documentLocationRealName = ? " +
			"AND id.documentId = ?";
		Object[] queryParameters = new Object[] {
			documentLocationRealName,
			documentId
		};

		@SuppressWarnings("unchecked")
		List<ReplacementProfileInstance> eplacementProfileInstances = getHibernateTemplate().find(query, queryParameters);
		getHibernateTemplate().deleteAll(eplacementProfileInstances);
	}
	
	@Override
	@Transactional(rollbackFor = Throwable.class)
	public void createNewProfileInstance(ReplacementProfileInstance replacementProfileInstance) {
		prepareProfileInstanceItemsForSaving(replacementProfileInstance);
		getHibernateTemplate().save(replacementProfileInstance);
	}
	
	@Override
	@Transactional(rollbackFor = Throwable.class)
	public void createOrUpdateProfileInstance(ReplacementProfileInstance replacementProfileInstance) {
		prepareProfileInstanceItemsForSaving(replacementProfileInstance);
		getHibernateTemplate().saveOrUpdate(replacementProfileInstance);
	}
	
	private void prepareProfileInstanceItemsForSaving(ReplacementProfileInstance replacementProfileInstance) {
		List<ReplacementProfileInstanceItem> items = replacementProfileInstance.getItems();
		for (int i = 0; i < items.size(); i++) {
			
			ReplacementProfileInstanceItem replacementProfileInstanceItem = items.get(i);
			
			replacementProfileInstanceItem.setProfileInstance(replacementProfileInstance);
			replacementProfileInstanceItem.setIndex(i);
		}
	}
	
	@Override
	public ReplacementProfileInstance getProfileInstanceForDocument(String documentLocationRealName, String documentId) {
		
		String query =
			"FROM ReplacementProfileInstance " +
			"WHERE id.documentLocationRealName = ? " +
			"AND id.documentId = ?";
		Object[] queryParameters = new Object[] {
			documentLocationRealName,
			documentId
		};

		@SuppressWarnings("unchecked")
		List<ReplacementProfileInstance> foundReplacementProfileInstances = getHibernateTemplate().find(query, queryParameters);
		return Iterables.getOnlyElement(foundReplacementProfileInstances, null);
	}
	
	@Override
	@Transactional(rollbackFor = Throwable.class)
	public void deleteProfileInstancesWithNoItems() {
		String query =
			"DELETE FROM ReplacementProfileInstance profileInstance " +
			"WHERE profileInstance.items IS EMPTY";
		getHibernateTemplate().bulkUpdate(query);
	}
}