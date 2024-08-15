package com.dbsp.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name="customer")
public class Customer {

	@Id
	@Column(name="customer_id")
	private int id;
	@Column(name="username")
	private String username;
	@Column(name="iban")
	private String iban;
	@Column(name="lieferadresse")
	private String lieferadresse;
	
	public Customer() {
		
	}

	public Customer(String username, String iban, String lieferadresse) {
		this.username = username;
		this.iban = iban;
		this.lieferadresse = lieferadresse;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getIban() {
		return iban;
	}

	public void setIban(String iban) {
		this.iban = iban;
	}

	public String getLieferadresse() {
		return lieferadresse;
	}

	public void setLieferadresse(String lieferadresse) {
		this.lieferadresse = lieferadresse;
	}
	
}
