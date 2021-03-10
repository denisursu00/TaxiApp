import { Component, Input, ViewChild, OnInit, Output, EventEmitter } from "@angular/core";
import { PermissionModel, DocumentLocationService, DocumentLocationModel, AppError, MessageDisplayer, BaseWindow } from "@app/shared";
import { DocumentLocationGeneralTabContentComponent } from "./document-location-general-tab-content/document-location-general-tab-content.component";
import { DocumentLocationSecurityTabContentComponent } from "./document-location-security-tab-content/document-location-security-tab-content.component";
import { Dialog } from "primeng/primeng";

@Component({
	selector: "app-document-location-window",
	templateUrl: "./document-location-window.component.html",
	styleUrls: ["./document-location-window.component.css"]
})
export class DocumentLocationWindowComponent extends BaseWindow implements OnInit {

	@Input()
	public documentLocationRealName: string;

	@Input()
	public mode: "new" | "edit";
	
	@Output()
	private windowClosed: EventEmitter<void>;

	@Output()
	private documentLocationSaved: EventEmitter<void>;

	@ViewChild(DocumentLocationGeneralTabContentComponent)
	private generalTabContent: DocumentLocationGeneralTabContentComponent;

	@ViewChild(DocumentLocationSecurityTabContentComponent)
	private securityTabContent: DocumentLocationSecurityTabContentComponent;

	private messageDisplayer: MessageDisplayer;
	private documentLocationService: DocumentLocationService;

	public saveButtonLabel: string;
	public cancelButtonLabel: string;

	public visible: boolean;

	public tabContentVisible: boolean = false;

	private documentLocation: DocumentLocationModel;

	public constructor(messageDisplayer: MessageDisplayer, documentLocationService: DocumentLocationService) {
		super();
		this.messageDisplayer = messageDisplayer;
		this.documentLocationService = documentLocationService;
		this.init();
	}

	private init(): void {
		this.visible = false;
		this.documentLocationSaved = new EventEmitter<void>();
		this.windowClosed = new EventEmitter<void>();
		
		
		this.documentLocation = new DocumentLocationModel();
	}

	public ngOnInit(): void {
		if (this.mode === "new") {
			this.prepareForAdd();
		} else if (this.mode === "edit") {
			this.prepareForEdit();
		}
	}
	
	public prepareForAdd(): void {
		this.visible = true;
		this.tabContentVisible = true;
	}

	public prepareForEdit(): void {
		this.documentLocationService.getDocumentLocationByRealName(this.documentLocationRealName, {
			onSuccess: (documentLocation: DocumentLocationModel): void => {
				this.documentLocation = documentLocation;
				this.visible = true;
				this.tabContentVisible = true;
			},
			onFailure: (appError: AppError): void => {
				this.messageDisplayer.displayAppError(appError);
				this.visible = false;
				this.windowClosed.emit();
			}
		});
	}

	public onSave(): void {
		if (!this.generalTabContent.isValid() || !this.securityTabContent.isValid()) {
			return;
		}

		this.prepareDocumentLocation();

		this.documentLocationService.saveDocumentLocation(this.documentLocation, {
			onSuccess: (documentLocationRealName: string): void => {
				this.messageDisplayer.displaySuccess("DOCUMENT_LOCATION_SAVED");
				this.visible = false;
				this.documentLocationSaved.emit();
			},
			onFailure: (appError: AppError): void => {
				this.messageDisplayer.displayAppError(appError);
			}
		});
	}

	public prepareDocumentLocation(): void {
		this.generalTabContent.populateDocumentLocation(this.documentLocation);
		this.securityTabContent.populateDocumentLocation(this.documentLocation);
		
	}

	public onHide(event: any): void {
		this.visible = false;
		this.windowClosed.emit();
	}
}
