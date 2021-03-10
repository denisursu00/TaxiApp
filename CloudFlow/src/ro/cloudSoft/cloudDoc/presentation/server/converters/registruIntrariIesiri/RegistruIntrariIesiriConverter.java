package ro.cloudSoft.cloudDoc.presentation.server.converters.registruIntrariIesiri;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.apache.commons.lang.StringUtils;

import ro.cloudSoft.cloudDoc.core.constants.NomenclatorConstants;
import ro.cloudSoft.cloudDoc.dao.nomenclator.NomenclatorValueDao;
import ro.cloudSoft.cloudDoc.dao.project.ProjectDao;
import ro.cloudSoft.cloudDoc.dao.registruIntrariIesiri.RegistruIntrariIesiriDao;
import ro.cloudSoft.cloudDoc.domain.AtasamentModel;
import ro.cloudSoft.cloudDoc.domain.nomenclator.NomenclatorValue;
import ro.cloudSoft.cloudDoc.domain.project.Project;
import ro.cloudSoft.cloudDoc.domain.project.Subactivity;
import ro.cloudSoft.cloudDoc.domain.registruIntrariIesiri.RegistruIesiri;
import ro.cloudSoft.cloudDoc.domain.registruIntrariIesiri.RegistruIesiriAtasament;
import ro.cloudSoft.cloudDoc.domain.registruIntrariIesiri.RegistruIesiriDestinatar;
import ro.cloudSoft.cloudDoc.domain.registruIntrariIesiri.RegistruIntrari;
import ro.cloudSoft.cloudDoc.domain.registruIntrariIesiri.RegistruIntrariAtasament;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.project.SubactivityModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.registruIntrariIesiri.RegistruIesiriDestinatarModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.registruIntrariIesiri.RegistruIesiriDestinatarViewModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.registruIntrariIesiri.RegistruIesiriModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.registruIntrariIesiri.RegistruIesiriViewModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.registruIntrariIesiri.RegistruIntrariModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.registruIntrariIesiri.RegistruIntrariViewModel;
import ro.cloudSoft.cloudDoc.services.organization.UserService;
import ro.cloudSoft.cloudDoc.utils.nomenclator.NomenclatorValueUtils;

public class RegistruIntrariIesiriConverter {

	private static final String COMMA_SEPARATOR = ", ";

	private RegistruIntrariIesiriDao registruIntrariIesiriDao;
	private NomenclatorValueDao nomenclatorValueDao;
	private ProjectDao projectDao;
	private UserService userService;

	public void setRegistruIntrariIesiriDao(RegistruIntrariIesiriDao registruIntrariIesiriDao) {
		this.registruIntrariIesiriDao = registruIntrariIesiriDao;
	}

	public void setNomenclatorValueDao(NomenclatorValueDao nomenclatorValueDao) {
		this.nomenclatorValueDao = nomenclatorValueDao;
	}

	public void setProjectDao(ProjectDao projectDao) {
		this.projectDao = projectDao;
	}

	public void setUserService(UserService userService) {
		this.userService = userService;
	}

	public RegistruIntrariModel getIntrariModelFromEntity(RegistruIntrari entity) {

		RegistruIntrariModel model = new RegistruIntrariModel();

		model.setId(entity.getId());
		model.setNumarInregistrare(entity.getNumarInregistrare());
		model.setDataInregistrare(entity.getDataInregistrare());
		model.setNumeEmitent(entity.getNumeEmitent());
		if (entity.getEmitent() != null) {
			model.setEmitentId(entity.getEmitent().getId());
		}
		model.setDepartamentEmitent(entity.getDepartamentEmitent());
		model.setNumarDocumentEmitent(entity.getNumarDocumentEmitent());
		model.setDataDocumentEmitent(entity.getDataDocumentEmitent());
		model.setTipDocumentId(entity.getTipDocument().getId());
		model.setCodTipDocument(entity.getTipDocument().getAttribute1());
		model.setTrimisPeMail(entity.isTrimisPeMail());
		model.setContinut(entity.getContinut());
		model.setNumarPagini(entity.getNumarPagini());
		model.setNumarAnexe(entity.getNumarAnexe());
		model.setRepartizatCatreUserId(entity.getRepartizatCatre().getId());
		model.setComisieSauGLIds(getComisieSauGLIdsFromEntity(entity));
		model.setProiectIds(getProjectIdsFromEntity(entity));
		model.setNecesitaRaspuns(entity.isNecesitaRaspuns());
		model.setTermenRaspuns(entity.getTermenRaspuns());
		model.setNumarInregistrareOfRegistruIesiri(entity.getRegistruIesiriId());
		model.setObservatii(entity.getObservatii());
		model.setRaspunsuriBanciCuPropuneri(entity.getRaspunsuriBanciCuPropuneri());
		model.setNrZileIntrareEmitent(entity.getNrZileIntrareEmitent());
		model.setNrZileRaspunsIntrare(entity.getNrZileRaspunsIntrare());
		model.setNrZileRaspunsEmitent(entity.getNrZileRaspunsEmitent());
		model.setNrZileTermenDataRaspuns(entity.getNrZileTermenDataRaspuns());
		model.setAnulat(entity.isAnulat());
		model.setMotivAnulare(entity.getMotivAnulare());
		model.setInchis(entity.isInchis());
		model.setTrebuieAnulat(entity.isTrebuieAnulat());
		model.setAtasamente(getAtasamenteFromRegIntrariEntity(entity));
		Subactivity subactivity = entity.getSubactivity();
		if (subactivity != null) {
			model.setSubactivity(new SubactivityModel(subactivity.getId(), subactivity.getName()));
		}
		return model;
	}

