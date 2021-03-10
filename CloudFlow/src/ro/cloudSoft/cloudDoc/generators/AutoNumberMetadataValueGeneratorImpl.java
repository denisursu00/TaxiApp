package ro.cloudSoft.cloudDoc.generators;

import static ro.cloudSoft.cloudDoc.core.AppExceptionCodes.autoNumberMetadataValueGenerator_error_couldNotProperlyGenerateValues;
import static ro.cloudSoft.cloudDoc.core.AppExceptionCodes.autoNumberMetadataValueGenerator_error_maximumValueReached;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Collection;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.InitializingBean;

import ro.cloudSoft.cloudDoc.core.AppException;
import ro.cloudSoft.cloudDoc.dao.content.AutoNumberMetadataSequenceValueDao;
import ro.cloudSoft.cloudDoc.dao.content.DocumentTypeDao;
import ro.cloudSoft.cloudDoc.domain.content.AutoNumberMetadataDefinition;
import ro.cloudSoft.cloudDoc.domain.content.Document;
import ro.cloudSoft.cloudDoc.domain.content.MetadataInstance;
import ro.cloudSoft.cloudDoc.utils.log.LogHelper;
import ro.cloudSoft.common.utils.DependencyInjectionUtils;

/**
 * 
 */
public class AutoNumberMetadataValueGeneratorImpl implements AutoNumberMetadataValueGenerator, InitializingBean {
	
	private static final LogHelper LOGGER = LogHelper.getInstance(AutoNumberMetadataValueGeneratorImpl.class);
	
	private int maximumNumberOfTries;
	
	private DocumentTypeDao documentTypeDao;
	private AutoNumberMetadataSequenceValueDao autoNumberMetadataSequenceValueDao;
	
	@Override
	public void afterPropertiesSet() throws Exception {		
		DependencyInjectionUtils.checkRequiredDependencies(
			
			maximumNumberOfTries,
			
			documentTypeDao,
			autoNumberMetadataSequenceValueDao
		);
	}
	
	private int getNextSequenceValue(Long autoNumberMetadataDefinitionId) throws AppException {
		
		int numberOfTries = 0;
		
		do {			
			numberOfTries++;
			
			int oldSequenceValue = autoNumberMetadataSequenceValueDao.getExistingSequenceValue(autoNumberMetadataDefinitionId);
			int newSequenceValue = (oldSequenceValue + 1);
			
			boolean properlyUpdated = autoNumberMetadataSequenceValueDao.updateSequenceValue(autoNumberMetadataDefinitionId, oldSequenceValue, newSequenceValue);
			if (properlyUpdated) {
				return newSequenceValue;
			}
		} while (numberOfTries < maximumNumberOfTries);
		
		String logMessage = "S-a incercat generarea secventei de mai multe ori, insa valorile nu sunt consistente. " +
			"Este probabil ca ruleaza prea multe thread-uri si mecanismul nu este adaptat pt. acest caz.";
		LOGGER.error(logMessage, "getNextSequenceValue");
		throw new AppException(autoNumberMetadataValueGenerator_error_couldNotProperlyGenerateValues);
	}
	
	private String formatNumber(int number, int numberLength) {
		String decimalFormatPattern = StringUtils.repeat("0", numberLength);
		NumberFormat numberFormatter = new DecimalFormat(decimalFormatPattern);
		return numberFormatter.format(number);
	}
	
	private String generateNewValue(Long autoNumberMetadataDefinitionId) throws AppException {
		
		AutoNumberMetadataDefinition autoNumberMetadataDefinition = (AutoNumberMetadataDefinition) documentTypeDao.getMetadataDefinition(autoNumberMetadataDefinitionId);
		
		int sequenceValue = getNextSequenceValue(autoNumberMetadataDefinitionId);
		
		String prefix = autoNumberMetadataDefinition.getPrefix();
		int numberLength = autoNumberMetadataDefinition.getNumberLength();
		
		if (sequenceValue < 0) {
			throw new IllegalStateException("Valoarea secventei nu poate fi negativa.");
		}
		int sequenceValueDigits = Integer.toString(sequenceValue).length();
		if (sequenceValueDigits > numberLength) {
			String logMessage = "Valoarea secventei pt. metadata de tip auto-number cu ID-ul " +
				"[" + autoNumberMetadataDefinitionId + "] are mai multe cifre decat setarea definitiei permite.";
			LOGGER.error(logMessage, "generateNewValue");
			throw new AppException(autoNumberMetadataValueGenerator_error_maximumValueReached);
		}
		
		String value = (prefix + formatNumber(sequenceValue, numberLength));
		return value;
	}
	
	@Override
	public void populateDocumentWithGeneratedValues(Document document, Collection<Long> definitionIdsForAutoNumberMetadataValuesToGenerate) throws AppException {
		for (Long autoNumberMetadataDefinitionId : definitionIdsForAutoNumberMetadataValuesToGenerate) {
			String value = generateNewValue(autoNumberMetadataDefinitionId);
			MetadataInstance metadataInstance = new MetadataInstance(autoNumberMetadataDefinitionId, value);
			document.getMetadataInstanceList().add(metadataInstance);
		}
	}
	
	public void setMaximumNumberOfTries(int maximumNumberOfTries) {
		this.maximumNumberOfTries = maximumNumberOfTries;
	}
	public void setDocumentTypeDao(DocumentTypeDao documentTypeDao) {
		this.documentTypeDao = documentTypeDao;
	}
	public void setAutoNumberMetadataSequenceValueDao(AutoNumberMetadataSequenceValueDao autoNumberMetadataSequenceValueDao) {
		this.autoNumberMetadataSequenceValueDao = autoNumberMetadataSequenceValueDao;
	}
}