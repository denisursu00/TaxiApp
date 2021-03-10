package ro.cloudSoft.cloudDoc.presentation.server.converters.organization;

import java.util.ArrayList;
import java.util.List;

import ro.cloudSoft.cloudDoc.domain.organization.Organization;
import ro.cloudSoft.cloudDoc.domain.organization.OrganizationUnit;
import ro.cloudSoft.cloudDoc.domain.organization.User;
import ro.cloudSoft.cloudDoc.domain.security.SecurityManager;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.organization.OrganizationUnitModel;
import ro.cloudSoft.cloudDoc.services.organization.OrganizationService;
import ro.cloudSoft.cloudDoc.services.organization.OrganizationUnitService;

public class OrganizationUnitConverter {

	public static OrganizationUnitModel getModelFromOrganizationUnit(OrganizationUnit organizationUnit) {
		
    	OrganizationUnitModel organizationUnitModel = new OrganizationUnitModel(organizationUnit.getId().toString(), organizationUnit.getName());
    	
    	if (organizationUnit.getParentOu() != null) {
    		String parentOrganizationUnitIdAsString = organizationUnit.getParentOu().getId().toString();
    		organizationUnitModel.setParentOrganizationUnitId(parentOrganizationUnitIdAsString);
    	} else if (organizationUnit.getOrganization() != null) {
    		String parentOrganizationIdAsString = organizationUnit.getOrganization().getId().toString();
    		organizationUnitModel.setParentOrganizationId(parentOrganizationIdAsString);
    	} else {
    		throw new IllegalArgumentException("O unitate organizatorica trebuie sa aiba un parinte.");
    	}
    	
    	organizationUnitModel.setDescription(organizationUnit.getDescription());
    	if (organizationUnit.getManager() != null){
	    	if (organizationUnit.getManager().getId() != null)
	    		organizationUnitModel.setManagerId(organizationUnit.getManager().getId().toString());
    	}
    	return organizationUnitModel;
    }

    public static OrganizationUnit getOrganizationUnitFromModel(OrganizationUnitModel organizationUnitModel,
    		OrganizationService organizationService, OrganizationUnitService organizationUnitService,
    		SecurityManager userSecurity) {
    	
    	OrganizationUnit organizationUnit = new OrganizationUnit();
    	
    	// nu trebuie setat id cand merge pe adaugare unit. org. de aceea pun conditia
    	if (organizationUnitModel.getId() != null && !organizationUnitModel.getId().equals("")) {
    		organizationUnit.setId(new Long(organizationUnitModel.getId()));
    	}
    	
    	if (organizationUnitModel.getParentOrganizationUnitId() != null) {
    		
    		Long parentOrganizationUnitId = Long.valueOf(organizationUnitModel.getParentOrganizationUnitId());
        	OrganizationUnit parentOrganizationUnit = organizationUnitService.getOrganizationUnitById(parentOrganizationUnitId);
        	
        	if (parentOrganizationUnit != null) {
        		organizationUnit.setParentOu(parentOrganizationUnit);
        	} else {
        		throw new IllegalStateException("Nu s-a gasit unitatea organizatorica cu ID-ul [" + parentOrganizationUnitId + "].");
        	}
    	} else if (organizationUnitModel.getParentOrganizationId() != null) {
        	
        	Long parentOrganizationId = Long.valueOf(organizationUnitModel.getParentOrganizationId());
        	Organization parentOrganization = organizationService.getOrganizationById(parentOrganizationId);
        	
        	if (parentOrganization != null) {
        		organizationUnit.setOrganization(parentOrganization);
        	} else {
        		throw new IllegalStateException("Nu s-a gasit organizatia cu ID-ul [" + parentOrganizationId + "].");
        	}
    	} else {
    		throw new IllegalArgumentException("O unitate organizatorica trebuie sa aiba un parinte.");
    	}
    	
    	organizationUnit.setName(organizationUnitModel.getName());
    	organizationUnit.setDescription(organizationUnitModel.getDescription());
    	
    	if (organizationUnitModel.getManagerId() != null){
    		organizationUnit.setManager(new User());
    		organizationUnit.getManager().setId(new Long(organizationUnitModel.getManagerId()));
    	}
    	
    	return organizationUnit;
    }

    public static List<OrganizationUnitModel> getModelListFromOrgUnitList(List<OrganizationUnit> orgUnits){
    	List<OrganizationUnitModel> result = new ArrayList<OrganizationUnitModel>();
    	for (OrganizationUnit orgUnit : orgUnits){
    		OrganizationUnitModel orgUnitModel = getModelFromOrganizationUnit(orgUnit);
    		result.add(orgUnitModel);
    	}
    	return result;
    }  
}