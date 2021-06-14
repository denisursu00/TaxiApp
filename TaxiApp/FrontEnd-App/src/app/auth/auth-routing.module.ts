import { NgModule } from "@angular/core";
import { Routes, RouterModule } from "@angular/router";
import { LoginComponent } from "./login";
import { RegisterComponent } from "./register/register.component";

const routes: Routes = [
	{
		path: "login",
		component: LoginComponent,
		data: {
			pageLayout: "simple"
		}
	},
	{
		path: "register",
		component: RegisterComponent,
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
