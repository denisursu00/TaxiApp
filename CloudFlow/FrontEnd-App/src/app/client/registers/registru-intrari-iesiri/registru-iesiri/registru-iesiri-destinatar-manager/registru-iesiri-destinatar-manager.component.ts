import { Component, OnInit, Input, Output, EventEmitter } from "@angular/core";
import { RegistruIesiriDestinatariModel, ArrayUtils, ObjectUtils, NomenclatorService, AppError, NomenclatorConstants, JoinedNomenclatorUiAttributesValueModel, MessageDisplayer, GetNomenclatorValuesRequestModel, NomenclatorSimpleFilter, NomenclatorValueModel } from "@app/shared";
import { ControlValueAccessor, NG_VALUE_ACCESSOR } from "@angular/forms";
import { RegistruIesiriDestinatarWindowInputData } from "../registru-iesiri-destinatar-window/registru-iesiri-destinatar-window.component";
import { SelectItem } from "primeng/primeng";

@Component({
	selector: "app-registru-iesiri-destinatar-manager",
	templateUrl: "./registru-iesiri-destinatar-manager.component.html",
	providers: [
		{ provide: NG_VALUE_ACCESSOR, useExisting: RegistruIesiriDestinatarManagerComponent, multi: true }
	]
})
export class RegistruIesiriDestinatarManagerComponent implements OnInit, ControlValueAccessor {

	@Input()
	public mode: "add" | "edit" | "view";

	@Input()
	public inputData: RegistruIesiriDestinatarManagerInputData;

	@Output()
	public mappedDestinatarAdded: EventEmitter<number>;

	private messageDisplayer: MessageDisplayer;

	public destinatari: RegistruIesiriDestinatariModel[];
	public selectedDestinatar: RegistruIesiriDestinatariModel;
	public nomenclatorIdByCode: Map<string, number>;

	public addActionEnabled: boolean;
	public removeActionEnabled: boolean;
	public editActionEnabled: boolean;
	public viewActionEnabled: boolean;

	public destinatarWindowVisible: boolean;
	public displayComisiiGlMembriiArb: boolean;
	public isLoadingComisiiGl: boolean;
	public comisiiGlMembriiArb: SelectItem[];
	public selectedComisieGlMembriiArb: number;
	public readonly: boolean = false;

	public destinatarWindowMode: "add" | "edit" | "view";

	public registruIesiriDestinatarWindowInputData: RegistruIesiriDestinatarWindowInputData;

	public loading: boolean;
	private cachedDestinatariViewNameById: object = {};

	private nomenclatorService: NomenclatorService;

	private onChange: any = () => { };
	private onTouched: any = () => { };

	public constructor(nomenclatorService: NomenclatorService, messageDisplayer: MessageDisplayer) {
		this.destinatari = [];
		this.destinatarWindowVisible = false;
		this.addActionEnabled = false;
		this.loading = false;
		this.nomenclatorService = nomenclatorService;
		this.displayComisiiGlMembriiArb = false;
		this.messageDisplayer = messageDisplayer;
		this.comisiiGlMembriiArb = [];
		this.selectedComisieGlMembriiArb = null;
		this.nomenclatorIdByCode = new Map();
		this.mappedDestinatarAdded = new EventEmitter();
	}

	public ngOnInit(): void {
		this.prepareNomenclatorValueSelectors();
		if (this.isAdd){
			this.isLoadingComisiiGl = true;
			this.loadComisiiGlSelectableItems();
		}
		this.updatePerspective();
	}

	private isAdd(): boolean {
		return this.mode === "add";
	}

	private isEdit(): boolean {
		return this.mode === "edit";
	}

	public isView(): boolean {
		return this.mode === "view";
	}

	private updatePerspective(): void {
		this.addActionEnabled = this.isAdd();
		this.viewActionEnabled = ObjectUtils.isNotNullOrUndefined(this.selectedDestinatar);
		this.editActionEnabled = ObjectUtils.isNotNullOrUndefined(this.selectedDestinatar) && !this.isView();
		this.removeActionEnabled = ObjectUtils.isNotNullOrUndefined(this.selectedDestinatar) && this.isAdd();
	}

	public prepareRegistruIesiriDestinatarWindowInputData(): void {
		this.registruIesiriDestinatarWindowInputData = new RegistruIesiriDestinatarWindowInputData();
		if (ObjectUtils.isNotNullOrUndefined(this.inputData)) {
			this.registruIesiriDestinatarWindowInputData.tipDocumentId = this.inputData.tipDocumentId;
			this.registruIesiriDestinatarWindowInputData.registruIesiriId = this.inputData.registruIesiriId;
			this.registruIesiriDestinatarWindowInputData.codDocumentEchivalent = this.inputData.codDocumentEchivalent;
			this.registruIesiriDestinatarWindowInputData.hasMappedDestinatar = this.inputData.hasMappedDestinatar;
			this.registruIesiriDestinatarWindowInputData.registruIesiriRegistryNumber = this.inputData.registruIesiriRegistryNumber;
			this.registruIesiriDestinatarWindowInputData.registruIesiriWindowPerspective = this.inputData.registruIesiriWindowPerspective;
		}
	}

