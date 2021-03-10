import { Component, Input } from "@angular/core";
import { FormGroup } from "@angular/forms";
import { MetadataDefinitionModel, DateConstants, DateUtils } from "@app/shared";

@Component({
	selector: "app-search-document-metadata",
	templateUrl: "./search-document-metadata.component.html"
})
export class SearchDocumentMetadataComponent {

	@Input()
	public formGroup: FormGroup;
	
	@Input()
	public metadataDefinition: MetadataDefinitionModel;

	public dateFormat: string;
	public dateTimeFormat: string;
	public monthFormat: string;
	public yearRange: string;
	
	public constructor() {
		this.dateFormat = DateConstants.DATE_FORMAT_FOR_TYPING;
		this.dateTimeFormat = DateConstants.DATE_TIME_FORMAT_FOR_TYPING;
		this.monthFormat = DateConstants.MONTH_FORMAT_FOR_TYPING;
		this.yearRange = DateUtils.getDefaultYearRangeForMetadata();
	}

	public get metadataFormControl() { 
		return this.formGroup.get(this.metadataDefinition.name); 
	}
}