import { Component, Input, Output, EventEmitter } from "@angular/core";
import { NG_VALUE_ACCESSOR, ControlValueAccessor } from "@angular/forms";
import { ArrayUtils, ObjectUtils, CursValutarService, CursValutarModel, MessageDisplayer, AppError } from "@app/shared";
import { CheltuialaArbModel, ValutaForCheltuieliArbEnum } from "@app/shared/model/deplasari-deconturi/cheltuiala-arb.model";


@Component({
	selector: "app-cheltuieli-arb-manager",
	templateUrl: "./cheltuieli-arb-manager.component.html",
	providers: [
		{ provide: NG_VALUE_ACCESSOR, useExisting: CheltuieliArbManagerComponent, multi: true }
	]
})
export class CheltuieliArbManagerComponent implements ControlValueAccessor {

	@Input()
	public dataDecont: Date;

	@Output()
	public dataChanged: EventEmitter<CheltuialaArbModel[]>;

	public selectedCheltuialaArb: CheltuialaArbModel;
	public cheltuieliArb: CheltuialaArbModel[];

	public cheltuieliArbWindowCheltuialaArbId: number;
	public cheltuieliArbWindowSelectedCheltuialaArb: CheltuialaArbModel;

	public addActionEnabled: boolean;
	public removeActionEnabled: boolean;
	public editActionEnabled: boolean;
	public viewActionEnabled: boolean;

	public cheltuieliArbWindowVisible: boolean;
	public cheltuieliArbWindowDataDecont: Date;
	public cheltuieliArbWindowMode: "add" | "edit" | "view";
	
	public readonly: boolean = false;

	public totalCosturi: number = 0;

	public totalCosturiInRon: number = 0;
	public totalCosturiInEur: number = 0;
	public totalCosturiInUsd: number = 0;

	private onChange: any = () => { };
	private onTouched: any = () => { };

	public scrollHeight: string;

	public constructor() {
		this.cheltuieliArb = [];
		this.cheltuieliArbWindowVisible = false;
		this.dataChanged = new EventEmitter<CheltuialaArbModel[]>();

		this.scrollHeight = (window.innerHeight - 200) + "px";
	}

	public onAddAction(): void {
		this.cheltuieliArbWindowMode = "add";
		this.cheltuieliArbWindowDataDecont = this.dataDecont;
		this.cheltuieliArbWindowVisible = true;
	}

	public onEditAction(): void {
		this.cheltuieliArbWindowMode = "edit";
		this.cheltuieliArbWindowVisible = true;
		this.cheltuieliArbWindowDataDecont = this.dataDecont;
	}

	public onViewAction(): void {
		this.cheltuieliArbWindowMode = "view";
		this.cheltuieliArbWindowVisible = true;
	}

	public onRemoveAction(): void {
		ArrayUtils.removeElement(this.cheltuieliArb, this.cheltuieliArbWindowSelectedCheltuialaArb);
		this.propagateValue();		
		this.calculateTotals();
	}

	public onCheltuieliArbWindowClosed(): void {
		this.cheltuieliArbWindowVisible = false;
	}

	public onCheltuieliArbWindowDataSaved(cheltuialaArb: CheltuialaArbModel): void {
		if (this.cheltuieliArbWindowMode === "add") {
			this.cheltuieliArb.push(cheltuialaArb);
		} else {
			let indexOfCheltuialaArb = this.cheltuieliArb.indexOf(this.cheltuieliArbWindowSelectedCheltuialaArb);
			this.cheltuieliArb[indexOfCheltuialaArb] = cheltuialaArb;
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
		this.onChange(this.cheltuieliArb);
		this.onTouched();
		this.dataChanged.emit(this.cheltuieliArb);
		this.selectedCheltuialaArb = null;
		this.changePerspective();
	}

	public writeValue(cheltuieliArb: CheltuialaArbModel[]): void {
		if (ArrayUtils.isEmpty(cheltuieliArb)) {
			this.cheltuieliArb = [];
			return;
		}
		this.cheltuieliArb = [...cheltuieliArb];
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
		if (ObjectUtils.isNotNullOrUndefined(this.selectedCheltuialaArb)) {
			this.cheltuieliArbWindowSelectedCheltuialaArb = this.selectedCheltuialaArb;
			this.removeActionEnabled = true;
			this.editActionEnabled = true;
			this.viewActionEnabled = true;
			if (this.readonly) {
				this.removeActionEnabled = false;
				this.editActionEnabled = false;
			}
		} else {
			this.cheltuieliArbWindowSelectedCheltuialaArb = null;
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

		this.cheltuieliArb.forEach((cheltuialaArb: CheltuialaArbModel) => {
			let cursValutar: number = 1;
			if (cheltuialaArb.valuta === ValutaForCheltuieliArbEnum.EUR) {
				this.totalCosturiInEur += cheltuialaArb.valoareCheltuiala;
				cursValutar = cheltuialaArb.cursValutar;
			} else if (cheltuialaArb.valuta === ValutaForCheltuieliArbEnum.USD) {
				this.totalCosturiInUsd += cheltuialaArb.valoareCheltuiala;
				cursValutar = cheltuialaArb.cursValutar;
			} else if (cheltuialaArb.valuta === ValutaForCheltuieliArbEnum.RON) {
				this.totalCosturiInRon += cheltuialaArb.valoareCheltuiala;
			}

			this.totalCosturi += cheltuialaArb.valoareCheltuiala * cursValutar;
		});
	}
}
