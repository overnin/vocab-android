package com.example.vocab.model;

import java.sql.Date;

public class Translation {
	
	private long id;
	private String sourceLanguage;
	private String sourceContent;
	private String destinationLanguage;
	private String destinationContent;
	private Date createdAt;
	
	public long getId() {
		return id;
	}
	
	public void setId(long id) {
		this.id = id;
	}

	public String getSourceLanguage() {
		return sourceLanguage;
	}

	public void setSourceLanguage(String sourceLanguage) {
		this.sourceLanguage = sourceLanguage;
	}

	public String getSourceContent() {
		return sourceContent;
	}

	public void setSourceContent(String sourceContent) {
		this.sourceContent = sourceContent;
	}

	public String getDestinationLanguage() {
		return destinationLanguage;
	}

	public void setDestinationLanguage(String destinationLanguage) {
		this.destinationLanguage = destinationLanguage;
	}

	public String getDestinationContent() {
		return destinationContent;
	}

	public void setDestinationContent(String destinationContent) {
		this.destinationContent = destinationContent;
	}

	public Date getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Date date) {
		this.createdAt = date;
	}
	

}
