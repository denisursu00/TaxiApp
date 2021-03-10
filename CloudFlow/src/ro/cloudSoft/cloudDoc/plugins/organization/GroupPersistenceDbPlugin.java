package ro.cloudSoft.cloudDoc.plugins.organization;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Iterables;

import ro.cloudSoft.cloudDoc.core.AppException;
import ro.cloudSoft.cloudDoc.domain.organization.Group;
import ro.cloudSoft.cloudDoc.domain.organization.User;
import ro.cloudSoft.cloudDoc.domain.security.SecurityManager;

public class GroupPersistenceDbPlugin extends HibernateDaoSupport implements GroupPersistencePlugin, InitializingBean {
	
	@Override
	public void delete(Group theGroup, SecurityManager userSecurity)
	{
		getHibernateTemplate().delete( theGroup );
	}

	@Override
	public List<User> getAllUsersWithGroup(Long groupId,
			SecurityManager userSecurity)
	{
		Group theGroup = (Group) getHibernateTemplate().get(Group.class, groupId);
		Set<User> users = theGroup.getUsers();
		ArrayList<User> usersList = new ArrayList<User>();
		usersList.addAll(users);
		return usersList;
	}

	@Override
	public Group getGroup(Long id, SecurityManager userSecurity)
	{
		Group theGroup = (Group) getHibernateTemplate().get(Group.class, id);
		return theGroup;
	}
	
	@Override
	public Group getGroupById(Long id) {
		String query = "FROM Group WHERE id = ?";
		@SuppressWarnings("unchecked")
		List<Group> foundGroups = getHibernateTemplate().find(query, id);
		return Iterables.getOnlyElement(foundGroups, null);
	}
	
	@Override
	public Group getGroupByName(String groupName) {
		
		String query = "FROM Group WHERE name = ?";
		@SuppressWarnings("unchecked")
		List<Group> foundGroups = getHibernateTemplate().find(query, groupName);
		
		if (foundGroups.size() == 1) {
			return foundGroups.get(0);
		} else if (foundGroups.size() == 0) {
			return null;
		} else {
			throw new IllegalStateException("S-au gasit mai multe grupuri cu numele [" + groupName + "].");
		}
	}

	@Override
	public Group getGroup(final String name, SecurityManager userSecurity) {
		return (Group) getHibernateTemplate().execute(new HibernateCallback() {
			
			@Override
			public Object doInHibernate(Session session) throws HibernateException, SQLException {
				
				String query = "FROM Group WHERE name = :name";
				Group group = (Group) session.createQuery(query).setParameter("name", name).uniqueResult();
				
				if (group != null) {
					Hibernate.initialize(group.getUsers());
				}
				
				return group;
			}
		});
	}

	@Override
	@SuppressWarnings("unchecked")
	public ArrayList<Group> getGroups(SecurityManager userSecurity)
	{
		String query = "FROM Group ORDER BY name";
		ArrayList<Group> groups = (ArrayList<Group>) getHibernateTemplate().find(query);
        return groups;
	}

	@Override
	@SuppressWarnings("unchecked")
	public void addUserToGroup(User user, String groupName, SecurityManager userSecurity) {
		
		String queryForGroup = "FROM Group WHERE name = ?";
		Group group = Iterables.<Group> getOnlyElement(getHibernateTemplate().find(queryForGroup, groupName));
		
		group.getUsers().add(user);
		getHibernateTemplate().saveOrUpdate(group);
    }
	
	@Override
	public Long setGroup(Group rl)
			throws AppException
	{
		getHibernateTemplate().saveOrUpdate( rl );
		return rl.getId();
	}
	
	@Override
	@Transactional(rollbackFor = Throwable.class)
	public void updateGroups(Collection<Group> groups) {
		for (Group group : groups) {
			getHibernateTemplate().update(group);
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<String> getEmails(final Collection<Long> groupIds) {
		
		if (CollectionUtils.isEmpty(groupIds)) {
			return Collections.emptyList();
		}
		
		return getHibernateTemplate().executeFind(new HibernateCallback() {
			@Override
			public Object doInHibernate(Session session) throws HibernateException, SQLException {
				String query =
					"SELECT DISTINCT user.email " +
					"FROM Group grup " +
					"JOIN grup.users user " +
					"WHERE grup.id IN (:groupIds)";
				return session.createQuery(query)
					.setParameterList("groupIds", groupIds)
					.list();
			}
		});
	}
	
	@Override
	public boolean groupExists(Long groupId) {
		
		String query = "SELECT COUNT(*) FROM Group grup WHERE id = ?";
		@SuppressWarnings("unchecked")
		List<Long> result = getHibernateTemplate().find(query, groupId);
		
		long groupCount = Iterables.getOnlyElement(result, 0L);
		return (groupCount == 1L);
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public Collection<Long> getIdsForUsersInGroup(Long groupId) {
		String query =
			"SELECT user.id " +
			"FROM Group theGroup " +
			"JOIN theGroup.users user " +
			"WHERE theGroup.id = ?";
		return getHibernateTemplate().find(query, groupId);
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public List<String> getGroupNamesOfUserWithId(Long userId) {
		String query =
			"SELECT theGroup.name " +
			"FROM User theUser " +
			"JOIN theUser.groups theGroup " +
			"WHERE theUser.id = ? " +
			"ORDER BY LOWER(theGroup.name)";
		return getHibernateTemplate().find(query, userId);
	}
	
	@Override
	public boolean isUserInGroupWithName(Long userId, String groupName) {
		
		String query =
			"SELECT COUNT(*) " +
			"FROM Group theGroup " +
			"JOIN theGroup.users theUser " +
			"WHERE theGroup.name = ? " +
			"AND theUser.id = ?";
		Object[] queryParameters = new Object[] {
			groupName,
			userId
		};

		@SuppressWarnings("unchecked")
		List<Long> countAsList = getHibernateTemplate().find(query, queryParameters);
		long count = Iterables.getOnlyElement(countAsList);
		
		if (count == 0) {
			return false;
		} else if (count == 1) {
			return true;
		} else {
			throw new IllegalStateException("S-a gasit utilizatorul cu ID-ul [" + userId + "] de mai multe ori in grupul cu numele [" + groupName + "].");
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public boolean isUserInGroupWithId(Long userId, Long groupId) {
		String query =
			"SELECT COUNT(*) " +
			"FROM Group theGroup " +
			"JOIN theGroup.users theUser " +
			"WHERE theGroup.id = ? " +
			"AND theUser.id = ?";
		Object[] queryParameters = new Object[] {
			groupId,
			userId
		};

		List<Long> countAsList = getHibernateTemplate().find(query, queryParameters);
		long count = Iterables.getOnlyElement(countAsList);
		
		if (count == 0) {
			return false;
		} else if (count == 1) {
			return true;
		} else {
			throw new IllegalStateException("S-a gasit utilizatorul cu ID-ul [" + userId + "] de mai multe ori in grupul cu id [" + groupId + "].");
		}
	}
}