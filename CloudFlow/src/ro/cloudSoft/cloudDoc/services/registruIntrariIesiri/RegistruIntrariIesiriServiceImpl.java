package ro.cloudSoft.cloudDoc.services.registruIntrariIesiri;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.comparators.NullComparator;
import org.apache.commons.lang.StringUtils;
import org.springframework.transaction.annotation.Transactional;

import ro.cloudSoft.cloudDoc.core.constants.NomenclatorConstants;
import ro.cloudSoft.cloudDoc.dao.nomenclator.NomenclatorDao;
import ro.cloudSoft.cloudDoc.dao.nomenclator.NomenclatorValueDao;
import ro.cloudSoft.cloudDoc.dao.registruIntrariIesiri.RegistruIntrariIesiriDao;
import ro.cloudSoft.cloudDoc.domain.AtasamentModel;
import ro.cloudSoft.cloudDoc.domain.content.Attachment;
import ro.cloudSoft.cloudDoc.domain.nomenclator.Nomenclator;
import ro.cloudSoft.cloudDoc.domain.nomenclator.NomenclatorValue;
import ro.cloudSoft.cloudDoc.domain.registruIntrariIesiri.RegistruIesiri;
import ro.cloudSoft.cloudDoc.domain.registruIntrariIesiri.RegistruIesiriAtasament;
import ro.cloudSoft.cloudDoc.domain.registruIntrariIesiri.RegistruIesiriDestinatar;
import ro.cloudSoft.cloudDoc.domain.registruIntrariIesiri.RegistruIntrari;
import ro.cloudSoft.cloudDoc.domain.registruIntrariIesiri.RegistruIntrariAtasament;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.DownloadableFile;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.registruIntrariIesiri.RegistruIesiriFilterModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.registruIntrariIesiri.RegistruIesiriModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.registruIntrariIesiri.RegistruIesiriViewModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.registruIntrariIesiri.RegistruIntrariFilter;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.registruIntrariIesiri.RegistruIntrariModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.registruIntrariIesiri.RegistruIntrariViewModel;
import ro.cloudSoft.cloudDoc.presentation.server.converters.registruIntrariIesiri.RegistruIntrariIesiriConverter;
import ro.cloudSoft.cloudDoc.services.nomenclator.NomenclatorService;
import ro.cloudSoft.cloudDoc.utils.FileSystemAttachmentManager;
import ro.cloudSoft.cloudDoc.utils.nomenclator.NomenclatorValueUtils;
import ro.cloudSoft.common.utils.DateUtils;
import ro.cloudSoft.common.utils.PagingList;

public class RegistruIntrariIesiriServiceImpl implements RegistruIntrariIesiriService {

	private static final String TIP_REGISTRU_INTRARI = "intrari";
	private static final String TIP_REGISTRU_IESIRI = "iesiri";

	private RegistruIntrariIesiriDao registruIntrariIesiriDao;
	private RegistruIntrariIesiriConverter registruIntrariIesiriConverter;
	private NomenclatorValueDao nomenclatorValueDao;
	private NomenclatorService nomenclatorService;
	private NomenclatorDao nomenclatorDao;
	private FileSystemAttachmentManager fileSystemAttachmentManager;
	
	public void setRegistruIntrariIesiriDao(RegistruIntrariIesiriDao registruIntrariIesiriDao) {
		this.registruIntrariIesiriDao = registruIntrariIesiriDao;
	}

	public void setRegistruIntrariIesiriConverter(RegistruIntrariIesiriConverter registruIntrariIesiriConverter) {
		this.registruIntrariIesiriConverter = registruIntrariIesiriConverter;
	}

	public void setNomenclatorValueDao(NomenclatorValueDao nomenclatorValueDao) {
		this.nomenclatorValueDao = nomenclatorValueDao;
	}

	public void setNomenclatorService(NomenclatorService nomenclatorService) {
		this.nomenclatorService = nomenclatorService;
	}

	public void setNomenclatorDao(NomenclatorDao nomenclatorDao) {
		this.nomenclatorDao = nomenclatorDao;
	}

	public void setFileSystemAttachmentManager(FileSystemAttachmentManager fileSystemAttachmentManager) {
		this.fileSystemAttachmentManager = fileSystemAttachmentManager;
	}

