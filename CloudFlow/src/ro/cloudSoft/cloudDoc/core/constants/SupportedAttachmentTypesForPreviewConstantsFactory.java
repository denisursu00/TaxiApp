package ro.cloudSoft.cloudDoc.core.constants;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;

import ro.cloudSoft.cloudDoc.config.environment.AppEnvironmentConfig;

import com.google.common.collect.Maps;

public class SupportedAttachmentTypesForPreviewConstantsFactory {

	private static final String PARAMETERS_FILE_PACKAGE_PATH_WITHOUT_APP_ENVIRONMENT_SUFFIX = "/conf/attachmentsPreview.properties";

	public static SupportedAttachmentTypesForPreviewConstants create() {

		String parametersFilePackagePath = AppEnvironmentConfig.getFilePackagePathWithSuffix(PARAMETERS_FILE_PACKAGE_PATH_WITHOUT_APP_ENVIRONMENT_SUFFIX);
		InputStream parametersFileAsStream = CereriDeConcediuConstantsFactory.class.getResourceAsStream(parametersFilePackagePath);
		
		try {
			Properties parameters = new Properties();
			try {
				parameters.load(parametersFileAsStream);
			} catch (IOException ioe) {
				throw new IllegalStateException("Nu s-a putut incarca fisierul cu parametri (" + parametersFilePackagePath + ").", ioe);
			}
			
			Set<String> previewUrls = parameters.stringPropertyNames();
			Map<String, String> previewUrlByFileExtensionInLowerCase = Maps.newHashMap();
			
			for (String previewUrl : previewUrls) {
				String supportedFileExtensionsJoined = parameters.getProperty(previewUrl);
				String[] supportedFileExtensions = StringUtils.split(supportedFileExtensionsJoined, ',');
				for (String supportedFileExtension : supportedFileExtensions) {
					String supportedFileExtensionInLowerCase = supportedFileExtension.toLowerCase();
					previewUrlByFileExtensionInLowerCase.put(supportedFileExtensionInLowerCase, previewUrl);
				}
			}
			
			SupportedAttachmentTypesForPreviewConstants constants = new SupportedAttachmentTypesForPreviewConstants();
			constants.setPreviewUrlByFileExtensionInLowerCase(previewUrlByFileExtensionInLowerCase);
			
			return constants;
		} finally {
			IOUtils.closeQuietly(parametersFileAsStream);
		}
	}
}