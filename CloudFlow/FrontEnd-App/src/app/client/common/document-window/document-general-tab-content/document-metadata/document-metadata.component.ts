import { Component, Input, OnInit } from "@angular/core";
import { FormGroup } from "@angular/forms";
import { DateUtils, BooleanUtils, ObjectUtils, StringUtils, AppError, DateConstants, DocumentService } from "@app/shared";
import { MetadataInstanceModel, DocumentTypeModel, NomenclatorMultipleFilter, ArrayUtils, NomenclatorMetadataDefinitionValueSelectionFilterModel, ValueOfNomenclatorValueField, NomenclatorUtils } from "@app/shared";
import { MetadataDefinitionModel, WorkflowStateModel, UserModel, NomenclatorAttributeModel, NomenclatorValueModel, SecurityManagerModel } from "@app/shared";
import { AutocompleteMetadataRequestModel, AutocompleteMetadataResponseModel } from "@app/shared";
import { MessageDisplayer, OrganizationService, NomenclatorService, AclService } from "@app/shared";
import { MetadataUtils } from "./metadata-utils";
import { MetadataEventMediator, MetadataEventHandler, MetadataEventName, MetadataEvent } from "./metadata-event-mediator";
import { AbstractControl } from "@angular/forms/src/model";
import { MetadataCollectionFieldInputData } from "./../metadata-collection-field/metadata-collection-field.component";
import { DocumentFormDataProvider } from "./../document-form-data-provider";

@Component({
	selector: "app-document-metadata",
	templateUrl: "./document-metadata.component.html"
})
export class DocumentMetadataComponent implements OnInit {

	@Input()
	public inputData: DocumentMetadataInputData;

	@Input()
	public formGroup: FormGroup;

	@Input()
	public documentReadonly: boolean;

	@Input()
	public documentMode: "add" | "viewOrEdit";

	@Input()
	public eventMediator: MetadataEventMediator;

	private organizationService: OrganizationService;
	private nomenclatorService: NomenclatorService;
	private messageDisplayer: MessageDisplayer;
	private aclService: AclService;
	private documentService: DocumentService;

	public metadataDefinition: MetadataDefinitionModel;
	private documentWorkflowState: WorkflowStateModel;

	public mandatory: boolean;
	public visible: boolean;
	public readonly: boolean;

	public dateFormat: string;
	public dateTimeFormat: string;
	public monthFormat: string;
	public yearRange: string;

	public nomenclatorCustomFilters: NomenclatorMultipleFilter[] = [];
	public nomenclatorFilterValuesFromMetadatas: object = {};

	public metadataCollectionFieldInputData: MetadataCollectionFieldInputData;

	public constructor(messageDisplayer: MessageDisplayer, organizationService: OrganizationService,
		nomenclatorService: NomenclatorService, aclService: AclService, documentService: DocumentService) {
		this.messageDisplayer = messageDisplayer;
		this.organizationService = organizationService;
		this.nomenclatorService = nomenclatorService;
		this.aclService = aclService;
		this.documentService = documentService;
		this.dateFormat = DateConstants.DATE_FORMAT_FOR_TYPING;
		this.dateTimeFormat = DateConstants.DATE_TIME_FORMAT_FOR_TYPING;
		this.monthFormat = DateConstants.MONTH_FORMAT_FOR_TYPING;
		this.yearRange = DateUtils.getDefaultYearRangeForMetadata();
	}

	public ngOnInit(): void {

		if (ObjectUtils.isNullOrUndefined(this.inputData)) {
			throw new Error("inputData cannot be null");
		}

		this.metadataDefinition = this.inputData.metadataDefinition;
		this.documentWorkflowState = this.inputData.documentWorkflowState;
		this.makeMetadataCollectionFieldInputDataIfApplicable();

		this.readonly = (this.documentReadonly || MetadataUtils.isRestrictedOnEdit(this.inputData.metadataDefinition, this.inputData.documentWorkflowState));
		if (this.documentReadonly) {
			this.mandatory = false;
		} else {
			this.mandatory = MetadataUtils.isMetadataMandatory(this.inputData.metadataDefinition, this.inputData.documentWorkflowState);
		}
		this.visible = MetadataUtils.isMetadataVisible(this.inputData.metadataDefinition, this.inputData.documentWorkflowState);

		this.fireMetadataInitializedEvent();
		this.subscribeForEvents();
	}

