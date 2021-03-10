import { NgModule } from "@angular/core";
import { Routes, RouterModule } from "@angular/router";
import { AUTH_ACCESS, AuthRouteGuard } from "@app/shared/auth";
import { NumarSedinteSiParticipantiReportComponent } from "./numar-sedinte-si-participanti-report";
import { ParticipariLaEvenimenteReportComponent } from "./participari-la-evenimente-report";
import { PrezentaSedinteInvitatiARBReportComponent } from "./prezenta-sedinte-invitati-arb-report";
import { PrezentaSedinteInvitatiExterniReportComponent } from "./prezenta-sedinte-invitati-externi-report";
import { PrezentaSedinteMembriiReportComponent } from "./prezenta-sedinte-membrii-report";
import { DecontCheltuieliAlteDeconturiReportComponent } from "./decont-cheltuieli-alte-deconturi-report";
import { ParticipareReprezentantiArbLaActiuniInAfaraAsociatieiReportComponent } from "./participare-reprezentanti-arb-la-actiuni-in-afara-asociatiei-report";
import { ActiuniOrganizateDeArbReportComponent } from "./actiuni-organizate-de-arb-report";
import { DashboardProiecteReportComponent } from "./dashboard-proiecte";
import { PrezentaAgaReportComponent } from "./prezenta-aga-report";
import { DeplDecontCheltReprArbReportComponent } from "./depl-decont-chelt-repr-arb-report";
import { AderareOioroReportComponent } from "./aderare-oioro-report";
import { MembriiAfiliatiReportComponent } from "./membrii-afiliati-report/membrii-afiliati-report.componente";
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

