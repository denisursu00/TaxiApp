package ro.cloudSoft.cloudDoc.utils.excel;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import ro.cloudSoft.cloudDoc.core.AppException;
import ro.cloudSoft.cloudDoc.core.AppExceptionUtils;
import ro.cloudSoft.cloudDoc.presentation.client.shared.exceptions.PresentationException;
import ro.cloudSoft.cloudDoc.presentation.server.utils.PresentationExceptionUtils;
import ro.cloudSoft.cloudDoc.services.appUtilities.AppUtilitiesException;

public class ExcelReaderUtils {
	public static Map<Integer, List<Object>> getDataFromFile(String filePath, int columnsNumber) throws AppUtilitiesException{
		Map<Integer, List<Object>> data = null;
		FileInputStream file = null;
		Workbook workbook = null;
		try {
			file = new FileInputStream(new File(filePath));

			workbook = new XSSFWorkbook(file);
			Sheet sheet = workbook.getSheetAt(0);

			data = new HashMap<Integer, List<Object>>();
			int i = 0;
			for (Row row : sheet) {
				List<Object> rowData = new ArrayList<Object>();
				for (int columnIndex = 0; columnIndex < columnsNumber; columnIndex++) {
					Cell cell = row.getCell(columnIndex);
					if (cell == null) {
						rowData.add(null);
					} else if ((cell.getCellTypeEnum() == CellType.NUMERIC) && DateUtil.isCellDateFormatted(cell)) {
						rowData.add(cell.getDateCellValue());
					} else {
						rowData.add("" + cell);
					}
				}

				data.put(i, rowData);
				i++;
			}
		} catch (FileNotFoundException e) {
			throw new AppUtilitiesException(" Fisierul nu a fost gasit sau calea spre fisier este gresita! ");
		} catch (IOException e) {
			throw new AppUtilitiesException(e.getMessage());
		} finally {
			try {
				if (workbook != null) {
					workbook.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
				throw new AppUtilitiesException(e.getMessage());
			}
			try {
				if (file != null) {
					file.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
				throw new AppUtilitiesException(e.getMessage());
			}
		}

		return data;

	}
}
