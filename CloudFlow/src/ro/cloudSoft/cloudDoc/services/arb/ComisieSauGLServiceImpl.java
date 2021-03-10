package ro.cloudSoft.cloudDoc.services.arb;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.transaction.annotation.Transactional;

import ro.cloudSoft.cloudDoc.core.AppException;
import ro.cloudSoft.cloudDoc.core.constants.GroupConstants;
import ro.cloudSoft.cloudDoc.core.constants.NomenclatorConstants;
import ro.cloudSoft.cloudDoc.dao.arb.ReprezentantiComisieSauGLDao;
import ro.cloudSoft.cloudDoc.domain.arb.DiplomaMembruReprezentantiComisieSauGL;
import ro.cloudSoft.cloudDoc.domain.arb.MembruReprezentantiComisieSauGL;
import ro.cloudSoft.cloudDoc.domain.arb.ReprezentantiComisieSauGL;
import ro.cloudSoft.cloudDoc.domain.nomenclator.NomenclatorValue;
import ro.cloudSoft.cloudDoc.domain.organization.Group;
import ro.cloudSoft.cloudDoc.domain.organization.User;
import ro.cloudSoft.cloudDoc.plugins.organization.GroupPersistencePlugin;
import ro.cloudSoft.cloudDoc.plugins.organization.UserPersistencePlugin;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.arb.MembruReprezentantiComisieSauGLInfoModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.arb.ReprezentantiComisieSauGLModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.nomenclator.NomenclatorValueModel;
import ro.cloudSoft.cloudDoc.presentation.server.converters.arb.ReprezentantiComisieSauGLConverter;
import ro.cloudSoft.cloudDoc.presentation.server.converters.nomenclator.NomenclatorValueConverter;
import ro.cloudSoft.cloudDoc.services.organization.GroupService;
import ro.cloudSoft.cloudDoc.utils.nomenclator.NomenclatorValueUtils;
import ro.cloudSoft.common.utils.DependencyInjectionUtils;

public class ComisieSauGLServiceImpl implements ComisieSauGLService, InitializingBean {
	
	private ReprezentantiComisieSauGLDao reprezentantiComisieSauGLDao;
	private ReprezentantiComisieSauGLConverter reprezentantiComisieSauGLConverter;
	private GroupService groupService;
	private GroupPersistencePlugin groupPersistencePlugin;
	private UserPersistencePlugin userPersistencePlugin;
	private NomenclatorValueConverter nomenclatorValueConverter;
	
	@Override
	public void afterPropertiesSet() throws Exception {
		DependencyInjectionUtils.checkRequiredDependencies(
				reprezentantiComisieSauGLDao, 
				reprezentantiComisieSauGLConverter,
				nomenclatorValueConverter
		);
	}
	
