import { Component, Input, OnInit } from "@angular/core";
import { ControlValueAccessor, NG_VALUE_ACCESSOR } from "@angular/forms";
import { AppError, ObjectUtils, UserModel, MetadataDefinitionModel, DateConstants, DateUtils, WorkflowStateModel, StringUtils, GroupModel, DocumentTypeModel } from "@app/shared";
import { MessageDisplayer, OrganizationService, NomenclatorService } from "@app/shared";
import { MetadataInstanceModel, ListMetadataItemModel, MetadataCollectionInstanceRowModel, ArrayUtils, MetadataCollectionInstanceModel } from "@app/shared";
import * as moment from "moment";
import { AdminUpdateDocumentMetadataInputData } from "./../document-metadata";
import { MetadataCollectionWindowInputData } from "./metadata-collection-window/metadata-collection-window.component";

@Component({
	selector: "app-admin-update-document-metadata-collection-field",
	templateUrl: "./metadata-collection-field.component.html",
	providers: [
		{ provide: NG_VALUE_ACCESSOR, useExisting: AdminUpdateDocumentMetadataCollectionFieldComponent, multi: true }
	]
})
export class AdminUpdateDocumentMetadataCollectionFieldComponent implements ControlValueAccessor, OnInit {

	@Input()
	public inputData: AdminUpdateDocumentMetadataCollectionFieldInputData;

	@Input()
	public readonly: boolean;

	private nomenclatorService: NomenclatorService;
	private messageDisplayer: MessageDisplayer;
	private organizationService: OrganizationService;

	private metadataDefinitionByIdMap: object;
	public columns: any[];
	private rowModelByIndexMap: object;
	public rowViews: MetadataCollectionRowView[];
	private indexOfRowModelInEditing: number;
	public loading: boolean;

	public selectedRowView: MetadataCollectionRowView;

	public addActionEnabled: boolean;
	public editActionEnabled: boolean;
	public removeActionEnabled: boolean;

	public metadataCollectionWindowVisible: boolean;
	public metadataCollectionWindowInputData: MetadataCollectionWindowInputData;

	private nomenclatorUiValueByValueId: object;
	private userModelByUserId: object;

	private onChange: any = () => { };
	private onTouched: any = () => { };

	public constructor(nomenclatorService: NomenclatorService, organizationService: OrganizationService, messageDisplayer: MessageDisplayer) {
		this.nomenclatorService = nomenclatorService;
		this.organizationService = organizationService;
		this.messageDisplayer = messageDisplayer;
		this.metadataCollectionWindowVisible = false;	
		this.rowModelByIndexMap = {};
		this.metadataDefinitionByIdMap = {};
		this.nomenclatorUiValueByValueId = {};
		this.userModelByUserId = {};
		this.loading = false;
	}

	public ngOnInit(): void {
		if (ObjectUtils.isNullOrUndefined(this.inputData)) {
			throw new Error("inputData cannot be null/undefined");
		}
		if (ObjectUtils.isNullOrUndefined(this.inputData.metadataCollectionDefinition)) {
			throw new Error("inputData.metadataDefinitio cannot be null/undefined");
		}
		this.init();
	}

	private init(): void {
		this.changePerspective();
		this.prepareColumns();

		this.inputData.metadataCollectionDefinition.metadataDefinitions.forEach((metadataDefinition: MetadataDefinitionModel) => {
			this.metadataDefinitionByIdMap[metadataDefinition.id] = metadataDefinition;
		});
	}

	private disableAllActions(): void {
		this.addActionEnabled = false;
		this.editActionEnabled = false;
		this.removeActionEnabled = false;
	}

	private prepareColumns(): void {
		this.columns = [];
		this.inputData.metadataCollectionDefinition.metadataDefinitions.forEach((metadataDefinition: MetadataDefinitionModel) => {
			this.columns.push({
				header: metadataDefinition.label,
				field: metadataDefinition.id
			});		
		});
	}

