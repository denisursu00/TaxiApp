package ro.cloudSoft.cloudDoc.dao.replacementProfiles;

import java.sql.SQLException;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import org.springframework.transaction.annotation.Transactional;

import ro.cloudSoft.cloudDoc.domain.organization.User;
import ro.cloudSoft.cloudDoc.domain.replacementProfiles.ReplacementProfile;
import ro.cloudSoft.cloudDoc.domain.replacementProfiles.ReplacementProfileStatusOption;

import com.google.common.collect.Iterables;

/**
 * 
 */
public class HibernateReplacementProfileDao extends HibernateDaoSupport implements ReplacementProfileDao {

	@Override
	@Transactional(rollbackFor = Throwable.class)
	public void createOrUpdateReplacementProfile(ReplacementProfile replacementProfile) {
		getHibernateTemplate().saveOrUpdate(replacementProfile);
	}

	@Override
	@Transactional(rollbackFor = Throwable.class)
	public void deleteReplacementProfile(ReplacementProfile replacementProfile) {
		getHibernateTemplate().delete(replacementProfile);
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<ReplacementProfile> getAllReplacementProfiles() {
		String query =
			"FROM ReplacementProfile " +
			"ORDER BY " +
			"	LOWER(requesterUsername) ASC, " +
			"	startDate DESC";
		return getHibernateTemplate().find(query);
	}

	@Override
	public ReplacementProfile getReplacementProfileById(Long replacementProfileId) {
		String query = "FROM ReplacementProfile WHERE id = ?";
		@SuppressWarnings("unchecked")
		List<ReplacementProfile> foundReplacementProfiles = getHibernateTemplate().find(query, replacementProfileId);
		return Iterables.getOnlyElement(foundReplacementProfiles);
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<ReplacementProfile> getReplacementProfilesForRequesterWithUsername(String requesterUsername) {
		String query =
			"FROM ReplacementProfile " +
			"WHERE LOWER(requesterUsername) = LOWER(?) " +
			"ORDER BY startDate DESC";
		return getHibernateTemplate().find(query, requesterUsername);
	}
	
	@Override
	public boolean replacementProfilesForUserProfilesInPeriodExist(final Collection<User> requesterUserProfiles,
			final Date startDate, final Date endDate, final Long idForReplacementProfileToBeExcluded,
			final Collection<ReplacementProfileStatusOption> statusesToBeExcluded) {
		
		Long count = (Long) getHibernateTemplate().execute(new HibernateCallback() {
			
			@Override
			public Object doInHibernate(Session session) throws HibernateException, SQLException {
				
				String queryString =
					"SELECT COUNT(*) " +
					"FROM ReplacementProfile replacementProfile " +
					"JOIN replacementProfile.selectedRequesterUserProfiles requesterUserProfile " +
					"WHERE requesterUserProfile IN (:requesterUserProfiles) " +
					"AND ((replacementProfile.startDate <= :endDate) AND (replacementProfile.endDate >= :startDate)) " +
					"AND replacementProfile.status.status NOT IN (:statusesToBeExcluded)";
				if (idForReplacementProfileToBeExcluded != null) {
					queryString += "AND replacementProfile.id != :idForReplacementProfileToBeExcluded";
				}
				
				Query query = session.createQuery(queryString);
				
				query.setParameterList("requesterUserProfiles", requesterUserProfiles);
				query.setParameter("startDate", startDate);
				query.setParameter("endDate", endDate);
				query.setParameterList("statusesToBeExcluded", statusesToBeExcluded);
				if (idForReplacementProfileToBeExcluded != null) {
					query.setParameter("idForReplacementProfileToBeExcluded", idForReplacementProfileToBeExcluded);
				}
				
				return query.uniqueResult();
			}
		});
		return (count > 0);
	}
	
	@Override
	public ReplacementProfile getActiveReplacementProfileForUser(Long userId) {
		
		Collection<Long> userIdAsCollection = Collections.singleton(userId);
		List<ReplacementProfile> foundReplacementProfilesForUser = getActiveReplacementProfileForUsers(userIdAsCollection);
		
		if (foundReplacementProfilesForUser.isEmpty()) {
			return null;
		} else if (foundReplacementProfilesForUser.size() == 1) {
			return foundReplacementProfilesForUser.get(0);
		} else {
			String exceptionMessage = "S-au gasit mai multe profile de inlocuire " +
				"active pentru profilul de utilizator cu ID-ul [" + userId + "]";
			throw new IllegalStateException(exceptionMessage);
		}
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public List<ReplacementProfile> getActiveReplacementProfileForUsers(final Collection<Long> userIds) {
		
		if (CollectionUtils.isEmpty(userIds)) {
			return Collections.emptyList();
		}
		
		return getHibernateTemplate().executeFind(new HibernateCallback() {
			
			@Override
			public Object doInHibernate(Session session) throws HibernateException, SQLException {
				String query =
					"SELECT replacementProfile " +
					"FROM ReplacementProfile replacementProfile " +
					"JOIN replacementProfile.selectedRequesterUserProfiles userProfile " +
					"WHERE replacementProfile.status.status = :activeStatus " +
					"AND userProfile.id IN (:userIds)";
				return session.createQuery(query)
					.setParameter("activeStatus", ReplacementProfileStatusOption.ACTIVE)
					.setParameterList("userIds", userIds)
					.list();
			}
		});
	}
	
	@Override
	public ReplacementProfile getActiveReplacementProfileWhereReplacementIsRequester(ReplacementProfile replacementProfileWithReplacement) {
		
		String query =
			"SELECT replacementProfile " +
			"FROM ReplacementProfile replacementProfile " +
			"JOIN replacementProfile.selectedRequesterUserProfiles userProfile " +
			"JOIN replacementProfile.selectedReplacementProfilesInWhichRequesterIsReplacement replacementProfileInWhichRequesterIsReplacement " +
			"WHERE replacementProfile.status.status = ? " +
			"AND userProfile = ? " +
			"AND replacementProfileInWhichRequesterIsReplacement = ?";
		Object[] queryParameters = new Object[] {
			ReplacementProfileStatusOption.ACTIVE,
			replacementProfileWithReplacement.getReplacement(),
			replacementProfileWithReplacement
		};

		@SuppressWarnings("unchecked")
		List<ReplacementProfile> foundReplacementProfiles = getHibernateTemplate().find(query, queryParameters);
		return Iterables.getOnlyElement(foundReplacementProfiles, null);
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public Collection<ReplacementProfile> getInactiveReplacementProfilesThatBegan(Date referenceDate) {
		
		String query =
			"FROM ReplacementProfile " +
			"WHERE status.status = ? " +
			"AND startDate <= ?";
		Object[] queryParameters = new Object[] {
			ReplacementProfileStatusOption.INACTIVE,
			referenceDate
		};

		return getHibernateTemplate().find(query, queryParameters);
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public Collection<ReplacementProfile> getActiveReplacementProfilesThatEnded(Date referenceDate) {
		
		String query =
			"FROM ReplacementProfile " +
			"WHERE status.status = ? " +
			"AND endDate <= ?";
		Object[] queryParameters = new Object[] {
			ReplacementProfileStatusOption.ACTIVE,
			referenceDate
		};

		return getHibernateTemplate().find(query, queryParameters);
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public Collection<ReplacementProfile> getReplacementProfilesWithIds(final Collection<Long> replacementProfileIds) {
		
		if (CollectionUtils.isEmpty(replacementProfileIds)) {
			return Collections.emptyList();
		}
		
		return getHibernateTemplate().executeFind(new HibernateCallback() {
			
			@Override
			public Object doInHibernate(Session session) throws HibernateException, SQLException {
				String query =
					"FROM ReplacementProfile " +
					"WHERE id IN (:replacementProfileIds)";
				return session.createQuery(query)
					.setParameterList("replacementProfileIds", replacementProfileIds)
					.list();
			}
		});
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public List<ReplacementProfile> getReplacementProfilesOfReplacementsWithOverlappingDateIntervalWithStatuses(
			final Collection<Long> idsForReplacementUsers, final Date startDate, final Date endDate,
			final Collection<ReplacementProfileStatusOption> allowedStatuses) {
		
		if (CollectionUtils.isEmpty(idsForReplacementUsers) || CollectionUtils.isEmpty(allowedStatuses)) {
			return Collections.emptyList();
		}
		
		return getHibernateTemplate().executeFind(new HibernateCallback() {
			
			@Override
			public Object doInHibernate(Session session) throws HibernateException, SQLException {
				String query = 
					"SELECT replacementProfile " +
					"FROM ReplacementProfile replacementProfile " +
					"WHERE replacementProfile.replacement.id IN (:idsForReplacementUsers) " +
					"AND ((replacementProfile.startDate <= :endDate) AND (replacementProfile.endDate >= :startDate)) " +
					"AND replacementProfile.status.status IN (:allowedStatuses) " +
					"ORDER BY " +
					"	LOWER(replacementProfile.requesterUsername) ASC, " +
					"	replacementProfile.startDate DESC";				
				return session.createQuery(query)
					.setParameterList("idsForReplacementUsers", idsForReplacementUsers)
					.setParameter("startDate", startDate)
					.setParameter("endDate", endDate)
					.setParameterList("allowedStatuses", allowedStatuses)
					.list();
			}
		});
	}
}