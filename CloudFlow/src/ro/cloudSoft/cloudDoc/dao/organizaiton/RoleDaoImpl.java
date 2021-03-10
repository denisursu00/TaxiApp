package ro.cloudSoft.cloudDoc.dao.organizaiton;

import java.util.List;

import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import ro.cloudSoft.cloudDoc.domain.organization.Role;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.organization.RolePermissionMappingViewModel;

public class RoleDaoImpl extends HibernateDaoSupport implements RoleDao {

	@SuppressWarnings("unchecked")
	@Override
	public List<String> getAllRoleNames() {
		String query = "SELECT role.name FROM Role role";
		return getHibernateTemplate().find(query);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Role> getAllRoles() {
		return getHibernateTemplate().find("FROM Role");
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<RolePermissionMappingViewModel> getAllRolePermissionMappingViews() {
		
		StringBuilder query = new StringBuilder();
		query.append(" SELECT new " + RolePermissionMappingViewModel.class.getName() + "(r.name, pg.label, p.label, p.description)");
		query.append(" FROM Role r ");
		query.append(" JOIN r.permissions p ");
		query.append(" JOIN p.permissionGroup pg ");
		query.append(" ORDER BY r.name, pg.uiOrder, p.uiOrder ");
		
		return getHibernateTemplate().find(query.toString());
	}
}
