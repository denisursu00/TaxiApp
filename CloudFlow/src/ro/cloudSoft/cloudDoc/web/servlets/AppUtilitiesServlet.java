package ro.cloudSoft.cloudDoc.web.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import ro.cloudSoft.cloudDoc.config.environment.AppEnvironment;
import ro.cloudSoft.cloudDoc.config.environment.AppEnvironmentConfig;
import ro.cloudSoft.cloudDoc.utils.log.LogHelper;
import ro.cloudSoft.common.utils.spring.SpringUtils;

public class AppUtilitiesServlet extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
	
	private static final String LOGGER_OPERATION = "app utilities operation";
	private static final LogHelper logger = LogHelper.getInstance(AppUtilitiesServlet.class);
	
	private static final String COMMAND_PARAM_NAME = "command";
	private static final String SECURITY_KEY_PARAM_NAME = "securityKey";
	private static final String SECURITY_KEY_PROD = "DIf7c447efjlFGNnTJG6owtFsGkoyMZE";
	private static final String SECURITY_KEY_NON_PROD = "oyOC819GwgxLoTkCuMMgEqRpe9pYbx7H";
	
	private static final String IMPORT_PREZENTA_COMISII_GL_COMMAND_NAME = "importPrezentaComisiiGL";
	private static final String IMPORT_PREZENTA_COMISII_GL_PARAM_NAME_EXCEL_FILE_PATH = "excelFilePath";
	private static final String IMPORT_PREZENTA_COMISII_GL_PARAM_NAME_WORKSPACE_NAME = "workspaceName";
	private static final String IMPORT_PREZENTA_COMISII_GL_PARAM_NAME_FOLDER_NAME = "folderName";
	
	private static final String IMPORT_DSP_COMMAND_NAME = "importDSP";
	private static final String IMPORT_DSP_PARAM_NAME_EXCEL_FILE_PATH = "excelFilePath";
	
	private static final String IMPORT_DSP_TASKS_COMMAND_NAME = "importDSPTasks";
	private static final String IMPORT_DSP_TASKS_PARAM_NAME_EXCEL_FILE_PATH = "excelFilePath";

	private static final String GET_INFO_DOCUMENTS_BY_NAME_COMMAND_NAME = "getInfoDocumentsByName";
	private static final String GET_INFO_DOCUMENTS_BY_NAME_PARAM_NAME_DOCUMENT_NAME = "documentName";	
	
	private static final String IMPORT_REGISTRU_INTRARI_COMMAND_NAME = "importRegistruIntrari";
	private static final String IMPORT_REGISTRU_INTRARI_PARAM_NAME_EXCEL_FILE_PATH = "excelFilePath";
	
	private static final String IMPORT_REGISTRU_IESIRI_COMMAND_NAME = "importRegistruIesiri";
	private static final String IMPORT_REGISTRU_IESIRI_PARAM_NAME_EXCEL_FILE_PATH = "excelFilePath";
	
	private static final String IMPORT_REGISTRU_DOCUMENTE_JUSTIFICATIVE_PLATI_COMMAND_NAME = "importRegistruDocumenteJustificativePlati";
	private static final String IMPORT_REGISTRU_DOCUMENTE_JUSTIFICATIVE_PLATI_PARAM_NAME_EXCEL_FILE_PATH = "excelFilePath";
	
	private static final String MODIFY_DOCUMENT_COMMAND_NAME = "modifyDocument";
	private static final String MODIFY_DOCUMENT_PARAM_NAME_DOCUMENT_LOCATION_REAL_NAME = "documentLocationRealName";
	private static final String MODIFY_DOCUMENT_PARAM_NAME_DOCUMENT_ID = "documentID";
	private static final String MODIFY_DOCUMENT_PARAM_NAME_NEW_DOCUMENT_NAME = "newDocumentName";
	private static final String MODIFY_DOCUMENT_PARAM_NAME_NEW_DOCUMENT_DESCRIPTION = "newDocumentDescription";
	private static final String MODIFY_DOCUMENT_PARAM_NAME_METADATA_NAME = "metadataName";
	private static final String MODIFY_DOCUMENT_PARAM_NAME_NEW_METADATA_VALUE = "newMetadataValue";
	
	private static final String SEND_MAIL_COMMAND_NAME = "sendMail";
	private static final String SEND_MAIL_PARAM_NAME_RECEIVER_ADDRESS = "receiverAddress";
	private static final String SEND_MAIL_PARAM_NAME_SUBJECT = "subject";
	private static final String SEND_MAIL_PARAM_NAME_CONTENT = "content";

	
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			handleNoCommand(request, response);
		} catch (Exception e) {
			try {
				
			} catch (Exception er) {
				e.printStackTrace();
				logger.error("AppUtilities: O eroare a fost intampinata la scriere raspunsului [" + er.getMessage() + "].", er, LOGGER_OPERATION);
				throw new ServletException(e);
			}
		}
	}
	
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			checkSecurity(request);
			String command = request.getParameter(COMMAND_PARAM_NAME);
			if (StringUtils.isNotBlank(command)) {
				if (SEND_MAIL_COMMAND_NAME.equals(command)) {
					handleSentMail(request, response);
				} else {
					handleUnknownAction(request, response);
				}
			} else {
				handleNoCommand(request, response);
			}
		} catch (Exception e) {
			try {
				
			} catch (Exception er) {
				e.printStackTrace();
				logger.error("AppUtilities: O eroare a fost intampinata la scriere raspunsului [" + er.getMessage() + "].", er, LOGGER_OPERATION);
				throw new ServletException(e);
			}
		}
	}
	
	private void handleSentMail(HttpServletRequest request, HttpServletResponse response) throws Exception{
		String receiverAddress = request.getParameter(SEND_MAIL_PARAM_NAME_RECEIVER_ADDRESS);
		String subject = request.getParameter(SEND_MAIL_PARAM_NAME_SUBJECT);
		String content = request.getParameter(SEND_MAIL_PARAM_NAME_CONTENT);
		writeResponseSuccess(request, response, "<b>Successfully sent mail </b>");	
	}
	
	private void checkSecurity(HttpServletRequest request) throws Exception {
		String securityKey = getSecurityKeyParameter(request);
		if (AppEnvironmentConfig.getCurrentEnvironment().equals(AppEnvironment.PRODUCTION)) {
			if (!SECURITY_KEY_PROD.equals(securityKey))  {
				throw new Exception("Access denied");
			}
		} else if (AppEnvironmentConfig.getCurrentEnvironment().equals(AppEnvironment.DEVELOPMENT) 
				|| AppEnvironmentConfig.getCurrentEnvironment().equals(AppEnvironment.TESTING)
				|| AppEnvironmentConfig.getCurrentEnvironment().equals(AppEnvironment.STAGING)) {
			if (!SECURITY_KEY_NON_PROD.equals(securityKey)) {
				throw new Exception("Access denied");
			}
		} else {
			throw new Exception("Access denied");
		}
	}
	
	private void handleNoCommand(HttpServletRequest request, HttpServletResponse response) throws Exception {
		writeResponseMessage(request, response, CommandExecutionStatus.NONE, null);
	}

	private void handleUnknownAction(HttpServletRequest request, HttpServletResponse response) throws Exception {
		writeResponseMessage(request, response, CommandExecutionStatus.ERROR, "Comanda necunoscuta");
	}
	
	
	
	private void writeResponseSuccess(HttpServletRequest request, HttpServletResponse response, String message) throws Exception {
		writeResponseMessage(request, response, CommandExecutionStatus.SUCCCESS, message);	
	}
	
	private String getCommandaRow(HttpServletRequest request, String command, String description, String... commandParameterNames) {
		
		String row = new String();
		row += "<tr>";
		row += "	<td width=\"20%\"><i>" + command + "</i></td>";
		row += "	<td width=\"30%\">" + description + "</td>";
		row += "	<td>";
		row += "		<form action=\"" + request.getContextPath() + "/appUtilities\" method=\"POST\">";
		row += "		<table>";
		row += "		<tr>";
		row += "			<td>";
		row += "				<input type=\"hidden\" name=\"command\" value=\"" + command + "\">";
		row += "			<table>";
		row += "					<tr>";		
		row += "						<td><span><i>" + SECURITY_KEY_PARAM_NAME + "</i></span></td>";
		row += "						<td><input type=\"text\" name=\""+ SECURITY_KEY_PARAM_NAME + "\" value=\"\" style=\"width: 300px;\"></td>";			
		row += "					</tr>";
		if (commandParameterNames != null && commandParameterNames.length > 0) {			
			for (String commandParameterName : commandParameterNames) {		
				row += "					<tr>";		
				row += "						<td><span><i>" + commandParameterName + "</i></span></td>";
				row += "						<td><input type=\"text\" name=\""+ commandParameterName + "\" value=\"\" style=\"width: 300px;\"></td>";			
				row += "					</tr>";
			}			
		}
		row += "			</table>";
		row += "				<br />";
		row += "				<input type=\"submit\" value=\"Executa\" onclick=\"return confirm('Sunteti sigur?');\">";
		row += "			</td>";
		row += "		</tr>";
		row += "		</table>";
		row += "		</form>";
		row += "	</td>";
		row += "</tr>";
		return row;
	}
	
	private void writeResponseMessage(HttpServletRequest request, HttpServletResponse response, CommandExecutionStatus cmdStatus, String message) throws Exception {
		
		response.setContentType("text/html");
		response.setCharacterEncoding("UTF-8");
		
		PrintWriter out = response.getWriter();
		
		out.println("<table border=\"1\" width=\"100%\" cellpadding=\"4\" cellspacing=\"0\">");
		out.println("<tr><td colspan=\"3\"><h4><center>Interfata utilitara pentru diverse operatii in aplicatie</center></h4></td></tr>");
		
		if (!CommandExecutionStatus.NONE.equals(cmdStatus)) {
			out.println("<tr><td colspan=\"3\">Comanda executata: <i>" + getCommandParameter(request) + "</i> - <b>" + cmdStatus + "</b><br />" + message + "</td></tr>");
		}
		
		out.println(getCommandaRow(request, IMPORT_PREZENTA_COMISII_GL_COMMAND_NAME , "Importa documente Prezenta Comisii GL.", IMPORT_PREZENTA_COMISII_GL_PARAM_NAME_EXCEL_FILE_PATH, IMPORT_PREZENTA_COMISII_GL_PARAM_NAME_WORKSPACE_NAME, IMPORT_PREZENTA_COMISII_GL_PARAM_NAME_FOLDER_NAME));
		out.println(getCommandaRow(request, IMPORT_DSP_COMMAND_NAME, "Importa proiecte (DSP-uri)", IMPORT_DSP_PARAM_NAME_EXCEL_FILE_PATH));
		out.println(getCommandaRow(request, IMPORT_DSP_TASKS_COMMAND_NAME, "Importa task-uri proiect DSP", IMPORT_DSP_TASKS_PARAM_NAME_EXCEL_FILE_PATH));
		out.println(getCommandaRow(request, GET_INFO_DOCUMENTS_BY_NAME_COMMAND_NAME, "Informatii document(e)", GET_INFO_DOCUMENTS_BY_NAME_PARAM_NAME_DOCUMENT_NAME));
		out.println(getCommandaRow(request, MODIFY_DOCUMENT_COMMAND_NAME, "Modificare document (nu este obligatorie completarea si a numelui de document si a metadatei)", MODIFY_DOCUMENT_PARAM_NAME_DOCUMENT_LOCATION_REAL_NAME, MODIFY_DOCUMENT_PARAM_NAME_DOCUMENT_ID, MODIFY_DOCUMENT_PARAM_NAME_NEW_DOCUMENT_NAME, MODIFY_DOCUMENT_PARAM_NAME_NEW_DOCUMENT_DESCRIPTION, MODIFY_DOCUMENT_PARAM_NAME_METADATA_NAME, MODIFY_DOCUMENT_PARAM_NAME_NEW_METADATA_VALUE));
		out.println(getCommandaRow(request, IMPORT_REGISTRU_INTRARI_COMMAND_NAME, "Importa atasamente registru intrari", IMPORT_REGISTRU_INTRARI_PARAM_NAME_EXCEL_FILE_PATH));
		out.println(getCommandaRow(request, IMPORT_REGISTRU_IESIRI_COMMAND_NAME, "Importa atasamente registru iesiri", IMPORT_REGISTRU_IESIRI_PARAM_NAME_EXCEL_FILE_PATH));
		out.println(getCommandaRow(request, IMPORT_REGISTRU_DOCUMENTE_JUSTIFICATIVE_PLATI_COMMAND_NAME, "Importa atasamente registru documente justificative plati", IMPORT_REGISTRU_DOCUMENTE_JUSTIFICATIVE_PLATI_PARAM_NAME_EXCEL_FILE_PATH));
		out.println(getCommandaRow(request, SEND_MAIL_COMMAND_NAME, "Trimite email", SEND_MAIL_PARAM_NAME_RECEIVER_ADDRESS, SEND_MAIL_PARAM_NAME_SUBJECT, SEND_MAIL_PARAM_NAME_CONTENT));

		
		out.println("</table>");
		out.println("<br />");
	}
	
	private String getCommandParameter(HttpServletRequest request) {
		return request.getParameter(COMMAND_PARAM_NAME);
	}
	
	private String getSecurityKeyParameter(HttpServletRequest request) {
		return request.getParameter(SECURITY_KEY_PARAM_NAME);
	}
	
	private static enum CommandExecutionStatus {
		SUCCCESS, ERROR, NONE
	}
}