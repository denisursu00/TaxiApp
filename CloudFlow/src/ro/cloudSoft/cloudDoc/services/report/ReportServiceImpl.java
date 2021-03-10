package ro.cloudSoft.cloudDoc.services.report;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.WordUtils;
import org.apache.commons.lang3.SerializationUtils;

import ro.cloudSoft.cloudDoc.core.AppException;
import ro.cloudSoft.cloudDoc.core.AppExceptionCodes;
import ro.cloudSoft.cloudDoc.core.AppExceptionUtils;
import ro.cloudSoft.cloudDoc.core.constants.CalendarConstants;
import ro.cloudSoft.cloudDoc.core.constants.GroupConstants;
import ro.cloudSoft.cloudDoc.core.constants.NomenclatorConstants;
import ro.cloudSoft.cloudDoc.core.constants.ParameterConstants;
import ro.cloudSoft.cloudDoc.core.constants.TaskConstants;
import ro.cloudSoft.cloudDoc.core.constants.arb.ArbConstants;
import ro.cloudSoft.cloudDoc.core.constants.arb.DocumentDspConstants;
import ro.cloudSoft.cloudDoc.dao.alteDeconturi.AlteDeconturiDao;
import ro.cloudSoft.cloudDoc.dao.arb.ReprezentantiComisieSauGLDao;
import ro.cloudSoft.cloudDoc.dao.bpm.WorkflowInstanceDao;
import ro.cloudSoft.cloudDoc.dao.calendar.CalendarDao;
import ro.cloudSoft.cloudDoc.dao.deplasariDeconturi.DeplasariDeconturiDao;
import ro.cloudSoft.cloudDoc.dao.nomenclator.NomenclatorDao;
import ro.cloudSoft.cloudDoc.dao.nomenclator.NomenclatorValueDao;
import ro.cloudSoft.cloudDoc.dao.parameters.ParametersDao;
import ro.cloudSoft.cloudDoc.dao.project.ProjectDao;
import ro.cloudSoft.cloudDoc.dao.project.TaskDao;
import ro.cloudSoft.cloudDoc.dao.registruIntrariIesiri.RegistruIntrariIesiriDao;
import ro.cloudSoft.cloudDoc.dao.views.DocumentHistoryCompletedTasksViewDao;
import ro.cloudSoft.cloudDoc.domain.alteDeconturi.AlteDeconturi;
import ro.cloudSoft.cloudDoc.domain.alteDeconturi.AlteDeconturi.TipAvansPrimitEnum;
import ro.cloudSoft.cloudDoc.domain.arb.MembruReprezentantiComisieSauGL.Calitate;
import ro.cloudSoft.cloudDoc.domain.alteDeconturi.AlteDeconturiCheltuiala;
import ro.cloudSoft.cloudDoc.domain.bpm.Workflow;
import ro.cloudSoft.cloudDoc.domain.bpm.WorkflowInstance;
import ro.cloudSoft.cloudDoc.domain.calendar.AuditCalendarEvent;
import ro.cloudSoft.cloudDoc.domain.calendar.CalendarEvent;
import ro.cloudSoft.cloudDoc.domain.calendar.MeetingCalendarEvent;
import ro.cloudSoft.cloudDoc.domain.content.CollectionInstance;
import ro.cloudSoft.cloudDoc.domain.content.Document;
import ro.cloudSoft.cloudDoc.domain.content.DocumentIdentifier;
import ro.cloudSoft.cloudDoc.domain.content.DocumentType;
import ro.cloudSoft.cloudDoc.domain.content.ListMetadataDefinition;
import ro.cloudSoft.cloudDoc.domain.content.ListMetadataItem;
import ro.cloudSoft.cloudDoc.domain.content.MetadataCollection;
import ro.cloudSoft.cloudDoc.domain.content.MetadataDefinition;
import ro.cloudSoft.cloudDoc.domain.content.MetadataInstance;
import ro.cloudSoft.cloudDoc.domain.deplasariDeconturi.CheltuialaArb;
import ro.cloudSoft.cloudDoc.domain.deplasariDeconturi.CheltuialaArb.ValutaForCheltuieliArbEnum;
import ro.cloudSoft.cloudDoc.domain.deplasariDeconturi.CheltuialaReprezentantArb;
import ro.cloudSoft.cloudDoc.domain.deplasariDeconturi.CheltuialaReprezentantArb.ValutaForCheltuieliReprezentantArbEnum;
import ro.cloudSoft.cloudDoc.domain.deplasariDeconturi.DeplasareDecont;
import ro.cloudSoft.cloudDoc.domain.nomenclator.Nomenclator;
import ro.cloudSoft.cloudDoc.domain.nomenclator.NomenclatorValue;
import ro.cloudSoft.cloudDoc.domain.organization.OrganizationEntity;
import ro.cloudSoft.cloudDoc.domain.organization.User;
import ro.cloudSoft.cloudDoc.domain.project.Project;
import ro.cloudSoft.cloudDoc.domain.project.Task;
import ro.cloudSoft.cloudDoc.domain.project.TaskStatus;
import ro.cloudSoft.cloudDoc.domain.registruIntrariIesiri.RegistruIesiri;
import ro.cloudSoft.cloudDoc.domain.registruIntrariIesiri.RegistruIesiriDestinatar;
import ro.cloudSoft.cloudDoc.domain.security.SecurityManager;
import ro.cloudSoft.cloudDoc.domain.views.ViewDocumentHistoryCompletedTasks;
import ro.cloudSoft.cloudDoc.plugins.content.arb.report.DocumentReportPlugin;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.SimpleListItemModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.content.ListMetadataItemModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.content.NomenclatorValueAsViewSearchRequestModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.content.NomenclatorValueSearchRequestModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.nomenclator.GetNomenclatorValuesRequestModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.nomenclator.NomenclatorFilter;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.nomenclator.NomenclatorMultipleFilter;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.nomenclator.NomenclatorSimpleFilter;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.nomenclator.NomenclatorValueModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.nomenclator.NomenclatorValueViewModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.report.ActiuniOrganizateDeArbReportFilterModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.report.ActiuniOrganizateDeArbReportModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.report.ActiuniOrganizateDeArbReportRowModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.report.ActiuniPeProiectRegistruIntrariIesiriReportModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.report.ActiuniPeProiectReportFilterModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.report.ActiuniPeProiectReportModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.report.AderareOioroReportFilterModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.report.AderareOioroReportRowModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.report.CentralizatorPrezentaPerioadaReportFilterModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.report.CentralizatorPrezentaPerioadaReportMapModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.report.CentralizatorPrezentaPerioadaReportModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.report.CentralizatorPrezentaPerioadaReportRowModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.report.CereriConcediuReportBundle;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.report.CereriConcediuReportFilterModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.report.CereriConcediuReportModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.report.CereriConcediuReportRowModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.report.ContinutAgendaModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.report.DashboardProiecteFilterBundle;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.report.DashboardProiecteFilterModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.report.DashboardProiecteRowReportModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.report.DecontCheltuieliAlteDeconturiReportFilterModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.report.DecontCheltuieliAlteDeconturiReportModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.report.DecontCheltuieliAlteDeconturiReportRowModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.report.DeplasariDeconturiReportBundleModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.report.DeplasariDeconturiReportFilterModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.report.DeplasariDeconturiReportRowModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.report.DocumentIdentifierModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.report.ListaProiectelorCareAuVizatActiunileLuniiReportFilterModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.report.ListaProiectelorCareAuVizatActiunileLuniiReportModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.report.MembriiAfiliatiReportFilterModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.report.MembriiAfiliatiReportModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.report.MembriiAfiliatiReportRowModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.report.NivelReprezentareComisiiReportFilterModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.report.NivelReprezentareComisiiReportModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.report.NotaGeneralaPeMembriiArbReportFilterModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.report.NotaGeneralaPeMembriiArbReportModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.report.NotaGeneralaReportFilterModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.report.NotaGeneralaReportRowModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.report.NoteBanciReportFilterModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.report.NoteBanciReportModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.report.NumarParticipantiSedinteComisieGlReportBundle;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.report.NumarParticipantiSedinteComisieGlReportFilterModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.report.NumarParticipantiSedinteComisieGlReportModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.report.NumarParticipantiSedinteComisieGlReportRowModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.report.NumarSedinteCdPvgSiParticipantiReportFilterModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.report.NumarSedinteCdPvgSiParticipantiReportModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.report.NumarSedinteCdPvgSiParticipantiReportRowModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.report.NumarSedinteComisieGlReportFilterModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.report.NumarSedinteComisieGlReportModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.report.NumarSedinteComisieGlReportRowModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.report.ParticipareReprezentantiArbLaActiuniInAfaraAsociatieiReportFilterModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.report.ParticipareReprezentantiArbLaActiuniInAfaraAsociatieiReportModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.report.ParticipareReprezentantiArbLaActiuniInAfaraAsociatieiReportRowModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.report.PrezentaAgaFilterModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.report.PrezentaAgaReportModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.report.PrezentaAgaReportRowModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.report.PrezentaComisiiGlInIntervalReportBundle;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.report.PrezentaComisiiGlInIntervalReportFilterModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.report.PrezentaComisiiGlInIntervalReportModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.report.PrezentaComisiiGlInIntervalReportRowModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.report.PrezentaReprezentivitateReportFilterModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.report.PrezentaReprezentivitateReportModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.report.PrezentaSedintaCdPvgInvitatiARBReportFilterModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.report.PrezentaSedintaCdPvgInvitatiARBReportModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.report.PrezentaSedintaCdPvgInvitatiARBReportRowModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.report.PrezentaSedintaCdPvgInvitatiExterniFilterModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.report.PrezentaSedintaCdPvgInvitatiReportRowModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.report.PrezentaSedintaCdPvgMembriiReportFilterModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.report.PrezentaSedintaCdPvgMembriiReportModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.report.PrezentaSedintaCdPvgMembriiReportRowModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.report.PrezentaSedinteCdPvgInvitatiExterniReportModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.report.RaspunsuriBanciReportBundleModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.report.RaspunsuriBanciReportFilterModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.report.RaspunsuriBanciReportRowModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.report.ReprezentantiBancaPerFunctieSiComisieReportFilterModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.report.ReprezentantiBancaPerFunctieSiComisieReportModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.report.ReprezentantiBancaPerFunctieSiComisieReportRowModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.report.TaskuriCumulateReportFilterModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.report.TaskuriCumulateReportModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.report.TaskuriCumulateReportRowModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.report.cheltuieliArbSiReprezentantArb.CheltuialaReprezentantArbAvansPrimit;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.report.cheltuieliArbSiReprezentantArb.CheltuialaReprezentantArbDiurna;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.report.cheltuieliArbSiReprezentantArb.CheltuieliArbReportModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.report.cheltuieliArbSiReprezentantArb.CheltuieliArbSiRePrezentantArbRowModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.report.cheltuieliArbSiReprezentantArb.CheltuieliArbSiReprezentantArbReportFilterModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.report.cheltuieliArbSiReprezentantArb.CheltuieliArbSiReprezentantArbReportModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.report.deplasareDecont.CheltuieliReprezentantArbReportFilterModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.report.deplasareDecont.CheltuieliReprezentantArbReportModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.report.deplasareDecont.CheltuieliReprezentantArbReportRowModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.report.documenteTrimiseDeArb.DocumenteTrimiseDeArbReportFilterModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.report.documenteTrimiseDeArb.DocumenteTrimiseDeArbReportModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.report.participariLaEvenimente.ParticipariEvenimenteReportFilterModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.report.participariLaEvenimente.ParticipariEvenimenteReportRowModel;
import ro.cloudSoft.cloudDoc.services.bpm.WorkflowService;
import ro.cloudSoft.cloudDoc.services.content.DocumentService;
import ro.cloudSoft.cloudDoc.services.content.DocumentTypeService;
import ro.cloudSoft.cloudDoc.services.nomenclator.NomenclatorService;
import ro.cloudSoft.cloudDoc.services.organization.GroupService;
import ro.cloudSoft.cloudDoc.services.organization.UserService;
import ro.cloudSoft.cloudDoc.services.parameters.ParametersService;
import ro.cloudSoft.cloudDoc.services.project.ProjectRealizationDegree;
import ro.cloudSoft.cloudDoc.services.project.ProjectRealizationDegreeProvider;
import ro.cloudSoft.cloudDoc.utils.CheltuieliUtils;
import ro.cloudSoft.cloudDoc.utils.DocumentTypeUtils;
import ro.cloudSoft.cloudDoc.utils.DocumentUtils;
import ro.cloudSoft.cloudDoc.utils.ProjectUtils;
import ro.cloudSoft.cloudDoc.utils.nomenclator.NomenclatorValueUtils;
import ro.cloudSoft.common.utils.DateUtils;
import ro.cloudSoft.common.utils.PagingList;
import ro.cloudSoft.common.utils.beans.BeanUtils;

public class ReportServiceImpl implements ReportService {

	private static final String REPORT_CHETUIELI_ARB_SI_REPREZENTANT_ARB_DIURNA_ZILNICA = "REPORT_CHETUIELI_ARB_SI_REPREZENTANT_ARB_DIURNA_ZILNICA";
	private static final String REPORT_CHETUIELI_ARB_SI_REPREZENTANT_ARB_NUMAR_ZILE = "REPORT_CHETUIELI_ARB_SI_REPREZENTANT_ARB_NUMAR_ZILE";
	private static final String REPORT_CHETUIELI_ARB_SI_REPREZENTANT_ARB_TOTAL_DIURNA = "REPORT_CHETUIELI_ARB_SI_REPREZENTANT_ARB_TOTAL_DIURNA";
	private static final String REPORT_CHETUIELI_ARB_SI_REPREZENTANT_ARB_NULL = "REPORT_CHETUIELI_ARB_SI_REPREZENTANT_ARB_NULL";
	private static final Long REPORT_CENTRALIZATOR_PREZENTA_PERIOADA_IN_AFARA_NOM = -1L;

	private DocumentReportPlugin documentReportPlugin;
	private DocumentTypeService documentTypeService;
	private ArbConstants arbConstants;
	private DeplasariDeconturiDao deplasariDeconturiDao;
	private TaskDao taskDao;
	private NomenclatorService nomenclatorService;
	private NomenclatorValueDao nomenclatorValueDao;
	private UserService userService;
	private NomenclatorDao nomenclatorDao;
	private RegistruIntrariIesiriDao registruIntrariIesiriDao;
	private AlteDeconturiDao alteDeconturiDao;
	private WorkflowInstanceDao workflowInstanceDao;
	private ProjectDao projectDao;
	private DocumentDspConstants documentDspConstants;
	private ParametersDao parametersDao;
	private GroupService groupService;
	private CalendarDao calendarDao;
	private DocumentService documentService;
	private DocumentHistoryCompletedTasksViewDao documentHistoryCompletedTasksViewDao;
	private WorkflowService workflowService;
	private ReprezentantiComisieSauGLDao reprezentantiComisieSauGLDao;
	private ParametersService parametersService;

	public void setDocumentReportPlugin(DocumentReportPlugin documentReportPlugin) {
		this.documentReportPlugin = documentReportPlugin;
	}

	public void setDocumentTypeService(DocumentTypeService documentTypeService) {
		this.documentTypeService = documentTypeService;
	}

	public void setArbConstants(ArbConstants arbConstants) {
		this.arbConstants = arbConstants;
	}

	public void setDeplasariDeconturiDao(DeplasariDeconturiDao deplasariDeconturiDao) {
		this.deplasariDeconturiDao = deplasariDeconturiDao;
	}

	public void setRegistruIntrariIesiriDao(RegistruIntrariIesiriDao registruIntrariIesiriDao) {
		this.registruIntrariIesiriDao = registruIntrariIesiriDao;
	}

	public void setNomenclatorService(NomenclatorService nomenclatorService) {
		this.nomenclatorService = nomenclatorService;
	}

	public void setNomenclatorValueDao(NomenclatorValueDao nomenclatorValueDao) {
		this.nomenclatorValueDao = nomenclatorValueDao;
	}

	public void setNomenclatorDao(NomenclatorDao nomenclatorDao) {
		this.nomenclatorDao = nomenclatorDao;
	}

	public void setTaskDao(TaskDao taskDao) {
		this.taskDao = taskDao;
	}

	public void setAlteDeconturiDao(AlteDeconturiDao alteDeconturiDao) {
		this.alteDeconturiDao = alteDeconturiDao;
	}

	public void setWorkflowInstanceDao(WorkflowInstanceDao workflowInstanceDao) {
		this.workflowInstanceDao = workflowInstanceDao;
	}

	public void setCalendarDao(CalendarDao calendarDao) {
		this.calendarDao = calendarDao;
	}

	public void setDocumentService(DocumentService documentService) {
		this.documentService = documentService;
	}

	public void setProjectDao(ProjectDao projectDao) {
		this.projectDao = projectDao;
	}

	public void setDocumentHistoryCompletedTasksViewDao(DocumentHistoryCompletedTasksViewDao documentHistoryCompletedTasksViewDao) {
		this.documentHistoryCompletedTasksViewDao = documentHistoryCompletedTasksViewDao;
	}

	public void setWorkflowService(WorkflowService workflowService) {
		this.workflowService = workflowService;
	}

	public void setReprezentantiComisieSauGLDao(ReprezentantiComisieSauGLDao reprezentantiComisieSauGLDao) {
		this.reprezentantiComisieSauGLDao = reprezentantiComisieSauGLDao;
	}

	public void setParametersService(ParametersService parametersService) {
		this.parametersService = parametersService;
	}

	@Override
	public NumarSedinteCdPvgSiParticipantiReportModel getNumarSedinteCdPvgSiParticipantiReport(NumarSedinteCdPvgSiParticipantiReportFilterModel filter) throws AppException {

		NumarSedinteCdPvgSiParticipantiReportModel report = new NumarSedinteCdPvgSiParticipantiReportModel();

		DocumentType prezentaCdPvgDocumentType = documentTypeService.getDocumentTypeByName(arbConstants.getDocumentPrezentaCdPvgConstants().getDocumentTypeName());

		List<Document> reportDocuments = documentReportPlugin.getDocumentsForNumarSedinteCdPvgSiParticipantiReport(filter);

		int totalOfTotalMembriiCDCounter = 0;
		int totalOfTotalInvitatiCounter = 0;
		int totalOfTotalInvitatiARBCounter = 0;
		int totalOfTotalParticipantiCounter = 0;

		List<NumarSedinteCdPvgSiParticipantiReportRowModel> reportRows = new ArrayList<>();
		if (CollectionUtils.isNotEmpty(reportDocuments)) {
			for (Document reportDocument : reportDocuments) {
				if (workflowInstanceDao.hasFinishedStatusWorkflowInstanceByDocumentId(reportDocument.getDocumentLocationRealName(), reportDocument.getId())) {
					NumarSedinteCdPvgSiParticipantiReportRowModel reportRow = new NumarSedinteCdPvgSiParticipantiReportRowModel();
					reportRow.setDataSedinta(DocumentUtils.getMetadataDateValue(reportDocument, prezentaCdPvgDocumentType,
							arbConstants.getDocumentPrezentaCdPvgConstants().getDataSedintaMetadataName()));

					int totalParticipantiCounter = 0;

					int totalMembriiCD = DocumentUtils.getSizeOfMetadataCollectionInstance(reportDocument, prezentaCdPvgDocumentType,
							arbConstants.getDocumentPrezentaCdPvgConstants().getInformatiiMembriiCdMetadataName());
					totalParticipantiCounter = totalParticipantiCounter + totalMembriiCD;
					totalOfTotalMembriiCDCounter = totalOfTotalMembriiCDCounter + totalMembriiCD;
					reportRow.setTotalMembriiCD(totalMembriiCD);

					int totalInvitati = DocumentUtils.getSizeOfMetadataCollectionInstance(reportDocument, prezentaCdPvgDocumentType,
							arbConstants.getDocumentPrezentaCdPvgConstants().getInformatiiInvitatiExterniMetadataName());
					totalParticipantiCounter = totalParticipantiCounter + totalInvitati;
					totalOfTotalInvitatiCounter = totalOfTotalInvitatiCounter + totalInvitati;
					reportRow.setTotalInvitati(totalInvitati);

					int totalInvitatiARB = DocumentUtils.getSizeOfMetadataCollectionInstance(reportDocument, prezentaCdPvgDocumentType,
							arbConstants.getDocumentPrezentaCdPvgConstants().getInformatiiInvitatiArbMetadataName());
					totalParticipantiCounter = totalParticipantiCounter + totalInvitatiARB;
					totalOfTotalInvitatiARBCounter = totalOfTotalInvitatiARBCounter + totalInvitatiARB;
					reportRow.setTotalInvitatiARB(totalInvitatiARB);

					reportRow.setTotalParticipanti(totalParticipantiCounter);

					totalOfTotalParticipantiCounter = totalOfTotalParticipantiCounter + totalParticipantiCounter;

					reportRows.add(reportRow);
				}
			}
		}

		Collections.sort(reportRows, new Comparator<NumarSedinteCdPvgSiParticipantiReportRowModel>() {

			@Override
			public int compare(NumarSedinteCdPvgSiParticipantiReportRowModel row1, NumarSedinteCdPvgSiParticipantiReportRowModel row2) {
				return row1.getDataSedinta().compareTo(row2.getDataSedinta());
			}
		});

		report.setTotalOfTotalMembriiCD(totalOfTotalMembriiCDCounter);
		report.setTotalOfTotalInvitati(totalOfTotalInvitatiCounter);
		report.setTotalOfTotalInvitatiARB(totalOfTotalInvitatiARBCounter);
		report.setTotalOfTotalParticipanti(totalOfTotalParticipantiCounter);
		report.setRows(reportRows);

		return report;
	}

	@Override
	public PrezentaSedinteCdPvgInvitatiExterniReportModel getPrezentaSedinteCdPvgInvitatiExterniReport(PrezentaSedintaCdPvgInvitatiExterniFilterModel filter) throws AppException {

		PrezentaSedinteCdPvgInvitatiExterniReportModel report = new PrezentaSedinteCdPvgInvitatiExterniReportModel();

		DocumentType prezentaCdPvgDocumentType = documentTypeService.getDocumentTypeByName(arbConstants.getDocumentPrezentaCdPvgConstants().getDocumentTypeName());

		List<Document> reportDocuments = documentReportPlugin.getDocumentsForPrezentaSedinteCdPvgInvitatiExterniReport(filter);

		report.setTotalInvitatiAcreditati(0);
		report.setTotalInvitatiInlocuitori(0);

		List<PrezentaSedintaCdPvgInvitatiReportRowModel> reportRows = new ArrayList<>();
		if (CollectionUtils.isNotEmpty(reportDocuments)) {
			for (Document reportDocument : reportDocuments) {
				if (workflowInstanceDao.hasFinishedStatusWorkflowInstanceByDocumentId(reportDocument.getDocumentLocationRealName(), reportDocument.getId())) {
					String tipSedinta = DocumentUtils.getMetadataListValueAsLabel(reportDocument, prezentaCdPvgDocumentType,
							arbConstants.getDocumentPrezentaCdPvgConstants().getSedintaMetadataName());
					Date dataSedinta = DocumentUtils.getMetadataDateValue(reportDocument, prezentaCdPvgDocumentType,
							arbConstants.getDocumentPrezentaCdPvgConstants().getDataSedintaMetadataName());

					List<CollectionInstance> metadataCollectionInfoMembrii = DocumentUtils.getMetadataCollectionInstance(reportDocument, prezentaCdPvgDocumentType,
							arbConstants.getDocumentPrezentaCdPvgConstants().getInformatiiInvitatiExterniMetadataName());
					for (CollectionInstance metadataInfoMembrii : metadataCollectionInfoMembrii) {
						PrezentaSedintaCdPvgInvitatiReportRowModel reportRow = new PrezentaSedintaCdPvgInvitatiReportRowModel();
						reportRow.setTipSedinta(tipSedinta);
						reportRow.setDataSedinta(dataSedinta);
						String infoInvitatiExterniMetadataName = arbConstants.getDocumentPrezentaCdPvgConstants().getInformatiiInvitatiExterniMetadataName();
						MetadataDefinition metadataDefinitionInstitutieInvitat = DocumentTypeUtils.getMetadataDefinitionFromCollectionByName(prezentaCdPvgDocumentType,
								infoInvitatiExterniMetadataName, arbConstants.getDocumentPrezentaCdPvgConstants().getInstitutieInvitatOfInformatiiInvitatiExterniMetadataName());
						MetadataDefinition metadataDefinitionInvitatAcreditat = DocumentTypeUtils.getMetadataDefinitionFromCollectionByName(prezentaCdPvgDocumentType,
								infoInvitatiExterniMetadataName, arbConstants.getDocumentPrezentaCdPvgConstants().getInvitatAcreditatOfInformatiiInvitatiExterniMetadataName());
						MetadataDefinition metadataDefinitionNumeInvitatInlocuitor = DocumentTypeUtils.getMetadataDefinitionFromCollectionByName(prezentaCdPvgDocumentType,
								infoInvitatiExterniMetadataName,
								arbConstants.getDocumentPrezentaCdPvgConstants().getNumeInvitatInlocuitorOfInformatiiInvitatiExterniMetadataName());
						MetadataDefinition metadataDefinitionPrenumeInvitatInlocuitor = DocumentTypeUtils.getMetadataDefinitionFromCollectionByName(prezentaCdPvgDocumentType,
								infoInvitatiExterniMetadataName,
								arbConstants.getDocumentPrezentaCdPvgConstants().getPrenumeInvitatInlocuitorOfInformatiiInvitatiExterniMetadataName());

						for (MetadataInstance metadataInstance : metadataInfoMembrii.getMetadataInstanceList()) {

							if (metadataInstance.getMetadataDefinitionId().equals(metadataDefinitionInstitutieInvitat.getId())) {
								Long institutieId = Long.parseLong(metadataInstance.getValue());
								reportRow.setInstitutieInvitat(institutieId.toString());
							}
							if (metadataInstance.getMetadataDefinitionId().equals(metadataDefinitionInvitatAcreditat.getId())) {
								Long invitaAcreditatId = Long.parseLong(metadataInstance.getValue());
								reportRow.setInvitatAcreditat(invitaAcreditatId.toString());
							}

							if (metadataInstance.getMetadataDefinitionId().equals(metadataDefinitionNumeInvitatInlocuitor.getId())) {

								reportRow.setInvitatInlocuitorNume(metadataInstance.getValue());
							}
							if (metadataInstance.getMetadataDefinitionId().equals(metadataDefinitionPrenumeInvitatInlocuitor.getId())) {

								reportRow.setInvitatInlocuitorPrenume(metadataInstance.getValue());
							}
						}

						if (areFilterConditionRespected(reportRow, filter)) {
							replaceIdWithValueFromNomenclators(reportRow);
							reportRows.add(reportRow);
							if (StringUtils.isNotEmpty(reportRow.getInvitatAcreditat())) {
								report.setTotalInvitatiAcreditati(report.getTotalInvitatiAcreditati() + 1);
							}
							if (StringUtils.isNotEmpty(reportRow.getInvitatInlocuitorNume())) {
								report.setTotalInvitatiInlocuitori(report.getTotalInvitatiInlocuitori() + 1);
							}
						}
					}
				}
			}
		}

		Collections.sort(reportRows, new Comparator<PrezentaSedintaCdPvgInvitatiReportRowModel>() {

			@Override
			public int compare(PrezentaSedintaCdPvgInvitatiReportRowModel row1, PrezentaSedintaCdPvgInvitatiReportRowModel row2) {
				return row1.getDataSedinta().compareTo(row2.getDataSedinta());
			}
		});

		report.setRows(reportRows);

		return report;
	}

	private boolean areFilterConditionRespected(PrezentaSedintaCdPvgInvitatiReportRowModel reportRow, PrezentaSedintaCdPvgInvitatiExterniFilterModel filter) {
		boolean areFilterConditionRespected = true;
		if (filter.getInstitutieInvitatId() != null
				&& (reportRow.getInstitutieInvitat() == null || !reportRow.getInstitutieInvitat().equals(filter.getInstitutieInvitatId().toString()))) {
			areFilterConditionRespected = false;
		}
		if (filter.getInvitatAcreditatId() != null
				&& (reportRow.getInvitatAcreditat() == null || !reportRow.getInvitatAcreditat().equals(filter.getInvitatAcreditatId().toString()))) {
			areFilterConditionRespected = false;
		}
		if (StringUtils.isNotBlank(filter.getInvitatInlocuitorNume()) && (reportRow.getInvitatInlocuitorNume() == null
				|| !reportRow.getInvitatInlocuitorNume().trim().toUpperCase().contains(filter.getInvitatInlocuitorNume().trim().toUpperCase()))) {
			areFilterConditionRespected = false;
		}
		if (StringUtils.isNotBlank(filter.getInvitatInlocuitorPrenume()) && (reportRow.getInvitatInlocuitorPrenume() == null
				|| !reportRow.getInvitatInlocuitorPrenume().trim().toUpperCase().contains(filter.getInvitatInlocuitorPrenume().trim().toUpperCase()))) {
			areFilterConditionRespected = false;
		}
		return areFilterConditionRespected;
	}

