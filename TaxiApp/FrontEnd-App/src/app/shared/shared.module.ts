import { NgModule } from "@angular/core";
import { HttpModule } from "@angular/http";
import { RouterModule } from "@angular/router";
import { ApiCaller } from "./api-caller";
import { JsonMapper } from "./json-mapper";
import { DatePipe, CommonModule } from "@angular/common";
import { MessageService } from "primeng/components/common/messageservice";
import { MessageDisplayer } from "./message-displayer";
import { ConfirmationUtils, TranslateUtils } from "./utils";
import { ConfirmationService } from "primeng/api";
import { FormsModule, ReactiveFormsModule } from "@angular/forms";
import { TranslateModule } from "@ngx-translate/core";
import { 
	AclService, 
	AppConstantsService,
	AuthService,
	CarsService,
	ClientsService,
	DriversService,
	OrganizationService,
	RidesService
} from "./service";
import { 
	MessagesWindowComponent,
	LoadingComponent,
	ConfirmationWindowComponent
} from "./components";
import { 
	ToolbarModule,
	ButtonModule,
	MenuItem,
	PanelMenuModule,
	InputTextModule,
	TreeModule,
	ListboxModule,
	DropdownModule,
	DataTableModule,
	RadioButtonModule,
	AccordionModule,
	PickListModule,
	FileUploadModule,
	DialogModule,
	MultiSelectModule,
	CalendarModule,
	CheckboxModule,
	InputTextareaModule,
	MessageModule,
	MessagesModule,
	KeyFilterModule,
	SelectButtonModule,
	AutoCompleteModule,
	TooltipModule,
	BlockUIModule,
	ProgressSpinnerModule,
	ChipsModule,
	ConfirmDialogModule
} from "primeng/primeng";
import { TableModule } from "primeng/table";
import { DateFormaterPipe } from "./pipe";
import { ParametersService } from "./service/parameters.service";
import { AuthRouteGuard, AuthManager } from "./auth"; 
import { HttpClientModule } from "@angular/common/http";
import { ToastModule } from "primeng/toast";
import { DateFormatterPipeFromStorageFormat } from "./pipe/date-formatter-from-storage-format";

@NgModule({
	declarations: [
		MessagesWindowComponent,
		DateFormaterPipe,
		DateFormatterPipeFromStorageFormat,
		LoadingComponent,
		ConfirmationWindowComponent
	],
	imports: [
		RouterModule,
		HttpModule,
		HttpClientModule,
		ReactiveFormsModule,
		TranslateModule,
		ToolbarModule,
		PanelMenuModule,
		ButtonModule,
		InputTextModule,
		TreeModule,
		FormsModule,
		ListboxModule,
		DataTableModule,
		RadioButtonModule,
		DropdownModule,
		AccordionModule,
		PickListModule,
		CommonModule,
		FileUploadModule,
		TableModule,
		DialogModule,
		MultiSelectModule,
		CalendarModule,
		CheckboxModule,
		MessageModule,
		MessagesModule,
		ToastModule,
		InputTextareaModule,
		KeyFilterModule,
		SelectButtonModule,
		AutoCompleteModule,
		TooltipModule,
		BlockUIModule,
		ProgressSpinnerModule,
		ChipsModule,
		ConfirmDialogModule
	],
	exports: [
		RouterModule,
		HttpModule,
		TranslateModule,
		ConfirmDialogModule,
		MessagesWindowComponent,
		DateFormaterPipe,
		DateFormatterPipeFromStorageFormat,
		LoadingComponent,
		ConfirmationWindowComponent
	],
	providers: [
		JsonMapper,
		DatePipe,
		MessageService,
		ConfirmationService,
		ConfirmationUtils,
		TranslateUtils,
		AclService,
		AppConstantsService,
		ParametersService,
		DriversService,
		OrganizationService,
		CarsService,
		RidesService,
		ClientsService,
		AuthService	
	]
})
export class SharedModule {
}
