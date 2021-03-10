import { Component, OnInit, ViewChild } from "@angular/core";
import { MessageDisplayer, ConfirmationUtils, TranslateUtils, NomenclatorValueModel, NomenclatorService, AppError, NomenclatorConstants, PageConstants, ConfirmationWindowFacade, ValueOfNomenclatorValueField } from "@app/shared";
import { MetadataDefinitionModel, ArrayUtils, ObjectUtils } from "@app/shared";
import { MembruReprezentantiComisieSauGLModel, ReprezentantiComisieSauGLModel, Message } from "@app/shared";
import { ReprezentantiComisieSauGLTabContent } from "./../reprezentanti-comisie-sau-gl-tab-content";
import { MembruReprezentantiComisieSauGLWindowInputData } from "../membru-reprezentanti-comisie-sau-gl-window/membru-reprezentanti-comisie-sau-gl-window.component";
import { Table } from "primeng/table";
import { ListItemUtils } from "@app/shared/utils/list-item-utils";
import { BrowserAnimationsModule } from "@angular/platform-browser/animations";

@Component({
	selector: "app-reprezentanti-comisie-sau-gl-membri-tab-content",
	templateUrl: "./reprezentanti-comisie-sau-gl-membri-tab-content.component.html"
})
export class ReprezentantiComisieSauGLMembriTabContentComponent extends ReprezentantiComisieSauGLTabContent {

	private static readonly EXPORT_CSV_FILE_NAME: string = "Membrii_";

	@ViewChild(Table)
	public dataTable: Table;

	public columns: any[];

	public selectedMembru: MembruView;

	private nomenclatorService: NomenclatorService;
	private messageDisplayer: MessageDisplayer;
	
	private translateUtils: TranslateUtils;

	public membriModels: MembruReprezentantiComisieSauGLModel[];
	public membriViews: MembruView[];

	public existingMembriModels: MembruReprezentantiComisieSauGLModel[];
	public existingMembersByInstitutieIdAndMembruId: string[] = [];
	public existingMembersByInstitutieNameAndMembruName: string[] = [];

	public uiValueByNomenclatorValueId: object;
	public nomenclatorValueModelByValueId: object;

	public addEnabled: boolean;
	public editEnabled: boolean;
	public viewEnabled: boolean;
	public deleteEnabled: boolean;

	public loading: boolean;
	public pageSize: number;
	public totalRecords: number;
	
	public membruWindowVisible: boolean;
	public membruWindowMode: "add" | "edit" | "view";
	public membruWindowInputData: MembruReprezentantiComisieSauGLWindowInputData;

	private messages: Message[];

	private indexOfModelEditing: number;
	
	public confirmationWindow: ConfirmationWindowFacade;
	public dataFilters: any[] = [];

	public scrollHeight: string;

	public constructor(nomenclatorService: NomenclatorService, messageDisplayer: MessageDisplayer, 
			translateUtils: TranslateUtils) {
		super();
		this.messageDisplayer = messageDisplayer;
		this.translateUtils = translateUtils;
		this.nomenclatorService = nomenclatorService;
		this.membriModels = [];
		this.uiValueByNomenclatorValueId = {};
		this.nomenclatorValueModelByValueId = {};
		this.pageSize = PageConstants.DEFAULT_PAGE_SIZE;		
		this.confirmationWindow = new ConfirmationWindowFacade();
	}
	
	protected doWhenNgOnInit(): void {
		this.init();
	}

	private init(): void {
		this.membriViews = [];
		this.membruWindowVisible = false;
		this.scrollHeight = (window.innerHeight - 300) + "px";
		this.changePerspective();
		this.columns = [
			{ field: "denumireInstitutie", header: this.translateUtils.translateLabel("MEMBRU_REPREZENTANTI_COMISIE_SAU_GL_INSTITUTIE") },
			{ field: "nume", header: this.translateUtils.translateLabel("MEMBRU_REPREZENTANTI_COMISIE_SAU_GL_NUME") },
			{ field: "prenume", header: this.translateUtils.translateLabel("MEMBRU_REPREZENTANTI_COMISIE_SAU_GL_PRENUME") },
			{ field: "functie", header: this.translateUtils.translateLabel("MEMBRU_REPREZENTANTI_COMISIE_SAU_GL_FUNCTIE") },
			{ field: "departament", header: this.translateUtils.translateLabel("MEMBRU_REPREZENTANTI_COMISIE_SAU_GL_DEPARTAMENT") },
			{ field: "email", header: this.translateUtils.translateLabel("MEMBRU_REPREZENTANTI_COMISIE_SAU_GL_EMAIL") },
			{ field: "telefon", header: this.translateUtils.translateLabel("MEMBRU_REPREZENTANTI_COMISIE_SAU_GL_TELEFON") },
			{ field: "calitate", header: this.translateUtils.translateLabel("MEMBRU_REPREZENTANTI_COMISIE_SAU_GL_CALITATE") },
			{ field: "stare", header: this.translateUtils.translateLabel("MEMBRU_REPREZENTANTI_COMISIE_SAU_GL_STARE") }
		];
	}