	private void replaceIdWithValueFromNomenclators(PrezentaSedintaCdPvgInvitatiReportRowModel reportRow) throws AppException {
		Map<Long, String> nomenclatorInstitutiiUiAttrValuesAsMap = nomenclatorService
				.getJoinedNomenclatorUiAtributesValuesAsMapByNomenclatorCode(NomenclatorConstants.INSTITUTII_NOMENCLATOR_CODE);
		Map<Long, String> nomenclatorPersoaneUiAttrValuesAsMap = nomenclatorService
				.getJoinedNomenclatorUiAtributesValuesAsMapByNomenclatorCode(NomenclatorConstants.PERSOANE_NOMENCLATOR_CODE);

		if (reportRow.getInstitutieInvitat() != null) {
			Long institutieId = Long.parseLong(reportRow.getInstitutieInvitat());
			reportRow.setInstitutieInvitat(nomenclatorInstitutiiUiAttrValuesAsMap.get(institutieId));
		}
		if (reportRow.getInvitatAcreditat() != null) {
			Long invitaAcreditatId = Long.parseLong(reportRow.getInvitatAcreditat());
			reportRow.setInvitatAcreditat(nomenclatorPersoaneUiAttrValuesAsMap.get(invitaAcreditatId));
		}
	}

	public PrezentaSedintaCdPvgMembriiReportModel getPrezentaSedintaCdPvgMembriiReport(PrezentaSedintaCdPvgMembriiReportFilterModel filter) throws AppException {

		PrezentaSedintaCdPvgMembriiReportModel report = new PrezentaSedintaCdPvgMembriiReportModel();

		DocumentType prezentaCdPvgDocumentType = documentTypeService.getDocumentTypeByName(arbConstants.getDocumentPrezentaCdPvgConstants().getDocumentTypeName());

		List<Document> reportDocuments = documentReportPlugin.getDocumentsForPrezentaSedinteCdPvgMembriiReport(filter);

		int totalOfTotalMembriiCDCounter = 0;

		Map<Long, String> nomenclatorInstitutiiUiAttrValuesAsMap = nomenclatorService
				.getJoinedNomenclatorUiAtributesValuesAsMapByNomenclatorCode(NomenclatorConstants.INSTITUTII_CD_NOMENCLATOR_CODE);
		Map<Long, String> nomenclatorMembriiUiAttrValuesAsMap = nomenclatorService
				.getJoinedNomenclatorUiAtributesValuesAsMapByNomenclatorCode(NomenclatorConstants.MEMBRI_CD_NOMENCLATOR_CODE);

		List<PrezentaSedintaCdPvgMembriiReportRowModel> reportRows = new ArrayList<>();
		if (CollectionUtils.isNotEmpty(reportDocuments)) {
			for (Document reportDocument : reportDocuments) {
				if (workflowInstanceDao.hasFinishedStatusWorkflowInstanceByDocumentId(reportDocument.getDocumentLocationRealName(), reportDocument.getId())) {
					String tipSedinta = DocumentUtils.getMetadataListValueAsLabel(reportDocument, prezentaCdPvgDocumentType,
							arbConstants.getDocumentPrezentaCdPvgConstants().getSedintaMetadataName());
					Date dataSedinta = DocumentUtils.getMetadataDateValue(reportDocument, prezentaCdPvgDocumentType,
							arbConstants.getDocumentPrezentaCdPvgConstants().getDataSedintaMetadataName());
					List<CollectionInstance> metadataCollectionInfoMembrii = DocumentUtils.getMetadataCollectionInstance(reportDocument, prezentaCdPvgDocumentType,
							arbConstants.getDocumentPrezentaCdPvgConstants().getInformatiiMembriiCdMetadataName());
					for (CollectionInstance metadataInfoMembrii : metadataCollectionInfoMembrii) {
						PrezentaSedintaCdPvgMembriiReportRowModel reportRow = new PrezentaSedintaCdPvgMembriiReportRowModel();
						reportRow.setTipSedinta(tipSedinta);
						reportRow.setDataSedinta(dataSedinta);
						String infoMemmbriiMetadataName = arbConstants.getDocumentPrezentaCdPvgConstants().getInformatiiMembriiCdMetadataName();
						MetadataDefinition metadataDefinitionInstitutie = DocumentTypeUtils.getMetadataDefinitionFromCollectionByName(prezentaCdPvgDocumentType,
								infoMemmbriiMetadataName, arbConstants.getDocumentPrezentaCdPvgConstants().getInstitutieOfInformatiiMembriiCdMetadataName());
						MetadataDefinition metadataDefinitionMembru = DocumentTypeUtils.getMetadataDefinitionFromCollectionByName(prezentaCdPvgDocumentType,
								infoMemmbriiMetadataName, arbConstants.getDocumentPrezentaCdPvgConstants().getMembruOfInformatiiMembriiCdMetadataName());
						Long institutieId = null;
						for (MetadataInstance metadataInstance : metadataInfoMembrii.getMetadataInstanceList()) {
							if (metadataInstance.getMetadataDefinitionId().equals(metadataDefinitionInstitutie.getId())) {
								institutieId = Long.parseLong(metadataInstance.getValue());
								reportRow.setInstitutieMembru(nomenclatorInstitutiiUiAttrValuesAsMap.get(institutieId));
							}
							if (metadataInstance.getMetadataDefinitionId().equals(metadataDefinitionMembru.getId())) {
								Long membruId = Long.parseLong(metadataInstance.getValue());
								reportRow.setMembru(nomenclatorMembriiUiAttrValuesAsMap.get(membruId));
							}
						}
						if (filter.getInstitutieMembru() == null || filter.getInstitutieMembru().equals(institutieId.toString())) {
							reportRows.add(reportRow);
							totalOfTotalMembriiCDCounter++;
						}
					}
				}
			}
		}
		report.setTotalMemmbrii(totalOfTotalMembriiCDCounter);

		Collections.sort(reportRows, new Comparator<PrezentaSedintaCdPvgMembriiReportRowModel>() {

			@Override
			public int compare(PrezentaSedintaCdPvgMembriiReportRowModel row1, PrezentaSedintaCdPvgMembriiReportRowModel row2) {
				return row1.getDataSedinta().compareTo(row2.getDataSedinta());
			}
		});

		report.setTotalMemmbrii(totalOfTotalMembriiCDCounter);
		report.setRows(reportRows);

		return report;
	}

	@Override
	public List<AderareOioroReportRowModel> getAderareOioroReport(AderareOioroReportFilterModel filter) throws AppException {

		Nomenclator nomenclatorReprArbOrgII = nomenclatorDao.findByCode(NomenclatorConstants.NOMENCLATOR_CODE_REPREZENTANTI_ARB_ORGANISME_INTERNE_SI_INTERNATIONALE);

		NomenclatorValueAsViewSearchRequestModel searchModelForReprArbOrgII = new NomenclatorValueAsViewSearchRequestModel();
		List<NomenclatorFilter> filters = new ArrayList<>();
		searchModelForReprArbOrgII.setNomenclatorId(nomenclatorReprArbOrgII.getId());

		if (CollectionUtils.isNotEmpty(filter.getOrganismIdList())) {
			NomenclatorMultipleFilter nomenclatorMultipleFilter = new NomenclatorMultipleFilter();
			nomenclatorMultipleFilter.setAttributeKey(NomenclatorConstants.REPREZENTANTI_ARB_ORGANISME_INTERNE_SI_INTERNATIONALE_ATTRIBUTE_KEY_ORGANSIM);
			List<String> filterValues = new ArrayList<>();
			ro.cloudSoft.cloudDoc.utils.CollectionUtils.addObjectCollectionInStringCollection(filter.getOrganismIdList(), filterValues);
			nomenclatorMultipleFilter.setValues(filterValues);
			filters.add(nomenclatorMultipleFilter);
		}

		if (filter.getAbreviere() != null) {
			NomenclatorSimpleFilter nomenclatorSimpleFilter = new NomenclatorSimpleFilter();
			nomenclatorSimpleFilter.setAttributeKey(NomenclatorConstants.REPREZENTANTI_ARB_ORGANISME_INTERNE_SI_INTERNATIONALE_ATTRIBUTE_KEY_ABREVERE);
			nomenclatorSimpleFilter.setValue(filter.getAbreviere()); // TODO: change to contains
			filters.add(nomenclatorSimpleFilter);
		}
		if (CollectionUtils.isNotEmpty(filter.getComitetIdList())) {
			NomenclatorMultipleFilter nomenclatorMultipleFilter = new NomenclatorMultipleFilter();
			nomenclatorMultipleFilter.setAttributeKey(NomenclatorConstants.REPREZENTANTI_ARB_ORGANISME_INTERNE_SI_INTERNATIONALE_ATTRIBUTE_KEY_COMITET);
			List<String> filterValues = new ArrayList<>();
			ro.cloudSoft.cloudDoc.utils.CollectionUtils.addObjectCollectionInStringCollection(filter.getComitetIdList(), filterValues);
			nomenclatorMultipleFilter.setValues(filterValues);
			filters.add(nomenclatorMultipleFilter);
		}
		if (CollectionUtils.isNotEmpty(filter.getInstitutieIdList())) {
			NomenclatorMultipleFilter nomenclatorMultipleFilter = new NomenclatorMultipleFilter();
			nomenclatorMultipleFilter.setAttributeKey(NomenclatorConstants.REPREZENTANTI_ARB_ORGANISME_INTERNE_SI_INTERNATIONALE_ATTRIBUTE_KEY_INSTITUTIE);
			List<String> filterValues = new ArrayList<>();
			ro.cloudSoft.cloudDoc.utils.CollectionUtils.addObjectCollectionInStringCollection(filter.getInstitutieIdList(), filterValues);
			nomenclatorMultipleFilter.setValues(filterValues);
			filters.add(nomenclatorMultipleFilter);
		}
		if (CollectionUtils.isNotEmpty(filter.getReprezentantIdList())) {
			NomenclatorMultipleFilter nomenclatorMultipleFilter = new NomenclatorMultipleFilter();
			nomenclatorMultipleFilter.setAttributeKey(NomenclatorConstants.REPREZENTANTI_ARB_ORGANISME_INTERNE_SI_INTERNATIONALE_ATTRIBUTE_KEY_REPREZENTANT);
			List<String> filterValues = new ArrayList<>();
			ro.cloudSoft.cloudDoc.utils.CollectionUtils.addObjectCollectionInStringCollection(filter.getReprezentantIdList(), filterValues);
			nomenclatorMultipleFilter.setValues(filterValues);
			filters.add(nomenclatorMultipleFilter);
		}
		if (filter.getFunctie() != null) {
			NomenclatorSimpleFilter nomenclatorSimpleFilter = new NomenclatorSimpleFilter();
			nomenclatorSimpleFilter.setAttributeKey(NomenclatorConstants.REPREZENTANTI_ARB_ORGANISME_INTERNE_SI_INTERNATIONALE_ATTRIBUTE_KEY_FUNCTIE);
			nomenclatorSimpleFilter.setValue(filter.getFunctie());
			filters.add(nomenclatorSimpleFilter);
		}
		if (CollectionUtils.isNotEmpty(filter.getCoordonatorArbIdList())) {
			NomenclatorMultipleFilter nomenclatorMultipleFilter = new NomenclatorMultipleFilter();
			nomenclatorMultipleFilter.setAttributeKey(NomenclatorConstants.REPREZENTANTI_ARB_ORGANISME_INTERNE_SI_INTERNATIONALE_ATTRIBUTE_KEY_COORDONATOR_ARB);
			List<String> filterValues = new ArrayList<>();
			ro.cloudSoft.cloudDoc.utils.CollectionUtils.addObjectCollectionInStringCollection(filter.getCoordonatorArbIdList(), filterValues);
			nomenclatorMultipleFilter.setValues(filterValues);
			filters.add(nomenclatorMultipleFilter);
		}

		searchModelForReprArbOrgII.setFilters(filters);
		searchModelForReprArbOrgII.setSortedAttributes(new ArrayList<>());

		List<NomenclatorValueViewModel> nomenclatorValueModelsPagingResults = nomenclatorService.searchNomenclatorValueViewModelsWithoutPaging(searchModelForReprArbOrgII);

		List<AderareOioroReportRowModel> rowsModels = new ArrayList<>();
		for (NomenclatorValueViewModel nomenclatorValueModel : nomenclatorValueModelsPagingResults) {
			AderareOioroReportRowModel rowModel = new AderareOioroReportRowModel();

			rowModel.setOrganism(nomenclatorValueModel.getAttributes().get(NomenclatorConstants.REPREZENTANTI_ARB_ORGANISME_INTERNE_SI_INTERNATIONALE_ATTRIBUTE_KEY_ORGANSIM));
			rowModel.setAbreviere(nomenclatorValueModel.getAttributes().get(NomenclatorConstants.REPREZENTANTI_ARB_ORGANISME_INTERNE_SI_INTERNATIONALE_ATTRIBUTE_KEY_ABREVERE));
			rowModel.setComitet(nomenclatorValueModel.getAttributes().get(NomenclatorConstants.REPREZENTANTI_ARB_ORGANISME_INTERNE_SI_INTERNATIONALE_ATTRIBUTE_KEY_COMITET));
			rowModel.setInstitutie(nomenclatorValueModel.getAttributes().get(NomenclatorConstants.REPREZENTANTI_ARB_ORGANISME_INTERNE_SI_INTERNATIONALE_ATTRIBUTE_KEY_INSTITUTIE));
			rowModel.setReprezentant(
					nomenclatorValueModel.getAttributes().get(NomenclatorConstants.REPREZENTANTI_ARB_ORGANISME_INTERNE_SI_INTERNATIONALE_ATTRIBUTE_KEY_REPREZENTANT));
			rowModel.setFunctie(nomenclatorValueModel.getAttributes().get(NomenclatorConstants.REPREZENTANTI_ARB_ORGANISME_INTERNE_SI_INTERNATIONALE_ATTRIBUTE_KEY_FUNCTIE));
			rowModel.setCoordonatorArb(
					nomenclatorValueModel.getAttributes().get(NomenclatorConstants.REPREZENTANTI_ARB_ORGANISME_INTERNE_SI_INTERNATIONALE_ATTRIBUTE_KEY_COORDONATOR_ARB));

			// cautare nr deplasari bugetate si an din
			// DETALII_NUMAR_DEPLASARI_BUGETATE_ORGANISME
			NomenclatorValueModel nomenclatorValueReprOrganismeII = nomenclatorService.getNomenclatorValue(nomenclatorValueModel.getId());
			String organismIdFromReprArbOrgII = BeanUtils.getPropertyValue(nomenclatorValueReprOrganismeII,
					NomenclatorConstants.REPREZENTANTI_ARB_ORGANISME_INTERNE_SI_INTERNATIONALE_ATTRIBUTE_KEY_ORGANSIM);
			Nomenclator nomenclatorDetaliiNrDeplsariBugetateOrganisme = nomenclatorDao.findByCode(NomenclatorConstants.DETALII_NUMAR_DEPLASARI_BUGETATE_NOMENCLATOR_CODE);

			NomenclatorValueSearchRequestModel searchModelForDetaliiNrDeplsariBugetateOrganisme = new NomenclatorValueSearchRequestModel();
			searchModelForDetaliiNrDeplsariBugetateOrganisme.setNomenclatorId(nomenclatorDetaliiNrDeplsariBugetateOrganisme.getId());
			NomenclatorSimpleFilter filtruOrganism = new NomenclatorSimpleFilter();
			filtruOrganism.setAttributeKey(NomenclatorConstants.DETALII_NUMAR_DEPLASARI_BUGETATE_ORGANISME_ATTRIBUTE_KEY_ORGANISM);
			filtruOrganism.setValue(organismIdFromReprArbOrgII);
			searchModelForDetaliiNrDeplsariBugetateOrganisme.setFilters(Arrays.asList(filtruOrganism));
			searchModelForDetaliiNrDeplsariBugetateOrganisme.setSortedAttributes(new ArrayList<>());
			searchModelForDetaliiNrDeplsariBugetateOrganisme.setOffset(0);
			searchModelForDetaliiNrDeplsariBugetateOrganisme.setPageSize(Integer.MAX_VALUE);

			PagingList<NomenclatorValueModel> pagingListValuesFromDNDB = nomenclatorService.searchNomenclatorValues(searchModelForDetaliiNrDeplsariBugetateOrganisme);
			boolean isMultipleRecordForOrganismInDetaliiDeplasari = Boolean.FALSE;
			if (pagingListValuesFromDNDB.getElements().size() > 0) {
				for (NomenclatorValueModel nomenclatorValueDNDB : pagingListValuesFromDNDB.getElements()) {
					Integer anDataDela = NomenclatorValueUtils.getYearFromDateAttribute(nomenclatorValueDNDB,
							NomenclatorConstants.DETALII_NUMAR_DEPLASARI_BUGETATE_ORGANISME_ATTRIBUTE_KEY_DE_LA_DATA);
					if (CollectionUtils.isNotEmpty(filter.getAniList())) {
						if (filter.getAniList().contains(anDataDela)) {
							rowModel.setNrDeplasariBugetate(Integer.parseInt(BeanUtils.getPropertyValue(nomenclatorValueDNDB,
									NomenclatorConstants.DETALII_NUMAR_DEPLASARI_BUGETATE_ORGANISME_ATTRIBUTE_KEY_NUMAR_DEPLASARI_BUGETATE)));
							rowModel.setAn(anDataDela);
						}
					} else {
						if (isMultipleRecordForOrganismInDetaliiDeplasari) {
							rowsModels.add(rowModel);
							try {
								AderareOioroReportRowModel rowModelDuplicate = (AderareOioroReportRowModel) rowModel.clone();
								rowModelDuplicate.setNrDeplasariBugetate(Integer.parseInt(BeanUtils.getPropertyValue(nomenclatorValueDNDB,
										NomenclatorConstants.DETALII_NUMAR_DEPLASARI_BUGETATE_ORGANISME_ATTRIBUTE_KEY_NUMAR_DEPLASARI_BUGETATE)));
								rowModelDuplicate.setAn(anDataDela);
								rowsModels.add(rowModelDuplicate);
							} catch (CloneNotSupportedException e) {
								throw AppExceptionUtils.getAppExceptionFromExceptionCause(e);
							}
						} else {
							rowModel.setNrDeplasariBugetate(Integer.parseInt(BeanUtils.getPropertyValue(nomenclatorValueDNDB,
									NomenclatorConstants.DETALII_NUMAR_DEPLASARI_BUGETATE_ORGANISME_ATTRIBUTE_KEY_NUMAR_DEPLASARI_BUGETATE)));
							rowModel.setAn(anDataDela);
							isMultipleRecordForOrganismInDetaliiDeplasari = Boolean.TRUE;
						}
					}
				}
			}
			if (rowModel.getAn() != 0) {
				rowsModels.add(rowModel);
			}
		}

		return rowsModels;
	}

	public List<ParticipariEvenimenteReportRowModel> getParticipariEvenimenteReport(ParticipariEvenimenteReportFilterModel filter) throws AppException {
		List<Task> tasks = taskDao.getTasksAssignmentsByParticipariEvenimenteReportFilterModel(filter);
		tasks = tasks.stream().filter(task -> task.getStatus() != TaskStatus.CANCELLED).collect(Collectors.toList());
		List<ParticipariEvenimenteReportRowModel> rowsModels = new ArrayList<>();

		for (Task task : tasks) {
			
			if (!task.getParticipationsTo().equals(TaskConstants.PARTICIPARI_LA_VALUE_ALTELE)) {
				ParticipariEvenimenteReportRowModel rowModel = new ParticipariEvenimenteReportRowModel();
	
				rowModel.setNumeTask(task.getName());
				rowModel.setNumeProiect(task.getProject().getName());
				rowModel.setNumeSubproiect(task.getSubactivity() != null ? task.getSubactivity().getName() : null);
				rowModel.setParticipariLa(task.getParticipationsTo());
				rowModel.setStatusTask(task.getStatus().name());
				List<OrganizationEntity> assignments = task.getAssignments();
				if (assignments != null) {
					List<String> responsabiliTask = new ArrayList<>();
					for (OrganizationEntity assignment : assignments) {
						if (assignment instanceof User) {
							responsabiliTask.add(((User) assignment).getDisplayName());
						} else {
							throw new RuntimeException("Is not allowed for organization_entity to not be instance of user!");
						}
					}
					rowModel.setResponsabiliTask(responsabiliTask);
				}
				rowModel.setDataInceput(task.getStartDate());
				rowModel.setDataSfarsit(task.getEndDate());
	
				rowsModels.add(rowModel);
			}
		}
		return rowsModels;

	}

	public PrezentaSedintaCdPvgInvitatiARBReportModel getPrezentaSedinteCdPvgInvitatiArbReport(PrezentaSedintaCdPvgInvitatiARBReportFilterModel filter,
			SecurityManager securityManager) throws AppException {

		PrezentaSedintaCdPvgInvitatiARBReportModel report = new PrezentaSedintaCdPvgInvitatiARBReportModel();

		DocumentType prezentaCdPvgDocumentType = documentTypeService.getDocumentTypeByName(arbConstants.getDocumentPrezentaCdPvgConstants().getDocumentTypeName());

		List<Document> reportDocuments = documentReportPlugin.getPrezentaSedinteCdPvgInvitatiArbReport(filter);

		Map<Long, String> nomenclatorPersoaneUiAttrValuesAsMap = nomenclatorService
				.getJoinedNomenclatorUiAtributesValuesAsMapByNomenclatorCode(NomenclatorConstants.PERSOANE_NOMENCLATOR_CODE);

		List<PrezentaSedintaCdPvgInvitatiARBReportRowModel> reportRows = new ArrayList<>();

		List<User> utilzatori = userService.getAllUsers(securityManager);
		Map<String, String> userFullNameByIdMap = new HashMap<>();
		for (User user : utilzatori) {
			userFullNameByIdMap.put(user.getId().toString(), user.getDisplayNameWithTitle());
		}

		if (CollectionUtils.isNotEmpty(reportDocuments)) {
			for (Document reportDocument : reportDocuments) {

				// TODO: report is build only on one document
				if (workflowInstanceDao.hasFinishedStatusWorkflowInstanceByDocumentId(reportDocument.getDocumentLocationRealName(), reportDocument.getId())) {
					String tipSedinta = DocumentUtils.getMetadataListValueAsLabel(reportDocument, prezentaCdPvgDocumentType,
							arbConstants.getDocumentPrezentaCdPvgConstants().getSedintaMetadataName());
					Date dataSedinta = DocumentUtils.getMetadataDateValue(reportDocument, prezentaCdPvgDocumentType,
							arbConstants.getDocumentPrezentaCdPvgConstants().getDataSedintaMetadataName());

					List<CollectionInstance> metadataCollectionInfoInvitatiArb = DocumentUtils.getMetadataCollectionInstance(reportDocument, prezentaCdPvgDocumentType,
							arbConstants.getDocumentPrezentaCdPvgConstants().getInformatiiInvitatiArbMetadataName());
					for (CollectionInstance metadataInfoMembrii : metadataCollectionInfoInvitatiArb) {

						String infoInvitatiArbMetadataName = arbConstants.getDocumentPrezentaCdPvgConstants().getInformatiiInvitatiArbMetadataName();
						MetadataDefinition metadataDefinitionInvitatArb = DocumentTypeUtils.getMetadataDefinitionFromCollectionByName(prezentaCdPvgDocumentType,
								infoInvitatiArbMetadataName, arbConstants.getDocumentPrezentaCdPvgConstants().getInvitatArbOfInformatiiInvitatiArbMetadataName());

						for (MetadataInstance metadataInstance : metadataInfoMembrii.getMetadataInstanceList()) {
							if (metadataInstance.getMetadataDefinitionId().equals(metadataDefinitionInvitatArb.getId())) {
								if (filter.getInvitatArb() == null || filter.getInvitatArb().equals(metadataInstance.getValue())) {
									PrezentaSedintaCdPvgInvitatiARBReportRowModel reportRow = new PrezentaSedintaCdPvgInvitatiARBReportRowModel();
									reportRow.setDataSedinta(dataSedinta);
									reportRow.setTipSedinta(tipSedinta);
									String userFullName = userFullNameByIdMap.get(metadataInstance.getValue());
									if (StringUtils.isBlank(userFullName)) {
										userFullName = "(sters)";
									}
									reportRow.setInvitatArb(userFullName);

									reportRows.add(reportRow);
								}
							}
						}
					}
					report.setRows(reportRows);
					report.setTotalInvitatiARB(reportRows.size());
					// TODO: report is build only on one document
				}
			}
		}

		Collections.sort(reportRows, new Comparator<PrezentaSedintaCdPvgInvitatiARBReportRowModel>() {

			@Override
			public int compare(PrezentaSedintaCdPvgInvitatiARBReportRowModel row1, PrezentaSedintaCdPvgInvitatiARBReportRowModel row2) {
				return row1.getDataSedinta().compareTo(row2.getDataSedinta());
			}
		});

		report.setRows(reportRows);

		return report;
	}

	@Override
	public List<DocumenteTrimiseDeArbReportModel> getDocumenteTrimiseDeArbReport(DocumenteTrimiseDeArbReportFilterModel filter) throws AppException {

		List<NomenclatorValue> tipInstitutieMembruArb = nomenclatorValueDao.findByNomenclatorCodeAndAttribute(NomenclatorConstants.TIP_INSTITUTIE,
				NomenclatorConstants.TIP_INSTITUTIE_DENUMIRE_ATTRIBUTE_KEY_COD, NomenclatorConstants.TIP_INSTITUTII_VALUE_FOR_MEMBRU_ARB);

		List<DocumenteTrimiseDeArbReportModel> rowsModels = new ArrayList<>();

		if (CollectionUtils.isEmpty(tipInstitutieMembruArb)) {
			return rowsModels;
		}

		String tipInstitutieMembruArbId = tipInstitutieMembruArb.get(0).getId().toString();

		List<RegistruIesiri> iesiri = registruIntrariIesiriDao.getIesiriByDocumenteTrimiseDeArbReportFilterModel(filter);

		for (RegistruIesiri iesire : iesiri) {
			for (RegistruIesiriDestinatar destinatar : iesire.getDestinatari()) {
				DocumenteTrimiseDeArbReportModel rowModel = new DocumenteTrimiseDeArbReportModel();

				rowModel.setDataInregistrare(iesire.getDataInregistrare());
				rowModel.setDocument(iesire.getContinut());
				rowModel.setNumarInregistrareIesire(iesire.getNumarInregistrare());
				if (destinatar.getInstitutie() != null) {
					if (!NomenclatorValueUtils.getAttributeValueAsString(destinatar.getInstitutie(), NomenclatorConstants.RESGISTRU_IESIRI_DESTINATARI_INSTITUTIE_ATTRIBUTE_KEY_COD)
							.equals(tipInstitutieMembruArbId)) {
						rowModel.setInstitutie(NomenclatorValueUtils.getAttributeValueAsString(destinatar.getInstitutie(), NomenclatorConstants.INSTITUTII_ATTRIBUTE_KEY_DENUMIRE));
						rowsModels.add(rowModel);
					}
				} else {
					rowModel.setInstitutie(destinatar.getNume());
					rowsModels.add(rowModel);
				}
			}
		}
		return rowsModels;
	}

	@Override
	public List<ListaProiectelorCareAuVizatActiunileLuniiReportModel> getListaProiectelorCareAuVizatActiunileLuniiReport(
			ListaProiectelorCareAuVizatActiunileLuniiReportFilterModel filter) throws AppException {

		List<ListaProiectelorCareAuVizatActiunileLuniiReportModel> rowsModels = new ArrayList<>();

		List<Project> projects = projectDao.getByReportFilter(filter);

		for (Project project : projects) {
			ListaProiectelorCareAuVizatActiunileLuniiReportModel rowModel = new ListaProiectelorCareAuVizatActiunileLuniiReportModel();
			rowModel.setDenumireProiect(project.getName());
			rowsModels.add(rowModel);
		}

		return rowsModels;
	}

