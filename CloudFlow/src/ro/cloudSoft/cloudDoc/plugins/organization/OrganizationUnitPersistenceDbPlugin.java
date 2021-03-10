package ro.cloudSoft.cloudDoc.plugins.organization;

import java.sql.SQLException;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import org.springframework.transaction.annotation.Transactional;

import ro.cloudSoft.cloudDoc.domain.organization.OrganizationUnit;
import ro.cloudSoft.cloudDoc.domain.organization.User;
import ro.cloudSoft.cloudDoc.domain.security.SecurityManager;

import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

public class OrganizationUnitPersistenceDbPlugin extends HibernateDaoSupport implements OrganizationUnitPersistencePlugin, InitializingBean {

	@Override
	@Transactional(rollbackFor = Throwable.class)
	public Long setOrganizationUnit(OrganizationUnit organizationUnit, SecurityManager userSecurity) {
		getHibernateTemplate().saveOrUpdate(organizationUnit);
		return organizationUnit.getId();
	}
	
	@Override
	public OrganizationUnit getOrganizationUnitById(Long id) {
		String query = "FROM OrganizationUnit WHERE id = ?";
		@SuppressWarnings("unchecked")
		List<OrganizationUnit> foundOrganizationUnits = getHibernateTemplate().find(query, id);
		return Iterables.getOnlyElement(foundOrganizationUnits, null);
	}
	
	@Override
	public OrganizationUnit getOrganizationUnitByNameAndParentOrganization(
			Long parentOrganizationId, String organizationUnitName) {
		
		String query =
			"SELECT organizationUnit " +
			"FROM OrganizationUnit organizationUnit " +
			"JOIN organizationUnit.organization parentOrganization " +
			"WHERE organizationUnit.name = ? " +
			"AND parentOrganization.id = ?";
		Object[] queryParameters = {
			organizationUnitName,
			parentOrganizationId
		};

		@SuppressWarnings("unchecked")
		List<OrganizationUnit> foundOrganizationUnits = getHibernateTemplate().find(query, queryParameters);
		
		if (foundOrganizationUnits.size() == 1) {
			return foundOrganizationUnits.get(0);
		} else if (foundOrganizationUnits.size() == 0) {
			return null;
		} else {
			String exceptionMessage = "S-au gasit mai multe unitati organizatorice cu numele " +
				"[" + organizationUnitName + "], care au ca parinte organizatia cu ID-ul " +
				"[" + parentOrganizationId + "].";
			throw new IllegalStateException(exceptionMessage);
		}
	}
	
	@Override
	public OrganizationUnit getOrganizationUnitByNameAndParentOrganizationUnit(
			Long parentOrganizationUnitId, String organizationUnitName) {
		
		String query =
			"SELECT organizationUnit " +
			"FROM OrganizationUnit organizationUnit " +
			"JOIN organizationUnit.parentOu parentOrganizationUnit " +
			"WHERE organizationUnit.name = ? " +
			"AND parentOrganizationUnit.id = ?";
		Object[] queryParameters = {
			organizationUnitName,
			parentOrganizationUnitId
		};

		@SuppressWarnings("unchecked")
		List<OrganizationUnit> foundOrganizationUnits = getHibernateTemplate().find(query, queryParameters);

		if (foundOrganizationUnits.size() == 1) {
			return foundOrganizationUnits.get(0);
		} else if (foundOrganizationUnits.size() == 0) {
			return null;
		} else {
			String exceptionMessage = "S-au gasit mai multe unitati organizatorice cu numele " +
				"[" + organizationUnitName + "], care au ca parinte unitatea organizatorica cu ID-ul " +
				"[" + parentOrganizationUnitId + "].";
			throw new IllegalStateException(exceptionMessage);
		}
	}
	
	@Override
	public OrganizationUnit getOrganizationUnitByName(String name) {
		
		String query = "FROM OrganizationUnit WHERE name = ?";
		@SuppressWarnings("unchecked")
		List<OrganizationUnit> foundOrganizationUnits = getHibernateTemplate().find(query, name);
		
		if (foundOrganizationUnits.size() == 1) {
			return foundOrganizationUnits.get(0);
		} else if (foundOrganizationUnits.size() == 0) {
			return null;
		} else {
			throw new IllegalArgumentException("S-au gasit mai multe unitati organizatorice cu numele [" + name + "].");
		}
	}

	@Override
    public Set<User> getOrganizationUnitUsers(Long organizationUnitId) {
    	OrganizationUnit organizationUnit = (OrganizationUnit) getHibernateTemplate().get(OrganizationUnit.class, organizationUnitId);
    	return organizationUnit.getUsers();
    }

	@Override
	@Transactional(rollbackFor = Throwable.class)
    public void delete(OrganizationUnit theGroup, SecurityManager userSecurity)
    {
    	getHibernateTemplate().delete(theGroup);
    }
	