	public RegistruIesiriModel getIesiriModelFromEntity(RegistruIesiri entity) {

		RegistruIesiriModel model = new RegistruIesiriModel();

		model.setId(entity.getId());
		model.setNumarInregistrare(entity.getNumarInregistrare());
		model.setDataInregistrare(entity.getDataInregistrare());
		model.setTipDocumentId(entity.getTipDocument().getId());
		model.setTipDocumentId(entity.getTipDocument().getId());
		model.setTrimisPeMail(entity.isTrimisPeMail());
		model.setContinut(entity.getContinut());
		model.setNumarPagini(entity.getNumarPagini());
		model.setNumarAnexe(entity.getNumarAnexe());
		model.setIntocmitDeUserId(entity.getIntocmitDe().getId());
		model.setProiectIds(getProjectsIdsFromProjectEntitiesList(entity.getProiecte()));
		model.setAsteptamRaspuns(entity.isAsteptamRaspuns());
		model.setTermenRaspuns(entity.getTermenRaspuns());
		model.setAnulat(model.isAnulat());
		model.setMotivAnulare(model.getMotivAnulare());
		model.setInchis(entity.isInchis());
		model.setTrebuieAnulat(entity.isTrebuieAnulat());
		
		List<RegistruIesiriDestinatarModel> destinatarModels = new ArrayList<>();
		for (RegistruIesiriDestinatar destinatar : entity.getDestinatari()) {
			RegistruIesiriDestinatarModel destinatarModel = new RegistruIesiriDestinatarModel();
			destinatarModel.setId(destinatar.getId());
			if (destinatar.getComisieGl() != null ) {
				destinatarModel.setComisieGlId(destinatar.getComisieGl().getId());
			}
			destinatarModel.setDataInregistrare(destinatar.getDataInregistrare());
			destinatarModel.setDepartament(destinatar.getDepartament());
			destinatarModel.setNumarInregistrare(destinatar.getNumarInregistrare());
			destinatarModel.setNume(destinatar.getNume());
			if (destinatar.getInstitutie() != null) {
				destinatarModel.setInstitutieId(destinatar.getInstitutie().getId());	
			} 
			destinatarModel.setObservatii(destinatar.getObservatii());
			destinatarModel.setRegistruIesiriId(destinatar.getRegistruIesiri().getId());
			if (destinatar.getRegistruIntrari() != null) {
				destinatarModel.setRegistruIntrariId(destinatar.getRegistruIntrari().getId());
			}
			destinatarModels.add(destinatarModel);
		}
		model.setDestinatari(destinatarModels);
		model.setAtasamente(getAtasamenteFromRegIesiriEntity(entity));
		Subactivity subactivity = entity.getSubactivity();
		if (subactivity != null) {
			model.setSubactivity(new SubactivityModel(subactivity.getId(), subactivity.getName()));
		}
		return model;
	}

	public List<Long> getProjectsIdsFromProjectEntitiesList(List<Project> projects) {
		List<Long> projectsIds = new ArrayList<>();
		for (Project project : projects) {
			projectsIds.add(project.getId());
		}
		return projectsIds;
	}

