package ro.cloudSoft.cloudDoc.web.rest.resources;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import ro.cloudSoft.cloudDoc.core.AppException;
import ro.cloudSoft.cloudDoc.domain.security.SecurityManager;
import ro.cloudSoft.cloudDoc.presentation.client.shared.exceptions.PresentationException;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.report.ActiuniOrganizateDeArbReportFilterModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.report.ActiuniOrganizateDeArbReportModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.report.ActiuniPeProiectReportFilterModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.report.ActiuniPeProiectReportModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.report.AderareOioroReportFilterModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.report.AderareOioroReportRowModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.report.CentralizatorPrezentaPerioadaReportFilterModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.report.CentralizatorPrezentaPerioadaReportModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.report.CereriConcediuReportBundle;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.report.CereriConcediuReportFilterModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.report.CereriConcediuReportModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.report.DashboardProiecteFilterBundle;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.report.DashboardProiecteFilterModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.report.DashboardProiecteRowReportModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.report.DecontCheltuieliAlteDeconturiReportFilterModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.report.DecontCheltuieliAlteDeconturiReportModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.report.DeplasariDeconturiReportBundleModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.report.DeplasariDeconturiReportFilterModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.report.DeplasariDeconturiReportRowModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.report.DocumentIdentifierModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.report.ListaProiectelorCareAuVizatActiunileLuniiReportFilterModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.report.ListaProiectelorCareAuVizatActiunileLuniiReportModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.report.MembriiAfiliatiReportFilterModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.report.MembriiAfiliatiReportModel;
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
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.report.NumarSedinteCdPvgSiParticipantiReportFilterModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.report.NumarSedinteCdPvgSiParticipantiReportModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.report.NumarSedinteComisieGlReportFilterModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.report.NumarSedinteComisieGlReportModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.report.ParticipareReprezentantiArbLaActiuniInAfaraAsociatieiReportFilterModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.report.ParticipareReprezentantiArbLaActiuniInAfaraAsociatieiReportModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.report.PrezentaAgaFilterModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.report.PrezentaAgaReportModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.report.PrezentaComisiiGlInIntervalReportBundle;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.report.PrezentaComisiiGlInIntervalReportFilterModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.report.PrezentaComisiiGlInIntervalReportModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.report.PrezentaReprezentivitateReportFilterModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.report.PrezentaReprezentivitateReportModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.report.PrezentaSedintaCdPvgInvitatiARBReportFilterModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.report.PrezentaSedintaCdPvgInvitatiARBReportModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.report.PrezentaSedintaCdPvgInvitatiExterniFilterModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.report.PrezentaSedintaCdPvgMembriiReportFilterModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.report.PrezentaSedintaCdPvgMembriiReportModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.report.PrezentaSedinteCdPvgInvitatiExterniReportModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.report.RaspunsuriBanciReportBundleModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.report.RaspunsuriBanciReportFilterModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.report.RaspunsuriBanciReportRowModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.report.ReprezentantiBancaPerFunctieSiComisieReportFilterModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.report.ReprezentantiBancaPerFunctieSiComisieReportModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.report.TaskuriCumulateReportFilterModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.report.TaskuriCumulateReportModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.report.cheltuieliArbSiReprezentantArb.CheltuieliArbSiReprezentantArbReportFilterModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.report.cheltuieliArbSiReprezentantArb.CheltuieliArbSiReprezentantArbReportModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.report.deplasareDecont.CheltuieliReprezentantArbReportFilterModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.report.deplasareDecont.CheltuieliReprezentantArbReportModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.report.documenteTrimiseDeArb.DocumenteTrimiseDeArbReportFilterModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.report.documenteTrimiseDeArb.DocumenteTrimiseDeArbReportModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.report.participariLaEvenimente.ParticipariEvenimenteReportFilterModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.report.participariLaEvenimente.ParticipariEvenimenteReportRowModel;
import ro.cloudSoft.cloudDoc.presentation.server.utils.PresentationExceptionUtils;
import ro.cloudSoft.cloudDoc.services.report.ReportService;

