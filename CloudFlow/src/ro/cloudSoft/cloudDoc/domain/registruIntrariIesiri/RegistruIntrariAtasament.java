package ro.cloudSoft.cloudDoc.domain.registruIntrariIesiri;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "REGISTRU_INTRARI_ATASAMENTE")
public class RegistruIntrariAtasament {
	private Long id;
	private RegistruIntrari registruIntrari;
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
	@JoinColumn(name = "REGISTRU_INTRARI_ID")
	public RegistruIntrari getRegistruIntrari() {
		return registruIntrari;
	}

	public void setRegistruIntrari(RegistruIntrari registruIntrari) {
		this.registruIntrari = registruIntrari;
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
