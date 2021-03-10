import { NgModule } from "@angular/core";
import { CommonModule } from "@angular/common";
import { FormsModule, ReactiveFormsModule } from "@angular/forms";
import { HttpModule } from "@angular/http";
import { RouterModule } from "@angular/router";
import { SharedModule } from "@app/shared";
import { TableModule } from "primeng/table";
import {
	InputTextModule,
	InputTextareaModule,
	DialogModule,
	ButtonModule,
	DataTableModule,
	MultiSelectModule,
	DropdownModule,
	TreeModule,
	ConfirmDialogModule,
	FileUploadModule,
	TabViewModule,
	AccordionModule,
	FieldsetModule,
	CalendarModule,
	BlockUIModule,
	ProgressSpinnerModule,
	MessageModule,
	MessagesModule,
	TieredMenuModule,
	KeyFilterModule,
	ContextMenuModule,
	PickListModule,
	CheckboxModule,
	PanelModule,
	TooltipModule,
	SelectButtonModule,
	InputSwitchModule,
	ChartModule,
	SpinnerModule,
	SliderModule
} from "primeng/primeng";
import { DataViewModule } from "primeng/dataview";
import { ScheduleModule } from "primeng/schedule";
import { TreeDragDropService } from "primeng/components/common/treedragdropservice";
import { ReportsRoutingModule } from "./reports-routing.module";
import { NumarSedinteSiParticipantiReportComponent } from "./numar-sedinte-si-participanti-report";
import { ParticipariLaEvenimenteReportComponent } from "./participari-la-evenimente-report";
import { PrezentaSedinteInvitatiARBReportComponent } from "./prezenta-sedinte-invitati-arb-report";
import { PrezentaSedinteInvitatiExterniReportComponent } from "./prezenta-sedinte-invitati-externi-report";
import { PrezentaSedinteMembriiReportComponent } from "./prezenta-sedinte-membrii-report";
import { DecontCheltuieliAlteDeconturiReportComponent } from "./decont-cheltuieli-alte-deconturi-report";
import { ClientCommonModule } from "@app/client/common";
import { TranslateModule, TranslateLoader } from "@ngx-translate/core";
import { AppTranslateLoader } from "@app/shared";
import { TipSedintaCdPvgComponent } from "./components/tip-sedinta-cd-pvg-selector";
import { ParticipareReprezentantiArbLaActiuniInAfaraAsociatieiReportComponent } from "./participare-reprezentanti-arb-la-actiuni-in-afara-asociatiei-report";
import { ActiuniOrganizateDeArbReportComponent } from "./actiuni-organizate-de-arb-report";
import { DashboardProiecteReportComponent } from "./dashboard-proiecte";
import { PrezentaAgaReportComponent } from "./prezenta-aga-report";
import { DeplDecontCheltReprArbReportComponent } from "./depl-decont-chelt-repr-arb-report";
import { AderareOioroReportComponent } from "./aderare-oioro-report";
import { MembriiAfiliatiReportComponent } from "./membrii-afiliati-report";
import { NumarSedinteComisieGlReportComponent } from "./numar-sedinte-comisie-gl-report";
import { NumarParticipantiSedinteComisieGlReportComponent } from "./numar-participanti-sedinte-comisie-gl-report";
import { PrezentaComisiiGlInIntervalReportComponent } from "./prezenta-comisii-gl-in-interval-report";
import { CheltuieliArbSiReprezentantArbReportComponent } from "./cheltuieli-arb-si-reprezentant-arb-report";
import { ActiuniPeProiectReportComponent } from "./actiuni-pe-proiect-report";
import { ListaProiectelorCareAuVizatActiunileLuniiReportComponent } from "./lista-proiectelor-care-au-vizat-actiunile-lunii";
import { DocumenteTrimiseDeArbReportComponent } from "./documente-trimise-de-arb-raport";
import { DeplasariDeconturiReportComponent } from "./deplasari-deconturi-report";
import { TaskuriCumulateReportComponent } from "./taskuri-cumulate-report";
import { CereriConcediuReportComponent } from "./cereri-concediu-report/cereri-concediu-report.component";
import { ReprezentantiBancaPerFunctieSiComisieReportComponent } from "./reprezentanti-banca-per-functie-si-comisie-report";
import { RaspunsuriBanciReportComponent } from "./raspunsuri-banci";
import { CentralizatorPrezentaPerioadaReportComponent } from "./centralizator-prezenta-perioada-report/centralizator-prezenta-perioada-report.component";
import { NotaGeneralaPeMembriiArbReportComponent } from "./nota-generala-pe-membrii-arb-report/nota-generala-pe-membrii-arb-report.component";
import { PrezentaReprezentativitateReportComponent } from "./prezenta-comisii-gl-reprezentativitate";
import { NotaGeneralaReportComponent } from "./nota-generala";
import { NivelReprezentareComisiiReportComponent } from "./nivel-reprezentare-comisii";
import { NoteBanciReportComponent } from "./note-banci";