	private prepareNomenclatorValueSelectors(): void {
		let codes: string[] = [ NomenclatorConstants.NOMENCLATOR_CODE_INSTITUTII, NomenclatorConstants.NOMENCLATOR_CODE_COMISII_SAU_GL];
		this.nomenclatorService.getNomenclatorIdByCodeAsMap(codes, {
			onSuccess: (nomenclatorIdByCode: object): void => {		
				if (ObjectUtils.isNotNullOrUndefined(nomenclatorIdByCode)) {
					for (let [key, value] of Object.entries(nomenclatorIdByCode)){
						this.nomenclatorIdByCode.set(key, value);
					}
				}
			},
			onFailure: (appError: AppError): void => {
				this.messageDisplayer.displayAppError(appError);
			}
		});
	}

	public onAddAction(): void {
		this.destinatarWindowMode = "add";
		this.destinatarWindowVisible = true;
		this.prepareRegistruIesiriDestinatarWindowInputData();
	}

	public onEditAction(): void {
		this.destinatarWindowMode = "edit";
		this.destinatarWindowVisible = true;
		this.prepareRegistruIesiriDestinatarWindowInputData();
	}

	public onViewAction(): void {
		this.destinatarWindowMode = "view";
		this.destinatarWindowVisible = true;
		this.prepareRegistruIesiriDestinatarWindowInputData();
	}

	public onAddArbMembersAction(): void {
		this.displayComisiiGlMembriiArb = true;
	}

	public loadComisiiGlSelectableItems(): void {
		this.nomenclatorService.getListOfConcatenatedUiAttributesByNomenclatorCode(NomenclatorConstants.NOMENCLATOR_CODE_COMISII_SAU_GL, {
			onSuccess: (comisiiGl: JoinedNomenclatorUiAttributesValueModel[]) => {
				for (let comisieGl of comisiiGl){
					this.comisiiGlMembriiArb.push({
						label: comisieGl.value,
						value: comisieGl.id
					});
				}
				this.isLoadingComisiiGl = false;
			},
			onFailure: (error: AppError) => {
				this.messageDisplayer.displayAppError(error);
			}
		});
	}

	public async onAddAffiliatedMembersAction(): Promise<void> {
		this.loading = true;
		try {
			let tipInstitutie: NomenclatorValueModel[] = await this.getInstitutionTypeByName(NomenclatorConstants.TIP_INSTITUTII_TIP_MEMBRU_AFILIAT);
			let institutii: NomenclatorValueModel[] = await this.getAllInstitutionsByTypeId(tipInstitutie[0].id);
			institutii.forEach(institutie => {
				let destinatar: RegistruIesiriDestinatariModel = new RegistruIesiriDestinatariModel();
				destinatar.destinatarExistentId = institutie.id;
				if (institutie[NomenclatorConstants.INSTITUTII_ATTRIBUTE_KEY_TIP_RADIAT] === "false"){
					if (!this.iesireHasDestinatar(destinatar)){
						this.destinatari.push(destinatar);
					}else{
						this.messageDisplayer.displayError("REGISTRU_IESIRI_DESTINATARI_MANAGER_RECEIVER_ALREADY_ADDED");
					}
				}
			});
			this.propagateValue();
			this.updateDestinatariView();
		}catch(error){
			if (error instanceof AppError){
				this.messageDisplayer.displayAppError(error);
			}else{
				console.error(error);
			}
		}
		this.loading = false;
	}

	public onClearSelectedComisieGlMembriiArb(): void {
		this.selectedComisieGlMembriiArb = null;
	}

	public async onAddComisieGlMembriiArb(): Promise<void> {
		this.displayComisiiGlMembriiArb = false;
		this.loading = true;
		try{
			let tipInstitutie: NomenclatorValueModel[] = await this.getInstitutionTypeByName(NomenclatorConstants.TIP_INSTITUTII_TIP_MEMBRU_ARB);
			let institutii: NomenclatorValueModel[] = await this.getAllInstitutionsByTypeId(tipInstitutie[0].id);
			institutii.forEach((institutie) => {
				if ( institutie[NomenclatorConstants.INSTITUTII_ATTRIBUTE_KEY_TIP_RADIAT] === "false" && institutie[NomenclatorConstants.INSTITUTII_ATTR_KEY_ABREVIERE] !== "ARB"){
					let destinatar: RegistruIesiriDestinatariModel = new RegistruIesiriDestinatariModel();
					destinatar.destinatarExistentId = institutie.id;
					if (this.selectedComisieGlMembriiArb !== null){
						destinatar.comisieGlId = this.selectedComisieGlMembriiArb;
					}
					if (!this.iesireHasDestinatar(destinatar)){
						this.destinatari.push(destinatar);
					}else{
						this.messageDisplayer.displayError("REGISTRU_IESIRI_DESTINATARI_MANAGER_RECEIVER_ALREADY_ADDED");
					}
				}
			});
			this.loading = false;
			this.propagateValue();
			this.updateDestinatariView();
		}catch(error){
			this.loading = false;
			if (error instanceof AppError){
				this.messageDisplayer.displayAppError(error);
			}else{
				console.error(error);
			}
		}
	}

