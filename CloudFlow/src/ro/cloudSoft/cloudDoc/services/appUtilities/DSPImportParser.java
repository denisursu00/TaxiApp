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

public class DSPImportParser {
	
	private static final String SHEET_NAME_PROIECTE = "General";
	private static final String SHEET_NAME_COMISII_GL_IMPLICATE = "Comisii GL implicate";
	private static final String SHEET_NAME_PARTICIPANTI_LA_PROIECT = "Participanti la proiect";
	private static final String SHEET_NAME_TASKURI = "Task-uri";

	private static final String DATE_COLUMN_FORMATS = "dd.MM.yyyy;dd.MM.yyyy HH:mm:ss;dd/MM/yyyy;dd/MM/yyyy HH:mm:ss";
	
	private static List<ColumnMapping> proiectSheetColumnMapping = new ArrayList<>();
	static {
		proiectSheetColumnMapping.add(new ColumnMapping("Abreviere proiect *", "abreviereProiect", ColumnMapping.ColumnValueType.String, null, true, null));
		proiectSheetColumnMapping.add(new ColumnMapping("Nume proiect *", "numeProiect", ColumnMapping.ColumnValueType.String, null, true, null));
		proiectSheetColumnMapping.add(new ColumnMapping("Descriere", "descriere", ColumnMapping.ColumnValueType.String, null, false, null));
		proiectSheetColumnMapping.add(new ColumnMapping("Domeniu bancar *", "domeniuBancar", ColumnMapping.ColumnValueType.String, null, true, null));
		proiectSheetColumnMapping.add(new ColumnMapping("Proiect initiat de ARB *", "proiectInitiatARB", ColumnMapping.ColumnValueType.YesNo, null, true, null));
		proiectSheetColumnMapping.add(new ColumnMapping("Arie de cuprindere *", "arieDeCuprindere", ColumnMapping.ColumnValueType.DSPArieDeCuprindere, null, true, null));
		proiectSheetColumnMapping.add(new ColumnMapping("Proiect initiat de alta entitate", "proiectInitiatDeAltaEntitate", ColumnMapping.ColumnValueType.String, null, false, null));
		proiectSheetColumnMapping.add(new ColumnMapping("Data inceput proiect *", "dataInceput", ColumnMapping.ColumnValueType.Date, null, true, null));
		proiectSheetColumnMapping.add(new ColumnMapping("Data sfarsit proiect *", "dataSfarsit", ColumnMapping.ColumnValueType.Date, null, true, null));
		proiectSheetColumnMapping.add(new ColumnMapping("Data implementarii *", "dataImplementarii", ColumnMapping.ColumnValueType.Date, DATE_COLUMN_FORMATS, true, null));
		proiectSheetColumnMapping.add(new ColumnMapping("Evaluarea impactului *", "evaluareaImpactului", ColumnMapping.ColumnValueType.String, null, true, null));
		proiectSheetColumnMapping.add(new ColumnMapping("Incadrare proiect *", "incadrareProiect", ColumnMapping.ColumnValueType.String, null, true, null));
		proiectSheetColumnMapping.add(new ColumnMapping("Responsabil proiect *", "responsabilProiect", ColumnMapping.ColumnValueType.String, null, true, null));
		proiectSheetColumnMapping.add(new ColumnMapping("Grad importanta *", "gradImportanta", ColumnMapping.ColumnValueType.DSPGradDeImportanta, null, true, null));
		proiectSheetColumnMapping.add(new ColumnMapping("Autoritati implicate", "autoritatiImplicate", ColumnMapping.ColumnValueType.ListOfStrings, null, false, null));
		proiectSheetColumnMapping.add(new ColumnMapping("Obiective proiect *", "obiectiveProiect", ColumnMapping.ColumnValueType.String, null, true, null));
		proiectSheetColumnMapping.add(new ColumnMapping("Cadrul legal", "cadruLegal", ColumnMapping.ColumnValueType.String, null, false, null));
		proiectSheetColumnMapping.add(new ColumnMapping("Specificitate proiect *", "specificitateProiect", ColumnMapping.ColumnValueType.String, null, true, null));
		proiectSheetColumnMapping.add(new ColumnMapping("Status proiect *", "statusProiect", ColumnMapping.ColumnValueType.DSPStatusProiect, null, true, null));
		proiectSheetColumnMapping.add(new ColumnMapping("Estimare realizare proiect(procent) *", "estimareRealizareProiect", ColumnMapping.ColumnValueType.Integer, null, true, null));
	}
	
	private static List<ColumnMapping> participantiSheetColumnMapping = new ArrayList<>();
	static {
		participantiSheetColumnMapping.add(new ColumnMapping("Abreviere proiect *", "abreviereProiect", ColumnMapping.ColumnValueType.String, null, true, null));
		participantiSheetColumnMapping.add(new ColumnMapping("Participant proiect *", "participant", ColumnMapping.ColumnValueType.String, null, true, null));
	}
	
	private static List<ColumnMapping> comisiiSheetColumnMapping = new ArrayList<>();
	static {
		comisiiSheetColumnMapping.add(new ColumnMapping("Abreviere proiect *", "abreviereProiect", ColumnMapping.ColumnValueType.String, null, true, null));
		comisiiSheetColumnMapping.add(new ColumnMapping("Denumire Comisii/GL implicat *", "denumireComisieGL", ColumnMapping.ColumnValueType.String, null, true, null));
	}
	
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
	
	public DSPImportParser(String excelFilePath) {
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
	
	public DSPAllXlsModel parse() throws AppUtilitiesException {
		DSPAllXlsModel model = new DSPAllXlsModel();
		try {
			
			init();
			
			List<DSPProiectXlsModel> proiectSheetModels = (List<DSPProiectXlsModel>) parseSheet(SHEET_NAME_PROIECTE, DSPProiectXlsModel.class, proiectSheetColumnMapping);
			List<DSPComisieGLXlsModel> comisiiSheetModels = (List<DSPComisieGLXlsModel>) parseSheet(SHEET_NAME_COMISII_GL_IMPLICATE, DSPComisieGLXlsModel.class, comisiiSheetColumnMapping);
			List<DSPParticipantXlsModel> participantiSheetModels = (List<DSPParticipantXlsModel>) parseSheet(SHEET_NAME_PARTICIPANTI_LA_PROIECT, DSPParticipantXlsModel.class, participantiSheetColumnMapping);
			List<DSPTaskXlsModel> taskuriSheetModels = (List<DSPTaskXlsModel>) parseSheet(SHEET_NAME_TASKURI, DSPTaskXlsModel.class, taskuriSheetColumnMapping);
			
			model.setProiecte(proiectSheetModels);
			model.setComisiiGLImplicate(comisiiSheetModels);
			model.setParticipanti(participantiSheetModels);
			model.setTaskuri(taskuriSheetModels);
			
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