import { NgModule } from "@angular/core";
import { Routes, RouterModule } from "@angular/router";
import { DocumentTypesComponent } from "./document-types";
import { AUTH_ACCESS, AuthRouteGuard } from "@app/shared/auth";
import { CalendarsComponent } from "./calendars";
import { GroupsComponent } from "./groups";
import { MimeTypesComponent } from "./mime-types";
import { NomenclatorsComponent } from "./nomenclators";
import { OrganizationalStructureComponent } from "./organizational-structure";
import { ParametersComponent } from "./parameters/parameters.component";
import { WorkflowsComponent } from "./workflows";
import { ReplacementProfilesComponent } from "@app/shared";
import { AdminPermissionEnum } from "@app/shared";
import { RolesAndPermissionsComponent } from "./roles-and-permissions";

const routes: Routes = [
	{
		path: "calendars",
		component: CalendarsComponent,
		canActivate: [AuthRouteGuard],
		data: {
			authPermissions: AUTH_ACCESS.ADMIN.CALENDARS.permissions
		}
	},
	{
		path: "document-types",
		component: DocumentTypesComponent,
		canActivate: [AuthRouteGuard],
		data: {
			authPermissions: AUTH_ACCESS.ADMIN.DOCUMENT_TYPES.permissions
		}
	},
	{
		path: "groups",
		component: GroupsComponent,
		canActivate: [AuthRouteGuard],
		data: {
			authPermissions: AUTH_ACCESS.ADMIN.GROUPS.permissions
		}
	},
	{
		path: "mime-types",
		component: MimeTypesComponent,
		canActivate: [AuthRouteGuard],
		data: {
			authPermissions: AUTH_ACCESS.ADMIN.MIME_TYPES.permissions
		}
	},
	{
		path: "nomenclators",
		component: NomenclatorsComponent,
		canActivate: [AuthRouteGuard],
		data: {
			authPermissions: AUTH_ACCESS.ADMIN.NOMENCLATORS.permissions
		}
	},
	{
		path: "organizational-structure",
		component: OrganizationalStructureComponent,
		canActivate: [AuthRouteGuard],
		data: {
			authPermissions: AUTH_ACCESS.ADMIN.ORGANIZATIONAL_STRUCTURE.permissions
		}
	},
	{
		path: "parameters",
		component: ParametersComponent,
		canActivate: [AuthRouteGuard],
		data: {
			authPermissions: AUTH_ACCESS.ADMIN.PARAMETERS.permissions
		}
	},
	{
		path: "replacement-profiles",
		component: ReplacementProfilesComponent,
		canActivate: [AuthRouteGuard],
		data: {
			authPermissions: AUTH_ACCESS.ADMIN.REPLACEMENT_PROFILES.permissions
		}
	},
	{
		path: "workflows",
		component: WorkflowsComponent,
		canActivate: [AuthRouteGuard],
		data: {
			authPermissions: AUTH_ACCESS.ADMIN.WORKFLOWS.permissions
		}
	},
	{
		path: "roles-and-permissions",
		component: RolesAndPermissionsComponent,
		canActivate: [AuthRouteGuard],
		data: {
			authPermissions: AUTH_ACCESS.ADMIN.ROLES_AND_PERMISSIONS.permissions
		}
	}/*,
	{
		path: "audit",
		component: AuditComponent,
		canActivate: [AuthRouteGuard],
		data: {
			authPermissions: AUTH_ACCESS.ADMIN.AUDIT.permissions
		}
	},
	{
		path: "history-and-errors",
		component: HistoryAndErrorsComponent,
		canActivate: [AuthRouteGuard],
		data: {
			authPermissions: AUTH_ACCESS.ADMIN.HISTORY_AND_ERRORS.permissions
		}
	}*/	
];

@NgModule({
	imports: [RouterModule.forChild(routes)],
	exports: [RouterModule]
})
export class AdminRoutingModule {
}
