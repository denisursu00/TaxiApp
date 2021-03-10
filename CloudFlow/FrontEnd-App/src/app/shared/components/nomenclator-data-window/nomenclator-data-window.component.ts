import { Component, OnInit, ViewChild, Input, Output, EventEmitter, AfterViewInit } from "@angular/core";
import { Validators, FormControl, FormGroup, FormBuilder, ValidatorFn, AbstractControl } from "@angular/forms";
import { ValueOfNomenclatorValueField } from "./../../components/nomenclator-value-field";
import { Message } from "./../../model";
import { AppError, AttributeTypeEnum, NomenclatorAttributeModel, NomenclatorValueModel, NomenclatorModel, SaveNomenclatorValueResponseModel } from "./../../model";
import { MessageDisplayer } from "./../../message-displayer";
import { NomenclatorService } from "./../../service";
import { DateConstants, IconConstants, NomenclatorConstants } from "./../../constants";
import { UrlBuilder, NomenclatorUtils, TranslateUtils, NavigationUtils, ObjectUtils, ArrayUtils, FormUtils, StringUtils, DateUtils, BooleanUtils } from "./../../utils";
import { NomenclatorValidators, StringValidators } from "./../../validators";
import { AttributeEventMediator, AttributeEventName, AttributeEvent } from "./nomenclator-attribute/attribute-event-mediator";
import { NomenclatorRunExpressionsRequestModel, NomenclatorRunExpressionsResponseModel } from "./../../model";
import { Dialog } from "primeng/primeng";
import { BaseWindow } from "./../../base-window";

@Component({
	selector: "app-nomenclator-data-window",
	templateUrl: "./nomenclator-data-window.component.html"
})
export class NomenclatorDataWindowComponent extends BaseWindow implements OnInit, AfterViewInit {

	@Input()
	public inputData: NomenclatorDataWindowInputData;

	@Input()
	public mode: "add" | "edit" | "view";
	
	@Output()
	private windowClosed: EventEmitter<NomenclatorDataWindowEvent>;
	
	private nomenclatorService: NomenclatorService;
	private messageDisplayer: MessageDisplayer;
	private translateUtils: TranslateUtils;

	private formBuilder: FormBuilder;
	public formGroup: FormGroup;

	private nomenclator: NomenclatorModel;

	public windowVisible: boolean = false;
	public formVisible: boolean = false;

	public title: string;
	
	public saveActionEnabled: boolean;
	public closeActionEnabled: boolean;

	public messagesWindowVisible: boolean = false;
	public messagesWindowMessages: Message[];

	public attributeEventMediator: AttributeEventMediator;
	private attributeInitializedEventCounter: number = 0;
	private attributesValuesMap: object = {};
	private attributeKeysThatTriggerRunExpressions: string[] = [];
	private needOfRunExpressionsCounter: number = 0;

	public constructor(nomenclatorService: NomenclatorService, messageDisplayer: MessageDisplayer, 
			translateUtils: TranslateUtils, formBuilder: FormBuilder) {
		super();
		this.nomenclatorService = nomenclatorService;
		this.messageDisplayer = messageDisplayer;
		this.translateUtils = translateUtils;
		this.windowClosed = new EventEmitter();
		this.formBuilder = formBuilder;
		this.attributeEventMediator = new AttributeEventMediator();
	}

	public ngOnInit(): void {
		this.init();
	}

	public ngAfterViewInit(): void {
	}

	private init(): void {
		this.formGroup = this.formBuilder.group([]);
		this.prepareByMode();
	}

	private reset(): void {
		this.formGroup.reset();
		this.formVisible = false;
	}

	private closeWindow(dataChanged: boolean): void {
		this.unlock();
		this.windowVisible = false;
		this.windowClosed.emit({
			dataChanged: dataChanged
		});
	}

	private openWindow(): void {
		this.unlock();
		this.windowVisible = true;
	}
	
	private prepareByMode() {
		this.reset();		
		if (this.isAdd()) {
			this.prepareForAdd();
		} else if (this.isEdit() || this.isView) {
			this.prepareForViewOrEdit();
		}
	}

	private isAdd(): boolean {
		return this.mode === "add";
	}

	private isEdit(): boolean {
		return this.mode === "edit";
	}