	@Override
	public List<RegistruIntrariModel> getAllIntrari() {
		List<RegistruIntrari> registruIntrari = registruIntrariIesiriDao.getAllIntrari();

		List<RegistruIntrariModel> registruIntrariModels = new ArrayList<RegistruIntrariModel>();
		if (CollectionUtils.isNotEmpty(registruIntrari)) {
			for (RegistruIntrari inregistrare : registruIntrari) {
				RegistruIntrariModel registruIntrariModel = registruIntrariIesiriConverter
						.getIntrariModelFromEntity(inregistrare);
				registruIntrariModels.add(registruIntrariModel);
			}
		}

		return registruIntrariModels;
	}

	@Override
	public List<RegistruIntrariViewModel> getAllRegistruIntrariViewModels() {
		List<RegistruIntrari> registruIntrari = registruIntrariIesiriDao.getAllIntrari();

		List<RegistruIntrariViewModel> registruIntrariViewModels = new ArrayList<RegistruIntrariViewModel>();
		if (CollectionUtils.isNotEmpty(registruIntrari)) {
			for (RegistruIntrari inregistrareIntrare : registruIntrari) {
				RegistruIntrariViewModel inregistrareIntrareViewModel = registruIntrariIesiriConverter
						.getIntrariViewModelFromEntity(inregistrareIntrare);
				registruIntrariViewModels.add(inregistrareIntrareViewModel);
			}
		}

		return registruIntrariViewModels;
	}

	@Override
	public List<Integer> getYearsOfExistingIntrari() {
		return registruIntrariIesiriDao.getYearsOfExistingIntrari();
	}
	
	@Override
	public List<Integer> getYearsOfExistingIesiri() {
		return registruIntrariIesiriDao.getYearsOfExistingIesiri();
	}

	@Override
	public List<RegistruIntrariViewModel> getAllRegistruIntrariViewModelsByYear(Integer year) {
		List<RegistruIntrari> intrari = registruIntrariIesiriDao.getAllIntrariByYear(year);
		List<RegistruIntrariViewModel> intrariViewModels = registruIntrariIesiriConverter
				.getIntrariViewModelsFromEntities(intrari);
		return intrariViewModels;
	}

