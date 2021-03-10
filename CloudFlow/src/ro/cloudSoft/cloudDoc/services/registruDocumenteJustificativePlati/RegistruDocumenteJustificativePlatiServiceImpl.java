package ro.cloudSoft.cloudDoc.services.registruDocumenteJustificativePlati;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.transaction.annotation.Transactional;

import ro.cloudSoft.cloudDoc.core.constants.NomenclatorConstants;
import ro.cloudSoft.cloudDoc.dao.nomenclator.NomenclatorDao;
import ro.cloudSoft.cloudDoc.dao.nomenclator.NomenclatorValueDao;
import ro.cloudSoft.cloudDoc.dao.registruDocumenteJustificativePlati.RegistruDocumenteJustificativePlatiDao;
import ro.cloudSoft.cloudDoc.domain.AtasamentModel;
import ro.cloudSoft.cloudDoc.domain.content.Attachment;
import ro.cloudSoft.cloudDoc.domain.nomenclator.Nomenclator;
import ro.cloudSoft.cloudDoc.domain.nomenclator.NomenclatorValue;
import ro.cloudSoft.cloudDoc.domain.registruDocumenteJustificativePlati.RegistruDocumenteJustificativePlati;
import ro.cloudSoft.cloudDoc.domain.registruDocumenteJustificativePlati.RegistruDocumenteJustificativePlatiAtasament;
import ro.cloudSoft.cloudDoc.domain.security.SecurityManager;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.DownloadableFile;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.registruDocumenteJustificativePlati.RegistruDocumenteJustificativePlatiModel;
import ro.cloudSoft.cloudDoc.presentation.server.converters.registruDocumenteJustificativePlati.RegistruDocumenteJustificativePlatiConverter;
import ro.cloudSoft.cloudDoc.services.nomenclator.NomenclatorService;
import ro.cloudSoft.cloudDoc.utils.FileSystemAttachmentManager;

public class RegistruDocumenteJustificativePlatiServiceImpl implements RegistruDocumenteJustificativePlatiService {

	private RegistruDocumenteJustificativePlatiDao registruDocumenteJustificativePlatiDao;
	private NomenclatorValueDao nomenclatorValueDao;
	private NomenclatorDao nomenclatorDao;
	
	private RegistruDocumenteJustificativePlatiConverter registruDocumenteJustificativePlatiConverter;
	
	private NomenclatorService nomenclatorService;
	private FileSystemAttachmentManager fileSystemAttachmentManager;

	public void setFileSystemAttachmentManager(FileSystemAttachmentManager fileSystemAttachmentManager) {
		this.fileSystemAttachmentManager = fileSystemAttachmentManager;
	}

	@Override
	public List<RegistruDocumenteJustificativePlatiModel> getAllDocumenteJustificativePlati() {
		
		List<RegistruDocumenteJustificativePlati> documenteJustificativePlati = registruDocumenteJustificativePlatiDao.getAll();
		
		List<RegistruDocumenteJustificativePlatiModel> documenteJustificativePlatiModels = new ArrayList<RegistruDocumenteJustificativePlatiModel>();
		for (RegistruDocumenteJustificativePlati documentJustificativPlati : documenteJustificativePlati) {
			RegistruDocumenteJustificativePlatiModel documenteJustificativePlatiModel = registruDocumenteJustificativePlatiConverter.getModelFromRegistruDocumenteJustificativePlati(documentJustificativPlati);
			documenteJustificativePlatiModels.add(documenteJustificativePlatiModel);
		}
		
		return documenteJustificativePlatiModels;
	}

	@Override
	public RegistruDocumenteJustificativePlatiModel getDocumentJustificativPlati(Long documentJustificativPlatiId) {
		RegistruDocumenteJustificativePlati documentJustificativPlati = registruDocumenteJustificativePlatiDao.find(documentJustificativPlatiId);
		return registruDocumenteJustificativePlatiConverter.getModelFromRegistruDocumenteJustificativePlati(documentJustificativPlati);
	}