	public get isView(): boolean {
		return this.mode === "view";
	}

	private prepareForAdd(): void {
		this.lock();
		this.nomenclatorService.getNomenclator(this.inputData.nomenclatorId, {
			onSuccess: (nomenclatorModel: NomenclatorModel): void => {
				this.sortAttributes(nomenclatorModel.attributes);			
				this.subscribeToAttributeInitializedEvent();
				this.nomenclator = nomenclatorModel;	
				this.prepareAttributeFormControls();
				this.updatePerspectiveForAdd();
				this.formVisible = true;
				this.openWindow();
			},
			onFailure: (error: AppError): void => {
				this.messageDisplayer.displayAppError(error);
				this.closeWindow(false);
			}
		});
	}

	private addOrUpdateAttributeValueFromEvent(event: AttributeEvent): void {
		
		let attributeKey: string = event.attributeDefinition.key;
		let attributeValue: any = event.attributeValue;

		this.attributesValuesMap[attributeKey] = null;
		if (ObjectUtils.isNotNullOrUndefined(attributeValue)) {
			if (attributeValue instanceof ValueOfNomenclatorValueField) {
				if (NomenclatorUtils.fieldValueHasValue(attributeValue)) {
					this.attributesValuesMap[attributeKey] = (<ValueOfNomenclatorValueField> attributeValue).value;
				}
			} else if (attributeValue instanceof Date) {
				this.attributesValuesMap[attributeKey] = DateUtils.formatForStorage(attributeValue);
			} else if (ObjectUtils.isString(attributeValue)) {
				if (StringUtils.isNotBlank(attributeValue)) {
					this.attributesValuesMap[attributeKey] = attributeValue;
				}
			} else if (ObjectUtils.isBoolean(attributeValue)) {
				this.attributesValuesMap[attributeKey] = BooleanUtils.isTrue(attributeValue);			
			} else {
				this.attributesValuesMap[attributeKey] = attributeValue;
			}
		}
	}

	private subscribeToAttributeInitializedEvent(): void {		
		this.attributeEventMediator.subscribe({
			eventName: AttributeEventName.ATTRIBUTE_INITIALIZED,
			handle: (event: AttributeEvent) => {

				this.addAttributeForTriggerRunExpresionsIfApplicable(event.attributeDefinition.key);
				this.addOrUpdateAttributeValueFromEvent(event);
				
				this.attributeInitializedEventCounter++;
				if (this.attributeInitializedEventCounter === this.nomenclator.attributes.length) {
					this.subscribeToAttributesThatTriggerRunExpressions();
					setTimeout(() => {
						this.runExpressions(false, (): void => {						
							setTimeout(() => {
								this.attributeEventMediator.fireEvent({
									eventName: AttributeEventName.ALL_ATTRIBUTES_READY,
									attributeValues: this.attributesValuesMap
								});
							}, 1);
						});
					}, 1);					
				}
			}
		});
	}

	private subscribeToAttributesThatTriggerRunExpressions(): void {
		if (ArrayUtils.isEmpty(this.attributeKeysThatTriggerRunExpressions)) {
			return;
		}
		this.attributeKeysThatTriggerRunExpressions.forEach((attributeKey: string) => {
			this.attributeEventMediator.subscribe({
				eventName: AttributeEventName.ATTRIBUTE_VALUE_CHANGE,
				attributeKey: attributeKey,
				handle: (event: AttributeEvent) => {
					this.addOrUpdateAttributeValueFromEvent(event);
					this.runExpressions(false);
				}
			});
		});
	}

	private get expressionsRunnable(): boolean {
		return ArrayUtils.isNotEmpty(this.attributeKeysThatTriggerRunExpressions);
	}