	@Override
	public CheltuieliReprezentantArbReportModel getDeplasariDeconturiCheltuieliReprezentantArb(CheltuieliReprezentantArbReportFilterModel filter) throws AppException {

		CheltuieliReprezentantArbReportModel reportModel = new CheltuieliReprezentantArbReportModel();
		List<CheltuieliReprezentantArbReportRowModel> rows = new ArrayList<>();
		reportModel.setTotalCheltuieli(new BigDecimal(0));

		List<DeplasareDecont> deplasariDeconturi = deplasariDeconturiDao.getDeplasariDeconturiByTitularAndDataDecont(filter.getTitular(), filter.getDataDecont(),
				filter.getDataDecontDeLa(), filter.getDataDecontPanaLa());
		BigDecimal avansPrimitCard = new BigDecimal(0);
		BigDecimal avansPrimitNumerar = new BigDecimal(0);
		BigDecimal totalCheltuieli = new BigDecimal(0);

		Map<Long, String> nomenclatorPersoaneUiAttrValuesAsMap = nomenclatorService
				.getJoinedNomenclatorUiAtributesValuesAsMapByNomenclatorCode(NomenclatorConstants.REPREZENTANTI_ARB_ORGANISME_INTERNE_SI_INTERNATIONALE_CODE);

		if ((filter.getTitular() != null) && (filter.getDataDecont() != null) && (deplasariDeconturi.size() == 1)) {
			DeplasareDecont deplasareDecont = deplasariDeconturi.get(0);
			reportModel.setTitular(nomenclatorPersoaneUiAttrValuesAsMap.get(deplasareDecont.getReprezentantArb().getId()));
			reportModel.setDataDecont(deplasareDecont.getCheltuieliArbDataDecont());
			reportModel.setNumarInregistrareDecont(deplasareDecont.getNumarInregistrare().split("/")[0]);
			for (CheltuialaReprezentantArb cheltuiala : deplasareDecont.getCheltuieliReprezentantArb()) {
				CheltuieliReprezentantArbReportRowModel row = new CheltuieliReprezentantArbReportRowModel();
				row.setDataDocumentJustificativ(cheltuiala.getDataDocumentJustificativ());
				row.setNrDocumentJustificativ(cheltuiala.getNumarDocumentJustificativ());
				row.setExplicatie(cheltuiala.getExplicatie());
				BigDecimal valoareCheltuiala = CheltuieliUtils.calculateRonValue(cheltuiala.getValoareCheltuiala(), cheltuiala.getValuta().toString(), cheltuiala.getCursValutar());
				row.setSuma(valoareCheltuiala);

				totalCheltuieli = totalCheltuieli.add(valoareCheltuiala);
				rows.add(row);
			}

			BigDecimal avansPrimitSumaRon = CheltuieliUtils.calculateRonValue(deplasareDecont.getCheltuieliReprezentantArbAvansPrimitSuma(),
					deplasareDecont.getCheltuieliReprezentantArbAvansPrimitSumaValuta(), deplasareDecont.getCheltuieliReprezentantArbDiurnaZilnicaCursValutar());
			if (deplasareDecont.getCheltuieliReprezentantArbAvansPrimitCardSauNumerar() != null) {
				if (deplasareDecont.getCheltuieliReprezentantArbAvansPrimitCardSauNumerar().equals(TipAvansPrimitEnum.CARD)) {
					avansPrimitCard = avansPrimitCard.add(avansPrimitSumaRon);
				} else {
					avansPrimitNumerar = avansPrimitNumerar.add(avansPrimitSumaRon);
				}
			}

			reportModel.setAvansPrimitCard(avansPrimitCard);
			reportModel.setAvansPrimitNumerar(avansPrimitNumerar);
			reportModel.setTotalCheltuieli(totalCheltuieli);
			BigDecimal rest = totalCheltuieli.subtract(avansPrimitNumerar).subtract(avansPrimitCard);
			if (rest.compareTo(BigDecimal.ZERO) < 0) {
				reportModel.setDiferentaDeRestituit(rest.negate());
				reportModel.setDiferentaDeIncasat(new BigDecimal(0));
			} else {
				reportModel.setDiferentaDeIncasat(rest);
				reportModel.setDiferentaDeRestituit(new BigDecimal(0));
			}
		} else {
			for (DeplasareDecont deplasareDecont : deplasariDeconturi) {
				for (CheltuialaReprezentantArb cheltuiala : deplasareDecont.getCheltuieliReprezentantArb()) {
					CheltuieliReprezentantArbReportRowModel row = new CheltuieliReprezentantArbReportRowModel();
					row.setTitular(nomenclatorPersoaneUiAttrValuesAsMap.get(deplasareDecont.getReprezentantArb().getId()));
					row.setDataDocumentJustificativ(cheltuiala.getDataDocumentJustificativ());
					row.setNrDocumentJustificativ(cheltuiala.getNumarDocumentJustificativ());

					rows.add(row);
				}
			}
		}

		reportModel.setRows(rows);

		return reportModel;
	}

	public void setUserService(UserService userService) {
		this.userService = userService;

	}

	@Override
	public ActiuniPeProiectReportModel getActiuniPeProiectReport(ActiuniPeProiectReportFilterModel filter) throws AppException {
		
		List<Task> tasks = taskDao.getAllTaskByActiuniPeProiectReportFilterModel(filter);
		tasks = tasks.stream().filter(task -> task.getStatus() != TaskStatus.CANCELLED).collect(Collectors.toList());
		List<ActiuniPeProiectRegistruIntrariIesiriReportModel> intrari = registruIntrariIesiriDao.getAllRegistruIntrariByActiuniPeProiectReportFilterModel(filter);
		List<ActiuniPeProiectRegistruIntrariIesiriReportModel> iesiri = registruIntrariIesiriDao.getAllRegistruIesiriByActiuniPeProiectReportFilterModel(filter);		
		List<Document> noteCdDocuments = documentReportPlugin.getDocumentsNoteCDForActiuniPeProiectReport(filter);
		
		ActiuniPeProiectReportPreparator reportPreparator = new ActiuniPeProiectReportPreparator(documentTypeService, arbConstants, projectDao, tasks, iesiri, intrari, noteCdDocuments, filter);
		return reportPreparator.prepare();
	}

	public DecontCheltuieliAlteDeconturiReportModel getAlteDeconturiCheltuieliReprezentantArbReport(DecontCheltuieliAlteDeconturiReportFilterModel filter) throws AppException {

		DecontCheltuieliAlteDeconturiReportModel reportModel = new DecontCheltuieliAlteDeconturiReportModel();
		List<DecontCheltuieliAlteDeconturiReportRowModel> rows = new ArrayList<>();

		BigDecimal avansPrimitCard = BigDecimal.ZERO;
		BigDecimal avansPrimitNumerar = BigDecimal.ZERO;
		BigDecimal totalCheltuieli = BigDecimal.ZERO;

		List<AlteDeconturi> deconturi = alteDeconturiDao.getByDecontCheltuieliAlteDeconturiReportFilterModel(filter);

		if ((filter.getTitular() != null) && (filter.getDataDecont() != null) && (deconturi.size() == 1)) {
			AlteDeconturi decont = deconturi.get(0);
			reportModel.setTitular(decont.getTitularDecont());
			reportModel.setDataDecont(decont.getDataDecont());
			reportModel.setNumarInregistrareDecont(decont.getNumarDecont().split("/")[0]);
			for (AlteDeconturiCheltuiala cheltuiala : decont.getCheltuieli()) {
				DecontCheltuieliAlteDeconturiReportRowModel row = new DecontCheltuieliAlteDeconturiReportRowModel();
				row.setDataDocumentJustificativ(cheltuiala.getDataDocumentJustificativ());
				row.setNumarDocumentJustificativ(cheltuiala.getNumarDocumentJustificativ());
				row.setExplicatie(cheltuiala.getExplicatie());
				row.setSuma(cheltuiala.getValoareCheltuiala());

				if (decont.getAvansPrimit() != null) {
					if (decont.getTipAvansPrimit().equals(TipAvansPrimitEnum.CARD)) {
						avansPrimitCard = decont.getAvansPrimit();
					} else {
						avansPrimitNumerar = decont.getAvansPrimit();
					}
				}

				totalCheltuieli = totalCheltuieli.add(cheltuiala.getValoareCheltuiala());

				rows.add(row);
			}
			reportModel.setAvansPrimitCard(avansPrimitCard);
			reportModel.setAvansPrimitNumerar(avansPrimitNumerar);
			reportModel.setTotalCheltuieli(totalCheltuieli);

			BigDecimal rest = totalCheltuieli.subtract(avansPrimitNumerar).subtract(avansPrimitCard);

			if (rest.compareTo(BigDecimal.ZERO) < 0) {
				reportModel.setDiferentaDeRestituit(rest.negate());
				reportModel.setDiferentaDeIncasat(BigDecimal.ZERO);
			} else {
				reportModel.setDiferentaDeRestituit(BigDecimal.ZERO);
				reportModel.setDiferentaDeIncasat(rest);
			}
		} else {
			for (AlteDeconturi decont : deconturi) {
				for (AlteDeconturiCheltuiala cheltuiala : decont.getCheltuieli()) {
					DecontCheltuieliAlteDeconturiReportRowModel row = new DecontCheltuieliAlteDeconturiReportRowModel();
					row.setTitular(WordUtils.capitalizeFully(decont.getTitularDecont()));
					row.setDataDocumentJustificativ(cheltuiala.getDataDocumentJustificativ());
					row.setNumarDocumentJustificativ(cheltuiala.getNumarDocumentJustificativ());

					rows.add(row);
				}
			}
		}

		reportModel.setRows(rows);

		return reportModel;
	}

	@Override
	public ParticipareReprezentantiArbLaActiuniInAfaraAsociatieiReportModel getParticipareReprezentantiArbLaActiuniInAfaraAsociatieiReport(
			ParticipareReprezentantiArbLaActiuniInAfaraAsociatieiReportFilterModel filter, SecurityManager userSecurity) throws AppException {

		ParticipareReprezentantiArbLaActiuniInAfaraAsociatieiReportModel reportModel = new ParticipareReprezentantiArbLaActiuniInAfaraAsociatieiReportModel();
		List<ParticipareReprezentantiArbLaActiuniInAfaraAsociatieiReportRowModel> rows = new ArrayList<>();
		reportModel.setRows(rows);
		List<String> calendarNames = new ArrayList<>();
		calendarNames.add(CalendarConstants.CALENDAR_NAME_VALUE_MISIUNI_CMAS);
		calendarNames.add(CalendarConstants.CALENDAR_NAME_VALUE_DEPLASARI);
		calendarNames.add(CalendarConstants.CALENDAR_NAME_VALUE_INTALNIRE_PVG);
		calendarNames.add(CalendarConstants.CALENDAR_NAME_VALUE_AGA_ARB);
		calendarNames.add(CalendarConstants.CALENDAR_NAME_VALUE_EVENIMENTE_INTALNIRI);
		calendarNames.add(CalendarConstants.CALENDAR_NAME_VALUE_SEDINTE_PARLAMENT);
		calendarNames.add(CalendarConstants.CALENDAR_NAME_VALUE_SEDINTE_COMISII_GL);
		calendarNames.add(CalendarConstants.CALENDAR_NAME_VALUE_CD_ARB);
		List<Long> calendarIds = calendarDao.getCalendarIdsByNames(calendarNames);
		List<CalendarEvent> calendarEvents = calendarDao.getCalendarEventsByCalendarIdsAndStartDateInterval(calendarIds, DateUtils.nullHourMinutesSeconds(filter.getDataInceput()),
				DateUtils.maximizeHourMinutesSeconds(filter.getDataSfarsit()));

		Map<Long, String> nomenclatorPersoaneUiAttrValuesAsMap = nomenclatorService.getJoinedNomenclatorUiAtributesValuesAsMapByNomenclatorCode(NomenclatorConstants.PERSOANE_NOMENCLATOR_CODE);
		
		for (CalendarEvent calendarEvent : calendarEvents) {
			if (calendarEvent.getCalendar().getName().equals(CalendarConstants.CALENDAR_NAME_VALUE_SEDINTE_COMISII_GL)) {
				MeetingCalendarEvent meetingEvent = (MeetingCalendarEvent) calendarEvent;
				String documentId = meetingEvent.getDocumentId();
				String documentLocationRealName = meetingEvent.getDocumentLocationRealName();
				if (StringUtils.isNotEmpty(documentId) && StringUtils.isNotEmpty(documentLocationRealName)) {
					DocumentIdentifier documentIdentifierPrezentaForSearch = new DocumentIdentifier(documentLocationRealName, documentId);

					List<Document> documentsForMinutaPrezentaComisiiGl = documentReportPlugin
							.getDocumentsMinutaPrezentaComisiiGlForParticipareReprzArbLaActiuniInAfaraAsocReport(documentIdentifierPrezentaForSearch);
					if (CollectionUtils.isNotEmpty(documentsForMinutaPrezentaComisiiGl) && documentsForMinutaPrezentaComisiiGl.size() == 1) {
						Document documentMinutaSedintaComGl = documentsForMinutaPrezentaComisiiGl.get(0);
						String ordineDeZiValue = DocumentUtils.getMetadataValueAsString(documentMinutaSedintaComGl, documentTypeService,
								arbConstants.getDocumentMinutaSedintaComisieGLConstants().getOrdineDeZiMetadataName());
						ParticipareReprezentantiArbLaActiuniInAfaraAsociatieiReportRowModel row = new ParticipareReprezentantiArbLaActiuniInAfaraAsociatieiReportRowModel();
						row.setActiune(meetingEvent.getSubject());
						row.setData(meetingEvent.getStartDate());
						row.setSubiectAgenda(ordineDeZiValue);
						rows.add(row);
					}
				}
			} else {
				if (calendarEvent instanceof MeetingCalendarEvent) {
					MeetingCalendarEvent meetingEvent = (MeetingCalendarEvent) calendarEvent;
					if (StringUtils.isEmpty(meetingEvent.getLocation())
							|| !meetingEvent.getLocation().trim().toLowerCase().contains(CalendarConstants.CALENDAR_EVENT_LOCATION_VALUE_ARB.trim().toLowerCase())) {
						ParticipareReprezentantiArbLaActiuniInAfaraAsociatieiReportRowModel row = new ParticipareReprezentantiArbLaActiuniInAfaraAsociatieiReportRowModel();
						row.setActiune(meetingEvent.getSubject());
						row.setData(meetingEvent.getStartDate());
						if (meetingEvent.getCalendar().getName().equals(CalendarConstants.CALENDAR_NAME_VALUE_INTALNIRE_PVG)) {
							List<ContinutAgendaModel> continutAgendaList = getAgendaContentFromEvent(meetingEvent, userSecurity);
							if (CollectionUtils.isEmpty(continutAgendaList)) {
								rows.add(row);
							} else {
								continutAgendaList.forEach(continutAgenda -> {
									ParticipareReprezentantiArbLaActiuniInAfaraAsociatieiReportRowModel rowClone = SerializationUtils.clone(row);
									rowClone.setDetaliuSubiectAgenda(continutAgenda.getDetaliuSubiect());
									rowClone.setSubiectAgenda(continutAgenda.getSubiect());

									if (StringUtils.isNotEmpty(rowClone.getSubiectAgenda())) {
										rows.add(rowClone);
									}
								});
							}
						} else if (meetingEvent.getCalendar().getName().equals(CalendarConstants.CALENDAR_NAME_VALUE_CD_ARB)) {
							DocumentIdentifier documentIdentifierOrdineDeZiForSearch = new DocumentIdentifier(meetingEvent.getDocumentLocationRealName(),
									meetingEvent.getDocumentId());
							List<Document> documentsForPrezentaCdPvg = documentReportPlugin
									.getDocumentsPrezentaCdPvgForParticipareReprzArbLaActiuniInAfaraAsocReport(documentIdentifierOrdineDeZiForSearch);
							if (CollectionUtils.isNotEmpty(documentsForPrezentaCdPvg) && documentsForPrezentaCdPvg.size() == 1) {

								Document documentPrezentaCdPvg = documentsForPrezentaCdPvg.get(0);
								List<String> participanti = getParticipantiFromDocumentPrezentaCdPvg(documentPrezentaCdPvg);
								if (CollectionUtils.isEmpty(participanti)) {
									ParticipareReprezentantiArbLaActiuniInAfaraAsociatieiReportRowModel rowCdArb = new ParticipareReprezentantiArbLaActiuniInAfaraAsociatieiReportRowModel();
									rowCdArb.setActiune(meetingEvent.getSubject());
									rowCdArb.setData(meetingEvent.getStartDate());
									rows.add(rowCdArb);
								} else {
									participanti.forEach(participant -> {
										ParticipareReprezentantiArbLaActiuniInAfaraAsociatieiReportRowModel rowCdArb = new ParticipareReprezentantiArbLaActiuniInAfaraAsociatieiReportRowModel();
										rowCdArb.setActiune(meetingEvent.getSubject());
										rowCdArb.setData(meetingEvent.getStartDate());
										rowCdArb.setParticipanti(participant);
										rows.add(rowCdArb);
									});
								}

							}
						} else {
							Set<OrganizationEntity> attendees = meetingEvent.getAttendees();
							if (CollectionUtils.isEmpty(attendees)) {
								if (meetingEvent.getReprezentantExtern() != null) {
									row.setParticipanti(nomenclatorPersoaneUiAttrValuesAsMap.get(meetingEvent.getReprezentantExtern().getId()));
								}
								rows.add(row);
							} else {
								attendees.forEach(attendee -> {
									ParticipareReprezentantiArbLaActiuniInAfaraAsociatieiReportRowModel rowForEachParticipant = new ParticipareReprezentantiArbLaActiuniInAfaraAsociatieiReportRowModel();
									rowForEachParticipant.setData(row.getData());
									rowForEachParticipant.setActiune(row.getActiune());
									rowForEachParticipant.setParticipanti(attendee.getDisplayName());
									rows.add(rowForEachParticipant);
								});
							}
						}
					}
				} else if (calendarEvent instanceof AuditCalendarEvent) {
					AuditCalendarEvent auditEvent = (AuditCalendarEvent) calendarEvent;
					if (StringUtils.isEmpty(auditEvent.getLocation())
							|| !auditEvent.getLocation().trim().toLowerCase().contains(CalendarConstants.CALENDAR_EVENT_LOCATION_VALUE_ARB.trim().toLowerCase())) {
						Set<OrganizationEntity> attendees = auditEvent.getAttendees();
						if (CollectionUtils.isEmpty(attendees)) {
							ParticipareReprezentantiArbLaActiuniInAfaraAsociatieiReportRowModel row = new ParticipareReprezentantiArbLaActiuniInAfaraAsociatieiReportRowModel();
							row.setActiune(auditEvent.getSubject());
							row.setData(auditEvent.getStartDate());
							rows.add(row);
						} else {
							attendees.forEach(attendee -> {
								ParticipareReprezentantiArbLaActiuniInAfaraAsociatieiReportRowModel rowForEachParticipant = new ParticipareReprezentantiArbLaActiuniInAfaraAsociatieiReportRowModel();
								rowForEachParticipant.setData(auditEvent.getStartDate());
								rowForEachParticipant.setActiune(auditEvent.getSubject());
								rowForEachParticipant.setParticipanti(attendee.getDisplayName());
								rows.add(rowForEachParticipant);
							});
						}
					}
				}
			}
		}
		return reportModel;

	}

	@Override
	public PrezentaComisiiGlInIntervalReportModel getPrezentaComisiiGlInIntervalReport(PrezentaComisiiGlInIntervalReportFilterModel filter) throws AppException {
		
		// normalize filter
		if (StringUtils.isNotBlank(filter.getFunctie())) {
			filter.setFunctie(filter.getFunctie().trim());
		}
		
		PrezentaComisiiGlInIntervalReportModel report = new PrezentaComisiiGlInIntervalReportModel();
		List<PrezentaComisiiGlInIntervalReportRowModel> rows = new ArrayList<PrezentaComisiiGlInIntervalReportRowModel>();

		Map<Long, String> nomenclatorInstitutiiUiAttrValuesAsMap = nomenclatorService
				.getJoinedNomenclatorUiAtributesValuesAsMapByNomenclatorCode(NomenclatorConstants.INSTITUTII_NOMENCLATOR_CODE);

		DocumentType prezentaComisiiGlDocumentType = documentTypeService.getDocumentTypeByName(arbConstants.getDocumentPrezentaComisieGlConstants().getDocumentTypeName());
		List<Document> reportDocuments = documentReportPlugin.getDocumentsForPrezentaComisiiGlInIntervalReport(filter);

		if (CollectionUtils.isNotEmpty(reportDocuments)) {

			for (Document reportDocument : reportDocuments) {

				List<CollectionInstance> metadataCollectionInfoInformatiiParticipanti = DocumentUtils.getMetadataCollectionInstance(reportDocument, prezentaComisiiGlDocumentType,
						arbConstants.getDocumentPrezentaComisieGlConstants().getInformatiiParticipantiMetadataName());

				if (metadataCollectionInfoInformatiiParticipanti != null) {
					for (CollectionInstance metadataInfoParticipanti : metadataCollectionInfoInformatiiParticipanti) {

						PrezentaComisiiGlInIntervalReportRowModel row = new PrezentaComisiiGlInIntervalReportRowModel();

						String infoInformatiiParticipantiMetadataName = arbConstants.getDocumentPrezentaComisieGlConstants().getInformatiiParticipantiMetadataName();

						MetadataDefinition metadataDefinitionInstitutie = DocumentTypeUtils.getMetadataDefinitionFromCollectionByName(prezentaComisiiGlDocumentType,
								infoInformatiiParticipantiMetadataName,
								arbConstants.getDocumentPrezentaComisieGlConstants().getNumeInstitutieOfInformatiiParticipantiMetadataName());
						MetadataDefinition metadataDefinitionMembruAcreditat = DocumentTypeUtils.getMetadataDefinitionFromCollectionByName(prezentaComisiiGlDocumentType,
								infoInformatiiParticipantiMetadataName,
								arbConstants.getDocumentPrezentaComisieGlConstants().getMembruAcreditatOfInformatiiParticipantiMetadataName());
						MetadataDefinition metadataDefinitionNumeParticipantInlocuitor = DocumentTypeUtils.getMetadataDefinitionFromCollectionByName(prezentaComisiiGlDocumentType,
								infoInformatiiParticipantiMetadataName,
								arbConstants.getDocumentPrezentaComisieGlConstants().getNumeParticipantInlocuitorOfInformatiiParticipantiMetadataName());
						MetadataDefinition metadataDefinitionPrenumeParticipantInlocuitor = DocumentTypeUtils.getMetadataDefinitionFromCollectionByName(
								prezentaComisiiGlDocumentType, infoInformatiiParticipantiMetadataName,
								arbConstants.getDocumentPrezentaComisieGlConstants().getPrenumeParticipantInlocuitorOfInformatiiParticipantiMetadataName());
						MetadataDefinition metadataDefinitionFunctie = DocumentTypeUtils.getMetadataDefinitionFromCollectionByName(prezentaComisiiGlDocumentType,
								infoInformatiiParticipantiMetadataName,
								arbConstants.getDocumentPrezentaComisieGlConstants().getFunctieMembruOfInformatiiParticipantiMetadataName());
						MetadataDefinition metadataDefinitionDepartament = DocumentTypeUtils.getMetadataDefinitionFromCollectionByName(prezentaComisiiGlDocumentType,
								infoInformatiiParticipantiMetadataName, arbConstants.getDocumentPrezentaComisieGlConstants().getDepartamentOfInformatiiParticipantiMetadataName());
						MetadataDefinition metadataDefinitionEmail = DocumentTypeUtils.getMetadataDefinitionFromCollectionByName(prezentaComisiiGlDocumentType,
								infoInformatiiParticipantiMetadataName, arbConstants.getDocumentPrezentaComisieGlConstants().getEmailOfInformatiiParticipantiMetadataName());
						MetadataDefinition metadataDefinitionTelefon = DocumentTypeUtils.getMetadataDefinitionFromCollectionByName(prezentaComisiiGlDocumentType,
								infoInformatiiParticipantiMetadataName, arbConstants.getDocumentPrezentaComisieGlConstants().getTelefonOfInformatiiParticipantiMetadataName());

						boolean areFilterConditionRespected = true;

						for (MetadataInstance metadataInstance : metadataInfoParticipanti.getMetadataInstanceList()) {
							if (metadataInstance.getMetadataDefinitionId().equals(metadataDefinitionInstitutie.getId())) {
								Long institutieId = Long.parseLong(metadataInstance.getValue());
								if (filter.getInstitutieId() != null && !institutieId.equals(filter.getInstitutieId())) {
									areFilterConditionRespected = false;
								}
								row.setInstitutie(nomenclatorInstitutiiUiAttrValuesAsMap.get(institutieId));
							}
							if (metadataInstance.getMetadataDefinitionId().equals(metadataDefinitionFunctie.getId())) {
								String functie = metadataInstance.getValue();
								if (StringUtils.isNotBlank(filter.getFunctie())) {
									String filterFunctie = filter.getFunctie().toUpperCase().trim();
									if (StringUtils.isBlank(functie) || (!functie.toUpperCase().contains(filterFunctie))) {
										areFilterConditionRespected = false;
									}									
								}
								row.setFunctie(functie);
							}
							if (metadataInstance.getMetadataDefinitionId().equals(metadataDefinitionDepartament.getId())) {
								row.setDepartament(metadataInstance.getValue());
							}
							if (metadataInstance.getMetadataDefinitionId().equals(metadataDefinitionEmail.getId())) {
								row.setEmail(metadataInstance.getValue());
							}
							if (metadataInstance.getMetadataDefinitionId().equals(metadataDefinitionTelefon.getId())) {
								row.setTelefon(metadataInstance.getValue());
							}

							if (filter.getNumeParticipantInlocuitor() == null && filter.getPrenumeParticipantInlocuitor() == null && filter.getParticipantAcreditat() == null) {
								if (metadataInstance.getMetadataDefinitionId().equals(metadataDefinitionMembruAcreditat.getId())) {
									String membruAcreeditat = metadataInstance.getValue();
									if (filter.getParticipantAcreditat() != null && !membruAcreeditat.equals(filter.getParticipantAcreditat())) {
										areFilterConditionRespected = false;
									}
									if (StringUtils.isNotBlank(membruAcreeditat)) {
										row.setPrticipant(membruAcreeditat);
									}
								}
								if (metadataInstance.getMetadataDefinitionId().equals(metadataDefinitionNumeParticipantInlocuitor.getId())) {
									String numeParticipantInlocuitor = metadataInstance.getValue();
									if (filter.getNumeParticipantInlocuitor() != null
											&& !numeParticipantInlocuitor.toUpperCase().contains(filter.getNumeParticipantInlocuitor().toUpperCase())) {
										areFilterConditionRespected = false;
									}
									if (StringUtils.isNotBlank(numeParticipantInlocuitor)) {
										row.setPrticipant(numeParticipantInlocuitor);
									}
								}

								if (metadataInstance.getMetadataDefinitionId().equals(metadataDefinitionPrenumeParticipantInlocuitor.getId())) {
									String prenumeParticipantInlocuitor = metadataInstance.getValue();
									if (filter.getPrenumeParticipantInlocuitor() != null
											&& !prenumeParticipantInlocuitor.toUpperCase().contains(filter.getPrenumeParticipantInlocuitor().toUpperCase())) {
										areFilterConditionRespected = false;
									}
									if (StringUtils.isNotBlank(prenumeParticipantInlocuitor)) {
										row.setPrticipant(row.getPrticipant() + " " + prenumeParticipantInlocuitor);
									}
								}
							}
							if (filter.getNumeParticipantInlocuitor() == null && filter.getPrenumeParticipantInlocuitor() == null && filter.getParticipantAcreditat() != null) {
								if (metadataInstance.getMetadataDefinitionId().equals(metadataDefinitionMembruAcreditat.getId())) {
									String membruAcreeditat = metadataInstance.getValue();
									if (filter.getParticipantAcreditat() != null && !membruAcreeditat.equals(filter.getParticipantAcreditat())) {
										areFilterConditionRespected = false;
									}
									if (StringUtils.isNotBlank(membruAcreeditat)) {
										row.setPrticipant(membruAcreeditat);
									}
								}
							}
							if ((filter.getNumeParticipantInlocuitor() != null || filter.getPrenumeParticipantInlocuitor() != null) && filter.getParticipantAcreditat() == null) {
								if (metadataInstance.getMetadataDefinitionId().equals(metadataDefinitionNumeParticipantInlocuitor.getId())) {
									String numeParticipantInlocuitor = metadataInstance.getValue();
									if (filter.getNumeParticipantInlocuitor() != null
											&& !numeParticipantInlocuitor.toUpperCase().contains(filter.getNumeParticipantInlocuitor().toUpperCase())) {
										areFilterConditionRespected = false;
									}
									if (StringUtils.isNotBlank(numeParticipantInlocuitor)) {
										row.setPrticipant(numeParticipantInlocuitor);
									}
								}

								if (metadataInstance.getMetadataDefinitionId().equals(metadataDefinitionPrenumeParticipantInlocuitor.getId())) {
									String prenumeParticipantInlocuitor = metadataInstance.getValue();
									if (filter.getPrenumeParticipantInlocuitor() != null
											&& !prenumeParticipantInlocuitor.toUpperCase().contains(filter.getPrenumeParticipantInlocuitor().toUpperCase())) {
										areFilterConditionRespected = false;
									}
									if (StringUtils.isNotBlank(prenumeParticipantInlocuitor)) {
										row.setPrticipant(row.getPrticipant() + " " + prenumeParticipantInlocuitor);
									}
								}
							}	
						}
						
						if (StringUtils.isNotBlank(filter.getFunctie()) && DocumentUtils.isNotMetadataCompleted(metadataDefinitionFunctie.getId(), metadataInfoParticipanti.getMetadataInstanceList())) {
							areFilterConditionRespected = false;
						}
						
						if (areFilterConditionRespected && row.getPrticipant() != null) {
							rows.add(row);
						}
					}
				}
			}
		}

		report.setRows(rows);

		return report;
	}

