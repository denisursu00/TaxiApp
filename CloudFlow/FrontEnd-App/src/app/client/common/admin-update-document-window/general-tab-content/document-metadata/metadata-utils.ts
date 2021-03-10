import { MetadataDefinitionModel, WorkflowStateModel, StringUtils, DateUtils } from "@app/shared";
import { BooleanUtils, ObjectUtils, WorkflowStateUtils } from "@app/shared";
import { MetadataFormControl } from "./metadata-form-control";

export class MetadataUtils {

	// S-ar putea muta la nivel de MetadataFormControl - dar trebuie sa fie gata - 
	public static getValueAsStringArray(metadataFormControl: MetadataFormControl, metadataType: string): string[] {
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