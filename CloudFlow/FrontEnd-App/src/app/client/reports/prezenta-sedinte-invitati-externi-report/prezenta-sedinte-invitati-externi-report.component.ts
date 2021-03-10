import { Component, OnInit, ViewChild } from "@angular/core";
import { PrezentaSedinteCdPvgInvitatiExterniReportModel, MessageDisplayer, ReportService, PrezentaSedinteCdPvgInvitatiExterniReportFilterModel, AppError, ObjectUtils, FormUtils, DateConstants, DateUtils, NomenclatorService, NomenclatorConstants, NomenclatorValueModel, StringUtils, TranslateUtils } from "../../../shared";
import { FormBuilder, FormGroup, FormControl, Validators } from "@angular/forms";
import { ReportConstants } from "../constants/report.constants";
import { SelectItem } from "primeng/api";
import { Table } from "primeng/table";
import { Column } from "primeng/primeng";
import { ExportUtils } from "@app/shared/utils/export-utils";
import { ListItemUtils } from "@app/shared/utils/list-item-utils";

@Component({
    selector: "app-prezenta-sedinte-invitati-externi-report",
    templateUrl: "./prezenta-sedinte-invitati-externi-report.component.html"
})
export class PrezentaSedinteInvitatiExterniReportComponent implements OnInit {

    private readonly EMPTY_REPORT: PrezentaSedinteCdPvgInvitatiExterniReportModel = new PrezentaSedinteCdPvgInvitatiExterniReportModel();
   
    @ViewChild(Table)
    public dataTable: Table;  
    public columns: Column[] = [];	
    
    private messageDisplayer: MessageDisplayer;
    private reportService: ReportService;
    private nomenclatorService: NomenclatorService;
    private translateUtils: TranslateUtils;

    public reportVisible: boolean;
    public loading: boolean;

    private formBuilder: FormBuilder;
    public formGroup: FormGroup;

    public dateFormat: string;
    public yearRange: string;

    public totalMembrii: number;
    public report: PrezentaSedinteCdPvgInvitatiExterniReportModel;
    public rowGroupMetadata: any;

    public institutieInvitatSelectItems: SelectItem[];
    public invitatAcreditatSelectItems: SelectItem[];

    public isInvitatAcreditat: boolean = true;

    public scrollHeight: string;

    public constructor(reportService: ReportService, formBuilder: FormBuilder, messageDisplayer: MessageDisplayer,
        nomenclatorService: NomenclatorService, translateUtils: TranslateUtils) {

        this.reportService = reportService;
        this.formBuilder = formBuilder;
        this.messageDisplayer = messageDisplayer;
        this.nomenclatorService = nomenclatorService;
        this.translateUtils = translateUtils;

        this.init();
    }

    private prepareInstitutieInvitatSelectItems(): void {
        this.institutieInvitatSelectItems = [];

        this.nomenclatorService.getNomenclatorValuesByNomenclatorCode(NomenclatorConstants.NOMENCLATOR_CODE_INSTITUTII, {
            onSuccess: (institutii: NomenclatorValueModel[]): void => {

                institutii.forEach(institutie => {
                    let id: string = institutie.id.toString();
                    let nume: string = institutie[NomenclatorConstants.INSITUTII_ATTR_KEY_DENUMIRE_INSTITUTIE];

                    this.institutieInvitatSelectItems.push({label: nume, value: id});
                });

                ListItemUtils.sortByLabel(this.institutieInvitatSelectItems);
            },
            onFailure: (appError: AppError) => {
                this.messageDisplayer.displayAppError(appError);
            }
        });
    }

    private prepareInvitatAcreditatSelectItems(): void {
        this.invitatAcreditatSelectItems = [];

        this.nomenclatorService.getNomenclatorValuesByNomenclatorCode(NomenclatorConstants.NOMENCLATOR_CODE_PERSOANE, {
            onSuccess: (persoane: NomenclatorValueModel[]): void => {
                
                persoane.forEach(persoana => {
                    let id: string = persoana.id.toString();
                    let nume: string = persoana[NomenclatorConstants.PERSOANE_ATTR_KEY_NUME];
                    let prenume: string = persoana[NomenclatorConstants.PERSOANE_ATTR_KEY_PRENUME];
                    let numeForDisplay: string = nume + " " + prenume;

                    this.invitatAcreditatSelectItems.push({ label: numeForDisplay, value: id });
                });

                ListItemUtils.sortByLabel(this.invitatAcreditatSelectItems);
            },
            onFailure: (appError: AppError) => {
                this.messageDisplayer.displayAppError(appError);
            }
        });
    }

