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
import ro.cloudSoft.cloudDoc.services.appUtilities.AppUtilitiesException;
import ro.cloudSoft.cloudDoc.services.appUtilities.AppUtilitiesService;
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
				writeResponseException(request, response, e);
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
				if (IMPORT_PREZENTA_COMISII_GL_COMMAND_NAME.equals(command)) {
					handleImportPrezentaComisiiGL(request, response);					
				} else if (IMPORT_DSP_COMMAND_NAME.equals(command)) {
					handleImportDSP(request, response);
				} else if (IMPORT_DSP_TASKS_COMMAND_NAME.equals(command)) {
					handleImportDSPTasks(request, response);
				} else if (GET_INFO_DOCUMENTS_BY_NAME_COMMAND_NAME.equals(command)) {
					handleGetInfoDocumentsByName(request, response);
				} else if (MODIFY_DOCUMENT_COMMAND_NAME.equals(command)) {
					handleModifyDocument(request, response);
				} else if (IMPORT_REGISTRU_INTRARI_COMMAND_NAME.equals(command)) {
					handleImportRegistruIntrari(request, response);
				} else if (IMPORT_REGISTRU_IESIRI_COMMAND_NAME.equals(command)) {
					handleImportRegistruIesiri(request, response);
				} else if (IMPORT_REGISTRU_DOCUMENTE_JUSTIFICATIVE_PLATI_COMMAND_NAME.equals(command)) {
					handleImportRegistruDocumenteJustificativePlati(request, response);
				} else if (SEND_MAIL_COMMAND_NAME.equals(command)) {
					handleSentMail(request, response);
				} else {
					handleUnknownAction(request, response);
				}
			} else {
				handleNoCommand(request, response);
			}
		} catch (Exception e) {
			try {
				writeResponseException(request, response, e);
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
		getAppUtilitiesService().sendMail(receiverAddress, subject, content);
		writeResponseSuccess(request, response, "<b>Successfully sent mail </b>");	
	}

	private AppUtilitiesService getAppUtilitiesService() {
		return (AppUtilitiesService) SpringUtils.getBean(AppUtilitiesService.class);
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
	
	private void handleImportPrezentaComisiiGL(HttpServletRequest request, HttpServletResponse response) throws Exception {	
		String excelFilePath = request.getParameter(IMPORT_PREZENTA_COMISII_GL_PARAM_NAME_EXCEL_FILE_PATH);
		String workspaceName = request.getParameter(IMPORT_PREZENTA_COMISII_GL_PARAM_NAME_WORKSPACE_NAME);
		String folderName = request.getParameter(IMPORT_PREZENTA_COMISII_GL_PARAM_NAME_FOLDER_NAME);
		getAppUtilitiesService().importPrezentaComisiiGL(excelFilePath, workspaceName, folderName);
		writeResponseSuccess(request, response, "<b>Successfully imported Prezenta Comisii GL</b>");	
	}
	
	private void handleImportDSP(HttpServletRequest request, HttpServletResponse response) throws Exception {	
		String excelFilePath = request.getParameter(IMPORT_DSP_PARAM_NAME_EXCEL_FILE_PATH);
		getAppUtilitiesService().importDSP(excelFilePath);
		writeResponseSuccess(request, response, "<b>Successfully imported DSP</b>");	
	}
	
	private void handleImportDSPTasks(HttpServletRequest request, HttpServletResponse response) throws Exception {	
		String excelFilePath = request.getParameter(IMPORT_DSP_TASKS_PARAM_NAME_EXCEL_FILE_PATH);
		getAppUtilitiesService().importDSPTasks(excelFilePath);
		writeResponseSuccess(request, response, "<b>Successfully imported DSP tasks</b>");	
	}
	
	private void handleGetInfoDocumentsByName(HttpServletRequest request, HttpServletResponse response) throws Exception {	
		String documentName = request.getParameter(GET_INFO_DOCUMENTS_BY_NAME_PARAM_NAME_DOCUMENT_NAME);
		List<String> infoDocuments = getAppUtilitiesService().getInfoDocumentsByName(documentName);
		StringBuilder text = new StringBuilder();
		if (CollectionUtils.isNotEmpty(infoDocuments)) {
			text.append("<p><b>"+infoDocuments.size() + "</b> documente</p>");
			for (String doc : infoDocuments) {
				text.append("<p>" + doc + "</p>");
			}
		} else {
			text.append("No documents.");
		}
		writeResponseSuccess(request, response, text.toString());	
	}
	
	private void handleModifyDocument(HttpServletRequest request, HttpServletResponse response) throws Exception {	
		String documentLocationRealName = request.getParameter(MODIFY_DOCUMENT_PARAM_NAME_DOCUMENT_LOCATION_REAL_NAME);
		String documentID = request.getParameter(MODIFY_DOCUMENT_PARAM_NAME_DOCUMENT_ID);
		String newDocumentName = request.getParameter(MODIFY_DOCUMENT_PARAM_NAME_NEW_DOCUMENT_NAME);
		String newDocumentDescription = request.getParameter(MODIFY_DOCUMENT_PARAM_NAME_NEW_DOCUMENT_DESCRIPTION);
		String metadataName = request.getParameter(MODIFY_DOCUMENT_PARAM_NAME_METADATA_NAME);
		String newMetadataValue = request.getParameter(MODIFY_DOCUMENT_PARAM_NAME_NEW_METADATA_VALUE);
		getAppUtilitiesService().modifyDocument(documentLocationRealName, documentID, newDocumentName, newDocumentDescription, metadataName, newMetadataValue);
		writeResponseSuccess(request, response, "<b>Successfully modified</b>");	
	}
	
	private void handleImportRegistruIntrari(HttpServletRequest request, HttpServletResponse response) throws Exception {	
		String excelFilePath = request.getParameter(IMPORT_REGISTRU_INTRARI_PARAM_NAME_EXCEL_FILE_PATH);
		getAppUtilitiesService().importRegistruIntrari(excelFilePath);
		writeResponseSuccess(request, response, "<b>Successfully imported atasamente registru intrari</b>");	
	}
	
	private void handleImportRegistruIesiri(HttpServletRequest request, HttpServletResponse response) throws Exception {	
		String excelFilePath = request.getParameter(IMPORT_REGISTRU_IESIRI_PARAM_NAME_EXCEL_FILE_PATH);
		getAppUtilitiesService().importRegistruIesiri(excelFilePath);
		writeResponseSuccess(request, response, "<b>Successfully imported atasamente registru iesiri</b>");	
	}
	
	private void handleImportRegistruDocumenteJustificativePlati(HttpServletRequest request, HttpServletResponse response) throws Exception {	
		String excelFilePath = request.getParameter(IMPORT_REGISTRU_DOCUMENTE_JUSTIFICATIVE_PLATI_PARAM_NAME_EXCEL_FILE_PATH);
		getAppUtilitiesService().importRegistruDocumenteJustificativePlati(excelFilePath);
		writeResponseSuccess(request, response, "<b>Successfully imported atasamente registru documente justificative plati</b>");	
	}

	private void handleUnknownAction(HttpServletRequest request, HttpServletResponse response) throws Exception {
		writeResponseMessage(request, response, CommandExecutionStatus.ERROR, "Comanda necunoscuta");
	}
	
	private void writeResponseException(HttpServletRequest request, HttpServletResponse response, Exception e) throws Exception {
		
		StringBuilder exceptionMessage = new StringBuilder();
		if (e instanceof AppUtilitiesException) {
			AppUtilitiesException ue = (AppUtilitiesException) e;			
			if (CollectionUtils.isNotEmpty(ue.getErrorMessages())) {				
				exceptionMessage.append("<ul>");
				for (String message : ue.getErrorMessages()) {
					exceptionMessage.append("<li>" + message + "</li>");
				}
				exceptionMessage.append("</ul>");
			} 	
		} else {
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			e.printStackTrace(pw);
			exceptionMessage.append("Error [" + sw.getBuffer().toString() + "]");
		}
		
		writeResponseMessage(request, response, CommandExecutionStatus.ERROR, exceptionMessage.toString());	
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