	@Override
	public ActiuniOrganizateDeArbReportModel getActiuniOrganizateDeArbReport(ActiuniOrganizateDeArbReportFilterModel filter, SecurityManager userSecurity) throws AppException {

		ActiuniOrganizateDeArbReportModel actiuniOrganizateDeArbReportModel = new ActiuniOrganizateDeArbReportModel();
		List<ActiuniOrganizateDeArbReportRowModel> rows = new ArrayList<>();
		actiuniOrganizateDeArbReportModel.setRows(rows);
		List<String> calendarNames = new ArrayList<>();
		calendarNames.add(CalendarConstants.CALENDAR_NAME_VALUE_MISIUNI_CMAS);
		calendarNames.add(CalendarConstants.CALENDAR_NAME_VALUE_DEPLASARI);
		calendarNames.add(CalendarConstants.CALENDAR_NAME_VALUE_INTALNIRE_PVG);
		calendarNames.add(CalendarConstants.CALENDAR_NAME_VALUE_AGA_ARB);
		calendarNames.add(CalendarConstants.CALENDAR_NAME_VALUE_EVENIMENTE_INTALNIRI);
		calendarNames.add(CalendarConstants.CALENDAR_NAME_VALUE_SEDINTE_PARLAMENT);
		calendarNames.add(CalendarConstants.CALENDAR_NAME_VALUE_SEDINTE_COMISII_GL);
		calendarNames.add(CalendarConstants.CALENDAR_NAME_VALUE_CD_ARB);
		List<Long> calendarIds = calendarDao.getCalendarIdsByNames(calendarNames);
		List<CalendarEvent> calendarEvents = calendarDao.getCalendarEventsByCalendarIdsAndStartDateInterval(calendarIds, DateUtils.nullHourMinutesSeconds(filter.getDataInceput()),
				DateUtils.maximizeHourMinutesSeconds(filter.getDataSfarsit()));

		for (CalendarEvent calendarEvent : calendarEvents) {
			if (calendarEvent.getCalendar().getName().equals(CalendarConstants.CALENDAR_NAME_VALUE_SEDINTE_COMISII_GL)) {
				MeetingCalendarEvent meetingEvent = (MeetingCalendarEvent) calendarEvent;
				String documentId = meetingEvent.getDocumentId();
				String documentLocationRealName = meetingEvent.getDocumentLocationRealName();
				if (StringUtils.isNotEmpty(documentId) && StringUtils.isNotEmpty(documentLocationRealName)) {
					DocumentIdentifier documentIdentifierPrezentaForSearch = new DocumentIdentifier(documentLocationRealName, documentId);
					List<Document> documentsForMinutaPrezentaComisiiGlForActiuniOrgArbReport = documentReportPlugin
							.getDocumentsMinutaPrezentaComisiiGlForActiuniOrgArbReport(documentIdentifierPrezentaForSearch);
					if (CollectionUtils.isNotEmpty(documentsForMinutaPrezentaComisiiGlForActiuniOrgArbReport)
							&& documentsForMinutaPrezentaComisiiGlForActiuniOrgArbReport.size() == 1) {
						Document documentMinutaSedintaComGl = documentsForMinutaPrezentaComisiiGlForActiuniOrgArbReport.get(0);
						String ordineDeZiValue = DocumentUtils.getMetadataValueAsString(documentMinutaSedintaComGl, documentTypeService,
								arbConstants.getDocumentMinutaSedintaComisieGLConstants().getOrdineDeZiMetadataName());
						ActiuniOrganizateDeArbReportRowModel row = new ActiuniOrganizateDeArbReportRowModel();
						row.setActiune(meetingEvent.getSubject());
						row.setData(meetingEvent.getStartDate());
						row.setSubiectAgenda(ordineDeZiValue);
						rows.add(row);
					}
				}
			} else {
				if (calendarEvent instanceof MeetingCalendarEvent) {
					MeetingCalendarEvent meetingEvent = (MeetingCalendarEvent) calendarEvent;
					if (StringUtils.isNotEmpty(meetingEvent.getLocation())
							&& meetingEvent.getLocation().trim().toLowerCase().contains(CalendarConstants.CALENDAR_EVENT_LOCATION_VALUE_ARB.trim().toLowerCase())) {
						ActiuniOrganizateDeArbReportRowModel row = new ActiuniOrganizateDeArbReportRowModel();
						row.setActiune(meetingEvent.getSubject());
						row.setData(meetingEvent.getStartDate());
						row.setSubiectAgenda(meetingEvent.getDescription());
						if (meetingEvent.getCalendar().getName().equals(CalendarConstants.CALENDAR_NAME_VALUE_INTALNIRE_PVG)) {
							List<ContinutAgendaModel> continutAgendaList = getAgendaContentFromEvent(meetingEvent, userSecurity);
							if (CollectionUtils.isEmpty(continutAgendaList)) {
								rows.add(row);
							} else {
								continutAgendaList.forEach(continutAgenda -> {
									ActiuniOrganizateDeArbReportRowModel rowClone = SerializationUtils.clone(row);
									rowClone.setDetaliuSubiectAgenda(continutAgenda.getDetaliuSubiect());
									rowClone.setSubiectAgenda(continutAgenda.getSubiect());
									if (StringUtils.isNotEmpty(rowClone.getSubiectAgenda())) {
										rows.add(rowClone);
									}
								});
							}
						} else if (meetingEvent.getCalendar().getName().equals(CalendarConstants.CALENDAR_NAME_VALUE_CD_ARB)) {
							DocumentIdentifier documentIdentifierOrdineDeZiForSearch = new DocumentIdentifier(meetingEvent.getDocumentLocationRealName(),
									meetingEvent.getDocumentId());
							List<Document> documentsForPrezentaCdPvgForActiuniOrgArbReport = documentReportPlugin
									.getDocumentsPrezentaCdPvgForActiuniOrgArbReport(documentIdentifierOrdineDeZiForSearch);
							if (CollectionUtils.isNotEmpty(documentsForPrezentaCdPvgForActiuniOrgArbReport) && documentsForPrezentaCdPvgForActiuniOrgArbReport.size() == 1) {

								Document documentPrezentaCdPvg = documentsForPrezentaCdPvgForActiuniOrgArbReport.get(0);
								List<String> participanti = getParticipantiFromDocumentPrezentaCdPvg(documentPrezentaCdPvg);
								if (CollectionUtils.isEmpty(participanti)) {
									ActiuniOrganizateDeArbReportRowModel rowCdArb = new ActiuniOrganizateDeArbReportRowModel();
									rowCdArb.setActiune(meetingEvent.getSubject());
									rowCdArb.setData(meetingEvent.getStartDate());
									rows.add(rowCdArb);
								} else {
									participanti.forEach(participant -> {
										ActiuniOrganizateDeArbReportRowModel rowCdArb = new ActiuniOrganizateDeArbReportRowModel();
										rowCdArb.setActiune(meetingEvent.getSubject());
										rowCdArb.setData(meetingEvent.getStartDate());
										rowCdArb.setParticipanti(participant);
										rows.add(rowCdArb);
									});
								}
							}
						} else {
							Set<OrganizationEntity> attendees = meetingEvent.getAttendees();
							if (CollectionUtils.isEmpty(attendees)) {
								rows.add(row);
							} else {
								attendees.forEach(attendee -> {
									ActiuniOrganizateDeArbReportRowModel rowForEachParticipant = new ActiuniOrganizateDeArbReportRowModel();
									rowForEachParticipant.setData(row.getData());
									rowForEachParticipant.setActiune(row.getActiune());
									rowForEachParticipant.setSubiectAgenda(row.getSubiectAgenda());
									rowForEachParticipant.setParticipanti(attendee.getDisplayName());
									rows.add(rowForEachParticipant);
								});
							}
						}
					}
				} else if (calendarEvent instanceof AuditCalendarEvent) {
					AuditCalendarEvent auditEvent = (AuditCalendarEvent) calendarEvent;
					if (StringUtils.isNotEmpty(auditEvent.getLocation())
							&& auditEvent.getLocation().trim().toLowerCase().contains(CalendarConstants.CALENDAR_EVENT_LOCATION_VALUE_ARB.trim().toLowerCase())) {
						Set<OrganizationEntity> attendees = auditEvent.getAttendees();
						if (CollectionUtils.isEmpty(attendees)) {
							ActiuniOrganizateDeArbReportRowModel row = new ActiuniOrganizateDeArbReportRowModel();
							row.setActiune(auditEvent.getSubject());
							row.setData(auditEvent.getStartDate());
							rows.add(row);
						} else {
							attendees.forEach(attendee -> {
								ActiuniOrganizateDeArbReportRowModel rowForEachParticipant = new ActiuniOrganizateDeArbReportRowModel();
								rowForEachParticipant.setData(auditEvent.getStartDate());
								rowForEachParticipant.setActiune(auditEvent.getSubject());
								rowForEachParticipant.setParticipanti(attendee.getDisplayName());
								rows.add(rowForEachParticipant);
							});
						}

					}
				}
			}
		}
		return actiuniOrganizateDeArbReportModel;

	}

	private List<String> getParticipantiFromDocumentPrezentaCdPvg(Document documentPrezentaCdPvg) {
		List<String> participanti = new ArrayList<String>();
		DocumentType documentType = documentTypeService.getDocumentTypeById(documentPrezentaCdPvg.getDocumentTypeId());
		Map<Long, List<CollectionInstance>> collectionInstanceListMap = documentPrezentaCdPvg.getCollectionInstanceListMap();
		List<MetadataCollection> metadataCollectionDefinitions = documentType.getMetadataCollections();
		Map<String, Long> metadataDefinitionsIdByName = DocumentUtils.getMetadataDefinitionsMapIdByNameFromMetadataCollections(metadataCollectionDefinitions);

		collectionInstanceListMap.forEach((key, collInstanceList) -> {
			if (key.equals(metadataDefinitionsIdByName.get(arbConstants.getDocumentPrezentaCdPvgConstants().getInformatiiInvitatiArbMetadataName()))) {
				collInstanceList.forEach(collInstance -> {
					List<MetadataInstance> metadataInstanceList = collInstance.getMetadataInstanceList();
					metadataInstanceList.forEach(metadataIntance -> {
						if (metadataIntance.getMetadataDefinitionId()
								.equals(metadataDefinitionsIdByName.get(arbConstants.getDocumentPrezentaCdPvgConstants().getInvitatArbOfInformatiiInvitatiArbMetadataName()))) {
							Long participantId = Long.parseLong(metadataIntance.getValue());
							User participant = userService.getUserById(participantId);
							participanti.add(participant.getDisplayName());
						}
					});
				});
			}
			if (key.equals(metadataDefinitionsIdByName.get(arbConstants.getDocumentPrezentaCdPvgConstants().getInformatiiInvitatiExterniMetadataName()))) {
				collInstanceList.forEach(collInstance -> {
					List<MetadataInstance> metadataInstanceList = collInstance.getMetadataInstanceList();
					String numeInvitatInlocuitor = "";
					String prenumeInvitatInlocuitor = "";
					for( MetadataInstance metadataIntance: metadataInstanceList) {
						if (metadataIntance.getMetadataDefinitionId().equals(
								metadataDefinitionsIdByName.get(arbConstants.getDocumentPrezentaCdPvgConstants().getInvitatAcreditatOfInformatiiInvitatiExterniMetadataName()))) {
							if (StringUtils.isNotEmpty(metadataIntance.getValue())) {
								Long invitatAcreditatId = Long.parseLong(metadataIntance.getValue());
								NomenclatorValueModel invitatAcreditatNomVal = nomenclatorService.getNomenclatorValue(invitatAcreditatId);
								String invitatAcreditatNume = NomenclatorValueUtils.getAttributeValueAsString(invitatAcreditatNomVal,
										NomenclatorConstants.PERSOANE_ATTRIBUTE_KEY_NUME);
								String invitatAcreditatPrenume = NomenclatorValueUtils.getAttributeValueAsString(invitatAcreditatNomVal,
										NomenclatorConstants.PERSOANE_ATTRIBUTE_KEY_PRENUME);
								participanti.add(String.join(" ", invitatAcreditatNume, invitatAcreditatPrenume));
							}
						}
						if (metadataIntance.getMetadataDefinitionId().equals(metadataDefinitionsIdByName
								.get(arbConstants.getDocumentPrezentaCdPvgConstants().getNumeInvitatInlocuitorOfInformatiiInvitatiExterniMetadataName()))) {
							if (StringUtils.isNotEmpty(metadataIntance.getValue())) {
								numeInvitatInlocuitor = metadataIntance.getValue();
							}
						}
						if (metadataIntance.getMetadataDefinitionId().equals(metadataDefinitionsIdByName
								.get(arbConstants.getDocumentPrezentaCdPvgConstants().getPrenumeInvitatInlocuitorOfInformatiiInvitatiExterniMetadataName()))) {
							if (StringUtils.isNotEmpty(metadataIntance.getValue())) {
								prenumeInvitatInlocuitor = metadataIntance.getValue();
							}
						}
						
					}
					if (StringUtils.isNotEmpty(numeInvitatInlocuitor) || StringUtils.isNotEmpty(prenumeInvitatInlocuitor)) {
						participanti.add(String.join(" ", numeInvitatInlocuitor, prenumeInvitatInlocuitor));
					}
				});
			}
			if (key.equals(metadataDefinitionsIdByName.get(arbConstants.getDocumentPrezentaCdPvgConstants().getInformatiiMembriiCdMetadataName()))) {
				collInstanceList.forEach(collInstance -> {
					List<MetadataInstance> metadataInstanceList = collInstance.getMetadataInstanceList();
					metadataInstanceList.forEach(metadataIntance -> {
						if (metadataIntance.getMetadataDefinitionId()
								.equals(metadataDefinitionsIdByName.get(arbConstants.getDocumentPrezentaCdPvgConstants().getMembruOfInformatiiMembriiCdMetadataName()))) {
							Long membruId = Long.parseLong(metadataIntance.getValue());
							NomenclatorValueModel membruNomVal = nomenclatorService.getNomenclatorValue(membruId);
							String membruNume = NomenclatorValueUtils.getAttributeValueAsString(membruNomVal, NomenclatorConstants.MEMBRI_CD_ATTRIBUTE_KEY_NUME);
							String membruPrenume = NomenclatorValueUtils.getAttributeValueAsString(membruNomVal, NomenclatorConstants.MEMBRI_CD_ATTRIBUTE_KEY_PRENUME);
							participanti.add(String.join(" ", membruNume, membruPrenume));
						}
					});
				});
			}
		});

		return participanti;
	}

	private List<ContinutAgendaModel> getAgendaContentFromEvent(MeetingCalendarEvent meetingEvent, SecurityManager userSecurity) throws AppException {
		List<ContinutAgendaModel> continutAgendaList = new ArrayList<ContinutAgendaModel>();
		String documentId = meetingEvent.getDocumentId();
		String documentLocationRealName = meetingEvent.getDocumentLocationRealName();
		if (StringUtils.isNotEmpty(documentId) && StringUtils.isNotEmpty(documentLocationRealName)) {
			Document document = documentService.getDocumentById(documentId, documentLocationRealName, userSecurity);
			DocumentType documentType = documentTypeService.getDocumentTypeById(document.getDocumentTypeId());
			String continutAgendaMetadataName = arbConstants.getDocumentAgendaSedintaPvgConstants().getContinutAgendaMetadataName();
			MetadataCollection continutAgendaMtdColl = DocumentTypeUtils.getMetadataCollectionDefinitionByName(documentType, continutAgendaMetadataName);
			MetadataDefinition subiectMtdDef = DocumentTypeUtils.getMetadataDefinitionFromCollectionByName(documentType, continutAgendaMetadataName,
					arbConstants.getDocumentAgendaSedintaPvgConstants().getSubiectOfContinutAgendaMetadataName());
			MetadataDefinition detaliuSubiectMtdDef = DocumentTypeUtils.getMetadataDefinitionFromCollectionByName(documentType, continutAgendaMetadataName,
					arbConstants.getDocumentAgendaSedintaPvgConstants().getDetaliuSubiectOfcontinutAgendaMetadataName());

			List<CollectionInstance> continutAgendaCollInstances = document.getCollectionInstanceListMap().get(continutAgendaMtdColl.getId());
			continutAgendaCollInstances.forEach(collInstance -> {
				ContinutAgendaModel continutAgenda = new ContinutAgendaModel();
				collInstance.getMetadataInstanceList().forEach(metadataInstance -> {
					if (metadataInstance.getMetadataDefinitionId().equals(subiectMtdDef.getId())) {
						continutAgenda.setSubiect(metadataInstance.getValue());
					}
					if (metadataInstance.getMetadataDefinitionId().equals(detaliuSubiectMtdDef.getId())) {
						continutAgenda.setDetaliuSubiect(metadataInstance.getValue());
					}
				});
				continutAgendaList.add(continutAgenda);
			});
			return continutAgendaList;
		} else {
			throw new RuntimeException("Missing document for meeting calendar event id:" + meetingEvent.getId());
		}

	}

	@Override
	public List<DashboardProiecteRowReportModel> getDashboardProiecteReport(DashboardProiecteFilterModel filter) throws AppException {

		List<Project> projects = projectDao.getDspProjectsByDashboardProiecteFilterModel(filter);
		List<DashboardProiecteRowReportModel> reportRows = new ArrayList<DashboardProiecteRowReportModel>();
		if (projects.isEmpty()) {
			return reportRows;
		}
		for (Project project : projects) {

			DashboardProiecteRowReportModel dashboardProjectRow = new DashboardProiecteRowReportModel();

			dashboardProjectRow.setArieActivitateBancara(
					NomenclatorValueUtils.getAttributeValueAsString(project.getDomeniuBancar(), NomenclatorConstants.DOMENIU_BANCAR_ATTRIBUTE_KEY_DENUMIRE));

			dashboardProjectRow.setImportantaActuala(
					NomenclatorValueUtils.getAttributeValueAsString(project.getGradImportanta(), NomenclatorConstants.IMPORTANTA_PROIECTE_ATTRIBUTE_KEY_GRAD_IMPORTANTA));

			dashboardProjectRow.setImportantaActualaCulare(NomenclatorValueUtils.getAttributeValueAsString(project.getGradImportanta(),
					NomenclatorConstants.IMPORTANTA_PROIECTE_ATTRIBUTE_KEY_CULOARE_GRAD_DE_IMPORTANTA));

			dashboardProjectRow.setDenumireProiect(project.getName());
			dashboardProjectRow.setAbreviereProiect(project.getProjectAbbreviation());
			dashboardProjectRow.setDataInitierii(project.getStartDate());
			dashboardProjectRow.setTermendeFinalizare(project.getEndDate());
			int gradRealizareEstimat = ProjectUtils.getLastEstimation(project);
			dashboardProjectRow.setGradRealizareEstimat(gradRealizareEstimat);

			dashboardProjectRow.setIncadrareProiect(
					NomenclatorValueUtils.getAttributeValueAsString(project.getIncadrareProiect(), NomenclatorConstants.INCADRARI_PROIECTE_ATTRIBUTE_KEY_INCADRARE_PROIECT));

			String incadrareProiectNomAttNumarActiuniValue = NomenclatorValueUtils.getAttributeValueAsString(project.getIncadrareProiect(),
					NomenclatorConstants.INCADRARI_PROIECTE_ATTRIBUTE_KEY_NUMAR_ACTIUNI_ESTIMATE);

			int nrActiuniEstimate = 0;
			if (StringUtils.isNotEmpty(incadrareProiectNomAttNumarActiuniValue)) {
				nrActiuniEstimate = Integer.parseInt(incadrareProiectNomAttNumarActiuniValue);
			}
			long nrZilePerioadaScursa = ProjectUtils.getNrZilePerioadaScursa(project);
			long nrZilePerioadaRamasa = ProjectUtils.getNrZilePerioadaRamasa(project);
			long nrZileProiect = DateUtils.pozitiveNumberDaysBetween(project.getEndDate(), project.getStartDate());
			int gradDeRealizareProiect = calculateGradDeRealizareProiect(project, nrActiuniEstimate, gradRealizareEstimat, nrZilePerioadaScursa, nrZileProiect);
			dashboardProjectRow.setGradRealizareProiect(gradDeRealizareProiect);
			dashboardProjectRow.setPerioadaScursaZile(nrZilePerioadaScursa);
			dashboardProjectRow.setNrZilePerioadaRamasa(nrZilePerioadaRamasa);
			dashboardProjectRow.setNrZileProiect(nrZileProiect);
			dashboardProjectRow.setParametru1(calculateParmetru1Value(nrZilePerioadaScursa, gradDeRealizareProiect, project, nrZileProiect));
			dashboardProjectRow.setParametru2(calculateParmetru2Value(nrZilePerioadaScursa, gradDeRealizareProiect, nrZileProiect));
			dashboardProjectRow.setEvaluareGradRealizare(calculateEvaluareGradUtilizare(gradDeRealizareProiect).toString());
			if (project.getGradImportanta() != null) {
				String gradImportantaValoareValue = NomenclatorValueUtils.getAttributeValueAsString(project.getGradImportanta(),
						NomenclatorConstants.IMPORTANTA_PROIECTE_ATTRIBUTE_KEY_VALOARE_GRAD_IMPORTANTA);
				dashboardProjectRow.setPrioritate(Integer.parseInt(gradImportantaValoareValue));
			}
			dashboardProjectRow.setResponsabilproiect(project.getResponsibleUser().getDisplayName());

			dashboardProjectRow.setDelegatResponsabilArb(null); // TODO nu se stie inca de unde se iau datele
			dashboardProjectRow.setStadiu(project.getStatus().name());
			dashboardProjectRow.setImpact(project.getEvaluareaImpactului());

			dashboardProjectRow.setProiectInitiatDeARB(project.getProiectInitiatArb());

			if (project.getArieDeCuprindere() != null) {
				dashboardProjectRow.setArieDeCuprindere(project.getArieDeCuprindere().getValue());
			}

			dashboardProjectRow.setProiectInitiatDeAltaEntitate(project.getProiectInitiatDeAltaEntitate());

			if (areRespectedFilterConditionsOnCumputedFields(dashboardProjectRow, filter)) {

				List<Task> tasks = taskDao.getInProgressTaskByIdAndProjectAndEndDate(project.getId(), filter.getActiuneDeUrmat(), filter.getDeadlineActiuniDeUrmatDeLa(),
						filter.getDeadlineActiuniDeUrmatPanaLa());
				if (tasks.isEmpty() && filter.getDeadlineActiuniDeUrmatDeLa() == null && filter.getDeadlineActiuniDeUrmatPanaLa() == null) {
					reportRows.add(dashboardProjectRow);
				}

				for (Task task : tasks) {
					DashboardProiecteRowReportModel dashboardProiecteRowClone = SerializationUtils.clone(dashboardProjectRow);
					dashboardProiecteRowClone.setDescriere(task.getDescription());
					dashboardProiecteRowClone.setActiuneDeUrmat(task.getName());
					dashboardProiecteRowClone.setDeadlineActiuneDeUrmat(task.getEndDate());
					reportRows.add(dashboardProiecteRowClone);
				}
			}
		}

		return reportRows;
	}

	private boolean areRespectedFilterConditionsOnCumputedFields(DashboardProiecteRowReportModel dashboardProjectRow, DashboardProiecteFilterModel filter) {

		if ((filter.getEvaluareGradDeRealizare() != null) && !dashboardProjectRow.getEvaluareGradRealizare().equals(filter.getEvaluareGradDeRealizare())) {
			return false;
		}

		if (filter.getGradRealizareProiectDeLa() > dashboardProjectRow.getGradRealizareProiect()) {
			return false;
		}

		if (filter.getGradRealizareProiectPanaLa() < dashboardProjectRow.getGradRealizareProiect()) {
			return false;
		}

		return true;
	}

	private ProjectRealizationDegree calculateEvaluareGradUtilizare(int gradDeRealizareProiect) {

		BigDecimal gradDeRealizareRatio = new BigDecimal((double) gradDeRealizareProiect / 100);
		return ProjectRealizationDegreeProvider.getProjectRealizationDegree(gradDeRealizareRatio);
	}

	private double calculateParmetru2Value(long nrZilePerioadaScursa, int gradRealizareProiect, long nrZileProiect) {

		double gradRealizareProiectRatio = gradRealizareProiect / 100;
		if ((nrZilePerioadaScursa > 0) && (gradRealizareProiect >= 1)) {
			return 1;
		} else if (gradRealizareProiectRatio < 1) {
			return gradRealizareProiect;
		}
		return -1;
	}

	private double calculateParmetru1Value(long nrZilePerioadaScursa, int gradDeRealizareProiect, Project project, long nrZileProiect) {

		double gradRealizareProiectRatio = (double) gradDeRealizareProiect / 100;

		if ((nrZilePerioadaScursa > 0) && (gradRealizareProiectRatio >= 1)) {
			return 1;
		} else if ((nrZilePerioadaScursa > 0) && (gradRealizareProiectRatio < 1) && (nrZileProiect != 0)) {
			return (gradRealizareProiectRatio / nrZilePerioadaScursa / nrZileProiect);
		}
		return -1;
	}

	private int calculateGradDeRealizareProiect(Project project, int nrActiuniEstimate, int gradDeRealizareEstimat, long nrZilePerioadaScursa, long nrZileProiect) {

		int nrActiuniRealizate = projectDao.getProjectTasksByStatus(project.getId(), TaskStatus.FINALIZED).size();
		int nrActiuniRealizateInTermen = projectDao.getNrTaskuriProiectFinalizateInTermen(project.getId());

		int pondereActiuniRealizatePerEstimate = Integer.parseInt(
				parametersDao.getByName(ParameterConstants.PARAM_NAME_PROIECT_GRAD_REALIZARE_PONDERE_IN_PROCENTE_PENTRU_NR_ACTIUNI_REALIZATE_PE_NR_ACTIUNI_ESTIMATE).getValue());
		int pondereActiuniRealizateInTermen = Integer
				.parseInt(parametersDao.getByName(ParameterConstants.PARAM_NAME_PROIECT_GRAD_REALIZARE_PONDERE_IN_PROCENTE_PENTRU_REALIZARE_ACTIUNI_IN_TERMEN).getValue());
		int pondereEstimareResponsabil = Integer
				.parseInt(parametersDao.getByName(ParameterConstants.PARAM_NAME_PROIECT_GRAD_REALIZARE_PONDERE_IN_PROCENTE_PENTRU_ESTIMARE_RESPONSABIL).getValue());

		if ((nrActiuniRealizate == 0) || (nrActiuniEstimate == 0) || (nrZilePerioadaScursa == 0) || (nrZileProiect == 0)) {
			return 0;
		}
		double realizariActiuniProiect = ((((double) nrActiuniRealizate / nrActiuniEstimate) * pondereActiuniRealizatePerEstimate)
				+ (((double) nrActiuniRealizateInTermen / nrActiuniRealizate) * pondereActiuniRealizateInTermen)
				+ (((double) gradDeRealizareEstimat / 100) * pondereEstimareResponsabil)) / 100;

		double pondereGradDeRealizare = realizariActiuniProiect / (nrZilePerioadaScursa / nrZileProiect);

		if (pondereGradDeRealizare > 1) {
			return 100;
		} else {
			return (int) (pondereGradDeRealizare * 100);
		}
	}

	public void setDocumentDspConstants(DocumentDspConstants documentDspConstants) {
		this.documentDspConstants = documentDspConstants;
	}

	public void setParametersDao(ParametersDao parametersDao) {
		this.parametersDao = parametersDao;
	}

