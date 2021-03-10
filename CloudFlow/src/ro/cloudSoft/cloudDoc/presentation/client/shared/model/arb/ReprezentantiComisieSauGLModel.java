package ro.cloudSoft.cloudDoc.presentation.client.shared.model.arb;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ReprezentantiComisieSauGLModel {

	private Long id;
	
	private Long comisieSauGLId;
	private Long presedinteId;
	private Date dataInceputMandatPresedinte;
	private Date dataExpirareMandatPresedinte;
	
	private Long vicepresedinte1Id;
	private Date dataInceputMandatVicepresedinte1;
	private Date dataExpirareMandatVicepresedinte1;
	
	private Long vicepresedinte2Id;
	private Date dataInceputMandatVicepresedinte2;
	private Date dataExpirareMandatVicepresedinte2;
	
	private Long vicepresedinte3Id;
	private Date dataInceputMandatVicepresedinte3;
	private Date dataExpirareMandatVicepresedinte3;
	
	private Long responsabilARBId;
	
	private Long membruCDCoordonatorId;
	private Date dataInceputMandatMembruCDCoordonator;
	private Date dataExpirareMandatMembruCDCoordonator;
	
	private List<MembruReprezentantiComisieSauGLModel> membri;
	
	public ReprezentantiComisieSauGLModel() {
		this.membri = new ArrayList<MembruReprezentantiComisieSauGLModel>();
	}
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getComisieSauGLId() {
		return comisieSauGLId;
	}

	public void setComisieSauGLId(Long comisieSauGLId) {
		this.comisieSauGLId = comisieSauGLId;
	}

	public Long getPresedinteId() {
		return presedinteId;
	}

	public void setPresedinteId(Long presedinteId) {
		this.presedinteId = presedinteId;
	}
	
	public Date getDataInceputMandatPresedinte() {
		return dataInceputMandatPresedinte;
	}
	
	public void setDataInceputMandatPresedinte(Date dataInceputMandatPresedinte) {
		this.dataInceputMandatPresedinte = dataInceputMandatPresedinte;
	}
	
	public Date getDataExpirareMandatPresedinte() {
		return dataExpirareMandatPresedinte;
	}

	public void setDataExpirareMandatPresedinte(Date dataExpirareMandatPresedinte) {
		this.dataExpirareMandatPresedinte = dataExpirareMandatPresedinte;
	}

	public Long getVicepresedinte1Id() {
		return vicepresedinte1Id;
	}

	public void setVicepresedinte1Id(Long vicepresedinte1Id) {
		this.vicepresedinte1Id = vicepresedinte1Id;
	}
	
	public Date getDataInceputMandatVicepresedinte1() {
		return dataInceputMandatVicepresedinte1;
	}
	
	public void setDataInceputMandatVicepresedinte1(Date dataInceputMandatVicepresedinte1) {
		this.dataInceputMandatVicepresedinte1 = dataInceputMandatVicepresedinte1;
	}
	
	public Date getDataExpirareMandatVicepresedinte1() {
		return dataExpirareMandatVicepresedinte1;
	}

	public void setDataExpirareMandatVicepresedinte1(Date dataExpirareMandatVicepresedinte1) {
		this.dataExpirareMandatVicepresedinte1 = dataExpirareMandatVicepresedinte1;
	}

	public Long getVicepresedinte2Id() {
		return vicepresedinte2Id;
	}

	public void setVicepresedinte2Id(Long vicepresedinte2Id) {
		this.vicepresedinte2Id = vicepresedinte2Id;
	}

	public void setDataInceputMandatVicepresedinte2(Date dataInceputMandatVicepresedinte2) {
		this.dataInceputMandatVicepresedinte2 = dataInceputMandatVicepresedinte2;
	}
	
	public Date getDataInceputMandatVicepresedinte2() {
		return dataInceputMandatVicepresedinte2;
	}
	
	public Date getDataExpirareMandatVicepresedinte2() {
		return dataExpirareMandatVicepresedinte2;
	}

	public void setDataExpirareMandatVicepresedinte2(Date dataExpirareMandatVicepresedinte2) {
		this.dataExpirareMandatVicepresedinte2 = dataExpirareMandatVicepresedinte2;
	}

	public Long getVicepresedinte3Id() {
		return vicepresedinte3Id;
	}

	public void setVicepresedinte3Id(Long vicepresedinte3Id) {
		this.vicepresedinte3Id = vicepresedinte3Id;
	}

	public Date getDataInceputMandatVicepresedinte3() {
		return dataInceputMandatVicepresedinte3;
	}
	
	public void setDataInceputMandatVicepresedinte3(Date dataInceputMandatVicepresedinte3) {
		this.dataInceputMandatVicepresedinte3 = dataInceputMandatVicepresedinte3;
	}
	
	public Date getDataExpirareMandatVicepresedinte3() {
		return dataExpirareMandatVicepresedinte3;
	}

	public void setDataExpirareMandatVicepresedinte3(Date dataExpirareMandatVicepresedinte3) {
		this.dataExpirareMandatVicepresedinte3 = dataExpirareMandatVicepresedinte3;
	}

	public Long getResponsabilARBId() {
		return responsabilARBId;
	}

	public void setResponsabilARBId(Long responsabilARBId) {
		this.responsabilARBId = responsabilARBId;
	}

	public Long getMembruCDCoordonatorId() {
		return membruCDCoordonatorId;
	}

	public void setMembruCDCoordonatorId(Long membruCDCoordonatorId) {
		this.membruCDCoordonatorId = membruCDCoordonatorId;
	}

	public Date getDataInceputMandatMembruCDCoordonator() {
		return dataInceputMandatMembruCDCoordonator;
	}
	
	public void setDataInceputMandatMembruCDCoordonator(Date dataInceputMandatMembruCDCoordonator) {
		this.dataInceputMandatMembruCDCoordonator = dataInceputMandatMembruCDCoordonator;
	}
	
	public Date getDataExpirareMandatMembruCDCoordonator() {
		return dataExpirareMandatMembruCDCoordonator;
	}

	public void setDataExpirareMandatMembruCDCoordonator(Date dataExpirareMandatMembruCDCoordonator) {
		this.dataExpirareMandatMembruCDCoordonator = dataExpirareMandatMembruCDCoordonator;
	}

	public List<MembruReprezentantiComisieSauGLModel> getMembri() {
		return membri;
	}

	public void setMembri(List<MembruReprezentantiComisieSauGLModel> membri) {
		this.membri = membri;
	}
}
