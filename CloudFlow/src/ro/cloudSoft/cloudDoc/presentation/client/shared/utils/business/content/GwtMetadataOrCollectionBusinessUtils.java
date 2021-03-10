package ro.cloudSoft.cloudDoc.presentation.client.shared.utils.business.content;

import ro.cloudSoft.cloudDoc.presentation.client.shared.model.bpm.WorkflowStateModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.content.MetadataCollectionDefinitionModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.content.MetadataDefinitionModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.utils.GwtValidateUtils;
import ro.cloudSoft.cloudDoc.presentation.client.shared.utils.business.bpm.GwtWorkflowStateBusinessUtils;

public class GwtMetadataOrCollectionBusinessUtils {
	
	/**
	 * Verifica daca o metadata este obligatorie in starea curenta a documentului.
	 * 
	 * @param metadataDefinition definitia metadatei
	 * @param documentState starea curenta a documentului
	 */
	public static boolean isMetadataMandatory(MetadataDefinitionModel metadataDefinition, WorkflowStateModel documentState) {
		return isMandatory(metadataDefinition, documentState);
	}

	/**
	 * Verifica daca o colectie este obligatorie in starea curenta a documentului.
	 * 
	 * @param metadataDefinition definitia colectiei
	 * @param documentState starea curenta a documentului
	 */
	public static boolean isCollectionMandatory(MetadataCollectionDefinitionModel collectionDefinition, WorkflowStateModel documentState) {
		return isMandatory(collectionDefinition, documentState);
	}

	/**
	 * Verifica daca o metadata este restrictionata in starea curenta a documentului.
	 * 
	 * @param metadataDefinition definitia metadatei
	 * @param documentState starea curenta a documentului
	 */
	public static boolean isMetadataRestrictedOnEdit(MetadataDefinitionModel metadataDefinition, WorkflowStateModel documentState) {
		return isRestrictedOnEdit(metadataDefinition.isRestrictedOnEdit(), metadataDefinition.getRestrictedOnEditStates(), documentState);
	}

	/**
	 * Verifica daca o colectie este restrictionata in starea curenta a documentului.
	 * 
	 * @param collectionDefinition definitia colectiei
	 * @param documentState starea curenta a documentului
	 */
	public static boolean isCollectionRestrictedOnEdit(MetadataCollectionDefinitionModel collectionDefinition, WorkflowStateModel documentState) {
		return isRestrictedOnEdit(collectionDefinition.isRestrictedOnEdit(), collectionDefinition.getRestrictedOnEditStates(), documentState);
	}

	public static boolean isMandatory(MetadataDefinitionModel metadataDefinition, WorkflowStateModel documentState) {
		
		// Metadata nu este obligatorie deloc.
		if (!metadataDefinition.isMandatory().booleanValue()) {
			return false;
		}
		
		/*
		 * Documentul nu are stare, deci tipul nu are un flux asociat, deci
		 * metadata este obligatorie.
		 */
		if (documentState == null) {
			return true;
		}
		
		String mandatoryStates = metadataDefinition.getMandatoryStates();
		
		/*
		 * Pasii in care metadata este obligatorie nu sunt definiti, deci
		 * daca starea curenta este prima din flux, metadata este obligatorie.
		 * Altfel, nu este.
		 */
		if (!GwtValidateUtils.isCompleted(mandatoryStates)) {
			return (documentState.getStateType().equals(WorkflowStateModel.STATETYPE_START)) ? true : false;
		}
		
		/*
		 * Daca starea curenta e printre cele trecute, atunci metadata este
		 * obligatorie.
		 */
		return GwtWorkflowStateBusinessUtils.isStateFound(mandatoryStates, documentState.getCode());
	}
	
	private static boolean isRestrictedOnEdit(boolean restrictedOnEditSetting, String restrictedOnEditStates, WorkflowStateModel documentState) {
		
		// Nueste restrictionata deloc.
		if (!restrictedOnEditSetting) {
			return false;
		}
		
		/*
		 * Documentul nu are stare, deci tipul nu are un flux asociat, deci
		 * metadata nu are cum sa fie restrictionata.
		 */
		if (documentState == null) {
			return false;
		}
		
		
		/*
		 * Pasii in care este restrictionata nu sunt definiti, deci nu este restrictionata.
		 * In mod normal nu ar trebui sa existe aceasta conditie.
		 */
		if (!GwtValidateUtils.isCompleted(restrictedOnEditStates)) {
			return false;
		}
		
		// Daca starea curenta e printre cele trecute, atunci este restrictionata.
		return GwtWorkflowStateBusinessUtils.isStateFound(restrictedOnEditStates, documentState.getCode());
	}
}