	@Override
	public DashboardProiecteFilterBundle getDashboardProiecteFilterBundle() {
		DashboardProiecteFilterBundle bundle = new DashboardProiecteFilterBundle();

		List<String> abrievieri = projectDao.getAllAbrevieriProiecte();
		List<SimpleListItemModel> abrievieriItemModels = new ArrayList<>();
		for (String string : abrievieri) {
			SimpleListItemModel item = new SimpleListItemModel(string);
			abrievieriItemModels.add(item);
		}
		bundle.setAbrevieriProiect(abrievieriItemModels);

		List<String> taskNames = taskDao.getAllTaskNames();
		List<SimpleListItemModel> taskNamesItemModels = new ArrayList<>();
		for (String string : taskNames) {
			SimpleListItemModel item = new SimpleListItemModel(string);
			taskNamesItemModels.add(item);
		}
		bundle.setTasks(taskNamesItemModels);

		Set<User> users = groupService.getGroupByName(GroupConstants.RESPONSABILI_PROIECT_ACTIVITATE_NAME).getUsers();
		List<SimpleListItemModel> usersItemModel = new ArrayList<>();
		for (User user : users) {
			usersItemModel.add(new SimpleListItemModel(user.getId().toString(), user.getDisplayName()));
		}
		bundle.setResponsibleUsers(usersItemModel);

		DocumentType documentType = documentTypeService.getDocumentTypeByName(documentDspConstants.getDocumentTypeName());
		List<SimpleListItemModel> ariidDeCuprindereItemModel = new ArrayList<>();
		ListMetadataDefinition listMetadataDefinition = (ListMetadataDefinition) DocumentTypeUtils.getMetadataDefinitionByName(documentType,
				documentDspConstants.getArieDeCuprindereMetadataName());
		for (ListMetadataItem listMetadataItem : listMetadataDefinition.getListItems()) {
			ariidDeCuprindereItemModel.add(new SimpleListItemModel(listMetadataItem.getValue(), listMetadataItem.getLabel()));
		}
		bundle.setArieDeCuprindereList(ariidDeCuprindereItemModel);
		List<String> nomenclatorCodes = new ArrayList<>();
		nomenclatorCodes.add(NomenclatorConstants.NOMENCLATOR_CODE_IMPORTANTA_PROIECTE);
		nomenclatorCodes.add(NomenclatorConstants.NOMENCLATOR_CODE_DOMENII_BANCARE);
		nomenclatorCodes.add(NomenclatorConstants.NOMENCLATOR_CODE_INCADRARI_PROIECTE);
		Map<String, Long> nomenclatorIdByCodeMapByNomenclatorCodes = nomenclatorService.getNomenclatorIdByCodeMapByNomenclatorCodes(nomenclatorCodes);
		bundle.setNomenclatorDomeniiBancareId(nomenclatorIdByCodeMapByNomenclatorCodes.get(NomenclatorConstants.NOMENCLATOR_CODE_DOMENII_BANCARE));
		bundle.setNomenclatorImportantaProiecteId(nomenclatorIdByCodeMapByNomenclatorCodes.get(NomenclatorConstants.NOMENCLATOR_CODE_IMPORTANTA_PROIECTE));
		bundle.setNomenclatorIncadrariProiecteId(nomenclatorIdByCodeMapByNomenclatorCodes.get(NomenclatorConstants.NOMENCLATOR_CODE_INCADRARI_PROIECTE));
		return bundle;
	}

	public void setGroupService(GroupService groupService) {
		this.groupService = groupService;
	}

	@Override
	public PrezentaAgaReportModel getPrezentaAgaReport(PrezentaAgaFilterModel filter) throws AppException {

		DocumentType prezentaAgaDocumentType = documentTypeService.getDocumentTypeByName(arbConstants.getDocumentPrezentaAgaConstants().getDocumentTypeName());

		List<Document> reportDocuments = documentReportPlugin.getDocumentsForPrezentaAgaReport(filter);

		Map<Long, String> nomenclatorInstitutiiUiAttrValuesAsMap = nomenclatorService
				.getJoinedNomenclatorUiAtributesValuesAsMapByNomenclatorCode(NomenclatorConstants.INSTITUTII_NOMENCLATOR_CODE);
		Map<Long, String> nomenclatorPersoaneUiAttrValuesAsMap = nomenclatorService
				.getJoinedNomenclatorUiAtributesValuesAsMapByNomenclatorCode(NomenclatorConstants.PERSOANE_NOMENCLATOR_CODE);

		Map<Long, List<PrezentaAgaReportRowModel>> rowsReportAsMap = new HashMap<>();

		if (CollectionUtils.isNotEmpty(reportDocuments)) {
			for (Document reportDocument : reportDocuments) {

				List<CollectionInstance> metadataCollectionInfoMembrii = DocumentUtils.getMetadataCollectionInstance(reportDocument, prezentaAgaDocumentType, arbConstants.getDocumentPrezentaAgaConstants().getPrezentaMembriiMetadataName());
				if (CollectionUtils.isNotEmpty(metadataCollectionInfoMembrii)) {
					for (CollectionInstance metadataInfoMembrii : metadataCollectionInfoMembrii) {
	
						PrezentaAgaReportRowModel reportRow = new PrezentaAgaReportRowModel();
	
						String infoPrezentaMembriiMetadataName = arbConstants.getDocumentPrezentaAgaConstants().getPrezentaMembriiMetadataName();
						MetadataDefinition metadataDefinitionInstitutie = DocumentTypeUtils.getMetadataDefinitionFromCollectionByName(prezentaAgaDocumentType,
								infoPrezentaMembriiMetadataName, arbConstants.getDocumentPrezentaAgaConstants().getIntitutieOfPrezentaMembriiMetadataName());
						MetadataDefinition metadataDefinitionFunctie = DocumentTypeUtils.getMetadataDefinitionFromCollectionByName(prezentaAgaDocumentType,
								infoPrezentaMembriiMetadataName,
								arbConstants.getDocumentPrezentaAgaConstants().getFunctieParticipantAcreditatInlocuitorOfPrezentaMembriiMetadataName());
						MetadataDefinition metadataDefinitionParticipantAcreditat = DocumentTypeUtils.getMetadataDefinitionFromCollectionByName(prezentaAgaDocumentType,
								infoPrezentaMembriiMetadataName, arbConstants.getDocumentPrezentaAgaConstants().getParticipantAcreditatOfPrezentaMembriiMetadataName());
						MetadataDefinition metadataDefinitionNumeParticipantInlocuitor = DocumentTypeUtils.getMetadataDefinitionFromCollectionByName(prezentaAgaDocumentType,
								infoPrezentaMembriiMetadataName, arbConstants.getDocumentPrezentaAgaConstants().getNumeParticipantInlocuitorOfPrezentaMembriiMetadataName());
						MetadataDefinition metadataDefinitionPrenumeParticipantInlocuitor = DocumentTypeUtils.getMetadataDefinitionFromCollectionByName(prezentaAgaDocumentType,
								infoPrezentaMembriiMetadataName, arbConstants.getDocumentPrezentaAgaConstants().getPrenumeParticipantInlocuitorOfPrezentaMembriiMetadataName());
						MetadataDefinition metadataDefinitionImputernicire = DocumentTypeUtils.getMetadataDefinitionFromCollectionByName(prezentaAgaDocumentType,
								infoPrezentaMembriiMetadataName, arbConstants.getDocumentPrezentaAgaConstants().getImputernicireOfPrezentaMembriiMetadataName());
						MetadataDefinition metadataDefinitionCalitateParticipant = DocumentTypeUtils.getMetadataDefinitionFromCollectionByName(prezentaAgaDocumentType,
								infoPrezentaMembriiMetadataName, arbConstants.getDocumentPrezentaAgaConstants().getCalitateParticipantOfPrezentaMembriiMetadataName());
	
						boolean areFilterConditionRespected = true;
						Long institutieId = null;
						for (MetadataInstance metadataInstance : metadataInfoMembrii.getMetadataInstanceList()) {
							if (metadataInstance.getMetadataDefinitionId().equals(metadataDefinitionInstitutie.getId())) {
								institutieId = Long.parseLong(metadataInstance.getValue());
								if (filter.getBancaId() != null && !institutieId.equals(filter.getBancaId())) {
									areFilterConditionRespected = false;
								}
							}
							if (metadataInstance.getMetadataDefinitionId().equals(metadataDefinitionFunctie.getId())) {
								String functie = metadataInstance.getValue();
								if (filter.getFunctie() != null && !functie.toUpperCase().contains(filter.getFunctie().toUpperCase())) {
									areFilterConditionRespected = false;
								}
								reportRow.setFunctie(metadataInstance.getValue());
							}
							if (metadataInstance.getMetadataDefinitionId().equals(metadataDefinitionParticipantAcreditat.getId())) {
								try {
									Long persoanaId = Long.parseLong(metadataInstance.getValue());
									reportRow.setConfirmare(nomenclatorPersoaneUiAttrValuesAsMap.get(persoanaId));
								} catch (NumberFormatException nfe) {
								}
							}
							if (metadataInstance.getMetadataDefinitionId().equals(metadataDefinitionNumeParticipantInlocuitor.getId())) {
								reportRow.setConfirmare(metadataInstance.getValue() + " ");
							}
							if (metadataInstance.getMetadataDefinitionId().equals(metadataDefinitionPrenumeParticipantInlocuitor.getId())) {
								reportRow.setConfirmare(reportRow.getConfirmare() + metadataInstance.getValue());
							}
							if (metadataInstance.getMetadataDefinitionId().equals(metadataDefinitionImputernicire.getId())) {
								reportRow.setNecesitaImputernicire(metadataInstance.getValue());
							}
							if (metadataInstance.getMetadataDefinitionId().equals(metadataDefinitionCalitateParticipant.getId())) {
								if (metadataInstance.getValue().equals(PrezentaAgaReportConstants.CALITATE_PARTICIPANT_PRESEDINTE)) {
									reportRow.setPrezentaMembriNivelPresedinte(reportRow.getConfirmare());
								}
								if (metadataInstance.getValue().equals(PrezentaAgaReportConstants.CALITATE_PARTICIPANT_VICEPRESEDINTE)) {
									reportRow.setPrezentaMembriNivelVicepresedinte(reportRow.getConfirmare());
								}
								if (metadataInstance.getValue().equals(PrezentaAgaReportConstants.CALITATE_PARTICIPANT_IMPUTERNICIT)) {
									reportRow.setPrezentaMembriNivelImputernicit(reportRow.getConfirmare());
								}
							}
	
						}
	
						if (areFilterConditionRespected && institutieId != null) {
							reportRow.setTotalPrezenta(1);
							if (!rowsReportAsMap.containsKey(institutieId)) {
								List<PrezentaAgaReportRowModel> items = new ArrayList<PrezentaAgaReportRowModel>();
								PrezentaAgaReportRowModel row = new PrezentaAgaReportRowModel();
	
								row.setBanca(nomenclatorInstitutiiUiAttrValuesAsMap.get(institutieId));
	
								if (filter.getFunctie() == null || (filter.getFunctie() != null && reportRow.getFunctie() != null)) {
									items.add(row);
									items.add(reportRow);
									rowsReportAsMap.put(institutieId, items);
								}
	
							} else {
								List<PrezentaAgaReportRowModel> items = rowsReportAsMap.get(institutieId);
	
								if (filter.getFunctie() == null || (filter.getFunctie() != null && reportRow.getFunctie() != null)) {
									items.add(reportRow);
									rowsReportAsMap.put(institutieId, items);
								}
							}
						}
					}
				}

			}
		}

		PrezentaAgaReportModel report = new PrezentaAgaReportModel();
		List<PrezentaAgaReportRowModel> rows = new ArrayList<PrezentaAgaReportRowModel>();
		
		int prezentaActualaTotalPresedinti = 0;
		int prezentaActualaTotalVicepresedinti = 0;
		int prezentaActualaTotalInlocuitori = 0;
		int prezentaActualaTotalPrezenta = 0;
		int prezentaActualaTotalMembriVotanti = 0;
		
		int nrCrtBanca = 1;

		for (List<PrezentaAgaReportRowModel> rowReport : rowsReportAsMap.values()) {
			PrezentaAgaReportRowModel banca = rowReport.get(0);

			banca.setTotalPrezenta(rowReport.size() - 1);
			banca.setNrCrt(nrCrtBanca);

			nrCrtBanca++;

			if (rowReport.size() > 1) {
				banca.setMembriVotanti(1);
			}
			rowReport.set(0, banca);

			prezentaActualaTotalMembriVotanti += banca.getMembriVotanti();
			prezentaActualaTotalPrezenta += banca.getTotalPrezenta();

			boolean existsPresedinteOfBanca = false;
			boolean existsVicepresedinteOfBanca = false;
			boolean existsImputernicitOfBanca = false;

			for (PrezentaAgaReportRowModel row : rowReport) {
				rows.add(row);

				if (row.getPrezentaMembriNivelPresedinte() != null) {
					existsPresedinteOfBanca = true;
				}
				if (row.getPrezentaMembriNivelVicepresedinte() != null) {
					existsVicepresedinteOfBanca = true;
				}
				if (row.getPrezentaMembriNivelImputernicit() != null) {
					existsImputernicitOfBanca = true;
				}
			}

			if (existsPresedinteOfBanca) {
				prezentaActualaTotalPresedinti += 1;
			}
			if (existsVicepresedinteOfBanca) {
				prezentaActualaTotalVicepresedinti += 1;
			}
			if (existsImputernicitOfBanca) {
				prezentaActualaTotalInlocuitori += 1;
			}
		}

		report.setRows(rows);
		
		// Prezenta Actuala
		report.setPrezentaActualaPresedinte(prezentaActualaTotalPresedinti);
		report.setPrezentaActualaVicepresedinte(prezentaActualaTotalVicepresedinti);
		report.setPrezentaActualaInlocuitor(prezentaActualaTotalInlocuitori);
		report.setPrezentaActualaTotal(prezentaActualaTotalPrezenta);
		report.setPrezentaActualaMembriVotanti(prezentaActualaTotalMembriVotanti);
		
		// Membri Asociatie (nr)
		Long nrMembriAsociatie = nomenclatorValueDao.countInstitutiiMembreARBNeradiateWithoutARB();
		report.setMembriAsociatie(nrMembriAsociatie.intValue());
		
		// Prezenta Necesara 2/3	
		BigDecimal nrMembriAsociatieAsBigDecimal = new BigDecimal(nrMembriAsociatie);
		BigDecimal prezentaNecesara = nrMembriAsociatieAsBigDecimal.multiply(BigDecimal.valueOf(2)).divide(BigDecimal.valueOf(3), 0, RoundingMode.HALF_UP);
		report.setPrezentaNecesara(prezentaNecesara);
		
		// Rest Necesar Prezenta
		Integer restPrezentaNecesara = 0;
		if (prezentaActualaTotalMembriVotanti < prezentaNecesara.intValue()) {
			restPrezentaNecesara = prezentaNecesara.intValue() - prezentaActualaTotalMembriVotanti;
		}
		report.setRestNecesarPrezenta(restPrezentaNecesara);
		
		// Prezenta Procent
		if (prezentaActualaTotalMembriVotanti == 0) {
			report.setPrezentaProcentPresedinte(BigDecimal.ZERO);
			report.setPrezentaProcentVicepresedinte(BigDecimal.ZERO);
			report.setPrezentaProcentInlocuitor(BigDecimal.ZERO);
			report.setPrezentaProcentMembriVotanti(BigDecimal.ZERO);
		} else {
			
			BigDecimal prezentaProcentPresedinte = BigDecimal.valueOf(prezentaActualaTotalPresedinti).divide(nrMembriAsociatieAsBigDecimal, 2, RoundingMode.HALF_UP).multiply(BigDecimal.valueOf(100));
			BigDecimal prezentaProcentVicepresedinte = BigDecimal.valueOf(prezentaActualaTotalVicepresedinti).divide(nrMembriAsociatieAsBigDecimal, 2, RoundingMode.HALF_UP).multiply(BigDecimal.valueOf(100));
			BigDecimal prezentaProcentInlocuitori = BigDecimal.valueOf(prezentaActualaTotalInlocuitori).divide(nrMembriAsociatieAsBigDecimal, 2, RoundingMode.HALF_UP).multiply(BigDecimal.valueOf(100));
			BigDecimal prezentaProcentMembriVotanti = BigDecimal.valueOf(prezentaActualaTotalMembriVotanti).divide(nrMembriAsociatieAsBigDecimal, 2, RoundingMode.HALF_UP).multiply(BigDecimal.valueOf(100));
			
			report.setPrezentaProcentPresedinte(prezentaProcentPresedinte);
			report.setPrezentaProcentVicepresedinte(prezentaProcentVicepresedinte);
			report.setPrezentaProcentInlocuitor(prezentaProcentInlocuitori);
			report.setPrezentaProcentMembriVotanti(prezentaProcentMembriVotanti);
		}
		
		// Necesar Adoptare
		BigDecimal necesarAdoptare = BigDecimal.valueOf(prezentaActualaTotalMembriVotanti).multiply(BigDecimal.valueOf(3)).divide(BigDecimal.valueOf(4), 0, RoundingMode.HALF_UP);
		report.setNecesarAdoptare(necesarAdoptare);
		
		// Jumatate plus unu
		BigDecimal jumatatePlusUnu = BigDecimal.ONE;
		if (nrMembriAsociatie.intValue() > 1) {
			jumatatePlusUnu = nrMembriAsociatieAsBigDecimal.divide(BigDecimal.valueOf(2), 0, RoundingMode.HALF_UP).add(BigDecimal.ONE);
		}
		report.setJumatePlusUnu(jumatatePlusUnu);
		
		// Necesar Adoptare Hotarare
		BigDecimal necesarAdoptareHotarare = necesarAdoptare.max(jumatatePlusUnu);
		report.setNecesarAdoptareHotarare(necesarAdoptareHotarare);
		
		// Necesar pentru vot secret
		BigDecimal necesarPentruVotSecret = BigDecimal.valueOf(prezentaActualaTotalMembriVotanti).divide(BigDecimal.valueOf(5), 0, RoundingMode.UP);
		report.setNecesarPentruVotSecret(necesarPentruVotSecret.intValue());

		return report;
	}

	@Override
	public NumarParticipantiSedinteComisieGlReportBundle getNumarParticipantiSedinteComisieGlReportBundle() throws AppException {
		DocumentType documentType = documentTypeService.getDocumentTypeByName(arbConstants.getDocumentPrezentaComisieGlConstants().getDocumentTypeName());
		ListMetadataDefinition listMetadataDefinition = (ListMetadataDefinition) DocumentTypeUtils.getMetadataDefinitionFromCollectionByName(documentType,
				arbConstants.getDocumentPrezentaComisieGlConstants().getInformatiiParticipantiMetadataName(),
				arbConstants.getDocumentPrezentaComisieGlConstants().getCalitateParticipantOfInformatiiParticipantiMetadataName());

		NumarParticipantiSedinteComisieGlReportBundle numarParticipantiSedinteComisieGlReportBundle = new NumarParticipantiSedinteComisieGlReportBundle();
		List<ListMetadataItemModel> calitateMembruItems = new ArrayList<ListMetadataItemModel>();

		for (ListMetadataItem listMetadataItem : listMetadataDefinition.getListItems()) {
			ListMetadataItemModel calitateMembruItem = new ListMetadataItemModel();

			calitateMembruItem.setLabel(listMetadataItem.getLabel());
			calitateMembruItem.setValue(listMetadataItem.getValue());

			calitateMembruItems.add(calitateMembruItem);
		}

		numarParticipantiSedinteComisieGlReportBundle.setCalitateMembruItems(calitateMembruItems);

		return numarParticipantiSedinteComisieGlReportBundle;
	}

	@Override
	public NumarParticipantiSedinteComisieGlReportModel getNumarParticipantiSedinteComisieGlReport(NumarParticipantiSedinteComisieGlReportFilterModel filter) throws AppException {
		
		// normalize filter
		if (StringUtils.isNotBlank(filter.getFunctie())) {
			filter.setFunctie(filter.getFunctie().trim());
		}
				
		Map<Long, String> nomenclatorInstitutiiUiAttrValuesAsMap = nomenclatorService
				.getJoinedNomenclatorUiAtributesValuesAsMapByNomenclatorCode(NomenclatorConstants.INSTITUTII_NOMENCLATOR_CODE);
		Map<Long, String> nomenclatorComisiiUiAttrValuesAsMap = nomenclatorService
				.getJoinedNomenclatorUiAtributesValuesAsMapByNomenclatorCode(NomenclatorConstants.COMISII_SAU_GL_NOMENCLATOR_CODE);

		DocumentType prezentaComisiiGlDocumentType = documentTypeService.getDocumentTypeByName(arbConstants.getDocumentPrezentaComisieGlConstants().getDocumentTypeName());
		List<Document> reportDocuments = documentReportPlugin.getDocumentsForNumarParticipantiSedinteComisieGlReport(filter);

		DocumentType prezentaComisieGLDocumentType = documentTypeService.getDocumentTypeByName(arbConstants.getDocumentPrezentaComisieGlConstants().getDocumentTypeName());

		Map<Long, List<NumarParticipantiSedinteComisieGlReportRowModel>> rowsReportAsMap = new HashMap<>();
		
		if (CollectionUtils.isNotEmpty(reportDocuments)) {
			for (Document reportDocument : reportDocuments) {
				WorkflowInstance workflowInstance = workflowInstanceDao.getForDocument(reportDocument.getId());
				
				if (workflowInstance == null || workflowInstance.getStatus().equals(WorkflowInstance.STATUS_FINNISHED)) {
					Date dataSedinta = DocumentUtils.getMetadataDateTimeValue(reportDocument, prezentaComisiiGlDocumentType,
							arbConstants.getDocumentPrezentaComisieGlConstants().getDataInceputMetadataName());
					Long comisieId = DocumentUtils.getMetadataNomenclatorValue(reportDocument, prezentaComisiiGlDocumentType,
							arbConstants.getDocumentPrezentaComisieGlConstants().getDenumireComisieGlMetadataName());
					Long responsabilId = DocumentUtils.getMetadataUserValue(reportDocument, prezentaComisiiGlDocumentType,
							arbConstants.getDocumentPrezentaComisieGlConstants().getResponsabilMetadataName());

					List<CollectionInstance> metadataCollectionInfoInformatiiParticipanti = DocumentUtils.getMetadataCollectionInstance(reportDocument,
							prezentaComisieGLDocumentType, arbConstants.getDocumentPrezentaComisieGlConstants().getInformatiiParticipantiMetadataName());

					if (metadataCollectionInfoInformatiiParticipanti != null) {
						for (CollectionInstance metadataInfoMembrii : metadataCollectionInfoInformatiiParticipanti) {

							NumarParticipantiSedinteComisieGlReportRowModel row = new NumarParticipantiSedinteComisieGlReportRowModel();

							String infoInformatiiParticipantiMetadataName = arbConstants.getDocumentPrezentaComisieGlConstants().getInformatiiParticipantiMetadataName();

							MetadataDefinition metadataDefinitionBanca = DocumentTypeUtils.getMetadataDefinitionFromCollectionByName(prezentaComisiiGlDocumentType,
									infoInformatiiParticipantiMetadataName,
									arbConstants.getDocumentPrezentaComisieGlConstants().getNumeInstitutieOfInformatiiParticipantiMetadataName());
							MetadataDefinition metadataDefinitionMembruAcreditat = DocumentTypeUtils.getMetadataDefinitionFromCollectionByName(prezentaComisiiGlDocumentType,
									infoInformatiiParticipantiMetadataName,
									arbConstants.getDocumentPrezentaComisieGlConstants().getMembruAcreditatOfInformatiiParticipantiMetadataName());
							MetadataDefinition metadataDefinitionNumeParticipantInlocuitor = DocumentTypeUtils.getMetadataDefinitionFromCollectionByName(
									prezentaComisiiGlDocumentType, infoInformatiiParticipantiMetadataName,
									arbConstants.getDocumentPrezentaComisieGlConstants().getNumeParticipantInlocuitorOfInformatiiParticipantiMetadataName());
							MetadataDefinition metadataDefinitionPrenumeParticipantInlocuitor = DocumentTypeUtils.getMetadataDefinitionFromCollectionByName(
									prezentaComisiiGlDocumentType, infoInformatiiParticipantiMetadataName,
									arbConstants.getDocumentPrezentaComisieGlConstants().getPrenumeParticipantInlocuitorOfInformatiiParticipantiMetadataName());
							MetadataDefinition metadataDefinitionFunctie = DocumentTypeUtils.getMetadataDefinitionFromCollectionByName(prezentaComisiiGlDocumentType,
									infoInformatiiParticipantiMetadataName,
									arbConstants.getDocumentPrezentaComisieGlConstants().getFunctieMembruOfInformatiiParticipantiMetadataName());
							MetadataDefinition metadataDefinitionDepartament = DocumentTypeUtils.getMetadataDefinitionFromCollectionByName(prezentaComisiiGlDocumentType,
									infoInformatiiParticipantiMetadataName,
									arbConstants.getDocumentPrezentaComisieGlConstants().getDepartamentOfInformatiiParticipantiMetadataName());
							MetadataDefinition metadataDefinitionCalitateMembru = DocumentTypeUtils.getMetadataDefinitionFromCollectionByName(prezentaComisiiGlDocumentType,
									infoInformatiiParticipantiMetadataName,
									arbConstants.getDocumentPrezentaComisieGlConstants().getCalitateParticipantOfInformatiiParticipantiMetadataName());

							boolean areFilterConditionRespected = true;
							Long bancaId = null;

							if (filter.getComisieId() != null && !comisieId.equals(filter.getComisieId())) {
								areFilterConditionRespected = false;
							}
							if (filter.getDataSedintaDeLa() != null && dataSedinta.before(DateUtils.nullHourMinutesSeconds(filter.getDataSedintaDeLa()))) {
								areFilterConditionRespected = false;
							}
							if (filter.getDataSedintaPanaLa() != null && dataSedinta.after(DateUtils.maximizeHourMinutesSeconds(filter.getDataSedintaPanaLa()))) {
								areFilterConditionRespected = false;
							}
							if (filter.getResponsabilId() != null && !responsabilId.equals(filter.getResponsabilId())) {
								areFilterConditionRespected = false;
							}

							row.setNumeComisie(nomenclatorComisiiUiAttrValuesAsMap.get(comisieId));
							row.setDataSedinta(dataSedinta);
							row.setResponsabilComisie(userService.getDisplayName(responsabilId));

							for (MetadataInstance metadataInstance : metadataInfoMembrii.getMetadataInstanceList()) {
								if (metadataInstance.getMetadataDefinitionId().equals(metadataDefinitionBanca.getId())) {
									bancaId = Long.parseLong(metadataInstance.getValue());
									if (filter.getBancaId() != null && !bancaId.equals(filter.getBancaId())) {
										areFilterConditionRespected = false;
									}
									row.setNumeBanca(nomenclatorInstitutiiUiAttrValuesAsMap.get(bancaId));
								}
								if (metadataInstance.getMetadataDefinitionId().equals(metadataDefinitionMembruAcreditat.getId())) {
									if (StringUtils.isNotBlank(metadataInstance.getValue())) {
										row.setParticipanti(metadataInstance.getValue());
									}
								}
								if (metadataInstance.getMetadataDefinitionId().equals(metadataDefinitionNumeParticipantInlocuitor.getId())) {
									if (StringUtils.isNotBlank(metadataInstance.getValue())) {
										row.setParticipanti(metadataInstance.getValue());
									}
								}

								if (metadataInstance.getMetadataDefinitionId().equals(metadataDefinitionPrenumeParticipantInlocuitor.getId())) {
									if (StringUtils.isNotBlank(metadataInstance.getValue())) {
										row.setParticipanti(row.getParticipanti() + " " + metadataInstance.getValue());
									}
								}
								if (metadataInstance.getMetadataDefinitionId().equals(metadataDefinitionFunctie.getId())) {
									String functie = metadataInstance.getValue();
									if (StringUtils.isNotBlank(filter.getFunctie())) {
										if (StringUtils.isBlank(functie) || (!functie.toUpperCase().contains(filter.getFunctie().toUpperCase().trim()))) {
											areFilterConditionRespected = false;
										}									
									}
									row.setFunctie(functie);
								}
								if (metadataInstance.getMetadataDefinitionId().equals(metadataDefinitionDepartament.getId())) {
									String deoartament = metadataInstance.getValue();
									if (filter.getDepartament() != null && !deoartament.toUpperCase().contains(filter.getDepartament().toUpperCase())) {
										areFilterConditionRespected = false;
									}
									row.setDepartament(metadataInstance.getValue());
								}
								if (metadataInstance.getMetadataDefinitionId().equals(metadataDefinitionCalitateMembru.getId())) {
									String calitate = metadataInstance.getValue();
									if (filter.getCalitateMembru() != null && !calitate.equals(filter.getCalitateMembru())) {
										areFilterConditionRespected = false;
									}
									String calitateMembru = DocumentUtils.getMetadataListLabelByValue(prezentaComisieGLDocumentType, infoInformatiiParticipantiMetadataName,
											arbConstants.getDocumentPrezentaComisieGlConstants().getCalitateParticipantOfInformatiiParticipantiMetadataName(), calitate);
									row.setCalitateMembru(calitateMembru);
								}
							}
							
							if (StringUtils.isNotBlank(filter.getFunctie()) && DocumentUtils.isNotMetadataCompleted(metadataDefinitionFunctie.getId(), metadataInfoMembrii.getMetadataInstanceList())) {
								areFilterConditionRespected = false;
							}

							if (areFilterConditionRespected && bancaId != null) {
								if (!rowsReportAsMap.containsKey(bancaId)) {
									List<NumarParticipantiSedinteComisieGlReportRowModel> items = new ArrayList<NumarParticipantiSedinteComisieGlReportRowModel>();
									items.add(row);
									rowsReportAsMap.put(bancaId, items);
								} else {
									List<NumarParticipantiSedinteComisieGlReportRowModel> items = rowsReportAsMap.get(bancaId);
									items.add(row);
									rowsReportAsMap.put(bancaId, items);
								}
							}
						}
					}
				}
			}
		}

		NumarParticipantiSedinteComisieGlReportModel report = new NumarParticipantiSedinteComisieGlReportModel();
		List<NumarParticipantiSedinteComisieGlReportRowModel> rows = new ArrayList<NumarParticipantiSedinteComisieGlReportRowModel>();

		int totalParticipanti = 0;

		for (List<NumarParticipantiSedinteComisieGlReportRowModel> rowReport : rowsReportAsMap.values()) {
			int totalparticipantiBanca = 0;
			for (NumarParticipantiSedinteComisieGlReportRowModel row : rowReport) {
				rows.add(row);
				totalparticipantiBanca++;
			}

			NumarParticipantiSedinteComisieGlReportRowModel rowTotalBanca = new NumarParticipantiSedinteComisieGlReportRowModel();
			rowTotalBanca.setNumeBanca(rowReport.get(0).getNumeBanca());
			rowTotalBanca.setResponsabilComisie(String.valueOf(totalparticipantiBanca));
			rows.add(rowTotalBanca);

			totalParticipanti += totalparticipantiBanca;
		}

		report.setTotalGeneral(totalParticipanti);
		report.setRows(rows);

		return report;
	}

