package ro.cloudSoft.cloudDoc.web.controllers.attachmentsPreview;

import java.io.ByteArrayInputStream;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;

import ro.cloudSoft.cloudDoc.domain.content.Attachment;
import ro.cloudSoft.cloudDoc.fileFormats.converters.PdfDocumentToPngConverter;
import ro.cloudSoft.common.utils.DependencyInjectionUtils;

public class PdfDocumentAsPngAttachmentPreviewController extends AbstractAttachmentPreviewController {
	
	private PdfDocumentToPngConverter pdfDocumentToPngConverter;
	private String temporaryFilesFolderPath;

	@Override
	public void afterPropertiesSet() throws Exception {
		
		super.afterPropertiesSet();
		
		DependencyInjectionUtils.checkRequiredDependencies(
			pdfDocumentToPngConverter,
			temporaryFilesFolderPath
		);
	}
	
	@Override
	public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		Attachment attachment = getAttachment(request);
		
		byte[] attachmentContent = attachment.getData();
		ByteArrayInputStream attachmentContentAsStream = new ByteArrayInputStream(attachmentContent);
		
		List<String> pngFileNames = pdfDocumentToPngConverter.convertToPng(attachmentContentAsStream, temporaryFilesFolderPath);
		request.setAttribute("pngFileNames", pngFileNames);
		
		request.getRequestDispatcher("/previewAttachmentAsPng.jsp").forward(request, response);
		return null;
	}
	
	public void setPdfDocumentToPngConverter(PdfDocumentToPngConverter pdfDocumentToPngConverter) {
		this.pdfDocumentToPngConverter = pdfDocumentToPngConverter;
	}
	public void setTemporaryFilesFolderPath(String temporaryFilesFolderPath) {
		this.temporaryFilesFolderPath = temporaryFilesFolderPath;
	}
}