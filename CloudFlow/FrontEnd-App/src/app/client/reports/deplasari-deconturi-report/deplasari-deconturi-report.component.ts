import { Component, OnInit, ViewChild } from "@angular/core";
import { MessageDisplayer, ReportService, DeplasariDeconturiRowModel, DateConstants, DateUtils, ObjectUtils, AppError, ValueOfNomenclatorValueField, TranslateUtils } from "./../../../shared";
import { FormBuilder, FormGroup, FormControl } from "@angular/forms";
import { DeplasariDeconturiReportFilterModel } from "@app/shared/model/reports/deplasari-deconturi-report-filter.model";
import { SelectItem } from "primeng/api";
import { DeplasariDeconturiReportBundleModel } from "@app/shared/model/reports/deplasari-deconturi-report-bundle.model";
import { Table } from "primeng/table";
import { Column } from "primeng/primeng";
import { ExportUtils } from "@app/shared/utils/export-utils";
import { ListItemUtils } from "@app/shared/utils/list-item-utils";

@Component({
    selector: 'app-deplasari-deconturi-report',
    templateUrl: './deplasari-deconturi-report.component.html'
})
export class DeplasariDeconturiReportComponent implements OnInit {

    private readonly EMPTY_REPORT: DeplasariDeconturiRowModel[] = [];

	@ViewChild(Table)
    public dataTable: Table;  
	public columns: Column[] = [];	

    private messageDisplayer: MessageDisplayer;
    private reportService: ReportService;
    private translateUtils: TranslateUtils;

    public reportVisible: boolean;
    public loading: boolean;

    private formBuilder: FormBuilder;
    public formGroup: FormGroup;

    public dateFormat: string;
    public yearRange: string;

    public report: DeplasariDeconturiRowModel[];

    public rowGroupMetadata: any;
    public rowValutaGroupMetadata: any;
    public rowValuta2GroupMetadata: any;

    public institutieSelectItems: SelectItem[];
    public comitetSelectItems: SelectItem[];
    public orasSelectItems: SelectItem[];
    public titularSelectItems: SelectItem[];

    public scrollHeight: string;

    public constructor(reportService: ReportService, formBuilder: FormBuilder, messageDisplayer: MessageDisplayer, translateUtils: TranslateUtils) {

        this.messageDisplayer = messageDisplayer;
        this.reportService = reportService;
        this.formBuilder = formBuilder;
        this.reportService = reportService;
        this.translateUtils = translateUtils;

        this.init();
    }

    private init(): void {
        this.loading = false;
        this.reportVisible = false;
        this.formGroup = this.formBuilder.group([]);
        this.dateFormat = DateConstants.DATE_FORMAT_FOR_TYPING;
        this.yearRange = DateUtils.getDefaultYearRange();
        this.report = this.EMPTY_REPORT;
        this.scrollHeight = (window.innerHeight - 180) + "px";
        this.addFormControls();
        this.prepareSelectItemsDataFromBundle();
        this.prepareColumns();
    }

