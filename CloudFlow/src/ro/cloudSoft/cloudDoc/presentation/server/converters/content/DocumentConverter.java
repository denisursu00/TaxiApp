package ro.cloudSoft.cloudDoc.presentation.server.converters.content;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;

import ro.cloudSoft.cloudDoc.dao.content.DocumentAttachmentDetailDao;
import ro.cloudSoft.cloudDoc.domain.content.ACL;
import ro.cloudSoft.cloudDoc.domain.content.CollectionInstance;
import ro.cloudSoft.cloudDoc.domain.content.Document;
import ro.cloudSoft.cloudDoc.domain.content.AdminUpdateDocument;
import ro.cloudSoft.cloudDoc.domain.content.DocumentAttachmentDetail;
import ro.cloudSoft.cloudDoc.domain.content.DocumentIdentifier;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.content.AdminUpdateDocumentModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.content.DocumentAttachmentModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.content.DocumentModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.content.MetadataCollectionInstanceModel;
import ro.cloudSoft.cloudDoc.presentation.server.converters.PermissionConverter;
import ro.cloudSoft.common.utils.ConvertUtils;

public class DocumentConverter {

	public static Document getDocumentFromModel(DocumentModel documentModel) {
		Document document = new Document();
		document.setId(documentModel.getId());
		document.setDocumentLocationRealName(documentModel.getDocumentLocationRealName());
		document.setDocumentTypeId(documentModel.getDocumentTypeId());
		
		document.setAuthor(documentModel.getAuthor());
		document.setCreated(ConvertUtils.getCalendarFromDate(documentModel.getCreated()));
		
		document.setName(documentModel.getDocumentName());
		document.setDescription(documentModel.getDocumentDescription());

		document.setMetadataInstanceList(MetadataInstanceConverter.getMetadataInstanceList(documentModel.getMetadataInstances()));

		Map<Long, List<CollectionInstance>> collectionInstanceMap = new HashMap<Long, List<CollectionInstance>>();
		if (documentModel.getMetadataCollectionInstances() != null) {
			for (MetadataCollectionInstanceModel instance : documentModel.getMetadataCollectionInstances()) {
				collectionInstanceMap.put(instance.getMetadataDefinitionId(), CollectionInstanceConverter.getCollectionList(instance.getCollectionInstanceRows()));
			}
		}
		document.setCollectionInstanceListMap(collectionInstanceMap);
		
		document.setHasStableVersion(documentModel.getHasStableVersion());
		
		ACL newAcl = new ACL();
		document.setAcl(newAcl);
		document.getAcl().setPermissions(PermissionConverter.getPermissionList(documentModel.getPermissions()));
		
		document.setWorkflowStateId(documentModel.getWorkflowStateId());
		document.setVersionWorkflowStateId(documentModel.getVersionWorkflowStateId());
		
		return document;
	}

	public static DocumentModel getModelFromDocument(Document document, DocumentAttachmentDetailDao documentAttachmentDetailDao) {
		DocumentModel documentModel = new DocumentModel();

		documentModel.setId(document.getId());
		documentModel.setDocumentLocationRealName(document.getDocumentLocationRealName());
		documentModel.setDocumentTypeId(document.getDocumentTypeId());
		documentModel.setDocumentName(document.getName());
		documentModel.setDocumentDescription(document.getDescription());
		
		documentModel.setAuthor(document.getAuthor());
		documentModel.setCreated(document.getCreated().getTime());
		documentModel.setLastModified(document.getLastUpdate().getTime());
		documentModel.setLockedBy(document.getLockedBy());

		documentModel.setMetadataInstances(MetadataInstanceConverter.getMetadataInstanceModelList(document.getMetadataInstanceList()));
		
		List<MetadataCollectionInstanceModel> metadataCollectionInstanceModels = new ArrayList<MetadataCollectionInstanceModel>();
		for (Map.Entry<Long, List<CollectionInstance>> entry : document.getCollectionInstanceListMap().entrySet()) {
			MetadataCollectionInstanceModel metadataCollectionInstance = new MetadataCollectionInstanceModel();
			metadataCollectionInstance.setMetadataDefinitionId(entry.getKey());
			metadataCollectionInstance.setCollectionInstanceRows(CollectionInstanceConverter.getCollectionInstanceModelList(entry.getValue()));
			metadataCollectionInstanceModels.add(metadataCollectionInstance);
		}
		documentModel.setMetadataCollectionInstances(metadataCollectionInstanceModels);
		
		documentModel.setHasStableVersion(document.getHasStableVersion());
		documentModel.setVersionNumber(document.getVersionNumber());

		List<DocumentAttachmentModel> attachmentModels = new ArrayList<DocumentAttachmentModel>();
		List<DocumentAttachmentDetail> attachmentDetails = documentAttachmentDetailDao.getAllOfDocument(new DocumentIdentifier(document.getDocumentLocationRealName(), document.getId()));
		for (String attachmentName : document.getAttachmentNames()) {
			DocumentAttachmentModel attachmentModel = new DocumentAttachmentModel();
			attachmentModel.setName(attachmentName);
			attachmentModel.setIsNew(false);
			attachmentModel.setDocumentWorkflowStateCode(getAtachmentDocumentWorkflowStateCode(attachmentModel.getName(), attachmentDetails));
			attachmentModels.add(attachmentModel);
		}
		documentModel.setAttachments(attachmentModels);

		documentModel.setPermissions(PermissionConverter.getPermissionModelList(document.getAcl().getPermissions()));
		
		documentModel.setWorkflowStateId(document.getWorkflowStateId());
		documentModel.setVersionWorkflowStateId(document.getVersionWorkflowStateId());
		
		return documentModel;
	}
	
