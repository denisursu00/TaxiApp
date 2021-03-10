package ro.cloudSoft.cloudDoc.services.appUtilities;

import java.text.DecimalFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.formula.FormulaParseException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

import com.google.common.collect.Lists;

import ro.cloudSoft.cloudDoc.services.appUtilities.DSPTaskXlsModel.PrioritateTask;
import ro.cloudSoft.cloudDoc.services.appUtilities.DSPTaskXlsModel.StatusTask;
import ro.cloudSoft.cloudDoc.services.appUtilities.DSPProiectXlsModel.DSPArieDeCuprindere;
import ro.cloudSoft.cloudDoc.services.appUtilities.DSPProiectXlsModel.DSPGradDeImportanta;
import ro.cloudSoft.cloudDoc.services.appUtilities.DSPProiectXlsModel.DSPStatusProiect;
import ro.cloudSoft.cloudDoc.services.appUtilities.DSPProiectXlsModel.YesNoValue;
import ro.cloudSoft.common.utils.NumberUtils;
import ro.cloudSoft.common.utils.PoiXlsCellUtils;


public class ImportSheetParser {
	
	private static final int DEFAULT_HEADER_ROW_INDEX = 0;
	private static final int DEFAULT_HEADER_COL_INDEX = 0;
	
	private final Sheet sheet;
	private final FormulaEvaluator formulaEvaluator;
	private final Class theClass;
	private final List<ColumnMapping> attributesMapping;
	
	private int headerRowIndex = DEFAULT_HEADER_ROW_INDEX;
	private int headerColIndex = DEFAULT_HEADER_COL_INDEX;
	
	private Map<Integer, ColumnMapping> attributeMappingByHeaderColumnIndex;
	private List<Object> rows;
	
	private List<String> messages;
	
	public ImportSheetParser(Sheet sheet, FormulaEvaluator formulaEvaluator, Class theClass, List<ColumnMapping> attributesMapping) {
		this.sheet = sheet;
		this.formulaEvaluator = formulaEvaluator;
		this.theClass = theClass;
		this.attributesMapping = attributesMapping;
	}
	
	private void init() {
		rows = new ArrayList<>();		
		attributeMappingByHeaderColumnIndex = new HashMap<>();
		messages = new ArrayList<>();
	}
	
	public List parse() throws AppUtilitiesException {
		
		init();
		
		determineHeaderColumnIndex();
		
		if (!areMessages()) {
			parseRows();
		}
		
		if (areMessages()) {
			throw new AppUtilitiesException(messages);
		}
		
		return rows;
	}
	
	private boolean areMessages() {
		return CollectionUtils.isNotEmpty(messages);
	}
	
	private void determineHeaderColumnIndex() {
		
		Row headerRow = sheet.getRow(headerRowIndex);
		
		if (headerRow != null) {
			XlsCellValueObtainer headerCellValueObtainer = new TextXlsCellValueObtainer();
			for (int colIndex = headerColIndex; colIndex < attributesMapping.size(); colIndex++) {			
				Cell headerCell = headerRow.getCell(colIndex);
				if (PoiXlsCellUtils.isNotBlankCell(headerCell)) {
					try {
						String headerCellValue = (String) headerCellValueObtainer.getValue(formulaEvaluator, headerCell);
						headerCellValue = StringUtils.substringBefore(headerCellValue, "\n");
						if (StringUtils.isNotBlank(headerCellValue)) {							
							headerCellValue = headerCellValue.trim();
							for (ColumnMapping attributeMapping : attributesMapping) {
								if (attributeMapping.getColumnNameXls().trim().equalsIgnoreCase(headerCellValue)) {
									attributeMappingByHeaderColumnIndex.put(colIndex, attributeMapping);
									break;
								}
							}
						}
					} catch (InvalidXlsCellValueFormatException fie) {
						String cellValueForDisplayRaw = XlsCellValueObtainerBase.getValoareForDisplayRaw(formulaEvaluator, headerCell);
						addFormatValidationMessage(ColumnMapping.ColumnValueType.String.getLabel(), null, cellValueForDisplayRaw, headerRowIndex, colIndex, null);
					} catch (WrongXlsCellException wce) {
						String cellValueForDisplayRaw = XlsCellValueObtainerBase.getValoareForDisplayRaw(formulaEvaluator, headerCell);
						addWrongCellValidationMessage(cellValueForDisplayRaw, headerRowIndex, colIndex, null);
					}
				} else {
					break;
				}
			}
		}
		
		if (attributeMappingByHeaderColumnIndex.size() > 0) {
			if (attributeMappingByHeaderColumnIndex.size() < attributesMapping.size()) {
				StringBuilder missingColumns = new StringBuilder();
				for (ColumnMapping cm : attributesMapping) {	
					boolean found = false;
					for (Integer i : attributeMappingByHeaderColumnIndex.keySet()) {
						ColumnMapping cm2 = attributeMappingByHeaderColumnIndex.get(i);
						if (cm.getColumnNameXls().trim().equals(cm2.getColumnNameXls().trim())) {
							found = true;
						}
					}
					if (!found) {
						if (missingColumns.length() > 0) {
							missingColumns.append(",");
						}
						missingColumns.append(" " + cm.getColumnNameXls());
					}
				}
				messages.add(0, prepareMessage("The number of header columns " + attributeMappingByHeaderColumnIndex.size() + " is less than expected number " + attributesMapping.size() + ". Missing columns: " + missingColumns.toString()));
			}
		} else {
			messages.add(0, prepareMessage("The header was not found."));
		}	
	}
	
