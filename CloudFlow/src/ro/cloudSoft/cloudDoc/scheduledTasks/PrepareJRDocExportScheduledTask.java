package ro.cloudSoft.cloudDoc.scheduledTasks;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.lowagie.text.Chunk;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;

import ro.cloudSoft.cloudDoc.core.AppException;
import ro.cloudSoft.cloudDoc.core.AppExceptionCodes;
import ro.cloudSoft.cloudDoc.core.constants.ParameterConstants;
import ro.cloudSoft.cloudDoc.core.constants.arb.ArbConstants;
import ro.cloudSoft.cloudDoc.dao.content.DocumentTypeDao;
import ro.cloudSoft.cloudDoc.dao.export_JR.ExportJRDao;
import ro.cloudSoft.cloudDoc.domain.bpm.MetadataWrapper;
import ro.cloudSoft.cloudDoc.domain.bpm.WorkflowState;
import ro.cloudSoft.cloudDoc.domain.content.Document;
import ro.cloudSoft.cloudDoc.domain.content.DocumentIdentifier;
import ro.cloudSoft.cloudDoc.domain.content.DocumentType;
import ro.cloudSoft.cloudDoc.domain.content.DocumentTypeTemplate;
import ro.cloudSoft.cloudDoc.domain.content.MetadataCollection;
import ro.cloudSoft.cloudDoc.domain.content.MetadataDefinition;
import ro.cloudSoft.cloudDoc.domain.content.MetadataInstance;
import ro.cloudSoft.cloudDoc.domain.content.TemplateType;
import ro.cloudSoft.cloudDoc.domain.export_jr.ExportAttachment;
import ro.cloudSoft.cloudDoc.domain.export_jr.ExportAttachment.EXPORT_ATTACHMENT_TYPE;
import ro.cloudSoft.cloudDoc.domain.export_jr.ExportDocument;
import ro.cloudSoft.cloudDoc.domain.export_jr.ExportMetadata;
import ro.cloudSoft.cloudDoc.domain.security.SecurityManager;
import ro.cloudSoft.cloudDoc.plugins.ExpressionEvaluator;
import ro.cloudSoft.cloudDoc.plugins.content.JR_DocumentLocationPlugin;
import ro.cloudSoft.cloudDoc.plugins.content.JR_DocumentPlugin;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.DownloadableFile;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.ExportType;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.content.DocumentModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.content.MetadataInstanceModel;
import ro.cloudSoft.cloudDoc.presentation.server.converters.content.DocumentConverter;
import ro.cloudSoft.cloudDoc.services.bpm.WorkflowService;
import ro.cloudSoft.cloudDoc.services.calendar.CalendarService;
import ro.cloudSoft.cloudDoc.services.content.DocumentService;
import ro.cloudSoft.cloudDoc.services.content.DocumentTypeService;
import ro.cloudSoft.cloudDoc.services.content.ExportDocumentDataHelper;
import ro.cloudSoft.cloudDoc.services.nomenclator.NomenclatorService;
import ro.cloudSoft.cloudDoc.services.organization.GroupService;
import ro.cloudSoft.cloudDoc.services.organization.UserService;
import ro.cloudSoft.cloudDoc.services.parameters.ParametersService;
import ro.cloudSoft.cloudDoc.services.project.ProjectService;
import ro.cloudSoft.cloudDoc.utils.DocumentTypeUtils;
import ro.cloudSoft.cloudDoc.utils.log.LogHelper;
import ro.cloudSoft.cloudDoc.utils.metadataValueFormatters.MetadataValueFormatterForDocumentExport;
import ro.cloudSoft.common.utils.JackRabbitUtils;
import ro.cloudSoft.common.utils.StringUtils2;

public class PrepareJRDocExportScheduledTask extends QuartzScheduledJobBean {

	private static final LogHelper LOGGER = LogHelper.getInstance(PrepareJRDocExportScheduledTask.class);
	
