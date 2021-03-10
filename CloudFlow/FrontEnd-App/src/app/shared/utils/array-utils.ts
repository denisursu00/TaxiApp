export class ArrayUtils {

	public static isEmpty(array: any[]): boolean {
		if (array === null || (typeof array === "undefined") || array.length === 0) {
			return true;
		}
		return false;
	}

	public static isNotEmpty(array: any[]): boolean {
		return !ArrayUtils.isEmpty(array);
	}

	public static removeElement(array: any[], element: any): void {
		let indexOf = array.indexOf(element);
		if (indexOf >= 0) {
			array.splice(indexOf, 1);
		}
	}

	public static removeElementByIndex(array: any[], elementIndex: number): void {
		if (elementIndex >= 0) {
			array.splice(elementIndex, 1);
		}
	}

	public static removeElements(array: any[], elements: any[]): void {
		if (ArrayUtils.isEmpty(array) || ArrayUtils.isEmpty(elements)) {
			return;
		}
		elements.forEach((element: any) => {
			ArrayUtils.removeElement(array, element);
		});
	}

	public static replaceElement(array: any[], replaceableElement: any, replacementElement: any): void {
		let indexOf = array.indexOf(replaceableElement);
		if (indexOf >= 0) {
			array[indexOf] = replacementElement;
		}
	}

	public static elementExists(array: any[], element: any): boolean {
		if (ArrayUtils.isEmpty(array)) {
			return false;
		}
		let indexOf = array.indexOf(element);
		return (indexOf >= 0);
	}
}