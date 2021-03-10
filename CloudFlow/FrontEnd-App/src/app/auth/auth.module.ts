import { NgModule } from "@angular/core";
import { CommonModule } from "@angular/common";
import { FormsModule, ReactiveFormsModule } from "@angular/forms";
import { AuthRoutingModule } from "./auth-routing.module";
import { LoginComponent } from "./login/login.component";
import { TranslateModule, TranslateLoader } from "@ngx-translate/core";
import { SharedModule, AppTranslateLoader } from "@app/shared";
import { ChangePasswordComponent } from "./change-password";

@NgModule({
	imports: [
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
		AuthRoutingModule
	],
	declarations: [
		LoginComponent,
		ChangePasswordComponent
	],
	providers: [
	]
})
export class AuthModule {
}
