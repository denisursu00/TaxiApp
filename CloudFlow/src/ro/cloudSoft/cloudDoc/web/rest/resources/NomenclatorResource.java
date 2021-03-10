package ro.cloudSoft.cloudDoc.web.rest.resources;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import ro.cloudSoft.cloudDoc.core.AppException;
import ro.cloudSoft.cloudDoc.presentation.client.shared.exceptions.PresentationException;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.content.NomenclatorValueAsViewSearchRequestModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.content.NomenclatorValueSearchRequestModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.nomenclator.CustomNomenclatorSelectionFiltersRequestModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.nomenclator.CustomNomenclatorSelectionFiltersResponseModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.nomenclator.GetNomenclatorValuesRequestModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.nomenclator.JoinedNomenclatorUiAttributesValueModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.nomenclator.NomenclatorAttributeModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.nomenclator.NomenclatorRunExpressionRequestModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.nomenclator.NomenclatorRunExpressionResponseModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.nomenclator.NomenclatorModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.nomenclator.NomenclatorValueModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.nomenclator.NomenclatorValueViewModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.nomenclator.SaveNomenclatorValueResponseModel;
import ro.cloudSoft.cloudDoc.presentation.server.utils.PresentationExceptionUtils;
import ro.cloudSoft.cloudDoc.services.nomenclator.NomenclatorService;
import ro.cloudSoft.common.utils.PagingList;

@Component
@Path("/Nomenclator")
@Consumes({MediaType.APPLICATION_JSON})
@Produces({MediaType.APPLICATION_JSON})
public class NomenclatorResource extends BaseResource {
	
	@Autowired
	private NomenclatorService nomenclatorService;
	
	@POST
	@Path("/saveNomenclator")
	public void saveNomenclator(NomenclatorModel nomenclatorModel) throws PresentationException {
		try {
			nomenclatorService.saveNomenclator(nomenclatorModel);
		} catch (AppException e) {
			throw PresentationExceptionUtils.getPresentationException(e);
		}
	}
	
	@POST
	@Path("/getNomenclator/{id}")
	public NomenclatorModel getNomenclator(@PathParam("id") Long id) {
		return nomenclatorService.getNomenclator(id);
	}
	
	@POST
	@Path("/getNomenclatorByCode/{code}")
	public NomenclatorModel getNomenclatorByCode(@PathParam("code") String code) {
		return nomenclatorService.getNomenclatorByCode(code);
	}
	
	@POST
	@Path("/getAllNomenclators")
	public List<NomenclatorModel> getAllNomenclators() {
		return nomenclatorService.getAllNomenclators();
	}
	
	@POST
	@Path("/getVisibleNomenclators")
	public List<NomenclatorModel> getVisibleNomenclators() {
		return nomenclatorService.getVisibleNomenclators();
	}
	
	
	@POST
	@Path("/getAvailableNomenclatorsForProcessingValuesFromUI")
	public List<NomenclatorModel> getAvailableNomenclatorsForProcessingValuesFromUI() {
		return nomenclatorService.getAvailableNomenclatorsForProcessingValuesFromUI();
	}
	
	@POST
	@Path("/getAvailableNomenclatorsForProcessingStructureFromUI")
	public List<NomenclatorModel> getAvailableNomenclatorsForProcessingStructureFromUI() {
		return nomenclatorService.getAvailableNomenclatorsForProcessingStructureFromUI();
	}
	
	
	@POST
	@Path("/saveNomenclatorValue")
	public SaveNomenclatorValueResponseModel saveNomenclatorValue(NomenclatorValueModel nomenclatorValueModel) {
		return nomenclatorService.saveNomenclatorValue(nomenclatorValueModel);
	}
	
	@POST
	@Path("/getNomenclatorValue/{id}")
	public NomenclatorValueModel getNomenclatorValue(@PathParam("id") Long id) {
		return nomenclatorService.getNomenclatorValue(id);
	}
	
	@POST
	@Path("/searchNomenclatorValues")
	public PagingList<NomenclatorValueModel> searchNomenclatorValues(NomenclatorValueSearchRequestModel requestModel) {
		return nomenclatorService.searchNomenclatorValues(requestModel);
	}
	
	@POST
	@Path("/saveNomenclatorAttribute")
	public void saveNomenclatorAttribute(NomenclatorAttributeModel nomenclatorAttributeModel) {
		nomenclatorService.saveNomenclatorAttribute(nomenclatorAttributeModel);
	}
	