	public static final String WORKFLOW_SEPARATOR_STEPS = ";";
	
	public static final String FONT_PATH_DEJAVU_SANS = "ro/cloudSoft/cloudDoc/fonts/DejaVuSans.ttf";
	public static final String FONT_PATH_DEJAVU_SANS_BOLD = "ro/cloudSoft/cloudDoc/fonts/DejaVuSans-Bold.ttf";
	
	@Autowired
	private JR_DocumentLocationPlugin documentLocationPlugin;
	@Autowired
	private JR_DocumentPlugin documentPlugin;
	@Autowired
	private DocumentTypeDao documentTypeDao;
	@Autowired
	private ExportJRDao exportJRDao;
	@Autowired
	private UserService userService;
	@Autowired
	private NomenclatorService nomenclatorService;
	@Autowired
	private GroupService groupService;
	@Autowired
	private DocumentService documentService;
	@Autowired
	private DocumentTypeService documentTypeService;
	@Autowired
	private CalendarService calendarService;
	@Autowired
	private ProjectService projectService;
	@Autowired
	private ArbConstants arbConstants;	
	@Autowired
	private WorkflowService workflowService;
	@Autowired
	private ParametersService parametersService;

	private HashMap<Long, DocumentType> docTypeById;
	private SecurityManager userSecurity;

	@Override
	public void doExecuteInternal(JobExecutionContext jobExecutionContext) throws JobExecutionException {
		try {			
			
			loadInit();	
			
			prepareDocumentsToArchiving();			
			deleteArchivedDocuments();
			
		} catch (Exception e) {
			e.printStackTrace();
			throw new JobExecutionException(e);
		}
	}

	private void loadInit() throws Exception {
		docTypeById = new HashMap<Long, DocumentType>();
		List<DocumentType> allDocumentTypesLazy = documentTypeDao.getAllDocumentTypesLazy();
		allDocumentTypesLazy.forEach(docType -> {
			docTypeById.put(docType.getId(), docType);
		});
		String applicationUsername = getBusinessConstants().getApplicationUserName();
		userSecurity = getSecurityManagerFactory().getSecurityManager(applicationUsername);
	}
	
	private void prepareDocumentsToArchiving() {
		try {
			Integer nrAniPanaLaArhivare = this.parametersService.getParamaterValueAsIntegerByParameterName(ParameterConstants.PARAM_NAME_DOCUMENT_NR_DE_ANI_PANA_LA_ARHIVARE);
			Calendar dateLimitAsCalendar = Calendar.getInstance();
			dateLimitAsCalendar.add(Calendar.YEAR, (-1) * nrAniPanaLaArhivare);
			Date dateLimit = dateLimitAsCalendar.getTime();
			
			// Documente care au flux - se iau cele a carui flux s-a finalizat inainte de data din argument. 
			List<DocumentIdentifier> documents = exportJRDao.getAvailableDocumentsForExportBeforeWorkflowFinishedDate(dateLimit);
			if (CollectionUtils.isNotEmpty(documents)) {
				for (DocumentIdentifier documentIdentifier : documents) {
					Document document = documentService.getDocumentById(documentIdentifier.getDocumentId(), documentIdentifier.getDocumentLocationRealName());
					if (isDocumentArchivable(document)) {
						try {
							saveDocumentInExportTable(document);
						} catch (Exception e) {
							LOGGER.error("Eroare la salvare export document in tabel [" + documentIdentifier.getDocumentLocationRealName() + ", "+ documentIdentifier.getDocumentId() + "]", e, "export documente pt. arhivare");
						}
					}
				}
			}
			
			// Documente care NU au flux (importate sau fara flux definit) - astea se cauta in JR dupa data crearii.
			Set<String> allDocumentLocationRealNames = documentLocationPlugin.getAllDocumentLocationRealNames();
			List<DocumentIdentifier> noWorkflowDocuments = documentPlugin.getAvailableDocumentsForArchivingByNoWorkflowAndBeforeDate(allDocumentLocationRealNames, docTypeById.keySet(), dateLimit);
			if (CollectionUtils.isNotEmpty(noWorkflowDocuments)) {
				for (DocumentIdentifier documentIdentifier : noWorkflowDocuments) {
					if (!exportJRDao.existsDocument(documentIdentifier)) {
						Document document = documentService.getDocumentById(documentIdentifier.getDocumentId(), documentIdentifier.getDocumentLocationRealName());
						try {
							saveDocumentInExportTable(document);
						} catch (Exception e) {
							LOGGER.error("Eroare la salvare export document in tabel [" + documentIdentifier.getDocumentLocationRealName() + ", "+ documentIdentifier.getDocumentId() + "]", e, "export documente pt. arhivare");
						}
					}
				}
			}
		} catch (Exception e) {
			throw new RuntimeException("error while preparing documents to archiving", e);
		}
	}
	
