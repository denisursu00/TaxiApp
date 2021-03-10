package ro.cloudSoft.cloudDoc.plugins.content.arb.report;

import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.Validate;

import ro.cloudSoft.cloudDoc.core.AppException;
import ro.cloudSoft.cloudDoc.core.constants.arb.ArbConstants;
import ro.cloudSoft.cloudDoc.domain.content.Document;
import ro.cloudSoft.cloudDoc.domain.content.DocumentIdentifier;
import ro.cloudSoft.cloudDoc.plugins.content.DocumentLocationPlugin;
import ro.cloudSoft.cloudDoc.plugins.content.JR_PluginBase;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.report.ActiuniPeProiectReportFilterModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.report.CentralizatorPrezentaPerioadaReportFilterModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.report.CereriConcediuReportFilterModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.report.MembriiAfiliatiReportFilterModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.report.NotaGeneralaPeMembriiArbReportFilterModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.report.NumarParticipantiSedinteComisieGlReportFilterModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.report.NumarSedinteCdPvgSiParticipantiReportFilterModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.report.NumarSedinteComisieGlReportFilterModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.report.PrezentaAgaFilterModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.report.PrezentaComisiiGlInIntervalReportFilterModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.report.PrezentaReprezentivitateReportFilterModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.report.PrezentaSedintaCdPvgInvitatiARBReportFilterModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.report.PrezentaSedintaCdPvgInvitatiExterniFilterModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.report.PrezentaSedintaCdPvgMembriiReportFilterModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.report.ReprezentantiBancaPerFunctieSiComisieReportFilterModel;
import ro.cloudSoft.cloudDoc.services.bpm.WorkflowService;
import ro.cloudSoft.cloudDoc.services.content.DocumentTypeService;
import ro.cloudSoft.cloudDoc.utils.log.LogHelper;

public class JR_DocumentReportPlugin extends JR_PluginBase implements DocumentReportPlugin {
	
	private static final LogHelper LOGGER = LogHelper.getInstance(JR_DocumentReportPlugin.class);
	
	private DocumentLocationPlugin documentLocationPlugin;	
	private DocumentTypeService documentTypeService;
	private ArbConstants arbConstants;
	private WorkflowService workflowService;
	
	public void setDocumentLocationPlugin(DocumentLocationPlugin documentLocationPlugin) {
		this.documentLocationPlugin = documentLocationPlugin;
	}
	public void setArbConstants(ArbConstants arbConstants) {
		this.arbConstants = arbConstants;
	}
	public void setDocumentTypeService(DocumentTypeService documentTypeService) {
		this.documentTypeService = documentTypeService;
	}
	public void setWorkflowService(WorkflowService workflowService) {
		this.workflowService = workflowService;
	}
	
	@Override
	public List<Document> getDocumentsForNumarSedinteCdPvgSiParticipantiReport(NumarSedinteCdPvgSiParticipantiReportFilterModel filter) throws AppException {
		
		Validate.notNull(filter.getTipSedinta(), "tip sedinta cannot be null");
		
		List<Document> documentResults = new LinkedList<>();
		
		Set<String> documentLocationRealNames = documentLocationPlugin.getAllDocumentLocationRealNames();
		if (CollectionUtils.isNotEmpty(documentLocationRealNames)) {
			NumarSedinteCdPvgSiParticipantiDocumentReportSearcher documentReportSearcher = new NumarSedinteCdPvgSiParticipantiDocumentReportSearcher(this.repository, this.credentials, filter, this.arbConstants, this.documentTypeService);
			for (String documentLocationRealName : documentLocationRealNames) {				
				List<Document> dlDocuments = documentReportSearcher.search(documentLocationRealName);
				if (CollectionUtils.isNotEmpty(dlDocuments)) {
					documentResults.addAll(dlDocuments);
				}
			}
		}
		
		return documentResults;
	}

	@Override
	public List<Document> getDocumentsForPrezentaSedinteCdPvgInvitatiExterniReport(PrezentaSedintaCdPvgInvitatiExterniFilterModel filter) throws AppException {
		Validate.notNull(filter.getTipSedinta(), "tip sedinta cannot be null");
		
		List<Document> documentResults = new LinkedList<>();
		
		Set<String> documentLocationRealNames = documentLocationPlugin.getAllDocumentLocationRealNames();
		if (CollectionUtils.isNotEmpty(documentLocationRealNames)) {
			PrezentaSedinteCdPvgInvitatiExterniReportSearcher documentReportSearcher = new PrezentaSedinteCdPvgInvitatiExterniReportSearcher(this.repository, this.credentials, filter, this.arbConstants, this.documentTypeService);
			for (String documentLocationRealName : documentLocationRealNames) {				
				List<Document> dlDocuments = documentReportSearcher.search(documentLocationRealName);
				if (CollectionUtils.isNotEmpty(dlDocuments)) {
					documentResults.addAll(dlDocuments);
				}
			}
		}
		
		return documentResults;
	}	
	
