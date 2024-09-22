package com.dbsp.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name="price")
public class Price {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="price_id")
	private Integer id;
	@Column(name="asin")
	private String asin;
	@Column(name="price_state")
	private String price_state;
	@Column(name="price_mult")
	private Double price_mult;
	@Column(name="price_currency")
	private String price_currency;
	@Column(name="price_value")
	private Integer price_value;
	@Column(name="price_shop_id")
	private Integer price_shop_id;
	
	public Price() {
		
	}

	public Price(String asin, String price_state, Double price_mult, String price_currency, Integer price_value,
			Integer price_shop_id) {
		this.asin = asin;
		this.price_state = price_state;
		this.price_mult = price_mult;
		this.price_currency = price_currency;
		this.price_value = price_value;
		this.price_shop_id = price_shop_id;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
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

	public Double getPrice_mult() {
		return price_mult;
	}

	public void setPrice_mult(Double price_mult) {
		this.price_mult = price_mult;
	}

	public String getPrice_currency() {
		return price_currency;
	}

	public void setPrice_currency(String price_currency) {
		this.price_currency = price_currency;
	}

	public Integer getPrice_value() {
		return price_value;
	}

	public void setPrice_value(Integer price_value) {
		this.price_value = price_value;
	}

	public Integer getPrice_shop_id() {
		return price_shop_id;
	}

	public void setPrice_shop_id(Integer price_shop_id) {
		this.price_shop_id = price_shop_id;
	}
	
}