	public getInstitutionTypeByName(name: string): Promise<any> {
		return new Promise<any>((resolve, reject) => {
			let tipInstitutieRequestModel: GetNomenclatorValuesRequestModel = new GetNomenclatorValuesRequestModel();
			let membruArbFilter: NomenclatorSimpleFilter = new NomenclatorSimpleFilter();
			membruArbFilter.attributeKey = NomenclatorConstants.TIP_INSTITUTII_ATTR_KEY_TIP;
			membruArbFilter.value = name;
			tipInstitutieRequestModel.filters.push(membruArbFilter);
			tipInstitutieRequestModel.nomenclatorCode = NomenclatorConstants.NOMENCLATOR_CODE_TIP_INSTITUTII;
			this.nomenclatorService.getNomenclatorValues(tipInstitutieRequestModel, {
				onSuccess: (tipInstitutie: NomenclatorValueModel[]) => {
					resolve(tipInstitutie);
				},
				onFailure: (error: AppError) => {
					reject(error);
				}
			});
		});
	}

	public getAllInstitutionsByTypeId(id: number): Promise<any> {
		return new Promise<any>((resolve, reject) => {
			let institutieRequestModel: GetNomenclatorValuesRequestModel = new GetNomenclatorValuesRequestModel();
			let tipInstitutieFilter: NomenclatorSimpleFilter = new NomenclatorSimpleFilter();
			tipInstitutieFilter.attributeKey = NomenclatorConstants.INSITUTII_ATTR_KEY_TIP_INSTITUTIE;
			tipInstitutieFilter.value = id;
			institutieRequestModel.filters.push(tipInstitutieFilter);
			institutieRequestModel.nomenclatorCode = NomenclatorConstants.NOMENCLATOR_CODE_INSTITUTII;
			this.nomenclatorService.getNomenclatorValues(institutieRequestModel, {
				onSuccess: (institutii: NomenclatorValueModel[]) => {
					resolve(institutii);
				},
				onFailure: (error: AppError) => {
					reject(error);
				}
			});
		});
		
	}

	public onRemoveAction(): void {
		ArrayUtils.removeElement(this.destinatari, this.selectedDestinatar);
		this.selectedDestinatar = null;
		this.updatePerspective();
		this.propagateValue();
	}

	public onDestinatarWindowClosed(): void {
		this.destinatarWindowVisible = false;
	}

	public onDestinatarDataSaved(destinatar: RegistruIesiriDestinatariModel): void {
		if (this.destinatarWindowMode === "add") {
			if (!this.iesireHasDestinatar(destinatar)){
				this.destinatari.push(destinatar);
				this.handleAutoCompleteData(destinatar);
			}else{
				this.messageDisplayer.displayError("REGISTRU_IESIRI_DESTINATARI_MANAGER_RECEIVER_ALREADY_ADDED");
			}
		} else {
			ArrayUtils.removeElement(this.destinatari, this.selectedDestinatar);
			if (!this.iesireHasDestinatar(destinatar)){
				this.destinatari.push(destinatar);
				this.handleAutoCompleteData(destinatar);
				this.selectedDestinatar = null;
				this.handleSelection(destinatar);
			}else{
				this.destinatari.push(this.selectedDestinatar);
				this.messageDisplayer.displayError("REGISTRU_IESIRI_DESTINATARI_MANAGER_RECEIVER_ALREADY_ADDED");
			}
		}
		this.propagateValue();
		this.updateDestinatariView();
	}

	public handleSelection(rowData: RegistruIesiriDestinatariModel): void {
		//a trebuit sa tratez selectarea asa deoarece pSelectableRow nu functioneaza corect cu grupuri de randuri ( la selectarea unui rand se selectau toate )
		this.handleSelectionHighlight(rowData);
		if (this.selectedDestinatar != null
			&& this.selectedDestinatar.destinatarExistentId === rowData.destinatarExistentId
			&& this.selectedDestinatar.nume === rowData.nume){
			this.selectedDestinatar = null;
		}else{
			this.selectedDestinatar = rowData;
		}
		this.updatePerspective();
	}