	private isDocumentModeAdd(): boolean {
		return this.documentMode === "add";
	}

	private isDocumentModeViewOrEdit(): boolean {
		return this.documentMode === "viewOrEdit";
	}

	public makeMetadataCollectionFieldInputDataIfApplicable(): void {
		if (this.metadataDefinition.type === MetadataDefinitionModel.TYPE_METADATA_COLLECTION) {
			this.metadataCollectionFieldInputData = {
				documentId: this.inputData.documentId,
				documentLocationRealName: this.inputData.documentLocationRealName,
				documentType: this.inputData.documentType,
				documentWorkflowState: this.inputData.documentWorkflowState,
				metadataCollectionDefinition: this.inputData.metadataDefinition,
				documentFormDataProvider: this.inputData.documentFormDataProvider
			};
		}
	}

	public get metadataFormControl() {
		return this.formGroup.get(this.inputData.metadataDefinition.name);
	}

	private autoCompleteWithDefaultValue(): void {

		// Daca documentul este in readonly atunci nu completez.
		if (BooleanUtils.isTrue(this.documentReadonly)) {
			return;
		}

		// Daca este colectie atunci nu e cazul (deocamdata).
		if (this.inputData.metadataDefinition.type === MetadataDefinitionModel.TYPE_METADATA_COLLECTION) {
			return;
		}

		// Daca deja are o valoare completata atunci nu completez.
		let currentValue: any = this.metadataFormControl.value;
		if (ObjectUtils.isNotNullOrUndefined(currentValue)) {
			return;
		}

		if (this.isDocumentModeAdd() || this.mandatory) {
			let defaultValue: any = null;
			if (this.metadataDefinition.type === MetadataDefinitionModel.TYPE_DATE) {
				if (BooleanUtils.isTrue(this.metadataDefinition.autoCompleteWithCurrentDate)) {
					defaultValue = new Date();
				} else if (ObjectUtils.isNotNullOrUndefined(this.metadataDefinition.defaultValue)) {
					defaultValue = DateUtils.parseFromStorage(this.metadataDefinition.defaultValue);
				}
			} else if (this.metadataDefinition.type === MetadataDefinitionModel.TYPE_DATE_TIME) {
				if (BooleanUtils.isTrue(this.metadataDefinition.autoCompleteWithCurrentDateTime)) {
					defaultValue = new Date();
				} else if (ObjectUtils.isNotNullOrUndefined(this.metadataDefinition.defaultValue)) {
					defaultValue = DateUtils.parseDateTimeFromStorage(this.metadataDefinition.defaultValue);
				}
			} else if (this.metadataDefinition.type === MetadataDefinitionModel.TYPE_MONTH) {
				if (BooleanUtils.isTrue(this.metadataDefinition.autoCompleteWithCurrentMonth)) {
					defaultValue = new Date();
				} else if (ObjectUtils.isNotNullOrUndefined(this.metadataDefinition.defaultValue)) {
					defaultValue = DateUtils.parseMonthYearFromStorage(this.metadataDefinition.defaultValue);
				}
			} else if (this.metadataDefinition.type === MetadataDefinitionModel.TYPE_USER) {
				if (ObjectUtils.isNotNullOrUndefined(this.metadataDefinition.defaultValue)) {
					defaultValue = this.metadataDefinition.defaultValue;
				} else if (BooleanUtils.isTrue(this.metadataDefinition.autoCompleteWithCurrentUser)
					&& ObjectUtils.isNotNullOrUndefined(this.documentWorkflowState)
					&& this.metadataDefinition.autoCompleteWithCurrentUserStateCode === this.documentWorkflowState.code) {
					this.aclService.getSecurityManager({
						onSuccess: (securityManager: SecurityManagerModel): void => {
							this.metadataFormControl.setValue(securityManager.userIdAsString);
							this.fireValueChangeEvent();
						},
						onFailure: (appError: AppError): void => {
							this.messageDisplayer.displayAppError(appError);
						}
					});
				}
			} else {
				if (ObjectUtils.isNotNullOrUndefined(this.metadataDefinition.defaultValue)) {
					if (this.metadataDefinition.type === MetadataDefinitionModel.TYPE_LIST) {
						defaultValue = [this.metadataDefinition.defaultValue];
					} else if (this.metadataDefinition.type === MetadataDefinitionModel.TYPE_NOMENCLATOR
						|| this.metadataDefinition.type === MetadataDefinitionModel.TYPE_CALENDAR) {
						defaultValue = StringUtils.toNumber(this.metadataDefinition.defaultValue);
					} else {
						defaultValue = this.metadataDefinition.defaultValue;
					}
				}
			}

			if (ObjectUtils.isNotNullOrUndefined(defaultValue)) {
				this.metadataFormControl.setValue(defaultValue);
				this.fireValueChangeEvent();
			}
		}
	}