	@POST
	@Path("/getNomenclatorAttribute/{id}")
	public NomenclatorAttributeModel getNomenclatorAttribute(@PathParam("id") Long id) {
		return nomenclatorService.getNomenclatorAttribute(id);
	}
	
	@POST
	@Path("/getNomenclatorAttributesByNomenclatorId/{nomenclatorId}")
	public List<NomenclatorAttributeModel> getNomenclatorAttributesByNomenclatorId(@PathParam("nomenclatorId") Long nomenclatorId) {
		return nomenclatorService.getNomenclatorAttributesByNomenclatorId(nomenclatorId);
	}
	
	@POST
	@Path("/getNomenclatorAttributesByNomenclatorCode/{nomenclatorCode}")
	public List<NomenclatorAttributeModel> getNomenclatorAttributesByNomenclatorCode(@PathParam("nomenclatorCode") String nomenclatorCode) {
		return nomenclatorService.getNomenclatorAttributesByNomenclatorCode(nomenclatorCode);
	}
	
	@POST
	@Path("/searchNomenclatorValuesAsView")
	public PagingList<NomenclatorValueViewModel> searchNomenclatorValuesAsView(NomenclatorValueAsViewSearchRequestModel requestModel) throws PresentationException {
		try {
			return nomenclatorService.searchNomenclatorValueViewModels(requestModel);
		} catch (AppException ae) {
			throw PresentationExceptionUtils.getPresentationException(ae);
		}
	}

	@POST
	@Path("/saveNomenclatorAttributes")
	public void saveNomenclatorAttributes(List<NomenclatorAttributeModel> nomenclatorAttributeModels) {
		nomenclatorService.saveNomenclatorAttributes(nomenclatorAttributeModels);
	}
	
	@POST
	@Path("/getListOfConcatenatedUiAttributesByNomenclatorId/{nomenclatorId}")
	public List<JoinedNomenclatorUiAttributesValueModel> getListOfConcatenatedUiAttributesByNomenclatorId(@PathParam("nomenclatorId") Long nomenclatorId) throws PresentationException {
		try {
			return nomenclatorService.getJoinedNomenclatorUiAtributesValueByNomenclatorId(nomenclatorId);
		} catch (AppException ae) {
			throw PresentationExceptionUtils.getPresentationException(ae);
		}
	}
	
	@POST
	@Path("/getListOfConcatenatedUiAttributesByNomenclatorCode/{nomenclatorCode}")
	public List<JoinedNomenclatorUiAttributesValueModel> getListOfConcatenatedUiAttributesByNomenclatorCode(@PathParam("nomenclatorCode") String nomenclatorCode) throws PresentationException {
		try {
			return nomenclatorService.getJoinedNomenclatorUiAtributesValueByNomenclatorCode(nomenclatorCode);
		} catch (AppException ae) {
			throw PresentationExceptionUtils.getPresentationException(ae);
		}
	}
	
	@POST
	@Path("/getUiAttributeValues")
	public Map<Long, String> getUiAttributeValues(List<Long> nomenclatorValueIds) {
		return nomenclatorService.getUiAttributeValues(nomenclatorValueIds);
	}
	
	@POST
	@Path("/nomenclatorHasValue/{nomenclatorId}")
	public boolean nomenclatorHasValue(@PathParam("nomenclatorId") Long nomenclatorId) {
		return nomenclatorService.nomenclatorHasValue(nomenclatorId);
	}
	
	@POST
	@Path("/nomenclatorHasValueByNomenclatorCode/{nomenclatorCode}")
	public boolean nomenclatorHasValue(@PathParam("nomenclatorCode") String nomenclatorCode) {
		return nomenclatorService.nomenclatorHasValueByNomenclatorCode(nomenclatorCode);
	}
	
	@POST
	@Path("/deleteNomenclator/{id}")
	public void deleteNomenclator(@PathParam("id") Long id) {
		nomenclatorService.deleteNomenclator(id);
	}
	
	@POST
	@Path("/deleteNomenclatorValue/{valueId}")
	public void deleteNomenclatorValue(@PathParam("valueId") Long valueId) {
		nomenclatorService.deleteNomenclatorValue(valueId);
	}
	
	@POST
	@Path("/getNomenclatorIdByCodeAsMap")
	public Map<String, Long> getNomenclatorIdByCodeMapByNomenclatorCodes(List<String> nomenclatorCodes) {
		return nomenclatorService.getNomenclatorIdByCodeMapByNomenclatorCodes(nomenclatorCodes);
	}
	
