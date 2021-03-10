import { Component, OnInit, ViewChild } from "@angular/core";
import { FormBuilder, FormGroup, FormControl, Validators } from "@angular/forms";
import { DocumentTypeModel, DateConstants, DateUtils, FormUtils, AderareOioroReportFilterModel, NomenclatorModel, NomenclatorValueAsViewSearchRequestModel, NomenclatorSortedAttribute, NomenclatorSimpleFilter, NomenclatorValueModel, GetNomenclatorValuesRequestModel, NomenclatorFilter, TranslateUtils } from "@app/shared";
import { ArrayUtils, ObjectUtils, AppError, ConfirmationUtils } from "@app/shared";
import { ReportService, MessageDisplayer } from "@app/shared";
import { SelectItem, Column } from "primeng/primeng";
import { AderareOioroReportRowModel, AderareOioroReportModel, JoinedNomenclatorUiAttributesValueModel, NomenclatorService, NomenclatorConstants, DeplasariDeconturiService } from "@app/shared";
import { getLocaleNumberSymbol } from "@angular/common";
import { Table } from "primeng/table";
import { ExportUtils } from "@app/shared/utils/export-utils";
import { ListItemUtils } from "@app/shared/utils/list-item-utils";

@Component({
	selector: "app-aderare-oioro-report",
	templateUrl: "./aderare-oioro-report.component.html"
})
export class AderareOioroReportComponent implements OnInit {
		
	@ViewChild(Table)
    public dataTable: Table;  
	public columns: Column[] = [];	

	private messageDisplayer: MessageDisplayer;
	private confirmationUtils: ConfirmationUtils;
	private reportService: ReportService;
	private nomenclatorService: NomenclatorService;
	private deplasariDeconturiService: DeplasariDeconturiService;
	private translateUtils: TranslateUtils;
	
	public reportVisible: boolean;
	public loading: boolean;

	private formBuilder: FormBuilder;
	public formGroup: FormGroup;

	public anSelectItems: SelectItem[];
	public organismSelectItems: SelectItem[];
	public comitetSelectItems: SelectItem[];
	public institutieSelectItems: SelectItem[];
	public reprezentantSelectItems: SelectItem[];
	public coordonatorArbSelectItems: SelectItem[];

	public reportRows: AderareOioroReportRowModel[]; 
	private years: Array<number>;

	public scrollHeight: string;

	public constructor(reportsService: ReportService, nomenclatorService: NomenclatorService, deplasariDeconturiService: DeplasariDeconturiService, formBuilder: FormBuilder,
			messageDisplayer: MessageDisplayer, confirmationUtils: ConfirmationUtils, translateUtils: TranslateUtils) {
		this.reportService = reportsService;
		this.nomenclatorService = nomenclatorService;
		this.deplasariDeconturiService = deplasariDeconturiService;
		this.formBuilder = formBuilder;
		this.messageDisplayer = messageDisplayer;
		this.confirmationUtils = confirmationUtils;	
		this.translateUtils = translateUtils;	
		this.init();
	}

	private prepareFilterValues(): void {
		this.prepareAnFilterValues();
		this.prepareNomenclatorFilterValues(NomenclatorConstants.NOMENCLATOR_CODE_ORGANISME);
		this.prepareNomenclatorFilterValues(NomenclatorConstants.NOMENCLATOR_CODE_COMITETE);
		this.prepareNomenclatorFilterValues(NomenclatorConstants.NOMENCLATOR_CODE_INSTITUTII);
		this.prepareNomenclatorFilterValues(NomenclatorConstants.NOMENCLATOR_CODE_PERSOANE);
		this.prepareReprezentantAndCoordonatorArbFilterValues();
	}
	
