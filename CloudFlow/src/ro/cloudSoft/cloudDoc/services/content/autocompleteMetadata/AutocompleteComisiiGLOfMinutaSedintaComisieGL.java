package ro.cloudSoft.cloudDoc.services.content.autocompleteMetadata;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.springframework.util.CollectionUtils;

import ro.cloudSoft.cloudDoc.core.AppException;
import ro.cloudSoft.cloudDoc.core.constants.arb.DocumentMinutaSedintaComisieGLConstants;
import ro.cloudSoft.cloudDoc.core.constants.arb.DocumentPrezentaComisieGlConstants;
import ro.cloudSoft.cloudDoc.dao.content.DocumentTypeDao;
import ro.cloudSoft.cloudDoc.domain.content.Document;
import ro.cloudSoft.cloudDoc.domain.content.DocumentIdentifier;
import ro.cloudSoft.cloudDoc.domain.content.DocumentType;
import ro.cloudSoft.cloudDoc.domain.content.MetadataCollection;
import ro.cloudSoft.cloudDoc.domain.content.MetadataDefinition;
import ro.cloudSoft.cloudDoc.domain.content.MetadataInstance;
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
import ro.cloudSoft.common.utils.spring.SpringUtils;

public class AutocompleteComisiiGLOfMinutaSedintaComisieGL extends AutocompleteMetadata {

	private static final LogHelper logger = LogHelper.getInstance(AutocompleteComisiiGLOfMinutaSedintaComisieGL.class);

	private DocumentPlugin documentPlugin;
	private DocumentTypeDao documentTypeDao;
	private DocumentPrezentaComisieGlConstants prezentaConstants;
	private DocumentMinutaSedintaComisieGLConstants minutaConstants;

	private void resolveDependencies() {
		this.documentPlugin = SpringUtils.getBean(DocumentPlugin.class);
		this.documentTypeDao = SpringUtils.getBean(DocumentTypeDao.class);
		this.prezentaConstants = SpringUtils.getBean(DocumentPrezentaComisieGlConstants.class);
		this.minutaConstants = SpringUtils.getBean(DocumentMinutaSedintaComisieGLConstants.class);
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
			MetadataCollection comisiiGlMtdCollDef = DocumentTypeUtils.getMetadataCollectionDefinitionByName(minutaDocumentType, minutaConstants.getComisiiGlMetadataName());
			MetadataDefinition numeComisieGlMtdDef = DocumentTypeUtils.getMetadataDefinitionFromCollectionByName(minutaDocumentType, minutaConstants.getComisiiGlMetadataName(),
					minutaConstants.getNumeComisieGlOfComisiiGlMetadataName());

			DocumentType prezentaDocType = documentTypeDao.getDocumentTypeByName(prezentaConstants.getDocumentTypeName());
			MetadataDefinition comisieOfPrezentaMtdDef = DocumentTypeUtils.getMetadataDefinitionByName(prezentaDocType, prezentaConstants.getDenumireComisieGlMetadataName());

			List<Document> documentePrezenta = new ArrayList<Document>();
			for (String sourceMetadataValue : sourceMetadataValues) {
				DocumentIdentifier documentIdentifier = MetadataValueHelper.getDocumentIdentifier(sourceMetadataValue);
				documentePrezenta.add(documentPlugin.getDocumentById(documentIdentifier.getDocumentId(), documentIdentifier.getDocumentLocationRealName(), userSecurity));
			}

			Set<String> comisiiGlIds = new HashSet<String>();
			for (Document documentPrezenta : documentePrezenta) {
				List<MetadataInstance> prezentaMetadataInstanceList = documentPrezenta.getMetadataInstanceList();
				for (MetadataInstance prezentaField : prezentaMetadataInstanceList) {
					if (prezentaField.getMetadataDefinitionId().equals(comisieOfPrezentaMtdDef.getId())) {
						comisiiGlIds.add(prezentaField.getValue());
					}
					
				}
			}

			List<CollectionInstanceModel> comisiiGLCollectionInstances = new ArrayList<>();
			comisiiGlIds.forEach(comisieGlId -> {
				CollectionInstanceModel collRowInstanceModel = new CollectionInstanceModel();
				List<MetadataInstanceModel> metadataInstanceModels = new ArrayList<>();
				MetadataInstanceModel comisieGlMtdInstanceModel = new MetadataInstanceModel();
				comisieGlMtdInstanceModel.setMetadataDefinitionId(numeComisieGlMtdDef.getId());
				comisieGlMtdInstanceModel.setValue(comisieGlId);
				metadataInstanceModels.add(comisieGlMtdInstanceModel);
				collRowInstanceModel.setMetadataInstanceList(metadataInstanceModels);
				comisiiGLCollectionInstances.add(collRowInstanceModel);
			});

			MetadataCollectionInstanceModel comisiiGLMetadataCollectionInstance = new MetadataCollectionInstanceModel();
			comisiiGLMetadataCollectionInstance.setMetadataDefinitionId(comisiiGlMtdCollDef.getId());
			comisiiGLMetadataCollectionInstance.setCollectionInstanceRows(comisiiGLCollectionInstances);

			response.setMetadataCollectionInstance(comisiiGLMetadataCollectionInstance);

		} catch (Exception e) {
			logger.error("targetMetadataCollectionDefinitionId: " + request.getTargetMetadataCollectionDefinitionId() + ", " + e.getMessage(), "doAutocomplete", userSecurity);
			throw new AppException();
		}
		return response;
	}

}
