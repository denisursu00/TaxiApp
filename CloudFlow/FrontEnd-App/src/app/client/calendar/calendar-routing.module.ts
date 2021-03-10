import { NgModule } from "@angular/core";
import { Routes, RouterModule } from "@angular/router";
import { CalendarManagerComponent } from "./../common/calendar-manager/calendar-manager.component";
import { AUTH_ACCESS, AuthRouteGuard } from "@app/shared/auth";

const routes: Routes = [
	{
		path: "",
		component: CalendarManagerComponent,
		canActivate: [AuthRouteGuard],
		data: {
			authPermissions: AUTH_ACCESS.CLIENT.CALENDAR.permissions
		}
	}
];

@NgModule({
	imports: [RouterModule.forChild(routes)],
	exports: [RouterModule]
})
export class CalendarRoutingModule { }
