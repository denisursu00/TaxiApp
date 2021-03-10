package ro.cloudSoft.cloudDoc.dao.replacementProfiles;

import java.util.List;

import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import org.springframework.transaction.annotation.Transactional;

import ro.cloudSoft.cloudDoc.domain.replacementProfiles.ReplacementProfile;
import ro.cloudSoft.cloudDoc.domain.replacementProfiles.ReplacementProfileStatus;
import ro.cloudSoft.cloudDoc.domain.replacementProfiles.ReplacementProfileStatusOption;

import com.google.common.collect.Iterables;

/**
 * 
 */
public class HibernateReplacementProfileStatusDao extends HibernateDaoSupport implements ReplacementProfileStatusDao {

	@Override
	@Transactional(rollbackFor = Throwable.class)
	public void deleteReplacementProfileStatus(ReplacementProfileStatus replacementProfileStatus) {
		getHibernateTemplate().delete(replacementProfileStatus);
	}
	
	@Override
	public ReplacementProfileStatus getReplacementProfileStatusByProfile(ReplacementProfile profile) {
		String query = "FROM ReplacementProfileStatus WHERE profile = ?";
		@SuppressWarnings("unchecked")
		List<ReplacementProfileStatus> foundStatuses = getHibernateTemplate().find(query, profile);
		return Iterables.getOnlyElement(foundStatuses);
	}
	
	@Override
	public ReplacementProfileStatusOption getStatusForReplacementProfile(Long replacementProfileId) {
		String query =
			"SELECT status " +
			"FROM ReplacementProfileStatus " +
			"WHERE profile.id = ?";
		@SuppressWarnings("unchecked")
		List<ReplacementProfileStatusOption> foundStatuses = getHibernateTemplate().find(query, replacementProfileId);
		return Iterables.getOnlyElement(foundStatuses);
	}
	
	@Override
	@Transactional(rollbackFor = Throwable.class)
	public void createOrUpdateReplacementProfileStatus(ReplacementProfileStatus replacementProfileStatus) {
		getHibernateTemplate().saveOrUpdate(replacementProfileStatus);
	}
}