	@Override
	@Transactional(rollbackFor = Throwable.class)
	public void saveRegistruIntrari(RegistruIntrariModel registruIntrariModel, String userName) {

		RegistruIntrari registruIntrari = registruIntrariIesiriConverter
				.getIntrariEntityFromModel(registruIntrariModel);
		calculateNrOfDaysFields(registruIntrari);
		if (registruIntrari.getNumarInregistrare() == null) {

			int year = getCurrentYear();

			NomenclatorValue registruIntrareCuUltimulNumarInregistrare = generateRegitruIntrariIesiriNumarInregistrare(
					RegistruIntrariIesiriServiceImpl.TIP_REGISTRU_INTRARI, year);
			nomenclatorValueDao.save(registruIntrareCuUltimulNumarInregistrare);

			registruIntrari
					.setNumarInregistrare(registruIntrareCuUltimulNumarInregistrare.getAttribute2() + " ~ " + year);
		} else {

			List<AtasamentModel> atasamenteModel = registruIntrariModel.getAtasamente();
			List<Long> atasamenteIdsToDelete = new ArrayList<>();
			if (atasamenteModel != null) {
				for (RegistruIntrariAtasament atasamentPersisted : registruIntrari.getAtasamente()) {
					boolean found = false;
					for (AtasamentModel atasamentModel : atasamenteModel) {
						if ((atasamentModel.getId() != null) && atasamentModel.getId().equals(atasamentPersisted.getId())) {
							found = true;
						}
					}
					if (!found) {
						atasamenteIdsToDelete.add(atasamentPersisted.getId());
					}
				}
				registruIntrariIesiriDao.deleteRegistruIntrariAtasamente(atasamenteIdsToDelete);
			}
		}
		
		registruIntrariIesiriDao.saveRegistruIntrari(registruIntrari);
		
		if (registruIntrariModel.getAtasamente() != null) {
			List<Attachment> attachments = fileSystemAttachmentManager.getAll(userName);
			List<RegistruIntrariAtasament> atasamenteToSave = new ArrayList<>();
			for (AtasamentModel atasamentModel : registruIntrariModel.getAtasamente()) {
				if (atasamentModel.getId() != null) {
					atasamenteToSave
					.add(registruIntrariIesiriDao.findRegistruIntrariAtasamentById(atasamentModel.getId()));
				} else {
					RegistruIntrariAtasament atasamentToSave = new RegistruIntrariAtasament();
					atasamentToSave.setFileName(atasamentModel.getFileName());
					
					for (Attachment attachment : attachments) {
						if (attachment.getName().equals(atasamentToSave.getFileName())) {
							atasamentToSave.setFilecontent(attachment.getData());
							atasamentToSave.setRegistruIntrari(registruIntrari);
							registruIntrariIesiriDao.saveRegistruIntrariAtasamente(atasamentToSave);
						}
					}
					atasamenteToSave.add(atasamentToSave);
					
				}
			}
			registruIntrari.setAtasamente(atasamenteToSave);
		}
		
		// mapare in registru iesiri acest registru intrare cu conditia iesire.destinatar.nume == intrare.emitent.nume 
		Long registruIesiriId = registruIntrari.getRegistruIesiriId();
		if (registruIesiriId != null) {
			RegistruIesiri registruIesirePentruMapareRegistruIntrare = registruIntrariIesiriDao.findIesire(registruIesiriId);
			if (registruIesirePentruMapareRegistruIntrare != null) {
				for (RegistruIesiriDestinatar destinatar : registruIesirePentruMapareRegistruIntrare.getDestinatari()) {
					NomenclatorValue institutieDestinatar = destinatar.getInstitutie();
					NomenclatorValue institutieEmitent = registruIntrari.getEmitent();
					if (
							((institutieDestinatar != null) && (institutieEmitent != null) && (institutieDestinatar.getId().equals(institutieEmitent.getId())))
								||
							((destinatar.getNume() != null) && destinatar.getNume().equals(registruIntrari.getNumeEmitent()))
						) {
						if (destinatar.getRegistruIntrari() == null) {
							destinatar.setRegistruIntrari(registruIntrari);
							destinatar.setDepartament(registruIntrari.getDepartamentEmitent());
							destinatar.setNumarInregistrare(registruIntrari.getNumarDocumentEmitent());
							destinatar.setDataInregistrare(registruIntrari.getDataDocumentEmitent());
							registruIntrariIesiriDao.saveRegistruIesiriDestinatari(destinatar);
						}
					}
				}
			}
		}
		
	}

	private int getCurrentYear() {
		Date currentDate = new Date();
		Calendar calendar = new GregorianCalendar();
		calendar.setTime(currentDate);
		return calendar.get(Calendar.YEAR);
	}
	
	public Long getLastNumarInregistrareByTipRegistruAndYear(String tipRegistru, Integer year) {
		NomenclatorValue numarInregistrare = nomenclatorValueDao.getRegistruIntrariIesiriCuUltimulNumarDeInregistrareByTipRegistruAndCurrentYear(tipRegistru, String.valueOf(year));
		if (numarInregistrare != null) {
			return Long.valueOf(numarInregistrare.getAttribute2());
		};
		return null;
	}

	private NomenclatorValue generateRegitruIntrariIesiriNumarInregistrare(String tipRegistru, int year) {
		NomenclatorValue registruCuUltimulNumarInregistrare = null;
		registruCuUltimulNumarInregistrare = generateRegistruIntrariIesiriNumarInregistrareByTipRegistruAndYear(
				tipRegistru, year);
		return registruCuUltimulNumarInregistrare;
	}

