package ro.cloudSoft.cloudDoc.services.content.autocompleteMetadata;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.springframework.util.CollectionUtils;

import ro.cloudSoft.cloudDoc.core.AppException;
import ro.cloudSoft.cloudDoc.core.constants.NomenclatorConstants;
import ro.cloudSoft.cloudDoc.core.constants.arb.DocumentMinutaSedintaComisieGLConstants;
import ro.cloudSoft.cloudDoc.core.constants.arb.DocumentPrezentaComisieGlConstants;
import ro.cloudSoft.cloudDoc.dao.content.DocumentTypeDao;
import ro.cloudSoft.cloudDoc.dao.nomenclator.NomenclatorValueDao;
import ro.cloudSoft.cloudDoc.domain.content.CollectionInstance;
import ro.cloudSoft.cloudDoc.domain.content.Document;
import ro.cloudSoft.cloudDoc.domain.content.DocumentIdentifier;
import ro.cloudSoft.cloudDoc.domain.content.DocumentType;
import ro.cloudSoft.cloudDoc.domain.content.MetadataCollection;
import ro.cloudSoft.cloudDoc.domain.content.MetadataDefinition;
import ro.cloudSoft.cloudDoc.domain.content.MetadataInstance;
import ro.cloudSoft.cloudDoc.domain.nomenclator.NomenclatorValue;
import ro.cloudSoft.cloudDoc.domain.security.SecurityManager;
import ro.cloudSoft.cloudDoc.plugins.content.DocumentPlugin;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.content.AutocompleteMetadataRequestModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.content.AutocompleteMetadataResponseModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.content.CollectionInstanceModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.content.MetadataCollectionInstanceModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.content.MetadataInstanceModel;
import ro.cloudSoft.cloudDoc.utils.DocumentTypeUtils;
import ro.cloudSoft.cloudDoc.utils.MetadataValueHelper;
import ro.cloudSoft.cloudDoc.utils.log.LogHelper;
import ro.cloudSoft.cloudDoc.utils.nomenclator.NomenclatorValueUtils;
import ro.cloudSoft.common.utils.spring.SpringUtils;

public class AutocompleteParticipantiOfMinutaSedintaComisieGL extends AutocompleteMetadata {

	private static final LogHelper logger = LogHelper.getInstance(AutocompleteParticipantiOfMinutaSedintaComisieGL.class);

	private static final String PARTICIPANT_VALUE_FOR_MEMBRU_AFILIAT = "membru_afiliat";
	private static final String PARTICIPANT_VALUE_FOR_MEMBRU_COMISIE = "membru_comisie";
	private static final String PARTICIPANT_VALUE_FOR_INVITAT = "invitat";

	private DocumentPlugin documentPlugin;
	private DocumentTypeDao documentTypeDao;
	private DocumentPrezentaComisieGlConstants prezentaConstants;
	private DocumentMinutaSedintaComisieGLConstants minutaConstants;
	private NomenclatorValueDao nomenclatorValueDao;

	private MetadataDefinition numarOfParticipantiMinutaMtdDef;
	private MetadataDefinition participantOfParticipantiMinutaMtdDef;
	private MetadataDefinition institutieOfParticipantiMinutaMtdDef;
	private MetadataDefinition comisieGlOfParticipantiMinutaMtdDef;

	private void resolveDependencies() {
		this.documentPlugin = SpringUtils.getBean(DocumentPlugin.class);
		this.documentTypeDao = SpringUtils.getBean(DocumentTypeDao.class);
		this.prezentaConstants = SpringUtils.getBean(DocumentPrezentaComisieGlConstants.class);
		this.minutaConstants = SpringUtils.getBean(DocumentMinutaSedintaComisieGLConstants.class);
		this.nomenclatorValueDao = SpringUtils.getBean(NomenclatorValueDao.class);
	}