@Component
@Path("/Report")
@Consumes({MediaType.APPLICATION_JSON})
@Produces({MediaType.APPLICATION_JSON})
public class ReportResource extends BaseResource{

	@Autowired
	private ReportService reportService;
	
	@POST
	@Path("/getNumarSedinteCdPvgSiParticipantiReport")
	public NumarSedinteCdPvgSiParticipantiReportModel getNumarSedinteCdPvgSiParticipantiReport(NumarSedinteCdPvgSiParticipantiReportFilterModel filter) throws PresentationException {
		try {
			return reportService.getNumarSedinteCdPvgSiParticipantiReport(filter);
		} catch (AppException ae) {
			throw PresentationExceptionUtils.getPresentationException(ae);
		}
	}
	
	@POST
	@Path("/getPrezentaSedinteCdPvgInvitatiExterniReport")
	public PrezentaSedinteCdPvgInvitatiExterniReportModel getPrezentaSedinteCdPvgInvitatiExterniReport(PrezentaSedintaCdPvgInvitatiExterniFilterModel filter) throws PresentationException {
		try {
			return reportService.getPrezentaSedinteCdPvgInvitatiExterniReport(filter);
		} catch (AppException ae) {
			throw PresentationExceptionUtils.getPresentationException(ae);
		}
	}
	
	@POST
	@Path("/getPrezentaSedinteCdPvgMembriiReport")
	public PrezentaSedintaCdPvgMembriiReportModel getPrezentaSedinteCdPvgMembriiReport(PrezentaSedintaCdPvgMembriiReportFilterModel filter) throws PresentationException {
		try {
			return reportService.getPrezentaSedintaCdPvgMembriiReport(filter);
		} catch (AppException ae) {
			throw PresentationExceptionUtils.getPresentationException(ae);
		}
	}
		
	@POST
	@Path("/getPrezentaSedinteCdPvgInvitatiArbReport")
	public PrezentaSedintaCdPvgInvitatiARBReportModel getPrezentaSedinteCdPvgInvitatiArbReport(PrezentaSedintaCdPvgInvitatiARBReportFilterModel filter) throws PresentationException {
		try {
			return reportService.getPrezentaSedinteCdPvgInvitatiArbReport(filter, getSecurity());
		} catch (AppException ae) {
			throw PresentationExceptionUtils.getPresentationException(ae);
		}
	}
	
	@POST
	@Path("/getParticipariEvenimenteReport")
	public List<ParticipariEvenimenteReportRowModel> getParticipariEvenimenteReport(ParticipariEvenimenteReportFilterModel filter) throws PresentationException {
		try {
			return reportService.getParticipariEvenimenteReport(filter);
		} catch (AppException ae) {
			throw PresentationExceptionUtils.getPresentationException(ae);
		}
	}

	
	@POST
	@Path("/getDocumenteTrimiseDeArbReport")
	public List<DocumenteTrimiseDeArbReportModel> getDocumenteTrimiseDeArbReport(DocumenteTrimiseDeArbReportFilterModel filter) throws PresentationException {
		try {
			return reportService.getDocumenteTrimiseDeArbReport(filter);
		} catch (AppException ae) {
			throw PresentationExceptionUtils.getPresentationException(ae);
		}
	}

	
	@POST
	@Path("/getListaProiectelorCareAuVizatActiunileLuniiReport")
	public List<ListaProiectelorCareAuVizatActiunileLuniiReportModel> getListaProiectelorCareAuVizatActiunileLuniiReport(ListaProiectelorCareAuVizatActiunileLuniiReportFilterModel filter) throws PresentationException {
		try {
			return reportService.getListaProiectelorCareAuVizatActiunileLuniiReport(filter);
		} catch (AppException ae) {
			throw PresentationExceptionUtils.getPresentationException(ae);
		}
	}

