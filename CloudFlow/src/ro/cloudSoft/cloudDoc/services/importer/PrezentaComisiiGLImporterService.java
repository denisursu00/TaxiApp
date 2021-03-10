package ro.cloudSoft.cloudDoc.services.importer;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MultiValuedMap;
import org.apache.commons.collections4.multimap.HashSetValuedHashMap;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.extjs.gxt.ui.client.data.ModelData;

import ro.cloudSoft.cloudDoc.core.constants.FormatConstants;
import ro.cloudSoft.cloudDoc.core.constants.NomenclatorConstants;
import ro.cloudSoft.cloudDoc.core.constants.arb.DocumentPrezentaComisieGlConstants;
import ro.cloudSoft.cloudDoc.dao.content.ImportedDocumentDao;
import ro.cloudSoft.cloudDoc.dao.nomenclator.NomenclatorValueDao;
import ro.cloudSoft.cloudDoc.domain.content.DocumentType;
import ro.cloudSoft.cloudDoc.domain.content.ImportedDocument;
import ro.cloudSoft.cloudDoc.domain.content.ImportedDocument.ImportSource;
import ro.cloudSoft.cloudDoc.domain.content.ListMetadataDefinition;
import ro.cloudSoft.cloudDoc.domain.content.ListMetadataItem;
import ro.cloudSoft.cloudDoc.domain.content.MetadataCollection;
import ro.cloudSoft.cloudDoc.domain.content.MetadataDefinition;
import ro.cloudSoft.cloudDoc.domain.nomenclator.NomenclatorValue;
import ro.cloudSoft.cloudDoc.domain.organization.Group;
import ro.cloudSoft.cloudDoc.domain.organization.User;
import ro.cloudSoft.cloudDoc.plugins.organization.UserPersistencePlugin;
import ro.cloudSoft.cloudDoc.presentation.client.shared.exceptions.PresentationException;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.PermissionModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.content.CollectionInstanceModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.content.DocumentLocationModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.content.DocumentModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.content.FolderModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.content.MetadataCollectionInstanceModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.content.MetadataInstanceModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.importer.InformatiiPrezentaComisiiGLImportModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.importer.PrezentaComisiiGLImportModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.services.content.DocumentGxtService;
import ro.cloudSoft.cloudDoc.presentation.client.shared.services.content.DocumentLocationGxtService;
import ro.cloudSoft.cloudDoc.services.appUtilities.AppUtilitiesException;
import ro.cloudSoft.cloudDoc.services.content.DocumentTypeService;
import ro.cloudSoft.cloudDoc.services.organization.GroupService;
import ro.cloudSoft.cloudDoc.utils.DocumentTypeUtils;
import ro.cloudSoft.cloudDoc.utils.excel.ExcelReaderUtils;

public class PrezentaComisiiGLImporterService {

	private static final String DATE_TIME_FORMAT_FROM_IMPORT = "yyyy.MM.dd HH:mm";
	private static final String DATE_FORMAT_FOR_EXPORT_IN_FILE_NAME = "dd.MM.yyyy";
	private final static String METATADATA_COLLECTION_INFORMATII_PARTICIPANTI_NAME = "informatii_participanti";
	private final static String CALITAET_PARTICIPANT_VALUE_TITULAR = "titular";

	@Autowired
	private DocumentTypeService documentTypeService;
	@Autowired
	private DocumentPrezentaComisieGlConstants documentPrezentaComisieGlConstants;
	
	private DocumentType documentType;
	@Autowired
	private UserPersistencePlugin userPersistencePlugin;
	@Autowired
	private NomenclatorValueDao nomenclatorValueDao;
	@Autowired
	private DocumentGxtService documentGxtService;
	@Autowired
	private GroupService groupService;
	@Autowired
	private DocumentLocationGxtService documentLocationGxtService;
	@Autowired
	private ImportedDocumentDao importedDocumentDao;
	
