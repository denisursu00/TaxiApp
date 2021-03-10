import { NgModule } from "@angular/core";
import { Routes, RouterModule } from "@angular/router";
import { AuthRouteGuard, AUTH_ACCESS } from "@app/shared/auth";
import { MyDocumentActivitiesComponent } from "@app/client/common/my-document-activities";

const routes: Routes = [
	{
		path: "",
		component: MyDocumentActivitiesComponent,
		canActivate: [AuthRouteGuard],
		data: {
			authPermissions: AUTH_ACCESS.CLIENT.MY_DOCUMENT_ACTIVITIES.permissions
		}
	}
];

@NgModule({
	imports: [RouterModule.forChild(routes)],
	exports: [RouterModule]
})
export class MyDocumentActivitiesRoutingModule { }
