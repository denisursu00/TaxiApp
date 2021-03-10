package ro.cloudSoft.cloudDoc.core.constants;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.lang.StringUtils;

import ro.cloudSoft.cloudDoc.config.environment.AppEnvironmentConfig;

import com.google.common.collect.Maps;

/**
 * 
 */
public class CereriDeConcediuConstantsFactory {

	private static final String PARAMETERS_FILE_PACKAGE_PATH_WITHOUT_APP_ENVIRONMENT_SUFFIX = "/conf/cereriDeConcediuParameters.properties";
	
	private static String getProperty(Properties properties, String propertyNameWithPlaceHolder, String variant) {
		String propertyName = StringUtils.replace(propertyNameWithPlaceHolder, "{variant}", variant);
		return properties.getProperty(propertyName);
	}
	
	private static Long getLongProperty(Properties properties, String propertyNameWithPlaceHolder, String variant) {
		String propertyAsString = getProperty(properties, propertyNameWithPlaceHolder, variant);
		Long propertyAsLong = Long.valueOf(propertyAsString);
		return propertyAsLong;
	}
	
	public static CereriDeConcediuConstants create() throws IOException {
		
		String parametersFilePackagePath = AppEnvironmentConfig.getFilePackagePathWithSuffix(PARAMETERS_FILE_PACKAGE_PATH_WITHOUT_APP_ENVIRONMENT_SUFFIX);
		InputStream parametersFileAsStream = CereriDeConcediuConstantsFactory.class.getResourceAsStream(parametersFilePackagePath);
		
		Properties parameters = new Properties();
		parameters.load(parametersFileAsStream);
		
		Map<Long, CerereDeConcediuConstants> constantsByDocumentTypeId = Maps.newHashMap();
		CereriDeConcediuConstants constants = new CereriDeConcediuConstants(constantsByDocumentTypeId);
		
		String variantsJoined = parameters.getProperty("cereri_de_concediu.variants");
		if (StringUtils.isBlank(variantsJoined)) {
			return constants;
		}
		
		String[] variants = StringUtils.split(variantsJoined, ',');
		for (String variant : variants) {
			
			Long documentTypeId = getLongProperty(parameters, "cereri_de_concediu.{variant}.document_type.id", variant);
			
			Long solicitantMetadataId = getLongProperty(parameters, "cereri_de_concediu.{variant}.document_type.metadata.solicitant.id", variant);
			Long inlocuitorMetadataId = getLongProperty(parameters, "cereri_de_concediu.{variant}.document_type.metadata.inlocuitor.id", variant);
			Long dataInceputMetadataId = getLongProperty(parameters, "cereri_de_concediu.{variant}.document_type.metadata.data_inceput.id", variant);
			Long dataSfarsitMetadataId = getLongProperty(parameters, "cereri_de_concediu.{variant}.document_type.metadata.data_sfarsit.id", variant);
			Long anulataMetadataId = getLongProperty(parameters, "cereri_de_concediu.{variant}.document_type.metadata.anulata.id", variant);
			String anulataMetadataPositiveValue = getProperty(parameters, "cereri_de_concediu.{variant}.document_type.metadata.anulata.value.positive", variant);
			String anulataMetadataNegativeValue = getProperty(parameters, "cereri_de_concediu.{variant}.document_type.metadata.anulata.value.negative", variant);
			Long aprobareDecisivaMetadataId = getLongProperty(parameters, "cereri_de_concediu.{variant}.document_type.metadata.aprobare_decisiva.id", variant);
			String aprobareDecisivaMetadataPositiveValue = getProperty(parameters, "cereri_de_concediu.{variant}.document_type.metadata.aprobare_decisiva.value.positive", variant);
			String aprobareDecisivaMetadataNegativeValue = getProperty(parameters, "cereri_de_concediu.{variant}.document_type.metadata.aprobare_decisiva.value.negative", variant);
			
			String anulareTransitionNamePrefix = getProperty(parameters, "cereri_de_concediu.{variant}.workflow.transitions.anulare.name.prefix", variant);
			
			CerereDeConcediuConstants constantsForVariant = new CerereDeConcediuConstants();
			
			constantsForVariant.setSolicitantMetadataId(solicitantMetadataId);
			constantsForVariant.setInlocuitorMetadataId(inlocuitorMetadataId);
			constantsForVariant.setDataInceputMetadataId(dataInceputMetadataId);
			constantsForVariant.setDataSfarsitMetadataId(dataSfarsitMetadataId);
			constantsForVariant.setAnulataMetadataId(anulataMetadataId);
			constantsForVariant.setAnulataMetadataPositiveValue(anulataMetadataPositiveValue);
			constantsForVariant.setAnulataMetadataNegativeValue(anulataMetadataNegativeValue);
			constantsForVariant.setAprobareDecisivaMetadataId(aprobareDecisivaMetadataId);
			constantsForVariant.setAprobareDecisivaMetadataPositiveValue(aprobareDecisivaMetadataPositiveValue);
			constantsForVariant.setAprobareDecisivaMetadataNegativeValue(aprobareDecisivaMetadataNegativeValue);
			
			constantsForVariant.setAnulareTransitionNamePrefix(anulareTransitionNamePrefix);
			
			constantsByDocumentTypeId.put(documentTypeId, constantsForVariant);
		}
		
		return constants;
	}
}