	public void importData(String filePath, String workspaceName, String folderName) throws AppUtilitiesException {
		String documentLocationRealName = getDocLocationRealName(workspaceName);
		String parentFolderId = getFolderId(folderName, documentLocationRealName);

		int columnLengthToRead = 16;
		Map<Integer, List<Object>> fileDataAsMap = ExcelReaderUtils.getDataFromFile(filePath, columnLengthToRead);

		MultiValuedMap<PrezentaComisiiGLImportModel, InformatiiPrezentaComisiiGLImportModel> importDataAsMultimap = new HashSetValuedHashMap<PrezentaComisiiGLImportModel, InformatiiPrezentaComisiiGLImportModel>();
		int firstRowIndex = 2; // first row with data
		for (int rowIndex = firstRowIndex; rowIndex < fileDataAsMap.size(); rowIndex++) {
			List<Object> row = fileDataAsMap.get(new Integer(rowIndex));
			buildModelFromFileData(importDataAsMultimap, row, rowIndex);
			
		}

		documentType = documentTypeService.getDocumentTypeByName(documentPrezentaComisieGlConstants.getDocumentTypeName());
		Map<String, MetadataCollection> metadataCollectionsAsMap = DocumentTypeUtils.getMetadataCollectionsAsMap(documentType);
		Map<String, MetadataDefinition> metadataDefinitionsAsMap = DocumentTypeUtils.getMetadataDefinitionsAsMap(documentType);

		for (PrezentaComisiiGLImportModel model : importDataAsMultimap.keySet()) {

			Collection<InformatiiPrezentaComisiiGLImportModel> informatiiPrezentaModelColl = importDataAsMultimap
					.get(model);

			DocumentModel documentModel = new DocumentModel();

			documentModel.setDocumentLocationRealName(documentLocationRealName);
			SimpleDateFormat dateFormatImport = new SimpleDateFormat(DATE_TIME_FORMAT_FROM_IMPORT);
			SimpleDateFormat dateFormatExportInFileName = new SimpleDateFormat(DATE_FORMAT_FOR_EXPORT_IN_FILE_NAME);
			String dateFileName = null;
			try {
				Date dataInceput = dateFormatImport.parse(model.getDataInceput());
				dateFileName = dateFormatExportInFileName.format(dataInceput);
			} catch (ParseException e) {
				throw new AppUtilitiesException(e.getMessage() + " la lina:" + model.getRowIndex() + " campul Data inceput sedinta");
			}

			documentModel.setDocumentName("Prezenta - " + model.getDenumireComisieGl() + " - " + dateFileName);
			documentModel.setId(null);
			documentModel.setCreated(new Date());
			User userAutor = userPersistencePlugin.getUserByUsername("application.user");
			documentModel.setAuthor(userAutor.getId().toString());
			List<PermissionModel> permisiiModel = new ArrayList<>();

			Group groupResponsabilComisie = groupService.getGroupByName("Responsabili GL Comisie");

			permisiiModel.add(new PermissionModel(PermissionModel.TYPE_GROUP, PermissionModel.PERMISSION_COLABORATOR,
					groupResponsabilComisie.getId().toString(), groupResponsabilComisie.getName()));
			documentModel.setPermissions(permisiiModel);
			documentModel.setAttachments(Collections.EMPTY_LIST);
			documentModel.setDocumentTypeId(documentType.getId());

			addToDocumentModelMetadataDefinitionsFields(model, metadataDefinitionsAsMap, documentModel);

			addToDocumentModelMetadataCollectionFieldsFromInformatiiPart(metadataCollectionsAsMap, documentModel,
					informatiiPrezentaModelColl);

			Collection<Long> definitionIdsForAutoNumberMetadataValuesToGenerate = Collections.EMPTY_LIST;
			try {
				String documentId = documentGxtService.checkin(documentModel, true, parentFolderId, documentLocationRealName,
						definitionIdsForAutoNumberMetadataValuesToGenerate, Collections.EMPTY_LIST);
				
				ImportedDocument importedDocument = new ImportedDocument();
				importedDocument.setDocumentId(documentId);
				importedDocument.setDocumentLocationRealName(documentLocationRealName);
				importedDocument.setImportSource(ImportSource.EXCEL);
				this.importedDocumentDao.save(importedDocument);
				
			} catch (PresentationException e) {
				throw new AppUtilitiesException(e.getMessage());
			}
		}
	}

