package ro.taxiApp.docs.presentation.server.converters.organization;

import ro.taxiApp.docs.domain.organization.Role;
import ro.taxiApp.docs.presentation.client.shared.model.organization.RoleModel;

public class RoleConverter {

	public static RoleModel getModelFromEntity(Role entity) {
		RoleModel model = new RoleModel();
		model.setId(entity.getId());
		model.setName(entity.getName());
		model.setDescription(entity.getDescription());
		return model;
	}
	
	public static Role getEntityFromModel(RoleModel model) {
		Role entiy = new Role();
		entiy.setId(model.getId());
		entiy.setName(model.getName());
		entiy.setDescription(model.getDescription());
		return entiy;
	}
}
