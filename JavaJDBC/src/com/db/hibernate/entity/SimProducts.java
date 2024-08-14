package com.db.hibernate.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="sim_products")
public class SimProducts {

	@Id
	@Column(name="asin_original")
	private String asin_original;
	@Id
	@Column(name="asin_similar")
	private String asin_similar;
	
	public SimProducts() {
		
	}
}
