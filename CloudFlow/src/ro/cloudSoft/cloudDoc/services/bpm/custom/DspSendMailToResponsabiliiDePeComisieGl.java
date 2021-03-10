package ro.cloudSoft.cloudDoc.services.bpm.custom;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;

import com.sun.star.uno.RuntimeException;

import ro.cloudSoft.cloudDoc.core.AppException;
import ro.cloudSoft.cloudDoc.core.constants.MetadataConstants;
import ro.cloudSoft.cloudDoc.core.constants.NomenclatorConstants;
import ro.cloudSoft.cloudDoc.dao.arb.ReprezentantiComisieSauGLDao;
import ro.cloudSoft.cloudDoc.dao.content.DocumentTypeDao;
import ro.cloudSoft.cloudDoc.dao.nomenclator.NomenclatorValueDao;
import ro.cloudSoft.cloudDoc.domain.arb.ReprezentantiComisieSauGL;
import ro.cloudSoft.cloudDoc.domain.bpm.WorkflowInstance;
import ro.cloudSoft.cloudDoc.domain.content.CollectionInstance;
import ro.cloudSoft.cloudDoc.domain.content.Document;
import ro.cloudSoft.cloudDoc.domain.content.MetadataCollection;
import ro.cloudSoft.cloudDoc.domain.content.MetadataDefinition;
import ro.cloudSoft.cloudDoc.domain.content.MetadataInstance;
import ro.cloudSoft.cloudDoc.domain.mail.EmailMessage;
import ro.cloudSoft.cloudDoc.domain.nomenclator.NomenclatorValue;
import ro.cloudSoft.cloudDoc.domain.organization.User;
import ro.cloudSoft.cloudDoc.services.MailService;
import ro.cloudSoft.cloudDoc.services.content.DocumentService;
import ro.cloudSoft.cloudDoc.services.organization.UserService;
import ro.cloudSoft.cloudDoc.utils.MetadataValueHelper;
import ro.cloudSoft.cloudDoc.utils.nomenclator.NomenclatorValueUtils;
import ro.cloudSoft.common.utils.spring.SpringUtils;

public class DspSendMailToResponsabiliiDePeComisieGl extends AutomaticTask {

