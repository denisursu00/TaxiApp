package ro.cloudSoft.cloudDoc.presentation.server.converters.project;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;

import ro.cloudSoft.cloudDoc.core.constants.NomenclatorConstants;
import ro.cloudSoft.cloudDoc.dao.nomenclator.NomenclatorDao;
import ro.cloudSoft.cloudDoc.dao.nomenclator.NomenclatorValueDao;
import ro.cloudSoft.cloudDoc.dao.project.ProjectDao;
import ro.cloudSoft.cloudDoc.domain.nomenclator.Nomenclator;
import ro.cloudSoft.cloudDoc.domain.nomenclator.NomenclatorValue;
import ro.cloudSoft.cloudDoc.domain.organization.OrganizationEntity;
import ro.cloudSoft.cloudDoc.domain.project.Project;
import ro.cloudSoft.cloudDoc.domain.project.ProjectEstimation;
import ro.cloudSoft.cloudDoc.domain.project.Subactivity;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.OrganizationEntityModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.project.ProjectComisiiSauGlViewModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.project.ProjectModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.project.ProjectViewModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.project.SubactivityModel;
import ro.cloudSoft.cloudDoc.presentation.server.converters.organization.OrganizationEntityConverter;
import ro.cloudSoft.cloudDoc.services.organization.UserService;
import ro.cloudSoft.cloudDoc.utils.nomenclator.NomenclatorValueUtils;

public class ProjectConverter {
	
	private UserService userService;
	private ProjectDao projectDao;
	private ProjectEstimationConverter projectEstimationConverter;
	private NomenclatorValueDao nomenclatorValueDao;
	private NomenclatorDao nomenclatorDao;
	
	public Project toEntity(ProjectModel model) {
		
		Project entity = new Project();
		if (model.getId() == null) {
			entity = new Project();
		} else {
			entity = projectDao.getById(model.getId());
		}
		
		entity.setId(model.getId());
		entity.setName(model.getName());
		entity.setDescription(model.getDescription());
		entity.setProjectAbbreviation(model.getProjectAbbreviation());
		entity.setDocumentId(model.getDocumentId());
		entity.setDocumentLocationRealName(model.getDocumentLocationRealName());
		entity.setInitiator(userService.getUserById(model.getInitiatorId()));
		entity.setResponsibleUser(userService.getUserById(model.getResponsibleUserId()));
		entity.setStartDate(model.getStartDate());
		entity.setEndDate(model.getEndDate());
		entity.setStatus(model.getStatus());
		entity.setType(model.getType());
		entity.setImplementationDate(model.getImplementationDate());
		if (model.getImportance() != null) {
			Nomenclator nomenclatorGradImportanta = nomenclatorDao.findByCode(NomenclatorConstants.NOMENCLATOR_CODE_IMPORTANTA_PROIECTE);
			List<NomenclatorValue> nomenclatorValueList = nomenclatorValueDao.findByNomenclatorIdAndAttribute(nomenclatorGradImportanta.getId(), NomenclatorConstants.IMPORTANTA_PROIECTE_ATTRIBUTE_KEY_VALOARE_GRAD_IMPORTANTA, model.getImportance().toString());
			if (CollectionUtils.isNotEmpty(nomenclatorValueList)) {
						entity.setGradImportanta(nomenclatorValueList.get(0));
			}
		}
		
		List<OrganizationEntity> participants = new ArrayList<OrganizationEntity>();
		for (OrganizationEntityModel organizationEntityModel : model.getParticipants()) {
			OrganizationEntity organizationEntity = OrganizationEntityConverter.getOrganizationEntityFromModel(organizationEntityModel);
			participants.add(organizationEntity);
		}
		entity.setParticipants(participants);
		
		if (model.getId() == null) {
			
			List<ProjectEstimation> estimations = new ArrayList<>();
			
			ProjectEstimation estimation = new ProjectEstimation();
			estimation.setStartDate(model.getStartDate());
			estimation.setEstimationInPercent(0);
			estimation.setProject(entity);
			
			estimations.add(estimation);
			
			entity.setEstimations(estimations);
		} else {
			entity.setEstimations(projectEstimationConverter.toEntities(model.getEstimations(), entity));
		}
		
		List<NomenclatorValue> comisiiSauGl = new ArrayList<>();
		for (Long valueId: model.getComisiiSauGlIds()) {
			comisiiSauGl.add(nomenclatorValueDao.find(valueId));
		}
		entity.setComisiiSauGl(comisiiSauGl);
		
		List<Subactivity> subactivities;
		if (model.getId() == null) {
			subactivities = new ArrayList<>();
		}else {
			subactivities = entity.getSubactivities();
			Boolean found;
			for (int i = 0; i < subactivities.size() ; i++) {
				found = false;
				for (SubactivityModel subactivityModel: model.getSubactivities()) {
					if (subactivities.get(i).getId().equals(subactivityModel.getId())) {
						subactivities.get(i).setName(subactivityModel.getName());
						found = true;
					}
				}
				if (!found) { subactivities.remove(i); }
			}
		}
		for(SubactivityModel subactivityModel: model.getSubactivities()) {
			if (subactivityModel.getId() == null) {
				Subactivity subactivity = new Subactivity();
				subactivity.setName(subactivityModel.getName());
				subactivity.setProject(entity);
				subactivities.add(subactivity);
			}
		}
		entity.setSubactivities(subactivities);
		if (model.getImportance() != null) {
			List<NomenclatorValue> values = nomenclatorValueDao.findByNomenclatorCodeAndAttribute(NomenclatorConstants.NOMENCLATOR_CODE_IMPORTANTA_PROIECTE, 
					NomenclatorConstants.IMPORTANTA_PROIECTE_ATTRIBUTE_KEY_VALOARE_GRAD_IMPORTANTA, 
					model.getImportance().toString());
			for( NomenclatorValue value : values ) {
				if (value.getAttribute2().equals(model.getImportance().toString())) {
					entity.setGradImportanta(value);
				}
			}
		}else {
			entity.setGradImportanta(null);
		}
		
		return entity;
	}
	