	private propagateValue(): void {
		let metadataCollectionInstance: MetadataCollectionInstanceModel = null;
		if (ArrayUtils.isNotEmpty(Object.keys(this.rowModelByIndexMap))) {
			metadataCollectionInstance = new MetadataCollectionInstanceModel();
			metadataCollectionInstance.metadataDefinitionId = this.inputData.metadataCollectionDefinition.id;
			metadataCollectionInstance.collectionInstanceRows = [];			
			let numberOfRows: number = Object.keys(this.rowModelByIndexMap).length;
			for (let rowModelIndex = 0; rowModelIndex < numberOfRows; rowModelIndex++) {
				metadataCollectionInstance.collectionInstanceRows.push(this.rowModelByIndexMap[rowModelIndex]);
			}
		}		
		this.onChange(metadataCollectionInstance);
		this.onTouched();
	}

	private prepareRowViews(): void {
		this.rowViews = [];
		if (ArrayUtils.isEmpty(Object.keys(this.rowModelByIndexMap))) {
			return;
		}
		// TODO - Partea de afisare a valorilor pentru metadatele nomenclator poate fi
		// optimizata -- dar a trebuit sa fie gata -- cat si aceasta metoda sa fie 
		// refacuta mai bine (pe viitor cand va fi timp).
		this.loading = true;		
		let rowViewsForUpdateNomenclatorUiValue: MetadataCollectionRowView[] = [];		
		let numberOfRows: number = Object.keys(this.rowModelByIndexMap).length;
		for (let rowModelIndex = 0; rowModelIndex < numberOfRows; rowModelIndex++) {
			let rowModel: MetadataCollectionInstanceRowModel = this.rowModelByIndexMap[rowModelIndex];
			let rowView: MetadataCollectionRowView = new MetadataCollectionRowView();
			rowView.rowModelIndex = rowModelIndex;		
			if (ArrayUtils.isNotEmpty(rowModel.metadataInstanceList)) {
				rowModel.metadataInstanceList.forEach((metadataInstance: MetadataInstanceModel) => {
					let metadataDefinition: MetadataDefinitionModel = this.metadataDefinitionByIdMap[metadataInstance.metadataDefinitionId];
					if (ObjectUtils.isNullOrUndefined(metadataDefinition)) {
						return;
					}
					if (ArrayUtils.isEmpty(metadataInstance.values)) {
						return;
					}
					if (metadataDefinition.type === MetadataDefinitionModel.TYPE_LIST) {						
						if (ArrayUtils.isNotEmpty(metadataDefinition.listItems)) {
							metadataDefinition.listItems.forEach((listItem: ListMetadataItemModel) => {
								if (listItem.value === metadataInstance.values[0]) {
									rowView.metadataValueByDefinitionId[metadataDefinition.id] = listItem.label;
								}
							});
						}
					} else if (metadataDefinition.type === MetadataDefinitionModel.TYPE_DATE) {
						let date: Date = moment(metadataInstance.values[0], DateConstants.DATE_FORMAT_FOR_STORAGE).toDate();
						rowView.metadataValueByDefinitionId[metadataDefinition.id] = DateUtils.formatForDisplay(date);
					} else if (metadataDefinition.type === MetadataDefinitionModel.TYPE_DATE_TIME) {
						let date: Date = moment(metadataInstance.values[0], DateConstants.DATE_TIME_FORMAT_FOR_STORAGE).toDate();
						rowView.metadataValueByDefinitionId[metadataDefinition.id] = DateUtils.formatDateTimeForDisplay(date);
					} else if (metadataDefinition.type === MetadataDefinitionModel.TYPE_NOMENCLATOR) {
						let nomenclatorValueId: string = metadataInstance.values[0];
						if (StringUtils.isNotBlank(nomenclatorValueId)) {
							let uiValue: string = this.nomenclatorUiValueByValueId[nomenclatorValueId];
							if (StringUtils.isNotBlank(uiValue)) {
								rowView.metadataValueByDefinitionId[metadataDefinition.id] = uiValue;
							} else {
								rowViewsForUpdateNomenclatorUiValue.push(rowView);
								this.nomenclatorUiValueByValueId[nomenclatorValueId] = null;
							}
						}						
					} else if (metadataDefinition.type === MetadataDefinitionModel.TYPE_USER) {
						// TODO - Nu este ok asa, trebuie facut un mechanism ceva ca si la
						// nomenclatoare care sa faca loading=false cand e gata si sa tina
						// fereastra vizibil in loading - dar acum trebuie sa fie gata si nu e timp -
						let userId: string = metadataInstance.values[0];
						if (StringUtils.isNotBlank(userId)) {
							let cachedUserModel: UserModel = this.userModelByUserId[userId];
							if (ObjectUtils.isNotNullOrUndefined(cachedUserModel)) {
								rowView.metadataValueByDefinitionId[metadataDefinition.id] = cachedUserModel.displayName;
							} else {
								this.organizationService.getUserById(userId, {
									onSuccess: (userModel: UserModel): void => {
										this.userModelByUserId[userId] = userModel;
										rowView.metadataValueByDefinitionId[metadataDefinition.id] = userModel.displayName;
									},
									onFailure: (error: AppError): void => {
										this.messageDisplayer.displayAppError(error);
									}
								});
							}							
						}						
					} else if (metadataDefinition.type === MetadataDefinitionModel.TYPE_GROUP) {
						let groupId: string = metadataInstance.values[0];
						this.organizationService.getGroupById(groupId, {
							onSuccess: (groupModel: GroupModel): void => {
								rowView.metadataValueByDefinitionId[metadataDefinition.id] = groupModel.name;
							},
							onFailure: (error: AppError): void => {
								this.messageDisplayer.displayAppError(error);
							}
						});
					} else {
						rowView.metadataValueByDefinitionId[metadataDefinition.id] = metadataInstance.values[0];
					}
				});
			}
			this.rowViews.push(rowView);
		}

		let nomenclatorValueIds: number[] = [];
		Object.keys(this.nomenclatorUiValueByValueId).forEach((key: string) => {
			if (ObjectUtils.isNullOrUndefined(this.nomenclatorUiValueByValueId[key])) {
				nomenclatorValueIds.push(parseInt(key, 0));
			}
		});
		if (ArrayUtils.isNotEmpty(nomenclatorValueIds)) {
			this.nomenclatorService.getUiAttributeValues(nomenclatorValueIds, {
				onSuccess: (uiValues: object) => {
					rowViewsForUpdateNomenclatorUiValue.forEach((rowView: MetadataCollectionRowView) => {
						let rowModel: MetadataCollectionInstanceRowModel = this.rowModelByIndexMap[rowView.rowModelIndex];
						rowModel.metadataInstanceList.forEach((metadataInstance: MetadataInstanceModel) => {
							let metadataDefinition: MetadataDefinitionModel = this.metadataDefinitionByIdMap[metadataInstance.metadataDefinitionId];							
							if (ArrayUtils.isNotEmpty(metadataInstance.values)) {
								if (metadataDefinition.type === MetadataDefinitionModel.TYPE_NOMENCLATOR) {
									let nomenclatorValueId: string = metadataInstance.values[0];
									if (StringUtils.isNotBlank(nomenclatorValueId)) {
										let uiValue: string = uiValues[nomenclatorValueId];
										if (StringUtils.isNotBlank(uiValue)) {
											rowView.metadataValueByDefinitionId[metadataDefinition.id] = uiValue;
											this.nomenclatorUiValueByValueId[nomenclatorValueId] = uiValue;
										}
									}						
								}
							}
						});
					});
					this.loading = false;
				},
				onFailure: (error: AppError) => {
					this.messageDisplayer.displayAppError(error);
				}
			});
		} else {
			this.loading = false;
		}
	}

