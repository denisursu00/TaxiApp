import { Component, OnInit, ViewChild, Input, OnChanges, EventEmitter, Output } from "@angular/core";
import { ControlValueAccessor, NG_VALUE_ACCESSOR } from "@angular/forms";
import { RegistruIntrariSelectorComponent, RegistruIntrariSelectorInputData } from "../registru-intrari-selector";
import { RegistruIntrariIesiriService, MessageDisplayer, UiUtils, ObjectUtils, RegistruIntrariModel, AppError } from "@app/shared";

@Component({
	selector: "app-registru-intrari-field",
	templateUrl: "./registru-intrari-field.component.html",
	providers: [
		{ provide: NG_VALUE_ACCESSOR, useExisting: RegistruIntrariFieldComponent, multi: true }
	]
})
export class RegistruIntrariFieldComponent implements OnInit, OnChanges, ControlValueAccessor {

	private readonly EMPTY_TEXT: string = "";

	@Input()
	public inputData: RegistruIntrariFieldInputData;

	@Output()
	public registruIntrariIdChanged: EventEmitter<RegistruIntrariModel>;

	@ViewChild(RegistruIntrariSelectorComponent)
	private registruIntrariSelector: RegistruIntrariSelectorComponent;

	private registruIntrariIesiriService: RegistruIntrariIesiriService;
	private messageDisplayer: MessageDisplayer;

	public numarInregistrare: string;

	public fieldValue: number;
	public placeholder: string;

	public visible: boolean;
	public width: number;
	public height: number | string;

	public clearValueEnabled: boolean = true;
	public selectValueEnabled: boolean = true;

	public registruIntrariSelectorInputData: RegistruIntrariSelectorInputData;

	private onChange: any = () => { };
	private onTouched: any = () => { };

	public constructor(registruIntrariIesiriService: RegistruIntrariIesiriService, messageDisplayer: MessageDisplayer) {
		this.registruIntrariIesiriService = registruIntrariIesiriService;
		this.messageDisplayer = messageDisplayer;
		this.init();
	}
	
	private init(): void {
		this.registruIntrariIdChanged = new EventEmitter();
		this.placeholder = this.EMPTY_TEXT;
		this.visible = false;
		this.adjustSize();
	}
	
	private adjustSize(): void {
		this.height = "auto";
		this.width = UiUtils.getDialogWidth();
	}

	public ngOnInit() {
	}

	public ngOnChanges() {
	}
	public onClearValue(): void {
		this.fieldValue = null;
		this.numarInregistrare = this.EMPTY_TEXT;
		this.propagateValue();
	}

	public onSelectValue(): void {
		this.registruIntrariSelectorInputData = new RegistruIntrariSelectorInputData();
		this.registruIntrariSelectorInputData.registruIntrariId = this.fieldValue;
		this.registruIntrariSelectorInputData.registruIntrariId = this.inputData.registruIesiriId;
		this.registruIntrariSelectorInputData.tipDocumentId = this.inputData.tipDocumentId;
		this.registruIntrariSelectorInputData.destinatarId = this.inputData.destinatarId;
		this.registruIntrariSelectorInputData.numeDestinatar = this.inputData.numeDestinatar;
		this.registruIntrariSelectorInputData.isIntrariForIesiri = this.inputData.isIntrariForIesiri;
		this.visible = true;
	}

	public onRegistruIntrariSelectionChanged(registruIntrariId: number): void {
		this.fieldValue = registruIntrariId;
	}

	public onAddValue(): void {
		this.loadNumarInregistrare()
			.then((registruIntrari: RegistruIntrariModel) => {
				this.propagateValue();
				this.registruIntrariIdChanged.emit(registruIntrari);
			});
		this.visible = false;
	}

	private loadNumarInregistrare(): Promise<RegistruIntrariModel> {
		return new Promise<RegistruIntrariModel>((resolve, reject) => {
			if (ObjectUtils.isNotNullOrUndefined(this.fieldValue)) {
				this.registruIntrariIesiriService.getRegistruIntrariById(this.fieldValue, {
					onSuccess: (registruIntrari: RegistruIntrariModel): void => {
						this.numarInregistrare = registruIntrari.numarInregistrare;
						resolve(registruIntrari);
					},
					onFailure: (appError: AppError): void => {
						reject();
						this.messageDisplayer.displayAppError(appError);
					}
				});
			}
		});
	}

	private propagateValue(): void {
		this.onChange(this.fieldValue);
		this.onTouched();
	}

	public writeValue(registruIntrariId: number): void {
		this.fieldValue = registruIntrariId;
		this.loadNumarInregistrare().then(() => this.propagateValue());
	}

	public registerOnChange(fn: any): void {
		this.onChange = fn;
	}

	public registerOnTouched(fn: any): void {
		this.onTouched = fn;
	}

	public setDisabledState(isDisabled: boolean): void {
		if (isDisabled) {
			this.clearValueEnabled = false;
			this.selectValueEnabled = false;
		} else {
			this.clearValueEnabled = true;
			this.selectValueEnabled = true;
		}
		
	}
}


export class RegistruIntrariFieldInputData {
	public registruIesiriId: number;
	public tipDocumentId: number;
	public destinatarId: number;
	public numeDestinatar: string;
	public isIntrariForIesiri: boolean;
}