	@Override
	public NumarSedinteComisieGlReportModel getNumarSedinteComisieGlReport(NumarSedinteComisieGlReportFilterModel filter) throws AppException {
						
		Map<Long, String> nomenclatorComisiiUiAttrValuesAsMap = nomenclatorService
				.getJoinedNomenclatorUiAtributesValuesAsMapByNomenclatorCode(NomenclatorConstants.COMISII_SAU_GL_NOMENCLATOR_CODE);

		DocumentType prezentaComisiiGlDocumentType = documentTypeService.getDocumentTypeByName(arbConstants.getDocumentPrezentaComisieGlConstants().getDocumentTypeName());
		List<Document> reportDocuments = documentReportPlugin.getDocumentsForNumarSedinteComisieGlReport(filter);

		Long comisieCategoryId = null;
		Long glCategoryId = null;

		List<NomenclatorValueModel> grupuriComisieGl = nomenclatorService.getNomenclatorValuesByNomenclatorCode(NomenclatorConstants.CATEGORII_COMISII_SAU_GL_NOMENCLATOR_CODE);

		for (NomenclatorValueModel grup : grupuriComisieGl) {
			String categoryGrup = NomenclatorValueUtils.getAttributeValueAsString(grup, NomenclatorConstants.NOMENCLATOR_CATEGORIE_COMISII_GL_ATTRIBUTE_KEY_CATEGORIE);
			if (categoryGrup.equals(NomenclatorConstants.NOMENCLATOR_CATEGORIE_COMISII_GL_ATTRIBUTE_CATEGORIE_VALUE_FOR_COMISIE)) {
				comisieCategoryId = grup.getId();
			} else {
				glCategoryId = grup.getId();
			}
		}

		NumarSedinteComisieGlReportModel report = new NumarSedinteComisieGlReportModel();
		List<NumarSedinteComisieGlReportRowModel> rows = new ArrayList<NumarSedinteComisieGlReportRowModel>();

		int totalComisie = 0;
		int totalGl = 0;
		int totalGeneral = 0;

		if (CollectionUtils.isNotEmpty(reportDocuments)) {
			for (Document reportDocument : reportDocuments) {

				Date dataSedinta = DocumentUtils.getMetadataDateTimeValue(reportDocument, prezentaComisiiGlDocumentType,
						arbConstants.getDocumentPrezentaComisieGlConstants().getDataInceputMetadataName());
				Long comisieId = DocumentUtils.getMetadataNomenclatorValue(reportDocument, prezentaComisiiGlDocumentType,
						arbConstants.getDocumentPrezentaComisieGlConstants().getDenumireComisieGlMetadataName());

				NomenclatorValueModel nomenclatorComisii = nomenclatorService.getNomenclatorValue(comisieId);
				String categorieComisie = NomenclatorValueUtils.getAttributeValueAsString(nomenclatorComisii,
						NomenclatorConstants.NOMENCLATOR_CATEGORIE_COMISII_GL_ATTRIBUTE_KEY_CATEGORIE);

				if (categorieComisie.equals(String.valueOf(comisieCategoryId))) {
					totalComisie++;
				}
				if (categorieComisie.equals(String.valueOf(glCategoryId))) {
					totalGl++;
				}

				NumarSedinteComisieGlReportRowModel row = new NumarSedinteComisieGlReportRowModel();

				row.setNumeComisieGl(nomenclatorComisiiUiAttrValuesAsMap.get(comisieId));
				row.setDataSedinta(dataSedinta);

				rows.add(row);
			}
		}

		totalGeneral = totalComisie + totalGl;

		report.setRows(rows);
		report.setTotalComisie(totalComisie);
		report.setTotalGl(totalGl);
		report.setTotalGeneral(totalGeneral);

		return report;
	}

	@Override
	public MembriiAfiliatiReportModel getMembriiAfiliatiReport(MembriiAfiliatiReportFilterModel filter) throws AppException {

		Map<Long, String> nomenclatorComisiiUiAttrValuesAsMap = nomenclatorService
				.getJoinedNomenclatorUiAtributesValuesAsMapByNomenclatorCode(NomenclatorConstants.COMISII_SAU_GL_NOMENCLATOR_CODE);
		Map<Long, String> nomenclatorInstitutiiUiAttrValuesAsMap = nomenclatorService
				.getJoinedNomenclatorUiAtributesValuesAsMapByNomenclatorCode(NomenclatorConstants.INSTITUTII_NOMENCLATOR_CODE);

		DocumentType prezentaComisiiGlDocumentType = documentTypeService.getDocumentTypeByName(arbConstants.getDocumentPrezentaComisieGlConstants().getDocumentTypeName());
		List<Document> reportDocuments = documentReportPlugin.getDocumentsForMembriiAfiliati(filter);

		MembriiAfiliatiReportModel report = new MembriiAfiliatiReportModel();
		List<MembriiAfiliatiReportRowModel> rows = new ArrayList<MembriiAfiliatiReportRowModel>();

		if (CollectionUtils.isNotEmpty(reportDocuments)) {
			for (Document reportDocument : reportDocuments) {

				Date dataSedinta = DocumentUtils.getMetadataDateTimeValue(reportDocument, prezentaComisiiGlDocumentType,
						arbConstants.getDocumentPrezentaComisieGlConstants().getDataInceputMetadataName());
				Long comisieId = DocumentUtils.getMetadataNomenclatorValue(reportDocument, prezentaComisiiGlDocumentType,
						arbConstants.getDocumentPrezentaComisieGlConstants().getDenumireComisieGlMetadataName());

				List<CollectionInstance> metadataCollectionInfoInformatiiParticipanti = DocumentUtils.getMetadataCollectionInstance(reportDocument, prezentaComisiiGlDocumentType,
						arbConstants.getDocumentPrezentaComisieGlConstants().getInformatiiParticipantiMetadataName());

				if (metadataCollectionInfoInformatiiParticipanti != null) {
					for (CollectionInstance metadataInfoParticipanti : metadataCollectionInfoInformatiiParticipanti) {

						String infoInformatiiParticipantiMetadataName = arbConstants.getDocumentPrezentaComisieGlConstants().getInformatiiParticipantiMetadataName();

						MetadataDefinition metadataDefinitionInstitutie = DocumentTypeUtils.getMetadataDefinitionFromCollectionByName(prezentaComisiiGlDocumentType,
								infoInformatiiParticipantiMetadataName,
								arbConstants.getDocumentPrezentaComisieGlConstants().getNumeInstitutieOfInformatiiParticipantiMetadataName());
						MetadataDefinition metadataDefinitionMembruAcreditat = DocumentTypeUtils.getMetadataDefinitionFromCollectionByName(prezentaComisiiGlDocumentType,
								infoInformatiiParticipantiMetadataName,
								arbConstants.getDocumentPrezentaComisieGlConstants().getMembruAcreditatOfInformatiiParticipantiMetadataName());
						MetadataDefinition metadataDefinitionNumeParticipantInlocuitor = DocumentTypeUtils.getMetadataDefinitionFromCollectionByName(prezentaComisiiGlDocumentType,
								infoInformatiiParticipantiMetadataName,
								arbConstants.getDocumentPrezentaComisieGlConstants().getNumeParticipantInlocuitorOfInformatiiParticipantiMetadataName());
						MetadataDefinition metadataDefinitionPrenumeParticipantInlocuitor = DocumentTypeUtils.getMetadataDefinitionFromCollectionByName(
								prezentaComisiiGlDocumentType, infoInformatiiParticipantiMetadataName,
								arbConstants.getDocumentPrezentaComisieGlConstants().getPrenumeParticipantInlocuitorOfInformatiiParticipantiMetadataName());

						boolean areFilterConditionRespected = true;

						MembriiAfiliatiReportRowModel row = new MembriiAfiliatiReportRowModel();

						row.setComisie(nomenclatorComisiiUiAttrValuesAsMap.get(comisieId));
						row.setDataSedinta(dataSedinta);

						for (MetadataInstance metadataInstance : metadataInfoParticipanti.getMetadataInstanceList()) {
							if (metadataInstance.getMetadataDefinitionId().equals(metadataDefinitionInstitutie.getId())) {
								Long institutieId = Long.parseLong(metadataInstance.getValue());
								if (filter.getInstitutieId() != null && !institutieId.equals(filter.getInstitutieId())) {
									areFilterConditionRespected = false;
								}
								row.setInstitutie(nomenclatorInstitutiiUiAttrValuesAsMap.get(institutieId));

								NomenclatorValueModel institutieNomenclatorValue = nomenclatorService.getNomenclatorValue(institutieId);
								Long tiInstitutieId = NomenclatorValueUtils.getAttributeValueAsLong(institutieNomenclatorValue,
										NomenclatorConstants.INSTITUTII_ATTRIBUTE_KEY_TIP_INSTITUTIE);

								NomenclatorValueModel tipInstitutieNomenclatorValue = nomenclatorService.getNomenclatorValue(tiInstitutieId);
								String tipInstitutie = NomenclatorValueUtils.getAttributeValueAsString(tipInstitutieNomenclatorValue,
										NomenclatorConstants.TIP_INSTITUTII_ATTRIBUTE_KEY_TIP_INSTITUTIE);

								if (!tipInstitutie.equals(NomenclatorConstants.TIP_INSTITUTII_VALUE_FOR_MEMBRU_AFILIAT)) {
									areFilterConditionRespected = false;
								}

							}

							if (filter.getNumeReprezentantInlocuitor() == null && filter.getPrenumeReprezentantInlocuitor() == null && filter.getNumeReprezentantAcreditat() == null
									&& filter.getPrenumeReprezentantAcreditat() == null) {
								if (metadataInstance.getMetadataDefinitionId().equals(metadataDefinitionMembruAcreditat.getId())) {
									String membruAcreeditat = metadataInstance.getValue();
									if (StringUtils.isNotBlank(membruAcreeditat)) {
										row.setReprezentant(membruAcreeditat);
									}
								}
								if (metadataInstance.getMetadataDefinitionId().equals(metadataDefinitionNumeParticipantInlocuitor.getId())) {
									String numeReprezentantInlocuitor = metadataInstance.getValue();
									if (StringUtils.isNotBlank(numeReprezentantInlocuitor)) {
										row.setReprezentant(numeReprezentantInlocuitor);
									}
								}

								if (metadataInstance.getMetadataDefinitionId().equals(metadataDefinitionPrenumeParticipantInlocuitor.getId())) {
									String prenumeReprezentantInlocuitor = metadataInstance.getValue();
									if (StringUtils.isNotBlank(prenumeReprezentantInlocuitor)) {
										row.setReprezentant(row.getReprezentant() + " " + prenumeReprezentantInlocuitor);
									}
								}
							}
							if (filter.getNumeReprezentantInlocuitor() == null && filter.getPrenumeReprezentantInlocuitor() == null
									&& (filter.getNumeReprezentantAcreditat() != null || filter.getPrenumeReprezentantAcreditat() != null)) {
								if (metadataInstance.getMetadataDefinitionId().equals(metadataDefinitionMembruAcreditat.getId())) {
									String membruAcreeditat = metadataInstance.getValue();
									if (filter.getNumeReprezentantAcreditat() != null
											&& !membruAcreeditat.toUpperCase().contains(filter.getNumeReprezentantAcreditat().toUpperCase())
											|| (filter.getPrenumeReprezentantAcreditat() != null
													&& !membruAcreeditat.toUpperCase().contains(filter.getPrenumeReprezentantAcreditat().toUpperCase()))) {
										areFilterConditionRespected = false;
									}
									if (StringUtils.isNotBlank(membruAcreeditat)) {
										row.setReprezentant(membruAcreeditat);
									}
								}
							}
							if ((filter.getNumeReprezentantInlocuitor() != null || filter.getPrenumeReprezentantInlocuitor() != null)
									&& (filter.getNumeReprezentantAcreditat() == null && filter.getPrenumeReprezentantAcreditat() == null)) {
								if (metadataInstance.getMetadataDefinitionId().equals(metadataDefinitionNumeParticipantInlocuitor.getId())) {
									String numeReprezentantInlocuitor = metadataInstance.getValue();
									if (filter.getNumeReprezentantInlocuitor() != null
											&& !numeReprezentantInlocuitor.toUpperCase().contains(filter.getNumeReprezentantInlocuitor().toUpperCase())) {
										areFilterConditionRespected = false;
									}
									if (StringUtils.isNotBlank(numeReprezentantInlocuitor)) {
										row.setReprezentant(numeReprezentantInlocuitor);
									}
								}

								if (metadataInstance.getMetadataDefinitionId().equals(metadataDefinitionPrenumeParticipantInlocuitor.getId())) {
									String prenumeReprezentantInlocuitor = metadataInstance.getValue();
									if (filter.getPrenumeReprezentantInlocuitor() != null
											&& !prenumeReprezentantInlocuitor.toUpperCase().contains(filter.getPrenumeReprezentantInlocuitor().toUpperCase())) {
										areFilterConditionRespected = false;
									}
									if (StringUtils.isNotBlank(prenumeReprezentantInlocuitor)) {
										row.setReprezentant(row.getReprezentant() + " " + prenumeReprezentantInlocuitor);
									}
								}
							}
						}

						if (areFilterConditionRespected && row.getReprezentant() != null) {
							rows.add(row);
						}

					}
				}
			}
		}

		report.setRows(rows);

		return report;
	}

	@Override
	public CheltuieliArbSiReprezentantArbReportModel getCheltuieliArbSiReprezentantArbReport(CheltuieliArbSiReprezentantArbReportFilterModel filter) throws AppException {

		List<CheltuieliArbSiRePrezentantArbRowModel> cheltuieliArb = deplasariDeconturiDao.getCheltuieliArbByCheltuieliArbSiReprezentantArbReportFilterModel(filter);
		List<CheltuieliArbSiRePrezentantArbRowModel> cheltuieliReprezentantArb = deplasariDeconturiDao
				.getCheltuieliReprezentantArbByCheltuieliArbSiReprezentantArbReportFilterModel(filter);
		CheltuialaReprezentantArbDiurna diurna = deplasariDeconturiDao.getDiurnaReprezentantArbByCheltuieliArbSiReprezentantArbReportFilterModel(filter);
		CheltuialaReprezentantArbAvansPrimit avans = deplasariDeconturiDao.getAvansReprezentantArbByCheltuieliArbSiReprezentantArbReportFilterModel(filter);

		CheltuieliArbSiReprezentantArbReportModel report = new CheltuieliArbSiReprezentantArbReportModel();
		CheltuieliArbReportModel cheltuialaArb = new CheltuieliArbReportModel();
		ro.cloudSoft.cloudDoc.presentation.client.shared.model.report.cheltuieliArbSiReprezentantArb.CheltuieliReprezentantArbReportModel cheltuialaReprezentantArb = new ro.cloudSoft.cloudDoc.presentation.client.shared.model.report.cheltuieliArbSiReprezentantArb.CheltuieliReprezentantArbReportModel();

		BigDecimal totalCheltuieliArb = BigDecimal.ZERO;
		BigDecimal totalCheltuieliReprezentantArb = BigDecimal.ZERO;

		if (avans == null) {
			CheltuialaReprezentantArbAvansPrimit avansNull = new CheltuialaReprezentantArbAvansPrimit();
			avansNull.setAvansPrimit(BigDecimal.ZERO);
			avans = avansNull;
		}
		if (diurna == null) {
			CheltuialaReprezentantArbDiurna diurnaNull = new CheltuialaReprezentantArbDiurna();
			diurnaNull.setDiurna(BigDecimal.ZERO);
			diurnaNull.setNumarZile(0);
			diurna = diurnaNull;
		}

		for (CheltuieliArbSiRePrezentantArbRowModel cheltuiala : cheltuieliArb) {
			cheltuiala.setValuta(filter.getValuta());

			if (cheltuiala.getNumerarSauCard() == null) {
				cheltuiala.setNumerarSauCard(REPORT_CHETUIELI_ARB_SI_REPREZENTANT_ARB_NULL);
			}

			totalCheltuieliArb = totalCheltuieliArb.add(cheltuiala.getSuma());
		}

		for (CheltuieliArbSiRePrezentantArbRowModel cheltuiala : cheltuieliReprezentantArb) {
			cheltuiala.setValuta(filter.getValuta());

			if (cheltuiala.getNumerarSauCard() == null) {
				cheltuiala.setNumerarSauCard(REPORT_CHETUIELI_ARB_SI_REPREZENTANT_ARB_NULL);
			}

			totalCheltuieliReprezentantArb = totalCheltuieliReprezentantArb.add(cheltuiala.getSuma());
		}

		CheltuieliArbSiRePrezentantArbRowModel cheltuialaRowDiurna = new CheltuieliArbSiRePrezentantArbRowModel();
		cheltuialaRowDiurna.setExplicatie(REPORT_CHETUIELI_ARB_SI_REPREZENTANT_ARB_DIURNA_ZILNICA);
		cheltuialaRowDiurna.setSuma(diurna.getDiurna());
		cheltuialaRowDiurna.setValuta(filter.getValuta());
		cheltuialaRowDiurna.setNumerarSauCard(diurna.getModalitatePlata());

		if (diurna.getModalitatePlata() == null) {
			cheltuialaRowDiurna.setNumerarSauCard(REPORT_CHETUIELI_ARB_SI_REPREZENTANT_ARB_NULL);
		}

		cheltuieliReprezentantArb.add(cheltuialaRowDiurna);

		CheltuieliArbSiRePrezentantArbRowModel cheltuialaRowZile = new CheltuieliArbSiRePrezentantArbRowModel();
		cheltuialaRowZile.setExplicatie(REPORT_CHETUIELI_ARB_SI_REPREZENTANT_ARB_NUMAR_ZILE);
		cheltuialaRowZile.setSuma(new BigDecimal(diurna.getNumarZile()));
		cheltuialaRowZile.setValuta(REPORT_CHETUIELI_ARB_SI_REPREZENTANT_ARB_NULL);
		cheltuialaRowZile.setNumerarSauCard(REPORT_CHETUIELI_ARB_SI_REPREZENTANT_ARB_NULL);
		cheltuieliReprezentantArb.add(cheltuialaRowZile);

		CheltuieliArbSiRePrezentantArbRowModel cheltuialaRowTotalDiurna = new CheltuieliArbSiRePrezentantArbRowModel();
		cheltuialaRowTotalDiurna.setExplicatie(REPORT_CHETUIELI_ARB_SI_REPREZENTANT_ARB_TOTAL_DIURNA);
		cheltuialaRowTotalDiurna.setSuma(diurna.getDiurna().multiply(new BigDecimal(diurna.getNumarZile())));
		cheltuialaRowTotalDiurna.setValuta(filter.getValuta());
		cheltuialaRowTotalDiurna.setNumerarSauCard(REPORT_CHETUIELI_ARB_SI_REPREZENTANT_ARB_NULL);
		cheltuieliReprezentantArb.add(cheltuialaRowTotalDiurna);

		totalCheltuieliReprezentantArb = totalCheltuieliReprezentantArb.add(cheltuialaRowTotalDiurna.getSuma());

		cheltuialaArb.setTotal(totalCheltuieliArb);
		cheltuialaArb.setRows(cheltuieliArb);

		cheltuialaReprezentantArb.setTotal(totalCheltuieliReprezentantArb);
		cheltuialaReprezentantArb.setAvans(avans.getAvansPrimit());
		cheltuialaReprezentantArb.setAvansModalitatePlata(avans.getModalitatePlata());

		if (avans.getModalitatePlata() == null) {
			cheltuialaReprezentantArb.setAvansModalitatePlata(REPORT_CHETUIELI_ARB_SI_REPREZENTANT_ARB_NULL);
		}

		cheltuialaReprezentantArb.setDiferenta(totalCheltuieliReprezentantArb.subtract(avans.getAvansPrimit()));

		cheltuialaReprezentantArb.setRows(cheltuieliReprezentantArb);

		report.setCheltuieliArb(cheltuialaArb);
		report.setCheltuieliReprezentantArb(cheltuialaReprezentantArb);

		return report;
	}

	@Override
	public List<DeplasariDeconturiReportRowModel> getDeplasariDeconturiReport(DeplasariDeconturiReportFilterModel filter) throws AppException {
		List<DeplasareDecont> deplasari = deplasariDeconturiDao.getAllByDeplasariDeconturiReportFilterModel(filter);
		List<DeplasariDeconturiReportRowModel> reportRows = new ArrayList<DeplasariDeconturiReportRowModel>();
		for (DeplasareDecont deplasare : deplasari) {
			DeplasariDeconturiReportRowModel row = new DeplasariDeconturiReportRowModel();
			row.setLuna(DateUtils.getMonth(deplasare.getDataDecizie()));
			row.setApelativ(deplasare.getApelativ());
			row.setInstitutie(deplasare.getDenumireInstitutie());
			if (deplasare.getReprezentantArb() != null) {
				String reprezentantIdFromNomPersoane = NomenclatorValueUtils.getAttributeValueAsString(deplasare.getReprezentantArb(),
						NomenclatorConstants.REPREZENTANTI_ARB_ORGANISME_INTERNE_SI_INTERNATIONALE_ATTRIBUTE_KEY_REPREZENTANT);
				NomenclatorValue nomenclatorValueReprezentant = nomenclatorValueDao.find(Long.parseLong(reprezentantIdFromNomPersoane));
				String numeReprezentant = NomenclatorValueUtils.getAttributeValueAsString(nomenclatorValueReprezentant, NomenclatorConstants.PERSOANE_ATTRIBUTE_KEY_NUME);
				String prenumeReprezentant = NomenclatorValueUtils.getAttributeValueAsString(nomenclatorValueReprezentant, NomenclatorConstants.PERSOANE_ATTRIBUTE_KEY_PRENUME);
				row.setReprezentantArbOioro(numeReprezentant + " " + prenumeReprezentant);
				row.setFunctia(NomenclatorValueUtils.getAttributeValueAsString(deplasare.getReprezentantArb(),
						NomenclatorConstants.REPREZENTANTI_ARB_ORGANISME_INTERNE_SI_INTERNATIONALE_ATTRIBUTE_KEY_FUNCTIE));
			}
			row.setNumarDecizie(deplasare.getNumarDecizie());
			row.setDataDecizie(deplasare.getDataDecizie());
			Long organismId = deplasare.getOrganismId();
			if (organismId != null) {
				NomenclatorValue organism = nomenclatorValueDao.find(organismId);
				row.setDenumireOrganism(NomenclatorValueUtils.getAttributeValueAsString(organism, NomenclatorConstants.ORGANISME_ATTRIBUTE_KEY_DENUMIRE));
				row.setAbreviereOrganism(NomenclatorValueUtils.getAttributeValueAsString(organism, NomenclatorConstants.ORGANISME_ATTRIBUTE_KEY_ABREVIERE));

			}
			row.setComitet(deplasare.getDenumireComitet());
			row.setNumarDeplasariEfectuate(deplasare.getNumarDeplasariEfectuate());
			row.setNumarDeplasariBugetateRamase(deplasare.getNumarDeplasariBugetateRamase());
			row.setEveniment(deplasare.getEveniment());
			row.setTara(deplasare.getTara());
			row.setOras(deplasare.getOras());
			row.setDataPlecare(deplasare.getDataPlecare());
			row.setDataSosire(deplasare.getDataSosire());
			row.setDataInceputConferinta(deplasare.getDataConferintaInceput());
			row.setDataSfarsitConferinta(deplasare.getDataConferintaSfarsit());
			row.setNumarNopti(deplasare.getNumarNopti());
			row.setSaTransmisMinutaIntalnitii(deplasare.isMinutaIntalnireTransmisa());
			row.setObservatii(deplasare.getObservatii());
			row.setTitularDecont(deplasare.getCheltuieliArbTitularDecont());
			row.setTipDecont(deplasare.getCheltuieliArbTipDecont());
			row.setNumarDecont(deplasare.getNumarInregistrare().split("/")[0]);
			row.setDataDecont(deplasare.getCheltuieliArbDataDecont());

			int rowsNr = deplasare.getCheltuieliReprezentantArb().size();
			if (rowsNr < deplasare.getCheltuieliArb().size()) {
				rowsNr = deplasare.getCheltuieliArb().size();
			}
			List<DeplasariDeconturiReportRowModel> rowsForCurrentDeplasare = new ArrayList<>();
			if (rowsNr == 0) {
				reportRows.add(row);
				continue;
			}
			for (int index = 0; index < rowsNr; index++) {
				rowsForCurrentDeplasare.add(SerializationUtils.clone(row));
			}
			List<CheltuialaArb> cheltuieliArb = deplasare.getCheltuieliArb();
			Collections.sort(cheltuieliArb, new Comparator<CheltuialaArb>() {
				@Override
				public int compare(CheltuialaArb arg0, CheltuialaArb arg1) {
					return arg0.getValuta().compareTo(arg1.getValuta());
				}
			});
			int index = 0;
			BigDecimal totalCheltuieliArbInRon = new BigDecimal(0);
			DeplasariDeconturiReportRowModel firstCheltuialaArbGroupByValuta = null;
			for (CheltuialaArb cheltuiala : cheltuieliArb) {
				DeplasariDeconturiReportRowModel rowModel = rowsForCurrentDeplasare.get(index);
				ValutaForCheltuieliArbEnum valuta = cheltuiala.getValuta();
				rowModel.setValuta(cheltuiala.getValuta().toString());
				rowModel.setCursValutar(cheltuiala.getCursValutar());
				rowModel.setNumarDocumentJustificativ(cheltuiala.getNumarDocumentJustificativ());
				rowModel.setDataDocumentJustificativ(cheltuiala.getDataDocumentJustificativ());

				switch (cheltuiala.getTipDocumentJustificativ()) {
				case CAZARE:
					rowModel.setCazare(cheltuiala.getValoareCheltuiala());
					break;
				case BILET_DE_AVION:
					rowModel.setBiletDeAvion(cheltuiala.getValoareCheltuiala());
					break;
				case ASIGURARE_MEDICALA:
					rowModel.setAsiguareMedicala(cheltuiala.getValoareCheltuiala());
					break;
				case TRANSFER_AEROPORT:
					rowModel.setTransferAeroport(cheltuiala.getValoareCheltuiala());
					break;
				default:
					rowModel.setAlteCheltuieli(cheltuiala.getValoareCheltuiala());
					break;
				}
				rowModel.setTotalCheltuieliPerValuta(cheltuiala.getValoareCheltuiala());
				if (cheltuiala.getValuta().equals(ValutaForCheltuieliArbEnum.RON)) {
					totalCheltuieliArbInRon = totalCheltuieliArbInRon.add(cheltuiala.getValoareCheltuiala());
				} else {
					totalCheltuieliArbInRon = totalCheltuieliArbInRon.add(cheltuiala.getCursValutar().multiply(cheltuiala.getValoareCheltuiala()));
				}
				if (firstCheltuialaArbGroupByValuta != null && firstCheltuialaArbGroupByValuta.getValuta().equals(rowModel.getValuta())) {
					firstCheltuialaArbGroupByValuta.setTotalCheltuieliPerValuta(firstCheltuialaArbGroupByValuta.getTotalCheltuieliPerValuta().add(rowModel.getTotalCheltuieliPerValuta()));
					rowModel.setTotalCheltuieliPerValuta(BigDecimal.ZERO);
				} else {
					firstCheltuialaArbGroupByValuta = rowModel;
				}
				index++;
			}
			// set on first row global values/deplasare
			rowsForCurrentDeplasare.get(0).setTotalCheltuieliInRON(totalCheltuieliArbInRon);

			List<CheltuialaReprezentantArb> cheltuieliReprezentantArb = deplasare.getCheltuieliReprezentantArb();
			Collections.sort(cheltuieliReprezentantArb, new Comparator<CheltuialaReprezentantArb>() {
				@Override
				public int compare(CheltuialaReprezentantArb arg0, CheltuialaReprezentantArb arg1) {
					return arg0.getValuta().compareTo(arg1.getValuta());
				}
			});
			BigDecimal totalCheltuieliReprezentantArbInRon = new BigDecimal(0);
			BigDecimal diurnaZilnicaRon = deplasare.getCheltuieliReprezentantArbDiurnaZilnica().multiply(deplasare.getCheltuieliReprezentantArbDiurnaZilnicaCursValutar());
			BigDecimal avansPrimitInRon = deplasare.getCheltuieliReprezentantArbAvansPrimitSuma().multiply(deplasare.getCheltuieliReprezentantArbAvansPrimitSumaCursValutar());
			DeplasariDeconturiReportRowModel firstCheltuialaReprezArbGroupByValuta = null;
			index = 0;
			for (CheltuialaReprezentantArb cheltuiala : cheltuieliReprezentantArb) {
				DeplasariDeconturiReportRowModel rowModel = rowsForCurrentDeplasare.get(index);
				ValutaForCheltuieliReprezentantArbEnum valuta = cheltuiala.getValuta();
				rowModel.setValuta2(cheltuiala.getValuta().toString());
				rowModel.setCursValutar2(cheltuiala.getCursValutar());
				rowModel.setNumarDocumentJustificativ2(cheltuiala.getNumarDocumentJustificativ());
				rowModel.setDataDocumentJustificativ2(cheltuiala.getDataDocumentJustificativ());

				switch (cheltuiala.getTipDocumentJustificativ()) {
				case CAZARE:
					rowModel.setCazare2(cheltuiala.getValoareCheltuiala());
					break;
				case BILET_DE_AVION:
					rowModel.setBiletDeAvion2(cheltuiala.getValoareCheltuiala());
					break;
				case TAXI_TREN_METROU:
					rowModel.setTaxiTrenMetrou(cheltuiala.getValoareCheltuiala());
					break;
				case COMISION_UTILIZARE_CARD:
					rowModel.setComisionUtilizareCard(cheltuiala.getValoareCheltuiala());
					break;
				default:
					rowModel.setAlteCheltuieli2(cheltuiala.getValoareCheltuiala());
					break;
				}

				rowModel.setTotalCheltuieliPerValuta2(cheltuiala.getValoareCheltuiala());
				if (cheltuiala.getValuta().equals(ValutaForCheltuieliReprezentantArbEnum.RON)) {
					totalCheltuieliReprezentantArbInRon = totalCheltuieliReprezentantArbInRon.add(cheltuiala.getValoareCheltuiala());
				} else {
					totalCheltuieliReprezentantArbInRon = totalCheltuieliReprezentantArbInRon.add(cheltuiala.getCursValutar().multiply(cheltuiala.getValoareCheltuiala()));
				}
				
				if (firstCheltuialaReprezArbGroupByValuta != null && firstCheltuialaReprezArbGroupByValuta.getValuta2().equals(rowModel.getValuta2())) {
					firstCheltuialaReprezArbGroupByValuta.setTotalCheltuieliPerValuta2(firstCheltuialaReprezArbGroupByValuta.getTotalCheltuieliPerValuta2().add(rowModel.getTotalCheltuieliPerValuta2()));
					rowModel.setTotalCheltuieliPerValuta2(BigDecimal.ZERO);
				} else {
					firstCheltuialaReprezArbGroupByValuta = rowModel;
				}
				index++;
			}
			// set on first row global values/deplasare
			rowsForCurrentDeplasare.get(0).setDiurnaZilnica(diurnaZilnicaRon);
			rowsForCurrentDeplasare.get(0).setNumarZile(deplasare.getCheltuieliReprezentantArbNumarZile());
			rowsForCurrentDeplasare.get(0).setTotalDiurnaInRON(deplasare.getCheltuieliReprezentantArbTotalDiurna());
			rowsForCurrentDeplasare.get(0).setAvans(avansPrimitInRon);
			totalCheltuieliReprezentantArbInRon = totalCheltuieliReprezentantArbInRon.add(deplasare.getCheltuieliReprezentantArbTotalDiurna());
			rowsForCurrentDeplasare.get(0).setTotalCheltuieliInRON2(totalCheltuieliReprezentantArbInRon);
			rowsForCurrentDeplasare.get(0).setTotalDeIncasatDeRestituitInRON(totalCheltuieliReprezentantArbInRon.subtract(avansPrimitInRon));
			reportRows.addAll(rowsForCurrentDeplasare);

		}
		groupByValuta(reportRows);
		return reportRows;
	}

