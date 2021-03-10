package ro.cloudSoft.cloudDoc.presentation.client.shared.utils.business.content;

import ro.cloudSoft.cloudDoc.presentation.client.shared.model.bpm.WorkflowStateModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.content.DocumentTypeModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.utils.GwtCompareUtils;
import ro.cloudSoft.cloudDoc.presentation.client.shared.utils.GwtStringUtils;
import ro.cloudSoft.cloudDoc.presentation.client.shared.utils.business.bpm.GwtWorkflowStateBusinessUtils;

public class GwtMandatoryAttachmentBusinessHelper {
	
	private final DocumentTypeModel documentType;
	
	private final CurrentDocumentStateProvider currentDocumentStateProvider;
	private final MetadataValueByNameProvider metadataValueByNameProvider;

	public GwtMandatoryAttachmentBusinessHelper(DocumentTypeModel documentType,
			CurrentDocumentStateProvider currentDocumentStateProvider,
			MetadataValueByNameProvider metadataValueByNameProvider) {
		
		this.documentType = documentType;
		
		this.currentDocumentStateProvider = currentDocumentStateProvider;
		this.metadataValueByNameProvider = metadataValueByNameProvider;
	}
	
	public boolean isMandatoryAttachment() {
		
		if (!documentType.isMandatoryAttachment()) {
			return false;
		}
		
		boolean mandatoryAttachmentInStates = documentType.isMandatoryAttachmentInStates();
		boolean mandatoryAttachmentWhenMetadataHasValue = documentType.isMandatoryAttachmentWhenMetadataHasValue();
		
		if (mandatoryAttachmentInStates && mandatoryAttachmentWhenMetadataHasValue) {
			return (isMandatoryAttachmentBecauseOfCurrentState() && isMandatoryAttachmentBecauseOfMetadataValue());
		}
		
		if (mandatoryAttachmentInStates) {
			return isMandatoryAttachmentBecauseOfCurrentState();
		}
		
		if (mandatoryAttachmentWhenMetadataHasValue) {
			return isMandatoryAttachmentBecauseOfMetadataValue();
		}
		
		return true;
	}
	
	private boolean isMandatoryAttachmentBecauseOfCurrentState() {
		
		if (!documentType.isMandatoryAttachmentInStates()) {
			return false;
		}
		
		if (GwtStringUtils.isBlank(documentType.getMandatoryAttachmentStates())) {
			throw new IllegalStateException("Desi este bifata optiunea corespunzatoare, starile in care atasamentul este obligatoriu NU sunt completate.");
		}
		
		WorkflowStateModel currentState = currentDocumentStateProvider.getCurrentState();
		
		return GwtWorkflowStateBusinessUtils.isStateFound(documentType.getMandatoryAttachmentStates(), currentState.getCode());
	}
	
	private boolean isMandatoryAttachmentBecauseOfMetadataValue() {

		if (!documentType.isMandatoryAttachmentWhenMetadataHasValue()) {
			return false;
		}

		if (GwtStringUtils.isBlank(documentType.getMetadataNameInMandatoryAttachmentCondition())
				|| GwtStringUtils.isBlank(documentType.getMetadataValueInMandatoryAttachmentCondition())) {
			throw new IllegalStateException("Numele sau valoarea metadatei legate de obligativitatea atasamentului NU sunt completate.");
		}
		
		String metadataValue = metadataValueByNameProvider.getMetadataValueByName(documentType.getMetadataNameInMandatoryAttachmentCondition());
		boolean isMetadataValueTheOneExpectedForMandatoryAttachmentCondition = GwtCompareUtils.areEqual(metadataValue, documentType.getMetadataValueInMandatoryAttachmentCondition());
		return isMetadataValueTheOneExpectedForMandatoryAttachmentCondition;
		
	}
	
	public static interface CurrentDocumentStateProvider {
		
		/**
		 * Returneaza starea curenta a documentului.
		 * Daca documentul nu are stare (tipul de document nu are flux atasat), atunci va returna null.
		 */
		WorkflowStateModel getCurrentState();
	}
	
	public static interface MetadataValueByNameProvider {

		/**
		 * Returneaza valoarea unei metadate, a carei definitie are numele dat.
		 * Daca nu se gaseste campul metadatei, va arunca exceptie.
		 */
		String getMetadataValueByName(String metadataDefinitionName);
	}
}