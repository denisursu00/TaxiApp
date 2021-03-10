package ro.cloudSoft.cloudDoc.dao.replacementProfiles;

import java.util.Collection;
import java.util.List;

import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import org.springframework.transaction.annotation.Transactional;

import ro.cloudSoft.cloudDoc.domain.replacementProfiles.ReplacementProfile;
import ro.cloudSoft.cloudDoc.domain.replacementProfiles.ReplacementProfileInstanceItem;

import com.google.common.collect.Iterables;

/**
 * 
 */
public class HibernateReplacementProfileInstanceItemDao extends HibernateDaoSupport implements ReplacementProfileInstanceItemDao {

	@Override
	@SuppressWarnings("unchecked")
	public Collection<ReplacementProfileInstanceItem> getInstanceItemsForProfile(ReplacementProfile replacementProfile) {
		String query = "FROM ReplacementProfileInstanceItem WHERE replacementProfileUsed = ?";
		return getHibernateTemplate().find(query, replacementProfile);
	}
	
	@Override
	@Transactional(rollbackFor = Throwable.class)
	public void deleteProfileInstanceItems(Collection<ReplacementProfileInstanceItem> items) {
		getHibernateTemplate().deleteAll(items);
	}
	
	@Override
	public ReplacementProfileInstanceItem getReplacementProfileInstanceItemForReplacement(
			String documentLocationRealName, String documentId, Long replacementUserId) {
		
		String query =
			"FROM ReplacementProfileInstanceItem " +
			"WHERE id.profileInstance.id.documentLocationRealName = ? " +
			"AND id.profileInstance.id.documentId = ? " +
			"AND replacementUsed.id = ?";
		Object[] queryParameters = new Object[] {
			documentLocationRealName,
			documentId,
			replacementUserId
		};

		@SuppressWarnings("unchecked")
		List<ReplacementProfileInstanceItem> foundItems = getHibernateTemplate().find(query, queryParameters);
		return Iterables.getOnlyElement(foundItems, null);
	}
}