package com.db.hibernate.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="customer_buy_item")
public class CustomerBuyItem {

	@Id
	@Column(name="customer_id")
	private int id;
	@Column(name="asin")
	private String asin;
	@Column(name="price_id")
	private int priceId;
	@Column(name="time_of_buy")
	private String timeOfBuy;
	
	public CustomerBuyItem() {
		
	}
}
