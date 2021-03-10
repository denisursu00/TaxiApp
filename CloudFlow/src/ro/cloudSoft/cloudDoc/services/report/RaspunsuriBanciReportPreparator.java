package ro.cloudSoft.cloudDoc.services.report;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import ro.cloudSoft.cloudDoc.core.constants.NomenclatorConstants;
import ro.cloudSoft.cloudDoc.core.constants.ParameterConstants;
import ro.cloudSoft.cloudDoc.dao.parameters.ParametersDao;
import ro.cloudSoft.cloudDoc.domain.registruIntrariIesiri.RegistruIesiri;
import ro.cloudSoft.cloudDoc.domain.registruIntrariIesiri.RegistruIesiriDestinatar;
import ro.cloudSoft.cloudDoc.domain.registruIntrariIesiri.RegistruIntrari;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.registruIntrariIesiri.RegistruIntrariModel.RaspunsuriBanciCuPropuneriEnum;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.report.RaspunsuriBanciReportFilterModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.report.RaspunsuriBanciReportRowModel;
import ro.cloudSoft.cloudDoc.utils.nomenclator.NomenclatorValueUtils;
import ro.cloudSoft.common.utils.DateUtils;

public class RaspunsuriBanciReportPreparator {

	private static final int COEFICIENT_CONVERSIE_DIN_PONDERE_IN_NOTA = 10;

	private double pondereNotaRaspunsuriAdjustataCuCalitateaRasp;
	private double pondereNotaRaspunsuriAdjustataCuVitezaRasp;
	private double pondereGradNumarRaspunsuri;
	private double pondereNotaNumarRaspunsuri;
	private int punctajMaxim;
	private double pondereRaspunsuriCuPropuneri;
	private double pondereRaspunsuriFaraPropuneri;
	private double pondereRaspunsuriCuIntarzierePeste1Zi;
	private double pondereRaspunsuriFaraIntarziere;

	private ParametersDao parametersDao;
	private Map<String, Long> nrInterogariByBanca;
	private List<RegistruIesiri> regIesiri;
	private RaspunsuriBanciReportFilterModel filter;

	public RaspunsuriBanciReportPreparator(ParametersDao parametersDao, Map<String, Long> nrInterogariByBanca, List<RegistruIesiri> regIesiri, RaspunsuriBanciReportFilterModel filter) {
		this.parametersDao = parametersDao;
		this.nrInterogariByBanca = nrInterogariByBanca;
		this.regIesiri = regIesiri;
		this.filter = filter;
		loadParametersValues();
	}
	
	
	public List<RaspunsuriBanciReportRowModel> prepareReport() {
		Map<String, RaspunsuriBanciReportRowModel> reportRowByDenumireBanca = new HashMap<String, RaspunsuriBanciReportRowModel>();
		
		for (RegistruIesiri regIesire : regIesiri) {
			for (RegistruIesiriDestinatar destinatar : regIesire.getDestinatari()) {
				String numeBanca = null;
				if (destinatar.getInstitutie() != null) {
					numeBanca = NomenclatorValueUtils.getAttributeValueAsString(destinatar.getInstitutie(), NomenclatorConstants.INSTITUTII_ATTRIBUTE_KEY_DENUMIRE);
				} else if (StringUtils.isNotEmpty(destinatar.getNume())) {
					numeBanca = destinatar.getNume();
				}
				if (numeBanca == null || (StringUtils.isNotEmpty(filter.getDenumireBanca()) && !numeBanca.equals(filter.getDenumireBanca()))) {
					continue;
				}
				if (!reportRowByDenumireBanca.containsKey(numeBanca)) {
					reportRowByDenumireBanca.put(numeBanca, new RaspunsuriBanciReportRowModel());
				}
				RaspunsuriBanciReportRowModel row = reportRowByDenumireBanca.get(numeBanca);
				if (destinatar.getRegistruIntrari() != null) {
					RegistruIntrari registruIntrari = destinatar.getRegistruIntrari();
					RaspunsuriBanciCuPropuneriEnum raspunsuriBanciCuPropuneri = registruIntrari.getRaspunsuriBanciCuPropuneri();
					if (raspunsuriBanciCuPropuneri.equals(RaspunsuriBanciCuPropuneriEnum.DA)) {
						row.setNrRaspunsuriCuPropuneri(row.getNrRaspunsuriCuPropuneri() + 1);
					} else {
						row.setNrRaspunsuriFaraPropuneri(row.getNrRaspunsuriFaraPropuneri() + 1);
					}
					if (regIesire.getTermenRaspuns() != null) {
						long numberDaysBetween = DateUtils.numberDaysBetween(registruIntrari.getDataInregistrare(), regIesire.getTermenRaspuns());
						if (numberDaysBetween < -1) {
							row.setNrRaspunsuriFaraIntarziere(row.getNrRaspunsuriFaraIntarziere() + 1);
						} else {
							row.setNrRaspunsuriCuIntarzierePesteOZi(row.getNrRaspunsuriCuIntarzierePesteOZi() + 1);
						}
					}
				}
			}
		}
		
		List<RaspunsuriBanciReportRowModel> rows = new ArrayList<>();
		reportRowByDenumireBanca.forEach((denumireBanca, row) -> {
			row.setDenumireBanca(denumireBanca);
			prepareRow(row);
			rows.add(row);
		});
		
		return rows;
	}