    private prepareColumns(): void {
        this.columns.push(this.buildColumn("lunaForDisplay", this.translateUtils.translateLabel("REPORT_DEPLASARI_DECONTURI_LUNA")));
        this.columns.push(this.buildColumn("apelativForDisplay", this.translateUtils.translateLabel("REPORT_DEPLASARI_DECONTURI_APELATIV")));
        this.columns.push(this.buildColumn("reprezentantArbOioro", this.translateUtils.translateLabel("REPORT_DEPLASARI_DECONTURI_REPREZENTANT_ARB_OIORO")));
        this.columns.push(this.buildColumn("functia", this.translateUtils.translateLabel("REPORT_DEPLASARI_DECONTURI_FUNCTIA")));
        this.columns.push(this.buildColumn("institutie", this.translateUtils.translateLabel("REPORT_DEPLASARI_DECONTURI_INSTITUTIE")));
        this.columns.push(this.buildColumn("numarDecizie", this.translateUtils.translateLabel("REPORT_DEPLASARI_DECONTURI_NUMAR_DECIZIE")));
        this.columns.push(this.buildColumn("dataDecizie", this.translateUtils.translateLabel("REPORT_DEPLASARI_DECONTURI_DATA_DECIZIE")));
        this.columns.push(this.buildColumn("denumireOrganism", this.translateUtils.translateLabel("REPORT_DEPLASARI_DECONTURI_DENUMIRE_ORGANISM")));
        this.columns.push(this.buildColumn("abreviereOrganism", this.translateUtils.translateLabel("REPORT_DEPLASARI_DECONTURI_ABREVIERE_ORGANISM")));
        this.columns.push(this.buildColumn("comitet", this.translateUtils.translateLabel("REPORT_DEPLASARI_DECONTURI_COMITET")));
        this.columns.push(this.buildColumn("numarDeplasariEfectuate", this.translateUtils.translateLabel("REPORT_DEPLASARI_DECONTURI_NUMAR_DEPLASARI_EFECTUATE")));
        this.columns.push(this.buildColumn("numarDeplasariBugetateRamase", this.translateUtils.translateLabel("REPORT_DEPLASARI_DECONTURI_NUMAR_DEPLASARI_BUGETATE_RAMASE")));
        this.columns.push(this.buildColumn("eveniment", this.translateUtils.translateLabel("REPORT_DEPLASARI_DECONTURI_EVENIMENT")));
        this.columns.push(this.buildColumn("tara", this.translateUtils.translateLabel("REPORT_DEPLASARI_DECONTURI_TARA")));
        this.columns.push(this.buildColumn("oras", this.translateUtils.translateLabel("REPORT_DEPLASARI_DECONTURI_ORAS")));
        this.columns.push(this.buildColumn("dataPlecare", this.translateUtils.translateLabel("REPORT_DEPLASARI_DECONTURI_DATA_PLECARE")));
        this.columns.push(this.buildColumn("dataSosire", this.translateUtils.translateLabel("REPORT_DEPLASARI_DECONTURI_DATA_SOSIRE")));
        this.columns.push(this.buildColumn("dataInceputConferinta", this.translateUtils.translateLabel("REPORT_DEPLASARI_DECONTURI_DATA_INCEPUT_CONFERINTA")));
        this.columns.push(this.buildColumn("dataSfarsitConferinta", this.translateUtils.translateLabel("REPORT_DEPLASARI_DECONTURI_DATA_SFARSIT_CONFERINTA")));
        this.columns.push(this.buildColumn("numarNopti", this.translateUtils.translateLabel("REPORT_DEPLASARI_DECONTURI_NUMAR_NOPTI")));
        this.columns.push(this.buildColumn("saTransmisMinutaIntalniriiForDisplay", this.translateUtils.translateLabel("REPORT_DEPLASARI_DECONTURI_SA_TRANSMIS_MINUTA_INTALITII")));
        this.columns.push(this.buildColumn("observatii", this.translateUtils.translateLabel("REPORT_DEPLASARI_DECONTURI_OBSERVATII")));
        this.columns.push(this.buildColumn("titularDecont", this.translateUtils.translateLabel("REPORT_DEPLASARI_DECONTURI_TITULAR_DECONT")));
        this.columns.push(this.buildColumn("tipDecont", this.translateUtils.translateLabel("REPORT_DEPLASARI_DECONTURI_TIP_DECONT")));
        this.columns.push(this.buildColumn("numarDecont", this.translateUtils.translateLabel("REPORT_DEPLASARI_DECONTURI_NUMAR_DECONT")));
        this.columns.push(this.buildColumn("dataDecont", this.translateUtils.translateLabel("REPORT_DEPLASARI_DECONTURI_DATA_DECONT")));
        this.columns.push(this.buildColumn("valutaForDisplay", this.translateUtils.translateLabel("REPORT_DEPLASARI_DECONTURI_VALUTA")));
        this.columns.push(this.buildColumn("cursValutar", this.translateUtils.translateLabel("REPORT_DEPLASARI_DECONTURI_CURS_VALUTAR")));
        this.columns.push(this.buildColumn("numarDocumentJustificativ", this.translateUtils.translateLabel("REPORT_DEPLASARI_DECONTURI_NUMAR_DOCUMENT_JUSTIFICATIV")));
        this.columns.push(this.buildColumn("dataDocumentJustificativ", this.translateUtils.translateLabel("REPORT_DEPLASARI_DECONTURI_DATA_DOCUMENT_JUSTIFICATIV")));
        this.columns.push(this.buildColumn("cazare", this.translateUtils.translateLabel("REPORT_DEPLASARI_DECONTURI_CAZARE")));
        this.columns.push(this.buildColumn("biletDeAvion", this.translateUtils.translateLabel("REPORT_DEPLASARI_DECONTURI_BILET_DE_AVION")));
        this.columns.push(this.buildColumn("asiguareMedicala", this.translateUtils.translateLabel("REPORT_DEPLASARI_DECONTURI_ASIGURARE_MEDICALA")));
        this.columns.push(this.buildColumn("transferAeroport", this.translateUtils.translateLabel("REPORT_DEPLASARI_DECONTURI_TRANSFER_AEROPORT")));
        this.columns.push(this.buildColumn("alteCheltuieli", this.translateUtils.translateLabel("REPORT_DEPLASARI_DECONTURI_ALTE_CHELTUIELI")));
        this.columns.push(this.buildColumn("totalCheltuieliPerValuta", this.translateUtils.translateLabel("REPORT_DEPLASARI_DECONTURI_TOTAL_CHELTUIELI_PER_VALUTA")));
        this.columns.push(this.buildColumn("totalCheltuieliInRON", this.translateUtils.translateLabel("REPORT_DEPLASARI_DECONTURI_TOTAL_CHELTUIELI_IN_RON")));
        this.columns.push(this.buildColumn("valuta2ForDisplay", this.translateUtils.translateLabel("REPORT_DEPLASARI_DECONTURI_VALUTA")));
        this.columns.push(this.buildColumn("cursValutar2", this.translateUtils.translateLabel("REPORT_DEPLASARI_DECONTURI_CURS_VALUTAR")));
        this.columns.push(this.buildColumn("numarDocumentJustificativ2", this.translateUtils.translateLabel("REPORT_DEPLASARI_DECONTURI_NUMAR_DOCUMENT_JUSTIFICATIV")));
        this.columns.push(this.buildColumn("dataDocumentJustificativ2", this.translateUtils.translateLabel("REPORT_DEPLASARI_DECONTURI_DATA_DOCUMENT_JUSTIFICATIV")));
        this.columns.push(this.buildColumn("cazare2", this.translateUtils.translateLabel("REPORT_DEPLASARI_DECONTURI_CAZARE")));
        this.columns.push(this.buildColumn("biletDeAvion2", this.translateUtils.translateLabel("REPORT_DEPLASARI_DECONTURI_BILET_DE_AVION")));
        this.columns.push(this.buildColumn("taxiTrenMetrou", this.translateUtils.translateLabel("REPORT_DEPLASARI_DECONTURI_TAXI_TREN_METROU")));
        this.columns.push(this.buildColumn("comisionUtilizareCard", this.translateUtils.translateLabel("REPORT_DEPLASARI_DECONTURI_COMISION_UTILIZARE_CARD")));
        this.columns.push(this.buildColumn("alteCheltuieli2", this.translateUtils.translateLabel("REPORT_DEPLASARI_DECONTURI_ALTE_CHELTUIELI")));
        this.columns.push(this.buildColumn("totalCheltuieliPerValuta2", this.translateUtils.translateLabel("REPORT_DEPLASARI_DECONTURI_TOTAL_CHELTUIELI_PER_VALUTA")));
        this.columns.push(this.buildColumn("diurnaZilnica", this.translateUtils.translateLabel("REPORT_DEPLASARI_DECONTURI_DIURNA_ZILNICA")));
        this.columns.push(this.buildColumn("numarZile", this.translateUtils.translateLabel("REPORT_DEPLASARI_DECONTURI_NUMAR_ZILE")));
        this.columns.push(this.buildColumn("totalDiurnaInRON", this.translateUtils.translateLabel("REPORT_DEPLASARI_DECONTURI_TOTAL_DIURNA_IN_RON")));
        this.columns.push(this.buildColumn("avans", this.translateUtils.translateLabel("REPORT_DEPLASARI_DECONTURI_AVANS")));
        this.columns.push(this.buildColumn("totalCheltuieliInRON2", this.translateUtils.translateLabel("REPORT_DEPLASARI_DECONTURI_TOTAL_CHELTUIELI_IN_RON")));
        this.columns.push(this.buildColumn("totalDeIncasatDeRestituitInRON", this.translateUtils.translateLabel("REPORT_DEPLASARI_DECONTURI_TOTAL_DE_INCASAT_RESTITUIT_IN_RON")));
    }
    
