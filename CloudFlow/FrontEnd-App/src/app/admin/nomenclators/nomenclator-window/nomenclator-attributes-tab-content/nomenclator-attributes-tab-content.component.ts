import { Component, ViewChild } from "@angular/core";
import { NomenclatorTabContent } from "../nomenclator-tab-content";
import { NomenclatorModel, Message, NomenclatorAttributeModel, ArrayUtils } from "@app/shared";
import { FormBuilder } from "@angular/forms";
import { NomenclatorAttributesManagerComponent } from "./nomenclator-attributes-manager/nomenclator-attributes-manager.component";

@Component({
	selector: "app-nomenclator-attributes-tab-content",
	templateUrl: "./nomenclator-attributes-tab-content.component.html"
})
export class NomenclatorAttributesTabContentComponent extends NomenclatorTabContent {
	
	@ViewChild(NomenclatorAttributesManagerComponent)
	public nomenclatorAttributesManagerComponent: NomenclatorAttributesManagerComponent;

	public nomenclatorAttributesManagerInputData: NomenclatorModel;
	public nomenclatorAttributes: NomenclatorAttributeModel[];
	public nomenclatorUiAttributeNames: String[];

	private messages: Message[];

	public constructor(formBuilder: FormBuilder) {
		super();
	}

	protected doWhenNgOnInit(): void {
		// Nothing here
	}

	protected prepareForAdd(): void {
		// Nothing here
	}
	
	protected prepareForEdit(): void {
		this.nomenclatorAttributes = this.inputData.attributes;
		this.nomenclatorUiAttributeNames = this.inputData.uiAttributeNames;
	}

	public populateNomenclator(nomenclator: NomenclatorModel): void {
		nomenclator.attributes = this.nomenclatorAttributesManagerComponent.getNomenclatorAttributes();
		if (ArrayUtils.isEmpty(nomenclator.uiAttributeNames)) {
			nomenclator.uiAttributeNames = [];
		}
		this.nomenclatorAttributesManagerComponent.getNomenclatorUiAttributes().forEach((name: string) => {
			nomenclator.uiAttributeNames.push(name);
		});
		nomenclator.attributes.forEach((attribute: NomenclatorAttributeModel) => {
			attribute.nomenclatorId = nomenclator.id;
		});
	}

	public isValid(): boolean {
		return this.validate();
	}

	private validate(): boolean {
		this.messages = [];
		let isValid: boolean = this.nomenclatorAttributesManagerComponent.isValid();
		if (!isValid) {
			this.messages = this.nomenclatorAttributesManagerComponent.getMessages();
		}
		return isValid;
	}

	public getMessages(): Message[] {
		return this.messages;
	}
}