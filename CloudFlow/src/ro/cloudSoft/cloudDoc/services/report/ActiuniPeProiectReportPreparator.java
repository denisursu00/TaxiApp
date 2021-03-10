package ro.cloudSoft.cloudDoc.services.report;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;

import ro.cloudSoft.cloudDoc.core.constants.arb.ArbConstants;
import ro.cloudSoft.cloudDoc.dao.project.ProjectDao;
import ro.cloudSoft.cloudDoc.domain.content.Document;
import ro.cloudSoft.cloudDoc.domain.content.DocumentType;
import ro.cloudSoft.cloudDoc.domain.project.Project;
import ro.cloudSoft.cloudDoc.domain.project.Task;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.report.ActiuniPeProiectNotaCDReportModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.report.ActiuniPeProiectRegistruIntrariIesiriReportModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.report.ActiuniPeProiectReportFilterModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.report.ActiuniPeProiectReportModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.report.ActiuniPeProiectTaskReportModel;
import ro.cloudSoft.cloudDoc.services.content.DocumentTypeService;
import ro.cloudSoft.cloudDoc.utils.DocumentUtils;

public class ActiuniPeProiectReportPreparator {
	
	private DocumentTypeService documentTypeService;
	private ArbConstants arbConstants;
	private ProjectDao projectDao;
	
	private List<Task> tasks;
	private List<ActiuniPeProiectRegistruIntrariIesiriReportModel> intrari;
	private List<ActiuniPeProiectRegistruIntrariIesiriReportModel> iesiri;
	private List<Document> noteCdDocuments;
	
	private DocumentType notaCDConsilieriFinaciariDasDocumentType;
	private DocumentType notaCDDirectoriDocumentType;
	private DocumentType notaCDResponsabilDocumentType;
	private DocumentType notaCDUseriARBDocumentType;
	
	private ActiuniPeProiectReportFilterModel filter;
	
	private Map<String, String> projectNameById = new HashMap<>();
	
	public ActiuniPeProiectReportPreparator(DocumentTypeService documentTypeService, ArbConstants arbConstants, ProjectDao projectDao,
			List<Task> tasks, List<ActiuniPeProiectRegistruIntrariIesiriReportModel> intrari, 
			List<ActiuniPeProiectRegistruIntrariIesiriReportModel> iesiri, List<Document> noteCdDocuments, 
			ActiuniPeProiectReportFilterModel filter) {
		this.documentTypeService = documentTypeService;
		this.arbConstants = arbConstants;
		this.projectDao = projectDao;
		this.tasks = tasks;
		this.intrari = intrari;
		this.iesiri = iesiri;
		this.noteCdDocuments = noteCdDocuments;
		this.filter = filter;
	}
	
	private void initContext() {
		
		this.notaCDConsilieriFinaciariDasDocumentType = documentTypeService.getDocumentTypeByName(arbConstants.getDocumentNotaCDConsilieriFinanciariBancariDASConstants().getDocumentTypeName());
		this.notaCDDirectoriDocumentType = documentTypeService.getDocumentTypeByName(arbConstants.getDocumentNotaCDDirectoriConstants().getDocumentTypeName());
		this.notaCDResponsabilDocumentType = documentTypeService.getDocumentTypeByName(arbConstants.getDocumentNotaCDResponsabilConstants().getDocumentTypeName());
		this.notaCDUseriARBDocumentType = documentTypeService.getDocumentTypeByName(arbConstants.getDocumentNotaCDUseriARBConstants().getDocumentTypeName());
		
		List<Project> projects = projectDao.getAll();
		for (Project project : projects) {
			projectNameById.put(project.getId().toString(), project.getName());
		}
	}
	