	public List<Document> getDocumentsForPrezentaSedinteCdPvgMembriiReport(PrezentaSedintaCdPvgMembriiReportFilterModel filter)	throws AppException {
		Validate.notNull(filter.getTipSedinta(), "tip sedinta cannot be null");
		
		List<Document> documentResults = new LinkedList<>();
		
		Set<String> documentLocationRealNames = documentLocationPlugin.getAllDocumentLocationRealNames();
		if (CollectionUtils.isNotEmpty(documentLocationRealNames)) {			
			PrezentaSendinteCdPvgMembriiDocumentReportSearcher documentReportSearcher = new PrezentaSendinteCdPvgMembriiDocumentReportSearcher(this.repository, this.credentials, filter, this.arbConstants, this.documentTypeService);
			for (String documentLocationRealName : documentLocationRealNames) {				
				List<Document> dlDocuments = documentReportSearcher.search(documentLocationRealName);
				if (CollectionUtils.isNotEmpty(dlDocuments)) {
					documentResults.addAll(dlDocuments);
				}
			}
		}
		
		return documentResults;
	}

	@Override
	public List<Document> getPrezentaSedinteCdPvgInvitatiArbReport(
			PrezentaSedintaCdPvgInvitatiARBReportFilterModel filter) throws AppException {
		Validate.notNull(filter.getTipSedinta(), "tip sedinta cannot be null");
		
		List<Document> documentResults = new LinkedList<>();
		
		Set<String> documentLocationRealNames = documentLocationPlugin.getAllDocumentLocationRealNames();
		if (CollectionUtils.isNotEmpty(documentLocationRealNames)) {			
			PrezentaSedinteCdPvgInvitatiArbSearcher documentReportSearcher = new PrezentaSedinteCdPvgInvitatiArbSearcher(this.repository, this.credentials, filter, this.arbConstants, this.documentTypeService);
			for (String documentLocationRealName : documentLocationRealNames) {				
				List<Document> dlDocuments = documentReportSearcher.search(documentLocationRealName);
				if (CollectionUtils.isNotEmpty(dlDocuments)) {
					documentResults.addAll(dlDocuments);
				}
			}
		}
		
		return documentResults;
	}
	@Override
	public List<Document> getDocumentsForPrezentaComisiiGlInIntervalReport(
			PrezentaComisiiGlInIntervalReportFilterModel filter) throws AppException {
		List<Document> documentResults = new LinkedList<>();
		
		Set<String> documentLocationRealNames = documentLocationPlugin.getAllDocumentLocationRealNames();
		if (CollectionUtils.isNotEmpty(documentLocationRealNames)) {
			PrezentaComisiiGlInIntervalReportSearcher documentReportSearcher = new PrezentaComisiiGlInIntervalReportSearcher(this.repository, this.credentials, filter, this.arbConstants, this.documentTypeService);
			for (String documentLocationRealName : documentLocationRealNames) {				
				List<Document> dlDocuments = documentReportSearcher.search(documentLocationRealName);
				if (CollectionUtils.isNotEmpty(dlDocuments)) {
					documentResults.addAll(dlDocuments);
				}
			}
		}
		
		return documentResults;
	}
	
	@Override
	public List<Document> getDocumentsForPrezentaAgaReport(PrezentaAgaFilterModel filter) throws AppException {

		List<Document> documentResults = new LinkedList<>();
		
		Set<String> documentLocationRealNames = documentLocationPlugin.getAllDocumentLocationRealNames();
		if (CollectionUtils.isNotEmpty(documentLocationRealNames)) {
			PrezentaAgaReportSearcher documentReportSearcher = new PrezentaAgaReportSearcher(this.repository, this.credentials, filter, this.arbConstants, this.documentTypeService);
			for (String documentLocationRealName : documentLocationRealNames) {				
				List<Document> dlDocuments = documentReportSearcher.search(documentLocationRealName);
				if (CollectionUtils.isNotEmpty(dlDocuments)) {
					documentResults.addAll(dlDocuments);
				}
			}
		}
		
		return documentResults;
	}
	
