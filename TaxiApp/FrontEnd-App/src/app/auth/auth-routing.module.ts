import { NgModule } from "@angular/core";
import { Routes, RouterModule } from "@angular/router";
import { LoginComponent } from "./login";
import { ChangePasswordComponent } from "./change-password";

const routes: Routes = [
	{
		path: "login",
		component: LoginComponent,
		data: {
			pageLayout: "simple"
		}
	},{
		path: "change-password",
		component: ChangePasswordComponent,
		data: {
			pageLayout: "simple"
		}
	},
	{
		path: "",
		redirectTo: "login",
		pathMatch: "full"
	}
];

@NgModule({
	imports: [RouterModule.forChild(routes)],
	exports: [RouterModule]
})
export class AuthRoutingModule {
}
