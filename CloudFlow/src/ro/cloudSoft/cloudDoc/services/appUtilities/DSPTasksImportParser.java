package ro.cloudSoft.cloudDoc.services.appUtilities;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

import com.google.common.collect.Lists;

import ro.cloudSoft.cloudDoc.services.appUtilities.ImportSheetParser.ColumnMapping;

public class DSPTasksImportParser {
	
	private static final String SHEET_NAME_TASKURI = "Task-uri";

	private static final String DATE_COLUMN_FORMATS = "dd.MM.yyyy;dd.MM.yyyy HH:mm:ss;dd/MM/yyyy;dd/MM/yyyy HH:mm:ss";
	
	private static List<ColumnMapping> taskuriSheetColumnMapping = new ArrayList<>();
	static {
		taskuriSheetColumnMapping.add(new ColumnMapping("Abreviere proiect *", "abreviereProiect", ColumnMapping.ColumnValueType.String, null, true, null));
		taskuriSheetColumnMapping.add(new ColumnMapping("Nume task *", "numeTask", ColumnMapping.ColumnValueType.String, null, true, null));
		taskuriSheetColumnMapping.add(new ColumnMapping("Descriere", "descriere", ColumnMapping.ColumnValueType.String, null, false, null));
		taskuriSheetColumnMapping.add(new ColumnMapping("Prioritate *", "prioritate", ColumnMapping.ColumnValueType.PrioritateTask, null, true, null));
		taskuriSheetColumnMapping.add(new ColumnMapping("Data inceput task *", "dataInceput", ColumnMapping.ColumnValueType.Date, null, true, null));
		taskuriSheetColumnMapping.add(new ColumnMapping("Data sfarsit task *", "dataSfarsit", ColumnMapping.ColumnValueType.Date, null, true, null));
		taskuriSheetColumnMapping.add(new ColumnMapping("Responsabil activitate *", "responsabilActivitate", ColumnMapping.ColumnValueType.String, null, true, null));
		taskuriSheetColumnMapping.add(new ColumnMapping("Participare la", "participareLa", ColumnMapping.ColumnValueType.String, null, false, null));
		taskuriSheetColumnMapping.add(new ColumnMapping("Explicatii", "explicatii", ColumnMapping.ColumnValueType.String, null, false, null));
		taskuriSheetColumnMapping.add(new ColumnMapping("Status task *", "status", ColumnMapping.ColumnValueType.StatusTask, null, true, null));
		taskuriSheetColumnMapping.add(new ColumnMapping("Data finalizare task", "dataFinalizare", ColumnMapping.ColumnValueType.Date, null, false, null));
		taskuriSheetColumnMapping.add(new ColumnMapping("Nume fisier atasat", "numeAtasamente", ColumnMapping.ColumnValueType.ListOfStrings, null, false, null));
	}
	
	private final String excelFilePath;	
	private Workbook workbook;
	private FormulaEvaluator formulaEvaluator;
	private List<String> errorMessages;
	
	public DSPTasksImportParser(String excelFilePath) {
		this.excelFilePath = excelFilePath;
		this.errorMessages = new ArrayList<>();
	}
	
	private void init() {
		try {
			
			File excelFile = new File(excelFilePath);
			if (!excelFile.exists()) {
				throw new RuntimeException("Fisierul excel specificat la calea [" + excelFilePath + "] nu exista.");
			}
			workbook = WorkbookFactory.create(excelFile);
			
			formulaEvaluator = workbook.getCreationHelper().createFormulaEvaluator();			
			
		} catch (InvalidFormatException e) {
			throw new RuntimeException("Invalid XLS file format", e);
		} catch (IOException e) {
			throw new RuntimeException("Exception during reading XLS file", e);
		}
	}
	
	public List<DSPTaskXlsModel> parse() throws AppUtilitiesException {
		List<DSPTaskXlsModel> model = new ArrayList<DSPTaskXlsModel>();
		try {
			
			init();
			
			model = (List<DSPTaskXlsModel>) parseSheet(SHEET_NAME_TASKURI, DSPTaskXlsModel.class, taskuriSheetColumnMapping);	
			if (areMessages()) {
				throw new AppUtilitiesException(errorMessages);
			}			
		} catch (AppUtilitiesException ve) {
			throw ve;
		} catch (Exception e) {
			throw new AppUtilitiesException(e.getMessage());
		} finally {
			try {
				if (workbook != null) {
					workbook.close();
				}				
			} catch (IOException e) {
				e.printStackTrace();
			}
			
		}
		return model;
	}
	
	private boolean areMessages() {
		return CollectionUtils.isNotEmpty(errorMessages);
	}
	
	private List parseSheet(String sheetName, Class theClass, List<ColumnMapping> sheetColumnMapping) {
		List rows = Lists.newArrayList();
		try {
			Sheet sheet = workbook.getSheet(sheetName);
			ImportSheetParser sheetParser = new ImportSheetParser(sheet, formulaEvaluator, theClass, sheetColumnMapping);
			rows = sheetParser.parse();
		} catch (AppUtilitiesException ve) {
			errorMessages.addAll(ve.getErrorMessages());
		} catch (Exception e) {
			e.printStackTrace();
			errorMessages.add(e.getMessage());
		}
		return rows; 
	}
}