    private init(): void {
        this.loading = false;
        this.reportVisible = false;
        this.dateFormat = DateConstants.DATE_FORMAT_FOR_TYPING;
        this.yearRange = DateUtils.getDefaultYearRange();
        this.report = this.EMPTY_REPORT;
        this.scrollHeight = (window.innerHeight - 180) + "px";
        this.prepareInstitutieInvitatSelectItems();
        this.prepareInvitatAcreditatSelectItems();
        this.prepareColumns();
    }

    private prepareColumns(): any {
        this.columns.push(this.buildColumn("tipSedinta", this.translateUtils.translateCode("LABELS.REPORT_PREZENTA_SEDINTE_INVITATI_EXTERNI_TIP_SEDINTA")));
        this.columns.push(this.buildColumn("dataSedintaForDisplay", this.translateUtils.translateCode("LABELS.REPORT_PREZENTA_SEDINTE_INVITATI_EXTERNI_DATA_SEDINTA")));
        this.columns.push(this.buildColumn("institutieInvitat", this.translateUtils.translateCode("LABELS.REPORT_PREZENTA_SEDINTE_INVITATI_EXTERNI_INSTITUTIE_INVITAT")));
        this.columns.push(this.buildColumn("invitatAcreditat", this.translateUtils.translateCode("LABELS.REPORT_PREZENTA_SEDINTE_INVITATI_EXTERNI_INVITAT_ACREDITAT")));
        this.columns.push(this.buildColumn("invitatInlocuitorForDisplay", this.translateUtils.translateCode("LABELS.REPORT_PREZENTA_SEDINTE_INVITATI_EXTERNI_INVITAT_INLOCUITOR")));
    }
    
    buildColumn(field: string, header: string): Column {
        let column = new Column();
        column.header =header;
        column.field = field;
        return column;
    }

    public ngOnInit(): void {
        this.formGroup = this.formBuilder.group([]);
        this.formGroup.addControl("tipSedinta", new FormControl(ReportConstants.DOCUMENT_PREZENTA_CD_PVG_TIP_SEDINTA_CD_VALUE));
        this.formGroup.addControl("institutieInvitat", new FormControl());
        this.formGroup.addControl("invitatAcreditat", new FormControl(true));
        this.formGroup.addControl("invitatAcreditatNume", new FormControl());
        this.formGroup.addControl("invitatInlocuitorNume", new FormControl());
        this.formGroup.addControl("invitatInlocuitorPrenume", new FormControl());
        this.formGroup.addControl("dataSedintaDeLa", new FormControl(null, [Validators.required]));
        this.formGroup.addControl("dataSedintaPanaLa", new FormControl(null, [Validators.required]));
    }

    private lock(): void {
        this.loading = true;
    }

    private unlock(): void {
        this.loading = false;
    }

    public onView(): void {
        if(!this.isFilterValid()) {
            return;
        }

        this.lock();
        this.reportVisible = true;

        let reportFilter: PrezentaSedinteCdPvgInvitatiExterniReportFilterModel = this.prepareFilterModel();

        this.reportService.getPrezentaSedinteCdPvgInvitatiExterniReport(reportFilter, {
            onSuccess: (report: PrezentaSedinteCdPvgInvitatiExterniReportModel): void => {

                this.report = report;
                
                this.reportVisible = true;
                this.unlock();    
            },
            onFailure: (error: AppError): void => {
                this.unlock();
                this.messageDisplayer.displayAppError(error);
            }
        })
    }

    private isFilterValid(): boolean {
        FormUtils.validateAllFormFields(this.formGroup);

        return this.formGroup.valid;
    }

    public onChangeTipInvitat(event: any): void {
		this.isInvitatAcreditat = event;
	}

