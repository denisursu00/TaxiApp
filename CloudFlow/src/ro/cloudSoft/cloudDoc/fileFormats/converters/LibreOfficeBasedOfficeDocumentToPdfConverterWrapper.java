package ro.cloudSoft.cloudDoc.fileFormats.converters;

import java.io.InputStream;
import java.util.Set;

import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;

import ro.cloudSoft.cloudDoc.utils.log.LogHelper;
import ro.cloudSoft.common.utils.DependencyInjectionUtils;
import ro.sdc.officeToPdf.LibreOfficeBasedOfficeDocumentToPdfConverter;
import ro.sdc.officeToPdf.OfficeDocumentToPdfConverter;

/**
 * Wrapper pentru convertorul adevarat
 * Este necesar pentru ca acesta sa poata fi activat / dezactivat (daca LibreOffice nu este instalat, de exemplu).
 */
public class LibreOfficeBasedOfficeDocumentToPdfConverterWrapper implements OfficeDocumentToPdfConverter, InitializingBean, DisposableBean {

	private static final LogHelper LOGGER = LogHelper.getInstance(LibreOfficeBasedOfficeDocumentToPdfConverterWrapper.class);
	
	private boolean enabled;
	private int libreOfficeServerPort;
	
	private LibreOfficeBasedOfficeDocumentToPdfConverter converter;
	
	@Override
	public void afterPropertiesSet() throws Exception {
		
		DependencyInjectionUtils.checkRequiredDependencies(
			enabled,
			libreOfficeServerPort
		);
		
		if (enabled) {
			converter = new LibreOfficeBasedOfficeDocumentToPdfConverter(libreOfficeServerPort);
			converter.init();
		} else {
			LOGGER.warn("ATENTIE: Convertorul este dezactivat, deci nu va putea fi folosit.",
				"initializarea convertorului din Office in PDF");
		}
	}
	
	@Override
	public void destroy() throws Exception {
		if (enabled) {
			converter.destroy();
		}
	}
	
	private void checkEnabled() {
		if (!enabled) {
			throw new UnsupportedOperationException("Convertorul este dezactivat.");
		}
	}

	@Override
	public Set<String> getExtensionsForSupportedFileTypesInLowerCase() {
		checkEnabled();
		return converter.getExtensionsForSupportedFileTypesInLowerCase();
	}
	
	@Override
	public String convertToPdf(String sourceOfficeDocumentFilePath) {
		checkEnabled();
		return converter.convertToPdf(sourceOfficeDocumentFilePath);
	}
	
	@Override
	public InputStream convertToPdf(String officeDocumentExtension, InputStream officeDocumentAsStream) {
		checkEnabled();
		return converter.convertToPdf(officeDocumentExtension, officeDocumentAsStream);
	}
	
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}
	public void setLibreOfficeServerPort(int libreOfficeServerPort) {
		this.libreOfficeServerPort = libreOfficeServerPort;
	}
}