	private void groupByValuta(List<DeplasariDeconturiReportRowModel> reportRows) {
		// TODO Auto-generated method stub
		
		
	}

	@Override
	public PrezentaComisiiGlInIntervalReportBundle getPrezentaComisiiGlInIntervalReportBundle(DocumentIdentifierModel document, SecurityManager userSecurity) throws AppException {

		PrezentaComisiiGlInIntervalReportBundle bundle = new PrezentaComisiiGlInIntervalReportBundle();

		Document documentPrezenta = documentService.getDocumentById(document.getDocumentId(), document.getDocumentLocationRealName(), userSecurity);

		if (documentPrezenta == null) {
			return bundle;
		}

		Map<Long, String> nomenclatorInstitutiiUiAttrValuesAsMap = nomenclatorService
				.getJoinedNomenclatorUiAtributesValuesAsMapByNomenclatorCode(NomenclatorConstants.INSTITUTII_NOMENCLATOR_CODE);
		DocumentType prezentaComisiiGlDocumentType = documentTypeService.getDocumentTypeById(documentPrezenta.getDocumentTypeId());

		List<SimpleListItemModel> institutii = new ArrayList<SimpleListItemModel>();
		List<SimpleListItemModel> participantiAcreditati = new ArrayList<SimpleListItemModel>();

		List<CollectionInstance> metadataCollectionInfoInformatiiParticipanti = DocumentUtils.getMetadataCollectionInstance(documentPrezenta, prezentaComisiiGlDocumentType,
				arbConstants.getDocumentPrezentaComisieGlConstants().getInformatiiParticipantiMetadataName());

		if (metadataCollectionInfoInformatiiParticipanti != null) {

			for (CollectionInstance metadataInfoParticipanti : metadataCollectionInfoInformatiiParticipanti) {

				String infoInformatiiParticipantiMetadataName = arbConstants.getDocumentPrezentaComisieGlConstants().getInformatiiParticipantiMetadataName();

				MetadataDefinition metadataDefinitionInstitutie = DocumentTypeUtils.getMetadataDefinitionFromCollectionByName(prezentaComisiiGlDocumentType,
						infoInformatiiParticipantiMetadataName, arbConstants.getDocumentPrezentaComisieGlConstants().getNumeInstitutieOfInformatiiParticipantiMetadataName());
				MetadataDefinition metadataDefinitionMembruAcreditat = DocumentTypeUtils.getMetadataDefinitionFromCollectionByName(prezentaComisiiGlDocumentType,
						infoInformatiiParticipantiMetadataName, arbConstants.getDocumentPrezentaComisieGlConstants().getMembruAcreditatOfInformatiiParticipantiMetadataName());

				for (MetadataInstance metadataInstance : metadataInfoParticipanti.getMetadataInstanceList()) {
					if (metadataInstance.getMetadataDefinitionId().equals(metadataDefinitionInstitutie.getId())) {
						Long institutieId = Long.parseLong(metadataInstance.getValue());
						SimpleListItemModel institutieItem = new SimpleListItemModel(institutieId.toString(), nomenclatorInstitutiiUiAttrValuesAsMap.get(institutieId));

						if (!institutii.stream().filter(item -> item.getItemValue().equals(institutieId.toString())).findFirst().isPresent()) {
							institutii.add(institutieItem);
						}
					}

					if (metadataInstance.getMetadataDefinitionId().equals(metadataDefinitionMembruAcreditat.getId())) {
						String membruAcreeditat = metadataInstance.getValue();
						SimpleListItemModel membruAcreditatItem = new SimpleListItemModel(membruAcreeditat, membruAcreeditat);

						if (!participantiAcreditati.stream().filter(item -> item.getItemValue().equals(membruAcreeditat)).findFirst().isPresent()) {
							participantiAcreditati.add(membruAcreditatItem);
						}
					}
				}
			}
		}

		bundle.setInstitutieItems(institutii);
		bundle.setParticipantAcreditatItems(participantiAcreditati);

		return bundle;
	}

	@Override
	public TaskuriCumulateReportModel getTaskuriCumulateReport(TaskuriCumulateReportFilterModel filter) throws AppException {
		final String REPORT_TASKURI_CUMULATE_ZONA_FLUXURI = "FLUXURI";
		final String REPORT_TASKURI_CUMULATE_ZONA_PROIECTE = "PROIECTE";

		TaskuriCumulateReportModel report = new TaskuriCumulateReportModel();
		List<TaskuriCumulateReportRowModel> rows = new ArrayList<TaskuriCumulateReportRowModel>();

		if (filter.getZonaTask() == null || filter.getZonaTask().equals(REPORT_TASKURI_CUMULATE_ZONA_PROIECTE)) {
			List<Task> tasks = taskDao.getAllTasksByTaskuriCumulateReportFilterModel(filter);
			for (Task task : tasks) {
				for (OrganizationEntity assignment : task.getAssignments()) {
					TaskuriCumulateReportRowModel row = new TaskuriCumulateReportRowModel();

					row.setDenumireTask(task.getName());
					row.setStatus(task.getStatus().toString());
					row.setUserAsignat(((User) assignment).getDisplayName());
					row.setZonaTask(REPORT_TASKURI_CUMULATE_ZONA_PROIECTE);

					if (filter.getUserAsignat() == null || assignment.getId().equals(filter.getUserAsignat())) {
						rows.add(row);
					}
				}
			}
		}

		if (filter.getZonaTask() == null || filter.getZonaTask().equals(REPORT_TASKURI_CUMULATE_ZONA_FLUXURI)) {
			List<ViewDocumentHistoryCompletedTasks> tasks = documentHistoryCompletedTasksViewDao.getAllByTaskuriCumulateReportFilterModel(filter);

			Map<String, String> documentNameAsMap = new HashMap<>();

			for (ViewDocumentHistoryCompletedTasks task : tasks) {
				String documentName = null;

				if (documentNameAsMap.containsKey(task.getDocumentLocationRealName() + task.getDocumentId())) {
					documentName = documentNameAsMap.get(task.getDocumentLocationRealName() + task.getDocumentId());
				} else {
					try {
						documentName = documentService.getDocumentName(task.getDocumentLocationRealName(), task.getDocumentId());
					} catch (AppException appEx) {
						if (appEx.getCode().equals(AppExceptionCodes.JR_ItemNotFoundException)) {
							documentName = "(document sters)";
						}
					} catch (Exception e) {
						throw new RuntimeException(e);
					}
					documentNameAsMap.put(task.getDocumentLocationRealName() + task.getDocumentId(), documentName);
				}

				TaskuriCumulateReportRowModel row = new TaskuriCumulateReportRowModel();

				row.setDenumireTask(documentName);
				row.setStatus(task.getDocumentWorkflowStatus());
				row.setUserAsignat(task.getAssigneeUseFirstname() + " " + task.getAssigneeUserLastname());
				row.setZonaTask(REPORT_TASKURI_CUMULATE_ZONA_FLUXURI);
				if (task.getDocumentWorkflowStatus().equals(WorkflowInstance.STATUS_RUNNING)) {
					row.setStatus(TaskStatus.IN_PROGRESS.toString());
				}
				if (task.getDocumentWorkflowStatus().equals(WorkflowInstance.STATUS_FINNISHED)) {
					row.setStatus(TaskStatus.FINALIZED.toString());
				}
				
				final String documentNameCompare = documentName;
				if ((filter.getUserAsignat() == null || task.getAssigneeUserId().equals(filter.getUserAsignat().toString())) && !rows.stream()
						.filter(o -> (o.getDenumireTask().equals(documentNameCompare) && o.getUserAsignat().equals(task.getAssigneeUseFirstname() + " " + task.getAssigneeUserLastname())))
						.findFirst().isPresent()) {
					if (documentName != null) {
						rows.add(row);
					}
				}
			}
		}

		report.setRows(rows);

		return report;
	}

	@Override
	public DeplasariDeconturiReportBundleModel getDeplasariDeconturiReportBundle() {
		DeplasariDeconturiReportBundleModel bundle = new DeplasariDeconturiReportBundleModel();

		List<SimpleListItemModel> denumireComiteteList = new ArrayList<>();
		deplasariDeconturiDao.getAllDistinctDenumiriComitete().forEach(denumireComite -> {
			denumireComiteteList.add(new SimpleListItemModel(denumireComite, denumireComite));
		});
		bundle.setDenumiriComiteteList(denumireComiteteList);

		List<SimpleListItemModel> denumiriInstitutii = new ArrayList<>();
		deplasariDeconturiDao.getAllDistinctDenumiriInstitutii().forEach(institutie -> {
			denumiriInstitutii.add(new SimpleListItemModel(institutie, institutie));
		});
		bundle.setDenumiriInstitutii(denumiriInstitutii);

		Nomenclator nomenclatorOrganisme = nomenclatorDao.findByCode(NomenclatorConstants.ORGANISME_NOMENCLATOR_CODE);
		bundle.setNomenclatorOrganismeId(nomenclatorOrganisme.getId());

		Nomenclator nomenclatorReprezentatOrg = nomenclatorDao.findByCode(NomenclatorConstants.REPREZENTANTI_ARB_ORGANISME_INTERNE_SI_INTERNATIONALE_CODE);
		bundle.setNomenclatorReprezentatOrgId(nomenclatorReprezentatOrg.getId());

		List<SimpleListItemModel> oraseList = new ArrayList<>();
		deplasariDeconturiDao.getAllDistinctOrase().forEach(oras -> {
			oraseList.add(new SimpleListItemModel(oras, oras));
		});
		bundle.setOraseList(oraseList);

		List<SimpleListItemModel> titulariDeconturi = new ArrayList<>();
		deplasariDeconturiDao.getAllDistinctTitulariWithDecont().forEach(titular -> {
			titulariDeconturi.add(new SimpleListItemModel(titular, titular));
		});
		bundle.setTitulariDeconturi(titulariDeconturi);

		return bundle;
	}

	@Override
	public CereriConcediuReportBundle getCereriConcediuReportBundle() {
		DocumentType documentType = documentTypeService.getDocumentTypeByName(arbConstants.getDocumentCerereConcediuConstants().getDocumentTypeName());
		ListMetadataDefinition concediuPersonalListMetadataDefinition = (ListMetadataDefinition) DocumentTypeUtils.getMetadataDefinitionByName(documentType,
				arbConstants.getDocumentCerereConcediuConstants().getTipConcediuPersonalMetadataName());
		ListMetadataDefinition concediuNepersonalListMetadataDefinition = (ListMetadataDefinition) DocumentTypeUtils.getMetadataDefinitionByName(documentType,
				arbConstants.getDocumentCerereConcediuConstants().getTipConcediuNepersonalMetadataName());

		CereriConcediuReportBundle cereriConcediuReportBundle = new CereriConcediuReportBundle();
		List<ListMetadataItemModel> tipConcediuItems = new ArrayList<ListMetadataItemModel>();

		for (ListMetadataItem listMetadataItem : concediuPersonalListMetadataDefinition.getListItems()) {
			ListMetadataItemModel tipConcediuItem = new ListMetadataItemModel();

			tipConcediuItem.setLabel(listMetadataItem.getLabel());
			tipConcediuItem.setValue(listMetadataItem.getValue());

			tipConcediuItems.add(tipConcediuItem);
		}

		for (ListMetadataItem listMetadataItem : concediuNepersonalListMetadataDefinition.getListItems()) {
			if (!tipConcediuItems.stream().filter(o -> (o.getLabel().equals(listMetadataItem.getLabel()) && o.getValue().equals(listMetadataItem.getValue()))).findFirst()
					.isPresent()) {

				ListMetadataItemModel tipConcediuItem = new ListMetadataItemModel();

				tipConcediuItem.setLabel(listMetadataItem.getLabel());
				tipConcediuItem.setValue(listMetadataItem.getValue());

				tipConcediuItems.add(tipConcediuItem);
			}
		}

		cereriConcediuReportBundle.setTipConcediuItems(tipConcediuItems);

		return cereriConcediuReportBundle;
	}

	@Override
	public CereriConcediuReportModel getCereriConcediuReport(CereriConcediuReportFilterModel filter) throws AppException {

		DocumentType cereriConcediuDocumentType = documentTypeService.getDocumentTypeByName(arbConstants.getDocumentCerereConcediuConstants().getDocumentTypeName());
		Workflow workflow = workflowService.getWorkflowByDocumentType(cereriConcediuDocumentType.getId());

		Long cerereNepersonalaAprobataWorkflowStateId = workflowService
				.getWorkflowState(workflow.getId(), arbConstants.getDocumentCerereConcediuConstants().getCerereNepersonalaAprobataWorkflowStateCode()).getId();
		Long cererePersonalaAprobataWorkflowStateId = workflowService
				.getWorkflowState(workflow.getId(), arbConstants.getDocumentCerereConcediuConstants().getCererePersonalaAprobataWorkflowStateCode()).getId();

		List<Document> reportDocuments = documentReportPlugin.getDocumentsForCereriConcediuReportModel(filter, cererePersonalaAprobataWorkflowStateId,
				cerereNepersonalaAprobataWorkflowStateId);

		CereriConcediuReportModel report = new CereriConcediuReportModel();
		List<CereriConcediuReportRowModel> rows = new ArrayList<CereriConcediuReportRowModel>();

		if (CollectionUtils.isNotEmpty(reportDocuments)) {
			for (Document reportDocument : reportDocuments) {
				WorkflowInstance workflowInstance = workflowInstanceDao.getForDocument(reportDocument.getId());
				if (workflowInstance != null && workflowInstance.getStatus().equals(WorkflowInstance.STATUS_FINNISHED)) {

					Long angajatId = DocumentUtils.getMetadataNomenclatorValue(reportDocument, cereriConcediuDocumentType,
							arbConstants.getDocumentCerereConcediuConstants().getBeneficiarConcediuMetadataName());
					String concediuPersonal = DocumentUtils.getMetadataListValueAsLabel(reportDocument, cereriConcediuDocumentType,
							arbConstants.getDocumentCerereConcediuConstants().getTipConcediuPersonalMetadataName());
					String concediuNepersonal = DocumentUtils.getMetadataListValueAsLabel(reportDocument, cereriConcediuDocumentType,
							arbConstants.getDocumentCerereConcediuConstants().getTipConcediuNepersonalMetadataName());
					Date dataInitiere = DocumentUtils.getMetadataDateValue(reportDocument, cereriConcediuDocumentType,
							arbConstants.getDocumentCerereConcediuConstants().getDataInitiereCerereConcediuMetadataName());
					Date dataInceput = DocumentUtils.getMetadataDateValue(reportDocument, cereriConcediuDocumentType,
							arbConstants.getDocumentCerereConcediuConstants().getDataInceputMetadataName());
					Date dataSfarsit = DocumentUtils.getMetadataDateValue(reportDocument, cereriConcediuDocumentType,
							arbConstants.getDocumentCerereConcediuConstants().getDataSfarsitMetadataName());
					String motivRespingere = DocumentUtils.getMetadataValueAsString(reportDocument, cereriConcediuDocumentType,
							arbConstants.getDocumentCerereConcediuConstants().getComentariuRespingereMetadataName());

					String angajat = userService.getDisplayName(angajatId);

					CereriConcediuReportRowModel row = new CereriConcediuReportRowModel();

					row.setAngajat(angajat);
					row.setDataInceput(dataInceput);
					row.setDataInitiere(dataInitiere);
					row.setDataSfarsit(dataSfarsit);
					row.setMotivRespingere(motivRespingere);
					if (reportDocument.getWorkflowStateId().equals(cererePersonalaAprobataWorkflowStateId)
							|| reportDocument.getWorkflowStateId().equals(cerereNepersonalaAprobataWorkflowStateId)) {
						row.setStatus(CereriConcediuReportFilterModel.REPORT_CERERI_CONCEDIU_CONCEDIU_APROBAT);
					} else {
						row.setStatus(CereriConcediuReportFilterModel.REPORT_CERERI_CONCEDIU_CONCEDIU_RESPINS);
					}
					if (concediuPersonal != null) {
						row.setTipConcediu(concediuPersonal);
					}
					if (concediuNepersonal != null) {
						row.setTipConcediu(concediuNepersonal);
					}

					rows.add(row);
				}

			}
		}

		report.setRows(rows);

		return report;
	}

	@Override
	public ReprezentantiBancaPerFunctieSiComisieReportModel getReprezentantiBancaPerFunctieSiComisieReport(ReprezentantiBancaPerFunctieSiComisieReportFilterModel filter)
			throws AppException {

		Map<Long, String> nomenclatorInstitutiiUiAttrValuesAsMap = nomenclatorService
				.getJoinedNomenclatorUiAtributesValuesAsMapByNomenclatorCode(NomenclatorConstants.INSTITUTII_NOMENCLATOR_CODE);
		Map<Long, String> nomenclatorComisiiUiAttrValuesAsMap = nomenclatorService
				.getJoinedNomenclatorUiAtributesValuesAsMapByNomenclatorCode(NomenclatorConstants.COMISII_SAU_GL_NOMENCLATOR_CODE);

		DocumentType prezentaComisiiGlDocumentType = documentTypeService.getDocumentTypeByName(arbConstants.getDocumentPrezentaComisieGlConstants().getDocumentTypeName());
		List<Document> reportDocuments = documentReportPlugin.getDocumentsForReprezentantiBancaPerFunctieSiComisieReportFilterModel(filter);

		DocumentType prezentaComisieGLDocumentType = documentTypeService.getDocumentTypeByName(arbConstants.getDocumentPrezentaComisieGlConstants().getDocumentTypeName());

		ReprezentantiBancaPerFunctieSiComisieReportModel report = new ReprezentantiBancaPerFunctieSiComisieReportModel();
		List<ReprezentantiBancaPerFunctieSiComisieReportRowModel> rows = new ArrayList<ReprezentantiBancaPerFunctieSiComisieReportRowModel>();

		if (CollectionUtils.isNotEmpty(reportDocuments)) {
			for (Document reportDocument : reportDocuments) {

				WorkflowInstance workflowInstance = workflowInstanceDao.getForDocument(reportDocument.getId());
				if (workflowInstance == null || workflowInstance.getStatus().equals(WorkflowInstance.STATUS_FINNISHED)) {
					Date dataSedinta = DocumentUtils.getMetadataDateTimeValue(reportDocument, prezentaComisiiGlDocumentType,
							arbConstants.getDocumentPrezentaComisieGlConstants().getDataInceputMetadataName());
					Long comisieId = DocumentUtils.getMetadataNomenclatorValue(reportDocument, prezentaComisiiGlDocumentType,
							arbConstants.getDocumentPrezentaComisieGlConstants().getDenumireComisieGlMetadataName());

					List<CollectionInstance> metadataCollectionInfoInformatiiParticipanti = DocumentUtils.getMetadataCollectionInstance(reportDocument,
							prezentaComisieGLDocumentType, arbConstants.getDocumentPrezentaComisieGlConstants().getInformatiiParticipantiMetadataName());

					if (metadataCollectionInfoInformatiiParticipanti != null) {
						for (CollectionInstance metadataInfoMembrii : metadataCollectionInfoInformatiiParticipanti) {

							ReprezentantiBancaPerFunctieSiComisieReportRowModel row = new ReprezentantiBancaPerFunctieSiComisieReportRowModel();

							String infoInformatiiParticipantiMetadataName = arbConstants.getDocumentPrezentaComisieGlConstants().getInformatiiParticipantiMetadataName();

							MetadataDefinition metadataDefinitionInstitutie = DocumentTypeUtils.getMetadataDefinitionFromCollectionByName(prezentaComisiiGlDocumentType,
									infoInformatiiParticipantiMetadataName,
									arbConstants.getDocumentPrezentaComisieGlConstants().getNumeInstitutieOfInformatiiParticipantiMetadataName());
							MetadataDefinition metadataDefinitionMembruAcreditat = DocumentTypeUtils.getMetadataDefinitionFromCollectionByName(prezentaComisiiGlDocumentType,
									infoInformatiiParticipantiMetadataName,
									arbConstants.getDocumentPrezentaComisieGlConstants().getMembruAcreditatOfInformatiiParticipantiMetadataName());
							MetadataDefinition metadataDefinitionNumeParticipantInlocuitor = DocumentTypeUtils.getMetadataDefinitionFromCollectionByName(
									prezentaComisiiGlDocumentType, infoInformatiiParticipantiMetadataName,
									arbConstants.getDocumentPrezentaComisieGlConstants().getNumeParticipantInlocuitorOfInformatiiParticipantiMetadataName());
							MetadataDefinition metadataDefinitionPrenumeParticipantInlocuitor = DocumentTypeUtils.getMetadataDefinitionFromCollectionByName(
									prezentaComisiiGlDocumentType, infoInformatiiParticipantiMetadataName,
									arbConstants.getDocumentPrezentaComisieGlConstants().getPrenumeParticipantInlocuitorOfInformatiiParticipantiMetadataName());
							MetadataDefinition metadataDefinitionFunctie = DocumentTypeUtils.getMetadataDefinitionFromCollectionByName(prezentaComisiiGlDocumentType,
									infoInformatiiParticipantiMetadataName,
									arbConstants.getDocumentPrezentaComisieGlConstants().getFunctieMembruOfInformatiiParticipantiMetadataName());
							MetadataDefinition metadataDefinitionEmail = DocumentTypeUtils.getMetadataDefinitionFromCollectionByName(prezentaComisiiGlDocumentType,
									infoInformatiiParticipantiMetadataName, arbConstants.getDocumentPrezentaComisieGlConstants().getEmailOfInformatiiParticipantiMetadataName());

							boolean areFilterConditionRespected = true;
							Long bancaId = null;

							if (CollectionUtils.isNotEmpty(filter.getComisieId()) && !filter.getComisieId().contains(comisieId)) {
								areFilterConditionRespected = false;
							}
							if (filter.getDataSedintaDeLa() != null && dataSedinta.before(DateUtils.nullHourMinutesSeconds(filter.getDataSedintaDeLa()))) {
								areFilterConditionRespected = false;
							}
							if (filter.getDataSedintaPanaLa() != null && dataSedinta.after(DateUtils.maximizeHourMinutesSeconds(filter.getDataSedintaPanaLa()))) {
								areFilterConditionRespected = false;
							}

							row.setNumeComisie(nomenclatorComisiiUiAttrValuesAsMap.get(comisieId));

							for (MetadataInstance metadataInstance : metadataInfoMembrii.getMetadataInstanceList()) {
								if (metadataInstance.getMetadataDefinitionId().equals(metadataDefinitionInstitutie.getId())) {
									bancaId = Long.parseLong(metadataInstance.getValue());
									if (CollectionUtils.isNotEmpty(filter.getInstitutieId()) && !filter.getInstitutieId().contains(bancaId)) {
										areFilterConditionRespected = false;
									}
									row.setInstitutie(nomenclatorInstitutiiUiAttrValuesAsMap.get(bancaId));
								}
								if (metadataInstance.getMetadataDefinitionId().equals(metadataDefinitionMembruAcreditat.getId())) {
									if (StringUtils.isNotBlank(metadataInstance.getValue())) {
										row.setNumeParticipant(metadataInstance.getValue());
									}
								}
								if (metadataInstance.getMetadataDefinitionId().equals(metadataDefinitionNumeParticipantInlocuitor.getId())) {
									if (StringUtils.isNotBlank(metadataInstance.getValue())) {
										row.setNumeParticipant(metadataInstance.getValue());
									}
								}

								if (metadataInstance.getMetadataDefinitionId().equals(metadataDefinitionPrenumeParticipantInlocuitor.getId())) {
									if (StringUtils.isNotBlank(metadataInstance.getValue())) {
										row.setNumeParticipant(row.getNumeParticipant() + " " + metadataInstance.getValue());
									}
								}
								if (metadataInstance.getMetadataDefinitionId().equals(metadataDefinitionFunctie.getId())) {
									row.setFunctie(metadataInstance.getValue());
								}
								if (metadataInstance.getMetadataDefinitionId().equals(metadataDefinitionEmail.getId())) {
									row.setEmail(metadataInstance.getValue());
								}
							}

							if (areFilterConditionRespected) {
								rows.add(row);
							}
						}
					}
				}
			}
		}

		report.setRows(rows);

		return report;
	}

