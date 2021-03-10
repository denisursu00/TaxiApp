package ro.cloudSoft.cloudDoc.domain.validators;

import org.apache.commons.lang.StringUtils;

import ro.cloudSoft.cloudDoc.core.AppException;
import ro.cloudSoft.cloudDoc.core.AppExceptionCodes;
import ro.cloudSoft.cloudDoc.domain.content.DocumentType;
import ro.cloudSoft.cloudDoc.domain.content.Folder;
import ro.cloudSoft.cloudDoc.domain.content.MetadataDefinition;
import ro.cloudSoft.cloudDoc.domain.security.SecurityManager;
import ro.cloudSoft.cloudDoc.services.content.FolderService;

public class DocumentTypeValidator {

	private final DocumentType documentType;
	private final FolderService folderService;
	private final SecurityManager userSecurity;
	
	public DocumentTypeValidator(DocumentType documentType, FolderService folderService, SecurityManager userSecurity) {
		this.documentType = documentType;
		this.folderService = folderService;
		this.userSecurity = userSecurity;
	}
	
	public void validate() throws AppException {
		// TODO alte validari
		validateAttachmentRelatedProperties();
		validateDefaultLocationRelatedFields();
	}
	
	private void validateAttachmentRelatedProperties() throws AppException {
		validateMandatoryAttachmentRelatedProperties();
		validateAttachmentTypes();
	}
	
	private void validateMandatoryAttachmentRelatedProperties() throws AppException {
		
		boolean mandatoryAttachment = documentType.getMandatoryAttachment();
		
		validateMandatoryAttachmentInStatesRelatedProperties(mandatoryAttachment);
		validateMandatoryAttachmentWhenMetadataHasValueRelatedProperties(mandatoryAttachment);
	}
	
	private void validateMandatoryAttachmentInStatesRelatedProperties(boolean mandatoryAttachment) {
		boolean mandatoryAttachmentInStates = documentType.isMandatoryAttachmentInStates();
		if (mandatoryAttachmentInStates) {

			if (!mandatoryAttachment) {
				String exceptionMessage = "Nu poate fi bifata optiunea 'atasament obligatoriu " +
					"in pasi' daca nu s-a bifat optiunea 'atasament obligatoriu')";
				throw new IllegalArgumentException(exceptionMessage);
			}

			if (StringUtils.isEmpty(documentType.getMandatoryAttachmentStates())) {
				String exceptionMessage = "Nu s-au completat pasii in care atasamentul e " +
					"obligatoriu, desi s-a bifat optiunea 'atasament obligatoriu in pasi'.";
				throw new IllegalArgumentException(exceptionMessage);
			}
		} else {
			if (StringUtils.isNotEmpty(documentType.getMandatoryAttachmentStates())) {
				String exceptionMessage = "Nu trebuie completati pasii in care atasamentul e " +
					"obligatoriu daca nu s-a bifat optiunea 'atasament obligatoriu in pasi'.";
				throw new IllegalArgumentException(exceptionMessage);
			}
		}
	}
	
