package com.db.hibernate.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="price")
public class Price {

	@Id
	@Column(name="price_id")
	private int id;
	@Column(name="asin")
	private String asin;
	@Column(name="price_state")
	private String price_state;
	@Column(name="price_mult")
	private double price_mult;
	@Column(name="price_currency")
	private String price_currency;
	@Column(name="price_value")
	private int price_value;
	@Column(name="price_shop_id")
	private int price_shop_id;
	
	public Price() {
		
	}

	public Price(String asin, String price_state, double price_mult, String price_currency, int price_value,
			int price_shop_id) {
		this.asin = asin;
		this.price_state = price_state;
		this.price_mult = price_mult;
		this.price_currency = price_currency;
		this.price_value = price_value;
		this.price_shop_id = price_shop_id;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getAsin() {
		return asin;
	}

	public void setAsin(String asin) {
		this.asin = asin;
	}

	public String getPrice_state() {
		return price_state;
	}

	public void setPrice_state(String price_state) {
		this.price_state = price_state;
	}

	public double getPrice_mult() {
		return price_mult;
	}

	public void setPrice_mult(double price_mult) {
		this.price_mult = price_mult;
	}

	public String getPrice_currency() {
		return price_currency;
	}

	public void setPrice_currency(String price_currency) {
		this.price_currency = price_currency;
	}

	public int getPrice_value() {
		return price_value;
	}

	public void setPrice_value(int price_value) {
		this.price_value = price_value;
	}

	public int getPrice_shop_id() {
		return price_shop_id;
	}

	public void setPrice_shop_id(int price_shop_id) {
		this.price_shop_id = price_shop_id;
	}
	
}
