package ro.cloudSoft.cloudDoc.web.controllers.attachmentsPreview;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.OutputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

import ro.cloudSoft.cloudDoc.domain.content.Attachment;
import ro.cloudSoft.common.utils.DependencyInjectionUtils;
import ro.sdc.officeToPdf.OfficeDocumentToPdfConverter;

public class OfficeDocumentAsPdfAttachmentPreviewController extends AbstractAttachmentPreviewController implements Controller, InitializingBean {
	
	private OfficeDocumentToPdfConverter officeDocumentToPdfConverter;
	
	@Override
	public void afterPropertiesSet() throws Exception {
		
		super.afterPropertiesSet();
		
		DependencyInjectionUtils.checkRequiredDependencies(
			officeDocumentToPdfConverter
		);
	}
	
	@Override
	public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		Attachment attachment = getAttachment(request);
		
		String attachmentName = attachment.getName();
		String attachmentExtension = FilenameUtils.getExtension(attachmentName);
		
		byte[] attachmentContent = attachment.getData();
		ByteArrayInputStream attachmentContentAsStream = new ByteArrayInputStream(attachmentContent);
		
		InputStream pdfAsStream = officeDocumentToPdfConverter.convertToPdf(attachmentExtension, attachmentContentAsStream);
		try {

			response.setContentType(CONTENT_TYPE_PDF);
			
			OutputStream responseOutputStream = response.getOutputStream();
			IOUtils.copy(pdfAsStream, responseOutputStream);
		} finally {
			pdfAsStream.close();
		}
		
		return null;
	}
	
	public void setOfficeDocumentToPdfConverter(OfficeDocumentToPdfConverter officeDocumentToPdfConverter) {
		this.officeDocumentToPdfConverter = officeDocumentToPdfConverter;
	}
}