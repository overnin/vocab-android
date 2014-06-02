package com.example.vocab.model;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Tryout {
	
	private long id;
	private Date createdAt;
	private String fromLanguage;

	private String answer;
	private boolean answered;
	private Date answeredAt;
	private boolean answerCorrect;

	private boolean skiped;
	private Date skipedAt;
	
	private long translationId;
	private Translation translation;
	
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
	public long getTranslationId() {
		return translationId;
	}
	public void setTranslationId(long translationId) {
		this.translationId = translationId;
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
	public boolean isSkiped() {
		return skiped;
	}
	public void setSkiped(boolean skiped) {
		this.skiped = skiped;
	}
	public Date getAnsweredAt() {
		return answeredAt;
	}
	public void setAnsweredAt(String answeredAt) {
		try {
			this.answeredAt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(answeredAt);
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}
	public boolean isAnswered() {
		return answered;
	}
	public void setAnswered(boolean answered) {
		this.answered = answered;
	}
	public boolean isAnswerCorrect() {
		return answerCorrect;
	}
	public void setAnswerCorrect(boolean answerCorrect) {
		this.answerCorrect = answerCorrect;
	}
	public Date getSkipedAt() {
		return skipedAt;
	}
	public void setSkipedAt(String skipedAt) {
		try {
			this.skipedAt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(skipedAt);
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}
	public Translation getTranslation() {
		return translation;
	}
	public void setTranslation(Translation translation) {
		this.translation = translation;
	}
	
	public boolean isSubmitCorrect(String submit){
		String correctAnswer = getCorrectAnswer();
		if (submit.equalsIgnoreCase(correctAnswer)) {
			return true;
		}
		return false;
	}
	
	public String getCorrectAnswer() {
		Translation translation = getTranslation();
		if (fromLanguage.equalsIgnoreCase(translation.getSourceLanguage())) {
			return translation.getDestinationContent();
		}
		return translation.getSourceContent();
	}
	
	public String getTryoutContent() { 
		if (getFromLanguage().equalsIgnoreCase(translation.getSourceLanguage())) {
			return translation.getSourceContent();
		} 
		return translation.getDestinationContent();
	}
	
	public String getTryoutLanguage() {
		if (getFromLanguage().equalsIgnoreCase(translation.getSourceLanguage())) {
			return translation.getDestinationLanguage();
		} 	
		return translation.getSourceLanguage();
	}
	
	
}
