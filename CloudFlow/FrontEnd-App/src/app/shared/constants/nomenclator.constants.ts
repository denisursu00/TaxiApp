export class NomenclatorConstants {

	
	public static BOOLEAN_ATTRIBUTE_VALUE_AS_STRING: string = "true";

	// NOMENCLATOR_CODE_XXX => XXX: nume nomenclator
	public static readonly NOMENCLATOR_CODE_COMISII_SAU_GL = "comisii_sau_gl";
	public static readonly NOMENCLATOR_CODE_PERSOANE = "persoane";
	public static readonly NOMENCLATOR_CODE_INSTITUTII = "institutii";
	public static readonly NOMENCLATOR_CODE_INSTITUTII_CD = "institutii_cd";
	public static readonly NOMENCLATOR_CODE_MEMBRI_CD = "membrii_institutii_cd";
	public static readonly NOMENCLATOR_CODE_MONEDE = "monede";
	public static readonly NOMENCLATOR_CODE_REGISTRU_INTRARI_TIP_DOCUMENT = "registru_intrari_tip_document";
	public static readonly NOMENCLATOR_CODE_TIP_INSTITUTII = "tip_institutii";
	public static readonly NOMENCLATOR_CODE_REGISTRU_IESIRI_TIP_DOCUMENT = "registru_iesiri_tip_document";
	public static readonly NOMENCLATOR_CODE_REPREZENTANT_ARB_ORGANISME_INTERNE_SI_INTERNATIONALE = "reprezentanti_arb_organisme_interne_si_internationale";
	public static readonly NOMENCLATOR_CODE_TIP_ORGANISM = "tip_organism";
	public static readonly NOMENCLATOR_CODE_ORGANISME = "organisme";
	public static readonly NOMENCLATOR_CODE_COMITETE = "comitete";
	public static readonly NOMENCLATOR_CODE_DOMENII_BANCARE = "domenii_bancare";
	public static readonly NOMENCLATOR_CODE_IMPORTANTA_PROIECTE = "importanta_proiecte";
	public static readonly NOMENCLATOR_CODE_INCADRARI_PROIECTE = "incadrari_proiecte";
	public static readonly NOMENCLATOR_CODE_DETALII_NUMAR_DEPLASARI_BUGETATE_ORGANISME = "detalii_numar_deplasari_bugetate_organisme";
	public static readonly NOMENCLATOR_CODE_LEVEL_FUNCTII_PERSOANE = "level_functii_persoane";
	public static readonly NOMENCLATOR_CODE_ZILE_LIBERE_LEGALE = "zile_libere_legale";
	public static readonly NOMENCLATOR_CODE_FUNCTII_PERSOANE = "functii_persoane";

	// PERSOANE_ATTR_KEY_XXX => XXX: nume atribut
	public static readonly PERSOANE_ATTR_KEY_INSTITUTIE = "attribute9";
	public static readonly PERSOANE_ATTR_KEY_NUME = "attribute1";
	public static readonly PERSOANE_ATTR_KEY_PRENUME = "attribute2";
	public static readonly PERSOANE_ATTR_KEY_DEPARTAMENT = "attribute7";
	public static readonly PERSOANE_ATTR_KEY_FUNCTIE = "attribute8";
	public static readonly PERSOANE_ATTR_KEY_EMAIL = "attribute3";
	public static readonly PERSOANE_ATTR_KEY_TELEFON = "attribute4";

	// TIP_INSTITUTII_ATTR_KEY_XXX => XXX: nume atribut
	public static readonly TIP_INSTITUTII_ATTR_KEY_TIP = "attribute1";
	public static readonly TIP_INSTITUTII_ATTR_KEY_ABREVIERE = "attribute3";
	public static readonly TIP_INSTITUTII_TIP_MEMBRU_ARB = "Membru ARB (B)";
	public static readonly TIP_INSTITUTII_TIP_MEMBRU_AFILIAT = "Membru Afiliat (A)";
	public static readonly TIP_INSTITUTII_TIP_ALTE_INSTITUTII = "Alte Institutii (I)";

	// INSTITUTII_ATTR_KEY_XXX => XXX: nume atribut
	public static readonly INSITUTII_ATTR_KEY_TIP_INSTITUTIE = "attribute1";
	public static readonly INSITUTII_ATTR_KEY_DENUMIRE_INSTITUTIE = "attribute2";
	public static readonly INSTITUTII_ATTR_KEY_ABREVIERE = "attribute3";
	public static readonly INSTITUTII_ATTRIBUTE_KEY_TIP_RADIAT = "attribute13";
	
	// INSTITUTII_CD_ATTR_KEY_XXX => XXX: nume atribut
	public static readonly INSTITUTII_CD_ATTR_KEY_DENUMIRE_INSTITUTIE = "attribute1";

	public static readonly REGISTRU_IESIRI_TIP_DOCUMENT_ATTR_KEY_CODE = "attribute1";	
	public static readonly REGISTRU_IESIRI_TIP_DOCUMENT_ATTR_KEY_DENUMIRE = "attribute2";	
	public static readonly REGISTRU_IESIRI_TIP_DOCUMENT_ATTR_KEY_CODE_ECHIVALENT_CERERE_INTRARE = "attribute3";
	public static readonly REGISTRU_IESIRI_TIP_DOCUMENT_ATTR_KEY_ASTEAPTA_RASPUNS = "attribute4";

	public static readonly COMISII_SAU_GL_ATTR_KEY_DENUMIRE = "attribute2";

	public static readonly NOMENCLATOR_CODE_REPREZENTANT_ARB_ORGANISME_INTERNE_SI_INTERNATIONALE_ATTR_KEY_REPREZENTANT = "attribute5";

	public static readonly TIP_ORGANISM_ATTR_KEY_TIP = "attribute2";
	public static readonly TIP_ORGANISM_TIP_ORGANISM_INTERN = "OI";
	public static readonly TIP_ORGANISM_TIP_ORGANISM_INTERNATIONAL = "ORO";

	public static readonly ORGANISME_ATTR_KEY_TIP = "attribute1";
	public static readonly ORGANISME_ATTR_KEY_DENUMIRE = "attribute2";

	public static readonly COMITETE_ATTR_KEY_ORGANISM = "attribute1";
	public static readonly COMITETE_ATTR_KEY_DENUMIRE = "attribute2";
	
	public static readonly INSTITUTII_ATTR_KEY_DENUMIRE_INSTITUTIE = "attribute2";
	public static readonly NOMENCLATOR_COMISII_GL_ATTRIBUTE_KEY_CATEGORIE = "attribute1";
	public static readonly NOMENCLATOR_CATEGORIE_COMISII_GL_ATTRIBUTE_KEY_CATEGORIE = "attribute1";
	public static readonly NOMENCLATOR_CATEGORIE_COMISII_GL_ATTRIBUTE_CATEGORIE_VALUE = "Comisie";
	
	public static readonly DETALII_NUMAR_DEPLASARI_BUGETATE_ORGANISME_KEY_DE_LA_DATA = "attribute2";
	
	public static readonly NOMENCLATOR_INSTITUTII_ATTRIBUTE_ABREVIERE_VALUE_ARB = "ARB";

	public static readonly LEVEL_FUNCTII_PERSOANE_ATTR_KEY_DENUMIRE = "attribute1";

	public static readonly ZILE_LIBERE_LEGALE_ATTR_KEY_LUNA = "attribute3";
	public static readonly ZILE_LIBERE_LEGALE_ATTR_KEY_ZI= "attribute2";

	// FUNCTII_PERSOANE_ATTR_KEY_XXX => XXX: nume atribut
	public static readonly FUNCTII_PERSOANE_ATTR_KEY_DENUMIRE = "attribute1";
	public static readonly FUNCTII_PERSOANE_ATTR_KEY_LEVEL = "attribute2";

	public static readonly REGISTRU_INTRARI_TIP_DOCUMENT_ATTR_KEY_DENUMIRE = "attribute2";	

	// IMPORTANTA_PROIECTE_ATTR_KEY_XXX => XXX: nume atribut
	public static readonly IMPORTANTA_PROIECTE_ATTR_KEY_GRAD = "attribute1";
	public static readonly IMPORTANTA_PROIECTE_ATTR_KEY_VALOARE= "attribute2";
	public static readonly IMPORTANTA_PROIECTE_ATTR_KEY_CULOARE = "attribute3";
	public static readonly IMPORTANTA_PROIECTE_ATTR_KEY_DESCRIERE = "attribute4";
}