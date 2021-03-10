package ro.cloudSoft.cloudDoc.dto;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public class SampleDTO {

	private String name;
	private Integer age;
	private Date birthDate;
	private List<DetailDTO> details;
	private DetailDTO detail;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Integer getAge() {
		return age;
	}
	public void setAge(Integer age) {
		this.age = age;
	}	
	public Date getBirthDate() {
		return birthDate;
	}
	public void setBirthDate(Date birthDate) {
		this.birthDate = birthDate;
	}
	public List<DetailDTO> getDetails() {
		return details;
	}
	public void setDetails(List<DetailDTO> details) {
		this.details = details;
	}
	public DetailDTO getDetail() {
		return detail;
	}
	public void setDetail(DetailDTO detail) {
		this.detail = detail;
	}
	
	public static class DetailDTO {
		
		private String property1;
		private Boolean property2;
		private BigDecimal property3;
		
		public String getProperty1() {
			return property1;
		}
		public void setProperty1(String property1) {
			this.property1 = property1;
		}
		public Boolean getProperty2() {
			return property2;
		}
		public void setProperty2(Boolean property2) {
			this.property2 = property2;
		}
		public BigDecimal getProperty3() {
			return property3;
		}
		public void setProperty3(BigDecimal property3) {
			this.property3 = property3;
		}
		
		
	}
}