    buildColumn(field: string, header: string): Column {
        let column = new Column();
        column.header =header;
        column.field = field;
        return column;
    }

    public ngOnInit(): void {
    }

    private lock(): void {
        this.loading = true;
    }

    public onView(): void {

        this.lock();
        this.reportVisible = true;
       
        let reportFilter: DeplasariDeconturiReportFilterModel =  this.prepareFilterModel();

        this.reportService.getDeplasariDeconturiReport(reportFilter, {
            onSuccess: (theReport: DeplasariDeconturiRowModel[]): void => {
                this.report = theReport;
                this.reportVisible = true;
                this.updateRowGroupMetaData();
                this.unlock();
            },
            onFailure: (error: AppError): void => {
                this.unlock();
                this.messageDisplayer.displayAppError(error);
            }
        });
    }

    private unlock(): void {
        this.loading = false;
    }

    public onExportExcel(): void {
        alert("not implemented yet");
    }

    public get footerVisible(): boolean {
        return ObjectUtils.isNotNullOrUndefined(this.report);
    }

    private getFormControlByName(name: string): FormControl {
        return <FormControl>this.formGroup.get(name);
    }
    public updateRowGroupMetaData(): void {
        this.rowGroupMetadata = {};

        let reportRows = this.report;
        let referenceIndex: number= 0;

        if (reportRows) {
            reportRows.forEach((row, index) => {
                let numarDecont = row.numarDecont;
                let dataDecont = row.dataDecont;
                if (index == 0) {
                    this.rowGroupMetadata[index] = { index: 0, size: 1 };
                } else {
                    let previousRow = reportRows[index - 1];
                    let previousRowNumarDecont = previousRow.numarDecont;
                    let previousRowDataDecont = previousRow.dataDecont;

                    if ((numarDecont === previousRowNumarDecont) && (dataDecont.getTime() === previousRowDataDecont.getTime())) {
                        this.rowGroupMetadata[referenceIndex].size++;
                        this.rowGroupMetadata[index] = { index: 0, size: 1 };
                    } else {
                        referenceIndex = index;
                        this.rowGroupMetadata[referenceIndex] = { index: index, size: 1 };
                    }

                }
            });
        }
        this.updateRowValutaGroupMetadata();
        this.updateRowValuta2GroupMetadata();
    }