const routes: Routes = [
	{
		path: "raport-numar-sedinte-si-participanti",
		component: NumarSedinteSiParticipantiReportComponent,
		canActivate: [AuthRouteGuard],
		data: {
			authPermissions: AUTH_ACCESS.CLIENT.REPORTS.permissions
		}
	},
	{
		path: "raport-participari-la-evenimente",
		component: ParticipariLaEvenimenteReportComponent,
		canActivate: [AuthRouteGuard],
		data: {
			authPermissions: AUTH_ACCESS.CLIENT.REPORTS.permissions
		}
	},
	{
		path: "raport-actiuni-pe-proiect",
		component: ActiuniPeProiectReportComponent,
		canActivate: [AuthRouteGuard],
		data: {
			authPermissions: AUTH_ACCESS.CLIENT.REPORTS.permissions
		}
	},
	{
		path: "raport-lista-proiectelor-care-au-vizat-actiunile-lunii",
		component: ListaProiectelorCareAuVizatActiunileLuniiReportComponent,
		canActivate: [AuthRouteGuard],
		data: {
			authPermissions: AUTH_ACCESS.CLIENT.REPORTS.permissions
		}
	},
	{
		path: "raport-prezenta-sedinte-invitati-arb",
		component: PrezentaSedinteInvitatiARBReportComponent,
		canActivate: [AuthRouteGuard],
		data: {
			authPermissions: AUTH_ACCESS.CLIENT.REPORTS.permissions
		}
	},
	{
		path: "raport-prezenta-sedinte-invitati-externi",
		component: PrezentaSedinteInvitatiExterniReportComponent,
		canActivate: [AuthRouteGuard],
		data: {
			authPermissions: AUTH_ACCESS.CLIENT.REPORTS.permissions
		}
	},
	{
		path: "raport-prezenta-sedinte-membrii",
		component: PrezentaSedinteMembriiReportComponent,
		canActivate: [AuthRouteGuard],
		data: {
			authPermissions: AUTH_ACCESS.CLIENT.REPORTS.permissions
		}
	},
	{
		path: "raport-documente-trimise-de-arb",
		component: DocumenteTrimiseDeArbReportComponent,
		canActivate: [AuthRouteGuard],
		data: {
			authPermissions: AUTH_ACCESS.CLIENT.REPORTS.permissions
		}
	},
	{
		path: "raport-decont-cheltuieli-alte-deconturi",
		component: DecontCheltuieliAlteDeconturiReportComponent,
		canActivate: [AuthRouteGuard],
		data: {
			authPermissions: AUTH_ACCESS.CLIENT.REPORTS.permissions
		}
	},
	{
		path: "raport-comisii-gl-in-interval",
		component: PrezentaComisiiGlInIntervalReportComponent,
		canActivate: [AuthRouteGuard],
		data: {
			authPermissions: AUTH_ACCESS.CLIENT.REPORTS.permissions
		}
	},
	{
		path: "raport-prezenta-aga",
		component: PrezentaAgaReportComponent,
		canActivate: [AuthRouteGuard],
		data: {
			authPermissions: AUTH_ACCESS.CLIENT.REPORTS.permissions
		}
	},
	{
		path: "raport-numar-participanti-sedinte-comisie-gl",
		component: NumarParticipantiSedinteComisieGlReportComponent,
		canActivate: [AuthRouteGuard],
		data: {
			authPermissions: AUTH_ACCESS.CLIENT.REPORTS.permissions
		}
	},
	{
		path: "raport-dashboard-proiecte",
		component: DashboardProiecteReportComponent,
		canActivate: [AuthRouteGuard],
		data: {
			authPermissions: AUTH_ACCESS.CLIENT.REPORTS.permissions
		}
	},
	{
		path: "raport-deplasari-deconturi-cheltuieli-reprezentanti-arb",
		component: DeplDecontCheltReprArbReportComponent,
		canActivate: [AuthRouteGuard],
		data: {
			authPermissions: AUTH_ACCESS.CLIENT.REPORTS.permissions
		}
	},
	{
		path: "raport-actiuni-organizate-de-arb",
		component: ActiuniOrganizateDeArbReportComponent,
		canActivate: [AuthRouteGuard],
		data: {
			authPermissions: AUTH_ACCESS.CLIENT.REPORTS.permissions
		}
	},
	{
		path: "raport-aderare-oioro",
		component: AderareOioroReportComponent,
		canActivate: [AuthRouteGuard],
		data: {
			authPermissions: AUTH_ACCESS.CLIENT.REPORTS.permissions
		}
	},
	{
		path: "raport-membrii-afiliati",
		component: MembriiAfiliatiReportComponent,
		canActivate: [AuthRouteGuard],
		data: {
			authPermissions: AUTH_ACCESS.CLIENT.REPORTS.permissions
		}
	},
	{
		path: "raport-numar-sedinte-comisie-gl",
		component: NumarSedinteComisieGlReportComponent,
		canActivate: [AuthRouteGuard],
		data: {
			authPermissions: AUTH_ACCESS.CLIENT.REPORTS.permissions
		}
	},
	{
		path: "raport-cheltuieli-arb-si-reprezentant-arb",
		component: CheltuieliArbSiReprezentantArbReportComponent,
		canActivate: [AuthRouteGuard],
		data: {
			authPermissions: AUTH_ACCESS.CLIENT.REPORTS.permissions
		}
	},
	{
		path: "raport-participare-reprezentanti-arb-la-actiuni-in-afara-asociatiei",
		component: ParticipareReprezentantiArbLaActiuniInAfaraAsociatieiReportComponent,
		canActivate: [AuthRouteGuard],
		data: {
			authPermissions: AUTH_ACCESS.CLIENT.REPORTS.permissions
		}
	},
	{
		path: "raport-taskuri-cumulate",
		component: TaskuriCumulateReportComponent,
		canActivate: [AuthRouteGuard],
		data: {
			authPermissions: AUTH_ACCESS.CLIENT.REPORTS.permissions
		}
	},
	{
		path: "raport-deplasari-deconturi",
		component: DeplasariDeconturiReportComponent,
		canActivate: [AuthRouteGuard],
		data: {
			authPermissions: AUTH_ACCESS.CLIENT.REPORTS.permissions
		}
	},
	{
		path: "raport-cereri-concediu",
		component: CereriConcediuReportComponent,
		canActivate: [AuthRouteGuard],
		data: {
			authPermissions: AUTH_ACCESS.CLIENT.REPORTS.permissions
		}
	},
	{
		path: "raport-reprezentanti-banca-per-functie-si-comisie",
		component: ReprezentantiBancaPerFunctieSiComisieReportComponent,
		canActivate: [AuthRouteGuard],
		data: {
			authPermissions: AUTH_ACCESS.CLIENT.REPORTS.permissions   
		}
	},
	{
		path: "raport-raspunsuri-banci",
		component: RaspunsuriBanciReportComponent,
		canActivate: [AuthRouteGuard],
		data: {
			authPermissions: AUTH_ACCESS.CLIENT.REPORTS.permissions
		}
	},
	{
		path: "raport-centralizator-prezenta-perioada",
		component: CentralizatorPrezentaPerioadaReportComponent,
		canActivate: [AuthRouteGuard],
		data: {
			authPermissions: AUTH_ACCESS.CLIENT.REPORTS.permissions
		}
	},
	{
		path: "raport-nota-generala-pe-membrii-arb",
		component: NotaGeneralaPeMembriiArbReportComponent,
		canActivate: [AuthRouteGuard],
		data: {
			authPermissions: AUTH_ACCESS.CLIENT.REPORTS.permissions
		}
	},
	{
		path: "raport-prezenta-comisii-gl-reprezentativitate",
		component: PrezentaReprezentativitateReportComponent,
		canActivate: [AuthRouteGuard],
		data: {
			authPermissions: AUTH_ACCESS.CLIENT.REPORTS.permissions
		}
	},
	{
		path: "raport-nota-generala",
		component: NotaGeneralaReportComponent,
		canActivate: [AuthRouteGuard],
		data: {
			authPermissions: AUTH_ACCESS.CLIENT.REPORTS.permissions
		}
	},
	{
		path: "raport-nivel-reprezentare-comisii",
		component: NivelReprezentareComisiiReportComponent,
		canActivate: [AuthRouteGuard],
		data: {
			authPermissions: AUTH_ACCESS.CLIENT.REPORTS.permissions
		}
	},
	{
		path: "raport-note-banci",
		component: NoteBanciReportComponent,
		canActivate: [AuthRouteGuard],
		data: {
			authPermissions: AUTH_ACCESS.CLIENT.REPORTS.permissions
		}
	}
];

@NgModule({
	imports: [RouterModule.forChild(routes)],
	exports: [RouterModule]
})
export class ReportsRoutingModule { }
