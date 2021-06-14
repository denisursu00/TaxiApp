import { NgModule } from "@angular/core";
import { CommonModule } from "@angular/common";
import { FormsModule, ReactiveFormsModule } from "@angular/forms";
import { AuthRoutingModule } from "./auth-routing.module";
import { LoginComponent } from "./login/login.component";
import { TranslateModule, TranslateLoader } from "@ngx-translate/core";
import { SharedModule, AppTranslateLoader } from "@app/shared";
import { RegisterComponent } from './register/register.component';
import { InputTextModule, KeyFilterModule } from "primeng/primeng";

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
		KeyFilterModule,
		InputTextModule,
		SharedModule,
		AuthRoutingModule
	],
	declarations: [
		LoginComponent,
		RegisterComponent
	],
	providers: [
	]
})
export class AuthModule {
}
