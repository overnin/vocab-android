package com.example.vocab.model;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Translation {
	
	private long id;
	private String sourceLanguage;
	private String sourceContent;
	private String destinationLanguage;
	private String destinationContent;
	private Date createdAt;
	private Date updatedAt;
	
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

	public void setCreatedAt(String date) {
		try {
			this.createdAt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(date);
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}

	public Date getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(String date) {
		try {
			this.updatedAt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(date);
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}
	

}
