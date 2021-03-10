package ro.cloudSoft.cloudDoc.web.controllers.attachmentsPreview;

import java.io.ByteArrayInputStream;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

import ro.cloudSoft.cloudDoc.domain.content.Attachment;
import ro.cloudSoft.cloudDoc.fileFormats.converters.OfficeDocumentToPngConverter;
import ro.cloudSoft.common.utils.DependencyInjectionUtils;

public class OfficeDocumentAsPngAttachmentPreviewController extends AbstractAttachmentPreviewController implements Controller, InitializingBean {

	private OfficeDocumentToPngConverter officeDocumentToPngConverter;
	private String temporaryFilesFolderPath;
	
	@Override
	public void afterPropertiesSet() throws Exception {
		
		super.afterPropertiesSet();
		
		DependencyInjectionUtils.checkRequiredDependencies(
			officeDocumentToPngConverter,
			temporaryFilesFolderPath
		);
	}
	
	@Override
	public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		Attachment attachment = getAttachment(request);
		
		String attachmentName = attachment.getName();
		String attachmentExtension = FilenameUtils.getExtension(attachmentName);
		
		byte[] attachmentContent = attachment.getData();
		ByteArrayInputStream attachmentContentAsStream = new ByteArrayInputStream(attachmentContent);
		
		List<String> pngFileNames = officeDocumentToPngConverter.convertToPng(attachmentExtension, attachmentContentAsStream, temporaryFilesFolderPath);
		request.setAttribute("pngFileNames", pngFileNames);
		
		request.getRequestDispatcher("/previewAttachmentAsPng.jsp").forward(request, response);
		return null;
	}
	
	public void setOfficeDocumentToPngConverter(OfficeDocumentToPngConverter officeDocumentToPngConverter) {
		this.officeDocumentToPngConverter = officeDocumentToPngConverter;
	}
	public void setTemporaryFilesFolderPath(String temporaryFilesFolderPath) {
		this.temporaryFilesFolderPath = temporaryFilesFolderPath;
	}
}