	private prepareAnFilterValues(): void {
		this.nomenclatorService.getYearsFromNomenclatorValuesByNomenclatorCode(NomenclatorConstants.NOMENCLATOR_CODE_DETALII_NUMAR_DEPLASARI_BUGETATE_ORGANISME,
			NomenclatorConstants.DETALII_NUMAR_DEPLASARI_BUGETATE_ORGANISME_KEY_DE_LA_DATA, {
			onSuccess: (years: number[]): void => {
				this.anSelectItems = [];
				years.forEach((year: number) => {
					let yearItem: SelectItem = { label: year.toString(), value: year };
					this.anSelectItems.push(yearItem);
				});		
				
				ListItemUtils.sortByLabel(this.anSelectItems);
			},
			onFailure: (appError: AppError): void => {
				this.messageDisplayer.displayAppError(appError);
			}
		});
	}

	private prepareReprezentantAndCoordonatorArbFilterValues(): void {
		let filterInstitutii: GetNomenclatorValuesRequestModel = this.buildGetNomenclatorValuesRequestModel(NomenclatorConstants.NOMENCLATOR_CODE_INSTITUTII,
			NomenclatorConstants.TIP_INSTITUTII_ATTR_KEY_ABREVIERE, NomenclatorConstants.NOMENCLATOR_INSTITUTII_ATTRIBUTE_ABREVIERE_VALUE_ARB);
		this.nomenclatorService.getNomenclatorValues(filterInstitutii, {
            onSuccess: (institutii: NomenclatorValueModel[]): void => { 
				let institutieId: string = institutii[0].id.toString();
				
				let filterPersoane: GetNomenclatorValuesRequestModel = this.buildGetNomenclatorValuesRequestModel(NomenclatorConstants.NOMENCLATOR_CODE_PERSOANE,
					NomenclatorConstants.PERSOANE_ATTR_KEY_INSTITUTIE, institutieId);
				this.nomenclatorService.getNomenclatorValues(filterPersoane, {
					onSuccess: (persoane: NomenclatorValueModel[]): void => { 
						
						let selectItems: SelectItem[] = [];
						persoane.forEach(persoana => {
							let selectItem: SelectItem = {
								value: persoana.id,
								label: persoana[NomenclatorConstants.PERSOANE_ATTR_KEY_NUME] + " " + persoana[NomenclatorConstants.PERSOANE_ATTR_KEY_PRENUME]
							};
							selectItems.push(selectItem);
						});         
						this.coordonatorArbSelectItems = selectItems;   
						
						ListItemUtils.sortByLabel(this.coordonatorArbSelectItems);    
					},
					onFailure: (appError: AppError) => {
						this.messageDisplayer.displayAppError(appError);
					}
				});         
            },
            onFailure: (appError: AppError) => {
                this.messageDisplayer.displayAppError(appError);
            }
		});	
	}

	private buildGetNomenclatorValuesRequestModel(nomenclatorCode: string, nomenclatorKey: string, nomenclatorKeyValue: string): GetNomenclatorValuesRequestModel {
		let filter: GetNomenclatorValuesRequestModel = new GetNomenclatorValuesRequestModel();
		filter.nomenclatorCode = nomenclatorCode;
		filter.filters = this.buildNomenclatorFilter(nomenclatorKey, nomenclatorKeyValue);
		return filter;
	}

	
	private buildNomenclatorFilter(nomenclatorKey: string, nomenclatorKeyValue: string): NomenclatorFilter[] {
		let filters : NomenclatorFilter[] = [];
		
		let nomenclatorFilter: NomenclatorSimpleFilter = new NomenclatorSimpleFilter();
		nomenclatorFilter.attributeKey = nomenclatorKey;
		nomenclatorFilter.value = nomenclatorKeyValue;

		filters.push(nomenclatorFilter);
		return filters;
	}
	