	public onMetadataValueChanged(): void {
		this.fireValueChangeEvent();
	}

	private fireMetadataInitializedEvent(): void {
		if (ObjectUtils.isNotNullOrUndefined(this.eventMediator)) {
			this.eventMediator.fireEvent({
				eventName: MetadataEventName.METADATA_INITIALIZED,
				metadataDefinition: this.metadataDefinition,
				metadataValue: this.metadataFormControl.value
			});
		}
	}

	private fireValueChangeEvent(): void {
		if (ObjectUtils.isNotNullOrUndefined(this.eventMediator)) {
			this.eventMediator.fireEvent({
				eventName: MetadataEventName.METADATA_VALUE_CHANGE,
				metadataDefinition: this.metadataDefinition,
				metadataValue: this.metadataFormControl.value
			});
		}
	}

	private subscribeForEvents(): void {
		if (ObjectUtils.isNullOrUndefined(this.eventMediator) || this.documentReadonly) {
			return;
		}
		this.subscribeForEventAllMetadatasReady();
		this.subscribeForEventMetadataValueChange();
	}

	private subscribeForEventAllMetadatasReady(): void {
		this.eventMediator.subscribe({
			eventName: MetadataEventName.ALL_METADATAS_READY,
			handle: (event: MetadataEvent) => {
				this.autoCompleteWithDefaultValue();
				if (this.isDocumentModeViewOrEdit()) {
					if (ObjectUtils.isNotNullOrUndefined(event.metadatasValues)) {
						Object.keys(event.metadatasValues).forEach((key: string) => {
							this.updateNomenclatorSelectionFilterValuesFromAttributes(key, event.metadatasValues[key]);
						});
					}
				}
				this.prepareNomenclatorCustomFilters(() => {
					this.changeNomenclatorFieldPerspectiveAfterChangeSelectionFilters();
				});
			}
		});
	}

	private subscribeForEventMetadataValueChange(): void {

		let metadataNames: string[] = [];
		if (StringUtils.isNotBlank(this.metadataDefinition.metadataNameForAutoCompleteWithMetadata)) {
			metadataNames.push(this.metadataDefinition.metadataNameForAutoCompleteWithMetadata);
		}
		if (this.metadataDefinition.type === MetadataDefinitionModel.TYPE_NOMENCLATOR) {
			if (ArrayUtils.isNotEmpty(this.metadataDefinition.valueSelectionFilters)) {
				this.metadataDefinition.valueSelectionFilters.forEach((selectionFilter: NomenclatorMetadataDefinitionValueSelectionFilterModel) => {
					if (StringUtils.isNotBlank(selectionFilter.metadataNameForAutocompleteFilterValue)) {
						metadataNames.push(selectionFilter.metadataNameForAutocompleteFilterValue);
					}
				});
			}
		}

		let uniqueMetadataNames: string[] = metadataNames.filter((value, index, self) => {
			return self.indexOf(value) === index;
		});
		uniqueMetadataNames.forEach((metadataName: string) => {
			this.eventMediator.subscribe({
				eventName: MetadataEventName.METADATA_VALUE_CHANGE,
				metadataName: metadataName,
				handle: (event: MetadataEvent): void => {
					this.handleMetadataValueChangeEvent(event);
				}
			});
		});
	}

