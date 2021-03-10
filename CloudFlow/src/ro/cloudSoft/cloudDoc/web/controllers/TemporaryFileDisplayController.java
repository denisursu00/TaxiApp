package ro.cloudSoft.cloudDoc.web.controllers;

import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStream;
import java.net.URLConnection;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

import ro.cloudSoft.common.utils.DependencyInjectionUtils;

/**
 * Controller ce afiseaza un fisier temporar, dupa care il sterge
 */
public class TemporaryFileDisplayController implements Controller, InitializingBean {
	
	private static final Logger LOGGER = Logger.getLogger(TemporaryFileDisplayController.class.getName());
	
	private static final String PARAMETER_NAME_TEMPORARY_FILE_NAME = "tempFileName";
	
	private String temporaryFilesFolderPath;

	@Override
	public void afterPropertiesSet() throws Exception {		
		DependencyInjectionUtils.checkRequiredDependencies(
			temporaryFilesFolderPath
		);
	}

	@Override
	public ModelAndView handleRequest(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		
		String temporaryFileName = request.getParameter(PARAMETER_NAME_TEMPORARY_FILE_NAME);
		if (StringUtils.isBlank(temporaryFileName)) {
			throw new IllegalArgumentException("Nu s-a specificat numele fisierului temporar.");
		}
		
		File temporaryFilesFolder = new File(temporaryFilesFolderPath);
		
		File temporaryFile = new File(temporaryFilesFolder, temporaryFileName);
		if (!temporaryFile.exists()) {
			throw new IllegalArgumentException("Nu exista fisierul temporar cu numele [" + temporaryFileName + "].");
		}
		
		String contentType = getContentType(temporaryFileName);
		
		try {
			FileInputStream temporaryFileInputStream = new FileInputStream(temporaryFile);
			try {
				response.setContentType(contentType);
				
				OutputStream responseOutputStream = response.getOutputStream();
				IOUtils.copy(temporaryFileInputStream, responseOutputStream);
			} finally {
				IOUtils.closeQuietly(temporaryFileInputStream);
			}
		} finally {
			boolean deletedTemporaryFile = temporaryFile.delete();
			if (!deletedTemporaryFile) {
				LOGGER.warning("Nu s-a putut sterge fisierul temporar [" + temporaryFile.getPath() + "].");
			}
		}
		
		return null;
	}
	
	private String getContentType(String fileName) {
		String contentType = URLConnection.guessContentTypeFromName(fileName);
		if (contentType == null) {
			throw new RuntimeException("Nu s-a putut determina content type-ul pentru fisierul [" + fileName + "].");
		}
		return contentType;
	}
	
	public void setTemporaryFilesFolderPath(String temporaryFilesFolderPath) {
		this.temporaryFilesFolderPath = temporaryFilesFolderPath;
	}
}