	private void deleteArchivedDocuments() {
		try {
			List<ExportDocument> documents = exportJRDao.getAllArchivedAndNotDeleted();
			if (CollectionUtils.isNotEmpty(documents)) {
				for (ExportDocument ed : documents) {
					boolean deleted = false;
					try {
						documentPlugin.deleteDocument(new DocumentIdentifier(ed.getJrDocumentLocationRealName(), ed.getJrId()));
						deleted = true;
					} catch (AppException appe) {
						if (appe.getCode().equals(AppExceptionCodes.JR_ItemNotFoundException)) {
							LOGGER.error("Documentul din exportul cu id [" + ed.getId() + "]  nu a fost gasit in JR.", "deleteArchivedDocument");
							deleted = true;
						} else {
							LOGGER.error("Exceptie la stergerea documentului din exportul cu id [" + ed.getId() + "].", "deleteArchivedDocument");
						}
					}
					if (deleted) {
						try {
							ed.setJrDeleted(true);
							exportJRDao.save(ed);
						} catch (Exception e) {
							LOGGER.error("Document sters, exceptie la update export document cu id [" + ed.getId() + "].", "deleteArchivedDocument");
						}
					}
				}
			}			
		} catch (Exception e) {
			throw new RuntimeException("error while deleteting archived documents", e);
		}
	}
	
	private boolean isDocumentArchivable(Document document) {
		DocumentType docType = docTypeById.get(document.getDocumentTypeId());
		if (docType != null) {
			return docType.isArchivable();
		}
		return false;
	}
	