	private NomenclatorValue generateRegistruIntrariIesiriNumarInregistrareByTipRegistruAndYear(String tipRegistru,
			int year) {

		Long numarInregistrareNou = null;

		NomenclatorValue registruCuUltimulNumarInregistrare = nomenclatorValueDao
				.getRegistruIntrariIesiriCuUltimulNumarDeInregistrareByTipRegistruAndCurrentYear(tipRegistru,
						String.valueOf(year));

		if (registruCuUltimulNumarInregistrare != null) {
			String numarInregistrare = registruCuUltimulNumarInregistrare.getAttribute2();
			numarInregistrareNou = Long.valueOf(numarInregistrare) + 1;
			registruCuUltimulNumarInregistrare.setAttribute2(String.valueOf(numarInregistrareNou));
		} else {
			numarInregistrareNou = 1L;

			Nomenclator registruGenerareNumarInregistrareNomenclator = getRegistruIntrariGenerareNumarInregistrareNomenclator();

			registruCuUltimulNumarInregistrare = new NomenclatorValue();
			registruCuUltimulNumarInregistrare.setNomenclator(registruGenerareNumarInregistrareNomenclator);
			registruCuUltimulNumarInregistrare.setAttribute1(tipRegistru);
			registruCuUltimulNumarInregistrare.setAttribute2(String.valueOf(numarInregistrareNou));
			registruCuUltimulNumarInregistrare.setAttribute3(String.valueOf(year));
		}

		return registruCuUltimulNumarInregistrare;
	}

	private Nomenclator getRegistruIntrariGenerareNumarInregistrareNomenclator() {
		List<String> codes = new ArrayList<String>();
		codes.add(NomenclatorConstants.REGISTRU_INTRARI_IESIRI_GENERARE_NUMAR_INREGISTRARE_NOMENCLATOR_CODE);
		Map<String, Long> map = nomenclatorService.getNomenclatorIdByCodeMapByNomenclatorCodes(codes);
		Long nomenclatorId = map
				.get(NomenclatorConstants.REGISTRU_INTRARI_IESIRI_GENERARE_NUMAR_INREGISTRARE_NOMENCLATOR_CODE);
		return nomenclatorDao.find(nomenclatorId);
	}

	@Override
	@Transactional(rollbackFor = Throwable.class)
	public void saveRegistruIesiri(RegistruIesiriModel registruIesiriModel, String userName) {
		
		List<RegistruIesiriDestinatar> registruIesiriDestinatari = new ArrayList<>();
		if (registruIesiriModel.getId() != null) {
			registruIesiriDestinatari = registruIntrariIesiriDao.findIesire(registruIesiriModel.getId())
					.getDestinatari();
		}
		
		List<RegistruIntrari> registreIntrariPentruMapare = new ArrayList<>();
		RegistruIesiri registruIesiri = registruIntrariIesiriConverter.getIesiriEntityFromModel(registruIesiriModel, registreIntrariPentruMapare);

		if (registruIesiri.getId() != null) {

			List<Long> registruIesiriDestinatariIdToRemove = new ArrayList<>();
			for (RegistruIesiriDestinatar registruIesiriDestinatarPersisted : registruIesiriDestinatari) {
				boolean found = false;
				for (RegistruIesiriDestinatar registruIesiriDestinatarNew : registruIesiri.getDestinatari()) {
					if (registruIesiriDestinatarNew.getId() != null
							&& registruIesiriDestinatarNew.getId().equals(registruIesiriDestinatarPersisted.getId())) {
						found = true;
					}
				}

				if (!found) {
					registruIesiriDestinatariIdToRemove.add(registruIesiriDestinatarPersisted.getId());
				}
			}

			registruIntrariIesiriDao.deleteRegistruIesiriDestinatari(registruIesiriDestinatariIdToRemove);

			List<AtasamentModel> atasamenteModel = registruIesiriModel.getAtasamente();
			List<Long> atasamenteIdsToDelete = new ArrayList<>();
			if (atasamenteModel != null) {
				for (RegistruIesiriAtasament atasamentPersisted : registruIesiri.getAtasamente()) {
					boolean found = false;
					for (AtasamentModel atasamentModel : atasamenteModel) {
						if (atasamentModel.getId() != null) {
							if (atasamentModel.getId().equals(atasamentPersisted.getId())) {
								found = true;
							}
						}
					}
					if (!found) {
						atasamenteIdsToDelete.add(atasamentPersisted.getId());
					}
				}
				registruIntrariIesiriDao.deleteRegistruIesiriAtasamente(atasamenteIdsToDelete);
			}
			
		}

		if (registruIesiriModel.getNumarInregistrare() == null) {

			int year = getCurrentYear();

			NomenclatorValue registruIesireCuUltimulNumarInregistrare = generateRegitruIntrariIesiriNumarInregistrare(
					RegistruIntrariIesiriServiceImpl.TIP_REGISTRU_IESIRI, year);
			nomenclatorValueDao.save(registruIesireCuUltimulNumarInregistrare);
			
			registruIesiri.setNumarInregistrare(registruIesireCuUltimulNumarInregistrare.getAttribute2() + " ~ " + year);
		}

		registruIntrariIesiriDao.saveRegistruIesiri(registruIesiri);

		if (registruIesiriModel.getAtasamente() != null) {

			List<Attachment> attachments = fileSystemAttachmentManager.getAll(userName);
			List<RegistruIesiriAtasament> atasamenteToSave = new ArrayList<>();
			for (AtasamentModel atasamentModel : registruIesiriModel.getAtasamente()) {
				if (atasamentModel.getId() != null) {
					atasamenteToSave.add(registruIntrariIesiriDao.findRegistruIesiriAtasamentById(atasamentModel.getId()));
				} else {
					RegistruIesiriAtasament atasamentToSave = new RegistruIesiriAtasament();
					atasamentToSave.setFileName(atasamentModel.getFileName());

					for (Attachment attachment : attachments) {
						if (attachment.getName().equals(atasamentToSave.getFileName())) {
							atasamentToSave.setFilecontent(attachment.getData());
							atasamentToSave.setRegistruIesiri(registruIesiri);
							registruIntrariIesiriDao.saveRegistruIesiriAtasamente(atasamentToSave);
						}
					}
					atasamenteToSave.add(atasamentToSave);

				}
			}
			registruIesiri.setAtasamente(atasamenteToSave);
		}
		
		// mapare registre intrari la acest registru de iesire
		for (RegistruIntrari	regIntrari : registreIntrariPentruMapare) {;
			regIntrari.setRegistruIesiriId(registruIesiri.getId());
			registruIntrariIesiriDao.saveRegistruIntrari(regIntrari);
		}
		
		//actualizare nrOfDaysFields pentru intrarile mapate
		for (RegistruIesiriDestinatar destinatar: registruIesiri.getDestinatari()) {
			RegistruIntrari intrare = destinatar.getRegistruIntrari();
			if (intrare != null) {
				calculateNrOfDaysFields(intrare);
				registruIntrariIesiriDao.saveRegistruIntrari(intrare);
			}
		}
	}

