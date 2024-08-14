package com.db.hibernate.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
//rechtsklick source --> organize imports

@Entity
@Table(name="shops")
public class Shops {

	@Id
	@Column(name="shop_id")
	private int id;
	@Column(name="shop_name")
	private String name;
	@Column(name="shop_street")
	private String street;
	@Column(name="shop_zip")
	private int zip;//PLZ
	
	public Shops() {
		
	}
	//rechtsklick source generate constructor with fields
	public Shops(String name, String street, int zip) {
		this.name = name;
		this.street = street;
		this.zip = zip;
	}
	
	//rechtsklick source generate setters getters
	public int getId() {
		return id;
	}
	public void setId(int id) {
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
	public int getZip() {
		return zip;
	}
	public void setZip(int zip) {
		this.zip = zip;
	}
	
	@Override
	public String toString() {
		return "Shops [id=" + id + ", name=" + name + ", street=" + street + ", zip=" + zip + "]";
	}
	
}
