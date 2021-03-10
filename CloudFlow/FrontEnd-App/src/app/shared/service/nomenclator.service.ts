import { Injectable } from "@angular/core";
import { ApiCaller } from "../api-caller";
import { NomenclatorModel } from "./../model/nomenclator/nomenclator.model";
import { NomenclatorAttributeModel} from "./../model/nomenclator/nomenclator-attribute.model";
import { NomenclatorValueModel } from "./../model/nomenclator/nomenclator-value.model";
import { SaveNomenclatorValueResponseModel } from "./../model/nomenclator/save-nomenclator-value-response.model";
import { AppError } from "./../model/app-error";
import { AsyncCallback } from "./../async-callback";
import { ApiPathConstants } from "./../constants/api-path.constants";
import { ApiPathUtils } from "./../utils";
import { NomenclatorValueAsViewSearchRequestModel, NomenclatorValueViewModel, JoinedNomenclatorUiAttributesValueModel, GetNomenclatorValuesRequestModel } from "../model/nomenclator";
import { PagingList } from "../model";
import { CustomNomenclatorSelectionFiltersRequestModel, CustomNomenclatorSelectionFiltersResponseModel } from "./../model/nomenclator";
import { NomenclatorRunExpressionsRequestModel, NomenclatorRunExpressionsResponseModel } from "./../model";

@Injectable()
export class NomenclatorService {

	private apiCaller: ApiCaller;

	constructor(apiCaller: ApiCaller) {
		this.apiCaller = apiCaller;
	}

	public getAllNomenclators(callback: AsyncCallback<NomenclatorModel[], AppError>): void {
		this.apiCaller.call(ApiPathConstants.GET_ALL_NOMENCLATORS, null, NomenclatorModel, callback);
	}
	
	public getVisibleNomenclators(callback: AsyncCallback<NomenclatorModel[], AppError>): void {
		this.apiCaller.call(ApiPathConstants.GET_VISIBLE_NOMENCLATORS, null, NomenclatorModel, callback);
	}

	public getAvailableNomenclatorsForProcessingValuesFromUI(callback: AsyncCallback<NomenclatorModel[], AppError>): void {
		this.apiCaller.call(ApiPathConstants.GET_AVAILABLE_NOMENCLATORS_FOR_PROCESSING_VALUES_FROM_UI, null, NomenclatorModel, callback);
	}

	public getAvailableNomenclatorsForProcessingStructureFromUI(callback: AsyncCallback<NomenclatorModel[], AppError>): void {
		this.apiCaller.call(ApiPathConstants.GET_AVAILABLE_NOMENCLATORS_FOR_PROCESSING_STRUCTURE_FROM_UI, null, NomenclatorModel, callback);
	}

	public getNomenclator(nomenclatoId: number, callback: AsyncCallback<NomenclatorModel, AppError>): void {
		let relativePath: string = ApiPathUtils.appendParametersToPath(ApiPathConstants.GET_NOMENCLATOR, "" + nomenclatoId);
		this.apiCaller.call(relativePath, null, NomenclatorModel, callback);
	}

	public getNomenclatorByCode(nomenclatorCode: string, callback: AsyncCallback<NomenclatorModel, AppError>): void {
		let relativePath: string = ApiPathUtils.appendParametersToPath(ApiPathConstants.GET_NOMENCLATOR_BY_CODE, nomenclatorCode);
		this.apiCaller.call(relativePath, null, NomenclatorModel, callback);
	}

	public getNomenclatorAttributesByNomenclatorId(nomenclatoId: number, callback: AsyncCallback<NomenclatorAttributeModel[], AppError>): void {
		let relativePath: string = ApiPathUtils.appendParametersToPath(ApiPathConstants.GET_NOMENCLATOR_ATTRIBUTES_BY_NOMENCLATOR_ID, "" + nomenclatoId);
		this.apiCaller.call(relativePath, null, NomenclatorAttributeModel, callback);
	}

	public getNomenclatorAttributesByNomenclatorCode(nomenclatorCode: string, callback: AsyncCallback<NomenclatorAttributeModel[], AppError>): void {
		let relativePath: string = ApiPathUtils.appendParametersToPath(ApiPathConstants.GET_NOMENCLATOR_ATTRIBUTES_BY_NOMENCLATOR_CODE, nomenclatorCode);
		this.apiCaller.call(relativePath, null, NomenclatorAttributeModel, callback);
	}
	
