package com.dbsp.entity;

import com.dbsp.entity.keys.StudiosId;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;

@Entity
@Table(name = "studios")
@IdClass(StudiosId.class)
public class Studios {

	@Id
	@Column(name = "studio_name")
	private String name;
	@Id
	@Column(name = "asin")
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
