import { Injectable } from "@angular/core";
import { ApiCaller } from "../api-caller";
import { AsyncCallback } from "../async-callback";
import { ApiPathConstants } from "../constants/api-path.constants";
import { AppError } from "../model/app-error";
import { DeplasariDeconturiReportBundleModel } from "../model/reports/deplasari-deconturi-report-bundle.model";
import { DeplasariDeconturiReportFilterModel } from "../model/reports/deplasari-deconturi-report-filter.model";
import { ParticipareReprezentantiArbLaActiuniInAfaraAsociatieiReportFilterModel } from "../model/reports/participare-reprezentanti-arb-la-actiuni-in-afara-asociatiei-report-filter.model";
import { ParticipareReprezentantiArbLaActiuniInAfaraAsociatieiReportModel } from "../model/reports/participare-reprezentanti-arb-la-actiuni-in-afara-asociatiei-report.model";
import { PrezentaReprezentativitateReportFilterModel } from "../model/reports/prezenta-reprezentativitate-report-filter.model";
import { PrezentaReprezentativitateReportModel } from "../model/reports/prezenta-reprezentativitate-report.model";
import { RaspunsuriBanciReportBundleModel } from "../model/reports/raspunsuri-banci-report-bundle.model";
import { RaspunsuriBanciReportFilterModel } from "../model/reports/raspunsuri-banci-report-filter.model";
import { RaspunsuriBanciReportRowModel } from "../model/reports/raspunsuri-banci-report-row.model.";
import { ActiuniOrganizateDeArbReportFilterModel, ActiuniOrganizateDeArbReportModel, ActiuniPeProiectReportFilterModel, ActiuniPeProiectReportModel, AderareOioroReportFilterModel, AderareOioroReportRowModel, CentralizatorPrezentaPerioadaReportFilterModel, CentralizatorPrezentaPerioadaReportModel, CereriConcediuReportBundle, CereriConcediuReportFilterModel, CereriConcediuReportModel, CheltuieliArbSiReprezentantArbReportFilterModel, CheltuieliArbSiReprezentantArbReportModel, DashboardProiecteReportBundle, DashboardProiecteReportFilterModel, DashboardProiecteReportRowModel, DecontCheltuieliAlteDeconturiReportFilterModel, DecontCheltuieliAlteDeconturiReportModel, DeplasariDeconturiRowModel, DeplDecontCheltReprArbReportFilterModel, DeplDecontCheltReprArbReportModel, DocumenteTrimiseDeArbReportFilterModel, DocumenteTrimiseDeArbReportModel, DocumentIdentifierModel, ListaProiectelorCareAuVizatActiunileLuniiReportFilterModel, ListaProiectelorCareAuVizatActiunileLuniiReportModel, MembriiAfiliatiReportFilterModel, MembriiAfiliatiReportModel, NotaGeneralaPeMembriiArbReportFilterModel, NotaGeneralaPeMembriiArbReportModel, NumarParticipantiSedinteComisieGlReportBundle, NumarParticipantiSedinteComisieGlReportFilterModel, NumarParticipantiSedinteComisieGlReportModel, NumarSedinteComisieGlReportFilterModel, NumarSedinteComisieGlReportModel, NumarSedinteSiParticipantiReportFilterModel, NumarSedinteSiParticipantiReportModel, ParticipariLaEvenimenteReportFilterModel, ParticipariLaEvenimenteReportRowModel, PrezentaAgaReportFilterModel, PrezentaAgaReportModel, PrezentaComisiiGlInIntervalReportBundle, PrezentaComisiiGlInIntervalReportFilterModel, PrezentaComisiiGlInIntervalReportModel, PrezentaSedinteCdPvgInvitatiARBReportFilterModel, PrezentaSedinteCdPvgInvitatiARBReportModel, PrezentaSedinteCdPvgInvitatiExterniReportFilterModel, PrezentaSedinteCdPvgInvitatiExterniReportModel, PrezentaSedinteMembriiReportFilterModel, PrezentaSedinteMembriiReportModel, ReprezentantiBancaPerFunctieSiComisieReportFilterModel, ReprezentantiBancaPerFunctieSiComisieReportModel, TaskuriCumulateReportFilterModel, TaskuriCumulateReportModel } from "./../model";
import { NotaGeneralaReportRowModel } from "../model/reports/nota-generala-report-row.model";
import { NotaGeneralaReportFilterModel } from "../model/reports/nota-generala-report-filter.model";
import { NivelReprezentareComisiiReportFilterModel } from "../model/reports/nivel-reprezentare-comisii-report-filter.model";
import { NivelReprezentareComisiiReportModel } from "../model/reports/nivel-reprezentare-comisii-report.model";
import { NoteBanciReportFilterModel } from "../model/reports/note-banci-report-filter.model";
import { NoteBanciReportModel } from "../model/reports/note-banci-report.model";

