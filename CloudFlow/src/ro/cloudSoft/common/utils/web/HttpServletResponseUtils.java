package ro.cloudSoft.common.utils.web;

import javax.servlet.http.HttpServletResponse;

public class HttpServletResponseUtils {

	public static void setHeaderForAttachmentWithName(HttpServletResponse response, String attachmentName) {
		response.setHeader("Content-Disposition", "attachment; filename=\"" + attachmentName + "\"");
	}
}