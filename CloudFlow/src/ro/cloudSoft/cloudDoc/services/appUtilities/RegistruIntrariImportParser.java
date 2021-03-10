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

public class RegistruIntrariImportParser {
	private static final String SHEET_NAME_ATASAMANTE = "Atasamente registru inrari";

	private static List<ColumnMapping> atasamenteSheetColumnMapping = new ArrayList<>();
	static {
		atasamenteSheetColumnMapping.add(new ColumnMapping("Numar inregistrare registru intrari*", "nrRegistruIntrari", ColumnMapping.ColumnValueType.String, null, true, null));
		atasamenteSheetColumnMapping.add(new ColumnMapping("Nume fisier *", "numeFisier", ColumnMapping.ColumnValueType.String, null, true, null));
	}
	
	private final String excelFilePath;	
	private Workbook workbook;
	private FormulaEvaluator formulaEvaluator;
	private List<String> errorMessages;
	
	public RegistruIntrariImportParser(String excelFilePath) {
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
	
	public RegistruIntrariAllXlsModel parse() throws AppUtilitiesException {
		RegistruIntrariAllXlsModel model = new RegistruIntrariAllXlsModel();
		try {
			
			init();
			List<RegistruIntrariAtasamentXlsModel> atasamenteSheetModels = (List<RegistruIntrariAtasamentXlsModel>) parseSheet(SHEET_NAME_ATASAMANTE, RegistruIntrariAtasamentXlsModel.class, atasamenteSheetColumnMapping);

			model.setAtasamente(atasamenteSheetModels);
			
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