	@Override
	public List<Document> getDocumentsMinutaPrezentaComisiiGlForActiuniOrgArbReport(DocumentIdentifier documentIdentifierPrezentaComisiiGlFilter)
			throws AppException {
		List<Document> documentResults = new LinkedList<>();
		
		Set<String> documentLocationRealNames = documentLocationPlugin.getAllDocumentLocationRealNames();
		if (CollectionUtils.isNotEmpty(documentLocationRealNames)) {
			MinutaSedintaComGLForActiuniOrgArbReportSearcher documentReportSearcher = new MinutaSedintaComGLForActiuniOrgArbReportSearcher(this.repository, this.credentials, documentIdentifierPrezentaComisiiGlFilter, this.arbConstants, this.documentTypeService);
			for (String documentLocationRealName : documentLocationRealNames) {				
				List<Document> dlDocuments = documentReportSearcher.search(documentLocationRealName);
				if (CollectionUtils.isNotEmpty(dlDocuments)) {
					documentResults.addAll(dlDocuments);
				}
			}
		}
		
		return documentResults;
	}
	
	@Override
	public List<Document> getDocumentsPrezentaCdPvgForActiuniOrgArbReport(DocumentIdentifier documentIdentifierOrdineDeZiFilter) throws AppException {
		List<Document> documentResults = new LinkedList<>();
		
		Set<String> documentLocationRealNames = documentLocationPlugin.getAllDocumentLocationRealNames();
		if (CollectionUtils.isNotEmpty(documentLocationRealNames)) {
			PrezentaSedintaCdPvgForActiuniOrgArbReportSearcher documentReportSearcher = new PrezentaSedintaCdPvgForActiuniOrgArbReportSearcher(this.repository, this.credentials, documentIdentifierOrdineDeZiFilter, this.arbConstants, this.documentTypeService);
			for (String documentLocationRealName : documentLocationRealNames) {				
				List<Document> dlDocuments = documentReportSearcher.search(documentLocationRealName);
				if (CollectionUtils.isNotEmpty(dlDocuments)) {
					documentResults.addAll(dlDocuments);
				}
			}
		}
		
		return documentResults;
	}
			
	public List<Document> getDocumentsForNumarParticipantiSedinteComisieGlReport(
			NumarParticipantiSedinteComisieGlReportFilterModel filter) throws AppException {
		List<Document> documentResults = new LinkedList<>();
		
		Set<String> documentLocationRealNames = documentLocationPlugin.getAllDocumentLocationRealNames();
		if (CollectionUtils.isNotEmpty(documentLocationRealNames)) {
			NumarParticipantiSedinteComisieGlReportSearcher documentReportSearcher = new NumarParticipantiSedinteComisieGlReportSearcher(this.repository, this.credentials, filter, this.arbConstants, this.documentTypeService);
			for (String documentLocationRealName : documentLocationRealNames) {				
				List<Document> dlDocuments = documentReportSearcher.search(documentLocationRealName);
				if (CollectionUtils.isNotEmpty(dlDocuments)) {
					documentResults.addAll(dlDocuments);
				}
			}
		}
		
		return documentResults;
	}
	
	@Override
	public List<Document> getDocumentsForNumarSedinteComisieGlReport(NumarSedinteComisieGlReportFilterModel filter)
			throws AppException {
		List<Document> documentResults = new LinkedList<>();
		
		Set<String> documentLocationRealNames = documentLocationPlugin.getAllDocumentLocationRealNames();
		
		if (CollectionUtils.isNotEmpty(documentLocationRealNames)) {
			NumarSedinteComisieGlReportSearcher documentReportSearcher = new NumarSedinteComisieGlReportSearcher(this.repository, this.credentials, filter, this.arbConstants, this.documentTypeService);
			for (String documentLocationRealName : documentLocationRealNames) {				
				List<Document> dlDocuments = documentReportSearcher.search(documentLocationRealName);
				if (CollectionUtils.isNotEmpty(dlDocuments)) {
					documentResults.addAll(dlDocuments);
				}
			}
		}
		
		return documentResults;
	}
	
