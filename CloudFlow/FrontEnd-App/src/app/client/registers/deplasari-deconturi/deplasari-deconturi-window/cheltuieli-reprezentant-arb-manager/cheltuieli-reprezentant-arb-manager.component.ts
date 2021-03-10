import { Component, Input, Output, EventEmitter } from "@angular/core";
import { NG_VALUE_ACCESSOR, ControlValueAccessor } from "@angular/forms";
import { ArrayUtils, ObjectUtils } from "@app/shared";
import { CheltuialaReprezentantArbModel, ValutaForCheltuieliReprezentantArbEnum } from "@app/shared/model/deplasari-deconturi/cheltuiala-reprezentant-arb.model";


@Component({
	selector: "app-cheltuieli-reprezentant-arb-manager",
	templateUrl: "./cheltuieli-reprezentant-arb-manager.component.html",
	providers: [
		{ provide: NG_VALUE_ACCESSOR, useExisting: CheltuieliReprezentantArbManagerComponent, multi: true }
	]
})
export class CheltuieliReprezentantArbManagerComponent implements ControlValueAccessor {

	@Input()
	public cheltuialaId: number;

	@Input()
	public dataDecont: Date;

	@Output()
	public dataChanged: EventEmitter<CheltuialaReprezentantArbModel[]>;

	public selectedCheltuialaReprezentantArb: CheltuialaReprezentantArbModel;
	public cheltuieliReprezentantArb: CheltuialaReprezentantArbModel[];

	public cheltuieliReprezentantArbWindowCheltuialaReprezentantArbId: number;
	public cheltuieliReprezentantArbWindowSelectedCheltuialaReprezentantArb: CheltuialaReprezentantArbModel;

	public addActionEnabled: boolean;
	public removeActionEnabled: boolean;
	public editActionEnabled: boolean;
	public viewActionEnabled: boolean;

	public cheltuieliReprezentantArbWindowVisible: boolean;
	public cheltuieliReprezentantArbWindowDataDecont: Date;
	public readonly: boolean = false;

	public cheltuieliReprezentantArbWindowMode: "add" | "edit" | "view";
	
	public totalCosturi: number = 0;

	public totalCosturiInRon: number = 0;
	public totalCosturiInEur: number = 0;
	public totalCosturiInUsd: number = 0;

	public scrollHeight: string;

	private onChange: any = () => { };
	private onTouched: any = () => { };

	public constructor() {
		this.cheltuieliReprezentantArb = [];
		this.cheltuieliReprezentantArbWindowVisible = false;
		this.dataChanged = new EventEmitter<CheltuialaReprezentantArbModel[]>();
		this.scrollHeight = (window.innerHeight - 200) + "px";
	}

	public onAddAction(): void {
		this.cheltuieliReprezentantArbWindowCheltuialaReprezentantArbId = this.cheltuialaId;
		this.cheltuieliReprezentantArbWindowMode = "add";
		this.cheltuieliReprezentantArbWindowVisible = true;
		this.cheltuieliReprezentantArbWindowDataDecont = this.dataDecont;
	}

	public onEditAction(): void {
		this.cheltuieliReprezentantArbWindowCheltuialaReprezentantArbId = this.cheltuialaId;
		this.cheltuieliReprezentantArbWindowMode = "edit";
		this.cheltuieliReprezentantArbWindowVisible = true;
		this.cheltuieliReprezentantArbWindowDataDecont = this.dataDecont;
	}

	public onViewAction(): void {
		this.cheltuieliReprezentantArbWindowCheltuialaReprezentantArbId = this.cheltuialaId;
		this.cheltuieliReprezentantArbWindowMode = "view";
		this.cheltuieliReprezentantArbWindowVisible = true;
	}

	public onRemoveAction(): void {
		ArrayUtils.removeElement(this.cheltuieliReprezentantArb, this.cheltuieliReprezentantArbWindowSelectedCheltuialaReprezentantArb);
		this.propagateValue();
		this.calculateTotals();
	}