	@Override
	protected AutocompleteMetadataResponseModel doAutocomplete(AutocompleteMetadataRequestModel request, SecurityManager userSecurity) throws AppException {

		AutocompleteMetadataResponseModel response = new AutocompleteMetadataResponseModel();

		try {
			this.resolveDependencies();

			List<String> sourceMetadataValues = request.getSourceMetadataValues();
			if (CollectionUtils.isEmpty(sourceMetadataValues)) {
				return response;
			}

			DocumentType minutaDocumentType = documentTypeDao.getDocumentTypeByName(minutaConstants.getDocumentTypeName());
			MetadataCollection participantiMinutaMtdCollDef = DocumentTypeUtils.getMetadataCollectionDefinitionByName(minutaDocumentType,
					minutaConstants.getParticipantMetadataName());
			numarOfParticipantiMinutaMtdDef = DocumentTypeUtils.getMetadataDefinitionFromCollectionByName(minutaDocumentType, minutaConstants.getParticipantMetadataName(),
					minutaConstants.getNumarOfParticipantMetadataName());
			participantOfParticipantiMinutaMtdDef = DocumentTypeUtils.getMetadataDefinitionFromCollectionByName(minutaDocumentType, minutaConstants.getParticipantMetadataName(),
					minutaConstants.getParticipantOfParticipantMetadataName());
			institutieOfParticipantiMinutaMtdDef = DocumentTypeUtils.getMetadataDefinitionFromCollectionByName(minutaDocumentType, minutaConstants.getParticipantMetadataName(),
					minutaConstants.getInstitutieOfParticipantMetadataName());
			comisieGlOfParticipantiMinutaMtdDef = DocumentTypeUtils.getMetadataDefinitionFromCollectionByName(minutaDocumentType, minutaConstants.getParticipantMetadataName(),
					minutaConstants.getComisieGlOfParticipantMetadataName());

			DocumentType prezentaDocType = documentTypeDao.getDocumentTypeByName(prezentaConstants.getDocumentTypeName());
			MetadataCollection infoParticipantiPrezentaMtdCollDef = DocumentTypeUtils.getMetadataCollectionDefinitionByName(prezentaDocType,
					prezentaConstants.getInformatiiParticipantiMetadataName());
			MetadataDefinition institutieOfInfoParticipantiPrezentaMtdDef = DocumentTypeUtils.getMetadataDefinitionFromCollectionByName(prezentaDocType,
					prezentaConstants.getInformatiiParticipantiMetadataName(), prezentaConstants.getNumeInstitutieOfInformatiiParticipantiMetadataName());
			MetadataDefinition membruAcredOfInfoParticipantiPrezentaMtdDef = DocumentTypeUtils.getMetadataDefinitionFromCollectionByName(prezentaDocType,
					prezentaConstants.getInformatiiParticipantiMetadataName(), prezentaConstants.getMembruAcreditatOfInformatiiParticipantiMetadataName());
			MetadataDefinition numeMembruOfInfoParticipantiPrezentaMtdDef = DocumentTypeUtils.getMetadataDefinitionFromCollectionByName(prezentaDocType,
					prezentaConstants.getInformatiiParticipantiMetadataName(), prezentaConstants.getNumeParticipantInlocuitorOfInformatiiParticipantiMetadataName());
			MetadataDefinition comisieGlPrezentaMtdDef = DocumentTypeUtils.getMetadataDefinitionByName(prezentaDocType, prezentaConstants.getDenumireComisieGlMetadataName());

			List<Document> documentePrezenta = new ArrayList<Document>();
			for (String sourceMetadataValue : sourceMetadataValues) {
				DocumentIdentifier documentIdentifier = MetadataValueHelper.getDocumentIdentifier(sourceMetadataValue);
				documentePrezenta.add(documentPlugin.getDocumentById(documentIdentifier.getDocumentId(), documentIdentifier.getDocumentLocationRealName(), userSecurity));
			}

			List<CollectionInstanceModel> participantiCollectionInstances = new ArrayList<>();

			for (Document docPrezenta : documentePrezenta) {
				Map<Long, Integer> participantiNumberByInstitutieId = new HashMap<Long, Integer>();
				int nrParticipantiForTipInstitutiiMembriiArb = 0;
				Map<Long, List<CollectionInstance>> collectionInstanceListMap = docPrezenta.getCollectionInstanceListMap();
				Long comisieGlId = null;
				for (MetadataInstance metadataInstance : docPrezenta.getMetadataInstanceList()) {
					if (StringUtils.isNotEmpty(metadataInstance.getValue()) && metadataInstance.getMetadataDefinitionId().equals(comisieGlPrezentaMtdDef.getId())) {
						comisieGlId = Long.parseLong(metadataInstance.getValue());
					}
				}
				List<CollectionInstance> list = collectionInstanceListMap.get(infoParticipantiPrezentaMtdCollDef.getId());
				for (CollectionInstance collInstance : list) {
					List<MetadataInstance> prezentaCollFields = collInstance.getMetadataInstanceList();
					Long institutieId = null;
					Integer nrParticipanti = 0;
					for (MetadataInstance prezentaCollField : prezentaCollFields) {
						if (StringUtils.isNotEmpty(prezentaCollField.getValue())) {
							if (prezentaCollField.getMetadataDefinitionId().equals(institutieOfInfoParticipantiPrezentaMtdDef.getId())) {
								institutieId = Long.parseLong(prezentaCollField.getValue());
							}
							if (prezentaCollField.getMetadataDefinitionId().equals(membruAcredOfInfoParticipantiPrezentaMtdDef.getId())) {
								nrParticipanti++;
							}
							if (prezentaCollField.getMetadataDefinitionId().equals(numeMembruOfInfoParticipantiPrezentaMtdDef.getId())) {
								nrParticipanti++;
							}
						}
					}

					if (participantiNumberByInstitutieId.containsKey(institutieId)) {
						Integer nrParticipantiFromMap = participantiNumberByInstitutieId.get(institutieId);
						nrParticipanti += nrParticipantiFromMap;
					}
					if (getTipInstitutieByIdInstitutie(institutieId).equals(NomenclatorConstants.TIP_INSTITUTII_VALUE_FOR_MEMBRU_ARB)) {
						nrParticipantiForTipInstitutiiMembriiArb += nrParticipanti;
					} else {
						participantiNumberByInstitutieId.put(institutieId, nrParticipanti);
					}
				}

				addCollectionRowInstanceModelForParticipantiMinuta(userSecurity, participantiCollectionInstances, participantiNumberByInstitutieId, comisieGlId,
						nrParticipantiForTipInstitutiiMembriiArb);
			}

			MetadataCollectionInstanceModel participantiMetadataCollectionInstance = new MetadataCollectionInstanceModel();
			participantiMetadataCollectionInstance.setMetadataDefinitionId(participantiMinutaMtdCollDef.getId());
			participantiMetadataCollectionInstance.setCollectionInstanceRows(participantiCollectionInstances);

			response.setMetadataCollectionInstance(participantiMetadataCollectionInstance);

		} catch (Exception e) {
			logger.error("targetMetadataCollectionDefinitionId: " + request.getTargetMetadataCollectionDefinitionId() + ", " + e.getMessage(), "doAutocomplete", userSecurity);
			throw new AppException();
		}
		return response;
	}

