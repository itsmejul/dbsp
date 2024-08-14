package com.db.hibernate.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

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
}