	@Override
	public PrezentaReprezentivitateReportModel getPrezentaComisiiArbReprezentativitate(PrezentaReprezentivitateReportFilterModel filter) throws AppException {

		List<Document> reportDocuments = documentReportPlugin.getDocumentsForPrezentaComisiiArbReprezentativitateReport(filter);
		List<NomenclatorValueModel> nomenclatorValuesForInstitutii = nomenclatorService.getNomenclatorValuesByNomenclatorCode(NomenclatorConstants.INSTITUTII_NOMENCLATOR_CODE);

		List<Long> institutiiMembriiArbIds = getAllIdsForInstitutiiMembriiArb();

		Map<Long, NomenclatorValueModel> nomenclatorInstitutiiValuesById = new HashMap<>();
		nomenclatorValuesForInstitutii.forEach(institutiteNomValues -> {
			nomenclatorInstitutiiValuesById.put(institutiteNomValues.getId(), institutiteNomValues);
		});

		DocumentType prezentaComisiiGlDocumentType = documentTypeService.getDocumentTypeByName(arbConstants.getDocumentPrezentaComisieGlConstants().getDocumentTypeName());
		RaspunsuriBanciReportFilterModel filterRaspunsuriBanci = new RaspunsuriBanciReportFilterModel();
		if (filter.getInstitutieId() != null) {
			NomenclatorValueModel institutiteNomenclatorValueModel = nomenclatorInstitutiiValuesById.get(filter.getInstitutieId());
			String denumireInstitutie = NomenclatorValueUtils.getAttributeValueAsString(institutiteNomenclatorValueModel, NomenclatorConstants.INSTITUTII_ATTRIBUTE_KEY_DENUMIRE);
			filterRaspunsuriBanci.setDenumireBanca(denumireInstitutie);
		}

		List<RaspunsuriBanciReportRowModel> raspunsuriBanciReport = getRaspunsuriBanciReport(filterRaspunsuriBanci);
		Map<String, RaspunsuriBanciReportRowModel> raspunsuriBanciRowByDenBanca = new HashMap<>();
		raspunsuriBanciReport.forEach(row -> {
			raspunsuriBanciRowByDenBanca.put(row.getDenumireBanca(), row);
		});

		LevelByFunctiiPersoaneAndInstitutiiSearcher levelSearcher = new LevelByFunctiiPersoaneAndInstitutiiSearcher(nomenclatorService);
		

		Map<String, Long> nrInterogariByBanca = registruIntrariIesiriDao.getNrInterogariByBancaAsMap();
		Map<Long, Long> nrRaspunsuriByBanca = registruIntrariIesiriDao.getNrRaspunsuriByBancaAsMap();

		PrezentaReprezentativitateReportPreparator reportPreparator = new PrezentaReprezentativitateReportPreparator(filter, reportDocuments, nomenclatorInstitutiiValuesById,
				prezentaComisiiGlDocumentType, raspunsuriBanciRowByDenBanca, arbConstants, workflowInstanceDao, parametersDao, levelSearcher, institutiiMembriiArbIds,
				nrInterogariByBanca, nrRaspunsuriByBanca);

		return reportPreparator.prepareReport();

	}

	public List<RaspunsuriBanciReportRowModel> getRaspunsuriBanciReport(RaspunsuriBanciReportFilterModel filter) {

		List<RegistruIesiri> regIesiri = registruIntrariIesiriDao.getAllRegistruIesiriByRaspunsuriBanciReportFilterModel(filter);
		Map<String, Long> nrInterogariByBanca = registruIntrariIesiriDao.getNrInterogariByBancaAsMapByRaspunsuriBanciFilter(filter);

		RaspunsuriBanciReportPreparator reportPreparator = new RaspunsuriBanciReportPreparator(parametersDao, nrInterogariByBanca, regIesiri, filter);
		return reportPreparator.prepareReport();
	}

	@Override
	public RaspunsuriBanciReportBundleModel getRaspunsuriBanciReportBundle() {
		RaspunsuriBanciReportBundleModel bundle = new RaspunsuriBanciReportBundleModel();
		bundle.setDenumiriBanci(registruIntrariIesiriDao.getAllDenumiriBanciFromRegistruIesiriDestinatariAsSelectItems());
		bundle.setProiecte(registruIntrariIesiriDao.getAllProjectsFromRegistruIesiriAsSelectItems());
		return bundle;
	}

	@Override
	public CentralizatorPrezentaPerioadaReportModel getCentralizatorPrezentaPerioadaReport(CentralizatorPrezentaPerioadaReportFilterModel filter) throws AppException {

		Map<Long, String> nomenclatorComisiiUiAttrValuesAsMap = nomenclatorService
				.getJoinedNomenclatorUiAtributesValuesAsMapByNomenclatorCode(NomenclatorConstants.COMISII_SAU_GL_NOMENCLATOR_CODE);
		Map<Long, String> nomenclatorInstitutiiUiAttrValuesAsMap = nomenclatorService
				.getJoinedNomenclatorUiAtributesValuesAsMapByNomenclatorCode(NomenclatorConstants.INSTITUTII_NOMENCLATOR_CODE);

		DocumentType prezentaComisiiGlDocumentType = documentTypeService.getDocumentTypeByName(arbConstants.getDocumentPrezentaComisieGlConstants().getDocumentTypeName());
		List<Document> reportDocuments = documentReportPlugin.getCentralizatorPrezentaPerioadaReport(filter);

		Map<Long, List<CentralizatorPrezentaPerioadaReportMapModel>> rowsBancaReportAsMap = new HashMap<>();

		if (CollectionUtils.isNotEmpty(reportDocuments)) {
			for (Document reportDocument : reportDocuments) {
				WorkflowInstance workflowInstance = workflowInstanceDao.getForDocument(reportDocument.getId());

				if (workflowInstance == null || workflowInstance.getStatus().equals(WorkflowInstance.STATUS_FINNISHED)) {
					Long comisieId = DocumentUtils.getMetadataNomenclatorValue(reportDocument, prezentaComisiiGlDocumentType,
							arbConstants.getDocumentPrezentaComisieGlConstants().getDenumireComisieGlMetadataName());

					List<CollectionInstance> metadataCollectionInfoParticipanti = DocumentUtils.getMetadataCollectionInstance(reportDocument, prezentaComisiiGlDocumentType,
							arbConstants.getDocumentPrezentaComisieGlConstants().getInformatiiParticipantiMetadataName());

					if (CollectionUtils.isNotEmpty(metadataCollectionInfoParticipanti)) {

						for (CollectionInstance metadataInfoMembrii : metadataCollectionInfoParticipanti) {
							CentralizatorPrezentaPerioadaReportMapModel row = new CentralizatorPrezentaPerioadaReportMapModel();
							row.setComisieId(comisieId);

							String infoInformatiiParticipantiMetadataName = arbConstants.getDocumentPrezentaComisieGlConstants().getInformatiiParticipantiMetadataName();

							MetadataDefinition metadataDefinitionInstitutie = DocumentTypeUtils.getMetadataDefinitionFromCollectionByName(prezentaComisiiGlDocumentType,
									infoInformatiiParticipantiMetadataName,
									arbConstants.getDocumentPrezentaComisieGlConstants().getNumeInstitutieOfInformatiiParticipantiMetadataName());
							MetadataDefinition metadataDefinitionFunctie = DocumentTypeUtils.getMetadataDefinitionFromCollectionByName(prezentaComisiiGlDocumentType,
									infoInformatiiParticipantiMetadataName,
									arbConstants.getDocumentPrezentaComisieGlConstants().getFunctieMembruOfInformatiiParticipantiMetadataName());
							MetadataDefinition metadataDefinitionCalitate = DocumentTypeUtils.getMetadataDefinitionFromCollectionByName(prezentaComisiiGlDocumentType,
									infoInformatiiParticipantiMetadataName,
									arbConstants.getDocumentPrezentaComisieGlConstants().getCalitateParticipantOfInformatiiParticipantiMetadataName());

							boolean areFilterConditionRespected = true;
							Long institutieId = null;
							for (MetadataInstance metadataInstance : metadataInfoMembrii.getMetadataInstanceList()) {
								if (metadataInstance.getMetadataDefinitionId().equals(metadataDefinitionInstitutie.getId())) {
									institutieId = Long.parseLong(metadataInstance.getValue());
									if (filter.getBancaId() != null && !institutieId.equals(filter.getBancaId())) {
										areFilterConditionRespected = false;
									}

									row.setBancaId(institutieId);
								}
								if (metadataInstance.getMetadataDefinitionId().equals(metadataDefinitionFunctie.getId())) {
									row.setFunctia(metadataInstance.getValue());
								}
								if (metadataInstance.getMetadataDefinitionId().equals(metadataDefinitionCalitate.getId())) {
									row.setCalitate(metadataInstance.getValue());
								}

							}

							if (areFilterConditionRespected && institutieId != null) {
								if (!rowsBancaReportAsMap.containsKey(institutieId)) {
									List<CentralizatorPrezentaPerioadaReportMapModel> items = new ArrayList<CentralizatorPrezentaPerioadaReportMapModel>();

									items.add(row);
									rowsBancaReportAsMap.put(institutieId, items);
								} else {
									List<CentralizatorPrezentaPerioadaReportMapModel> items = rowsBancaReportAsMap.get(institutieId);

									items.add(row);
								}
							}
						}
					}
				}
			}
		}
		
		Map<Long, Map<Long, List<CentralizatorPrezentaPerioadaReportMapModel>>> rowsBancaComisieReportAsMap = new HashMap<>();

		for (List<CentralizatorPrezentaPerioadaReportMapModel> rows : rowsBancaReportAsMap.values()) {
			Map<Long, List<CentralizatorPrezentaPerioadaReportMapModel>> rowsComisieReportAsMap = new HashMap<>();

			for (CentralizatorPrezentaPerioadaReportMapModel row : rows) {
				Long comisieKey = row.getComisieId();

				if (!rowsComisieReportAsMap.containsKey(comisieKey)) {
					List<CentralizatorPrezentaPerioadaReportMapModel> items = new ArrayList<CentralizatorPrezentaPerioadaReportMapModel>();

					items.add(row);
					rowsComisieReportAsMap.put(comisieKey, items);
				} else {
					List<CentralizatorPrezentaPerioadaReportMapModel> items = rowsComisieReportAsMap.get(comisieKey);

					items.add(row);
				}
			}
			rowsBancaComisieReportAsMap.put(rows.get(0).getBancaId(), rowsComisieReportAsMap);

		}
		
		CentralizatorPrezentaPerioadaReportModel report = new CentralizatorPrezentaPerioadaReportModel();
		List<CentralizatorPrezentaPerioadaReportRowModel> rows = new ArrayList<CentralizatorPrezentaPerioadaReportRowModel>();

		Integer totalParticipariLevel0 = 0;
		Integer totalParticipariLevel1 = 0;
		Integer totalParticipariLevel2 = 0;
		Integer totalParticipariLevel3 = 0;
		Integer totalParticipariLevel3Plus = 0;
		Integer totalParticipariInAfaraNom = 0;
		
		LevelByFunctiiPersoaneAndInstitutiiSearcher levelSearcher = new LevelByFunctiiPersoaneAndInstitutiiSearcher(nomenclatorService);
		Map<String, Long> levelIdByName = levelSearcher.getLevelIdByNameMap();

		for (Long bancaKey : rowsBancaComisieReportAsMap.keySet()) {
			Map<Long, List<CentralizatorPrezentaPerioadaReportMapModel>> banci = rowsBancaComisieReportAsMap.get(bancaKey);

			Integer totalParticipariBancaLevel0 = 0;
			Integer totalParticipariBancaLevel1 = 0;
			Integer totalParticipariBancaLevel2 = 0;
			Integer totalParticipariBancaLevel3 = 0;
			Integer totalParticipariBancaLevel3Plus = 0;
			Integer totalParticipariBancaInAfaraNom = 0;

			for (Long comisieKey : banci.keySet()) {
				List<CentralizatorPrezentaPerioadaReportMapModel> comisii = banci.get(comisieKey);

				Integer totalParticipariConisieLevel0 = 0;
				Integer totalParticipariConisieLevel1 = 0;
				Integer totalParticipariConisieLevel2 = 0;
				Integer totalParticipariConisieLevel3 = 0;
				Integer totalParticipariConisieLevel3Plus = 0;
				Integer participariConisieInAfaraNom = 0;

				Long level0Id = levelIdByName.get(NomenclatorConstants.NOMENCLATOR_LEVEL_FUNCTII_PERSOANE_ATTRIBUTE_DENUMIRE_VALUE_FOR_LEVEL_0);
				Long level1Id = levelIdByName.get(NomenclatorConstants.NOMENCLATOR_LEVEL_FUNCTII_PERSOANE_ATTRIBUTE_DENUMIRE_VALUE_FOR_LEVEL_1);
				Long level2Id = levelIdByName.get(NomenclatorConstants.NOMENCLATOR_LEVEL_FUNCTII_PERSOANE_ATTRIBUTE_DENUMIRE_VALUE_FOR_LEVEL_2);
				Long level3Id = levelIdByName.get(NomenclatorConstants.NOMENCLATOR_LEVEL_FUNCTII_PERSOANE_ATTRIBUTE_DENUMIRE_VALUE_FOR_LEVEL_3);
				Long level3PlusId = levelIdByName.get(NomenclatorConstants.NOMENCLATOR_LEVEL_FUNCTII_PERSOANE_ATTRIBUTE_DENUMIRE_VALUE_FOR_LEVEL_3_PLUS);
				Long inAfaraNomId = REPORT_CENTRALIZATOR_PREZENTA_PERIOADA_IN_AFARA_NOM;

				for (CentralizatorPrezentaPerioadaReportMapModel comisie : comisii) {
					String functia = comisie.getFunctia();
					String calitateString = comisie.getCalitate();
					
					if (StringUtils.isBlank(calitateString)) {
						if (filter.getLevelId() == null || filter.getLevelId().equals(level3Id)) {
							totalParticipariConisieLevel3++;
						}
						continue;
					}
					Calitate calitate = Calitate.valueOf(calitateString.toUpperCase());
					if (calitate == null) {
						if (filter.getLevelId() == null || filter.getLevelId().equals(level3Id)) {
							totalParticipariConisieLevel3++;
						}
					} else if (calitate.equals(Calitate.INLOCUITOR)) {
						if (filter.getLevelId() == null || filter.getLevelId().equals(inAfaraNomId)) {
							participariConisieInAfaraNom++;
						}
					} else if (calitate.equals(Calitate.TITULAR) || calitate.equals(Calitate.SUPLEANT)) {
						String levelName = levelSearcher.getLevelNameByFunctieAndInstitutieId(functia, bancaKey);
						if (StringUtils.isBlank(levelName)) {
							if (filter.getLevelId() == null || filter.getLevelId().equals(level3Id)) {
								totalParticipariConisieLevel3++;
							}
						} else if (levelName.equals(NomenclatorConstants.NOMENCLATOR_LEVEL_FUNCTII_PERSOANE_ATTRIBUTE_DENUMIRE_VALUE_FOR_LEVEL_0)) {
							if (filter.getLevelId() == null || filter.getLevelId().equals(level0Id)) {
								totalParticipariConisieLevel0++;
							}
						} else if (levelName.equals(NomenclatorConstants.NOMENCLATOR_LEVEL_FUNCTII_PERSOANE_ATTRIBUTE_DENUMIRE_VALUE_FOR_LEVEL_1)) {
							if (filter.getLevelId() == null || filter.getLevelId().equals(level1Id)) {
								totalParticipariConisieLevel1++;
							}	
						} else if (levelName.equals(NomenclatorConstants.NOMENCLATOR_LEVEL_FUNCTII_PERSOANE_ATTRIBUTE_DENUMIRE_VALUE_FOR_LEVEL_2)) {
							if (filter.getLevelId() == null || filter.getLevelId().equals(level2Id)) {
								totalParticipariConisieLevel2++;
							}
						} else if (levelName.equals(NomenclatorConstants.NOMENCLATOR_LEVEL_FUNCTII_PERSOANE_ATTRIBUTE_DENUMIRE_VALUE_FOR_LEVEL_3)) {
							if (filter.getLevelId() == null || filter.getLevelId().equals(level3Id)) {
								totalParticipariConisieLevel3++;
							}
						} else {
							if (filter.getLevelId() == null || filter.getLevelId().equals(level3PlusId)) {
								totalParticipariConisieLevel3Plus++;
							}	
						}
					}
				}

				CentralizatorPrezentaPerioadaReportRowModel row = new CentralizatorPrezentaPerioadaReportRowModel();

				row.setBanca(nomenclatorInstitutiiUiAttrValuesAsMap.get(bancaKey));
				row.setComisie(nomenclatorComisiiUiAttrValuesAsMap.get(comisieKey));
				row.setParticipariLevel0(totalParticipariConisieLevel0);
				row.setParticipariLevel1(totalParticipariConisieLevel1);
				row.setParticipariLevel2(totalParticipariConisieLevel2);
				row.setParticipariLevel3(totalParticipariConisieLevel3);
				row.setParticipariLevel3Plus(totalParticipariConisieLevel3Plus);
				row.setParticipariInAfaraNom(participariConisieInAfaraNom);
				rows.add(row);

				totalParticipariBancaLevel0 += totalParticipariConisieLevel0;
				totalParticipariBancaLevel1 += totalParticipariConisieLevel1;
				totalParticipariBancaLevel2 += totalParticipariConisieLevel2;
				totalParticipariBancaLevel3 += totalParticipariConisieLevel3;
				totalParticipariBancaLevel3Plus += totalParticipariConisieLevel3Plus;
				totalParticipariBancaInAfaraNom += participariConisieInAfaraNom;
			}

			CentralizatorPrezentaPerioadaReportRowModel row = new CentralizatorPrezentaPerioadaReportRowModel();

			row.setBanca(nomenclatorInstitutiiUiAttrValuesAsMap.get(bancaKey));
			row.setParticipariLevel0(totalParticipariBancaLevel0);
			row.setParticipariLevel1(totalParticipariBancaLevel1);
			row.setParticipariLevel2(totalParticipariBancaLevel2);
			row.setParticipariLevel3(totalParticipariBancaLevel3);
			row.setParticipariLevel3Plus(totalParticipariBancaLevel3Plus);
			row.setParticipariInAfaraNom(totalParticipariBancaInAfaraNom);

			rows.add(row);

			totalParticipariLevel0 += totalParticipariBancaLevel0;
			totalParticipariLevel1 += totalParticipariBancaLevel1;
			totalParticipariLevel2 += totalParticipariBancaLevel2;
			totalParticipariLevel3 += totalParticipariBancaLevel3;
			totalParticipariLevel3Plus += totalParticipariBancaLevel3Plus;
			totalParticipariInAfaraNom+= totalParticipariBancaInAfaraNom;
		}

		report.setRows(rows);

		report.setTotalParticipariLevel0(totalParticipariLevel0);
		report.setTotalParticipariLevel1(totalParticipariLevel1);
		report.setTotalParticipariLevel2(totalParticipariLevel2);
		report.setTotalParticipariLevel3(totalParticipariLevel3);
		report.setTotalParticipariLevel3Plus(totalParticipariLevel3Plus);
		report.setTotalParticipariInAfaraNom(totalParticipariInAfaraNom);

		return report;
	}

	private List<Long> getAllIdsForInstitutiiMembriiArb() throws AppException {

		List<Long> institutiiMembriiArbIds = new ArrayList<Long>();
		List<NomenclatorFilter> tipInstitutieFilters = Arrays
				.asList(new NomenclatorSimpleFilter(NomenclatorConstants.TIP_INSTITUTIE_DENUMIRE_ATTRIBUTE_KEY_COD, NomenclatorConstants.TIP_INSTITUTII_VALUE_FOR_MEMBRU_ARB));
		GetNomenclatorValuesRequestModel requestModelTipInstitutie = new GetNomenclatorValuesRequestModel(NomenclatorConstants.TIP_INSTITUTIE, tipInstitutieFilters);
		String tipInstitutiiMembruArbId = nomenclatorService.getNomenclatorValues(requestModelTipInstitutie).get(0).getId().toString();

		List<NomenclatorFilter> institutiiFilters = Arrays
				.asList(new NomenclatorSimpleFilter(NomenclatorConstants.INSTITUTII_ATTRIBUTE_KEY_TIP_INSTITUTIE, tipInstitutiiMembruArbId));
		List<NomenclatorValueModel> institutiiMembruArb = nomenclatorService
				.getNomenclatorValues(new GetNomenclatorValuesRequestModel(NomenclatorConstants.INSTITUTII_NOMENCLATOR_CODE, institutiiFilters));
		for (NomenclatorValueModel institutie : institutiiMembruArb) {
			institutiiMembriiArbIds.add(institutie.getId());
		}

		return institutiiMembriiArbIds;
	}

	@Override
	public NotaGeneralaPeMembriiArbReportModel getNotaGeneralaPeMembriiArbReport(NotaGeneralaPeMembriiArbReportFilterModel filter) throws AppException {

		DocumentType prezentaComisiiGlDocumentType = documentTypeService.getDocumentTypeByName(arbConstants.getDocumentPrezentaComisieGlConstants().getDocumentTypeName());
		List<Document> reportDocuments = documentReportPlugin.getNotaGeneralaPeMembriiArbReport(filter);

		List<Long> institutiiMembriiArbIds = getAllIdsForInstitutiiMembriiArb();

		LevelByFunctiiPersoaneAndInstitutiiSearcher levelSearcher = new LevelByFunctiiPersoaneAndInstitutiiSearcher(nomenclatorService);

		NotaGeneralaPeMembriiArbReportPreparator notaGeneralaPeMembriiArbReportServiceHelper = new NotaGeneralaPeMembriiArbReportPreparator(parametersDao, nomenclatorService,
				registruIntrariIesiriDao, institutiiMembriiArbIds, reportDocuments, prezentaComisiiGlDocumentType, workflowInstanceDao, arbConstants, filter, levelSearcher);

		return notaGeneralaPeMembriiArbReportServiceHelper.prepareReport();

	}

	@Override
	public List<NotaGeneralaReportRowModel> getNotaGeneralaReport(NotaGeneralaReportFilterModel filter) throws AppException {
		
		PrezentaReprezentivitateReportFilterModel filterReprezentativitate = new PrezentaReprezentivitateReportFilterModel();
		filterReprezentativitate.setInstitutieId(filter.getInstitutieId());
		filterReprezentativitate.setDataInceputSedintaDeLa(filter.getDataInceputSedintaDeLa());
		filterReprezentativitate.setDataInceputSedintaPanaLa(filter.getDataInceputSedintaPanaLa());
		List<Document> reportDocuments = documentReportPlugin.getDocumentsForPrezentaComisiiArbReprezentativitateReport(filterReprezentativitate);
		List<NomenclatorValueModel> nomenclatorValuesForInstitutii = nomenclatorService.getNomenclatorValuesByNomenclatorCode(NomenclatorConstants.INSTITUTII_NOMENCLATOR_CODE);

		List<Long> institutiiMembriiArbIds = getAllIdsForInstitutiiMembriiArb();

		Map<Long, NomenclatorValueModel> nomenclatorInstitutiiValuesById = new HashMap<>();
		nomenclatorValuesForInstitutii.forEach(institutiteNomValues -> {
			nomenclatorInstitutiiValuesById.put(institutiteNomValues.getId(), institutiteNomValues);
		});

		DocumentType prezentaComisiiGlDocumentType = documentTypeService.getDocumentTypeByName(arbConstants.getDocumentPrezentaComisieGlConstants().getDocumentTypeName());
		RaspunsuriBanciReportFilterModel filterRaspunsuriBanci = new RaspunsuriBanciReportFilterModel();
		if (filter.getInstitutieId() != null) {
			NomenclatorValueModel institutiteNomenclatorValueModel = nomenclatorInstitutiiValuesById.get(filter.getInstitutieId());
			String denumireInstitutie = NomenclatorValueUtils.getAttributeValueAsString(institutiteNomenclatorValueModel, NomenclatorConstants.INSTITUTII_ATTRIBUTE_KEY_DENUMIRE);
			filterRaspunsuriBanci.setDenumireBanca(denumireInstitutie);
		}

		List<RaspunsuriBanciReportRowModel> raspunsuriBanciReport = getRaspunsuriBanciReport(filterRaspunsuriBanci);
		Map<String, RaspunsuriBanciReportRowModel> raspunsuriBanciRowByDenBanca = new HashMap<>();
		raspunsuriBanciReport.forEach(row -> {
			raspunsuriBanciRowByDenBanca.put(row.getDenumireBanca(), row);
		});

		LevelByFunctiiPersoaneAndInstitutiiSearcher levelSearcher = new LevelByFunctiiPersoaneAndInstitutiiSearcher(nomenclatorService);

		Map<String, Long> nrInterogariByBanca = registruIntrariIesiriDao.getNrInterogariByBancaAsMap();
		Map<Long, Long> nrRaspunsuriByBanca = registruIntrariIesiriDao.getNrRaspunsuriByBancaAsMap();

		NotaGeneralaReportPreparator reportPreparator = new NotaGeneralaReportPreparator(filter, reportDocuments, nomenclatorInstitutiiValuesById,
				prezentaComisiiGlDocumentType, raspunsuriBanciRowByDenBanca, arbConstants, workflowInstanceDao, parametersService, levelSearcher, institutiiMembriiArbIds,
				nrInterogariByBanca, nrRaspunsuriByBanca);
		return reportPreparator.prepareReport();
	}

	@Override
	public NivelReprezentareComisiiReportModel getNivelReprezentareComisiiReport(NivelReprezentareComisiiReportFilterModel filter) throws AppException {
		
		NivelReprezentareComisiiReportPreparator reportPreparator = new NivelReprezentareComisiiReportPreparator(reprezentantiComisieSauGLDao, nomenclatorService, filter);
		
		return reportPreparator.prepareReport();
	}

	@Override
	public NoteBanciReportModel getNoteBanciReport(NoteBanciReportFilterModel filter) throws AppException {
		PrezentaReprezentivitateReportFilterModel filterReprezentativitate = new PrezentaReprezentivitateReportFilterModel();
		filterReprezentativitate.setInstitutieId(filter.getInstitutieId());
		filterReprezentativitate.setDataInceputSedintaDeLa(filter.getDataInceputSedintaDeLa());
		filterReprezentativitate.setDataInceputSedintaPanaLa(filter.getDataInceputSedintaPanaLa());
		List<Document> reportDocuments = documentReportPlugin.getDocumentsForPrezentaComisiiArbReprezentativitateReport(filterReprezentativitate);
		
		List<NomenclatorValueModel> nomenclatorValuesForInstitutii = nomenclatorService.getNomenclatorValuesByNomenclatorCode(NomenclatorConstants.INSTITUTII_NOMENCLATOR_CODE);

		List<Long> institutiiMembriiArbIds = getAllIdsForInstitutiiMembriiArb();

		Map<Long, NomenclatorValueModel> nomenclatorInstitutiiValuesById = new HashMap<>();
		nomenclatorValuesForInstitutii.forEach(institutiteNomValues -> {
			nomenclatorInstitutiiValuesById.put(institutiteNomValues.getId(), institutiteNomValues);
		});

		DocumentType prezentaComisiiGlDocumentType = documentTypeService.getDocumentTypeByName(arbConstants.getDocumentPrezentaComisieGlConstants().getDocumentTypeName());
		RaspunsuriBanciReportFilterModel filterRaspunsuriBanci = new RaspunsuriBanciReportFilterModel();
		if (filter.getInstitutieId() != null) {
			NomenclatorValueModel institutiteNomenclatorValueModel = nomenclatorInstitutiiValuesById.get(filter.getInstitutieId());
			String denumireInstitutie = NomenclatorValueUtils.getAttributeValueAsString(institutiteNomenclatorValueModel, NomenclatorConstants.INSTITUTII_ATTRIBUTE_KEY_DENUMIRE);
			filterRaspunsuriBanci.setDenumireBanca(denumireInstitutie);
		}

		LevelByFunctiiPersoaneAndInstitutiiSearcher levelSearcher = new LevelByFunctiiPersoaneAndInstitutiiSearcher(nomenclatorService);

		NoteBanciReportPreparator reportPreparator = new NoteBanciReportPreparator(workflowInstanceDao, filter, reportDocuments, nomenclatorInstitutiiValuesById, prezentaComisiiGlDocumentType,
				arbConstants, parametersService, levelSearcher, institutiiMembriiArbIds);

		return reportPreparator.prepareReport();
	}
}
