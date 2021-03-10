package ro.cloudSoft.cloudDoc.fileFormats.converters;

import java.io.InputStream;
import java.util.List;

public interface PdfDocumentToPngConverter {

	/**
	 * Converteste un document PDF in poze PNG - cate un fisier PNG pentru fiecare pagina.
	 * Fisierele PNG rezultate se vor afla in acelasi director cu documentul PDF.
	 * 
	 * @return numele fisierelor PNG rezultate (/cale/document.pdf => [document_0001.png, document_0002.png, ...])
	 */
	List<String> convertToPng(String sourcePdfDocumentFilePath);

	/**
	 * Converteste un document PDF in poze PNG - cate un fisier PNG pentru fiecare pagina.
	 * Fisierele PNG rezultate se vor salva in directorul dat.
	 * 
	 * @param sourcePdfDocumentContentAsStream continutul documentului PDF
	 * @param folderPath calea directorului unde se vor salva fisierele PNG
	 * 
	 * @return numele fisierelor PNG rezultate (document_0001.png, document_0002.png, ...)
	 */
	List<String> convertToPng(InputStream sourcePdfDocumentContentAsStream, String folderPath);
}