	private prepareNomenclatorFilterValues(nomenclatorCode : string): void {
		this.nomenclatorService.getListOfConcatenatedUiAttributesByNomenclatorCode(nomenclatorCode, {
			onSuccess: (concatenatedAttributesViewModels: JoinedNomenclatorUiAttributesValueModel[]): void => {
				switch (nomenclatorCode) {
					case NomenclatorConstants.NOMENCLATOR_CODE_ORGANISME: {
						this.organismSelectItems = this.prepareNomenclatorFilterValuesSelectItems(concatenatedAttributesViewModels);
						break;
					}
					case NomenclatorConstants.NOMENCLATOR_CODE_COMITETE: {
						this.comitetSelectItems = this.prepareNomenclatorFilterValuesSelectItems(concatenatedAttributesViewModels);
						break;
					}
					case NomenclatorConstants.NOMENCLATOR_CODE_INSTITUTII: {
						this.institutieSelectItems = this.prepareNomenclatorFilterValuesSelectItems(concatenatedAttributesViewModels);
						break;
					}
					case NomenclatorConstants.NOMENCLATOR_CODE_PERSOANE: {
						this.reprezentantSelectItems = this.prepareNomenclatorFilterValuesSelectItems(concatenatedAttributesViewModels);
						break;
					}
					default: {
						this.messageDisplayer.displayError("unknown columField: " + nomenclatorCode);
					}
				}

				ListItemUtils.sortByLabel(this.organismSelectItems);  
				ListItemUtils.sortByLabel(this.comitetSelectItems);  
				ListItemUtils.sortByLabel(this.institutieSelectItems);  
				ListItemUtils.sortByLabel(this.reprezentantSelectItems);  
				
			},
			onFailure: (appError: AppError): void => {
				this.messageDisplayer.displayAppError(appError);
			}
		});
	}
	
	private prepareNomenclatorFilterValuesSelectItems(concatenatedAttributesViewModels: JoinedNomenclatorUiAttributesValueModel[]): SelectItem[] {
		let selectItems: SelectItem[] = [];

		concatenatedAttributesViewModels.forEach((concatenatedAttributeViewModel: JoinedNomenclatorUiAttributesValueModel) => {
			selectItems.push(this.buildNomenclatorFilterValuesSelectItem(concatenatedAttributeViewModel));
		});
		return selectItems;
	}

	private buildNomenclatorFilterValuesSelectItem(concatenatedAttributeViewModel: JoinedNomenclatorUiAttributesValueModel): SelectItem {
		let selectItem: SelectItem = {
			label: concatenatedAttributeViewModel.value,
			value: concatenatedAttributeViewModel.id
		};
		return selectItem;
	}
		
	private init(): void {
		this.loading = false;
		this.reportVisible = false;
		this.reportRows = [];
		this.formGroup = this.formBuilder.group([]);
		this.scrollHeight = (window.innerHeight - 180) + "px";
		this.prepareFilterValues();;
		this.prepareColumns();
    }

    private prepareColumns(): void {
        this.columns.push(this.buildColumn("an", this.translateUtils.translateLabel("REPORT_ADERARE_OIORO_AN")));
        this.columns.push(this.buildColumn("organism", this.translateUtils.translateLabel("REPORT_ADERARE_OIORO_ORGANISM")));
        this.columns.push(this.buildColumn("abreviere", this.translateUtils.translateLabel("REPORT_ADERARE_OIORO_ABREVIERE")));
        this.columns.push(this.buildColumn("comitet", this.translateUtils.translateLabel("REPORT_ADERARE_OIORO_COMITET")));
        this.columns.push(this.buildColumn("institutie", this.translateUtils.translateLabel("REPORT_ADERARE_OIORO_INSTITUTIE")));
        this.columns.push(this.buildColumn("reprezentant", this.translateUtils.translateLabel("REPORT_ADERARE_OIORO_REPREZENTANT")));
        this.columns.push(this.buildColumn("functie", this.translateUtils.translateLabel("REPORT_ADERARE_OIORO_FUNCTIE")));
        this.columns.push(this.buildColumn("coordonatorArb", this.translateUtils.translateLabel("REPORT_ADERARE_OIORO_COORDONATOR_ARB")));
        this.columns.push(this.buildColumn("nrDeplasariBugetate", this.translateUtils.translateLabel("REPORT_ADERARE_OIORO_NR_DEPLASARI_BUGETATE")));
    }
    