@Injectable()
export class ReportService {

	private apiCaller: ApiCaller;

	public constructor(apiCaller: ApiCaller) {
		this.apiCaller = apiCaller;
	}

	public getNumarSedinteSiParticipantiReport(filter: NumarSedinteSiParticipantiReportFilterModel, callback: AsyncCallback<NumarSedinteSiParticipantiReportModel, AppError>) {
		this.apiCaller.call(ApiPathConstants.GET_NUMAR_SEDINTE_SI_PARTICIPANTI_REPORT, filter, NumarSedinteSiParticipantiReportModel, callback);
	}

	public getAderareOioroReport(filter: AderareOioroReportFilterModel, callback: AsyncCallback<AderareOioroReportRowModel[], AppError>) {
		this.apiCaller.call(ApiPathConstants.GET_ADERARE_OIORO_REPORT, filter, AderareOioroReportRowModel, callback);
	}

	public getDeplasariDeconturiDecontCheltuieliReprezentantArb(filter: DeplDecontCheltReprArbReportFilterModel, callback: AsyncCallback<DeplDecontCheltReprArbReportModel, AppError>) {
		this.apiCaller.call(ApiPathConstants.GET_DEPLASARI_DECONTURI_DECONT_CHELT_REPR_ARB, filter, DeplDecontCheltReprArbReportModel, callback);
	}
	
	public getPrezentaSedinteMembriiReport(filter: PrezentaSedinteMembriiReportFilterModel, callback: AsyncCallback<PrezentaSedinteMembriiReportModel, AppError>) {
		this.apiCaller.call(ApiPathConstants.GET_PREZENTA_SEDINTE_MEMBRII, filter, PrezentaSedinteMembriiReportModel, callback);
	}

	public getDocumenteTrimiseDeArbReport(filter: DocumenteTrimiseDeArbReportFilterModel, callback: AsyncCallback<DocumenteTrimiseDeArbReportModel[], AppError>) {
		this.apiCaller.call(ApiPathConstants.GET_DOCUMENTE_TRIMISE_DE_ARB, filter, DocumenteTrimiseDeArbReportModel, callback);
	}

	public getPrezentaSedinteCdPvgInvitatiArbReport(filter: PrezentaSedinteCdPvgInvitatiARBReportFilterModel, callback: AsyncCallback<PrezentaSedinteCdPvgInvitatiARBReportModel, AppError>) {
		this.apiCaller.call(ApiPathConstants.GET_PREZENTA_SEDINTE_INVITATI_ARB_REPORT, filter, PrezentaSedinteCdPvgInvitatiARBReportModel, callback);
	}

	public getPrezentaSedinteCdPvgInvitatiExterniReport(filter: PrezentaSedinteCdPvgInvitatiExterniReportFilterModel, callback: AsyncCallback<PrezentaSedinteCdPvgInvitatiExterniReportModel, AppError>) {
		this.apiCaller.call(ApiPathConstants.GET_PREZENTA_SEDINTE_INVITATI_EXTERNI_REPORT, filter, PrezentaSedinteCdPvgInvitatiExterniReportModel, callback);
	}
	
