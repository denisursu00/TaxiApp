package ro.cloudSoft.cloudDoc.plugins.organization;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import ro.cloudSoft.cloudDoc.domain.organization.Organization;
import ro.cloudSoft.cloudDoc.domain.organization.OrganizationUnit;
import ro.cloudSoft.cloudDoc.domain.organization.User;
import ro.cloudSoft.cloudDoc.domain.organization.User.UserTypeEnum;
import ro.cloudSoft.cloudDoc.domain.organization.tree.OrganizationNode;
import ro.cloudSoft.cloudDoc.domain.organization.tree.OrganizationTree;
import ro.cloudSoft.cloudDoc.domain.security.SecurityManager;
import ro.cloudSoft.common.utils.DependencyInjectionUtils;

import com.google.common.collect.Iterables;

public class OrganizationPersistenceDbPlugin extends HibernateDaoSupport implements OrganizationPersistencePlugin, InitializingBean {

	private String organizationName;
	
	public void afterPropertiesSetOverride() throws Exception {
		
		afterPropertiesSet();
		
		DependencyInjectionUtils.checkRequiredDependencies(
			organizationName
		);
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public OrganizationTree load(SecurityManager userSecurity) 
	{
		List<Organization> organizations = getHibernateTemplate().find("from Organization where name = '" + organizationName  + "'");
		return computeorganizationTree(organizations.get(0));
	}

	private OrganizationTree computeorganizationTree(Organization organization) {
		if (organization == null) {
			return null;
		}
		OrganizationTree theOT = new OrganizationTree();

		OrganizationNode theRootNode = new OrganizationNode();
		theRootNode.setName(organization.getName());
		theRootNode.setId(organization.getId().toString());
		theRootNode.setType(0);
		if (organization.getOrganizationManager() != null) {
			theRootNode.setManagerId(organization.getOrganizationManager().getId());
		}

		theRootNode.setChildren(getSubGroups(organization.getOrganizationUnits(), organization.getOrganizationUsers(), organization.getId()));

		theOT.setRootElement(theRootNode);

		return theOT;
	}

	private ArrayList<OrganizationNode> getSubGroups(Set<OrganizationUnit> groups, Set<User> users, Long parentId) {
		ArrayList<OrganizationNode> returnValue = new ArrayList<OrganizationNode>();
		if (users != null) {
			for (User u : users) {
				OrganizationNode node = new OrganizationNode();
				node.setId(u.getId().toString());
				node.setName(u.getName());
				node.setType(2);
				node.setTitle(u.getTitle());
				node.setCustomTitleTemplate(u.getCustomTitleTemplate());
				returnValue.add(node);
			}
		}
		if (groups != null) {
			for (OrganizationUnit g : groups) {
				OrganizationNode node = new OrganizationNode();
				node.setId(g.getId().toString());
				node.setName(g.getName());
				node.setType(1);
				// Daca unitatea organizatorica are manager, i-l seteaza nodului.
				if (g.getManager() != null) {
					node.setManagerId(g.getManager().getId());
				}
				node.setChildren(getSubGroups(g.getSubOus(), g.getUsers(), g.getId()));
				returnValue.add(node);
			}
		}
		return returnValue;
	}

	@Override
	public Long setOrganization(Organization orgman, SecurityManager userSecurity) {
		getHibernateTemplate().saveOrUpdate(orgman);
		return orgman.getId();
	}
	
	/**
	 * {@inheritDoc}
	 * 
	 * Din moment ce organizatia poate fi un arbore mare si "adanc", colectiile "lazy"
	 * (unitati organizatorice, utilizatori etc.) trebuie initializate.
	 */
	@Override
	public Organization getOrganizationForPopulator(final String organizationName, SecurityManager userSecurity) {
		
		Organization organization = (Organization) getHibernateTemplate().execute(new HibernateCallback() {
			
			@Override
			public Object doInHibernate(Session session) throws HibernateException, SQLException {
				
				@SuppressWarnings("unchecked")
				List<Organization> organizations = session.createQuery("FROM Organization WHERE name = :name").setString("name", organizationName).list();
				
		        if (organizations.isEmpty()) {
		        	return null;
		        }
		        
		        Organization foundOrganization = organizations.get(0);
		        
		        getHibernateTemplate().initialize(foundOrganization.getOrganizationUnits());
		        getHibernateTemplate().initialize(foundOrganization.getOrganizationUsers());
		        
		        return foundOrganization;
			}
		});
		
		return organization;
	}
	
	@Override
	public Organization getOrganizationById(Long organizationId) {
		String query = "FROM Organization WHERE id = ?";
		@SuppressWarnings("unchecked")
		List<Organization> foundOrganizations = getHibernateTemplate().find(query, organizationId);
		return Iterables.getOnlyElement(foundOrganizations);
	}
	
	@Override
	public Organization getOrganization() {
		
		String query = "FROM Organization";
		@SuppressWarnings("unchecked")
		List<Organization> foundOrganizations = getHibernateTemplate().find(query);
		
		if (foundOrganizations.size() == 1) {
			return foundOrganizations.get(0);
		} else if (foundOrganizations.size() == 0) {
			throw new IllegalStateException("Nu s-a gasit nici o organizatie.");
		} else {
			throw new IllegalStateException("S-au gasit mai multe organizatii in aplicatie.");
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<Organization> getOrganizationsWhereUserIsManager(Long userId) {
		String query =
			"SELECT organization " +
			"FROM Organization organization " +
			"JOIN organization.organizationManager manager " +
			"WHERE manager.id = ?";
		return getHibernateTemplate().find(query, userId);
	}

	public void setOrganizationName(String organizationName) {
		this.organizationName = organizationName;
	}
}