    public updateRowValutaGroupMetadata(): void {
        this.rowValutaGroupMetadata = {};

        let reportRows = this.report;
        let referenceIndex: number= 0;

        if (reportRows) {
            reportRows.forEach((row, index) => {
                let valuta = row.valuta;
                if (index == 0) {
                    this.rowValutaGroupMetadata[index] = { index: 0, size: 1 };
                } else {
                    let previousRow = reportRows[index - 1];
                    let previousRowValuta = previousRow.valuta;

                    if (valuta === previousRowValuta) {
                        this.rowValutaGroupMetadata[referenceIndex].size++;
                        this.rowValutaGroupMetadata[index] = { index: 0, size: 1 };
                    } else {
                        referenceIndex = index;
                        this.rowValutaGroupMetadata[referenceIndex] = { index: index, size: 1 };
                    }

                }
            });
        }
        
    }

    public updateRowValuta2GroupMetadata(): void {
        this.rowValuta2GroupMetadata = {};

        let reportRows = this.report;
        let referenceIndex: number= 0;

        if (reportRows) {
            reportRows.forEach((row, index) => {
                let valuta = row.valuta2;
                if (index == 0) {
                    this.rowValuta2GroupMetadata[index] = { index: 0, size: 1 };
                } else {
                    let previousRow = reportRows[index - 1];
                    let previousRowValuta = previousRow.valuta2;

                    if (valuta === previousRowValuta) {
                        this.rowValuta2GroupMetadata[referenceIndex].size++;
                        this.rowValuta2GroupMetadata[index] = { index: 0, size: 1 };
                    } else {
                        referenceIndex = index;
                        this.rowValuta2GroupMetadata[referenceIndex] = { index: index, size: 1 };
                    }

                }
            });
        }
    }

