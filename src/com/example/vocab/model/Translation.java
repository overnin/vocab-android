package com.example.vocab.model;

public class Translation {
	
	private long id;
	private String origin;
	private String translated;
	
	public long getId() {
		return id;
	}
	
	public void setId(long id) {
		this.id = id;
	}
	
	public String getOrigin() {
		return origin;
	}
	
	public void setOrigin(String origin) {
		this.origin = origin;
	}
	
	public String getTranslated() {
		return translated;
	}
	
	public void setTranslated(String translated) {
		this.translated = translated;
	}
}
