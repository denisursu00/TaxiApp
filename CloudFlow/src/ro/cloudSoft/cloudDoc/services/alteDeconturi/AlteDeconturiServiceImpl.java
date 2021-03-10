package ro.cloudSoft.cloudDoc.services.alteDeconturi;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.ListIterator;

import org.apache.commons.lang.WordUtils;
import org.springframework.transaction.annotation.Transactional;

import ro.cloudSoft.cloudDoc.core.constants.NomenclatorConstants;
import ro.cloudSoft.cloudDoc.dao.alteDeconturi.AlteDeconturiDao;
import ro.cloudSoft.cloudDoc.dao.nomenclator.NomenclatorDao;
import ro.cloudSoft.cloudDoc.dao.nomenclator.NomenclatorValueDao;
import ro.cloudSoft.cloudDoc.domain.alteDeconturi.AlteDeconturi;
import ro.cloudSoft.cloudDoc.domain.alteDeconturi.AlteDeconturiCheltuiala;
import ro.cloudSoft.cloudDoc.domain.alteDeconturi.AlteDeconturiCheltuialaAtasament;
import ro.cloudSoft.cloudDoc.domain.content.Attachment;
import ro.cloudSoft.cloudDoc.domain.nomenclator.Nomenclator;
import ro.cloudSoft.cloudDoc.domain.nomenclator.NomenclatorValue;
import ro.cloudSoft.cloudDoc.domain.security.SecurityManager;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.DownloadableFile;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.alteDeconturi.AlteDeconturiModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.alteDeconturi.AlteDeconturiViewModel;
import ro.cloudSoft.cloudDoc.presentation.server.converters.alteDeconturi.AlteDeconturiConverter;
import ro.cloudSoft.cloudDoc.utils.FileSystemAttachmentManager;
import ro.cloudSoft.cloudDoc.utils.nomenclator.NomenclatorValueUtils;

public class AlteDeconturiServiceImpl implements AlteDeconturiService {

	private AlteDeconturiDao alteDeconturiDao;
	private NomenclatorValueDao nomenclatorValueDao;
	private NomenclatorDao nomenclatorDao;
	private AlteDeconturiConverter alteDeconturiConverter;
	private FileSystemAttachmentManager fileSystemAttachmentManager;
	
	public void setAlteDeconturiDao(AlteDeconturiDao alteDeconturiDao) {
		this.alteDeconturiDao = alteDeconturiDao;
	}
	
	public void setNomenclatorValueDao(NomenclatorValueDao nomenclatorValueDao) {
		this.nomenclatorValueDao = nomenclatorValueDao;
	}
	
	public void setNomenclatorDao(NomenclatorDao nomenclatorDao) {
		this.nomenclatorDao = nomenclatorDao;
	}
	
	public void setAlteDeconturiConverter(AlteDeconturiConverter alteDeconturiConverter) {
		this.alteDeconturiConverter = alteDeconturiConverter;
	}
	
	public void setFileSystemAttachmentManager(FileSystemAttachmentManager fileSystemAttachmentManager) {
		this.fileSystemAttachmentManager = fileSystemAttachmentManager;
	}

	@Override
	public List<Integer> getYearsOfExistingDeconturi() {
		return alteDeconturiDao.getYearsOfExistingDeconturi();
	}

	@Override
	public List<AlteDeconturiViewModel> getAllAlteDeconturiViewModelsByYear(Integer year) {
		List<AlteDeconturi> alteDeconturi = alteDeconturiDao.getAllAlteDeconturiByYear(year);
		List<AlteDeconturiViewModel> deconturiViewModels = alteDeconturiConverter.getAlteDeconturiViewModelsFromEntities(alteDeconturi);
		return deconturiViewModels;
	}

	@Override
	public AlteDeconturiModel getDecontById(Long decontId) {
		AlteDeconturi decont = alteDeconturiDao.findDecontById(decontId);
		AlteDeconturiModel decontModel = alteDeconturiConverter.getAlteDeconturiModelFromEntity(decont);
		return decontModel;
	}

	@Override
	public boolean isDecontCanceled(Long decontId) {
		AlteDeconturi decont = alteDeconturiDao.findDecontById(decontId);
		return decont.isAnulat();
	}

