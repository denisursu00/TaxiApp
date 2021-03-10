package ro.cloudSoft.cloudDoc.plugins.organization;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.base.Function;
import com.google.common.collect.Collections2;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

import ro.cloudSoft.cloudDoc.domain.nomenclator.NomenclatorValue;
import ro.cloudSoft.cloudDoc.domain.organization.User;
import ro.cloudSoft.cloudDoc.domain.organization.UserAndNomenclatorPersonRelation;

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
	public Set<Long> getIdsOfUsersInGroupWithName(String groupName) {
		String query =
			"SELECT theUser.id " +
			"FROM Group theGroup " +
			"JOIN theGroup.users theUser " +
			"WHERE theGroup.name = ?";
		@SuppressWarnings("unchecked")
		List<Long> idsOfUsersInGroupWithNameAsList = getHibernateTemplate().find(query, groupName);
		return Sets.newHashSet(idsOfUsersInGroupWithNameAsList);
	}
	
	@Override
	public Set<User> getUsersInGroupWithName(String groupName) {
		String query =
			"SELECT theUser " +
			"FROM Group theGroup " +
			"JOIN theGroup.users theUser " +
			"WHERE theGroup.name = ?";
		@SuppressWarnings("unchecked")
		List<User> idsOfUsersInGroupWithNameAsList = getHibernateTemplate().find(query, groupName);
		return Sets.newHashSet(idsOfUsersInGroupWithNameAsList);
	}

	@Override
	@Transactional(rollbackFor = Throwable.class)
	public void saveUser(User user) {
		
		if (user.getOu() != null) {
			if (user.getOu().getManager() != null) {
				/*
				 * Daca manager-ul este exact utilizatorul care se salveaza, in sesiunea Hibernate vor exista
				 * 2 obiecte diferite ce se refera la aceeasi entitate Hibernate, iar Hibernate va da exceptie
				 * la salvarea utilizatorului. Pentru a evita exceptia, trebuie sa scot manager-ul din cache-ul
				 * sesiunii Hibernate (prin "evict").
				 */
				getHibernateTemplate().evict(user.getOu().getManager());
			}
		} else if (user.getOrganization() != null) {
			if (user.getOrganization().getOrganizationManager() != null) {
				/*
				 * Daca manager-ul este exact utilizatorul care se salveaza, in sesiunea Hibernate vor exista
				 * 2 obiecte diferite ce se refera la aceeasi entitate Hibernate, iar Hibernate va da exceptie
				 * la salvarea utilizatorului. Pentru a evita exceptia, trebuie sa scot manager-ul din cache-ul
				 * sesiunii Hibernate (prin "evict").
				 */
				getHibernateTemplate().evict(user.getOrganization().getOrganizationManager());
			}
		}
		
		getHibernateTemplate().saveOrUpdate(user);
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<Long> getOrganizationUnitIds(Long userId) {
		List<Long> organizationUnitIds = new ArrayList<Long>();
		
		// Ia ID-ul parintelui utilizatorului.
		String query = "SELECT user.ou.id FROM User user WHERE user.id = " + userId.toString();
		List<Long> parentIds = getHibernateTemplate().find(query);
		if (!parentIds.isEmpty()) {
			// Daca are parinte, atunci ia id-ul parintelui.
			Long parentId = parentIds.get(0);
			/*
			 * Cat timp exista un parinte (al utilizatorului sau al unitatii
			 * organizatorice, adauga-i ID-ul si cauta-i parintele propriu.
			 */
			while (parentId != null) {
				organizationUnitIds.add(parentId);
				query = "SELECT ou.parentOu.id FROM OrganizationUnit ou WHERE ou.id = " + parentId.toString();
				parentIds = getHibernateTemplate().find(query);
				if (!parentIds.isEmpty()) {
					parentId = parentIds.get(0);
				} else {
					parentId = null;
				}
			}
		}
		
		return organizationUnitIds;
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<Long> getGroupIds(Long userId) {
		String query = "SELECT group.id FROM User user, IN (user.groups) group WHERE user.id = " + userId.toString();
		return getHibernateTemplate().find(query);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<String> getGroupNames(Long userId) {
		String query = "SELECT group.name FROM User user, IN (user.groups) group WHERE user.id = ? ";
		return getHibernateTemplate().find(query, userId);
	}
	
	@Override
	public Map<Long, String> getUsersNameMap(final Set<Long> userIds) {
		
		if (userIds.isEmpty()) {
			return Collections.emptyMap();
		}

		@SuppressWarnings("unchecked")
		List<User> users = getHibernateTemplate().executeFind(new HibernateCallback() {
			@Override
			public Object doInHibernate(Session session) throws HibernateException, SQLException {
				String query = "FROM User WHERE id IN (:userIds)";
				return session.createQuery(query)
					.setParameterList("userIds", userIds)
					.list();
			}
		});
		
		Map<Long, String> userNameById = Maps.newHashMap();
		
		for (User user : users) {
			userNameById.put(user.getId(), user.getName());
		}
		
		return userNameById;
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public List<HashMap<String, HashMap<String,String>>> getUsersAndDepartment(String userIds) {
		
		String queryString= "select eu.id, eu.firstName||' '||eu.lastName as user, ou.name as department  from "+
        " User eu  left join eu.ou  ou" +
        " where eu IN ( "+userIds+")";
		List<Object[]> results = getHibernateTemplate().find(queryString);
		List<HashMap<String,HashMap<String,String>>> hmList=new ArrayList<HashMap<String,HashMap<String,String>>>();
		if (results != null)
		{
			for(Object[] iterator:results )
			{	
			HashMap<String,HashMap<String,String>> hm=new HashMap<String,HashMap<String,String>>();
			//hm=(id,(user,department))
			HashMap<String,String> userDeptMap=new HashMap<String,String>();
			userDeptMap.put((String)iterator[1],iterator[2]!=null?iterator[2].toString():"");
			hm.put(iterator[0].toString(),userDeptMap);
			hmList.add(hm);
			}
			return hmList;
		}
		else
		return null;
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<String> getEmails(final Collection<Long> userIds) {
		
		if (CollectionUtils.isEmpty(userIds)) {
			return Collections.emptyList();
		}
		
		return getHibernateTemplate().executeFind(new HibernateCallback() {
			@Override
			public Object doInHibernate(Session session) throws HibernateException, SQLException {
				String query =
					"SELECT user.email " +
					"FROM User user " +
					"WHERE user.id IN (:userIds)";
				return session.createQuery(query)
					.setParameterList("userIds", userIds)
					.list();
			}
		});
	}
	
	@Override
	public String getEmail(Long userId) {
		List<String> emailAsList = getEmails(Collections.singleton(userId));
		if (emailAsList.size() == 1) {
			return emailAsList.get(0);
		} else if (emailAsList.size() == 0) {
			throw new IllegalArgumentException("Nu s-a gasit e-mail-ul pt. utilizatorul cu ID-ul [" + userId + "].");
		} else {
			throw new IllegalStateException("Pentru utilizatorul cu ID-ul [] s-au gasit mai multe e-mail-uri: [" + StringUtils.join(emailAsList, ", ") + "].");
		}
	}
	
	@Override
	public String getDisplayName(Long userId) {
		
		String query = "From User WHERE id = ?";
		@SuppressWarnings("unchecked")		
		List<User> userAsList = getHibernateTemplate().find(query, userId);
		
		User user = Iterables.getOnlyElement(userAsList, null);
		return (user != null) ? user.getDisplayName() : null;
	}
	
	@Override
	public Map<Long, String> getEmailByUserId(final Collection<Long> userIds) {
		
		if (userIds.isEmpty()) {
			return Collections.emptyMap();
		}
		
		@SuppressWarnings("unchecked")		
		List<Object[]> results = getHibernateTemplate().executeFind(new HibernateCallback() {
			
			@Override
			public Object doInHibernate(Session session) throws HibernateException, SQLException {
				String query = "SELECT id, email FROM User WHERE id IN (:userIds)";
				return session.createQuery(query)
					.setParameterList("userIds", userIds)
					.list();
			}
		});
		
		Map<Long, String> emailByUserId = Maps.newHashMap();
		
		for (Object[] result : results) {
			
			Long userId = (Long) result[0];
			String email = (String) result[1];
			
			emailByUserId.put(userId, email);
		}
		
		return emailByUserId;
	}
	
	@Override
	public Map<Long, String> getOrganizationUnitNameByUserId(final Collection<Long> userIds) {
		
		if (userIds.isEmpty()) {
			return Collections.emptyMap();
		}
		
		@SuppressWarnings("unchecked")		
		List<Object[]> results = getHibernateTemplate().executeFind(new HibernateCallback() {
			
			@Override
			public Object doInHibernate(Session session) throws HibernateException, SQLException {
				String query =
					"SELECT " +
					"	user.id, " +
					"	organizationUnit.name " +
					"FROM User user " +
					"JOIN user.ou organizationUnit " +
					"WHERE user.id IN (:userIds)";
				return session.createQuery(query)
					.setParameterList("userIds", userIds)
					.list();
			}
		});
		
		Map<Long, String> organizationUnitNameByUserId = Maps.newHashMap();
		
		for (Object[] result : results) {
			
			Long userId = (Long) result[0];
			String organizationUnitName = (String) result[1];
			
			organizationUnitNameByUserId.put(userId, organizationUnitName);
		}
		
		return organizationUnitNameByUserId;
	}
	
	@Override
	public Map<String, String> getOrganizationUnitNameByUserEmailInLowerCase(Collection<String> userEmails) {
		
		if (userEmails.isEmpty()) {
			return Collections.emptyMap();
		}
		
		final Collection<String> userEmailsInLowerCase = Collections2.transform(userEmails, new Function<String, String>() {
			
			@Override
			public String apply(String userEmail) {
				return userEmail.toLowerCase();
			}
		});
		
		@SuppressWarnings("unchecked")		
		List<Object[]> results = getHibernateTemplate().executeFind(new HibernateCallback() {
			
			@Override
			public Object doInHibernate(Session session) throws HibernateException, SQLException {
				String query =
					"SELECT " +
					"	LOWER(user.email), " +
					"	organizationUnit.name " +
					"FROM User user " +
					"JOIN user.ou organizationUnit " +
					"WHERE LOWER(user.email) IN (:userEmailsInLowerCase)";
				return session.createQuery(query)
					.setParameterList("userEmailsInLowerCase", userEmailsInLowerCase)
					.list();
			}
		});
		
		Map<String, String> organizationUnitNameByUserEmailInLowerCase = Maps.newHashMap();
		
		for (Object[] result : results) {
			
			String userEmailInLowerCase = (String) result[0];
			String organizationUnitName = (String) result[1];
			
			organizationUnitNameByUserEmailInLowerCase.put(userEmailInLowerCase, organizationUnitName);
		}
		
		return organizationUnitNameByUserEmailInLowerCase;
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<User> getUsersWithIds(final Collection<Long> userIds) {
		
		if (userIds.isEmpty()) {
			return Collections.emptyList();
		}
		
		return getHibernateTemplate().executeFind(new HibernateCallback() {
			@Override
			public Object doInHibernate(Session session) throws HibernateException, SQLException {
				String query = "FROM User user WHERE id IN (:userIds)";
				return session.createQuery(query)
					.setParameterList("userIds", userIds)
					.list();
			}
		});
	}
	
	@Override
	public boolean hasMultipleAccounts(String username) {
		String query = "SELECT COUNT(*) FROM User WHERE LOWER(username) = LOWER(?)";
		@SuppressWarnings("unchecked")
		List<Long> countAsList = getHibernateTemplate().find(query, username);
		long count = Iterables.getOnlyElement(countAsList);
		return (count > 1);
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
	public User getUserByUsernameAndTitle(String username, String title) {
		
		String query =
			"FROM User " +
			"WHERE LOWER(username) = LOWER(?) " +
			"AND title = ?";
		Object[] queryParameters = new Object[] {
			username,
			title
		};

		@SuppressWarnings("unchecked")
		List<User> foundUsers = getHibernateTemplate().find(query, queryParameters);
		
		if (foundUsers.size() == 1) {
			return foundUsers.get(0);
		} else if (foundUsers.size() == 0) {
			return null;
		} else {
			throw new IllegalStateException("S-au gasit mai multi utilizatori cu username-ul [" + username + "] si titlul [" + title + "].");
		}
	}
	
	@Override
	public User getUserById(Long id) {
		return (User) getHibernateTemplate().get(User.class, id);
	}
	
	@Override
	public List<User> getUsersWithUsername(String username) {
		String query =
			"FROM User " +
			"WHERE LOWER(username) = LOWER(?) " +
			"ORDER BY LOWER(firstName || ' ' || lastName)";
		@SuppressWarnings("unchecked")
		List<User> foundUsers = getHibernateTemplate().find(query, username);
		if (foundUsers.isEmpty()) {
			String exceptionMessage = "No users found with username [" + username + "].";
			throw new IllegalArgumentException(exceptionMessage);
		}
		return foundUsers;
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public Collection<Long> getIdsForUsersMatchedByUsernameInLowercase(final Collection<Long> idsForKnownUsers) {
		
		if (CollectionUtils.isEmpty(idsForKnownUsers)) {
			return Collections.emptyList();
		}
		
		return getHibernateTemplate().executeFind(new HibernateCallback() {
			
			@Override
			public Object doInHibernate(Session session) throws HibernateException, SQLException {

				String queryForUsernamesInLowercase =
					"SELECT LOWER(username) " +
					"FROM User " +
					"WHERE id IN (:idsForKnownUsers)";
				List<String> usernamesInLowercase = session.createQuery(queryForUsernamesInLowercase)
					.setParameterList("idsForKnownUsers", idsForKnownUsers)
					.list();
				
				if (usernamesInLowercase.isEmpty()) {
					return Collections.emptyList();
				}
				
				String queryForIdsForUsersMatchedByUsernameInLowercase =
					"SELECT id " +
					"FROM User " +
					"WHERE LOWER(username) IN (:usernamesInLowercase)";
				return session.createQuery(queryForIdsForUsersMatchedByUsernameInLowercase)
					.setParameterList("usernamesInLowercase", usernamesInLowercase)
					.list();
			}
		});
	}
	
	@Override
	public boolean userWithUsernameAndTitleExists(String username, String title, Long idForUserToExclude) {
		
		String query = "SELECT COUNT(*) FROM User WHERE LOWER(username) = LOWER(?) AND title = ?";
		if (idForUserToExclude != null) {
			query += " AND id != ?";
		}
		
		List<Object> queryParameters = Lists.<Object> newArrayList(username, title);
		if (idForUserToExclude != null) {
			queryParameters.add(idForUserToExclude);
		}

		@SuppressWarnings("unchecked")
		List<Long> countAsList = getHibernateTemplate().find(query, queryParameters.toArray());
		long count = Iterables.getOnlyElement(countAsList, 0L);
		
		return (count > 0);
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public List<String> getAllUsernames() {
		String query = "SELECT DISTINCT username FROM User ORDER BY LOWER(username)";
		return getHibernateTemplate().find(query);
	}
	
	@Override
	@Transactional(rollbackFor = Throwable.class)
	public void updateUsers(Collection<User> users) {
		for (User user : users) {
			getHibernateTemplate().update(user);
		}
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public List<User> getUserAccountsForUserWithIdInGroupWithName(Long userId, String groupName) {
		
		String query =
			"SELECT userAccount " +
			"FROM " +
			"	User userWithId," +
			"	User userAccount " +
			"JOIN userAccount.groups theGroup " +
			"WHERE userWithId.id = ? " +
			"AND LOWER(userWithId.username) = LOWER(userAccount.username) " +
			"AND theGroup.name = ?";
		Object[] queryParameters = new Object[] {
			userId,
			groupName
		};
		
		return getHibernateTemplate().find(query, queryParameters);
	}
	
	@Override
	public Set<Long> getIdsOfUserAccountsWithUsernameInGroupWithName(String username, String groupName) {
		
		String query =
			"SELECT theUser.id " +
			"FROM User theUser " +
			"JOIN theUser.groups theGroup " +
			"WHERE LOWER(theUser.username) = LOWER(?) " +
			"AND theGroup.name = ?";
		Object[] queryParameters = new Object[] {
			username,
			groupName
		};

		@SuppressWarnings("unchecked")
		List<Long> userIds = getHibernateTemplate().find(query, queryParameters);
		return Sets.newHashSet(userIds);
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public List<User> getUserAccountsWithUsernameInGroupWithName(String username, String groupName) {
		
		String query =
			"SELECT theUser " +
			"FROM User theUser " +
			"JOIN theUser.groups theGroup " +
			"WHERE LOWER(theUser.username) = LOWER(?) " +
			"AND theGroup.name = ? " +
			"ORDER BY LOWER(theUser.title)";
		Object[] queryParameters = new Object[] {
			username,
			groupName
		};
		
		return getHibernateTemplate().find(query, queryParameters);
	}
	
	@Override
	public void saveUserAndNomenclatorPersonRelation(UserAndNomenclatorPersonRelation userPerson) {
		if (userPerson.getId() == null) {
			getHibernateTemplate().save(userPerson);
		} else {
			getHibernateTemplate().saveOrUpdate(userPerson);
		}
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public UserAndNomenclatorPersonRelation getUserAndNomenclatorPersonRelationByUserId(Long userId) {
		String query =
				"SELECT unp " +
				"FROM " + UserAndNomenclatorPersonRelation.class.getSimpleName() + " unp " +
				"WHERE unp.user.id = ? ";	
		List<UserAndNomenclatorPersonRelation> results = getHibernateTemplate().find(query, userId);
		return Iterables.getOnlyElement(results, null);
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public NomenclatorValue getNomenclatorPersonByUserId(Long userId) {
		String query =
				"SELECT unp.nomenclatorPerson " +
				"FROM " + UserAndNomenclatorPersonRelation.class.getSimpleName() + " unp " +
				"WHERE unp.user.id = ? ";	
		List<NomenclatorValue> results = getHibernateTemplate().find(query, userId);
		return Iterables.getOnlyElement(results, null);
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public User getUserByNomenclatorPersonId(Long nomenclatorPersonId) {
		String query =
				"SELECT unp.user " +
				"FROM " + UserAndNomenclatorPersonRelation.class.getSimpleName() + " unp " +
				"WHERE unp.user.id = ? ";	
		List<User> results =getHibernateTemplate().find(query, nomenclatorPersonId);
		return Iterables.getOnlyElement(results, null);
	}
	
	@Override
	public void deleteUserAndNomenclatorPersonRelation(UserAndNomenclatorPersonRelation relation) {
		getHibernateTemplate().delete(relation);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<String> getUserRoleNames(Long userId) {
		String query = "SELECT role.name FROM User user, IN (user.roles) role WHERE user.id = ? ";
		return getHibernateTemplate().find(query, userId);
	}

	@Override
	public List<User> getAllUsersWithAssignedTasks() {
		String query =
				"FROM User " +
				"WHERE id IN ("
				+ " SELECT assignments "
				+ " FROM Task task "
				+ " JOIN task.assignments assignments)";
		
			@SuppressWarnings("unchecked")
			List<User> users = getHibernateTemplate().find(query);

			return users;
	}

}