	@POST
	@Path("/getActiuniPeProiectReport")
	public ActiuniPeProiectReportModel getActiuniPeProiectReport(ActiuniPeProiectReportFilterModel filter) throws PresentationException {
		try {
			return reportService.getActiuniPeProiectReport(filter);
		} catch (AppException ae) {
			throw PresentationExceptionUtils.getPresentationException(ae);
		}
	}

	@POST
	@Path("/getAderareOioroReport")
	public List<AderareOioroReportRowModel> getAderareOioroReport(AderareOioroReportFilterModel filter) throws PresentationException {
		try {
			return reportService.getAderareOioroReport(filter);
		} catch (AppException ae) {
			throw PresentationExceptionUtils.getPresentationException(ae);
		}
	}

	@POST
	@Path("/getDeplasariDeconturiCheltuieliReprezentantArb")
	public CheltuieliReprezentantArbReportModel getDeplasariDeconturiCheltuieliReprezentantArb(CheltuieliReprezentantArbReportFilterModel filter) throws PresentationException {
		try {
			return reportService.getDeplasariDeconturiCheltuieliReprezentantArb(filter);
		}catch (AppException ae) {
			throw PresentationExceptionUtils.getPresentationException(ae);
		}
	}

	@POST
	@Path("/getAlteDeconturiCheltuieliReprezentantArbReport")
	public DecontCheltuieliAlteDeconturiReportModel getAlteDeconturiCheltuieliReprezentantArbReport(DecontCheltuieliAlteDeconturiReportFilterModel filter) throws PresentationException {
		try {
			return reportService.getAlteDeconturiCheltuieliReprezentantArbReport(filter);
		} catch (AppException ae) {
			throw PresentationExceptionUtils.getPresentationException(ae);
		}
	}


	@POST
	@Path("/getActiuniOrganizateDeArbReport")
	public ActiuniOrganizateDeArbReportModel getActiuniOrganizateDeArbReport(ActiuniOrganizateDeArbReportFilterModel filter) throws PresentationException {
		try {
			return reportService.getActiuniOrganizateDeArbReport(filter, getSecurity());
		} catch (AppException ae) {
			throw PresentationExceptionUtils.getPresentationException(ae);
		}
	}
	
	@POST
	@Path("/getCheltuieliArbSiReprezentantArbReport")
	public CheltuieliArbSiReprezentantArbReportModel getCheltuieliArbSiReprezentantArbReport(CheltuieliArbSiReprezentantArbReportFilterModel filter) throws PresentationException {
		try {
			return reportService.getCheltuieliArbSiReprezentantArbReport(filter);
		} catch (AppException ae) {
			throw PresentationExceptionUtils.getPresentationException(ae);
		}
	}

	@POST
	@Path("/getPrezentaComisiiGlInIntervalReportBundle")
	public PrezentaComisiiGlInIntervalReportBundle getPrezentaComisiiGlInIntervalReportBundle( DocumentIdentifierModel document ) throws PresentationException {
		SecurityManager userSecurity = getSecurity();
		try {
			return reportService.getPrezentaComisiiGlInIntervalReportBundle(document, userSecurity);
		} catch (AppException ae) {
			throw PresentationExceptionUtils.getPresentationException(ae);
		}
	}
	
	@POST
	@Path("/getPrezentaComisiiGlInIntervalReport")
	public PrezentaComisiiGlInIntervalReportModel getPrezentaComisiiGlInIntervalReport(PrezentaComisiiGlInIntervalReportFilterModel filter) throws PresentationException {
		try {
			return reportService.getPrezentaComisiiGlInIntervalReport(filter);
		} catch (AppException ae) {
			throw PresentationExceptionUtils.getPresentationException(ae);
		}
	}
	
	@POST
	@Path("/getDashboardProiecteReport")
	public List<DashboardProiecteRowReportModel> getDashboardProiecteReport(DashboardProiecteFilterModel filter) throws PresentationException {
		try {
			return reportService.getDashboardProiecteReport(filter);
		} catch (AppException ae) {
			throw PresentationExceptionUtils.getPresentationException(ae);
		}
	}
	

