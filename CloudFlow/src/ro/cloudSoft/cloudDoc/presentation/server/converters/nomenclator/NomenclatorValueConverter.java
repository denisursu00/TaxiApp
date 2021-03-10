package ro.cloudSoft.cloudDoc.presentation.server.converters.nomenclator;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;

import ro.cloudSoft.cloudDoc.dao.nomenclator.NomenclatorDao;
import ro.cloudSoft.cloudDoc.dao.nomenclator.NomenclatorValueDao;
import ro.cloudSoft.cloudDoc.domain.nomenclator.Nomenclator;
import ro.cloudSoft.cloudDoc.domain.nomenclator.NomenclatorValue;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.nomenclator.NomenclatorValueModel;

public class NomenclatorValueConverter {
	
	private NomenclatorDao nomenclatorDao;
	private NomenclatorValueDao nomenclatorValueDao;
	
	public NomenclatorValueModel getModel(NomenclatorValue entity) {
		NomenclatorValueModel model = new NomenclatorValueModel();
		model.setId(entity.getId());
		model.setNomenclatorId(entity.getNomenclator().getId());		
		model.setAttribute1(entity.getAttribute1());
		model.setAttribute2(entity.getAttribute2());
		model.setAttribute3(entity.getAttribute3());
		model.setAttribute4(entity.getAttribute4());
		model.setAttribute5(entity.getAttribute5());
		model.setAttribute6(entity.getAttribute6());
		model.setAttribute7(entity.getAttribute7());
		model.setAttribute8(entity.getAttribute8());
		model.setAttribute9(entity.getAttribute9());
		model.setAttribute10(entity.getAttribute10());
		model.setAttribute11(entity.getAttribute11());
		model.setAttribute12(entity.getAttribute12());
		model.setAttribute13(entity.getAttribute13());
		model.setAttribute14(entity.getAttribute14());
		model.setAttribute15(entity.getAttribute15());
		model.setAttribute16(entity.getAttribute16());
		model.setAttribute17(entity.getAttribute17());
		model.setAttribute18(entity.getAttribute18());
		model.setAttribute19(entity.getAttribute19());
		model.setAttribute20(entity.getAttribute20());
		model.setAttribute21(entity.getAttribute21());
		model.setAttribute22(entity.getAttribute22());
		model.setAttribute23(entity.getAttribute23());
		model.setAttribute24(entity.getAttribute24());
		model.setAttribute25(entity.getAttribute25());
		model.setAttribute26(entity.getAttribute26());
		model.setAttribute27(entity.getAttribute27());
		model.setAttribute28(entity.getAttribute28());
		model.setAttribute29(entity.getAttribute29());
		model.setAttribute30(entity.getAttribute30());
		model.setAttribute31(entity.getAttribute31());
		model.setAttribute32(entity.getAttribute32());
		model.setAttribute33(entity.getAttribute33());
		model.setAttribute34(entity.getAttribute34());
		model.setAttribute35(entity.getAttribute35());
		model.setAttribute36(entity.getAttribute36());
		model.setAttribute37(entity.getAttribute37());
		model.setAttribute38(entity.getAttribute38());
		model.setAttribute39(entity.getAttribute39());
		model.setAttribute40(entity.getAttribute40());
		model.setAttribute41(entity.getAttribute41());
		model.setAttribute42(entity.getAttribute42());
		model.setAttribute43(entity.getAttribute43());
		model.setAttribute44(entity.getAttribute44());
		model.setAttribute45(entity.getAttribute45());
		model.setAttribute46(entity.getAttribute46());
		model.setAttribute47(entity.getAttribute47());
		model.setAttribute48(entity.getAttribute48());
		model.setAttribute49(entity.getAttribute49());
		model.setAttribute50(entity.getAttribute50());
		return model;
	}
	
	public NomenclatorValue getEntity(NomenclatorValueModel model) {
		
		NomenclatorValue entity = new NomenclatorValue();
		if (model.getId() != null) {
			entity = nomenclatorValueDao.find(model.getId());
		}
		
		Nomenclator nomenclator = nomenclatorDao.find(model.getNomenclatorId());
		entity.setNomenclator(nomenclator);
		
		entity.setAttribute1(model.getAttribute1());
		entity.setAttribute2(model.getAttribute2());
		entity.setAttribute3(model.getAttribute3());
		entity.setAttribute4(model.getAttribute4());
		entity.setAttribute5(model.getAttribute5());
		entity.setAttribute6(model.getAttribute6());
		entity.setAttribute7(model.getAttribute7());
		entity.setAttribute8(model.getAttribute8());
		entity.setAttribute9(model.getAttribute9());
		entity.setAttribute10(model.getAttribute10());
		entity.setAttribute11(model.getAttribute11());
		entity.setAttribute12(model.getAttribute12());
		entity.setAttribute13(model.getAttribute13());
		entity.setAttribute14(model.getAttribute14());
		entity.setAttribute15(model.getAttribute15());
		entity.setAttribute16(model.getAttribute16());
		entity.setAttribute17(model.getAttribute17());
		entity.setAttribute18(model.getAttribute18());
		entity.setAttribute19(model.getAttribute19());
		entity.setAttribute20(model.getAttribute20());
		entity.setAttribute21(model.getAttribute21());
		entity.setAttribute22(model.getAttribute22());
		entity.setAttribute23(model.getAttribute23());
		entity.setAttribute24(model.getAttribute24());
		entity.setAttribute25(model.getAttribute25());
		entity.setAttribute26(model.getAttribute26());
		entity.setAttribute27(model.getAttribute27());
		entity.setAttribute28(model.getAttribute28());
		entity.setAttribute29(model.getAttribute29());
		entity.setAttribute30(model.getAttribute30());
		entity.setAttribute31(model.getAttribute31());
		entity.setAttribute32(model.getAttribute32());
		entity.setAttribute33(model.getAttribute33());
		entity.setAttribute34(model.getAttribute34());
		entity.setAttribute35(model.getAttribute35());
		entity.setAttribute36(model.getAttribute36());
		entity.setAttribute37(model.getAttribute37());
		entity.setAttribute38(model.getAttribute38());
		entity.setAttribute39(model.getAttribute39());
		entity.setAttribute40(model.getAttribute40());
		entity.setAttribute41(model.getAttribute41());
		entity.setAttribute42(model.getAttribute42());
		entity.setAttribute43(model.getAttribute43());
		entity.setAttribute44(model.getAttribute44());
		entity.setAttribute45(model.getAttribute45());
		entity.setAttribute46(model.getAttribute46());
		entity.setAttribute47(model.getAttribute47());
		entity.setAttribute48(model.getAttribute48());
		entity.setAttribute49(model.getAttribute49());
		entity.setAttribute50(model.getAttribute50());
		return entity;
	}
	
	public List<NomenclatorValueModel> getModels(List<NomenclatorValue> entities) {
		
		List<NomenclatorValueModel> models = new ArrayList<NomenclatorValueModel>();
		if (CollectionUtils.isNotEmpty(entities)) {
			for (NomenclatorValue entity : entities) {
				models.add(getModel(entity));
			}
		}
		return models;
	}

	public void setNomenclatorDao(NomenclatorDao nomenclatorDao) {
		this.nomenclatorDao = nomenclatorDao;
	}
	
	public void setNomenclatorValueDao(NomenclatorValueDao nomenclatorValueDao) {
		this.nomenclatorValueDao = nomenclatorValueDao;
	}
}