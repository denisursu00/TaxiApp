import { MetadataDefinitionModel, WorkflowStateModel, StringUtils, DateUtils } from "@app/shared";
import { BooleanUtils, ObjectUtils, WorkflowStateUtils } from "@app/shared";
import { MetadataFormControl } from "./metadata-form-controls";

export class MetadataUtils {

	public static isMetadataMandatory(metadataDefinition: MetadataDefinitionModel, documentWorkflowState: WorkflowStateModel): boolean {
		
		// Metadata nu este obligatorie deloc.
		if (BooleanUtils.isFalse(metadataDefinition.mandatory)) {
			return false;
		}

		// Documentul nu are stare, deci tipul nu are un flux asociat, deci
		// metadata este obligatorie.
		if (ObjectUtils.isNullOrUndefined(documentWorkflowState)) {
			return true;
		}

		// Pasii in care metadata este obligatorie nu sunt definiti, deci
		// daca starea curenta este prima din flux, metadata este obligatorie.
		// Altfel, nu este.
		if (StringUtils.isBlank(metadataDefinition.mandatoryStates)) {
			return ((documentWorkflowState.stateType === WorkflowStateModel.STATETYPE_START) ? true : false);
		}

		// Daca starea curenta e printre cele trecute, atunci metadata este obligatorie.
		return WorkflowStateUtils.isStateFound(metadataDefinition.mandatoryStates, documentWorkflowState.code);
	}

	public static isRestrictedOnEdit(metadataDefinition: MetadataDefinitionModel, documentWorkflowState: WorkflowStateModel): boolean {
		
		// Nu este restrictionata deloc.
		if (BooleanUtils.isFalse(metadataDefinition.restrictedOnEdit)) {
			return false;
		}

		// Documentul nu are stare, deci tipul nu are un flux asociat, deci
		// metadata nu are cum sa fie restrictionata.
		if (ObjectUtils.isNullOrUndefined(documentWorkflowState)) {
			return false;
		}

		// Pasii in care este restrictionata nu sunt definiti, deci nu este restrictionata.
		// In mod normal nu ar trebui sa existe aceasta conditie.
		if (StringUtils.isBlank(metadataDefinition.restrictedOnEditStates)) {
			return false;
		}

		// Daca starea curenta e printre cele trecute, atunci este restrictionata.
		return WorkflowStateUtils.isStateFound(metadataDefinition.restrictedOnEditStates, documentWorkflowState.code);
	}

	public static isMetadataVisible(metadataDefinition: MetadataDefinitionModel, documentWorkflowState: WorkflowStateModel): boolean {
		
		// Este vizibila daca invisible.
		if (BooleanUtils.isFalse(metadataDefinition.invisible)) {
			return true;
		}

		// Este bifat ca invisible, dar nu are stare flux.
		if (ObjectUtils.isNullOrUndefined(documentWorkflowState)) {
			return true;
		}

		// Este bifat ca invisible si nu exista stari setate.
		if (StringUtils.isBlank(metadataDefinition.invisibleInStates)) {
			return false;
		}

		return !WorkflowStateUtils.isStateFound(metadataDefinition.invisibleInStates, documentWorkflowState.code);
	}

	public static isMetadataInvisible(metadataDefinition: MetadataDefinitionModel, documentWorkflowState: WorkflowStateModel): boolean {
		return !this.isMetadataVisible(metadataDefinition, documentWorkflowState);
	}

	// S-ar putea muta la nivel de MetadataFormControl - dar trebuie sa fie gata - 
	public static getValueAsStringArray(metadataFormControl: MetadataFormControl<any>, metadataType: string): string[] {
		if (ObjectUtils.isNullOrUndefined(metadataFormControl)) {
			throw new Error("metadataFormControl cannot be null/undefined");
		}
		let controlValue: any = metadataFormControl.value;
		if (ObjectUtils.isNullOrUndefined(controlValue)) {
			return [];
		}
		if (controlValue instanceof Array) {
			return controlValue;
		}
		let metadataValues: string[] = [];
		if (controlValue instanceof Date) {
			if (metadataType === MetadataDefinitionModel.TYPE_DATE) {
				metadataValues.push(DateUtils.formatForStorage(controlValue)); 
			} else if (metadataType === MetadataDefinitionModel.TYPE_DATE_TIME) {
				metadataValues.push(DateUtils.formatDateTimeForStorage(controlValue)); 
			} else if (metadataType === MetadataDefinitionModel.TYPE_MONTH) {
				metadataValues.push(DateUtils.formatMonthYearForStorage(controlValue)); 
			} else {
				throw new Error("type necunoscut");
			}			
		} else if (typeof controlValue === "number" || typeof controlValue === "boolean") {
			metadataValues.push(controlValue.toString());
		} else if (typeof controlValue === "string") {
			metadataValues.push(controlValue);
		} else {
			throw new Error("Metadata value type nu este cunoscut [" + (typeof controlValue) + "]");
		}
		return metadataValues;
	}
}