	@POST
	@Path("/getParticipareReprezentantiArbLaActiuniInAfaraAsociatieiReport")
	public ParticipareReprezentantiArbLaActiuniInAfaraAsociatieiReportModel getParticipareReprezentantiArbLaActiuniInAfaraAsociatieiReport(ParticipareReprezentantiArbLaActiuniInAfaraAsociatieiReportFilterModel filter) throws PresentationException {
		try {
			return reportService.getParticipareReprezentantiArbLaActiuniInAfaraAsociatieiReport(filter, getSecurity());
		} catch (AppException ae) {
			throw PresentationExceptionUtils.getPresentationException(ae);
		}
	}

	@POST
	@Path("/getDashboardProiecteFilterBundle")
	public DashboardProiecteFilterBundle getDasboardProiecteFiltersSelectItems() {
		return reportService.getDashboardProiecteFilterBundle();
	}
	

	@POST
	@Path("/getPrezentaAgaReport")
	public PrezentaAgaReportModel getPrezentaAgaReport(PrezentaAgaFilterModel filter) throws PresentationException {
		try {
			return reportService.getPrezentaAgaReport(filter);
		} catch (AppException ae) {
			throw PresentationExceptionUtils.getPresentationException(ae);
		}
	}

	@POST
	@Path("/getNumarParticipantiSedinteComisieGlReportBundle")
	public NumarParticipantiSedinteComisieGlReportBundle getNumarParticipantiSedinteComisieGlReportBundle() throws PresentationException {
		try {
			return reportService.getNumarParticipantiSedinteComisieGlReportBundle();
		} catch (AppException ae) {
			throw PresentationExceptionUtils.getPresentationException(ae);
		}
	}
	
	
	@POST
	@Path("/getNumarParticipantiSedinteComisieGlReport")
	public NumarParticipantiSedinteComisieGlReportModel getNumarParticipantiSedinteComisieGlReport(NumarParticipantiSedinteComisieGlReportFilterModel filter) throws PresentationException {
		try {
			return reportService.getNumarParticipantiSedinteComisieGlReport(filter);
		} catch (AppException ae) {
			throw PresentationExceptionUtils.getPresentationException(ae);
		}
	}
	

	@POST
	@Path("/getNumarSedinteComisieGlReport")
	public NumarSedinteComisieGlReportModel getNumarSedinteComisieGlReport(NumarSedinteComisieGlReportFilterModel filter) throws PresentationException {
		try {
			return reportService.getNumarSedinteComisieGlReport(filter);
		} catch (AppException ae) {
			throw PresentationExceptionUtils.getPresentationException(ae);
		}
	}
	
	@POST
	@Path("/getMembriiAfiliatiReport")
	public MembriiAfiliatiReportModel getMembriiAfiliatiReport(MembriiAfiliatiReportFilterModel filter) throws PresentationException {
		try {
			return reportService.getMembriiAfiliatiReport(filter);
		} catch (AppException ae) {
			throw PresentationExceptionUtils.getPresentationException(ae);
		}
	}

	@POST
	@Path("/getTaskuriCumulateReport")
	public TaskuriCumulateReportModel getTaskuriCumulateReport(TaskuriCumulateReportFilterModel filter) throws PresentationException {
		try {
			return reportService.getTaskuriCumulateReport(filter);
		} catch (AppException ae) {
			throw PresentationExceptionUtils.getPresentationException(ae);
		}
	}

	@POST
	@Path("/getDeplasariDeconturiReport")
	public List<DeplasariDeconturiReportRowModel> getDeplasariDeconturiReport(DeplasariDeconturiReportFilterModel filter) throws PresentationException {
		try {
			return reportService.getDeplasariDeconturiReport(filter);
		} catch (AppException ae) {
			throw PresentationExceptionUtils.getPresentationException(ae);
		}
	}

	@POST
	@Path("/getDeplasariDeconturiReportBundle")
	public DeplasariDeconturiReportBundleModel getDeplasariDeconturiReportBundle() throws PresentationException {
		return reportService.getDeplasariDeconturiReportBundle();
	}
	
	@POST
	@Path("/getCereriConcediuReportBundle")
	public CereriConcediuReportBundle getCereriConcediuReportBundle() throws PresentationException {
		return reportService.getCereriConcediuReportBundle();
	}
	
