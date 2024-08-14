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

	public Dvdspec(String asin, String aspectratio, String format, int regioncode, String releasedate, int runningtime,
			String theatr_release, String upc) {
		this.asin = asin;
		this.aspectratio = aspectratio;
		this.format = format;
		this.regioncode = regioncode;
		this.releasedate = releasedate;
		this.runningtime = runningtime;
		this.theatr_release = theatr_release;
		this.upc = upc;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getAsin() {
		return asin;
	}

	public void setAsin(String asin) {
		this.asin = asin;
	}

	public String getAspectratio() {
		return aspectratio;
	}

	public void setAspectratio(String aspectratio) {
		this.aspectratio = aspectratio;
	}

	public String getFormat() {
		return format;
	}

	public void setFormat(String format) {
		this.format = format;
	}

	public int getRegioncode() {
		return regioncode;
	}

	public void setRegioncode(int regioncode) {
		this.regioncode = regioncode;
	}

	public String getReleasedate() {
		return releasedate;
	}

	public void setReleasedate(String releasedate) {
		this.releasedate = releasedate;
	}

	public int getRunningtime() {
		return runningtime;
	}

	public void setRunningtime(int runningtime) {
		this.runningtime = runningtime;
	}

	public String getTheatr_release() {
		return theatr_release;
	}

	public void setTheatr_release(String theatr_release) {
		this.theatr_release = theatr_release;
	}

	public String getUpc() {
		return upc;
	}

	public void setUpc(String upc) {
		this.upc = upc;
	}
	
}