    private prepareSelectItemsDataFromBundle() {
        this.reportService.getDeplasariDeconturiReportBundle({
            onSuccess: (bundle: DeplasariDeconturiReportBundleModel): void => {
                this.populateDDLFromBundle(bundle);
            },
            onFailure: (appError: AppError): void => {
                this.messageDisplayer.displayAppError(appError);
            }
        });

    }
    private populateDDLFromBundle(bundle: DeplasariDeconturiReportBundleModel): void {

        this.institutieSelectItems = [];
        this.comitetSelectItems = [];
        this.orasSelectItems = [];
        this.titularSelectItems = [];

        if (ObjectUtils.isNotNullOrUndefined(bundle)) {
            if (ObjectUtils.isNotNullOrUndefined(bundle.denumiriInstitutii)) {
                bundle.denumiriInstitutii.forEach(element => {
                    this.institutieSelectItems.push(element);
                });
            }
            if (ObjectUtils.isNotNullOrUndefined(bundle.denumiriComiteteList)) {
                bundle.denumiriComiteteList.forEach(element => {
                    this.comitetSelectItems.push(element);
                });
            }
            if (ObjectUtils.isNotNullOrUndefined(bundle.oraseList)) {
                bundle.oraseList.forEach(element => {
                    this.orasSelectItems.push(element);
                });
            }
            if (ObjectUtils.isNotNullOrUndefined(bundle.titulariDeconturi)) {
                bundle.titulariDeconturi.forEach(element => {
                    this.titularSelectItems.push(element);
                });
            }
        }

        ListItemUtils.sortByLabel(this.institutieSelectItems);
        ListItemUtils.sortByLabel(this.comitetSelectItems);
        ListItemUtils.sortByLabel(this.orasSelectItems);
        ListItemUtils.sortByLabel(this.titularSelectItems);
        
        this.reprezentantFormControl.setValue(new ValueOfNomenclatorValueField(bundle.nomenclatorReprezentatOrgId));
        this.organismFormControl.setValue(new ValueOfNomenclatorValueField(bundle.nomenclatorOrganismeId));

    }
    private prepareFilterModel(): DeplasariDeconturiReportFilterModel {

        let filter: DeplasariDeconturiReportFilterModel = new DeplasariDeconturiReportFilterModel();

        if (ObjectUtils.isNotNullOrUndefined(this.dataPlecareDeLaFormControl.value)) {
            filter.dataPlecareDeLa = this.dataPlecareDeLaFormControl.value;
        }
        if (ObjectUtils.isNotNullOrUndefined(this.dataPlecarePanaLaFormControl.value)) {
            filter.dataPlecarePanaLa = this.dataPlecarePanaLaFormControl.value;
        }
        if (ObjectUtils.isNotNullOrUndefined(this.dataSosireDeLaFormControl.value)) {
            filter.dataSosireDeLa = this.dataSosireDeLaFormControl.value;
        }
        if (ObjectUtils.isNotNullOrUndefined(this.dataSosirePanaLaFormControl.value)) {
            filter.dataSosirePanaLa = this.dataSosirePanaLaFormControl.value;
        }
        if (ObjectUtils.isNotNullOrUndefined(this.orasFormControl.value)) {
        	filter.oras = this.orasFormControl.value;
        }
        if (ObjectUtils.isNotNullOrUndefined(this.organismFormControl.value.value)) {
        	filter.organismId = this.organismFormControl.value.value;
        }
        if (ObjectUtils.isNotNullOrUndefined(this.reprezentantFormControl.value.value)) {
        	filter.reprezentantId = this.reprezentantFormControl.value.value;
        }
        if (ObjectUtils.isNotNullOrUndefined(this.titularFormControl.value)) {
        	filter.titularDecont = this.titularFormControl.value;
        }
        if (ObjectUtils.isNotNullOrUndefined(this.institutieFormControl.value)) {
        	filter.denumireInstitutie = this.institutieFormControl.value;
        }

        return filter;
    }

