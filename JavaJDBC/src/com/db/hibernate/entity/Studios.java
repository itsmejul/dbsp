package com.db.hibernate.entity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="studios")
public class Studios {

	@Id
	@Column(name="studio_name")
	private String name;
	@Id
	@Column(name="asin")
	private String asin;
	
	public Studios() {
		
	}

	public Studios(String name, String asin) {
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
