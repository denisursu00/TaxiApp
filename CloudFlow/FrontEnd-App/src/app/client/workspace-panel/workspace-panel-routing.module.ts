import { NgModule } from "@angular/core";
import { Routes, RouterModule } from "@angular/router";
import { WorkspacePanelComponent } from "./workspace-panel.component";
import { AUTH_ACCESS, AuthRouteGuard } from "@app/shared/auth";

const routes: Routes = [
	{
		path: "",
		component: WorkspacePanelComponent,
		canActivate: [AuthRouteGuard],
		data: {
			authPermissions: AUTH_ACCESS.CLIENT.WORKSPACE_PANEL.permissions
		}
	}
];

@NgModule({
	imports: [RouterModule.forChild(routes)],
	exports: [RouterModule]
})
export class WorkspacePanelRoutingModule { }
