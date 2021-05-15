import { ObjectUtils } from "./object-utils";

export class StringUtils {

	public static isBlank(stringValue: string): boolean {
		return (ObjectUtils.isNullOrUndefined(stringValue) || stringValue.trim() === "");
	}

	public static isNotBlank(stringValue: string): boolean {
		return !StringUtils.isBlank(stringValue);
	}
	
	public static joinWith(separator: string, ...strings: string[]): string {
		return strings.join(separator);
	}
	
	public static isNumeric(value: string): boolean {
		
		if (StringUtils.isBlank(value)) {
			return false;
		}
		if (value.startsWith("-0") && value.length === 2) {
			return false;
		}
		let regExp: RegExp = new RegExp(/^[-+]?\d+(\.\d+)?$/);
		return regExp.test(value);
	}

	public static isNotNumeric(value: string): boolean {
		return !this.isNumeric(value);
	}

	public static contains(theString: string, substring: string) {
		if (StringUtils.isBlank(theString) || StringUtils.isBlank(substring)) {
			return false;
		}
		return (theString.indexOf(substring) !== -1);
	}

	public static containsIgnoreCase(string: string, substring: string): boolean {
		if (ObjectUtils.isNullOrUndefined(string) || ObjectUtils.isNullOrUndefined(substring)) {
			return false;
		}
		let stringLower: string = string.toLowerCase();
		let substringLower: string = substring.toLowerCase();
		return stringLower.includes(substringLower);
	}

	public static startsWithIgnoreCase(string: string, substring: string): boolean {
		if (ObjectUtils.isNullOrUndefined(string) || ObjectUtils.isNullOrUndefined(substring)) {
			return false;
		}
		let stringLower: string = string.toLowerCase();
		let substringLower: string = substring.toLowerCase();
		return stringLower.startsWith(substringLower);
	}

	public static splitString(string: string, separator): string[] {
		return string.split(separator);
	}

	public static capitalizeFirstLetter(string) {
		return string.charAt(0).toUpperCase() + string.slice(1);
	}
	
	public static toNumber(numberAsString: string): number {
		if (StringUtils.isBlank(numberAsString)) {
			return null;
		}
		return Number(numberAsString);
	}

	public static removeSlashFromStartAndEnd(str: string): string {
		if (StringUtils.isBlank(str)) {
			return str;
		}
		let newStr: string = str.trim();
		if (newStr.startsWith("/")) {
			newStr = newStr.substring(1);
		}
		if (newStr.endsWith("/")) {
			newStr = newStr.substring(0, newStr.lastIndexOf("/"));
		}
		return newStr;
	}

	public static hashCode(str: string): any {
		let h;
		for(let i = 0; i < str.length; i++)  {
			  h = Math.imul(31, h) + str.charCodeAt(i) | 0;
		}
		return h;
	}
}