	@Transactional
	@Override
	public void saveDocumentJustificativPlati(RegistruDocumenteJustificativePlatiModel documentJustificativPlatiModel, SecurityManager securityManager) {
		
		RegistruDocumenteJustificativePlati documentJustificativPlati = registruDocumenteJustificativePlatiConverter.getEntityFromModel(documentJustificativPlatiModel);
		if (documentJustificativPlati.getNumarInregistrare() == null) {
			
			int year = getCurrentYear();
			
			NomenclatorValue registruFacturiCuUltimulNumarInregistrare = generateRegitruFacturiNumarInregistrare(documentJustificativPlati, year);
			nomenclatorValueDao.save(registruFacturiCuUltimulNumarInregistrare);
			
			documentJustificativPlati.setNumarInregistrare(registruFacturiCuUltimulNumarInregistrare.getAttribute2() + registruFacturiCuUltimulNumarInregistrare.getAttribute3() + "/" + year);
		} else {
			deleteAtasamenteDeSters(documentJustificativPlatiModel);
		}

		if (documentJustificativPlatiModel.getAtasamente() != null) {
			List<Attachment> attachments = fileSystemAttachmentManager.getAll(securityManager.getUserUsername());
			List<RegistruDocumenteJustificativePlatiAtasament> atasamenteToSave = new ArrayList<>();
			for (AtasamentModel atasamentModel : documentJustificativPlatiModel.getAtasamente()) {
				if (atasamentModel.getId() != null) {
					atasamenteToSave.add(registruDocumenteJustificativePlatiDao.findAtasamentOfRegistruIesiriById(atasamentModel.getId()));
				} else {
					RegistruDocumenteJustificativePlatiAtasament atasamentToSave = new RegistruDocumenteJustificativePlatiAtasament();
					atasamentToSave.setRegistruDocumenteJustificativePlati(documentJustificativPlati);
					atasamentToSave.setFileName(atasamentModel.getFileName());

					for (Attachment attachment : attachments) {
						if (attachment.getName().equals(atasamentToSave.getFileName())) {
							atasamentToSave.setFilecontent(attachment.getData());
							atasamentToSave.setRegistruDocumenteJustificativePlati(documentJustificativPlati);
						}
					}
					atasamenteToSave.add(atasamentToSave);

				}
			}
			documentJustificativPlati.setAtasamente(atasamenteToSave);
		}
		
		registruDocumenteJustificativePlatiDao.save(documentJustificativPlati);
	}

	private void deleteAtasamenteDeSters(RegistruDocumenteJustificativePlatiModel documentJustificativPlatiModel) {
		if (documentJustificativPlatiModel.getId() == null) {
			return;
		}		
		List<Long> persistedIds = registruDocumenteJustificativePlatiDao.getAttachmentIds(documentJustificativPlatiModel.getId());
		if (CollectionUtils.isEmpty(persistedIds)) {
			return;
		}
		List<AtasamentModel> atasamenteModel = documentJustificativPlatiModel.getAtasamente();		
		List<Long> atasamenteIdsToDelete = new ArrayList<>();
		for (Long atasamentPersistedId : persistedIds) {
			boolean found = false;
			if (CollectionUtils.isNotEmpty(atasamenteModel)) {
				for (AtasamentModel atasamentModel : atasamenteModel) {
					if (atasamentModel.getId() != null && atasamentModel.getId().equals(atasamentPersistedId)) {
						found = true;
					}
				}
			}
			if (!found) {
				atasamenteIdsToDelete.add(atasamentPersistedId);
			}
		}
		registruDocumenteJustificativePlatiDao.deleteAtasamenteOfRegistruDocumenteJustificativePlatiByIds(atasamenteIdsToDelete);
	}
		
	private int getCurrentYear() {
		Date currentDate = new Date();
		Calendar calendar = new GregorianCalendar();
		calendar.setTime(currentDate);
		return calendar.get(Calendar.YEAR);
	}
	
	private NomenclatorValue generateRegitruFacturiNumarInregistrare(RegistruDocumenteJustificativePlati documentJustificativPlati, int year) {
		
		Long numarInregistrareNou = null;
		String tipDocumentId = documentJustificativPlati.getTipDocument().getId().toString();
		NomenclatorValue registruFacturiCuUltimulNumarInregistrare = nomenclatorValueDao.getRegistruFacturiCuUltimulNumarDeInregistrareByTipDocumentAndCurrentYear(tipDocumentId, String.valueOf(year));
		
		if (registruFacturiCuUltimulNumarInregistrare != null) {
			String numarInregistrare = registruFacturiCuUltimulNumarInregistrare.getAttribute3();
			numarInregistrareNou = Long.valueOf(numarInregistrare) + 1;
			registruFacturiCuUltimulNumarInregistrare.setAttribute3(String.valueOf(numarInregistrareNou));
		} else {
			numarInregistrareNou = 1L;
			
			Nomenclator registruFacturiGenerareNumarInregistrareNomenclator = getRegistruFacturiGenerareNumarInregistrareNomenclator();
			
			registruFacturiCuUltimulNumarInregistrare = new NomenclatorValue();
			registruFacturiCuUltimulNumarInregistrare.setNomenclator(registruFacturiGenerareNumarInregistrareNomenclator);
			registruFacturiCuUltimulNumarInregistrare.setAttribute1(tipDocumentId);
			registruFacturiCuUltimulNumarInregistrare.setAttribute2(documentJustificativPlati.getTipDocument().getAttribute2());
			registruFacturiCuUltimulNumarInregistrare.setAttribute3(String.valueOf(numarInregistrareNou));
			registruFacturiCuUltimulNumarInregistrare.setAttribute4(String.valueOf(year));
		}
		
		return registruFacturiCuUltimulNumarInregistrare;
	}
	