	private runExpressions(selfCall: boolean, callback?: () => any): void {
		if (!this.expressionsRunnable) {
			if (ObjectUtils.isNotNullOrUndefined(callback)) {
				callback();
			}
			return;
		}
		if (!selfCall) {
			this.needOfRunExpressionsCounter++;
			if (this.needOfRunExpressionsCounter > 1) {
				return;
			}
		}

		this.lock();

		let requestModel: NomenclatorRunExpressionsRequestModel = new NomenclatorRunExpressionsRequestModel();
		requestModel.nomenclatorId = this.inputData.nomenclatorId;
		requestModel.attributeValueByKey = this.attributesValuesMap;
		
		this.nomenclatorService.runExpressions(requestModel, {
			onSuccess: (responseModel: NomenclatorRunExpressionsResponseModel): void => {
				
				this.needOfRunExpressionsCounter--;
				
				this.attributeEventMediator.fireEvent({
					eventName: AttributeEventName.EXPRESSIONS_RAN,
					expressionsRanResponse: responseModel
				});
				
				if (this.needOfRunExpressionsCounter > 0) {
					this.runExpressions(true, callback);
				} else {
					if (ObjectUtils.isNotNullOrUndefined(callback)) {
						callback();
					}					
					this.unlock();
				}				
			},
			onFailure: (error: AppError): void => {
				this.messageDisplayer.displayAppError(error);
			}
		});
	}

	private addAttributeForTriggerRunExpresionsIfApplicable(attributeKey: string) {
		// TODO - Chestia de a sti ce atribute fac trigger la run expressions ar fi mai bine sa vina 
		// din backend. Deocamdata, ramane asa ca nu mai avem timp.
		this.nomenclator.attributes.forEach((attributeDef: NomenclatorAttributeModel) => {
			if (this.isAttributeUsedInExpressions(attributeKey, attributeDef.requiredCheckExpression)) {
				this.attributeKeysThatTriggerRunExpressions.push(attributeKey);
			}
			if (this.isAttributeUsedInExpressions(attributeKey, attributeDef.invisibleCheckExpression)) {
				this.attributeKeysThatTriggerRunExpressions.push(attributeKey);
			}
		});
		this.attributeKeysThatTriggerRunExpressions = this.attributeKeysThatTriggerRunExpressions.filter((value, index, self) => {
			return self.indexOf(value) === index;
		});
	}

	private isAttributeUsedInExpressions(attributeKey: string, expressions: string): boolean {
		if (StringUtils.isBlank(attributeKey)) {
			return false;
		}
		if (StringUtils.isBlank(expressions)) {
			return false;
		}
		let atributeKeyAsPlaceHolder: string = "{" + attributeKey + "}";
		return StringUtils.contains(expressions, atributeKeyAsPlaceHolder);
	}

	private prepareForViewOrEdit(): void {
		this.lock();
		this.nomenclatorService.getNomenclator(this.inputData.nomenclatorId, {
			onSuccess: (nomenclatorModel: NomenclatorModel): void => {
				this.sortAttributes(nomenclatorModel.attributes);							
				this.subscribeToAttributeInitializedEvent();
				this.nomenclator = nomenclatorModel;				
				this.nomenclatorService.getNomenclatorValue(this.inputData.nomenclatorValueId, {
					onSuccess: (nomenclatorValue: NomenclatorValueModel): void => {
						this.prepareAttributeFormControls(nomenclatorValue);
						this.updatePerspectiveForViewOrEdit();
						this.formVisible = true;
						this.openWindow();
					},
					onFailure: (error: AppError) => {
						this.messageDisplayer.displayAppError(error);
						this.closeWindow(false);
					}
				});				
			},
			onFailure: (error: AppError): void => {
				this.messageDisplayer.displayAppError(error);
				this.closeWindow(false);
			}
		});
	}

	private prepareAttributeFormControls(nomenclatorValue?: NomenclatorValueModel): void {
		this.nomenclator.attributes.forEach((attribute: NomenclatorAttributeModel) => {
			let attributeFormControl: FormControl = new FormControl();
			attributeFormControl.setValue(this.prepareAttributeValue(attribute, nomenclatorValue));
			if (this.isAdd() || this.isEdit()) {
				let validators: any[] = [];
				if (attribute.type === AttributeTypeEnum.NUMERIC) {
					validators.push(StringValidators.numeric());
				}
				if (BooleanUtils.isTrue(attribute.required)) {
					if (attribute.type !== AttributeTypeEnum.BOOLEAN) {
						validators.push(Validators.required);					
						if (attribute.type === AttributeTypeEnum.NOMENCLATOR) {
							validators.push(NomenclatorValidators.nomenclatorValueRequired());
						}
						if (attribute.type === AttributeTypeEnum.TEXT || attribute.type === AttributeTypeEnum.NUMERIC) {
							validators.push(StringValidators.blank());	
						}
					}
				}
				attributeFormControl.clearValidators();
				if (ArrayUtils.isNotEmpty(validators)) {
					attributeFormControl.setValidators(validators);
				}				
			}
			this.formGroup.addControl(attribute.key, attributeFormControl);
		});
	}

