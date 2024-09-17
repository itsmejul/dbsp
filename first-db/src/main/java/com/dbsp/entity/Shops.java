package com.dbsp.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
//import javax.persistence.*;

@Entity
@Table(name = "shops")
public class Shops {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY) // Ensures Hibernate uses the auto-increment functionality
	@Column(name = "shop_id")
	private Integer id;
	@Column(name = "shop_name")
	private String name;
	@Column(name = "shop_street")
	private String street;
	@Column(name = "shop_zip")
	private Integer zip;// PLZ

	public Shops() {

	}

	// rechtsklick source generate constructor with fields
	public Shops(String name, String street, Integer zip) {
		this.name = name;
		this.street = street;
		this.zip = zip;
	}

	// rechtsklick source generate setters getters
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getStreet() {
		return street;
	}

	public void setStreet(String street) {
		this.street = street;
	}

	public Integer getZip() {
		return zip;
	}

	public void setZip(Integer zip) {
		this.zip = zip;
	}

	@Override
	public String toString() {
		return "Shops [id=" + id + ", name=" + name + ", street=" + street + ", zip=" + zip + "]";
	}

}
