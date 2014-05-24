package com.example.vocab.model;

import java.sql.Date;

public class Result {
	
	private long id;
	private Date date;
	private long translationId;
	private String result;
	
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public long getTranslationId() {
		return translationId;
	}
	public void setTranslationId(long translationId) {
		this.translationId = translationId;
	}
	public String getResult() {
		return result;
	}
	public void setResult(String result) {
		this.result = result;
	}
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	

}