	@SuppressWarnings("unchecked")
	@Transactional
	private void saveDocumentInExportTable(Document doc) throws JobExecutionException {
		try {
			DocumentType docType = docTypeById.get(doc.getDocumentTypeId());
			ExportDocument exportDoc = new ExportDocument();
			exportDoc.setJrId(doc.getId());
			exportDoc.setJrDocumentLocationRealName(doc.getDocumentLocationRealName());
			exportDoc.setType(docType.getName());
			exportDoc.setName(doc.getName());
			exportDoc.setDescription(doc.getDescription());
			exportDoc.setArchived(false);
			exportDoc.setJrDeleted(false);
			exportDoc.setAttachments(new ArrayList<>());
			exportDoc.setMetadatas(new ArrayList<>());

			MetadataValueFormatterForDocumentExport metadataValueFormatter = new MetadataValueFormatterForDocumentExport(userService, nomenclatorService, groupService, documentService, calendarService, projectService);
			Map<String, Object> metdataTableModel = null;
			metdataTableModel = ExportDocumentDataHelper.buildExportMap(doc, docType, metadataValueFormatter, TemplateType.EXPORT_TO_IARCHIVE_TABLE, arbConstants);
			addComputedValuesForSpecificDocumentType(metdataTableModel, doc, docType, arbConstants);
			for (MetadataDefinition md : docType.getMetadataDefinitions()) {
				if (metdataTableModel.containsKey(md.getName())) {
					Object value = metdataTableModel.get(md.getName());
					if (value != null && value instanceof String) {
						exportDoc.getMetadatas().add(new ExportMetadata(exportDoc, md.getName(), (String)value));
					}
				}
			}
			
			// Caz specific (de preluat o metadta din valorile unei metadate colectie).
			if (docType.getName().equals(arbConstants.getDocumentMinutaSedintaComisieGLConstants().getDocumentTypeName())) {					
				String comisiiGlMetadataName = arbConstants.getDocumentMinutaSedintaComisieGLConstants().getComisiiGlMetadataName();
				Object value = metdataTableModel.get(comisiiGlMetadataName);
				if (value != null) {						
					List<String> values = new ArrayList<>();
					List<Map<String, Object>> valueList = (List<Map<String, Object>>) value;
					for (Map<String, Object> valueFromList : valueList) {
						Object theValue = valueFromList.get(arbConstants.getDocumentMinutaSedintaComisieGLConstants().getNumeComisieGlOfComisiiGlMetadataName());
						if (theValue != null && StringUtils.isNotBlank(theValue.toString())) {
							values.add(theValue.toString());
						}
					}
					if (CollectionUtils.isNotEmpty(values)) {
						String finalValue = "";
						if (values.size() == 1) {
							finalValue = values.get(0);
						} else {
							for (String val : values) {
								if (finalValue.length() > 0) {
									finalValue += ", ";
								}
								finalValue += "[" + val + "]";
							}
						}
						exportDoc.getMetadatas().add(new ExportMetadata(exportDoc, "denumire_comisie_gl", finalValue));
					}
				}
			}
			
			Map<String, Object> metadataDocumentModel = ExportDocumentDataHelper.buildExportMap(doc, docType, metadataValueFormatter, TemplateType.EXPORT_TO_IARCHIVE_DOCUMENT_METADATA, arbConstants);
			if (metadataDocumentModel != null) {
				byte[] metadataPdfContent = generateMetadataPDF(docType, doc, metadataDocumentModel);
				exportDoc.getAttachments().add(new ExportAttachment(exportDoc, "Valori metadate.pdf", metadataPdfContent, EXPORT_ATTACHMENT_TYPE.METADATA));
			}
			
			Map<String, MetadataWrapper> metadataWrapperByMetadataName = new HashMap<String, MetadataWrapper>(); 
			for (MetadataInstance metadataInstanceModel : doc.getMetadataInstanceList()) {
				MetadataDefinition metadataDefinition = DocumentTypeUtils.getMetadataDefinitionById(docType, metadataInstanceModel.getMetadataDefinitionId());
				metadataWrapperByMetadataName.put(metadataDefinition.getName(), new MetadataWrapper(metadataDefinition.getMetadataType(), metadataInstanceModel.getValues()));
			}
			
			List<DocumentTypeTemplate> templates = documentTypeService.getTemplates(docType.getId());
			for (DocumentTypeTemplate template : templates) {
				if (StringUtils.isNotBlank(template.getExportAvailabilityExpression())) {
	    			boolean available = ExpressionEvaluator.evaluateDocumentExpression(template.getExportAvailabilityExpression(), metadataWrapperByMetadataName);
	    			if (!available) {
	    				continue;
	    			}
	    		}		
				DownloadableFile templateDF = documentService.exportDocument(doc.getDocumentLocationRealName(), doc.getId(), template.getName(), ExportType.DOCX, userSecurity, true);
				exportDoc.getAttachments().add(new ExportAttachment(exportDoc, templateDF.getFileName(), templateDF.getContent(), EXPORT_ATTACHMENT_TYPE.XDOC_TEMPPLATE));
			}
			
			for (String attachmentName : doc.getAttachmentNames()) {
				DownloadableFile attachmentDF = documentService.getDocumentAttachmentAsDownloadableFile(doc.getDocumentLocationRealName(), doc.getId(), attachmentName, null, userSecurity);
				exportDoc.getAttachments().add(new ExportAttachment(exportDoc, attachmentDF.getFileName(), attachmentDF.getContent(), EXPORT_ATTACHMENT_TYPE.ATTACHMENT));
			}

			exportJRDao.save(exportDoc);
			
		} catch (Exception e) {
			e.printStackTrace();
			throw new JobExecutionException(e);
		}
	}
	
