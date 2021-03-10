package ro.cloudSoft.cloudDoc.domain.registruDocumenteJustificativePlati;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import ro.cloudSoft.cloudDoc.domain.registruIntrariIesiri.RegistruIesiri;

@Entity
@Table(name = "REG_DOC_JUST_PLATI_ATASAMENTE")
public class RegistruDocumenteJustificativePlatiAtasament {

	private Long id;
	private RegistruDocumenteJustificativePlati registruDocumenteJustificativePlati;
	private String fileName;
	private byte[] filecontent;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "REG_DOC_JUST_PLATI_ID")
	public RegistruDocumenteJustificativePlati getRegistruDocumenteJustificativePlati() {
		return registruDocumenteJustificativePlati;
	}

	public void setRegistruDocumenteJustificativePlati(
			RegistruDocumenteJustificativePlati registruDocumenteJustificativePlati) {
		this.registruDocumenteJustificativePlati = registruDocumenteJustificativePlati;
	}

	@Column(name = "FILE_NAME")
	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	@Column(name = "FILE_CONTENT")
	public byte[] getFilecontent() {
		return filecontent;
	}

	public void setFilecontent(byte[] filecontent) {
		this.filecontent = filecontent;
	}

}
