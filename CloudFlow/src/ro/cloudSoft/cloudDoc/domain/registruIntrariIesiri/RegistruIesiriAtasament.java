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
@Table(name = "REGISTRU_IESIRI_ATASAMENTE")
public class RegistruIesiriAtasament {

	private Long id;
	private RegistruIesiri registruIesiri;
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
	@JoinColumn(name = "REGISTRU_IESIRI_ID")
	public RegistruIesiri getRegistruIesiri() {
		return registruIesiri;
	}

	public void setRegistruIesiri(RegistruIesiri registruIesiri) {
		this.registruIesiri = registruIesiri;
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
