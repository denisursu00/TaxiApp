package ro.cloudSoft.cloudDoc.dao.organizaiton;

import java.util.Collection;
import java.util.List;

import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import org.springframework.transaction.annotation.Transactional;

import ro.cloudSoft.cloudDoc.domain.organization.UserDeactivation;

import com.google.common.collect.Iterables;

public class HibernateUserDeactivationDao extends HibernateDaoSupport implements UserDeactivationDao {

	@Override
	@Transactional(rollbackFor = Throwable.class)
	public void saveAll(Collection<UserDeactivation> userDeactivations) {
		for (UserDeactivation userDeactivation : userDeactivations) {
			getHibernateTemplate().save(userDeactivation);
		}
	}
	
	@Override
	public UserDeactivation getForUserWithId(Long userId) {
		String query =
			"FROM UserDeactivation userDeactivation " +
			"WHERE userDeactivation.user.id = ?";
		@SuppressWarnings("unchecked")
		List<UserDeactivation> foundUserDeactivations = getHibernateTemplate().find(query, userId);
		return Iterables.getOnlyElement(foundUserDeactivations, null);
	}
	
	@Override
	@Transactional(rollbackFor = Throwable.class)
	public void delete(UserDeactivation userDeactivation) {
		getHibernateTemplate().delete(userDeactivation);
	}
}