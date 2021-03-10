package ro.cloudSoft.common.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.apache.poi.ss.usermodel.Workbook;

public class PoiXlsUtils {

	public static byte[] toByteArray(Workbook workbook) throws IOException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();		
		workbook.write(baos);		
		return baos.toByteArray();
	}
}
