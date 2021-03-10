import { Component, OnInit, Input, Output, EventEmitter} from "@angular/core";
import { Dialog, SelectItem } from "primeng/primeng";
import { PrezentaOnlineService } from "@app/shared/service/prezenta-online.service";
import { MessageDisplayer, PrezentaMembriiReprezentantiComisieGl, AppError, FormUtils, NomenclatorService, NomenclatorConstants, NomenclatorValueModel, UiUtils, BaseWindow, ObjectUtils } from "@app/shared";
import { FormBuilder, FormGroup, FormControl, Validators } from "@angular/forms";
import { ListItemUtils } from "@app/shared/utils/list-item-utils";

@Component({
	selector: "app-participant-window",
	templateUrl: "./participant-window.component.html"
})
export class ParticipantWindowComponent extends BaseWindow implements OnInit {

    public static readonly CALITATE_INLOCUITOR: string = "INLOCUITOR";

	@Input()
    public documentId: string;
    
	@Input()
	public documentLocationRealName: string;

	@Output()
    public dataSaved: EventEmitter<void>;
    
	@Output()
	public windowClosed: EventEmitter<void>;

	private prezentaOnlineService: PrezentaOnlineService;
	private nomenclatorService: NomenclatorService;
    private messageDisplayer: MessageDisplayer;
    
    private formBuilder: FormBuilder;
    public formGroup: FormGroup;

    public institutieSelectItems: SelectItem[];
    
    public visible: boolean = false;

	public constructor(prezentaOnlineService: PrezentaOnlineService, nomenclatorService: NomenclatorService, messageDisplayer: MessageDisplayer, formBuilder: FormBuilder) {
        super();
        this.prezentaOnlineService = prezentaOnlineService;
        this.nomenclatorService = nomenclatorService;
        this.messageDisplayer = messageDisplayer;
        this.formBuilder = formBuilder;
		this.windowClosed = new EventEmitter<void>();
		this.dataSaved = new EventEmitter<void>();
	}
	
	private init(): void {
		this.visible = true;
	}

	public ngOnInit(): void {			
        this.init();
        this.prepareInstitutieSelectItems();

        this.formGroup = this.formBuilder.group([]);
        this.formGroup.addControl("institutie", new FormControl(null, [Validators.required]));
        this.formGroup.addControl("numeMembruInlocuitor", new FormControl(null, [Validators.required]));
        this.formGroup.addControl("prenumeMembruInlocuitor", new FormControl(null, [Validators.required]));
        this.formGroup.addControl("functie", new FormControl());
        this.formGroup.addControl("departament", new FormControl());
        this.formGroup.addControl("telefon", new FormControl());
        this.formGroup.addControl("email", new FormControl());
    }
    
    private prepareInstitutieSelectItems(): void {
        this.institutieSelectItems = [];
        
        this.nomenclatorService.getNomenclatorValuesByNomenclatorCode(NomenclatorConstants.NOMENCLATOR_CODE_INSTITUTII, {
            onSuccess: (institutii: NomenclatorValueModel[]): void => {
                institutii.forEach(institutie => {
                    let id: number = institutie.id;
                    let nume: string = institutie[NomenclatorConstants.INSTITUTII_ATTR_KEY_DENUMIRE_INSTITUTIE];
                    let radiat : boolean = ObjectUtils.isNotNullOrUndefined(institutie[NomenclatorConstants.INSTITUTII_ATTRIBUTE_KEY_TIP_RADIAT]) && institutie[NomenclatorConstants.INSTITUTII_ATTRIBUTE_KEY_TIP_RADIAT].toLowerCase() == 'true';                    
                    
                    if (!radiat) {
                        this.institutieSelectItems.push({ label: nume, value: id });
                    }
                });
		
				ListItemUtils.sortByLabel(this.institutieSelectItems);
            },
            onFailure: (appError: AppError): void => {
                this.messageDisplayer.displayAppError(appError);
            }
		});
	}

	public onHide(): void {
		this.windowClosed.emit();
    }
    
	public onAdd(): void {
        if(!this.isFilterValid()) {
            return;
        }

        this.prezentaOnlineService.saveParticipant(this.prepareMembruNou(), {
			onSuccess: (): void => {
				this.messageDisplayer.displaySuccess("PREZENTA_ONLINE_PARTICIPANT_NOU_SUCCESSFULLY_DELETED");
                this.dataSaved.emit();
			},
			onFailure: (appError: AppError): void => {
				this.messageDisplayer.displayAppError(appError);
                this.dataSaved.emit();
			}
        });  
    }

    public onDropdownClick(): void {
        UiUtils.onDropdownClick();
    }
    
    private prepareMembruNou(): PrezentaMembriiReprezentantiComisieGl {
        let membru: PrezentaMembriiReprezentantiComisieGl = new PrezentaMembriiReprezentantiComisieGl();

        membru.institutieId = this.institutieFormControl.value;
        membru.nume = this.numeMembruInlocuitorFormControl.value;
        membru.prenume = this.prenumeMembruInlocuitorFormControl.value;
        membru.functie = this.functieFormControl.value;
        membru.departament = this.departamentFormControl.value;
        membru.telefon = this.telefonFormControl.value;
        membru.email = this.emailFormControl.value;
        membru.calitate = ParticipantWindowComponent.CALITATE_INLOCUITOR;
        membru.documentId = this.documentId;
        membru.documentLocationRealName = this.documentLocationRealName;

        return membru;
    }

    private isFilterValid(): boolean {
        FormUtils.validateAllFormFields(this.formGroup);

        return this.formGroup.valid;
    }
    
    public get institutieFormControl(): FormControl {
        return this.getFormControlByName("institutie");
    }
    
    public get numeMembruInlocuitorFormControl(): FormControl {
        return this.getFormControlByName("numeMembruInlocuitor");
    }
    
    public get prenumeMembruInlocuitorFormControl(): FormControl {
        return this.getFormControlByName("prenumeMembruInlocuitor");
    }
    
    public get functieFormControl(): FormControl {
        return this.getFormControlByName("functie");
    }
    
    public get departamentFormControl(): FormControl {
        return this.getFormControlByName("departament");
    }
    
    public get telefonFormControl(): FormControl {
        return this.getFormControlByName("telefon");
    }
    
    public get emailFormControl(): FormControl {
        return this.getFormControlByName("email");
    }

    private getFormControlByName(name: string): FormControl {
        return <FormControl> this.formGroup.get(name);
    }

}