	@Override
	public List<Document> getDocumentsForMembriiAfiliati(MembriiAfiliatiReportFilterModel filter) throws AppException {
		List<Document> documentResults = new LinkedList<>();
		
		Set<String> documentLocationRealNames = documentLocationPlugin.getAllDocumentLocationRealNames();
		if (CollectionUtils.isNotEmpty(documentLocationRealNames)) {
			MembriiAfiliatiReportSearcher documentReportSearcher = new MembriiAfiliatiReportSearcher(this.repository, this.credentials, filter, this.arbConstants, this.documentTypeService);
			for (String documentLocationRealName : documentLocationRealNames) {				
				List<Document> dlDocuments = documentReportSearcher.search(documentLocationRealName);
				if (CollectionUtils.isNotEmpty(dlDocuments)) {
					documentResults.addAll(dlDocuments);
				}
			}
		}
		
		return documentResults;
	}
	@Override
	public List<Document> getDocumentsMinutaPrezentaComisiiGlForParticipareReprzArbLaActiuniInAfaraAsocReport(
			DocumentIdentifier documentPrezentaComisiiGlFilter) throws AppException {
		List<Document> documentResults = new LinkedList<>();
		
		Set<String> documentLocationRealNames = documentLocationPlugin.getAllDocumentLocationRealNames();
		if (CollectionUtils.isNotEmpty(documentLocationRealNames)) {
			MinutaSedintaComGLForParticipareReprezArbLaActInAfaraAsocReportSearcher documentReportSearcher = new MinutaSedintaComGLForParticipareReprezArbLaActInAfaraAsocReportSearcher(this.repository, this.credentials, documentPrezentaComisiiGlFilter, this.arbConstants, this.documentTypeService);
			for (String documentLocationRealName : documentLocationRealNames) {				
				List<Document> dlDocuments = documentReportSearcher.search(documentLocationRealName);
				if (CollectionUtils.isNotEmpty(dlDocuments)) {
					documentResults.addAll(dlDocuments);
				}
			}
		}
		
		return documentResults;
	}
	
	@Override
	public List<Document> getDocumentsPrezentaCdPvgForParticipareReprzArbLaActiuniInAfaraAsocReport(DocumentIdentifier documentOrdineDeZiFilter) throws AppException {
		List<Document> documentResults = new LinkedList<>();
		
		Set<String> documentLocationRealNames = documentLocationPlugin.getAllDocumentLocationRealNames();
		if (CollectionUtils.isNotEmpty(documentLocationRealNames)) {
			PrezentaSedintaCdPvgForParticipantiReprzArbLaActInAfaraAsocReportSearcher documentReportSearcher = new PrezentaSedintaCdPvgForParticipantiReprzArbLaActInAfaraAsocReportSearcher(this.repository, this.credentials, documentOrdineDeZiFilter, this.arbConstants, this.documentTypeService);
			for (String documentLocationRealName : documentLocationRealNames) {				
				List<Document> dlDocuments = documentReportSearcher.search(documentLocationRealName);
				if (CollectionUtils.isNotEmpty(dlDocuments)) {
					documentResults.addAll(dlDocuments);
				}
			}
		}
		
		return documentResults;
	}
	@Override
	public List<Document> getDocumentsForCereriConcediuReportModel(CereriConcediuReportFilterModel filter, Long cererePersonalaAprobataWorkflowStateId, Long cerereNepersonalaAprobataWorkflowStateId) throws AppException {
		List<Document> documentResults = new LinkedList<>();
		
		Set<String> documentLocationRealNames = documentLocationPlugin.getAllDocumentLocationRealNames();
		if (CollectionUtils.isNotEmpty(documentLocationRealNames)) {
			CereriConcediuReportSearcher documentReportSearcher = new CereriConcediuReportSearcher(this.repository, this.credentials, filter, this.arbConstants, this.documentTypeService, cererePersonalaAprobataWorkflowStateId, cerereNepersonalaAprobataWorkflowStateId);
			for (String documentLocationRealName : documentLocationRealNames) {				
				List<Document> dlDocuments = documentReportSearcher.search(documentLocationRealName);
				if (CollectionUtils.isNotEmpty(dlDocuments)) {
					documentResults.addAll(dlDocuments);
				}
			}
		}
		
		return documentResults;
	}
	@Override
	public List<Document> getDocumentsForReprezentantiBancaPerFunctieSiComisieReportFilterModel(
			ReprezentantiBancaPerFunctieSiComisieReportFilterModel filter) throws AppException {

		List<Document> documentResults = new LinkedList<>();
		
		Set<String> documentLocationRealNames = documentLocationPlugin.getAllDocumentLocationRealNames();
		if (CollectionUtils.isNotEmpty(documentLocationRealNames)) {
			ReprezentantiBancaPerFunctieSiComisieReportSearcher documentReportSearcher = new ReprezentantiBancaPerFunctieSiComisieReportSearcher(this.repository, this.credentials, filter, this.arbConstants, this.documentTypeService);
			for (String documentLocationRealName : documentLocationRealNames) {				
				List<Document> dlDocuments = documentReportSearcher.search(documentLocationRealName);
				if (CollectionUtils.isNotEmpty(dlDocuments)) {
					documentResults.addAll(dlDocuments);
				}
			}
		}
		
		return documentResults;
	}
	@Override
	public List<Document> getCentralizatorPrezentaPerioadaReport(CentralizatorPrezentaPerioadaReportFilterModel filter)
			throws AppException {

		List<Document> documentResults = new LinkedList<>();
		
		Set<String> documentLocationRealNames = documentLocationPlugin.getAllDocumentLocationRealNames();
		if (CollectionUtils.isNotEmpty(documentLocationRealNames)) {
			CentralizatorPrezentaPerioadaReportSearcher documentReportSearcher = new CentralizatorPrezentaPerioadaReportSearcher(this.repository, this.credentials, filter, this.arbConstants, this.documentTypeService);
			for (String documentLocationRealName : documentLocationRealNames) {				
				List<Document> dlDocuments = documentReportSearcher.search(documentLocationRealName);
				if (CollectionUtils.isNotEmpty(dlDocuments)) {
					documentResults.addAll(dlDocuments);
				}
			}
		}
		
		return documentResults;
	}
	@Override
	public List<Document> getNotaGeneralaPeMembriiArbReport(NotaGeneralaPeMembriiArbReportFilterModel filter)
			throws AppException {

		List<Document> documentResults = new LinkedList<>();
		
		Set<String> documentLocationRealNames = documentLocationPlugin.getAllDocumentLocationRealNames();
		if (CollectionUtils.isNotEmpty(documentLocationRealNames)) {
			NotaGeneralaPeMembriiArbReportSearcher documentReportSearcher = new NotaGeneralaPeMembriiArbReportSearcher(this.repository, this.credentials, filter, this.arbConstants, this.documentTypeService);
			for (String documentLocationRealName : documentLocationRealNames) {				
				List<Document> dlDocuments = documentReportSearcher.search(documentLocationRealName);
				if (CollectionUtils.isNotEmpty(dlDocuments)) {
					documentResults.addAll(dlDocuments);
				}
			}
		}
		
		return documentResults;
	}
	
