export class UiUtils {

	public static getDialogWidth(): number {
		let availWidth = window.screen.availWidth;
		if (availWidth <= 640) {
			return window.screen.availWidth - window.screen.availWidth / 6;
		} else if (availWidth <= 1025) {
			return window.screen.availWidth - window.screen.availWidth / 4;
		} else {
			return window.screen.availWidth - window.screen.availWidth / 3;
		}
	}

	public static getAspectRation(): number {
		return window.screen.availWidth / window.screen.availHeight;
	}

	public static appendTableCellCollapseStyle(style: any) {
		style["overflow"]="hidden";
		style["white-space"]="nowrap";
		style["text-overflow"]="ellipsis";
	}

	public static appendTableCellExpandStyle(style: any) {
		style["white-space"]="normal";
	}

	public static isMobileDevices(): boolean {
		if (window.innerWidth <= 640) {
			return true;
		} else {
			return false;
		}
	}

	public static onDropdownClick() {
		let thisClass: any = this;
		setTimeout(function () {
			if (thisClass.filterViewChild != undefined) {
				thisClass.filterViewChild.nativeElement.focus();
			}
		}, 200);
	}
}