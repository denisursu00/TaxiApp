import { NgModule } from "@angular/core";
import { HttpModule } from "@angular/http";
import { RouterModule } from "@angular/router";
import { SharedModule, DocumentTypeModel } from "@app/shared";
import { MimeTypesComponent } from "./mime-types/mime-types.component";
import { FormsModule, ReactiveFormsModule } from "@angular/forms";
import { OrganizationalStructureComponent } from "./organizational-structure/organizational-structure.component";
import { SplitButtonModule } from "primeng/splitbutton";
import { MimeTypeWindowComponent } from "./mime-types/mime-type-window/mime-type-window.component";
import { CommonModule } from "@angular/common";
import { TableModule } from "primeng/table";
import { SelectButtonModule } from "primeng/selectbutton";
import {
	ButtonModule,
	InputTextModule,
	DialogModule,
	TabViewModule,
	BlockUIModule,
	ProgressSpinnerModule,
	RadioButtonModule,
	FieldsetModule,
	CheckboxModule,
	InputTextareaModule,
	PickListModule,
	TieredMenuModule,
	DropdownModule,
	AutoCompleteModule,
	OverlayPanelModule,
	KeyFilterModule,
	ListboxModule,
	CalendarModule,
	TooltipModule,
	ColorPickerModule,
	AccordionModule,
	SpinnerModule,
	PasswordModule
} from "primeng/primeng";
import { DocumentTypesComponent } from "./document-types";
import { RolesAndPermissionsComponent } from "./roles-and-permissions";
import { 
	DocumentTypeWindowComponent,
	DocumentTypeGeneralTabContentComponent,
	DocumentTypeInitiatorsTabContentComponent,
	DocumentTypeAttachmentsTabContentComponent,
	DocumentTypeMetadataTabContentComponent,
	MetadataDefinitionComponent,
	MetadataDefinitionsManagerComponent,
	MetadataDefinitionsOfMetadataCollectionWindowComponent,
	MetadataDefinitionTypeFieldComponent,
	MetadataDefinitionsOfMetadataCollectionFieldComponent,
	MetadataListItemsFieldComponent,
	MetadataListItemWindowComponent,
	MetadataDefinitionDefaultValueFieldComponent,
	NomenclatorMetadataDefinitionValueSelectionFiltersFieldComponent
} from "./document-types";
import {
	WorkflowsComponent,
	WorkflowWindowComponent,
	WorkflowGeneralTabContentComponent,
	WorkflowSupervisorsTabContentComponent,
	WorkflowStatesTabContentComponent,
	WorkflowTransitionsTabContentComponent
} from "./workflows";
import { TransitionNotificationsComponent } from "./workflows/workflow-window/workflow-transitions-tab-content/transition-notifications/transition-notifications.component";
import { TransitionRoutingTypeSelectorComponent } from "./workflows/workflow-window/workflow-transitions-tab-content/transition-routing-type-selector/transition-routing-type-selector.component";
import { UserWindowComponent } from "./organizational-structure/user-window";
import { OrganizationUnitWindowComponent } from "./organizational-structure/organization-unit-window";
import { DirectoryUserSearchWindowComponent } from "./organizational-structure/directory-user-search-window";
import { UsersFoundInDirectoryWindowComponent } from "./organizational-structure/users-found-in-directory-window";
import { TransitionNotificationWindowComponent } from "./workflows/workflow-window/workflow-transitions-tab-content/transition-notifications/transition-notification-window/transition-notification-window.component";
import { GroupWindowComponent } from "./groups/group-window/group-window.component";
import { WorkflowTransitionDestinationGroupWindowComponent } from "./workflows/workflow-window/workflow-transitions-tab-content/workflow-transition-destination-group-window/workflow-transition-destination-group-window.component";
import { WorkflowTransitionOrganizationUnitWindowComponent } from "./workflows/workflow-window/workflow-transitions-tab-content/workflow-transition-organization-unit-window/workflow-transition-organization-unit-window.component";
import { UserMetadataFromDocumentTypesSelectorComponent } from "./workflows/workflow-window/workflow-transitions-tab-content/user-metadata-from-document-types-selector/user-metadata-from-document-types-selector.component";
import { WorkflowGraphWindowComponent } from "./workflows/workflow-graph-window/workflow-graph-window.component";
import { GroupsComponent } from "./groups/groups.component";
import { NomenclatorsComponent, NomenclatorWindowComponent, NomenclatorGeneralTabContentComponent, NomenclatorAttributesTabContentComponent, NomenclatorAttributesManagerComponent, NomenclatorAttributesListComponent, NomenclatorAttributeDefinitionComponent } from "./nomenclators";
import { NomenclatorAttributeDefinitionDefaultValueFieldComponent } from "./nomenclators";
import { NomenclatorAttributeDefinitionSelectionFiltersFieldComponent } from "./nomenclators";
import { CalendarsComponent } from "./calendars/calendars.component";
import { DataViewModule } from "primeng/dataview";
import { CalendarWindowComponent } from "./calendars/calendar-window/calendar-window.component";
import { CalendarGeneralTabContentComponent } from "./calendars/calendar-window/calendar-general-tab-content/calendar-general-tab-content.component";
import { CalendarUsersRightsTabContentComponent } from "./calendars/calendar-window/calendar-users-rights-tab-content/calendar-users-rights-tab-content.component";
import { CalendarUserRightsSelectorComponent } from "./calendars/calendar-window/calendar-users-rights-tab-content/calendar-user-rights-selector/calendar-user-rights-selector.component";
import { ParametersComponent } from "./parameters/parameters.component";
import { ParameterWindowComponent } from "./parameters/parameter-window/parameter-window.component";
import { AdminRoutingModule } from "./admin-routing.module";