	private static String getAtachmentDocumentWorkflowStateCode(String attachmentName, List<DocumentAttachmentDetail> documentAttachmentDetails) {
		if (CollectionUtils.isNotEmpty(documentAttachmentDetails)) {
			for (DocumentAttachmentDetail detail : documentAttachmentDetails) {
				if (detail.getAttachmentName().equals(attachmentName)) {
					return detail.getDocumentWorkflowStateCode();
				}
			}
		}
		return null;
	}

	public static List<DocumentModel> getModelsFromDocuments(List<Document> documents, DocumentAttachmentDetailDao documentAttachmentDetailDao) {
		List<DocumentModel> models = new ArrayList<DocumentModel>();
		if (documents != null) {
			for (Document document : documents) {
				DocumentModel model = getModelFromDocument(document, documentAttachmentDetailDao);
				models.add(model);
			}
		}
		return models;
	}

	public static List<Document> getDocumentsFromModels(List<DocumentModel> models) {
		List<Document> documents = new ArrayList<Document>();
		if (models != null) {
			for (DocumentModel model : models) {
				Document document = getDocumentFromModel(model);
				documents.add(document);
			}
		}
		return documents;
	}
	
	public static AdminUpdateDocument getAdminUpdateDocumentFromModel(AdminUpdateDocumentModel model) {
		AdminUpdateDocument document = new AdminUpdateDocument();
		
		document.setId(model.getId());
		document.setDocumentLocationRealName(model.getDocumentLocationRealName());
		document.setName(model.getDocumentName());
		document.setDescription(model.getDocumentDescription());
		document.setDocumentTypeId(model.getDocumentTypeId());
		
		document.setMetadataInstances(MetadataInstanceConverter.getMetadataInstanceList(model.getMetadataInstances()));
		
		Map<Long, List<CollectionInstance>> collectionInstanceMap = new HashMap<Long, List<CollectionInstance>>();
		if (document.getCollectionInstanceListMap() != null) {
			for (MetadataCollectionInstanceModel instance : model.getMetadataCollectionInstances()) {
				collectionInstanceMap.put(instance.getMetadataDefinitionId(), CollectionInstanceConverter.getCollectionList(instance.getCollectionInstanceRows()));
			}
		}
		document.setCollectionInstanceListMap(collectionInstanceMap);
		
		return document;
	}
	
	public static AdminUpdateDocumentModel getModelFromAdminUpdateDocument(AdminUpdateDocument document) {
		
		AdminUpdateDocumentModel model = new AdminUpdateDocumentModel();

		model.setId(document.getId());
		model.setDocumentLocationRealName(document.getDocumentLocationRealName());
		model.setDocumentTypeId(document.getDocumentTypeId());
		model.setDocumentName(document.getName());
		model.setDocumentDescription(document.getDescription());
		
		model.setMetadataInstances(MetadataInstanceConverter.getMetadataInstanceModelList(document.getMetadataInstances()));
		
		List<MetadataCollectionInstanceModel> metadataCollectionInstanceModels = new ArrayList<MetadataCollectionInstanceModel>();
		for (Map.Entry<Long, List<CollectionInstance>> entry : document.getCollectionInstanceListMap().entrySet()) {
			MetadataCollectionInstanceModel metadataCollectionInstance = new MetadataCollectionInstanceModel();
			metadataCollectionInstance.setMetadataDefinitionId(entry.getKey());
			metadataCollectionInstance.setCollectionInstanceRows(CollectionInstanceConverter.getCollectionInstanceModelList(entry.getValue()));
			metadataCollectionInstanceModels.add(metadataCollectionInstance);
		}
		model.setMetadataCollectionInstances(metadataCollectionInstanceModels);
		
		return model;
	}
}