	public getUiAttributeValues(nomenclatorValueIds: number[], callback: AsyncCallback<object, AppError>): void {
		this.apiCaller.call(ApiPathConstants.GET_UI_ATTRIBUTE_VALUES, nomenclatorValueIds, Object, callback);
	}
	
	public searchNomenclatorValuesAsView(requestModel: NomenclatorValueAsViewSearchRequestModel, callback: AsyncCallback<PagingList<NomenclatorValueViewModel>, AppError>) {
		this.apiCaller.call(ApiPathConstants.SEARCH_NOMENCLATOR_VALUES_AS_VIEW, requestModel, PagingList, callback);
	}

	public getListOfConcatenatedUiAttributesByNomenclatorId(nomenclatoId: number, callback: AsyncCallback<JoinedNomenclatorUiAttributesValueModel[], AppError>): void {
		let relativePath: string = ApiPathUtils.appendParametersToPath(ApiPathConstants.GET_LIST_OF_CONCATENATED_UI_ATTRIBUTES_BY_NOMENCLATOR_ID, "" + nomenclatoId);
		this.apiCaller.call(relativePath, null, JoinedNomenclatorUiAttributesValueModel, callback);
	}

	public getListOfConcatenatedUiAttributesByNomenclatorCode(nomenclatorCode: string, callback: AsyncCallback<JoinedNomenclatorUiAttributesValueModel[], AppError>): void {
		let relativePath: string = ApiPathUtils.appendParametersToPath(ApiPathConstants.GET_LIST_OF_CONCATENATED_UI_ATTRIBUTES_BY_NOMENCLATOR_CODE, nomenclatorCode);
		this.apiCaller.call(relativePath, null, JoinedNomenclatorUiAttributesValueModel, callback);
	}

	public saveNomenclator(nomenclatorModel, callback: AsyncCallback<null, AppError>): void {
		this.apiCaller.call(ApiPathConstants.SAVE_NOMENCLATOR, nomenclatorModel, null, callback);
	}

	public existsPersonAndInstitutionInNomPersoane(nomenclatorModel, callback: AsyncCallback<boolean, AppError>): void {
		this.apiCaller.call(ApiPathConstants.EXISTS_PERSON_AND_INSTITUTION_IN_NOM_PERSOANE, nomenclatorModel, Boolean, callback);
	}

	public nomenclatorHasValue(nomenclatoId: number, callback: AsyncCallback<boolean, AppError>): void {
		let relativePath: string = ApiPathUtils.appendParametersToPath(ApiPathConstants.NOMENCLATOR_HAS_VALUE, "" + nomenclatoId);
		this.apiCaller.call(relativePath, null, Boolean, callback);
	}

	public nomenclatorHasValueByNomenclatorCode(nomenclatorCode: string, callback: AsyncCallback<boolean, AppError>): void {
		let relativePath: string = ApiPathUtils.appendParametersToPath(ApiPathConstants.NOMENCLATOR_HAS_VALUE_BY_NOMENCLATOR_CODE, nomenclatorCode);
		this.apiCaller.call(relativePath, null, Boolean, callback);
	}

	public deleteNomenclator(nomenclatoId: number, callback: AsyncCallback<null, AppError>): void {
		let relativePath: string = ApiPathUtils.appendParametersToPath(ApiPathConstants.DELETE_NOMENCLATOR, "" + nomenclatoId);
		this.apiCaller.call(relativePath, null, null, callback);
	}

	public getNomenclatorValue(nomenclatorValueId: number, callback: AsyncCallback<NomenclatorValueModel, AppError>): void {
		let relativePath: string = ApiPathUtils.appendParametersToPath(ApiPathConstants.GET_NOMENCLATOR_VALUE, "" + nomenclatorValueId);
		this.apiCaller.call(relativePath, null, NomenclatorValueModel, callback);
	}

