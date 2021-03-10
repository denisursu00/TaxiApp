import { Component, OnInit, Input, Output, EventEmitter, ViewChild } from "@angular/core";
import { Dialog, DataTable } from "primeng/primeng";
import { PrezentaOnlineService } from "@app/shared/service/prezenta-online.service";
import { MessageDisplayer, PrezentaMembriiReprezentantiComisieGl, AppError, ObjectUtils, StringUtils, ConfirmationWindowFacade, UiUtils, BaseWindow, MessageType } from "@app/shared";
import { ParticipantiModel } from "@app/shared/model/prezenta-online/participanti.model";
import { AuthManager, AUTH_ACCESS } from "@app/shared/auth";
import { TipDocumentJustificativForCheltuieliEnum } from "@app/shared/model/alte-deconturi";

@Component({
	selector: "app-prezenta-online-window",
	templateUrl: "./prezenta-online-window.component.html"
})
export class PrezentaOnlineWindowComponent extends BaseWindow implements OnInit {

	@Input()
	public inputData: PrezentaOnlineWindowInputData;
    
	@Output()
	public windowClosed: EventEmitter<void>;

	@Output()
	public participantiImportati: EventEmitter<void>;

	@ViewChild('membriiTable') 
	public membriiTable: DataTable;

	private prezentaOnlineService: PrezentaOnlineService;
	private messageDisplayer: MessageDisplayer;
	private authManager: AuthManager;

	public confirmationWindow: ConfirmationWindowFacade;

    public visible: boolean = false;
    public participantWindowVisible: boolean = false;
	public rowsVisible: boolean = false;
	public isPrezentaFinalizata: boolean = false;
	public finalizarePrezentaVisible: boolean = false;
	public isMobileDevices: boolean = false;
	public isUserPrezenta: boolean = false;

	public timeout: any = null;
    
	public title: string;
	public scrollHeight: string;

	public membrii: PrezentaMembriiReprezentantiComisieGl[];
	public participanti: ParticipantiModel;

	public numeInstitutie: string;
	public nume: string;
	public prenume: string;

	public constructor(prezentaOnlineService: PrezentaOnlineService,  messageDisplayer: MessageDisplayer, authManager: AuthManager) {
		super();
		this.prezentaOnlineService = prezentaOnlineService;
		this.messageDisplayer = messageDisplayer;
		this.authManager = authManager;
		this.windowClosed = new EventEmitter<void>();
		this.participantiImportati = new EventEmitter<void>();
		this.confirmationWindow = new ConfirmationWindowFacade();
	}
	
	private init(): void {
		this.visible = true;
		this.unlock();

		this.scrollHeight = (window.innerHeight - 250) + "px";	
		this.isMobileDevices = UiUtils.isMobileDevices();
	}

	public ngOnInit(): void {			
        this.init();
        this.title = this.inputData.numeDocument;
		this.getMembriiComisieGL(this.inputData.comisieGlId);	
		this.isPrezentaFinalizataByDocument(this.inputData.documentId, this.inputData.documentLocationRealName);

		if (this.authManager.hasOnlyOnePermission(AUTH_ACCESS.PREZENTA.COMPLETARE.permissions.toString())) {
			this.isUserPrezenta = true;
		}
	}

	private getMembriiComisieGL(comisieGlId: number): void {
		this.lock();
		this.prezentaOnlineService.getAllMembriiReprezentantiByComisieGlId(comisieGlId,{
			onSuccess: (membrii: PrezentaMembriiReprezentantiComisieGl[]) => {
				this.membrii = membrii;
				this.getAllParticipantiByDocument(this.inputData.documentId, this.inputData.documentLocationRealName);
				this.unlock();
			},
			onFailure: (appError: AppError) => {
				this.unlock();
				this.messageDisplayer.displayAppError(appError);
			}
		});
	}

	public onHide(): void {
		this.windowClosed.emit();
	}

	public onPrezent(membru: PrezentaMembriiReprezentantiComisieGl){
		this.lock();
		membru.documentId = this.inputData.documentId;
		membru.documentLocationRealName = this.inputData.documentLocationRealName;

		this.prezentaOnlineService.saveParticipant(membru, {
			onSuccess: (): void => {
				this.messageDisplayer.displaySuccess("PREZENTA_ONLINE_PARTICIPANT_SUCCESSFULLY_SAVED");
				this.unlock();
				this.getAllParticipantiByDocument(this.inputData.documentId, this.inputData.documentLocationRealName);

				this.numeInstitutie = null;
				this.nume = null;
				this.prenume = null;
				this.membriiTable.reset();
				this.onInputFilter()
			},
			onFailure: (appError: AppError): void => {
				this.messageDisplayer.displayAppError(appError);
				this.unlock();
			}
		});
	}

	public onFinalizarePrezenta(){
		this.confirmationWindow.confirm({
			approve: () => {
				this.confirmationWindow.hide();
				this.finalizarePrezenta();
			},
			reject: () => {
				this.confirmationWindow.hide();
			}
		}, "PREZENTA_ONLINE_CONFIRM_FINALIZED", true, MessageType.ERROR);
	}

	private finalizarePrezenta(): void {
		this.lock();
		this.prezentaOnlineService.finalizarePrezentaByDocument(this.inputData.documentId, this.inputData.documentLocationRealName, {
			onSuccess: (): void => {
				this.messageDisplayer.displaySuccess("PREZENTA_ONLINE_SUCCESSFULLY_FINALIZED");
				this.isPrezentaFinalizata = true;
				this.unlock();
			},
			onFailure: (appError: AppError): void => {
				this.messageDisplayer.displayAppError(appError);
				this.unlock();
			}
		});
	}

