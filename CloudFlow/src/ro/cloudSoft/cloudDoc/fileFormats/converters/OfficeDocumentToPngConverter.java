package ro.cloudSoft.cloudDoc.fileFormats.converters;

import java.io.InputStream;
import java.util.List;

public interface OfficeDocumentToPngConverter {

	/**
	 * Converteste un document MS Office in poze PNG - cate un fisier PNG pentru fiecare pagina.
	 * Fisierele PNG rezultate se vor salva in directorul dat.
	 * 
	 * @param officeDocumentExtension extensia documentului Office (doc, xls etc.)
	 * @param officeDocumentAsStream documentul Office reprezentat ca un stream
	 * @param folderPath calea directorului unde se vor salva fisierele PNG
	 * 
	 * @return numele fisierelor PNG rezultate (document_0001.png, document_0002.png, ...)
	 */
	List<String> convertToPng(String officeDocumentExtension, InputStream officeDocumentAsStream, String folderPath);
}