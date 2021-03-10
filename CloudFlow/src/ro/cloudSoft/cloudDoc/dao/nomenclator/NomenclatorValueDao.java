package ro.cloudSoft.cloudDoc.dao.nomenclator;

import java.util.List;

import ro.cloudSoft.cloudDoc.domain.nomenclator.NomenclatorValue;
import ro.cloudSoft.cloudDoc.domain.nomenclator.NomenclatorValueSearchCriteria;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.nomenclator.GetNomenclatorValuesRequestModel;
import ro.cloudSoft.common.utils.PagingList;

public interface NomenclatorValueDao {
	
	public Long save(NomenclatorValue nomenclatorValue);
	
	public NomenclatorValue find(Long id);
	
	public List<NomenclatorValue> getAll(Long nomenclatorId);
	
	public List<NomenclatorValue> getAllByNomenclatorCode(String nomenclatorCode);
	
	public PagingList<NomenclatorValue> searchValues(final int offset, final int pageSize, NomenclatorValueSearchCriteria searchCriteria);

	public List<NomenclatorValue> getAllByIds(List<Long> ids);

	public List<NomenclatorValue> findByNomenclatorId(Long nomenclatorId);

	public NomenclatorValue getRegistruFacturiCuUltimulNumarDeInregistrareByTipDocumentAndCurrentYear(String tipDocumentId, String year);

	NomenclatorValue getRegistruIntrariIesiriCuUltimulNumarDeInregistrareByTipRegistruAndCurrentYear(String tipRegistru, String year);

	public NomenclatorValue getNomenclatorValueCuUltimulNumarInregistrareByNomenclatorIdAndCurrentYear(Long nomenclatorId, String year);
	
	public NomenclatorValue getDetaliiNumarDeplasariBugetateNomenclatorValueByOrganismIdAndCurrentYear(Long nomenclatorId, String organismId, String year);

	public List<NomenclatorValue> findByNomenclatorIdAndAttribute(Long nomenclatorId, String attributeKey, String attributeValue);
	
	public List<NomenclatorValue> findByNomenclatorCodeAndAttribute(String nomenclatorCode, String attributekey, String attributeValue);
	public List<NomenclatorValue> findByNomenclatorCodeAndAttributes(String nomenclatorCode, String attributekey1, String attributeValue1, String attributekey2, String attributeValue2);
	
	public List<NomenclatorValue> findByNomenclatorCode(String nomenclatorCode);
	
	public List<NomenclatorValue> getNomenclatorValues(GetNomenclatorValuesRequestModel requestModel);
	
	boolean nomenclatorHasValuesByNomenclatorId(Long nomenclatorId);
	
	List<NomenclatorValue> getDistinctNomenclatorValuesByNomenclatorAndOtherNomenclatorAndAttributeThatUseIt(Long nomenclatorId, Long nomenclatorIdThatUseIt, String nomenclatorAttributeKeyThatUseIt);
	
	/**
	 * Obs: Institutia "ARB" (care este de tip membru ARB) nu va intra la numaratoare.
	 * @return
	 */
	Long countInstitutiiMembreARBNeradiateWithoutARB();

}
