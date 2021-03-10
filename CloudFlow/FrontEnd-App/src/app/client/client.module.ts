import { RouterModule } from "@angular/router";
import { NgModule } from "@angular/core";
import { ClientRoutingModule } from "./client-routing.module";

@NgModule({
	imports: [
		RouterModule,
		ClientRoutingModule
	],
	exports: [
		RouterModule
	]
})
export class ClientModule { }