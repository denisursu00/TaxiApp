import { NgModule } from "@angular/core";
import { CommonModule } from "@angular/common";
import { FormsModule, ReactiveFormsModule } from "@angular/forms";
import { TranslateModule, TranslateLoader } from "@ngx-translate/core";
import { AppTranslateLoader, SharedModule } from "@app/shared";
import { PrezentaOnlineRoutingModule } from "./prezenta-online-routing.module";
import { PrezentaOnlineComponent } from "./prezenta-online.component";
import { RouterModule } from "@angular/router";
import { HttpModule } from "@angular/http";
import { PrezentaOnlineService } from "@app/shared/service/prezenta-online.service";
import { TableModule } from "primeng/table";
import { InputTextModule } from "primeng/inputtext";
import { PrezentaOnlineWindowComponent } from "./prezenta-online-window/prezenta-online-window.component";
import { DialogModule } from "primeng/dialog";
import { ButtonModule } from "primeng/button";
import { ParticipantWindowComponent } from "./prezenta-online-window/participant-window";
import { DropdownModule } from "primeng/dropdown";

@NgModule({
	imports: [
		RouterModule,
		HttpModule,
		TranslateModule.forChild({
			loader: {
				provide: TranslateLoader,
				useClass: AppTranslateLoader
			}
		}),
		CommonModule,
		FormsModule,
		ReactiveFormsModule,
		SharedModule,
		PrezentaOnlineRoutingModule,
		TableModule,
		InputTextModule,
		DialogModule,
		ButtonModule,
		DropdownModule
	],
	declarations: [
		PrezentaOnlineComponent,
		PrezentaOnlineWindowComponent,
		ParticipantWindowComponent
	],
	providers: [
		PrezentaOnlineService
	],
	exports: [
		RouterModule,
		HttpModule
	]
})
export class PrezentaOnlineModule {
}