	public ProjectModel toModel(Project entity) {
		ProjectModel model = new ProjectModel();
		model.setId(entity.getId());
		model.setName(entity.getName());
		model.setDescription(entity.getDescription());
		model.setProjectAbbreviation(entity.getProjectAbbreviation());
		model.setDocumentId(entity.getDocumentId());
		model.setDocumentLocationRealName(entity.getDocumentLocationRealName());
		model.setInitiatorId(entity.getInitiator().getId());
		model.setResponsibleUserId(entity.getResponsibleUser().getId());
		model.setStartDate(entity.getStartDate());
		model.setEndDate(entity.getEndDate());
		model.setStatus(entity.getStatus());
		model.setType(entity.getType());
		model.setImplementationDate(entity.getImplementationDate());

		if (entity.getGradImportanta() != null) {
			model.setImportance(Integer.parseInt(NomenclatorValueUtils.getAttributeValueAsString(entity.getGradImportanta(), NomenclatorConstants.IMPORTANTA_PROIECTE_ATTRIBUTE_KEY_VALOARE_GRAD_IMPORTANTA)));
		}
		
		List<OrganizationEntityModel> participants = new ArrayList<OrganizationEntityModel>();
		for (OrganizationEntity organizationEntity : entity.getParticipants()) {
			OrganizationEntityModel organizationEntityModel = new OrganizationEntityModel();
			organizationEntityModel = OrganizationEntityConverter.getModelFromOrganizationEntity(organizationEntity);
			participants.add(organizationEntityModel);
		}
		model.setParticipants(participants);
		model.setEstimations(projectEstimationConverter.toModels(entity.getEstimations()));
		
		List<Long> comisiiSauGlValueIds = new ArrayList<>();
		for (NomenclatorValue nomenclatorValue : entity.getComisiiSauGl()) {
			comisiiSauGlValueIds.add(nomenclatorValue.getId());
		}
		model.setComisiiSauGlIds(comisiiSauGlValueIds);
		model.setSubactivities(entity.getSubactivities().stream().map(subactivity -> new SubactivityModel(subactivity.getId(), subactivity.getName())).collect(Collectors.toList()));
	
		return model;
	}
	