	public List<Long> getComisieSauGLIdsFromEntity(RegistruIntrari entity) {
		List<Long> comisieSauGLIds = new ArrayList<Long>();
		for (NomenclatorValue registruIntrariComisieSauGL : entity.getComisiiSauGL()) {
			comisieSauGLIds.add(registruIntrariComisieSauGL.getId());
		}
		return comisieSauGLIds;
	}

	private List<AtasamentModel> getAtasamenteFromRegIntrariEntity(RegistruIntrari entity) {
		List<AtasamentModel> atasamente = new ArrayList<>();
		for (RegistruIntrariAtasament atasament : entity.getAtasamente()) {
			AtasamentModel atasamentModel = new AtasamentModel();
			atasamentModel.setId(atasament.getId());
			atasamentModel.setFileName(atasament.getFileName());
			atasamente.add(atasamentModel);
		}
		return atasamente;
	}

	private List<AtasamentModel> getAtasamenteFromRegIesiriEntity(RegistruIesiri entity) {
		List<AtasamentModel> atasamente = new ArrayList<>();
		for (RegistruIesiriAtasament atasament : entity.getAtasamente()) {
			AtasamentModel atasamentModel = new AtasamentModel();
			atasamentModel.setId(atasament.getId());
			atasamentModel.setFileName(atasament.getFileName());
			atasamente.add(atasamentModel);
		}
		return atasamente;
	}

	public List<Long> getProjectIdsFromEntity(RegistruIntrari entity) {
		List<Long> proiectIds = new ArrayList<Long>();
		for (Project registruIntrariProiect : entity.getProiecte()) {
			proiectIds.add(registruIntrariProiect.getId());
		}
		return proiectIds;
	}

	public List<RegistruIntrariViewModel> getIntrariViewModelsFromEntities(List<RegistruIntrari> entities) {
		List<RegistruIntrariViewModel> intrariViewModels = new ArrayList<RegistruIntrariViewModel>();
		for (RegistruIntrari intrare : entities) {
			RegistruIntrariViewModel intrareViewModel = getIntrariViewModelFromEntity(intrare);
			intrariViewModels.add(intrareViewModel);
		}
		return intrariViewModels;
	}

	public RegistruIntrariViewModel getIntrariViewModelFromEntity(RegistruIntrari entity) {

		RegistruIntrariViewModel model = new RegistruIntrariViewModel();

		model.setId(entity.getId());
		model.setNumarInregistrare(entity.getNumarInregistrare());
		model.setDataInregistrare(entity.getDataInregistrare());
		NomenclatorValue emitent = entity.getEmitent();
		if (emitent != null) {
			model.setNumeEmitent(NomenclatorValueUtils.getAttributeValueAsString(emitent, NomenclatorConstants.INSTITUTII_ATTRIBUTE_KEY_DENUMIRE_INSTITUTIE));
		} else {
			model.setNumeEmitent(entity.getNumeEmitent());
		}
		model.setDepartamentEmitent(entity.getDepartamentEmitent());
		model.setNumarDocumentEmitent(entity.getNumarDocumentEmitent());
		model.setDataDocumentEmitent(entity.getDataDocumentEmitent());
		model.setTipDocument(entity.getTipDocument().getAttribute2());
		model.setCodTipDocument(entity.getTipDocument().getAttribute1());
		model.setTrimisPeMail(entity.isTrimisPeMail());
		model.setContinut(entity.getContinut());
		model.setNumarPagini(entity.getNumarPagini());
		model.setNumarAnexe(entity.getNumarAnexe());
		model.setRepartizatCatre(
				entity.getRepartizatCatre().getFirstName() + " " + entity.getRepartizatCatre().getLastName());
		model.setComisieSauGL(getComisiiSauGLOfRegistruIntrariFromEntity(entity));
		model.setProiect(getProiecteOfRegistruIntrariFromEntity(entity));
		model.setNecesitaRaspuns(entity.isNecesitaRaspuns());
		model.setTermenRaspuns(entity.getTermenRaspuns());
		if (entity.getRegistruIesiriId() != null) {
			model.setNumarInregistrareOfRegistruIesiri(
					registruIntrariIesiriDao.findIesire(entity.getRegistruIesiriId()).getNumarInregistrare());
		}
		model.setObservatii(entity.getObservatii());
		model.setRaspunsuriBanciCuPropuneri(entity.getRaspunsuriBanciCuPropuneri());
		model.setNrZileIntrareEmitent(entity.getNrZileIntrareEmitent());
		model.setNrZileRaspunsIntrare(entity.getNrZileRaspunsIntrare());
		model.setNrZileRaspunsEmitent(entity.getNrZileRaspunsEmitent());
		model.setNrZileTermenDataRaspuns(entity.getNrZileTermenDataRaspuns());
		model.setAnulat(entity.isAnulat());
		model.setMotivAnulare(entity.getMotivAnulare());
		model.setInchis(entity.isInchis());
		model.setTrebuieAnulat(entity.isTrebuieAnulat());
		model.setAtasamente(getAtasamenteFromRegIntrariEntity(entity));
		Subactivity subactivity = entity.getSubactivity();
		if (subactivity != null) {
			model.setSubactivityName(entity.getSubactivity().getName());
		}
		return model;
	}