	@Override
	public RegistruIesiriModel getRegistruIesiri(Long registruIesiriId) {
		RegistruIesiri registruIesiri = registruIntrariIesiriDao.findIesire(registruIesiriId);
		return this.registruIntrariIesiriConverter.getIesiriModelFromEntity(registruIesiri);
	}

	@Override
	public List<RegistruIesiriViewModel> getAllRegistruIesiriViewModels() {
		List<RegistruIesiri> registruIesiri = registruIntrariIesiriDao.getAllRegistruIesiri();
		return registruIntrariIesiriConverter.getIesiriViewModelsFromEntity(registruIesiri);
	}

	public RegistruIntrariModel getRegistruIntrariById(Long registruId) {
		RegistruIntrari registruIntrari = registruIntrariIesiriDao.getById(registruId);
		RegistruIntrariModel registruIntrariModel = registruIntrariIesiriConverter
				.getIntrariModelFromEntity(registruIntrari);
		return registruIntrariModel;
	}

	@Override
	@Transactional(rollbackFor = Throwable.class)
	public void cancelRegistruIntrari(Long registruIntrariId, String motivAnulare) {
		RegistruIntrari registruIntrari = registruIntrariIesiriDao.findIntrare(registruIntrariId);
		registruIntrari.setAnulat(true);
		registruIntrari.setMotivAnulare(motivAnulare);
		registruIntrari.setTrebuieAnulat(false);
		if (registruIntrari.isNecesitaRaspuns()) {
			RegistruIesiri iesire = registruIntrariIesiriDao.findIesire(registruIntrari.getRegistruIesiriId());
			if (iesire != null) {
				iesire.setTrebuieAnulat(true);
				registruIntrariIesiriDao.saveRegistruIesiri(iesire);
			}
		}
		registruIntrari.setRegistruIesiriId(null);
		RegistruIesiriDestinatar destinatar = registruIntrariIesiriDao.getRegistruIesireDestinatarByIntrareId(registruIntrariId);
		if (destinatar != null) {
			destinatar.setRegistruIntrari(null);
			registruIntrariIesiriDao.saveRegistruIesiriDestinatari(destinatar);
		}
		registruIntrariIesiriDao.saveRegistruIntrari(registruIntrari);
	}

