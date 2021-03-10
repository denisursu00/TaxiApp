package ro.cloudSoft.cloudDoc.utils;

import java.math.BigDecimal;

import org.apache.commons.lang.StringUtils;

import ro.cloudSoft.cloudDoc.domain.deplasariDeconturi.CheltuialaReprezentantArb.ValutaForCheltuieliReprezentantArbEnum;

public class CheltuieliUtils {

	public static BigDecimal calculateRonValue(BigDecimal valoareInValuta, String codValuta, BigDecimal cursValutar) {
		BigDecimal valoareRon = null;
		if (StringUtils.isNotBlank(codValuta)) {
			if (codValuta.equals(ValutaForCheltuieliReprezentantArbEnum.RON.toString())) {
				valoareRon = valoareInValuta;
			} else {
				valoareRon = valoareInValuta.multiply(cursValutar);
			}
		}

		return valoareRon;
	}
}