	private prepareAttributeValue(attribute: NomenclatorAttributeModel, nomenclatorValue?: NomenclatorValueModel): any {
		let rawValue: string = ObjectUtils.isNotNullOrUndefined(nomenclatorValue) ? nomenclatorValue[attribute.key] : null;
		if (attribute.type === AttributeTypeEnum.DATE) {
			if (StringUtils.isBlank(rawValue)) {
				return null;
			}
			return DateUtils.parseFromStorage(rawValue);
		} else if (attribute.type === AttributeTypeEnum.NOMENCLATOR) {
			let valueOfNomenclatorValueField: ValueOfNomenclatorValueField = new ValueOfNomenclatorValueField(attribute.typeNomenclatorId);
			if (StringUtils.isNotBlank(rawValue)) {
				valueOfNomenclatorValueField.value = parseInt(rawValue, 0);
			}
			return valueOfNomenclatorValueField;
		} else if (attribute.type === AttributeTypeEnum.BOOLEAN) {
			return ("true" === rawValue);
		}
		return rawValue;
	}

	private setTitle(): void {
		let action: string = null;
		if (this.isAdd()) {
			action = this.translateUtils.translateLabel("ADD");	
		} else if (this.isEdit()) {
			action = this.translateUtils.translateLabel("EDIT");	
		} else if (this.isView) {
			action = this.translateUtils.translateLabel("VIEW");	
		}
		this.title = action + " [" + this.nomenclator.name + "]";
	}

	private updatePerspectiveForAdd(): void {
		this.disableAllActions();
		this.saveActionEnabled = true;
		this.closeActionEnabled = true;
		this.setTitle();
	}

	private updatePerspectiveForViewOrEdit(): void {
		this.disableAllActions();
		if (this.isAdd() || this.isEdit()) {
			this.saveActionEnabled = true;
		}		
		this.closeActionEnabled = true;
		this.setTitle();
	}

	private disableAllActions() {
		this.closeActionEnabled = false;
		this.saveActionEnabled = false;
	}

	public onHide(event: any): void {
		this.closeWindow(false);
	}

	private prepareNomenclatorValue(): NomenclatorValueModel {
		let nomenclatorValue: NomenclatorValueModel = new NomenclatorValueModel();	
		nomenclatorValue.nomenclatorId = this.inputData.nomenclatorId;
		if (this.isAdd()) {
			this.populateModelFromForm(nomenclatorValue);
		} else if (this.isEdit()) {
			nomenclatorValue.id = this.inputData.nomenclatorValueId;
			this.populateModelFromForm(nomenclatorValue);
		} else {
			throw new Error("Valoarea pentru mode [" + this.mode + "] este invalida/incompatibila acestei metode.");
		}
		return nomenclatorValue;
	}

	private populateModelFromForm(nomenclatorValue: NomenclatorValueModel): void {
		this.nomenclator.attributes.forEach((attribute: NomenclatorAttributeModel) => {
			nomenclatorValue[attribute.key] = this.getAttributeControlValue(attribute);
		});
	}

	private getAttributeControlValue(attribute: NomenclatorAttributeModel): string {
		let attributeControl: FormControl = <FormControl> this.formGroup.get(attribute.key);
		let controlValue: any = attributeControl.value;	
		if (controlValue instanceof Date) {
			return DateUtils.formatForStorage(controlValue);
		} else if (controlValue instanceof ValueOfNomenclatorValueField) {
			let nomenclatorvalue: number = (<ValueOfNomenclatorValueField> controlValue).value;
			if (ObjectUtils.isNotNullOrUndefined(nomenclatorvalue)) {
				return nomenclatorvalue.toString();
			}
		} else if (controlValue instanceof Number) {
			return (<Number> controlValue).toString();
		} else if (ObjectUtils.isBoolean(controlValue)) {
			return "" + controlValue;
		} else if (ObjectUtils.isNotNullOrUndefined(controlValue)) {
			return controlValue;
		}
		return null;
	}