	private String getFolderId(String folderName, String documentLocationRealName) throws AppUtilitiesException {
		List<ModelData> foldersFromDocumentLocation;
		try {
			foldersFromDocumentLocation = documentLocationGxtService
					.getFoldersFromDocumentLocation(documentLocationRealName);
		} catch (PresentationException e) {
			throw new AppUtilitiesException(e.getMessage());
		}
		String parentFolderId = null;
		StringBuffer allFolderNames = new StringBuffer();
		for (ModelData modelData : foldersFromDocumentLocation) {
			FolderModel folderModel = (FolderModel) modelData;
			allFolderNames.append(";" + folderModel.getName());
			if (folderModel.getName().equalsIgnoreCase(folderName)) {
				parentFolderId = folderModel.getId();
			}
		}
		if (parentFolderId == null) {
			throw new AppUtilitiesException(
					"The folder name does not exist! Choose one from:" + allFolderNames.toString());
		}
		return parentFolderId;
	}

	private String getDocLocationRealName(String workspaceName) throws AppUtilitiesException {
		List<DocumentLocationModel> allDocumentLocations = new ArrayList<>();
		try {
			allDocumentLocations = documentLocationGxtService.getAllDocumentLocations();
		} catch (PresentationException e) {
			throw new AppUtilitiesException(e.getMessage());
		}
		String documentLocationRealName = null;
		StringBuffer allWorkspaceNames = new StringBuffer();
		for (DocumentLocationModel documentLocationModel : allDocumentLocations) {
			allWorkspaceNames.append(";" + documentLocationModel.getName());
			if (documentLocationModel.getName().equalsIgnoreCase(workspaceName)) {
				documentLocationRealName = documentLocationModel.getRealName();
			}
		}
		if (documentLocationRealName == null) {
			throw new AppUtilitiesException(
					"The workspace name does not exist! Choose one from:" + allWorkspaceNames.toString());
		}
		return documentLocationRealName;
	}