	private void addCollectionRowInstanceModelForParticipantiMinuta(SecurityManager userSecurity, List<CollectionInstanceModel> participantiCollectionInstances,
			Map<Long, Integer> participantiNumberByInstitutieId, Long comisieGlId, int nrParticipantiForTipInstitutiiMembriiArb) throws AppException {

		Set<Long> institutiiIds = participantiNumberByInstitutieId.keySet();
		for (Long institutieId : institutiiIds) {

			Integer nrParticipanti = participantiNumberByInstitutieId.get(institutieId);

			String participant = null;
			String tipInstitutie = getTipInstitutieByIdInstitutie(institutieId);
			if (tipInstitutie.equals(NomenclatorConstants.TIP_INSTITUTII_VALUE_FOR_MEMBRU_AFILIAT)) {
				participant = PARTICIPANT_VALUE_FOR_MEMBRU_AFILIAT;
			} else if (tipInstitutie.equals(NomenclatorConstants.TIP_INSTITUTII_VALUE_FOR_ALTE_INSTITUTII)) {
				participant = PARTICIPANT_VALUE_FOR_INVITAT;
			} else {
				logger.error("Invalid value :" + tipInstitutie + " for metadata participant of participanti from document: Prezenta comisii GL ", "doAutocomplete", userSecurity);
				throw new AppException();
			}

			CollectionInstanceModel collRowInstanceModel = buildCollRowInstanceModel(comisieGlId, institutieId, nrParticipanti, participant, tipInstitutie);
			participantiCollectionInstances.add(collRowInstanceModel);
		}

		if (nrParticipantiForTipInstitutiiMembriiArb != 0) {
			CollectionInstanceModel collRowInstanceModel = buildCollRowInstanceModel(comisieGlId, null, nrParticipantiForTipInstitutiiMembriiArb,
					PARTICIPANT_VALUE_FOR_MEMBRU_COMISIE, NomenclatorConstants.TIP_INSTITUTII_VALUE_FOR_MEMBRU_ARB);
			participantiCollectionInstances.add(collRowInstanceModel);
		}
	}

