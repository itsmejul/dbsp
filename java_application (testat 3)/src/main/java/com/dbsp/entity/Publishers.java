package com.dbsp.entity;

import com.dbsp.entity.keys.PublishersId;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;

@Entity
@Table(name = "publishers")
@IdClass(PublishersId.class)
public class Publishers {

	@Id
	@Column(name = "publisher_name")
	private String name;
	@Id
	@Column(name = "asin")
	private String asin;

	public Publishers() {

	}

	public Publishers(String name, String asin) {
		super();
		this.name = name;
		this.asin = asin;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAsin() {
		return asin;
	}

	public void setAsin(String asin) {
		this.asin = asin;
	}

}
