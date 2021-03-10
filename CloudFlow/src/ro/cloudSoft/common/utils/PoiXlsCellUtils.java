package ro.cloudSoft.common.utils;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.util.CellReference;

public class PoiXlsCellUtils {

	public static boolean isNotBlankCell(Cell cell) {
		return !isBlankCell(cell);
	}
	
	public static boolean isBlankCell(Cell cell) {
		
		if (cell == null) {
			return true;
		}
		
		int cellType = cell.getCellType();
		if (cellType == Cell.CELL_TYPE_BLANK) {
			return true;
		} else if (cellType == Cell.CELL_TYPE_STRING) {
			String cellValue = cell.getStringCellValue();
			return StringUtils.isBlank(cellValue);
		} else {
			// Other types cannot be empty.
			return false;
		}
	}
	
	public static String getPositionAsString(int rowIndex, int colIndex) {
		return new CellReference(rowIndex, colIndex).formatAsString();
	}
}