	public onRowSelect(event: any): void {
		this.changePerspective();
	}

	public onRowUnSelect(event: any): void {
		this.changePerspective();
	}

	private changePerspective(): void {
		this.disableAllActions();
		if (!this.readonly) {
			this.addActionEnabled = true;
			this.editActionEnabled = ObjectUtils.isNotNullOrUndefined(this.selectedRowView);
			this.removeActionEnabled = ObjectUtils.isNotNullOrUndefined(this.selectedRowView);
		}
	}

	public onMetadataCollectionWindowSaved(newOrUpdatedModel: MetadataCollectionInstanceRowModel): void {
		let indexOfRowModel: number = this.indexOfRowModelInEditing;
		if (ObjectUtils.isNullOrUndefined(indexOfRowModel)) {
			indexOfRowModel = Object.keys(this.rowModelByIndexMap).length;
		}
		this.completeAutoNumberMetadataValue(newOrUpdatedModel);
		this.rowModelByIndexMap[indexOfRowModel] = newOrUpdatedModel;
		this.indexOfRowModelInEditing = null;				
		this.refreshAfterChanged();
		this.metadataCollectionWindowVisible = false;
	}

	private completeAutoNumberMetadataValue(newOrUpdatedModel: MetadataCollectionInstanceRowModel): void {
		if (ObjectUtils.isNotNullOrUndefined(newOrUpdatedModel.id)) {
			return;
		}
		newOrUpdatedModel.metadataInstanceList.forEach((metadataInstance: MetadataInstanceModel) => {
			let metadataDefinition: MetadataDefinitionModel = this.metadataDefinitionByIdMap[metadataInstance.metadataDefinitionId];
			if (metadataDefinition.type === MetadataDefinitionModel.TYPE_AUTO_NUMBER) {
				if (ArrayUtils.isEmpty(metadataInstance.values)) {
					metadataInstance.values = [];					
					metadataInstance.values.push(this.generateMetadataAutoNumberValue(metadataDefinition));
				}
			}
		});
	}

