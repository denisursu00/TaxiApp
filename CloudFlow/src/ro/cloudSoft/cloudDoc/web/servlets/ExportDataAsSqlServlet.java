package ro.cloudSoft.cloudDoc.web.servlets;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;

import ro.cloudSoft.cloudDoc.services.export.ExportDataAsSqlService;
import ro.cloudSoft.common.utils.spring.SpringUtils;

@SuppressWarnings("serial")
public class ExportDataAsSqlServlet extends HttpServlet {
	
	private final int ARBITARY_SIZE = 1048;
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
		ExportDataAsSqlService exportDataAsSqlService = SpringUtils.getBean("exportDataAsSqlService");
		
		
        String input = null;
        String export = req.getParameter("export");
        try {
        	
        	if (StringUtils.isBlank(export)) {
        		input = exportDataAsSqlService.exportAll();
        	} else {
        		if (export.equals("workflows")) {
        			input = exportDataAsSqlService.exportWorkflows();
        		} else if (export.equals("documentTypes")) {
        			input = exportDataAsSqlService.exportDocumentTypes();
        		} else {
        			throw new Exception("unknow export [" + export + "]");
        		}
        	}
        	resp.setContentType("application/sql");
        	resp.setHeader("Content-disposition", "attachment; filename=data-"+ System.currentTimeMillis() + ".sql");
        	try(InputStream in = IOUtils.toInputStream(input, "UTF-8"); OutputStream out = resp.getOutputStream()) {
        		byte[] buffer = new byte[ARBITARY_SIZE];     
        		int numBytesRead;
        		while ((numBytesRead = in.read(buffer)) > 0) {
        			out.write(buffer, 0, numBytesRead);
        		}
            }
        } catch (Exception e) {
        	e.printStackTrace();
        	PrintWriter out = resp.getWriter();
            out.println("ERROR: " + e.getMessage());
		}
	}
}
