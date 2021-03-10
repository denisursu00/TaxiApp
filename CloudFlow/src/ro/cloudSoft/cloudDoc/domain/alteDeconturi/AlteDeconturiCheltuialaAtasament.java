package ro.cloudSoft.cloudDoc.domain.alteDeconturi;

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
@Table(name = "ALTE_DECONTURI_CHELTUIELI_ATASAMENTE")
public class AlteDeconturiCheltuialaAtasament {

	private Long id;
	private AlteDeconturiCheltuiala alteDeconturiCheltuiala;
	private String fileName;
	private byte[] fileContent;
	

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ALTE_DECONTURI_CHELTUIALA_ID", nullable = false)
	public AlteDeconturiCheltuiala getAlteDeconturiCheltuiala() {
		return alteDeconturiCheltuiala;
	}

	public void setAlteDeconturiCheltuiala(AlteDeconturiCheltuiala alteDeconturiCheltuiala) {
		this.alteDeconturiCheltuiala = alteDeconturiCheltuiala;
	}
	
	@Column(name = "FILE_NAME")
	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	@Column(name = "FILE_CONTENT")
	public byte[] getFileContent() {
		return fileContent;
	}

	public void setFileContent(byte[] fileContent) {
		this.fileContent = fileContent;
	}
	
}