	public getParticipariLaEvenimenteReport(filter: ParticipariLaEvenimenteReportFilterModel, callback: AsyncCallback<ParticipariLaEvenimenteReportRowModel[], AppError>) {
		this.apiCaller.call(ApiPathConstants.GET_PARTICIPARI_LA_EVENIMENTE_REPORT, filter, ParticipariLaEvenimenteReportRowModel, callback);
	}

	public getListaProiectelorCareAuVizatActiunileLuniiReport(filter: ListaProiectelorCareAuVizatActiunileLuniiReportFilterModel, callback: AsyncCallback<ListaProiectelorCareAuVizatActiunileLuniiReportModel[], AppError>) {
		this.apiCaller.call(ApiPathConstants.GET_LISTA_PROIECTELOR_CARE_AU_VIZAT_ACTIUNILE_LUNII_REPORT, filter, ListaProiectelorCareAuVizatActiunileLuniiReportModel, callback);
	}
	
	public getActiuniPeProiectReport(filter: ActiuniPeProiectReportFilterModel, callback: AsyncCallback<ActiuniPeProiectReportModel, AppError>) {
		this.apiCaller.call(ApiPathConstants.GET_ACTIUNI_PE_PROIECT_REPORT, filter, ActiuniPeProiectReportModel, callback);
	}
	
	public getDecontCheltuieliAlteDeconturiReport(filter: DecontCheltuieliAlteDeconturiReportFilterModel, callback: AsyncCallback<DecontCheltuieliAlteDeconturiReportModel, AppError>) {
		this.apiCaller.call(ApiPathConstants.GET_DEPLASARI_DECONT_ALTE_DECONTURI_REPORT, filter, DecontCheltuieliAlteDeconturiReportModel, callback);
	}
	
	public getParticipareReprezentantiArbLaActiuniInAfaraAsociatieiReport(filter: ParticipareReprezentantiArbLaActiuniInAfaraAsociatieiReportFilterModel, callback: AsyncCallback<ParticipareReprezentantiArbLaActiuniInAfaraAsociatieiReportModel, AppError>) {
		this.apiCaller.call(ApiPathConstants.GET_PARTICIPARE_REPREZENTANTI_ARB_LA_ACTIUNI_IN_AFARA_ASOCIATIEI_REPORT, filter, ParticipareReprezentantiArbLaActiuniInAfaraAsociatieiReportModel, callback);
	}
	
	public getCheltuieliArbSiReprezentantArbReport(filter: CheltuieliArbSiReprezentantArbReportFilterModel, callback: AsyncCallback<CheltuieliArbSiReprezentantArbReportModel, AppError>) {
		this.apiCaller.call(ApiPathConstants.GET_CHELTUIELI_ARB_SI_REPREZENTANT_ARB_REPORT, filter, CheltuieliArbSiReprezentantArbReportModel, callback);
	}
	
	public getActiuniOrganizateDeArbReport(filter: ActiuniOrganizateDeArbReportFilterModel, callback: AsyncCallback<ActiuniOrganizateDeArbReportModel, AppError>) {
		this.apiCaller.call(ApiPathConstants.GET_ACTIUNI_ORGANIZATE_DE_ARB_REPORT, filter, ActiuniOrganizateDeArbReportModel, callback);
	}
	
	public getDashboardProiecteReport(filter: DashboardProiecteReportFilterModel, callback: AsyncCallback<DashboardProiecteReportRowModel[], AppError>) {
		this.apiCaller.call(ApiPathConstants.GET_DASHBOARD_PROIECTE_REPORT, filter, DashboardProiecteReportRowModel, callback);
	}

	public getPrezentaComisiiGlInIntervalReportBundle(document: DocumentIdentifierModel,callback: AsyncCallback<PrezentaComisiiGlInIntervalReportBundle, AppError>) {
		this.apiCaller.call(ApiPathConstants.GET_PREZENTA_COMISII_GL_IN_INTERVAL_REPORT_BUNDLE, document, PrezentaComisiiGlInIntervalReportBundle, callback);
	}