    public prepareFilterModel(): PrezentaSedinteCdPvgInvitatiExterniReportFilterModel {
        let filter: PrezentaSedinteCdPvgInvitatiExterniReportFilterModel = new PrezentaSedinteCdPvgInvitatiExterniReportFilterModel();
        
        filter.tipSedinta = this.tipSedintaFormControl.value;
        filter.dataSedintaDeLa = this.dataSedintaDeLaFormControl.value;
        filter.dataSedintaPanaLa = this.dataSedintaPanaLaFormControl.value;
        filter.institutieInvitatId = Number.parseInt(this.institutieInvitatFormControl.value);
        if (this.invitatAcreditatFormControl.value) { 
            filter.invitatAcreditatId = Number.parseInt(this.invitatAcreditatNumeFormControl.value);
            filter.invitatInlocuitorNume = null;
            filter.invitatInlocuitorPrenume = null;
        } else {
            filter.invitatAcreditatId = null; 
            if (StringUtils.isNotBlank(this.invitatInlocuitorNumeFormControl.value) ) {
				filter.invitatInlocuitorNume = this.invitatInlocuitorNumeFormControl.value;
			}
			if (StringUtils.isNotBlank(this.invitatInlocuitorPrenumeFormControl.value) ) {
				filter.invitatInlocuitorPrenume = this.invitatInlocuitorPrenumeFormControl.value;
			}
        }

        return filter;
    }

    public onExportExcel(): void {
        alert("not implemented yet");
    }

    public get footerVisible(): boolean {
        return ObjectUtils.isNotNullOrUndefined(this.report.rows);
    }

    public get tipSedintaFormControl(): FormControl {
        return this.getFormControlByName("tipSedinta");
    }

    public get dataSedintaDeLaFormControl(): FormControl {
        return this.getFormControlByName("dataSedintaDeLa");
    }

    public get dataSedintaPanaLaFormControl(): FormControl {
        return this.getFormControlByName("dataSedintaPanaLa");
    }

    public get institutieInvitatFormControl(): FormControl {
        return this.getFormControlByName("institutieInvitat");
    }

    public get invitatAcreditatFormControl(): FormControl {
        return this.getFormControlByName("invitatAcreditat");
    }

    public get invitatAcreditatNumeFormControl(): FormControl {
        return this.getFormControlByName("invitatAcreditatNume");
    }

    public get invitatInlocuitorNumeFormControl(): FormControl {
        return this.getFormControlByName("invitatInlocuitorNume");
    }

    public get invitatInlocuitorPrenumeFormControl(): FormControl {
        return this.getFormControlByName("invitatInlocuitorPrenume");
    }

    private getFormControlByName(name: string): FormControl {
        return <FormControl> this.formGroup.get(name);
    }

    public onSort(): void {
        this.updateRowGroupMetaData();
    }

    public updateRowGroupMetaData(): void {
        this.rowGroupMetadata = {};

        let reportRows = this.report.rows;

        if(reportRows) {
            reportRows.forEach((row, index) => {
                let tipSedinta = row.tipSedinta;
                let dataSedinta = row.dataSedintaForDisplay;

                if(index == 0) {
                    this.rowGroupMetadata[tipSedinta] = { index: 0, size: 1 };
                } else {
                    let previousTipSedinta = reportRows[index - 1];
                    let previousRowGroupTipSedinta = previousTipSedinta.tipSedinta;

                    if(tipSedinta === previousRowGroupTipSedinta) {
                        this.rowGroupMetadata[tipSedinta].size++;
                    } else {
                        this.rowGroupMetadata[tipSedinta] = { index: index, size: 1 };
                    }
                }

                if(index == 0) {
                    this.rowGroupMetadata[dataSedinta] = { index: 0, size: 1 };
                } else {
                    let previousDataSedinta = reportRows[index - 1];
                    let previousRowGroupDataSedintaForDisplay = previousDataSedinta.dataSedintaForDisplay;

                    if(dataSedinta === previousRowGroupDataSedintaForDisplay) {
                        this.rowGroupMetadata[dataSedinta].size++;
                    } else {
                        this.rowGroupMetadata[dataSedinta] = { index: index, size: 1 };
                    }
                }
            });
        }
    }

    public exportCSV(): void {
		if (ObjectUtils.isNullOrUndefined(this.dataTable)) {
			return;
		}
		this.dataTable.exportFunction = (exportCell: any) => {
			exportCell.data = ExportUtils.replaceQuotationMarkIfStringData( exportCell.data);
        }
        this.dataTable.exportFilename = this.translateUtils.translateCode("LABELS.REPORT_PREZENTA_SEDINTE_INVITATI_EXTERNI");
		this.dataTable.exportCSV();
	}
}