	private String prepareMessage(String message) {
		return "(Sheet: " + sheet.getSheetName() + ") -- " + message;
	}
	
	private void parseRows() {
		
		int rowIndexStart = headerRowIndex + 1;
		int rowIndex = rowIndexStart;
		
		while(true) {
			
			Row row = sheet.getRow(rowIndex);
			if (isRowFinal(row)) {
				break;
			}
			
			Object rowAsObject = null;
			try {
				rowAsObject = theClass.newInstance();
			} catch (Exception e) {
				throw new RuntimeException(e.getMessage(), e);
			}
			
			for (Integer colIndex : attributeMappingByHeaderColumnIndex.keySet()) {
				
				ColumnMapping attributeMapping = attributeMappingByHeaderColumnIndex.get(colIndex);
				
				Object valueAsObject = null;
				
				Cell cell = row.getCell(colIndex);				
				if (PoiXlsCellUtils.isNotBlankCell(cell)) {
					XlsCellValueObtainer obtainer = getXlsCellValueObtainerByAttributeMapping(attributeMapping);
					try {
						 valueAsObject = obtainer.getValue(formulaEvaluator, cell);
						 
						 if (valueAsObject != null) {						 
							 if (attributeMapping.columnValueType.equals(ColumnMapping.ColumnValueType.Integer)) {
								 if (valueAsObject instanceof Double) {
									 valueAsObject = ((Double) valueAsObject).intValue();
								 }
							 } else if (attributeMapping.columnValueType.equals(ColumnMapping.ColumnValueType.ListOfStrings)) {
								 if (valueAsObject instanceof String) {
									 String valueAsString = valueAsObject.toString();
									 if (StringUtils.isNotBlank(valueAsString)) {
										 String[] values = valueAsString.split(";");
										 valueAsObject = Arrays.asList(values);	
									 }
								 } else {
									 throw new RuntimeException("Invalid value for list of strings");
								 }
							 } else if (attributeMapping.columnValueType.equals(ColumnMapping.ColumnValueType.YesNo)) {
								 if (valueAsObject instanceof String) {
									 String valueAsString = valueAsObject.toString();
									 if (StringUtils.isNotBlank(valueAsString)) {
										 valueAsObject = YesNoValue.getByCode(valueAsString);			
									 }
								 } else {
									 throw new RuntimeException("Invalid value for 'yes-no' value");
								 }
							 } else if (attributeMapping.columnValueType.equals(ColumnMapping.ColumnValueType.PrioritateTask)) {
								 if (valueAsObject instanceof String) {
									 String valueAsString = valueAsObject.toString().trim();
									 if (StringUtils.isNotBlank(valueAsString)) {
										 valueAsObject = PrioritateTask.valueOf(valueAsString);			
									 }
								 } else {
									 throw new RuntimeException("Invalid value for 'prioritate task' value");
								 }
							 } else if (attributeMapping.columnValueType.equals(ColumnMapping.ColumnValueType.StatusTask)) {
								 if (valueAsObject instanceof String) {
									 String valueAsString = valueAsObject.toString().trim();									 if (StringUtils.isNotBlank(valueAsString)) {
										 valueAsObject = StatusTask.valueOf(valueAsString);			
									 }
								 } else {
									 throw new RuntimeException("Invalid value for 'status task' value");
								 }
							 } else if (attributeMapping.columnValueType.equals(ColumnMapping.ColumnValueType.DSPArieDeCuprindere)) {
								 if (valueAsObject instanceof String) {
									 String valueAsString = valueAsObject.toString().trim();
									 if (StringUtils.isNotBlank(valueAsString)) {
										 valueAsObject = DSPArieDeCuprindere.getByCode(valueAsString);			
									 }
								 } else {
									 throw new RuntimeException("Invalid value for 'arie de cuprindere' value");
								 }
							 } else if (attributeMapping.columnValueType.equals(ColumnMapping.ColumnValueType.DSPGradDeImportanta)) {
								 if (valueAsObject instanceof String) {
									 String valueAsString = valueAsObject.toString().trim();
									 if (StringUtils.isNotBlank(valueAsString)) {
										 valueAsObject = DSPGradDeImportanta.getByCode(valueAsString);			
									 }
								 } else {
									 throw new RuntimeException("Invalid value for 'grad de importanta' value");
								 }
							 } else if (attributeMapping.columnValueType.equals(ColumnMapping.ColumnValueType.DSPStatusProiect)) {
								 if (valueAsObject instanceof String) {
									 String valueAsString = valueAsObject.toString().trim();
									 if (StringUtils.isNotBlank(valueAsString)) {
										 valueAsObject = DSPStatusProiect.valueOf(valueAsString);			
									 }
								 } else {
									 throw new RuntimeException("Invalid value for 'status proiect' value");
								 }
							 } else if (attributeMapping.columnValueType.equals(ColumnMapping.ColumnValueType.String)) {
								 valueAsObject = valueAsObject.toString().trim();
							 }
						 }						 
					} catch (WrongXlsCellException e) {
						String cellValueForDisplayRaw = XlsCellValueObtainerBase.getValoareForDisplayRaw(formulaEvaluator, cell);
						addWrongCellValidationMessage(cellValueForDisplayRaw, rowIndex, colIndex, attributeMapping.getColumnNameXls());
					} catch (InvalidXlsCellValueFormatException e) {
						String cellValueForDisplayRaw = XlsCellValueObtainerBase.getValoareForDisplayRaw(formulaEvaluator, cell);
						addFormatValidationMessage(attributeMapping.getColumnValueType().getLabel(), attributeMapping.getColumnValueFormat(), cellValueForDisplayRaw, rowIndex, colIndex, attributeMapping.getColumnNameXls());
					} catch (Exception e) {
						addValidationMessage(e.getMessage(), rowIndex, colIndex, attributeMapping.getColumnNameXls());
					}
				}
				
				if (valueAsObject == null) {
					 valueAsObject = attributeMapping.getDefaultValue();
				 }
				
				if (attributeMapping.isRequired() && valueAsObject == null) {
					addRequiredCellValidationMessage(attributeMapping.getColumnNameXls(), rowIndex, colIndex);
				}
				
				if (valueAsObject != null) {
					try {
						BeanUtils.setProperty(rowAsObject, attributeMapping.getBeanPropertyName(), valueAsObject);
					} catch (Exception e) {
						prepareMessage(e.getMessage());
					}
				}
			}
			
			rows.add(rowAsObject);
			
			rowIndex++;
		}
	}
	
