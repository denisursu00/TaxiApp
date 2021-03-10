import { Component, Output, EventEmitter, Input, OnInit } from "@angular/core";
import { FormBuilder, FormGroup, FormControl, Validators } from "@angular/forms";
import { FormUtils, TranslateUtils, UiUtils, RegistruIntrariIesiriService, MessageDisplayer, AppError, RegistruIntrariViewModel, RegistruIesiriViewModel } from "@app/shared";

@Component({
	selector: "app-anulare-registru-intrari-iesiri-window",
	templateUrl: "./anulare-registru-intrari-iesiri-window.component.html"
})
export class AnulareRegistruIntrariIesiriWindowComponent implements OnInit{
	
	@Input()
	public inregistrare: RegistruIntrariViewModel | RegistruIesiriViewModel;

	@Input()
	public tipRegistru: "intrare" | "iesire";

	@Output()
	public windowClosed: EventEmitter<void>;

	private registruIntrariIesirService: RegistruIntrariIesiriService;
	private messageDisplayer: MessageDisplayer;

	private formBuilder: FormBuilder;
	public formGroup: FormGroup;
	private translateUtils: TranslateUtils;

	public windowVisible: boolean = false;
	public title: string;
	public cancelDisabled: boolean;
	public nrInregistrareAfectate: String[];

	public constructor(registruIntrariIesirService: RegistruIntrariIesiriService, messageDisplayer: MessageDisplayer, formBuilder: FormBuilder,
			translateUtils: TranslateUtils) {
		this.registruIntrariIesirService = registruIntrariIesirService;
		this.messageDisplayer = messageDisplayer;
		this.formBuilder = formBuilder;
		this.translateUtils = translateUtils;
		this.windowClosed = new EventEmitter<void>();
		this.cancelDisabled = true;
		this.nrInregistrareAfectate = [];
		this.openWindow();
		this.init();
	}

	public ngOnInit(){
		this.getNrInregistrareMappedRegistri().then((nrInregistrare: String[]) => {
			this.nrInregistrareAfectate = nrInregistrare;
			this.cancelDisabled = false;
		}).catch((error) => {
			if (error instanceof AppError){
				this.messageDisplayer.displayAppError(error);
			}else{
				console.error(error);
			}
		});
	}

	private openWindow(): void {
		this.windowVisible = true;
	}

	private init(): void {
		this.title = this.translateUtils.translateLabel("ANULARE_REGISTRU_INTRARI_IESIRI_TITLE");
		this.prepareForm();
	}

	private prepareForm(): void {
		this.formGroup = this.formBuilder.group([]);
		this.formGroup.addControl("motivAnulare", new FormControl(null, [Validators.required]));
	}

	private isValid(): boolean {
		return this.validate();
	}

	private validate(): boolean {
		FormUtils.validateAllFormFields(this.formGroup);
		let isValid: boolean = this.formGroup.valid;
		return isValid;
	}

	public onOkAction(): void {
		if (!this.isValid()) {
			return;
		}
		
		if (this.tipRegistru === "iesire") {
			this.cancelIesire();
		} else if (this.tipRegistru === "intrare") {
			this.cancelIntrare();
		} else {
			throw new Error("Input property [tipRegistru] can only have [iesire] or [intrare] value.");
		}

	}

	private cancelIesire(): void {
		this.registruIntrariIesirService.cancelRegistruIesiri(this.inregistrare.id, this.motivAnulareFormControl.value, {
			onSuccess: (): void => {
				this.messageDisplayer.displaySuccess("REGISTRU_REGISTRU_IESIRI_CANCELED_SUCCESSFULLY");
				this.closeWindow();
			},
			onFailure: (appError: AppError): void => {
				this.messageDisplayer.displayAppError(appError);
			}
		});
	}

	private cancelIntrare(): void {
		this.registruIntrariIesirService.cancelRegistruIntrari(this.inregistrare.id, this.motivAnulareFormControl.value, {
			onSuccess: (): void => {
				this.messageDisplayer.displaySuccess("REGISTRU_REGISTRU_INTRARI_CANCELED_SUCCESSFULLY");
				this.closeWindow();
			},
			onFailure: (appError: AppError): void => {
				this.messageDisplayer.displayAppError(appError);
			}
		});
	}

	private getNrInregistrareMappedRegistri(): Promise<any> {
		return new Promise<any>((resolve, reject) => {
			this.registruIntrariIesirService.getNrInregistrareMappedRegistriByRegistruId(this.tipRegistru, this.inregistrare.id, {
				onSuccess: (nrInregistrare: String[]): void => {
					resolve(nrInregistrare);
				},
				onFailure: (appError: AppError): void => {
					reject(appError);
				}
			});
		});
	}

	public onHide(event: any): void {
		this.closeWindow();
			
	}

	public onCloseAction(): void {
		this.closeWindow();
	}

	private closeWindow(): void {
		this.windowVisible = false;
		this.windowClosed.emit();
	}
	
	public get motivAnulareFormControl(): FormControl {
		return <FormControl> this.formGroup.get("motivAnulare");
	}
}