	private String getComisiiSauGLOfRegistruIntrariFromEntity(RegistruIntrari entity) {
		List<String> comisiiSauGL = new ArrayList<String>();
		for (NomenclatorValue registruIntrariComisieSauGL : entity.getComisiiSauGL()) {
			comisiiSauGL.add(registruIntrariComisieSauGL.getAttribute2());
		}
		return StringUtils.join(comisiiSauGL, RegistruIntrariIesiriConverter.COMMA_SEPARATOR);
	}

	private String getProiecteOfRegistruIntrariFromEntity(RegistruIntrari entity) {
		List<String> proiecte = new ArrayList<String>();
		for (Project registruIntrariProiect : entity.getProiecte()) {
			proiecte.add(registruIntrariProiect.getName());
		}
		return StringUtils.join(proiecte, RegistruIntrariIesiriConverter.COMMA_SEPARATOR);
	}

	public RegistruIntrari getIntrariEntityFromModel(RegistruIntrariModel model) {

		RegistruIntrari entity = null;
		if (model.getId() != null) {
			entity = registruIntrariIesiriDao.findIntrare(model.getId());
		} else {
			entity = new RegistruIntrari();
			entity.setNumarInregistrare(model.getNumarInregistrare());
			entity.setInchis(model.isInchis());
			entity.setNecesitaRaspuns(model.isNecesitaRaspuns());
			entity.setTermenRaspuns(model.getTermenRaspuns());
			entity.setTipDocument(nomenclatorValueDao.find(model.getTipDocumentId()));
		}
		
		List<NomenclatorValue> comisiiSauGLForSave = new ArrayList<>();
		if (model.getComisieSauGLIds() != null) {
			for (Long comisieSauGLId : model.getComisieSauGLIds()) {
				NomenclatorValue comisieSauGL = nomenclatorValueDao.find(comisieSauGLId);
				comisiiSauGLForSave.add(comisieSauGL);
			}
		}
		entity.setComisiiSauGL(comisiiSauGLForSave);

		List<Project> proiecteForSave = new ArrayList<>();
		for (Long proiectId : model.getProiectIds()) {
			Project project = projectDao.getById(proiectId);
			proiecteForSave.add(project);
		}
		entity.setProiecte(proiecteForSave);
		
		entity.setDataInregistrare(model.getDataInregistrare());
		if (model.getEmitentId() != null) {
			entity.setEmitent(nomenclatorValueDao.find(model.getEmitentId()));
		}else {
			entity.setEmitent(null);
		}
		entity.setNumeEmitent(model.getNumeEmitent());
		entity.setDepartamentEmitent(model.getDepartamentEmitent());
		entity.setTrimisPeMail(model.isTrimisPeMail());
		entity.setContinut(model.getContinut());
		entity.setNumarPagini(model.getNumarPagini());
		entity.setNumarAnexe(model.getNumarAnexe());
		entity.setRepartizatCatre(userService.getUserById(model.getRepartizatCatreUserId()));

		entity.setNumarDocumentEmitent(model.getNumarDocumentEmitent());
		entity.setDataDocumentEmitent(model.getDataDocumentEmitent());
		entity.setRegistruIesiriId(model.getNumarInregistrareOfRegistruIesiri()); 
		entity.setObservatii(model.getObservatii());
		entity.setRaspunsuriBanciCuPropuneri(model.getRaspunsuriBanciCuPropuneri());
		if (model.getMotivAnulare() != null) {
			entity.setMotivAnulare(model.getMotivAnulare());
		}
		
		entity.setNrZileIntrareEmitent(model.getNrZileIntrareEmitent());
		entity.setNrZileRaspunsIntrare(model.getNrZileRaspunsIntrare());
		entity.setNrZileRaspunsEmitent(model.getNrZileRaspunsEmitent());
		entity.setNrZileTermenDataRaspuns(model.getNrZileTermenDataRaspuns());
		
		if (model.getSubactivity() != null) {
			entity.setSubactivity(projectDao.getProjectSubactivities(model.getProiectIds().get(0))
										.stream().filter(subactivity -> subactivity.getId().equals(model.getSubactivity().getId()))
										.findAny().orElse(null));
		}else {
			entity.setSubactivity(null);
		}
		return entity;
	}


