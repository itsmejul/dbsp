package com.db.hibernate.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

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
}