	private boolean isRowFinal(Row row) {
		if (row == null) {
			return true;
		}
		
		for (Integer colIndex : attributeMappingByHeaderColumnIndex.keySet()) {
			ColumnMapping attributeMapping = attributeMappingByHeaderColumnIndex.get(colIndex);
			Cell cell = row.getCell(colIndex);				
			if (PoiXlsCellUtils.isNotBlankCell(cell)) {
				XlsCellValueObtainer obtainer = getXlsCellValueObtainerByAttributeMapping(attributeMapping);
				try {
					 Object valueAsObject = obtainer.getValue(formulaEvaluator, cell);
					 if (valueAsObject != null && StringUtils.isNotBlank(valueAsObject.toString())) {
						 return false;
					 }
				} catch (Exception e) {
					throw new RuntimeException("isRowFinal? error: " + e.getMessage(), e);
				}
			}
		}
		return true;
	}
	
	private XlsCellValueObtainer getXlsCellValueObtainerByAttributeMapping(ColumnMapping attributeMapping) {
		if (ColumnMapping.ColumnValueType.String.equals(attributeMapping.getColumnValueType())
				|| ColumnMapping.ColumnValueType.DSPArieDeCuprindere.equals(attributeMapping.getColumnValueType())
				|| ColumnMapping.ColumnValueType.DSPGradDeImportanta.equals(attributeMapping.getColumnValueType())
				|| ColumnMapping.ColumnValueType.DSPStatusProiect.equals(attributeMapping.getColumnValueType())
				|| ColumnMapping.ColumnValueType.PrioritateTask.equals(attributeMapping.getColumnValueType())
				|| ColumnMapping.ColumnValueType.StatusTask.equals(attributeMapping.getColumnValueType())) {
			return new TextXlsCellValueObtainer();
		} else if (ColumnMapping.ColumnValueType.ListOfStrings.equals(attributeMapping.getColumnValueType())) {
			return new TextXlsCellValueObtainer();
		} else if (ColumnMapping.ColumnValueType.YesNo.equals(attributeMapping.getColumnValueType())) {
			return new TextXlsCellValueObtainer();
		} else if (ColumnMapping.ColumnValueType.Integer.equals(attributeMapping.getColumnValueType())) {
			return new NumberXlsCellValueObtainer();
		} else if (ColumnMapping.ColumnValueType.Number.equals(attributeMapping.getColumnValueType())) {
			return new NumberXlsCellValueObtainer();
		} else if (ColumnMapping.ColumnValueType.Date.equals(attributeMapping.getColumnValueType())) {
			List<String> dateFormats = null;
			if (StringUtils.isNotBlank(attributeMapping.getColumnValueFormat())) {
				String[] dateFormatsArray = attributeMapping.getColumnValueFormat().split(";");
				dateFormats = Arrays.asList(dateFormatsArray);
			}
			return new DateXlsCellValueObtainer(dateFormats);
		} else {
			throw new RuntimeException("Unknown column type [" + attributeMapping.getColumnValueType() + "].");
		}
	}
	
