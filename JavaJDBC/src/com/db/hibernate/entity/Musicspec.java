package com.db.hibernate.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="musicspec")
public class Musicspec {

	@Id
	@Column(name="cd_id")
	private int id;
	@Column(name="asin")
	private String asin;
	@Column(name="binding")
	private String binding;
	@Column(name="format")
	private String format;
	@Column(name="num_discs")
	private int num_discs;
	@Column(name="releasedate")
	private String releasedate;
	@Column(name="upc")
	private String upc;
	
	public Musicspec() {
		
	}
}
