package ro.cloudSoft.cloudDoc.plugins;

import java.util.Date;

import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.commons.lang3.StringUtils;

import ro.cloudSoft.cloudDoc.core.constants.FormatConstants;

public class JexlDateFunctions {

	public String currentDateAsMetadataFormat() {			
		return DateFormatUtils.format(new Date(), FormatConstants.METADATA_DATE_FOR_SAVING);
	}
	
	public String dateAsMetadataFormatFromMetadataDateTime(String metadataDateTime) {	
		// TODO - 0016990: JexlDateFunctions.dateAsMetadataFormatFromMetadataDateTime de extras date din format
		return StringUtils.substringBefore(metadataDateTime, " ");
	}
	
	public String currentDateAsNomenclatorAttributeFormat() {			
		return DateFormatUtils.format(new Date(), FormatConstants.NOMENCLATOR_ATTRIBUTE_DATE_FOR_SAVING);
	}	
}