	public onCheltuieliReprezentantArbWindowClosed(): void {
		this.cheltuieliReprezentantArbWindowVisible = false;
	}

	public onCheltuieliReprezentantArbWindowDataSaved(cheltuialaReprezentantArb: CheltuialaReprezentantArbModel): void {
		if (this.cheltuieliReprezentantArbWindowMode === "add") {
			this.cheltuieliReprezentantArb.push(cheltuialaReprezentantArb);
		} else {
			let indexOfCheltuialaReprezentantArb = this.cheltuieliReprezentantArb.indexOf(this.cheltuieliReprezentantArbWindowSelectedCheltuialaReprezentantArb);
			this.cheltuieliReprezentantArb[indexOfCheltuialaReprezentantArb] = cheltuialaReprezentantArb;
		}
		this.propagateValue();
		this.calculateTotals();
	}

	public onRowSelect(event: any): void {
		this.changePerspective();
	}

	public onRowUnSelect(event: any): void {
		this.changePerspective();
	}

	private propagateValue(): void {
		this.onChange(this.cheltuieliReprezentantArb);
		this.onTouched();
		this.dataChanged.emit(this.cheltuieliReprezentantArb);
		this.selectedCheltuialaReprezentantArb = null;
		this.changePerspective();
	}

	public writeValue(cheltuieliReprezentantArb: CheltuialaReprezentantArbModel[]): void {
		if (ArrayUtils.isEmpty(cheltuieliReprezentantArb)) {
			this.cheltuieliReprezentantArb = [];
			return;
		}
		this.cheltuieliReprezentantArb = [...cheltuieliReprezentantArb];
		this.calculateTotals();
	}

	public registerOnChange(fn: any): void {
		this.onChange = fn;
	}

	public registerOnTouched(fn: any): void {
		this.onTouched = fn;
	}

	public setDisabledState(isDisabled: boolean): void {
		this.readonly = isDisabled;
		this.addActionEnabled = isDisabled;
	}

	private changePerspective(): void {
		if (ObjectUtils.isNotNullOrUndefined(this.selectedCheltuialaReprezentantArb)) {
			this.cheltuieliReprezentantArbWindowSelectedCheltuialaReprezentantArb = this.selectedCheltuialaReprezentantArb;
			this.removeActionEnabled = true;
			this.editActionEnabled = true;
			this.viewActionEnabled = true;
			if (this.readonly) {
				this.removeActionEnabled = false;
				this.editActionEnabled = false;
			}
		} else {
			this.cheltuieliReprezentantArbWindowSelectedCheltuialaReprezentantArb = null;
			this.removeActionEnabled = false;
			this.editActionEnabled = false;
			this.viewActionEnabled = false;
		}
	}
	
	private calculateTotals(): void {
		this.totalCosturi = 0;
		this.totalCosturiInEur = 0;
		this.totalCosturiInUsd = 0;
		this.totalCosturiInRon = 0;

		this.cheltuieliReprezentantArb.forEach((cheltuialaReprezentantArb: CheltuialaReprezentantArbModel) => {
			let cursValutar: number = 1;
			if (cheltuialaReprezentantArb.valuta === ValutaForCheltuieliReprezentantArbEnum.EUR) {
				this.totalCosturiInEur += cheltuialaReprezentantArb.valoareCheltuiala;
				cursValutar = cheltuialaReprezentantArb.cursValutar;
			} else if (cheltuialaReprezentantArb.valuta === ValutaForCheltuieliReprezentantArbEnum.USD) {
				this.totalCosturiInUsd += cheltuialaReprezentantArb.valoareCheltuiala;
				cursValutar = cheltuialaReprezentantArb.cursValutar;
			} else if (cheltuialaReprezentantArb.valuta === ValutaForCheltuieliReprezentantArbEnum.RON) {
				this.totalCosturiInRon += cheltuialaReprezentantArb.valoareCheltuiala;
			}

			this.totalCosturi += cheltuialaReprezentantArb.valoareCheltuiala * cursValutar;
		});
	}
}