	private void addToDocumentModelMetadataCollectionFieldsFromInformatiiPart( Map<String, MetadataCollection> metadataCollectionsAsMap, DocumentModel dm,
			Collection<InformatiiPrezentaComisiiGLImportModel> informatiiPrezentaModelColl) throws AppUtilitiesException {
		
		List<MetadataCollectionInstanceModel> metadataCollectionInstanceModels = new ArrayList<MetadataCollectionInstanceModel>();

		// metadata colectie "Informatii participanti(colectie)"
		MetadataCollectionInstanceModel informatiiPartMtdCollIM = new MetadataCollectionInstanceModel();
		MetadataCollection informatiiParticitpantiMtdColl = metadataCollectionsAsMap
				.get(METATADATA_COLLECTION_INFORMATII_PARTICIPANTI_NAME);
		informatiiPartMtdCollIM.setMetadataDefinitionId(informatiiParticitpantiMtdColl.getId());

		// Nume institutie* (NOMENCLATOR institutii)
		MetadataDefinition numeInstitutieMD = DocumentTypeUtils.getMetadataDefinitionFromCollectionByName(documentType,
				METATADATA_COLLECTION_INFORMATII_PARTICIPANTI_NAME, "nume_institutie");
		// Membru acreditat
		MetadataDefinition membruAcredMD = DocumentTypeUtils.getMetadataDefinitionFromCollectionByName(documentType,
				METATADATA_COLLECTION_INFORMATII_PARTICIPANTI_NAME, "membru_acreditat");
		// Nume membru inlocuitor
		MetadataDefinition numeMembruInlocMD = DocumentTypeUtils.getMetadataDefinitionFromCollectionByName(documentType,
				METATADATA_COLLECTION_INFORMATII_PARTICIPANTI_NAME, "nume_membru_inlocuitor");
		// Prenume membru inlocuitor
		MetadataDefinition prenumeMembruInlocMD = DocumentTypeUtils.getMetadataDefinitionFromCollectionByName(
				documentType, METATADATA_COLLECTION_INFORMATII_PARTICIPANTI_NAME, "prenume_membru_inlocuitor");
		// Functie*
		MetadataDefinition functieMembruMD = DocumentTypeUtils.getMetadataDefinitionFromCollectionByName(documentType,
				METATADATA_COLLECTION_INFORMATII_PARTICIPANTI_NAME, "functie_membru");
		// Departament*
		MetadataDefinition departamentMD = DocumentTypeUtils.getMetadataDefinitionFromCollectionByName(documentType,
				METATADATA_COLLECTION_INFORMATII_PARTICIPANTI_NAME, "departament");
		// Telefon*
		MetadataDefinition telefonMD = DocumentTypeUtils.getMetadataDefinitionFromCollectionByName(documentType,
				METATADATA_COLLECTION_INFORMATII_PARTICIPANTI_NAME, "telefon");
		// Adresa email participant*
		MetadataDefinition emailMD = DocumentTypeUtils.getMetadataDefinitionFromCollectionByName(documentType,
				METATADATA_COLLECTION_INFORMATII_PARTICIPANTI_NAME, "adresa_email_participant");
		// Calitate participant*
		ListMetadataDefinition calitateParticipantListMD = (ListMetadataDefinition) DocumentTypeUtils.getMetadataDefinitionFromCollectionByName(
				documentType, METATADATA_COLLECTION_INFORMATII_PARTICIPANTI_NAME, "calitate_participant");

		List<CollectionInstanceModel> collectionInstanceRows = new ArrayList<CollectionInstanceModel>();
		informatiiPartMtdCollIM.setCollectionInstanceRows(collectionInstanceRows);
		for (InformatiiPrezentaComisiiGLImportModel model : informatiiPrezentaModelColl) {
			CollectionInstanceModel collectionInstanceRowModel = new CollectionInstanceModel();
			List<MetadataInstanceModel> metadataInstanceList = new ArrayList<MetadataInstanceModel>();

			MetadataInstanceModel numeInstitutieMtdInst = new MetadataInstanceModel();
			if (StringUtils.isEmpty(model.getNumeInstitutie())) {
				throw new AppUtilitiesException("In documentul importat la linia: " + model.getRowIndex() + " > Nu exista valoare pentru <Nume institutie>");
			}
			List<NomenclatorValue> numeInstitutieNValues = nomenclatorValueDao.findByNomenclatorCodeAndAttribute( NomenclatorConstants.INSTITUTII_NOMENCLATOR_CODE,
					NomenclatorConstants.INSTITUTII_ATTRIBUTE_KEY_DENUMIRE, model.getNumeInstitutie());
			if (CollectionUtils.isEmpty(numeInstitutieNValues)) {
						throw new AppUtilitiesException("In documentul importat la linia: " + model.getRowIndex() + " valoarea:" + model.getNumeInstitutie() + " nu se regaseste in nomenclator institutie");
			}
			NomenclatorValue numeInstitutieNV = numeInstitutieNValues.get(0);
			numeInstitutieMtdInst.setMetadataDefinitionId(numeInstitutieMD.getId());
			numeInstitutieMtdInst.setValue(numeInstitutieNV.getId().toString());
			metadataInstanceList.add(numeInstitutieMtdInst);

			MetadataInstanceModel membruAcreditatMtdInst = new MetadataInstanceModel();
			membruAcreditatMtdInst.setMetadataDefinitionId(membruAcredMD.getId());
			membruAcreditatMtdInst.setValue(model.getMembruAcred());
			metadataInstanceList.add(membruAcreditatMtdInst);

			if (StringUtils.isNotEmpty(model.getNumeMembruInloc())) {
				MetadataInstanceModel numeMembruInlocMtdInst = new MetadataInstanceModel();
				numeMembruInlocMtdInst.setMetadataDefinitionId(numeMembruInlocMD.getId());
				numeMembruInlocMtdInst.setValue(model.getNumeMembruInloc());
				metadataInstanceList.add(numeMembruInlocMtdInst);
			}

			if (StringUtils.isNotEmpty(model.getPrenumeMembruInloc())) {
				MetadataInstanceModel prenumeMembruInlocMtdInst = new MetadataInstanceModel();
				prenumeMembruInlocMtdInst.setMetadataDefinitionId(prenumeMembruInlocMD.getId());
				prenumeMembruInlocMtdInst.setValue(model.getPrenumeMembruInloc());
				metadataInstanceList.add(prenumeMembruInlocMtdInst);
			}

			if (StringUtils.isNotEmpty(model.getFunctie())) {
				MetadataInstanceModel functieMtdInst = new MetadataInstanceModel();
				functieMtdInst.setMetadataDefinitionId(functieMembruMD.getId());
				functieMtdInst.setValue(model.getFunctie());
				metadataInstanceList.add(functieMtdInst);
			}

			if (StringUtils.isNotEmpty(model.getDepartament())) {
				MetadataInstanceModel departamentMtdInst = new MetadataInstanceModel();
				departamentMtdInst.setMetadataDefinitionId(departamentMD.getId());
				departamentMtdInst.setValue(model.getDepartament());
				metadataInstanceList.add(departamentMtdInst);
			}

			if (StringUtils.isNotEmpty(model.getTelefon())) {
				MetadataInstanceModel telefonMtdInst = new MetadataInstanceModel();
				telefonMtdInst.setMetadataDefinitionId(telefonMD.getId());
				telefonMtdInst.setValue(model.getTelefon());
				metadataInstanceList.add(telefonMtdInst);
			}

			if (StringUtils.isNotEmpty(model.getEmail())) {
				MetadataInstanceModel emailMtdInst = new MetadataInstanceModel();
				emailMtdInst.setMetadataDefinitionId(emailMD.getId());
				emailMtdInst.setValue(model.getEmail());
				metadataInstanceList.add(emailMtdInst);
			}
			
			if (StringUtils.isNotEmpty(model.getCalitate())) {
				String listItemValue = null;
				for (ListMetadataItem listItem: calitateParticipantListMD.getListItems()) {
					if (listItem.getLabel().trim().toLowerCase().equals(model.getCalitate().trim().toLowerCase())) {
						listItemValue = listItem.getValue();
					}
				}
				if (listItemValue == null) {
					throw new AppUtilitiesException("In documentul importat la linia: " + model.getRowIndex() + " valoarea:" + model.getCalitate() + " nu se regaseste in tabelul ListMetadataItem pentru documentul curent");
				}
				MetadataInstanceModel calitateParticipantMtdInst = new MetadataInstanceModel();
				calitateParticipantMtdInst.setMetadataDefinitionId(calitateParticipantListMD.getId());
				//get calitate value By label
				calitateParticipantMtdInst.setValue(listItemValue);
				metadataInstanceList.add(calitateParticipantMtdInst);
			}

			collectionInstanceRowModel.setMetadataInstanceList(metadataInstanceList);
			collectionInstanceRows.add(collectionInstanceRowModel);
		}

		metadataCollectionInstanceModels.add(informatiiPartMtdCollIM);
		dm.setMetadataCollectionInstances(metadataCollectionInstanceModels);
	}