	public List<Document> getDocumentsForPrezentaComisiiArbReprezentativitateReport(PrezentaReprezentivitateReportFilterModel filter) throws AppException {

		List<Document> documentResults = new LinkedList<>();
		
		Set<String> documentLocationRealNames = documentLocationPlugin.getAllDocumentLocationRealNames();
		if (CollectionUtils.isNotEmpty(documentLocationRealNames)) {
			PrezentaComisiiArbReprezentativitateReportSearcher documentReportSearcher = new PrezentaComisiiArbReprezentativitateReportSearcher(this.repository, this.credentials, filter, this.arbConstants, this.documentTypeService);
			for (String documentLocationRealName : documentLocationRealNames) {				
				List<Document> dlDocuments = documentReportSearcher.search(documentLocationRealName);
				if (CollectionUtils.isNotEmpty(dlDocuments)) {
					documentResults.addAll(dlDocuments);
				}
			}
		}
		
		return documentResults;
	}
	
	@Override
	public List<Document> getDocumentsNoteCDForActiuniPeProiectReport(ActiuniPeProiectReportFilterModel filter) throws AppException {
		
		List<Document> documentResults = new LinkedList<>();
		
		Set<String> documentLocationRealNames = documentLocationPlugin.getAllDocumentLocationRealNames();
		if (CollectionUtils.isNotEmpty(documentLocationRealNames)) {
			ActiuniPeProiectNoteCDReportSearcher documentReportSearcher = new ActiuniPeProiectNoteCDReportSearcher(this.repository, this.credentials, filter, this.arbConstants, this.documentTypeService, this.workflowService);
			for (String documentLocationRealName : documentLocationRealNames) {				
				List<Document> dlDocuments = documentReportSearcher.search(documentLocationRealName);
				if (CollectionUtils.isNotEmpty(dlDocuments)) {
					documentResults.addAll(dlDocuments);
				}
			}
		}
		
		return documentResults;
	}
	
}
