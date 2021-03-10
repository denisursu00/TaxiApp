import { Component, OnInit, Input, Output, EventEmitter } from "@angular/core";
import { UiUtils, FormUtils, BaseWindow } from "@app/shared";
import { FormGroup, FormControl, AbstractControl } from "@angular/forms";
import { CheltuialaArbModel } from "@app/shared/model/deplasari-deconturi";
import { Dialog } from "primeng/primeng";

@Component({
	selector: "app-cheltuieli-arb-manager-window",
	templateUrl: "./cheltuieli-arb-manager-window.component.html"
})
export class CheltuieliArbManagerWindowComponent extends BaseWindow implements OnInit {

	@Input()
	public cheltuieliArb: CheltuialaArbModel[];

	@Output()
	public windowClosed: EventEmitter<void>;
	
	public windowVisible: boolean = true;

	public formGroup: FormGroup;
	
	public constructor() {
		super();
		this.windowClosed = new EventEmitter<void>();
		this.init();
	}

	public ngOnInit() {
		this.cheltuieliArbFormControl.setValue(this.cheltuieliArb);
	}

	private init(): void {
		this.prepareForm();
	}

	private prepareForm(): void {
		this.formGroup = new FormGroup({});
		this.formGroup.addControl("cheltuieliArb", new FormControl(null, []));

		this.cheltuieliArbFormControl.disable();
	}

	public onHide($event): void {
		this.windowClosed.emit();
	}

	public get cheltuieliArbFormControl(): AbstractControl {
		return FormUtils.getControlByName(this.formGroup, "cheltuieliArb");
	}
}
