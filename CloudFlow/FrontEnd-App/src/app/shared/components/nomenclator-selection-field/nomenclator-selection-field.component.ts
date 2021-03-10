import { Component, Input, Output, OnInit, EventEmitter } from "@angular/core";
import { ControlValueAccessor, NG_VALUE_ACCESSOR } from "@angular/forms";
import { StringUtils, ObjectUtils, ArrayUtils } from "./../../utils";
import { NomenclatorService } from "./../../service";
import { AppError, NomenclatorModel } from "./../../model";
import { MessageDisplayer } from "./../../message-displayer";

@Component({
	selector: "app-nomenclator-selection-field",
	template: `
		<span *ngIf="loading">{{'MESSAGES.LOADING' | translate}}</span>
		<p-dropdown *ngIf="!loading"
			[options]="nomenclators" 
			[(ngModel)]="selectedNomenclator"
			optionLabel="name"
			filter="true"
			autoDisplayFirst="false"
			[placeholder]="'LABELS.SELECT_NOMENCLATOR' | translate" 
			(onChange)="onSelectionChanged($event)"
			filter="true"
			(onBlur)="onSelectionBlured($event)">
		</p-dropdown>
	`,
	providers: [
		{ provide: NG_VALUE_ACCESSOR, useExisting: NomenclatorSelectionFieldComponent, multi: true }
	]
})
export class NomenclatorSelectionFieldComponent implements ControlValueAccessor, OnInit {

	@Output()
	public selectionChanged: EventEmitter<number>;

	public nomenclators: NomenclatorModel[];
	public selectedNomenclator: NomenclatorModel;
	public loading: boolean;

	private nomenclatorService: NomenclatorService;
	private messageDisplayer: MessageDisplayer;

	private fieldValue: number;

	private onChange: any = () => {};
	private onTouched: any = () => {};

	public constructor(nomenclatorService: NomenclatorService, messageDisplayer: MessageDisplayer) {
		this.nomenclatorService = nomenclatorService;
		this.messageDisplayer = messageDisplayer;
		this.loading = true;
		this.selectionChanged = new EventEmitter();
	}

	public ngOnInit(): void {
		this.loadGroups();
	}

	private loadGroups(): void {
		this.nomenclatorService.getVisibleNomenclators({
			onSuccess: (nomenclators: NomenclatorModel[]) => {
				this.nomenclators = nomenclators;
				this.selectNomenclator();
				this.loading = false;
			},
			onFailure: (appError: AppError) => {
				this.messageDisplayer.displayAppError(appError);
			}
		});	
	}

	private selectNomenclator(): void {
		if (ArrayUtils.isEmpty(this.nomenclators)) {
			return;
		}
		if (ObjectUtils.isNullOrUndefined(this.fieldValue)) {
			return;
		}
		this.nomenclators.forEach((nomeclator: NomenclatorModel) => {
			if (this.fieldValue === nomeclator.id) {
				this.selectedNomenclator = nomeclator;
			}
		});
	}

	private propagateValue(): void {
		if (ObjectUtils.isNullOrUndefined(this.selectedNomenclator)) {
			this.fieldValue = null;
		} else {
			this.fieldValue = this.selectedNomenclator.id;
		}
		this.onChange(this.fieldValue);
		this.onTouched();
	}

	public onSelectionChanged(event: any): void {
		this.propagateValue();
		this.selectionChanged.emit(this.fieldValue);
	}

	public onSelectionBlured(event: any): void {
		this.onTouched();
	}

	public writeValue(nomenclatorId: number): void {
		this.fieldValue = nomenclatorId;
		this.selectNomenclator();
	}
	
	public registerOnChange(fn: any): void {
		this.onChange = fn;
	}

	public registerOnTouched(fn: any): void {
		this.onTouched = fn;
	}
}