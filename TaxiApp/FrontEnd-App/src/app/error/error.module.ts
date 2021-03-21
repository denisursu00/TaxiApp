import { NgModule } from "@angular/core";
import { CommonModule } from "@angular/common";
import { ErrorRoutingModule } from "./error-routing.module";
import { PageNotFoundComponent } from "./page-not-found/page-not-found.component";
import { AccessDeniedComponent } from "./access-denied/access-denied.component";
import { TranslateModule, TranslateLoader } from "@ngx-translate/core";
import { SharedModule, AppTranslateLoader } from "@app/shared";

@NgModule({
	imports: [
		TranslateModule.forChild({
			loader: {
				provide: TranslateLoader,
				useClass: AppTranslateLoader
			}
		}),
		CommonModule,
		ErrorRoutingModule
	],
	declarations: [
		PageNotFoundComponent, 
		AccessDeniedComponent
	]
})
export class ErrorModule {
}
