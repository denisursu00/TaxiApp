import { Component, Input, Output, EventEmitter } from "@angular/core";
import { NG_VALUE_ACCESSOR, ControlValueAccessor } from "@angular/forms";
import { AlteDeconturiCheltuieliModel } from "@app/shared/model/alte-deconturi";
import { ArrayUtils, ObjectUtils } from "@app/shared";


@Component({
	selector: "app-alte-deconturi-cheltuieli-manager",
	templateUrl: "./alte-deconturi-cheltuieli-manager.component.html",
	providers: [
		{ provide: NG_VALUE_ACCESSOR, useExisting: AlteDeconturiCheltuieliManagerComponent, multi: true }
	]
})
export class AlteDeconturiCheltuieliManagerComponent implements ControlValueAccessor {

	@Input()
	public decontId: number;

	@Input()
	public dataDecont: Date;

	@Output()
	public dataChanged: EventEmitter<AlteDeconturiCheltuieliModel[]>;

	public selectedCheltuiala: AlteDeconturiCheltuieliModel;
	public cheltuieli: AlteDeconturiCheltuieliModel[];

	public cheltuieliWindowDecontId: number;
	public cheltuieliWindowDataDecont: Date;
	public cheltuieliWindowSelectedCheltuiala: AlteDeconturiCheltuieliModel;

	public addActionEnabled: boolean;
	public removeActionEnabled: boolean;
	public editActionEnabled: boolean;
	public viewActionEnabled: boolean;

	public cheltuieliWindowVisible: boolean;
	public readonly: boolean = false;

	public cheltuieliWindowMode: "add" | "edit" | "view";

	public scrollHeight: string;

	private onChange: any = () => { };
	private onTouched: any = () => { };

	public constructor() {
		this.cheltuieli = [];
		this.cheltuieliWindowVisible = false;
		this.dataChanged = new EventEmitter<AlteDeconturiCheltuieliModel[]>();

		this.scrollHeight = (window.innerHeight - 200) + "px";
	}

	public onAddAction(): void {
		this.cheltuieliWindowDecontId = this.decontId;
		this.cheltuieliWindowDataDecont = this.dataDecont;
		this.cheltuieliWindowMode = "add";
		this.cheltuieliWindowVisible = true;
	}

	public onEditAction(): void {
		this.cheltuieliWindowDecontId = this.decontId;
		this.cheltuieliWindowDataDecont = this.dataDecont;
		this.cheltuieliWindowMode = "edit";
		this.cheltuieliWindowVisible = true;
	}

	public onViewAction(): void {
		this.cheltuieliWindowDecontId = this.decontId;
		this.cheltuieliWindowMode = "view";
		this.cheltuieliWindowVisible = true;
	}

	public onRemoveAction(): void {
		ArrayUtils.removeElement(this.cheltuieli, this.cheltuieliWindowSelectedCheltuiala);
		this.propagateValue();
	}

	public onCheltuieliWindowClosed(): void {
		this.cheltuieliWindowVisible = false;
	}

	public onCheltuieliWindowDataSaved(cheltuiala: AlteDeconturiCheltuieliModel): void {
		if (this.cheltuieliWindowMode === "add") {
			this.cheltuieli.push(cheltuiala);
		} else {
			let indexOfCheltuiala = this.cheltuieli.indexOf(this.cheltuieliWindowSelectedCheltuiala);
			this.cheltuieli[indexOfCheltuiala] = cheltuiala;
		}
		this.propagateValue();
	}

	public onRowSelect(event: any): void {
		this.changePerspective();
	}

	public onRowUnSelect(event: any): void {
		this.changePerspective();
	}

	private propagateValue(): void {
		this.onChange(this.cheltuieli);
		this.onTouched();
		this.dataChanged.emit(this.cheltuieli);
		this.selectedCheltuiala = null;
		this.changePerspective();
	}

	public writeValue(cheltuieli: AlteDeconturiCheltuieliModel[]): void {
		if (ArrayUtils.isEmpty(cheltuieli)) {
			this.cheltuieli = [];
			return;
		}
		this.cheltuieli = [...cheltuieli];
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
		if (ObjectUtils.isNotNullOrUndefined(this.selectedCheltuiala)) {
			this.cheltuieliWindowSelectedCheltuiala = this.selectedCheltuiala;
			this.removeActionEnabled = true;
			this.editActionEnabled = true;
			this.viewActionEnabled = true;
			if (this.readonly) {
				this.removeActionEnabled = false;
				this.editActionEnabled = false;
			}
		} else {
			this.cheltuieliWindowSelectedCheltuiala = null;
			this.removeActionEnabled = false;
			this.editActionEnabled = false;
			this.viewActionEnabled = false;
		}
	}
}
