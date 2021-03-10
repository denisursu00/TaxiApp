import { NgModule } from "@angular/core";
import { CommonModule } from "@angular/common";
import { MyDocumentActivitiesRoutingModule } from "./my-document-activities-routing.module";
import { ClientCommonModule } from "@app/client/common";

@NgModule({
	imports: [
		CommonModule,
		ClientCommonModule,
		MyDocumentActivitiesRoutingModule
	],
	declarations: []
})
export class MyDocumentActivitiesModule { }
