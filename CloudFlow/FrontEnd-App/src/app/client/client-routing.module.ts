import { NgModule } from "@angular/core";
import { Routes, RouterModule } from "@angular/router";
import { DashboardComponent } from "./dashboard/dashboard.component";
import { AuthRouteGuard, AUTH_ACCESS } from "@app/shared/auth";

const routes: Routes = [
	{
		path: "dashboard",
		loadChildren: "./dashboard/dashboard.module#DashboardModule"
	},
	{
		path: "my-document-activities",
		loadChildren: "./my-document-activities/my-document-activities.module#MyDocumentActivitiesModule"
	},
	{
		path: "workspace-panel",
		loadChildren: "./workspace-panel/workspace-panel.module#WorkspacePanelModule"
	},
	{
		path: "searches-and-reports",
		loadChildren: "./searches-and-reports/searches-and-reports.module#SearchesAndReportsModule"
	},
	{
		path: "calendar",
		loadChildren: "./calendar/calendar.module#CalendarModule"
	},
	{
		path: "nomenclators",
		loadChildren: "./nomenclators/nomenclators.module#NomenclatorsModule"
	},
	{
		path: "projects",
		loadChildren: "./projects/projects.module#ProjectsModule"
	},
	{
		path: "reports",
		loadChildren: "./reports/reports.module#ReportsModule"
	},
	{
		path: "registers",
		loadChildren: "./registers/registers.module#RegistersModule"
	},
	{
		path: "",
		redirectTo: "dashboard",
		pathMatch: "full"
	}
];

@NgModule({
	imports: [RouterModule.forChild(routes)],
	exports: [RouterModule]
})
export class ClientRoutingModule { }