	/**
	 * Returneaza ID-urile tututor unitatilor organizatorice care au ca parinti (direct sau indirect) unitatile cu ID-urile date.
	 * In set vor fi incluse si ID-urile unitatilor date.
	 */
	private Set<Long> getAllOrganizationUnitIdsForParents(Collection<Long> idsForParentOrganizationUnits) {
		
		if (CollectionUtils.isEmpty(idsForParentOrganizationUnits)) {
			return Collections.emptySet();
		}

		final Set<Long> allOrganizationUnitIds = Sets.newHashSet(idsForParentOrganizationUnits);
		
		final List<Long> parentOrganizationUnitIds = Lists.newLinkedList(idsForParentOrganizationUnits);
		
		while (!parentOrganizationUnitIds.isEmpty()) {

			@SuppressWarnings("unchecked")
			List<Long> childOrganizationUnitIds = getHibernateTemplate().executeFind(new HibernateCallback() {
				@Override
				public Object doInHibernate(Session session) throws HibernateException, SQLException {
					String query = "SELECT childOrganizationUnit.id " +
						"FROM OrganizationUnit parentOrganizationUnit " +
						"JOIN parentOrganizationUnit.subOus childOrganizationUnit " +
						"WHERE parentOrganizationUnit.id IN (:parentOrganizationUnitIds)";
					return session.createQuery(query)
						.setParameterList("parentOrganizationUnitIds", parentOrganizationUnitIds)
						.list();
				}
			});
			
			allOrganizationUnitIds.addAll(childOrganizationUnitIds);
			
			parentOrganizationUnitIds.clear();
			parentOrganizationUnitIds.addAll(childOrganizationUnitIds);
		}
		
		return allOrganizationUnitIds;
	}

	/**
	 * Returneaza ID-urile tututor unitatilor organizatorice care au ca parinte unitatea cu ID-ul dat.
	 * In set va fi inclus si ID-ul unitatii date.
	 */
	private Set<Long> getAllOrganizationUnitIdsForParent(Long idForParentOrganizationUnit) {
		return getAllOrganizationUnitIdsForParents(Collections.singleton(idForParentOrganizationUnit));
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<String> getEmails(Collection<Long> organizationUnitIds) {
		
		if (CollectionUtils.isEmpty(organizationUnitIds)) {
			return Collections.emptyList();
		}
		
		final Collection<Long> allOrganizationUnitIds = getAllOrganizationUnitIdsForParents(organizationUnitIds);
		
		return getHibernateTemplate().executeFind(new HibernateCallback() {
			@Override
			public Object doInHibernate(Session session) throws HibernateException, SQLException {
				String query = "SELECT DISTINCT user.email " +
					"FROM OrganizationUnit organizationUnit " +
					"JOIN organizationUnit.users user " +
					"WHERE organizationUnit.id IN (:allOrganizationUnitIds)";
				return session.createQuery(query)
					.setParameterList("allOrganizationUnitIds", allOrganizationUnitIds)
					.list();
			}
		});
	}
	
	@Override
	public boolean hasChildOrganizationUnits(Long organizationUnitId) {
		OrganizationUnit organizationUnit = (OrganizationUnit) getHibernateTemplate().get(OrganizationUnit.class, organizationUnitId);
		return (!organizationUnit.getSubOus().isEmpty());
	}
	
	@Override
	public boolean hasUsers(Long organizationUnitId) {
		OrganizationUnit organizationUnit = (OrganizationUnit) getHibernateTemplate().get(OrganizationUnit.class, organizationUnitId);
		return (!organizationUnit.getUsers().isEmpty());
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<OrganizationUnit> getOrganizationUnitsWhereUserIsManager(Long userId) {
		String query =
			"SELECT organizationUnit " +
			"FROM OrganizationUnit organizationUnit " +
			"JOIN organizationUnit.manager manager " +
			"WHERE manager.id = ?";
		return getHibernateTemplate().find(query, userId);
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public Collection<Long> getIdsForUsersInOrganizationUnit(Long organizationUnitId) {
		final Collection<Long> allOrganizationUnitIds = getAllOrganizationUnitIdsForParent(organizationUnitId);
		return getHibernateTemplate().executeFind(new HibernateCallback() {
			
			@Override
			public Object doInHibernate(Session session) throws HibernateException, SQLException {
				String query =
					"SELECT user.id " +
					"FROM OrganizationUnit organizationUnit " +
					"JOIN organizationUnit.users user " +
					"WHERE organizationUnit.id IN (:organizationUnitIds)";
				return session.createQuery(query)
					.setParameterList("organizationUnitIds", allOrganizationUnitIds)
					.list();
			}
		});
	}
}