	private CollectionInstanceModel buildCollRowInstanceModel(Long comisieGlId, Long institutieId, Integer nrParticipanti, String participant, String tipInstitutie) {
		List<MetadataInstanceModel> metadataInstanceModels = new ArrayList<>();
		CollectionInstanceModel collRowInstanceModel = new CollectionInstanceModel();

		MetadataInstanceModel numarMtdInstanceModel = new MetadataInstanceModel();
		numarMtdInstanceModel.setMetadataDefinitionId(numarOfParticipantiMinutaMtdDef.getId());
		numarMtdInstanceModel.setValue(nrParticipanti.toString());
		metadataInstanceModels.add(numarMtdInstanceModel);

		MetadataInstanceModel participantiMtdInstanceModel = new MetadataInstanceModel();
		participantiMtdInstanceModel.setMetadataDefinitionId(participantOfParticipantiMinutaMtdDef.getId());
		participantiMtdInstanceModel.setValue(participant);
		metadataInstanceModels.add(participantiMtdInstanceModel);

		if (!tipInstitutie.equals(NomenclatorConstants.TIP_INSTITUTII_VALUE_FOR_MEMBRU_ARB)) {
			MetadataInstanceModel institutieMtdInstanceModel = new MetadataInstanceModel();
			institutieMtdInstanceModel.setMetadataDefinitionId(institutieOfParticipantiMinutaMtdDef.getId());
			institutieMtdInstanceModel.setValue(institutieId.toString());
			metadataInstanceModels.add(institutieMtdInstanceModel);
		}

		if (comisieGlId != null) {
			MetadataInstanceModel comisieGlMtdInstanceModel = new MetadataInstanceModel();
			comisieGlMtdInstanceModel.setMetadataDefinitionId(comisieGlOfParticipantiMinutaMtdDef.getId());
			comisieGlMtdInstanceModel.setValue(comisieGlId.toString());
			metadataInstanceModels.add(comisieGlMtdInstanceModel);
		}

		collRowInstanceModel.setMetadataInstanceList(metadataInstanceModels);
		return collRowInstanceModel;
	}

	private String getTipInstitutieByIdInstitutie(Long institutieId) {
		NomenclatorValue institutieNomValue = nomenclatorValueDao.find(institutieId);
		Long tipInstitieId = NomenclatorValueUtils.getAttributeValueAsLong(institutieNomValue, NomenclatorConstants.INSTITUTII_ATTRIBUTE_KEY_TIP_INSTITUTIE);
		NomenclatorValue tipInstitutieNomValue = nomenclatorValueDao.find(tipInstitieId);
		String tipInstitutie = NomenclatorValueUtils.getAttributeValueAsString(tipInstitutieNomValue, NomenclatorConstants.TIP_INSTITUTII_ATTRIBUTE_KEY_TIP_INSTITUTIE);
		return tipInstitutie;
	}

}