	private handleMetadataValueChangeEvent(event: MetadataEvent): void {
		if (this.isNomenclatorSelectionFilterApplicable()) {
			this.metadataDefinition.valueSelectionFilters.forEach((selectFilter: NomenclatorMetadataDefinitionValueSelectionFilterModel) => {
				if (StringUtils.isNotBlank(selectFilter.metadataNameForAutocompleteFilterValue)
					&& selectFilter.metadataNameForAutocompleteFilterValue === event.metadataDefinition.name) {
					this.updateNomenclatorSelectionFilterValuesFromAttributes(event.metadataDefinition.name, event.metadataValue);
					this.prepareNomenclatorCustomFilters(() => {
						this.changeNomenclatorFieldPerspectiveAfterChangeSelectionFilters();
					});
				}
			});
		}
		if (StringUtils.isNotBlank(this.metadataDefinition.metadataNameForAutoCompleteWithMetadata)
			&& this.metadataDefinition.metadataNameForAutoCompleteWithMetadata === event.metadataDefinition.name) {
			this.handleAutoCompleteFromMetadata(event);
		}
	}

	private handleAutoCompleteFromMetadata(event: MetadataEvent): void {
		this.metadataFormControl.reset();
		if (ObjectUtils.isNullOrUndefined(event.metadataValue)) {
			return;
		}
		if (event.metadataDefinition.type === MetadataDefinitionModel.TYPE_USER) {
			this.autoCompleteFromMetadataUser(event.metadataValue);
		} else if (event.metadataDefinition.type === MetadataDefinitionModel.TYPE_NOMENCLATOR) {
			this.autoCompleteFromMetadataNomenclator(event.metadataValue);
		} else if (event.metadataDefinition.type === MetadataDefinitionModel.TYPE_TEXT || event.metadataDefinition.type === MetadataDefinitionModel.TYPE_TEXT_AREA) {
			this.autoCompleteFromSimpleMetadata(event.metadataValue);
		} else if (event.metadataDefinition.type === MetadataDefinitionModel.TYPE_DOCUMENT) {
			this.autoCompleteFromDocumentMetadata(event);
		} else {
			throw new Error("not implmented for source metadata type [" + event.metadataDefinition.type + "]");
		}
	}

	private autoCompleteFromMetadataUser(userId: string): void {
		this.organizationService.getUserById(userId, {
			onSuccess: (userModel: UserModel): void => {
				if (this.metadataDefinition.typeOfAutoCompleteWithMetadata === MetadataDefinitionModel.TYPE_OF_AUTO_COMPLETE_WITH_METADATA_USER_TITLE) {
					this.metadataFormControl.setValue(userModel.title);
				} else if (this.metadataDefinition.typeOfAutoCompleteWithMetadata === MetadataDefinitionModel.TYPE_OF_AUTO_COMPLETE_WITH_METADATA_USER_EMAIL) {
					this.metadataFormControl.setValue(userModel.email);
				}
			},
			onFailure: (error: AppError): void => {
				this.messageDisplayer.displayAppError(error);
			}
		});
	}

	private autoCompleteFromMetadataNomenclator(nomenclatorValueId: number): void {
		this.nomenclatorService.getNomenclatorValue(nomenclatorValueId, {
			onSuccess: (nomenclatorValueModel: NomenclatorValueModel): void => {
				if (this.metadataDefinition.typeOfAutoCompleteWithMetadata === MetadataDefinitionModel.TYPE_OF_AUTO_COMPLETE_WITH_METADATA_NOMENCLATOR_ATTRIBUTE) {
					let value: string = nomenclatorValueModel[this.metadataDefinition.nomenclatorAttributeKeyForAutoCompleteWithMetadata];
					if (ObjectUtils.isNotNullOrUndefined(value)) {
						this.metadataFormControl.setValue(value);
					}
				}
			},
			onFailure: (error: AppError): void => {
				this.messageDisplayer.displayAppError(error);
			}
		});
	}

