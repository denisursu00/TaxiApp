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
import { DocumentWorkflowHistoryService } from "./service/document-workflow-history.service";
import { 
	OrganizationService, 
	DocumentService, 
	GroupService, 
	AclService, 
	DocumentLocationService, 
	DocumentSearchService, 
	DocumentTypeService,
	FolderService,
	WorkflowService,
	AttachmentService,
	ReplacementProfilesService,
	AppConstantsService,
	MimeTypeService,
	DocumentTemplateService,
	NomenclatorService,
	ProjectService,
	RegistruDocumenteJustificativePlatiService,
	ComisieSauGLService,
	RegistruIntrariIesiriService,
	ReportService,
	AlteDeconturiService,
	CursValutarService,
	AuthService
} from "./service";
import { 
	OrganizationSelectorComponent, 
	GroupSelectorComponent, 
	PermissionListComponent, 
	PermissionManagerComponent, 
	DocumentTypeSelectorComponent,
	MessagesWindowComponent,
	WorkflowStatesSelectionWindowComponent,
	WorkflowStatesSelectionFieldComponent,
	GroupSelectionFieldComponent,
	NomenclatorSelectionFieldComponent,
	NomenclatorUiAttributesSelectionFieldComponent,
	NomenclatorValueFieldComponent,
	NomenclatorDataTableComponent,
	NomenclatorDataToolbarComponent,
	NomenclatorDataWindowComponent,
	NomenclatorAttributeComponent,
	LoadingComponent,
	CalendarSelectionFieldComponent,
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
import { DocumentStatesSelectorComponent } from "./components/document-states-selector/document-states-selector.component";
import { UserSelectorComponent } from "./components/user-selector/user-selector.component";
import { ReplacementProfilesComponent } from "./components/replacement-profiles";
import { ReplacementProfilesWindowComponent } from "./components/replacement-profiles/replacement-profiles-window/replacement-profiles-window.component";
import { TableModule } from "primeng/table";
import { MimeTypeSelectorComponent } from "./components/mime-type-selector/mime-type-selector.component";
import { PicklistComponent } from "./components/picklist/picklist.component";
import { OrganizationalStructureEntitiesSelectorComponent } from "./components/organizational-structure-entities-selector/organizational-structure-entities-selector.component";
import { DocumentAttachmentComponent } from "./components/document-attachment";
import { DocumentTemplateComponent } from "./components/document-template";
import { FolderSelectorComponent } from "./components/folder-selector";
import { NomenclatorValuesSelectionWindowComponent } from "./components/nomenclator-values-selection-window/nomenclator-values-selection-window.component";
import { CalendarService } from "./service/calendar.service";
import { CalendarSelectorComponent } from "./components/calendar-selector/calendar-selector.component";
import { UserProjectsSelectorComponent } from "./components/user-projects-selector/user-projects-selector.component";
import { TaskAssignmentsSelectorComponent } from "./components/task-assignments-selector/task-assignments-selector.component";
import { DateFormaterPipe } from "./pipe";
import { ParametersService } from "./service/parameters.service";
import { DeplasariDeconturiService } from "./service/deplasari-deconturi.service";
import { AuthRouteGuard, AuthManager } from "./auth"; 
import { HttpClientModule } from "@angular/common/http";
import { ToastModule } from "primeng/toast";
import { DateFormatterPipeFromStorageFormat } from "./pipe/date-formatter-from-storage-format";

@NgModule({
	declarations: [
		OrganizationSelectorComponent,
		GroupSelectorComponent,
		PermissionListComponent,
		DocumentTypeSelectorComponent,
		PermissionManagerComponent,
		DocumentStatesSelectorComponent,
		UserSelectorComponent,
		ReplacementProfilesComponent,
		ReplacementProfilesWindowComponent,
		MimeTypeSelectorComponent,
		PicklistComponent,
		OrganizationalStructureEntitiesSelectorComponent,
		DocumentAttachmentComponent,
		DocumentTemplateComponent,
		FolderSelectorComponent,
		MessagesWindowComponent,
		WorkflowStatesSelectionWindowComponent,
		WorkflowStatesSelectionFieldComponent,
		GroupSelectionFieldComponent,
		NomenclatorSelectionFieldComponent,
		NomenclatorValuesSelectionWindowComponent,
		NomenclatorUiAttributesSelectionFieldComponent,
		NomenclatorValueFieldComponent,
		CalendarSelectorComponent,
		NomenclatorDataTableComponent,
		UserProjectsSelectorComponent,
		TaskAssignmentsSelectorComponent,
		NomenclatorDataToolbarComponent,
		NomenclatorDataWindowComponent,
		NomenclatorAttributeComponent,
		DateFormaterPipe,
		DateFormatterPipeFromStorageFormat,
		LoadingComponent,
		CalendarSelectionFieldComponent,
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
		OrganizationSelectorComponent,
		GroupSelectorComponent,
		PermissionListComponent,
		DocumentTypeSelectorComponent,
		PermissionManagerComponent,
		DocumentStatesSelectorComponent,
		UserSelectorComponent,
		ReplacementProfilesComponent,
		ReplacementProfilesWindowComponent,
		MimeTypeSelectorComponent,
		PicklistComponent,
		OrganizationalStructureEntitiesSelectorComponent,
		DocumentAttachmentComponent,
		DocumentTemplateComponent,
		FolderSelectorComponent,
		MessagesWindowComponent,
		WorkflowStatesSelectionWindowComponent,
		WorkflowStatesSelectionFieldComponent,
		GroupSelectionFieldComponent,
		NomenclatorSelectionFieldComponent,
		NomenclatorValuesSelectionWindowComponent,
		NomenclatorUiAttributesSelectionFieldComponent,
		NomenclatorValueFieldComponent,
		CalendarSelectorComponent,
		NomenclatorDataTableComponent,
		UserProjectsSelectorComponent,
		TaskAssignmentsSelectorComponent,
		NomenclatorDataToolbarComponent,
		NomenclatorDataWindowComponent,
		NomenclatorAttributeComponent,
		DateFormaterPipe,
		DateFormatterPipeFromStorageFormat,
		LoadingComponent,
		CalendarSelectionFieldComponent,
		ConfirmationWindowComponent
	],
	providers: [
		JsonMapper,
		DatePipe,
		MessageService,
		DocumentSearchService,
		DocumentLocationService,
		FolderService,
		ConfirmationService,
		ConfirmationUtils,
		TranslateUtils,
		OrganizationService,
		DocumentService,
		GroupService,
		DocumentTypeService,
		AclService,
		DocumentWorkflowHistoryService,
		WorkflowService,
		AttachmentService,
		ReplacementProfilesService,
		AppConstantsService,
		MimeTypeService,
		DocumentTemplateService,
		NomenclatorService,
		CalendarService,
		ProjectService,
		RegistruDocumenteJustificativePlatiService,
		ComisieSauGLService,
		RegistruIntrariIesiriService,
		ReportService,
		ParametersService,
		AlteDeconturiService,
		DeplasariDeconturiService,
		CursValutarService,
		AuthService	
	]
})
export class SharedModule {
}