	@Override
	@Transactional(rollbackFor = Throwable.class)
	public void cancelRegistruIesiri(Long registruIesiriId, String motivAnulare) {
		RegistruIesiri registruIesiri = registruIntrariIesiriDao.findIesire(registruIesiriId);
		List<RegistruIesiriDestinatar> destinatari = registruIesiri.getDestinatari();
		if (CollectionUtils.isNotEmpty(destinatari)) {
			destinatari.forEach(destinatar -> {
				RegistruIntrari intrare = destinatar.getRegistruIntrari();
				if (intrare != null) {
					intrare.setRegistruIesiriId(null);
					if (registruIesiri.isAsteptamRaspuns()) {
						intrare.setTrebuieAnulat(true);
					}
					registruIntrariIesiriDao.saveRegistruIntrari(intrare);
					destinatar.setRegistruIntrari(null);
				}
			});
			registruIesiri.setDestinatari(destinatari);
		}
		registruIesiri.setAnulat(true);
		registruIesiri.setMotivAnulare(motivAnulare);
		registruIesiri.setTrebuieAnulat(false);
		registruIntrariIesiriDao.saveRegistruIesiri(registruIesiri);
	}

	@Override
	public boolean isRegistruIesiriCanceled(Long registruIesiriId) {
		RegistruIesiri registruIesiri = registruIntrariIesiriDao.findIesire(registruIesiriId);
		return registruIesiri.isAnulat();
	}
	
	@Override
	public boolean isRegistruIesiriFinalized(Long registruIesiriId) {
		RegistruIesiri registruIesiri = registruIntrariIesiriDao.findIesire(registruIesiriId);
		return registruIesiri.isInchis();
	}

	@Override
	public boolean isRegistruIntrariCanceled(Long registruIntrariId) {
		RegistruIntrari registruIntrari = registruIntrariIesiriDao.findIntrare(registruIntrariId);
		return registruIntrari.isAnulat();
	}
	
	@Override
	public boolean isRegistruIntrariFinalized(Long registruIntrariId) {
		RegistruIntrari registruIntrari = registruIntrariIesiriDao.findIntrare(registruIntrariId);
		return registruIntrari.isInchis();
	}

	@Override
	public void finalizareRegistruIntrari(Long registruIntrariId) {
		RegistruIntrari registruIntrari = registruIntrariIesiriDao.findIntrare(registruIntrariId);
		registruIntrari.setInchis(true);
		registruIntrariIesiriDao.saveRegistruIntrari(registruIntrari);		
	}

	@Override
	public void finalizareRegistruIesiri(Long registruIesiriId) {
		RegistruIesiri registruIesiri = registruIntrariIesiriDao.findIesire(registruIesiriId);
		registruIesiri.setInchis(true);
		registruIntrariIesiriDao.saveRegistruIesiri(registruIesiri);		
	}
	
	public DownloadableFile downloadAtasamentOfRegistruIntrariById(Long atasamentId) {
		RegistruIntrariAtasament registruIntrariAtasamente = registruIntrariIesiriDao
				.findRegistruIntrariAtasamentById(atasamentId);
		if (registruIntrariAtasamente != null) {
			return new DownloadableFile(registruIntrariAtasamente.getFileName(),
					registruIntrariAtasamente.getFilecontent());
		}
		return null;
	}

	@Override
	public DownloadableFile downloadAtasamentOfRegistruIesiriById(Long atasamentId) {
		RegistruIesiriAtasament registruIesiriAtasamente = registruIntrariIesiriDao
				.findRegistruIesiriAtasamentById(atasamentId);
		if (registruIesiriAtasamente != null) {
			return new DownloadableFile(registruIesiriAtasamente.getFileName(),
					registruIesiriAtasamente.getFilecontent());
		}
		return null;
	}
	
