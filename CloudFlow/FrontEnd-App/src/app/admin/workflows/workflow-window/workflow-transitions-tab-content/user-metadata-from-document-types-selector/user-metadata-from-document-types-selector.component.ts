import { Component, Input, OnInit, EventEmitter, Output } from "@angular/core";
import { ArrayUtils, DocumentTypeModel, MetadataDefinitionModel } from "@app/shared";
import { SelectItem } from "primeng/primeng";
import { ControlValueAccessor, NG_VALUE_ACCESSOR } from "@angular/forms";
import { ListItemUtils } from "@app/shared/utils/list-item-utils";

@Component({
	selector: "app-user-metadata-from-document-types-selector",
	templateUrl: "./user-metadata-from-document-types-selector.component.html",
	providers: [{
		provide: NG_VALUE_ACCESSOR,
		useExisting: UserMetadataFromDocumentTypesSelectorComponent,
		multi: true
	}]
})
export class UserMetadataFromDocumentTypesSelectorComponent implements OnInit, ControlValueAccessor {

	public _documentTypes: DocumentTypeModel[];

	@Output()
	private selectionChanged: EventEmitter<MetadataDefinitionModel>;

	public selectedUserMetadata: MetadataDefinitionModel;
	public userMetadatasSelectItems: SelectItem[];

	private onChange = (userMetadata: MetadataDefinitionModel) => {};
	private onTouche = () => {};

	public constructor() {
		this.selectionChanged = new EventEmitter<MetadataDefinitionModel>();
		this.userMetadatasSelectItems = [];
	}

	public ngOnInit(): void {
		if (ArrayUtils.isEmpty(this._documentTypes)) {
			return;
		}
		this.prepareUserMetadatasSelectItems();
	}

	private prepareUserMetadatasSelectItems(): void {
		this.userMetadatasSelectItems = [];
		this._documentTypes.forEach((documentType: DocumentTypeModel) => {
			documentType.metadataDefinitions.forEach((metadata: MetadataDefinitionModel) => {
				if (metadata.type === MetadataDefinitionModel.TYPE_USER && !this.userMetadataSelectItemExists(metadata)) {
					this.userMetadatasSelectItems.push(this.createUserMetadatasSelectItem(metadata));
				}
			});
		});

		ListItemUtils.sortByLabel(this.userMetadatasSelectItems);
	}

	private userMetadataSelectItemExists(metadata: MetadataDefinitionModel): boolean {
		let exists: boolean = false;
		this.userMetadatasSelectItems.forEach((item: SelectItem) => {
			if (item.label === metadata.name) {
				exists = true;
			}
		});
		return exists;
	}

	private createUserMetadatasSelectItem(metadata: MetadataDefinitionModel): SelectItem {
		let selectItem: SelectItem = {
			value: metadata,
			label: metadata.name
		};
		return selectItem;
	}

	public onUserMetadataSelectionChanged(event: any): void {
		this.propagateValue();
	}

	private propagateValue(): void {
		this.selectionChanged.emit(this.selectedUserMetadata);
		this.onChange(this.selectedUserMetadata);
		this.onTouche();
	}

	public writeValue(userMetadata: MetadataDefinitionModel): void {
		this.selectedUserMetadata = userMetadata;
	}

	public registerOnChange(fn: any): void {
		this.onChange = fn;
	}
	
	public registerOnTouched(fn: any): void {
		this.onTouche = fn;
	}

	@Input()
	public set documentTypes(documentTypes: DocumentTypeModel[]) {
		this._documentTypes = documentTypes;
		this.prepareUserMetadatasSelectItems();
	}
}
