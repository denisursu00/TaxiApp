import { Component, OnInit, ViewChild, Input, OnChanges, SimpleChanges, EventEmitter, Output } from "@angular/core";
import { ControlValueAccessor, NG_VALUE_ACCESSOR } from "@angular/forms";
import { UiUtils, ObjectUtils, MessageDisplayer, RegistruIntrariIesiriService, RegistruIesiriModel, AppError } from "@app/shared";
import { RegistruIesiriSelectorComponent, RegistruIesiriSelectorInputData } from "../registru-iesiri-selector/registru-iesiri-selector.component";

@Component({
	selector: "app-registru-iesiri-field",
	templateUrl: "./registru-iesiri-field.component.html",
	providers: [
		{ provide: NG_VALUE_ACCESSOR, useExisting: RegistruIesiriFieldComponent, multi: true }
	]
})
export class RegistruIesiriFieldComponent implements OnInit, ControlValueAccessor {

	private readonly EMPTY_TEXT: string = "";

	@Input()
	public inputData: RegistruIesiriFieldInputData;

	@Output()
	public registruIesiriChanged: EventEmitter<RegistruIesiriModel>;

	@ViewChild(RegistruIesiriSelectorComponent)
	private registruIesiriSelector: RegistruIesiriSelectorComponent;

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

	public registruIesiriSelectorInputData: RegistruIesiriSelectorInputData;

	private onChange: any = () => { };
	private onTouched: any = () => { };

	public constructor(registruIntrariIesiriService: RegistruIntrariIesiriService, messageDisplayer: MessageDisplayer) {
		this.registruIntrariIesiriService = registruIntrariIesiriService;
		this.messageDisplayer = messageDisplayer;
		this.init();
	}
	
	private init(): void {
		this.registruIesiriChanged = new EventEmitter();
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

	public onClearValue(): void {
		this.fieldValue = null;
		this.numarInregistrare = this.EMPTY_TEXT;
		this.propagateValue();
	}

	public onSelectValue(): void {
		this.registruIesiriSelectorInputData = new RegistruIesiriSelectorInputData();
		this.registruIesiriSelectorInputData.registruIesiriId = this.fieldValue;
		this.registruIesiriSelectorInputData.tipDocumentId = this.inputData.tipDocumentId;
		this.registruIesiriSelectorInputData.emitentId = this.inputData.emitentId;
		this.registruIesiriSelectorInputData.numeEmitent = this.inputData.numeEmitent;
		this.registruIesiriSelectorInputData.isIesiriForIntrari = this.inputData.isIesiriForIntrari;
		this.visible = true;
	}

	public onRegistruIesiriSelectionChanged(registruIesiriId: number): void {
		this.fieldValue = registruIesiriId;
	}

	public onAddValue(): void {
		this.loadNumarInregistrare().then((registruIesiri: RegistruIesiriModel) => {
			this.propagateValue();
			this.registruIesiriChanged.emit(registruIesiri);
		});
		this.visible = false;
	}

	private loadNumarInregistrare(): Promise<RegistruIesiriModel> {
		return new Promise<RegistruIesiriModel>((resolve, reject) => {
			if (ObjectUtils.isNotNullOrUndefined(this.fieldValue)) {
				this.registruIntrariIesiriService.getRegistruIesiri(this.fieldValue, {
					onSuccess: (registruIesiri: RegistruIesiriModel): void => {
						this.numarInregistrare = registruIesiri.numarInregistrare;
						resolve(registruIesiri);
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

	public writeValue(registruIesiriId: number): void {
		this.fieldValue = registruIesiriId;
		this.loadNumarInregistrare().then(() => this.propagateValue());
	}

	public registerOnChange(fn: any): void {
		this.onChange = fn;
	}

	public registerOnTouched(fn: any): void {
		this.onTouched = fn;
	}

	public setDisabledState(isDisabled: boolean): void {
		this.clearValueEnabled = !isDisabled;
		this.selectValueEnabled = !isDisabled;		
		
	}
}

export class RegistruIesiriFieldInputData {

	public tipDocumentId: number;
	public emitentId: number;
	public numeEmitent: string;
	public isIesiriForIntrari: boolean;
}
