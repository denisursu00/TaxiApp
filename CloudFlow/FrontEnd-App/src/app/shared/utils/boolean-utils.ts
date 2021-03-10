import { ObjectUtils } from "./object-utils";

export class BooleanUtils {

	public static isTrue(booleanValue: boolean): boolean {
		if (ObjectUtils.isNullOrUndefined(booleanValue)) {
			return false;
		}
		return booleanValue;
	}

	public static isFalse(booleanValue: boolean): boolean {
		return !BooleanUtils.isTrue(booleanValue);
	}

}