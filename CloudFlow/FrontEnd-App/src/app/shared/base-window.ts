import { Dialog, DomHandler } from "primeng/primeng";
import { Input, ViewChild } from "@angular/core";
import { PDialogMinimizer } from "./pdialog-minimizer";

export class BaseWindow {

	private static MAXIMIZED_DEFAULT_VALUE: boolean = true;

	@Input()
	public maximized: boolean = BaseWindow.MAXIMIZED_DEFAULT_VALUE;
	
	@ViewChild("pDialog")
	public pDialog: Dialog;

	public loadingVisible: boolean = true;

	private pDialogMinimizer: PDialogMinimizer = null;

	public onShow(event: any): void {
		this.pDialog.center();
		if (this.maximized) {
			setTimeout(() => {
				this.pDialog.maximize();
			}, 0);
		}
		this.doOnShow();
	}

	protected doOnShow(): void {}
	
	public lock() {
		this.loadingVisible = true;
	}

	public unlock() {
		this.loadingVisible = false;
	}

	public isLock(): boolean {
		return this.loadingVisible === true;
	}

	public onToggleMinimize(): void {
		if (this.pDialogMinimizer === null) {
			this.pDialogMinimizer = new PDialogMinimizer(this.pDialog);
		}
		this.pDialogMinimizer.toggleMinimize();
	}

	public get minimized(): boolean {
		if (this.pDialogMinimizer === null) {
			return false;
		}
		return this.pDialogMinimizer.minimized;
	}
}