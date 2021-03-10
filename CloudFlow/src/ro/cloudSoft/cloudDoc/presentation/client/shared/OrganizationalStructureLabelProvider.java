package ro.cloudSoft.cloudDoc.presentation.client.shared;

import ro.cloudSoft.cloudDoc.presentation.client.shared.model.organization.OrganizationModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.organization.OrganizationUnitModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.organization.UserModel;

import com.extjs.gxt.ui.client.data.ModelData;
import com.extjs.gxt.ui.client.data.ModelStringProvider;

/**
 * Pune la dispozitie label-uri pentru o structura organizatorica.
 */
public class OrganizationalStructureLabelProvider implements ModelStringProvider<ModelData> {
	
	@Override
	public String getStringValue(ModelData model, String property) {
		if (model instanceof OrganizationModel) {
			return ((OrganizationModel) model).getName();
		} else if (model instanceof OrganizationUnitModel) {
			return ((OrganizationUnitModel) model).getName();
		} else if (model instanceof UserModel) {
			return ((UserModel) model).getDisplayName();
		} else {
			return null;
		}
	}
}