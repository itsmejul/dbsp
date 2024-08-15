package com.dbsp.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name="lists")
public class Lists {

	@Id
	@Column(name="asin")
	private String asin;
	@Id
	@Column(name="listname")
	private String listname;
	
	public Lists() {
		
	}

	public Lists(String asin, String listname) {
		this.asin = asin;
		this.listname = listname;
	}

	public String getAsin() {
		return asin;
	}

	public void setAsin(String asin) {
		this.asin = asin;
	}

	public String getListname() {
		return listname;
	}

	public void setListname(String listname) {
		this.listname = listname;
	}
	
}
