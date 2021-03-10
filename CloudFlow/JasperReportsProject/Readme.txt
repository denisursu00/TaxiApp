
Parametri generali (care vin completati automat din aplicatie):
---------------------------------------------------------------
- nume_presedinte_arb: String 


Reguli:
----------------------------------------------------------------
1) Se creaza un folder separat pentru fiecare raport.

2) Orice template de raport trebuie sa aibe numele sufixat cu Report, ex: AbcXyzReport.jrxml.

3) Orice template de subraport trebuie sa aibe numele sufixat cu Subreport, ex: AbcXyzSubreport.jrxml.

4) Subrapoartele vor fi in acelasi folder cu raportul principal.

5) Numele de imagini, subrapoarte, fisiere styles etc, va fi folosit exact asa cum e si numele fisierelor (referirea in jrxml).

6) Pentru testare se creeaza un fisier in folderul cu raportul numit TestData.json, iar adapter-ul va 
fi in acelasi loc si se va numi TestDataAdapter.xml.


Impachetare zip pentru DocumentTypeTemplateReports
----------------------------------------------------------------
Se creaza un zip care contine in radacina urmatoarele:
- toate fisierele din CommonResources
- toate fisierele din folderul raportului, cu exceptia celor de Test (TestData.json, TestDataAdapter.xml, sau cele cu extensia .jasper)

Obs: La sabloanele tipului de document se incarca zip-ul (din interfata)