	private lock(): void {
		this.loading = true;
	}

	private unlock(): void {
		this.loading = false;
	}

	private updateViews(): void {
		this.clearFilters();
		this.membriViews = [];
		if (ArrayUtils.isEmpty(this.membriModels)) {
			return;
		}

		this.loading = true;
		let uiInstitutieNomValueIds: number[] = [];
		let membriNomValueIds: number[] = [];
		this.membriModels.forEach((model: MembruReprezentantiComisieSauGLModel, index: number) => {
			let cachedUiDenumireInstitutie: string = this.uiValueByNomenclatorValueId[model.institutieId];
			if (ObjectUtils.isNullOrUndefined(cachedUiDenumireInstitutie)) {
				if (!ArrayUtils.elementExists(uiInstitutieNomValueIds, model.institutieId)) {
					uiInstitutieNomValueIds.push(model.institutieId);
				}
			}
			if (ObjectUtils.isNotNullOrUndefined(model.membruInstitutieId)) {	
				let cachedUiMembruInstitutie: NomenclatorValueModel = this.nomenclatorValueModelByValueId[model.membruInstitutieId];
				if (ObjectUtils.isNullOrUndefined(cachedUiMembruInstitutie)) {
					if (!ArrayUtils.elementExists(membriNomValueIds, model.membruInstitutieId)) {
						membriNomValueIds.push(model.membruInstitutieId);
					}
				}
			}
		});
		
		this.resolveDenumireInstitutieCache(uiInstitutieNomValueIds, () => {
			this.resolveMembriinstitutieCache(membriNomValueIds, () => {
				this.membriModels.forEach((model: MembruReprezentantiComisieSauGLModel, index: number) => {			
					let view: MembruView = new MembruView();
					view.modelIndex = index;
					let cachedUiDenumireInstitutie: string = this.uiValueByNomenclatorValueId[model.institutieId];
					if (ObjectUtils.isNotNullOrUndefined(cachedUiDenumireInstitutie)) {
						view.denumireInstitutie = cachedUiDenumireInstitutie;
					} else {
						// Nu ar trebui sa ajunga.
						throw new Error("denumire institutie nu exista in cache");
					}
					if (ObjectUtils.isNullOrUndefined(model.membruInstitutieId)) {
						this.mapMembruFromModelToView(view, model);
					} else {
						let cachedUiMembruInstitutie: NomenclatorValueModel = this.nomenclatorValueModelByValueId[model.membruInstitutieId];
						if (ObjectUtils.isNotNullOrUndefined(cachedUiMembruInstitutie)) {
							this.mapMembruFromNomenclatorValueToView(view, cachedUiMembruInstitutie);
						} else {
							// Nu ar trebui sa ajunga.
							throw new Error("membru nu exista in cache");
						}
					}
					view.calitate = this.translateUtils.translateLabel("MEMBRU_REPREZENTANTI_COMISIE_SAU_GL_CALITATE_" + model.calitate);
					view.stare = this.translateUtils.translateLabel("MEMBRU_REPREZENTANTI_COMISIE_SAU_GL_STARE_" + model.stare);

					this.membriViews.push(view);
				});
				
				ListItemUtils.sort(this.membriViews, "denumireInstitutie");
				
				this.dataTable.filter("activ", "stare", "startsWith");
				this.totalRecords = this.membriViews.length;
			});
			
			this.loading = false;
		});
	}

	clearFilters() {
		this.dataFilters = [];
		this.dataTable.reset();
	}

	private resolveDenumireInstitutieCache(nomValueIds: number[], callback: () => void): void {
		if (ArrayUtils.isEmpty(nomValueIds)) {
			callback();
		} else {
			this.nomenclatorService.getUiAttributeValues(nomValueIds, {
				onSuccess: (map: object): void => {
					nomValueIds.forEach((nomValueId: number) => {
						let uiValue: string = map[nomValueId];
						if (ObjectUtils.isNotNullOrUndefined(uiValue)) {
							this.uiValueByNomenclatorValueId[nomValueId] = uiValue;
						}
					});
					callback();
				},
				onFailure: (error: AppError): void => {
					this.messageDisplayer.displayAppError(error);
				}
			});
		}
	}