	public onImportaPrezenta(){
		this.confirmationWindow.confirm({
			approve: () => {
				this.confirmationWindow.hide();
				this.importaPrezenta();
			},
			reject: () => {
				this.confirmationWindow.hide();
			}
		},  "PREZENTA_ONLINE_CONFIRM_IMPORT");
	}

	private importaPrezenta(): void {
		this.lock();
		this.prezentaOnlineService.importaPrezentaOnlineByDocument(this.inputData.documentId, this.inputData.documentLocationRealName, {
			onSuccess: (): void => {
				this.messageDisplayer.displaySuccess("PREZENTA_ONLINE_SUCCESSFULLY_IMPORT");
				this.participantiImportati.emit();
				this.unlock();
			},
			onFailure: (appError: AppError): void => {
				this.messageDisplayer.displayAppError(appError);
				this.unlock();
			}
		});
	}

	public onStergere(participant: PrezentaMembriiReprezentantiComisieGl){
		this.confirmationWindow.confirm({
			approve: () => {
				this.confirmationWindow.hide();
				this.deleteParticipant(participant);
			},
			reject: () => {
				this.confirmationWindow.hide();
			}
		},  "PREZENTA_ONLINE_CONFIRM_DELETE_PARTICIPANT");
		
	}

	private deleteParticipant(participant: PrezentaMembriiReprezentantiComisieGl): void {
		this.lock();
		this.prezentaOnlineService.deleteParticipant(participant.id, {
			onSuccess: (): void => {
				this.messageDisplayer.displaySuccess("PREZENTA_ONLINE_PARTICIPANT_SUCCESSFULLY_DELETED");
				this.unlock();
				this.getAllParticipantiByDocument(this.inputData.documentId, this.inputData.documentLocationRealName);
				if (ObjectUtils.isNotNullOrUndefined(this.membrii)) {
					this.membrii.forEach(membru => {
						if (participant.institutieId == membru.institutieId && participant.nume == membru.nume && participant.prenume == membru.prenume) {
							membru.isPrezent = false;
						}
					});
				}	
			},
			onFailure: (appError: AppError): void => {
				this.messageDisplayer.displayAppError(appError);
				this.unlock();
			}
		});
	}

	public onAdd(): void {
		this.participantWindowVisible = true;
	}

	public onParticipantWindowClose(): void {
		this.participantWindowVisible = false;
	}

	public onParticipantWindowDataSaved(): void {	
		this.getAllParticipantiByDocument(this.inputData.documentId, this.inputData.documentLocationRealName);
		this.participantWindowVisible = false;
	}

	private getAllParticipantiByDocument(documentId: string, documentLocationRealName: string): void {
		this.lock();
		this.prezentaOnlineService.getAllParticipantiByDocument(documentId, documentLocationRealName, {
			onSuccess: (participanti: ParticipantiModel) => {
				if (ObjectUtils.isNotNullOrUndefined(this.membrii)) {
					this.membrii.forEach(membru => {
						if (participanti.rows.some( participant => participant.institutieId == membru.institutieId && participant.nume == membru.nume && participant.prenume == membru.prenume)) {
							membru.isPrezent = true;
						}
					});
				}
				this.participanti = participanti;
				if (participanti.totalParticipanti > 0) {
					this.finalizarePrezentaVisible = true;
				} else {
					this.finalizarePrezentaVisible = false;
				}								
				this.unlock();
			},
			onFailure: (appError: AppError) => {
				this.unlock();
				this.messageDisplayer.displayAppError(appError);
			}
		});
	}

	private isPrezentaFinalizataByDocument(documentId: string, documentLocationRealName: string): void {
		this.lock();
		this.prezentaOnlineService.isPrezentaFinalizataByDocument(documentId, documentLocationRealName, {
			onSuccess: (isPrezentaFinalizata: boolean) => {
				this.isPrezentaFinalizata = isPrezentaFinalizata;								
				this.unlock();
			},
			onFailure: (appError: AppError) => {
				this.unlock();
				this.messageDisplayer.displayAppError(appError);
			}
		});
	}

	public onInputFilter(){
		let thisClass: any = this;
		clearTimeout(this.timeout);	
		this.timeout = setTimeout(function () {
			if (ObjectUtils.isNotNullOrUndefined(thisClass.membriiTable.filters.numeInstitutie) || 
					ObjectUtils.isNotNullOrUndefined(thisClass.membriiTable.filters.nume) || 
					ObjectUtils.isNotNullOrUndefined(thisClass.membriiTable.filters.prenume)) {
				thisClass.rowsVisible = true;
			} else {
				thisClass.rowsVisible = false;
			}
		}, 1000);

		if (ObjectUtils.isNullOrUndefined(this.membriiTable.filters.numeInstitutie) && 
				ObjectUtils.isNullOrUndefined(this.membriiTable.filters.nume) && 
				ObjectUtils.isNullOrUndefined(this.membriiTable.filters.prenume)) {					
			this.rowsVisible = false;
		}		
	}
}

export class PrezentaOnlineWindowInputData {
	public documentId: string;
	public documentLocationRealName: string;
    public numeDocument: string;
    public comisieGlId: number;
}
