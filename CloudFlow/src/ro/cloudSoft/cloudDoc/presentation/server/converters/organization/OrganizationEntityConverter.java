package ro.cloudSoft.cloudDoc.presentation.server.converters.organization;

import ro.cloudSoft.cloudDoc.domain.organization.Group;
import ro.cloudSoft.cloudDoc.domain.organization.OrganizationEntity;
import ro.cloudSoft.cloudDoc.domain.organization.OrganizationUnit;
import ro.cloudSoft.cloudDoc.domain.organization.User;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.OrganizationEntityModel;

public class OrganizationEntityConverter 
{
	
	public static OrganizationEntityModel getModelFromOrganizationEntity(OrganizationEntity oe) 
	{
		OrganizationEntityModel model = new OrganizationEntityModel();
		model.setId(oe.getId());
		
		if ( oe instanceof User )
		{
			model.setType(OrganizationEntityModel.TYPE_USER);
		}
		else if ( oe instanceof OrganizationUnit )
		{
			model.setType(OrganizationEntityModel.TYPE_ORG_UNIT);
		}
		else if ( oe instanceof Group )
		{
			model.setType(OrganizationEntityModel.TYPE_GROUP);
		}
		
		return model;
	}
	
	public static OrganizationEntity getOrganizationEntityFromModel(OrganizationEntityModel model) 
	{
		OrganizationEntity oe = null;
		switch ( model.getType().intValue() )
		{
			case OrganizationEntityModel.TYPE_USER:
				oe = new User();
				break;
			case OrganizationEntityModel.TYPE_ORG_UNIT:
				oe = new OrganizationUnit();
				break;
			case OrganizationEntityModel.TYPE_GROUP:
				oe = new Group();
				break;
		}
		oe.setId( model.getId() );
		return oe;
	}
}