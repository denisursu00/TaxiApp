package ro.taxiApp.docs.plugins.organization;

import java.util.List;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import org.springframework.transaction.annotation.Transactional;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;

import ro.taxiApp.docs.domain.organization.User;

public class UserPersistenceDbPlugin extends HibernateDaoSupport implements UserPersistencePlugin, InitializingBean {
	
	@Override
	@Transactional(rollbackFor = Throwable.class)
	public void delete(User user) {
		getHibernateTemplate().delete(user);	
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<User> getAllUsers() {
        return getHibernateTemplate().find("FROM User ORDER BY LOWER(firstName || ' ' || lastName)");
	}
	
	@Override
	@Transactional(rollbackFor = Throwable.class)
	public void saveUser(User user) {
		getHibernateTemplate().saveOrUpdate(user);
	}
	
	@Override
	@Transactional(rollbackFor = Throwable.class)
	public Long saveUserAndReturnId(User user) {
		return (Long) getHibernateTemplate().save(user);
	}
	
	@Override
	public boolean userExists(Long id) {
		String query = "SELECT COUNT(*) FROM User WHERE id = ?";
		@SuppressWarnings("unchecked")
		List<Long> countAsList = getHibernateTemplate().find(query, id);
		long count = Iterables.getOnlyElement(countAsList);
		return (count > 0);
	}
	
	@Override
	public User getUserByUsername(String username) {
		String query = "FROM User WHERE LOWER(username) = LOWER(?)";
		@SuppressWarnings("unchecked")
		List<User> foundUsers = getHibernateTemplate().find(query, username);
		return Iterables.getOnlyElement(foundUsers, null);
	}
	
	@Override
	public User getUserById(Long id) {
		return (User) getHibernateTemplate().get(User.class, id);
	}
	
	@Override
	public boolean userWithUsernameExists(String username, Long idForUserToExclude) {
		
		String query = "SELECT COUNT(*) FROM User WHERE LOWER(username) = LOWER(?) ";
		if (idForUserToExclude != null) {
			query += " AND id != ?";
		}
		
		List<Object> queryParameters = Lists.<Object> newArrayList(username);
		if (idForUserToExclude != null) {
			queryParameters.add(idForUserToExclude);
		}

		@SuppressWarnings("unchecked")
		List<Long> countAsList = getHibernateTemplate().find(query, queryParameters.toArray());
		long count = Iterables.getOnlyElement(countAsList, 0L);
		
		return (count > 0);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<String> getUserRoleNames(Long userId) {
		String query = "SELECT role.name FROM User user, IN (user.roles) role WHERE user.id = ? ";
		return getHibernateTemplate().find(query, userId);
	}

}