	public getPrezentaComisiiGlInIntervalReport(filter: PrezentaComisiiGlInIntervalReportFilterModel, callback: AsyncCallback<PrezentaComisiiGlInIntervalReportModel, AppError>) {
		this.apiCaller.call(ApiPathConstants.GET_PREZENTA_COMISII_GL_IN_INTERVAL_REPORT, filter, PrezentaComisiiGlInIntervalReportModel, callback);
	}
	
	public getDashboardProiecteReportBundle(callback: AsyncCallback<DashboardProiecteReportBundle, AppError>) {
		this.apiCaller.call(ApiPathConstants.GET_DASHBOARD_PROIECTE_REPORT_BUNDLE, null, DashboardProiecteReportBundle, callback);
	}
	
	public getPrezentaAgaReport(filter: PrezentaAgaReportFilterModel, callback: AsyncCallback<PrezentaAgaReportModel, AppError>) {
		this.apiCaller.call(ApiPathConstants.GET_PREZENTA_AGA_REPORT, filter, PrezentaAgaReportModel, callback);
	}
	
	public getNumarParticipantiSedinteComisieGlReportBundle(callback: AsyncCallback<NumarParticipantiSedinteComisieGlReportBundle, AppError>) {
		this.apiCaller.call(ApiPathConstants.GET_NUMAR_PARTICIPANTI_SEDINTE_COMISIE_GL_REPORT_BUNDLE, null, NumarParticipantiSedinteComisieGlReportBundle, callback);
	}
	
	public getNumarParticipantiSedinteComisieGlReport(filter: NumarParticipantiSedinteComisieGlReportFilterModel, callback: AsyncCallback<NumarParticipantiSedinteComisieGlReportModel, AppError>) {
		this.apiCaller.call(ApiPathConstants.GET_NUMAR_PARTICIPANTI_SEDINTE_COMISIE_GL_REPORT, filter, NumarParticipantiSedinteComisieGlReportModel, callback);
	}

	public getNumarSedinteComisieGlReport(filter: NumarSedinteComisieGlReportFilterModel, callback: AsyncCallback<NumarSedinteComisieGlReportModel, AppError>) {
		this.apiCaller.call(ApiPathConstants.GET_NUMAR_SEDINTE_COMISIE_GL_REPORT, filter, NumarSedinteComisieGlReportModel, callback);
	}
	
	public getMembriiAfiliatiReport(filter: MembriiAfiliatiReportFilterModel, callback: AsyncCallback<MembriiAfiliatiReportModel, AppError>) {
		this.apiCaller.call(ApiPathConstants.GET_MEMBRII_AFILIATI_REPORT, filter, MembriiAfiliatiReportModel, callback);
	}
	
	public getTaskuriCumulate(filter: TaskuriCumulateReportFilterModel, callback: AsyncCallback<TaskuriCumulateReportModel, AppError>) {
		this.apiCaller.call(ApiPathConstants.GET_TASKURI_CUMULATE_REPORT, filter, TaskuriCumulateReportModel, callback);
	}
	
	public getDeplasariDeconturiReport(filter: DeplasariDeconturiReportFilterModel, callback: AsyncCallback<DeplasariDeconturiRowModel[], AppError>) {
		this.apiCaller.call(ApiPathConstants.GET_DEPLASARI_DECONTURI_REPORT, filter, DeplasariDeconturiRowModel, callback);
	}
	
	public getDeplasariDeconturiReportBundle(callback: AsyncCallback<DeplasariDeconturiReportBundleModel, AppError>) {
		this.apiCaller.call(ApiPathConstants.GET_DEPLASARI_DECONTURI_REPORT_BUNDLE, null, DeplasariDeconturiReportBundleModel, callback);
	}
	