	private void buildModelFromFileData(MultiValuedMap<PrezentaComisiiGLImportModel, InformatiiPrezentaComisiiGLImportModel> importDataAsMultimap,
			List<Object> row, int rowIndex) throws AppUtilitiesException {
		
		PrezentaComisiiGLImportModel model = new PrezentaComisiiGLImportModel();
		int rowIndexTabel = rowIndex + 1;
		model.setRowIndex(rowIndexTabel);
		if ((row.get(0) == null) || StringUtils.isEmpty((String) row.get(0))) {
			throw new AppUtilitiesException("In documentul importat la linia: " + model.getRowIndex() + " > Nu exista valoare pentru <Initiator prezenta Comisie GL>");
		}
		model.setIntiatorPrezenta((String) row.get(0));
		if ((row.get(1) == null) || StringUtils.isEmpty((String) row.get(1))) {
			throw new AppUtilitiesException("In documentul importat la linia: " + model.getRowIndex() + " > Nu exista valoare pentru <Sedinta>");
		}
		model.setSedinta((String) row.get(1));
		if ((row.get(2) == null) || StringUtils.isEmpty((String) row.get(2))) {
			throw new AppUtilitiesException("In documentul importat la linia: " + model.getRowIndex() + " > Nu exista valoare pentru <Denumire Comisie GL>");
		}
		model.setDenumireComisieGl((String) row.get(2));
		if (row.get(3) == null) {
			throw new AppUtilitiesException("In documentul importat la linia: " + model.getRowIndex() + " > Nu exista valoare pentru <Data inceput sedinta>");
		}
		DateFormat dateTimeMetadataForSaveFormat = new SimpleDateFormat(FormatConstants.METADATA_DATE_TIME_FOR_SAVING);
		Date dataInceput = (Date) row.get(3);
		model.setDataInceput(dateTimeMetadataForSaveFormat.format(dataInceput));
		if (row.get(4) == null) {
			throw new AppUtilitiesException("In documentul importat la linia: " + model.getRowIndex() + " > Nu exista valoare pentru <Data sfarsit sedinta>");
		}
		Date dataSfarsit = (Date) row.get(4);
		model.setDataSfarsit(dateTimeMetadataForSaveFormat.format(dataSfarsit));
		if ((row.get(15) == null) || StringUtils.isEmpty((String) row.get(15))) {
			throw new AppUtilitiesException("In documentul importat la linia: " + model.getRowIndex() + " > Nu exista valoare pentru <Responsabil GL Comisie>");
		}
		if (dataInceput.getTime() > dataSfarsit.getTime()) {
			throw new AppUtilitiesException("In documentul importat la linia: " + model.getRowIndex() + " > <Data inceput sedinta> trebuie sa fie mai mica decat <Data sfarsit sedinta>");

		}
		model.setResponsabil((String) row.get(15));

		InformatiiPrezentaComisiiGLImportModel informatiiPrezComGlImportModel = new InformatiiPrezentaComisiiGLImportModel();
		informatiiPrezComGlImportModel.setRowIndex(rowIndexTabel);
		informatiiPrezComGlImportModel.setTipInstitutie((String) row.get(5));
		informatiiPrezComGlImportModel.setNumeInstitutie((String) row.get(6));
		informatiiPrezComGlImportModel.setMembruAcred((String) row.get(7));
		informatiiPrezComGlImportModel.setNumeMembruInloc((String) row.get(8));
		informatiiPrezComGlImportModel.setPrenumeMembruInloc((String) row.get(9));
		informatiiPrezComGlImportModel.setFunctie((String) row.get(10));
		informatiiPrezComGlImportModel.setDepartament((String) row.get(11));
		informatiiPrezComGlImportModel.setTelefon((String) row.get(12));
		informatiiPrezComGlImportModel.setEmail((String) row.get(13));
		informatiiPrezComGlImportModel.setCalitate((String) row.get(14));
		importDataAsMultimap.put(model, informatiiPrezComGlImportModel);
	}

