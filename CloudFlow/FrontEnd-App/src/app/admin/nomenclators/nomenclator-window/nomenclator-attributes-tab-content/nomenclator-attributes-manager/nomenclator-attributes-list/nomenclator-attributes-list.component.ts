import { Component, Input, OnInit, Output, EventEmitter, OnChanges } from "@angular/core";
import { NomenclatorAttributeModel, ObjectUtils, ArrayUtils } from "@app/shared";

@Component({
	selector: "app-nomenclator-attributes-list-component",
	templateUrl: "./nomenclator-attributes-list.component.html"
})
export class NomenclatorAttributesListComponent implements OnInit, OnChanges {

	@Input()
	public nomenclatorAttributes: NomenclatorAttributeModel[];

	@Output()
	private selectionChanged: EventEmitter<NomenclatorAttributeModel>;

	public selectedAttribute: NomenclatorAttributeModel;

	public constructor() {
		this.nomenclatorAttributes = [];
		this.selectionChanged = new EventEmitter<NomenclatorAttributeModel>();
	}

	public ngOnInit(): void {
		// Nothing here
	}

	public ngOnChanges(): void {
		// Nothing here
	}

	public onAttributeSelected(event: any): void {
		this.selectionChanged.emit(this.selectedAttribute);
	}

	public onAttributeUnselected(event: any): void {
		this.selectedAttribute = null;
		this.selectionChanged.emit(this.selectedAttribute);
	}

	public getNomenclatorAttributes(): NomenclatorAttributeModel[] {
		return this.nomenclatorAttributes;
	}
}