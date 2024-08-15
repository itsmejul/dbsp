package com.dbsp.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name="audiotext")
public class Audiotext {

	@Id
	@Column(name="asin")
	private String asin;
	@Id
	@Column(name="type")
	private String type;
	@Id
	@Column(name="language")
	private String language;
	@Id
	@Column(name="audioformat")
	private String audioformat;
	
	public Audiotext() {
		
	}

	public Audiotext(String asin, String type, String language, String audioformat) {
		this.asin = asin;
		this.type = type;
		this.language = language;
		this.audioformat = audioformat;
	}

	public String getAsin() {
		return asin;
	}

	public void setAsin(String asin) {
		this.asin = asin;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public String getAudioformat() {
		return audioformat;
	}

	public void setAudioformat(String audioformat) {
		this.audioformat = audioformat;
	}
	
}