	private void addComputedValuesForSpecificDocumentType(Map<String, Object> exportData, Document document, DocumentType documentType, ArbConstants arbConstants) {
		if (documentType.getName().equals(arbConstants.getDocumentMinutaSedintaComisieGLConstants().getDocumentTypeName())) {
			List<MetadataInstance> metadataInstanceList = document.getMetadataInstanceList();
			String docPrezentaMtdName = arbConstants.getDocumentMinutaSedintaComisieGLConstants().getDenumireDocumentPrezentaComisieGlMetadataName();
			
			String documentLocationRealNameAndDocumentId = (String) JackRabbitUtils.getMetadataValue(document, documentType, docPrezentaMtdName);
			
			String[] documentLocationRealNameAndDocumentIdArray = StringUtils
					.splitByWholeSeparator(documentLocationRealNameAndDocumentId, ":");
			Document docPrezenta;
			try {
				docPrezenta = documentService.getDocumentForAutomaticAction(documentLocationRealNameAndDocumentIdArray[0],
						documentLocationRealNameAndDocumentIdArray[1]);
			} catch (AppException e) {
				throw new RuntimeException(e);
			}
			
			DocumentType docTypePrezenta = documentTypeService.getDocumentTypeByName(arbConstants.getDocumentPrezentaComisieGlConstants().getDocumentTypeName());
			String denComisieGlPrezentaMtdName = arbConstants.getDocumentMinutaSedintaComisieGLConstants().getDenumireDocumentPrezentaComisieGlMetadataName();
			
			if (docPrezenta != null) {
				String denumireComisieGlFromPrezenta = (String) JackRabbitUtils.getMetadataValue(docPrezenta, docTypePrezenta, denComisieGlPrezentaMtdName);
				exportData.put(arbConstants.getDocumentMinutaSedintaComisieGLConstants().getDenumireComisieGlMetadataName(), denumireComisieGlFromPrezenta);
			}
		}
		
	}