	private isValid(): boolean {
		return this.validate();
	}

	private validate(): boolean {
		this.messagesWindowMessages = [];
		FormUtils.validateAllFormFields(this.formGroup);
		let isValid: boolean = this.formGroup.valid;
		if (!isValid) {
			this.messagesWindowMessages.push(Message.createForError("REQUIRED_FIELDS_NOT_COMPLETED_OR_VALUES_NOT_CORRECT"));
		}
		return isValid;
	}

	private displayValidationMessages(): void {
		this.messagesWindowVisible = true;
	}

	public onMessagesWindowClosed(event: any) {
		this.messagesWindowVisible = false;
	}

	public onSaveAction(event: any): void {		
		if (!this.isValid()) {
			this.displayValidationMessages();
			return;
		}
		let nomenclatorValue: NomenclatorValueModel = this.prepareNomenclatorValue();
		this.lock();
		this.nomenclatorService.existsPersonAndInstitutionInNomPersoane(nomenclatorValue, {
			onSuccess: (exists: boolean): void => {
				if (!exists) {
					this.saveNomValue(nomenclatorValue);
				} else {
					this.nomenclatorService.getNomenclatorValue(Number.parseInt(nomenclatorValue[NomenclatorConstants.PERSOANE_ATTR_KEY_INSTITUTIE]), {
						onSuccess: (institutieNomenclatorValue: NomenclatorValueModel): void => {
							let message = this.translateUtils.translateMessage("PERSON");
							message += " " + nomenclatorValue[NomenclatorConstants.PERSOANE_ATTR_KEY_NUME] + " " + nomenclatorValue[NomenclatorConstants.PERSOANE_ATTR_KEY_PRENUME];
							let denumireInstitutie = institutieNomenclatorValue[NomenclatorConstants.INSITUTII_ATTR_KEY_DENUMIRE_INSTITUTIE];
							message += " " + this.translateUtils.translateMessage("WITH_INSTITUTION") + " " + denumireInstitutie + " " + this.translateUtils.translateMessage("ALREADY_EXISTS");
							
							this.unlock();
							this.messageDisplayer.displayError(message);
						},
						onFailure: (error: AppError) => {
							this.unlock();
							this.messageDisplayer.displayAppError(error);
						}
					});				
				}
			},
			onFailure: (error: AppError): void => {
				this.unlock();
				this.messageDisplayer.displayAppError(error);
			}
		});
		
	}
	private saveNomValue(nomenclatorValue : NomenclatorValueModel) {
		this.nomenclatorService.saveNomenclatorValue(nomenclatorValue, {
			onSuccess: (saveResponse: SaveNomenclatorValueResponseModel): void => {
				if (saveResponse.status === SaveNomenclatorValueResponseModel.STATUS_SUCCESS) {
					this.messageDisplayer.displaySuccess("SAVED_SUCCESSFULLY");
					this.closeWindow(true);
				} else if (saveResponse.status === SaveNomenclatorValueResponseModel.STATUS_ERROR) {
					this.messagesWindowMessages = [];
					saveResponse.messages.forEach((message: string) => {
						this.messagesWindowMessages.push(Message.createForError(message, false));
					});
					this.unlock();
					this.displayValidationMessages();
				} else {
					throw new Error("Status necunoscut dupa salvare " + saveResponse.status);
				}
			},
			onFailure: (error: AppError): void => {
				this.unlock();
				this.messageDisplayer.displayAppError(error);
			}
		});
	}

	public onCloseAction(event: any): void {
		this.closeWindow(false);
	}

	private sortAttributes(attributes: NomenclatorAttributeModel[]): void {
		attributes.sort((md1: NomenclatorAttributeModel, md2: NomenclatorAttributeModel): number => {
			if (md1.uiOrder < md2.uiOrder) {
				return -1;
			}
			if (md1.uiOrder > md2.uiOrder) {
				return 1;
			}
			return 0;
		});
	}
}

export class NomenclatorDataWindowInputData {

	public nomenclatorId: number;
	public nomenclatorValueId: number;
}

export interface NomenclatorDataWindowEvent {
	dataChanged: boolean;
}