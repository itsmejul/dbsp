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
}