	private generateMetadataAutoNumberValue(metadataDefinition: MetadataDefinitionModel): string {
		let metadataValueNumber: number = this.getMaxValueNumberForMetadataAutoNumber(metadataDefinition) + 1;
		return this.formatMetadataAutoNumberValue(metadataDefinition, metadataValueNumber);
	}

	private formatMetadataAutoNumberValue(metadataDefinition: MetadataDefinitionModel, metadataValueNumber: number): string {
		
		if (ObjectUtils.isNullOrUndefined(metadataValueNumber)) {
			throw Error("Parameter [metadataValueNumber] cannot be null or undefined.");
		}
		if (metadataValueNumber < 0) {
			throw Error("Parameter [metadataValueNumber] cannot be lesser than 0.");
		}

		let metadataAutoNumberValue: string = metadataDefinition.prefix;
		let prefixLength: number = metadataDefinition.prefix.length;
		
		let metadataValueNumberAsString = metadataValueNumber.toString();
		let metadataValueNumberAsStringLength = metadataValueNumberAsString.length;

		if (metadataDefinition.numberLength - (prefixLength + metadataValueNumberAsStringLength) < 0) {
			throw Error("Parameter [metadataValueNumber] cannot have more than " + (metadataDefinition.numberLength -  prefixLength) + " character(s) in length.");
		}
		
		metadataAutoNumberValue = metadataAutoNumberValue.padEnd(metadataDefinition.numberLength - metadataValueNumberAsStringLength, "0");
		metadataAutoNumberValue = metadataAutoNumberValue.padEnd(metadataDefinition.numberLength, metadataValueNumberAsString);
		
		return metadataAutoNumberValue;
	}

	private getMaxValueNumberForMetadataAutoNumber(metadataDefinition: MetadataDefinitionModel): number {
		if (metadataDefinition.type !== MetadataDefinitionModel.TYPE_AUTO_NUMBER) {
			throw new Error("Invalid metadata type here. Type " + MetadataDefinitionModel.TYPE_AUTO_NUMBER + " is expected.");
		}
		let maxValueNumber: number = 0;
		let numberOfRowModels: number = Object.keys(this.rowModelByIndexMap).length;
		for (let rowModelIndex = 0; rowModelIndex < numberOfRowModels; rowModelIndex++) {
			let rowModel: MetadataCollectionInstanceRowModel = this.rowModelByIndexMap[rowModelIndex];
			if (ArrayUtils.isNotEmpty(rowModel.metadataInstanceList)) {
				rowModel.metadataInstanceList.forEach((metadataInstance: MetadataInstanceModel) => {
					if (metadataInstance.metadataDefinitionId === metadataDefinition.id) {
						let value: string = metadataInstance.values[0];
						value = value.replace(metadataDefinition.prefix, "");
						let valueNumber: number = parseInt(value, 0);
						if (valueNumber > maxValueNumber) {
							maxValueNumber = valueNumber;
						}
					}
				});
			}
		}
		return maxValueNumber;
	}

	public onMetadataCollectionWindowCanceled(event: any): void {
		this.metadataCollectionWindowVisible = false;
		this.indexOfRowModelInEditing = null;
	}

