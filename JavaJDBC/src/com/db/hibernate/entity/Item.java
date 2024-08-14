package com.db.hibernate.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="item")
public class Item {

	@Id
	@Column(name="asin")
	private String asin;
	@Column(name="title")
	private String title;
	@Column(name="pgroup")
	private String pgroup;
	@Column(name="salesrank")
	private int salesrank;
	@Column(name="avg_review_score")
	private double avg_review_score;
	@Column(name="ean")
	private String ean;
	@Column(name="picture")
	private String picture;
	@Column(name="detailpage")
	private String detailpage;
	
	public Item() {
		
	}

	public Item(String asin, String title, String pgroup, int salesrank, double avg_review_score, String ean,
			String picture, String detailpage) {
		this.asin = asin;
		this.title = title;
		this.pgroup = pgroup;
		this.salesrank = salesrank;
		this.avg_review_score = avg_review_score;
		this.ean = ean;
		this.picture = picture;
		this.detailpage = detailpage;
	}

	public String getAsin() {
		return asin;
	}

	public void setAsin(String asin) {
		this.asin = asin;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getPgroup() {
		return pgroup;
	}

	public void setPgroup(String pgroup) {
		this.pgroup = pgroup;
	}

	public int getSalesrank() {
		return salesrank;
	}

	public void setSalesrank(int salesrank) {
		this.salesrank = salesrank;
	}

	public double getAvg_review_score() {
		return avg_review_score;
	}

	public void setAvg_review_score(double avg_review_score) {
		this.avg_review_score = avg_review_score;
	}

	public String getEan() {
		return ean;
	}

	public void setEan(String ean) {
		this.ean = ean;
	}

	public String getPicture() {
		return picture;
	}

	public void setPicture(String picture) {
		this.picture = picture;
	}

	public String getDetailpage() {
		return detailpage;
	}

	public void setDetailpage(String detailpage) {
		this.detailpage = detailpage;
	}
	
}
