package com.example.xoriant.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;

import org.springframework.web.multipart.MultipartFile;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class JasonComparatorRequest {
	
	
	public JasonComparatorRequest() {
		super();
	}
	
	public JasonComparatorRequest(String fileContent) {
		super();
		this.fileContent = fileContent;
	}


	@Id
	@GeneratedValue
	private Integer baseId;
	
	@Lob
	private String fileContent;

	public Integer getBaseId() {
		return baseId;
	}

	public void setBaseId(Integer baseId) {
		this.baseId = baseId;
	}


	public String getFileContent() {
		return fileContent;
	}

	public void setFileContent(String fileContent) {
		this.fileContent = fileContent;
	}
	
	

}
