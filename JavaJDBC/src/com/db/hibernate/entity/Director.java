package com.db.hibernate.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="director")
public class Director {

	@Id
	@Column(name="director_name")
	private String name;
	@Id
	@Column(name="asin")
	private String asin;
	
	public Director() {
		
	}

	public Director(String name, String asin) {
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