    private addFormControls(): void {
        this.formGroup.addControl("institutie", new FormControl());
        this.formGroup.addControl("reprezentant", new FormControl());
        this.formGroup.addControl("organism", new FormControl());
        this.formGroup.addControl("comitet", new FormControl());
        this.formGroup.addControl("oras", new FormControl());
        this.formGroup.addControl("dataPlecareDeLa", new FormControl());
        this.formGroup.addControl("dataSosireDeLa", new FormControl());
        this.formGroup.addControl("dataPlecarePanaLa", new FormControl());
        this.formGroup.addControl("dataSosirePanaLa", new FormControl());
        this.formGroup.addControl("titular", new FormControl());
    }

    private get institutieFormControl(): FormControl {
        return this.getFormControlByName("institutie");
    }
    private get reprezentantFormControl(): FormControl {
        return this.getFormControlByName("reprezentant");
    }
    private get organismFormControl(): FormControl {
        return this.getFormControlByName("organism");
    }
    private get comitetFormControl(): FormControl {
        return this.getFormControlByName("comitet");
    }
    private get orasFormControl(): FormControl {
        return this.getFormControlByName("oras");
    }
    private get dataPlecareDeLaFormControl(): FormControl {
        return this.getFormControlByName("dataPlecareDeLa");
    }
    private get dataPlecarePanaLaFormControl(): FormControl {
        return this.getFormControlByName("dataPlecarePanaLa");
    }
    private get dataSosireDeLaFormControl(): FormControl {
        return this.getFormControlByName("dataSosireDeLa");
    }
    private get dataSosirePanaLaFormControl(): FormControl {
        return this.getFormControlByName("dataSosirePanaLa");
    }
    private get titularFormControl(): FormControl {
        return this.getFormControlByName("titular");
    }

    public exportCSV(): void {
		if (ObjectUtils.isNullOrUndefined(this.dataTable)) {
			return;
		}
		this.dataTable.exportFunction = (exportCell: any) => {
			exportCell.data = ExportUtils.replaceQuotationMarkIfStringData( exportCell.data);
		    if (exportCell.field === "dataDecizie" 
                || exportCell.field === "dataDecont"
                || exportCell.field === "dataDocumentJustificativ"
                || exportCell.field === "dataDocumentJustificativ2" ) {
				return DateUtils.formatForDisplay(exportCell.data);
			} else if (exportCell.field === "lunaForDisplay"
                || exportCell.field === "apelativForDisplay"
                || exportCell.field === "saTransmisMinutaIntalniriiForDisplay"
                || exportCell.field === "valutaForDisplay"
                || exportCell.field === "valuta2ForDisplay" ) {
				return this.translateUtils.translateCode(exportCell.data);
			} else if (exportCell.field === "dataPlecare"
                || exportCell.field === "dataSosire"
                || exportCell.field === "dataInceputConferinta"
                || exportCell.field === "dataSfarsitConferinta" ) {
                return DateUtils.formatDateTimeForDisplay(exportCell.data);
            } else if (exportCell.field === "cursValutar"
                || exportCell.field === "totalCheltuieliInRON"
                || exportCell.field === "cursValutar2"
                || exportCell.field === "totalDiurnaInRON"
                || exportCell.field === "avans"
                || exportCell.field === "totalCheltuieliInRON2"
                || exportCell.field === "totalDeIncasatDeRestituitInRON" ) {
                return exportCell.data.toFixed(2);
            } else {
				return exportCell.data;
			}
		}
		this.dataTable.exportFilename = this.translateUtils.translateCode("LABELS.REPORT_DEPLASARI_DECONTURI");
		this.dataTable.exportCSV();
	}
}