	public RegistruIesiri getIesiriEntityFromModel(RegistruIesiriModel model, List<RegistruIntrari> registreIntrariPentruMapareRegistruIesire) {
		
		RegistruIesiri entity = null;
		if (model.getId() != null) {
			entity = registruIntrariIesiriDao.findIesire(model.getId());
		} else {
			entity = new RegistruIesiri();
		}

		entity.setNumarInregistrare(model.getNumarInregistrare());
		entity.setDataInregistrare(model.getDataInregistrare());

		entity.setTrimisPeMail(model.isTrimisPeMail());
		entity.setContinut(model.getContinut());
		entity.setNumarPagini(model.getNumarPagini());
		entity.setNumarAnexe(model.getNumarAnexe());
		entity.setIntocmitDe(userService.getUserById(model.getIntocmitDeUserId()));
		entity.setAnulat(model.isAnulat());
		entity.setMotivAnulare(model.getMotivAnulare());
		entity.setInchis(model.isInchis());

		List<RegistruIesiriDestinatar> destinatari = new ArrayList<>();
		for (RegistruIesiriDestinatarModel destinatarModel : model.getDestinatari()) {
			RegistruIesiriDestinatar destinatar = null;
			if (destinatarModel.getId() != null) {
				destinatar = registruIntrariIesiriDao.findRegistruIesireDestinatar(destinatarModel.getId());
			} else {
				destinatar = new RegistruIesiriDestinatar();
			}
			destinatar.setId(destinatarModel.getId());
			if ( destinatarModel.getComisieGlId() != null) {
				destinatar.setComisieGl(nomenclatorValueDao.find(destinatarModel.getComisieGlId()));
			} else {
				destinatar.setComisieGl(null);
			}
			destinatar.setDataInregistrare(destinatarModel.getDataInregistrare());
			destinatar.setNumarInregistrare(destinatarModel.getNumarInregistrare());
			destinatar.setNume(destinatarModel.getNume());
			if (destinatarModel.getInstitutieId() != null) {
				destinatar.setInstitutie(nomenclatorValueDao.find(destinatarModel.getInstitutieId()));
			} else {
				destinatar.setInstitutie(null);
			}
			destinatar.setDepartament(destinatarModel.getDepartament());
			destinatar.setObservatii(destinatarModel.getObservatii());
			destinatar.setRegistruIesiri(entity);
			
			if (destinatarModel.getRegistruIntrariId() != null) {				
				if ((destinatar.getRegistruIntrari() == null) || !Objects.equals(destinatar.getRegistruIntrari().getId(), destinatarModel.getRegistruIntrariId()) || (destinatar.getId() == null)) {
					destinatar.setRegistruIntrari(registruIntrariIesiriDao.findIntrare(destinatarModel.getRegistruIntrariId()));
					registreIntrariPentruMapareRegistruIesire.add(destinatar.getRegistruIntrari());
				}
			} else {
				destinatar.setRegistruIntrari(null);
			}
			 
			
			destinatari.add(destinatar);
		}

		entity.setDestinatari(destinatari);

		List<Project> proiecteForSave = new ArrayList<Project>();
		for (Long proiectId : model.getProiectIds()) {
			Project project = projectDao.getById(proiectId);
			proiecteForSave.add(project);
		}
		entity.setProiecte(proiecteForSave);

		entity.setAsteptamRaspuns(model.isAsteptamRaspuns());
		entity.setTermenRaspuns(model.getTermenRaspuns());

		entity.setTipDocument(nomenclatorValueDao.find(model.getTipDocumentId()));
		if (model.getSubactivity() != null) {
			entity.setSubactivity(projectDao.getProjectSubactivities(model.getProiectIds().get(0))
										.stream().filter(subactivity -> subactivity.getId().equals(model.getSubactivity().getId()))
										.findAny().orElse(null));
		}else {
			entity.setSubactivity(null);
		}
		return entity;
	}


