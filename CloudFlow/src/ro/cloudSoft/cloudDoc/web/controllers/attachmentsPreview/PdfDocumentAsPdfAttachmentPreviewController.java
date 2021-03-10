package ro.cloudSoft.cloudDoc.web.controllers.attachmentsPreview;

import java.io.OutputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

import ro.cloudSoft.cloudDoc.domain.content.Attachment;

public class PdfDocumentAsPdfAttachmentPreviewController extends AbstractAttachmentPreviewController implements Controller, InitializingBean {
	
	@Override
	public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		Attachment attachment = getAttachment(request);		
		byte[] attachmentContent = attachment.getData();
		
		response.setContentType(CONTENT_TYPE_PDF);
		
		OutputStream responseOutputStream = response.getOutputStream();
		IOUtils.write(attachmentContent, responseOutputStream);
		
		return null;
	}
}