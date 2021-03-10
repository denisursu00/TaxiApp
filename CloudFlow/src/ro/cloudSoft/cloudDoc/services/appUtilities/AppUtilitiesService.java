package ro.cloudSoft.cloudDoc.services.appUtilities;

import java.util.List;

import org.apache.commons.lang3.StringUtils;

import ro.cloudSoft.cloudDoc.domain.mail.EmailMessage;
import ro.cloudSoft.cloudDoc.plugins.content.DocumentUtilitiesPlugin;
import ro.cloudSoft.cloudDoc.services.MailService;

public class AppUtilitiesService {

	private PrezentaComisiiGLImporter prezentaComisiiGLImporter;
	private DSPImporter dspImporter;
	private DSPTasksImporter dspTasksImporter;
	private DocumentUtilitiesPlugin documentUtilitiesPlugin;
	private RegistruIntrariImporter registruIntrariImporter;
	private RegistruIesiriImporter registruIesiriImporter;
	private RegistruDocumenteJustificativePlatiImporter registruDocumenteJustificativePlatiImporter;
	private MailService mailService;

	public void importPrezentaComisiiGL(String excelFilePath, String workspaceName, String folderName)
			throws AppUtilitiesException {
		prezentaComisiiGLImporter.doImport(excelFilePath, workspaceName, folderName);
	}
	
	public void sendMail(String receiverAddress, String subject, String content) throws AppUtilitiesException{
		if (StringUtils.isBlank(receiverAddress)) {
			throw new AppUtilitiesException("Receiver address must be valid!");
		}
		if (StringUtils.isBlank(subject)) {
			throw new AppUtilitiesException("Subject can't be empty!");
		}
		EmailMessage emailMessage = new EmailMessage(receiverAddress, subject, content);
		mailService.send(emailMessage);
	}

	public void importDSP(String excelFilePath) throws AppUtilitiesException {
		dspImporter.doImport(excelFilePath);
	}

	public void importDSPTasks(String excelFilePath) throws AppUtilitiesException {
		dspTasksImporter.doImport(excelFilePath);
	}

	public List<String> getInfoDocumentsByName(String documentName) throws AppUtilitiesException {
		try {
			return documentUtilitiesPlugin.getInfoDocumentsByName(documentName);
		} catch (Exception e) {
			throw new AppUtilitiesException(e.getMessage());
		}
	}

	public void modifyDocument(String documentLocationRealName, String documentID, String newDocumentName,
			String newDocumentDescription, String metadataName, String newMetadataValue) throws AppUtilitiesException {
		documentUtilitiesPlugin.modifyDocument(documentLocationRealName, documentID, newDocumentName,
				newDocumentDescription, metadataName, newMetadataValue);
	}

	public void importRegistruIntrari(String excelFilePath) throws AppUtilitiesException {
		registruIntrariImporter.doImport(excelFilePath);
	}

	public void importRegistruIesiri(String excelFilePath) throws AppUtilitiesException {
		registruIesiriImporter.doImport(excelFilePath);
	}
	
	public void importRegistruDocumenteJustificativePlati(String excelFilePath) throws AppUtilitiesException {
		registruDocumenteJustificativePlatiImporter.doImport(excelFilePath);
	}

	public void setPrezentaComisiiGLImporter(PrezentaComisiiGLImporter prezentaComisiiGLImporter) {
		this.prezentaComisiiGLImporter = prezentaComisiiGLImporter;
	}

	public void setDspImporter(DSPImporter dspImporter) {
		this.dspImporter = dspImporter;
	}

	public void setDspTasksImporter(DSPTasksImporter dspTasksImporter) {
		this.dspTasksImporter = dspTasksImporter;
	}

	public void setDocumentUtilitiesPlugin(DocumentUtilitiesPlugin documentUtilitiesPlugin) {
		this.documentUtilitiesPlugin = documentUtilitiesPlugin;
	}

	public void setRegistruIntrariImporter(RegistruIntrariImporter registruIntrariImporter) {
		this.registruIntrariImporter = registruIntrariImporter;
	}

	public void setRegistruIesiriImporter(RegistruIesiriImporter registruIesiriImporter) {
		this.registruIesiriImporter = registruIesiriImporter;
	}

	public void setRegistruDocumenteJustificativePlatiImporter(
			RegistruDocumenteJustificativePlatiImporter registruDocumenteJustificativePlatiImporter) {
		this.registruDocumenteJustificativePlatiImporter = registruDocumenteJustificativePlatiImporter;
	}

	public void setMailService(MailService mailService) {
		this.mailService = mailService;
	}
}