	@POST
	@Path("/getCereriConcediuReport")
	public CereriConcediuReportModel getCereriConcediuReport(CereriConcediuReportFilterModel filter) throws PresentationException {
		try {
			return reportService.getCereriConcediuReport(filter);
		} catch (AppException ae) {
			throw PresentationExceptionUtils.getPresentationException(ae);
		}
	}
	
	@POST
	@Path("/getReprezentantiBancaPerFunctieSiComisieReport")
	public ReprezentantiBancaPerFunctieSiComisieReportModel getReprezentantiBancaPerFunctieSiComisieReport(ReprezentantiBancaPerFunctieSiComisieReportFilterModel filter) throws PresentationException {
		try {
			return reportService.getReprezentantiBancaPerFunctieSiComisieReport(filter);
		} catch (AppException ae) {
			throw PresentationExceptionUtils.getPresentationException(ae);
		}
	}

	@POST
	@Path("/getPrezentaComisiiArbReprezentativitate")
	public PrezentaReprezentivitateReportModel getPrezentaComisiiArbReprezentativitate(PrezentaReprezentivitateReportFilterModel filter) throws PresentationException {
		try {
			return reportService.getPrezentaComisiiArbReprezentativitate(filter);
		} catch (AppException ae) {
			throw PresentationExceptionUtils.getPresentationException(ae);
		}
	}
	
	@POST
	@Path("/getRaspunsuriBanciReport")
	public List<RaspunsuriBanciReportRowModel> getRaspunsuriBanciReport(RaspunsuriBanciReportFilterModel filter) throws PresentationException {
		return reportService.getRaspunsuriBanciReport(filter);
	}
	
	@POST
	@Path("/getRaspunsuriBanciReportBundle")
	public RaspunsuriBanciReportBundleModel getRaspunsuriBanciReportBundle() throws PresentationException {
		return reportService.getRaspunsuriBanciReportBundle();
	}
	
	@POST
	@Path("/getCentralizatorPrezentaPerioadaReport")
	public CentralizatorPrezentaPerioadaReportModel getCentralizatorPrezentaPerioadaReport(CentralizatorPrezentaPerioadaReportFilterModel filter) throws PresentationException {
		try {
			return reportService.getCentralizatorPrezentaPerioadaReport(filter);
		} catch (AppException ae) {
			throw PresentationExceptionUtils.getPresentationException(ae);
		}
	}
	
	@POST
	@Path("/getNotaGeneralaPeMembriiArbReport")
	public NotaGeneralaPeMembriiArbReportModel getNotaGeneralaPeMembriiArbReport(NotaGeneralaPeMembriiArbReportFilterModel filter) throws PresentationException {
		try {
			return reportService.getNotaGeneralaPeMembriiArbReport(filter);
		} catch (AppException ae) {
			throw PresentationExceptionUtils.getPresentationException(ae);
		}
	}
	
	@POST
	@Path("/getNivelReprezentareComisiiReport")
	public NivelReprezentareComisiiReportModel getNivelReprezentareComisiiReport(NivelReprezentareComisiiReportFilterModel filter) throws PresentationException {
		try {
			return reportService.getNivelReprezentareComisiiReport(filter);
		} catch (AppException ae) {
			throw PresentationExceptionUtils.getPresentationException(ae);
		}
	}

	@POST
	@Path("/getNotaGeneralaReport")
	public List<NotaGeneralaReportRowModel> getNotaGeneralaReport(NotaGeneralaReportFilterModel filter) throws PresentationException {
		try {
			return reportService.getNotaGeneralaReport(filter);
		} catch (AppException ae) {
			throw PresentationExceptionUtils.getPresentationException(ae);
		}
	}

	@POST
	@Path("/getNoteBanciReport")
	public NoteBanciReportModel getNoteBanciReport(NoteBanciReportFilterModel filter) throws PresentationException {
		try {
			return reportService.getNoteBanciReport(filter);
		} catch (AppException ae) {
			throw PresentationExceptionUtils.getPresentationException(ae);
		}
	}

}