    buildColumn(field: string, header: string): Column {
        let column = new Column();
        column.header =header;
        column.field = field;
        return column;
    }
	
	public ngOnInit(): void {
		this.formGroup.addControl("an", new FormControl());
		this.formGroup.addControl("organism", new FormControl());
		this.formGroup.addControl("abreviere", new FormControl());
		this.formGroup.addControl("comitet", new FormControl());
		this.formGroup.addControl("institutie", new FormControl());
		this.formGroup.addControl("functie", new FormControl());
		this.formGroup.addControl("reprezentant", new FormControl());
		this.formGroup.addControl("coordonatorArb", new FormControl());
	}

	private lock(): void {
		this.loading = true;
	}

	private unlock(): void {
		this.loading = false;
	}

	public onView(): void {
				
		this.lock();
		this.reportVisible = false;
		let reportFilter: AderareOioroReportFilterModel = this.prepareFilterModel();
		this.reportService.getAderareOioroReport(reportFilter, {
			onSuccess: (theReportRows: AderareOioroReportRowModel[]): void => {
				if (ObjectUtils.isNullOrUndefined(theReportRows)) {
					theReportRows = [];
				}
				this.reportRows = theReportRows;		
				this.reportVisible = true;
				this.unlock();
			},
			onFailure: (error: AppError): void => {
				this.unlock();
				this.messageDisplayer.displayAppError(error);
			}
		});
	}

	private prepareFilterModel(): AderareOioroReportFilterModel {
		let filter: AderareOioroReportFilterModel = new AderareOioroReportFilterModel();
		filter.aniList = this.anFormControl.value;
		filter.abreviere = this.abreviereFormControl.value;
		filter.comitetIdList = this.comitetFormControl.value;
		filter.institutieIdList = this.institutieFormControl.value;
		filter.reprezentantIdList = this.reprezentantFormControl.value;
		filter.organismIdList = this.organismFormControl.value;
		filter.functie = this.functieFormControl.value;
		filter.coordonatorArbIdList = this.coordonatorArbFormControl.value;
		
		return filter;
	}

	public onExportExcel(): void {
		alert("not implemented yet");
	}

	public get anFormControl(): FormControl {
		return this.getFormControlByName("an");
	}
	public get organismFormControl(): FormControl {
		return this.getFormControlByName("organism");
	}
	public get abreviereFormControl(): FormControl {
		return this.getFormControlByName("abreviere");
	}
	public get comitetFormControl(): FormControl {
		return this.getFormControlByName("comitet");
	}
	public get institutieFormControl(): FormControl {
		return this.getFormControlByName("institutie");
	}
	public get reprezentantFormControl(): FormControl {
		return this.getFormControlByName("reprezentant");
	}
	public get functieFormControl(): FormControl {
		return this.getFormControlByName("functie");
	}
	public get coordonatorArbFormControl(): FormControl {
		return this.getFormControlByName("coordonatorArb");
	}

	private getFormControlByName(name: string): FormControl {
		return <FormControl> this.formGroup.get(name);
	}
	
	public exportCSV(): void {
		if (ObjectUtils.isNullOrUndefined(this.dataTable)) {
			return;
		}
		this.dataTable.exportFunction = (exportCell: any) => {
			exportCell.data = ExportUtils.replaceQuotationMarkIfStringData( exportCell.data);
			return exportCell.data;
		}
		this.dataTable.exportFilename = this.translateUtils.translateCode("LABELS.REPORT_ADERARE_OIORO");
		this.dataTable.exportCSV();
	}
	
}