	private void addToDocumentModelMetadataDefinitionsFields(PrezentaComisiiGLImportModel model,
			Map<String, MetadataDefinition> metadataDefinitionsAsMap, DocumentModel dm) throws AppUtilitiesException{

		List<MetadataInstanceModel> metadataInstanceModels = new ArrayList<>();

		// initiator
		User userInitiator = userPersistencePlugin
				.getUserByUsername(buildUsernameFromName(model.getIntiatorPrezenta()));
		if (userInitiator == null) {
			throw new AppUtilitiesException("In documentul importat la linia: " + model.getRowIndex() + " nu sa gasit valoarea in tabela useri pentru <Initiator prezenta Comisie GL>");
		}
		MetadataInstanceModel initiatorMtdInst = new MetadataInstanceModel();
		MetadataDefinition initiatorMD = metadataDefinitionsAsMap.get("initiator_prezenta_comisie_gl");
		initiatorMtdInst.setMetadataDefinitionId(initiatorMD.getId());
		initiatorMtdInst.setValue(userInitiator.getId().toString());
		metadataInstanceModels.add(initiatorMtdInst);

		// sedinta
		MetadataInstanceModel sedintaMtdInst = new MetadataInstanceModel();
		MetadataDefinition sedintaMD = metadataDefinitionsAsMap
				.get(documentPrezentaComisieGlConstants.getSedintaMetadataName());
		List<NomenclatorValue> sedintaNVList = nomenclatorValueDao
				.findByNomenclatorCodeAndAttribute(NomenclatorConstants.CATEGORII_COMISII_SAU_GL_NOMENCLATOR_CODE,
						NomenclatorConstants.COMISII_SAU_GL_ATTRIBUTE_KEY_CATEGORIE, model.getSedinta());
		if (CollectionUtils.isEmpty(sedintaNVList)) {
			throw new AppUtilitiesException("In documentul importat la linia: " + model.getRowIndex() + " nu sa gasit valoarea in nomenclatorul Categorii comisii sau GL pentru <Sedinta>");
		}
		sedintaMtdInst.setMetadataDefinitionId(sedintaMD.getId());
		sedintaMtdInst.setValue(sedintaNVList.get(0).getId().toString());
		metadataInstanceModels.add(sedintaMtdInst);

		// Denumire Comisie GL*
		MetadataInstanceModel denumireComisieGLMtdInst = new MetadataInstanceModel();
		MetadataDefinition denumireComisieGLMD = metadataDefinitionsAsMap
				.get(documentPrezentaComisieGlConstants.getDenumireComisieGlMetadataName());

		List<NomenclatorValue> denumireComisieGLNVList = nomenclatorValueDao
				.findByNomenclatorCodeAndAttribute(NomenclatorConstants.COMISII_SAU_GL_NOMENCLATOR_CODE,
						NomenclatorConstants.COMISII_SAU_GL_ATTRIBUTE_KEY_DENUMIRE, model.getDenumireComisieGl());
		if (CollectionUtils.isEmpty(denumireComisieGLNVList)) {
			throw new AppUtilitiesException("In documentul importat la linia: " + model.getRowIndex() + " nu sa gasit valoarea in nomenclatorul Comisii sau GL pentru <Denumire Comisie GL>");
		}
		denumireComisieGLMtdInst.setMetadataDefinitionId(denumireComisieGLMD.getId());
		denumireComisieGLMtdInst.setValue(denumireComisieGLNVList.get(0).getId().toString());
		metadataInstanceModels.add(denumireComisieGLMtdInst);

		// Data inceput sedinta*
		MetadataInstanceModel dataInceputMtdInst = new MetadataInstanceModel();
		MetadataDefinition dataInceputGLMD = metadataDefinitionsAsMap
				.get(documentPrezentaComisieGlConstants.getDataInceputMetadataName());
		dataInceputMtdInst.setMetadataDefinitionId(dataInceputGLMD.getId());
		dataInceputMtdInst.setValue(model.getDataInceput());
		metadataInstanceModels.add(dataInceputMtdInst);

		// Data sfarsit sedinta*
		MetadataInstanceModel dataSfarsitMtdInst = new MetadataInstanceModel();
		MetadataDefinition dataSfarsitGLMD = metadataDefinitionsAsMap
				.get(documentPrezentaComisieGlConstants.getDataSfarsitMetadataName());
		dataSfarsitMtdInst.setMetadataDefinitionId(dataSfarsitGLMD.getId());
		dataSfarsitMtdInst.setValue(model.getDataSfarsit());
		metadataInstanceModels.add(dataSfarsitMtdInst);

		// responsabil
		User userResponsabil = userPersistencePlugin.getUserByUsername(buildUsernameFromName(model.getResponsabil()));
		if (userResponsabil == null) {
			throw new AppUtilitiesException("In documentul importat la linia: " + model.getRowIndex() + " nu sa gasit valoarea in tabela useri pentru <Responsabil GL Comisie>");
		}
		MetadataInstanceModel responsabilMtdInst = new MetadataInstanceModel();
		MetadataDefinition responsabilMD = metadataDefinitionsAsMap.get("responsabil_gl_comisie");
		responsabilMtdInst.setMetadataDefinitionId(responsabilMD.getId());
		responsabilMtdInst.setValue(userResponsabil.getId().toString());
		metadataInstanceModels.add(responsabilMtdInst);

		dm.setMetadataInstances(metadataInstanceModels);
	}

	public String buildUsernameFromName(String responsabilName) {
		String[] strings = responsabilName.split(" ");
		return strings[0].toLowerCase() + "." + strings[1].toLowerCase();
	}

	public void setDocumentTypeService(DocumentTypeService documentTypeService) {
		this.documentTypeService = documentTypeService;
	}

	public void setDocumentPrezentaComisieGlConstants(
			DocumentPrezentaComisieGlConstants documentPrezentaComisieGlConstants) {
		this.documentPrezentaComisieGlConstants = documentPrezentaComisieGlConstants;
	}

}