	private refreshAfterChanged(): void {
		this.prepareRowViews();
		this.selectedRowView = null;
		this.propagateValue();
		this.changePerspective();
	}

	public onAddAction(event: any): void {
		this.metadataCollectionWindowInputData = {
			documentId: this.inputData.documentId,
			documentLocationRealName: this.inputData.documentLocationRealName,
			documentType: this.inputData.documentType,
			metadataCollectionDefinition: this.inputData.metadataCollectionDefinition,
			collectionInstanceRow: null

		};
		this.metadataCollectionWindowVisible = true;
	}

	public onEditAction(event: any): void {
		if (ObjectUtils.isNullOrUndefined(this.selectedRowView)) {
			return;
		}
		this.indexOfRowModelInEditing = this.selectedRowView.rowModelIndex;
		this.metadataCollectionWindowInputData = {
			documentId: this.inputData.documentId,
			documentLocationRealName: this.inputData.documentLocationRealName,
			documentType: this.inputData.documentType,
			metadataCollectionDefinition: this.inputData.metadataCollectionDefinition,
			collectionInstanceRow: this.cloneRowModel(this.rowModelByIndexMap[this.indexOfRowModelInEditing])
		};
		this.metadataCollectionWindowVisible = true;
	}

	private cloneRowModel(rowModel: MetadataCollectionInstanceRowModel): MetadataCollectionInstanceRowModel {
		let clone: MetadataCollectionInstanceRowModel = new MetadataCollectionInstanceRowModel();
		clone.id = rowModel.id;
		clone.metadataInstanceList = [];
		if (ArrayUtils.isNotEmpty(rowModel.metadataInstanceList)) {
			rowModel.metadataInstanceList.forEach((metadataInstance: MetadataInstanceModel) => {
				clone.metadataInstanceList.push(this.cloneMetadataInstanceModel(metadataInstance));
			});
		}
		return clone;
	}

	private cloneMetadataInstanceModel(metadataInstance: MetadataInstanceModel): MetadataInstanceModel {
		let clone: MetadataInstanceModel = new MetadataInstanceModel();
		clone.metadataDefinitionId = metadataInstance.metadataDefinitionId;
		clone.values = [];
		metadataInstance.values.forEach((value: string) => {
			clone.values.push(value);
		});
		return clone;
	}
	
	public onRemoveAction(event: any): void {
		if (ObjectUtils.isNullOrUndefined(this.selectedRowView)) {
			return;
		}
		let newRowModelByIndexMap = {};		
		let newRowModelIndex: number = 0;
		let numberOfRows: number = Object.keys(this.rowModelByIndexMap).length;
		for (let rowModelIndex = 0; rowModelIndex < numberOfRows; rowModelIndex++) {
			if (rowModelIndex !== this.selectedRowView.rowModelIndex) {
				newRowModelByIndexMap[newRowModelIndex] = this.rowModelByIndexMap[rowModelIndex];
				newRowModelIndex++;
			}
		}
		this.rowModelByIndexMap = newRowModelByIndexMap;
		this.refreshAfterChanged();
	}

	public writeValue(metadataCollectionInstance: MetadataCollectionInstanceModel): void {	
		this.rowModelByIndexMap = {};	
		if (ObjectUtils.isNotNullOrUndefined(metadataCollectionInstance) && ArrayUtils.isNotEmpty(metadataCollectionInstance.collectionInstanceRows)) {
			metadataCollectionInstance.collectionInstanceRows.forEach((rowModel: MetadataCollectionInstanceRowModel, index: number) => {
				this.rowModelByIndexMap[index] = rowModel;
			});
		}	
		this.prepareRowViews();
	}

	public registerOnChange(fn: any): void {
		this.onChange = fn;
	}

	public registerOnTouched(fn: any): void {
		this.onTouched = fn;
	}
}

class MetadataCollectionRowView {
	
	public rowModelIndex: number;
	public metadataValueByDefinitionId: object;

	public constructor() {
		this.metadataValueByDefinitionId = {};
	}
}

export interface AdminUpdateDocumentMetadataCollectionFieldInputData {
	
	documentId?: string;
	documentLocationRealName?: string;
	documentType: DocumentTypeModel;
	
	metadataCollectionDefinition: MetadataDefinitionModel;
}