package com.example.vocab.model;

import java.sql.Date;

public class Tryout {
	
	private long id;
	private long translationId;
	private Date createdDate;
	private String fromLanguage;
	private String answer;
	private String result;
	
	public Date getCreatedDate() {
		return createdDate;
	}
	public void setCreatedDate(Date date) {
		this.createdDate = date;
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
	public String getAnswer() {
		return answer;
	}
	public void setAnswer(String answer) {
		this.answer = answer;
	}
	public String getFromLanguage() {
		return fromLanguage;
	}
	public void setFromLanguage(String fromLanguage) {
		this.fromLanguage = fromLanguage;
	}
	

}