	public ProjectViewModel toViewModel(Project entity) {
		
		ProjectViewModel model = new ProjectViewModel();
		model.setId(entity.getId());
		model.setName(entity.getName());
		model.setDescription(entity.getDescription());
		model.setProjectAbbreviation(entity.getProjectAbbreviation());
		model.setDocumentId(entity.getDocumentId());
		model.setDocumentLocationRealName(entity.getDocumentLocationRealName());
		model.setInitiatorId(entity.getInitiator().getId());
		model.setResponsibleUserId(entity.getResponsibleUser().getId());
		model.setStartDate(entity.getStartDate());
		model.setEndDate(entity.getEndDate());
		model.setStatus(entity.getStatus());
		model.setType(entity.getType());
		model.setImplementationDate(entity.getImplementationDate());
		if (entity.getGradImportanta() != null) {
			model.setImportance(Integer.parseInt(NomenclatorValueUtils.getAttributeValueAsString(entity.getGradImportanta(), NomenclatorConstants.IMPORTANTA_PROIECTE_ATTRIBUTE_KEY_VALOARE_GRAD_IMPORTANTA)));
		}
		List<OrganizationEntityModel> participants = new ArrayList<OrganizationEntityModel>();
		for (OrganizationEntity organizationEntity : entity.getParticipants()) {
			OrganizationEntityModel organizationEntityModel = new OrganizationEntityModel();
			organizationEntityModel = OrganizationEntityConverter.getModelFromOrganizationEntity(organizationEntity);
			participants.add(organizationEntityModel);
		}
		model.setParticipants(participants);
		model.setEstimations(projectEstimationConverter.toModels(entity.getEstimations()));
		
		List<ProjectComisiiSauGlViewModel> comisiiSauGl = new ArrayList<>();
		for (NomenclatorValue nomenclatorValue : entity.getComisiiSauGl()) {
			ProjectComisiiSauGlViewModel comisieSauGlViewModel = new ProjectComisiiSauGlViewModel();
			comisieSauGlViewModel.setId(nomenclatorValue.getId());
			comisieSauGlViewModel.setDenumire(NomenclatorValueUtils.getAttributeValueAsString(nomenclatorValue, NomenclatorConstants.COMISII_SAU_GL_ATTRIBUTE_KEY_DENUMIRE));
			comisiiSauGl.add(comisieSauGlViewModel);
		}
		model.setComisiiSauGl(comisiiSauGl);
		model.setSubactivities(entity.getSubactivities().stream().map(subactivity -> new SubactivityModel(subactivity.getId(), subactivity.getName())).collect(Collectors.toList()));
		return model;
	}
	
	public List<ProjectModel> toModels(List<Project> entities) {
		List<ProjectModel> models = new ArrayList<ProjectModel>();
		for (Project entity : entities) {
			models.add(toModel(entity));
		}
		return models;
	}
	
	public void setUserService(UserService userService) {
		this.userService = userService;
	}
	
	public void setProjectDao(ProjectDao projectDao) {
		this.projectDao = projectDao;
	}
	
	public void setProjectEstimationConverter(ProjectEstimationConverter projectEstimationConverter) {
		this.projectEstimationConverter = projectEstimationConverter;
	}
	
	public void setNomenclatorValueDao(NomenclatorValueDao nomenclatorValueDao) {
		this.nomenclatorValueDao = nomenclatorValueDao;
	}

	public void setNomenclatorDao(NomenclatorDao nomenclatorDao) {
		this.nomenclatorDao = nomenclatorDao;
	}
	
}