	public ActiuniPeProiectReportModel prepare() {
		
		this.initContext();
		
		ActiuniPeProiectReportModel report = new ActiuniPeProiectReportModel();
		List<ActiuniPeProiectTaskReportModel> rowsTask = new ArrayList<>();
		List<ActiuniPeProiectRegistruIntrariIesiriReportModel> rowsRegistruIntrariIesiri = new ArrayList<>();
		List<ActiuniPeProiectNotaCDReportModel> rowsNotaCD = new ArrayList<>();
		
		for (Task task : tasks) {
			ActiuniPeProiectTaskReportModel actiuniPeProiectTaskReportModel = new ActiuniPeProiectTaskReportModel();

			actiuniPeProiectTaskReportModel.setNumeProiect(task.getProject().getName());
			actiuniPeProiectTaskReportModel.setNumeSubproiect(task.getSubactivity() != null ? task.getSubactivity().getName() : null);
			actiuniPeProiectTaskReportModel.setTipActiune(task.getParticipationsTo());
			actiuniPeProiectTaskReportModel.setActiuni(task.getName());
			actiuniPeProiectTaskReportModel.setDataInceputTask(task.getStartDate());
			actiuniPeProiectTaskReportModel.setStatus(task.getStatus().toString());

			rowsTask.add(actiuniPeProiectTaskReportModel);
		}

		for (ActiuniPeProiectRegistruIntrariIesiriReportModel intrare : intrari) {
			String numarInregistrarePerAn = intrare.getNumarDeInregistrare();
			String[] numarInregistrareSplitArray = numarInregistrarePerAn.split("/");

			intrare.setNumarDeInregistrare("IN/" + numarInregistrareSplitArray[0]);
			
			rowsRegistruIntrariIesiri.add(intrare);
		}

		for (ActiuniPeProiectRegistruIntrariIesiriReportModel iesire : iesiri) {
			String numarInregistrarePerAn = iesire.getNumarDeInregistrare();
			String[] numarInregistrareSplitArray = numarInregistrarePerAn.split("/");

			iesire.setNumarDeInregistrare("IE/" + numarInregistrareSplitArray[0]);
			
			rowsRegistruIntrariIesiri.add(iesire);
		}
		
		if (CollectionUtils.isNotEmpty(noteCdDocuments)) {
			for (Document notaCdDocument : noteCdDocuments) {
				rowsNotaCD.addAll(prepareNoteCDReportRows(notaCdDocument));
			}
		}
		
		report.setActiuniPeProiectRegistruIntrariIesiri(rowsRegistruIntrariIesiri);
		report.setActiuniPeProiectTask(rowsTask);
		report.setActiuniPeProiectNotaCD(rowsNotaCD);
		
		sortReportData(report);
		
		return report;
	}
	
	private void sortReportData(ActiuniPeProiectReportModel report) {
		Collections.sort(report.getActiuniPeProiectNotaCD(), new Comparator<ActiuniPeProiectNotaCDReportModel>() {
			
			@Override
			public int compare(ActiuniPeProiectNotaCDReportModel o1, ActiuniPeProiectNotaCDReportModel o2) {
				return o1.getTipNota().compareTo(o2.getTipNota());
			}
		});
	}
	