@NgModule({
	declarations: [
		MimeTypesComponent,
		WorkflowsComponent,
		OrganizationalStructureComponent,
		MimeTypeWindowComponent,
		UserWindowComponent,
		OrganizationUnitWindowComponent,
		DirectoryUserSearchWindowComponent,
		UsersFoundInDirectoryWindowComponent,
		DocumentTypesComponent,
		DocumentTypeWindowComponent,
		DocumentTypeGeneralTabContentComponent,
		DocumentTypeInitiatorsTabContentComponent,
		DocumentTypeAttachmentsTabContentComponent,
		DocumentTypeMetadataTabContentComponent,
		MetadataDefinitionComponent,
		MetadataDefinitionsManagerComponent,
		MetadataDefinitionsOfMetadataCollectionWindowComponent,
		MetadataDefinitionTypeFieldComponent,
		MetadataDefinitionsOfMetadataCollectionFieldComponent,
		MetadataListItemsFieldComponent,
		MetadataListItemWindowComponent,
		MetadataDefinitionDefaultValueFieldComponent,
		NomenclatorMetadataDefinitionValueSelectionFiltersFieldComponent,
		WorkflowWindowComponent,
		WorkflowGeneralTabContentComponent,
		WorkflowSupervisorsTabContentComponent,
		WorkflowStatesTabContentComponent,
		WorkflowTransitionsTabContentComponent,
		TransitionNotificationsComponent,
		TransitionNotificationWindowComponent,
		GroupsComponent,
		GroupWindowComponent,
		TransitionRoutingTypeSelectorComponent,
		WorkflowTransitionDestinationGroupWindowComponent,
		WorkflowTransitionOrganizationUnitWindowComponent,
		UserMetadataFromDocumentTypesSelectorComponent,
		WorkflowGraphWindowComponent,
		CalendarsComponent,
		CalendarWindowComponent,
		CalendarGeneralTabContentComponent,
		CalendarUsersRightsTabContentComponent,
		NomenclatorsComponent,
		NomenclatorWindowComponent,
		NomenclatorGeneralTabContentComponent,
		NomenclatorAttributesTabContentComponent,
		NomenclatorAttributesManagerComponent,
		NomenclatorAttributesListComponent,
		NomenclatorAttributeDefinitionComponent,
		NomenclatorAttributeDefinitionDefaultValueFieldComponent,
		NomenclatorAttributeDefinitionSelectionFiltersFieldComponent,
		CalendarUserRightsSelectorComponent,
		ParametersComponent,
		ParameterWindowComponent,
		RolesAndPermissionsComponent
	],
	imports: [
		RouterModule,
		HttpModule,
		SharedModule,
		FormsModule,
		ReactiveFormsModule,
		SplitButtonModule,
		ButtonModule,
		InputTextModule,
		DialogModule,
		TabViewModule,
		CommonModule,
		CheckboxModule,
		DropdownModule,
		TableModule,
		BlockUIModule,
		ProgressSpinnerModule,
		RadioButtonModule,
		FieldsetModule,
		InputTextareaModule,
		PickListModule,
		DropdownModule,
		TieredMenuModule,
		AutoCompleteModule,
		OverlayPanelModule,
		KeyFilterModule,
		ListboxModule,
		CalendarModule,
		DataViewModule,
		ColorPickerModule,
		SelectButtonModule,
		TooltipModule,
		AccordionModule,
		SpinnerModule,
		PasswordModule,
		AdminRoutingModule
	],
	exports: [
		RouterModule,
		HttpModule
	]
})
export class AdminModule {
}
