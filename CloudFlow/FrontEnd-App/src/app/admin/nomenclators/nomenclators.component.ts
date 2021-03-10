import { Component, OnInit } from "@angular/core";
import { NomenclatorService, NomenclatorModel, AppError, MessageDisplayer, ObjectUtils, ConfirmationUtils } from "@app/shared";
import { Column } from "primeng/primeng";
import { reject } from "q";

@Component({
	selector: "app-nomenclators",
	templateUrl: "./nomenclators.component.html"
})
export class NomenclatorsComponent {

	private static readonly COLUMN_NAME: string = "name";
	private static readonly COLUMN_DESCRIPTION: string = "description";

	private nomeclatorService: NomenclatorService;
	private messageDisplayer: MessageDisplayer;

	private confirmationUtils: ConfirmationUtils;

	public columns: Column[] = [];
	public nomenclators: NomenclatorModel[] = [];
	public selectedNomenclator: NomenclatorModel;

	public dataLoading: boolean = true;
	public editButtonDisabled: boolean = true;
	public deleteButtonDisabled: boolean = true;

	public nomenclatorWindowVisible: boolean = false;
	public nomenclatorWindowNomenclatorId: number;
	public nomenclatorWindowMode: string;

	public scrollHeight: string;

	public constructor(nomeclatorService: NomenclatorService, messageDisplayer: MessageDisplayer, confirmationUtils: ConfirmationUtils) {
		this.nomeclatorService = nomeclatorService;
		this.messageDisplayer = messageDisplayer;
		this.confirmationUtils = confirmationUtils;
		this.init();
	}

	private init():void {
		this.prepareColumns();
		this.loadNomenclators();

		this.scrollHeight = (window.innerHeight - 300) + "px";
	}

	private prepareColumns(): void {
		this.columns = [];
		this.columns.push(
			this.buildColumn("LABELS.NAME", NomenclatorsComponent.COLUMN_NAME),
			this.buildColumn("LABELS.DESCRIPTION", NomenclatorsComponent.COLUMN_DESCRIPTION)
		);
	}

	private buildColumn(header: string, field: string): Column {
		let column: Column = new Column();
		column.header = header;
		column.field = field;
		return column;
	}

	private lock() {
		this.dataLoading = true;
	}

	private unlock() {
		this.dataLoading = false;
	}

	public loadNomenclators(): void {
		this.lock();
		this.nomeclatorService.getAvailableNomenclatorsForProcessingStructureFromUI({
			onSuccess: (nomenclators: NomenclatorModel[]) => {
				this.nomenclators = nomenclators;
				this.unlock();
			},
			onFailure: (appError: AppError) => {
				this.messageDisplayer.displayAppError(appError);
				this.unlock();
			}
		});
	}

	public onNomenclatorSelect(event: any): void {
		this.editButtonDisabled = false;
		this.deleteButtonDisabled = false;
	}

	public onNomenclatorUnselect(event: any): void {
		this.editButtonDisabled = true;
		this.deleteButtonDisabled = true;
	}

	public onAdd(event: any): void {
		this.nomenclatorWindowMode = "add";
		this.nomenclatorWindowVisible = true;
	}

	public onEdit(event: any): void {
		if (ObjectUtils.isNotNullOrUndefined(this.selectedNomenclator)) {
			this.editNomenclator();
		}
	}

	private editNomenclator(): void {
		this.nomenclatorWindowMode = "edit";
		this.nomenclatorWindowNomenclatorId = this.selectedNomenclator.id;
		this.nomenclatorWindowVisible = true;
	}

	public onDelete(event: any): void {
		this.confirmationUtils.confirm("CONFIRM_DELETE_NOMENCLATOR", {
			approve: (): void => {
				this.lock();
				this.nomenclatorHasValue();
			},
			reject: (): void => {}
		});
	}

	private nomenclatorHasValue(): void {
		this.nomeclatorService.nomenclatorHasValue(this.selectedNomenclator.id, {
			onSuccess: (hasValue: boolean) => {
				if (!hasValue) {
					this.deleteNomenclator();
				} else {
					this.messageDisplayer.displayWarn("NOMENCLATOR_HAS_VALUE");
					this.unlock();
				}
			},
			onFailure: (appError: AppError) => {
				this.messageDisplayer.displayAppError(appError);
				this.unlock();
				this.refresh();
			}
		});
	}

	private deleteNomenclator(): void {
		this.nomeclatorService.deleteNomenclator(this.selectedNomenclator.id, {
			onSuccess: () => {
				this.messageDisplayer.displaySuccess("NOMENCLATOR_DELETED_SUCCESSFULLY");
				this.unlock();
				this.refresh();
			},
			onFailure: (appError: AppError) => {
				this.messageDisplayer.displayAppError(appError);
				this.unlock();
				this.refresh();
			}
		});
	}

	public onRefresh(event: any): void {
		this.refresh();
	}

	private refresh(): void {
		this.reset();
		this.loadNomenclators();
	}

	private reset(): void {
		this.nomenclators = [];
		this.selectedNomenclator = null;
		this.editButtonDisabled = true;
		this.deleteButtonDisabled = true;
	}

	public onNomenclatorWindowClosed(event: any): void {
		this.onWindowClosed();
		this.nomenclatorWindowVisible = false;
	}

	private onWindowClosed(): void {
		this.refresh();
	}
}