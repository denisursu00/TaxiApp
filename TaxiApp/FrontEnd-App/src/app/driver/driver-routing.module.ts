import { NgModule } from "@angular/core";
import { Routes, RouterModule } from "@angular/router";
import { AUTH_ACCESS, AuthRouteGuard } from "@app/shared/auth";
import { PersonalPageDriverComponent } from "./personal-page-driver/personal-page-driver.component";

const routes: Routes = [
	{
		path: "personal-page",
		component: PersonalPageDriverComponent,
		canActivate: [AuthRouteGuard],
		data: {
			authPermissions: AUTH_ACCESS.DRIVER.PERSONAL_PAGE.permissions
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
export class DriverRoutingModule {

}
