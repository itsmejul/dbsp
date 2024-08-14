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
}