	private void addWrongCellValidationMessage(String cellValueRaw, int cellRow, int cellCol, String columnName) {
		addValidationMessage("Wrong cell with value [" + (cellValueRaw != null ? cellValueRaw : "") + "]", cellRow, cellCol, columnName);
	}
	
	private void addRequiredCellValidationMessage(String columnName, int cellRow, int cellCol) {
		addValidationMessage("Required value", cellRow, cellCol, columnName);
	}
	
	private void addFormatValidationMessage(String valueType, String valueFormat, String cellValueRaw, int cellRow, int cellCol, String columnName) {
		String validationMessage = "Invalid value [" + cellValueRaw +  "] for type " + valueType;
		if (valueFormat != null) {
			validationMessage += " and format " + StringUtils.replace(valueFormat, ";", " or ");
		}
		addValidationMessage(validationMessage, cellRow, cellCol, columnName);
	}
	
	private void addValidationMessage(String validationMessage, int cellRow, int cellCol, String columnName) {
		String position = PoiXlsCellUtils.getPositionAsString(cellRow, cellCol);		
		String message = prepareMessage("Cell " + position + (columnName != null ? " [" + columnName + "]" : "") + ": " + validationMessage + ".");
		messages.add(message);
	}
	
	public static class ColumnMapping {
		
		private String columnNameXls;
		private String propertyName;
		private ColumnValueType columnValueType;
		private String columnValueFormat;
		private boolean required;
		private Object defaultValue;
		
		public ColumnMapping(String columnNameXls, String propertyName, ColumnValueType columnValueType, String columnValueFormat, boolean required, Object defaultValue) {
			this.columnNameXls = columnNameXls;
			this.propertyName = propertyName;
			this.columnValueType = columnValueType;
			this.columnValueFormat = columnValueFormat;
			this.required = required;
			this.defaultValue = defaultValue;
		}
		
		public static enum ColumnValueType {
			
			String("Text"),
			Integer("Integer"),
			Number("Number"),
			ListOfStrings("ListOfStrings"),
			YesNo("YesNo"),
			Date("Date"),
			
			PrioritateTask("PrioritateTask"),
			StatusTask("StatusTask"),
			DSPArieDeCuprindere("DSPArieDeCuprindere"),
			DSPGradDeImportanta("DSPGradDeImportanta"),
			DSPStatusProiect("DSPStatusProiect");
			