	public RegistruIesiriViewModel getIesiriViewModelFromEntity(RegistruIesiri entity) {

		RegistruIesiriViewModel model = new RegistruIesiriViewModel();

		model.setId(entity.getId());
		model.setNumarInregistrare(entity.getNumarInregistrare());
		model.setDataInregistrare(entity.getDataInregistrare());
		model.setTipDocument(NomenclatorValueUtils.getAttributeValueAsString(entity.getTipDocument(),
				NomenclatorConstants.REGISTRU_IESIRI_TIP_DOCUMENT_ATTRIBUTE_KEY_DENUMIRE));
		model.setCodTipDocument(NomenclatorValueUtils.getAttributeValueAsString(entity.getTipDocument(),
				NomenclatorConstants.REGISTRU_IESIRI_TIP_DOCUMENT_ATTRIBUTE_KEY_COD));
		model.setTrimisPeMail(entity.isTrimisPeMail());
		model.setContinut(entity.getContinut());
		model.setNumarPagini(entity.getNumarPagini());
		model.setNumarAnexe(entity.getNumarAnexe());
		model.setIntocmitDeUser(entity.getIntocmitDe().getDisplayName());
		model.setAsteptamRaspuns(entity.isAsteptamRaspuns());
		model.setTermenRaspuns(entity.getTermenRaspuns());
		model.setAnulat(entity.isAnulat());
		model.setMotivAnulare(entity.getMotivAnulare());
		model.setInchis(entity.isInchis());
		model.setTrebuieAnulat(entity.isTrebuieAnulat());

		String numeProiecteConcatenate = "";
		for (Project proiect : entity.getProiecte()) {
			if (StringUtils.isBlank(numeProiecteConcatenate)) {
				numeProiecteConcatenate += proiect.getName();
			} else {
				numeProiecteConcatenate += ", " + proiect.getName();
			}
		}
		model.setNumeProiecteConcatenate(numeProiecteConcatenate);

		List<RegistruIesiriDestinatarViewModel> destinatarviewModels = new ArrayList<>();
		for (RegistruIesiriDestinatar destinatar : entity.getDestinatari()) {
			RegistruIesiriDestinatarViewModel destinatarViewModel = new RegistruIesiriDestinatarViewModel();
			destinatarViewModel.setId(destinatar.getId());
			destinatarViewModel.setComisieGl(NomenclatorValueUtils.getAttributeValueAsString(destinatar.getComisieGl(), NomenclatorConstants.COMISII_SAU_GL_ATTRIBUTE_KEY_DENUMIRE));
			destinatarViewModel.setDataInregistrare(destinatar.getDataInregistrare());
			destinatarViewModel.setDepartament(destinatar.getDepartament());
			destinatarViewModel.setNumarInregistrare(destinatar.getNumarInregistrare());
			if (destinatar.getInstitutie() == null) {
				destinatarViewModel.setNume(destinatar.getNume());
			} else {
				destinatarViewModel.setNume(NomenclatorValueUtils.getAttributeValueAsString(destinatar.getInstitutie(), NomenclatorConstants.INSTITUTII_ATTRIBUTE_KEY_DENUMIRE_INSTITUTIE));
			}
			destinatarViewModel.setObservatii(destinatar.getObservatii());
			destinatarViewModel.setRegistruIesiriId(destinatar.getRegistruIesiri().getId());
			if (destinatar.getRegistruIntrari() != null) {
				destinatarViewModel.setRegistruIntrariId(destinatar.getRegistruIntrari().getId());
				destinatarViewModel.setNrInregistrareIntrare(destinatar.getRegistruIntrari().getNumarInregistrare());
			}
			destinatarviewModels.add(destinatarViewModel);
		}
		model.setDestinatari(destinatarviewModels);
		Subactivity subactivity = entity.getSubactivity();
		if (subactivity != null) {
			model.setSubactivityName(subactivity.getName());
		}

		return model;
	}

	public List<RegistruIesiriViewModel> getIesiriViewModelsFromEntity(List<RegistruIesiri> entities) {
		List<RegistruIesiriViewModel> viewModels = new ArrayList<>();
		for (RegistruIesiri entity : entities) {
			viewModels.add(getIesiriViewModelFromEntity(entity));
		}
		return viewModels;
	}

}