	@Override
	@Transactional
	public void saveAlteDeconturi(AlteDeconturiModel alteDeconturiModel, SecurityManager userSecurity) {
		
		List<AlteDeconturiCheltuiala> alteDeconturiCheltuieli = new ArrayList<>();
		if (alteDeconturiModel.getId() != null) {
			alteDeconturiCheltuieli = alteDeconturiDao.findDecontById(alteDeconturiModel.getId()).getCheltuieli();
		}
		
		AlteDeconturi alteDeconturi = alteDeconturiConverter.getAlteDeconturiFromModel(alteDeconturiModel);
		
		if (alteDeconturi.getId() != null) {
			
			List<Long> alteDeconturiCheltuieliIdToRemove = new ArrayList<>();
			List<Long> atasamenteIdToRemove = new ArrayList<>();
			for (AlteDeconturiCheltuiala alteDeconturiCheltuieliPersisted : alteDeconturiCheltuieli) {
				boolean foundPersistedCheltuialaInNewList = false;
				for (AlteDeconturiCheltuiala alteDeconturiCheltuieliNew : alteDeconturi.getCheltuieli()) {
					if (alteDeconturiCheltuieliNew.getId() != null && alteDeconturiCheltuieliNew.getId().equals(alteDeconturiCheltuieliPersisted.getId())) {
						foundPersistedCheltuialaInNewList = true;
					
						if (alteDeconturiCheltuieliPersisted.getAtasamente() != null) {
							for (AlteDeconturiCheltuialaAtasament atasamentPersisted : alteDeconturiCheltuieliPersisted.getAtasamente() ) {
								boolean foundPersistedAttachmentInNewList = false;
								for (AlteDeconturiCheltuialaAtasament atasamentNew : alteDeconturiCheltuieliNew.getAtasamente()) {
									if (atasamentNew.getId() != null && atasamentPersisted.getId().equals(atasamentPersisted.getId())) {
										foundPersistedAttachmentInNewList = true;
									}
								}
								if (!foundPersistedAttachmentInNewList) {
									atasamenteIdToRemove.add(atasamentPersisted.getId());
								}
							}
							
						}
					}					
					
				}
				
				if (!foundPersistedCheltuialaInNewList) {
					alteDeconturiCheltuieliIdToRemove.add(alteDeconturiCheltuieliPersisted.getId());
					if (alteDeconturiCheltuieliPersisted.getAtasamente() != null) {
						for (AlteDeconturiCheltuialaAtasament alteDeconturiCheltuialaAtasament : alteDeconturiCheltuieliPersisted.getAtasamente()) {
							if (alteDeconturiCheltuialaAtasament.getId() != null) {
								atasamenteIdToRemove.add(alteDeconturiCheltuialaAtasament.getId());
							}
						}
					}
				}
			}
						
			alteDeconturiDao.deleteAtasamenteOfCheltuialaByIds(atasamenteIdToRemove);			
			alteDeconturiDao.deleteCheltuieli(alteDeconturiCheltuieliIdToRemove);
		}	
		
		updateAttachmentsForSave(alteDeconturi.getCheltuieli(), userSecurity);
		
		if (alteDeconturi.getNumarDecont() == null) {
					
			int year = getCurrentYear();
			NomenclatorValue alteDeconturiCuUltimulNumarDecont = generateAlteDeconturiNumarDecontByYear(year);
			
			alteDeconturi.setNumarDecont(NomenclatorValueUtils.getAttributeValueAsString(alteDeconturiCuUltimulNumarDecont, NomenclatorConstants.ALTE_DECONTURI_GENERARE_NUMAR_DECONT_ATTRIBUTE_KEY_NUMAR_DECONT) + "/" + year);
		}
		
		alteDeconturiDao.saveAlteDeconturi(alteDeconturi);
	}
	
	private void updateAttachmentsForSave(List<AlteDeconturiCheltuiala> cheltuieli, SecurityManager userSecurity) {
		if (cheltuieli != null) {

			List<Attachment> attachments = fileSystemAttachmentManager.getAll(userSecurity.getUserUsername());
			
			for (AlteDeconturiCheltuiala cheltuiala : cheltuieli) {
				if (cheltuiala.getAtasamente() != null) {
					ListIterator<AlteDeconturiCheltuialaAtasament> iteratorAtasament =  cheltuiala.getAtasamente().listIterator();
					while (iteratorAtasament.hasNext()) { 
						AlteDeconturiCheltuialaAtasament atasament = iteratorAtasament.next();
						if (atasament.getId() == null) {
							for(Attachment attachment : attachments) {
								if (attachment.getName().equals(atasament.getFileName())) {
									atasament.setFileContent(attachment.getData());
								}
							}
							if (atasament.getFileContent() == null) {
								iteratorAtasament.remove(); //TODO: fix in https://mantis.cloud-soft.eu/view.php?id=17209
							}
						}
					}
				}				
			}			
		}
	}
	
	private int getCurrentYear() {
		return Calendar.getInstance().get(Calendar.YEAR);
	}
	