	public PagingList<RegistruIesiriViewModel> getRegistruIesiriViewModelByFilter(RegistruIesiriFilterModel filter) {
		String docTypeCodEchivalent = null;
		if (filter.getEmitentId() != null || filter.getNumeEmitent() != null) {
			NomenclatorValue nomenclatorTipDocumentValue = nomenclatorValueDao.find(filter.getTipDocumentIntrareId());
			docTypeCodEchivalent = NomenclatorValueUtils.getAttributeValueAsString(nomenclatorTipDocumentValue, NomenclatorConstants.RESGISTRU_INTRARI_TIP_DOCUMENT_ATTRIBUTE_KEY_COD_ECHIVALENT);
			if (docTypeCodEchivalent == null) {
				return new PagingList<RegistruIesiriViewModel>(0, 0, new ArrayList<RegistruIesiriViewModel>());
			}
		}
		List<RegistruIesiri> iesiri = registruIntrariIesiriDao.getIesiriByFilter(filter, docTypeCodEchivalent);
		Integer offset = filter.getOffset();
		Integer pageEndIdx = offset + filter.getPageSize();
		if (offset + pageEndIdx > iesiri.size()) {
			pageEndIdx = iesiri.size();
		}
		List<RegistruIesiriViewModel> iesiriViewModels = registruIntrariIesiriConverter.getIesiriViewModelsFromEntity(iesiri);
		if (filter.getSortField() != null) {
			if (filter.getSortField().equals("numarInregistrare")) {
				Comparator<RegistruIesiriViewModel> compareByNrInregistrare = 
						(RegistruIesiriViewModel re1, RegistruIesiriViewModel re2) -> Integer.valueOf(StringUtils.split(re1.getNumarInregistrare(),"~")[0].trim())
																						.compareTo(Integer.valueOf(StringUtils.split(re2.getNumarInregistrare(),"~")[0].trim()));
				Collections.sort(iesiriViewModels, compareByNrInregistrare);
			}else {
				BeanComparator<RegistruIesiriViewModel> comparator = new BeanComparator<>(filter.getSortField(), new NullComparator(false));
				Collections.sort(iesiriViewModels, comparator);
			}
			if (!filter.getIsAscendingOrder()) {
				Collections.reverse(iesiriViewModels);
			}
		}
		PagingList<RegistruIesiriViewModel> iesiriViewModelPage = new PagingList<>(iesiri.size(), filter.getOffset(), iesiriViewModels.subList(offset, pageEndIdx));
		return iesiriViewModelPage;
	}
	
	public PagingList<RegistruIntrariViewModel> getRegistruIntrariByFilter(RegistruIntrariFilter registruIntrariFilter) {
		String docTypeCodEchivalent = null;
		if (registruIntrariFilter.getDestinatarId() != null || registruIntrariFilter.getNumeDestinatar() != null) {
			NomenclatorValue nomenclatorTipDocumentValue = nomenclatorValueDao.find(registruIntrariFilter.getTipDocumentIdDestinatar());
			docTypeCodEchivalent = NomenclatorValueUtils.getAttributeValueAsString(nomenclatorTipDocumentValue, NomenclatorConstants.RESGISTRU_IESIRI_TIP_DOCUMENT_ATTRIBUTE_KEY_COD_ECHIVALENT);
			if (docTypeCodEchivalent == null) {
				return new PagingList<RegistruIntrariViewModel>(0, 0, new ArrayList<RegistruIntrariViewModel>());
			}
		}
		
		List<RegistruIntrari> intrari = registruIntrariIesiriDao.getAllRegistruIntrariByFilter(registruIntrariFilter, docTypeCodEchivalent);
		
		Integer offset = registruIntrariFilter.getOffset();
		Integer pageEndIdx = offset + registruIntrariFilter.getPageSize();
		if (offset + pageEndIdx > intrari.size()) {
			pageEndIdx = intrari.size();
		}
		List<RegistruIntrariViewModel> intrariViewModels = registruIntrariIesiriConverter.getIntrariViewModelsFromEntities(intrari);
		if (registruIntrariFilter.getSortField() != null) {
			if (registruIntrariFilter.getSortField().equals("numarInregistrare")) {
				Comparator<RegistruIntrariViewModel> compareByNrInregistrare = 
						(RegistruIntrariViewModel re1, RegistruIntrariViewModel re2) -> Integer.valueOf(StringUtils.split(re1.getNumarInregistrare(),"~")[0].trim())
																						.compareTo(Integer.valueOf(StringUtils.split(re2.getNumarInregistrare(),"~")[0].trim()));
				Collections.sort(intrariViewModels, compareByNrInregistrare);
			}else {
				BeanComparator<RegistruIntrariViewModel> comparator = new BeanComparator<>(registruIntrariFilter.getSortField(), new NullComparator(false));
				Collections.sort(intrariViewModels, comparator);
			}
			if (!registruIntrariFilter.getIsAscendingOrder()) {
				Collections.reverse(intrariViewModels);
			}
		}
				
		PagingList<RegistruIntrariViewModel> intrariViewModelPage = new PagingList<>(intrari.size(), registruIntrariFilter.getOffset(), intrariViewModels.subList(offset, pageEndIdx));
		return intrariViewModelPage;
	}
	