	public void prepareRow(RaspunsuriBanciReportRowModel row) {

		row.setNrTotalRaspunsuriPropuneri(row.getNrRaspunsuriCuPropuneri() + row.getNrRaspunsuriFaraPropuneri());
		calculateCoeficientStructuralCalitateRaspunsuri(row);
		calculateNotaRaspunsuriAdjustataCuCalitateaRaspunsuri(row);

		row.setNrTotalRaspunsuriTermen(row.getNrRaspunsuriCuIntarzierePesteOZi() + row.getNrRaspunsuriFaraIntarziere());
		calculateCoeficientStructuralVitezaRaspunsuri(row);
		calculateNotaRaspunsuriAdjustataCuVitezaRaspunsuri(row);

		calculateNotaTotala(row);
	}

	private void loadParametersValues() {
		punctajMaxim = Integer.parseInt(parametersDao.getByName(ParameterConstants.PARAM_NAME_RAPORT_COEFICIENT_PUNCTAJ_MAXIM).getValue());
		pondereGradNumarRaspunsuri = Double.parseDouble(parametersDao.getByName(ParameterConstants.PARAM_NAME_RAPORT_COEFICIENT_PROCENTUAL_GRAD_NR_RASPUNSURI).getValue()) / 100;
		pondereRaspunsuriFaraPropuneri = Double.parseDouble(parametersDao.getByName(ParameterConstants.PARAM_NAME_RAPORT_COEFICIENT_PROCENTUAL_RASPUNSURI_FARA_PROPUNERI_ARGUMENTARI).getValue()) / 100;
		pondereRaspunsuriCuPropuneri = Double.parseDouble(parametersDao.getByName(ParameterConstants.PARAM_NAME_RAPORT_COEFICIENT_PROCENTUAL_RASPUNSURI_CU_PROPUNERI_ARGUMENTARI).getValue()) / 100;
		pondereRaspunsuriCuIntarzierePeste1Zi = Double.parseDouble(parametersDao.getByName(ParameterConstants.PARAM_NAME_RAPORT_COEFICIENT_PROCENTUAL_RASPUNSURI_CU_INTARZIERE_PESTE_O_ZI).getValue()) / 100;
		pondereRaspunsuriFaraIntarziere = Double.parseDouble(parametersDao.getByName(ParameterConstants.PARAM_NAME_RAPORT_COEFICIENT_PROCENTUAL_RASPUNSURI_FARA_INTARZIERE).getValue()) / 100;
		pondereNotaNumarRaspunsuri = Double.parseDouble(parametersDao.getByName(ParameterConstants.PARAM_NAME_RAPORT_COEFICIENT_PROCENTUAL_NOTA_NUMAR_RASPUNSURI).getValue()) / 100;
		pondereNotaRaspunsuriAdjustataCuVitezaRasp = Double.parseDouble(parametersDao.getByName(ParameterConstants.PARAM_NAME_RAPORT_COEFICIENT_PROCENTUAL_NOTA_RASPUNSURI_AJUSTATA_CU_VITEZA_RASPUNS).getValue()) / 100;
		pondereNotaRaspunsuriAdjustataCuCalitateaRasp = Double.parseDouble(parametersDao.getByName(ParameterConstants.PARAM_NAME_RAPORT_COEFICIENT_PROCENTUAL_NOTA_RASPUNSURI_AJUSTATA_CALITATE_RASPUNS).getValue()) / 100;

	}

