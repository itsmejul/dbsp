package com.db.hibernate.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="dvdspec")
public class Dvdspec {

	@Id
	@Column(name="dvd_id")
	private int id;
	@Column(name="asin")
	private String asin;
	@Column(name="aspectratio")
	private String aspectratio;
	@Column(name="format")
	private String format;
	@Column(name="regioncode")
	private int regioncode;
	@Column(name="releasedate")
	private String releasedate;
	@Column(name="runningtime")
	private int runningtime;
	@Column(name="theatr_release")
	private String theatr_release;
	@Column(name="upc")
	private String upc;
	
	public Dvdspec() {
		
	}
}