	private List<ActiuniPeProiectNotaCDReportModel> prepareNoteCDReportRows(Document documentNotaCD) {
		
		List<ActiuniPeProiectNotaCDReportModel> notaCDs = new ArrayList<ActiuniPeProiectNotaCDReportModel>();
		
		DocumentType notaCDDocumentType = null;
		String dataCdArbMetadataName = null;
		String denumireProiectMetadataName = null;
		String tipNotaCdMetadataName = null;
		String subiectSpreAprobareMetadataName = null;
		
		if (documentNotaCD.getDocumentTypeId().equals(this.notaCDConsilieriFinaciariDasDocumentType.getId())) {
			
			notaCDDocumentType = this.notaCDConsilieriFinaciariDasDocumentType;
			
			dataCdArbMetadataName = arbConstants.getDocumentNotaCDConsilieriFinanciariBancariDASConstants().getDataCdArbMetadataName();
			denumireProiectMetadataName = arbConstants.getDocumentNotaCDConsilieriFinanciariBancariDASConstants().getDenumireProiectMetadataName();
			tipNotaCdMetadataName = arbConstants.getDocumentNotaCDConsilieriFinanciariBancariDASConstants().getTipNotaCdMetadataName();
			subiectSpreAprobareMetadataName = arbConstants.getDocumentNotaCDConsilieriFinanciariBancariDASConstants().getSubiectSpreAprobareMetadataName();
		
		} else if (documentNotaCD.getDocumentTypeId().equals(this.notaCDDirectoriDocumentType.getId())) {
			
			notaCDDocumentType = this.notaCDDirectoriDocumentType;
			
			dataCdArbMetadataName = arbConstants.getDocumentNotaCDDirectoriConstants().getDataCdArbMetadataName();
			denumireProiectMetadataName = arbConstants.getDocumentNotaCDDirectoriConstants().getDenumireProiectMetadataName();
			tipNotaCdMetadataName = arbConstants.getDocumentNotaCDDirectoriConstants().getTipNotaCdMetadataName();
			subiectSpreAprobareMetadataName = arbConstants.getDocumentNotaCDDirectoriConstants().getSubiectSpreAprobareMetadataName();
		
		} else if (documentNotaCD.getDocumentTypeId().equals(this.notaCDResponsabilDocumentType.getId())) {
			notaCDDocumentType = this.notaCDResponsabilDocumentType;
			
			dataCdArbMetadataName = arbConstants.getDocumentNotaCDResponsabilConstants().getDataCdArbMetadataName();
			denumireProiectMetadataName = arbConstants.getDocumentNotaCDResponsabilConstants().getDenumireProiectMetadataName();
			tipNotaCdMetadataName = arbConstants.getDocumentNotaCDResponsabilConstants().getTipNotaCdMetadataName();
			subiectSpreAprobareMetadataName = arbConstants.getDocumentNotaCDResponsabilConstants().getSubiectSpreAprobareMetadataName();
		
		} else if (documentNotaCD.getDocumentTypeId().equals(this.notaCDUseriARBDocumentType.getId())) {
			
			notaCDDocumentType = this.notaCDUseriARBDocumentType;
			
			dataCdArbMetadataName = arbConstants.getDocumentNotaCDUseriARBConstants().getDataCdArbMetadataName();
			denumireProiectMetadataName = arbConstants.getDocumentNotaCDUseriARBConstants().getDenumireProiectMetadataName();
			tipNotaCdMetadataName = arbConstants.getDocumentNotaCDUseriARBConstants().getTipNotaCdMetadataName();
			subiectSpreAprobareMetadataName = arbConstants.getDocumentNotaCDUseriARBConstants().getSubiectSpreAprobareMetadataName();
		
		} else {
			throw new RuntimeException("ID tip de document neidentificat [" + documentNotaCD.getDocumentTypeId() + "]");
		}
		
		List<String> proiectIds = DocumentUtils.getMetadataValuesAsStringList(documentNotaCD, documentTypeService, denumireProiectMetadataName);
		if (CollectionUtils.isNotEmpty(proiectIds)) {
			for (String proiectId : proiectIds) {
				if (filter.getProiectId() != null && !filter.getProiectId().toString().equals(proiectId)) {
					continue;
				}
				ActiuniPeProiectNotaCDReportModel notaCD = new ActiuniPeProiectNotaCDReportModel();
				notaCD.setNumeProiect(this.projectNameById.get(proiectId));
				notaCD.setTipNota(DocumentUtils.getMetadataListValueAsLabel(documentNotaCD, notaCDDocumentType, tipNotaCdMetadataName));
				notaCD.setSubiectNota(DocumentUtils.getMetadataValueAsString(documentNotaCD, notaCDDocumentType, subiectSpreAprobareMetadataName));
				notaCD.setDataCdArb(DocumentUtils.getMetadataDateValue(documentNotaCD, notaCDDocumentType, dataCdArbMetadataName));
				
				notaCDs.add(notaCD);
			}
		}	
		return notaCDs;
	}
}