	private autoCompleteFromSimpleMetadata(metadataValue: string): void {
		if (this.metadataDefinition.typeOfAutoCompleteWithMetadata === MetadataDefinitionModel.TYPE_OF_AUTO_COMPLETE_WITH_METADATA_METADATA_VALUE) {
			this.metadataFormControl.setValue(metadataValue);
		} else if (this.metadataDefinition.typeOfAutoCompleteWithMetadata === MetadataDefinitionModel.TYPE_OF_AUTO_COMPLETE_WITH_METADATA_DELETE_VALUE) {
			this.metadataFormControl.setValue(null);
		}
	}

	private autoCompleteFromDocumentMetadata(event: MetadataEvent): void {
		if (this.metadataDefinition.typeOfAutoCompleteWithMetadata === MetadataDefinitionModel.TYPE_OF_AUTO_COMPLETE_WITH_METADATA_CUSTOM_CLASS) {
			let request: AutocompleteMetadataRequestModel = new AutocompleteMetadataRequestModel();
			request.targetMetadataCollectionDefinitionId = this.metadataDefinition.id;
			if (ObjectUtils.isNotNullOrUndefined(event.metadataValue)) {
				if (ObjectUtils.isArray(event.metadataValue)) {
					request.sourceMetadataValues = event.metadataValue;
				} else {
					request.sourceMetadataValues = [event.metadataValue];
				}
			}
			this.documentService.autocompleteMetadata(request, {
				onSuccess: (response: AutocompleteMetadataResponseModel): void => {
					if (this.metadataDefinition.type === MetadataDefinitionModel.TYPE_METADATA_COLLECTION) {
						this.metadataFormControl.setValue(response.metadataCollectionInstance);
					} else {
						throw new Error("not implemented for target metadata type [" + this.metadataDefinition.type + "]");
					}
				},
				onFailure: (error: AppError): void => {
					this.messageDisplayer.displayAppError(error);
				}
			});
		} else {
			throw new Error("unimplemented for type [" + this.metadataDefinition.typeOfAutoCompleteWithMetadata + "]");
		}
	}

	private isNomenclatorSelectionFilterApplicable(): boolean {
		return (this.metadataDefinition.type === MetadataDefinitionModel.TYPE_NOMENCLATOR
			&& ArrayUtils.isNotEmpty(this.metadataDefinition.valueSelectionFilters));
	}

	// Argumentul "callback" e pus in eventualitatea ca vor fi si apeluri spre server.
	private prepareNomenclatorCustomFilters(callback: () => any): void {
		let nomenclatorCustomFilters = [];
		if (this.isNomenclatorSelectionFilterApplicable()) {
			this.metadataDefinition.valueSelectionFilters.forEach((selectFilter: NomenclatorMetadataDefinitionValueSelectionFilterModel) => {
				let nomenclatorFilter: NomenclatorMultipleFilter = this.getNomenclatorFilterByAttributeKey(nomenclatorCustomFilters, selectFilter.filterAttributeKey);
				if (nomenclatorFilter == null) {
					nomenclatorFilter = new NomenclatorMultipleFilter();
					nomenclatorFilter.attributeKey = selectFilter.filterAttributeKey;
					nomenclatorFilter.values = [];
					nomenclatorCustomFilters.push(nomenclatorFilter);
				}
				if (StringUtils.isNotBlank(selectFilter.defaultFilterValue)) {
					nomenclatorFilter.values.push(selectFilter.defaultFilterValue);
				}
				if (StringUtils.isNotBlank(selectFilter.metadataNameForAutocompleteFilterValue)) {
					let filterValueByMetadataName = this.nomenclatorFilterValuesFromMetadatas[selectFilter.metadataNameForAutocompleteFilterValue];
					if (ObjectUtils.isNotNullOrUndefined(filterValueByMetadataName)) {
						nomenclatorFilter.values.push(filterValueByMetadataName);
					}

				}
			});
		}
		if (ArrayUtils.isEmpty(nomenclatorCustomFilters)) {
			this.nomenclatorCustomFilters = null;
		} else {
			this.nomenclatorCustomFilters = [];
			nomenclatorCustomFilters.forEach((filter: NomenclatorMultipleFilter) => {
				if (ArrayUtils.isNotEmpty(filter.values)) {
					this.nomenclatorCustomFilters.push(filter);
				}
			});
		}
		callback();
	}