			private final String label;
			
			private ColumnValueType(final String label) {
				this.label = label;
			}
			
			public String getLabel() {
				return label;
			}

			public static ColumnValueType getByLabel(String label) {
				for (ColumnValueType column : ColumnValueType.values()) {
					if (label.equals(column.getLabel())) {
						return column;
					}
				}
				
				throw new IllegalArgumentException("No column value type with label [" + label + "] exist in ColumnValueTypeEnum.");
			}
		}
		
		public String getBeanPropertyName() {			
			return this.propertyName; // StringUtils.uncapitalize(columnNameXls);
		}
		
		public String getPropertyName() {
			return propertyName;
		}
		
		public Object getDefaultValue() {
			return defaultValue;
		}

		public String getColumnNameXls() {
			return columnNameXls;
		}

		public void setColumnNameXls(String columnNameXls) {
			this.columnNameXls = columnNameXls;
		}

		public ColumnValueType getColumnValueType() {
			return columnValueType;
		}

		public void setColumnValueType(ColumnValueType columnValueType) {
			this.columnValueType = columnValueType;
		}

		public String getColumnValueFormat() {
			return columnValueFormat;
		}

		public void setColumnValueFormat(String columnValueFormat) {
			this.columnValueFormat = columnValueFormat;
		}
		
		public boolean isRequired() {
			return required;
		}
		
	}
	
	@SuppressWarnings("serial")
	private static class InvalidXlsCellValueFormatException extends Exception {
	}
	
	@SuppressWarnings("serial")
	public static class WrongXlsCellException extends Exception {
	}
	
	private static interface XlsCellValueObtainer {

		Object getValue(FormulaEvaluator formulaEvaluator, Cell cell) throws WrongXlsCellException, InvalidXlsCellValueFormatException;
	}
	
	private static abstract class XlsCellValueObtainerBase implements XlsCellValueObtainer {
		
		public static String getValoareForDisplayRaw(FormulaEvaluator formulaEvaluator, Cell rawCell) {
			
			String valueWhenBlank = "";
			
			if (rawCell == null) {
				return valueWhenBlank;
			}
			
		    Cell cell = formulaEvaluator.evaluateInCell(rawCell);
			
			int cellType = cell.getCellType();
			if (cellType == Cell.CELL_TYPE_BLANK) {
				return valueWhenBlank;
			} else if (cellType == Cell.CELL_TYPE_STRING) {
				return cell.getStringCellValue().trim();
			} else if (cellType == Cell.CELL_TYPE_NUMERIC) {
				double numericValue = cell.getNumericCellValue();
				cell.setCellType(Cell.CELL_TYPE_STRING);
				String stringValue = cell.getStringCellValue().trim();
				cell.setCellValue(numericValue);
				return stringValue;
			} else if (cellType == Cell.CELL_TYPE_ERROR) {
				return cell.toString();
			} else {
				// TODO Ce altceva as putea face?!
				throw new IllegalArgumentException("Tip necunoscut de celula (celula [" + rawCell.toString() + "]): [" + cellType + "]");
			}		
		}

		@Override
		public Object getValue(FormulaEvaluator formulaEvaluator, Cell cell) throws WrongXlsCellException, InvalidXlsCellValueFormatException {
			
			if (cell == null) {
				return null;
			}
			
		    Cell cellWithEvaluatedFormula;
			try {
				cellWithEvaluatedFormula = formulaEvaluator.evaluateInCell(cell);
			} catch (FormulaParseException fpe) {
			    throw new WrongXlsCellException();
			}
			if (cellWithEvaluatedFormula.getCellType() == Cell.CELL_TYPE_ERROR) {
				throw new WrongXlsCellException();
			}
			return getValue(cellWithEvaluatedFormula);
		}
		
		protected abstract Object getValue(Cell cell) throws InvalidXlsCellValueFormatException;
	}
	
	private static class DateXlsCellValueObtainer extends XlsCellValueObtainerBase {
		
		private static String DEFAULT_DATE_FORMAT = "dd.MM.yyyy";
		
		private final List<String> formats;
		
		public DateXlsCellValueObtainer(List<String> formats) {
			if (CollectionUtils.isEmpty(formats)) {
				this.formats = Lists.newArrayList(DEFAULT_DATE_FORMAT);
			} else {
				this.formats = formats;
			}
		}
		