	private Nomenclator getRegistruFacturiGenerareNumarInregistrareNomenclator() {
		List<String> codes = new ArrayList<String>();
		codes.add(NomenclatorConstants.REGISTRU_FACTURI_GENERARE_NUMAR_INREGISTRARE_NOMENCLATOR_CODE);
		Map<String, Long> map = nomenclatorService.getNomenclatorIdByCodeMapByNomenclatorCodes(codes);
		Long nomenclatorId = map.get(NomenclatorConstants.REGISTRU_FACTURI_GENERARE_NUMAR_INREGISTRARE_NOMENCLATOR_CODE);
		return nomenclatorDao.find(nomenclatorId);
	}

	@Override
	public void cancelDocumentJustificativPlati(RegistruDocumenteJustificativePlatiModel documentJustificativPlatiModel) {
		RegistruDocumenteJustificativePlati documentJustificativPlati = registruDocumenteJustificativePlatiConverter.getEntityFromModel(documentJustificativPlatiModel);
		documentJustificativPlati.setAnulat(true);
		registruDocumenteJustificativePlatiDao.save(documentJustificativPlati);
	}

	public void setRegistruDocumenteJustificativePlatiDao(
			RegistruDocumenteJustificativePlatiDao registruDocumenteJustificativePlatiDao) {
		this.registruDocumenteJustificativePlatiDao = registruDocumenteJustificativePlatiDao;
	}
	
	public void setNomenclatorValueDao(NomenclatorValueDao nomenclatorValueDao) {
		this.nomenclatorValueDao = nomenclatorValueDao;
	}
	
	public void setNomenclatorDao(NomenclatorDao nomenclatorDao) {
		this.nomenclatorDao = nomenclatorDao;
	}
	
	public void setRegistruDocumenteJustificativePlatiConverter(
			RegistruDocumenteJustificativePlatiConverter registruDocumenteJustificativePlatiConverter) {
		this.registruDocumenteJustificativePlatiConverter = registruDocumenteJustificativePlatiConverter;
	}
	
	public void setNomenclatorService(NomenclatorService nomenclatorService) {
		this.nomenclatorService = nomenclatorService;
	}

	@Override
	public List<RegistruDocumenteJustificativePlatiModel> getAllByYear(Integer year) {
		List<RegistruDocumenteJustificativePlati> documenteJustificativePlati = registruDocumenteJustificativePlatiDao.getAllByYear(year);
		
		List<RegistruDocumenteJustificativePlatiModel> documenteJustificativePlatiModels = new ArrayList<RegistruDocumenteJustificativePlatiModel>();
		for (RegistruDocumenteJustificativePlati documentJustificativPlati : documenteJustificativePlati) {
			RegistruDocumenteJustificativePlatiModel documenteJustificativePlatiModel = registruDocumenteJustificativePlatiConverter.getModelFromRegistruDocumenteJustificativePlati(documentJustificativPlati);
			documenteJustificativePlatiModels.add(documenteJustificativePlatiModel);
		}
		
		return documenteJustificativePlatiModels;
		
	}

	@Override
	public List<Integer> getYearsWithInregistrariDocumenteJustificativePlati() {
		return registruDocumenteJustificativePlatiDao.getYearsWithInregistrariDocumenteJustificativePlati();
	}

	@Override
	public DownloadableFile downloadAtasamentOfRegistruDocumenteJustificativePlatiById(Long atasamentId) {
		RegistruDocumenteJustificativePlatiAtasament atasament = registruDocumenteJustificativePlatiDao.findAtasamentOfRegistruIesiriById(atasamentId);
		return new DownloadableFile(atasament.getFileName(), atasament.getFilecontent());
	}
}
