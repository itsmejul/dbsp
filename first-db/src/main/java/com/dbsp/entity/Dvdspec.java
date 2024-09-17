package com.dbsp.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name="dvdspec")
public class Dvdspec {

	@Id
	@Column(name="dvd_id")
	private Integer id;
	@Column(name="asin")
	private String asin;
	@Column(name="aspectratio")
	private String aspectratio;
	@Column(name="format")
	private String format;
	@Column(name="regioncode")
	private Integer regioncode;
	@Column(name="releasedate")
	private String releasedate;
	@Column(name="runningtime")
	private Integer runningtime;
	@Column(name="theatr_release")
	private String theatr_release;
	@Column(name="upc")
	private String upc;
	
	public Dvdspec() {
		
	}

	public Dvdspec(String asin, String aspectratio, String format, Integer regioncode, String releasedate, Integer runningtime,
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

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
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

	public Integer getRegioncode() {
		return regioncode;
	}

	public void setRegioncode(Integer regioncode) {
		this.regioncode = regioncode;
	}

	public String getReleasedate() {
		return releasedate;
	}

	public void setReleasedate(String releasedate) {
		this.releasedate = releasedate;
	}

	public Integer getRunningtime() {
		return runningtime;
	}

	public void setRunningtime(Integer runningtime) {
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