	@POST
	@Path("/getNomenclatorValuesByNomenclatorId/{nomenclatorId}")
	public List<NomenclatorValueModel> getNomenclatorValuesByNomenclatorId(@PathParam("nomenclatorId") Long nomenclatorId) {
		return nomenclatorService.getNomenclatorValuesByNomenclatorId(nomenclatorId);
	}
	
	@POST
	@Path("/getNomenclatorValuesByNomenclatorCode/{nomenclatorCode}")
	public List<NomenclatorValueModel> getNomenclatorValuesByNomenclatorCode(@PathParam("nomenclatorCode") String nomenclatorCode) {
		return nomenclatorService.getNomenclatorValuesByNomenclatorCode(nomenclatorCode);
	}
	
	@POST
	@Path("/getNomenclatorValues")
	public List<NomenclatorValueModel> getNomenclatorValues(GetNomenclatorValuesRequestModel requestModel) throws PresentationException {
		try {
			return nomenclatorService.getNomenclatorValues(requestModel);
		} catch (AppException ae) {
			throw PresentationExceptionUtils.getPresentationException(ae);
		}

	}
	
	@POST
	@Path("/getNomenclatorIdByCode/{nomenclatorCode}")
	public Long getNomenclatorIdByCode(@PathParam("nomenclatorCode") String nomenclatorCode) {
		List<String> codes = new ArrayList<>();
		codes.add(nomenclatorCode);
		Map<String, Long> idByCodeMap = nomenclatorService.getNomenclatorIdByCodeMapByNomenclatorCodes(codes);
		return idByCodeMap.get(nomenclatorCode);
	}	

	@POST
	@Path("/getYearsFromNomenclatorValuesByNomenclatorCodeAndAttributeKey/{nomenclatorCode}/{attributeKey}")
	public List<Integer> getYearsFromNomenclatorValuesByNomenclatorCodeAndAttributeKey(@PathParam("nomenclatorCode") String nomenclatorCode, @PathParam("attributeKey") String attributeKey) {
		return nomenclatorService.getYearsFromNomenclatorValuesByNomenclatorCodeAndAttributeKey(nomenclatorCode, attributeKey);
	}
	
	@POST
	@Path("/getCustomNomenclatorSelectionFilters")
	public CustomNomenclatorSelectionFiltersResponseModel getCustomNomenclatorSelectionFilters(CustomNomenclatorSelectionFiltersRequestModel requestModel) throws PresentationException {
		try {
			return nomenclatorService.getCustomNomenclatorSelectionFilters(getSecurity(), requestModel);
		} catch (AppException ae) {
			throw PresentationExceptionUtils.getPresentationException(ae);
		}
	}
	
	@POST
	@Path("/runExpressions")
	public NomenclatorRunExpressionResponseModel runExpressions(NomenclatorRunExpressionRequestModel requestModel) throws PresentationException {
		try {
			return nomenclatorService.runExpressions(getSecurity(), requestModel);
		} catch (AppException ae) {
			throw PresentationExceptionUtils.getPresentationException(ae);
		}
	}
	
	@POST
	@Path("/getNomenclatorUIValuesAsFilterForNomenclatorAttributeThatUseIt/{nomenclatorId}/{nomenclatorAttributeIdThatUseIt}")
	public List<JoinedNomenclatorUiAttributesValueModel> getNomenclatorUIValuesAsFilterForNomenclatorAttributeThatUseIt(@PathParam("nomenclatorId") Long nomenclatorId, @PathParam("nomenclatorAttributeIdThatUseIt") Long nomenclatorAttributeIdThatUseIt) throws PresentationException {
		try {
			return nomenclatorService.getNomenclatorUIValuesAsFilterForNomenclatorAttributeThatUseIt(nomenclatorId, nomenclatorAttributeIdThatUseIt);
		} catch (AppException ae) {
			throw PresentationExceptionUtils.getPresentationException(ae);
		}
	}
	
	@POST
	@Path("/existsPersonAndInstitutionInNomPersoane")
	public boolean existsPersonAndInstitutionInNomPersoane(NomenclatorValueModel nomenclatorValueModel) throws PresentationException {
		return nomenclatorService.existsPersonAndInstitutionInNomPersoane(nomenclatorValueModel);
	}
	
}