	private resolveMembriinstitutieCache(membriNomValueIds: number[], callback: () => void): void {
		if (ArrayUtils.isEmpty(membriNomValueIds)) {
			callback();
		} else {
			let counter: number = 0;
			membriNomValueIds.forEach((nomValueId: number) => {
				this.nomenclatorService.getNomenclatorValue(nomValueId, {
					onSuccess: (nomValue: NomenclatorValueModel): void => {
						this.nomenclatorValueModelByValueId[nomValueId] = nomValue;
						counter++;
						if (membriNomValueIds.length === counter) {
							callback();
						}
					},
					onFailure: (error: AppError): void => {
						this.messageDisplayer.displayAppError(error);
					}
				});
			});
		}
	}

	private mapMembruFromNomenclatorValueToView(view: MembruView, nomenclatorValue: NomenclatorValueModel): void {
		view.nume = nomenclatorValue[NomenclatorConstants.PERSOANE_ATTR_KEY_NUME];
		view.prenume = nomenclatorValue[NomenclatorConstants.PERSOANE_ATTR_KEY_PRENUME];
		view.functie = nomenclatorValue[NomenclatorConstants.PERSOANE_ATTR_KEY_FUNCTIE];
		view.departament = nomenclatorValue[NomenclatorConstants.PERSOANE_ATTR_KEY_DEPARTAMENT];
		view.email = nomenclatorValue[NomenclatorConstants.PERSOANE_ATTR_KEY_EMAIL];
		view.telefon = nomenclatorValue[NomenclatorConstants.PERSOANE_ATTR_KEY_TELEFON];
	}

	private mapMembruFromModelToView(view: MembruView, model: MembruReprezentantiComisieSauGLModel): void {
		view.nume = model.nume;
		view.prenume = model.prenume;
		view.functie = model.functie;
		view.departament = model.departament;
		view.email = model.email;
		view.telefon = model.telefon;
	}

	private changePerspective(): void {
		this.addEnabled = !this.readonly;
		this.editEnabled = (!this.readonly && ObjectUtils.isNotNullOrUndefined(this.selectedMembru));
		this.viewEnabled = ObjectUtils.isNotNullOrUndefined(this.selectedMembru);
		this.deleteEnabled = (!this.readonly && ObjectUtils.isNotNullOrUndefined(this.selectedMembru));
	}

	public onAdd(event: any): void {
		this.createExistingMembersByInstitutieAndMembruLists(false);
		this.indexOfModelEditing = null;
		let newMembruWindowInputData: MembruReprezentantiComisieSauGLWindowInputData = new MembruReprezentantiComisieSauGLWindowInputData();
		newMembruWindowInputData.institutieNomenclatorId = this.inputData.institutiiNomenclatorId;
		newMembruWindowInputData.persoaneNomenclatorId = this.inputData.persoaneNomenclatorId;
		this.membruWindowInputData = newMembruWindowInputData;
		this.membruWindowMode = "add";
		this.membruWindowVisible = true;
	}

	private createExistingMembersByInstitutieAndMembruLists(ignoreSelectedMember: boolean) {
		if (ObjectUtils.isNotNullOrUndefined(this.membriModels)) {
			this.existingMembersByInstitutieIdAndMembruId = [];
			this.membriModels.forEach((member, index) => {
				if (!ignoreSelectedMember || index != this.selectedMembru.modelIndex) {
					this.existingMembersByInstitutieIdAndMembruId.push(member.buildKeyFromInstitutieIdAndMembruInstitieId());
				}
			});
		}
		
		if (ObjectUtils.isNotNullOrUndefined(this.membriViews)) {
			this.existingMembersByInstitutieNameAndMembruName = [];
			this.membriViews.forEach(member => {
				if (!ignoreSelectedMember || member.modelIndex != this.selectedMembru.modelIndex) {
					this.existingMembersByInstitutieNameAndMembruName.push(member.buildKeyFromInstitutieDenumireAndMembruNumePrenume());
				}
			});
		}
		

		if (ObjectUtils.isNotNullOrUndefined(this.membriModels)) {
			this.existingMembriModels = [];
			this.membriModels.forEach((member, index) => {
				if (!ignoreSelectedMember || index != this.selectedMembru.modelIndex) {
					this.existingMembriModels.push(member);
				}
			});
		}

	}

