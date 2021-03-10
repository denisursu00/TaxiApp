import { NgModule } from "@angular/core";
import { Routes, RouterModule } from "@angular/router";
import { MyProjectTasksComponent } from "./../common/my-project-tasks";
import { ProjectsComponent } from "./projects.component";
import { AUTH_ACCESS, AuthRouteGuard } from "@app/shared/auth";

const routes: Routes = [
	{
		path: "",
		redirectTo: "initiation-updating-projects",
		pathMatch: "full"
	},
	{
		path: "my-project-activities",
		component: MyProjectTasksComponent,
		canActivate: [AuthRouteGuard],
		data: {
			authPermissions: AUTH_ACCESS.CLIENT.PROJECTS.MY_PROJECT_ACTIVITIES.permissions
		}
	},
	{
		path: "initiation-updating-projects",
		component: ProjectsComponent,
		canActivate: [AuthRouteGuard],
		data: {
			authPermissions: AUTH_ACCESS.CLIENT.PROJECTS.INITIATION_UPDATING_PROJECTS.permissions
		}
	}
];

@NgModule({
	imports: [RouterModule.forChild(routes)],
	exports: [RouterModule]
})
export class ProjectsRoutingModule { }