	@Override
	public List<String> getNrInregistrareOfMappedRegistriIntrariByIesireId(Long registruIesiriId) {
		List<RegistruIntrari> mappedIntrari = registruIntrariIesiriDao.getMappedIntrariByIesireId(registruIesiriId);
		return mappedIntrari != null ? mappedIntrari.stream()
				.map(RegistruIntrari::getNumarInregistrare)
				.collect(Collectors.toList()) : new ArrayList<String>();
	}

	@Override
	public List<String> getNrInregistrareOfMappedRegistriIesiriByIntrareId(Long registruIntrariId) {
		List<RegistruIesiri> mappedIesiri = registruIntrariIesiriDao.getMappedIesiriByIntrareId(registruIntrariId);
		return mappedIesiri != null ? mappedIesiri.stream()
				.map(RegistruIesiri::getNumarInregistrare)
				.collect(Collectors.toList()) : new ArrayList<String>();
	}

	private void calculateNrOfDaysFields(RegistruIntrari entity) {
		Date dataInregistrare = entity.getDataInregistrare();
		Date dataDocumentEmitent = entity.getDataDocumentEmitent();
		if ((dataInregistrare != null)  && (dataDocumentEmitent != null)) {
			dataInregistrare = DateUtils.nullHourMinutesSeconds(dataInregistrare);
			dataDocumentEmitent = DateUtils.nullHourMinutesSeconds(dataDocumentEmitent);
			entity.setNrZileIntrareEmitent((int) DateUtils.pozitiveNumberDaysBetween(dataDocumentEmitent, dataInregistrare));
			if(entity.getRegistruIesiriId() != null) {
				RegistruIesiri registruIesiri = registruIntrariIesiriDao.findIesire(entity.getRegistruIesiriId());
				if (registruIesiri == null) {
					throw new RuntimeException("Invalid nr registru iesiri assigned in registru intrari!");
				}
				Date dataInregistrareIesire = registruIesiri.getDataInregistrare();
				if (dataInregistrareIesire != null) {
					dataInregistrareIesire = DateUtils.nullHourMinutesSeconds(dataInregistrareIesire);
					entity.setNrZileRaspunsIntrare((int) DateUtils.pozitiveNumberDaysBetween(dataInregistrare, dataInregistrareIesire));
					entity.setNrZileRaspunsEmitent((int) DateUtils.pozitiveNumberDaysBetween(dataDocumentEmitent, dataInregistrareIesire));
				}
				Date termenRaspunsIesire = registruIesiri.getTermenRaspuns();
				if (termenRaspunsIesire != null) {
					termenRaspunsIesire = DateUtils.nullHourMinutesSeconds(termenRaspunsIesire);
					entity.setNrZileTermenDataRaspuns((int) DateUtils.numberDaysBetween(termenRaspunsIesire, dataInregistrare));
				}
				
			}
		}
	}

	@Override
	public boolean isSubactivityUsed(Long subactivityId) {
		if (subactivityId == null) {
			return false;
		}
		List<RegistruIesiri> iesiri = this.registruIntrariIesiriDao.getAllIesiriBySubactivityId(subactivityId);
		if (CollectionUtils.isNotEmpty(iesiri)) {
			return true;
		}
		List<RegistruIntrari> intrari = this.registruIntrariIesiriDao.getAllIntrariBySubactivityId(subactivityId);
		if (CollectionUtils.isNotEmpty(intrari)) {
			return true;
		}
		return false;
	}
	
}
