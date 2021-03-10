import { NgModule } from "@angular/core";
import { CommonModule } from "@angular/common";
import { FormsModule, ReactiveFormsModule } from "@angular/forms";
import { HttpModule } from "@angular/http";
import { RouterModule } from "@angular/router";
import { SharedModule } from "@app/shared";
import { TableModule } from "primeng/table";
import {
	InputTextModule,
	InputTextareaModule,
	DialogModule,
	ButtonModule,
	DataTableModule,
	MultiSelectModule,
	DropdownModule,
	TreeModule,
	ConfirmDialogModule,
	FileUploadModule,
	TabViewModule,
	AccordionModule,
	FieldsetModule,
	CalendarModule,
	ProgressSpinnerModule,
	MessageModule,
	MessagesModule,
	TieredMenuModule,
	KeyFilterModule,
	ContextMenuModule,
	PickListModule,
	CheckboxModule,
	PanelModule,
	TooltipModule,
	SelectButtonModule,
	InputSwitchModule,
	ChartModule,
	SpinnerModule,
	BlockUIModule
} from "primeng/primeng";
import { DataViewModule } from "primeng/dataview";
import { ScheduleModule } from "primeng/schedule";
import { TreeDragDropService } from "primeng/components/common/treedragdropservice";
import { RegistruDocumenteJustificativePlatiComponent } from "./registru-documente-justificative-plati";
import { RegistruDocumenteJustificativePlatiWindowComponent } from "./registru-documente-justificative-plati/registru-documente-justificative-plati-window/registru-documente-justificative-plati-window.component";
import { ComisiiSauGLComponent } from "./comisii-sau-gl";
import { ReprezentantiComisieSauGLWindowComponent } from "./comisii-sau-gl/reprezentanti-comisie-sau-gl-window";
import { ReprezentantiComisieSauGLGeneralTabContentComponent } from "./comisii-sau-gl/reprezentanti-comisie-sau-gl-window";
import { ReprezentantiComisieSauGLMembriTabContentComponent } from "./comisii-sau-gl/reprezentanti-comisie-sau-gl-window";
import { MembruReprezentantiComisieSauGLWindowComponent, DiplomaMembruReprezentantiComisieSauGLWindowComponent } from "./comisii-sau-gl/reprezentanti-comisie-sau-gl-window";
import { AnulareDocumentJustificativPlatiWindowComponent } from "./registru-documente-justificative-plati/anulare-document-justificativ-plati-window/anulare-document-justificativ-plati-window.component";
import { RegistruIntrariIesiriComponent } from "./registru-intrari-iesiri/registru-intrari-iesiri.component";
import { RegistruIesiriComponent } from "./registru-intrari-iesiri/registru-iesiri/registru-iesiri.component";
import { RegistruIntrariComponent } from "./registru-intrari-iesiri/registru-intrari/registru-intrari.component";
import { RegistruIntrariWindowComponent } from "./registru-intrari-iesiri/registru-intrari/registru-intrari-window/registru-intrari-window.component";
import { RegistruIesiriDestinatarWindowComponent } from "./registru-intrari-iesiri/registru-iesiri/registru-iesiri-destinatar-window/registru-iesiri-destinatar-window.component";
import { RegistruIesiriWindowComponent } from "./registru-intrari-iesiri/registru-iesiri/registru-iesiri-window/registru-iesiri-window.component";
import { RegistruIesiriDestinatarManagerComponent } from "./registru-intrari-iesiri/registru-iesiri/registru-iesiri-destinatar-manager/registru-iesiri-destinatar-manager.component";
import { RegistruIesiriSelectorComponent } from "./registru-intrari-iesiri/registru-iesiri/registru-iesiri-selector/registru-iesiri-selector.component";
import { AnulareRegistruIntrariIesiriWindowComponent } from "./registru-intrari-iesiri/anulare-registru-intrari-iesiri";
import { RegistruIntrariSelectorComponent } from "./registru-intrari-iesiri/registru-intrari/registru-intrari-selector";
import { RegistruIesiriFieldComponent } from "./registru-intrari-iesiri/registru-iesiri/registru-iesiri-field/registru-iesiri-field.component";
import { RegistruIntrariFieldComponent } from "./registru-intrari-iesiri/registru-intrari/registru-intrari-field/registru-intrari-field.component";
import { AlteDeconturiComponent } from "./alte-deconturi";
import { AlteDeconturiWindowComponent } from "./alte-deconturi/alte-deconturi-window/alte-deconturi-window.component";
import { AlteDeconturiCheltuieliManagerComponent } from "./alte-deconturi/alte-deconturi-cheltuieli-manager/alte-deconturi-cheltuieli-manager.component";
import { AlteDeconturiCheltuieliWindowComponent } from "./alte-deconturi/alte-deconturi-cheltuieli-window/alte-deconturi-cheltuieli-window.component";
import { AnulareAlteDeconturiWindowComponent } from "./alte-deconturi/anulare-alte-deconturi-window";
import { DeplasariDeconturiComponent } from "./deplasari-deconturi";
import { DeplasariDeconturiWindowComponent } from "./deplasari-deconturi/deplasari-deconturi-window/deplasari-deconturi-window.component";
import { ClientCommonModule } from "@app/client/common";
import { CheltuieliArbManagerComponent } from "./deplasari-deconturi/deplasari-deconturi-window/cheltuieli-arb-manager/cheltuieli-arb-manager.component";
import { CheltuieliArbWindowComponent } from "./deplasari-deconturi/deplasari-deconturi-window/cheltuieli-arb-window/cheltuieli-arb-window.component";
import { CheltuieliReprezentantArbManagerComponent } from "./deplasari-deconturi/deplasari-deconturi-window/cheltuieli-reprezentant-arb-manager/cheltuieli-reprezentant-arb-manager.component";
import { CheltuieliReprezentantArbWindowComponent } from "./deplasari-deconturi/deplasari-deconturi-window/cheltuieli-reprezentant-arb-window/cheltuieli-reprezentant-arb-window.component";
import { AnulareDeplasareWindowComponent } from "./deplasari-deconturi/deplasari-deconturi-window/anulare-deplasare-window";
import { CheltuieliArbManagerWindowComponent } from "./deplasari-deconturi/deplasari-deconturi-window/cheltuieli-arb-manager-window/cheltuieli-arb-manager-window.component";
import { CheltuieliReprezentantArbManagerWindowComponent } from "./deplasari-deconturi/deplasari-deconturi-window/cheltuieli-reprezentant-arb-manager-window/cheltuieli-reprezentant-arb-manager-window.component";
import { RegistersRoutingModule } from "./registers-routing.module";
import { TranslateModule, TranslateLoader } from "@ngx-translate/core";
import { AppTranslateLoader } from "@app/shared";
import { RegistrIesiriSelectorDestinatariFilterWindowComponent } from "./registru-intrari-iesiri/registru-iesiri/registru-iesiri-selector/registru-iesiri-selector-destinatari-filter-window/registru-iesiri-selector-destinatari-filter-window.component";

