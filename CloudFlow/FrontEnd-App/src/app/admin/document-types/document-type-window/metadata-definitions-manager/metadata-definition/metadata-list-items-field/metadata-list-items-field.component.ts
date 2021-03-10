import { Component, Input, OnInit, Output, EventEmitter } from "@angular/core";
import { ControlValueAccessor, NG_VALUE_ACCESSOR } from "@angular/forms";
import { ListMetadataItemModel, ArrayUtils, ObjectUtils } from "@app/shared";

@Component({
	selector: "app-metadata-list-items-field",
	templateUrl: "./metadata-list-items-field.component.html",
	providers: [
		{ provide: NG_VALUE_ACCESSOR, useExisting: MetadataListItemsFieldComponent, multi: true }
	]
})
export class MetadataListItemsFieldComponent implements ControlValueAccessor {

	@Output()
	public valueChanged: EventEmitter<ListMetadataItemModel[]>;
	
	public items: ListMetadataItemModel[];
	public selectedItem: ListMetadataItemModel;

	public editActionEnabled: boolean;
	public deleteActionEnabled: boolean;

	public metadataListItemWindowVisible: boolean;
	public metadataListItemWindowTakenValues: string[];
	public metadataListItemWindowTakenLabels: string[];
	public metadataListItemWindowSuggestedOrderNumber: number;
	public metadataListItemWindowListItem: ListMetadataItemModel;

	private indexOfItemInEditing: number;

	private onChange: any = () => { };
	private onTouched: any = () => { };

	public constructor() {
		this.editActionEnabled = false;
		this.deleteActionEnabled = false;
		this.valueChanged = new EventEmitter();
	}

	public onItemSelect(event: any): void {
		this.changePerspective();
	}

	public onItemUnselect(event: any): void {
		this.changePerspective();
	}

	private changePerspective(): void {
		this.deleteActionEnabled = ObjectUtils.isNotNullOrUndefined(this.selectedItem);
		this.editActionEnabled = ObjectUtils.isNotNullOrUndefined(this.selectedItem);
	}

	private propagateFieldValue(): void {
		if (ArrayUtils.isEmpty(this.items)) {
			this.onChange(null);
		} else {
			this.onChange(this.items);
		}
		this.onTouched();
	}

	public onAddAction(event: any): void {
		if (ArrayUtils.isEmpty(this.items)) {
			this.items = [];
		}
		this.indexOfItemInEditing = null;
		this.metadataListItemWindowTakenLabels = this.getAllLabels();
		this.metadataListItemWindowTakenValues = this.getAllValues();
		this.metadataListItemWindowSuggestedOrderNumber = this.getSuggestedOerderNumber();
		this.metadataListItemWindowListItem = null;
		this.metadataListItemWindowVisible = true;
	}

	public onEditAction(event: any): void {
		if (ObjectUtils.isNullOrUndefined(this.selectedItem)) {
			return;
		}
		this.indexOfItemInEditing = this.items.indexOf(this.selectedItem);		
		let editItem: ListMetadataItemModel = this.selectedItem.clone();
		let takenLabels = this.getAllLabels();
		ArrayUtils.removeElement(takenLabels, editItem.label);
		this.metadataListItemWindowTakenLabels = takenLabels;
		let takenValues = this.getAllValues();
		ArrayUtils.removeElement(takenValues, editItem.value);
		this.metadataListItemWindowTakenValues = takenValues;
		this.metadataListItemWindowListItem = editItem;
		this.metadataListItemWindowVisible = true;
	}

	private getAllValues(): string[] {
		let values: string[] = [];
		if (ArrayUtils.isNotEmpty(this.items)) {
			this.items.forEach((item: ListMetadataItemModel) => {
				values.push(item.value);
			});
		}
		return values;
	}
	
	private getAllLabels(): string[] {
		let values: string[] = [];
		if (ArrayUtils.isNotEmpty(this.items)) {
			this.items.forEach((item: ListMetadataItemModel) => {
				values.push(item.label);
			});
		}
		return values;
	}

	private getLastOrderNumber(): number {
		let lastOrderNumber: number = 0;
		if (ArrayUtils.isNotEmpty(this.items)) {
			this.items.forEach((listItem: ListMetadataItemModel) => {
				if (listItem.orderNumber > lastOrderNumber) {
					lastOrderNumber = listItem.orderNumber;
				}
			});
		}
		return lastOrderNumber;
	}

	private getSuggestedOerderNumber(): number {
		let lastOrderNumber: number = this.getLastOrderNumber();
		return (lastOrderNumber + 1);
	}

	public onDeleteAction(event: any): void {
		if (ObjectUtils.isNullOrUndefined(this.selectedItem)) {
			return;
		}
		ArrayUtils.removeElement(this.items, this.selectedItem);
		this.selectedItem = null;
		this.propagateFieldValue();
		this.changePerspective();
		this.valueChanged.emit(this.items);
	}

	public onMetadataListItemWindowSaved(savedItem: ListMetadataItemModel): void {
		if (ObjectUtils.isNullOrUndefined(this.indexOfItemInEditing)) {
			this.items.push(savedItem);
		} else {
			this.items[this.indexOfItemInEditing] = savedItem;
			this.selectedItem = null;
			this.indexOfItemInEditing = null;
		}
		this.metadataListItemWindowVisible = false;
		this.propagateFieldValue();
		this.changePerspective();
		this.valueChanged.emit(this.items);
	}

	public onMetadataListItemWindowCanceled(event: any): void {
		this.metadataListItemWindowVisible = false;
	}

	public writeValue(listItems: ListMetadataItemModel[]): void {
		this.items = listItems;
		this.propagateFieldValue();
	}
	
	public registerOnChange(fn: any): void {
		this.onChange = fn;
	}

	public registerOnTouched(fn: any): void {
		this.onTouched = fn;
	}
}