	@Override
	public void execute(WorkflowInstance workflowInstance) throws AutomaticTaskExecutionException, AppException {	
		
		String numarProiect = null;
		String numeProiect = null;
		User responsabilProiect = null;
		
		List<Long> comisiiNomenclatorValueIds = null;
		
		Document document = getDocumentService().getDocumentById(workflowInstance.getDocumentId(), workflowInstance.getWorkspaceName(), getUserSecurity());
		List<MetadataInstance> documentMetadataInstanceList = document.getMetadataInstanceList();
		
		for (MetadataInstance metadataInstance : documentMetadataInstanceList) {
			MetadataDefinition metadataDefinition = getDocumentTypeDao().getMetadataDefinition(metadataInstance.getMetadataDefinitionId());
			
			if (metadataDefinition.getName().equals(MetadataConstants.DSP_METADATA_NAME_NUMAR_PROIECT)) {
				numarProiect = MetadataValueHelper.getAutoNumberValue(metadataInstance.getValue());
			}
			
			if (metadataDefinition.getName().equals(MetadataConstants.DSP_METADATA_NAME_NUME_PROIECT)) {
				numeProiect = MetadataValueHelper.getText(metadataInstance.getValue());
			}
			
			if (metadataDefinition.getName().equals(MetadataConstants.DSP_METADATA_NAME_RESPONSABIL)) {
				Long responsabilUserId = MetadataValueHelper.getUserId(metadataInstance.getValue());
				responsabilProiect = getUserService().getUserById(responsabilUserId);
			}
		}
		
		comisiiNomenclatorValueIds = new ArrayList<Long>();
		Map<Long, List<CollectionInstance>> documentCollectionMetadataInstanceList = document.getCollectionInstanceListMap();
		for (Entry<Long, List<CollectionInstance>> entry : documentCollectionMetadataInstanceList.entrySet()) {
			MetadataCollection metadataDefinition = getDocumentTypeDao().getMetadataCollectionDefinition(entry.getKey());
			if (metadataDefinition.getName().equals(MetadataConstants.DSP_METADATA_COLLECTION_NAME_COMISII_GL_IMPLICATE)) {
				List<CollectionInstance> collectionInstances = entry.getValue();
				for (CollectionInstance collectionInstance : collectionInstances) {
					
					for (MetadataInstance metadataInstance : collectionInstance.getMetadataInstanceList()) {
						
						MetadataDefinition collectionInstanceMetadataDefinition = getDocumentTypeDao().getMetadataDefinition(metadataInstance.getMetadataDefinitionId());
						
						if (collectionInstanceMetadataDefinition.getName().equals(MetadataConstants.DSP_COLLECTION_COMISII_GL_IMPLICATE_METADATA_NAME_DENUMIRE_COMISIE_GL)) {
							comisiiNomenclatorValueIds.add(MetadataValueHelper.getNomenclatorValueId(metadataInstance.getValue()));
						}
					}
				}
			}
		}
		
		if (numarProiect == null) {
			throw new RuntimeException("Metadata [numar_proiect] nu a fost gasita pe document.");
		}
		
		if (numeProiect == null) {
			throw new RuntimeException("Metadata [nume_proiect] nu a fost gasita pe document.");
		}
		
		if (responsabilProiect == null) {
			throw new RuntimeException("Metadata [responsabil] nu a fost gasita pe document.");
		}
		
		if (CollectionUtils.isEmpty(comisiiNomenclatorValueIds)) {
			throw new RuntimeException("Metadata [comisii_gl_implicate] nu a fost gasita pe document.");
		}
		
		for (Long comisieNomenclatorValueId :comisiiNomenclatorValueIds) {
			NomenclatorValue comisieNomenclatorValue = getNomenclatorValueDaoImpl().find(comisieNomenclatorValueId);
			String denumireComisie = getDenumireComisieByNomenclatorValueId(comisieNomenclatorValue);
			
			String emailResponsabilComisie = getEmailResponsabilArb(comisieNomenclatorValueId);
			if (StringUtils.isNotBlank(emailResponsabilComisie)) {
				sendEmail(emailResponsabilComisie, numarProiect, numeProiect, responsabilProiect.getDisplayName(), denumireComisie);
			}
		}
		
	}
	
	private String getDenumireComisieByNomenclatorValueId(NomenclatorValue comisieNomenclatorValue) {
		return NomenclatorValueUtils.getAttributeValueAsString(comisieNomenclatorValue, NomenclatorConstants.COMISII_SAU_GL_ATTRIBUTE_KEY_DENUMIRE);
	}
	
	private void sendEmail(String emailAddress, String numarProiect, String numeProiect, String numeResponsabilProiect, String denumireComisie) {
		String subject = "Comisie implicata în  proiect nr " + numarProiect;
		String content = "Comisia " + denumireComisie + " a fost implicata in  proiectul " + numeProiect + " de catre " + numeResponsabilProiect + ".";
		
		EmailMessage emailMessage = new EmailMessage(emailAddress, subject, content);
		getMailService().send(emailMessage);
	}
	
	private String getEmailResponsabilArb(Long comisieNomenclatorValueId) {
		ReprezentantiComisieSauGL comisieSauGL = getReprezentantiComisieSauGLDao().getByComisieSauGLId(comisieNomenclatorValueId);
		if (comisieSauGL == null) {
			return null;
		}
		User responsabilArb = comisieSauGL.getResponsabilARB();
		if (responsabilArb == null) {
			return null;
		}
		return responsabilArb.getEmail();
	}
	
	private DocumentService getDocumentService() {
		return SpringUtils.getBean("documentService");
	}
	
	private DocumentTypeDao getDocumentTypeDao() {
		return SpringUtils.getBean("documentTypeDao");
	}
	
	private UserService getUserService() {
		return SpringUtils.getBean("userService");
	}
	
	private NomenclatorValueDao getNomenclatorValueDaoImpl() {
		return SpringUtils.getBean("nomenclatorValueDao");
	}
	
	private ReprezentantiComisieSauGLDao getReprezentantiComisieSauGLDao() {
		return SpringUtils.getBean("reprezentantiComisieSauGLDao");
	}
	
	private MailService getMailService() {
		return SpringUtils.getBean("mailService");
	}
}