	private byte[] generateMetadataPDF(DocumentType docType, Document doc, Map<String, Object> model) {
		try {
			
			BaseFont normalBaseFont = BaseFont.createFont(FONT_PATH_DEJAVU_SANS, BaseFont.IDENTITY_H, true);
			Font normalFont = new Font(normalBaseFont, 10);
			BaseFont boldBaseFont = BaseFont.createFont(FONT_PATH_DEJAVU_SANS_BOLD, BaseFont.IDENTITY_H, true);
			Font boldFont = new Font(boldBaseFont, 10);
			Font titleFont = new Font(boldBaseFont, 14);
			
			com.lowagie.text.Document document = new com.lowagie.text.Document();
			
			ByteArrayOutputStream os = new ByteArrayOutputStream();
			PdfWriter.getInstance(document, os);
			document.open();
			Paragraph titleP = new Paragraph(doc.getName(), titleFont);
			titleP.setAlignment(Element.ALIGN_CENTER);
			document.add(titleP);
			document.add(new Paragraph(" "));
			
			WorkflowState workflowState = null;
			if (doc.getWorkflowStateId() != null) {
				workflowState = workflowService.getWorkflowStateById(doc.getWorkflowStateId());
			}
			
			List<Object> availablefMetadatasAndCollections = new ArrayList<>();
			for (MetadataDefinition md : docType.getMetadataDefinitions()) {
				if (isMetadataAvailableForExport(md, workflowState)) {
					availablefMetadatasAndCollections.add(md);
				}
			}
			if (CollectionUtils.isNotEmpty(docType.getMetadataCollections())) {
				for (MetadataCollection mc : docType.getMetadataCollections()) {
					if (isMetadataCollectionAvailableForExport(mc, workflowState)) {
						availablefMetadatasAndCollections.add(mc);
					}
				}
			}		
			Collections.sort(availablefMetadatasAndCollections, new Comparator<Object>() {
				
				@Override
				public int compare(Object o1, Object o2) {
					
					Integer orderNumberO1 = 0;					
					if (o1 instanceof MetadataDefinition) {
						orderNumberO1 = ((MetadataDefinition) o1).getOrderNumber();
					}
					if (o1 instanceof MetadataCollection) {
						orderNumberO1 = ((MetadataCollection) o1).getOrderNumber();
					}
					
					Integer orderNumberO2 = 0;
					if (o2 instanceof MetadataDefinition) {
						orderNumberO2 = ((MetadataDefinition) o2).getOrderNumber();
					}
					if (o2 instanceof MetadataCollection) {
						orderNumberO2 = ((MetadataCollection) o2).getOrderNumber();
					}
					
					return orderNumberO1.compareTo(orderNumberO2);
				}
			});
			
			for (Object metadataOrCollection : availablefMetadatasAndCollections) {
				
				if (metadataOrCollection instanceof MetadataDefinition) {
					
					String labelOfMetadata = ((MetadataDefinition) metadataOrCollection).getLabel();
					Object modelValue = model.get(((MetadataDefinition) metadataOrCollection).getName());
					
					Paragraph labelAndValue = new Paragraph(labelOfMetadata + ": ", boldFont);
					Chunk value = new Chunk(StringUtils2.getStringValueOfObject(modelValue), normalFont);
					labelAndValue.add(value);
					document.add(labelAndValue);
				
				} else if (metadataOrCollection instanceof MetadataCollection) {
					
					document.add(new Paragraph(" ")); // pt spatiu linie
					
					List<MetadataDefinition> orderedMetadatas = new ArrayList<>();
					orderedMetadatas.addAll(((MetadataCollection) metadataOrCollection).getMetadataDefinitions());
					orderedMetadatas.sort(new Comparator<MetadataDefinition>() {
						@Override
						public int compare(MetadataDefinition arg0, MetadataDefinition arg1) {
							return Integer.valueOf(arg0.getOrderNumber()).compareTo(arg1.getOrderNumber());
						}
					});
					
					String labelCollection = ((MetadataCollection) metadataOrCollection).getLabel();
					Object modelValue = model.get(((MetadataCollection) metadataOrCollection).getName());
					
					PdfPTable table = new PdfPTable(orderedMetadatas.size() + 1);
					table.setWidthPercentage(100);
					
					PdfPCell titleCell = new PdfPCell(new Phrase(labelCollection, boldFont));
					titleCell.setHorizontalAlignment(Element.ALIGN_CENTER);
					titleCell.setColspan(orderedMetadatas.size() + 1);
					table.addCell(titleCell);
					
					PdfPCell headerCellLabel = new PdfPCell(new Phrase(ExportDocumentDataHelper.NR_CRT_FIELD_LABEL, boldFont));
					headerCellLabel.setHorizontalAlignment(Element.ALIGN_CENTER);
					table.addCell(headerCellLabel);
					orderedMetadatas.forEach(header -> {
						PdfPCell headerCell = new PdfPCell(new Phrase(header.getLabel(), boldFont));
						headerCell.setHorizontalAlignment(Element.ALIGN_CENTER);
						table.addCell(headerCell);
					});
					table.setHeaderRows(1);
					
					if (modelValue != null) {
						
						List<Map<String, String>> listMap = (List) modelValue;
						int nrCrt=1;
						for (Map<String, String> map : listMap) {
							Map<String, String> parentMap = map;
							table.addCell(new PdfPCell(new Phrase(String.valueOf(nrCrt++), normalFont)));
							orderedMetadatas.forEach(metadataDefinition -> {
								table.addCell(new PdfPCell(new Phrase(StringUtils2.getStringValueOfObject(parentMap.get(metadataDefinition.getName())), normalFont)));
							});
						}
						
						document.add(table);
						
					} else {
						
						PdfPCell noDataCell = new PdfPCell(new Phrase("(nicio valoare adaugata)", normalFont));
						noDataCell.setHorizontalAlignment(Element.ALIGN_CENTER);
						noDataCell.setColspan(orderedMetadatas.size() + 1);
						table.addCell(noDataCell);
						
						document.add(table);
					}					
				} else {
					throw new RuntimeException("unknown type of object [" + metadataOrCollection.getClass().getName() + "]");
				}				
			}
			
			document.close();

			return os.toByteArray();

		} catch (Exception e) {
			throw new RuntimeException("eroare la generare pdf cu valori metadate", e);
		}
	}
	
