package ro.cloudSoft.cloudDoc.services.cursValutar;

import ro.cloudSoft.cloudDoc.dao.cursValutar.CursValutarDao;
import ro.cloudSoft.cloudDoc.domain.cursValutar.CursValutar;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.cursValutar.CursValutarModel;
import ro.cloudSoft.cloudDoc.presentation.server.converters.cursValutar.CursValutarConverter;
import ro.cloudSoft.common.utils.DateUtils;

public class CursValutarServiceImpl implements CursValutarService {
	
	private CursValutarDao cursValutarDao;
	private CursValutarBnrService cursValutarBnrService;
	
	private CursValutarConverter cursValutarConverter;
	
	@Override
	public void loadCursValutar() throws Exception {

		try {
			CursValutar cursValutar = cursValutarBnrService.getCursValutar();
			
			CursValutar ultimulCursValutar = cursValutarDao.getCursValutarCurent();
			
			if (ultimulCursValutar != null && DateUtils.isSameDate(cursValutar.getData(), ultimulCursValutar.getData())) {
				return;
			}
			
			cursValutarDao.save(cursValutar);
			
		} catch (Exception e) {
			throw new Exception(e.getMessage(), e);
		}
	}
	
	@Override
	public CursValutarModel getCursValutarCurent() {
		CursValutar cursValutar =  cursValutarDao.getCursValutarCurent();
		return cursValutarConverter.toModel(cursValutar);
	}
	
	public void setCursValutarDao(CursValutarDao cursValutarDao) {
		this.cursValutarDao = cursValutarDao;
	}

	public void setCursValutarBnrService(CursValutarBnrService cursValutarBnrService) {
		this.cursValutarBnrService = cursValutarBnrService;
	}

	public void setCursValutarConverter(CursValutarConverter cursValutarConverter) {
		this.cursValutarConverter = cursValutarConverter;
	}
}
