import { Dialog, DomHandler } from "primeng/primeng";
import { Input, ViewChild } from "@angular/core";
import { ObjectUtils } from "./utils/object-utils";

export class PDialogMinimizer {

	private pDialog: Dialog;

	private _minimized: boolean = false;

	private prevDialogContainerHeight: any = null;

	public constructor(pDialog: Dialog) {
		if (ObjectUtils.isNullOrUndefined(pDialog)) {
			throw new Error("pDialog cannot be null or undefined");
		}
		this.pDialog = pDialog;
	}

	public get minimized(): boolean {
		return this._minimized;
	}

	public toggleMinimize(): void {
		if (this._minimized) {
			this.revertMinimize();
		} else {
			this.minimize();
		}
	}

	private minimize(): void {
		
		this.prevDialogContainerHeight = this.pDialog.container.style.height;
		let newContainerHeight: any = DomHandler.getOuterHeight(this.pDialog.headerViewChild.nativeElement);
		this.pDialog.container.style.height = newContainerHeight + "px";
		this.pDialog.contentViewChild.nativeElement.style.visibility = "hidden";
		if (ObjectUtils.isNotNullOrUndefined(this.pDialog.footerViewChild)) {
			this.pDialog.footerViewChild.nativeElement.style.visibility = "hidden";
		}

		DomHandler.removeClass(document.body, "ui-overflow-hidden");
		
		this._minimized = true;
	}

	private revertMinimize(): void {
		
		let revertContainerHeight: any = this.prevDialogContainerHeight;
		this.pDialog.container.style.height = revertContainerHeight;
		this.pDialog.contentViewChild.nativeElement.style.visibility = "visible";
		if (ObjectUtils.isNotNullOrUndefined(this.pDialog.footerViewChild)) {
			this.pDialog.footerViewChild.nativeElement.style.visibility = "visible";
		}

		DomHandler.addClass(document.body, "ui-overflow-hidden");
		
		this._minimized = false;
	}
}