package ro.cloudSoft.cloudDoc.services.cursValutar;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.GeneralSecurityException;
import java.util.Date;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.apache.commons.collections4.CollectionUtils;

import ro.cloudSoft.cloudDoc.core.constants.CursValutarConstants;
import ro.cloudSoft.cloudDoc.domain.cursValutar.CursValutar;
import ro.cloudSoft.cloudDoc.services.cursValutar.LTCube.Rate;

public class CursValutarBnrService {
	
	private CursValutarConstants cursValutarConstants;
	
	public CursValutar getCursValutar() throws JAXBException, MalformedURLException, IOException {
		
		CursValutar cursValutar = new CursValutar();
		
		JAXBContext jaxbContext = JAXBContext.newInstance(DataSet.class);
		Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
		String urlPath = cursValutarConstants.getUrl();
		URL url = new URL(urlPath);
		
		TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {
			public java.security.cert.X509Certificate[] getAcceptedIssuers() {
				return new java.security.cert.X509Certificate[0];
			}

			public void checkClientTrusted(java.security.cert.X509Certificate[] certs, String authType) {
			}

			public void checkServerTrusted(java.security.cert.X509Certificate[] certs, String authType) {
			}
		} };

		// Install the all-trusting trust manager
		try {
			SSLContext sc = SSLContext.getInstance("SSL");
			sc.init(null, trustAllCerts, new java.security.SecureRandom());
			HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
		} catch (GeneralSecurityException e) {
		}
		// Now you can access an https URL without having the certificate in the
		// truststore
		try {
			url = new URL(urlPath);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}

		HttpsURLConnection con = (HttpsURLConnection) url.openConnection();

		DataSet dataSet = (DataSet) unmarshaller.unmarshal(con.getInputStream());

		Date cursValutarDate = dataSet.getHeader().getPublishingDate().toGregorianCalendar().getTime();
		cursValutar.setData(cursValutarDate);

		List<LTCube> cubes = dataSet.getBody().getCube();
		if (CollectionUtils.isNotEmpty(cubes)) {
			LTCube cube = cubes.get(0);
			List<Rate> rates = cube.getRate();
			for (Rate rate : rates) {
				if ("EUR".equals(rate.getCurrency().toUpperCase())) {
					cursValutar.setEur(rate.getValue());
				} else if ("USD".equals(rate.getCurrency().toUpperCase())) {
					cursValutar.setUsd(rate.getValue());
				}
			}
		}
		return cursValutar;
	}

	public void setCursValutarConstants(CursValutarConstants cursValutarConstants) {
		this.cursValutarConstants = cursValutarConstants;
	}
}
