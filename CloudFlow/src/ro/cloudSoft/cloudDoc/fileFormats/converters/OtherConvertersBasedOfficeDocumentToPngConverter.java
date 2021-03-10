package ro.cloudSoft.cloudDoc.fileFormats.converters;

import java.io.InputStream;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.InitializingBean;

import ro.cloudSoft.common.utils.DependencyInjectionUtils;
import ro.sdc.officeToPdf.OfficeDocumentToPdfConverter;

public class OtherConvertersBasedOfficeDocumentToPngConverter implements OfficeDocumentToPngConverter, InitializingBean {
	
	private OfficeDocumentToPdfConverter officeDocumentToPdfConverter;
	private PdfDocumentToPngConverter pdfDocumentToPngConverter;
	
	@Override
	public void afterPropertiesSet() throws Exception {
		DependencyInjectionUtils.checkRequiredDependencies(
			officeDocumentToPdfConverter,
			pdfDocumentToPngConverter
		);
	}

	@Override
	public List<String> convertToPng(String officeDocumentExtension, InputStream officeDocumentAsStream, String folderPath) {
		InputStream pdfDocumentAsStream = officeDocumentToPdfConverter.convertToPdf(officeDocumentExtension, officeDocumentAsStream);
		try {
			return pdfDocumentToPngConverter.convertToPng(pdfDocumentAsStream, folderPath);
		} finally {
			IOUtils.closeQuietly(pdfDocumentAsStream);
		}
	}
	
	public void setOfficeDocumentToPdfConverter(OfficeDocumentToPdfConverter officeDocumentToPdfConverter) {
		this.officeDocumentToPdfConverter = officeDocumentToPdfConverter;
	}
	public void setPdfDocumentToPngConverter(PdfDocumentToPngConverter pdfDocumentToPngConverter) {
		this.pdfDocumentToPngConverter = pdfDocumentToPngConverter;
	}
}