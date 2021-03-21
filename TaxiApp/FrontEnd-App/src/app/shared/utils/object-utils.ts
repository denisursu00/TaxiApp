export class ObjectUtils {

	public static isNullOrUndefined(object: any): boolean {
		if (object === null || typeof object === "undefined") {
			return true;
		}
		return false;
	}

	public static isNotNullOrUndefined(object: any): boolean {
		return !ObjectUtils.isNullOrUndefined(object);
	}

	public static isArray(object: any): boolean {
		if (ObjectUtils.isNullOrUndefined(object)) {
			return false;
		}
		return (object instanceof Array);
	}

	public static isNotArray(object: any): boolean {
		return !ObjectUtils.isArray(object);
	}

	public static isUndefined(object: any): boolean {
		return typeof object === "undefined";
	}

	public static isAJavaScriptObject(objectReference: { new(): any }): boolean {
		return objectReference === Object || objectReference === Array || objectReference === Boolean || objectReference === Date || objectReference === Number || objectReference === String;
	}

	public static isNumber(object: any): boolean {
		return typeof(object) === "number";
	}

	public static isString(object: any): boolean {
		return typeof(object) === "string";
	}

	public static isNotString(object: any): boolean {
		return !ObjectUtils.isString(object);
	}

	public static isBoolean(object: any): boolean {
		return typeof(object) === "boolean";
	}

	public static isDate(object: any): boolean {
		if (object instanceof Date) {
			return true;
		}
		return false;
	}
}