	public saveNomenclatorValue(nomenclatorValue: NomenclatorValueModel, callback: AsyncCallback<SaveNomenclatorValueResponseModel, AppError>): void {
		this.apiCaller.call(ApiPathConstants.SAVE_NOMENCLATOR_VALUE, nomenclatorValue, SaveNomenclatorValueResponseModel, callback);
	}

	public deleteNomenclatorValue(valueId: number, callback: AsyncCallback<null, AppError>): void {
		let relativePath: string = ApiPathUtils.appendParametersToPath(ApiPathConstants.DELETE_NOMENCLATOR_VALUE, "" + valueId);
		this.apiCaller.call(relativePath, null, null, callback);
	}

	public getNomenclatorIdByCodeAsMap(nomenclatorCodes: string[], callback: AsyncCallback<object, AppError>): void {
		this.apiCaller.call(ApiPathConstants.GET_NOMENCLATOR_ID_BY_CODE_AS_MAP, nomenclatorCodes, Object, callback);
	}

	public getNomenclatorValuesByNomenclatorId(nomenclatoId, callback: AsyncCallback<NomenclatorValueModel[], AppError>): void {
		let relativePath: string = ApiPathUtils.appendParametersToPath(ApiPathConstants.GET_NOMENCLATOR_VALUES_BY_NOMENCLATOR_ID, "" + nomenclatoId);
		this.apiCaller.call(relativePath, null, NomenclatorValueModel, callback);
	}

	public getNomenclatorValuesByNomenclatorCode(nomenclatorCode: string, callback: AsyncCallback<NomenclatorValueModel[], AppError>): void {
		let relativePath: string = ApiPathUtils.appendParametersToPath(ApiPathConstants.GET_NOMENCLATOR_VALUES_BY_NOMENCLATOR_CODE, nomenclatorCode);
		this.apiCaller.call(relativePath, null, NomenclatorValueModel, callback);
	}
	
	public getNomenclatorValues(requestModel: GetNomenclatorValuesRequestModel, callback: AsyncCallback<NomenclatorValueModel[], AppError>) {
		this.apiCaller.call(ApiPathConstants.GET_NOMENCLATOR_VALUES_BY_REQUEST_MODEL, requestModel, NomenclatorValueModel, callback);
	}

	public getYearsFromNomenclatorValuesByNomenclatorCode(nomenclatorCode: string, attributeKey: string, callback: AsyncCallback<number[], AppError>): void {
		let relativePath: string = ApiPathUtils.appendParametersToPath(ApiPathConstants.GET_YEARS_FROM_NOMENCLATOR_VALUES_BY_NOMENCLATOR_CODE_AND_ATTRIBUTE_KEY, nomenclatorCode, attributeKey);
		this.apiCaller.call(relativePath, null, Number, callback);
	}

	public getCustomNomenclatorSelectionFilters(requestModel: CustomNomenclatorSelectionFiltersRequestModel, callback: AsyncCallback<CustomNomenclatorSelectionFiltersResponseModel, AppError>): void {
		this.apiCaller.call(ApiPathConstants.GET_CUSTOM_NOMENCLATOR_SELECTION_FILTERS, requestModel, CustomNomenclatorSelectionFiltersResponseModel, callback);
	}

	public runExpressions(requestModel: NomenclatorRunExpressionsRequestModel, callback: AsyncCallback<NomenclatorRunExpressionsResponseModel, AppError>): void {
		this.apiCaller.call(ApiPathConstants.NOMENCLATOR_RUN_EXPRESSIONS, requestModel, NomenclatorRunExpressionsResponseModel, callback);
	}

	public getNomenclatorUIValuesAsFilterForNomenclatorAttributeThatUseIt(nomenclatorId: number, nomenclatorAttributeIdThaUseIt: number, callback: AsyncCallback<JoinedNomenclatorUiAttributesValueModel[], AppError>): void {
		let relativePath: string = ApiPathUtils.appendParametersToPath(ApiPathConstants.GET_NOMENCLATOR_UI_VALUES_AS_FILTER_FOR_NOMENCLATOR_ATTRIBUTE_THAT_USE_IT, String(nomenclatorId), String(nomenclatorAttributeIdThaUseIt));
		this.apiCaller.call(relativePath, null, JoinedNomenclatorUiAttributesValueModel, callback);
	}
}