	public getCereriConcediuReportBundle(callback: AsyncCallback<CereriConcediuReportBundle, AppError>) {
		this.apiCaller.call(ApiPathConstants.GET_CERERI_CONCEDIU_REPORT_BUNDLE, null, CereriConcediuReportBundle, callback);
	}
	
	public getCereriConcediuReport(filter: CereriConcediuReportFilterModel, callback: AsyncCallback<CereriConcediuReportModel, AppError>) {
		this.apiCaller.call(ApiPathConstants.GET_CERERI_CONCEDIU_REPORT, filter, CereriConcediuReportModel, callback);
	}
	
	public getReprezentantiBancaPerFunctieSiComisieReport(filter: ReprezentantiBancaPerFunctieSiComisieReportFilterModel, callback: AsyncCallback<ReprezentantiBancaPerFunctieSiComisieReportModel, AppError>) {
		this.apiCaller.call(ApiPathConstants.GET_REPREZENTANTI_BANCA_PER_FUNCTIE_SI_COMISIE_REPORT, filter, ReprezentantiBancaPerFunctieSiComisieReportModel, callback);
	}
	
	public getPrezentaComisiiArbReprezentativitate(filter: PrezentaReprezentativitateReportFilterModel, callback: AsyncCallback<PrezentaReprezentativitateReportModel, AppError>) {
		this.apiCaller.call(ApiPathConstants.GET_PREZENTA_COMISII_GL_REPREZENTATIVITATE, filter, PrezentaReprezentativitateReportModel, callback);
	}

	public getRaspunsuriBanciReport(filter: RaspunsuriBanciReportFilterModel, callback: AsyncCallback<RaspunsuriBanciReportRowModel[], AppError>) {
		this.apiCaller.call(ApiPathConstants.GET_RASPUNSURI_BANCI_REPORT, filter, RaspunsuriBanciReportRowModel, callback);
	}
	
	public getRaspunsuriBanciReportBundle(callback: AsyncCallback<RaspunsuriBanciReportBundleModel, AppError>) {
		this.apiCaller.call(ApiPathConstants.GET_RASPUNSURI_BANCI_REPORT_BUNDLE, null, RaspunsuriBanciReportBundleModel, callback);
	}
	
	public getCentralizatorPrezentaPerioadaReport(filter: CentralizatorPrezentaPerioadaReportFilterModel, callback: AsyncCallback<CentralizatorPrezentaPerioadaReportModel, AppError>) {
		this.apiCaller.call(ApiPathConstants.GET_CENTRALIZATOR_PREZENTA_PERIOADA_REPORT, filter, CentralizatorPrezentaPerioadaReportModel, callback);
	}
	
	public getNotaGeneralaPeMembriiArbReport(filter: NotaGeneralaPeMembriiArbReportFilterModel, callback: AsyncCallback<NotaGeneralaPeMembriiArbReportModel, AppError>) {
		this.apiCaller.call(ApiPathConstants.GET_NOTA_GENERALA_PE_MEMBRII_ARB_REPORT, filter, NotaGeneralaPeMembriiArbReportModel, callback);
	}
	
	public getNotaGeneralaReport(filter: NotaGeneralaReportFilterModel, callback: AsyncCallback<NotaGeneralaReportRowModel[], AppError>) {
		this.apiCaller.call(ApiPathConstants.GET_NOTA_GENERALA_REPORT, filter, NotaGeneralaReportRowModel, callback);
	}
	
	public getNivelReprezentareComisiiReport(filter: NivelReprezentareComisiiReportFilterModel, callback: AsyncCallback<NivelReprezentareComisiiReportModel, AppError>) {
		this.apiCaller.call(ApiPathConstants.GET_NIVEL_REPREZENTARE_COMISII_REPORT, filter, NivelReprezentareComisiiReportModel, callback);
	}
	
	public getNoteBanciReport(filter: NoteBanciReportFilterModel, callback: AsyncCallback<NoteBanciReportModel, AppError>) {
		this.apiCaller.call(ApiPathConstants.GET_NOTE_BANCI_REPORT, filter, NoteBanciReportModel, callback);
	}
}