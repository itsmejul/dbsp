package com.db.hibernate.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="actor")
public class Actor {

	@Id
	@Column(name="actor_name")
	private String name;
	@Id
	@Column(name="asin")
	private String asin;
	
	public Actor() {
		
	}

	public Actor(String name, String asin) {
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