		@Override
		protected Object getValue(Cell cell) throws InvalidXlsCellValueFormatException {
			int cellType = cell.getCellType();
			if (cellType == Cell.CELL_TYPE_BLANK) {
				return null;
			} else if ((cellType == Cell.CELL_TYPE_NUMERIC) && DateUtil.isCellDateFormatted(cell)) {
				return cell.getDateCellValue();
			} else if (cellType == Cell.CELL_TYPE_STRING) {
				String stringValue = cell.getStringCellValue().trim();
				if (StringUtils.isNotBlank(stringValue)) {
					return parseFromString(stringValue);
				} else {
					return null;
				}
			} else {
				throw new InvalidXlsCellValueFormatException();
			}
		}
		
		private Date parseFromString(String stringValue) throws InvalidXlsCellValueFormatException {
			for (String format : formats) {
				SimpleDateFormat dateFormatter = new StrictSimpleDateFormat(format);		
				try {
					return dateFormatter.parse(stringValue);
				} catch (ParseException e) {
					// nothing
				}
			}
			throw new InvalidXlsCellValueFormatException();
		}
	}
	
	private static class NumberXlsCellValueObtainer extends XlsCellValueObtainerBase {
		
		private static final char DECIMAL_SEPARATOR = ',';
		private static final char DECIMAL_SEPARATOR_OTHER = '.';
		private static final String NUMBER_FORMAT = "0" + DECIMAL_SEPARATOR + "############";
		private static final DecimalFormatSymbols SYMBOLS;
		static {
			SYMBOLS = new DecimalFormatSymbols();
		    // Trebuie sa inlocuiesc si separatorul miilor pt. ca ar putea fi exact cel care l-am setat pt. zecimale.
		    SYMBOLS.setGroupingSeparator(DECIMAL_SEPARATOR_OTHER);
			SYMBOLS.setDecimalSeparator(DECIMAL_SEPARATOR);
		}

		@Override
		protected Object getValue(Cell cell) throws InvalidXlsCellValueFormatException {
			int cellType = cell.getCellType();
			if (cellType == Cell.CELL_TYPE_BLANK) {
				return null;
			} else if (cellType == Cell.CELL_TYPE_NUMERIC) {
				return cell.getNumericCellValue();
			} else if (cellType == Cell.CELL_TYPE_STRING) {
				String stringValue = cell.getStringCellValue().trim();
				if (StringUtils.isNotBlank(stringValue)) {
					return parseFromString(stringValue);
				} else {
					return null;
				}
			} else {
				throw new InvalidXlsCellValueFormatException();
			}
		}
		
		private Double parseFromString(String stringValue) throws InvalidXlsCellValueFormatException {
			
			stringValue = stringValue.replace(DECIMAL_SEPARATOR_OTHER, DECIMAL_SEPARATOR);
			
			try {
				return NumberUtils.parseNumber(stringValue, NUMBER_FORMAT, SYMBOLS);
			} catch (ParseException e) {
				throw new InvalidXlsCellValueFormatException();
			}
		}
	}
	
	private static class StrictSimpleDateFormat extends SimpleDateFormat {
		
		public StrictSimpleDateFormat(String pattern) {
			super(pattern);
			setLenient(false);
		}

		@Override
		public Date parse(String dateAsString) throws ParseException {
			
			Date date = super.parse(dateAsString);
			
			String formattedDate = format(date);
			if (!dateAsString.equals(formattedDate)) {
				throw new ParseException("Valoarea [" + dateAsString + "] nu respecta formatul [" + toPattern() + "].", 0);
			}
			
			return date;
		}
	}
	
	private static class TextXlsCellValueObtainer extends XlsCellValueObtainerBase {

		@Override
		protected Object getValue(Cell cell) throws InvalidXlsCellValueFormatException {
			int cellType = cell.getCellType();
			if (cellType == Cell.CELL_TYPE_BLANK) {
				return null;
			} else if (cellType == Cell.CELL_TYPE_STRING) {
				return cell.getStringCellValue().trim();
			} else if (cellType == Cell.CELL_TYPE_NUMERIC) {
				double numericValue = cell.getNumericCellValue();
				cell.setCellType(Cell.CELL_TYPE_STRING);
				String stringValue = cell.getStringCellValue().trim();
				cell.setCellValue(numericValue);
				return stringValue;
			} else {
				throw new InvalidXlsCellValueFormatException();
			}
		}

	}

}
