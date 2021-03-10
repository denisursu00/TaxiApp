package ro.cloudSoft.cloudDoc.dao.cursValutar;

import ro.cloudSoft.cloudDoc.domain.cursValutar.CursValutar;

public interface CursValutarDao {
	
	Long save(CursValutar cursValutar);
	
	CursValutar getCursValutarCurent();
}
