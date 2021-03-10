import { Component, OnInit, Input } from "@angular/core";
import { NomenclatorService, MessageDisplayer, TranslateUtils, NomenclatorConstants, AppError, UiUtils } from "@app/shared";
import { FormBuilder, FormGroup } from "@angular/forms";
import { PrezentaOnlineService } from "@app/shared/service/prezenta-online.service";
import { DocumentPrezentaOnlineModel } from "@app/shared/model/prezenta-online";
import { PrezentaOnlineWindowInputData } from "./prezenta-online-window/prezenta-online-window.component";
@Component({
	selector: "app-prezenta-online",
	templateUrl: "./prezenta-online.component.html",
})
export class PrezentaOnlineComponent {

	private prezentaOnlineService: PrezentaOnlineService;
	private messageDisplayer: MessageDisplayer;
	
	public loading: boolean;
	public prezentaOnlineWindowVisible: boolean = false;
	public isMobileDevices: boolean = false;

	public scrollHeight: string;

	public documentePrezenta: DocumentPrezentaOnlineModel[];
	public selectedDocumentPrezenta: DocumentPrezentaOnlineModel;
	public prezentaOnlineWindowInputData: PrezentaOnlineWindowInputData;
	
	public constructor(prezentaOnlineService: PrezentaOnlineService,  messageDisplayer: MessageDisplayer) {
		this.prezentaOnlineService = prezentaOnlineService;
		this.messageDisplayer = messageDisplayer;

		this.init();
	} 

	private init(): void {
		this.loading = false;

		this.scrollHeight = (window.innerHeight - 200) + "px";
		this.isMobileDevices = UiUtils.isMobileDevices();
		
		this.getDocumentsPrezenta();
	}

	
	private lock(): void {
        this.loading = true;
    }

    private unlock(): void {
        this.loading = false;
	}
	
	private getDocumentsPrezenta(): void {
		this.lock();
		this.prezentaOnlineService.getAllDocumentsPrezenta({
			onSuccess: (documente: DocumentPrezentaOnlineModel[]) => {
				this.documentePrezenta = documente;
				this.unlock();
				console.log(this.documentePrezenta);
				
			},
			onFailure: (appError: AppError) => {
				this.unlock();
				this.messageDisplayer.displayAppError(appError);
			}
		});
	}

	public onRowSelect(): void {
		this.prezentaOnlineWindowVisible = true;

		this.prezentaOnlineWindowInputData = new PrezentaOnlineWindowInputData();
		this.prezentaOnlineWindowInputData.documentId = this.selectedDocumentPrezenta.documentId;
		this.prezentaOnlineWindowInputData.documentLocationRealName = this.selectedDocumentPrezenta.documentLocationRealName;
		this.prezentaOnlineWindowInputData.numeDocument = this.selectedDocumentPrezenta.numeDocument;
		this.prezentaOnlineWindowInputData.comisieGlId = this.selectedDocumentPrezenta.comisieGlId;
	}

	public onRowUnselect(): void {
	}

	public onPezentaOnlineWindowClose() {
		this.prezentaOnlineWindowVisible = false;
		this.getDocumentsPrezenta();
	}

	public onParticipantiImportati() {
		this.prezentaOnlineWindowVisible = false;
		this.getDocumentsPrezenta();
	}


}
