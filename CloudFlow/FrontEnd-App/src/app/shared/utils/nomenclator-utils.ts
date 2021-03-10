import { ValueOfNomenclatorValueField } from "./../components";
import { ObjectUtils } from "./object-utils";
import { ArrayUtils } from "./array-utils";
import { NomenclatorValueModel } from "../model";
import { NomenclatorConstants } from "../constants";

export class NomenclatorUtils {

	public static fieldValueHasValue(fieldValue: ValueOfNomenclatorValueField): boolean {
		return (ObjectUtils.isNotNullOrUndefined(fieldValue) && ObjectUtils.isNotNullOrUndefined(fieldValue.value));
	}

	public static fieldValueHasNotValue(fieldValue: ValueOfNomenclatorValueField): boolean {
		return !NomenclatorUtils.fieldValueHasValue(fieldValue);
	}

	public static fieldValueHasValues(fieldValue: ValueOfNomenclatorValueField): boolean {
		return (ObjectUtils.isNotNullOrUndefined(fieldValue) && ArrayUtils.isNotEmpty(fieldValue.values));
	}

	public static fieldValueHasNotValues(fieldValue: ValueOfNomenclatorValueField): boolean {
		return !NomenclatorUtils.fieldValueHasValues(fieldValue);
	}

	public static getFieldValueAsBoolean(nomenclatorValue: NomenclatorValueModel, attributeKey: string): Boolean {
		let fieldValue: string = nomenclatorValue[attributeKey];
		if (ObjectUtils.isNullOrUndefined(fieldValue)) {
			return null;
		}
		if (fieldValue.toLowerCase() === NomenclatorConstants.BOOLEAN_ATTRIBUTE_VALUE_AS_STRING) {
			return true;
		}
		return false;
	}
}