	private void validateMandatoryAttachmentWhenMetadataHasValueRelatedProperties(boolean mandatoryAttachment) throws AppException {

		boolean mandatoryAttachmentWhenMetadataHasValue = documentType.isMandatoryAttachmentWhenMetadataHasValue();
		if (mandatoryAttachmentWhenMetadataHasValue && !mandatoryAttachment) {
			String exceptionMessage = "Nu poate fi bifata optiunea 'atasament obligatoriu " +
				"cand o metadata are o valoare' daca nu s-a bifat 'atasament obligatoriu'.";
			throw new IllegalStateException(exceptionMessage);
		}
		
		String metadataNameInMandatoryAttachmentCondition = documentType.getMetadataNameInMandatoryAttachmentCondition();
		if (StringUtils.isEmpty(metadataNameInMandatoryAttachmentCondition)) {
			if (mandatoryAttachmentWhenMetadataHasValue) {
				String exceptionMessage = "Nu s-a completat numele metadatei, desi s-a bifat " +
					"optiunea 'atasament obligatoriu cand o metadata are o valoare'.";
				throw new IllegalStateException(exceptionMessage);
			} else {
				// OK
			}
		} else {
			
			// E completat numele.
			
			if (mandatoryAttachmentWhenMetadataHasValue) {
				
				// OK pana aici
				
				if (!metadataDefinitionWithNameExists(metadataNameInMandatoryAttachmentCondition)) {
					throw new AppException(AppExceptionCodes.NO_METADATA_FOUND_WITH_SPECIFIED_NAME);
				}
			} else {
				String exceptionMessage = "Numele metadatei NU trebuie sa fie completat daca " +
					"NU s-a bifat optiunea 'atasament obligatoriu cand o metadata are o valoare'.";
				throw new IllegalStateException(exceptionMessage);
			}
		}
		
		String metadataValueInMandatoryAttachmentCondition = documentType.getMetadataValueInMandatoryAttachmentCondition();
		if (StringUtils.isEmpty(metadataValueInMandatoryAttachmentCondition)) {
			if (mandatoryAttachmentWhenMetadataHasValue) {
				String exceptionMessage = "Nu s-a completat valoarea metadatei, desi s-a bifat " +
					"optiunea 'atasament obligatoriu cand o metadata are o valoare'.";
				throw new IllegalStateException(exceptionMessage);
			} else {
				// OK
			}
		} else {
			
			// E completata valoarea.
			
			if (mandatoryAttachmentWhenMetadataHasValue) {
				// OK
			} else {
				String exceptionMessage = "Valoarea metadatei NU trebuie sa fie completata daca " +
					"NU s-a bifat optiunea 'atasament obligatoriu cand o metadata are o valoare'.";
				throw new IllegalStateException(exceptionMessage);
			}
		}
	}
	
	private void validateAttachmentTypes() throws AppException {
		
		boolean mandatoryAttachment = documentType.getMandatoryAttachment();
		if (!mandatoryAttachment) {
			return;
		}
		
		boolean hasDefinedAttachmentTypes = (!documentType.getAllowedMimeTypes().isEmpty());
		if (!hasDefinedAttachmentTypes) {
			throw new AppException(AppExceptionCodes.NO_ALLOWED_ATTACHMENT_TYPES_DEFINED);
		}
	}
	
	private boolean metadataDefinitionWithNameExists(String nameToCheck) {
		for (MetadataDefinition metadataDefinition : documentType.getMetadataDefinitions()) {
			if (metadataDefinition.getName().equals(nameToCheck)) {
				return true;
			}
		}
		return false;
	}
	
	private void validateDefaultLocationRelatedFields() throws AppException {
		
		if (StringUtils.isNotEmpty(documentType.getParentDocumentLocationRealNameForDefaultLocation()) && StringUtils.isNotEmpty(documentType.getFolderIdForDefaultLocation())) {
			
			// OK pana aici
			
			Folder folderForDefaultLocation = folderService.getFolderById(documentType.getFolderIdForDefaultLocation(), documentType.getParentDocumentLocationRealNameForDefaultLocation(), userSecurity);
			if (folderForDefaultLocation.getDocumentTypeId() != null) {
				if (documentType.getId() != null) {
					if (folderForDefaultLocation.getDocumentTypeId().equals(documentType.getId())) {
						// OK
					} else {
						// Folder-ul are restrictie pe tip de document, ALTUL decat cel de validat.
						throw new AppException(AppExceptionCodes.FOLDER_HAS_DOCUMENT_TYPE_RESTRICTION_FOR_OTHER_DOCUMENT_TYPE);
					}
				} else {
					// Folder-ul are restrictie pe tip de document, ALTUL decat cel de validat.
					throw new AppException(AppExceptionCodes.FOLDER_HAS_DOCUMENT_TYPE_RESTRICTION_FOR_OTHER_DOCUMENT_TYPE);
				}
			}
			
		} else if (StringUtils.isEmpty(documentType.getParentDocumentLocationRealNameForDefaultLocation()) && StringUtils.isEmpty(documentType.getFolderIdForDefaultLocation())) {
			// OK
		} else {
			throw new IllegalArgumentException("Pentru locatia implicita trebuie completat atat numele 'real' al spatiului de lucru, cat si ID-ul folder-ului.");
		}
	}
}