@NgModule({
	imports: [
		TranslateModule.forChild({
			loader: {
				provide: TranslateLoader,
				useClass: AppTranslateLoader
			}
		}),
		CommonModule,
		RouterModule,
		HttpModule,
		SharedModule,
		InputTextModule,
		InputTextareaModule,
		DialogModule,
		ButtonModule,
		DataTableModule,
		CommonModule,
		MultiSelectModule,
		FormsModule,
		ReactiveFormsModule,
		DropdownModule,
		TreeModule,
		TabViewModule,
		AccordionModule,
		FileUploadModule,
		MessageModule,
		MessagesModule,
		FieldsetModule,
		CalendarModule,
		BlockUIModule,
		ProgressSpinnerModule,
		KeyFilterModule,
		TableModule,
		TieredMenuModule,
		ContextMenuModule,
		PickListModule,
		CheckboxModule,
		PanelModule,
		TooltipModule,
		ScheduleModule,
		SelectButtonModule,
		InputSwitchModule,
		DataViewModule,
		SpinnerModule,
		ReportsRoutingModule,
		ClientCommonModule,
		SliderModule
	],
	declarations: [
		NumarSedinteSiParticipantiReportComponent,
		ParticipariLaEvenimenteReportComponent,
		PrezentaSedinteInvitatiARBReportComponent,
		PrezentaSedinteInvitatiExterniReportComponent,
		PrezentaSedinteMembriiReportComponent,
		DecontCheltuieliAlteDeconturiReportComponent,
		ParticipareReprezentantiArbLaActiuniInAfaraAsociatieiReportComponent,
		TipSedintaCdPvgComponent,
		CheltuieliArbSiReprezentantArbReportComponent,
		TipSedintaCdPvgComponent,
		PrezentaComisiiGlInIntervalReportComponent,
		ActiuniOrganizateDeArbReportComponent,
		DashboardProiecteReportComponent,
		PrezentaAgaReportComponent,
		NumarParticipantiSedinteComisieGlReportComponent,
		TipSedintaCdPvgComponent,
		DeplDecontCheltReprArbReportComponent,
		NumarSedinteComisieGlReportComponent,
		AderareOioroReportComponent,
		MembriiAfiliatiReportComponent,
		ActiuniPeProiectReportComponent,
		ListaProiectelorCareAuVizatActiunileLuniiReportComponent,
		DocumenteTrimiseDeArbReportComponent,
		TaskuriCumulateReportComponent,
		DeplasariDeconturiReportComponent,
		CereriConcediuReportComponent,
		ReprezentantiBancaPerFunctieSiComisieReportComponent,
		CentralizatorPrezentaPerioadaReportComponent,
		NotaGeneralaPeMembriiArbReportComponent,
		PrezentaReprezentativitateReportComponent,
		RaspunsuriBanciReportComponent,
		CentralizatorPrezentaPerioadaReportComponent,
		NotaGeneralaReportComponent,
		NivelReprezentareComisiiReportComponent,
		NoteBanciReportComponent
	]
})
export class ReportsModule { }
