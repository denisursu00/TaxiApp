import { NgModule } from "@angular/core";
import { Routes, RouterModule } from "@angular/router";
import { AUTH_ACCESS, AuthRouteGuard } from "@app/shared/auth";
import { OrderTaxiComponent } from "./order-taxi/order-taxi.component";
import { PersonalPageClientComponent } from "./personal-page-client/personal-page-client.component";

const routes: Routes = [
	{
		path: "personal-page",
		component: PersonalPageClientComponent,
		canActivate: [AuthRouteGuard],
		data: {
			authPermissions: AUTH_ACCESS.CLIENT.PERSONAL_PAGE.permissions
		}
	},
	{
		path: "order-taxi",
		component: OrderTaxiComponent,
		canActivate: [AuthRouteGuard],
		data: {
			authPermissions: AUTH_ACCESS.CLIENT.ORDER_TAXI.permissions
		}
	},
	{
		path: "",
		redirectTo: "personal-page",
		pathMatch: "full"
	}
];

@NgModule({
	imports: [RouterModule.forChild(routes)],
	exports: [RouterModule]
})
export class ClientRoutingModule {

}
