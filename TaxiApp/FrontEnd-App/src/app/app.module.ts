import { NgModule, ModuleWithProviders, APP_INITIALIZER } from "@angular/core";
import { BrowserModule } from "@angular/platform-browser";
import { BrowserAnimationsModule } from "@angular/platform-browser/animations";
import { HttpModule } from "@angular/http";
import { RouterModule } from "@angular/router";
import { TranslateModule, TranslateLoader } from "@ngx-translate/core";
import { AppTranslateLoader } from "@app/shared";
import { AppComponent } from "./app.component";
import { GrowlModule, ConfirmDialogModule, AccordionModule, PanelModule, ScrollPanelModule,	MessageModule, MessagesModule, Dialog, DialogModule } from "primeng/primeng";
import { AppRightPanelComponent } from "./app.rightpanel.component";
import { AppMenuComponent, AppSubMenuComponent } from "./app.menu.component";
import { AppBreadcrumbComponent } from "./app.breadcrumb.component";
import { AppTopBarComponent } from "./app.topbar.component";
import { AppFooterComponent } from "./app.footer.component";
import { BreadcrumbService } from "./breadcrumb.service";
import { AppRoutingModule } from "./app-routing.module";
import { SharedModule } from "@app/shared";
import { AppInitializer } from "./app-initializer";
import { AppMenuService } from "./app.menu.service";
import { ApiCaller } from "@app/shared/api-caller";
import { Router } from "@angular/router";
import { HTTP_INTERCEPTORS } from "@angular/common/http";
import { AuthInterceptor } from "@app/shared/auth/auth-interceptor";
import { ToastModule } from "primeng/toast";

export function initialize(initializer: AppInitializer) {
	return () => initializer.initialize();
}

@NgModule({
	declarations: [
		AppComponent,
		AppRightPanelComponent,
		AppMenuComponent,
		AppSubMenuComponent,
		AppBreadcrumbComponent,
		AppTopBarComponent,
		AppFooterComponent
	],
	imports: [
		RouterModule,
		TranslateModule.forRoot({
			loader: {
				provide: TranslateLoader,
				useClass: AppTranslateLoader
			}
		}),
		BrowserModule,
		BrowserAnimationsModule,
		HttpModule,
		ConfirmDialogModule,
		AccordionModule,
		PanelModule,
		ScrollPanelModule,		
		GrowlModule,
		MessageModule,
		MessagesModule,
		ToastModule,
		SharedModule,
		DialogModule,
		AppRoutingModule
	],
	providers: [
		{
			provide: HTTP_INTERCEPTORS,
			useClass: AuthInterceptor,
			multi: true
		},
		BreadcrumbService,
		AppMenuService,
		AppInitializer,
		{
			provide: APP_INITIALIZER, 
			useFactory: initialize, 
			deps: [AppInitializer], 
			multi: true 
		}
	],
	bootstrap: [AppComponent]
})
export class AppModule {
}