	private boolean isMetadataAvailableForExport(MetadataDefinition md, WorkflowState workflowState) {
		if (!md.isInvisible()) {
			return true;
		}
		if (workflowState == null) {
			return true;
		}
		if (StringUtils.isBlank(md.getInvisibleInStates())) {
			return false;
		}
		return !isStateFound(md.getInvisibleInStates(), workflowState.getCode());
	}
	
	private boolean isMetadataCollectionAvailableForExport(MetadataCollection mc, WorkflowState workflowState) {
		if (!mc.isInvisible()) {
			return true;
		}
		if (workflowState == null) {
			return true;
		}
		if (StringUtils.isBlank(mc.getInvisibleInStates())) {
			return false;
		}
		return !isStateFound(mc.getInvisibleInStates(), workflowState.getCode());
	}
	
	public static boolean isStateFound(String stateCodesJoinedAsString, String codeForStateToFind) {
		String normalizedStateCodesJoinedAsString = (WORKFLOW_SEPARATOR_STEPS + stateCodesJoinedAsString + WORKFLOW_SEPARATOR_STEPS);
		return normalizedStateCodesJoinedAsString.contains(WORKFLOW_SEPARATOR_STEPS + codeForStateToFind + WORKFLOW_SEPARATOR_STEPS);
	}
	
	public void setDocumentLocationPlugin(JR_DocumentLocationPlugin jrDocumentLocationPlugin) {
		this.documentLocationPlugin = jrDocumentLocationPlugin;
	}

	public void setDocumentPlugin(JR_DocumentPlugin jr_DocumentPlugin) {
		this.documentPlugin = jr_DocumentPlugin;
	}

	public void setDocumentTypeDao(DocumentTypeDao documentTypeDao) {
		this.documentTypeDao = documentTypeDao;
	}

	public void setExportJRDao(ExportJRDao exportJRDao) {
		this.exportJRDao = exportJRDao;
	}

	public void setUserService(UserService userService) {
		this.userService = userService;
	}

	public void setNomenclatorService(NomenclatorService nomenclatorService) {
		this.nomenclatorService = nomenclatorService;
	}

	public void setGroupService(GroupService groupService) {
		this.groupService = groupService;
	}

	public void setDocumentService(DocumentService documentService) {
		this.documentService = documentService;
	}

	public void setDocumentTypeService(DocumentTypeService documentTypeService) {
		this.documentTypeService = documentTypeService;
	}

	public void setCalendarService(CalendarService calendarService) {
		this.calendarService = calendarService;
	}

	public void setProjectService(ProjectService projectService) {
		this.projectService = projectService;
	}

	public void setArbConstants(ArbConstants arbConstants) {
		this.arbConstants = arbConstants;
	}
	
	public void setWorkflowService(WorkflowService workflowService) {
		this.workflowService = workflowService;
	}
	
	public void setParametersService(ParametersService parametersService) {
		this.parametersService = parametersService;
	}
}