	@Override
	@Transactional
	public void saveReprezentanti(ReprezentantiComisieSauGLModel model) throws AppException {
		
		List<Long> persistedMemberIds = new ArrayList<Long>();
		List<Long> persistedDiplomeIds = new ArrayList<>();
		Long oldResponsabilArbId = null;
		if (model.getId() != null) {
			ReprezentantiComisieSauGL persistedEntity = reprezentantiComisieSauGLDao.getById(model.getId());
			if (persistedEntity.getResponsabilARB() != null) {
				oldResponsabilArbId = persistedEntity.getResponsabilARB().getId();
			}
			for (MembruReprezentantiComisieSauGL membru : persistedEntity.getMembri()) {
				persistedMemberIds.add(membru.getId());
				List<DiplomaMembruReprezentantiComisieSauGL> diplome = membru.getDiplome();
				if (CollectionUtils.isNotEmpty(diplome)) {
					for (DiplomaMembruReprezentantiComisieSauGL diploma : diplome) {
						persistedDiplomeIds.add(diploma.getId());
					}
				}
			}
		}		
		
		ReprezentantiComisieSauGL newOrUpdatedEntity = reprezentantiComisieSauGLConverter.toEntity(model);
		
		if (CollectionUtils.isNotEmpty(persistedMemberIds)) {					

			List<Long> remainedMembriIds = new ArrayList<>();
			List<Long> remainedDiplomeIds = new ArrayList<>();
			
			for (MembruReprezentantiComisieSauGL newOrUpdatedEntityMembru : newOrUpdatedEntity.getMembri()) {
				if (newOrUpdatedEntityMembru.getId() != null) {
					remainedMembriIds.add(newOrUpdatedEntityMembru.getId());
				}
				List<DiplomaMembruReprezentantiComisieSauGL> newOrUpdatedDiplome = newOrUpdatedEntityMembru.getDiplome();
				if (CollectionUtils.isNotEmpty(newOrUpdatedDiplome)) {
					for (DiplomaMembruReprezentantiComisieSauGL newOrUpdatedDiploma : newOrUpdatedEntityMembru.getDiplome()) {
						if (newOrUpdatedDiploma.getId() != null) {
							remainedDiplomeIds.add(newOrUpdatedDiploma.getId());
						}
					}
				}				
			}			
			if (CollectionUtils.isNotEmpty(remainedDiplomeIds)) {
				persistedDiplomeIds.removeAll(remainedDiplomeIds);							
			}	
			if (CollectionUtils.isNotEmpty(persistedDiplomeIds)) {
				reprezentantiComisieSauGLDao.deleteDiplomeMembri(persistedDiplomeIds);
			}	
			if (CollectionUtils.isNotEmpty(remainedMembriIds)) {
				persistedMemberIds.removeAll(remainedMembriIds);				
			}			
			if (CollectionUtils.isNotEmpty(persistedMemberIds)) {
				reprezentantiComisieSauGLDao.deleteMembri(persistedMemberIds);					
			}
		}
		
		reprezentantiComisieSauGLDao.save(newOrUpdatedEntity);
		
		Long newResponsabilArbId = model.getResponsabilARBId();		

		if (	(oldResponsabilArbId != null) 
					&& 
				( (newResponsabilArbId == null) || !oldResponsabilArbId.equals(newResponsabilArbId) )  ) {
			Boolean existaInAlteComisii = reprezentantiComisieSauGLDao.existsResponsabilArbInAllReprezentanti(oldResponsabilArbId);
			if (!existaInAlteComisii) {
				Group group = groupService.getGroupByName(GroupConstants.GROUP_NAME_RESPONSABILI_GL_COMISIE);
				Iterator<User> userIterator = group.getUsers().iterator();
				while (userIterator.hasNext()) {
					User user = userIterator.next();
					if (user.getId().equals(oldResponsabilArbId)) {
						userIterator.remove();
					}
				}
				groupPersistencePlugin.setGroup(group);
			} 
		}
		if (	(newResponsabilArbId != null)
					&&
				( (oldResponsabilArbId == null) || !oldResponsabilArbId.equals(newResponsabilArbId) )   ) {
			Group group = groupService.getGroupByName(GroupConstants.GROUP_NAME_RESPONSABILI_GL_COMISIE);
			boolean newUserAllreadyExistsInGroup = false;
			for (User user : group.getUsers()) {
				if (user.getId().equals(newResponsabilArbId)) {
					newUserAllreadyExistsInGroup = true;
				}
			}
			if (!newUserAllreadyExistsInGroup) {
				User newUser = userPersistencePlugin.getUserById(newResponsabilArbId);
				group.getUsers().add(newUser);
				groupPersistencePlugin.setGroup(group);
			} 
		}
	}
	
	@Override
	public ReprezentantiComisieSauGLModel getReprezentantiByComisieSauGLId(Long comisieSauGLId) {
		ReprezentantiComisieSauGL entity = reprezentantiComisieSauGLDao.getByComisieSauGLId(comisieSauGLId);
		if (entity != null) {
			return reprezentantiComisieSauGLConverter.toModel(entity);
		}
		return null;
	}
	
	@Override
	public List<NomenclatorValueModel> getAllInstitutiiOfMembriiComisieSauGL(Long comisieSauGLId) throws AppException {
		List<NomenclatorValue> institutii = reprezentantiComisieSauGLDao.getAllInstitutiiOfMembriiComisieSauGL(comisieSauGLId);		
		return nomenclatorValueConverter.getModels(institutii);
	}
	