@NgModule({
	imports: [
		TranslateModule.forChild({
			loader: {
				provide: TranslateLoader,
				useClass: AppTranslateLoader
			}
		}),
		CommonModule,
		RouterModule,
		HttpModule,
		SharedModule,
		InputTextModule,
		InputTextareaModule,
		DialogModule,
		ButtonModule,
		BlockUIModule,
		DataTableModule,
		CommonModule,
		MultiSelectModule,
		FormsModule,
		ReactiveFormsModule,
		DropdownModule,
		TreeModule,
		TabViewModule,
		AccordionModule,
		FileUploadModule,
		MessageModule,
		MessagesModule,
		FieldsetModule,
		CalendarModule,
		ProgressSpinnerModule,
		KeyFilterModule,
		TableModule,
		TieredMenuModule,
		ContextMenuModule,
		PickListModule,
		CheckboxModule,
		PanelModule,
		TooltipModule,
		ScheduleModule,
		SelectButtonModule,
		InputSwitchModule,
		DataViewModule,
		SpinnerModule,
		RegistersRoutingModule,
		ClientCommonModule
	],
	declarations: [
		RegistruDocumenteJustificativePlatiComponent,
		RegistruDocumenteJustificativePlatiWindowComponent,
		ComisiiSauGLComponent,
		ReprezentantiComisieSauGLWindowComponent,
		ReprezentantiComisieSauGLGeneralTabContentComponent,
		ReprezentantiComisieSauGLMembriTabContentComponent,
		MembruReprezentantiComisieSauGLWindowComponent,
		DiplomaMembruReprezentantiComisieSauGLWindowComponent,
		AnulareDocumentJustificativPlatiWindowComponent,
		RegistruIntrariIesiriComponent,
		RegistruIesiriComponent,
		RegistruIntrariComponent,
		RegistruIntrariWindowComponent,
		RegistruIesiriDestinatarWindowComponent,
		RegistrIesiriSelectorDestinatariFilterWindowComponent,
		RegistruIesiriWindowComponent,
		RegistruIesiriDestinatarManagerComponent,
		RegistruIesiriSelectorComponent,
		AnulareRegistruIntrariIesiriWindowComponent,
		RegistruIntrariSelectorComponent,
		RegistruIesiriFieldComponent,
		RegistruIntrariFieldComponent,
		AlteDeconturiComponent,
		AlteDeconturiWindowComponent,
		AlteDeconturiCheltuieliManagerComponent,
		AlteDeconturiCheltuieliWindowComponent,
		AnulareAlteDeconturiWindowComponent,
		DeplasariDeconturiComponent,
		DeplasariDeconturiWindowComponent,
		CheltuieliArbManagerComponent,
		CheltuieliArbWindowComponent,
		CheltuieliReprezentantArbManagerComponent,
		CheltuieliReprezentantArbWindowComponent,
		AnulareDeplasareWindowComponent,
		CheltuieliArbManagerWindowComponent,
		CheltuieliReprezentantArbManagerWindowComponent
	]
})
export class RegistersModule { }
