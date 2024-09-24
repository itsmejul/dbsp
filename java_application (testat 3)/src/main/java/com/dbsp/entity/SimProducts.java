package com.dbsp.entity;

import com.dbsp.entity.keys.SimProductsId;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;

@Entity
@Table(name = "sim_products")
@IdClass(SimProductsId.class)
public class SimProducts {

	@Id
	@Column(name = "asin_original")
	private String asin_original;
	@Id
	@Column(name = "asin_similar")
	private String asin_similar;

	public SimProducts() {

	}

	public SimProducts(String asin_original, String asin_similar) {
		super();
		this.asin_original = asin_original;
		this.asin_similar = asin_similar;
	}

	public String getAsin_original() {
		return asin_original;
	}

	public void setAsin_original(String asin_original) {
		this.asin_original = asin_original;
	}

	public String getAsin_similar() {
		return asin_similar;
	}

	public void setAsin_similar(String asin_similar) {
		this.asin_similar = asin_similar;
	}

}
