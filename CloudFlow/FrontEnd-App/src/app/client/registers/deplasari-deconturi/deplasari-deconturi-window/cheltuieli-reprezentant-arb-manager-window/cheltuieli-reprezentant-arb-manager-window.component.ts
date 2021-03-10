import { Component, OnInit, Input, Output, EventEmitter } from "@angular/core";
import { UiUtils, FormUtils, BaseWindow } from "@app/shared";
import { FormGroup, FormControl, AbstractControl } from "@angular/forms";
import { CheltuialaReprezentantArbModel } from "@app/shared/model/deplasari-deconturi";
import { Dialog } from "primeng/primeng";

@Component({
	selector: "app-cheltuieli-reprezentant-arb-manager-window",
	templateUrl: "./cheltuieli-reprezentant-arb-manager-window.component.html"
})
export class CheltuieliReprezentantArbManagerWindowComponent extends BaseWindow implements OnInit {

	@Input()
	public cheltuieliReprezentantArb: CheltuialaReprezentantArbModel[];

	@Output()
	public windowClosed: EventEmitter<void>;
	
	public windowVisible: boolean = true;

	public formGroup: FormGroup;

	public constructor() {
		super();
		this.windowClosed = new EventEmitter<void>();
		this.init();
		this.unlock();
	}

	public ngOnInit() {
		this.cheltuieliReprezentantArbFormControl.setValue(this.cheltuieliReprezentantArb);
	}

	private init(): void {
		this.prepareForm();
	}

	private prepareForm(): void {
		this.formGroup = new FormGroup({});
		this.formGroup.addControl("cheltuieliReprezentantArb", new FormControl(null, []));

		this.cheltuieliReprezentantArbFormControl.disable();
	}

	public onHide($event): void {
		this.windowClosed.emit();
	}

	public get cheltuieliReprezentantArbFormControl(): AbstractControl {
		return FormUtils.getControlByName(this.formGroup, "cheltuieliReprezentantArb");
	}
}