	private NomenclatorValue generateAlteDeconturiNumarDecontByYear(int year) {

		Long numarDecontNou = null;

		Nomenclator alteDeconturiGenerareNumarDecontNomenclator = getAlteDeconturiGenerareNumarDecontNomenclator();
		NomenclatorValue alteDeconturiCuUltimulNumarDecont = getAlteDeconturiCuUltimulNumarDecontByNomenclatorAndCurrentYear(
				alteDeconturiGenerareNumarDecontNomenclator.getId(), String.valueOf(year));
		
		if (alteDeconturiCuUltimulNumarDecont != null) {
			String numarDecont = NomenclatorValueUtils.getAttributeValueAsString(alteDeconturiCuUltimulNumarDecont, NomenclatorConstants.ALTE_DECONTURI_GENERARE_NUMAR_DECONT_ATTRIBUTE_KEY_NUMAR_DECONT);
			numarDecontNou = Long.valueOf(numarDecont) + 1;
			NomenclatorValueUtils.setAttributeValue(alteDeconturiCuUltimulNumarDecont, NomenclatorConstants.ALTE_DECONTURI_GENERARE_NUMAR_DECONT_ATTRIBUTE_KEY_NUMAR_DECONT, String.valueOf(numarDecontNou));
		} else {
			numarDecontNou = 1L;
			
			alteDeconturiCuUltimulNumarDecont = new NomenclatorValue();
			alteDeconturiCuUltimulNumarDecont.setNomenclator(alteDeconturiGenerareNumarDecontNomenclator);
			NomenclatorValueUtils.setAttributeValue(alteDeconturiCuUltimulNumarDecont, NomenclatorConstants.ALTE_DECONTURI_GENERARE_NUMAR_DECONT_ATTRIBUTE_KEY_NUMAR_DECONT, String.valueOf(numarDecontNou));
			NomenclatorValueUtils.setAttributeValue(alteDeconturiCuUltimulNumarDecont, NomenclatorConstants.ALTE_DECONTURI_GENERARE_NUMAR_DECONT_ATTRIBUTE_KEY_AN, String.valueOf(year));
		}
		
		nomenclatorValueDao.save(alteDeconturiCuUltimulNumarDecont);
		
		return alteDeconturiCuUltimulNumarDecont;
	}
	
	private NomenclatorValue getAlteDeconturiCuUltimulNumarDecontByNomenclatorAndCurrentYear(Long nomenclatorId, String year) {
		return nomenclatorValueDao.getNomenclatorValueCuUltimulNumarInregistrareByNomenclatorIdAndCurrentYear(nomenclatorId, year);
	}
	
	private Nomenclator getAlteDeconturiGenerareNumarDecontNomenclator() {
		return nomenclatorDao.findByCode(NomenclatorConstants.ALTE_DECONTURI_GENERARE_NUMAR_DECONT_NOMENCLATOR_CODE);
	}

	@Override
	@Transactional
	public void deleteDecontById(Long decontId) {
		AlteDeconturi alteDeconturi = alteDeconturiDao.findDecontById(decontId);
		List<Long> cheltuieliIds = new ArrayList<Long>();
		for (AlteDeconturiCheltuiala cheltuiala : alteDeconturi.getCheltuieli()) {
			cheltuieliIds.add(cheltuiala.getId());
		}
		alteDeconturiDao.deleteCheltuieli(cheltuieliIds);
		alteDeconturiDao.deleteDecontById(decontId);
	}

	@Override
	public void cancelDecont(Long decontId, String motivAnulare) {
		AlteDeconturi decont = alteDeconturiDao.findDecontById(decontId);
		decont.setAnulat(true);
		decont.setMotivAnulare(motivAnulare);
		alteDeconturiDao.saveAlteDeconturi(decont);
	}

	@Override
	public void finalizeDecontById(Long decontId) {
		AlteDeconturi decont = alteDeconturiDao.findDecontById(decontId);
		decont.setFinalizat(true);
		alteDeconturiDao.saveAlteDeconturi(decont);
	}

	@Override
	public boolean isDecontFinalized(Long decontId) {
		AlteDeconturi decont = alteDeconturiDao.findDecontById(decontId);
		return decont.isFinalizat();
	}

	@Override
	public DownloadableFile downloadAtasamentOfCheltuialaById(Long attachmentId) {
		AlteDeconturiCheltuialaAtasament atasament = alteDeconturiDao.findAtasamentOfCheltuialaById(attachmentId);
		if (atasament != null) {
			return new DownloadableFile(atasament.getFileName(), atasament.getFileContent());
		}		
		return null;
	}

	@Override
	public List<String> getAllDistinctTitulari() {
		List<String> titulari = alteDeconturiDao.getAllDistinctTitulari();
		
		titulari.replaceAll(titular -> WordUtils.capitalizeFully(titular));
		
		return titulari;
	}

}