	private getNomenclatorFilterByAttributeKey(nomenclatorCustomFilters: NomenclatorMultipleFilter[], attributeKey: string): NomenclatorMultipleFilter {
		let returnFilter: NomenclatorMultipleFilter = null;
		nomenclatorCustomFilters.forEach((filter: NomenclatorMultipleFilter) => {
			if (filter.attributeKey === attributeKey) {
				returnFilter = filter;
			}
		});
		return returnFilter;
	}

	private updateNomenclatorSelectionFilterValuesFromAttributes(metadataName: string, metadataValue: any): void {
		this.nomenclatorFilterValuesFromMetadatas[metadataName] = null;
		if (ObjectUtils.isNotNullOrUndefined(metadataValue)) {
			if (metadataValue instanceof ValueOfNomenclatorValueField) {
				if (NomenclatorUtils.fieldValueHasValue(metadataValue)) {
					this.nomenclatorFilterValuesFromMetadatas[metadataName] = (<ValueOfNomenclatorValueField>metadataValue).value;
				}
			} else if (metadataValue instanceof Date) {
				this.nomenclatorFilterValuesFromMetadatas[metadataName] = DateUtils.formatForStorage(metadataValue);
			} else if (ObjectUtils.isString(metadataValue)) {
				if (StringUtils.isNotBlank(metadataValue)) {
					this.nomenclatorFilterValuesFromMetadatas[metadataName] = metadataValue;
				}
			} else if (ObjectUtils.isBoolean(metadataValue)) {
				if (metadataValue) {
					this.nomenclatorFilterValuesFromMetadatas[metadataName] = true;
				} else {
					// TODO - De vazut pt false....se pune null acum - oarecum se poate pune filtru doar pe true.
				}
			} else {
				this.nomenclatorFilterValuesFromMetadatas[metadataName] = metadataValue;
			}
		}
	}

	private isOtherMetadataCompletedForNomenclatorSelectionFilters(metadataName: string): boolean {
		let found: boolean = false;
		Object.keys(this.nomenclatorFilterValuesFromMetadatas).forEach((keyAsMetadataName: string) => {
			if (keyAsMetadataName === metadataName && ObjectUtils.isNotNullOrUndefined(this.nomenclatorFilterValuesFromMetadatas[keyAsMetadataName])) {
				found = true;
			}
		});
		return found;
	}

	private changeNomenclatorFieldPerspectiveAfterChangeSelectionFilters(): void {

		// Codul de mai jos se va decomenta cand se va dori ca daca nu sunt
		// completate valori pe metadatele dependente pt. selectie filtru
		// atunci campul sa fie diabled.

		/*
		if (this.documentReadonly) {
			return;
		}
		if (this.isNomenclatorSelectionFilterApplicable()) {
			this.readonly = false;
			let filterValuesByOthersMetadatasCompleted: boolean = true;
			this.metadataDefinition.valueSelectionFilters.forEach((selectionFilter: NomenclatorMetadataDefinitionValueSelectionFilterModel) => {
				if (StringUtils.isNotBlank(selectionFilter.metadataNameForAutocompleteFilterValue)) {
					if (!this.isOtherMetadataCompletedForNomenclatorFilters(selectionFilter.metadataNameForAutocompleteFilterValue)) {
						filterValuesByOthersMetadatasCompleted = false;
					}
				}
			});			
			if (!filterValuesByOthersMetadatasCompleted) {
				this.readonly = true;
			}
		}
		*/
	}
}

export interface DocumentMetadataInputData {

	documentId?: string;
	documentLocationRealName?: string;
	documentType: DocumentTypeModel;

	metadataDefinition: MetadataDefinitionModel;
	documentWorkflowState?: WorkflowStateModel;

	documentFormDataProvider: DocumentFormDataProvider;
}
