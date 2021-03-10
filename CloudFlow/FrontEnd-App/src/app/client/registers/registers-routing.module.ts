import { NgModule } from "@angular/core";
import { Routes, RouterModule } from "@angular/router";
import { RegistruDocumenteJustificativePlatiComponent } from "./registru-documente-justificative-plati";
import { AUTH_ACCESS, AuthRouteGuard } from "@app/shared/auth";
import { RegistruIntrariIesiriComponent } from "./registru-intrari-iesiri";
import { ComisiiSauGLComponent } from "./comisii-sau-gl";
import { DeplasariDeconturiComponent } from "./deplasari-deconturi";
import { AlteDeconturiComponent } from "./alte-deconturi";

const routes: Routes = [
	{
		path: "registru-documente-justificative-plati",
		component: RegistruDocumenteJustificativePlatiComponent,
		canActivate: [AuthRouteGuard],
		data: {
			authPermissions: AUTH_ACCESS.CLIENT.REGISTERS.REGISTRU_DOCUMENTE_JUSTIFICATIVE_PLATI.permissions
		}
	},
	{
		path: "registru-intrari-iesiri",
		component: RegistruIntrariIesiriComponent,
		canActivate: [AuthRouteGuard],
		data: {
			authPermissions: AUTH_ACCESS.CLIENT.REGISTERS.REGISTRU_INTRARI_IESIRI.permissions
		}
	},
	{
		path: "comisii-sau-gl",
		component: ComisiiSauGLComponent,
		canActivate: [AuthRouteGuard],
		data: {
			authPermissions: AUTH_ACCESS.CLIENT.REGISTERS.COMISII_SAU_GL.permissions
		}
	},
	{
		path: "deplasari-deconturi",
		component: DeplasariDeconturiComponent,
		canActivate: [AuthRouteGuard],
		data: {
			authPermissions: AUTH_ACCESS.CLIENT.REGISTERS.DEPLASARI_DECONTURI.permissions
		}
	},
	{
		path: "alte-deconturi",
		component: AlteDeconturiComponent,
		canActivate: [AuthRouteGuard],
		data: {
			authPermissions: AUTH_ACCESS.CLIENT.REGISTERS.ALTE_DECONTURI.permissions
		}
	}
];

@NgModule({
	imports: [RouterModule.forChild(routes)],
	exports: [RouterModule]
})
export class RegistersRoutingModule { }