	public onEdit(event: any): void {
		this.createExistingMembersByInstitutieAndMembruLists(true);
		this.indexOfModelEditing = this.selectedMembru.modelIndex;
		let newMembruWindowInputData: MembruReprezentantiComisieSauGLWindowInputData = new MembruReprezentantiComisieSauGLWindowInputData();
		newMembruWindowInputData.institutieNomenclatorId = this.inputData.institutiiNomenclatorId;
		newMembruWindowInputData.persoaneNomenclatorId = this.inputData.persoaneNomenclatorId;
		newMembruWindowInputData.membruModel = this.membriModels[this.selectedMembru.modelIndex];
		this.membruWindowInputData = newMembruWindowInputData;
		this.membruWindowMode = "edit";
		this.membruWindowVisible = true;
	}

	public onView(event: any): void {
		let viewMembruWindowInputData: MembruReprezentantiComisieSauGLWindowInputData = new MembruReprezentantiComisieSauGLWindowInputData();
		viewMembruWindowInputData.institutieNomenclatorId = this.inputData.institutiiNomenclatorId;
		viewMembruWindowInputData.persoaneNomenclatorId = this.inputData.persoaneNomenclatorId;
		viewMembruWindowInputData.membruModel = this.membriModels[this.selectedMembru.modelIndex];
		this.membruWindowInputData = viewMembruWindowInputData;
		this.membruWindowMode = "view";
		this.membruWindowVisible = true;
	}

	public onDelete(event: any): void {
		if (ObjectUtils.isNullOrUndefined(this.selectedMembru)) {
			return;
		}
		this.confirmationWindow.confirm({
			approve: (): void => {
				this.confirmationWindow.hide();
				this.membriModels.splice(this.selectedMembru.modelIndex, 1);
				this.selectedMembru = null;
				this.changePerspective();
				this.updateViews();
			}, 
			reject: (): void => {
				this.confirmationWindow.hide();
			}
		}, "DELETE_CONFIRM");
	}

	public onMembruSelected(event: any): void {
		this.changePerspective();
	}

	public onMembruUnselected(event: any): void {
		this.changePerspective();
	}

	public onMembruWindowSaved(membruModel: MembruReprezentantiComisieSauGLModel): void {
		if (this.indexOfModelEditing === null) {
			this.membriModels.push(membruModel);
		} else {
			this.membriModels[this.indexOfModelEditing] = membruModel;
		}	
		this.updateViews();
		this.membruWindowVisible = false;
	}

	public onMembruWindowClosed(): void {
		this.membruWindowVisible = false;
	}

	protected prepareForViewOrEdit(): void {
		if (ObjectUtils.isNotNullOrUndefined(this.inputData.reprezentantiModel)) {
			this.inputData.reprezentantiModel.membri.forEach((membruModel: MembruReprezentantiComisieSauGLModel, index: number) => {
				this.membriModels.push(membruModel);
			});
			this.updateViews();
		}
	}

	public isValid(): boolean {
		return this.validate();
	}

	private validate(): boolean {
		this.messages = [];
		let isValid: boolean = true;
		isValid = ArrayUtils.isNotEmpty(this.membriViews);
		if (!isValid) {
			this.messages.push(Message.createForError("COMMITTEE_OR_GL_HAS_NO_MEMBERS_ADDED"));
		}
		return isValid;
	}

	public populateModel(reprezentantiModel: ReprezentantiComisieSauGLModel): void {
		reprezentantiModel.membri = this.membriModels;
	}

	public getMessages(): Message[] {
		return this.messages;
	}	

	public onExportCSV(): void {
		this.dataTable.exportFilename = ReprezentantiComisieSauGLMembriTabContentComponent.EXPORT_CSV_FILE_NAME.concat(this.inputData.comisieSauGL[NomenclatorConstants.COMISII_SAU_GL_ATTR_KEY_DENUMIRE]);
		this.dataTable.exportCSV();
	}
}

class MembruView {
	
	public modelIndex: number;

	public denumireInstitutie: string;
	public nume: string;
	public prenume: string;
	public functie: string;
	public departament: string;
	public email: string;
	public telefon: string;
	public calitate: string;
	public stare: string;
	
	public buildKeyFromInstitutieDenumireAndMembruNumePrenume(): string {
		return this.denumireInstitutie + "-" + this.nume + "-" + this.prenume;
	}
}