	public handleAutoCompleteData(destinatar: RegistruIesiriDestinatariModel): void {
		if(this.inputData.codDocumentEchivalent && destinatar.registruIntrariId){
			this.mappedDestinatarAdded.emit(destinatar.registruIntrariId);
		}
	}

	public handleSelectionHighlight(rowData: RegistruIesiriDestinatariModel): void  {
		this.destinatari.forEach(destinatar => {
			if (destinatar.destinatarExistentId === rowData.destinatarExistentId
				&& destinatar.nume === rowData.nume){
					destinatar.rowStyleClass["ui-state-highlight"] = !destinatar.rowStyleClass["ui-state-highlight"];
				}
			else {
				destinatar.rowStyleClass["ui-state-highlight"] = false;
			}
		});
	}

	public getListOfDestinatariNames(): string {
		let result: Array<string> = new Array();
		this.destinatari.forEach(destinatar => {
			result.push(destinatar.numeForView);
		});
		return result.join(", ");
	}

	private propagateValue(): void {
		this.onChange(this.destinatari);
		this.onTouched();
	}

	public writeValue(destinatariValue: RegistruIesiriDestinatariModel[]): void {
		if (ArrayUtils.isEmpty(destinatariValue)) {
			ArrayUtils.removeElements(this.destinatari, this.destinatari);
		}else{
			this.destinatari = [...destinatariValue];
		}
		this.updateDestinatariView();
	}

	private updateDestinatariView(): void {
		if (ArrayUtils.isEmpty(this.destinatari)) {
			return;
		}
		this.loading = true;
		this.destinatari.forEach((destinatar: RegistruIesiriDestinatariModel) => {
			if (ObjectUtils.isNotNullOrUndefined(destinatar.destinatarExistentId)) {
				let cachedviewName: string = this.cachedDestinatariViewNameById[destinatar.destinatarExistentId];
				this.cachedDestinatariViewNameById[destinatar.destinatarExistentId] = cachedviewName;
			}
		});
		this.resolveCache(() => {
			this.destinatari.forEach((destinatar: RegistruIesiriDestinatariModel) => {
				if (ObjectUtils.isNotNullOrUndefined(destinatar.destinatarExistentId)) {
					destinatar.numeForView = this.cachedDestinatariViewNameById[destinatar.destinatarExistentId];
				} else {
					destinatar.numeForView = destinatar.nume;
				}
			});
			this.destinatari.sort((dest1, dest2) => {
				let nume1 = dest1.numeForView.toUpperCase();
				let nume2 = dest2.numeForView.toUpperCase();
				return nume1 < nume2 ? -1 : nume1 > nume2 ? 1 : 0;
			});
			this.loading = false;
		});
	}

	private resolveCache(callback: () => any): void {
		let ids: number[] = [];
		Object.keys(this.cachedDestinatariViewNameById).forEach((id: string) => {
			if (ObjectUtils.isNullOrUndefined(this.cachedDestinatariViewNameById[id])) {
				ids.push(Number(id));
			}
		});
		if (ArrayUtils.isNotEmpty(ids)) {
			this.nomenclatorService.getUiAttributeValues(ids, {
				onSuccess: (uiAttributesMap: object): void => {
					ids.forEach((id: number) => {
						this.cachedDestinatariViewNameById[id] = uiAttributesMap[id];
					});
					callback();
				},
				onFailure: (error: AppError): void => {
					callback();
				}
			});
		} else {
			callback();
		}
	}

	private iesireHasDestinatar(thatDestinatar: RegistruIesiriDestinatariModel): boolean {
		let found: RegistruIesiriDestinatariModel;
		if (ObjectUtils.isNotNullOrUndefined(thatDestinatar.destinatarExistentId)){
			found = this.destinatari.find( thisDestinatar => {
				return thisDestinatar.destinatarExistentId === thatDestinatar.destinatarExistentId;
			});
		}else{
			found = this.destinatari.find( thisDestinatar => {
				return thisDestinatar.nume === thatDestinatar.nume;
			});
		}
		return ObjectUtils.isNotNullOrUndefined(found);
	}

	public registerOnChange(fn: any): void {
		this.onChange = fn;
	}

	public registerOnTouched(fn: any): void {
		this.onTouched = fn;
	}

	public setDisabledState(isDisabled: boolean): void {
		this.readonly = isDisabled;
	}

}

export class RegistruIesiriDestinatarManagerInputData {
	public tipDocumentId: number;
	public codDocumentEchivalent: string;
	public registruIesiriId: number;
	public hasMappedDestinatar: boolean;
	public registruIesiriRegistryNumber: string;
	public registruIesiriWindowPerspective: "add" | "edit" | "view" = "add";
}