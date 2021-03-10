import { Component, Input, EventEmitter, Output, OnInit, ViewChild } from "@angular/core";
import { BaseWindow, NomenclatorService, NomenclatorConstants, AppError, MessageDisplayer, JoinedNomenclatorUiAttributesValueModel, TranslateUtils, DateConstants, ValueOfNomenclatorValueField, NomenclatorValueFieldComponent } from "@app/shared";
import { SelectItem } from "primeng/primeng";

@Component({
	selector: "app-registru-iesiri-selector-destinatari-filter-window",
	templateUrl: "./registru-iesiri-selector-destinatari-filter-window.html"
})
export class RegistrIesiriSelectorDestinatariFilterWindowComponent extends BaseWindow implements OnInit {

	@Input()
	public windowVisible: Boolean;

	@Output()
	private destinatariFiltered: EventEmitter<any>;
	@Output()
	private windowClosed: EventEmitter<any>;

	@ViewChild(NomenclatorValueFieldComponent)
	public commiteeWgValueFieldSelector: NomenclatorValueFieldComponent;

	private nomenclatorService: NomenclatorService;
	private messageDisplayer: MessageDisplayer;

	public isLoading: Boolean;
	public institutiiSelect: SelectItem[];
	public selectedInstitutii: Array<Number>;
	public selectedInstitutionList: Array<String>;

	public dateFormat: String;

	public departament: String;
	public destinatarNou: String;
	public numarInregistrare: String;
	public dataInregistrare: Date;
	public observatii: String;
	public commiteeWgNomenclatorValue: ValueOfNomenclatorValueField;

	public destinatarNouDisabled: boolean;

	public constructor(nomenclatorService: NomenclatorService, messageDisplayer: MessageDisplayer){
		super();
		this.nomenclatorService = nomenclatorService;
		this.messageDisplayer = messageDisplayer;
		this.init();
	}
	public init(): void{
		this.windowVisible = false;
		this.destinatariFiltered = new EventEmitter<any>();
		this.windowClosed = new EventEmitter<any>();
		this.isLoading  = true;
		this.institutiiSelect = [];
		this.selectedInstitutii = [];
		this.selectedInstitutionList = [];
		this.dateFormat = DateConstants.DATE_FORMAT_FOR_TYPING;
		this.destinatarNouDisabled = false;
	}

	ngOnInit(){
		this.populateInstitutii();
		this.prepareNomenclatorValueFields();
	}

	private populateInstitutii():void {
		this.nomenclatorService.getListOfConcatenatedUiAttributesByNomenclatorCode(NomenclatorConstants.NOMENCLATOR_CODE_INSTITUTII, {
			onSuccess: (nomenclatorValues: JoinedNomenclatorUiAttributesValueModel[]): void => {
				this.buildSelectableList(nomenclatorValues);
				this.isLoading = false;
			},
			onFailure: (error: AppError): void => {
				this.isLoading = false;
				this.messageDisplayer.displayAppError(error);
			}
		});
	}

	private prepareNomenclatorValueFields(): void {
		this.nomenclatorService.getNomenclatorIdByCodeAsMap([NomenclatorConstants.NOMENCLATOR_CODE_COMISII_SAU_GL], {
			onSuccess: (nomenclatorIds): void => {
				this.commiteeWgNomenclatorValue = new ValueOfNomenclatorValueField(nomenclatorIds[NomenclatorConstants.NOMENCLATOR_CODE_COMISII_SAU_GL]);
			}, onFailure: (error: AppError): void => {
				this.messageDisplayer.displayAppError(error);
			}
		});
	}

	private buildSelectableList(institutii: JoinedNomenclatorUiAttributesValueModel[]): void {
		for (let institutie of institutii){
			this.institutiiSelect.push({label: institutie.value, value: institutie.id});
		}
	}

	public onHide(): void{
		this.windowClosed.emit();
	}

	public onShow(): void{
	}

	public onFilterAction(event: any): void{
		let filter: Map<String, any> = new Map();
		filter.set("departament", this.departament);
		filter.set("numarInregistrare", this.numarInregistrare);
		filter.set("dataInregistrare", this.dataInregistrare);
		filter.set("observatii", this.observatii);
		filter.set("comisieGl", this.commiteeWgNomenclatorValue.value);
		filter.set("institutionIds", this.selectedInstitutii);
		filter.set("destinatarNou", this.destinatarNou);
		this.destinatariFiltered.emit(filter);
		this.windowClosed.emit();
	}

	public onClearAllAction(event: any): void{
		this.departament = null;
		this.numarInregistrare = null;
		this.dataInregistrare = null;
		this.observatii = null;
		this.commiteeWgValueFieldSelector.onClearNomenclatorValue();
		this.selectedInstitutii = null;
		this.selectedInstitutionList = [];
		this.destinatarNouDisabled = false;
		this.destinatarNou = null;
	}

	public onSelectedInstitutiiChanged(): void{
		this.selectedInstitutionList = [];
		for (let id of this.selectedInstitutii){
			for (let institutie of this.institutiiSelect){
				if (institutie.value === id){
					this.selectedInstitutionList.push(institutie.label);
				}
			}
		}
		if (this.selectedInstitutii.length === 0){
			this.destinatarNouDisabled = false;
		}else{
			this.destinatarNou = null;
			this.destinatarNouDisabled = true;
		}
	}
}