package ro.taxiApp.docs.dao.organizaiton;

import java.util.List;

import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import ro.taxiApp.docs.domain.organization.Role;
import ro.taxiApp.docs.presentation.client.shared.model.organization.RolePermissionMappingViewModel;

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
	public Role getRoleByName(String roleName) {
		String query = "SELECT role FROM Role role WHERE role.name LIKE ? ";
		List<Role> roles = getHibernateTemplate().find(query, roleName);
		if (roles != null) {
			return roles.get(0);
		} else {
			return null;
		}
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<RolePermissionMappingViewModel> getAllRolePermissionMappingViews() {
		
		StringBuilder query = new StringBuilder();
		query.append(" SELECT new " + RolePermissionMappingViewModel.class.getName() + "(r.name, pg.label, p.label, p.description)");
		query.append(" FROM Role r ");
		query.append(" JOIN r.permissions p ");
		query.append(" JOIN p.permissionGroup pg ");
		query.append(" ORDER BY r.name ");
		
		return getHibernateTemplate().find(query.toString());
	}
}