	private void calculateNotaTotala(RaspunsuriBanciReportRowModel row) {

		long numarTotalInterogarArb = nrInterogariByBanca.get(row.getDenumireBanca());
		double numarTotalInterogariArbPonderat = pondereGradNumarRaspunsuri * numarTotalInterogarArb;

		double numarTotalRaspunsuri = row.getNrTotalRaspunsuriPropuneri();

		double notaNumarRaspunsuri = (numarTotalRaspunsuri / numarTotalInterogariArbPonderat * COEFICIENT_CONVERSIE_DIN_PONDERE_IN_NOTA);
		if (notaNumarRaspunsuri > punctajMaxim) {
			notaNumarRaspunsuri = punctajMaxim;
		}

		double notaTotala = pondereNotaRaspunsuriAdjustataCuCalitateaRasp * row.getNotaRaspunsuriAjustataCuCalitateaRaspunsurilor()
				+ pondereNotaRaspunsuriAdjustataCuVitezaRasp * row.getNotaRaspunsuriAjustataCuVitezaRaspunsurilor() + pondereNotaNumarRaspunsuri * notaNumarRaspunsuri;

		row.setNotaTotalaRaspunsuriBanci(notaTotala);

	}

	private void calculateNotaRaspunsuriAdjustataCuVitezaRaspunsuri(RaspunsuriBanciReportRowModel row) {
		double notaRaspunsuri = row.getCoeficientStructuralVitezaRaspunsuri() * COEFICIENT_CONVERSIE_DIN_PONDERE_IN_NOTA;
		if (notaRaspunsuri > punctajMaxim) {
			notaRaspunsuri = punctajMaxim;
		}
		row.setNotaRaspunsuriAjustataCuVitezaRaspunsurilor(notaRaspunsuri);

	}

	private void calculateCoeficientStructuralVitezaRaspunsuri(RaspunsuriBanciReportRowModel row) {
		double coeficient = 0.0;
		if (row.getNrTotalRaspunsuriPropuneri() != 0) {
			coeficient = (pondereRaspunsuriFaraIntarziere * row.getNrRaspunsuriFaraIntarziere())
					+ (pondereRaspunsuriCuIntarzierePeste1Zi * row.getNrRaspunsuriCuIntarzierePesteOZi());
		}
		row.setCoeficientStructuralVitezaRaspunsuri(coeficient);

	}

	private void calculateCoeficientStructuralCalitateRaspunsuri(RaspunsuriBanciReportRowModel row) {
		double coeficientStructuralCalitateRaspunsuri = 0.0;
		if (row.getNrTotalRaspunsuriPropuneri() != 0) {
			coeficientStructuralCalitateRaspunsuri = (pondereRaspunsuriCuPropuneri * row.getNrRaspunsuriCuPropuneri())
					+ (pondereRaspunsuriFaraPropuneri * row.getNrRaspunsuriFaraPropuneri());
		}
		row.setCoeficientStructuralCalitateRaspunsuri(coeficientStructuralCalitateRaspunsuri);
	}

	private void calculateNotaRaspunsuriAdjustataCuCalitateaRaspunsuri(RaspunsuriBanciReportRowModel row) {
		double notaRaspunsuri = row.getCoeficientStructuralCalitateRaspunsuri() * COEFICIENT_CONVERSIE_DIN_PONDERE_IN_NOTA;
		if (notaRaspunsuri > punctajMaxim) {
			notaRaspunsuri = punctajMaxim;
		}
		row.setNotaRaspunsuriAjustataCuCalitateaRaspunsurilor(notaRaspunsuri);
	}

}
