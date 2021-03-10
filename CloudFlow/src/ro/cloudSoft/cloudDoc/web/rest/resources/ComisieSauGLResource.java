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
import ro.cloudSoft.cloudDoc.core.constants.NomenclatorConstants;
import ro.cloudSoft.cloudDoc.core.constants.ParameterConstants;
import ro.cloudSoft.cloudDoc.presentation.client.shared.exceptions.PresentationException;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.arb.MembruReprezentantiComisieSauGLInfoModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.arb.MembruReprezentantiComisieSauGLModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.arb.ReprezentantiComisieSauGLEditBundle;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.arb.ReprezentantiComisieSauGLModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.nomenclator.NomenclatorValueModel;
import ro.cloudSoft.cloudDoc.presentation.server.utils.PresentationExceptionUtils;
import ro.cloudSoft.cloudDoc.services.arb.ComisieSauGLService;
import ro.cloudSoft.cloudDoc.services.nomenclator.NomenclatorService;
import ro.cloudSoft.cloudDoc.services.parameters.ParametersService;
import ro.cloudSoft.cloudDoc.utils.nomenclator.NomenclatorValueUtils;

@Component
@Path("/ComisieSauGL")
@Consumes({MediaType.APPLICATION_JSON})
@Produces({MediaType.APPLICATION_JSON})
public class ComisieSauGLResource extends BaseResource {

	@Autowired
	private ComisieSauGLService comisieSauGLService;
	
	@Autowired
	private NomenclatorService nomenclatorService;
	
	@Autowired
	private ParametersService parametersService;
	
	@POST
	@Path("/getReprezentantiEditBundleByComisieSauGLId/{comisieSauGLId}")
	public ReprezentantiComisieSauGLEditBundle getById(@PathParam("comisieSauGLId") Long comisieSauGLId) throws PresentationException {
		try {
			ReprezentantiComisieSauGLEditBundle bundle = new ReprezentantiComisieSauGLEditBundle();
			
			ReprezentantiComisieSauGLModel reprezentanti = comisieSauGLService.getReprezentantiByComisieSauGLId(comisieSauGLId);
			bundle.setReprezentanti(reprezentanti);
			
			NomenclatorValueModel comisieSauGlNomenclatorValue = nomenclatorService.getNomenclatorValue(comisieSauGLId);
			bundle.setComisieSauGL(comisieSauGlNomenclatorValue);

			Long categorieComisiiGlId = NomenclatorValueUtils.getAttributeValueAsLong(comisieSauGlNomenclatorValue, NomenclatorConstants.NOMENCLATOR_COMISII_GL_ATTRIBUTE_KEY_CATEGORIE);
			NomenclatorValueModel categorieComisiiGlNomenclatorValue = nomenclatorService.getNomenclatorValue(categorieComisiiGlId);
			String denumireCategorie = NomenclatorValueUtils.getAttributeValueAsString(categorieComisiiGlNomenclatorValue, NomenclatorConstants.NOMENCLATOR_CATEGORIE_COMISII_GL_ATTRIBUTE_KEY_CATEGORIE);
			if (denumireCategorie.equals(NomenclatorConstants.NOMENCLATOR_CATEGORIE_COMISII_GL_ATTRIBUTE_CATEGORIE_VALUE_FOR_COMISIE)) {
				bundle.setCategorieComisie(true);
			} else {
				bundle.setCategorieComisie(false);
			}
			List<String> nomenclatorCodes = new ArrayList<String>();
			nomenclatorCodes.add(NomenclatorConstants.PERSOANE_NOMENCLATOR_CODE);
			nomenclatorCodes.add(NomenclatorConstants.INSTITUTII_NOMENCLATOR_CODE);
			nomenclatorCodes.add(NomenclatorConstants.MEMBRI_CD_NOMENCLATOR_CODE);
			
			Map<String, Long> mapNomenclators = nomenclatorService.getNomenclatorIdByCodeMapByNomenclatorCodes(nomenclatorCodes);
			bundle.setInstitutiiNomenclatorId(mapNomenclators.get(NomenclatorConstants.INSTITUTII_NOMENCLATOR_CODE));
			bundle.setMembriCDNomenclatorId(mapNomenclators.get(NomenclatorConstants.MEMBRI_CD_NOMENCLATOR_CODE));
			bundle.setPersoaneNomenclatorId(mapNomenclators.get(NomenclatorConstants.PERSOANE_NOMENCLATOR_CODE));
			
			Integer nrAniValabilitateMandatPresedinteVicepresedinte = parametersService.getParamaterValueAsIntegerByParameterName(ParameterConstants.PARAM_NAME_COMISIE_GL_NR_ANI_VALABILITATE_MANDAT_PRESEDINTE_VICEPRESEDINTE);
			Integer nrAniValabilitateMandatMembruCdCoordonator = parametersService.getParamaterValueAsIntegerByParameterName(ParameterConstants.PARAM_NAME_COMISIE_GL_NR_ANI_VALABILITATE_MANDAT_MEMBRU_CD_COORDONATOR);
			
			bundle.setNrAniValabilitateMandatPresedinteVicepresedinte(nrAniValabilitateMandatPresedinteVicepresedinte);
			bundle.setNrAniValabilitateMandatMembruCdCoordonator(nrAniValabilitateMandatMembruCdCoordonator);
			
			return bundle;
			
		} catch (AppException appException) {
			throw PresentationExceptionUtils.getPresentationException(appException);
		}
	}
	
	@POST	
	@Path("/saveReprezentanti")
	public void saveReprezentanti(ReprezentantiComisieSauGLModel model) throws PresentationException {
		try {
			comisieSauGLService.saveReprezentanti(model);
		} catch (AppException appException) {
			throw PresentationExceptionUtils.getPresentationException(appException);
		}
	}
	
	@POST	
	@Path("/getAllInstitutiiOfMembriiComisieSauGL/{comisieSauGLId}")
	public List<NomenclatorValueModel> getAllInstitutiiOfMembriiComisieSauGL(@PathParam("comisieSauGLId") Long comisieSauGLId) throws PresentationException {
		try {
			return comisieSauGLService.getAllInstitutiiOfMembriiComisieSauGL(comisieSauGLId);
		} catch (AppException appException) {
			throw PresentationExceptionUtils.getPresentationException(appException);
		}
	}
	
	@POST	
	@Path("/getMembriiReprezentantiComisieSauGLByInstitutie/{comisieSauGLId}/{institutieId}")
	public List<MembruReprezentantiComisieSauGLInfoModel> getMembriiReprezentantiComisieSauGLByInstitutie(@PathParam("comisieSauGLId") Long comisieSauGLId, @PathParam("institutieId") Long institutieId) throws PresentationException {
		try {
			return comisieSauGLService.getMembriiReprezentantiComisieSauGLByInstitutie(comisieSauGLId, institutieId);
		} catch (AppException appException) {
			throw PresentationExceptionUtils.getPresentationException(appException);
		}
	}
}
