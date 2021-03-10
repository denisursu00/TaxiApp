import { Component, Input, Output, OnInit, EventEmitter, AfterViewInit } from "@angular/core";
import { FormBuilder, FormGroup, AbstractControl } from "@angular/forms";
import { MetadataCollectionWindowInputData } from "./metadata-collection-window.component";
import { MetadataCollectionInstanceRowModel, MetadataInstanceModel, BaseWindow } from "@app/shared";

export abstract class MetadataCollectionWindowContent extends BaseWindow {

	@Input()
	public inputData: MetadataCollectionWindowInputData;

	@Input()
	public documentMode: "add" | "viewOrEdit";

	public readonly: boolean = false; // Intr-un fel e inutil intrucat e doar add si edit (altfel nu se deschide fereastra)

	public isViewOrEdit() {
		return this.documentMode === "viewOrEdit";
	}
	
	public abstract validate(): boolean;

	public abstract getMetadataInstances(): MetadataInstanceModel[];
}