	@Override
	public ReprezentantiComisieSauGLModel getReprezentantiById(Long id) {
		ReprezentantiComisieSauGL entity = reprezentantiComisieSauGLDao.getById(id);
		return reprezentantiComisieSauGLConverter.toModel(entity);
	}
	
	@Override
	public List<MembruReprezentantiComisieSauGLInfoModel> getMembriiReprezentantiComisieSauGLByInstitutie(Long comisieSauGLId, Long institutieId) throws AppException {
		List<MembruReprezentantiComisieSauGLInfoModel> membriModels = new ArrayList<>();
		List<MembruReprezentantiComisieSauGL> membri = this.reprezentantiComisieSauGLDao.getMembriiReprezentantiComisieSauGLByInstitutie(comisieSauGLId, institutieId);
		if (CollectionUtils.isNotEmpty(membri)) {
			for (MembruReprezentantiComisieSauGL m : membri) {
				MembruReprezentantiComisieSauGLInfoModel infoModel = new MembruReprezentantiComisieSauGLInfoModel();
				NomenclatorValue mInstitutie = m.getMembruInstitutie();
				if (mInstitutie != null) {
					infoModel.setNume(NomenclatorValueUtils.getAttributeValueAsString(mInstitutie, NomenclatorConstants.PERSOANE_ATTRIBUTE_KEY_NUME));
					infoModel.setPrenume(NomenclatorValueUtils.getAttributeValueAsString(mInstitutie, NomenclatorConstants.PERSOANE_ATTRIBUTE_KEY_PRENUME));
					infoModel.setDepartament(NomenclatorValueUtils.getAttributeValueAsString(mInstitutie, NomenclatorConstants.PERSOANE_ATTRIBUTE_KEY_DEPARTAMENT));
					infoModel.setTelefon(NomenclatorValueUtils.getAttributeValueAsString(mInstitutie, NomenclatorConstants.PERSOANE_ATTRIBUTE_KEY_TELEFON));
					infoModel.setFunctie(NomenclatorValueUtils.getAttributeValueAsString(mInstitutie, NomenclatorConstants.PERSOANE_ATTRIBUTE_KEY_FUNCTIE));
					infoModel.setEmail(NomenclatorValueUtils.getAttributeValueAsString(mInstitutie, NomenclatorConstants.PERSOANE_ATTRIBUTE_KEY_EMAIL));
				} else {
					infoModel.setNume(m.getNume());
					infoModel.setPrenume(m.getPrenume());
					infoModel.setDepartament(m.getDepartament());
					infoModel.setTelefon(m.getTelefon());
					infoModel.setFunctie(m.getFunctie());
					infoModel.setEmail(m.getEmail());
				}
				infoModel.setCalitate(m.getCalitate().name());
				membriModels.add(infoModel);
			}
		}
		return membriModels;
	}
	
	public void setReprezentantiComisieSauGLDao(ReprezentantiComisieSauGLDao reprezentantiComisieSauGLDao) {
		this.reprezentantiComisieSauGLDao = reprezentantiComisieSauGLDao;
	}
	
	public void setReprezentantiComisieSauGLConverter(
			ReprezentantiComisieSauGLConverter reprezentantiComisieSauGLConverter) {
		this.reprezentantiComisieSauGLConverter = reprezentantiComisieSauGLConverter;
	}

	public void setGroupService(GroupService groupService) {
		this.groupService = groupService;
	}

	public void setGroupPersistencePlugin(GroupPersistencePlugin groupPersistencePlugin) {
		this.groupPersistencePlugin = groupPersistencePlugin;
	}

	public void setUserPersistencePlugin(UserPersistencePlugin userPersistencePlugin) {
		this.userPersistencePlugin = userPersistencePlugin;
	}
	
	public void setNomenclatorValueConverter(NomenclatorValueConverter nomenclatorValueConverter) {
		this.nomenclatorValueConverter = nomenclatorValueConverter;
	}	
}
